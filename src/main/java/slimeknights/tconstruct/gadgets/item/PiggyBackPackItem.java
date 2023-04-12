package slimeknights.tconstruct.gadgets.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.network.packet.EntityPassengersSetS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import slimeknights.mantle.client.screen.ElementScreen;
import slimeknights.mantle.item.ArmorTooltipItem;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.items.GadgetItems;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.capability.piggyback.CapabilityTinkerPiggyback;
import slimeknights.tconstruct.library.capability.piggyback.ITinkerPiggyback;
import slimeknights.tconstruct.library.client.Icons;
import slimeknights.tconstruct.library.effect.TinkerEffect;

import javax.annotation.Nonnull;

public class PiggyBackPackItem extends ArmorTooltipItem {
    
    // todo: turn this into a config
    private static final int MAX_ENTITY_STACK = 3; // how many entities can be carried at once
    
    private static final ArmorMaterial PIGGYBACK = new ArmorMaterial() {
        private final Ingredient empty_repair_material = Ingredient.fromItems(Items.AIR);
        
        @Override
        public int getDurability(EquipmentSlot slotIn) {
            return 0;
        }
        
        @Override
        public int getDamageReductionAmount(EquipmentSlot slotIn) {
            return 0;
        }
        
        @Override
        public int getEnchantability() {
            return 0;
        }
        
        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.field_14788;
        }
        
        @Override
        public Ingredient getRepairMaterial() {
            return this.empty_repair_material;
        }
        
        @Override
        public String getName() {
            return Util.resource("piggyback");
        }
        
