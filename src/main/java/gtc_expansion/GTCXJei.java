package gtc_expansion;

import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.jei.GTJeiEntry;
import gtclassic.api.jei.GTJeiHandler;

public class GTCXJei {
    public static void initJei(){

        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST, GTCXBlocks.electrolyzer, GTCXMachineGui.GTCXIndustrialElectrolyzerGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.ALLOY_SMELTER_RECIPE_LIST, GTCXBlocks.alloySmelter, GTCXMachineGui.GTCXAlloySmelterGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST, GTCXBlocks.assemblingMachine, GTCXMachineGui.GTCXAssemblingMachineGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST, GTCXBlocks.chemicalReactor, GTCXMachineGui.GTCXChemicalReactorGui.class, 73, 34, 30, 10));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST, GTCXBlocks.industrialGrinder, GTCXMachineGui.GTCXIndustrialGrinderGui.class, 78, 29, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST, GTCXBlocks.implosionCompressor, GTCXMachineGui.GTCXImplosionCompressorGui.class, 78, 27, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.VACUUM_FREEZER_RECIPE_LIST, GTCXBlocks.vacuumFreezer, GTCXMachineGui.GTCXVacuumFreezerGui.class, 78, 28, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST, GTCXBlocks.primitiveBlastFurnace, GTCXMachineGui.GTCXPrimitiveBlastFurnaceGui.class, 78, 24, 20, 18));
    }
}
