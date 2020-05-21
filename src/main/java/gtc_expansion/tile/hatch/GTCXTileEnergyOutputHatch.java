package gtc_expansion.tile.hatch;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.util.IGTCasingBackgroundBlock;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.common.GTLang;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public abstract class GTCXTileEnergyOutputHatch extends TileEntityElectricBlock implements IGTCasingBackgroundBlock, IGTDebuggableTile {
    @NetworkField(
            index = 6
    )
    public int casing = 0;
    private int prevCasing = 0;

    @NetworkField(
            index = 7
    )
    public int config = 0;
    private int prevConfig = 0;
    public GTCXTileEnergyOutputHatch(int tier, int maxEnergy, int output) {
        super(tier, (int) EnergyNet.instance.getPowerFromTier(tier), maxEnergy);
        this.output = output;
        this.addGuiFields("output");
        this.addNetworkFields("casing", "config");
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
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return null;
    }

    @Override
    public boolean hasGui(EntityPlayer player) {
        return false;
    }

    @Override
    public int getProcessRate() {
        return 128;
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
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return false;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        if (amount <= 0.0D) {
            return 0.0D;
        }
        energy = ((int) (energy + amount));
        int left = 0;
        if (energy >= maxEnergy) {
            left = energy - maxEnergy;
            energy = maxEnergy;
        }
        getNetwork().updateTileGuiField(this, "energy");
        return left;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Casing: " + fromCasing(casing).getLocalizedName(), true);
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
            if (world.getBlockState(offset).getBlock() == GTCXBlocks.casingStandard){
                standard++;
            } else if (world.getBlockState(offset).getBlock() == GTCXBlocks.casingReinforced){
                reinforced++;
            } else if (world.getBlockState(offset).getBlock() == GTCXBlocks.casingAdvanced){
                advanced++;
            }
        }
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
        else if (standard == 3 && reinforced == 3){
            casing = world.rand.nextInt(1) + 1;
        }
        else if (standard == 3 && advanced == 3){
            casing = world.rand.nextInt(1) == 0 ? 1 : 3;
        }
        else if (reinforced == 3 && advanced == 3){
            casing = world.rand.nextInt(1) + 2;
        }
        else if ((standard == 2 && reinforced == 2 && advanced == 2) || (standard == 1 && reinforced == 1 && advanced == 1)){
            casing = world.rand.nextInt(2) + 1;
        }
        else if (standard == 3){
            casing = 1;
        }
        else if (reinforced == 3){
            casing = 2;
        }
        else if (advanced == 3){
            casing = 3;
        }
        else if ((standard + reinforced == 4) || (standard + reinforced == 2)){
            casing = world.rand.nextInt(1) + 1;
        }
        else if ((standard + advanced == 4) || (standard + advanced == 2)){
            casing = world.rand.nextInt(1) == 0 ? 1 : 3;
        }
        else if ((reinforced + advanced == 4) || (reinforced + advanced == 2)){
            casing = world.rand.nextInt(1) + 2;
        }
        else if (standard == 2){
            casing = 1;
        }
        else if (reinforced == 2){
            casing = 2;
        }
        else if (advanced == 2){
            casing = 3;
        }
        else if (standard == 1){
            casing = 1;
        }
        else if (reinforced == 1){
            casing = 2;
        }
        else if (advanced == 1){
            casing = 3;
        }
        if (casing != this.prevCasing) {
            this.getNetwork().updateTileEntityField(this, "casing");
        }

        this.prevCasing = casing;
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
        if (world.getTileEntity(pos) instanceof IGTCasingBackgroundBlock){
            return ((IGTCasingBackgroundBlock)world.getTileEntity(pos)).getCasing() == casing;
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

    public static class GTCXTileDynamoHatch extends GTCXTileEnergyOutputHatch{
        public GTCXTileDynamoHatch() {
            super(4, 10000, 2048);
        }
    }
}
