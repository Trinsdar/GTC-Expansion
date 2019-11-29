package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GTCXItemToolWrench extends ItemToolWrench implements IGTColorItem {

    GTMaterial material;

    public GTCXItemToolWrench(GTMaterial mat, ToolMaterial tmat) {
        this.maxStackSize = 1;
        this.material = mat;
        this.setMaxDamage(tmat.getMaxUses());
        setRegistryName(this.material.getName() + "_wrench");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_wrench");
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
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[20];
    }

    @Override
    public Color getColor(ItemStack stack, int index) {
        return this.material.getColor();
    }

    public GTMaterial getMaterial() {
        return this.material;
    }

    @Override
    public boolean isWrench(ItemStack var1) {
        return true;
    }

    @Override
    public boolean canOverrideLossChance(ItemStack stack) {
        return true;
    }
}
