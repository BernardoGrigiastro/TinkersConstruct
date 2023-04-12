package slimeknights.tconstruct;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.pulsar.control.PulseManager;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.common.ServerProxy;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.common.data.TConstructBlockTagsProvider;
import slimeknights.tconstruct.common.data.TConstructItemTagsProvider;
import slimeknights.tconstruct.common.data.TConstructLootTableProvider;
import slimeknights.tconstruct.common.data.TConstructRecipeProvider;
import slimeknights.tconstruct.fabric.DistExecutor;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.materials.MaterialManager;
import slimeknights.tconstruct.library.materials.client.MaterialRenderManager;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Random;

/**
 * TConstruct, the tool mod. Craft your tools with style, then modify until the original is gone!
 *
 * @author mDiyo
 */

public class TConstruct implements ModInitializer {
    
    public static final String modID = Util.MODID;
    
    public static final Logger log = LogManager.getLogger(modID);
    public static final Random random = new Random();
    
    /* Instance of this mod, used for grabbing prototype fields */
    public static TConstruct instance;
    
    public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    
    public static PulseManager pulseManager;
    
    public TConstruct() {
        instance = this;
    }
    
    @Override
    public void onInitialize() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    
        pulseManager = new PulseManager(Config.pulseConfig);
        pulseManager.registerPulse(new TinkerCommons());
        pulseManager.registerPulse(new TinkerFluids());
        pulseManager.registerPulse(new TinkerWorld());
        pulseManager.registerPulse(new TinkerGadgets());
        pulseManager.enablePulses();
    
        DistExecutor.runWhenOn(EnvType.CLIENT, () -> TinkerBook::initBook);
        DistExecutor.runWhenOn(EnvType.CLIENT, () -> () -> ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new MaterialRenderManager()));
    
        MinecraftForge.EVENT_BUS.register(this);
    
        preInit();
        init();
        postInit();
    }
    
    public static void preInit() {
        proxy.preInit();
        
        TinkerNetwork.instance.setup();
    }
    
    public static void init() {
        proxy.init();
    }
    
    public static void postInit() {
        proxy.postInit();
    }
    
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator datagenerator = event.getGenerator();
        
        if (event.includeServer()) {
            datagenerator.addProvider(new TConstructBlockTagsProvider(datagenerator));
            datagenerator.addProvider(new TConstructItemTagsProvider(datagenerator));
            datagenerator.addProvider(new TConstructLootTableProvider(datagenerator));
            datagenerator.addProvider(new TConstructRecipeProvider(datagenerator));
        }
    }
    
    @SubscribeEvent
    public void onServerAboutToStart(final FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(new MaterialManager());
    }
}
