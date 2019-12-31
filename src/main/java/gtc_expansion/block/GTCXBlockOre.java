package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBaseOre;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GTCXBlockOre extends GTBlockBaseOre {

    String name;

    /*
     For ores using a color different then the material.
     */
    public GTCXBlockOre(GTMaterial mat,  Color color, float hardness, int level) {
        super(color, getSetFromFlags(mat), getBackgroundSetFromFlags(mat));
        this.name = mat.getName();
        setRegistryName(this.name + "_ore");
        setUnlocalizedName(GTCExpansion.MODID + ".ore" + this.name);
        setCreativeTab(GTMod.creativeTabGT);
        setHardness(hardness);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", level);
        setSoundType(SoundType.STONE);
    }

    public GTCXBlockOre(GTMaterial mat, float hardness, int level) {
        this(mat, mat.getColor(), hardness, level);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format(this.getUnlocalizedName().replace("tile", "tooltip")));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(IBlockState iBlockState) {
        return FULL_BLOCK_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getParticleTexture(IBlockState state) {
        return this.getTextureFromState(state, EnumFacing.SOUTH);
    }

    @Override
    public List<IBlockState> getValidStates() {
        return this.blockState.getValidStates();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromStack(ItemStack stack) {
        return this.getStateFromMeta(stack.getMetadata());
    }

    @Override
    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockstate, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        Random random = world instanceof World ? ((World) world).rand : new Random();
        if (this.equals(GTCXBlocks.orePyrite)){
            drops.add(GTMaterialGen.getDust(GTMaterial.Pyrite, 2 + random.nextInt(1 + fortune)));
        } else if (this.equals(GTCXBlocks.oreCinnabar)) {
            drops.add(GTMaterialGen.getDust(GTCXMaterial.Cinnabar, 2 + random.nextInt(1 + fortune)));
            if (random.nextInt(Math.max(1,  4/(fortune + 1))) == 0){
                drops.add(new ItemStack(Items.REDSTONE));
            }
        } else if (this.equals(GTCXBlocks.oreSphalerite)){
            drops.add(GTMaterialGen.getDust(GTCXMaterial.Sphalerite, 2 + random.nextInt(1 + fortune)));
            if (random.nextInt(Math.max(1,  4/(fortune + 1))) == 0){
                drops.add(GTMaterialGen.getDust(GTCXMaterial.Zinc, 1));
            }
            if (random.nextInt(Math.max(1, 32/(fortune+1))) == 0){
                drops.add(GTMaterialGen.getGem(GTCXMaterial.GarnetYellow, 1));
            }
        } else if (this.equals(GTCXBlocks.oreOlivine)) {
            drops.add(GTMaterialGen.getGem(GTCXMaterial.Olivine, 1 + random.nextInt(1 + fortune)));
        } else if (this.equals(GTCXBlocks.oreSodalite)){
            drops.add(GTMaterialGen.getDust(GTMaterial.Sodalite, 6 + 3 * random.nextInt(1 + fortune)));
            if (random.nextInt(Math.max(1,  4/(fortune + 1))) == 0){
                drops.add(GTMaterialGen.getDust(GTMaterial.Aluminium, 1));
            }
        } else {
            drops.add(new ItemStack(this));
        }
        return drops;
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int xp = 0;
        if (this.equals(GTCXBlocks.oreOlivine)) {
            xp = MathHelper.getInt(rand, 2, 5);
        }
        if (this.equals(GTCXBlocks.orePyrite) || this.equals(GTCXBlocks.oreSphalerite) ||this.equals(GTCXBlocks.oreSodalite) || this.equals(GTCXBlocks.oreCinnabar)) {
            xp = MathHelper.getInt(rand, 3, 7);
        }
        return xp;
    }

    private static TextureSet getSetFromFlags(GTMaterial mat) {
        if (GTMaterial.isGem(mat) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.OlivineOverworld)) {
            return TextureSet.GEM;
        }
        if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Cassiterite)){
            return TextureSet.LAPIS;
        }
        if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Tetrahedrite) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Galena)){
            return TextureSet.METAL;
        }
        return !mat.hasFlag(GTMaterialFlag.INGOT) && !mat.hasFlag(GTMaterialFlag.NULL) ? TextureSet.LAPIS : TextureSet.METAL;
    }

    private static BackgroundSet getBackgroundSetFromFlags(GTMaterial mat){
        if (GTMaterialGen.isMaterialEqual(mat, GTMaterial.Pyrite) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Cinnabar) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Sphalerite)){
            return BackgroundSet.NETHERRACK;
        }
        if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Olivine) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Tungstate) || GTMaterialGen.isMaterialEqual(mat, GTMaterial.Sheldonite) || GTMaterialGen.isMaterialEqual(mat, GTMaterial.Sodalite)){
            return BackgroundSet.ENDSTONE;
        }
        return BackgroundSet.STONE;
    }
}
