package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerIndustrialGrinder;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.util.GELang;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.multi.GTTileMultiBaseMachineSimple;
import gtclassic.util.GTValues;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
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
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

public class GETileMultiIndustrialGrinder extends GTTileMultiBaseMachineSimple {
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    protected static final int slotFuel = 8;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GEBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GEBlocks.casingReinforced.getDefaultState();
    public static final IBlockState waterState = Blocks.WATER.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialgrinder.png");
    private static final int defaultEu = 120;

    public GETileMultiIndustrialGrinder() {
        super(9, 2, defaultEu, 128);
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    @Override
    public LocaleComp getBlockName() {
        return GELang.INDUSTRIAL_GRINDER;
    }

    @Override
    public int[] getInputSlots() {
        return slotInputs;
    }

    @Override
    public IFilter[] getInputFilters(int[] ints) {
        return new IFilter[] { filter };
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        for (int i : this.getInputSlots()) {
            if (slot <= i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GERecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GEMachineGui.GEIndustrialGrinderGui.class;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GEContainerIndustrialGrinder(entityPlayer.inventory, this);
    }

    public static void init(){
        String modid = GTValues.IC2_EXTRAS;
        if (Loader.isModLoaded(GTValues.IC2_EXTRAS) && GTConfig.compatIc2Extras){
            addWaterRecipe("oreUranium", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "uraniumpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Plutonium, 1));
            addWaterRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "silverpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Lead, 2));
            addWaterRecipe("oreIron", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "ironpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Tin, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "goldpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Copper, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "copperpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Gold, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreTin", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "tinpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 1), GEMaterialGen.getSmallDust(GEMaterial.Zinc, 1));
            addWaterRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "leadpurifiedcrushedore", 2), GEMaterialGen.getSmallDust(GEMaterial.Silver, 2));
            addWaterRecipe("oreCassiterite", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "tinpurifiedcrushedore", 4), GEMaterialGen.getSmallDust(GEMaterial.Carbon, 1));
            addWaterRecipe("oreTetrahedrite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 2), GEMaterialGen.getSmallDust(GEMaterial.Zinc, 1), GEMaterialGen.getSmallDust(GEMaterial.Antimony, 1));
            addWaterRecipe("oreGalena", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Galena, 2), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 1), GEMaterialGen.getSmallDust(GEMaterial.Silver, 1));
            addWaterRecipe("oreIridium", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Iridium, 2), GEMaterialGen.getSmallDust(GEMaterial.Platinum, 2));
            addWaterRecipe("oreBauxite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Bauxite, 4), GTMaterialGen.getDust(GEMaterial.Aluminium, 1));
            addWaterRecipe("orePyrite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Pyrite, 5), GTMaterialGen.getDust(GEMaterial.Sulfur, 2));
            addWaterRecipe("oreCinnabar", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Cinnabar, 5), GEMaterialGen.getSmallDust(GEMaterial.Redstone, 2), GEMaterialGen.getSmallDust(GEMaterial.Glowstone, 1));
            addWaterRecipe("oreSphalerite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Sphalerite, 5), GTMaterialGen.getDust(GEMaterial.Zinc, 2), GEMaterialGen.getSmallDust(GEMaterial.GarnetYellow, 1));
            addWaterRecipe("oreTungstate", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tungsten, 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 3), GEMaterialGen.getSmallDust(GEMaterial.Manganese, 3));
            addWaterRecipe("oreSheldonite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Platinum, 2), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GEMaterialGen.getNugget(GEMaterial.Iridium, 2));
            addMercuryRecipe("oreIridium", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Iridium, 2), GTMaterialGen.getDust(GEMaterial.Platinum, 1));
            addMercuryRecipe("oreSheldonite", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Platinum, 3), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GEMaterialGen.getNugget(GEMaterial.Iridium, 2));
            addMercuryRecipe("oreGalena", 1, totalEu(12800), GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Galena, 2), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 1), Ic2Items.silverDust);
            addMercuryRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "silverpurifiedcrushedore", 3), GEMaterialGen.getSmallDust(GEMaterial.Lead, 2));
            addMercuryRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "goldpurifiedcrushedore", 3), GEMaterialGen.getSmallDust(GEMaterial.Copper, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addMercuryRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "copperpurifiedcrushedore", 2), Ic2Items.goldDust);
            addMercuryRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "leadpurifiedcrushedore", 2), Ic2Items.silverDust);
        } else {
            addWaterRecipe("oreUranium", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Uranium, 2), GEMaterialGen.getSmallDust(GEMaterial.Plutonium, 1));
            addWaterRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.silverDust, 2), GEMaterialGen.getSmallDust(GEMaterial.Lead, 2));
            addWaterRecipe("oreIron", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GEMaterialGen.getSmallDust(GEMaterial.Tin, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.goldDust, 2), GEMaterialGen.getSmallDust(GEMaterial.Copper, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.copperDust, 2), GEMaterialGen.getSmallDust(GEMaterial.Gold, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addWaterRecipe("oreTin", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.tinDust, 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 1), GEMaterialGen.getSmallDust(GEMaterial.Zinc, 1));
            addWaterRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Lead, 2), GEMaterialGen.getSmallDust(GEMaterial.Silver, 2));
            addWaterRecipe("oreCassiterite", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.tinDust, 4), GEMaterialGen.getSmallDust(GEMaterial.Carbon, 1));
            addWaterRecipe("oreTetrahedrite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Tetrahedrite, 2), GEMaterialGen.getSmallDust(GEMaterial.Zinc, 1), GEMaterialGen.getSmallDust(GEMaterial.Antimony, 1));
            addWaterRecipe("oreGalena", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Galena, 2), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 1), GEMaterialGen.getSmallDust(GEMaterial.Silver, 1));
            addWaterRecipe("oreIridium", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Iridium, 2), GEMaterialGen.getSmallDust(GEMaterial.Platinum, 2));
            addWaterRecipe("oreBauxite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Bauxite, 4), GTMaterialGen.getDust(GEMaterial.Aluminium, 1));
            addWaterRecipe("orePyrite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Pyrite, 5), GTMaterialGen.getDust(GEMaterial.Sulfur, 2));
            addWaterRecipe("oreCinnabar", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Cinnabar, 5), GEMaterialGen.getSmallDust(GEMaterial.Redstone, 2), GEMaterialGen.getSmallDust(GEMaterial.Glowstone, 1));
            addWaterRecipe("oreSphalerite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Sphalerite, 5), GTMaterialGen.getDust(GEMaterial.Zinc, 2), GEMaterialGen.getSmallDust(GEMaterial.GarnetYellow, 1));
            addWaterRecipe("oreTungstate", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Tungsten, 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 3), GEMaterialGen.getSmallDust(GEMaterial.Manganese, 3));
            addWaterRecipe("oreSheldonite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Platinum, 2), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GEMaterialGen.getNugget(GEMaterial.Iridium, 2));
            addMercuryRecipe("oreIridium", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Iridium, 2), GTMaterialGen.getDust(GEMaterial.Platinum, 1));
            addMercuryRecipe("oreSheldonite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Platinum, 3), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GEMaterialGen.getNugget(GEMaterial.Iridium, 2));
            addMercuryRecipe("oreGalena", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Galena, 2), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 1), Ic2Items.silverDust);
            addMercuryRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.silverDust, 3), GEMaterialGen.getSmallDust(GEMaterial.Lead, 2));
            addMercuryRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.goldDust, 3), GEMaterialGen.getSmallDust(GEMaterial.Copper, 1), GEMaterialGen.getSmallDust(GEMaterial.Nickel, 1));
            addMercuryRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.copperDust, 2), Ic2Items.goldDust);
            addMercuryRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Lead, 2), Ic2Items.silverDust);
        }
        addWaterRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 16), totalEu(204800), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getIc2(Ic2Items.netherrackDust, 16));
        addWaterRecipe("oreRuby", 1, totalEu(12800), GTMaterialGen.getGem(GEMaterial.Ruby, 1), GEMaterialGen.getSmallDust(GEMaterial.Ruby, 6), GEMaterialGen.getSmallDust(GEMaterial.GarnetRed, 2));
        addWaterRecipe("oreSapphire", 1, totalEu(12800), GTMaterialGen.getGem(GEMaterial.Sapphire, 1), GEMaterialGen.getSmallDust(GEMaterial.Sapphire, 6), GEMaterialGen.getSmallDust(GEMaterial.Aluminium, 2));
        //addWaterRecipe("oreTungsten", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Tungsten, 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 3), GEMaterialGen.getSmallDust(GEMaterial.Manganese, 3));
        addWaterRecipe("oreCoal", 1, totalEu(12800), GTMaterialGen.get(Items.COAL), Ic2Items.coalDust, GEMaterialGen.getSmallDust(GEMaterial.Thorium, 1));
        addWaterRecipe("oreLapis", 1, totalEu(12800), new ItemStack(Items.DYE, 12, 4), GTMaterialGen.getDust(GEMaterial.Lazurite, 3));
        addWaterRecipe("oreRedstone", 1, totalEu(12800), GTMaterialGen.get(Items.REDSTONE, 10), GEMaterialGen.getSmallDust(GEMaterial.Glowstone, 2));
        addWaterRecipe("oreDiamond", 1, totalEu(12800), GTMaterialGen.get(Items.DIAMOND), GEMaterialGen.getSmallDust(GEMaterial.Diamond, 8), Ic2Items.hydratedCoalDust);
        addWaterRecipe("oreEmerald", 1, totalEu(12800), GTMaterialGen.get(Items.EMERALD), GEMaterialGen.getSmallDust(GEMaterial.Emerald, 6), GEMaterialGen.getSmallDust(GEMaterial.Olivine, 2));
        addWaterRecipe("oreOlivine", 1, totalEu(12800), GTMaterialGen.getGem(GEMaterial.Olivine, 1), GEMaterialGen.getSmallDust(GEMaterial.Olivine, 6), GEMaterialGen.getSmallDust(GEMaterial.Emerald, 2));
        addWaterRecipe("oreSodalite", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Sodalite, 12), GTMaterialGen.getDust(GEMaterial.Aluminium, 3));
        addMercuryRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 8), totalEu(102400), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getIc2(Ic2Items.netherrackDust, 8));

    }

    public static void addWaterCellRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(Ic2Items.emptyCell);
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(Ic2Items.waterCell) }, modifiers, outlist);
    }

    public static void addWaterRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {
        addWaterCellRecipe(input, modifiers, outputs);
        addWaterTubeRecipe(input, modifiers, outputs);
        addWaterBucketRecipe(input, modifiers, outputs);
    }

    public static void addWaterTubeRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(GTMaterialGen.getModdedTube("water", 1)) }, modifiers, outlist);
    }

    public static void addWaterBucketRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                            ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(Items.BUCKET));
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(GTMaterialGen.get(Items.WATER_BUCKET)) }, modifiers, outlist);
    }

    public static void addWaterCellRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(Ic2Items.emptyCell);
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(Ic2Items.waterCell) }, modifiers, outlist);
    }

    public static void addWaterRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        addWaterCellRecipe(input, amount, modifiers, outputs);
        addWaterTubeRecipe(input, amount, modifiers, outputs);
        addWaterBucketRecipe(input, amount, modifiers, outputs);
    }

    public static void addWaterTubeRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(GTMaterialGen.getModdedTube("water", 1)) }, modifiers, outlist);
    }

    public static void addWaterBucketRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(Items.BUCKET));
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(GTMaterialGen.get(Items.WATER_BUCKET)) }, modifiers, outlist);
    }

    public static void addRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outlist);
    }

    public static void addMercuryRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(GTMaterialGen.getTube(GTMaterial.Mercury, 1)) }, modifiers, outlist);
    }

    public static void addMercuryRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(GTMaterialGen.getTube(GTMaterial.Mercury, 1)) }, modifiers, outlist);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<ItemStack> outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }

        addRecipe(inlist, new MachineOutput(mods, outputs));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GERecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    /*@Override
    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = super.provideStructure();
        int3 dir = new int3(getPos(), getFacing());
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), waterState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.forward(2).right(2).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        return states;
    }*/

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        if (!isStandardCasing(dir.back(1))){
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.back(1))){
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (world.getBlockState(dir.down(1).asBlockPos()) != waterState) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.forward(2).right(2))) {// missing front right column
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        return true;
    }

    public boolean isStandardCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == casingStandardState;
    }

    public boolean isReinforcedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == casingReinforcedState;
    }
}
