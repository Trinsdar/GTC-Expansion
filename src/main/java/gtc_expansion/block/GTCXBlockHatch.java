package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXIcons;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.model.GTCXModelHatch;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.EnumFacing.SOUTH;

public class GTCXBlockHatch  extends GTCXBlockTile implements ICustomModeledBlock {
    public static PropertyInteger casing = PropertyInteger.create("casing", 0, 3);
    public GTCXBlockHatch(String name, LocaleComp comp) {
        super(name, comp);
    }

    public GTCXBlockHatch(String name, LocaleComp comp, int additionalInfo) {
        super(name, comp, additionalInfo);
    }

    public GTCXBlockHatch(String name, LocaleComp comp, Material material) {
        super(name, comp, material);
    }

    public GTCXBlockHatch(String name, LocaleComp comp, Material material, int additionalInfo) {
        super(name, comp, material, additionalInfo);
    }

    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing side) {
        if (side == state.getValue(allFacings)){
            return this == GTCXBlocks.dynamoHatch ? GTCXIcons.s(8, 3).getSprite() : this == GTCXBlocks.inputHatch ? GTCXIcons.s(9, 3).getSprite() : this == GTCXBlocks.machineControlHatch ? GTCXIcons.s(8, 2).getSprite() : GTCXIcons.s(10, 3).getSprite();
        }
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[26];
    }

    public GTCXBlockCasing fromCasing(int casing){
        switch (casing){
            case 1: return GTCXBlocks.casingStandard;
            case 2: return GTCXBlocks.casingReinforced;
            default: return GTCXBlocks.casingAdvanced;
        }
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return getTextureFromState(state, SOUTH);
        //int cas = state.getValue(casing);
        //return cas > 0 ? Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[((cas - 1) * 16) + 7] : Ic2Icons.getTextures("gtclassic_terrain")[2];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BaseModel getModelFromState(IBlockState state) {
        return new GTCXModelHatch(this, state);
    }

    @Override
    public List<IBlockState> getValidModelStates() {
        return this.getBlockState().getValidStates();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean hasRotation(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            ((IGTCasingBackgroundBlock)tile).setCasing();
            ((IGTCasingBackgroundBlock)tile).setConfig();
        }
        if (tile instanceof GTCXTileMachineControlHatch){
            ((GTCXTileMachineControlHatch)tile).onBlockPlaced();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            ((IGTCasingBackgroundBlock)tile).setCasing();
            ((IGTCasingBackgroundBlock)tile).setConfig();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, allFacings, active);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(active, false).withProperty(allFacings, EnumFacing.NORTH);
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        EnumFacing[] facings = EnumFacing.VALUES;
        int facingsLength = facings.length;

        for(int i = 0; i < facingsLength; ++i) {
            EnumFacing side = facings[i];
            states.add(def.withProperty(allFacings, side).withProperty(active, false));
            states.add(def.withProperty(allFacings, side).withProperty(active, true));
        }

        return states;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IGTCasingBackgroundBlock block = (IGTCasingBackgroundBlock)worldIn.getTileEntity(pos);
        if (block != null) {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, block.getFacing());
            }

            return state.withProperty(active, block.getActive());
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, EnumFacing.NORTH);
            }

            return state.withProperty(active, false);
        }
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IGTCasingBackgroundBlock) {
                IGTCasingBackgroundBlock casing = (IGTCasingBackgroundBlock)tile;
                return new BlockStateContainerIC2.IC2BlockState(state, new GTCXBlockCasing.IntWrapper(casing.getConfig(), casing.getCasing()));
            }
        } catch (Exception e) {
            GTCExpansion.logger.info("IC2BlockState Failed");
            e.printStackTrace();
        }

        return super.getExtendedState(state, world, pos);
    }
}
