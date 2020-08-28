package gtc_expansion.util;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GTCXMaterialWrench extends Material {
    public GTCXMaterialWrench(boolean noHarvest) {
        super(MapColor.IRON);
        if (noHarvest){
            this.setRequiresTool();
        }
        this.setImmovableMobility();
    }
}
