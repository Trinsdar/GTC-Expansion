package gtc_expansion.interfaces;

import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes;
import gtc_expansion.util.GTCXTank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IGTOwnerTile {
    void setShouldCheckRecipe(boolean checkRecipe);

    void setDisabled(boolean disabled);

    void setStackInSlot(int slot, ItemStack stack);

    ItemStack getStackInSlot(int slot);

    boolean hasCapability(Capability<?> capability, EnumFacing facing);

    <T> T getCapability(Capability<T> capability, EnumFacing facing);

    GTCXTank getInputTank1();

    GTCXTank getInputTank2();

    GTCXTank getOutputTank1();

    GTCXTank getOutputTank2();

    default void setOutputModes(boolean second, OutputModes mode){}
}
