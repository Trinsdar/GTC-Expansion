package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTCoverBlock;
import gtc_expansion.model.GTCXModelPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseConnect;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GTCXBlockPipe extends GTBlockBaseConnect implements IGTCoverBlock {
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
        return null;
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing side) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[7];
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
                return new BlockStateContainerIC2.IC2BlockState(state, pipe.storage.getQuads());
            }
        } catch (Exception var6) {
        }

        return super.getExtendedState(state, world, pos);
    }
}
