package gtc_expansion.block;

import gtclassic.GTMod;
import gtclassic.api.crops.GTCropBlock;
import gtclassic.api.crops.GTCropType;
import gtclassic.api.material.GTMaterialFlag;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXCropBlock extends GTCropBlock {
    GTCropType entry;
    public GTCXCropBlock(GTCropType entry) {
        super(entry);
        this.entry = entry;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int state) {
        return state == 4 ? this.getSprite(this.entry.getSpriteSheet())[this.entry.getId()]
                : (entry.getMaterial().hasFlag(GTMaterialFlag.RUBY) || entry.getMaterial().hasFlag(GTMaterialFlag.SAPPHIRE) ? this.getSprite(GTMod.MODID +"_crops")[7 + state] : this.getSprite("bc")[31 + state]);
    }
}
