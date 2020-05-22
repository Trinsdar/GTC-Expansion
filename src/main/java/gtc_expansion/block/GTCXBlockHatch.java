package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.util.IGTCasingBackgroundBlock;
import gtclassic.api.model.GTModelOre;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
import ic2.core.platform.textures.obj.ILayeredBlockModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GTCXBlockHatch  extends GTCXBlockTile implements ILayeredBlockModel, ICustomModeledBlock {
    public static PropertyInteger casing = PropertyInteger.create("casing", 0, 3);
    public static PropertyInteger config = PropertyInteger.create("config", 0, 63);
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
    public boolean isLayered(IBlockState iBlockState) {
        return true;
    }

    @Override
    public int getLayers(IBlockState iBlockState) {
        return 2;
    }

    @Override
    public AxisAlignedBB getRenderBox(IBlockState iBlockState, int i) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public TextureAtlasSprite getLayerTexture(IBlockState state, EnumFacing facing, int i) {
        int cas = state.getValue(casing);
        if (i == 0){
            if (cas == 0){
                return this.getTextureFromState(state, facing);
            }
            return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[((cas - 1) * 16) + getIndex(facing, state)];
        }
        if (i == 1 && facing == state.getValue(allFacings)){
            return this == GTCXBlocks.dynamoHatch ? Ic2Icons.getTextures("dynamo_hatch_front_overlay")[0] : Ic2Icons.getTextures("machine_back_overlay")[0];
        }
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[26];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BaseModel getModelFromState(IBlockState state) {
        return new GTModelOre(this, state);
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

    public int getIndex(EnumFacing textureFacing, IBlockState state){
        int id = 7;
        int con = state.getValue(config);
        EnumFacing facing = state.getValue(allFacings);
        if (between(60, 62, con) || or(con, 51, 59, 55, 15, 47,31)){ // has at least 4 blocks around
            id = 6;
        }
        int facingIndex = facing.getHorizontalIndex() != -1 ? facing.getHorizontalIndex() : 0;
        if (textureFacing.getAxis() == EnumFacing.Axis.Y){ // if textureFacing is on top or bottom
            boolean u = textureFacing == EnumFacing.UP;
            if (between(4, 15, con)){
                id = facing.getAxis() == EnumFacing.Axis.X ? 0 : 1; // for blocks on one or both of the north and south faces
            }
            if (between(16, 19, con) || between(32, 35, con) || between(48, 51, con)){
                id = facing.getAxis() == EnumFacing.Axis.X ? 1 : 0; // for blocks on one or both of the east and west faces
            }
            int[] array = { 2, 3, 4, 5};
            // these 4 are for variations of 3 blocks on horizontal sizes
            if (between(28, 31, con)){
                id = u ? array[rotateSubtract(2, facingIndex)] : array[rotateAdd(2, facingIndex)];
            }
            if (between(44, 47, con)){
                id = u ? array[rotateSubtract(0, facingIndex)] : array[rotateAdd(0, facingIndex)];
            }
            if (between(52, 55, con)){
                id = u ? array[rotateSubtract(3, facingIndex)] : array[rotateAdd(1, facingIndex)];
            }
            if (between(56, 59, con)){
                id = u ? array[rotateSubtract(1, facingIndex)] : array[rotateAdd(3, facingIndex)];
            }
            int[] array2 = {8, 9, 11, 10};
            // these 4 are for variations of 2 blocks on horizontal sizes in 90 degree styles
            if (between(20, 23, con)){
                id = u ? array2[rotateSubtract(2, facingIndex)] : array2[rotateAdd( 1, facingIndex)];
            }
            if (between(36, 39, con)){
                id = u ? array2[rotateSubtract(3, facingIndex)] : array2[rotateAdd( 0, facingIndex)];
            }
            if (between(40, 43, con)){
                id = u ? array2[rotateSubtract(0, facingIndex)] : array2[rotateAdd( 3, facingIndex)];
            }
            if (between(24, 27, con)){
                id = u ? array2[rotateSubtract(1, facingIndex)] : array2[rotateAdd( 2, facingIndex)];
            }
        } else { // not vertical facings
            if (between(1, 3, con)){ // block on top, bottom, or both
                id = 1;
            }
            if (or(con, 14, 50, 30, 46, 58, 54)){ // block on opposite sides and bottom
                id = 5;
            }
            if (or(con, 13, 49, 29, 45, 57, 53)){ // block on opposite sides and top
                id = 3;
            }
            if (or(con, 12, 48, 20, 24, 36, 40)){ // block on opposite sides
                id = 0;
            }
            if (textureFacing.getAxis() == EnumFacing.Axis.Z){ // north or south facing
                int increase = textureFacing == EnumFacing.NORTH ? 0 : 1;
                if (or(con, 17, 21, 25)){ // block on bottom and west facing
                    id = 8 + increase;
                }
                if (or(con, 18, 22, 26)){ // block on top and west facing
                    id = 10 + increase;
                }
                if (or(con, 34, 38, 42)){ // block on top and east facing
                    id = 11 - increase;
                }
                if (or(con,33, 37, 41)){ // block on bottom and east facing
                    id = 9 - increase;
                }
                if (or(con, 35, 39, 43)){ // block on bottom, top, and east facing
                    id = textureFacing == EnumFacing.NORTH ? 4 : 2;
                }
                if (or(con, 19, 23, 27)){ // block on bottom, top, and west facing
                    id = textureFacing == EnumFacing.NORTH ? 2 : 4;
                }
                if (between(9, 11, con) || between(5, 7, con)){ // block on bottom, top, and north or south facing
                    id = 1;
                }
                if (or(con,16, 32, 52, 56)){ // block on north or south facing, though not both
                    id = 0;
                }
            } else {
                int increase = textureFacing == EnumFacing.EAST ? 0 : 1;
                if (or(con, 10, 26, 42)){ // block on top and south facing
                    id = 11 - increase;
                }
                if (or(con, 6, 22, 38)){ // block on top and north facing
                    id = 10 + increase;
                }
                if (or(con, 9, 25, 41)){ // block on bottom and south facing
                    id = 9 - increase;
                }
                if (or(con, 5, 21, 37)){ // block on bottom and north facing
                    id = 8 + increase;
                }
                if (or(con, 7, 23, 39)){ // block on bottom, top, and north facing
                    id = textureFacing == EnumFacing.WEST ? 4 : 2;
                }
                if (or(con, 11, 27, 43)){ // block on bottom, top, and south facing
                    id = textureFacing == EnumFacing.WEST ? 2 : 4;
                }
                if (between(17, 19, con) || between(33, 35, con)){ // block on bottom, top, and west or east facing
                    id = 1;
                }
                if (or(con, 4, 8,28, 44)){ // block on west or east facing, though not both
                    id = 0;
                }
            }
        }
        return id;
    }

    public int rotateAdd(int indexStart, int addition){
        return (indexStart + addition) % 4;
    }

    public int rotateSubtract(int indexStart, int subtraction){
        int index = (indexStart - subtraction);
        return index < 0 ? 4 + index : index;
    }

    public boolean or(int compare, int... values){
        for (int i : values){
            if (compare == i){
                return true;
            }
        }
        return false;
    }

    public boolean between(int min, int max, int compare){
        return compare >= min && compare <= max;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.getTileEntity(pos) instanceof IGTCasingBackgroundBlock){
            ((IGTCasingBackgroundBlock)worldIn.getTileEntity(pos)).setCasing();
            ((IGTCasingBackgroundBlock)worldIn.getTileEntity(pos)).setConfig();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        if (world.getTileEntity(pos) instanceof IGTCasingBackgroundBlock){
            ((IGTCasingBackgroundBlock)world.getTileEntity(pos)).setCasing();
            ((IGTCasingBackgroundBlock)world.getTileEntity(pos)).setConfig();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, casing, allFacings, active, config);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(casing, 0).withProperty(active, false).withProperty(allFacings, EnumFacing.NORTH).withProperty(config, 0);
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        EnumFacing[] facings = EnumFacing.VALUES;
        int facingsLength = facings.length;

        for(int i = 0; i < facingsLength; ++i) {
            for (int j = 0; j < 4; j++){
                EnumFacing side = facings[i];
                for (int k = 0; k < 64; k++){
                    states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(casing, j).withProperty(config, k));
                    states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(casing, j).withProperty(config, k));
                }

            }
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

            return state.withProperty(active, block.getActive()).withProperty(casing, block.getCasing()).withProperty(config, block.getConfig());
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, EnumFacing.NORTH);
            }

            return state.withProperty(active, false).withProperty(casing, 0).withProperty(config, 0);
        }
    }
}
