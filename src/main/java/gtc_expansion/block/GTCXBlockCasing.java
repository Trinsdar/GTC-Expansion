package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.tile.GTCXTileCasing;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
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
                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[getIndexes(enumFacing, state)];

            }
        }
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[(this.index * 12) + getIndex(enumFacing, state)];
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
                id = facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0; // for blocks on one or both of the north and south faces
            }
            if (between(16, 19, con) || between(32, 35, con) || between(48, 51, con)){
                id = facing.getAxis() == EnumFacing.Axis.Z ? 0 : 1; // for blocks on one or both of the east and west faces
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
            int[] array2 = {8, 9, 10, 11};
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
                    id = 11 - increase;
                }
                if (or(con, 34, 38, 42)){ // block on top and east facing
                    id = 10 + increase;
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
                    id = 10 + increase;
                }
                if (or(con, 6, 22, 38)){ // block on top and north facing
                    id = 11 - increase;
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

    public int getIndexes(EnumFacing textureFacing, IBlockState state)
    {
        int con = state.getValue(config);
        RotationList list = RotationList.ofNumber(con).remove(textureFacing).remove(textureFacing.getOpposite());
        if(list.size() == 0 || list.size() == 4)
        {
            return list.size() == 4 ? 6 : 7;
        }
        int index = 0;
        int result = 0;
        for(EnumFacing facing : list)
        {
            result += (1 << (index++ * 2)) + convert(textureFacing, facing) & 3;
        }
        return result;
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
                    case WEST: return 2;
                    case EAST: return 3;
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
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
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
