package gtc_expansion.util.jei;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import net.minecraft.block.Block;

public enum GEJeiRegistry {
    ELECTROLYZER(GETileElectrolyzer.RECIPE_LIST, GEBlocks.electrolyzer, GEMachineGui.GTIndustrialElectrolyzerGui.class, 78, 24, 20, 18),
    ALLOYSMELTER(GETileAlloySmelter.RECIPE_LIST, GEBlocks.alloySmelter, GEMachineGui.GTAlloySmelterGui.class, 78, 24, 20, 18),
    INDUSTRIALGRINDER(GETileMultiIndustrialGrinder.RECIPE_LIST, GEBlocks.industrialGrinder, GEMachineGui.GTIndustrialGrinderGui.class, 78, 29, 20, 11),
    IMPLOSIONCOMPRESSOR(GETileMultiImplosionCompressor.RECIPE_LIST, GEBlocks.implosionCompressor, GEMachineGui.GTImplosionCompressorGui.class, 78, 27, 20, 11),
    VACUUMFREEZER(GETileMultiVacuumFreezer.RECIPE_LIST, GEBlocks.vacuumFreezer, GEMachineGui.GTVacuumFreezerGui.class, 78, 28, 20, 11);
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
