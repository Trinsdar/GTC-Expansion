package gtc_expansion.model;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXIcons;
import gtc_expansion.block.GTCXBlockCasing;
import gtclassic.api.interfaces.IGTColorBlock;
import ic2.core.RotationList;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Ic2Models;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ITexturedBlock;
import ic2.core.util.helpers.BlockStateContainerIC2;
import ic2.core.util.helpers.ConnectionState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

import static net.minecraft.util.EnumFacing.*;

public class GTCXModelCasing extends BaseModel {

    List<BakedQuad>[][][] quads;
    ITexturedBlock block;
    IBlockState meta;
    int index;

    public GTCXModelCasing(ITexturedBlock model, IBlockState state, int index) {
        super(Ic2Models.getBlockTransforms());
        this.block = model;
        this.meta = state;
        this.index = index;
    }

    public void init() {
        this.quads = this.createTripleList(9,7, 64);
        this.setParticalTexture(this.block.getParticleTexture(this.meta));
        boolean color = this.block instanceof IGTColorBlock;
        EnumFacing blockFacing = EnumFacing.NORTH;
        ModelRotation rotation = ModelRotation.X0_Y0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 64; j++){
                for (int k = 0; k < 9; k++){
                    AxisAlignedBB box = this.block.getRenderBoundingBox(this.meta);
                    ModelRotation sideRotation;
                    BlockPartFace face;
                    TextureAtlasSprite sprite;
                    for (EnumFacing facing : EnumFacing.values()) {
                        sideRotation = this.getRotation(blockFacing, facing, rotation);
                        face = this.createBlockFace(facing, i, color);
                        if (i == 0){
                            sprite =  Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 16) + getIndexes(facing, j)];
                        } else {
                            sprite = meta.getValue(GTCXBlockCasing.allFacings) == facing && this.index < 2 && k > 0 ? this.getTextureFromRotor(meta.getValue(GTCXBlockCasing.active), k) : this.block.getTextureFromState(this.meta, facing);
                        }

                        if (sprite != null) {
                            this.quads[k][facing.getIndex()][j].add(this.getBakery().makeBakedQuad(this.getMinBox(facing, box), this.getMaxBox(facing, box), face, sprite, facing, sideRotation, (BlockPartRotation) null, false, true));
                        }
                    }
                }
            }

        }
    }

    public TextureAtlasSprite getTextureFromRotor(boolean active, int rotor){
        int xOffset = active ? 3 : 0;
        int yOffset = this.index == 0 ? 3 : 0;
        return GTCXIcons.s(getX(rotor) + xOffset, 8 + yOffset + getY(rotor)).getSprite();
    }

    public int getX(int rotor){
        switch (rotor){
            case 1:
            case 6:
            case 4:
                return  0;
            case 2:
            case 7:
                return  1;
            case 3:
            case 8:
            case 5:
                return  2;
            default: return -1;
        }
    }

    public int getY(int rotor){
        switch (rotor){
            case 1:
            case 3:
            case 2:
                return  0;
            case 4:
            case 5:
                return  1;
            case 6:
            case 8:
            case 7:
                return  2;
            default: return -1;
        }
    }

    public int getIndexes(EnumFacing textureFacing, int config) {
        RotationList list = RotationList.ofNumber(config).remove(textureFacing).remove(textureFacing.getOpposite());
        ConnectionState connectionState = ConnectionState.fromList(list, textureFacing);
        boolean positive = textureFacing.getAxisDirection() == AxisDirection.POSITIVE;
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (list.size() == 0 || list.size() == 4) {
            return list.size() == 4 ? 6 : 7;
        }
        int index = connectionState.getIndex();
        int[] array;
        if (list.size() == 1){
            positive = (textureFacing.getAxis() == EnumFacing.Axis.X) == positive;
            array = textureFacing.getAxis() == EnumFacing.Axis.Y ? (!containsAxis(list, EnumFacing.Axis.X) && textureFacing != DOWN ? new int[]{13, 14, 15, 12} : new int[]{15, 14, 13 ,12}) : (!containsAxis(list, EnumFacing.Axis.Y) && !positive ? new int[]{15, 12, 13, 14} : new int[]{15, 14, 13, 12});
            return array[index];
        }
        if (list.size() == 2){
            if (isOpposites(list)){
                offset = (textureFacing.getAxis() == EnumFacing.Axis.Y && containsAxis(list, EnumFacing.Axis.Z)) || containsAxis(list, EnumFacing.Axis.Y) ? 1 : 0;
                return index + offset;
            }
//            positive = (textureFacing.getAxis() == Axis.X) == positive;
//            array = positive ? new int[]{8, 9, 10, 11} : new int[]{11, 8, 9, 10};
//            return array[index];
        }
        int result = 0;
        int additive = 0;
        for(EnumFacing facing : list) {
            if (list.size() == 2){
                result += getAdditive(textureFacing, facing);
            } else {
                additive += getAdditiveWith3(textureFacing, facing);
            }
        }
        if (additive > 5 && additive < 10){
            result = additive == 6 ? 2 : additive == 7 ? 4 : additive == 8 ? 3 : 5;
        }
        return result;
    }

    protected boolean isOpposites(RotationList list){
        return list.contains(RotationList.VERTICAL) || list.contains(RotationList.X_AXIS) || list.contains(RotationList.Z_AXIS);
    }

    protected boolean containsAxis(RotationList list, EnumFacing.Axis axis){
        switch (axis){
            case X: return list.contains(EAST) || list.contains(WEST);
            case Z: return list.contains(NORTH) || list.contains(SOUTH);
            case Y: return list.contains(UP) || list.contains(DOWN);
            default: return false;
        }
    }

    protected int getAdditiveWith3(EnumFacing textureFacing, EnumFacing facing){
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (textureFacing.getAxis() != Axis.Y){
            return convert(textureFacing, facing) == 0 ? 1 : convert(textureFacing, facing) == 1 ? 2 : convert(textureFacing, facing) == 2 ? 3 + offset : 4 - offset;
        }
        return convert(textureFacing, facing) == 3 ? 1 + offset : convert(textureFacing, facing) == 2 ? 2 - offset : convert(textureFacing, facing) == 1 ? 3 : 4;
    }

    protected int getAdditive(EnumFacing textureFacing, EnumFacing facing){
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (textureFacing.getAxis() != Axis.Y){
            return convert(textureFacing, facing) == 0 ? 5 : convert(textureFacing, facing) == 1 ? 7 : convert(textureFacing, facing) == 2 ? 3 + offset : 4 - offset;
        }
        return convert(textureFacing, facing) == 3 ? 5 + (offset * 2) : convert(textureFacing, facing) == 2 ? 7 - (offset * 2) : convert(textureFacing, facing) == 1 ? 3 : 4;
    }

    protected int convert(EnumFacing side, EnumFacing index)
    {
        switch(side.getAxis())
        {
            case X:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Y:
                switch(index)
                {
                    case WEST: return 0;
                    case EAST: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Z:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case WEST: return 3;
                    case EAST: return 2;
                    default: return -1;
                }
        }
        return 0;
    }

    protected ModelRotation getRotation(EnumFacing facing, EnumFacing side, ModelRotation defaultRotation) {
        if (facing.getAxis().isHorizontal() && side.getAxis().isVertical()) {
            return defaultRotation;
        } else {
            return facing.getAxis().isVertical() && side.getAxis() == EnumFacing.Axis.X ? defaultRotation : ModelRotation.X0_Y0;
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        int i;
        int j;
        if (!(state instanceof BlockStateContainerIC2.IC2BlockState)) {
            i = 0;
            j = 0;
        } else {
            GTCXBlockCasing.IntWrapper wrapper = ((BlockStateContainerIC2.IC2BlockState) state).getData();
            i = wrapper.getFirst();
            j = wrapper.getSecond();
        }
        return this.quads[j][side == null ? 6 : side.getIndex()][i];
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, PerspectiveMapWrapper.handlePerspective(this, this.getCamera(), cameraTransformType).getRight());
    }

    protected BlockPartFace createBlockFace(EnumFacing side, int layer, boolean color) {
        return new BlockPartFace((EnumFacing) null, -1, "", new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F,
                16.0F }, 0));
    }

    private List<BakedQuad>[][][] createTripleList(int first, int second, int third){
        List<BakedQuad>[][][] list = new List[first][second][third];
        for (int i = 0; i < first; i++){
            list[i] = this.createDoubleList(second, third);
        }
        return list;
    }

    private List<BakedQuad>[][] createDoubleList(int first, int second){
        List<BakedQuad>[][] list = new List[first][second];
        for (int i = 0; i < first; i++){
            list[i] = this.createList(second);
        }
        return list;
    }
}
