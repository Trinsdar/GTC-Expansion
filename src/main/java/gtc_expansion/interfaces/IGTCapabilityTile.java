package gtc_expansion.interfaces;

import ic2.core.inventory.base.IHasGui;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IGTCapabilityTile extends IHasGui {

    boolean hasCapability(Capability<?> capability, EnumFacing facing);
    
    <T> T getCapability(Capability<T> capability, EnumFacing facing);
}
