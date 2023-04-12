package slimeknights.tconstruct.library;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.client.texture.AbstractColoredTexture;

import java.util.Map;

@SideOnly(Side.CLIENT)
public final class TinkerRegistryClient {

    // the logger for the library
    public static final Logger log = Util.getLogger("API-Client");
    /*---------------------------------------------------------------------------
    | GUI & CRAFTING                                                            |
    ---------------------------------------------------------------------------*/
    private static final Map<Item, ToolBuildGuiInfo> toolBuildInfo = Maps.newLinkedHashMap();
    /*---------------------------------------------------------------------------
    | MATERIAL TEXTURE CREATION                                                 |
    ---------------------------------------------------------------------------*/
    private static final Map<String, AbstractColoredTexture> textureProcessors = Maps.newHashMap();

    private TinkerRegistryClient() {
    }

    public static void addToolBuilding(ToolBuildGuiInfo info) {
        toolBuildInfo.put(info.tool.getItem(), info);
    }

    public static ToolBuildGuiInfo getToolBuildInfoForTool(Item tool) {
        return toolBuildInfo.get(tool);
    }

    public static void clear() {
        toolBuildInfo.clear();
    }

}
