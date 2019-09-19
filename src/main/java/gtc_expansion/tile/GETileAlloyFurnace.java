package gtc_expansion.tile;

import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerAlloyFurnace;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.tile.base.GETileFuelBaseMachine;
import gtc_expansion.util.FuelMachineFilter;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GETileAlloyFurnace extends GETileFuelBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/alloyfurnace.png");
    protected static final int[] slotInputs = { 0, 1 };
    public static final int slotOutput = 2;
    public static final int slotFuel = 3;
    public IFilter filter = new FuelMachineFilter(this);

    public GETileAlloyFurnace() {
        super(4, 200, 1);
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, getFuelSlot());
        handler.registerDefaultSlotAccess(AccessRule.Import, getInputSlots());
        handler.registerDefaultSlotAccess(AccessRule.Export, getOutputSlots());
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), getFuelSlot());
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), getOutputSlots());
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), getInputSlots());
        handler.registerInputFilter(CommonFilters.IronFurnaceFuelWithLava, getFuelSlot());
        handler.registerOutputFilter(CommonFilters.NotIronFurnaceFuelWithLava, getFuelSlot());
        handler.registerInputFilter(filter, getInputSlots());
        handler.registerSlotType(SlotType.Fuel, getFuelSlot());
        handler.registerSlotType(SlotType.Input, getInputSlots());
        handler.registerSlotType(SlotType.Output, getOutputSlots());
    }

    @Override
    public int getFuelSlot() {
        return slotFuel;
    }

    @Override
    public int[] getInputSlots() {
        return slotInputs;
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[] { filter };
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot != slotFuel;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{slotOutput};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GERecipeLists.ALLOY_SMELTER_RECIPE_LIST;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GEContainerAlloyFurnace(player.inventory, this);
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GEMachineGui.GEAlloySmelterGui.class;
    }
}
