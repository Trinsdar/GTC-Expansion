package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.tile.GTCXTileCasing;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import gtclassic.api.model.GTModelOre;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
import ic2.core.platform.textures.obj.ILayeredBlockModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

import static net.minecraft.util.EnumFacing.AxisDirection;
import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraft.util.EnumFacing.VALUES;
import static net.minecraft.util.EnumFacing.WEST;

public class GTCXBlockCasing extends GTBlockBaseMachine implements ILayeredBlockModel, ICustomModeledBlock {
    public static PropertyInteger rotor = PropertyInteger.create("rotor", 0, 8);
    public static PropertyInteger config = PropertyInteger.create("config", 0, 63);
    int index;
    public GTCXBlockCasing(String name, LocaleComp comp, int index, float resistance) {
        super(Material.IRON, comp, 0);
        setRegistryName(name.toLowerCase());
        this.setCreativeTab(GTMod.creativeTabGT);
        this.setSoundType(SoundType.METAL);
        this.setResistance(resistance);
        this.setHardness(3.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.index = index;
    }

    @Override
    public GTCXBlockCasing setHardness(float hardness) {
        return (GTCXBlockCasing) super.setHardness(hardness);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 16) + getIndexes(enumFacing, state)];
    }

    public int getIndexes(EnumFacing textureFacing, IBlockState state) {
        int con = state.getValue(config);
        EnumFacing blockFacing = state.getValue(allFacings);
        RotationList list = RotationList.ofNumber(con).remove(textureFacing).remove(textureFacing.getOpposite());
        if (list.size() == 0 || list.size() == 4) {
            return list.size() == 4 ? 6 : 7;
        }
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (textureFacing.getAxis() != EnumFacing.Axis.Y){
            if (list.size() == 1){
                return containsAxis(list, EnumFacing.Axis.Y) ? (list.contains(UP) ? 15 : 13) : (list.contains(WEST) || list.contains(SOUTH) ? 14 - (offset * 2) : 12 + (offset * 2));
            }
            if (list.size() == 2){
                if (!containsAxis(list, EnumFacing.Axis.Y)){
                    return 0;
                }
                if (!containsAxis(list, EnumFacing.Axis.Z) && !containsAxis(list, EnumFacing.Axis.X)){
                    return  1;
                }
            }
        }
        int result = 0;
        if (textureFacing.getAxis() == EnumFacing.Axis.Y){
            if (list.size() == 1){
                return list.contains(EAST) ? 12 : list.contains(SOUTH) ? 13 + (offset * 2) : list.contains(WEST) ? 14 : 15 - (offset * 2);
            }
            if (list.size() == 2 && ((!containsAxis(list, EnumFacing.Axis.X) && containsAxis(list, EnumFacing.Axis.Z)) || (!containsAxis(list, EnumFacing.Axis.Z) && containsAxis(list, EnumFacing.Axis.X)))){
                return containsAxis(list, EnumFacing.Axis.X) ? 0 : 1;
            }
        }
        int additive = 0;
        for(EnumFacing facing : list) {
            if (list.size() == 2){
                result += getAdditive(textureFacing, facing, blockFacing);
            } else {
                additive += getAdditiveWith3(textureFacing, facing, blockFacing);
            }
        }
        if (additive > 5 && additive < 10){
            result = additive == 6 ? 2 : additive == 7 ? 4 : additive == 8 ? 3 : 5;
        }
        return result;
    }

    protected boolean containsAxis(RotationList list, EnumFacing.Axis axis){
        switch (axis){
            case X: return list.contains(EAST) || list.contains(WEST);
            case Z: return list.contains(NORTH) || list.contains(SOUTH);
            case Y: return list.contains(UP) || list.contains(DOWN);
            default: return false;
        }
    }

    protected int getAdditiveWith3(EnumFacing textureFacing, EnumFacing facing, EnumFacing blockFacing){
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (textureFacing.getAxis() != EnumFacing.Axis.Y){
            return convert(textureFacing, facing) == 0 ? 1 : convert(textureFacing, facing) == 1 ? 2 : convert(textureFacing, facing) == 2 ? 3 + offset : 4 - offset;
        }
        return convert(textureFacing, facing) == 3 ? 1 + offset : convert(textureFacing, facing) == 2 ? 2 - offset : convert(textureFacing, facing) == 1 ? 3 : 4;
    }

    protected int getAdditive(EnumFacing textureFacing, EnumFacing facing, EnumFacing blockFacing){
        int offset = textureFacing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 1;
        if (textureFacing.getAxis() != EnumFacing.Axis.Y){
            return convert(textureFacing, facing) == 0 ? 5 : convert(textureFacing, facing) == 1 ? 7 : convert(textureFacing, facing) == 2 ? 3 + offset : 4 - offset;
        }
        return convert(textureFacing, facing) == 3 ? 5 + (offset * 2) : convert(textureFacing, facing) == 2 ? 7 - (offset * 2) : convert(textureFacing, facing) == 1 ? 3 : 4;
    }

    @Override
    public boolean hasRotation(IBlockState state) {
        return false;
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

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 12) + 7];
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
        if (i == 0){
            return this.getTextureFromState(state, facing);
        }
        if (i == 1 && facing == state.getValue(allFacings) && state.getValue(rotor) > 0 && (this == GTCXBlocks.casingStandard || this == GTCXBlocks.casingReinforced)){
            return  getTextureFromRotor(state.getValue(active), state.getValue(rotor));
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

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileCasing();
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    public TextureAtlasSprite getTextureFromRotor(boolean active, int rotor){
        String activeTexture = active ? "active_" : "";
        String steam = this == GTCXBlocks.casingStandard ? "steam" : "gas";
        String location = getLocation(rotor);
        return Ic2Icons.getTextures(steam + "_turbine_front_" + activeTexture + location)[0];
    }

    public String getLocation(int rotor){
        switch (rotor){
            case 1: return  "top_left";
            case 2: return  "top";
            case 3: return  "top_right";
            case 4: return  "left";
            case 5: return  "right";
            case 6: return  "bottom_left";
            case 7: return  "bottom";
            case 8: return  "bottom_right";
            default: return "";
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, rotor, allFacings, active, config);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(rotor, 0).withProperty(active, false).withProperty(allFacings, NORTH).withProperty(config, 0);
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        EnumFacing[] facings = VALUES;
        int facingsLength = facings.length;

        for(int i = 0; i < facingsLength; ++i) {
            for (int j = 0; j < 9; j++){
                EnumFacing side = facings[i];
                for (int k = 0; k < 64; k++){
                    states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(rotor, j).withProperty(config, k));
                    states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(rotor, j).withProperty(config, k));
                }

            }
        }

        return states;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        GTCXTileCasing block = (GTCXTileCasing)worldIn.getTileEntity(pos);
        if (block != null) {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, block.getFacing());
            } else {
                state = state.withProperty(allFacings, NORTH);
            }

            return state.withProperty(active, block.getActive()).withProperty(rotor, block.getRotor()).withProperty(config, block.getConfig());
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, NORTH);
            }

            return state.withProperty(active, false).withProperty(rotor, 0).withProperty(config, 0);
        }
    }

    @Override
    public boolean hasFacing() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.gtclassic.nomobs"));
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList();
        list.add(new ItemStack(this));
        return list;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileCasing){
            ((GTCXTileCasing)tile).setNeighborMap(this);
            if (this == GTCXBlocks.casingStandard){
                ((GTCXTileCasing)tile).setRotor(this);
            }
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileCasing){
            ((GTCXTileCasing)tile).setNeighborMap(this);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileCasing){
            ((GTCXTileCasing)tile).setNeighborMap(this);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (this == GTCXBlocks.iridiumTungstensteelBlock || this == GTCXBlocks.tungstensteelReinforcedStone){
            if (entity instanceof EntityWither || entity instanceof EntityWitherSkull){
                return false;
            }
            return super.canEntityDestroy(state, world, pos, entity);
        }
        return super.canEntityDestroy(state, world, pos, entity);
    }
}
