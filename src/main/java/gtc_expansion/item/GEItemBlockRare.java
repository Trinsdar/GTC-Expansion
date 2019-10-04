package gtc_expansion.item;

import gtc_expansion.GEBlocks;
import gtclassic.GTBlocks;
import gtclassic.itemblock.GTItemBlockRare;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.player.PlayerHandler;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GEItemBlockRare extends GTItemBlockRare {
    public GEItemBlockRare(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        PlayerHandler handler = PlayerHandler.getClientPlayerHandler();
        if (handler.hasEUReader()) {
            if (this.compare(stack, GEBlocks.alloySmelter) || this.compare(stack, GEBlocks.implosionCompressor)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(32));
            }

            if (this.compare(stack, GEBlocks.electrolyzer) || this.compare(stack, GEBlocks.vacuumFreezer) || this.compare(stack, GEBlocks.industrialGrinder)) {
                tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(128));
            }
        }
    }
}
