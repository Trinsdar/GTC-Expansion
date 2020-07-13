package gtc_expansion.interfaces;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IGTCoverBlock {
    TextureAtlasSprite getCoverTexture(EnumFacing facing);
}
