package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtclassic.common.block.GTBlockCasing;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GEBlockCasing extends GTBlockCasing {
    int index;
    public GEBlockCasing(String name, int index, float resistence) {
        super(name, index, resistence);
        this.index = index;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.index];
    }
}
