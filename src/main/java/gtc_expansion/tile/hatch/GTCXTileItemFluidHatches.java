package gtc_expansion.tile.hatch;

import gtc_expansion.container.GTCXContainerItemFluidHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.item.tools.GTCXItemToolHammer;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GTCXTileItemFluidHatches extends TileEntityMachine implements ITankListener, ITickable, IClickable, IGTItemContainerTile, IHasGui, IGTCasingBackgroundBlock, IGTDebuggableTile {
    boolean input;
    @NetworkField(index = 3)
    protected final IC2Tank tank;
    private static final int slotInput = 0;
    private static final int slotOutput = 1;
    private static final int slotDisplay = 2;
    public static final String NBT_TANK = "tank";
    @NetworkField(
            index = 4
    )
    public int casing = 0;
    private int prevCasing = 0;

    @NetworkField(
            index = 5
    )
    public int config = 0;
    private int prevConfig = 0;
    public IGTOwnerTile owner = null;
    public GTCXTileItemFluidHatches(boolean input) {
        super(3);
        this.input = input;
        this.tank = new IC2Tank(32000);
        this.tank.addListener(this);
        this.addGuiFields(NBT_TANK);
        this.addNetworkFields("casing", "config");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInput);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.ALL, slotInput);
        handler.registerDefaultSlotsForSide(RotationList.ALL, slotOutput);
        handler.registerSlotType(SlotType.Input, slotInput);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("casing") || field.equals("config")) {
            this.prevCasing = this.casing;
            this.prevConfig = this.config;
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
    }

    @Override
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return this.getFacing() != facing;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        if (owner != null){
            owner.setShouldCheckRecipe(true);
        }
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, NBT_TANK);
        this.inventory.set(slotDisplay, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
        if (owner != null){
            owner.setShouldCheckRecipe(true);
        }
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.readFromNBT(nbt.getCompoundTag(NBT_TANK));
        casing = nbt.getInteger("casing");
        config = nbt.getInteger("config");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(this.getTag(nbt, NBT_TANK));
        nbt.setInteger("casing", casing);
        nbt.setInteger("config", config);
        return nbt;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.addAll(getInventoryDrops());
        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.add(this.getStackInSlot(slotInput));
        list.add(this.getStackInSlot(slotOutput));
        return list;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    int tickSkipper = 0;

    @Override
    public void update() {
        if (tickSkipper <= 0){
            inputOutputFromFacing();
            GTHelperFluid.doFluidContainerThings(this, this.tank, slotInput, slotOutput);
            if (tickSkipper < 0){
                tickSkipper = 0;
            }
        } else {
            tickSkipper--;
        }

    }

    public void inputOutputFromFacing(){
        if (input) {
            GTUtility.importFromSideIntoMachine(this, this.getFacing());
            GTUtility.importFluidFromSideToMachine(this, tank, this.getFacing(), 1000);
        } else {
            GTUtility.exportFromMachineToSide(this, this.getFacing(), slotOutput);
            GTUtility.exportFluidFromMachineToSide(this, tank, this.getFacing(), 1000);
        }
    }

    public void skip5Ticks(){
        tickSkipper = 5;
    }
    public void skipTick(){
        tickSkipper = 1;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
        return input ? GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, this.tank) : GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, this.tank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerItemFluidHatch(entityPlayer.inventory, this);
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

    public IC2Tank getTank() {
        return tank;
    }

    public ItemStack getOutput(){
        return this.getStackInSlot(slotOutput);
    }

    public ItemStack getInput(){
        return this.getStackInSlot(slotInput);
    }

    public void setOwner(IGTOwnerTile tile){
        this.owner = tile;
    }

    public IGTOwnerTile getOwner(){
        return owner;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        Block block = fromCasing(casing);
        map.put("Casing: " + (block == Blocks.AIR ? "None" : block.getLocalizedName()), true);
        map.put("Config: "+ config, true);
    }

    public Block fromCasing(int casing){
        switch (casing){
            case 1: return GTCXBlocks.casingStandard;
            case 2: return GTCXBlocks.casingReinforced;
            case 3: return GTCXBlocks.casingAdvanced;
            default: return Blocks.AIR;
        }
    }

    @Override
    public int getCasing(){
        return casing;
    }

    @Override
    public void setCasing(){
        int standard = 0;
        int reinforced = 0;
        int advanced = 0;
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offset = this.getPos().offset(facing);
            Block block = world.getBlockState(offset).getBlock();
            if (block == GTCXBlocks.casingStandard){
                standard++;
            } else if (block == GTCXBlocks.casingReinforced){
                reinforced++;
            } else if (block == GTCXBlocks.casingAdvanced){
                advanced++;
            }
        }
        int max = max(standard, reinforced, advanced);
        if (standard == 0 && reinforced == 0 && advanced == 0){
            casing = 0;
        }
        else if (standard > 3){
            casing = 1;
        }
        else if (reinforced > 3){
            casing = 2;
        }
        else if (advanced > 3){
            casing = 3;
        }
        else if (twoOutOfThree(standard, reinforced, advanced)){
            casing = world.rand.nextInt(2) + 1;
        }
        else if (twoOutOfThree(standard, advanced, reinforced)){
            casing = world.rand.nextInt(2) == 0 ? 1 : 3;
        }
        else if (twoOutOfThree(reinforced, advanced, standard)){
            casing = world.rand.nextInt(2) + 2;
        }
        else if ((standard == 2 && reinforced == 2 && advanced == 2) || (standard == 1 && reinforced == 1 && advanced == 1)){
            casing = world.rand.nextInt(3) + 1;
        }
        else if (only(standard, reinforced, advanced)){
            casing = 1;
        }
        else if (only(reinforced, advanced, standard)){
            casing = 2;
        }
        else if (only(advanced, standard, reinforced)){
            casing = 3;
        }
        else if (max == standard){
            casing = 1;
        } else if (max == reinforced){
            casing = 2;
        }
        else if (max == advanced){
            casing = 3;
        }
        if (casing != this.prevCasing) {
            world.notifyNeighborsOfStateChange(pos, GTCXBlocks.casingStandard, true);
            this.getNetwork().updateTileEntityField(this, "casing");
        }

        this.prevCasing = casing;
    }

    public boolean only(int value, int compare1, int compare2){
        return value <= 3 && compare1 == 0 && compare2 == 0;
    }

    public boolean twoOutOfThree(int value, int value2, int compare){
        return compare == 0 && ((value == 3 && value2 == 3) || (value == 2 && value2 == 2) ||(value == 1 && value2 == 1));
    }

    public int max(int value1, int value2, int value3){
        if (value1 > value2 && value1 > value3){
            return value1;
        }
        if (value2 > value1 && value2 > value3){
            return value2;
        }
        if (value3 > value1 && value3 > value2){
            return value3;
        }
        return 0;
    }

    @Override
    public int getConfig(){
        return config;
    }

    @Override
    public void setConfig(){
        Block block = fromCasing(casing);
        config = 0;
        for (EnumFacing facing : EnumFacing.values()){
            boolean hasBlock = (world.getBlockState(pos.offset(facing)).getBlock() == block || isHatchWithCasing(pos.offset(facing))) && block != Blocks.AIR;
            if (hasBlock){
                config += 1 << facing.getIndex();
            }
        }
        if (config != this.prevConfig) {
            this.getNetwork().updateTileEntityField(this, "config");
        }

        this.prevConfig = config;
    }

    public boolean isHatchWithCasing(BlockPos pos){
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            return ((IGTCasingBackgroundBlock)tile).getCasing() == casing;
        }
        return false;
    }

    @Override
    public EnumFacing getFacing(){
        return super.getFacing();
    }

    @Override
    public boolean getActive(){
        return super.getActive();
    }

    public static class GTCXTileInputHatch extends GTCXTileItemFluidHatches{

        public GTCXTileInputHatch() {
            super(true);
        }
    }

    public static class GTCXTileOutputHatch extends GTCXTileItemFluidHatches implements IGTDebuggableTile{
        OutputModes cycle = OutputModes.ITEM_AND_FLUID;

        public GTCXTileOutputHatch() {
            super(false);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setBoolean("SupportsFluidOutput", cycle.isFluid());
            nbt.setBoolean("SupportsItemOutput", cycle.isItem());
            return nbt;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            boolean fluid = nbt.getBoolean("SupportsFluidOutput");
            boolean item = nbt.getBoolean("SupportsItemOutput");
            cycle = cycle.fromBool(item, fluid);
        }

        @Override
        public void inputOutputFromFacing(){
            if (cycle.isFluid()){
                GTUtility.exportFluidFromMachineToSide(this, tank, this.getFacing(), 1000);
            }
            if (cycle.isItem() && !this.getStackInSlot(slotOutput).isEmpty()){
                GTUtility.exportFromMachineToSide(this, this.getFacing(), slotOutput);
            }
        }

        public OutputModes getCycle() {
            return cycle;
        }

        public void cycleModes(EntityPlayer player){
            if (this.isSimulating()){
                this.cycle = cycle.cycle(player);
            }
        }

        public enum OutputModes{
            ITEM_AND_FLUID(true, true),
            ITEM_ONLY(true, false),
            FLUID_ONLY(false, true),
            NEITHER(false, false);

            boolean item;
            boolean fluid;
            OutputModes(boolean item, boolean fluid){
                this.item = item;
                this.fluid = fluid;
            }

            OutputModes fromBool(boolean item, boolean fluid){
                if (item && fluid){
                    return ITEM_AND_FLUID;
                } else if (item){
                    return ITEM_ONLY;
                } else if (fluid){
                    return FLUID_ONLY;
                } else {
                    return NEITHER;
                }
            }

            OutputModes cycle(EntityPlayer player){
                if (this == ITEM_AND_FLUID){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_0);
                    return ITEM_ONLY;
                } else if (this == ITEM_ONLY){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_1);
                    return FLUID_ONLY;
                } else if (this == FLUID_ONLY){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_2);
                    return NEITHER;
                } else {
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_3);
                    return ITEM_AND_FLUID;
                }
            }

            public boolean isFluid() {
                return fluid;
            }

            public boolean isItem() {
                return item;
            }
        }

        @Override
        public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
            ItemStack stack = entityPlayer.getHeldItem(enumHand);
            if (stack.getItem() instanceof GTCXItemToolHammer){
                this.cycleModes(entityPlayer);
                stack.damageItem(1, entityPlayer);
                return true;
            }
            return super.onRightClick(entityPlayer, enumHand, enumFacing, side);
        }

        @Override
        public void getData(Map<String, Boolean> data){
            data.put("Outputs Items: " + cycle.isItem(), false);
            data.put("Outputs Fluids: " + cycle.isFluid(), false);
        }
    }

    public static class GTCXTileFusionMaterialInjector extends GTCXTileInputHatch{

        public GTCXTileFusionMaterialInjector() {
            super();
        }

        @Override
        public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
            return this.getFacing() != facing && facing.getAxis().isHorizontal();
        }
    }

    public static class GTCXTileFusionMaterialExtractor extends GTCXTileOutputHatch{

        public GTCXTileFusionMaterialExtractor() {
            super();
        }

        @Override
        public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
            return this.getFacing() != facing && facing.getAxis().isHorizontal();
        }
    }
}
