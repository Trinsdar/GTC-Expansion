package gtc_expansion.item.itemblock;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.color.GTColorItemBlock;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound() && this.compare(stack, GTCXBlocks.electrumCable)){
            NBTTagCompound nbt = StackUtil.getNbtData(stack);
            if (nbt.hasKey("insulation")){
                int insulation = nbt.getInteger("insulation");
                if (insulation == 1){
                    return GTCXLang.ELECTRUM_CABLE_INSULATED_1.getLocalized();
                } else if (insulation == 2){
                    return GTCXLang.ELECTRUM_CABLE_INSULATED_2.getLocalized();
                } else if (insulation == 3){
                    return GTCXLang.ELECTRUM_CABLE_INSULATED_4.getLocalized();
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }
}
