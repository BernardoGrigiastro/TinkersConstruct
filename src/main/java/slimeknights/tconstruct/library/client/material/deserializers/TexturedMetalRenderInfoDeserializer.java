package slimeknights.tconstruct.library.client.material.deserializers;

import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class TexturedMetalRenderInfoDeserializer extends MetalRenderInfoDeserializer {

  protected String texture;

  @Override
  public MaterialRenderInfo getMaterialRenderInfo() {
    return new MaterialRenderInfo.MetalTextured(new Identifier(texture), fromHex(color), shinyness, brightness, hueshift);
  }
}
