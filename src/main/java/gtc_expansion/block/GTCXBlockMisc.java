package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBase;
import ic2.core.platform.lang.components.base.LangComponentHolder;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Lang;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GTCXBlockMisc extends GTBlockBase {

    String name;
    int id;
    LocaleComp comp;

    public GTCXBlockMisc(String name, String toolRequired, int id, float hardness, float resistence, int level, Material material, SoundType type) {
        super(material);
        this.name = name;
        this.id = id;
        this.comp = Ic2Lang.nullKey;
        setRegistryName(this.name);
        setUnlocalizedName(GTCExpansion.MODID + "." + this.name);
        setCreativeTab(GTMod.creativeTabGT);
        setHardness(hardness);
        setResistance(resistence);
        setHarvestLevel(toolRequired, level);
        setSoundType(type);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("Mobs cannot spawn on this block"));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(IBlockState iBlockState) {
        return FULL_BLOCK_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTextureFromState(IBlockState iBlockState, EnumFacing enumFacing) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_blocks")[this.id];
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

    public LocaleComp getName() {
        return this.comp;
    }

    public Block setUnlocalizedName(LocaleComp name) {
        this.comp = name;
        return super.setUnlocalizedName(name.getUnlocalized());
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public Block setUnlocalizedName(String name) {
        this.comp = new LangComponentHolder.LocaleBlockComp("tile." + name);
        return super.setUnlocalizedName(name);
    }
}
