package gtc_expansion.interfaces;

import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes;
import gtc_expansion.util.GTCXTank;
import ic2.core.inventory.container.ContainerIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IGTOwnerTile {
    void setShouldCheckRecipe(boolean checkRecipe);

    void setDisabled(boolean disabled);

    void setStackInSlot(int slot, ItemStack stack);

    ItemStack getStackInSlot(int slot);

    int getOutputSlot(GTCXTileItemFluidHatches hatch);

    int getInputSlot(GTCXTileItemFluidHatches hatch);

    boolean hasCapability(Capability<?> capability, EnumFacing facing);

    <T> T getCapability(Capability<T> capability, EnumFacing facing, GTCXTileItemFluidHatches hatch);

    GTCXTank getTank(GTCXTileItemFluidHatches hatch);

    default void setOutputModes(boolean second, OutputModes mode){}

    void invalidateStructure();

    EnumFacing getFacing();

    ContainerIC2 getGuiContainer(EntityPlayer entityPlayer, GTCXTileItemFluidHatches hatch);
}