        @Override
        public float getToughness() {
            return 0;
        }
    };
    
    public PiggyBackPackItem() {
        super(PIGGYBACK, EquipmentSlot.field_6174, (new Properties()).group(TinkerRegistry.tabGadgets).maxStackSize(16));
    }
    
    @Nonnull
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        return new TypedActionResult<>(ActionResult.field_5811, itemStackIn);
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        // is the chest slot empty?
        ItemStack chestArmor = playerIn.getItemStackFromSlot(this.slot);
        
        // need enough space to exchange the chest armor
        if (chestArmor.getItem() != this && playerIn.inventory.getFirstEmptyStack() == -1) {
            // not enough inventory space
            return false;
        }
        
        if (this.pickupEntity(playerIn, target)) {
            // unequip old armor
            if (chestArmor.getItem() != this) {
                ItemHandlerHelper.giveItemToPlayer(playerIn, chestArmor);
                chestArmor = ItemStack.EMPTY;
            }
            
            // we could pick it up just fine, check if we need to "equip" more of the item
            if (chestArmor.isEmpty()) {
                playerIn.setItemStackToSlot(this.slot, stack.split(1));
            } else if (chestArmor.getCount() < this.getEntitiesCarriedCount(playerIn)) {
                stack.split(1);
                chestArmor.grow(1);
            }
            // successfully picked up an entity
            return true;
        }
        
        return false;
    }
    
    private boolean pickupEntity(PlayerEntity player, Entity target) {
        if (player.getEntityWorld().isClient) {
            return false;
        }
        // silly players, clicking on entities they're already carrying or riding
        if (target.getRidingEntity() == player || player.getRidingEntity() == target) {
            return false;
        }
        
        int count = 0;
        Entity toRide = player;
        while (toRide.isBeingRidden() && count < MAX_ENTITY_STACK) {
            toRide = toRide.getPassengers().get(0);
            count++;
            // don't allow more than 1 player, that can easily cause endless loops with riding detection for some reason.
            if (toRide instanceof PlayerEntity && target instanceof PlayerEntity) {
                return false;
            }
        }
        
        // can only ride one entity each
        if (!toRide.isBeingRidden() && count < MAX_ENTITY_STACK) {
            // todo: possibly throw off all passengers of the target
            if (target.startRiding(toRide, true)) {
                if (player instanceof ServerPlayerEntity) {
                    TinkerNetwork.instance.sendVanillaPacket(player, new EntityPassengersSetS2CPacket(player));
                }
                return true;
            }
        }
        return false;
    }
    
    private int getEntitiesCarriedCount(LivingEntity player) {
        int count = 0;
        Entity ridden = player;
        while (ridden.isBeingRidden()) {
            count++;
            ridden = ridden.getPassengers().get(0);
        }
        
        return count;
    }
    
    public void matchCarriedEntitiesToCount(LivingEntity player, int maxCount) {
        int count = 0;
        // get top rider
        Entity ridden = player;
        while (ridden.isBeingRidden()) {
            ridden = ridden.getPassengers().get(0);
            count++;
            
            if (count > maxCount) {
                ridden.stopRiding();
            }
        }
    }
    
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;
            if (livingEntity.getItemStackFromSlot(EquipmentSlot.field_6174) == stack && entityIn.isBeingRidden()) {
                int amplifier = this.getEntitiesCarriedCount(livingEntity) - 1;
                livingEntity.addPotionEffect(new StatusEffectInstance(CarryPotionEffect.INSTANCE, 1, amplifier, true, false));
            }
        }
    }
    
    public static class CarryPotionEffect extends TinkerEffect {
        
        public static final CarryPotionEffect INSTANCE = new CarryPotionEffect();
        static final String UUID = "ff4de63a-2b24-11e6-b67b-9e71128cae77";
        
        CarryPotionEffect() {
            super(Util.getResource("carry"), StatusEffectType.field_18273, true);
            
            this.addAttributesModifier(EntityAttributes.MOVEMENT_SPEED, UUID, -0.05D, EntityAttributeModifier.Operation.field_6331);
        }
        
        @Override
        public boolean isReady(int duration, int amplifier) {
            return true; // check every tick
        }
        
        @Override
        public void performEffect(@Nonnull LivingEntity livingEntityIn, int p_76394_2_) {
            ItemStack chestArmor = livingEntityIn.getItemStackFromSlot(EquipmentSlot.field_6174);
            if (chestArmor.isEmpty() || chestArmor.getItem() != GadgetItems.piggy_backpack) {
                GadgetItems.piggy_backpack.matchCarriedEntitiesToCount(livingEntityIn, 0);
            } else {
                GadgetItems.piggy_backpack.matchCarriedEntitiesToCount(livingEntityIn, chestArmor.getCount());
                if (!livingEntityIn.getEntityWorld().isClient) {
                    if (livingEntityIn.getCapability(CapabilityTinkerPiggyback.PIGGYBACK, null).isPresent()) {
                        ITinkerPiggyback piggyback = livingEntityIn.getCapability(CapabilityTinkerPiggyback.PIGGYBACK, null).orElse(null);
                        if (piggyback != null) {
                            piggyback.updatePassengers();
                        }
                    }
                }
            }
        }
        
        @Override
        @OnlyIn(Dist.CLIENT)
        public void renderInventoryEffect(StatusEffectInstance effect, AbstractInventoryScreen<?> gui, int x, int y, float z) {
            this.renderHUDEffect(effect, gui, x, y, z, 1f);
        }
        
        @Override
        @OnlyIn(Dist.CLIENT)
        public void renderHUDEffect(StatusEffectInstance effect, DrawableHelper gui, int x, int y, float z, float alpha) {
            MinecraftClient.getInstance().getTextureManager().bindTexture(Icons.ICON);
            ElementScreen element;
            
            switch (effect.getAmplifier()) {
                case 0:
                    element = Icons.ICON_PIGGYBACK_1;
                    break;
                case 1:
                    element = Icons.ICON_PIGGYBACK_2;
                    break;
                default:
                    element = Icons.ICON_PIGGYBACK_3;
                    break;
            }
            
            element.draw(x + 6, y + 7);
        }
    }
}
