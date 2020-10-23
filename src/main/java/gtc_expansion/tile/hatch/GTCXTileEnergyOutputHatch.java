package gtc_expansion.tile.hatch;

import gtc_expansion.block.GTCXBlockHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.interfaces.IGTEnergySource;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.common.GTLang;
import ic2.api.classic.audio.PositionSpec;
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
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Sounds;
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
    private IGTEnergySource owner = null;
    public GTCXTileEnergyOutputHatch(int tier, int maxEnergy, int output) {
        super(0);
        this.output = output;
        this.addedToEnergyNet = false;
        this.tier = tier;
        this.maxEnergy = maxEnergy;
        this.addGuiFields("output");
        this.addNetworkFields("casing", "config", "energy");
        this.addInfos(new EnergyInfo(this), new TierInfo(tier), new EmitterInfo(this));
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
        this.output = nbt.getInteger("output");
        casing = nbt.getInteger("casing");
        config = nbt.getInteger("config");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
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
    public void onBlockBreak() {
        if (this.owner != null && owner instanceof IGTOwnerTile){
            ((IGTOwnerTile)owner).invalidateStructure();
        }
    }

    @Override
    public double getOfferedEnergy() {
        return this.owner == null  ? 0.0f : Math.min(this.getStoredEU(), this.output);
    }

    @Override
    public int getMaxSendingEnergy() {
        return this.output;
    }

    @Override
    public int getStoredEU() {
        return this.owner != null ? this.owner.getStoredEnergy() : 0;
    }

    @Override
    public int getMaxEU() {
        return this.owner != null ? this.owner.getMaxEnergy() : 0;
    }

    @Override
    public void drawEnergy(double amount) {
        if (owner != null){
            owner.drawEnergy(amount);
        }
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
        return this.getMaxSendingEnergy();
    }

    public void addEnergy(int amount){
        this.energy += amount;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        Block block = fromCasing(casing);
        map.put("Casing: " + (block == Blocks.AIR ? "None" : block.getLocalizedName()), true);
        map.put("Config: "+ config, true);
        map.put("Has owner? " + (owner != null), true);
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
        int hatches = 0;
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offset = this.getPos().offset(facing);
            Block block = world.getBlockState(offset).getBlock();
            if (block == GTCXBlocks.casingStandard){
                standard++;
            } else if (block == GTCXBlocks.casingReinforced){
                reinforced++;
            } else if (block == GTCXBlocks.casingAdvanced){
                advanced++;
            } else if (block instanceof GTCXBlockHatch){
                hatches++;
            }
        }
        if (standard > 3 || (standard == 3 && hatches == 1)){
            casing = 1;
        } else if (reinforced > 3 || (reinforced == 3 && hatches == 1)){
            casing = 2;
        } else if (advanced > 3 || (advanced == 3 && hatches == 1)){
            casing = 3;
        } else {
            casing = 0;
        }
        if (casing != this.prevCasing) {
            world.notifyNeighborsOfStateChange(pos, GTCXBlocks.casingStandard, true);
            this.getNetwork().updateTileEntityField(this, "casing");
        }

        this.prevCasing = casing;
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
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        this.setConfig();
    }

    @Override
    public EnumFacing getFacing(){
        return super.getFacing();
    }

    @Override
    public boolean getActive(){
        return super.getActive();
    }

    public void setOwner(IGTEnergySource owner) {
        this.owner = owner;
    }

    public IGTEnergySource getOwner() {
        return owner;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
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
            if (stack.getItem() instanceof IGTScrewdriver && side.isServer()){
                this.cycleTier(entityPlayer);
                ((IGTScrewdriver)stack.getItem()).damage(stack, entityPlayer);
                IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
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
