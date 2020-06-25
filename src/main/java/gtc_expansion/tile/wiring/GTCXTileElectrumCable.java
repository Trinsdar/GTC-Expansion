package gtc_expansion.tile.wiring;

import gtc_expansion.data.GTCXBlocks;
import gtclassic.api.material.GTMaterial;
import net.minecraft.block.Block;

public class GTCXTileElectrumCable extends GTCXTileColoredCable {
    public GTCXTileElectrumCable() {
        super(GTMaterial.Electrum);
    }

    public Block getBlockDrop() {
        return GTCXBlocks.electrumCable;
    }

    @Override
    public double getConductionLoss() {
        if (insulation == 1){
            return 0.4D;
        }
        if (insulation == 2){
            return 0.35D;
        }
        if (insulation == 3){
            return 0.3D;
        }
        return 0.045D;
    }

    @Override
    public double getInsulationEnergyAbsorption() {
        switch(insulation) {
            case 1: return 128.0D;
            case 2: return 512.0D;
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
        return 513;
    }
}
