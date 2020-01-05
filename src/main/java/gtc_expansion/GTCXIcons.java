package gtc_expansion;

import gtclassic.common.GTConfig;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Sprites;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static ic2.core.platform.textures.Ic2Icons.addCustomTexture;
import static ic2.core.platform.textures.Ic2Icons.addSprite;
import static ic2.core.platform.textures.Ic2Icons.addTextureEntry;

public class GTCXIcons {
    @SideOnly(Side.CLIENT)
    public static void loadSprites() {
        makeSprite("blocks", 16, 16);
        makeSprite("materials", 16, 16);
        makeSprite("items", 16, 16);
        makeSprite("crops", 7, 1);
        collectBasicTileSprites();
        if (GTConfig.general.animatedTextures){
            addCustomTexture("industrialblastfurnace", 0, 9, location("bf_front"));
            addCustomTexture("dieselgenerator", 0, 7, location("diesel_generator_top"));
        }
        if (GTConfig.general.debugMode){
            GTCExpansion.logger.info("All GregTech Classic Expansion textures generated without error");
        }

    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation("gtclassic", "animations/" + name);
    }

    public static void makeSprite(String name, int maxX, int maxY){
        addSprite(new Sprites.SpriteData(GTCExpansion.MODID + "_" + name, GTCExpansion.MODID
                + ":textures/sprites/sprites_" + name + ".png", new Sprites.SpriteInfo(maxX, maxY)));
        addTextureEntry(new Sprites.TextureEntry(GTCExpansion.MODID + "_" + name, 0, 0, maxX, maxY));
    }

    public static void collectBasicTileSprites() {
        String[] var0 = GTCXBlocks.textureTileBasic;
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            String string = var0[var2];
            if (GTConfig.general.debugMode){
                GTCExpansion.logger.info("Attempting to get sprite data for: " + string);
            }
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/" + string + ".png", new Sprites.SpriteInfo(1, 12)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 12));
        }

    }
}
