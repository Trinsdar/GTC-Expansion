package gtc_expansion.tile.wiring;

import gtc_expansion.data.GTCXBlocks;
import gtclassic.api.material.GTMaterial;
import net.minecraft.block.Block;

public class GTCXTileAluminiumCable extends GTCXTileColoredCable {
    public GTCXTileAluminiumCable() {
        super(GTMaterial.Aluminium);
    }

    @Override
    public Block getBlockDrop() {
        return GTCXBlocks.aluminiumCable;
    }

    @Override
    public double getConductionLoss() {
        if (insulation == 1){
            return 1.05D;
        }
        if (insulation == 2){
            return 1.0D;
        }
        if (insulation == 3){
            return 0.9D;
        }
        return 1.1D;
    }

    @Override
    public double getInsulationEnergyAbsorption() {
        switch(insulation) {
            case 1: return 512.0D;
            case 2: return 2048.0D;
            case 3: return  9001.0D;
            default: return 0.0D;
        }
    }

    @Override
    public double getInsulationBreakdownEnergy() {
        return 9001D;
    }

    @Override
    public double getConductorBreakdownEnergy() {
        return 2049;
    }


}
