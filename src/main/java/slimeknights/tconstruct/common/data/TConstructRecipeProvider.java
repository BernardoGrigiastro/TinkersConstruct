package slimeknights.tconstruct.common.data;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.blocks.CommonBlocks;
import slimeknights.tconstruct.blocks.DecorativeBlocks;
import slimeknights.tconstruct.items.CommonItems;
import slimeknights.tconstruct.library.TinkerPulseIds;

import java.util.function.Consumer;

public class TConstructRecipeProvider extends RecipesProvider {
    
    public TConstructRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }
    
    @Override
    protected void registerRecipes(Consumer<RecipeJsonProvider> consumer) {
        this.addCommon(consumer);
        
        if (TConstruct.pulseManager.isPulseLoaded(TinkerPulseIds.TINKER_WORLD_PULSE_ID)) {
            this.addWorld(consumer);
        }
        
        if (TConstruct.pulseManager.isPulseLoaded(TinkerPulseIds.TINKER_GADGETS_PULSE_ID)) {
            this.addGadgets(consumer);
        }
    }
    
    private void addCommon(Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonFactory.shapelessRecipe(CommonBlocks.firewood).addIngredient(Items.field_8183).addIngredient(CommonBlocks.lavawood).addIngredient(Items.field_8183).addCriterion("has_lavawood", this.hasItem(CommonBlocks.lavawood)).build(consumer, "tconstruct:common/firewood/firewood");
        ShapedRecipeJsonFactory.shapedRecipe(CommonBlocks.firewood_slab, 6).key('#', CommonBlocks.firewood).patternLine("###").addCriterion("has_firewood", this.hasItem(CommonBlocks.firewood)).build(consumer, "tconstruct:common/firewood/firewood_slab");
        ShapedRecipeJsonFactory.shapedRecipe(CommonBlocks.firewood_stairs, 4).key('#', CommonBlocks.firewood).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_firewood", this.hasItem(CommonBlocks.firewood)).build(consumer, "tconstruct:common/firewood/firewood_stairs");
        ShapedRecipeJsonFactory.shapedRecipe(CommonBlocks.lavawood_slab, 6).key('#', CommonBlocks.firewood).patternLine("###").addCriterion("has_firewood", this.hasItem(CommonBlocks.firewood)).build(consumer, "tconstruct:common/firewood/lavawood_slab");
        ShapedRecipeJsonFactory.shapedRecipe(CommonBlocks.lavawood_stairs, 4).key('#', CommonBlocks.firewood).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_firewood", this.hasItem(CommonBlocks.firewood)).build(consumer, "tconstruct:common/firewood/lavawood_stairs");
        
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.black_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_BLACK).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/black_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.blue_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_BLUE).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/blue_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.brown_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_BROWN).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/brown_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.cyan_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_CYAN).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/cyan_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.gray_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_GRAY).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/gray_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.green_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_GREEN).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/green_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.light_blue_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_LIGHT_BLUE).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/light_blue_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.light_gray_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_LIGHT_GRAY).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/light_gray_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.lime_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_LIME).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/lime_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.magenta_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_MAGENTA).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/magenta_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.orange_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_ORANGE).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/orange_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.pink_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_PINK).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/pink_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.purple_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_PURPLE).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/purple_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.red_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_RED).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/red_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.white_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_WHITE).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/white_clear_stained_glass");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.yellow_clear_stained_glass, 8).key('#', DecorativeBlocks.clear_glass).key('X', Tags.Items.DYES_YELLOW).patternLine("###").patternLine("#X#").patternLine("###").setGroup("tconstruct:stained_clear_glass").addCriterion("has_clear_glass", this.hasItem(DecorativeBlocks.clear_glass)).build(consumer, "tconstruct:common/glass/yellow_clear_stained_glass");
        
        //ShapedRecipeBuilder.shapedRecipe(alubrass_block).key('#', alubrass_ingot)
        
        ShapelessRecipeJsonFactory.shapelessRecipe(CommonBlocks.graveyard_soil).addIngredient(Blocks.field_10566).addIngredient(Items.field_8511).addIngredient(Items.field_8324).addCriterion("has_dirt", this.hasItem(Blocks.field_10566)).addCriterion("has_rotten_flesh", this.hasItem(Items.field_8511)).addCriterion("has_bone_meal", this.hasItem(Items.field_8324)).build(consumer, "tconstruct:common/soil/graveyard_soil");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.mud_bricks).key('#', CommonItems.mud_brick).patternLine("##").patternLine("##").addCriterion("has_mud_brick", this.hasItem(CommonItems.mud_brick)).build(consumer, "tconstruct:common/soil/mud_bricks_block");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.mud_bricks_slab, 6).key('#', DecorativeBlocks.mud_bricks).patternLine("###").setGroup("tconstruct:mud_brick_slab").addCriterion("has_mud_bricks", this.hasItem(DecorativeBlocks.mud_bricks)).build(consumer, "tconstruct:common/soil/mud_bricks_slab_block");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.mud_bricks_slab).key('#', CommonItems.mud_brick).patternLine("##").setGroup("tconstruct:mud_brick_slab").addCriterion("has_mud_brick", this.hasItem(CommonItems.mud_brick)).build(consumer, "tconstruct:common/soil/mud_bricks_slab");
        ShapedRecipeJsonFactory.shapedRecipe(DecorativeBlocks.mud_bricks_stairs, 4).key('#', DecorativeBlocks.mud_bricks).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_mud_bricks", this.hasItem(DecorativeBlocks.mud_bricks)).build(consumer, "tconstruct:common/soil/mud_bricks_stairs");
        
        ShapelessRecipeJsonFactory.shapelessRecipe(Items.field_8145).addIngredient(Blocks.field_10255).addIngredient(Blocks.field_10255).addIngredient(Blocks.field_10255).addCriterion("has_gravel", this.hasItem(Blocks.field_10255)).build(consumer, "tconstruct:common/flint");
    }
    
    private void addWorld(Consumer<RecipeJsonProvider> consumer) {
    
    }
    
    private void addGadgets(Consumer<RecipeJsonProvider> consumer) {
    /*ShapedRecipeBuilder.shapedRecipe(LOADER_BLOCK, 10)
            .key('#', Items.ENDER_PEARL)
            .patternLine("# #").patternLine("###").patternLine("# #")
            .addCriterion("has_cobblestone", hasItem(Items.ENDER_PEARL))
            .build(consumer);*/
    }
}
