package gtc_expansion.tile.base;

import gtc_expansion.util.EnergyConsumer;
import gtc_expansion.util.MultiBlockHelper;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.tile.GTTileBaseMachine;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.transport.IItemTransporter;
import ic2.core.inventory.transport.TransporterManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GTCXTileMultiBaseMachineAdv extends GTTileBaseMachine implements IMetaDelegate, IGTMultiTileStatus {
    public boolean lastState;
    public boolean firstCheck = true;
    List<IEnergyTile> lastPositions = null;

    public GTCXTileMultiBaseMachineAdv(int slots, int upgrades, int defaultinput, int maxinput) {
        super(slots, upgrades, defaultinput, 100, maxinput);
    }

    @Override
    public boolean canWork() {
        boolean superCall = super.canWork();
        if (superCall) {
            if (this.world.getTotalWorldTime() % 256L == 0L || this.firstCheck) {
                boolean lastCheck = this.lastState;
                this.lastState = this.checkStructure();
                this.firstCheck = false;
                if (lastCheck != this.lastState) {
                    if (this.addedToEnergyNet) {
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                    }

                    this.lastPositions = null;
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                    this.addedToEnergyNet = true;
                    MultiBlockHelper.INSTANCE.removeCore(this.getWorld(), this.getPos());
                    if (this.lastState) {
                        MultiBlockHelper.INSTANCE.addCore(this.getWorld(), this.getPos(), new ArrayList(this.provideStructure().keySet()));
                    }
                }
            }

            superCall = superCall && this.lastState;
        }

        return superCall;
    }

    public Map<BlockPos, IBlockState> provideStructure() {
        return new Object2ObjectLinkedOpenHashMap();
    }

    public boolean checkStructure() {
        return this.world.isAreaLoaded(this.pos, 3);
    }

    @Override
    public boolean hasGui(EntityPlayer player) {
        return true;
    }

    @Override
    public List<IEnergyTile> getSubTiles() {
        if (this.lastPositions == null) {
            this.lastPositions = new ArrayList();
            this.lastPositions.add(this);
            if (this.checkStructure()) {
                Iterator var1 = this.provideStructure().entrySet().iterator();

                while(var1.hasNext()) {
                    Map.Entry<BlockPos, IBlockState> entry = (Map.Entry)var1.next();
                    if (!((BlockPos)entry.getKey()).equals(this.getPos()) && this.world.getBlockState((BlockPos)entry.getKey()) == entry.getValue()) {
                        this.lastPositions.add(new EnergyConsumer(this.getWorld(), (BlockPos)entry.getKey(), this));
                    }
                }
            }
        }

        return this.lastPositions;
    }

    @Override
    public void onUnloaded() {
        MultiBlockHelper.INSTANCE.removeCore(this.getWorld(), this.getPos());
        super.onUnloaded();
    }

    @Override
    public void update() {
        tryImportItems();
        super.update();
        tryExportItems();
    }

    public List<TileEntity> getImportTiles() {
        int3 dir = new int3(getPos(), getFacing());
        List<TileEntity> list = new ArrayList<>();
        list.add(world.getTileEntity(dir.up(1).asBlockPos()));
        return list;
    }

    public List<TileEntity> getExportTiles() {
        int3 dir = new int3(getPos(), getFacing());
        List<TileEntity> list = new ArrayList<>();
        list.add(world.getTileEntity(dir.up(1).asBlockPos()));
        return list;
    }

    public void tryImportItems() {
        if (world.getTotalWorldTime() % 20 == 0 && canWork()) {
            upper:
            for (TileEntity tile : getImportTiles()){
                IItemTransporter slave = TransporterManager.manager.getTransporter(tile, true);
                if (slave == null) {
                    return;
                }
                IItemTransporter controller = TransporterManager.manager.getTransporter(this, true);

                IFilter filter = new MachineFilter(this);
                int limit = 64;

                lower:
                for (int i = 0; i < limit; ++i) {
                    ItemStack stack = slave.removeItem(filter, getFacing().getOpposite(), 1, false);
                    if (stack.isEmpty()) {
                        break lower;
                    }

                    ItemStack added = controller.addItem(stack, getFacing().UP, true);
                    if (added.getCount() <= 0) {
                        break lower;
                    }

                    slave.removeItem(filter, getFacing().getOpposite(), 1, true);
                }
            }

        }
    }

    public void tryExportItems() {
        if (world.getTotalWorldTime() % 20 == 0) {
            for (TileEntity tile : getExportTiles()){
                IItemTransporter slave = TransporterManager.manager.getTransporter(tile, true);
                if (slave == null) {
                    return;
                }
                IItemTransporter controller = TransporterManager.manager.getTransporter(this, true);

                int limit = 64;

                for (int i = 0; i < limit; ++i) {
                    ItemStack stack = controller.removeItem(CommonFilters.Anything, getFacing().EAST, 1, false);
                    if (stack.isEmpty()) {
                        break;
                    }

                    ItemStack added = slave.addItem(stack, getFacing().UP, true);
                    if (added.getCount() <= 0) {
                        break;
                    }

                    controller.removeItem(CommonFilters.Anything, getFacing().getOpposite(), 1, true);
                }
            }
        }
    }

    @Override
    public boolean getStructureValid() {
        return lastState;
    }
}
