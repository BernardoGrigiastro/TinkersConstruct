package slimeknights.tconstruct.library.client.texture;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import java.util.Collection;
import java.util.function.Function;

public class MetalTextureTexture extends MetalColoredTexture {

  private final Identifier addTextureLocation;
  protected TextureColoredTexture texture2;

  public MetalTextureTexture(Identifier addTextureLocation, Identifier baseTexture, String spriteName, int baseColor, float shinyness, float brightness, float hueshift) {
    super(baseTexture, spriteName, baseColor, shinyness, brightness, hueshift);
    this.addTextureLocation = addTextureLocation;
    texture2 = new TextureColoredTexture(addTextureLocation, baseTexture, spriteName);
  }

  @Override
  public Collection<Identifier> getDependencies() {
    return ImmutableList.<Identifier>builder()
        .addAll(super.getDependencies())
        .add(addTextureLocation)
        .build();
  }

  @Override
  public boolean load(IResourceManager manager, Identifier location, Function<Identifier, Sprite> textureGetter) {
    // at first do the metal texture
    texture2.load(manager, location, textureGetter);
    return super.load(manager, location, textureGetter);
  }

  @Override
  protected void processData(int[] data) {
    int[] textureData = texture2.getFrameTextureData(0)[0];
    for(int i = 0; i < data.length && i < textureData.length; i++) {
      data[i] = textureData[i];
    }
    super.processData(data);
  }
}
