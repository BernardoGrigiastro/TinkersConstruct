package slimeknights.tconstruct.common;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.mantle.network.AbstractPacket;
import slimeknights.tconstruct.fabric.TinkerItemGroup;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.client.CustomFontRenderer;
import slimeknights.tconstruct.library.client.CustomTextureCreator;
import slimeknights.tconstruct.library.client.crosshair.CrosshairRenderEvents;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.client.material.deserializers.*;
import slimeknights.tconstruct.library.client.model.MaterialModelLoader;
import slimeknights.tconstruct.library.client.model.ModifierModelLoader;
import slimeknights.tconstruct.library.client.model.ToolModelLoader;
import slimeknights.tconstruct.library.client.particle.EntitySlimeFx;
import slimeknights.tconstruct.library.client.particle.Particles;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialGUI;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.client.ParticleEffect;
import slimeknights.tconstruct.shared.client.ParticleEndspeed;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.common.client.particle.*;
import slimeknights.tconstruct.tools.harvest.TinkerHarvestTools;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public abstract class ClientProxy extends CommonProxy {
    
    public static final Identifier BOOK_MODIFY = Util.getResource("textures/gui/book/modify.png");
    protected static final ToolModelLoader loader = new ToolModelLoader();
    protected static final MaterialModelLoader materialLoader = new MaterialModelLoader();
    protected static final ModifierModelLoader modifierLoader = new ModifierModelLoader();
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Material RenderMaterials[];
    public static Material RenderMaterialString;
    public static CustomFontRenderer fontRenderer;
    
    public static void initClient() {
        // i wonder if this is OK :D
        ModelLoaderRegistry.registerLoader(loader);
        ModelLoaderRegistry.registerLoader(materialLoader);
        ModelLoaderRegistry.registerLoader(modifierLoader);
        
        MaterialRenderInfoLoader.addRenderInfo("colored", ColoredRenderInfoDeserializer.class);
        MaterialRenderInfoLoader.addRenderInfo("multicolor", MultiColorRenderInfoDeserializer.class);
        MaterialRenderInfoLoader.addRenderInfo("inverse_multicolor", InverseMultiColorRenderInfoDeserializer.class);
        MaterialRenderInfoLoader.addRenderInfo("metal", MetalRenderInfoDeserializer.class);
        MaterialRenderInfoLoader.addRenderInfo("metal_textured", TexturedMetalRenderInfoDeserializer.class);
        MaterialRenderInfoLoader.addRenderInfo("block", BlockRenderInfoDeserializer.class);
        
        MinecraftForge.EVENT_BUS.register(CustomTextureCreator.INSTANCE);
    }
    
    public static void initRenderMaterials() {
        RenderMaterials = new Material[4];
        RenderMaterials[0] = new MaterialGUI("_internal_render1");
        RenderMaterials[0].setRenderInfo(0x684e1e);
        RenderMaterials[1] = new MaterialGUI("_internal_render2");
        RenderMaterials[1].setRenderInfo(0xc1c1c1);
        RenderMaterials[2] = new MaterialGUI("_internal_render3");
        RenderMaterials[2].setRenderInfo(0x2376dd);
        RenderMaterials[3] = new MaterialGUI("_internal_render4");
        RenderMaterials[3].setRenderInfo(0x7146b0);
        
        RenderMaterialString = new MaterialGUI("_internal_renderString");
        RenderMaterialString.setRenderInfo(0xffffff);
        
        // yes, these will only be registered clientside
        // but it shouldn't matter because they're never used serverside and we don't use indices
        Stream.concat(Stream.of(RenderMaterials), Stream.of(RenderMaterialString)).forEach(TinkerRegistry::addMaterial);
    }
    
    public static void initRenderer() {
        if (TinkerHarvestTools.pickaxe != null) {
            ((TinkerItemGroup)TinkerRegistry.tabTools).setIcon(TinkerHarvestTools.pickaxe.buildItemForRendering(ImmutableList.of(RenderMaterials[0], RenderMaterials[1], RenderMaterials[2])));
        }
        if (TinkerTools.pickHead != null) {
            ((TinkerItemGroup)TinkerRegistry.tabParts).setIcon(TinkerTools.pickHead.getItemstackWithMaterial(RenderMaterials[2]));
        }
    
        ReloadableResourceManager resourceManager = (ReloadableResourceManager) mc.getResourceManager();
        resourceManager.registerListener(MaterialRenderInfoLoader.INSTANCE);
        resourceManager.registerListener(CustomTextureCreator.INSTANCE);
        
        // Font renderer for tooltips and GUIs
        fontRenderer = new CustomFontRenderer(mc.options, new Identifier("textures/font/ascii.png"), mc.textureManager);
        if (mc.options.language != null) {
            fontRenderer.setUnicodeFlag(mc.getLanguageManager().isCurrentLocaleUnicode() || mc.options.forceUnicodeFont);
            fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
        resourceManager.registerReloadListener(fontRenderer);
        
        
        // Font Renderer for the tinker books
        TextRenderer bookRenderer = new CustomFontRenderer(mc.options, new Identifier("textures/font/ascii.png"), mc.textureManager);
        bookRenderer.setUnicodeFlag(true);
        if (mc.options.language != null) {
            fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
        TinkerBook.INSTANCE.fontRenderer = bookRenderer;
        
        MinecraftForge.EVENT_BUS.register(CrosshairRenderEvents.INSTANCE);
    }
    
    public static Particle createParticle(Particles type, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... data) {
        switch (type) {
            // entities
            case BLUE_SLIME:
                return new EntitySlimeFx(world, x, y, z, TinkerCommons.matSlimeBallBlue.getItem(), TinkerCommons.matSlimeBallBlue.getItemDamage());
            // attack
            case CLEAVER_ATTACK:
                return new ParticleAttackCleaver(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case LONGSWORD_ATTACK:
                return new ParticleAttackLongsword(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case RAPIER_ATTACK:
                return new ParticleAttackRapier(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case HATCHET_ATTACK:
                return new ParticleAttackHatchet(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case LUMBERAXE_ATTACK:
                return new ParticleAttackLumberAxe(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case FRYPAN_ATTACK:
                return new ParticleAttackFrypan(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            case HAMMER_ATTACK:
                return new ParticleAttackHammer(world, x, y, z, xSpeed, ySpeed, zSpeed, mc.getTextureManager());
            // effects
            case EFFECT:
                return new ParticleEffect(data[1], world, x, y, z, xSpeed, ySpeed, zSpeed);
            case ENDSPEED:
                return new ParticleEndspeed(world, x, y, z, xSpeed, ySpeed, zSpeed);
        }
        
        return null;
    }
    
    /**
     * Register with name only, defaults to TiC domain
     */
    protected void registerItemModelTiC(ItemStack item, String name) {
        if (item != null && !ChatUtil.isNullOrEmpty(name)) {
            ModelRegisterUtil.registerItemModel(item, Util.getResource(name));
        }
    }
    
    @Override
    public void sendPacketToServerOnly(AbstractPacket packet) {
        TinkerNetwork.sendToServer(packet);
    }
    
    @Override
    public void spawnParticle(Particles particleType, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... data) {
        if (world == null) {
            world = mc.world;
        }
        Particle effect = createParticle(particleType, world, x, y, z, xSpeed, ySpeed, zSpeed, data);
        mc.particleManager.addEffect(effect);
        
        if (particleType == Particles.EFFECT && data[0] > 1) {
            for (int i = 0; i < data[0] - 1; i++) {
                effect = createParticle(particleType, world, x, y, z, xSpeed, ySpeed, zSpeed, data);
                mc.particleManager.addEffect(effect);
            }
        }
    }
    
    @Override
    public void spawnSlimeParticle(World world, double x, double y, double z) {
        mc.particleManager.addParticle(new EntitySlimeFx(world, x, y, z, TinkerCommons.matSlimeBallBlue.getItem(), TinkerCommons.matSlimeBallBlue.getDamage()));
    }
    
    @Override
    public void preventPlayerSlowdown(Entity player, float originalSpeed, Item item) {
        // has to be done in onUpdate because onTickUsing is too early and gets overwritten. bleh.
        if (player instanceof ClientPlayerEntity) {
            ClientPlayerEntity playerSP = (ClientPlayerEntity) player;
            ItemStack usingItem = playerSP.getActiveItem();
            if (!usingItem.isEmpty() && usingItem.getItem() == item) {
                // no slowdown from charging it up
                playerSP.input.movementForward *= originalSpeed * 5.0F;
                playerSP.input.movementSideways *= originalSpeed * 5.0F;
            }
        }
    }
    
    @Override
    public void customExplosion(World world, Explosion explosion) {
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }
    
    @Override
    public void updateEquippedItemForRendering(Hand hand) {
        mc.getItemRenderer().resetEquippedProgress(hand);
        mc.getItemRenderer().updateEquippedItem();
    }
    
    public static class PatternMeshDefinition implements ItemMeshDefinition {
        
        private final Identifier baseLocation;
        
        public PatternMeshDefinition(Identifier baseLocation) {
            this.baseLocation = baseLocation;
        }
        
        @Nonnull
        @Override
        public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
            Item item = Pattern.getPartFromTag(stack);
            String suffix = "";
            if (item != null) {
                suffix = Pattern.getTextureIdentifier(item);
            }
            
            return new ModelResourceLocation(new Identifier(baseLocation.getResourceDomain(), baseLocation.getResourcePath() + suffix), "inventory");
        }
    }
}
