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
        makeSprite("connected_blocks", 16, 5);
        makeSprite("materials", 16, 4);
        makeSprite("items", 16, 6);
        makeSprite("crops", 7, 1);
        collectBasicTileSprites();
        collectTurbineSprites(true);
        collectTurbineSprites(false);
        if (GTConfig.general.animatedTextures){
            addCustomTexture("industrial_front_active", 0, 0, location("bf_front"));
            addCustomTexture("diesel_generator_top_active", 0, 0, location("diesel_generator_top"));
            addCustomTexture("gas_turbine_top_active", 0, 0, location("gas_turbine_top"));
        }
        if (GTConfig.general.debugMode){
            GTCExpansion.logger.info("All GregTech Classic Expansion textures generated without error");
        }
        setTexture(GTCXBlocks.advancedWorktable, s("advanced_worktable_bottom"), s("advanced_worktable_top"), s("advanced_worktable_side"), s("advanced_worktable_side"), s("advanced_worktable_side"), s("advanced_worktable_side"), s("advanced_worktable_bottom"), s("advanced_worktable_top_active"), s("advanced_worktable_side"), s("advanced_worktable_side"), s("advanced_worktable_side"), s("advanced_worktable_side"));
        GTCXIconInfo t1 = s("alloy_furnace_side");
        setTexture(GTCXBlocks.alloyFurnace, t1, t1, t1, s("alloy_furnace_front"), t1, t1, t1, t1, t1, s("alloy_furnace_front_active"), t1, t1);
        t1 = s("machine_back");
        setTexture(GTCXBlocks.alloySmelter, s(0), s(1), t1, s("alloy_smelter_front"), s(2), s(2), s(0), s(1), t1, s("alloy_smelter_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.fluidSmelter, s(0), s(1), t1, s("fluid_smelter_front"), s(2), s(2), s(0), s(1), t1, s("fluid_smelter_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.fluidCaster, s(0), s(1), t1, s("fluid_caster_front"), s(2), s(2), s(0), s(1), t1, s("fluid_caster_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.assemblingMachine, s(0), s("assembling_machine_top"), t1, s(22), s(2), s(2), s(0), s("assembling_machine_top"), t1, s(23), s(2), s(2));
        setTexture(GTCXBlocks.chemicalReactor, s(0), s(1), s("chemical_reactor_side"), s("chemical_reactor_side"), s("chemical_reactor_side"), s("chemical_reactor_side"), s(0), s(1), s("chemical_reactor_side_active"), s("chemical_reactor_side_active"), s("chemical_reactor_side_active"), s("chemical_reactor_side_active"));
        setTexture(GTCXBlocks.dieselGenerator, s(0), s("diesel_generator_top"), t1, t1, t1, t1, s(0), s("diesel_generator_top_active"), t1, t1, t1, t1);
        t1 = s("distillation_tower_side");
        GTCXIconInfo iS = s("industrial_side"), iF = s("industrial_front"), iFA = s("industrial_front_active");
        setTexture(GTCXBlocks.distillationTower, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        setTexture(GTCXBlocks.dustBin, s("dustbin_bottom"), s("dustbin_top"), s(2), s("dustbin_front"), s(2), s(2));
        setTexture(GTCXBlocks.electricLocker, s(0), s(1), s(2), s("electric_locker_front"), s(2), s(2));
        setTexture(GTCXBlocks.gasTurbine, s(0), s("gas_turbine_top"), s(2), s(2), s(2), s(2), s(0), s("gas_turbine_top_active"), s(2), s(2), s(2), s(2));
        t1 = s("implosion_compressor_side_1"); GTCXIconInfo t2 = s("implosion_compressor_side_2");
        setTexture(GTCXBlocks.implosionCompressor, iS, iF, t1, t1, t2, t2, iS, iFA, t1, t1, t2, t2);
        t1 = s("industrial_blast_furnace_side");
        setTexture(GTCXBlocks.industrialBlastFurnace, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        setTexture(GTCXBlocks.electrolyzer, s(0), s(1), s("electrolyzer_side"), s("electrolyzer_side"), s("electrolyzer_side"), s("electrolyzer_side"), s(0), s(1), s("electrolyzer_side_active"), s("electrolyzer_side_active"), s("electrolyzer_side_active"), s("electrolyzer_side_active"));
        t1 = s("industrial_grinder_side");
        setTexture(GTCXBlocks.industrialGrinder, t1, t1, iS, iF, t1, t1, t1, t1, iS, iFA, t1, t1);
        t1 = s("machine_back");
        setTexture(GTCXBlocks.lathe, s(0), s(1), t1, s("lathe_front"), s(2), s(2), s(0), s(1), t1, s("lathe_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.locker, s(0), s(1), s(2), s("locker_front"), s(2), s(2));
        setTexture(GTCXBlocks.microwave, s(0), s(1), t1, s("microwave_front"), s(2), s(2), s(0), s(1), t1, s("microwave_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.plateBender, s(0), s(1), t1, s("plate_bender_front"), s(2), s(2), s(0), s(1), t1, s("plate_bender_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.plateCutter, s(0), s(1), t1, s("plate_cutter_front"), s(2), s(2), s(0), s(1), t1, s("plate_cutter_front_active"), s(2), s(2));
        t2 = s(GTCExpansion.MODID + "_blocks", 1);
        setTexture(GTCXBlocks.primitiveBlastFurnace, t2, t2, t2, s("primitive_blast_furnace_front"), t2, t2, t2, t2, t2, s("primitive_blast_furnace_front_active"), t2, t2);
        t2 = s("vacuum_freezer_side");
        setTexture(GTCXBlocks.stoneCompressor, s("bmach_lv", 15), s("stone_compressor_top"), s("bmach_lv", 47), s("stone_compressor_front"), s("bmach_lv", 47), s("bmach_lv", 47), s("bmach_lv", 15), s("stone_compressor_top"), s("bmach_lv", 47), s("stone_compressor_front_active"), s("bmach_lv", 47), s("bmach_lv", 47));
        setTexture(GTCXBlocks.stoneExtractor, s("bmach_lv", 15), s("stone_compressor_top"), s("stone_extractor_side"), s("stone_extractor_front"), s("stone_extractor_side"), s("stone_extractor_side"), s("bmach_lv", 15), s("stone_compressor_top"), s("stone_extractor_side_active"), s("stone_extractor_front_active"), s("stone_extractor_side_active"), s("stone_extractor_side_active"));
        setTexture(GTCXBlocks.vacuumFreezer, iS, iF, t2, t2, t2, t2, iS, iFA, t2, t2, t2, t2);
        setTexture(GTCXBlocks.wiremill, s(0), s("wiremill_top"), t1, s(22), s(2), s(2), s(0), s("wiremill_top_active"), t1, s(23), s(2), s(2));
        setTexture(GTCXBlocks.fusionMaterialInjector, s(42), s(42), s(42), s(39), s(42), s(42));
        setTexture(GTCXBlocks.fusionEnergyInjector, s(42), s(42), s(42), s(42), s(42), s(42));
        setTexture(GTCXBlocks.fusionMaterialExtractor, s(39), s(39), s(39), s(42), s(39), s(39));
        setTexture(GTCXBlocks.fusionEnergyExtractor, s(39), s(39), s(39), s(41), s(39), s(39));
        setTexture(GTCXBlocks.thermalBoiler, s(0), s(1), s(2), s("thermal_boiler_front"), s(2), s(2), s(0), s(1), s(2), s("thermal_boiler_front_active"), s(2), s(2));
        setTexture(GTCXBlocks.largeSteamTurbine, s(0), s(1), s(2), s("steam_turbine_front_center"), s(2), s(2), s(0), s(1), s(2), s("steam_turbine_front_active_center"), s(2), s(2));
        setTexture(GTCXBlocks.largeGasTurbine, s(0), s(1), s(2), s("gas_turbine_front_center"), s(2), s(2), s(0), s(1), s(2), s("gas_turbine_front_active_center"), s(2), s(2));
        setTexture(GTCXBlocks.fusionComputer, s(42), s(42), s(42), s(47), s(42), s(42), s(43), s(43), s(43), s(47), s(43), s(43));
        setTexture(GTCXBlocks.trashBin, s(0), s(97), s("trash_bin_side"), s("trash_bin_side"), s("trash_bin_side"), s("trash_bin_side"));
        setTexture(GTCXBlocks.extruder, s(0), s(21), s("machine_back"), s("extruder_front"), s("machine_side_gauge"), s("machine_side_gauge"), s(0), s(22), s("machine_back"), s("extruder_front_active"), s("machine_side_gauge_red"), s("machine_side_gauge_red"));
        setTexture(GTCXBlocks.industrialSawmill, s("industrial_side"), s("industrial_front"), s("industrial_sawmill_side_2"), s("industrial_sawmill_side_2"), s("industrial_sawmill_side_1"), s("industrial_sawmill_side_1"), s("industrial_side"), s("industrial_front_active"), s("industrial_sawmill_side_2"), s("industrial_sawmill_side_2"), s("industrial_sawmill_side_1"), s("industrial_sawmill_side_1"));
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

        for(int i = 0; i < length; ++i) {
            String string = strings[i];
            if (GTConfig.general.debugMode){
                GTCExpansion.logger.info("Attempting to get sprite data for: " + string);
            }
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/tiles/" + string + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 1));
        }

    }

    public static void collectTurbineSprites(boolean isSteam){
        String[] positions = {"bottom", "bottom_left", "bottom_right", "left", "center", "right", "top", "top_left", "top_right"};
        String steam = isSteam ? "steam" : "gas";
        for (String string : positions){
            String active = steam + "_turbine_front_active_" + string;
            String nonActive = steam + "_turbine_front_" + string;
            if (GTConfig.general.debugMode){
                GTCExpansion.logger.info("Attempting to get sprite data for: " + active);
                GTCExpansion.logger.info("Attempting to get sprite data for: " + nonActive);

            }
            Ic2Icons.addSprite(new Sprites.SpriteData(nonActive, "gtc_expansion:textures/sprites/tiles/" + steam + "_turbine/" + nonActive + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(nonActive, 0, 0, 1, 1));
            Ic2Icons.addSprite(new Sprites.SpriteData(active, "gtc_expansion:textures/sprites/tiles/" + steam + "_turbine/" + active + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(active, 0, 0, 1, 1));
            addCustomTexture(active, 0, 0, location(active));
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
