package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXIcons;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.GTCXTileAlloyFurnace;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileBath;
import gtc_expansion.tile.GTCXTileBrick;
import gtc_expansion.tile.GTCXTileCentrifuge;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileDieselGenerator;
import gtc_expansion.tile.GTCXTileDigitalTank;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileExtruder;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileGasTurbine;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.tile.GTCXTilePlateCutter;
import gtc_expansion.tile.GTCXTileStoneCompressor;
import gtc_expansion.tile.GTCXTileStoneExtractor;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileFusionEnergyInjector;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import gtc_expansion.tile.multi.GTCXTileMultiCokeOven;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialSawmill;
import gtc_expansion.tile.multi.GTCXTileMultiLargeGasTurbine;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiThermalBoiler;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtc_expansion.tile.steam.GTCXTileCoalBoiler;
import gtc_expansion.tile.steam.GTCXTileSteamAlloySmelter;
import gtc_expansion.tile.steam.GTCXTileSteamCompressor;
import gtc_expansion.tile.steam.GTCXTileSteamExtractor;
import gtc_expansion.tile.steam.GTCXTileSteamForgeHammer;
import gtc_expansion.tile.steam.GTCXTileSteamFurnace;
import gtc_expansion.tile.steam.GTCXTileSteamMacerator;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTReaderInfoBlock;
import gtclassic.api.material.GTMaterialGen;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.lang.storage.Ic2InfoLang;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GTCXBlockTile extends GTBlockBaseMachine implements IGTReaderInfoBlock {
    String name;
    public GTCXBlockTile(String name, LocaleComp comp) {
        this(name, comp, 0);
    }

    public GTCXBlockTile(String name, LocaleComp comp, int additionalInfo) {
        super(Material.IRON, comp, additionalInfo);
        this.name = name;
        setRegistryName(this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
        setHardness(100.0F);
        setResistance(20.0F);
        setSoundType(SoundType.METAL);
    }

    public GTCXBlockTile(String name, LocaleComp comp, Material material){
        this(name, comp, material, 0);
    }

    public GTCXBlockTile(String name, LocaleComp comp, Material material, int additionalInfo){
        super(material, comp, additionalInfo);
        this.name = name;
        setRegistryName(this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
        this.setSoundType(SoundType.STONE);
        if (name.equals("fire_brick_block")){
            this.setHardness(2.0F);
        } else {
            this.setHardness(4.0F);
        }
        this.setResistance(10.0F);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this == GTCXBlocks.fireBrickBlock){
            tooltip.add(I18n.format("Mobs cannot spawn on this block"));
        }
        if (this == GTCXBlocks.thermalBoiler || this == GTCXBlocks.largeGasTurbine || this == GTCXBlocks.largeSteamTurbine || this == GTCXBlocks.fusionComputer){
            if (GuiScreen.isShiftKeyDown()){
                tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock0"));
                tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock1"));
                tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock2"));
                tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock3"));
                if (this != GTCXBlocks.thermalBoiler){
                    tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock4"));
                }
                if (this == GTCXBlocks.fusionComputer){
                    tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + "multiblock5"));
                }
            } else {
                tooltip.add(GTCXLang.PRESS_SHIFT.getLocalized());
            }
        }
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (this == GTCXBlocks.electrolyzer) {
            return new GTCXTileElectrolyzer();
        }
        if (this == GTCXBlocks.alloySmelter){
            return new GTCXTileAlloySmelter();
        }
        if (this == GTCXBlocks.assemblingMachine){
            return new GTCXTileAssemblingMachine();
        }
        if (this == GTCXBlocks.chemicalReactor){
            return new GTCXTileChemicalReactor();
        }
        if (this == GTCXBlocks.industrialGrinder){
            return new GTCXTileMultiIndustrialGrinder();
        }
        if (this == GTCXBlocks.implosionCompressor){
            return new GTCXTileMultiImplosionCompressor();
        }
        if (this == GTCXBlocks.vacuumFreezer){
            return new GTCXTileMultiVacuumFreezer();
        }
        if (this == GTCXBlocks.industrialBlastFurnace){
            return new GTCXTileMultiIndustrialBlastFurnace();
        }
        if (this == GTCXBlocks.distillationTower){
            return new GTCXTileMultiDistillationTower();
        }
        if (this == GTCXBlocks.alloyFurnace){
            return new GTCXTileAlloyFurnace();
        }
        if (this == GTCXBlocks.primitiveBlastFurnace){
            return new GTCXTileMultiPrimitiveBlastFurnace();
        }
        if (this == GTCXBlocks.fluidCaster){
            return new GTCXTileFluidCaster();
        }
        if (this == GTCXBlocks.fluidSmelter){
            return new GTCXTileFluidSmelter();
        }
        if (this == GTCXBlocks.plateBender){
            return new GTCXTilePlateBender();
        }
        if (this == GTCXBlocks.lathe){
            return new GTCXTileLathe();
        }
        if (this == GTCXBlocks.wiremill){
            return new GTCXTileWiremill();
        }
        if (this == GTCXBlocks.microwave){
            return new GTCXTileMicrowave();
        }
        if (this == GTCXBlocks.dieselGenerator){
            return new GTCXTileDieselGenerator();
        }
        if (this == GTCXBlocks.gasTurbine){
            return new GTCXTileGasTurbine();
        }
        if (this == GTCXBlocks.centrifuge){
            return new GTCXTileCentrifuge();
        }
        if (this == GTCXBlocks.stoneCompressor){
            return new GTCXTileStoneCompressor();
        }
        if (this == GTCXBlocks.steamCompressor){
            return new GTCXTileSteamCompressor();
        }
        if (this == GTCXBlocks.inputHatch){
            return new GTCXTileItemFluidHatches.GTCXTileInputHatch();
        }
        if (this == GTCXBlocks.outputHatch){
            return new GTCXTileItemFluidHatches.GTCXTileOutputHatch();
        }
        if (this == GTCXBlocks.dynamoHatch){
            return new GTCXTileEnergyOutputHatch.GTCXTileDynamoHatch();
        }
        if (this == GTCXBlocks.machineControlHatch){
            return new GTCXTileMachineControlHatch();
        }
        if (this == GTCXBlocks.fusionMaterialInjector){
            return new GTCXTileItemFluidHatches.GTCXTileFusionMaterialInjector();
        }
        if (this == GTCXBlocks.fusionMaterialExtractor){
            return new GTCXTileItemFluidHatches.GTCXTileFusionMaterialExtractor();
        }
        if (this == GTCXBlocks.fusionEnergyInjector){
            return new GTCXTileFusionEnergyInjector();
        }
        if (this == GTCXBlocks.fusionEnergyExtractor){
            return new GTCXTileEnergyOutputHatch.GTCXTileFusionEnergyExtractor();
        }
        if (this == GTCXBlocks.thermalBoiler){
            return new GTCXTileMultiThermalBoiler();
        }
        if (this == GTCXBlocks.largeSteamTurbine){
            return new GTCXTileMultiLargeSteamTurbine();
        }
        if (this == GTCXBlocks.largeGasTurbine){
            return new GTCXTileMultiLargeGasTurbine();
        }
        if (this == GTCXBlocks.plateCutter){
            return new GTCXTilePlateCutter();
        }
        if (this == GTCXBlocks.fusionComputer){
            return new GTCXTileMultiFusionReactor();
        }
        if (this == GTCXBlocks.extruder){
            return new GTCXTileExtruder();
        }
        if (this == GTCXBlocks.industrialSawmill){
            return new GTCXTileMultiIndustrialSawmill();
        }
        if (this == GTCXBlocks.stoneExtractor){
            return new GTCXTileStoneExtractor();
        }
        if (this == GTCXBlocks.bath){
            return new GTCXTileBath();
        }
        if (this == GTCXBlocks.digitalTank){
            return new GTCXTileDigitalTank();
        }
        if (this == GTCXBlocks.cokeOven){
            return new GTCXTileMultiCokeOven();
        }
        if (this == GTCXBlocks.fireBrickBlock){
            return new GTCXTileBrick();
        }
        if (this == GTCXBlocks.coalBoiler){
            return new GTCXTileCoalBoiler();
        }
        if (this == GTCXBlocks.steamMacerator){
            return new GTCXTileSteamMacerator();
        }
        if (this == GTCXBlocks.steamExtractor){
            return new GTCXTileSteamExtractor();
        }
        if (this == GTCXBlocks.steamFurnace){
            return new GTCXTileSteamFurnace();
        }
        if (this == GTCXBlocks.steamForgeHammer){
            return new GTCXTileSteamForgeHammer();
        }
        if (this == GTCXBlocks.steamAlloySmelter){
            return new GTCXTileSteamAlloySmelter();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite[] getIconSheet(int meta) {
        return GTCXIcons.getTextureData(this);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList<>();
        TileEntity te = this.getLocalTile() == null ? world.getTileEntity(pos) : this.getLocalTile();
        if (this == GTCXBlocks.primitiveBlastFurnace || this == GTCXBlocks.alloyFurnace || this == GTCXBlocks.cokeOven){
            if (te instanceof IGTItemContainerTile){
                list.addAll(((IGTItemContainerTile) te).getDrops());
            }
            return list;
        } else if (this == GTCXBlocks.fireBrickBlock){
            list.add(GTMaterialGen.get(this));
            return list;
        } else {
            return super.getDrops(world, pos, state, fortune);
        }
    }

    public boolean hasVertical() {
        return this == GTCXBlocks.inputHatch || this == GTCXBlocks.outputHatch || this == GTCXBlocks.dynamoHatch || this == GTCXBlocks.machineControlHatch;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, pos, state, player);
        TileEntity tile = world.getTileEntity(pos);
        if (this == GTCXBlocks.fusionComputer && tile instanceof GTCXTileMultiFusionReactor){
            GTCXTileMultiFusionReactor reactor = (GTCXTileMultiFusionReactor) tile;
            reactor.removeTilesWithOwners();
        }
        if (this == GTCXBlocks.fusionEnergyInjector && tile instanceof GTCXTileFusionEnergyInjector){
            GTCXTileFusionEnergyInjector injector = (GTCXTileFusionEnergyInjector) tile;
            injector.onBlockRemoved();
        }
        if (this == GTCXBlocks.thermalBoiler && tile instanceof GTCXTileMultiThermalBoiler){
            ((GTCXTileMultiThermalBoiler)tile).onBlockBreak();
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (this == GTCXBlocks.largeSteamTurbine && tile instanceof GTCXTileMultiLargeSteamTurbine){
            ((GTCXTileMultiLargeSteamTurbine)tile).onBlockPlaced();
        }
        if (this == GTCXBlocks.largeGasTurbine && tile instanceof GTCXTileMultiLargeGasTurbine){
            ((GTCXTileMultiLargeGasTurbine)tile).onBlockPlaced();
        }
        if (this.hasVertical() && !IC2.platform.isRendering()) {
            if (tile instanceof TileEntityBlock) {
                TileEntityBlock block = (TileEntityBlock) tile;
                int pitch = Math.round(placer.rotationPitch);
                if (pitch >= 65) {
                    block.setFacing(EnumFacing.UP);
                } else if (pitch <= -65) {
                    block.setFacing(EnumFacing.DOWN);
                } else {
                    block.setFacing(EnumFacing.fromAngle(placer.rotationYaw).getOpposite());
                }
            }
        }
    }

    @Override
    public void addReaderInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag iTooltipFlag) {
        if (this.compare(stack, GTCXBlocks.alloySmelter) || this.compare(stack, GTCXBlocks.implosionCompressor) || this.compare(stack, GTCXBlocks.assemblingMachine) || this.compare(stack, GTCXBlocks.chemicalReactor) || this.compare(stack, GTCXBlocks.lathe) || this.compare(stack, GTCXBlocks.microwave) || this.compare(stack, GTCXBlocks.plateBender) || this.compare(stack, GTCXBlocks.plateCutter) || this.compare(stack, GTCXBlocks.wiremill) || this.compare(stack, GTCXBlocks.centrifuge)) {
            tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(32));
        }

        if (this.compare(stack, GTCXBlocks.electrolyzer) || this.compare(stack, GTCXBlocks.vacuumFreezer) || this.compare(stack, GTCXBlocks.industrialGrinder) || this.compare(stack, GTCXBlocks.industrialSawmill) || this.compare(stack, GTCXBlocks.extruder) || this.compare(stack, GTCXBlocks.industrialBlastFurnace) || this.compare(stack, GTCXBlocks.fluidCaster) || this.compare(stack, GTCXBlocks.fluidSmelter) || this.compare(stack, GTCXBlocks.distillationTower) || this.compare(stack, GTCXBlocks.advancedWorktable)) {
            tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(128));
        }
        if (this.compare(stack, GTCXBlocks.electricLocker)) {
            tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(512));
        }
        if (this.compare(stack, GTCXBlocks.gasTurbine) || this.compare(stack, GTCXBlocks.dieselGenerator)){
            tooltip.add(Ic2InfoLang.electricProduction.getLocalizedFormatted(Ic2InfoLang.electricTransferRateVariable.getLocalized()));
        }
        if (this.compare(stack, GTCXBlocks.fusionComputer)){
            tooltip.add(Ic2InfoLang.euReaderSinkInfo.getLocalizedFormatted(8192));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this == GTCXBlocks.fireBrickBlock){
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof GTCXTileBrick) {
                GTCXTileBrick brick = (GTCXTileBrick)te;
                if (brick.getOwner() instanceof IClickable){
                    IClickable click = (IClickable) brick.getOwner();
                    if (click.hasRightClick() && click.onRightClick(playerIn, hand, facing, FMLCommonHandler.instance().getEffectiveSide())) {
                        return true;
                    }
                }
            }
            if (playerIn.isSneaking()) {
                return false;
            } else {
                return te instanceof GTCXTileBrick && ((GTCXTileBrick)te).getOwner() != null && (IC2.platform.isRendering() || IC2.platform.launchGui(playerIn, ((GTCXTileBrick)te).getOwner(), hand));
            }
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        if (this == GTCXBlocks.fireBrickBlock){
            return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[1];
        }
        return super.getTextureFromState(iBlockState, enumFacing);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        if (this == GTCXBlocks.fireBrickBlock){
            return this.getTextureFromState(state, EnumFacing.SOUTH);
        }
        return super.getParticleTexture(state);
    }

    public boolean compare(ItemStack stack, Block block) {
        return StackUtil.isStackEqual(stack, new ItemStack(block));
    }

    @Override
    public boolean hasFacing() {
        return super.hasFacing() && this != GTCXBlocks.fireBrickBlock;
    }
}
