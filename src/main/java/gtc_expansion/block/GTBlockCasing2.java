package gtc_expansion.block;

import gtc_expansion.GTMod2;
import gtclassic.GTMod;
import gtclassic.block.GTBlockCasing;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTBlockCasing2 extends GTBlockCasing {
    int index;
    public GTBlockCasing2(String name, int index) {
        super(name, index);
        this.index = index;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTMod2.MODID + "_blocks")[this.index];
    }
}
