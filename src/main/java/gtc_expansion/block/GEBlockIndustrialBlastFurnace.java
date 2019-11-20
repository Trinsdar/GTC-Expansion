package gtc_expansion.block;

import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.util.GELang;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GEBlockIndustrialBlastFurnace extends GEBlockTile {
    public GEBlockIndustrialBlastFurnace() {
        super("industrialblastfurnace", GELang.INDUSTRIAL_BLAST_FURNACE, 3);
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        return new GETileMultiIndustrialBlastFurnace();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt;
            nbt = StackUtil.getNbtData(stack);
            if (nbt.getBoolean("Kanthal")) {
                tooltip.add(TextFormatting.GREEN + I18n.format("Has kanthal heating coils inserted."));
            }
            if (nbt.getBoolean("Nichrome")) {
                tooltip.add(TextFormatting.GREEN + I18n.format("Has nichrome heating coils inserted."));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> items = new ArrayList<>();
        TileEntity te = this.getLocalTile() == null ? world.getTileEntity(pos) : this.getLocalTile();
        if (te instanceof IGTItemContainerTile) {
            items.addAll(((IGTItemContainerTile) te).getInventoryDrops());
            return items;
        }
        return items;
    }

    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te,
                                          EntityPlayer player, int fortune) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        if (te instanceof IGTItemContainerTile) {
            items.addAll(((IGTItemContainerTile) te).getDrops());
        }
        return items;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        NBTTagCompound nbt;
        if (tile instanceof GETileMultiIndustrialBlastFurnace) {
            GETileMultiIndustrialBlastFurnace ibf = (GETileMultiIndustrialBlastFurnace) tile;
            if (stack.hasTagCompound()) {
                nbt = StackUtil.getNbtData(stack);
                if (nbt.getBoolean("Kanthal")) {
                    ibf.setKanthal(true);
                }
                if (nbt.getBoolean("Nichrome")){
                    ibf.setNichrome(true);
                }
            }
        }
    }
}
