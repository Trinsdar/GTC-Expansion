package gtc_expansion.util.jei;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.recipes.GERecipeLists;
import gtclassic.GTBlocks;
import gtclassic.gui.GTGuiMachine;
import gtclassic.tile.multi.GTTileMultiBlastFurnace;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import net.minecraft.block.Block;

public enum GEJeiRegistry {
    ELECTROLYZER(GERecipeLists.ELECTROLYZER_RECIPE_LIST, GEBlocks.electrolyzer, GEMachineGui.GEIndustrialElectrolyzerGui.class, 78, 24, 20, 18),
    ALLOYSMELTER(GERecipeLists.ALLOY_SMELTER_RECIPE_LIST, GEBlocks.alloySmelter, GEMachineGui.GEAlloySmelterGui.class, 78, 24, 20, 18),
    INDUSTRIALGRINDER(GERecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST, GEBlocks.industrialGrinder, GEMachineGui.GEIndustrialGrinderGui.class, 78, 29, 20, 11),
    IMPLOSIONCOMPRESSOR(GERecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST, GEBlocks.implosionCompressor, GEMachineGui.GEImplosionCompressorGui.class, 78, 27, 20, 11),
    VACUUMFREEZER(GERecipeLists.VACUUM_FREEZER_RECIPE_LIST, GEBlocks.vacuumFreezer, GEMachineGui.GEVacuumFreezerGui.class, 78, 28, 20, 11),
    PRIMITIVEBLASTFURNACE(GERecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST, GEBlocks.primitiveBlastFurnace, GEMachineGui.GEPrimitiveBlastFurnaceGui.class, 78, 24, 20, 18);
    private GTRecipeMultiInputList list;
    private Block catalyst;
    @SuppressWarnings("rawtypes")
    private Class gui;
    private int clickX;
    private int clickY;
    private int sizeX;
    private int sizeY;

    @SuppressWarnings("rawtypes")
    GEJeiRegistry(GTRecipeMultiInputList list, Block catalyst, Class gui, int clickX, int clickY, int sizeX,
                  int sizeY) {
        this.list = list;
        this.catalyst = catalyst;
        this.gui = gui;
        this.clickX = clickX;
        this.clickY = clickY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public GTRecipeMultiInputList getRecipeList() {
        return this.list;
    }

    public Block getCatalyst() {
        return this.catalyst;
    }

    @SuppressWarnings("rawtypes")
    public Class getGuiClass() {
        return this.gui;
    }

    public int getClickX() {
        return this.clickX;
    }

    public int getClickY() {
        return this.clickY;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }
}