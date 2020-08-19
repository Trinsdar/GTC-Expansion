package gtc_expansion.item.tools;

import com.google.common.collect.Sets;
import forestry.api.arboriculture.IToolGrafter;
import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ICustomItemCameraTransform;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXItemToolBranchCutter extends ItemTool implements IStaticTexturedItem, IGTColorItem, ILayeredItemModel, ICustomItemCameraTransform, IToolGrafter {
    GTMaterial material;

    protected GTCXItemToolBranchCutter(GTMaterial material, ToolMaterial tmat) {
        super(2.5F, -2.8F, tmat, Sets.newHashSet(Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL));
        this.material = material;
        if (Loader.isModLoaded("forestry")){
            this.setHarvestLevel("grafter", 3);
        }
        setRegistryName(this.material.getName() + "_branch_cutter");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_branch_cutter");
        setCreativeTab(GTMod.creativeTabGT);
    }

    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Block block = state.getBlock();
        return block instanceof BlockLeaves || state.getMaterial() == Material.LEAVES || super.canHarvestBlock(state, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!stack.isItemDamaged()) {
            tooltip.add(I18n.format("item.for.uses", new Object[]{stack.getMaxDamage() + 1}));
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    @Override
    public Color getColor(ItemStack itemStack, int i) {
        return  i == 1 ? material.getColor() : Color.WHITE;
    }

    @Override
    public boolean hasCustomTransform(int i) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ResourceLocation getCustomTransform(int i) {
        return new ResourceLocation("minecraft:models/item/handheld");
    }

    @Override
    public boolean isLayered(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getLayers(ItemStack itemStack) {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTexture(int i, ItemStack itemStack) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[38 + i];
    }

    @Override
    public List<Integer> getValidVariants() {
        return Collections.singletonList(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[38 + i];
    }

    @Override
    public float getSaplingModifier(ItemStack itemStack, World world, EntityPlayer entityPlayer, BlockPos blockPos) {
        return 100f;
    }
}
