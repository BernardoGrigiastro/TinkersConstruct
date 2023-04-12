package slimeknights.tconstruct.world;

import net.minecraft.item.ItemStack;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.blocks.WorldBlocks;
import slimeknights.tconstruct.common.ServerProxy;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.common.registry.BaseRegistryAdapter;
import slimeknights.tconstruct.library.TinkerPulseIds;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.world.worldgen.NetherSlimeIslandPiece;
import slimeknights.tconstruct.world.worldgen.NetherSlimeIslandStructure;
import slimeknights.tconstruct.world.worldgen.SlimeIslandPiece;
import slimeknights.tconstruct.world.worldgen.SlimeIslandStructure;

@Pulse(id = TinkerPulseIds.TINKER_WORLD_PULSE_ID, description = "Everything that's found in the world and worldgen")
@ObjectHolder(TConstruct.modID)
public class TinkerWorld extends TinkerPulse {
    
    public static final StructureFeature<DefaultFeatureConfig> SLIME_ISLAND = injected();
    public static final StructureFeature<DefaultFeatureConfig> NETHER_SLIME_ISLAND = injected();
    static final Logger log = Util.getLogger(TinkerPulseIds.TINKER_WORLD_PULSE_ID);
    public static ServerProxy proxy = DistExecutor.runForDist(() -> WorldClientProxy::new, () -> ServerProxy::new);
    // todo: create own planttype
    public static PlantType slimePlantType = PlantType.Nether;
    public static StructurePieceType SLIME_ISLAND_PIECE;
    public static StructurePieceType NETHER_SLIME_ISLAND_PIECE;
    
    public TinkerWorld() {
        proxy.construct();
        //slimePlantType = PlantType.create("slime"); TODO: RE-ENABLE THIS AFTER FORGE FIXES IT
    }
    
    public static void applyFeatures() {
        ConfiguredFeature<?> SLIME_ISLAND_FEATURE = Biome.createDecoratedFeature(SLIME_ISLAND, FeatureConfig.DEFAULT, Decorator.field_14250, DecoratorConfig.DEFAULT);
        FlatChunkGeneratorConfig.FEATURE_TO_GENERATION_STEP.put(SLIME_ISLAND_FEATURE, GenerationStep.Feature.field_13173);
        FlatChunkGeneratorConfig.STRUCTURE_TO_FEATURES.put("tconstruct:slime_island", new ConfiguredFeature[]{SLIME_ISLAND_FEATURE});
        FlatChunkGeneratorConfig.FEATURE_TO_FEATURE_CONFIG.put(SLIME_ISLAND_FEATURE, FeatureConfig.DEFAULT);
        
        ConfiguredFeature<?> NETHER_SLIME_ISLAND_FEATURE = Biome.createDecoratedFeature(NETHER_SLIME_ISLAND, FeatureConfig.DEFAULT, Decorator.field_14250, DecoratorConfig.DEFAULT);
        FlatChunkGeneratorConfig.FEATURE_TO_GENERATION_STEP.put(NETHER_SLIME_ISLAND_FEATURE, GenerationStep.Feature.field_13177);
        FlatChunkGeneratorConfig.STRUCTURE_TO_FEATURES.put("tconstruct:nether_slime_island", new ConfiguredFeature[]{NETHER_SLIME_ISLAND_FEATURE});
        FlatChunkGeneratorConfig.FEATURE_TO_FEATURE_CONFIG.put(NETHER_SLIME_ISLAND_FEATURE, FeatureConfig.DEFAULT);
        
        if (Config.SERVER.generateSlimeIslands.get()) {
            for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
                if (biome.getCategory() != Biome.Category.field_9366 && biome.getCategory() != Biome.Category.THEEND) {
                    addStructure(biome, GenerationStep.Feature.field_13173, SLIME_ISLAND);
                } else if (biome.getCategory() == Biome.Category.field_9366) {
                    addStructure(biome, GenerationStep.Feature.field_13177, NETHER_SLIME_ISLAND);
                    
                    if (Config.SERVER.generateCobalt.get()) {
                        addCobaltOre(biome);
                    }
                    
                    if (Config.SERVER.generateArdite.get()) {
                        addArditeOre(biome);
                    }
                }
            }
        }
    }
    
    private static void addStructure(Biome biome, GenerationStep.Feature stage, StructureFeature structure) {
        biome.addFeature(stage, Biome.createDecoratedFeature(structure, FeatureConfig.DEFAULT, Decorator.field_14250, DecoratorConfig.DEFAULT));
        biome.addStructure(structure, FeatureConfig.DEFAULT);
    }
    
    private static void addCobaltOre(Biome biome) {
        int veinCount = Config.SERVER.veinCountCobalt.get() / 2;
        biome.addFeature(GenerationStep.Feature.field_13177, Biome.createDecoratedFeature(Feature.field_13517, new OreFeatureConfig(OreFeatureConfig.Target.field_13727, WorldBlocks.cobalt_ore.getDefaultState(), 5), Decorator.field_14241, new RangeDecoratorConfig(veinCount, 32, 0, 64)));
        biome.addFeature(GenerationStep.Feature.field_13177, Biome.createDecoratedFeature(Feature.field_13517, new OreFeatureConfig(OreFeatureConfig.Target.field_13727, WorldBlocks.cobalt_ore.getDefaultState(), 5), Decorator.field_14241, new RangeDecoratorConfig(veinCount, 0, 0, 128)));
    }
    
    private static void addArditeOre(Biome biome) {
        int veinCount = Config.SERVER.veinCountArdite.get() / 2;
        biome.addFeature(GenerationStep.Feature.field_13177, Biome.createDecoratedFeature(Feature.field_13517, new OreFeatureConfig(OreFeatureConfig.Target.field_13727, WorldBlocks.ardite_ore.getDefaultState(), 5), Decorator.field_14241, new RangeDecoratorConfig(veinCount, 32, 0, 64)));
        biome.addFeature(GenerationStep.Feature.field_13177, Biome.createDecoratedFeature(Feature.field_13517, new OreFeatureConfig(OreFeatureConfig.Target.field_13727, WorldBlocks.ardite_ore.getDefaultState(), 5), Decorator.field_14241, new RangeDecoratorConfig(veinCount, 0, 0, 128)));
    }
    
    @SubscribeEvent
    public void onFeaturesRegistry(RegistryEvent.Register<Feature<?>> event) {
        BaseRegistryAdapter<Feature<?>> registry = new BaseRegistryAdapter<>(event.getRegistry());
        
        SLIME_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, registry.getResource("slime_island_piece"), SlimeIslandPiece::new);
        registry.register(new SlimeIslandStructure(DefaultFeatureConfig::deserialize), "slime_island");
        
        NETHER_SLIME_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, registry.getResource("nether_slime_island_piece"), NetherSlimeIslandPiece::new);
        registry.register(new NetherSlimeIslandStructure(DefaultFeatureConfig::deserialize), "nether_slime_island");
    }
    
    @SubscribeEvent
    public void preInit(final FMLCommonSetupEvent event) {
        proxy.preInit();
        
        applyFeatures();
    }
    
    @SubscribeEvent
    public void init(final InterModEnqueueEvent event) {
        proxy.init();
    }
    
    @SubscribeEvent
    public void postInit(final InterModProcessEvent event) {
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
        proxy.postInit();
        TinkerRegistry.tabWorld.setDisplayIcon(new ItemStack(WorldBlocks.blue_slime_sapling));
    }
    
}
