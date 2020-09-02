package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBaseFluidPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import ic2.core.fluid.IC2Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.FluidStack;

public class GTCXDrainModuleLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.IMPORT;
    int leftOver = 0;
    static final FluidStack water = GTMaterialGen.getFluidStack("water", 1000);
    public GTCXDrainModuleLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {
        if (this.facing == EnumFacing.UP && this.pipe instanceof GTCXTileBaseFluidPipe){
            GTCXTileBaseFluidPipe fluidPipe = (GTCXTileBaseFluidPipe) pipe;
            IC2Tank tank = fluidPipe.getTank();
            if (leftOver > 0){
                if (tank.getCapacity() - tank.getFluidAmount() >= leftOver){
                    tank.fill(water, true);
                    leftOver = 0;
                } else {
                    int room = tank.getCapacity() - tank.getFluidAmount();
                    leftOver -= room;
                    tank.fill(GTMaterialGen.getFluidStack("water", room), true);
                }
                return;
            }
            if (tank.getFluid() == null || tank.getFluid().isFluidEqual(water)){
                if (fluidPipe.getWorld().getBlockState(fluidPipe.getPos().offset(this.facing)).getBlock() == Blocks.WATER){
                    if (!BiomeDictionary.hasType(fluidPipe.getWorld().getBiome(fluidPipe.getPos()), BiomeDictionary.Type.OCEAN)){
                        fluidPipe.getWorld().setBlockToAir(fluidPipe.getPos().up());
                    }
                    if (tank.getCapacity() - tank.getFluidAmount() >= 1000){
                        tank.fill(water, true);
                        leftOver = 0;
                    } else {
                        int room = tank.getCapacity() - tank.getFluidAmount();
                        leftOver = 1000 - room;
                        tank.fill(GTMaterialGen.getFluidStack("water", room), true);
                    }

                }
            }

        }
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (this.pipe.isSimulating()){
            mode = mode.cycle(player);
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        leftOver = nbt.getInteger("leftOver");
        mode = Modes.values()[nbt.getInteger("mode")];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("leftOver", leftOver);
        nbt.setInteger("mode", mode.ordinal());
    }

    @Override
    public void read(IInputBuffer buffer) {
        this.leftOver = buffer.readInt();
        mode = Modes.values()[buffer.readInt()];
    }

    @Override
    public void write(IOutputBuffer buffer) {
        buffer.writeInt(leftOver);
        buffer.writeInt(mode.ordinal());
    }

    public enum Modes{
        IMPORT,
        IMPORT_CONDITIONAL,
        IMPORT_INVERSE_CONDITIONAL;
        Modes cycle(EntityPlayer player){
            if (this == IMPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_0);
                return IMPORT_CONDITIONAL;
            } else if (this == IMPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_1);
                return IMPORT_INVERSE_CONDITIONAL;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_3);
                return IMPORT;
            }
        }
    }
}
