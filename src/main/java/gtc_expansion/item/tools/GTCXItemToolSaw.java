package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.api.item.IBoxable;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXItemToolSaw extends Item implements IStaticTexturedItem, IGTColorItem, ILayeredItemModel, IBoxable {
    GTMaterial material;

    public GTCXItemToolSaw(GTMaterial mat, ToolMaterial tmat) {
        this.maxStackSize = 1;
        this.material = mat;
        this.setMaxDamage(tmat.getMaxUses());
        this.setNoRepair();
        setRegistryName(this.material.getName() + "_saw");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_saw");
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        return copy.attemptDamageItem(1, itemRand, null) ? ItemStack.EMPTY : copy;
    }

    @Override
    public Color getColor(ItemStack itemStack, int index) {
        if (index == 0) {
            return this.material.equals(GTCXMaterial.TungstenSteel) ? GTCXMaterial.Steel.getColor() : this.material.equals(GTCXMaterial.Steel) ? GTCXMaterial.Iron.getColor() : GTMaterial.Wood.getColor();
        } else {
            return this.material.getColor();
        }
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isLayered(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getLayers(ItemStack itemStack) {
        return 2;
    }

    @Override
    public TextureAtlasSprite getTexture(int i, ItemStack itemStack) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[40 + i];
    }

    @Override
    public List<Integer> getValidVariants() {
        return Collections.singletonList(0);
    }

    @Override
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[40];
    }
}
