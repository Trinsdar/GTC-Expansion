package gtc_expansion.block;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtclassic.GTMod;
import gtclassic.api.block.GTBlockBase;
import gtclassic.api.interfaces.IGTBurnableBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GTCXBlockMisc extends GTBlockBase implements IGTBurnableBlock, ITileEntityProvider {

    String name;
    int id;

    public GTCXBlockMisc(String name, LocaleComp comp, String toolRequired, int id, float hardness, float resistence, int level, Material material, SoundType type) {
        super(material);
        this.name = name;
        this.id = id;
        setRegistryName(this.name);
        setUnlocalizedName(comp);
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

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    /*@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        GTCExpansion.logger.info("Side: " + FMLCommonHandler.instance().getEffectiveSide());
        GTCExpansion.logger.info("Hand "+ hand.name());
        if (hand == EnumHand.OFF_HAND){
            return false;
        }
        try {
            throw new IllegalStateException("Test");
        } catch (Exception e){
            e.printStackTrace();
        }
        if (te instanceof GTCXTileBrick) {
            GTCXTileBrick brick = (GTCXTileBrick)te;
            GTCExpansion.logger.info("Tile brick");
            if (brick.getOwner() instanceof IClickable){
                GTCExpansion.logger.info("Owner clickable");
                IClickable click = (IClickable) brick.getOwner();
                if (click.hasRightClick() && click.onRightClick(playerIn, hand, facing, FMLCommonHandler.instance().getEffectiveSide())) {
                    GTCExpansion.logger.info("Fluid filled");
                    return true;
                }
            }
        }
        if (playerIn.isSneaking()) {
            return false;
        } else {
            GTCExpansion.logger.info("Gui opened");
            return te instanceof GTCXTileBrick && ((GTCXTileBrick)te).getOwner() != null && (IC2.platform.isRendering() || IC2.platform.launchGui(playerIn, ((GTCXTileBrick)te).getOwner(), hand));
        }
    }*/

    @Override
    public int getBlockBurnTime(Block block) {
        return this == GTCXBlocks.coalCokeBlock ? 32000 : 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
