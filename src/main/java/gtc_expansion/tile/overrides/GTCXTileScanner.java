package gtc_expansion.tile.overrides;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import forestry.core.utils.GeneticsUtil;
import gtc_expansion.container.GTCXContainerScanner;
import gtc_expansion.data.GTCXLang;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.crops.ICropSeed;
import ic2.api.energy.EnergyNet;
import ic2.core.RotationList;
import ic2.core.block.machine.low.TileEntityCropAnalyzer;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.inventory.transport.wrapper.RangedInventoryWrapper;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GTCXTileScanner extends TileEntityCropAnalyzer {
    public IFilter filter;

    protected void addSlots(InventoryHandler handler) {
        this.filter = new MachineFilter(this);
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, 0);
        handler.registerDefaultSlotAccess(AccessRule.Import, 1);
        handler.registerDefaultSlotAccess(AccessRule.Export, 2);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), 1);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), 2);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, 0);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), 0);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, 0);
        handler.registerInputFilter(this.filter, 1);
        handler.registerSlotType(SlotType.Discharge, 0);
        handler.registerSlotType(SlotType.Input, 1);
        handler.registerSlotType(SlotType.Output, 2);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.SCANNER;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerScanner(player.inventory, this);
    }

    @Override
    public float getMaxProgress() {
        ItemStack stack = this.inventory.get(1);
        if (stack.getItem() instanceof ICropSeed) {
            return super.getMaxProgress();
        } else {
            if (stack.isEmpty()){
                return 0.0F;
            }
            stack = GeneticsUtil.convertToGeneticEquivalent(stack);
            ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
            if (speciesRoot == null) {
                return 0.0F;
            }
            if (!stack.isEmpty()) {
                IIndividual individual = speciesRoot.getMember(stack);
                if (individual == null || individual.isAnalyzed()){
                    return 0.0F;
                }
                return  (float)energyNeeded[3];
            }
            return 0.0F;
        }
    }

    @Override
    public double getRecipeProgress() {
        ItemStack stack = this.inventory.get(1);
        if (stack.getItem() instanceof ICropSeed) {
            return super.getRecipeProgress();
        } else {
            if (stack.isEmpty()){
                return 0.0D;
            }
            stack = GeneticsUtil.convertToGeneticEquivalent(stack);
            ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
            if (speciesRoot == null) {
                return 0.0D;
            }
            if (!stack.isEmpty()) {
                IIndividual individual = speciesRoot.getMember(stack);
                if (individual == null || individual.isAnalyzed()){
                    return 0.0D;
                }
                return  (double)this.progress / (double)energyNeeded[3];
            }
            return 0.0D;
        }
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        if (super.isValidInput(stack)){
            return true;
        }
        if (stack.isEmpty()){
            return false;
        }
        stack = GeneticsUtil.convertToGeneticEquivalent(stack);
        ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
        if (speciesRoot == null) {
            return false;
        }
        if (!stack.isEmpty()) {
            IIndividual individual = speciesRoot.getMember(stack);
            if (individual == null || individual.isAnalyzed()){
                return false;
            }
            return (this.inventory.get(1).isEmpty() || StackUtil.isStackEqual(this.inventory.get(1), stack, false, false));
        }
        return false;
    }

    @Override
    public IHasInventory getInputInventory() {
        return (new RangedInventoryWrapper(this, 0)).setFilters(this.filter);
    }

    @Override
    public void update() {
        this.updateNeighbors();
        if (this.inventory.get(2).isEmpty()) {
            ItemStack stack = this.inventory.get(1);
            if (stack.getItem() instanceof ICropSeed) {
                super.update();
                return;
            }

            if (stack.isEmpty()){
                if (this.progress != 0) {
                    this.progress = 0;
                    this.getNetwork().updateTileGuiField(this, "progress");
                }
                return;
            }
            stack = GeneticsUtil.convertToGeneticEquivalent(stack);
            ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
            if (speciesRoot == null) {
                if (this.progress != 0) {
                    this.progress = 0;
                    this.getNetwork().updateTileGuiField(this, "progress");
                }
                return;
            }
            if (!stack.isEmpty()) {
                IIndividual individual = speciesRoot.getMember(stack);
                if (individual == null || individual.isAnalyzed()){
                    if (this.progress != 0) {
                        this.progress = 0;
                        this.getNetwork().updateTileGuiField(this, "progress");
                    }
                    return;
                }
                if (this.hasEnergy(this.energyUsage)) {
                    this.handleChargeSlot(energyNeeded[3]);
                    this.progress += this.energyUsage;
                    this.useEnergy(this.energyUsage);
                    if (this.progress >= energyNeeded[3]) {
                        this.progress = 0;
                        boolean output = individual.analyze();
                        if (output) {
                            NBTTagCompound nbttagcompound = new NBTTagCompound();
                            individual.writeToNBT(nbttagcompound);
                            stack.setTagCompound(nbttagcompound);
                            for(int i = 0; i < 4; ++i) {
                                ItemStack upgrade = (ItemStack)this.inventory.get(i + this.inventory.size() - 4);
                                if (upgrade.getItem() instanceof IMachineUpgradeItem) {
                                    ((IMachineUpgradeItem)upgrade.getItem()).onProcessFinished(upgrade, this);
                                }
                            }
                            this.notifyNeighbors();
                        }
                        this.inventory.set(2, stack);
                        this.inventory.set(1, ItemStack.EMPTY);
                    }

                    this.getNetwork().updateTileGuiField(this, "progress");
                } else if (this.progress > 0) {
                    this.progress = 0;
                    this.getNetwork().updateTileGuiField(this, "progress");
                }
            } else if (this.progress != 0) {
                this.progress = 0;
                this.getNetwork().updateTileGuiField(this, "progress");
            }
        } else if (this.progress != 0) {
            this.progress = 0;
            this.getNetwork().updateTileGuiField(this, "progress");
        }
        for(int i = 0; i < 4; ++i) {
            ItemStack item = this.inventory.get(i + this.inventory.size() - 4);
            if (item.getItem() instanceof IMachineUpgradeItem) {
                ((IMachineUpgradeItem)item.getItem()).onTick(item, this);
            }
        }

        this.updateComparators();
    }

    @Override
    public void setOverclockerRates() {
        int extraEnergyDemand = 0;
        double energyDemandMultiplier = 1.0D;
        int extraEnergyStorage = 0;
        double energyStorageMultiplier = 1.0D;
        int extraTier = 0;

        for(int i = 0; i < 4; ++i) {
            ItemStack item = (ItemStack)this.inventory.get(i + this.inventory.size() - 4);
            if (item != null && item.getItem() instanceof IMachineUpgradeItem) {
                IMachineUpgradeItem upgrade = (IMachineUpgradeItem)item.getItem();
                upgrade.onInstalling(item, this);
                extraEnergyDemand += upgrade.getExtraEnergyDemand(item, this) * item.getCount();
                energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(item, this), (double)item.getCount());
                extraEnergyStorage += upgrade.getExtraEnergyStorage(item, this) * item.getCount();
                energyStorageMultiplier *= Math.pow(upgrade.getEnergyStorageMultiplier(item, this), (double)item.getCount());
                extraTier += upgrade.getExtraTier(item, this) * item.getCount();
            }
        }

        this.energyUsage = applyModifier(1, extraEnergyDemand, energyDemandMultiplier);
        this.setMaxEnergy(applyModifier(10000, extraEnergyStorage, energyStorageMultiplier));
        this.tier = 1 + extraTier;
        if (this.tier > 13) {
            this.tier = 13;
        }

        this.maxInput = (int) EnergyNet.instance.getPowerFromTier(this.tier);
        if (this.energyUsage < 1) {
            this.energyUsage = 1;
        }

    }

    static int applyModifier(int base, int extra, double multiplier) {
        long ret = Math.round((double)(base + extra) * multiplier);
        return ret > 2147483647L ? 2147483647 : (int)ret;
    }
}
