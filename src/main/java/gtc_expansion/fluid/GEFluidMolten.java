package gtc_expansion.fluid;


import gtc_expansion.GTCExpansion;
import gtc_expansion.material.GEMaterial;
import gtclassic.GTMod;
import gtclassic.api.fluid.GTFluid;
import gtclassic.api.material.GTMaterial;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GEFluidMolten extends GTFluid {
    public GEFluidMolten(GTMaterial mat) {
        super(mat, "molten");
        if (mat.equals(GEMaterial.Iron) || mat.equals(GEMaterial.Steel)){
            setColor(Color.RED.getRGB());
        }
        GTMod.debugLogger(mat.getDisplayName());
        material = Material.LAVA;
        temperature = 2000;
    }

    public ResourceLocation getStill()
    {
        return new ResourceLocation(GTCExpansion.MODID, "fluids/molten");
    }

    public ResourceLocation getFlowing()
    {
        return new ResourceLocation(GTCExpansion.MODID, "fluids/moltenflowing");
    }
}
