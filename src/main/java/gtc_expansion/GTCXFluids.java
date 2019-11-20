package gtc_expansion;

import gtc_expansion.fluid.GTCXFluidMolten;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.GTMod;
import gtclassic.api.material.GTMaterial;
import net.minecraftforge.fluids.FluidRegistry;

public class GTCXFluids {

    public static void registerFluids() {
        for (GTMaterial mat : GTMaterial.values()) {
            if (mat.hasFlag(GTCXMaterial.molten)) {
                GTMod.debugLogger("Generating GregTech molten metal: " + mat.getDisplayName());
                FluidRegistry.registerFluid(new GTCXFluidMolten(mat));
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
