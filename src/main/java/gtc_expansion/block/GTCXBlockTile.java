package gtc_expansion.block;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXIcons;
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
import gtc_expansion.tile.GTCXTileStoneCompressor;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

        if (this == GTCXBlocks.importHatch){
            return new GTCXTileItemFluidHatches.GTCXTileImportHatch();
        }
//        if (this == GEBlocks.fusionReactor){
//            return new GETileMultiFusionReactor();
//        }
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
}
