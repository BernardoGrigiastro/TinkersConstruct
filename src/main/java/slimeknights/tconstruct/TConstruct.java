package slimeknights.tconstruct;

import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.IReloadableResourceManager;
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
import slimeknights.mantle.util.BlankBlockDropJsonGenerator;
import slimeknights.mantle.util.BlockStateJsonGenerator;
import slimeknights.mantle.util.LanguageJsonGenerator;
import slimeknights.mantle.util.ModelJsonGenerator;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.common.ServerProxy;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.common.data.TConstructBlockTagsProvider;
import slimeknights.tconstruct.common.data.TConstructItemTagsProvider;
import slimeknights.tconstruct.common.data.TConstructLootTableProvider;
import slimeknights.tconstruct.common.data.TConstructRecipeProvider;
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

@Mod(TConstruct.modID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TConstruct {

  public static final String modID = Util.MODID;

  public static final Logger log = LogManager.getLogger(modID);
  public static final Random random = new Random();

  /* Instance of this mod, used for grabbing prototype fields */
  public static TConstruct instance;

  public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

  public static PulseManager pulseManager;

  public TConstruct() {
    instance = this;

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);

    pulseManager = new PulseManager(Config.pulseConfig);
    pulseManager.registerPulse(new TinkerCommons());
    pulseManager.registerPulse(new TinkerFluids());
    pulseManager.registerPulse(new TinkerWorld());
    pulseManager.registerPulse(new TinkerGadgets());
    pulseManager.enablePulses();

    DistExecutor.runWhenOn(Dist.CLIENT, () -> TinkerBook::initBook);
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new MaterialRenderManager()));

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public static void preInit(final FMLCommonSetupEvent event) {
    proxy.preInit();

    TinkerNetwork.instance.setup();
  }

  @SubscribeEvent
  public static void init(final InterModEnqueueEvent event) {
    proxy.init();
  }

  @SubscribeEvent
  public static void postInit(final InterModProcessEvent event) {
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

      if (false) {
        datagenerator.addProvider(new BlockStateJsonGenerator(datagenerator, modID));
        datagenerator.addProvider(new ModelJsonGenerator(datagenerator, modID));
        datagenerator.addProvider(new LanguageJsonGenerator(datagenerator, modID));
        datagenerator.addProvider(new BlankBlockDropJsonGenerator(datagenerator, modID));
      }
    }
  }

  @SubscribeEvent
  public void onServerAboutToStart(final FMLServerAboutToStartEvent event) {
    event.getServer().getResourceManager().addReloadListener(new MaterialManager());
  }
}
