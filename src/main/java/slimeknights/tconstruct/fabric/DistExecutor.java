package slimeknights.tconstruct.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class DistExecutor {
    public static <T> T runForDist(Supplier<Callable<T>> client, Supplier<Callable<T>> server) {
        try {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
                return client.get().call();
            return server.get().call();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static void runWhenOn(EnvType env, Supplier<Runnable> runnableSupplier) {
        try {
            if (FabricLoader.getInstance().getEnvironmentType() == env)
                runnableSupplier.get().run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
