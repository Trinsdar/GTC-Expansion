package gtc_expansion;

import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.fluid.GTFluid;
import gtclassic.api.material.GTMaterialGen;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class GTCXFluids {

    public static void registerFluids() {
        Fluid fluid = GTMaterialGen.getFluid(GTCXMaterial.Iron);
        if (fluid instanceof GTFluid){
            GTFluid fluidGT = (GTFluid) fluid;
            fluidGT.setColor(Color.RED.getRGB());
        }
        fluid = GTMaterialGen.getFluid(GTCXMaterial.Steel);
        if (fluid instanceof GTFluid){
            GTFluid fluidGT = (GTFluid) fluid;
            fluidGT.setColor(new Color(140, 0, 0).getRGB());
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
