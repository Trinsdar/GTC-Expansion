package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import ic2.api.item.ElectricItem;
import ic2.core.item.base.ItemBatteryBase;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

public class GTCXItemBatteryReusable extends ItemBatteryBase {
    int id;

    public GTCXItemBatteryReusable(String name, int maxCharge, int transferLimit, int tier, int id) {
        super(0);
        this.setRightClick();
        this.setRegistryName(name + "_battery");
        this.setUnlocalizedName(GTCExpansion.MODID + "." + name + "_battery");
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.provider = true;
        this.id = id;
        this.setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Color.CYAN.hashCode();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.shouldBeStackable(stack) ? 16 : 1;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return !this.shouldBeStackable(stack);
    }

    private boolean shouldBeStackable(ItemStack stack) {
        return !stack.hasTagCompound() || ElectricItem.manager.getCharge(stack) == 0.0D;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return !this.shouldBeStackable(stack) && super.showDurabilityBar(stack);
    }

    @Override
    public boolean wantsToPlay(ItemStack stack) {
        return true;
    }

    @Override
    public ResourceLocation createSound(ItemStack stack) {
        return Ic2Sounds.batteryUse;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(ItemStack item) {
        int meta = item.getItemDamage();
        int id2 = meta == 0 ? id + 1 : id;
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[id2];
    }
}
