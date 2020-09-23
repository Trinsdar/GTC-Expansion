package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTCoverBlock;
import gtc_expansion.item.itemblock.GTCXItemBlockPipe;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.model.GTCXModelPipe;
import gtc_expansion.model.GTCXModelPipeFullBlock;
import gtc_expansion.tile.pipes.GTCXTileBaseItemPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtc_expansion.util.GTCXMaterialWrench;
import gtc_expansion.util.GTCXWrenchUtils;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseConnect;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.interfaces.IGTItemBlock;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.item.block.ItemBlockRare;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static gtc_expansion.util.GTCXHelperPipe.GTPipeModel.*;

public class GTCXBlockPipe extends GTBlockBaseConnect implements IGTCoverBlock, IGTColorBlock, IGTItemBlock {
    public static final Material PIPE = new GTCXMaterialWrench(true);
    GTMaterial material;
    GTCXHelperPipe.GTPipeModel type;
    boolean item;
    public GTCXBlockPipe(GTMaterial material, GTCXHelperPipe.GTPipeModel type){
        super(PIPE);
        setUnlocalizedName(GTCExpansion.MODID + "." + type.getSuffix() + material.getName() + "_pipe");
        setRegistryName(type.getSuffix() + material.getName() + "_pipe");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        setCreativeTab(GTMod.creativeTabGT);
        this.material = material;
        this.type = type;
        this.item = material.equals(GTCXMaterial.Brass);
        this.setHarvestLevel("wrench", 1);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return type.equals("wrench");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getCoverTexture(EnumFacing facing) {
        return null;
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileBaseItemPipe();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing side) {
        int open = type == SMALL ? 8 : type == MED ? 9 : type == LARGE ? 10 : type == HUGE ? 11 : 12;
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[side == EnumFacing.UP ? open : 7];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return this.getTextureFromState(state, EnumFacing.SOUTH);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BaseModel getModelFromState(IBlockState iBlockState) {
        if (type == QUAD || type == HUGE){
            return new GTCXModelPipeFullBlock(iBlockState, type.getSizes());
        }
        return new GTCXModelPipe(iBlockState, type.getSizes());
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof GTCXTileBasePipe) {
                GTCXTileBasePipe pipe = (GTCXTileBasePipe)tile;
                if (this.type == QUAD || type == HUGE){
                    return new GTCXBlockState(state, pipe.storage.getQuadList(), pipe.getConnections());
                }
                return new GTCXBlockState(state, pipe.storage.getQuads(), pipe.getConnections());
            }
        } catch (Exception e) {
            GTCExpansion.logger.info("IC2BlockState Failed");
            e.printStackTrace();
        }

        return super.getExtendedState(state, world, pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState();
    }

    @Override
    public List<IBlockState> getValidStates() {
        return this.getBlockState().getValidStates();
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList();
        states.add(def);
        return states;
    }

    @Override
    public EnumFacing getRotation(IBlockState state) {
        return EnumFacing.NORTH;
    }

    @Override
    public boolean hasFacing() {
        return false;
    }

    @Override
    public List<IBlockState> getValidModelStates() {
        return this.getBlockState().getValidStates();
    }

    @Override
    public boolean canSetFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        NBTTagCompound nbt = StackUtil.getNbtData(stack);
        if (tile instanceof GTCXTileBasePipe){
            GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
            if (nbt.hasKey("color")) {
                pipe.setTileColor(nbt.getInteger("color"));
            } else {
                pipe.setTileColor(material.getColor().getRGB());
            }
            pipe.setModel(this.type);
            pipe.setMaterial(this.material);
            if (placer instanceof EntityPlayer){
                for (EnumFacing facing : EnumFacing.values()){
                    boolean lookingAt = false;

                    RayTraceResult rt1 = GTCXWrenchUtils.getBlockLookingat1((EntityPlayer) placer, pos);
                    RayTraceResult rt2 = GTCXWrenchUtils.getBlockLookingat2((EntityPlayer) placer, pos);


                    if (rt1 != null) {
                        if (GTCXWrenchUtils.arePosEqual(rt1.getBlockPos(), pos.offset(facing, 1))) lookingAt = true;
                    }
                    if (rt2 != null) {
                        if (GTCXWrenchUtils.arePosEqual(rt2.getBlockPos(), pos.offset(facing, 1))) lookingAt = true;
                    }

                    if (lookingAt){
                        TileEntity offset = worldIn.getTileEntity(pos.offset(facing));
                        if (offset instanceof GTCXTileBasePipe && pipe.canConnect(offset, facing)){
                            pipe.addConnection(facing);
                            ((GTCXTileBasePipe)offset).addConnection(facing.getOpposite());
                        } else if (offset != null && ((item && offset.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) || (!item && offset.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())))){
                            pipe.addConnection(facing);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileBasePipe) {
            GTCXTileBasePipe colorTile = (GTCXTileBasePipe) tile;
            int color1 = color == EnumDyeColor.WHITE ? material.getColor().getRGB() : GTValues.getColorFromEnumDyeColor(color);
            colorTile.setTileColor(color1);
            IBlockState state = tile.getWorld().getBlockState(tile.getPos());
            world.notifyBlockUpdate(pos, state, state, 2);
            return true;
        }
        return false;
    }

    @Override
    public Color getColor(IBlockState iBlockState, IBlockAccess worldIn, BlockPos pos, Block block, int i) {
        if (worldIn != null) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof IGTRecolorableStorageTile) {
                IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
                return colorTile.getTileColor();
            }
        }
        return material.getColor();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof GTCXTileBasePipe){
            return ((GTCXTileBasePipe)tileEntity).anchors.contains(face) || this.type == QUAD ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player, int fortune) {
        List<ItemStack> list = new ArrayList<>();
        if (te instanceof IGTItemContainerTile){
            list.addAll(((IGTItemContainerTile) te).getDrops());
            return list;
        }
        return list;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList<>();
        TileEntity te = this.getLocalTile() == null ? world.getTileEntity(pos) : this.getLocalTile();
        if (te instanceof IGTItemContainerTile){
            list.addAll(((IGTItemContainerTile) te).getDrops());
            return list;
        }
        return list;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        state = this.getActualState(state, world, pos);
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileBasePipe){
            GTCXTileBasePipe cable = (GTCXTileBasePipe) tile;
            if (cable.isColored() && cable.color != material.getColor().getRGB()) {
                nbt.setInteger("color", cable.color);
            }
        }
        return stack;
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

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (type == HUGE || type == QUAD){
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof GTCXTileBasePipe){
            return ((GTCXTileBasePipe)tileEntity).anchors.contains(side);
        }
        return super.isSideSolid(base_state, world, pos, side);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return type == QUAD || type == HUGE;
    }

    public boolean isBlockNormalCube(IBlockState state) {
        return type == QUAD || type == HUGE;
    }

    public boolean isFullCube(IBlockState state) {
        return type == QUAD || type == HUGE;
    }

    @Override
    public Class<? extends ItemBlockRare> getCustomItemBlock() {
        return GTCXItemBlockPipe.class;
    }

    public static class GTCXBlockState extends BlockStateContainerIC2.IC2BlockState{
        public Object data2;
        public GTCXBlockState(IBlockState state, Object toInject1, Object toInject2) {
            super(state, toInject1);
            this.data2 = toInject2;
        }

        public <T> T getData2() {
            return (T) this.data2;
        }

        public <T> T getData2(Class<T> clz) {
            return (T) this.data2;
        }

        public int getAsInteger() {
            return !(this.data instanceof Integer) ? 0 : (Integer)this.data2;
        }
    }
}
