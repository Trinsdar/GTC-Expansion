package gtc_expansion.block;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.tile.GTCXTileAlloyFurnace;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

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
//        if (this == GEBlocks.fusionReactor){
//            return new GETileMultiFusionReactor();
//        }
        return new TileEntityBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite[] getIconSheet(int meta) {
        return Ic2Icons.getTextures(this.name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        state = this.getActualState(state, world, pos);
        if (this == GTCXBlocks.alloyFurnace && state.getValue(active)){
            float f;
            float f3;
            float f4;
            float f5;
            TileEntity te = world.getTileEntity(pos);
            int facing = te instanceof TileEntityBlock ? ((TileEntityBlock)te).getFacing().getIndex() : 0;
            f = (float)pos.getX() + 0.5F;
            float f2 = (float)pos.getY() + 0.0F + world.rand.nextFloat() * 6.0F / 16.0F;
            f3 = (float)pos.getZ() + 0.5F;
            f4 = 0.52F;
            f5 = world.rand.nextFloat() * 0.6F - 0.3F;
            double x = 0.0D;
            double y = 0.0D;
            double z = 0.0D;
            boolean spawn = false;
            if (facing == 2) {
                x = f + f5;
                y = f2;
                z = f3 - f4;
                spawn = true;
            } else if (facing == 3) {
                x = f + f5;
                y = f2;
                z = f3 + f4;
                spawn = true;
            } else if (facing == 4) {
                x = f - f4;
                y = f2;
                z = f3 + f5;
                spawn = true;
            } else if (facing == 5) {
                x = f + f4;
                y = f2;
                z = f3 + f5;
                spawn = true;
            }

            if (spawn) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
