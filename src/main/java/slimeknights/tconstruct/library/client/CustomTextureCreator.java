package slimeknights.tconstruct.library.client;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.client.model.IPatternOffset;
import slimeknights.tconstruct.library.client.model.MaterialModelLoader;
import slimeknights.tconstruct.library.client.texture.*;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialGUI;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Textures registered with this creator will get a texture created/loaded for each material.
 */
public class CustomTextureCreator implements IResourceManagerReloadListener {
    
    public static final CustomTextureCreator INSTANCE = new CustomTextureCreator();
    public static final Material guiMaterial;
    /**
     * Holds all sprites built from the base-texture used as the key.
     */
    public static Map<String, Map<String, Sprite>> sprites = Maps.newHashMap();
    // set these to the pattern/cast model to generate part-textures for them
    public static Identifier patternModelLocation;
    public static Identifier castModelLocation;
    public static String patternLocString;
    public static String castLocString;
    private static Logger log = Util.getLogger("TextureGen");
    private static Set<Identifier> baseTextures = Sets.newHashSet();
    private static Map<Identifier, Set<IToolPart>> texturePartMapping = Maps.newHashMap();

    static {
        guiMaterial = new MaterialGUI("_internal_gui");
        guiMaterial.setRenderInfo(new MaterialRenderInfo.AbstractMaterialRenderInfo() {
            @Override
            public Sprite getTexture(Identifier baseTexture, String location) {
                return new GuiOutlineTexture(baseTexture, location);
            }
        });
    }

    private int createdTextures;
    
    public static void registerTextures(Collection<Identifier> textures) {
        baseTextures.addAll(textures);
    }
    
    public static void registerTexture(Identifier texture) {
        baseTextures.add(texture);
    }
    
    public static void registerTextureForPart(Identifier texture, IToolPart toolPart) {
        if (!texturePartMapping.containsKey(texture)) {
            texturePartMapping.put(texture, Sets.newHashSet());
        }
        texturePartMapping.get(texture).add(toolPart);
        registerTexture(texture);
    }
    
    public static boolean exists(String res) {
        List<IResource> resources = null;
        try {
            Identifier loc = new Identifier(res);
            loc = new Identifier(loc.getResourceDomain(), "textures/" + loc.getResourcePath() + ".png");
            resources = MinecraftClient.getMinecraft().getResourceManager().getAllResources(loc);
        } catch (IOException e) {
            return false;
        } finally {
            if (resources != null) {
                for (IResource resource : resources) {
                    IOUtils.closeQuietly(resource);
                }
            }
        }
        return true;
    }
    
