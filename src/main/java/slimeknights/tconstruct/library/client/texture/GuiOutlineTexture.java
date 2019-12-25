package slimeknights.tconstruct.library.client.texture;

import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.client.RenderUtil;

public class GuiOutlineTexture extends ExtraUtilityTexture {

  public GuiOutlineTexture(Identifier baseTexture, String spriteName) {
    super(baseTexture, spriteName);
  }

  @Override
  protected int colorPixel(int pixel, int pxCoord) {
    if(!trans[pxCoord]) {
      if(edge[pxCoord]) {
        return RenderUtil.compose(50, 50, 50, 255);
      }
      else {
        return 0;
      }
    }

    return pixel;
  }
}
