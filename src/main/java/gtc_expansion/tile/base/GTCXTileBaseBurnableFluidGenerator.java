package gtc_expansion.tile.base;

import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerBurnableFluidGenerator;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityFuelGeneratorBase;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.function.Predicate;

public abstract class GTCXTileBaseBurnableFluidGenerator extends TileEntityFuelGeneratorBase implements ITankListener, IClickable, IGTDebuggableTile {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fluidgen.png");
    private static final Box2D fuelBox = new Box2D(99, 37,14, 14);
    private static final Vec2i fuelPos = new Vec2i(176, 2);
    @NetworkField(
            index = 7
    )
    public final IC2Tank tank;
    @NetworkField(
            index = 8
    )
    public float maxFuel = 0.0F;
    protected int slotInput = 0;
    protected int slotOutput = 1;
    protected int slotDisplay = 2;
    protected MultiRecipe lastRecipe;
    protected boolean shouldCheckRecipe;
    public static final String RECIPE_TICKS = "recipeTicks";
    public static final String RECIPE_EU = "recipeEu";

    public GTCXTileBaseBurnableFluidGenerator(int slots) {
        super(slots);
        this.tank = new IC2Tank(8000);
        this.tank.addListener(this);
        shouldCheckRecipe = true;
        this.addGuiFields("tank", "maxFuel");
        maxStorage = 100000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.UP.invert());
        handler.registerDefaultSlotAccess(AccessRule.Import, 0);
        handler.registerDefaultSlotAccess(AccessRule.Export, 1);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), 0);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), 1);
        handler.registerSlotType(SlotType.Input, 0);
        handler.registerSlotType(SlotType.Output, 1);
    }

    @Override
    public void update() {
        if (this.shouldCheckRecipe) {
            this.lastRecipe = this.getRecipe();
            this.shouldCheckRecipe = false;
        }
        boolean operate = this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE;
        doFluidContainerThings();
        if (operate && this.needsFuel()){
            gainFuel();
        }
        int oldEnergy = this.storage;
        boolean active = this.gainEnergy();
        if (this.isActive != active){
            this.setActive(active);
        }

        if (oldEnergy != this.storage) {
            this.getNetwork().updateTileGuiField(this, "storage");
        }
        if (maxFuel < 0){
            maxFuel = 0;
            this.getNetwork().updateTileGuiField(this, "maxFuel");
        }
        if (this.fuel <= 0){
            if (!shouldCheckRecipe){
                shouldCheckRecipe = true;
            }
            if (maxFuel > 0){
                maxFuel = 0;
                this.getNetwork().updateTileGuiField(this, "maxFuel");
            }
            if (fuel < 0){
                fuel = 0;
                this.getNetwork().updateTileGuiField(this, "fuel");}
        }
        checkProduction();
        this.updateComparators();
    }

    public void checkProduction(){
        if (!this.isActive && this.fuel <= 0 && this.tank.getFluid() == null){
            if (production > 0){
                if (storage > 0){
                    if (production != 32){
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                        production = 32;
                        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                    }
                } else {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                    production = 0;
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                }
            } else {
                if (storage > 0){
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                    production = 32;
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                }
            }
        }
    }

    public void doFluidContainerThings(){
        GTHelperFluid.doFluidContainerThings(this, this.tank, slotInput, slotOutput);
    }

    @Override
    public boolean gainFuel() {
        if (this.fuel <= 0 && this.tank.getFluidAmount() > 0) {
            if (this.tank.getFluid() != null) {
                FluidStack drained = this.tank.drainInternal(1000, false);
                if (drained != null) {
                    int toAdd = (int)((float)getRecipeTicks(lastRecipe.getOutputs()) * ((float)drained.amount / 1000.0F));
                    if (toAdd >= 1){
                        this.tank.drainInternal(1000, true);
                        this.fuel += toAdd;
                        this.maxFuel = (float)toAdd;
                        int newProduction = getRecipeEu(lastRecipe.getOutputs());
                        if (production != newProduction){
                            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                            this.production = newProduction;
                            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                        }
                        this.getNetwork().updateTileGuiField(this, "fuel");
                        this.getNetwork().updateTileGuiField(this, "maxFuel");
                        this.getNetwork().updateTileGuiField(this, "production");
                    }
                }
            }
        }
        return false;
    }

    public FluidStack getContained() {
        return this.tank.getFluid();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.readFromNBT(nbt.getCompoundTag("Tank"));
        this.maxFuel = nbt.getFloat("MaxFuel");
        this.production = nbt.getInteger("Production");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(this.getTag(nbt, "Tank"));
        nbt.setFloat("MaxFuel", this.maxFuel);
        nbt.setInteger("Production", production);
        return nbt;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null
                ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
                : super.getCapability(capability, facing);
    }

    @Override
    public ResourceLocation getOperationSoundFile() {
        return new ResourceLocation("ic2", "sounds/Generators/WatermillLoop.ogg");
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.getNetwork().updateTileGuiField(this, "tank");
        this.inventory.set(slotDisplay, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
        if (isSimulating() && lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE){
            lastRecipe = null;
        }
        shouldCheckRecipe = true;
    }

    public MultiRecipe getRecipe(){
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        FluidStack input = tank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, input) ? lastRecipe : null;
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, input);
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        return lastRecipe;
    }

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, FluidStack input) {
        IRecipeInput recipeInput = entry.getInput(0);
        if (recipeInput instanceof RecipeInputFluid){
            return input != null && input.isFluidEqual(((RecipeInputFluid)recipeInput).fluid);
        }
        return false;
    }

    public abstract GTRecipeMultiInputList getRecipeList();

    @Override
    public ResourceLocation getTexture() {
        return GUI_LOCATION;
    }

    public IC2Tank getTankInstance() {
        return tank;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerBurnableFluidGenerator(entityPlayer.inventory, this);
    }

    @Override
    public Box2D getEnergyBox() {
        return null;
    }

    @Override
    public Box2D getFuelBox() {
        return fuelBox;
    }

    @Override
    public Vec2i getFuelPos() {
        return fuelPos;
    }

    public static int getRecipeTicks(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 0;
        }
        return output.getMetadata().getInteger(RECIPE_TICKS);
    }

    public static int getRecipeEu(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 0;
        }
        return output.getMetadata().getInteger(RECIPE_EU);
    }

    @Override
    public boolean delayActiveUpdate() {
        return true;
    }

    @Override
    public int getDelay() {
        return 60;
    }

    @Override
    public double getWrenchDropRate() {
        return 1.0D;
    }

    @Override
    public int getSourceTier() {
        return 2;
    }

    public int getMaxSendingEnergy() {
        return this.production + 1;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor var1, EnumFacing facing) {
        return true;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerEmptyThings(entityPlayer, enumHand, world, pos, tank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }

    @Override
    public float getMaxFuel() {
        return maxFuel;
    }

    @Override
    public void getData(Map<String, Boolean> data){
        FluidStack fluid = this.tank.getFluid();
        data.put("Input Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        data.put("Producing: " + production + " EU/tick", true);
    };

}
