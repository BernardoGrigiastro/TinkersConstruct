package slimeknights.tconstruct.library.client.texture;

import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

public class TinkerTexture extends Sprite {
    
    protected TinkerTexture(String spriteName) {
        super(spriteName);
    }
    
    public static Sprite loadManually(Identifier sprite) {
        return new TinkerTexture(sprite.toString());
    }
}
