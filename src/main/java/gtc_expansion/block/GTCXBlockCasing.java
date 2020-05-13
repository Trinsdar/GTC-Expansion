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
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
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
    public static PropertyBool up = PropertyBool.create("up");
    public static PropertyBool down = PropertyBool.create("down");
    public static PropertyBool north = BlockFence.NORTH;
    public static PropertyBool south = BlockFence.SOUTH;
    public static PropertyBool west = BlockFence.WEST;
    public static PropertyBool east = BlockFence.EAST;
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

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, EnumFacing enumFacing) {
        if (this == GTCXBlocks.casingStandard){
            EnumFacing facing = state.getValue(allFacings);
            if (state.getValue(rotor) > 0 && facing == enumFacing){
                return  getTextureFromRotor(state.getValue(active), state.getValue(rotor));
            } else {
                if (state.getValue(propertyFromFacing(enumFacing))){
                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.index];
                }
                switch (enumFacing){
                    case UP: {
                        int surroundingBlocks = 0;
                        if (state.getValue(north)){
                            surroundingBlocks++;
                        }
                        if (state.getValue(west)){
                            surroundingBlocks++;
                        }
                        if (state.getValue(east)){
                            surroundingBlocks++;
                        }
                        if (state.getValue(south)){
                            surroundingBlocks++;
                        }
                        if (surroundingBlocks == 0){
                            return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.index];
                        }
                        if (surroundingBlocks == 1){
                            if (state.getValue(north)){
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[1];
                            }
                            if (state.getValue(west)){
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[0];
                            }
                            if (state.getValue(east)){
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[0];
                            }
                            if (state.getValue(south)){
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[1];
                            }
                        }
                        if (surroundingBlocks == 2){
                            if (state.getValue(north)){
                                if (state.getValue(west)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[10];
                                } else if (state.getValue(east)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[11];
                                } else if (state.getValue(south)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[1];
                                }
                            } else if (state.getValue(south)) {
                                if (state.getValue(west)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[9];
                                } else if (state.getValue(east)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[8];
                                }
                            } else if (state.getValue(west) && state.getValue(east)) {
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[0];
                            }
                        }
                        if (surroundingBlocks == 3){
                            if (state.getValue(north)){
                                if (state.getValue(west)){
                                    if (state.getValue(east)){
                                        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[5];
                                    } else if (state.getValue(south)){
                                        return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[4];
                                    }
                                } else if (state.getValue(east) && state.getValue(south)){
                                    return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[2];
                                }
                            } else if (state.getValue(south) && state.getValue(west) && state.getValue(east)) {
                                return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[3];
                            }
                        }
                        if (surroundingBlocks == 4){
                            return Ic2Icons.getTextures(GTCExpansion.MODID + "_connected_blocks")[6];
                        }
                    }
                }
            }
        }
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.index];
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.index];
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

    public static PropertyBool propertyFromFacing(EnumFacing facing){
        switch (facing){
            case UP: return up;
            case DOWN: return down;
            case NORTH: return north;
            case SOUTH: return south;
            case WEST: return west;
            case EAST: return east;
            default: return active;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, rotor, allFacings, active, up, down, north, south, west, east);
    }

    @Override
    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(rotor, 0).withProperty(active, false).withProperty(allFacings, EnumFacing.NORTH).withProperty(up, false).withProperty(down, false).withProperty(north, false).withProperty(south, false).withProperty(east, false).withProperty(west, false);
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
                states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(rotor, j).withProperty(up, false).withProperty(down, false).withProperty(north, false).withProperty(south, false).withProperty(east, false).withProperty(west, false));
                states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(rotor, j).withProperty(up, true).withProperty(down, true).withProperty(north, true).withProperty(south, true).withProperty(east, true).withProperty(west, true));
                states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(rotor, j).withProperty(up, false).withProperty(down, false).withProperty(north, false).withProperty(south, false).withProperty(east, false).withProperty(west, false));
                states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(rotor, j).withProperty(up, true).withProperty(down, true).withProperty(north, true).withProperty(south, true).withProperty(east, true).withProperty(west, true));
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
            }

            return state.withProperty(active, block.getActive()).withProperty(rotor, block.getRotor()).withProperty(up, block.up).withProperty(down, block.down).withProperty(north, block.north).withProperty(south, block.south).withProperty(east, block.east).withProperty(west, block.west);
        } else {
            if (this.hasFacing()) {
                state = state.withProperty(allFacings, EnumFacing.NORTH);
            }

            return state.withProperty(active, false).withProperty(rotor, 0).withProperty(up, false).withProperty(down, false).withProperty(north, false).withProperty(south, false).withProperty(east, false).withProperty(west, false);
        }
    }

    @Override
    public boolean hasFacing() {
        return this == GTCXBlocks.casingStandard;
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
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        if (world.getTileEntity(pos) instanceof GTCXTileCasing){
            ((GTCXTileCasing)world.getTileEntity(pos)).setNeighborMap(this);
        }
    }
}
