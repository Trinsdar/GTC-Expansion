package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.item.base.ItemIC2;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.registry.Ic2Lang;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IAdvancedTexturedItem;
import ic2.core.platform.textures.obj.ITexturedItem;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.ToolTipType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GTCXItemBatterySingleUse extends ItemIC2 implements ITexturedItem, IAdvancedTexturedItem {
    private ModelResourceLocation[] locations;
    int maxCharge;
    int tier;
    int id;
    public GTCXItemBatterySingleUse(String name, int maxCharge, int tier, int id) {
        this.setRegistryName(name + "_battery");
        this.setUnlocalizedName(GTCExpansion.MODID + "." + name + "_battery");
        this.locations = new ModelResourceLocation[5];
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.id = id;
        this.setCreativeTab(GTMod.creativeTabGT);
        this.setMaxStackSize(1);
    }

    @Override
    public List<Integer> getValidVariants() {
        return new ArrayList();
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Color.CYAN.hashCode();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getChargeLeft(stack) == maxCharge || getChargeLeft(stack) == 0 ? 16 : 1;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getChargeLeft(stack) != maxCharge && getChargeLeft(stack) != 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - getChargeLeft(stack) / maxCharge;
    }

    private float getChargeLeft(ItemStack stack){
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getFloat("charge");
    }

    private void setChargeLeft(ItemStack stack, int amount){
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setFloat("charge", amount);
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack full = getFull();
            items.add(full);
        }
    }

    public ItemStack getFull(){
        ItemStack stack = new ItemStack(this, 1, 0);
        setChargeLeft(stack, maxCharge);
        return stack;
    }

    public ItemStack getEmpty(){
        ItemStack stack = new ItemStack(this, 1, 0);
        setChargeLeft(stack, 0);
        return stack;
    }

    @Override
    public int getTextureEntry(int meta) {
        return 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (IC2.platform.isSimulating()) {
            int left = (int)getChargeLeft(stack);

            if (left == 0){
                return ActionResult.newResult(EnumActionResult.PASS, stack);
            }

            for(int i = 0; i < 9 && left > 0; ++i) {
                ItemStack otherItem = playerIn.inventory.getStackInSlot(i);
                if (otherItem.getItem() instanceof IElectricItem) {
                    if (stack.getCount() == 1){
                        setChargeLeft(stack, (int)((double)left - ElectricItem.manager.charge(otherItem, left, tier, true, false)));
                        left = (int)getChargeLeft(stack);
                    } else {
                        IC2.platform.messagePlayer(playerIn, "Please use 1 battery at a time when filling electric items.");
                        return ActionResult.newResult(EnumActionResult.PASS, stack);
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void onSortedItemToolTip(ItemStack stack, EntityPlayer player, boolean debugTooltip, List<String> tooltip, Map<ToolTipType, List<String>> sortedTooltip) {
        super.onSortedItemToolTip(stack, player, debugTooltip, tooltip, sortedTooltip);
        tooltip.add((int)getChargeLeft(stack) + "/" + maxCharge + " EU");
        if (getChargeLeft(stack) == 0){
            tooltip.add("Empty. You should recycle it properly.");
        }
        List<String> ctrlList = sortedTooltip.get(ToolTipType.Ctrl);
        ctrlList.add(Ic2Lang.onItemRightClick.getLocalized());
        ctrlList.add(Ic2InfoLang.chargesItems.getLocalized());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation createResourceLocationForStack(ItemStack stack) {
        int damage = stack.getItemDamage();
        ResourceLocation location = this.getRegistryName();
        String name = stack.getUnlocalizedName();
        this.locations[damage] = new ModelResourceLocation(location.getResourceDomain() + name.substring(name.indexOf(".") + 1) + damage, "inventory");
        return this.locations[damage];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getResourceLocationForStack(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (getChargeLeft(stack) == 0.0D) {
            return this.locations[4];
        } else if (damage <= 1) {
            return this.locations[0];
        } else if (damage <= 8) {
            return this.locations[1];
        } else if (damage <= 14) {
            return this.locations[2];
        } else {
            return damage <= 20 ? this.locations[3] : this.locations[4];
        }
    }

    @Override
    public List<ItemStack> getValidItemVariants() {
        List<ItemStack> list = new ArrayList();

        for(int i = 0; i < 5; ++i) {
            ItemStack item = new ItemStack(this);
            item.setItemDamage(i);
            list.add(item);
        }

        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(ItemStack item) {
        int meta = item.getItemDamage();
        int id2 = meta == 0 ? id + 1 : id;
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[id2];
    }
}
