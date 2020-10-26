package gtc_expansion;

import gtc_expansion.data.GTCXBlocks;
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
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.DISTILLATION_TOWER_RECIPE_LIST, GTCXBlocks.distillationTower, GTCXMachineGui.GTCXDistillationTowerGui.class, 80, 4, 16, 72));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.PLATE_BENDER_RECIPE_LIST, GTCXBlocks.plateBender, GTCXMachineGui.GTCXPlateBenderGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.LATHE_RECIPE_LIST, GTCXBlocks.lathe, GTCXMachineGui.GTCXLatheGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.WIREMILL_RECIPE_LIST, GTCXBlocks.wiremill, GTCXMachineGui.GTCXWiremillGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.MICROWAVE_RECIPE_LIST, GTCXBlocks.microwave, GTCXMachineGui.GTCXMicrowaveGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.PLATE_CUTTER_RECIPE_LIST, GTCXBlocks.plateCutter, GTCXMachineGui.GTCXPlateCutterGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.INDUSTRIAL_SAWMILL_RECIPE_LIST, GTCXBlocks.industrialSawmill, GTCXMachineGui.GTCXIndustrialSawmillGui.class, 58, 28, 20, 11));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.EXTRUDER_RECIPE_LIST, GTCXBlocks.extruder, GTCXMachineGui.GTCXExtruderGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.BATH_RECIPE_LIST, GTCXBlocks.bath, GTCXMachineGui.GTCXBathGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.COKE_OVEN_RECIPE_LIST, GTCXBlocks.cokeOven, GTCXMachineGui.GTCXCokeOvenGui.class, 78, 24, 20, 18));
        GTJeiHandler.addEntry(new GTJeiEntry(GTCXRecipeLists.FORGE_HAMMER_RECIPE_LIST, GTCXBlocks.steamForgeHammer, GTCXMachineGui.GTCXForgeHammerGui.class, 80, 34, 20, 18));
    }
}
