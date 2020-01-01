package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtclassic.common.block.GTBlockOreBedrock;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class GTCXBlockOreBedrock extends GTBlockOreBedrock {
    int id;
    public GTCXBlockOreBedrock(String name, int id) {
        super(name, id);
        this.id = id;
    }

    @Override
    public TextureAtlasSprite getTopLayer() {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.id];
    }
}
