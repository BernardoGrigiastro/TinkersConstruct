package slimeknights.tconstruct.library.materials;

import net.minecraft.util.Identifier;

/**
 * This is just a copy of ResourceLocation for type safety.
 */
public class MaterialId extends Identifier {
    
    public MaterialId(String namespaceIn, String pathIn) {
        super(namespaceIn, pathIn);
    }
    
    public MaterialId(Identifier resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }
}
