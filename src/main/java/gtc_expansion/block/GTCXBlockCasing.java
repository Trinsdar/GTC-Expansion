package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.model.GTCXModelCasing;
import gtc_expansion.tile.GTCXTileCasing;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.models.BaseModel;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
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
        if (enumFacing == state.getValue(allFacings) && state.getValue(rotor) > 0 && (this == GTCXBlocks.casingStandard || this == GTCXBlocks.casingReinforced)){
            return  getTextureFromRotor(state.getValue(active), state.getValue(rotor));
        }
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
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean hasRotation(IBlockState state) {
        return false;
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
        return new BlockStateContainerIC2(this, rotor, allFacings, active);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(rotor, 0).withProperty(active, false).withProperty(allFacings, NORTH);
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
                states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(rotor, j));
                states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(rotor, j));
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

            return state.withProperty(active, block.getActive()).withProperty(rotor, block.getRotor());
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, NORTH);
            }

            return state.withProperty(active, false).withProperty(rotor, 0);
        }
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof GTCXTileCasing) {
                GTCXTileCasing casing = (GTCXTileCasing)tile;
                return new BlockStateContainerIC2.IC2BlockState(state, casing.getConfig());
            }
        } catch (Exception e) {
            GTCExpansion.logger.info("IC2BlockState Failed");
            e.printStackTrace();
        }

        return super.getExtendedState(state, world, pos);
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
