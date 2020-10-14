package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtc_expansion.util.RotationHelper;
import ic2.api.classic.item.IDamagelessElectricItem;
import ic2.api.classic.item.IElectricTool;
import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack empty = new ItemStack(this, 1, 0);
            ItemStack full = new ItemStack(this, 1, 0);
            ElectricItem.manager.discharge(empty, 2.147483647E9D, 2147483647, true, false, false);
            ElectricItem.manager.charge(full, 2.147483647E9D, 2147483647, true, false);
            items.add(empty);
            items.add(full);
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (RotationHelper.rotateBlock(world, pos, side)){
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
