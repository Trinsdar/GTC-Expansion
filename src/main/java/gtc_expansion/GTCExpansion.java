package gtc_expansion;

import gtc_expansion.events.GTCXOtherEvents;
import gtc_expansion.events.GTCXRadiationEvent;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.proxy.GTCXCommonProxy;
import gtclassic.api.helpers.GTValues;
import ic2.core.IC2;
import ic2.core.RotationList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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

import java.util.Calendar;

@Mod(modid = GTCExpansion.MODID, name = GTCExpansion.MODNAME, version = GTCExpansion.MODVERSION, dependencies = GTCExpansion.DEPENDS, useMetadata = true)
public class GTCExpansion {

	public static final String MODID = "gtc_expansion";
	public static final String MODNAME = "GTC Expansion";
	public static final String MODVERSION = "@VERSION@";
	public static final String DEPENDS = "required-after:ic2;required-after:ic2-classic-spmod;required-after:gtclassic@[1.1.3,);after:twilightforest@[3.9.984,);after:ic2c_extras@[1.4.4.12,);after:gravisuit@[1.0.8.1,)";
	@SidedProxy(clientSide = MODID + ".proxy.GTCXClientProxy", serverSide = MODID + ".proxy.GTCXCommonProxy")
	public static GTCXCommonProxy proxy;
	@Mod.Instance
	public static GTCExpansion instance;
	public static Logger logger;

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
//		for (int i = 0; i < 64; i++){
//			for (EnumFacing facing : EnumFacing.HORIZONTALS){
//				logger.info("Old way for facing " + facing.getName() + " and config " + i + " = " + getIndex(facing, i));
//				logger.info("New way for facing " + facing.getName() + " and config " + i + " = " + getIndexes(facing, i));
//			}
//		}
		proxy.preInit(event);
		Calendar calendar = Calendar.getInstance();
		aprilFirst = (calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1);
	}

	public int getIndex(EnumFacing textureFacing, int con){
		int id = 7;
		EnumFacing facing = EnumFacing.NORTH;
		if (between(60, 62, con) || or(con, 51, 59, 55, 15, 47,31)){ // has at least 4 blocks around
			id = 6;
		}
		int facingIndex = facing.getHorizontalIndex() != -1 ? facing.getHorizontalIndex() : 0;
		if (textureFacing.getAxis() == EnumFacing.Axis.Y){
			boolean u = textureFacing == EnumFacing.UP;
			if (between(4, 15, con)){
				id = facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0;
			}
			if (between(16, 19, con) || between(32, 35, con) || between(48, 51, con)){
				id = facing.getAxis() == EnumFacing.Axis.Z ? 0 : 1;
			}
			int[] array = { 2, 3, 4, 5};
			if (between(28, 31, con)){
				id = u ? array[rotateSubtract(2, facingIndex)] : array[rotateAdd(2, facingIndex)];
			}
			if (between(44, 47, con)){
				id = u ? array[rotateSubtract(0, facingIndex)] : array[rotateAdd(0, facingIndex)];
			}
			if (between(52, 55, con)){
				id = u ? array[rotateSubtract(3, facingIndex)] : array[rotateAdd(1, facingIndex)];
			}
			if (between(56, 59, con)){
				id = u ? array[rotateSubtract(1, facingIndex)] : array[rotateAdd(3, facingIndex)];
			}
			int[] array2 = {8, 9, 10, 11};
			if (between(20, 23, con)){
				id = u ? array2[rotateSubtract(2, facingIndex)] : array2[rotateAdd( 1, facingIndex)];
			}
			if (between(36, 39, con)){
				id = u ? array2[rotateSubtract(3, facingIndex)] : array2[rotateAdd( 0, facingIndex)];
			}
			if (between(40, 43, con)){
				id = u ? array2[rotateSubtract(0, facingIndex)] : array2[rotateAdd( 3, facingIndex)];
			}
			if (between(24, 27, con)){
				id = u ? array2[rotateSubtract(1, facingIndex)] : array2[rotateAdd( 2, facingIndex)];
			}
		} else {
			if (between(1, 3, con)){
				id = 1;
			}
			if (or(con, 14, 50, 30, 46, 58, 54)){
				id = 5;
			}
			if (or(con, 13, 49, 29, 45, 57, 53)){
				id = 3;
			}
			if (or(con, 12, 48, 20, 24, 36, 40)){
				id = 0;
			}
			if (textureFacing.getAxis() == EnumFacing.Axis.Z){
				int increase = textureFacing == EnumFacing.NORTH ? 0 : 1;
				if (or(con, 17, 21, 25)){
					id = 8 + increase;
				}
				if (or(con, 18, 22, 26)){
					id = 11 - increase;
				}
				if (or(con, 34, 38, 42)){
					id = 10 + increase;
				}
				if (or(con,33, 37, 41)){
					id = 9 - increase;
				}
				if (or(con, 35, 39, 43)){
					id = textureFacing == EnumFacing.NORTH ? 4 : 2;
				}
				if (or(con, 19, 23, 27)){
					id = textureFacing == EnumFacing.NORTH ? 2 : 4;
				}
				if (between(9, 11, con) || between(5, 7, con)){
					id = 1;
				}
				if (or(con,16, 32, 52, 56)){
					id = 0;
				}
			} else {
				int increase = textureFacing == EnumFacing.EAST ? 0 : 1;
				if (or(con, 10, 26, 42)){
					id = 10 + increase;
				}
				if (or(con, 6, 22, 38)){
					id = 11 - increase;
				}
				if (or(con, 9, 25, 41)){
					id = 9 - increase;
				}
				if (or(con, 5, 21, 37)){
					id = 8 + increase;
				}
				if (or(con, 7, 23, 39)){
					id = textureFacing == EnumFacing.WEST ? 4 : 2;
				}
				if (or(con, 11, 27, 43)){
					id = textureFacing == EnumFacing.WEST ? 2 : 4;
				}
				if (between(17, 19, con) || between(33, 35, con)){
					id = 1;
				}
				if (or(con, 4, 8,28, 44)){
					id = 0;
				}
			}
		}
		return id;
	}

	public int rotateAdd(int indexStart, int addition){
		return (indexStart + addition) % 4;
	}

	public int rotateSubtract(int indexStart, int subtraction){
		int index = (indexStart - subtraction);
		return index < 0 ? 4 + index : index;
	}

	public boolean or(int compare, int... values){
		for (int i : values){
			if (compare == i){
				return true;
			}
		}
		return false;
	}

	public boolean between(int min, int max, int compare){
		return compare >= min && compare <= max;
	}

	public int getIndexes(EnumFacing textureFacing, int con)
	{
		RotationList list = RotationList.ofNumber(con).remove(textureFacing).remove(textureFacing.getOpposite());
		if(list.size() == 0 || list.size() == 4)
		{
			return list.size() == 4 ? 6 : 7;
		}
		int index = 0;
		int result = 0;
		for(EnumFacing facing : list)
		{
			result += (1 << (index++ * 2)) + convert(textureFacing, facing) & 3;
		}
		return result;
	}

	protected int convert(EnumFacing side, EnumFacing index)
	{
		switch(side.getAxis())
		{
			case X:
				switch(index)
				{
					case DOWN: return 0;
					case UP: return 1;
					case NORTH: return 2;
					case SOUTH: return 3;
					default: return -1;
				}
			case Y:
				switch(index)
				{
					case WEST: return 0;
					case EAST: return 1;
					case NORTH: return 2;
					case SOUTH: return 3;
					default: return -1;
				}
			case Z:
				switch(index)
				{
					case DOWN: return 0;
					case UP: return 1;
					case WEST: return 2;
					case EAST: return 3;
					default: return -1;
				}
		}
		return 0;
	}

	public static ResourceLocation getAprilFirstSound(ResourceLocation original){
		return aprilFirst ? cowMoo : original;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		MinecraftForge.EVENT_BUS.register(new GTCXOtherEvents());
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
