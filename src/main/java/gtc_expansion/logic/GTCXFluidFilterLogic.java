package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class GTCXFluidFilterLogic extends GTCXBaseCoverLogic {
    FluidStack filter = null;
    Modes mode = Modes.NORMAL;
    public GTCXFluidFilterLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {

    }

    public FluidStack getFilter() {
        return filter;
    }

    public boolean matches(FluidStack compare){
        boolean invert = this.mode == Modes.INVERT || this.mode == Modes.INVERT_EXPORT;
        if (compare == null || this.filter == null){
            return true;
        }
        return invert != compare.isFluidEqual(this.filter);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        mode = Modes.values()[nbt.getInteger("mode")];
        this.filter = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Filter"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", mode.ordinal());
        if (this.filter != null){
            nbt.setTag("Filter", this.filter.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void read(IInputBuffer buffer) {
        mode = Modes.values()[buffer.readInt()];
        this.filter = FluidStack.loadFluidStackFromNBT(buffer.readNBTData());
    }

    @Override
    public void write(IOutputBuffer buffer) {
        buffer.writeInt(mode.ordinal());
        if (this.filter != null){
            buffer.writeNBTData(this.filter.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (this.pipe.isSimulating()){
            mode = player.isSneaking() ? mode.cycleBack(player) : mode.cycle(player);
        }
        return true;
    }

    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        if (side.isServer()){
            ItemStack stack = entityPlayer.getHeldItem(enumHand);
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null){
                this.filter = fluid;
                return true;
            }
        }
        return false;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put(this.mode.toString(), true);
        String fluid = this.filter != null ? this.filter.getLocalizedName() : "None";
        map.put("Filtered Fluid: " + fluid, true);
    }

    public enum Modes{
        NORMAL,
        INVERT,
        NORMAL_EXPORT,
        INVERT_EXPORT;
        Modes cycle(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return INVERT;
            } else if (this == INVERT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return NORMAL_EXPORT;
            } else if (this == NORMAL_EXPORT){
                return INVERT_EXPORT;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return NORMAL;
            }
        }

        Modes cycleBack(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return INVERT_EXPORT;
            } else if (this == INVERT_EXPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return NORMAL_EXPORT;
            } else if (this == NORMAL_EXPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return INVERT;
            } else {
                return NORMAL;
            }
        }
    }
}
