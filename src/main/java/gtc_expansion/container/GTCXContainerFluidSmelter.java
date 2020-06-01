package gtc_expansion.container;

import gtc_expansion.gui.GTCXGuiICompFluidSmelterString;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.gui.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDischarge;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class GTCXContainerFluidSmelter extends ContainerTileComponent<GTCXTileFluidSmelter> {

    public static Box2D machineProgressBox = new Box2D(78, 24, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public static ArrayList<IFilter> filters = new ArrayList<>();
    public GTCXContainerFluidSmelter(InventoryPlayer player, GTCXTileFluidSmelter tile) {
        super(tile);
        IFilter[] filter = new IFilter[filters.size()];
        filter = filters.toArray(filter);
        this.addSlotToContainer(new SlotCustom(tile, 0, 53, 25, tile.filter)); // input
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 2, 8, 62)); // battery
        this.addSlotToContainer(new SlotDisplay(tile,  1, 107, 25)); // output tank
        this.addSlotToContainer(new SlotCustom(tile, 3, 44, 62, new ArrayFilter(filter)));
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 4 + i, 80 + i * 18, 62));
        }
        this.addComponent(new GTCXGuiICompFluidSmelterString(tile));
        this.addComponent(new GTGuiCompMachinePower(tile, 116, 62));
        this.addPlayerInventory(player);
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.dissableInvName();
    }

    @Override
    public ResourceLocation getTexture() {
        return this.getGuiHolder().getGuiTexture();
    }

    @Override
    public int guiInventorySize() {
        return this.getGuiHolder().slotCount;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }
}
