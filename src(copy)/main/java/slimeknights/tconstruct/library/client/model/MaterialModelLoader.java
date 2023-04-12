package slimeknights.tconstruct.library.client.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.CustomTextureCreator;
import slimeknights.tconstruct.library.client.model.format.Offset;
import slimeknights.tconstruct.library.tools.IToolPart;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MaterialModelLoader implements ICustomModelLoader {
    
    // used to create only actually needed textures in the texturegenerator instead of ALL materials for all parts
    private static final Map<Identifier, Set<IToolPart>> partTextureRestriction = Maps.newHashMap();
    public static String EXTENSION = ".tmat";
    
    public static void addPartMapping(Identifier resourceLocation, IToolPart toolPart) {
        if (!partTextureRestriction.containsKey(resourceLocation)) {
            partTextureRestriction.put(resourceLocation, Sets.newHashSet());
        }
        
        partTextureRestriction.get(resourceLocation).add(toolPart);
    }
    
    public static Optional<Identifier> getToolPartModelLocation(IToolPart toolPart) {
        return partTextureRestriction.entrySet().stream().filter(entry -> entry.getValue().contains(toolPart)).findFirst().map(Map.Entry::getKey);
    }
    
    public static Identifier getReducedPath(Identifier location) {
        String path = location.getResourcePath();
        path = path.substring("models/item/".length());
        return new Identifier(location.getResourceDomain(), path);
    }
    
    @Override
    public boolean accepts(Identifier modelLocation) {
        return modelLocation.getResourcePath().endsWith(EXTENSION); // tinkermaterialmodel extension. Foo.tmat.json
    }
    
    @Override
    public IModel loadModel(Identifier modelLocation) {
        try {
            Offset offset = ModelHelper.loadOffsetFromJson(modelLocation);
            IModel model = new MaterialModel(ModelHelper.loadTextureListFromJson(modelLocation), offset.x, offset.y);
            
            Identifier originalLocation = getReducedPath(modelLocation);
            
            // register the base texture for texture generation
            if (partTextureRestriction.containsKey(originalLocation)) {
                for (IToolPart toolPart : partTextureRestriction.get(originalLocation)) {
                    for (Identifier texture : model.getTextures()) {
                        CustomTextureCreator.registerTextureForPart(texture, toolPart);
                    }
                }
            } else {
                CustomTextureCreator.registerTextures(model.getTextures());
            }
            
            return model;
        } catch (IOException e) {
            TinkerRegistry.log.error("Could not load material model {}", modelLocation.toString());
            TinkerRegistry.log.debug(e);
        }
        return ModelLoaderRegistry.getMissingModel();
    }
    
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
    
    }
}
