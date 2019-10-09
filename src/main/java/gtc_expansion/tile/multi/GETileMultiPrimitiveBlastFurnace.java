package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerPrimitiveBlastFurnace;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.tile.base.GETileFuelBaseMachine;
import gtc_expansion.util.FuelMachineFilter;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.util.energy.MultiBlockHelper;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.registry.Ic2Items;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GETileMultiPrimitiveBlastFurnace extends GETileFuelBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/primitiveblastfurnace.png");
    public boolean lastState;
    public boolean firstCheck = true;
    public static final IBlockState brickState = GEBlocks.fireBrickBlock.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();

    protected static final int[] slotInputs = { 0, 1, 2, 3 };
    public static final int[] slotOutputs = {4, 5, 6, 7};
    public static final int slotFuel = 8;
    public IFilter filter = new FuelMachineFilter(this);

    public GETileMultiPrimitiveBlastFurnace() {
        super(9, 100, 1);
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
        return GERecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST;
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

    public static void init() {
        /** Iron Processing **/
        addRecipe(new IRecipeInput[] {GTTileBaseMachine.input("oreIron", 1),
                GTTileBaseMachine.input("dustCalcite", 1) }, 800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 3));
        addRecipe(new IRecipeInput[] { GTTileBaseMachine.input("dustPyrite", 3),
                GTTileBaseMachine.input("dustCalcite", 1) }, 800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 2));
        /** Steel **/
        addRecipe(new IRecipeInput[] { GTTileBaseMachine.input("dustSteel", 1) }, 1600, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
        addRecipe(new IRecipeInput[] { GTTileBaseMachine.input("ingotRefinedIron", 1),
                GTTileBaseMachine.input("dustCoal", 2) }, 1600, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
        addRecipe(new IRecipeInput[] { GTTileBaseMachine.input("ingotRefinedIron", 1),
                GTTileBaseMachine.input("dustCarbon", 1) }, 1600, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalTime(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create(total - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        RecipeModifierHelpers.IRecipeModifier[] modifiers = totalTime(totalTime);
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GERecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), 1);
    }

    @Override
    public boolean canWork() {
        if (world.getTotalWorldTime() % 256 == 0 || firstCheck) {
            boolean lastCheck = this.lastState;
            lastState = checkStructure();
            firstCheck = false;
            if (lastCheck != this.lastState) {
                MultiBlockHelper.INSTANCE.removeCore(this.getWorld(), this.getPos());
                if (this.lastState) {
                    MultiBlockHelper.INSTANCE.addCore(this.getWorld(), this.getPos(), new ArrayList<>(this.provideStructure().keySet()));
                }
            }
        }
        return lastState;
    }

    @Override
    public void onUnloaded() {
        MultiBlockHelper.INSTANCE.removeCore(getWorld(), getPos());
        super.onUnloaded();
    }

    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = new Object2ObjectLinkedOpenHashMap<>();
        int3 dir = new int3(this.getPos(), this.getFacing());

        int i;
        for(i = 0; i < 3; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.back(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.right(1).asBlockPos(), brickState);

        for(i = 0; i < 3; ++i) {
            states.put(dir.down(1).asBlockPos(), airState);
        }
        states.put(dir.down(1).asBlockPos(), brickState);

        states.put(dir.right(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.back(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 4; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.forward(2).right(1).asBlockPos(), brickState);
        states.put(dir.right(1).asBlockPos(), brickState);

        for(i = 0; i < 3; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        return states;
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
        for (int i = 0; i < 3; i++) {
            if (world.getBlockState(dir.down(1).asBlockPos()) != Blocks.AIR.getDefaultState()) {
                return false;
            }
        }
        if (!(isBrick(dir.down(1)))) {
            return false;
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
