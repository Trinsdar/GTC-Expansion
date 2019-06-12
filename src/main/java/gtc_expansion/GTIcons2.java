package gtc_expansion;

import gtclassic.GTBlocks;
import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Sprites;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTIcons2 {
    @SideOnly(Side.CLIENT)
    public static void loadSprites() {
        collectBasicTileSprites();
        GTMod.debugLogger("All GregTech textures generated without error");
    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation("gtclassic", "animations/" + name);
    }

    public static void collectBasicTileSprites() {
        String[] var0 = GTBlocks2.textureTileBasic;
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            String string = var0[var2];
            GTMod.debugLogger("Attempting to get sprite data for: " + string);
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/" + string + ".png", new Sprites.SpriteInfo(1, 12)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 12));
        }

    }
}
