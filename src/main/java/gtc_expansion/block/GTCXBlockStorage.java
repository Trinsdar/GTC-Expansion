package gtc_expansion.block;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.tile.GTCXTileAdvancedWorktable;
import gtc_expansion.tile.GTCXTileTrashBin;
import gtc_expansion.tile.GTCXTileDustbin;
import gtc_expansion.tile.GTCXTileElectricLocker;
import gtc_expansion.tile.GTCXTileLocker;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.common.tile.GTTileWorktable;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GTCXBlockStorage extends GTCXBlockTile implements IGTColorBlock {

	public GTCXBlockStorage(String name, LocaleComp comp) {
		super(name, comp);
	}

	public GTCXBlockStorage(String name, LocaleComp comp, int tooltipSize) {
		super(name, comp, tooltipSize);
	}

	public GTCXBlockStorage(String name, LocaleComp comp, Material blockMat, int tooltipSize) {
		super(name, comp, blockMat, tooltipSize);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		NBTTagCompound nbt = StackUtil.getNbtData(stack);
		if (nbt.hasKey("color")) {
			tooltip.add(I18n.format("tooltip.gtclassic.paintedtrue"));
		} else {
			tooltip.add(I18n.format("tooltip.gtclassic.paintedfalse"));
		}
		//GTBlockStorage.tooltipPaintable(stack, tooltip);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity tile = worldIn.getTileEntity(pos);
		NBTTagCompound nbt = StackUtil.getNbtData(stack);
		if (tile instanceof IGTRecolorableStorageTile) {
			IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
			if (nbt.hasKey("color")) {
				colorTile.setTileColor(nbt.getInteger("color"));
			} else if (this == GTCXBlocks.advancedWorktable){
				colorTile.setTileColor(GTMaterial.Electrum.getColor().getRGB());
			} else {
				colorTile.setTileColor(Color.WHITE.getRGB());
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof GTTileWorktable && ((GTTileWorktable) tile).inUse) {
			return false;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IGTRecolorableStorageTile) {
			IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
			int color1 = this == GTCXBlocks.advancedWorktable && color == EnumDyeColor.WHITE ? GTMaterial.Electrum.getColor().getRGB() : GTValues.getColorFromEnumDyeColor(color);
			colorTile.setTileColor(color1);
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			world.notifyBlockUpdate(pos, state, state, 2);
			return true;
		}
		return false;
	}

	@Override
	public Color getColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, Block block, int index) {
		if (worldIn != null) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof IGTRecolorableStorageTile) {
				IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
				return colorTile.getTileColor();
			}
		}
		return this == GTCXBlocks.advancedWorktable ? GTMaterial.Electrum.getColor() : Color.white;
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
		List<ItemStack> items = new ArrayList<>();
		if (te instanceof IGTItemContainerTile) {
			items.addAll(((IGTItemContainerTile) te).getDrops());
			return items;
		}
		return items;
	}

	@Override
	public boolean hasFacing() {
		//return this != GTBlocks.tileWorktable;
		return true;
	}

	@Override
	public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
		if (this == GTCXBlocks.locker) {
			return new GTCXTileLocker();
		}
		if (this == GTCXBlocks.electricLocker){
			return new GTCXTileElectricLocker();
		}
		if (this == GTCXBlocks.advancedWorktable){
			return new GTCXTileAdvancedWorktable();
		}
		if (this == GTCXBlocks.dustBin){
			return new GTCXTileDustbin();
		}
		if (this == GTCXBlocks.trashBin){
			return new GTCXTileTrashBin();
		}
		return null;
	}
}
