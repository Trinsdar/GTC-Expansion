package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import ic2.api.classic.audio.AudioPosition;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.energy.tile.IInsulationModifieableConductor;
import ic2.api.classic.item.ICutterItem;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ICustomItemCameraTransform;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import ic2.core.util.misc.StackUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXItemInsulationCutter extends Item implements ICutterItem, IStaticTexturedItem, ICustomItemCameraTransform {
    public GTCXItemInsulationCutter() {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(512);
        this.setNoRepair();
        this.setRegistryName(GTCExpansion.MODID, "cutter");
        setUnlocalizedName(GTCExpansion.MODID + ".cutter");
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack){
        ItemStack copy = itemStack.copy();
        return copy.attemptDamageItem(1, itemRand, null) ? ItemStack.EMPTY : copy;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int meta) {
        return Ic2Icons.getTextures("i1")[54];
    }

    @Override
    public boolean hasCustomTransform(int meta) {
        return true;
    }

    @Override
    public ResourceLocation getCustomTransform(int meta) {
        return new ResourceLocation("minecraft:models/item/handheld");
    }

    @Override
    public void cutInsulationFrom(ItemStack stack, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IInsulationModifieableConductor) {
            IInsulationModifieableConductor cable = (IInsulationModifieableConductor)tile;
            if (cable.tryRemoveInsulation()) {
                if (IC2.platform.isSimulating()) {
                    StackUtil.dropAsEntity(world, pos, Ic2Items.rubber.copy());
                    stack.damageItem(3, player);
                } else {
                    IC2.audioManager.playOnce(new AudioPosition(world, pos), PositionSpec.Center, Ic2Sounds.cutterUse, true, IC2.audioManager.getDefaultVolume());
                }
            }
        }

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return Ic2Items.cutter.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
