package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.tile.GTTileWorktable;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.tile.machine.IEUStorage;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileAdvancedWorktable extends GTTileWorktable implements IEnergySink, IEUStorage, ITickable {
    @NetworkField(index = 3)
    public int energy;
    public int tier = 2;
    public int maxEnergy = 100000;
    public int maxInput = 128;
    public boolean addedToEnergyNet;
    public static ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/advancedworktable.png");

    public GTCXTileAdvancedWorktable() {
        super();
        this.addGuiFields("energy");
    }

    @Override
    public Block getBlockDrop() {
        return GTCXBlocks.advancedWorktable;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        ItemStack block = GTMaterialGen.get(this.getBlockDrop());
        if (this.isColored() && this.color != GTMaterial.Electrum.getColor().getRGB()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(block);
            nbt.setInteger("color", this.color);
        }

        drops.addAll(this.getInventoryDrops());
        drops.add(block);
        return drops;
    }

    @Override
    public ResourceLocation getGuiTexture(){
        return TEXTURE;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energy = nbt.getInteger("energy");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("energy", this.energy);
        return nbt;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    @Override
    public void onUnloaded() {
        if (this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    public int getStoredEU() {
        return this.energy;
    }

    @Override
    public int getMaxEU() {
        return this.maxEnergy;
    }

    @Override
    public double getDemandedEnergy() {
        return (double)(this.maxEnergy - this.energy);
    }

    @Override
    public int getSinkTier() {
        return this.tier;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        // Only place invert is actually called for energy input
        if (amount <= (double) this.maxInput && amount > 0.0D && !this.isInverted()) {
            this.energy = (int) ((double) this.energy + amount);
            int left = 0;
            if (this.energy >= this.maxEnergy) {
                left = this.energy - this.maxEnergy;
                this.energy = this.maxEnergy;
            }
            this.getNetwork().updateTileGuiField(this, "energy");
            return (double) left;
        } else {
            return 0.0D;
        }
    }

    public boolean isInverted() {
        return this.world.isBlockPowered(this.getPos());
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return true;
    }

    @Override
    public void update() {
        tryCharge();
    }

    public boolean hasEnergy() {
        return this.energy > 0;
    }

    public void tryCharge() {
        // Here I iterate the input slots to try to charge items
        for (int i = 17; i < 22; ++i) {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IElectricItem && ElectricItem.manager.getCharge(stack) < ElectricItem.manager.getMaxCharge(stack)) {
                if (hasEnergy()) {
                    int removed = (int) ElectricItem.manager.charge((ItemStack) stack, (double) this.energy, this.tier, false, false);
                    this.energy -= removed;
                    if (removed > 0) {
                        this.getNetwork().updateTileGuiField(this, "energy");
                    }
                }
                // MABYEDO limit charge to one item at a time?
            }
        }
    }
}
