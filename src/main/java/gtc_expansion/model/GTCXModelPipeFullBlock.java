package gtc_expansion.model;

import gtc_expansion.block.GTCXBlockPipe.GTCXBlockState;
import gtclassic.api.block.GTBlockBaseConnect;
import ic2.core.RotationList;
import ic2.core.block.base.util.texture.TextureCopyStorage;
import ic2.core.platform.textures.Ic2Models;
import ic2.core.platform.textures.models.BaseModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GTCXModelPipeFullBlock extends BaseModel {
    List<BakedQuad>[][] quads; // All possible connection configurations
    IBlockState state;
    // functions
    int[] sizes; // This is an int array to draw the size of the cable, [0, 16] would be a full
    // block, [4, 12] would be half a block.
    public GTCXModelPipeFullBlock(IBlockState block, int[] sizes) {
        super(Ic2Models.getBlockTransforms());
        this.state = block;
        this.sizes = sizes;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void init() {
        GTBlockBaseConnect wire = (GTBlockBaseConnect) this.state.getBlock();
        //IGTCoverBlock pipe = (IGTCoverBlock) this.state.getBlock();
        quads = this.createDoubleList(64, 7);
        this.setParticalTexture(wire.getParticleTexture(this.state));
        int min = this.sizes[0];// low size
        int max = this.sizes[1];// high size
        Map<EnumFacing, BakedQuad> coreQuads = this.generateCoreQuads(wire, min, max, false);
        Map<EnumFacing, BakedQuad> coreQuadsSided = this.generateCoreQuads(wire, min, max, true);
        Map<EnumFacing, List<BakedQuad>> sideQuads = new EnumMap(EnumFacing.class);
        EnumFacing[] facings = EnumFacing.VALUES;
        int facingsLength = facings.length;
        for (int i = 0; i < facingsLength; ++i) {
            EnumFacing side = facings[i];
            sideQuads.put(side, this.generateQuadsForSide(wire, side, min, max));
        }

        for (int j = 0; j < 64; ++j) {
            RotationList rotation = RotationList.ofNumber(j);
            List<BakedQuad>[] quadList = this.quads[j];
            Iterator rotations = rotation.iterator();
            EnumFacing side;
            while (rotations.hasNext()) {
                side = (EnumFacing) rotations.next();
                quadList[side.getIndex()].addAll(sideQuads.get(side));
                quadList[6].addAll(sideQuads.get(side));
            }
            rotations = rotation.invert().iterator();
            while (rotations.hasNext()) {
                side = (EnumFacing) rotations.next();
                BakedQuad quad = coreQuads.get(side);
                if (rotation.size() >= 1){
                    quad = coreQuadsSided.get(side);
                }
                /*if (rotation.size() == 1){
                    for (EnumFacing facing : EnumFacing.VALUES){
                        if (rotation.contains(facing) && side != facing.getOpposite()){
                            quad = coreQuadsSided.get(side);
                            break;
                        }
                    }
                }*/
                quadList[side.getIndex()].add(quad);
                quadList[6].add(quad);
            }
        }
    }

    // Merges core and anchor quads to create all quads
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == null) {
            if (!(state instanceof GTCXBlockState)) {
                // if its in jei/creative tab it will default to the int passed below, 12 =
                // (4+8) (north+south)
                return this.quads[12][6];
            } else {
                return this.getEmptyList();
            }
        } else {
            if (state instanceof GTCXBlockState){
                TextureCopyStorage.QuadList[] quadList = ((GTCXBlockState)state).getData();
                Vec3i vec = ((GTCXBlockState)state).getData2();
                if (vec.getY() > 0) {
                    List<BakedQuad> covers = new ArrayList<>(quadList[side.getIndex()].getQuads());
                    List<BakedQuad> list = new ArrayList(this.quads[vec.getX()][side.getIndex()]);
                    return covers.isEmpty() ? list : covers;
                } else {
                    return this.quads[vec.getX()][side.getIndex()];
                }
            }
            return this.getEmptyList();
        }
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

    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return PerspectiveMapWrapper.handlePerspective(this, this.getCamera(), cameraTransformType);
    }

    // This is where the basic/core model quads are generated
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<EnumFacing, BakedQuad> generateCoreQuads(GTBlockBaseConnect wire, int min, int max, boolean hasSide) {
        Vector3f minF = new Vector3f((float) min, (float) min, (float) min);
        Vector3f maxF = new Vector3f((float) max, (float) max, (float) max);
        BlockPartFace face = new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] { (float) min,
                (float) min, (float) max, (float) max }, 0));
        Map<EnumFacing, BakedQuad> quads = new EnumMap(EnumFacing.class);
        EnumFacing[] facings = EnumFacing.VALUES;
        for (EnumFacing side : facings) {
            TextureAtlasSprite sprite = wire.getTextureFromState(this.state, (hasSide ? EnumFacing.NORTH : EnumFacing.UP));
            quads.put(side, this.getBakery().makeBakedQuad(minF, maxF, face, sprite, side, ModelRotation.X0_Y0, null, true, true));
        }
        return quads;
    }

    // This is where the sides connected to things are generated
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<BakedQuad> generateQuadsForSide(GTBlockBaseConnect wire, EnumFacing facing, int min, int max) {
        List<BakedQuad> quads = new ArrayList();
        Pair<Vector3f, Vector3f> position = this.getPosForSide(facing, min, max, 0);
        BlockPartFace face = new BlockPartFace(null, 0, "", new BlockFaceUV(new float[]{(float) min,
                (float) min, (float) max, (float) max}, 0));
        // If you would like a different texture for connected sides, change the sprite
        // var to what you want
        TextureAtlasSprite sprite = wire.getTextureFromState(this.state, EnumFacing.UP);
        quads.add(this.getBakery().makeBakedQuad(position.getKey(), position.getValue(), face, sprite, facing, ModelRotation.X0_Y0, null, true, true));
        return quads;
    }

    private Pair<Vector3f, Vector3f> getPosForSide(EnumFacing facing, int min, int max, int offset) {
        switch (facing) {
            case DOWN:
                return Pair.of(new Vector3f(min, 0.0F, min), new Vector3f(max, min + offset, max));
            case UP:
                return Pair.of(new Vector3f(min, max - offset, min), new Vector3f(max, 16.0F, max));
            case NORTH:
                return Pair.of(new Vector3f(min, min, 0.0F), new Vector3f(max, max, min + offset));
            case SOUTH:
                return Pair.of(new Vector3f(min, min, max - offset), new Vector3f(max, max, 16.0F));
            case WEST:
                return Pair.of(new Vector3f(0.0F, min, min), new Vector3f(min + offset, max, max));
            case EAST:
                return Pair.of(new Vector3f(max - offset, min, min), new Vector3f(16.0F, max, max));
        }
        return Pair.of(new Vector3f(min, min, min), new Vector3f(max, max, max));
    }

    // The zeros passed as the second arg in the BlockPartFace constructor make it
    // so the face is colorable, make it -1 if you dont want it to be colored
    private BlockPartFace getFace(EnumFacing facing, int min, int max, int index, int rotation) {
        switch (facing) {
            case DOWN:
            case SOUTH:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { min, max, max, 16.0F }, rotation));
            case UP:
            case NORTH:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { min, 0.0F, max, min }, rotation));
            case WEST:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { 0.0F, min, min, max }, rotation));
            case EAST:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { max, min, 16.0F, max }, rotation));
        }
        return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, rotation));
    }

    private BlockPartFace getAnchorFace(EnumFacing facing, int min, int max, int index, int rotation) {
        switch (facing) {
            case DOWN:
            case SOUTH:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { min + 2, max, max, 16.0F }, rotation));
            case UP:
            case NORTH:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { min + 2, 0.0F, max, min }, rotation));
            case WEST:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { 2.0F, min, min, max }, rotation));
            case EAST:
                return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { max - 2, min, 16.0F, max }, rotation));
        }
        return new BlockPartFace(null, index, "", new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, rotation));
    }

    private List<BakedQuad>[][] createDoubleList(int first, int second){
        List<BakedQuad>[][] list = new List[first][second];
        for (int i = 0; i < first; i++){
            list[i] = this.createList(second);
        }
        return list;
    }
}
