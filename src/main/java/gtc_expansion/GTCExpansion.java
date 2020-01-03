package gtc_expansion;

import gtc_expansion.events.GTCXOtherEvents;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.proxy.GTCXCommonProxy;
import ic2.core.IC2;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = GTCExpansion.MODID, name = GTCExpansion.MODNAME, version = GTCExpansion.MODVERSION, dependencies = GTCExpansion.DEPENDS, useMetadata = true)
public class GTCExpansion {

	public static final String MODID = "gtc_expansion";
	public static final String MODNAME = "GregTech Classic Expansion";
	public static final String MODVERSION = "@VERSION@";
	public static final String DEPENDS = "required-after:ic2;required-after:ic2-classic-spmod;required-after:gtclassic@[1.0.9,);after:twilightforest@[3.9.984,);after:ic2c_extras;after:gravisuit@[1.0.8.1,)";
	@SidedProxy(clientSide = MODID + ".proxy.GTCXClientProxy", serverSide = MODID + ".proxy.GTCXCommonProxy")
	public static GTCXCommonProxy proxy;
	@Mod.Instance
	public static GTCExpansion instance;
	public static Logger logger;

	static {
		GTCXMaterial.initMaterials();
	}

	public GTCExpansion(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public synchronized void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		MinecraftForge.EVENT_BUS.register(new GTCXOtherEvents());
		GameRegistry.registerWorldGenerator(new GTCXWorldGen(), 0);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		IC2.getInstance().saveRecipeInfo(IC2.configFolder);
	}

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MODID))
		{
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}
}
