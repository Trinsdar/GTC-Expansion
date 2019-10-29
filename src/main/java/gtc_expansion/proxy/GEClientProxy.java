package gtc_expansion.proxy;

import gtc_expansion.GEIcons;
import gtc_expansion.GTCExpansion;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber({Side.CLIENT})
public class GEClientProxy extends GECommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onIconLoad(Ic2Icons.SpriteReloadEvent event) {
        GEIcons.loadSprites();
    }

    @SubscribeEvent
    public static void onRegisterTexture(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/molten"));
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/moltenflowing"));;
    }
}
