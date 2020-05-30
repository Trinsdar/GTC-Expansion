package gtc_expansion.proxy;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXIcons;
import gtc_expansion.GTCXItems;
import gtc_expansion.GTCXJei;
import gtc_expansion.entity.GTCXEntityElectricBoat;
import ic2.core.entity.render.RenderOldClassicBoat;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber({Side.CLIENT})
public class GTCXClientProxy extends GTCXCommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        GTCXItems.magicDye.initModel();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent e){
        super.init(e);
        RenderingRegistry.registerEntityRenderingHandler(GTCXEntityElectricBoat.class, new IRenderFactory() {
            public Render createRenderFor(RenderManager manager) {
                return new RenderOldClassicBoat(manager);
            }
        });
        GTCXJei.initJei();
    }

    @SubscribeEvent
    public void onIconLoad(Ic2Icons.SpriteReloadEvent event) {
        GTCXIcons.loadSprites();
    }

    @SubscribeEvent
    public static void onRegisterTexture(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/molten"));
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/moltenflowing"));;
    }
}
