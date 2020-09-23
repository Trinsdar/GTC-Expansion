package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerPrimitiveBlastFurnace;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCapabilityTile;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileBrick;
import gtc_expansion.util.MultiBlockHelper;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDisplayTickTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseFuelMachine;
import gtclassic.common.util.GTIFilters;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static gtclassic.api.tile.GTTileBaseMachine.input;

public class GTCXTileMultiPrimitiveBlastFurnace extends GTTileBaseFuelMachine implements IGTMultiTileStatus, IGTItemContainerTile, IGTDisplayTickTile, IGTCapabilityTile {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/primitiveblastfurnace.png");
    public boolean lastState;
    public boolean firstCheck = true;
    private int tickOffset = 0;
    public static final IBlockState brickState = GTCXBlocks.fireBrickBlock.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();

    protected static final int[] slotInputs = { 0, 1, 2, 3 };
    public static final int[] slotOutputs = {4, 5, 6, 7};
    public static final int slotFuel = 8;
    public IFilter filter;

    public GTCXTileMultiPrimitiveBlastFurnace() {
        super(9, 100, 1);
        this.addGuiFields("lastState");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        this.filter = new GTIFilters.FuelMachineFilter(this);
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
    public LocaleComp getBlockName() {
        return GTCXLang.PRIMITIVE_BLAST_FURNACE;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return GTCExpansion.getAprilFirstSound(super.getStartSoundFile());
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
        return GTCXRecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerPrimitiveBlastFurnace(entityPlayer.inventory, this);
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GTCXMachineGui.GTCXPrimitiveBlastFurnaceGui.class;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", lastState);
        return nbt;
    }

    public static void init() {
        /** Iron Processing **/
        addRecipe(new IRecipeInput[] {input("oreIron", 1),
                input("dustCalcite", 1) }, 800, GTMaterialGen.get(Items.IRON_INGOT, 3));
        addRecipe(new IRecipeInput[] { input("dustPyrite", 3),
                input("dustCalcite", 1) }, 800, GTMaterialGen.get(Items.IRON_INGOT, 2));
        /** Galena **/
        addRecipe(new IRecipeInput[] {
                input("dustGalena", 2) }, 1200, GTMaterialGen.getIngot(GTCXMaterial.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
        /** Steel **/
        addRecipe(new IRecipeInput[] { input("dustSteel", 1) }, 1600, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1));
        addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
                input("dustCoal", 2) }, 1600, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 2));
        addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
                input("dustCarbon", 1) }, 1600, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 1));
        addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
                input("dustCoke", 1) }, 1600, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 1));
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalTime(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create(total - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime,  ItemStack... outputs) {
        addRecipe(inputs, totalTime, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime, String recipeId, ItemStack... outputs) {
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
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        GTCXRecipeLists.PRIMITIVE_BLAST_FURNACE_RECIPE_LIST.addRecipe(input, output, recipeId, 0);
    }

    @Override
    public boolean canWork() {
        if (world.getTotalWorldTime() % (128 + this.tickOffset) == 0 || firstCheck) {
            boolean lastCheck = this.lastState;
            lastState = checkStructure();
            firstCheck = false;
            this.getNetwork().updateTileGuiField(this, "lastState");
            /*if (lastCheck != this.lastState) {
                MultiBlockHelper.INSTANCE.removeCore(this.getWorld(), this.getPos());
                if (this.lastState) {
                    MultiBlockHelper.INSTANCE.addCore(this.getWorld(), this.getPos(), new ArrayList<>(this.provideStructure().keySet()));
                }
            }*/
        }
        return lastState;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        this.firstCheck = true;
        if (this.isSimulating()) {
            this.tickOffset = world.rand.nextInt(128);
        }
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
            if (!world.isAirBlock(dir.down(1).asBlockPos())) {
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
        if (world.getBlockState(pos.asBlockPos()) == brickState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileBrick){
                GTCXTileBrick brick = (GTCXTileBrick) tile;
                if (brick.getOwner() == null || brick.getOwner() != this){
                    brick.setOwner(this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBlockBreak() {
        super.onBlockBreak();
        for (BlockPos pos : this.provideStructure().keySet()) {
            this.removeAllBricks(pos);
        }
    }

    public void removeAllBricks(BlockPos pos){
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileBrick){
            GTCXTileBrick brick = (GTCXTileBrick) tile;
            brick.setOwner(null);
        }
    }

    @Override
    public boolean getStructureValid() {
        return lastState;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<ItemStack>();
        ItemStack machine = GTMaterialGen.get(GTCXBlocks.primitiveBlastFurnace);
        list.add(machine);

        list.addAll(getInventoryDrops());

        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < this.inventory.size(); ++i) {
            ItemStack stack = this.inventory.get(i);
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
        return list;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomTickDisplay(IBlockState iBlockState, World world, BlockPos blockPos, Random random) {
        if (this.isActive){
            float f;
            float f3;
            float f4;
            float f5;
            TileEntity te = world.getTileEntity(pos);
            int facing = te instanceof TileEntityBlock ? ((TileEntityBlock)te).getFacing().getIndex() : 0;
            f = (float)pos.getX() + 0.5F;
            float f2 = (float)pos.getY() + 0.0F + world.rand.nextFloat() * 6.0F / 16.0F;
            f3 = (float)pos.getZ() + 0.5F;
            f4 = 0.52F;
            f5 = world.rand.nextFloat() * 0.6F - 0.3F;
            double x = 0.0D;
            double y = 0.0D;
            double z = 0.0D;
            boolean spawn = false;
            if (facing == 2) {
                x = f + f5;
                y = f2;
                z = f3 - f4;
                spawn = true;
            } else if (facing == 3) {
                x = f + f5;
                y = f2;
                z = f3 + f4;
                spawn = true;
            } else if (facing == 4) {
                x = f - f4;
                y = f2;
                z = f3 + f5;
                spawn = true;
            } else if (facing == 5) {
                x = f + f4;
                y = f2;
                z = f3 + f5;
                spawn = true;
            }

            if (spawn) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
