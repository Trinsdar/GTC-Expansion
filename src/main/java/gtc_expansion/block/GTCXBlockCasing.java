package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.tile.GTCXTileCasing;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.util.helpers.BlockStateContainerIC2;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GTCXBlockCasing extends GTBlockBaseMachine {
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
        if (this == GTCXBlocks.casingStandard){
            EnumFacing facing = state.getValue(allFacings);
            if (state.getValue(rotor) > 0 && facing == enumFacing){
                return  getTextureFromRotor(state.getValue(active), state.getValue(rotor));
            } else {
                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[getIndex(enumFacing, state)];

            }
        }
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 12) + getIndex(enumFacing, state)];
    }

    public int getIndex(EnumFacing textureFacing, IBlockState state){
        int id = 7;
        int con = state.getValue(config);
        EnumFacing facing = state.getValue(allFacings);
        if (textureFacing == EnumFacing.UP || textureFacing == EnumFacing.DOWN){

            boolean u = textureFacing == EnumFacing.UP;
            if (between(4, 15, con)){
                id = facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0;
            }
            if (between(16, 19, con) || between(32, 35, con) || between(48, 51, con)){
                id = facing.getAxis() == EnumFacing.Axis.Z ? 0 : 1;
            }
            int up = u ? 3 : 5;
            int down = u ? 5 : 3;
            if (between(28, 31, con)){
                if (facing == EnumFacing.WEST){
                    id = up;
                }
                if (facing == EnumFacing.EAST){
                    id = down;
                }
                if (facing == EnumFacing.NORTH){
                    id = 2;
                }
                if (facing == EnumFacing.SOUTH){
                    id = 4;
                }
            }

            if (between(44, 47, con)){
                if (facing == EnumFacing.WEST){
                    id = down;
                }
                if (facing == EnumFacing.EAST){
                    id = up;
                }
                if (facing == EnumFacing.NORTH){
                    id = 4;
                }
                if (facing == EnumFacing.SOUTH){
                    id = 2;
                }
            }
            if (between(52, 55, con)){
                if (facing == EnumFacing.WEST){
                    id = 4;
                }
                if (facing == EnumFacing.EAST){
                    id = 2;
                }
                if (facing == EnumFacing.NORTH){
                    id = up;
                }
                if (facing == EnumFacing.SOUTH){
                    id = down;
                }
            }
            if (between(56, 59, con)){
                if (facing == EnumFacing.WEST){
                    id = 2;
                }
                if (facing == EnumFacing.EAST){
                    id = 4;
                }
                if (facing == EnumFacing.NORTH){
                    id = down;
                }
                if (facing == EnumFacing.SOUTH){
                    id = up;
                }
            }
            if (between(60, 62, con)){
                id = 6;
            }
            if (between(1, 3, con)){
                id = 7;
            }
            if (u){
                if (between(20, 23, con)){
                    if (facing == EnumFacing.WEST){
                        id = 9;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 11;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 8;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 10;
                    }
                }
                if (between(36, 39, con)){
                    if (facing == EnumFacing.WEST){
                        id = 10;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 8;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 9;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 11;
                    }
                }
                if (between(40, 43, con)){
                    if (facing == EnumFacing.WEST){
                        id = 11;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 9;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 10;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 8;
                    }
                }
                if (between(24, 27, con)){
                    if (facing == EnumFacing.WEST){
                        id = 8;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 10;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 11;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 9;
                    }
                }
            } else {
                if (between(20, 23, con)){
                    if (facing == EnumFacing.WEST){
                        id = 10;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 8;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 11;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 9;
                    }
                }
                if (between(36, 39, con)){
                    if (facing == EnumFacing.WEST){
                        id = 9;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 11;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 10;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 8;
                    }
                }
                if (between(40, 43, con)){
                    if (facing == EnumFacing.WEST){
                        id = 8;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 10;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 9;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 11;
                    }
                }
                if (between(24, 27, con)){
                    if (facing == EnumFacing.WEST){
                        id = 11;
                    }
                    if (facing == EnumFacing.EAST){
                        id = 9;
                    }
                    if (facing == EnumFacing.NORTH){
                        id = 8;
                    }
                    if (facing == EnumFacing.SOUTH){
                        id = 10;
                    }
                }
            }
        } else {
            if (between(1, 3, con)){
                id = 1;
            }
            if (con == 14 || con == 50 || con == 30 || con == 46 || con == 58 || con == 54){
                id = 5;
            }
            if (con == 13 || con == 49 || con == 29 || con == 45 || con == 57 || con == 53){
                id = 3;
            }
            if (con == 51 || con == 59 || con == 55 || con == 15 || con == 47 || con == 31){
                id = 6;
            }
            if (con == 12 || con == 48 || con == 20 || con == 24 || con == 36 || con == 40){
                id = 0;
            }
            if (textureFacing == EnumFacing.NORTH){
                if (con == 17 || con == 25){
                    id = 8;
                }
                if (con == 18 || con == 26){
                    id = 11;
                }
                if (con == 34 || con == 42){
                    id = 10;
                }
                if (con == 33 || con == 41){
                    id = 9;
                }
                if (con == 35 || con == 43){
                    id = 4;
                }
                if (con == 19 || con == 27){
                    id = 2;
                }
                if (between(9, 11, con)){
                    id = 1;
                }
                if (con == 16 || con == 32 || con == 56){
                    id = 0;
                }
            }
            if (textureFacing == EnumFacing.SOUTH){
                if (con == 17 || con == 21){
                    id = 9;
                }
                if (con == 18 || con == 22){
                    id = 10;
                }
                if (con == 34 || con == 38){
                    id = 11;
                }
                if (con == 33 || con == 37){
                    id = 8;
                }
                if (con == 35 || con == 43){
                    id = 2;
                }
                if (con == 19 || con == 23){
                    id = 4;
                }
                if (between(5, 7, con)){
                    id = 1;
                }
                if (con == 16 || con == 32 || con == 52){
                    id = 0;
                }
            }
            if (textureFacing == EnumFacing.EAST){
                if (con == 10 || con == 26){
                    id = 10;
                }
                if (con == 6 || con == 22){
                    id = 11;
                }
                if (con == 9 || con == 25){
                    id = 9;
                }
                if (con == 5 || con == 21){
                    id = 8;
                }
                if (con == 7 || con == 23){
                    id = 2;
                }
                if (con == 11 || con == 27){
                    id = 4;
                }
                if (between(17, 19, con)){
                    id = 1;
                }
                if (con == 4 || con == 8 || con == 28){
                    id = 0;
                }
            }
            if (textureFacing == EnumFacing.WEST){
                if (con == 10 || con == 42){
                    id = 11;
                }
                if (con == 6 || con == 38){
                    id = 10;
                }
                if (con == 9 || con == 41){
                    id = 8;
                }
                if (con == 5 || con == 37){
                    id = 9;
                }
                if (con == 7 || con == 39){
                    id = 4;
                }
                if (con == 11 || con == 43){
                    id = 2;
                }
                if (between(33, 35, con)){
                    id = 1;
                }
                if (con == 4 || con == 8 || con == 44){
                    id = 0;
                }
            }
        }
        return id;
    }

    public boolean between(int min, int max, int compare){
        return compare >= min && compare <= max;
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 12) + 7];
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
        String location = getLocation(rotor);
        return Ic2Icons.getTextures("steam_turbine_front_" + activeTexture + location)[0];
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
        return this.getDefaultState().withProperty(rotor, 0).withProperty(active, false).withProperty(allFacings, EnumFacing.NORTH).withProperty(config, 0);
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = this.getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        EnumFacing[] facings = EnumFacing.VALUES;
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
                state = state.withProperty(allFacings, EnumFacing.NORTH);
            }

            return state.withProperty(active, block.getActive()).withProperty(rotor, block.getRotor()).withProperty(config, block.getConfig());
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, EnumFacing.NORTH);
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
        if (worldIn.getTileEntity(pos) instanceof GTCXTileCasing){
            ((GTCXTileCasing)worldIn.getTileEntity(pos)).setNeighborMap(this);
//            if (this == GTCXBlocks.casingStandard){
//                ((GTCXTileCasing)worldIn.getTileEntity(pos)).setRotor(this);
//            }
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        if (world.getTileEntity(pos) instanceof GTCXTileCasing){
            ((GTCXTileCasing)world.getTileEntity(pos)).setNeighborMap(this);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (this == GTCXBlocks.iridiumTungstensteelBlock){
            if (entity instanceof EntityWither || entity instanceof EntityWitherSkull){
                return false;
            }
            return super.canEntityDestroy(state, world, pos, entity);
        }
        return super.canEntityDestroy(state, world, pos, entity);
    }
}
