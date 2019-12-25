package slimeknights.tconstruct.plugin.theoneprobe;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;

public class GetTheOneProbe implements Function<ITheOneProbe, Void> {

    @Nullable
    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(new CastingInfoProvider());
        probe.registerProvider(new ProgressInfoProvider());
        return null;
    }
}
