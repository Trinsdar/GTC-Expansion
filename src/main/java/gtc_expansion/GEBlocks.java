package gtc_expansion;

import gtc_expansion.block.GEBlockCasing;
import gtc_expansion.block.GEBlockIndustrialBlastFurnace;
import gtc_expansion.block.GEBlockMisc;
import gtc_expansion.block.GEBlockOre;
import gtc_expansion.block.GEBlockTile;
import gtc_expansion.item.GEItemBlockRare;
import gtc_expansion.tile.GETileAlloyFurnace;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileAssemblingMachine;
import gtc_expansion.tile.GETileChemicalReactor;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiDistillationTower;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtc_expansion.util.GELang;
import gtclassic.api.color.GTColorItemBlock;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.interfaces.IGTItemBlock;
import ic2.core.IC2;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class GEBlocks {
    private GEBlocks() {
        throw new IllegalStateException("Utility class");
    }

    static final List<Block> toRegister = new ArrayList();
    public static final GEBlockTile electrolyzer = registerBlock(new GEBlockTile("industrialelectrolyzer", GELang.INDUSTRIAL_ELECTROLYZER, 1));
    public static final GEBlockTile alloySmelter = registerBlock(new GEBlockTile("alloysmelter", GELang.ALLOY_SMELTER));
    public static final GEBlockTile assemblingMachine = registerBlock(new GEBlockTile("assemblingmachine", GELang.ASSEMBLING_MACHINE));
    public static final GEBlockTile chemicalReactor = registerBlock(new GEBlockTile("chemicalreactor", GELang.CHEMICAL_REACTOR));
    public static final GEBlockTile industrialGrinder = registerBlock(new GEBlockTile("industrialgrinder", GELang.INDUSTRIAL_GRINDER));
    public static final GEBlockTile implosionCompressor = registerBlock(new GEBlockTile("implosioncompressor", GELang.IMPLOSION_COMPRESSOR));
    public static final GEBlockIndustrialBlastFurnace industrialBlastFurnace = registerBlock(new GEBlockIndustrialBlastFurnace());
    public static final GEBlockTile vacuumFreezer = registerBlock(new GEBlockTile("vacuumfreezer", GELang.VACUUM_FREEZER));
    public static final GEBlockTile distillationTower = registerBlock(new GEBlockTile("distillationtower", GELang.DISTILLATION_TOWER));
    public static final GEBlockTile alloyFurnace = registerBlock(new GEBlockTile("alloyfurnace", GELang.ALLOY_FURNACE, Material.ROCK));
    public static final GEBlockTile primitiveBlastFurnace = registerBlock(new GEBlockTile("primitiveblastfurnace", GELang.PRIMITIVE_BLAST_FURNACE, Material.ROCK, 3));
    //public static final GEBlockTile fusionReactor = registerBlock(new GEBlockTile("fusionreactor", GTLang.FUSION_REACTOR, 5));


    public static final GEBlockCasing casingStandard = registerBlock(new GEBlockCasing("standard", 2,75F));
    public static final GEBlockCasing casingReinforced = registerBlock(new GEBlockCasing("reinforced", 4, 150.0F));
    public static final GEBlockCasing casingAdvanced = registerBlock(new GEBlockCasing("advanced", 1, 200F));

    public static final GEBlockMisc fireBrickBlock = registerBlock(new GEBlockMisc("fire_brick_block", "pickaxe", 3, 2.0F, 10.0F, 1, Material.ROCK, SoundType.STONE));

    public static final GEBlockOre orePyrite = registerBlock(new GEBlockOre("pyrite", 16, 2.0F, 1));
    public static final GEBlockOre oreCinnabar = registerBlock(new GEBlockOre("cinnabar", 17, 3.0F, 2));
    public static final GEBlockOre oreSphalerite = registerBlock(new GEBlockOre("sphalerite", 18, 2.0F, 1));
    public static final GEBlockOre oreTungstate = registerBlock(new GEBlockOre("tungstate", 19, 4.0F, 2));
    public static final GEBlockOre oreSheldonite = registerBlock(new GEBlockOre("sheldonite", 20, 3.5F, 3));
    public static final GEBlockOre oreOlivine = registerBlock(new GEBlockOre("olivine", 21, 3.0F, 3));
    public static final GEBlockOre oreSodalite = registerBlock(new GEBlockOre("sodalite", 22, 3.0F, 2));
    public static final GEBlockOre oreOlivineOverworld = registerBlock(new GEBlockOre("olivine_overworld", 23, 3.0F, 3));
    public static final GEBlockOre oreCassiterite = registerBlock(new GEBlockOre("cassiterite", 24, 3.0F, 1));
    public static final GEBlockOre oreTetrahedrite = registerBlock(new GEBlockOre("tetrahedrite", 25, 3.0F, 1));
    public static final GEBlockOre oreGalena = registerBlock(new GEBlockOre("galena", 26, 4.0F, 2));


    protected static final String[] textureTileBasic = new String[]{"industrialelectrolyzer", "alloysmelter", "assemblingmachine", "chemicalreactor", "distillationtower", "industrialgrinder", "vacuumfreezer", "industrialblastfurnace", "implosioncompressor", "alloyfurnace", "primitiveblastfurnace"};
    public static void registerBlocks() {
        for(Block block : toRegister){
            createBlock(block);
        }
    }

    static <T extends Block> T registerBlock(T block) {
        toRegister.add(block);
        return block;
    }

    public static void createBlock(Block block) {
        IC2.getInstance().createBlock(block, getItemBlock(block));
    }

    static Class<? extends ItemBlockRare> getItemBlock(Block block) {
        if (block instanceof IGTItemBlock) {
            return ((IGTItemBlock)block).getCustomItemBlock();
        } else {
            return block instanceof IGTColorBlock ? GTColorItemBlock.class : GEItemBlockRare.class;
        }
    }

    public static void registerTiles() {
        registerUtil(GETileElectrolyzer.class, "IndustrialElectrolyzer");
        registerUtil(GETileAlloySmelter.class, "AlloySmelter");
        registerUtil(GETileMultiIndustrialGrinder.class, "IndustrialGrinder");
        registerUtil(GETileMultiImplosionCompressor.class, "ImplosionCompressor");
        registerUtil(GETileMultiVacuumFreezer.class, "VacuumFreezer");
        registerUtil(GETileAlloyFurnace.class, "AlloyFurnace");
        registerUtil(GETileAssemblingMachine.class, "AssemblingMachine");
        registerUtil(GETileChemicalReactor.class, "ChemicalReactor");
        registerUtil(GETileMultiDistillationTower.class, "DistillationTower");
        registerUtil(GETileMultiPrimitiveBlastFurnace.class, "PrimitiveBlastFurnace");
        registerUtil(GETileMultiIndustrialBlastFurnace.class, "IndustrialBlastFurnace");
        //registerUtil(GETileMultiFusionReactor.class, "BigFusionReactor");
    }

    public static void registerUtil(Class tile, String name) {
        GameRegistry.registerTileEntity(tile, new ResourceLocation("gtclassic", "tileEntity" + name));
    }
}
