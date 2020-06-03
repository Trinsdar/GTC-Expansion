package gtc_expansion.block;

import gtc_expansion.GTCXIcons;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.tile.GTCXTileAlloyFurnace;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileDieselGenerator;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileGasTurbine;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.tile.GTCXTilePlateCutter;
import gtc_expansion.tile.GTCXTileStoneCompressor;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileFusionEnergyInjector;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiLargeGasTurbine;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiThermalBoiler;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GTCXBlockTile extends GTBlockBaseMachine {
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
        this.setHardness(4.0F);
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
        if (this == GTCXBlocks.stoneCompressor){
            return new GTCXTileStoneCompressor();
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
        if (this == GTCXBlocks.fusionReactor){
            return new GTCXTileMultiFusionReactor();
        }
        return new TileEntityBlock();
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
        if (this == GTCXBlocks.primitiveBlastFurnace || this == GTCXBlocks.alloyFurnace){
            if (te instanceof IGTItemContainerTile){
                list.addAll(((IGTItemContainerTile) te).getDrops());
            }
            return list;
        } else {
            return super.getDrops(world, pos, state, fortune);
        }
    }

    public boolean hasVertical() {
        return this == GTCXBlocks.inputHatch || this == GTCXBlocks.outputHatch || this == GTCXBlocks.dynamoHatch;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, pos, state, player);
        TileEntity tile = world.getTileEntity(pos);
        if (this == GTCXBlocks.largeSteamTurbine && tile instanceof GTCXTileMultiLargeSteamTurbine){
            GTCXTileMultiLargeSteamTurbine turbine = (GTCXTileMultiLargeSteamTurbine) tile;
            turbine.onBlockRemoved();
        }
        if (this == GTCXBlocks.largeGasTurbine && tile instanceof GTCXTileMultiLargeGasTurbine){
            GTCXTileMultiLargeGasTurbine turbine = (GTCXTileMultiLargeGasTurbine) tile;
            turbine.onBlockRemoved();
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
                    block.setFacing(EnumFacing.fromAngle((double) placer.rotationYaw).getOpposite());
                }
            }
        }
    }
}