    public static boolean belongsToToolPart(Identifier location) {
        for (IToolPart toolpart : TinkerRegistry.getToolParts()) {
            if (!(toolpart instanceof Item)) {
                continue; // WHY?!
            }
            try {
                Optional<Identifier> storedResourceLocation = MaterialModelLoader.getToolPartModelLocation(toolpart);
                if (storedResourceLocation.isPresent()) {
                    Identifier stored = storedResourceLocation.get();
                    Identifier modelLocation = new Identifier(stored.getResourceDomain(), "item/" + stored.getResourcePath());
                    IModel partModel = ModelLoaderRegistry.getModel(modelLocation);
                    
                    // the actual texture of the part
                    Identifier baseTexture = partModel.getTextures().iterator().next();
                    if (baseTexture.toString().equals(location.toString())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    // low since other event-handlers might want to register textures beforehand
    @SubscribeEvent(priority = EventPriority.LOW)
    public void createCustomTextures(TextureStitchEvent.Pre event) {
        // get the material info at this point, to override hardcoded material rendering with resources
        MaterialRenderInfoLoader.INSTANCE.onResourceManagerReload(MinecraftClient.getMinecraft().getResourceManager());
        
        createdTextures = 0;
        // create textures for each material where needed
        createMaterialTextures(event.getMap());
        
        // add stencil and cast textures for all used toolparts
        createPatterntextures(event.getMap());
        
        log.debug("Generated " + createdTextures + " Textures for Materials");
    }
    
    private void createMaterialTextures(TextureMap map) {
        // Create textures for toolparts and tools - Textures that need 1 per material
        for (Identifier baseTexture : baseTextures) {
            // exclude missingno :I
            if (baseTexture.toString().equals("minecraft:missingno")) {
                continue;
            }
            
            Set<IToolPart> parts = texturePartMapping.get(baseTexture);
            
            Map<String, Sprite> builtSprites = Maps.newHashMap();
            for (Material material : TinkerRegistry.getAllMaterials()) {
                boolean usable;
                if (parts == null || material instanceof MaterialGUI) {
                    usable = true;
                } else {
                    usable = false;
                    for (IToolPart toolPart : parts) {
                        usable |= toolPart.canUseMaterialForRendering(material);
                    }
                }
                
                if (usable) {
                    Sprite sprite = createTexture(material, baseTexture, map);
                    if (sprite != null) {
                        builtSprites.put(material.identifier, sprite);
                    }
                }
            }
            
            if (belongsToToolPart(baseTexture)) {
                Sprite sprite = createTexture(guiMaterial, baseTexture, map);
                if (sprite != null) {
                    builtSprites.put(guiMaterial.identifier, sprite);
                }
            }
            
            sprites.put(baseTexture.toString(), builtSprites);
        }
    }
    
    private Sprite createTexture(Material material, Identifier baseTexture, TextureMap map) {
        String location = baseTexture.toString() + "_" + material.identifier;
        Sprite sprite;
        
        if (exists(location)) {
            sprite = map.registerSprite(new Identifier(location));
        } else {
            // material does not need a special generated texture
            if (material.renderInfo == null) {
                return null;
            }
            
            // different base texture?
            if (material.renderInfo.getTextureSuffix() != null) {
                String loc2 = baseTexture.toString() + "_" + material.renderInfo.getTextureSuffix();
                Sprite base2 = map.getTextureExtry(loc2);
                // can we manually load it?
                if (base2 == null && exists(loc2)) {
                    base2 = TinkerTexture.loadManually(new Identifier(loc2));
                    // save in the map so it's getting reused by the others and is available
                    map.setTextureEntry(base2);
                }
                if (base2 != null) {
                    baseTexture = new Identifier(base2.getIconName());
                }
            }
            
            sprite = material.renderInfo.getTexture(baseTexture, location);
            createdTextures++;
        }
        
        // stitch new textures
        if (sprite != null && material.renderInfo.isStitched()) {
            map.setTextureEntry(sprite);
        }
        return sprite;
    }
    
    private void createPatterntextures(TextureMap map) {
        // create Pattern textures
        if (patternModelLocation != null) {
            patternLocString = createPatternTexturesFor(map, patternModelLocation, TinkerRegistry.getPatternItems(), PatternTexture.class);
        }
        // create cast textures
        if (castModelLocation != null) {
            castLocString = createPatternTexturesFor(map, castModelLocation, TinkerRegistry.getCastItems(), CastTexture.class);
        }
    }
    
    public String createPatternTexturesFor(TextureMap map, Identifier baseTextureLoc, Iterable<Item> items, Class<? extends TextureColoredTexture> clazz) {
        Constructor<? extends TextureColoredTexture> constructor;
        String baseTextureString;
        Identifier patternLocation;
        try {
            constructor = clazz.getConstructor(Identifier.class, Identifier.class, String.class);
            IModel patternModel = ModelLoaderRegistry.getModel(baseTextureLoc);
            patternLocation = patternModel.getTextures().iterator().next();
            baseTextureString = patternLocation.toString();
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        
        for (Item item : items) {
            try {
                // get id
                String identifier = Pattern.getTextureIdentifier(item);
                String partPatternLocation = baseTextureString + identifier;
                Sprite partPatternTexture;
                if (exists(partPatternLocation)) {
                    partPatternTexture = map.registerSprite(new Identifier(partPatternLocation));
                    map.setTextureEntry(partPatternTexture);
                } else {
                    Identifier modelLocation = item.getRegistryName();
                    IModel partModel = ModelLoaderRegistry.getModel(new Identifier(modelLocation.getResourceDomain(), "item/parts/" + modelLocation.getResourcePath() + MaterialModelLoader.EXTENSION));
                    Identifier partTexture = partModel.getTextures().iterator().next();
                    
                    if (partModel != ModelLoaderRegistry.getMissingModel()) {
                        partPatternTexture = constructor.newInstance(partTexture, patternLocation, partPatternLocation);
                        if (partModel instanceof IPatternOffset) {
                            IPatternOffset offset = (IPatternOffset) partModel;
                            ((TextureColoredTexture) partPatternTexture).setOffset(offset.getXOffset(), offset.getYOffset());
                        }
                        map.setTextureEntry(partPatternTexture);
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        
        return baseTextureString;
    }
    
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        // clear cache
        baseTextures.clear();
        for (Map map : sprites.values()) {
            // safety in case there are some references lying around
            map.clear();
        }
        sprites.clear();
    }
}
