package slimeknights.tconstruct.common.conditions;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import slimeknights.tconstruct.TConstruct;

public class PulseLoadedCondition implements ICondition {
    
    private static final Identifier NAME = new Identifier("tconstruct", "pulse_loaded");
    private final String pulseId;
    
    public PulseLoadedCondition(String pulseId) {
        this.pulseId = pulseId;
    }
    
    @Override
    public Identifier getID() {
        return NAME;
    }
    
    @Override
    public boolean test() {
        return TConstruct.pulseManager.isPulseLoaded(this.pulseId);
    }
    
    @Override
    public String toString() {
        return "pulse_loaded(\"" + this.pulseId + "\")";
    }
    
    public static class Serializer implements IConditionSerializer<PulseLoadedCondition> {
        
        public static final Serializer INSTANCE = new Serializer();
        
        @Override
        public void write(JsonObject json, PulseLoadedCondition value) {
            json.addProperty("pulseid", value.pulseId);
        }
        
        @Override
        public PulseLoadedCondition read(JsonObject json) {
            return new PulseLoadedCondition(JsonHelper.getString(json, "pulseid"));
        }
        
        @Override
        public Identifier getID() {
            return PulseLoadedCondition.NAME;
        }
    }
}