package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTCoverBlock;
import gtc_expansion.model.GTCXModelPipe;
import gtc_expansion.tile.pipes.GTCXTileBaseItemPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseConnect;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.awt.Color;

public class GTCXBlockPipe extends GTBlockBaseConnect implements IGTCoverBlock, IGTColorBlock {
    GTMaterial material;
    GTCXHelperPipe.GTPipeModel type;
    public GTCXBlockPipe(String name, LocaleComp comp, GTMaterial material, GTCXHelperPipe.GTPipeModel type){
        super();
        setUnlocalizedName(comp);
        setRegistryName(name);
        this.setHardness(-1.0F);
        this.setSoundType(SoundType.METAL);
        setCreativeTab(GTMod.creativeTabGT);
        this.material = material;
        this.type = type;
    }
    @Override
    public TextureAtlasSprite getCoverTexture(EnumFacing facing) {
        return null;
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileBaseItemPipe();
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing side) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[side == EnumFacing.UP ? 8 : 7];
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return this.getTextureFromState(state, EnumFacing.SOUTH);
    }

    @Override
    public BaseModel getModelFromState(IBlockState iBlockState) {
        return new GTCXModelPipe(iBlockState, type.getSizes());
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof GTCXTileBasePipe) {
                GTCXTileBasePipe pipe = (GTCXTileBasePipe)tile;
                return new BlockStateContainerIC2.IC2BlockState(state, new GTCXHelperPipe.GTCXQuadWrapper(pipe.storage.getQuads(), pipe.getConnections()));
            }
        } catch (Exception e) {
            GTCExpansion.logger.info("IC2BlockState Failed");
            e.printStackTrace();
        }

        return super.getExtendedState(state, world, pos);
    }

    @Override
    public Color getColor(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, Block block, int i) {
        return material.getColor();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof GTCXTileBasePipe)) {
            return new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);
        } else {
            GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
            double thickness = (this.type.getSizes()[1] - this.type.getSizes()[0]) / 32.0D;
            double minX = 0.5D - thickness;
            double minY = 0.5D - thickness;
            double minZ = 0.5D - thickness;
            double maxX = 0.5D + thickness;
            double maxY = 0.5D + thickness;
            double maxZ = 0.5D + thickness;
            //TODO: make this an iterator
            if (pipe.connection.contains(EnumFacing.WEST) || pipe.anchors.contains(EnumFacing.WEST)) {
                minX = 0.0D;
                if (pipe.anchors.contains(EnumFacing.WEST)){
                    minZ = 0.0D;
                    maxZ = 1.0D;
                    minY = 0.0D;
                    maxY = 1.0D;
                }
            }
            if (pipe.connection.contains(EnumFacing.DOWN) || pipe.anchors.contains(EnumFacing.DOWN)) {
                minY = 0.0D;
                if (pipe.anchors.contains(EnumFacing.DOWN)){
                    minX = 0.0D;
                    maxX = 1.0D;
                    minZ = 0.0D;
                    maxZ = 1.0D;
                }
            }
            if (pipe.connection.contains(EnumFacing.NORTH) || pipe.anchors.contains(EnumFacing.NORTH)) {
                minZ = 0.0D;
                if (pipe.anchors.contains(EnumFacing.NORTH)){
                    minX = 0.0D;
                    maxX = 1.0D;
                    minY = 0.0D;
                    maxY = 1.0D;
                }
            }
            if (pipe.connection.contains(EnumFacing.EAST) || pipe.anchors.contains(EnumFacing.EAST)) {
                maxX = 1.0D;
                if (pipe.anchors.contains(EnumFacing.EAST)){
                    minZ = 0.0D;
                    maxZ = 1.0D;
                    minY = 0.0D;
                    maxY = 1.0D;
                }
            }
            if (pipe.connection.contains(EnumFacing.UP) || pipe.anchors.contains(EnumFacing.UP)) {
                maxY = 1.0D;
                if (pipe.anchors.contains(EnumFacing.UP)){
                    minX = 0.0D;
                    maxX = 1.0D;
                    minZ = 0.0D;
                    maxZ = 1.0D;
                }
            }
            if (pipe.connection.contains(EnumFacing.SOUTH) || pipe.anchors.contains(EnumFacing.SOUTH)) {
                maxZ = 1.0D;
                if (pipe.anchors.contains(EnumFacing.SOUTH)){
                    minX = 0.0D;
                    maxX = 1.0D;
                    minY = 0.0D;
                    maxY = 1.0D;
                }
            }
            return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }
}
