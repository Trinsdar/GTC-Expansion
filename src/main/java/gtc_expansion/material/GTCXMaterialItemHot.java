package gtc_expansion.material;

import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialItem;
import ic2.core.item.armor.electric.ItemArmorQuantumSuit;
import ic2.core.item.armor.standart.ItemHazmatArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class GTCXMaterialItemHot extends GTMaterialItem {
    GTMaterial material;
    public GTCXMaterialItemHot(GTMaterial material, GTMaterialFlag flag) {
        super(material, flag);
        this.material = material;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase player = (EntityLivingBase) entityIn;
            if (!ItemHazmatArmor.isFullHazmatSuit(player) && !player.isImmuneToFire() && !hasFullQuantumSuit(player)) {
                entityIn.attackEntityFrom(DamageSource.IN_FIRE, 4.0F);
            }
        }
    }

    public boolean hasFullQuantumSuit(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)){
            return false;
        }
        EntityPlayer player = (EntityPlayer) entity;
        for (int i = 0; i < 4; i++) {
            if (!(player.inventory.armorInventory.get(i).getItem() instanceof ItemArmorQuantumSuit)) {
                return false;
            }
        }
        return true;
    }
}
