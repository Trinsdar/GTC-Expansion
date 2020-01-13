package gtc_expansion.block;

import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseConnect;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ILayeredBlockModel;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.awt.Color;

public class GTCXBlockWire extends GTBlockBaseConnect implements IGTColorBlock, ILayeredBlockModel {
    public GTCXBlockWire(String name, LocaleComp comp){
        super();
        setUnlocalizedName(comp);
        setRegistryName(name);
        this.setHardness(0.2F);
        this.setSoundType(SoundType.CLOTH);
        this.setHarvestLevel("axe", 0);
        setCreativeTab(GTMod.creativeTabGT);
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
            } else {
                colorTile.setTileColor(GTMaterial.Electrum.getColor().getRGB());
            }
        }
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTRecolorableStorageTile) {
            IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
            int color1 = color == EnumDyeColor.WHITE ? GTMaterial.Electrum.getColor().getRGB() : color.getColorValue();
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
        return GTMaterial.Electrum.getColor();
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return null;
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    @Override
    public BaseModel getModelFromState(IBlockState iBlockState) {
        return null;
    }

    @Override
    public boolean isLayered(IBlockState iBlockState) {
        return false;
    }

    @Override
    public int getLayers(IBlockState iBlockState) {
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBox(IBlockState iBlockState, int i) {
        return null;
    }

    @Override
    public TextureAtlasSprite getLayerTexture(IBlockState iBlockState, EnumFacing enumFacing, int i) {
        return null;
    }
}
