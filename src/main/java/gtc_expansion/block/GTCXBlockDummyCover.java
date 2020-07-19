package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import ic2.core.block.base.BlockMultiID;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
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
    }

    @Override
    public List<IBlockState> getValidStates() {
        return getBlockState().getValidStates();
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        for (EnumFacing side : EnumFacing.VALUES) {
            for (int meta : getValidMetas()){
                states.add(def.withProperty(allFacings, side).withProperty(active, false).withProperty(metadata, meta));
                states.add(def.withProperty(allFacings, side).withProperty(active, true).withProperty(metadata, meta));
            }
        }
        return states;
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
        if (this.getMetaFromState(state) == 1){
            return GTCXItems.conveyorModule;
        }
        return super.getItemDropped(state, rand, fortune);
    }
}
