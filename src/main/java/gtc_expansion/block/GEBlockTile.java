package gtc_expansion.block;

import gtc_expansion.GEBlocks;
import gtc_expansion.tile.GETileAlloyFurnace;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileAssemblingMachine;
import gtc_expansion.tile.GETileChemicalReactor;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.block.GTBlockMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class GEBlockTile extends GTBlockMachine {
    int size;
    public GEBlockTile(String name, LocaleComp comp) {
        this(name, comp, 0);
    }

    public GEBlockTile(String name, LocaleComp comp, int additionalInfo) {
        super(name, comp, additionalInfo);
        this.size = additionalInfo + 1;
    }

    public GEBlockTile(String name, LocaleComp comp, Material material){
        this(name, comp, material, 0);
    }

    public GEBlockTile(String name, LocaleComp comp, Material material, int additionalInfo){
        super(name, comp, material);
        this.setSoundType(SoundType.STONE);
        this.setHardness(4.0F);
        this.size = additionalInfo + 1;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for(int i = 0; i < this.size; ++i) {
            tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip") + i));
        }

    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (this == GEBlocks.electrolyzer) {
            return new GETileElectrolyzer();
        }
        if (this == GEBlocks.alloySmelter){
            return new GETileAlloySmelter();
        }
        if (this == GEBlocks.assemblingMachine){
            return new GETileAssemblingMachine();
        }
        if (this == GEBlocks.chemicalReactor){
            return new GETileChemicalReactor();
        }
        if (this == GEBlocks.industrialGrinder){
            return new GETileMultiIndustrialGrinder();
        }
        if (this == GEBlocks.implosionCompressor){
            return new GETileMultiImplosionCompressor();
        }
        if (this == GEBlocks.vacuumFreezer){
            return new GETileMultiVacuumFreezer();
        }
        if (this == GEBlocks.industrialBlastFurnace){
            return new GETileMultiIndustrialBlastFurnace();
        }
        if (this == GEBlocks.alloyFurnace){
            return new GETileAlloyFurnace();
        }
        if (this == GEBlocks.primitiveBlastFurnace){
            return new GETileMultiPrimitiveBlastFurnace();
        }
        return new TileEntityBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        state = this.getActualState(state, world, pos);
        if (this == GEBlocks.alloyFurnace && state.getValue(active)){
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
