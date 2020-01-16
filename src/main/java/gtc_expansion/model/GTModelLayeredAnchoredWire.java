package gtc_expansion.model;

import gtclassic.api.model.GTModelWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class GTModelLayeredAnchoredWire extends GTModelWire {

    TextureAtlasSprite anchorTexture;
    public GTModelLayeredAnchoredWire(IBlockState block, TextureAtlasSprite texture, TextureAtlasSprite anchorTexture, int[] sizes) {
        super(block, texture, sizes);
        this.anchorTexture = anchorTexture;
    }
}
