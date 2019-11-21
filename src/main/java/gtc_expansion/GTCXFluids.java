package gtc_expansion;

import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.fluid.GTFluid;
import gtclassic.api.material.GTMaterialGen;

import java.awt.Color;

public class GTCXFluids {

    public static void registerFluids() {
        GTFluid fluid = (GTFluid) GTMaterialGen.getFluid(GTCXMaterial.Iron);
        fluid.setColor(Color.RED.getRGB());
        fluid = (GTFluid) GTMaterialGen.getFluid(GTCXMaterial.Steel);
        fluid.setColor(Color.RED.getRGB());
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
