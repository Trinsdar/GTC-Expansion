package gtc_expansion.tile.steam;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerSteamAlloySmelter;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseSteamMachine;
import gtc_expansion.util.GTCXSteamMachineFilter;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXTileSteamAlloySmelter extends GTCXTileBaseSteamMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/bronzealloysmelter.png");
    public IFilter filter = new GTCXSteamMachineFilter(this);
    public GTCXTileSteamAlloySmelter() {
        super(3, 100, 32);
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0, 1};
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[]{filter};
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot < 2;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{2};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.ALLOY_SMELTER_RECIPE_LIST;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return Ic2Sounds.electricFurnaceLoop;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerSteamAlloySmelter(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GTCXMachineGui.GTCXSteamAlloySmelterGui.class;
    }

    public ResourceLocation getGuiLocation(){
        return GUI_LOCATION;
    }
}
