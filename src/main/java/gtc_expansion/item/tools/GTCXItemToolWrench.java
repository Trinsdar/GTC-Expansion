package gtc_expansion.item.tools;

import cofh.api.item.IToolHammer;
import gtc_expansion.GTCExpansion;
import gtc_expansion.block.GTCXBlockPipe;
import gtc_expansion.interfaces.IGTWrench;
import gtc_expansion.util.GTCXBetterPipesCompat;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.core.IC2;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

@Optional.Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhcore", striprefs = true)
public class GTCXItemToolWrench extends ItemToolWrench implements IGTColorItem, IGTWrench, IToolHammer {

    GTMaterial material;
    float efficiency;

    public GTCXItemToolWrench(GTMaterial mat, ToolMaterial tmat) {
        this.maxStackSize = 1;
        this.material = mat;
        this.setMaxDamage(tmat.getMaxUses());
        setRegistryName(this.material.getName() + "_wrench");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_wrench");
        setCreativeTab(GTMod.creativeTabGT);
        this.setHarvestLevel("wrench", tmat.getHarvestLevel());
        this.efficiency = tmat.getEfficiency();
        this.setNoRepair();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (state.getBlock() instanceof GTCXBlockPipe || (Loader.isModLoaded("betterpipes") && GTCXBetterPipesCompat.isAcceptableBlock(state))) {
            return this.efficiency;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn, ItemStack stack) {
        return super.canHarvestBlock(blockIn, stack) || blockIn.getBlock() instanceof BlockHopper || blockIn.getBlock() == Blocks.FURNACE;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote && state.getBlock() instanceof GTCXBlockPipe)
        {
            stack.damageItem(1, entityLiving);
        }

        return state.getBlock() instanceof GTCXBlockPipe;
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

    @Override
    public boolean hasBigCost(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeUsed(ItemStack stack) {
        return true;
    }

    public boolean canBeUsed(ItemStack stack, EntityPlayer player) {
        return canBeUsed(stack) && !IC2.keyboard.isAltKeyDown(player) && !player.isSneaking();
    }

    @Override
    public void damage(ItemStack stack, EntityPlayer player) {
        stack.damageItem(1, player);
    }

    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
        if (user instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) user;
            return IC2.keyboard.isAltKeyDown(player) || IC2.keyboard.isSneakKeyDown(player) || !Loader.isModLoaded("betterpipes");
        }
        return false;
    }

    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
        if (user instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) user;
            return IC2.keyboard.isAltKeyDown(player) || IC2.keyboard.isSneakKeyDown(player) || !Loader.isModLoaded("betterpipes");
        }
        return false;
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {
        if (user instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) user;
            this.damage(item, player);
        }
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {
        if (user instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) user;
            this.damage(item, player);
        }
    }
}
