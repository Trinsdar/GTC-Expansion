package gtc_expansion.tile;

import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerAlloyFurnace;
import gtc_expansion.tile.base.GETileFuelBaseMachine;
import gtc_expansion.util.FuelMachineFilter;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GETileAlloyFurnace extends GETileFuelBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/alloyfurnace.png");
    public static final int slotInput0 = 0;
    public static final int slotInput1 = 1;
    public static final int slotOutput = 2;
    public static final int slotFuel = 3;
    protected static final int[] slotInputs = { slotInput0, slotInput1 };
    public IFilter filter = new FuelMachineFilter(this);

    public GETileAlloyFurnace() {
        super(4, 400, 1);
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
        return GETileAlloySmelter.RECIPE_LIST;
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
        return GEMachineGui.GTAlloySmelterGui.class;
    }
}
