package gtc_expansion.item.tools;

import com.google.common.collect.Sets;
import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXWrenchUtils;
import gtclassic.GTMod;
import gtclassic.api.interfaces.IGTColorItem;
import gtclassic.api.material.GTMaterial;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ICustomItemCameraTransform;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import mods.railcraft.api.items.IToolCrowbar;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Collections;
import java.util.List;

@Optional.Interface(iface = "mods.railcraft.api.items.IToolCrowbar", modid = "railcraft")
public class GTCXItemToolCrowbar extends ItemTool implements IStaticTexturedItem, IGTColorItem, ILayeredItemModel, ICustomItemCameraTransform, IToolCrowbar {
    GTMaterial material;

    protected GTCXItemToolCrowbar(GTMaterial material, ToolMaterial tmat) {
        super(2.5F, -2.8F, tmat, Sets.newHashSet(Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL));
        this.material = material;
        if (Loader.isModLoaded("railcraft")){
            this.setHarvestLevel("crowbar", 2);
        }
        setRegistryName(this.material.getName() + "_crowbar");
        setUnlocalizedName(GTCExpansion.MODID + "." + this.material.getName() + "_crowbar");
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("tooltip.gtc_expansion.crowbar"));
        if (Loader.isModLoaded("railcraft")){
            tooltip.add(I18n.format("item.railcraft.tool.crowbar.tips").replace("\\n", "\n").replace("\\%", "@"));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(player);
        if (tile instanceof GTCXTileBasePipe && lookingAt != null){
            GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
            EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
            if (sideToggled != null && pipe.anchors.contains(sideToggled)){
                ItemStack stack = pipe.storage.getCoverDrop(sideToggled);
                if (!stack.isEmpty()){
                    pipe.removeCover(sideToggled);
                    if (!player.addItemStackToInventory(stack)){
                        player.dropItem(stack, true);
                    }
                    if (!player.isCreative()) {
                        player.getHeldItem(hand).damageItem(1, player);
                    }
                    player.swingArm(hand);
                    worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
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
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[34 + i];
    }

    @Override
    public List<Integer> getValidVariants() {
        return Collections.singletonList(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_materials")[34 + i];
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public boolean canWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
        return true;
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public void onWhack(EntityPlayer player, EnumHand hand, ItemStack stack, BlockPos blockPos) {
        if (!player.isCreative()) {
            stack.damageItem(1, player);
        }
        player.swingArm(hand);
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public boolean canLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        return entityPlayer.isSneaking();
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public void onLink(EntityPlayer player, EnumHand hand, ItemStack stack, EntityMinecart entityMinecart) {
        if (!player.isCreative()) {
            stack.damageItem(1, player);
        }
        player.swingArm(hand);
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public boolean canBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        return !entityPlayer.isSneaking();
    }

    @Optional.Method(modid = "railcraft")
    @Override
    public void onBoost(EntityPlayer player, EnumHand hand, ItemStack stack, EntityMinecart entityMinecart) {
        if (!player.isCreative()) {
            stack.damageItem(1, player);
        }
        player.swingArm(hand);
    }
}
