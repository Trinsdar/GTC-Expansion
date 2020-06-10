package gtc_expansion.item.overrides;

import ic2.core.item.tool.electric.ItemElectricToolDrill;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.item.ItemBlock.setTileEntityNBT;

public class GTCXItemDrill extends ItemElectricToolDrill {
    final ItemStack torch = new ItemStack(Blocks.TORCH);
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        ItemStack stack = ItemStack.EMPTY;
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        for (ItemStack stack1 : player.inventory.mainInventory){
            if (stack1.getItem() == torch.getItem()){
                stack = stack1;
                break;
            }
        }
        if (!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(Blocks.TORCH, pos, false, facing, player)) {
            IBlockState state1 = Blocks.TORCH.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player, hand);
            if (placeBlockAt(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, state1)) {
                state1 = worldIn.getBlockState(pos);
                SoundType soundtype = state1.getBlock().getSoundType(state1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        else {
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }

    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.TORCH) {
            setTileEntityNBT(world, player, pos, stack);
            Blocks.TORCH.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
        }

        return true;
    }
}
