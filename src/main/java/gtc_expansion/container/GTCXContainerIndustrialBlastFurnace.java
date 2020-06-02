package gtc_expansion.container;

import gtc_expansion.data.GTCXItems;
import gtc_expansion.gui.GTCXGuiICompIBFString;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.gui.GTGuiCompMultiTileStatus;
import gtclassic.api.gui.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerIndustrialBlastFurnace extends ContainerTileComponent<GTCXTileMultiIndustrialBlastFurnace> {

	public static final Box2D machineProgressBox = new Box2D(78, 23, 20, 18);
	public static final Vec2i machineProgressPos = new Vec2i(176, 0);

	public GTCXContainerIndustrialBlastFurnace(InventoryPlayer player, GTCXTileMultiIndustrialBlastFurnace tile) {
		super(tile);
		for (int y = 0; y < 2; ++y) {
			for (int x = 0; x < 2; ++x) {
				this.addSlotToContainer(new SlotCustom(tile, x + y * 2, 35 + x * 18, 17 + y * 18, tile.filter));
			}
		}
		for (int y = 0; y < 2; ++y) {
			for (int x = 0; x < 2; ++x) {
				this.addSlotToContainer(new SlotOutput(player.player, tile, 4 + x + y * 2, 107 + x * 18, 17 + y * 18));
			}
		}
		this.addSlotToContainer(new SlotCustom(tile, 8, 8, 63, new ArrayFilter(new BasicItemFilter(GTCXItems.kanthalHeatingCoil), new BasicItemFilter(GTCXItems.nichromeHeatingCoil))));
		for (int i = 0; i < 2; ++i) {
			this.addSlotToContainer(new GTSlotUpgrade(tile, 9 + i, 152, 17 + i * 18));
		}
		this.addPlayerInventory(player);
		this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
		this.addComponent(new GTGuiCompMachinePower(tile));
		this.addComponent(new GTGuiCompMultiTileStatus(tile,new Box2D(12, 6, 12, 51)));
		this.addComponent(new GTCXGuiICompIBFString(tile));
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