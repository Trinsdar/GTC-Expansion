package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXItemMisc extends Item implements IStaticTexturedItem {

    String name, sprite;
    int x;
    int y;

    /**
     * Constructor for making a simple item with no action.
     *
     * @param name   - String name for the item
     * @param x      - int column
     * @param y      - int row
     * @param sprite - String sprite name
     */
    public GTCXItemMisc(String name, int x, int y, String sprite) {
        this.name = name;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        setRegistryName(this.name.toLowerCase());
        setUnlocalizedName(GTCExpansion.MODID + "." + this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
    }

    /**
     * Constructor for making a simple item with no action.
     *
     * @param name - String name for the item
     * @param x    - int column
     * @param y    - int row
     */
    public GTCXItemMisc(String name, int x, int y) {
        this(name, x, y, GTCExpansion.MODID + "_items");
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(sprite)[(this.y * 16) + this.x];
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return this == GTCXItems.coalCoke ? 3200 : super.getItemBurnTime(itemStack);
    }
}
