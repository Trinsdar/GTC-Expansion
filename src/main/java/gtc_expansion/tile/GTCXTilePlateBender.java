package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerPlateBender;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTCXTilePlateBender extends GTTileBaseMachine {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/platebender.png");
    public static final int slotInput = 0;
    public static final int slotOutput = 1;
    public static final int slotFuel = 2;
    protected static final int[] slotInputs = { slotInput };
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 4;

    public GTCXTilePlateBender() {
        super(3, 4, defaultEu, 100, 32);
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutput);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.PLATE_BENDER;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerPlateBender(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXPlateBenderGui.class;
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
        return GTCXRecipeLists.PLATE_BENDER_RECIPE_LIST;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public boolean hasGui(EntityPlayer player) {
        return true;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.extractorOp);
    }

    public static void init() {
        addRecipe(Ic2Items.mixedMetalIngot, Ic2Items.advancedAlloy);
        addRecipe("plateTin", 2, GTMaterialGen.getIc2(Ic2Items.emptyCell, 8));
        addRecipe("plateIron", 3, GTMaterialGen.get(Items.BUCKET));
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
            String refinedIron = GTCXValues.STEEL_MODE ? "casingSteel" : "casingRefinedIron";
            addRecipe("casingIron", 2, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "emptyfuelrod"));
            addRecipe("casingTin", 1, Ic2Items.tinCan);
            addRecipe(refinedIron, 1, Ic2Items.miningPipe);
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
    }

    public static void addRecipe(ItemStack input, ItemStack output) {
        addRecipe(input(input), 400, output);
    }

    public static void addRecipe(String input, int amount, ItemStack output) {
        addRecipe(input(input, amount), 400, output);
    }

    public static void addRecipe(IRecipeInput input, ItemStack output) {
        addRecipe(input, 400, output);
    }

    public static void addRecipe(IRecipeInput input, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{input}, totalEu(totalEu), output.getUnlocalizedName(), output);
    }

    public static void addRecipe(IRecipeInput input, int totalEu, ItemStack output, String recipeId) {
        addRecipe(new IRecipeInput[]{input}, totalEu(totalEu), recipeId, output);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, String recipeId, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
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
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        GTCXRecipeLists.PLATE_BENDER_RECIPE_LIST.addRecipe(input, output, recipeId, 4);
    }
}
