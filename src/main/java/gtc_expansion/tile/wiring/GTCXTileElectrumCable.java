package gtc_expansion.tile.wiring;

import gtc_expansion.GTCXBlocks;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.tile.wiring.GTTileSuperconductorCable;
import ic2.api.classic.energy.tile.IAnchorConductor;
import ic2.api.classic.energy.tile.IEnergyConductorColored;
import ic2.api.classic.energy.tile.IEnergyConductorColored.WireColor;
import ic2.api.classic.energy.tile.IInsulationModifieableConductor;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.network.adv.NetworkField.BitLevel;
import ic2.api.classic.util.IWorldTickCallback;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.util.texture.TextureCopyStorage;
import ic2.core.block.wiring.tile.TileEntityCable;
import ic2.core.block.wiring.tile.TileEntityMultipartLuminator;
import ic2.core.energy.EnergyNetLocal;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GTCXTileElectrumCable extends TileEntityBlock implements IInsulationModifieableConductor, IGTRecolorableStorageTile, INetworkTileEntityEventListener, IAnchorConductor, IGTItemContainerTile {

    @NetworkField(index = 8)
    public RotationList connection;
    @NetworkField(
            index = 9
    )
    public RotationList anchors;
    protected boolean addedToEnergyNet;
    @NetworkField(index = 10)
    public int color;
    @NetworkField(
            index = 11
    )
    public byte foamed;
    @NetworkField(
            index = 12
    )
    public TextureCopyStorage storage;
    @NetworkField(
            index = 13,
            compression = BitLevel.Bit8
    )
    public int insulation;
    private static final String NBT_COLOR = "color";

    public GTCXTileElectrumCable() {
        this.color = 16383998;
        this.insulation = 0;
        this.foamed = 0;
        this.connection = RotationList.EMPTY;
        this.anchors = RotationList.EMPTY;
        this.addNetworkFields("connection", NBT_COLOR, "foamed", "storage", "insulation");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateConnections();
        if (!this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    @Override
    public void onUnloaded() {
        if (this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey(NBT_COLOR)) {
            this.color = nbt.getInteger(NBT_COLOR);
        } else {
            this.color = 16383998;
        }
        byte newFoamed = nbt.getByte("Foaming");
        this.insulation = nbt.getInteger("Insulation");
        this.connection = RotationList.ofNumber(nbt.getByte("Connection"));
        this.anchors = RotationList.ofNumber(nbt.getByte("Anchors"));
        if (newFoamed == 1) {
            this.changeFoam(newFoamed, true);
        } else {
            this.foamed = newFoamed;
        }
        this.storage.readFromNBT(nbt.getCompoundTag("Storage"));

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger(NBT_COLOR, this.color);
        nbt.setByte("Foaming", this.foamed);
        nbt.setInteger("Insulation", this.insulation);
        this.storage.writeToNBT(this.getTag(nbt, "Storage"));
        return nbt;
    }

    public boolean changeFoam(byte foamed) {
        return this.changeFoam(foamed, false);
    }

    private boolean changeFoam(byte newFoam, boolean duringLoad) {
        if (this.foamed == newFoam) {
            return false;
        } else {
            if (this.isSimulating()) {
                if ((this.foamed = newFoam) == 1) {
                    this.storage.setAll(WireColor.Silver);
                    if (!duringLoad) {
                        this.getNetwork().updateTileEntityField(this, "storage");
                    }

                    IC2.callbacks.addCallback(this.world, new IWorldTickCallback() {
                        public ActionResult<Integer> tickCallback(World world) {
                            if (!isInvalid() && foamed == 1) {
                                if (world.getLightFromNeighbors(getPos()) * 6 >= world.rand.nextInt(1000)) {
                                    changeFoam((byte)2);
                                    return ActionResult.newResult(EnumActionResult.SUCCESS, 0);
                                } else {
                                    return ActionResult.newResult(EnumActionResult.PASS, 500);
                                }
                            } else {
                                return ActionResult.newResult(EnumActionResult.FAIL, 0);
                            }
                        }
                    }, 500);
                } else if (this.foamed == 0) {
                }

                if (!duringLoad) {
                    this.getNetwork().updateTileEntityField(this, "foamed");
                }
            }

            return true;
        }
    }

    @Override
    public void onNetworkEvent(int event) {
        switch(event) {
            case 0:
                this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)this.getPos().getX() + Math.random(), (double)this.getPos().getY() + 1.2D, (double)this.getPos().getZ() + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                return;
            default:
                IC2.platform.displayError("An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\nT: " + this + " (" + this.getPos() + ")");
        }
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return this.connection.contains(side);
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return this.connection.contains(side);
    }

    @Override
    public double getConductionLoss() {
        if (insulation == 1){
            return 0.04D;
        }
        if (insulation == 2){
            return 0.035D;
        }
        if (insulation == 3){
            return 0.03D;
        }
        return 0.045D;
    }

    @Override
    public double getInsulationEnergyAbsorption() {
        return 0;
    }

    @Override
    public double getInsulationBreakdownEnergy() {
        return 9001D;
    }

    @Override
    public double getConductorBreakdownEnergy() {
        return 129;
    }

    @Override
    public void removeInsulation() {
        tryRemoveInsulation();
    }

    @Override
    public void removeConductor() {
        boolean burn = EnergyNetLocal.burn && this.world.rand.nextFloat() < EnergyNetLocal.chance;
        this.world.setBlockToAir(this.getPos());
        this.getNetwork().initiateTileEntityEvent(this, 0, true);
        if (burn) {
            this.world.setBlockState(this.getPos(), Blocks.FIRE.getDefaultState());
        }
    }

    public void updateConnections() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos sidedPos = pos.offset(facing);
            if (world.isBlockLoaded(sidedPos)) {
                world.neighborChanged(sidedPos, Blocks.AIR, pos);
            }
        }
        if (world.isBlockLoaded(pos)) {
            world.neighborChanged(pos, Blocks.AIR, pos);
        }
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        if (!this.isRendering()) {
            RotationList newList = RotationList.EMPTY;
            EnumFacing[] var3 = EnumFacing.VALUES;
            int var4 = var3.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                EnumFacing dir = var3[var5];
                IEnergyTile tile = EnergyNet.instance.getSubTile(this.getWorld(), this.getPos().offset(dir));
                if (tile == null || !(tile instanceof IEnergyAcceptor) && !(tile instanceof IEnergyEmitter)) {
                    tile = EnergyNet.instance.getTile(this.getWorld(), this.getPos().offset(dir));
                }
                if (tile != null && this.canConnect(tile, dir)) {
                    newList = newList.add(dir);
                }
            }
            if (this.connection.getCode() != newList.getCode()) {
                this.connection = newList;
                this.getNetwork().updateTileEntityField(this, "connection");
            }
        }
    }

    public boolean canConnect(IEnergyTile tile, EnumFacing side) {
        if (this.anchors.contains(side)) {
            return false;
        } else if (tile instanceof TileEntityCable) {
            return canInteractWithIc2Cable((TileEntityCable)tile, side.getOpposite());
        } else if (tile instanceof GTCXTileElectrumCable){
            return canInteractWithCable((GTCXTileElectrumCable)tile, side.getOpposite());
        } else if (tile instanceof GTTileSuperconductorCable) {
            return true;
        } else if (tile instanceof TileEntityMultipartLuminator) {
            return !((TileEntityMultipartLuminator)tile).hasSide(side.getOpposite().getIndex()) && this.canInteractWithAPICable((IEnergyConductorColored)tile);
        } else if (tile instanceof IAnchorConductor && ((IAnchorConductor)tile).hasAnchor(side.getOpposite())) {
            return false;
        } else if (tile instanceof IEnergyConductorColored) {
            return this.canInteractWithAPICable((IEnergyConductorColored)tile);
        } else if (tile instanceof IEnergyAcceptor && !(tile instanceof IEnergyEmitter)) {
            return ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
        } else if (tile instanceof IEnergyEmitter && !(tile instanceof IEnergyAcceptor)) {
            return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite());
        } else if (tile instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter) {
            return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite())
                    || ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
        } else {
            return tile instanceof IMetaDelegate || tile instanceof INetworkTileEntityEventListener;
        }
    }

    public boolean canInteractWithIc2Cable(TileEntityCable cable, EnumFacing side) {
        if (cable.hasAnchor(side)) {
            return false;
        } else {
            return this.color == GTMaterial.Electrum.getColor().getRGB() || cable.getConductorColor() == WireColor.Blank || this.color == cable.getConductorColor().toColor().getColorValue();
        }
    }

    public boolean canInteractWithCable(GTCXTileElectrumCable cable, EnumFacing side) {
        if (cable.hasAnchor(side)) {
            return false;
        } else {
            return this.color == GTMaterial.Electrum.getColor().getRGB() || cable.color == GTMaterial.Electrum.getColor().getRGB() || this.color == cable.color;
        }
    }

    public boolean canInteractWithAPICable(IEnergyConductorColored cable) {
        return this.color == GTMaterial.Electrum.getColor().getRGB() || cable.getConductorColor() == WireColor.Blank || this.color == cable.getConductorColor().toColor().getColorValue();
    }

    @Override
    public void onNetworkUpdate(String field) {
        if (field.equals("connection")) {
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
        super.onNetworkUpdate(field);
    }

    @Override
    public void setTileColor(int color) {
        if (this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        this.addedToEnergyNet = false;
        this.color = color;
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
        this.getNetwork().updateTileEntityField(this, NBT_COLOR);
    }

    @Override
    public Color getTileColor() {
        return new Color(this.color);
    }

    @Override
    public boolean isColored() {
        return this.color != 16383998;
    }

    @Override
    public boolean addAnchor(EnumFacing enumFacing) {
        if (this.anchors.contains(enumFacing)) {
            return false;
        } else if (this.isRendering()) {
            return true;
        } else {
            if (this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }

            this.addedToEnergyNet = false;
            this.anchors = this.anchors.add(enumFacing);
            this.getNetwork().updateTileEntityField(this, "anchors");
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            return true;
        }
    }

    @Override
    public boolean removeAnchor(EnumFacing enumFacing) {
        if (this.anchors.notContains(enumFacing)) {
            return false;
        } else {
            if (this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }

            this.addedToEnergyNet = false;
            this.anchors = this.anchors.remove(enumFacing);
            this.getNetwork().updateTileEntityField(this, "anchors");
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            return true;
        }
    }

    @Override
    public boolean hasAnchor(EnumFacing enumFacing) {
        return this.anchors.contains(enumFacing);
    }

    @Override
    public boolean tryAddInsulation() {
        if (this.insulation >= 3 || this.insulation < 0){
            return false;
        }
        if (this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        this.addedToEnergyNet = false;
        insulation += 1;
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
        this.getNetwork().updateTileEntityField(this, "insulation");
        return true;
    }

    @Override
    public boolean tryRemoveInsulation() {
        if (this.insulation <= 0){
            return false;
        }

        if (this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        this.addedToEnergyNet = false;

        this.insulation -= 1;

        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
        this.getNetwork().updateTileEntityField(this, "insulation");
        if (this.insulation == 0){
            setTileColor(GTMaterial.Electrum.getColor().getRGB());
        }
        return true;
    }

    public boolean hasInsulation(){
        return insulation > 0;
    }

    public boolean setInsulation(int newInsulation){
        if (newInsulation < 0 || newInsulation > 3){
            return false;
        }
        if (this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        this.addedToEnergyNet = false;

        this.insulation = newInsulation;

        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
        this.getNetwork().updateTileEntityField(this, "insulation");
        return true;
    }

    public Block getBlockDrop() {
        return GTCXBlocks.electrumCable;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();

        ItemStack block = GTMaterialGen.get(this.getBlockDrop());
        if (this.isColored() && this.color != GTMaterial.Electrum.getColor().getRGB()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(block);
            nbt.setInteger("color", this.color);
        }
        if (this.insulation > 0){
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(block);
            nbt.setInteger("insulation", this.insulation);
        }
        list.add(block);
        for(int i = 0; i < 6; ++i) {
            if (this.anchors.contains(EnumFacing.getFront(i))) {
                list.add(Ic2Items.miningPipe.copy());
            }
        }

        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        return null;
    }

    //    @Override
//    public boolean hasSpecialAction(EntityPlayer player, EnumFacing facing, Vec3d hit) {
//        EnumFacing side = (new ClickHelper(hit, (float)(registry.getInfoFromType(this.cableType).getConductorThickness(this.insulation) / 16.0D))).getFacing(facing);
//        return side != null && this.anchors.contains(side);
//    }
//
//    @Override
//    public EnumActionResult doSpecialAction(EntityPlayer player, EnumFacing facing, Vec3d hit) {
//        EnumFacing side = (new ClickHelper(hit, (float)(registry.getInfoFromType(this.cableType).getConductorThickness(this.insulation) / 16.0D))).getFacing(facing);
//        if (this.isRendering()) {
//            return EnumActionResult.PASS;
//        } else if (side != null && this.removeAnchor(side)) {
//            ItemStack pipe = Ic2Items.miningPipe.copy();
//            if (!player.inventory.addItemStackToInventory(pipe)) {
//                player.dropItem(pipe, true);
//            }
//
//            return EnumActionResult.SUCCESS;
//        } else {
//            return super.doSpecialAction(player, facing, hit);
//        }
//    }
}
