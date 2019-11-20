package gtc_expansion;

import gtc_expansion.recipes.GERecipeLists;
import gtclassic.api.jei.GTJeiEntry;
import gtclassic.api.jei.GTJeiHandler;

public class GEJei {
    public static void initJei(){

        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.ELECTROLYZER_RECIPE_LIST, GEBlocks.electrolyzer, GEMachineGui.GEIndustrialElectrolyzerGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.ALLOY_SMELTER_RECIPE_LIST, GEBlocks.alloySmelter, GEMachineGui.GEAlloySmelterGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST, GEBlocks.assemblingMachine, GEMachineGui.GEAssemblingMachineGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.CHEMICAL_REACTOR_RECIPE_LIST, GEBlocks.chemicalReactor, GEMachineGui.GEChemicalReactorGui.class, 73, 34, 30, 10));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST, GEBlocks.industrialGrinder, GEMachineGui.GEIndustrialGrinderGui.class, 78, 29, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST, GEBlocks.implosionCompressor, GEMachineGui.GEImplosionCompressorGui.class, 78, 27, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.VACUUM_FREEZER_RECIPE_LIST, GEBlocks.vacuumFreezer, GEMachineGui.GEVacuumFreezerGui.class, 78, 28, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GERecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST, GEBlocks.primitiveBlastFurnace, GEMachineGui.GEPrimitiveBlastFurnaceGui.class, 78, 24, 20, 18));
    }
}
