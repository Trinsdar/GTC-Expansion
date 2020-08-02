package gtc_expansion.tile.hatch;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.item.tools.GTCXItemToolHammer;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.common.GTLang;
import ic2.api.classic.energy.tile.IEnergySourceInfo;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.tile.machine.IEUStorage;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.block.base.util.comparator.ComparatorManager;
import ic2.core.block.base.util.comparator.comparators.ComparatorEUStorage;
import ic2.core.block.base.util.info.EmitterInfo;
import ic2.core.block.base.util.info.EnergyInfo;
import ic2.core.block.base.util.info.TierInfo;
import ic2.core.block.base.util.info.misc.IEmitterTile;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.obj.IClickable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public abstract class GTCXTileEnergyOutputHatch extends TileEntityMachine implements IEUStorage, IEnergySourceInfo, IEmitterTile, IGTCasingBackgroundBlock, IGTDebuggableTile {
    public int tier;
    public int output;
    public int maxEnergy;
    @NetworkField(
            index = 3
    )
    public int energy;
    public boolean addedToEnergyNet;
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
    public GTCXTileEnergyOutputHatch(int tier, int maxEnergy, int output) {
        super(0);
        this.output = output;
        this.addedToEnergyNet = false;
        this.tier = tier;
        this.maxEnergy = maxEnergy;
        this.addGuiFields("output");
        this.addNetworkFields("casing", "config", "energy");
        this.addInfos(new InfoComponent[]{new EnergyInfo(this), new TierInfo(tier), new EmitterInfo(this)});
    }

    @Override
    protected void addComparators(ComparatorManager manager) {
        super.addComparators(manager);
        manager.addComparatorMode(new ComparatorEUStorage(this));
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
    public void onLoaded() {
        super.onLoaded();
        if (!this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    @Override
    public void onUnloaded() {
        if (this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energy = nbt.getInteger("energy");
        this.output = nbt.getInteger("output");
        casing = nbt.getInteger("casing");
        config = nbt.getInteger("config");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("energy", this.energy);
        nbt.setInteger("output", this.output);
        nbt.setInteger("casing", casing);
        nbt.setInteger("config", config);
        return nbt;
    }

    @Override
    public double getWrenchDropRate() {
        return 1.0D;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTLang.AESU;
    }

    @Override
    public double getOfferedEnergy() {
        return this.energy < this.output ? 0.0f :  this.output;
    }

    @Override
    public int getMaxSendingEnergy() {
        return this.output;
    }

    @Override
    public int getStoredEU() {
        return this.energy;
    }

    @Override
    public int getMaxEU() {
        return this.maxEnergy;
    }

    @Override
    public void drawEnergy(double amount) {
        this.energy -= (int)amount;
    }

    @Override
    public int getSourceTier() {
        return this.tier;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor iEnergyAcceptor, EnumFacing enumFacing) {
        return this.getFacing() == enumFacing;
    }

    @Override
    public int getOutput() {
        return this.output;
    }

    public void addEnergy(int amount){
        this.energy += amount;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        Block block = fromCasing(casing);
        map.put("Casing: " + (block == Blocks.AIR ? "None" : block.getLocalizedName()), true);
        map.put("Config: "+ config, true);
    }

    public static Block fromCasing(int casing){
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

    public boolean or(int compare, int... values){
        for (int i : values){
            if (compare == i){
                return true;
            }
        }
        return false;
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

    public static class GTCXTileDynamoHatch extends GTCXTileEnergyOutputHatch implements IClickable {
        public GTCXTileDynamoHatch() {
            super(4, 100000, 2048);
        }

        public void cycleTier(EntityPlayer player){
            if (tier == 4){
                tier = 5;
                output = 8192;
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_DYNAMO_HATCH_MODE_1);
            } else {
                tier = 4;
                output = 2048;
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_DYNAMO_HATCH_MODE_0);
            }
        }

        @Override
        public boolean hasRightClick() {
            return true;
        }

        @Override
        public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
            ItemStack stack = entityPlayer.getHeldItem(enumHand);
            if (stack.getItem() instanceof GTCXItemToolHammer){
                this.cycleTier(entityPlayer);
                stack.damageItem(1, entityPlayer);
                return true;
            }
            return false;
        }

        @Override
        public boolean hasLeftClick() {
            return false;
        }

        @Override
        public void onLeftClick(EntityPlayer entityPlayer, Side side) {

        }
    }

    public static class GTCXTileFusionEnergyExtractor extends GTCXTileEnergyOutputHatch{

        public GTCXTileFusionEnergyExtractor() {
            super(12, 1000000000, 134217728);
        }
    }
}
