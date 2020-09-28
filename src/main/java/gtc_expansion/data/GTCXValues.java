package gtc_expansion.data;

import gtc_expansion.GTCXConfiguration;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTItems;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

import static gtclassic.api.recipe.GTRecipeCraftingHandler.combineRecipeObjects;

public class GTCXValues {
    static boolean usePlates = GTCXConfiguration.general.usePlates;

    public static final String PRE = usePlates ? "plate" : "ingot";
    public static final boolean STEEL_MODE = IC2.config.getFlag("SteelRecipes");

    public static final IRecipeInput INGOT_STEELS = combineRecipeObjects("ingotSteel", "ingotStainlessSteel");
    public static final IRecipeInput MATERIAL_MACHINE = usePlates ? combineRecipeObjects(getRefinedIronPlate(), "plateAluminium", "plateAluminum") : GTValues.INPUT_INGOT_MACHINE;
    public static final IRecipeInput MATERIAL_STEELS = combineRecipeObjects(PRE + "Steel", PRE + "StainlessSteel");
    public static final IRecipeInput MATERIAL_STEELS_ALUMINIUM = combineRecipeObjects(MATERIAL_STEELS, PRE + "Aluminium", PRE + "Aluminum");
    public static final IRecipeInput MATERIAL_BRASS_BRONZE = combineRecipeObjects( PRE + "Bronze", PRE + "Brass");
    public static final IRecipeInput MATERIAL_TIN_ZINC = combineRecipeObjects( PRE + "Zinc", PRE + "Tin");
    public static final IRecipeInput MATERIAL_INVAR_ALUMINIUM = combineRecipeObjects( PRE + "Invar", PRE + "Aluminium");
    public static final IRecipeInput MATERIAL_MIXED_METAL_1 = combineRecipeObjects( PRE + "Aluminium", PRE + "Silver", PRE + "Electrum");
    public static final IRecipeInput MATERIAL_MIXED_METAL_2 = combineRecipeObjects( PRE + "Tungsten", PRE + "Titanium");
    public static final String MATERIAL_REFINED_IRON = usePlates ? getRefinedIronPlate() : GTValues.INGOT_REFINEDIRON;
    public static final IRecipeInput ROD_STEELS = combineRecipeObjects( "rodSteel", "rodStainlessSteel");
    public static final IRecipeInput ANY_PISTON = combineRecipeObjects( Blocks.STICKY_PISTON, Blocks.PISTON);
    public static final IRecipeInput PLATE_ELECTRIC = combineRecipeObjects( getRefinedIronPlate(), "plateSilicon", "plateSilver");
    public static final IRecipeInput REINFORCED_GLASS = combineRecipeObjects( Ic2Items.reinforcedGlass, Ic2Items.reinforcedGlassClear);
    public static final IRecipeInput GRINDER = combineRecipeObjects( GTCXItems.diamondGrinder, GTCXItems.wolframiumGrinder);
    public static final IRecipeInput TIER_2_ENERGY = combineRecipeObjects( Ic2Items.energyCrystal, GTItems.lithiumBattery);
    public static final IRecipeInput PUMP = combineRecipeObjects(
            Loader.isModLoaded("buildcraftfactory") ?
                    new Object[]{Ic2Items.pump.copy(), GTMaterialGen.get(GTCXItems.pumpModule), GTMaterialGen.getModBlock("buildcraftfactory", "pump")} : new Object[]{Ic2Items.pump.copy(), GTMaterialGen.get(GTCXItems.pumpModule)});
    public static final String MACHINE_CHEAP = "machineBlockCheap";
    public static final String MACHINE_VERY_ADV = "machineBlockVeryAdvanced";
    public static final ItemStack[] EMPTY = new ItemStack[0];

    public static final String INVAR = PRE + "Invar";
    public static final String LEAD = PRE + "Lead";
    public static final String REFINED_IRON = PRE + "RefinedIron";
    public static final String BRASS = PRE + "Brass";
    public static final String BRONZE = PRE + "Bronze";
    public static final String COPPER = PRE + "Copper";
    public static final String TIN = PRE + "Tin";
    public static final String CHROME = PRE + "Chrome";
    public static final String ELECTRUM = PRE + "Electrum";
    public static final String PLATINUM = PRE + "Platinum";
    public static final String TITANIUM = PRE + "Titanium";
    public static final IRecipeInput ALUMINIUM = combineRecipeObjects(PRE + "Aluminium", PRE + "Aluminum");
    public static final String TUNGSTEN_STEEL = PRE + "Tungstensteel";
    public static final String TUNGSTEN = PRE + "Tungsten";
    public static final String STAINLESS_STEEL = PRE + "StainlessSteel";
    public static final String STEEL = PRE + "Steel";
    public static final String OSMIUM = PRE + "OsmiumGT";

