package gtc_expansion.util.jei;

import gtc_expansion.GTBlocks2;
import gtc_expansion.GTGuiMachine2;
import gtc_expansion.tile.GTTileAlloySmelter;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtc_expansion.tile.multi.GTTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTTileMultiVacuumFreezer;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import net.minecraft.block.Block;

public enum GTJeiRegistry2 {
    ELECTROLYZER(GTTileElectrolyzer.RECIPE_LIST, GTBlocks2.electrolyzer, GTGuiMachine2.GTIndustrialElectrolyzerGui.class, 78, 24, 20, 18),
    ALLOYSMELTER(GTTileAlloySmelter.RECIPE_LIST, GTBlocks2.alloySmelter, GTGuiMachine2.GTAlloySmelterGui.class, 78, 24, 20, 18),
    INDUSTRIALGRINDER(GTTileMultiIndustrialGrinder.RECIPE_LIST, GTBlocks2.industrialGrinder, GTGuiMachine2.GTIndustrialGrinderGui.class, 78, 29, 20, 11),
    IMPLOSIONCOMPRESSOR(GTTileMultiImplosionCompressor.RECIPE_LIST, GTBlocks2.implosionCompressor, GTGuiMachine2.GTImplosionCompressorGui.class, 78, 28, 20, 11),
    VACUUMFREEZER(GTTileMultiVacuumFreezer.RECIPE_LIST, GTBlocks2.vacuumFreezer, GTGuiMachine2.GTVacuumFreezerGui.class, 78, 28, 20, 11);
    private GTRecipeMultiInputList list;
    private Block catalyst;
    @SuppressWarnings("rawtypes")
    private Class gui;
    private int clickX;
    private int clickY;
    private int sizeX;
    private int sizeY;

    @SuppressWarnings("rawtypes")
    GTJeiRegistry2(GTRecipeMultiInputList list, Block catalyst, Class gui, int clickX, int clickY, int sizeX,
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
