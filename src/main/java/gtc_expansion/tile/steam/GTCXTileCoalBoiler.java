package gtc_expansion.tile.steam;

import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerCoalBoiler;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.tile.machine.IFuelMachine;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GTCXTileCoalBoiler extends TileEntityMachine implements IFuelMachine, ITickable, INetworkTileEntityEventListener, IFluidHandler, ITankListener, IHasGui {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/bronzeboiler.png");
    @NetworkField(index = 3)
    int heat;
    @NetworkField(index = 4)
    int maxHeat = 500;
    @NetworkField(index = 5)
    int fuel = 0;
    @NetworkField(index = 6)
    int maxFuel;
    @NetworkField(index = 7)
    IC2Tank water = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.getFluid().getName().equals("water");
        }
    };
    @NetworkField(index = 8)
    IC2Tank steam = new IC2Tank(16000);
    boolean hadNoWater;
    int lossTimer = 0;
    public GTCXTileCoalBoiler() {
        super(4);
        this.water.addListener(this);
        this.steam.addListener(this);
        this.addNetworkFields("heat", "maxHeat", "fuel", "maxFuel", "water", "steam");
        this.addGuiFields("heat", "maxHeat", "fuel", "maxFuel", "water", "steam");
    }

    public int getHeat() {
        return heat;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    public IC2Tank getWater() {
        return water;
    }

    public IC2Tank getSteam() {
        return steam;
    }

    @Override
    public float getFuel() {
        return fuel;
    }

    @Override
    public float getMaxFuel() {
        return maxFuel;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.SMALL_COAL_BOILER;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public void update() {
        GTHelperFluid.doFluidContainerThings(this, water, 0, 1);
        if (this.heat <= 20) {
            this.heat = 20;
            this.getNetwork().updateTileGuiField(this, "heat");
            this.lossTimer = 0;
        }
        if (++this.lossTimer > 45) {
            this.heat -= 1;
            this.getNetwork().updateTileGuiField(this, "heat");
            this.lossTimer = 0;
        }
        for (EnumFacing facing : RotationList.DOWN.invert()){
            GTUtility.exportFluidFromMachineToSide(this, steam, facing, steam.getFluidAmount());
        }
        if (this.getWorld().getWorldTime() % 20 == 0){
            if (this.heat > 100){
                if (this.water.getFluidAmount() == 0){
                    hadNoWater = true;
                } else {
                    if (hadNoWater){
                        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
                        this.world.setBlockToAir(this.getPos());
                        return;
                    }
                    this.water.drain(1, true);
                    int room = steam.getCapacity() - steam.getFluidAmount();
                    int fill = Math.min(room, 150);
                    if (room > 0){
                        this.steam.fill(GTMaterialGen.getFluidStack("steam", fill), true);
                    }
                    if (fill < 150){
                        getNetwork().initiateTileEntityEvent(this, 3, false);
                        this.steam.drain(4000, true);
                    }
                }
            } else {
                this.hadNoWater = false;
            }
        }
        boolean byproductStuffed = false;
        ItemStack byproductStack = getStackInSlot(2);

        if (!byproductStack.isEmpty() && byproductStack.getCount() == byproductStack.getMaxStackSize()){
            byproductStuffed = true;
        }
        if (this.fuel <= 0 && !this.getStackInSlot(3).isEmpty() && !byproductStuffed){
            int fuelEnergy = 0;
            GTMaterial byproduct = GTCXMaterial.Ashes;
            boolean validFuel = false;
            ItemStack fuelStack = this.getStackInSlot(3);
            if (GTHelperStack.isEqual(fuelStack, new ItemStack(Items.COAL, 1, 0))){
                this.maxFuel = 160;
                this.getNetwork().updateTileGuiField(this, "maxFuel");
                byproduct = GTCXMaterial.DarkAshes;
                validFuel = true;
            } else if (GTHelperStack.isEqual(fuelStack, new ItemStack(Items.COAL, 1, 1))){
                this.maxFuel = 160;
                this.getNetwork().updateTileGuiField(this, "maxFuel");
                validFuel = true;
            } else if (GTHelperStack.matchOreDict(fuelStack, "fuelCoke")){
                this.maxFuel = 320;
                this.getNetwork().updateTileGuiField(this, "maxFuel");
                validFuel = true;
            }

            if (validFuel){
                this.fuel += maxFuel;
                this.getNetwork().updateTileGuiField(this, "fuel");
                this.getStackInSlot(3).shrink(1);
                if (this.getStackInSlot(2).isEmpty()){
                    this.setStackInSlot(2, GTMaterialGen.getDust(byproduct, 1));
                } else {
                    ItemStack toAdd = GTMaterialGen.getDust(byproduct, 1);
                    if (GTHelperStack.isEqual(toAdd, this.getStackInSlot(2))){
                        this.getStackInSlot(2).grow(1);
                    } else {
                        this.setStackInSlot(2, GTMaterialGen.getDust(GTCXMaterial.Ashes, this.getStackInSlot(2).getCount() + 1));
                    }
                }
            }
        }
        if ((this.heat < 500) && (this.fuel > 0) && (world.getWorldTime() % 12L == 0L)) {
            this.fuel -= 1;
            this.heat += 1;
            this.getNetwork().updateTileGuiField(this, "heat");
            this.getNetwork().updateTileGuiField(this, "fuel");
        }
        this.setActive(this.fuel > 0);
    }

    @Override
    public void onNetworkEvent(int event) {
        if (event == 3){
            IC2.audioManager.playOnce(this, PositionSpec.Center, SoundEvents.BLOCK_FIRE_EXTINGUISH.getSoundName(), false, IC2.audioManager.defaultVolume);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("heat", heat);
        nbt.setInteger("maxHeat", maxHeat);
        nbt.setInteger("fuel", fuel);
        nbt.setInteger("maxFuel", maxFuel);
        water.writeToNBT(this.getTag(nbt, "water"));
        steam.writeToNBT(this.getTag(nbt, "steam"));
        nbt.setBoolean("hadNoWater", hadNoWater);
        nbt.setInteger("lossTimer", lossTimer);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.heat = nbt.getInteger("heat");
        this.maxHeat = nbt.getInteger("maxHeat");
        this.fuel = nbt.getInteger("fuel");
        this.maxFuel = nbt.getInteger("maxFuel");
        this.water.readFromNBT(nbt.getCompoundTag("water"));
        this.steam.readFromNBT(nbt.getCompoundTag("steam"));
        this.hadNoWater = nbt.getBoolean("hadNoWater");
        this.lossTimer = nbt.getInteger("lossTimer");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    public ResourceLocation getGuiTexture(){
        return GUI_LOCATION;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, "water");
        this.getNetwork().updateTileGuiField(this, "steam");
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> combined = new ArrayList<>();
        Stream.of(water.getTankProperties(), steam.getTankProperties()).flatMap(Stream::of).forEach(combined::add);
        return combined.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return water.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return steam.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return steam.drain(maxDrain, doDrain);
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerCoalBoiler(entityPlayer.inventory, this);
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
}
