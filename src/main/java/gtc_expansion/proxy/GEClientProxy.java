package gtc_expansion.proxy;

import gtc_expansion.GEIcons;
import ic2.core.platform.textures.Ic2Icons;
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
}
