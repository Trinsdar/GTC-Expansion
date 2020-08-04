package gtc_expansion.interfaces;

import ic2.api.classic.energy.tile.IEnergySourceInfo;

// Way for me to get Stored Eu without using IEUStorage
public interface IGTEnergySource extends IEnergySourceInfo {
    int getStoredEnergy();

    int getMaxEnergy();
}
