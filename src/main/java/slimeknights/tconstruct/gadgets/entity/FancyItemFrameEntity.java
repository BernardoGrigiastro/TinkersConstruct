package slimeknights.tconstruct.gadgets.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import slimeknights.tconstruct.gadgets.TinkerGadgets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FancyItemFrameEntity extends ItemFrameEntity implements IEntityAdditionalSpawnData {
    
    private static final TrackedData<Integer> VARIANT = DataTracker.createKey(FancyItemFrameEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final String TAG_VARIANT = "Variant";
    
    public FancyItemFrameEntity(EntityType<? extends FancyItemFrameEntity> type, World world) {
        super(type, world);
    }
    
    public FancyItemFrameEntity(World worldIn, BlockPos blockPos, Direction face, int variant) {
        super(TinkerGadgets.fancy_item_frame, worldIn);
        this.blockPos = blockPos;
        this.updateFacingWithBoundingBox(face);
        this.dataTracker.set(VARIANT, variant);
    }
    
    private static void removeClickEvents(Text p_207712_0_) {
        p_207712_0_.applyTextStyle((p_213318_0_) -> {
            p_213318_0_.setClickEvent(null);
        }).getSiblings().forEach(FancyItemFrameEntity::removeClickEvents);
    }
    
    @Override
    protected void registerData() {
        super.registerData();
        
        this.dataTracker.register(VARIANT, 0);
    }
    
    public FrameType getFrameType() {
        return FrameType.byId(this.getVariantIndex());
    }
    
    public int getVariantIndex() {
        return this.dataTracker.get(VARIANT);
    }
    
    @Nullable
    @Override
    public ItemEntity entityDropItem(@Nonnull ItemStack stack, float offset) {
        if (stack.getItem() == Items.field_8143) {
            stack = new ItemStack(FrameType.getFrameFromType(this.getFrameType()));
        }
        return super.entityDropItem(stack, offset);
    }
    
    @Nonnull
    @Override
    public ItemStack getPickedResult(HitResult target) {
        ItemStack held = this.getDisplayedItem();
        if (held.isEmpty()) {
            return new ItemStack(FrameType.getFrameFromType(this.getFrameType()));
        } else {
            return held.copy();
        }
    }
    
    @Override
    public void writeAdditional(CompoundTag compound) {
        super.writeAdditional(compound);
        compound.putInt(TAG_VARIANT, this.getVariantIndex());
    }
    
    @Override
    public void readAdditional(CompoundTag compound) {
        super.readAdditional(compound);
        this.dataTracker.set(VARIANT, compound.getInt(TAG_VARIANT));
    }
    
    @Nonnull
    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    public void writeSpawnData(PacketByteBuf buffer) {
        buffer.writeVarInt(this.getVariantIndex());
        buffer.writeBlockPos(this.blockPos);
        buffer.writeVarInt(this.facing.getIndex());
    }
    
    @Override
    public void readSpawnData(PacketByteBuf buffer) {
        this.dataTracker.set(VARIANT, buffer.readVarInt());
        this.blockPos = buffer.readBlockPos();
        this.updateFacingWithBoundingBox(Direction.byIndex(buffer.readVarInt()));
    }
    
    @Override
    public Text getName() {
        Text itextcomponent = this.getCustomName();
        if (itextcomponent != null) {
            Text textComponent = itextcomponent.deepCopy();
            removeClickEvents(textComponent);
            return textComponent;
        } else {
            String translationKey = this.getType().getTranslationKey();
            
            return new TranslatableText(translationKey + "." + this.getFrameType().getName());
        }
    }
}
