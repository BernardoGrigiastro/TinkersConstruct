package slimeknights.tconstruct.world.client;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;

@SideOnly(Side.CLIENT)
public class CustomStateMap extends StateMapperBase {

    private final String customName;

    public CustomStateMap(String customName) {
        this.customName = customName;
    }

    @Nonnull
    @Override
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        LinkedHashMap<IProperty<?>, Comparable<?>> linkedhashmap = Maps.newLinkedHashMap(state.getProperties());
        Identifier res = new Identifier(Block.REGISTRY.getNameForObject(state.getBlock()).getResourceDomain(), customName);

        return new ModelResourceLocation(res, this.getPropertyString(linkedhashmap));
    }
}
