package gtc_expansion.item.overrides;

import gtc_expansion.data.GTCXLang;
import ic2.core.item.block.ItemBlockMachineLV;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GTCXItemBlockMachineLV extends ItemBlockMachineLV {
    public GTCXItemBlockMachineLV(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.getMetadata() == 9){
            tooltip.add(I18n.format("tooltip.gtc_expansion.blockScanner0"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public LocaleComp getLangComponent(ItemStack stack) {
        if (stack.getMetadata() == 9){
            return GTCXLang.SCANNER;
        }
        return super.getLangComponent(stack);
    }
}
