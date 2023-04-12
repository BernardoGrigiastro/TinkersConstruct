package slimeknights.tconstruct.library.materials;

import com.google.common.collect.ImmutableList;
import slimeknights.tconstruct.library.Util;

import java.util.List;

public class BowStringMaterialStats extends AbstractMaterialStats {

    public final static String LOC_Multiplier = "stat.bowstring.modifier.name";

    public final static String LOC_MultiplierDesc = "stat.bowstring.modifier.desc";

    public final static String COLOR_Modifier = HandleMaterialStats.COLOR_Modifier;

    public final float modifier; // around 1.0

    public BowStringMaterialStats(float modifier) {
        super(MaterialTypes.BOWSTRING);
        this.modifier = modifier;
    }

    public static String formatModifier(float quality) {
        return formatNumber(LOC_Multiplier, COLOR_Modifier, quality);
    }

    @Override
    public List<String> getLocalizedInfo() {
        return ImmutableList.of(formatModifier(modifier));
    }

    @Override
    public List<String> getLocalizedDesc() {
        return ImmutableList.of(Util.translate(LOC_MultiplierDesc));
    }
}