    public static final boolean T = true;
    public static final boolean F = false;

    public static final byte                SBIT[] = { 1, 2, 4, 8,16,32,64};
    /** Insert Facing and a Connectivity BitMask to see if it is connecting to that Side. Technically this is a simple Bit Operation, but accessing an Array with "FACE_CONNECTED[aSide][aConnections]" just looks nicer than "(aConnections & (1 << aSide) != 0)". */
    public static final boolean[][] FACE_CONNECTED = {
            {F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T,F,T},
            {F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T,F,F,T,T},
            {F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T,F,F,F,F,T,T,T,T},
            {F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T},
            {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T},
            {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T},
            {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T}
    };

    private GTCXValues() {
        throw new IllegalStateException("Utility class");
    }

    public static String getRefinedIronRod(){
        return GTCXValues.STEEL_MODE ? "rodSteel" : "rodRefinedIron";
    }

    public static ICraftingRecipeList.IRecipeModifier colorTransfer(ItemStack input){
        return new ICraftingRecipeList.IRecipeModifier() {

            final String id = "color";
            boolean tag = false;
            int color;

            @Override
            public void clear() {
                tag = false;
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                if (StackUtil.isStackEqual(input, provided)){
                    NBTTagCompound nbt = StackUtil.getNbtData(provided);
                    if (nbt.hasKey(id)){
                        color = nbt.getInteger(id);
                        tag = true;
                    }
                }

                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                if (forDisplay) {
                    StackUtil.addToolTip(output, "Color gets transfered");
                } else {
                    if (tag){
                        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(output);
                        nbt.setInteger(id, color);
                    }
                }

                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static ICraftingRecipeList.IRecipeModifier gtcxTooltip(){
        return new ICraftingRecipeList.IRecipeModifier() {
            @Override
            public void clear() {
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                if (forDisplay) {
                    StackUtil.addToolTip(output, "Recipe overridden by GTC Expansion");
                }
                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static ICraftingRecipeList.IRecipeModifier foamTransfer(ItemStack input){
        return new ICraftingRecipeList.IRecipeModifier() {

            final String id = "StoredFoam";
            boolean tag = false;
            int damage;

            @Override
            public void clear() {
                tag = false;
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                if (StackUtil.isStackEqual(input, provided, false, false, false, true)){
                    damage = provided.getMaxDamage() - provided.getItemDamage();
                    tag = true;
                }

                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                if (forDisplay) {
                    StackUtil.addToolTip(output, "Stored Foam gets transfered");
                } else {
                    if (tag){
                        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(output);
                        nbt.setInteger(id, damage);
                    }
                }

                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static ICraftingRecipeList.IRecipeModifier insulationSetting(ItemStack input, int fromInsulation, int toInsulation){
        return new ICraftingRecipeList.IRecipeModifier() {

            final String id = "insulation";
            final String colorId = "color";
            int color;
            boolean tag = false;
            boolean colorTag = false;

            @Override
            public void clear() {
                tag = false;
                colorTag = false;
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                if (StackUtil.isStackEqual(input, provided)){
                    NBTTagCompound nbt = StackUtil.getNbtData(provided);
                    if (nbt.hasKey(colorId)){
                        color = nbt.getInteger(colorId);
                        colorTag = true;
                    }
                    if (fromInsulation == 0 && (!nbt.hasKey(id) || nbt.getInteger(id) == 0)){
                        tag = true;
                        return true;
                    }

                    if (nbt.hasKey(id)){
                        tag = true;
                        return nbt.getInteger(id) == fromInsulation;
                    }
                    return false;
                }

                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                if (forDisplay) {
                    StackUtil.addToolTip(output, "Insulation is set");
                    StackUtil.addToolTip(output, "Color gets transfered");
                } else {
                    if (tag){
                        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(output);
                        nbt.setInteger(id, toInsulation);
                        if (colorTag){
                            nbt.setInteger(colorId, color);
                        }
                    }
                }

                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static ICraftingRecipeList.IRecipeModifier hasEnoughInsulation(ItemStack input, int insulation){
        return new ICraftingRecipeList.IRecipeModifier() {

            final String id = "insulation";
            int color;
            boolean tag = false;

            @Override
            public void clear() {
                tag = false;
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                if (StackUtil.isStackEqual(input, provided)){
                    NBTTagCompound nbt = StackUtil.getNbtData(provided);
                    if (insulation == 0 && (!nbt.hasKey(id) || nbt.getInteger(id) == 0)){
                        return true;
                    }
                    if (nbt.hasKey(id)){
                        return nbt.getInteger(id) == insulation;
                    }
                    return false;
                }

                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static String getRefinedIronPlate() {
        return GTCXValues.STEEL_MODE ? "plateSteel" : "plateRefinedIron";
    }
}
