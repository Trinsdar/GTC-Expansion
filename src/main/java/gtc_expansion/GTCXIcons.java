package gtc_expansion;

import gtc_expansion.data.GTCXBlocks;
import gtclassic.common.GTConfig;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Sprites;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static ic2.core.platform.textures.Ic2Icons.addCustomTexture;
import static ic2.core.platform.textures.Ic2Icons.addSprite;
import static ic2.core.platform.textures.Ic2Icons.addTextureEntry;

public class GTCXIcons {
    private static final Map<Block, GTCXIconInfo[]> TEXTURE_MAP = new HashMap<>();
    private static final GTCXIconInfo[] SET_NULL = { new GTCXIconInfo(111), new GTCXIconInfo(111) };

    @SideOnly(Side.CLIENT)
    public static void loadSprites() {
        makeSprite("blocks", 16, 2);
        makeSprite("connected_blocks", 16, 6);
        makeSprite("materials", 16, 4);
        makeSprite("items", 16, 6);
        makeSprite("crops", 7, 1);
        makeSprite("tiles", 16, 16);
        if (GTConfig.general.animatedTextures){
            addCustomTexture(GTCExpansion.MODID + "_tiles", 0, 2, location("bf_front"));
            addCustomTexture(GTCExpansion.MODID + "_tiles", 12, 0, location("diesel_generator_top"));
            addCustomTexture(GTCExpansion.MODID + "_tiles", 11, 1, location("gas_turbine_top"));
            addCustomTexture(GTCExpansion.MODID + "_tiles", 7, 1, location("fluid_caster_front_active"));
            collectTurbineSprites(true);
            collectTurbineSprites(false);
        }
        if (GTConfig.general.debugMode){
            GTCExpansion.logger.info("All GregTech Classic Expansion textures generated without error");
        }
        setTexture(GTCXBlocks.advancedWorktable, s(0,0), s(2, 0), s(1,0), s(1,0), s(1,0), s(1,0), s(0,0), s(3,0), s(1,0), s(1,0), s(1,0), s(1,0));
        GTCXIconInfo t1 = s(9,5);
        setTexture(GTCXBlocks.alloyFurnace, t1, t1, t1, s(7,5), t1, t1, t1, t1, t1, s(8,5), t1, t1);
        t1 = s(98);
        setTexture(GTCXBlocks.alloySmelter, s(0), s(1), t1, s(4,0), s(2), s(2), s(0), s(1), t1, s(5,0), s(2), s(2));
        setTexture(GTCXBlocks.fluidSmelter, s(0), s(1), t1, s(8,1), s(2), s(2), s(0), s(1), t1, s(9,1), s(2), s(2));
        setTexture(GTCXBlocks.fluidCaster, s(0), s(1), t1, s(6,1), s(2), s(2), s(0), s(1), t1, s(7,1), s(2), s(2));
        setTexture(GTCXBlocks.assemblingMachine, s(0), s(6,0), t1, s(22), s(2), s(2), s(0), s(6,0), t1, s(23), s(2), s(2));
        setTexture(GTCXBlocks.chemicalReactor, s(0), s(1), s(9,0), s(9,0), s(9,0), s(9,0), s(0), s(1), s(10,0), s(10,0), s(10,0), s(10,0));
        setTexture(GTCXBlocks.dieselGenerator, s(0), s(11,0), t1, t1, t1, t1, s(0), s(12,0), t1, t1, t1, t1);
        t1 = s(13,0);
        GTCXIconInfo iS = s(4,2), iF = s(15,1), iFA = s(0,2);
        setTexture(GTCXBlocks.distillationTower, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        setTexture(GTCXBlocks.dustBin, s(14,0), s(0,1), s(2), s(15,0), s(2), s(2));
        setTexture(GTCXBlocks.electricLocker, s(0), s(1), s(2), s(1,1), s(2), s(2));
        setTexture(GTCXBlocks.gasTurbine, s(0), s(10,1), s(2), s(2), s(2), s(2), s(0), s(11,1), s(2), s(2), s(2), s(2));
        t1 = s(12,1); GTCXIconInfo t2 = s(13,1);
        setTexture(GTCXBlocks.implosionCompressor, iS, iF, t1, t1, t2, t2, iS, iFA, t1, t1, t2, t2);
        t1 = s(14,1);
        setTexture(GTCXBlocks.industrialBlastFurnace, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        setTexture(GTCXBlocks.electrolyzer, s(0), s(1), s(2,1), s(2,1), s(2,1), s(2,1), s(0), s(1), s(3,1), s(3,1), s(3,1), s(3,1));
        t1 = s(1,2);
        setTexture(GTCXBlocks.industrialGrinder, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        t1 = s(98);
        setTexture(GTCXBlocks.lathe, s(0), s(1), t1, s(5,2), s(2), s(2), s(0), s(1), t1, s(6,2), s(2), s(2));
        setTexture(GTCXBlocks.locker, s(0), s(1), s(2), s(7,2), s(2), s(2));
        setTexture(GTCXBlocks.microwave, s(0), s(1), t1, s(12,2), s(2), s(2), s(0), s(1), t1, s(13,2), s(2), s(2));
        setTexture(GTCXBlocks.plateBender, s(0), s(1), t1, s(14,2), s(2), s(2), s(0), s(1), t1, s(15,2), s(2), s(2));
        setTexture(GTCXBlocks.plateCutter, s(0), s(1), t1, s(0,3), s(2), s(2), s(0), s(1), t1, s(1,3), s(2), s(2));
        t2 = s(GTCExpansion.MODID + "_blocks", 1);
        setTexture(GTCXBlocks.primitiveBlastFurnace, t2, t2, t2, s(12,5), t2, t2, t2, t2, t2, s(13,5), t2, t2);
        t2 = s(5,3);
        setTexture(GTCXBlocks.stoneCompressor, s("bmach_lv", 15), s(2,5), s("bmach_lv", 47), s(0,5), s("bmach_lv", 47), s("bmach_lv", 47), s("bmach_lv", 15), s(2,5), s("bmach_lv", 47), s(1,5), s("bmach_lv", 47), s("bmach_lv", 47));
        setTexture(GTCXBlocks.stoneExtractor, s("bmach_lv", 15), s(2,5), s(5,5), s(3,5), s(5,5), s(5,5), s("bmach_lv", 15), s(2,5), s(6,5), s(4,5), s(6,5), s(6,5));
        setTexture(GTCXBlocks.vacuumFreezer, iS, iF, t2, t2, t2, t2, iS, iFA, t2, t2, t2, t2);
        setTexture(GTCXBlocks.wiremill, s(0), s(6,3), t1, s(22), s(2), s(2), s(0), s(7,3), t1, s(23), s(2), s(2));
        setTexture(GTCXBlocks.fusionMaterialInjector, s(42), s(42), s(42), s(39), s(42), s(42));
        setTexture(GTCXBlocks.fusionEnergyInjector, s(42), s(42), s(42), s(42), s(42), s(42));
        setTexture(GTCXBlocks.fusionMaterialExtractor, s(39), s(39), s(39), s(42), s(39), s(39));
        setTexture(GTCXBlocks.fusionEnergyExtractor, s(39), s(39), s(39), s(41), s(39), s(39));
        setTexture(GTCXBlocks.thermalBoiler, s(0), s(1), s(2), s(2,3), s(2), s(2), s(0), s(1), s(2), s(3,3), s(2), s(2));
        setTexture(GTCXBlocks.largeSteamTurbine, s(0), s(1), s(2), s(1,12), s(2), s(2), s(0), s(1), s(2), s(4,12), s(2), s(2));
        setTexture(GTCXBlocks.largeGasTurbine, s(0), s(1), s(2), s(1,9), s(2), s(2), s(0), s(1), s(2), s(4,9), s(2), s(2));
        setTexture(GTCXBlocks.fusionComputer, s(42), s(42), s(42), s(47), s(42), s(42), s(43), s(43), s(43), s(47), s(43), s(43));
        setTexture(GTCXBlocks.trashBin, s(0), s(97), s(4,3), s(4,3), s(4,3), s(4,3));
        setTexture(GTCXBlocks.extruder, s(0), s(21), s(98), s(4,1), s(9,2), s(9,2), s(0), s(22), s(98), s(5,1), s(11,2), s(11,2));
        setTexture(GTCXBlocks.industrialSawmill, s(4,2), s(15,1), s(3,2), s(3,2), s(2,2), s(2,2), s(4,2), s(0,2), s(3,2), s(3,2), s(2,2), s(2,2));
        setTexture(GTCXBlocks.centrifuge, s(0), s(10), s(12), s(12), s(12), s(12), s(0), s(11), s(13), s(13), s(13), s(13));
        setTexture(GTCXBlocks.bath, s(0), s(1), s(98), s(7,0), s(9,2), s(9,2), s(0), s(1), s(98), s(8,0), s(10,2), s(10,2));
        setTexture(GTCXBlocks.digitalTank, s(96), s(97), s(98), s(64), s(98), s(98));
        t2 = s(GTCExpansion.MODID + "_blocks", 1);
        setTexture(GTCXBlocks.cokeOven, t2, t2, t2, s(10,5), t2, t2, t2, t2, t2, s(11,5), t2, t2);
        setTexture(GTCXBlocks.steamCompressor, s(7,6), s(12,6), s(9,7), s(10,6), s(8,7), s(8,7), s(7,6), s(12,6), s(9,7), s(11,6), s(8,7), s(8,7));
        setTexture(GTCXBlocks.coalBoiler, s(9, 6), s(6, 6), s(5,6), s(3,6), s(5,6), s(5,6), s(9, 6), s(6, 6), s(5,6), s(4,6), s(5,6), s(5,6));
        setTexture(GTCXBlocks.steamMacerator, s(7,6), s(6,7), s(9,7), s(4,7), s(8,7), s(8,7), s(7,6), s(7,7), s(9,7), s(5,7), s(8,7), s(8,7));
        setTexture(GTCXBlocks.steamExtractor, s(7,6), s(10,7), s(9,7), s(13,6), s(8,7), s(8,7), s(7,6), s(10,7), s(9,7), s(14,6), s(8,7), s(8,7));
        setTexture(GTCXBlocks.steamFurnace, s(9,6), s(10,7), s(9,7), s(0,7), s(2,6), s(2,6), s(9,6), s(10,7), s(9,7), s(1,7), s(2,6), s(2,6));
        setTexture(GTCXBlocks.steamAlloySmelter, s(9,6), s(10,7), s(9,7), s(0,6), s(2,6), s(2,6), s(9,6), s(10,7), s(9,7), s(1,6), s(2,6), s(2,6));
        setTexture(GTCXBlocks.steamForgeHammer, s(7,6), s(10,7), s(9,7), s(2,7), s(8,7), s(8,7), s(7,6), s(10,7), s(9,7), s(3,7), s(8,7), s(8,7));
    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation("gtc_expansion", "animations/" + name);
    }

    public static void makeSprite(String name, int maxX, int maxY){
        addSprite(new Sprites.SpriteData(GTCExpansion.MODID + "_" + name, GTCExpansion.MODID
                + ":textures/sprites/sprites_" + name + ".png", new Sprites.SpriteInfo(maxX, maxY)));
        addTextureEntry(new Sprites.TextureEntry(GTCExpansion.MODID + "_" + name, 0, 0, maxX, maxY));
    }

    public static void collectBasicTileSprites() {
        String[] strings = GTCXBlocks.textureTileBasic;
        int length = strings.length;

        for (String string : strings) {
            if (GTConfig.general.debugMode) {
                GTCExpansion.logger.info("Attempting to get sprite data for: " + string);
            }
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/tiles/" + string + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 1));
        }

    }

    public static void collectBasicBronzeTileSprites() {
        String[] strings = new String[]{"bronze_boiler_front", "bronze_boiler_front_active", "bronze_boiler_side", "bronze_boiler_top", "bronze_bottom", "bronze_bottom_pipe", "bronze_brick_bottom", "bronze_side", "bronze_alloysmelter_front", "bronze_alloysmelter_front_active", "bronze_alloysmelter_side", "bronze_compressor_front", "bronze_compressor_front_active", "bronze_compressor_top", "bronze_extractor_front", "bronze_extractor_front_active", "bronze_furnace_front", "bronze_furnace_front_active", "bronze_furnace_side", "bronze_hammer_front", "bronze_hammer_front_active", "bronze_macerator_front", "bronze_macerator_front_active", "bronze_side_pipe", "bronze_top", "bronze_macerator_top", "bronze_macerator_top_active"};
        int length = strings.length;

        for (String string : strings) {
            if (GTConfig.general.debugMode) {
                GTCExpansion.logger.info("Attempting to get sprite data for: " + string);
            }
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/tiles/bronze/" + string + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 1));
        }

    }

    public static void collectCoverSprites(){
        String[] covers = { "", "conveyor", "drain", "itemvalve", "valve", "shutter", "redstone_controller", "filter"};
        for (String cover : covers){
            String under = cover.isEmpty() ? "" : "_";
            Ic2Icons.addSprite(new Sprites.SpriteData("cover" + under + cover, "gtc_expansion:textures/sprites/tiles/covers/"  + "cover" + under + cover + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry("cover" + under + cover, 0, 0, 1, 1));
        }
    }

    public static void collectTurbineSprites(boolean isSteam){
    	String[][] array = {{"top_left", "top", "top_right"}, {"left", "center", "right"}, {"bottom_left", "bottom", "bottom_right"}};
    	int yOffset = isSteam ? 3 : 0;
    	String steam = isSteam ? "steam" : "gas";
    	for (int y = 0; y < array.length; y++) {
    		String[] subArray = array[y];
    		for (int x = 0; x < subArray.length; x++) {
    			String string = subArray[x];
    			String active = steam + "_turbine_front_active_" + string;
    			addCustomTexture(GTCExpansion.MODID + "_tiles", 3 + x, 8 + y + yOffset, location(active));
    			
    		}
    		
    	}
    }

    /**
     * Getting a dynamically generated sprite array for a block
     *
     * @param block - the block to get sprite data for
     * @return - will return the sprite sheet if present or missing gtc texture (set
     *         null)
     */
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite[] getTextureData(Block block) {
        return TEXTURE_MAP.containsKey(block) ? textureHelper(TEXTURE_MAP.get(block)) : textureHelper(SET_NULL);
    }

    /**
     *
     * @param block  to make textures for
     * @param values the spirte locations for the block texture
     */
    @SideOnly(Side.CLIENT)
    private static void setTexture(Block block, GTCXIconInfo... values) {
        TEXTURE_MAP.put(block, values);
    }

    /**
     *
     * @param arr size determines style of texture, 2 = off/on all sides, 6 = all
     *            side but single state, 12 = all sides full state
     * @return the constructed sprite
     */
    @SideOnly(Side.CLIENT)
    private static TextureAtlasSprite[] textureHelper(GTCXIconInfo[] arr) {
        if (arr.length == 2) {
            return buildTexture(arr[0], arr[0], arr[0], arr[0], arr[0], arr[0], arr[1], arr[1], arr[1], arr[1], arr[1], arr[1]);
        }
        if (arr.length == 6) {
            return buildTexture(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
        }
        if (arr.length == 12) {
            return buildTexture(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10], arr[11]);
        }
        return buildTexture(arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110], arr[110]);
    }

    /** How to make a custom texture **/
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite[] buildTexture(GTCXIconInfo... arr) {
        TextureAtlasSprite[] texture = new TextureAtlasSprite[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            texture[i] = arr[i].getSprite();
        }
        return texture;
    }

    public static GTCXIconInfo s(String spriteName){
        return new GTCXIconInfo(spriteName);
    }

    public static GTCXIconInfo s(int spriteId){
        return new GTCXIconInfo(spriteId);
    }

    public static GTCXIconInfo s(int x, int y){
        return new GTCXIconInfo(GTCExpansion.MODID + "_tiles", x + (16 * y));
    }

    public static GTCXIconInfo s(String spriteName, int spriteId){
        return new GTCXIconInfo(spriteName, spriteId);
    }

    public static class GTCXIconInfo{
        int spriteId;
        String spriteName;
        public GTCXIconInfo(String spriteName, int spriteId){
            this.spriteName = spriteName;
            this.spriteId = spriteId;
        }

        public GTCXIconInfo(String spriteName){
            this.spriteName = spriteName;
            this.spriteId = 0;
        }

        public GTCXIconInfo(int spriteId){
            this.spriteName = "gtclassic_terrain";
            this.spriteId = spriteId;
        }

        @SideOnly(Side.CLIENT)
        public TextureAtlasSprite getSprite(){
            return Ic2Icons.getTextures(spriteName)[spriteId];
        }
    }
}
