package gtc_expansion.tile.hatch;

import gtc_expansion.block.GTCXBlockHatch;
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
    public void onBlockBreak() {
        if (owner != null){
            owner.setDisabled(false);
            owner.invalidateStructure();
        }
    }

    @Override
    public void onBlockUpdate(Block block) {
        onBlockPlaced();
        this.setConfig();
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
