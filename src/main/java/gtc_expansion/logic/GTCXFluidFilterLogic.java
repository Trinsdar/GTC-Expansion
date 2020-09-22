package gtc_expansion.logic;

import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import ic2.core.util.misc.StackUtil;
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
        boolean invert = this.mode == Modes.INVERT;
        if (compare == null || this.filter == null){
            return false;
        }
        return invert != compare.isFluidEqual(this.filter);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int index = nbt.getInteger("mode");
        mode = Modes.values()[index < 2 ? index : 0];
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
        int index = buffer.readInt();
        mode = Modes.values()[index < 2 ? index : 0];
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
            mode = mode.cycle(player);
        }
        return true;
    }

    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        ItemStack stack = entityPlayer.getHeldItem(enumHand);
        FluidStack fluid = this.getFluidContained(stack);
        if (fluid != null){
            if (side.isServer()){
                this.filter = fluid;
                IC2.platform.messagePlayer(entityPlayer, "Filter set to " + fluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    private FluidStack getFluidContained(ItemStack stack){
        if (stack.getItem() == GTCXItems.dataOrbStorage){
            NBTTagCompound nbt = StackUtil.getNbtData(stack);
            if (!nbt.hasKey("Fluid")) {
                return null;
            }
            NBTTagCompound data = nbt.getCompoundTag("Fluid");
            return FluidStack.loadFluidStackFromNBT(data);
        }
        return FluidUtil.getFluidContained(stack);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put(this.mode.toString(), true);
        String fluid = this.filter != null ? this.filter.getLocalizedName() : "None";
        map.put("Filtered Fluid: " + fluid, true);
    }

    @Override
    public boolean allowsPipeOutput(){
        return true;
    }

    public boolean allowsPipeOutput(FluidStack compare){
        if (this.mode.ordinal() < 2){
            boolean invert = this.mode == Modes.INVERT;
            if (compare == null || this.filter == null){
                return false;
            }
            return invert != compare.isFluidEqual(this.filter);
        }
        return allowsPipeOutput();
    }

    public enum Modes{
        NORMAL,
        INVERT;
        Modes cycle(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_FILTER_MODE_1);
                return INVERT;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_FILTER_MODE_0);
                return NORMAL;
            }
        }
    }
}
