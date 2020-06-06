package gtc_expansion.item.overrides;

import ic2.core.item.tool.electric.ItemElectricToolDrill;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTCXItemDrill extends ItemElectricToolDrill {
    final ItemStack torch = new ItemStack(Blocks.TORCH);
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN){
            for (ItemStack stack : player.inventory.mainInventory){
                if (stack.getItem() == torch.getItem()){
                    if (placeTorch(player, worldIn, pos, hand, facing, hitX, hitY, hitZ)){
                        if (!player.isCreative()){
                            stack.shrink(1);
                        }
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    public boolean placeTorch(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        BlockPos offset = pos.offset(facing);
        if (grass(world, pos)){
            world.setBlockState(pos, Blocks.TORCH.getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
            return true;
        }
        if (canPlaceAt(world, pos, facing) && world.getBlockState(offset).getBlock().isAir(world.getBlockState(offset), world, offset)){
            world.setBlockState(offset, Blocks.TORCH.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, hand));
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
            return true;
        }
        return false;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState state = worldIn.getBlockState(blockpos);
        Block block = state.getBlock();
        BlockFaceShape blockfaceshape = state.getBlockFaceShape(worldIn, blockpos, facing);

        if (facing.equals(EnumFacing.UP) && state.getBlock().canPlaceTorchOnTop(state, worldIn, pos))
        {
            return true;
        }
        else if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
        {
            return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID;
        }
        else
        {
            return false;
        }
    }

    protected static boolean isExceptionBlockForAttaching(Block attachBlock)
    {
        return attachBlock instanceof BlockShulkerBox || attachBlock instanceof BlockLeaves || attachBlock instanceof BlockTrapDoor || attachBlock == Blocks.BEACON || attachBlock == Blocks.CAULDRON || attachBlock == Blocks.GLASS || attachBlock == Blocks.GLOWSTONE || attachBlock == Blocks.ICE || attachBlock == Blocks.SEA_LANTERN || attachBlock == Blocks.STAINED_GLASS;
    }

    protected static boolean isExceptBlockForAttachWithPiston(Block attachBlock)
    {
        return isExceptionBlockForAttaching(attachBlock) || attachBlock == Blocks.PISTON || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
    }


    public static boolean grass(World world, BlockPos pos) {return grass(block(world, pos), meta(world, pos));}
    public static boolean grass(Block block, int meta) {
        if (block == Blocks.TALLGRASS) return true;
        if (block == Blocks.DOUBLE_PLANT)  return meta ==  2 || meta ==  3;
        return false;
    }

    public static Block block(World world, BlockPos pos) {return world.getBlockState(pos).getBlock();}
    public static int meta(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().getMetaFromState(state);
    }
}
