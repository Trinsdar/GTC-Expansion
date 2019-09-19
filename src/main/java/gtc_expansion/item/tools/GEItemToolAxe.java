package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtc_expansion.material.GEMaterial;
import gtclassic.GTMod;
import gtclassic.color.GTColorItemInterface;
import gtclassic.material.GTMaterial;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ICustomItemCameraTransform;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GEItemToolAxe extends ItemAxe
        implements IStaticTexturedItem, GTColorItemInterface, ILayeredItemModel, ICustomItemCameraTransform {

    GTMaterial material;

    public GEItemToolAxe(GTMaterial mat, ToolMaterial tmat) {
        super(tmat, tmat.getAttackDamage(), tmat.getEfficiency());
        this.material = mat;
        setRegistryName(this.material.getName() + "_axe");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_axe");
        setCreativeTab(GTMod.creativeTabGT);
    }


    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[27];
    }

    @Override
    public Color getColor(ItemStack stack, int index) {
        if (index == 0) {
            return GEMaterial.Wood.getColor();
        } else {
            return this.material.getColor();
        }
    }

    @Override
    public boolean isLayered(ItemStack var1) {
        return true;
    }

    @Override
    public int getLayers(ItemStack var1) {
        return 2;
    }

    @Override
    public TextureAtlasSprite getTexture(int var1, ItemStack var2) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[27 + var1];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) {
            return;
        }
        ItemStack stack = new ItemStack(this);
        if (this.material.equals(GEMaterial.Flint)){
            stack.addEnchantment(Enchantments.FIRE_ASPECT, 1);
        }
        items.add(stack);
    }

    public GTMaterial getMaterial() {
        return this.material;
    }

    public ResourceLocation getCustomTransform(int meta) {
        return new ResourceLocation("minecraft:models/item/handheld");
    }

    @Override
    public boolean hasCustomTransform(int var1) {
        return true;
    }
}
