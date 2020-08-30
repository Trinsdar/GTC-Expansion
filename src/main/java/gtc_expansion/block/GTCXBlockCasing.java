package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.model.GTCXModelCasing;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch;
import gtc_expansion.tile.multi.GTCXTileMultiLargeGasTurbine;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import gtclassic.api.helpers.int3;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityWitherSkull;
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

import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.VALUES;

public class GTCXBlockCasing extends GTBlockBaseMachine implements ICustomModeledBlock {
    int index;
    public GTCXBlockCasing(String name, LocaleComp comp, int index, float resistance) {
        this(name, comp, index, resistance, Material.IRON);
    }

    public GTCXBlockCasing(String name, LocaleComp comp, int index, float resistance, Material material) {
        super(material, comp, 0);
        setRegistryName(name.toLowerCase());
        this.setCreativeTab(GTMod.creativeTabGT);
        this.setSoundType(SoundType.METAL);
        this.setResistance(resistance);
        this.setHardness(3.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.index = index;
    }

    @Override
    public GTCXBlockCasing setSoundType(SoundType sound) {
        return (GTCXBlockCasing) super.setSoundType(sound);
    }

    @Override
    public GTCXBlockCasing setHardness(float hardness) {
        return (GTCXBlockCasing) super.setHardness(hardness);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[26];
        //return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 16) + getIndexes(enumFacing, state)];
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 12) + 7];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BaseModel getModelFromState(IBlockState state) {
        return new GTCXModelCasing(this, state, this.index);
    }

    @Override
    public List<IBlockState> getValidModelStates() {
        return this.getBlockState().getValidStates();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return this == GTCXBlocks.pureGlass ? BlockRenderLayer.CUTOUT : BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean hasRotation(IBlockState state) {
        return false;
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, allFacings, active);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(active, false).withProperty(allFacings, NORTH);
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        EnumFacing[] facings = VALUES;
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
        return state;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            int config = 0;
            for (EnumFacing facing : EnumFacing.values()){
                boolean hasBlock = world.getBlockState(pos.offset(facing)).getBlock() == this || isHatchWithCasing(pos.offset(facing), world);
                if (hasBlock){
                    config += 1 << facing.getIndex();
                }
            }
            int rotor = this.getRotors(state, world, pos);
            return new BlockStateContainerIC2.IC2BlockState(state, new GTCXBlockCasing.IntWrapper(config, rotor));
        } catch (Exception e) {
            GTCExpansion.logger.info("IC2BlockState Failed");
            e.printStackTrace();
        }

        return super.getExtendedState(state, world, pos);
    }

    public boolean isHatchWithCasing(BlockPos pos, IBlockAccess world){
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            return GTCXTileEnergyOutputHatch.fromCasing(((IGTCasingBackgroundBlock)tile).getCasing()) == this;
        }
        return false;
    }

    public int getRotors(IBlockState state, IBlockAccess world, BlockPos pos){
        EnumFacing facing = state.getValue(allFacings);
        int rotor = 0;
        if (this == GTCXBlocks.casingStandard && facing.getHorizontalIndex() != -1){
            int3 original = new int3(pos, facing);
            int3 dir = new int3(pos, facing);
            if (world.getTileEntity(dir.up(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 8;
            } else if (world.getTileEntity(dir.set(original).up(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 7;
            } else if (world.getTileEntity(dir.set(original).up(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 6;
            } else if (world.getTileEntity(dir.set(original).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 5;
            } else if (world.getTileEntity(dir.set(original).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 4;
            } else if (world.getTileEntity(dir.set(original).down(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 3;
            } else if (world.getTileEntity(dir.set(original).down(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 2;
            } else if (world.getTileEntity(dir.set(original).down(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 1;
            } else {
                rotor = 0;
            }
        }
        if (this == GTCXBlocks.casingReinforced && facing.getHorizontalIndex() != -1){
            int3 original = new int3(pos, facing);
            int3 dir = new int3(pos, facing);
            if (world.getTileEntity(dir.up(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 8;
            } else if (world.getTileEntity(dir.set(original).up(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 7;
            } else if (world.getTileEntity(dir.set(original).up(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 6;
            } else if (world.getTileEntity(dir.set(original).left(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 5;
            } else if (world.getTileEntity(dir.set(original).right(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 4;
            } else if (world.getTileEntity(dir.set(original).down(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 3;
            } else if (world.getTileEntity(dir.set(original).down(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 2;
            } else if (world.getTileEntity(dir.set(original).down(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeGasTurbine){
                rotor = 1;
            } else {
                rotor = 0;
            }
        }
        return rotor;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return this != GTCXBlocks.pureGlass;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (this != GTCXBlocks.pureGlass) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        IBlockState other = blockAccess.getBlockState(pos.offset(side));
        return other.getBlock() != this;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return this != GTCXBlocks.pureGlass;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return this != GTCXBlocks.pureGlass;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return this != GTCXBlocks.pureGlass ? 255 : 0;
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
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (this == GTCXBlocks.iridiumTungstensteelBlock || this == GTCXBlocks.tungstensteelReinforcedStone){
            if (entity instanceof EntityWither || entity instanceof EntityWitherSkull){
                return false;
            }
            return super.canEntityDestroy(state, world, pos, entity);
        }
        return super.canEntityDestroy(state, world, pos, entity);
    }

    public static class IntWrapper{
        int first;
        int second;
        public IntWrapper(int first, int second){
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }
}
