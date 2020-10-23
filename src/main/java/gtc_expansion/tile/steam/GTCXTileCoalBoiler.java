package gtc_expansion.tile.steam;

import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.tile.machine.IFuelMachine;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;

public class GTCXTileCoalBoiler extends TileEntityMachine implements IFuelMachine, ITickable {
    @NetworkField(index = 3)
    int heat;
    @NetworkField(index = 4)
    int maxHeat = 500;
    @NetworkField(index = 5)
    int fuel = 0;
    @NetworkField(index = 6)
    int maxFuel;
    @NetworkField(index = 7)
    IC2Tank water = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.getFluid().getName().equals("water");
        }
    };
    @NetworkField(index = 8)
    IC2Tank steam = new IC2Tank(16000);
    public GTCXTileCoalBoiler() {
        super(4);
    }

    public int getHeat() {
        return heat;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    @Override
    public float getFuel() {
        return fuel;
    }

    @Override
    public float getMaxFuel() {
        return maxFuel;
    }

    @Override
    public void update() {

        if (this.getWorld().getWorldTime() % 20 == 0){
            if (this.heat > 100){

            }
        }
    }
}
