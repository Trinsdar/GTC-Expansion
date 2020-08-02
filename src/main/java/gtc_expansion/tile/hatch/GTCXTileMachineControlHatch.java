package gtc_expansion.tile.hatch;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.block.base.tile.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileMachineControlHatch extends TileEntityMachine implements IGTCasingBackgroundBlock, IGTItemContainerTile {
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
    private int redstoneLevel = -1;
    public GTCXTileMachineControlHatch() {
        super(1);
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
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return this.getFacing() != facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        casing = nbt.getInteger("casing");
        config = nbt.getInteger("config");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
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
        list.add(this.getStackInSlot(0));
        return list;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    public void setOwner(IGTOwnerTile tile){
        this.owner = tile;
    }

    public IGTOwnerTile getOwner(){
        return owner;
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
    public void onBlockBreak() {
        if (owner != null){
            owner.setDisabled(false);
        }
    }

    @Override
    public void onBlockUpdate(Block block) {
        onBlockPlaced();
    }

    public void onBlockPlaced(){
        int newLevel = world.getRedstonePower(this.pos.offset(this.getFacing()), this.getFacing());
        if (newLevel != this.redstoneLevel) {
            this.redstoneLevel = newLevel;
        }
        if (this.owner != null) {
            if (this.redstoneLevel > 0) {
                owner.setDisabled(true);
            } else {
                owner.setDisabled(false);
            }
        }
    }
}
