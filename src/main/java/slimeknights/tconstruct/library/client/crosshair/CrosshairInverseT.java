package slimeknights.tconstruct.library.client.crosshair;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class CrosshairInverseT extends Crosshair {

  public CrosshairInverseT(Identifier texture) {
    super(texture);
  }

  public CrosshairInverseT(Identifier texture, int size) {
    super(texture, size);
  }

  @Override
  protected void drawCrosshair(float spread, float width, float height, float partialTicks) {
    drawTipCrosshairPart(width / 2f, height / 2f - spread, 0);
    drawTipCrosshairPart(width / 2f - spread, height / 2f, 1);
    drawTipCrosshairPart(width / 2f + spread, height / 2f, 2);
    drawTipCrosshairPart(width / 2f, height / 2f + spread, 3);
  }

  private void drawTipCrosshairPart(double x, double y, int part) {
    final double s = 8d;
    final double z = -90;

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder vb = tessellator.getBuffer();
    vb.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_UV); // 4
    // top part
    if(part == 0) {
      vb.pos(x - s, y - s, z).tex(0, 0).endVertex();
      vb.pos(x, y, z).tex(0.46875, 0.46875).endVertex();
      vb.pos(x + s, y - s, z).tex(0.9375, 0).endVertex();
    }
    // left part
    else if(part == 1) {
      vb.pos(x - s, y - s, z).tex(0, 0).endVertex();
      vb.pos(x - s, y + s, z).tex(0, 0.9375).endVertex();
      vb.pos(x, y, z).tex(0.46875, 0.46875).endVertex();
    }
    // right part
    else if(part == 2) {
      vb.pos(x, y, z).tex(0.46875, 0.46875).endVertex();
      vb.pos(x + s, y + s, z).tex(0.9375, 0.9375).endVertex();
      vb.pos(x + s, y - s, z).tex(0.9375, 0).endVertex();
    }

    tessellator.draw();
  }
}
