package slimeknights.tconstruct.gadgets.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import slimeknights.mantle.item.ItemTooltip;
import slimeknights.mantle.util.LocUtils;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.SlimeBounceHandler;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.block.BlockSlime.SlimeType;
import slimeknights.tconstruct.tools.common.network.EntityMovementChangePacket;

import javax.annotation.Nonnull;

public class ItemSlimeSling extends ItemTooltip {

  public ItemSlimeSling() {
    this.setMaxStackSize(1);
    this.setCreativeTab(TinkerRegistry.tabGadgets);
    this.hasSubtypes = true;
  }

  @Nonnull
  @Override
  public TypedActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    playerIn.setActiveHand(hand);
    return new TypedActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
  }

  @Nonnull
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }

  // sling logic

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
    if(!(entity instanceof EntityPlayer)) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;
    // has to be on ground to do something
    if(!player.onGround) {
      return;
    }

    // copy chargeup code from bow \o/
    int i = this.getMaxItemUseDuration(stack) - timeLeft;
    float f = i / 20.0F;
    f = (f * f + f * 2.0F) / 3.0F;
    f *= 4f;

    if(f > 6f) {
      f = 6f;
    }

    // check if player was targeting a block
    HitResult mop = rayTrace(world, player, false);

    if(mop != null && mop.typeOfHit == HitResult.Type.BLOCK) {
      // we fling the inverted player look vector
      Vec3d vec = player.getLookVec().normalize();

      player.addVelocity(vec.x * -f,
                         vec.y * -f / 3f,
                         vec.z * -f);

      if(player instanceof EntityPlayerMP) {
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        TinkerNetwork.sendTo(new EntityMovementChangePacket(player), playerMP);
        //playerMP.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
      }
      player.playSound(Sounds.slimesling, 1f, 1f);
      SlimeBounceHandler.addBounceHandler(player);
    }
  }

  /* colors */

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {
    int meta = stack.getMetadata(); // should call getMetadata below
    if(meta < SlimeType.values().length) {
      return super.getUnlocalizedName(stack) + "." + LocUtils.makeLocString(SlimeType.values()[meta].name());
    }
    else {
      return super.getUnlocalizedName(stack);
    }
  }

  @Override
  public void getSubItems(CreativeTabs tab, DefaultedList<ItemStack> subItems) {
    if(this.isInCreativeTab(tab)) {
      for(SlimeType type : SlimeType.VISIBLE_COLORS) {
        subItems.add(new ItemStack(this, 1, type.getMeta()));
      }
    }
  }
}
