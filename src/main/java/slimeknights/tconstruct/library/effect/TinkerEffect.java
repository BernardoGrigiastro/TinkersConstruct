package slimeknights.tconstruct.library.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;

public class TinkerEffect extends StatusEffect {
    
    private final boolean show;
    
    public TinkerEffect(Identifier location, StatusEffectType typeIn, boolean showInInventory) {
        this(location, typeIn, showInInventory, 0xffffff);
    }
    
    public TinkerEffect(Identifier location, StatusEffectType typeIn, boolean showInInventory, int color) {
        super(typeIn, color);
        
        this.setRegistryName(location);
        
        this.show = showInInventory;
    }
    
    @Override
    public boolean shouldRenderInvText(StatusEffectInstance effect) {
        return this.show;
    }
    
    public StatusEffectInstance apply(LivingEntity entity, int duration) {
        return this.apply(entity, duration, 0);
    }
    
    public StatusEffectInstance apply(LivingEntity entity, int duration, int level) {
        StatusEffectInstance effect = new StatusEffectInstance(this, duration, level, false, false);
        entity.addPotionEffect(effect);
        return effect;
    }
    
    public int getLevel(LivingEntity entity) {
        StatusEffectInstance effect = entity.getActivePotionEffect(this);
        if (effect != null) {
            return effect.getAmplifier();
        }
        return 0;
    }
    
    @Override
    public boolean shouldRender(StatusEffectInstance effect) {
        return this.show;
    }
    
}
