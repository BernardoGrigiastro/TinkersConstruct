package slimeknights.tconstruct.library.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import javax.vecmath.Vector3f;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.CustomTextureCreator;
import slimeknights.tconstruct.library.materials.Material;

public class MaterialModel implements IPatternOffset, IModel {

  protected final int offsetX;
  protected final int offsetY;

  private final ImmutableList<Identifier> textures;

  public MaterialModel(ImmutableList<Identifier> textures) {
    this(textures, 0, 0);
  }

  public MaterialModel(ImmutableList<Identifier> textures, int offsetX, int offsetY) {
    this.textures = textures;

    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  @Override
  public IBakedModel bake(IModelState state, VertexFormat format, Function<Identifier, Sprite> bakedTextureGetter) {
    return bakeIt(state, format, bakedTextureGetter);
  }

  // the only difference here is the return-type
  public BakedMaterialModel bakeIt(IModelState state, VertexFormat format, Function<Identifier, Sprite> bakedTextureGetter) {
    // take offset of texture into account
    if(offsetX != 0 || offsetY != 0) {
      state = new ModelStateComposition(state, TRSRTransformation
          .blockCenterToCorner(new TRSRTransformation(new Vector3f(offsetX / 16f, -offsetY / 16f, 0), null, null, null)));
    }
    ImmutableMap<TransformType, TRSRTransformation> map = PerspectiveMapWrapper.getTransforms(state);

    // normal model as the base
    IBakedModel base = new ItemLayerModel(textures).bake(state, format, bakedTextureGetter);

    // turn it into a baked material-model
    BakedMaterialModel bakedMaterialModel = new BakedMaterialModel(base, map);

    // and generate the baked model for each material-variant we have for the base texture
    String baseTexture = base.getParticleTexture().getIconName();
    Map<String, Sprite> sprites = CustomTextureCreator.sprites.get(baseTexture);

    if(sprites != null) {
      for(Map.Entry<String, Sprite> entry : sprites.entrySet()) {
        Material material = TinkerRegistry.getMaterial(entry.getKey());

        IModel model2 = ItemLayerModel.INSTANCE.retexture(ImmutableMap.of("layer0", entry.getValue().getIconName()));
        IBakedModel bakedModel2 = model2.bake(state, format, bakedTextureGetter);

        // if it's a colored material we need to color the quads. But only if the texture was not a custom texture
        if(material.renderInfo.useVertexColoring() && !CustomTextureCreator.exists(baseTexture + "_" + material.identifier)) {
          int color = (material.renderInfo).getVertexColor();

          ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
          // ItemLayerModel.BakedModel only uses general quads
          for(BakedQuad quad : bakedModel2.getQuads(null, null, 0)) {
            quads.add(ModelHelper.colorQuad(color, quad));
          }

          // create a new model with the colored quads
          bakedModel2 = new BakedSimpleItem(quads.build(), map, bakedModel2);
        }

        bakedMaterialModel.addMaterialModel(material, bakedModel2);
      }
    }

    return bakedMaterialModel;
  }

  @Override
  public Collection<Identifier> getDependencies() {
    return ImmutableList.of();
  }

  @Override
  public Collection<Identifier> getTextures() {
    return textures;
  }

  @Override
  public IModelState getDefaultState() {
    return ModelHelper.DEFAULT_ITEM_STATE;
  }

  @Override
  public int getXOffset() {
    return offsetX;
  }

  @Override
  public int getYOffset() {
    return offsetY;
  }
}
