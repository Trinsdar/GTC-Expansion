package gtc_expansion;

import gtc_expansion.fluid.GEFluidMolten;
import gtc_expansion.material.GEMaterial;
import gtclassic.GTBlocks;
import gtclassic.GTMod;
import gtclassic.fluid.GTFluid;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import net.minecraftforge.fluids.FluidRegistry;

public class GEFluids {

    public static void registerFluids() {
        for (GTMaterial mat : GTMaterial.values()) {
            if (mat.hasFlag(GEMaterial.molten)) {
                GTMod.debugLogger("Generating GregTech molten metal: " + mat.getDisplayName());
                FluidRegistry.registerFluid(new GEFluidMolten(mat));
            }
        }
    }

    public static void registerFluidBlocks() {
        // commenting for now till I figure out whether I want to do fluid blocks or not
        /*for (GTMaterial mat : GTMaterial.values()) {
            if (mat.hasFlag(GTMaterialFlag.GAS)) {
                GTBlocks.createBlock(new GTFluidBlockGas(mat));
            }
            if (mat.hasFlag(GTMaterialFlag.FLUID)) {
                GTBlocks.createBlock(new GTFluidBlock(mat));
            }
            if (mat.hasFlag(GEMaterial.molten)) {
                GTBlocks.createBlock(new GTFluidBlockMolten(mat));
            }
        }*/
    }
}
