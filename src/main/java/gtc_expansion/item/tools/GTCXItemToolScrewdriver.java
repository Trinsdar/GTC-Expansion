package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.util.RotationHelper;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.api.item.IBoxable;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class GTCXItemToolScrewdriver extends Item implements IStaticTexturedItem, IGTColorItem, ILayeredItemModel, IBoxable, IGTScrewdriver {

    GTMaterial material;

    public GTCXItemToolScrewdriver(GTMaterial mat, Item.ToolMaterial tmat) {
        this.maxStackSize = 1;
        this.material = mat;
        this.setMaxDamage(tmat.getMaxUses());
        this.setNoRepair();
        setRegistryName(this.material.getName() + "_screwdriver");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_screwdriver");
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        return copy.attemptDamageItem(1, itemRand, null) ? ItemStack.EMPTY : copy;
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int meta) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[32];
    }

    @Override
    public Color getColor(ItemStack stack, int index) {
        if (index == 0) {
            return this.material.equals(GTCXMaterial.TungstenSteel) ? GTCXMaterial.Steel.getColor() : this.material.equals(GTCXMaterial.Steel) ? GTCXMaterial.Iron.getColor() : GTMaterial.Wood.getColor();
        } else {
            return this.material.getColor();
        }
    }

    @Override
    public boolean isLayered(ItemStack var1) {
        return true;
    }

    @Override
    public int getLayers(ItemStack var1) {
        return 2;
    }

    @Override
    public TextureAtlasSprite getTexture(int var1, ItemStack var2) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[32 + var1];
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (RotationHelper.rotateBlock(world, pos, side)){
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        /*TileEntity tile = worldIn.getTileEntity(pos);
        RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(player);
        if (tile instanceof GTCXTileBasePipe && lookingAt != null){
            GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
            EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
            if (sideToggled != null && pipe.anchors.contains(sideToggled)){
                if (pipe.storage.getCoverLogicMap().get(sideToggled).cycleMode(player)){
                    if (!player.isCreative()) {
                        player.getHeldItem(hand).damageItem(1, player);
                    }
                    player.swingArm(hand);
                    IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                }
            }
        }*/
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean canScrewdriverBeUsed(ItemStack stack) {
        return true;
    }

    @Override
    public void damage(ItemStack stack, EntityPlayer player) {
        stack.damageItem(1, player);
    }
}
