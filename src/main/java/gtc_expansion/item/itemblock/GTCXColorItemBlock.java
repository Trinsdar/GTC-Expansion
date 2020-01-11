package gtc_expansion.item.itemblock;

import gtc_expansion.GTCXBlocks;
import gtclassic.api.color.GTColorItemBlock;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GTCXColorItemBlock extends GTColorItemBlock {
    public GTCXColorItemBlock(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        PlayerHandler handler = PlayerHandler.getClientPlayerHandler();
        if (handler.hasEUReader()) {
            if (this.compare(stack, GTCXBlocks.advancedWorktable)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(128));
            }
            if (this.compare(stack, GTCXBlocks.electricLocker)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(512));
            }
        }
    }

    public boolean compare(ItemStack stack, Block block) {
        return StackUtil.isStackEqual(stack, new ItemStack(block));
    }
}
