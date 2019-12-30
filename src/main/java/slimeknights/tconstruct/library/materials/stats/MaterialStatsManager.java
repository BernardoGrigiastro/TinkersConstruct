package slimeknights.tconstruct.library.materials.stats;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.exception.TinkerAPIMaterialException;
import slimeknights.tconstruct.library.exception.TinkerJSONException;
import slimeknights.tconstruct.library.materials.MaterialId;
import slimeknights.tconstruct.library.materials.json.MaterialStatJsonWrapper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loads the different material stats from the datapacks.
 * The file locations do not matter per se, the file must specify which material it contains stats for.
 * Each file contains stats for exactly one one material.
 * The stats must be registered with TiC before loading or it'll fail.
 * <p>
 * The reason for the material id inside the file is so that multiple mods can add different stats to the same material.
 * If two different sources add the same stats to the same material the first one encountered will be used, and the second one will be skipped.
 * (e.g. having a 'Laser' stat type, and there are 2 mods who add Laser stat types to the iron material)
 * <p>
 * The location inside datapacks is "materials/stats".
 * So if your mods name is "foobar", the location for your mads material stats is "data/foobar/materials/stats".
 */
public class MaterialStatsManager extends JsonDataLoader {
    
    @VisibleForTesting protected static final String FOLDER = "materials/stats";
    @VisibleForTesting
    protected static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(Identifier.class, new Identifier.Serializer()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * This map represents the known stats of the manager. Only known materials can be loaded.
     * Usually they're registered by the registry, when a new material stats type is registered.
     * It is not cleared on reload, since it does not represend loaded data. Think of it as a GSON type adapter.
     */
    private Map<MaterialStatsId, Class<? extends BaseMaterialStats>> materialStatClasses = new HashMap<>();
    
    private Map<MaterialId, Map<MaterialStatsId, BaseMaterialStats>> materialToStatsPerType = ImmutableMap.of();
    
    public MaterialStatsManager() {
        super(GSON, FOLDER);
    }
    
    public void registerMaterialStat(MaterialStatsId materialStatType, Class<? extends BaseMaterialStats> statsClass) {
        if (materialStatClasses.containsKey(materialStatType)) {
            throw TinkerAPIMaterialException.materialStatsTypeRegisteredTwice(materialStatType);
        }
        materialStatClasses.put(materialStatType, statsClass);
    }
    
    public <T extends BaseMaterialStats> Optional<T> getStats(MaterialId materialId, MaterialStatsId statId) {
        Map<MaterialStatsId, BaseMaterialStats> materialStats = materialToStatsPerType.getOrDefault(materialId, ImmutableMap.of());
        IMaterialStats stats = materialStats.get(statId);
        // class will always match, since it's only filled by deserialization, which only puts it in if it's the registered type
        //noinspection unchecked
        return Optional.ofNullable((T) stats);
    }
    
    @Override
    protected void apply(Map<Identifier, JsonObject> splashList, ResourceManager resourceManagerIn, Profiler profilerIn) {
        // Combine all loaded material files into one map, removing possible conflicting stats and printing a warning about them
        materialToStatsPerType = splashList.entrySet().stream().map(entry -> loadMaterialStats(entry.getKey(), entry.getValue())).filter(Objects::nonNull).collect(Collectors.toMap(statsFileContent -> statsFileContent.materialId, statsFileContent -> transformAndCombineStatsForMaterial(statsFileContent.stats, Collections.emptyList()), (map1, map2) -> transformAndCombineStatsForMaterial(map1.values(), map2.values())));
    }
    
    private Map<MaterialStatsId, BaseMaterialStats> transformAndCombineStatsForMaterial(Collection<BaseMaterialStats> statsList1, Collection<BaseMaterialStats> statsList2) {
        return Stream.concat(statsList1.stream(), statsList2.stream()).collect(Collectors.toMap(BaseMaterialStats::getIdentifier, baseMaterialStats -> baseMaterialStats, (baseMaterialStats, baseMaterialStats2) -> {
            LOGGER.error("Duplicate stats {} for a material, ignoring additional definitions. " + "Some mod is probably trying to add duplicate stats to another mods material.", baseMaterialStats2.getIdentifier());
            return baseMaterialStats;
        }));
    }
    
    @Nullable
    private StatsFileContent loadMaterialStats(Identifier file, JsonObject jsonObject) {
        try {
            MaterialStatJsonWrapper materialStatJsonWrapper = GSON.fromJson(jsonObject, MaterialStatJsonWrapper.class);
            MaterialId materialId = materialStatJsonWrapper.getMaterialId();
            if (materialId == null) {
                throw TinkerJSONException.materialStatsJsonWithoutMaterial();
            }
            JsonArray statsJsonArray = jsonObject.getAsJsonArray("stats");
            List<BaseMaterialStats> stats = new ArrayList<>();
            
            if (statsJsonArray != null) {
                for (JsonElement statJson : statsJsonArray) {
                    try {
                        stats.add(deserializeMaterialStat(materialId, statJson));
                    } catch (Exception e) {
                        LOGGER.error("Could not deserialize material stats from file {}. JSON: {}", file, statJson, e);
                    }
                }
            }
            
            return new StatsFileContent(new MaterialId(materialId), stats);
        } catch (Exception e) {
            LOGGER.error("Could not deserialize material stats file {}. JSON: {}", file, jsonObject, e);
            return null;
        }
    }
    
    private BaseMaterialStats deserializeMaterialStat(Identifier materialId, JsonElement statsJson) {
        MaterialStatJsonWrapper.BaseMaterialStatsJson baseMaterialStatsJson = GSON.fromJson(statsJson, MaterialStatJsonWrapper.BaseMaterialStatsJson.class);
        Identifier statsId = baseMaterialStatsJson.getId();
        if (statsId == null) {
            throw TinkerJSONException.materialStatsJsonWithoutId(materialId);
        }
        Class<? extends BaseMaterialStats> materialStatClass = materialStatClasses.get(new MaterialStatsId(statsId));
        if (materialStatClass == null) {
            throw TinkerAPIMaterialException.materialNotRegistered(statsId);
        }
        return GSON.fromJson(statsJson, materialStatClass);
    }
    
    private static class StatsFileContent {
        private final MaterialId materialId;
        private final List<BaseMaterialStats> stats;
        
        StatsFileContent(MaterialId materialId, List<BaseMaterialStats> stats) {
            this.materialId = materialId;
            this.stats = stats;
        }
    }
}
