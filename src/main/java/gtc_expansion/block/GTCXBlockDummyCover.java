package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import ic2.core.block.base.BlockMultiID;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.util.helpers.BlockStateContainerIC2;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GTCXBlockDummyCover extends BlockMultiID {
    public GTCXBlockDummyCover() {
        super(Material.IRON);
        this.setRegistryName("dummy_cover");
        this.setUnlocalizedName(GTCExpansion.MODID + ".dummy_cover");
        this.setCreativeTab(null);
    }

    @Override
    public List<IBlockState> getValidStates() {
        return getBlockState().getValidStates();
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        for (int meta : getValidMetas()){
            states.add(def.withProperty(metadata, meta));
        }
        return states;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, this.getMetadataProperty());
    }

    public IBlockState getDefaultBlockState() {
        return this.getDefaultState().withProperty(this.getMetadataProperty(), 0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return null;
    }

    @Override
    public List<Integer> getValidMetas() {
        return Arrays.asList(0, 1, 2, 3, 4);
    }

    @Override
    public TextureAtlasSprite[] getIconSheet(int i) {
        return new TextureAtlasSprite[0];
    }

    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState state, int meta, int extraMeta, EnumFacing side) {
        return Ic2Icons.getTextures("cover" + getCoverTexture(meta))[0];
    }

    public String getCoverTexture(int meta){
        switch (meta){
            case 1: return "_conveyor";
            case 2: return "_drain";
            case 3: return "_itemvalve";
            case 4: return "_valve";
            default: return "";
        }
    }

    @Override
    public boolean hasRotation(IBlockState state) {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return this.getTextureFromState(state, EnumFacing.SOUTH);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        int meta = this.getMetaFromState(state);
        switch (meta) {
            case 1:
                return GTCXItems.conveyorModule;
            case 2:
                return GTCXItems.drain;
            case 3:
                return GTCXItems.itemTransportValve;
            case 4:
                return GTCXItems.pumpModule;
        }
        return super.getItemDropped(state, rand, fortune);
    }
}
