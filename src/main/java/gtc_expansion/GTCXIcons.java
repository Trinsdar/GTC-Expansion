package gtc_expansion;

import gtclassic.common.GTConfig;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Sprites;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static ic2.core.platform.textures.Ic2Icons.addCustomTexture;
import static ic2.core.platform.textures.Ic2Icons.addSprite;
import static ic2.core.platform.textures.Ic2Icons.addTextureEntry;

public class GTCXIcons {
    @SideOnly(Side.CLIENT)
    public static void loadSprites() {
        makeSprite("blocks", 16, 2);
        makeSprite("materials", 16, 4);
        makeSprite("items", 16, 6);
        makeSprite("crops", 7, 1);
        collectBasicTileSprites();
        if (GTConfig.general.animatedTextures){
            addCustomTexture("industrial_front_active", 0, 0, location("bf_front"));
            addCustomTexture("diesel_generator_top_active", 0, 0, location("diesel_generator_top"));
            addCustomTexture("gas_turbine_top_active", 0, 0, location("gas_turbine_top"));
        }
        if (GTConfig.general.debugMode){
            GTCExpansion.logger.info("All GregTech Classic Expansion textures generated without error");
        }

    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation("gtc_expansion", "animations/" + name);
    }

    public static void makeSprite(String name, int maxX, int maxY){
        addSprite(new Sprites.SpriteData(GTCExpansion.MODID + "_" + name, GTCExpansion.MODID
                + ":textures/sprites/sprites_" + name + ".png", new Sprites.SpriteInfo(maxX, maxY)));
        addTextureEntry(new Sprites.TextureEntry(GTCExpansion.MODID + "_" + name, 0, 0, maxX, maxY));
    }

    public static void collectBasicTileSprites() {
        String[] strings = GTCXBlocks.textureTileBasic;
        int length = strings.length;

        for(int i = 0; i < length; ++i) {
            String string = strings[i];
            if (GTConfig.general.debugMode){
                GTCExpansion.logger.info("Attempting to get sprite data for: " + string);
            }
            Ic2Icons.addSprite(new Sprites.SpriteData(string, "gtc_expansion:textures/sprites/tiles/" + string + ".png", new Sprites.SpriteInfo(1, 1)));
            Ic2Icons.addTextureEntry(new Sprites.TextureEntry(string, 0, 0, 1, 1));
        }

    }

    public static GTCXIconInfo s(String spriteName){
        return new GTCXIconInfo(spriteName);
    }

    public static GTCXIconInfo s(int spriteId){
        return new GTCXIconInfo(spriteId);
    }

    public static GTCXIconInfo s(String spriteName, int spriteId){
        return new GTCXIconInfo(spriteName, spriteId);
    }

    public static class GTCXIconInfo{
        int spriteId;
        String spriteName;
        public GTCXIconInfo(String spriteName, int spriteId){
            this.spriteName = spriteName;
            this.spriteId = spriteId;
        }

        public GTCXIconInfo(String spriteName){
            this.spriteName = spriteName;
            this.spriteId = 0;
        }

        public GTCXIconInfo(int spriteId){
            this.spriteName = "gtclassic_terrain";
            this.spriteId = spriteId;
        }

        public TextureAtlasSprite getSprite(){
            return Ic2Icons.getTextures(spriteName)[spriteId];
        }
    }
}
