package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBaseFluidPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import ic2.core.fluid.IC2Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class GTCXPumpModuleLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.IMPORT;
    public GTCXPumpModuleLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {
        if (this.pipe instanceof GTCXTileBaseFluidPipe){
            GTCXTileBaseFluidPipe fluidPipe = (GTCXTileBaseFluidPipe) pipe;
            boolean redstone = pipe.isRedstonePowered();
            boolean proceed = this.mode == Modes.IMPORT || this.mode == Modes.IMPORT_EXPORT || ((this.mode == Modes.IMPORT_CONDITIONAL || this.mode == Modes.IMPORT_EXPORT_CONDITIONAL) && redstone) || ((this.mode == Modes.IMPORT_INVERSE_CONDITIONAL || this.mode == Modes.IMPORT_EXPORT_INVERSE_CONDITIONAL) && !redstone);
            TileEntity tile = fluidPipe.getWorld().getTileEntity(fluidPipe.getPos().offset(this.facing));
            if (pipe.connection.contains(this.facing) && tile != null && !(tile instanceof GTCXTileBaseFluidPipe) & tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.facing.getOpposite()) && proceed){
                IFluidHandler tileFluid = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.facing.getOpposite());
                if (tileFluid != null){
                    FluidStack drainSimulate = tileFluid.drain(fluidPipe.getTank().getCapacity(), false);
                    if (drainSimulate != null){
                        IC2Tank tank = fluidPipe.getFluidTankFillable2(drainSimulate);
                        if (tank != null){
                            FluidStack pipeFluid = tank.getFluid();
                            if (pipeFluid == null || (pipeFluid.isFluidEqual(drainSimulate) && tank.getFluidAmount() < tank.getCapacity())){
                                tileFluid.drain(new GTCXTileBaseFluidPipe.FacingFillWrapper(this.facing, fluidPipe).fill(drainSimulate, true), true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        mode = Modes.values()[nbt.getInteger("mode")];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", mode.ordinal());
    }

    @Override
    public void read(IInputBuffer buffer) {
        mode = Modes.values()[buffer.readInt()];
    }

    @Override
    public void write(IOutputBuffer buffer) {
        buffer.writeInt(mode.ordinal());
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (this.pipe.isSimulating()){
            mode = mode.cycle(player);
        }
        return true;
    }

    @Override
    public boolean allowsPipeOutput(){
        return mode.ordinal() > 2;
    }

    public enum Modes{
        IMPORT,
        IMPORT_CONDITIONAL,
        IMPORT_INVERSE_CONDITIONAL,
        IMPORT_EXPORT,
        IMPORT_EXPORT_CONDITIONAL,
        IMPORT_EXPORT_INVERSE_CONDITIONAL;
        Modes cycle(EntityPlayer player){
            if (this == IMPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return IMPORT_CONDITIONAL;
            } else if (this == IMPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return IMPORT_INVERSE_CONDITIONAL;
            } else if (this == IMPORT_INVERSE_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_3);
                return IMPORT_EXPORT;
            } else if (this == IMPORT_EXPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_4);
                return IMPORT_EXPORT_CONDITIONAL;
            } else if (this == IMPORT_EXPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_5);
                return IMPORT_EXPORT_INVERSE_CONDITIONAL;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return IMPORT;
            }
        }
    }
}
