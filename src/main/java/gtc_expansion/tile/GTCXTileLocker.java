package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.container.GTCXContainerLocker;
import gtclassic.api.tile.GTTileBaseRecolorableTile;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.util.obj.IClickable;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;


public class GTCXTileLocker extends GTTileBaseRecolorableTile implements IHasGui, IClickable {
    public static final ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/locker.png");
    public GTCXTileLocker() {
        super(4);
    }

    public ResourceLocation getGuiTexture(){
        return TEXTURE;
    }

    @Override
    public Block getBlockDrop() {
        return GTCXBlocks.locker;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerLocker(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GuiComponentContainer.class;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return !this.isInvalid();
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        if (enumFacing == this.getFacing() && entityPlayer.getHeldItem(enumHand).isEmpty()){
            for (int i = 0; i < 4; i++){
                ItemStack armorStack = entityPlayer.getItemStackFromSlot(getSlot(i));
                ItemStack inventoryStack = this.getStackInSlot(i);
                if (!armorStack.isEmpty() && !inventoryStack.isEmpty()){
                    ItemStack copy = armorStack.copy();
                    ItemStack copy1 = inventoryStack.copy();
                    armorStack.shrink(armorStack.getCount());
                    inventoryStack.shrink(inventoryStack.getCount());
                    entityPlayer.setItemStackToSlot(getSlot(i), copy1);
                    this.setStackInSlot(i, copy);
                } else if (!armorStack.isEmpty()){
                    this.setStackInSlot(i, armorStack.copy());
                    armorStack.shrink(armorStack.getCount());
                } else if (!inventoryStack.isEmpty()){
                    entityPlayer.setItemStackToSlot(getSlot(i), inventoryStack.copy());
                    inventoryStack.shrink(inventoryStack.getCount());
                }
            }
            world.playSound(entityPlayer, entityPlayer.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return true;
        }
        return !entityPlayer.isCreative();
    }

    private EntityEquipmentSlot getSlot(int slot){
        if (slot == 0){
            return EntityEquipmentSlot.HEAD;
        } else if (slot == 1){
            return EntityEquipmentSlot.CHEST;
        } else if (slot == 2){
            return EntityEquipmentSlot.LEGS;
        } else {
            return EntityEquipmentSlot.FEET;
        }
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }
}
