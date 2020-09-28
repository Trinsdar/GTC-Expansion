package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTScrewdriver;
import ic2.api.classic.item.IDamagelessElectricItem;
import ic2.api.classic.item.IElectricTool;
import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXItemElectricScrewdriver extends GTCXItemMisc implements IBoxable, IGTScrewdriver, IDamagelessElectricItem, IElectricTool {
    public GTCXItemElectricScrewdriver() {
        super("electric_screwdriver", 1, 3);
    }

    @Override
    public boolean canScrewdriverBeUsed(ItemStack stack) {
        return ElectricItem.manager.canUse(stack, 100);
    }

    @Override
    public void damage(ItemStack stack, EntityPlayer player) {
        ElectricItem.manager.use(stack, 100, player);
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }

    @Override
    public List<Integer> getValidVariants() {
        return Collections.singletonList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[49];
    }

    @Override
    public EnumEnchantmentType getType(ItemStack itemStack) {
        return EnumEnchantmentType.BREAKABLE;
    }

    @Override
    public boolean isSpecialSupported(ItemStack itemStack, Enchantment enchantment) {
        return enchantment == Enchantments.FORTUNE;
    }

    @Override
    public boolean isExcluded(ItemStack itemStack, Enchantment enchantment) {
        return enchantment == Enchantments.MENDING;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 12000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 250.D;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Color.CYAN.hashCode();
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
