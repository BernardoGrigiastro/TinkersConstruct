/* Code for ctl and shift down from TicTooltips by squeek502
 * https://github.com/squeek502/TiC-Tooltips/blob/1.7.10/java/squeek/tictooltips/helpers/KeyHelper.java
 */

package slimeknights.tconstruct.library;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFW;
import slimeknights.mantle.util.RecipeMatchRegistry;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class Util {
    
    public static final String MODID = "tconstruct";
    public static final String RESOURCE = MODID.toLowerCase(Locale.US);
    public static final Marker TCONSTRUCT = MarkerManager.getMarker("TCONSTRUCT");
    
    public static final DecimalFormat df = new DecimalFormat("#,###,###.##", DecimalFormatSymbols.getInstance(Locale.US));
    public static final DecimalFormat dfPercent = new DecimalFormat("#%");
    /* Position helpers */
    private static ImmutableMap<Vec3i, Direction> offsetMap;
    
    static {
        ImmutableMap.Builder<Vec3i, Direction> builder = ImmutableMap.builder();
        for (Direction facing : Direction.values()) {
            builder.put(facing.getDirectionVec(), facing);
        }
        offsetMap = builder.build();
    }
    
    public static Logger getLogger(String type) {
        String log = MODID;
        
        return LogManager.getLogger(log + "-" + type);
    }
    
    public static Optional<String> getCurrentlyActiveExternalMod() {
        return Optional.ofNullable(ModLoadingContext.get().getActiveContainer().getModId()).filter(activeModId -> !MODID.equals(activeModId));
    }
    
    /**
     * Removes all whitespaces from the given string and makes it lowerspace.
     */
    public static String sanitizeLocalizationString(String string) {
        return string.toLowerCase(Locale.US).replaceAll(" ", "").trim();
    }
    
    /**
     * Returns the given Resource prefixed with tinkers resource location. Use this function instead of hardcoding
     * resource locations.
     */
    public static String resource(String res) {
        return String.format("%s:%s", RESOURCE, res);
    }
    
    public static Identifier getResource(String res) {
        return new Identifier(RESOURCE, res);
    }
    
    public static ModelIdentifier getModelResource(String res, String variant) {
        return new ModelIdentifier(resource(res), variant);
    }
    
    public static Identifier getModifierResource(String res) {
        return getResource("models/item/modifiers/" + res);
    }
    
    /**
     * Prefixes the given unlocalized name with tinkers prefix. Use this when passing unlocalized names for a uniform
     * namespace.
     */
    public static String prefix(String name) {
        return String.format("%s.%s", RESOURCE, name.toLowerCase(Locale.US));
    }
    
    /**
     * Translate the string, insert parameters into the translation key
     */
    public static String translate(String key, Object... pars) {
        // translates twice to allow rerouting/alias
        return I18n.format(I18n.format(String.format(key, pars)).trim()).trim();
    }
    
    /**
     * Translate the string, insert parameters into the result of the translation
     */
    public static String translateFormatted(String key, Object... pars) {
        // translates twice to allow rerouting/alias
        return I18n.format(I18n.format(key, pars).trim()).trim();
    }
    
    /**
     * Returns a fixed size DEEP copy of the list
     */
    public static DefaultedList<ItemStack> deepCopyFixedNonNullList(DefaultedList<ItemStack> in) {
        return RecipeMatchRegistry.copyItemStackArray(in);
    }
    
    /**
     * @deprecated use deepCopyFixedNonNullList
     */
    @Deprecated
    public static DefaultedList<ItemStack> copyItemStackArray(DefaultedList<ItemStack> in) {
        return deepCopyFixedNonNullList(in);
    }
    
    /* Code for ctl and shift down  from TicTooltips by squeek502
     * https://github.com/squeek502/TiC-Tooltips/blob/1.7.10/java/squeek/tictooltips/helpers/KeyHelper.java
     */
    public static boolean isCtrlKeyDown() {
        // prioritize CONTROL, but allow OPTION as well on Mac (note: GuiScreen's isCtrlKeyDown only checks for the OPTION key on Mac)
        boolean isCtrlKeyDown = InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_RIGHT_CONTROL);
        if (!isCtrlKeyDown && MinecraftClient.IS_SYSTEM_MAC) {
            isCtrlKeyDown = InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_ALT) || InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_RIGHT_ALT);
        }
        
        return isCtrlKeyDown;
    }
    
    public static boolean isShiftKeyDown() {
        return InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyDown(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
    
    /**
     * Returns the actual color value for a chatformatting
     */
    public static int enumChatFormattingToColor(Formatting color) {
        int i = color.getColorIndex();
        int j = (i >> 3 & 1) * 85;
        int k = (i >> 2 & 1) * 170 + j;
        int l = (i >> 1 & 1) * 170 + j;
        int i1 = (i >> 0 & 1) * 170 + j;
        if (i == 6) {
            k += 85;
        }
        if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
        }
        
        return (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
    }
    
    /**
     * Gets the offset direction from two blocks
     *
     * @param offset Position offset
     * @return Direction of the offset, or null if no direction
     */
    public static Direction facingFromOffset(BlockPos offset) {
        return offsetMap.get(offset);
    }
    
    /**
     * Gets the offset direction from two blocks
     *
     * @param pos      Base position
     * @param neighbor Position Neighbor position
     * @return Direction of the offset, or null if no direction
     */
    public static Direction facingFromNeighbor(BlockPos pos, BlockPos neighbor) {
        // neighbor is first. For example, neighbor height is 11, pos is 10, so result is 1 or up
        return facingFromOffset(neighbor.subtract(pos));
    }
    
    /**
     * Returns true if the player clicked within the specified bounding box
     *
     * @param aabb Bounding box clicked
     * @param hitX X hit location
     * @param hitY Y hit location
     * @param hitZ Z hit location
     * @return True if the click was within the box
     */
    public static boolean clickedAABB(Box aabb, float hitX, float hitY, float hitZ) {
        return aabb.minX <= hitX && hitX <= aabb.maxX && aabb.minY <= hitY && hitY <= aabb.maxY && aabb.minZ <= hitZ && hitZ <= aabb.maxZ;
    }
    
}
