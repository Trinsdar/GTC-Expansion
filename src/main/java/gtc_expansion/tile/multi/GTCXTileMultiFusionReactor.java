package gtc_expansion.tile.multi;

import gtc_expansion.GTCXBlocks;
import gtclassic.api.helpers.int3;
import gtclassic.common.GTBlocks;
import gtclassic.common.tile.multi.GTTileMultiFusionReactor;
import net.minecraft.block.state.IBlockState;

public class GTCXTileMultiFusionReactor extends GTTileMultiFusionReactor {
    IBlockState coilState = GTBlocks.casingFusion.getDefaultState();
    IBlockState casingState = GTCXBlocks.casingAdvanced.getDefaultState();

    @Override
    public boolean checkStructure() {
        int3 dir = new int3(this.getPos(), this.getFacing());
        this.status = "No";
        this.getNetwork().updateTileGuiField(this, "status");
        //top section
        if (!isCasing(dir.up(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }

        // middle section
        if (!isCasing(dir.forward(1).down(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }

        // bottom section
        if (!isCasing(dir.down(1).back(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).right(1))){
            return false;
        }
        this.status = "Yes";
        this.getNetwork().updateTileGuiField(this, "status");
        return true;
    }

    public boolean isCasing(int3 pos){
        return world.getBlockState(pos.asBlockPos()) == casingState;
    }

}
