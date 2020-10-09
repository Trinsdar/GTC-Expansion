package gtc_expansion;

import gtc_expansion.events.GTCXOtherEvents;
import gtc_expansion.events.GTCXRadiationEvent;
import gtc_expansion.events.GTCXServerTickEvent;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.oneprobe.GTCXOneProbePlugin;
import gtc_expansion.proxy.GTCXCommonProxy;
import gtclassic.api.helpers.GTValues;
import ic2.core.IC2;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

@Mod(modid = GTCExpansion.MODID, name = GTCExpansion.MODNAME, version = GTCExpansion.MODVERSION, dependencies = GTCExpansion.DEPENDS, useMetadata = true)
public class GTCExpansion {

	public static final String MODID = "gtc_expansion";
	public static final String MODNAME = "GTC Expansion";
	public static final String MODVERSION = "@VERSION@";
	public static final String DEPENDS = "required-after:ic2;required-after:ic2-classic-spmod;required-after:gtclassic@[1.1.6,);after:twilightforest@[3.9.984,);after:ic2c_extras@[1.4.4.12,);after:gravisuit@[1.0.8.1,)";
	@SidedProxy(clientSide = MODID + ".proxy.GTCXClientProxy", serverSide = MODID + ".proxy.GTCXCommonProxy")
	public static GTCXCommonProxy proxy;
	@Mod.Instance
	public static GTCExpansion instance;
	public static Logger logger;

	public int counter = 0;

	public ArrayList<BlockPos> wrenchMap = new ArrayList<>();

	public static boolean firstLoad = false;

	static {
		GTCXMaterial.initMaterials();
	}

	public static ResourceLocation cowMoo = new ResourceLocation(MODID, "sounds/cow_moo-sound.ogg");
	private static boolean aprilFirst;

	public GTCExpansion(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public synchronized void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
		GTCXOneProbePlugin.init();
		Calendar calendar = Calendar.getInstance();
		aprilFirst = (calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1);
		File directory = event.getModConfigurationDirectory();
		File dat = new File(directory.getPath(),"ic2/first_load.dat");
		if (!dat.exists()){
			firstLoad = true;
			try {
				dat.createNewFile();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public static ResourceLocation getAprilFirstSound(ResourceLocation original){
		return aprilFirst ? cowMoo : original;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		MinecraftForge.EVENT_BUS.register(new GTCXOtherEvents());
		MinecraftForge.EVENT_BUS.register(new GTCXServerTickEvent());
		//MultiBlockHelper.INSTANCE.init();
		GTCXRadiationEvent.init();
		if (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTCXConfiguration.general.enableRadiation){
			MinecraftForge.EVENT_BUS.register(new GTCXRadiationEvent());
		}
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
