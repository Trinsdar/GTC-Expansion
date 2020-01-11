package gtc_expansion.item.itemblock;

import gtc_expansion.GTCXBlocks;
import gtclassic.api.itemblock.GTItemBlockRare;
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

public class GTCXItemBlockRare extends GTItemBlockRare {
    public GTCXItemBlockRare(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        PlayerHandler handler = PlayerHandler.getClientPlayerHandler();
        if (handler.hasEUReader()) {
            if (this.compare(stack, GTCXBlocks.alloySmelter) || this.compare(stack, GTCXBlocks.implosionCompressor) || this.compare(stack, GTCXBlocks.assemblingMachine) || this.compare(stack, GTCXBlocks.chemicalReactor) || this.compare(stack, GTCXBlocks.lathe) || this.compare(stack, GTCXBlocks.microwave) || this.compare(stack, GTCXBlocks.plateBender) || this.compare(stack, GTCXBlocks.wiremill)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(32));
            }

            if (this.compare(stack, GTCXBlocks.electrolyzer) || this.compare(stack, GTCXBlocks.vacuumFreezer) || this.compare(stack, GTCXBlocks.industrialGrinder) || this.compare(stack, GTCXBlocks.industrialBlastFurnace) || this.compare(stack, GTCXBlocks.fluidCaster) || this.compare(stack, GTCXBlocks.fluidSmelter) || this.compare(stack, GTCXBlocks.distillationTower) || this.compare(stack, GTCXBlocks.advancedWorktable)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(128));
            }
            if (this.compare(stack, GTCXBlocks.electricLocker)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(512));
            }
            if (this.compare(stack, GTCXBlocks.gasTurbine) || this.compare(stack, GTCXBlocks.dieselGenerator)){
                tooltip.add(Ic2InfoLang.electricProduction.getLocalizedFormatted(Ic2InfoLang.electricTransferRateVariable.getLocalized()));
            }
        }
    }

    public boolean compare(ItemStack stack, Block block) {
        return StackUtil.isStackEqual(stack, new ItemStack(block));
    }
}
