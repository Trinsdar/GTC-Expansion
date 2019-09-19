package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerPrimitiveBlastFurnace;
import gtc_expansion.tile.base.GETileFuelBaseMachine;
import gtc_expansion.util.FuelMachineFilter;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GETileMultiPrimitiveBlastFurnace extends GETileFuelBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/primitiveblastfurnace.png");
    public static final GTRecipeMultiInputList RECIPE_LIST = new GTRecipeMultiInputList("gt.primitiveblastfurnace");
    public boolean lastState;
    public boolean firstCheck = true;
    public static final IBlockState brickState = GEBlocks.fireBrickBlock.getDefaultState();

    protected static final int[] slotInputs = { 0, 1, 2, 3 };
    public static final int[] slotOutputs = {4, 5, 6, 7};
    public static final int slotFuel = 8;
    public IFilter filter = new FuelMachineFilter(this);

    public GETileMultiPrimitiveBlastFurnace() {
        super(9, 800, 1);
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
        return new IFilter[]{filter};
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot != slotFuel;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return RECIPE_LIST;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GEContainerPrimitiveBlastFurnace(entityPlayer.inventory, this);
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GEMachineGui.GEPrimitiveBlastFurnaceGui.class;
    }

    @Override
    public boolean canWork() {
        if (world.getTotalWorldTime() % 256 == 0 || firstCheck) {
            lastState = checkStructure();
            firstCheck = false;
        }
        return lastState;
    }

    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        for (int i = 0; i < 3; i++) {// above tile
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.back(1))) {// back
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.right(1))) {// right
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.right(1))) {// right
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.back(1))) {// back
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.forward(2).right(1))) {//missing block under controller
            return false;
        }
        if (!isBrick(dir.right(1))){// missing front right column
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        return true;
    }

    public boolean isBrick(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == brickState;
    }
}
