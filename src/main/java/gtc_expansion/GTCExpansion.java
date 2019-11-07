package gtc_expansion;

import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.proxy.GECommonProxy;
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
	public static final String DEPENDS = "required-after:ic2;required-after:ic2-classic-spmod;required-after:gtclassic@[1.0.6,);after:twilightforest@[3.9.984,);after:ic2c_extras";
	@SidedProxy(clientSide = MODID + ".proxy.GEClientProxy", serverSide = MODID + ".proxy.GECommonProxy")
	public static GECommonProxy proxy;
	@Mod.Instance
	public static GTCExpansion instance;
	public static Logger logger;

	static {
		GEMaterial.initMaterials();
		GEMaterialGen.init();
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
		MinecraftForge.EVENT_BUS.register(new GEEvents());
		GameRegistry.registerWorldGenerator(new GEWorldGen(), 0);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
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
