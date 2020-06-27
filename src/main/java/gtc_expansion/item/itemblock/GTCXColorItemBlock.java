package gtc_expansion.item.itemblock;

import gtc_expansion.block.GTCXBlockWire;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtclassic.api.color.GTColorItemBlock;
import gtclassic.api.interfaces.IGTColorBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.awt.Color;

public class GTCXColorItemBlock extends GTColorItemBlock{
    private static final String INSULATION = "insulation";
    Block block;
    public GTCXColorItemBlock(Block block) {
        super(block);
        this.block = block;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if ((this.block == GTCXBlocks.electrumCable || this.block == GTCXBlocks.aluminiumCable) && this.isInCreativeTab(tab)){
            ItemStack stack = new ItemStack(block);
            items.add(stack.copy());
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            nbt.setInteger(INSULATION, 1);
            items.add(stack.copy());
            nbt.setInteger(INSULATION, 2);
            items.add(stack.copy());
            nbt.setInteger(INSULATION, 3);
            items.add(stack.copy());
        } else {
            super.getSubItems(tab, items);
        }
    }

    public boolean compare(ItemStack stack, Block block) {
        return StackUtil.isStackEqual(stack, new ItemStack(block));
    }

    @Override
    public LocaleComp getLangComponent(ItemStack stack) {
        if (stack.hasTagCompound()){
            NBTTagCompound nbt = StackUtil.getNbtData(stack);
            if (nbt.hasKey(INSULATION)){
                int insulation = nbt.getInteger(INSULATION);
                if (this.compare(stack, GTCXBlocks.electrumCable)){
                    if (insulation == 1){
                        return GTCXLang.ELECTRUM_CABLE_INSULATED_1;
                    } else if (insulation == 2){
                        return GTCXLang.ELECTRUM_CABLE_INSULATED_2;
                    } else if (insulation == 3){
                        return GTCXLang.ELECTRUM_CABLE_INSULATED_4;
                    }
                }
                if (this.compare(stack, GTCXBlocks.aluminiumCable)){
                    if (insulation == 1){
                        return GTCXLang.ALUMINIUM_CABLE_INSULATED_1;
                    } else if (insulation == 2){
                        return GTCXLang.ALUMINIUM_CABLE_INSULATED_2;
                    } else if (insulation == 3){
                        return GTCXLang.ALUMINIUM_CABLE_INSULATED_4;
                    }
                }
            }
        }
        return super.getLangComponent(stack);
    }

    @Override
    public Color getColor(ItemStack stack, int index) {
        if (this.compare(stack, GTCXBlocks.electrumCable) || this.compare(stack, GTCXBlocks.aluminiumCable)){
            NBTTagCompound nbt = StackUtil.getNbtData(stack);
            if (nbt.hasKey("color") && index < 2) {
                return new Color(nbt.getInteger("color"));
            }
            IBlockState state = null;
            if (nbt.hasKey(INSULATION)){
                state = block.getDefaultState().withProperty(GTCXBlockWire.INSULATION, nbt.getInteger(INSULATION));
            }
            if (this.block instanceof IGTColorBlock) {
                return ((IGTColorBlock) block).getColor(state, null, null, this.block, index);
            } else {
                return null;
            }
        }
        return super.getColor(stack, index);
    }
}
