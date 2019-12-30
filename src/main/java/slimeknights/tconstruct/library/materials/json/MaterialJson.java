package slimeknights.tconstruct.library.materials.json;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class MaterialJson {
    
    private final Boolean craftable;
    private final Identifier fluid;
    private final Identifier shardItem;
    
    public MaterialJson(Boolean craftable, Identifier fluid, Identifier shardItem) {
        this.craftable = craftable;
        this.fluid = fluid;
        this.shardItem = shardItem;
    }
    
    @Nullable
    public Boolean getCraftable() {
        return craftable;
    }
    
    @Nullable
    public Identifier getFluid() {
        return fluid;
    }
    
    @Nullable
    public Identifier getShardItem() {
        return shardItem;
    }
}
