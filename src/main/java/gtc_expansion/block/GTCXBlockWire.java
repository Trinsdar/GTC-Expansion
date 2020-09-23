package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.model.GTModelLayeredAnchoredWire;
import gtc_expansion.tile.wiring.GTCXTileAluminiumCable;
import gtc_expansion.tile.wiring.GTCXTileColoredCable;
import gtc_expansion.tile.wiring.GTCXTileElectrumCable;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseConnect;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.interfaces.IGTColorBlock;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTReaderInfoBlock;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import ic2.api.classic.item.ICutterItem;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.render.model.BlockCopyModel;
import ic2.core.block.wiring.BlockCable;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2States;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ILayeredBlockModel;
import ic2.core.util.helpers.BlockStateContainerIC2;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GTCXBlockWire extends GTBlockBaseConnect implements IGTColorBlock, ILayeredBlockModel, IGTReaderInfoBlock {
    public static final PropertyInteger INSULATION = PropertyInteger.create("insulation", 0, 3);
    public static final String NBT_INSULATION = "insulation";
    public static final PropertyInteger FOAMED = PropertyInteger.create("foamed", 0, 2);
    GTMaterial material;
    public GTCXBlockWire(String name, LocaleComp comp, GTMaterial material){
        super();
        setUnlocalizedName(comp);
        setRegistryName(name);
        this.setHardness(0.2F);
        this.setSoundType(SoundType.CLOTH);
        this.setHarvestLevel("axe", 0);
        setCreativeTab(GTMod.creativeTabGT);
        this.material = material;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, INSULATION, FOAMED);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        NBTTagCompound nbt = StackUtil.getNbtData(stack);
        if (tile instanceof GTCXTileColoredCable){
            GTCXTileColoredCable wireTile = (GTCXTileColoredCable) tile;
            if (nbt.hasKey(NBT_INSULATION)) {
                wireTile.setInsulation(nbt.getInteger(NBT_INSULATION));
            }
            if (nbt.hasKey("color")) {
                wireTile.setTileColor(nbt.getInteger("color"));
            } else {
                wireTile.setTileColor(material.getColor().getRGB());
            }
        }
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable) {
            GTCXTileColoredCable colorTile = (GTCXTileColoredCable) tile;
            if (!colorTile.hasInsulation() && colorTile.foamed < 2){
                return false;
            }
            int color1 = color == EnumDyeColor.WHITE ? material.getColor().getRGB() : GTValues.getColorFromEnumDyeColor(color);
            colorTile.setTileColor(color1, side);
            IBlockState state = tile.getWorld().getBlockState(tile.getPos());
            world.notifyBlockUpdate(pos, state, state, 2);
            return true;
        }
        return false;
    }

    @Override
    public Color getColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, Block block, int index) {
        if (state != null && state.getValue(FOAMED) > 0){
            if (worldIn != null) {
                TileEntity tile = worldIn.getTileEntity(pos);
                if (tile instanceof GTCXTileColoredCable && state.getValue(FOAMED) > 1) {
                    GTCXTileColoredCable colorTile = (GTCXTileColoredCable) tile;
                    int dir = index / 50;
                    int tintIndex = index % 50;
                    int[] colors = colorTile.getStorage().getEntry(dir).getColorMultiplier();
                    if (tintIndex >= colors.length){
                        return Color.WHITE;
                    }
                    return new Color(colors[tintIndex]);
                }
            }
            return Color.WHITE;
        } else {
            if (index == 0){
                return material.getColor();
            } else if (index == 1){
                if (worldIn != null) {
                    TileEntity tile = worldIn.getTileEntity(pos);
                    if (tile instanceof IGTRecolorableStorageTile) {
                        IGTRecolorableStorageTile colorTile = (IGTRecolorableStorageTile) tile;
                        return colorTile.getTileColor();
                    }
                }
                return state != null && state.getValue(INSULATION) > 0? new Color(4, 4, 4) : material.getColor();
            } else {
                return Color.WHITE;
            }
        }
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        if (this == GTCXBlocks.electrumCable){
            return new GTCXTileElectrumCable();
        }
        if (this == GTCXBlocks.aluminiumCable){
            return new GTCXTileAluminiumCable();
        }
        return null;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable) {
            GTCXTileColoredCable cable = (GTCXTileColoredCable)tile;
            int i = cable.insulation > 3 ? 3 : cable.insulation;
            return state.withProperty(INSULATION, i).withProperty(FOAMED, (int)cable.foamed);
        } else {
            return state.withProperty(INSULATION, 0).withProperty(FOAMED, 0);
        }
    }

    @Override
    public IBlockState getDefaultBlockState() {
        IBlockState state = this.getDefaultState().withProperty(INSULATION, 0).withProperty(FOAMED, 0);

        return state;
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 3; j++){
                states.add(def.withProperty(INSULATION, i).withProperty(FOAMED, j));
            }
        }

        return states;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof GTCXTileColoredCable) {
                GTCXTileColoredCable cable = (GTCXTileColoredCable)tile;
                if (cable.foamed > 1) {
                    return new BlockStateContainerIC2.IC2BlockState(state, cable.storage.getQuads());
                }
                return new BlockStateContainerIC2.IC2BlockState(state, cable.getConnections());
            }
        } catch (Exception var6) {
        }

        return super.getExtendedState(state, world, pos);
    }


    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof GTCXTileColoredCable && ((GTCXTileColoredCable)tile).foamed > 0 ? 255 : 0;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        state = this.getActualState(state, worldIn, pos);
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable){
            GTCXTileColoredCable cable = (GTCXTileColoredCable) tile;
            if (cable.foamed > 0 && player.isSneaking()){
                cable.changeFoam((byte)0);
                return false;
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList<>();
        TileEntity te = this.getLocalTile() == null ? world.getTileEntity(pos) : this.getLocalTile();
        if (te instanceof IGTItemContainerTile){
            list.addAll(((IGTItemContainerTile) te).getDrops());
            return list;
        }
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean hasFacing() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return this.getTextureFromState(state, EnumFacing.SOUTH);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[2];
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return null;
    }

    private int[] getSize(IBlockState state) {
        int var = (16 - getThickness(state)) / 2;
        return new int[] {var, 16 - var };
    }

    private int getThickness(IBlockState state){
        return 2 + (state.getValue(INSULATION) * 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BaseModel getModelFromState(IBlockState state) {
        if (state.getValue(FOAMED) == 1) {
            return BlockCopyModel.getFoamModel(Ic2States.constructionFoamCable);
        } else {
            return state.getValue(FOAMED) == 2 ? BlockCopyModel.getFoamModel(Ic2Icons.getTextures("bcable")[195]) : new GTModelLayeredAnchoredWire(state, Ic2Icons.getTextures("bcable")[277], getSize(state));
        }
    }

    @Override
    public boolean isLayered(IBlockState iBlockState) {
        return true;
    }

    @Override
    public int getLayers(IBlockState state) {
        return state.getValue(INSULATION) == 0 ? 2 : 3;
    }

    @Override
    public AxisAlignedBB getRenderBox(IBlockState state, int i) {
        double thickness = 2 + state.getValue(INSULATION) / 32.0D;
        double minX = 0.5D - thickness;
        double minY = 0.5D - thickness;
        double minZ = 0.5D - thickness;
        double maxX = 0.5D + thickness;
        double maxY = 0.5D + thickness;
        double maxZ = 0.5D + thickness;
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public TextureAtlasSprite getLayerTexture(IBlockState state, EnumFacing enumFacing, int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[ i == 2 ? getInsulationIndex(state) : 2 + i];
    }

    public int getInsulationIndex(IBlockState state){
        return 3 + state.getValue(INSULATION);
    }

    @Override
    public IBlockState getStateFromStack(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        int insulation = nbt.hasKey(NBT_INSULATION) ? nbt.getInteger(NBT_INSULATION) : 0;
        IBlockState state = this.getDefaultState().withProperty(GTCXBlockWire.INSULATION, insulation).withProperty(FOAMED, 0);
        return state;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        state = this.getActualState(state, world, pos);
        if (state.getValue(INSULATION) == 0){
            return super.getPickBlock(state, target, world, pos, player);
        }
        if (state.getValue(FOAMED) == 1){
            return Ic2Items.constructionFoam;
        }
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger(NBT_INSULATION, state.getValue(INSULATION));
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable){
            GTCXTileColoredCable cable = (GTCXTileColoredCable) tile;
            if (cable.foamed > 0) {
                try {
                    IBlockState realState = cable.storage.getEntry(target.sideHit.getIndex()).getModelState();
                    return realState.getBlock().getPickBlock(realState, target, world, pos, player);
                } catch (Exception ignored) {
                }
            }
            if (cable.isColored() && cable.color != material.getColor().getRGB()) {
                nbt.setInteger("color", cable.color);
            }
        }
        return stack;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable) {
            int foamed = ((GTCXTileColoredCable)tile).foamed;
            if (foamed > 0) {
                return BlockFaceShape.SOLID;
            }
        }

        return BlockFaceShape.UNDEFINED;
    }

    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable) {
            GTCXTileColoredCable cable = (GTCXTileColoredCable)tile;
            if (cable.foamed > 1) {
                IBlockState state = cable.storage.getEntry(side.getIndex()).getModelState();
                if (state != null) {
                    return state.isSideSolid(world, pos, side);
                }
            }
        }

        return super.isSideSolid(base_state, world, pos, side);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof GTCXTileColoredCable)) {
            return new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);
        } else {
            GTCXTileColoredCable cable = (GTCXTileColoredCable) tile;
            if (cable.foamed > 0) {
                return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            }
            double thickness = (2 + (cable.insulation * 2)) / 32.0D;
            double minX = 0.5D - thickness;
            double minY = 0.5D - thickness;
            double minZ = 0.5D - thickness;
            double maxX = 0.5D + thickness;
            double maxY = 0.5D + thickness;
            double maxZ = 0.5D + thickness;
            if (cable.connection.contains(EnumFacing.WEST) || cable.anchors.contains(EnumFacing.WEST)) {
                minX = 0.0D;
            }
            if (cable.connection.contains(EnumFacing.DOWN) || cable.anchors.contains(EnumFacing.DOWN)) {
                minY = 0.0D;
            }
            if (cable.connection.contains(EnumFacing.NORTH) || cable.anchors.contains(EnumFacing.NORTH)) {
                minZ = 0.0D;
            }
            if (cable.connection.contains(EnumFacing.EAST) || cable.anchors.contains(EnumFacing.EAST)) {
                maxX = 1.0D;
            }
            if (cable.connection.contains(EnumFacing.UP) || cable.anchors.contains(EnumFacing.UP)) {
                maxY = 1.0D;
            }
            if (cable.connection.contains(EnumFacing.SOUTH) || cable.anchors.contains(EnumFacing.SOUTH)) {
                maxZ = 1.0D;
            }
            return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        if (stack.getItem() instanceof ICutterItem) {
            ((ICutterItem)stack.getItem()).cutInsulationFrom(stack, worldIn, pos, playerIn);
        }

    }

    @Override
    public void addReaderInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this == GTCXBlocks.electrumCable) {
            tooltip.add((Ic2InfoLang.euReaderCableLimit.getLocalizedFormatted(512)));
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack.copy());
            if (nbt.hasKey(NBT_INSULATION)){
                int insulation = nbt.getInteger(NBT_INSULATION);
                if (insulation == 1){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted( 0.4D));
                } else if (insulation == 2){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted(0.35D));
                } else if (insulation == 3){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted( 0.3D));
                }
            } else {
                tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted(0.45D));
            }
        }
        if (this == GTCXBlocks.aluminiumCable) {
            tooltip.add((Ic2InfoLang.euReaderCableLimit.getLocalizedFormatted(2048)));
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack.copy());
            if (nbt.hasKey(NBT_INSULATION)){
                int insulation = nbt.getInteger(NBT_INSULATION);
                if (insulation == 1){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted( 1.05D));
                } else if (insulation == 2){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted(1.0D));
                } else if (insulation == 3){
                    tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted( 0.9D));
                }
            } else {
                tooltip.add(Ic2InfoLang.euReaderCableLoss.getLocalizedFormatted(1.1D));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileColoredCable) {
            GTCXTileColoredCable cable = (GTCXTileColoredCable) tile;
            ItemStack stack = playerIn.getHeldItem(hand);
            if (cable.foamed == 1 && StackUtil.isStackEqual(stack, new ItemStack(Blocks.SAND))) {
                cable.changeFoam((byte) 2);
                if (!playerIn.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }

                return true;
            }

            if (cable.foamed == 0 && StackUtil.isStackEqual(stack, Ic2Items.constructionFoam.copy())) {
                cable.changeFoam((byte) 1);
                if (!playerIn.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }

                return true;
            }

            if (StackUtil.isStackEqual(stack, Ic2Items.miningPipe)) {
                EnumFacing rotation = (new BlockCable.ClickHelper(hitX, hitY, hitZ, (float) this.getThickness(state) / 16)).getFacing(facing);
                if (rotation != null && cable.addAnchor(rotation)) {
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    return true;
                }
            }
        }
        return false;
    }
}
