package slimeknights.tconstruct.shared;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

@SuppressWarnings("WeakerAccess")
public final class TinkerFood {
    
    /* Slime balls are not exactly food items, but you CAN eat them.. if you really want to. */
    public static final FoodComponent BLUE_SLIME_BALL = (new FoodComponent.Builder()).hunger(1).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5909, 20 * 45, 2), 1.0F).effect(new StatusEffectInstance(StatusEffects.field_5913, 20 * 60, 2), 1.0F).build();
    public static final FoodComponent PURPLE_SLIME_BALL = (new FoodComponent.Builder()).hunger(1).saturation(2.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5908, 20 * 45), 1.0F).effect(new StatusEffectInstance(StatusEffects.field_5926, 20 * 60), 1.0F).build();
    public static final FoodComponent BLOOD_SLIME_BALL = (new FoodComponent.Builder()).hunger(1).saturation(1.5F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5899, 20 * 45, 2), 1.0F).effect(new StatusEffectInstance(StatusEffects.field_5914, 20 * 60), 1.0F).build();
    public static final FoodComponent MAGMA_SLIME_BALL = (new FoodComponent.Builder()).hunger(1).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5911, 20 * 45), 1.0F).effect(new StatusEffectInstance(StatusEffects.field_5920, 20 * 15), 1.0F).effect(new StatusEffectInstance(StatusEffects.field_5918, 20 * 60), 1.0F).build();
    public static final FoodComponent PINK_SLIME_BALL = (new FoodComponent.Builder()).hunger(1).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5916, 20 * 10, 2), 1.0F).build();
    
    /* Bacon. What more is there to say? */
    public static final FoodComponent BACON = (new FoodComponent.Builder()).hunger(4).saturation(0.6F).build();
    
    /* Jerkies from drying racks */
    public static final FoodComponent MONSTER_JERKY = (new FoodComponent.Builder()).hunger(4).saturation(0.4F).build();
    public static final FoodComponent BEEF_JERKY = (new FoodComponent.Builder()).hunger(8).saturation(1.0F).build();
    public static final FoodComponent CHICKEN_JERKY = (new FoodComponent.Builder()).hunger(6).saturation(0.8F).build();
    public static final FoodComponent PORK_JERKY = (new FoodComponent.Builder()).hunger(8).saturation(1.0F).build();
    public static final FoodComponent MUTTON_JERKY = (new FoodComponent.Builder()).hunger(6).saturation(1.0F).build();
    public static final FoodComponent RABBIT_JERKY = (new FoodComponent.Builder()).hunger(5).saturation(0.8F).build();
    public static final FoodComponent FISH_JERKY = (new FoodComponent.Builder()).hunger(5).saturation(0.8F).build();
    public static final FoodComponent SALMON_JERKY = (new FoodComponent.Builder()).hunger(6).saturation(1.0F).build();
    public static final FoodComponent CLOWNFISH_JERKY = (new FoodComponent.Builder()).hunger(3).saturation(0.8F).build();
    public static final FoodComponent PUFFERFISH_JERKY = (new FoodComponent.Builder()).hunger(3).saturation(0.8F).build();
    
    /* Slime Drops. Slime balls + drying rack = "healthy" */
    public static final FoodComponent GREEN_SLIME_DROP = (new FoodComponent.Builder()).hunger(1).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5904, 20 * 90, 2), 1.0F).build();
    public static final FoodComponent BLUE_SLIME_DROP = (new FoodComponent.Builder()).hunger(3).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5913, 20 * 90, 2), 1.0F).build();
    public static final FoodComponent PURPLE_SLIME_DROP = (new FoodComponent.Builder()).hunger(3).saturation(2.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5926, 20 * 90), 1.0F).build();
    public static final FoodComponent BLOOD_SLIME_DROP = (new FoodComponent.Builder()).hunger(3).saturation(1.5F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5914, 20 * 90), 1.0F).build();
    public static final FoodComponent MAGMA_SLIME_DROP = (new FoodComponent.Builder()).hunger(6).saturation(1.0F).setAlwaysEdible().effect(new StatusEffectInstance(StatusEffects.field_5918, 20 * 90), 1.0F).build();
    
    private TinkerFood() {}
}
