package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GEItemMisc extends Item implements IStaticTexturedItem {

    String name;
    int x;
    int y;

    /**
     * Constructor for making a simple item with no action.
     *
     * @param name - String name for the item
     * @param x    - int column
     * @param y    - int row
     */
    public GEItemMisc(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        setRegistryName(this.name.toLowerCase());
        setUnlocalizedName(GTCExpansion.MODID + "." + this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[(this.y * 16) + this.x];
    }
}
