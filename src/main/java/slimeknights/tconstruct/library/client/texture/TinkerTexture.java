package slimeknights.tconstruct.library.client.texture;

import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

public class TinkerTexture extends Sprite {

  public static Sprite loadManually(Identifier sprite) {
    return new TinkerTexture(sprite.toString());
  }

  protected TinkerTexture(String spriteName) {
    super(spriteName);
  }
}
