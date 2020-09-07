package gtc_expansion.item.itemblock;

import gtc_expansion.block.GTCXBlockPipeFluid;
import gtclassic.api.color.GTColorItemBlock;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class GTCXItemBlockPipe extends GTColorItemBlock {
    public GTCXItemBlockPipe(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.getBlock() instanceof GTCXBlockPipeFluid){
            tooltip.add(I18n.format("tooltip.gtc_expansion.fluid_pipe0", ((GTCXBlockPipeFluid)this.getBlock()).getTransferRate()));
            tooltip.add(I18n.format("tooltip.gtc_expansion.fluid_pipe1", ((GTCXBlockPipeFluid)this.getBlock()).getTransferRate() * 2));
        }
    }
}
