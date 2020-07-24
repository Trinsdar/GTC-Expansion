package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

public class GTCXItemTurbineRotor extends GTCXItemDamageable implements IGTColorItem {
    protected GTMaterial material;
    /**
     * Constructor for making a simple item with no action.
     *
     * @param material  - Material of the item - used for the color
     * @param maxDamage
     */
    public GTCXItemTurbineRotor(GTMaterial material, int maxDamage) {
        super(material.getName() + "_turbine_rotor", 0, 0, maxDamage);
        this.material = material;
    }

    @Override
    public Color getColor(ItemStack itemStack, int i) {
        return material.getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[36];
    }
}
