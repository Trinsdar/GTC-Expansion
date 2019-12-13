package gtc_expansion;

import gtc_expansion.block.GTCXBlockCasing;
import gtc_expansion.block.GTCXBlockIndustrialBlastFurnace;
import gtc_expansion.block.GTCXBlockMisc;
import gtc_expansion.block.GTCXBlockOre;
import gtc_expansion.block.GTCXBlockTile;
import gtc_expansion.item.GTCXItemBlockRare;
import gtc_expansion.tile.GTCXTileAlloyFurnace;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtc_expansion.util.GTCXLang;
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

public class GTCXBlocks {
    private GTCXBlocks() {
        throw new IllegalStateException("Utility class");
    }

    static final List<Block> toRegister = new ArrayList();
    public static final GTCXBlockTile electrolyzer = registerBlock(new GTCXBlockTile("industrialelectrolyzer", GTCXLang.INDUSTRIAL_ELECTROLYZER, 1));
    public static final GTCXBlockTile alloySmelter = registerBlock(new GTCXBlockTile("alloysmelter", GTCXLang.ALLOY_SMELTER));
    public static final GTCXBlockTile assemblingMachine = registerBlock(new GTCXBlockTile("assemblingmachine", GTCXLang.ASSEMBLING_MACHINE));
    public static final GTCXBlockTile chemicalReactor = registerBlock(new GTCXBlockTile("chemicalreactor", GTCXLang.CHEMICAL_REACTOR));
    public static final GTCXBlockTile industrialGrinder = registerBlock(new GTCXBlockTile("industrialgrinder", GTCXLang.INDUSTRIAL_GRINDER));
    public static final GTCXBlockTile implosionCompressor = registerBlock(new GTCXBlockTile("implosioncompressor", GTCXLang.IMPLOSION_COMPRESSOR));
    public static final GTCXBlockIndustrialBlastFurnace industrialBlastFurnace = registerBlock(new GTCXBlockIndustrialBlastFurnace());
    public static final GTCXBlockTile vacuumFreezer = registerBlock(new GTCXBlockTile("vacuumfreezer", GTCXLang.VACUUM_FREEZER));
    public static final GTCXBlockTile distillationTower = registerBlock(new GTCXBlockTile("distillationtower", GTCXLang.DISTILLATION_TOWER));
    public static final GTCXBlockTile alloyFurnace = registerBlock(new GTCXBlockTile("alloyfurnace", GTCXLang.ALLOY_FURNACE, Material.ROCK));
    public static final GTCXBlockTile primitiveBlastFurnace = registerBlock(new GTCXBlockTile("primitiveblastfurnace", GTCXLang.PRIMITIVE_BLAST_FURNACE, Material.ROCK, 3));
    public static final GTCXBlockTile fluidCaster = registerBlock(new GTCXBlockTile("fluidcaster", GTCXLang.FLUID_CASTER));
    public static final GTCXBlockTile plateBender = registerBlock(new GTCXBlockTile("platebender", GTCXLang.PLATE_BENDER));
    //public static final GEBlockTile fusionReactor = registerBlock(new GEBlockTile("fusionreactor", GTLang.FUSION_REACTOR, 5));


    public static final GTCXBlockCasing casingStandard = registerBlock(new GTCXBlockCasing("standard", 2,75F));
    public static final GTCXBlockCasing casingReinforced = registerBlock(new GTCXBlockCasing("reinforced", 4, 150.0F));
    public static final GTCXBlockCasing casingAdvanced = registerBlock(new GTCXBlockCasing("advanced", 1, 200F));

    public static final GTCXBlockMisc fireBrickBlock = registerBlock(new GTCXBlockMisc("fire_brick_block", "pickaxe", 3, 2.0F, 10.0F, 1, Material.ROCK, SoundType.STONE));

    public static final GTCXBlockOre orePyrite = registerBlock(new GTCXBlockOre("pyrite", 16, 2.0F, 1));
    public static final GTCXBlockOre oreCinnabar = registerBlock(new GTCXBlockOre("cinnabar", 17, 3.0F, 2));
    public static final GTCXBlockOre oreSphalerite = registerBlock(new GTCXBlockOre("sphalerite", 18, 2.0F, 1));
    public static final GTCXBlockOre oreTungstate = registerBlock(new GTCXBlockOre("tungstate", 19, 4.0F, 2));
    public static final GTCXBlockOre oreSheldonite = registerBlock(new GTCXBlockOre("sheldonite", 20, 3.5F, 3));
    public static final GTCXBlockOre oreOlivine = registerBlock(new GTCXBlockOre("olivine", 21, 3.0F, 3));
    public static final GTCXBlockOre oreSodalite = registerBlock(new GTCXBlockOre("sodalite", 22, 3.0F, 2));
    public static final GTCXBlockOre oreOlivineOverworld = registerBlock(new GTCXBlockOre("olivine_overworld", 23, 3.0F, 3));
    public static final GTCXBlockOre oreCassiterite = registerBlock(new GTCXBlockOre("cassiterite", 24, 3.0F, 1));
    public static final GTCXBlockOre oreTetrahedrite = registerBlock(new GTCXBlockOre("tetrahedrite", 25, 3.0F, 1));
    public static final GTCXBlockOre oreGalena = registerBlock(new GTCXBlockOre("galena", 26, 4.0F, 2));


    protected static final String[] textureTileBasic = new String[]{"industrialelectrolyzer", "alloysmelter", "assemblingmachine", "chemicalreactor", "distillationtower", "industrialgrinder", "vacuumfreezer", "industrialblastfurnace", "implosioncompressor", "alloyfurnace", "primitiveblastfurnace", "fluidcaster", "platebender"};
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
            return block instanceof IGTColorBlock ? GTColorItemBlock.class : GTCXItemBlockRare.class;
        }
    }

    public static void registerTiles() {
        registerUtil(GTCXTileElectrolyzer.class, "IndustrialElectrolyzer");
        registerUtil(GTCXTileAlloySmelter.class, "AlloySmelter");
        registerUtil(GTCXTileMultiIndustrialGrinder.class, "IndustrialGrinder");
        registerUtil(GTCXTileMultiImplosionCompressor.class, "ImplosionCompressor");
        registerUtil(GTCXTileMultiVacuumFreezer.class, "VacuumFreezer");
        registerUtil(GTCXTileAlloyFurnace.class, "AlloyFurnace");
        registerUtil(GTCXTileAssemblingMachine.class, "AssemblingMachine");
        registerUtil(GTCXTileChemicalReactor.class, "ChemicalReactor");
        registerUtil(GTCXTileMultiDistillationTower.class, "DistillationTower");
        registerUtil(GTCXTileMultiPrimitiveBlastFurnace.class, "PrimitiveBlastFurnace");
        registerUtil(GTCXTileMultiIndustrialBlastFurnace.class, "IndustrialBlastFurnace");
        registerUtil(GTCXTileFluidCaster.class, "FluidCaster");
        //registerUtil(GETileMultiFusionReactor.class, "BigFusionReactor");
    }

    public static void registerUtil(Class tile, String name) {
        GameRegistry.registerTileEntity(tile, new ResourceLocation("gtclassic", "tileEntity" + name));
    }
}
