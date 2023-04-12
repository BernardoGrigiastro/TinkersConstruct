package slimeknights.tconstruct.tools.traits;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TraitHeavy extends AbstractTrait {

    protected static final UUID KNOCKBACK_MODIFIER = UUID.fromString("cca17597-84ae-44fe-bf98-ca08a9047079");

    public TraitHeavy() {
        super("heavy", 0xffffff);
    }

    @Override
    public void getAttributeModifiers(@Nonnull
            EntityEquipmentSlot slot, ItemStack stack, Multimap<String, EntityAttributeModifier> attributeMap) {
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            attributeMap.put(EntityAttributes.KNOCKBACK_RESISTANCE.getName(), new EntityAttributeModifier(KNOCKBACK_MODIFIER, "Knockback modifier", 1, 0));
        }
    }
}
