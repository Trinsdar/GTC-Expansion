package gtc_expansion.tile;

import gtclassic.tile.GTTileBaseMachine;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

public class GETileChemicalReactor extends GTTileBaseMachine {
    public GETileChemicalReactor(int slots, int upgrades, int energyPerTick, int maxProgress, int maxinput) {
        super(slots, upgrades, energyPerTick, maxProgress, maxinput);
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public IFilter[] getInputFilters(int[] ints) {
        return new IFilter[0];
    }

    @Override
    public boolean isRecipeSlot(int i) {
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return null;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return null;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }
}
