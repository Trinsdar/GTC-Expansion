package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXLang;
import gtclassic.GTMod;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.item.base.ItemElectricTool;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Lang;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.ToolTipType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GTCXItemDiamondChainsaw extends ItemElectricTool
        implements IStaticTexturedItem {

    public static final ItemStack diamondAxe;

    public GTCXItemDiamondChainsaw() {
        super(0.0F, 0.0F, ToolMaterial.IRON);
        this.tier = 1;
        this.maxCharge = 10000;
        this.transferLimit = 100;
        this.operationEnergyCost = 50;
        this.efficiency = 18.0F;
        this.setHarvestLevel("axe", 3);
        this.setRegistryName("diamond_chainsaw");
        this.setUnlocalizedName(GTCExpansion.MODID + "." + "diamond_chainsaw");
        this.setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return diamondAxe.canHarvestBlock(state) || state.getBlock() == Blocks.WEB && ElectricItem.manager.canUse(stack, this.getEnergyCost(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Color.CYAN.hashCode();
    }

    @Override
    public void onSortedItemToolTip(ItemStack stack, EntityPlayer player, boolean debugTooltip, List<String> tooltip, Map<ToolTipType, List<String>> sortedTooltip) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        if (!tag.getBoolean("noShear")) {
            tooltip.add(GTCXLang.MESSAGE_DIAMOND_CHAINSAW_NORMAL.getLocalized());
        } else {
            tooltip.add(GTCXLang.MESSAGE_DIAMOND_CHAINSAW_NO_SHEAR.getLocalized());
        }
        List<String> ctrlTip = sortedTooltip.get(ToolTipType.Ctrl);
        ctrlTip.add(Ic2Lang.onItemRightClick.getLocalized());
        ctrlTip.add(Ic2Lang.pressTo.getLocalizedFormatted(IC2.keyboard.getKeyName(2), GTCXLang.DIAMOND_CHAINSAW_SHEAR_TOGGLE.getLocalized()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        if (!ElectricItem.manager.canUse(stack, this.getEnergyCost(stack))) {
            return 1.0F;
        } else {
            if (material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
                    && material != Material.LEAVES){
                return  super.getDestroySpeed(stack, state);
            }
            if (tag.getInteger("logCount") > 0){
                return Math.max(0.25F, 24.0F / tag.getInteger("logCount"));
            }
            return this.efficiency;
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase entity, EnumHand hand) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        if (!tag.getBoolean("noShear")) {
            return Ic2Items.chainSaw.getItem().itemInteractionForEntity(stack, playerIn, entity, hand);
        }
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        World worldIn = player.world;
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(itemstack);
        if (!player.isSneaking()) {
            Set<BlockPos> positions = getTargetBlocks(worldIn, pos, player);
            if (!positions.isEmpty()){

                for (BlockPos pos2 : positions) {
                    breakBlock(pos2, itemstack, worldIn, pos, player);
                }
                tag.setInteger("logCount", 0);
            }
            tag.setInteger("logCount", 0);
        }
        tag.setInteger("logCount", 0);
        if (!tag.getBoolean("noShear")) {
            return Ic2Items.chainSaw.getItem().onBlockStartBreak(itemstack, pos, player);
        } else {
            return false;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
                                    EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            IC2.achievements.issueStat((EntityPlayer) entityLiving, "blocksSawed");
        }
        return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
    }

    @SuppressWarnings("deprecation")
    public void breakBlock(BlockPos pos, ItemStack saw, World world, BlockPos oldPos, EntityPlayer player) {
        if (oldPos == pos) {
            return;
        }
        if (!ElectricItem.manager.canUse(saw, this.getEnergyCost(saw))) {
            return;
        }
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlockHardness(world, pos) == -1.0F) {
            return;
        }
        ElectricItem.manager.use(saw, this.getEnergyCost(saw), player);
        blockState.getBlock().harvestBlock(world, player, pos, blockState, world.getTileEntity(pos), saw);
        world.setBlockToAir(pos);
        world.removeTileEntity(pos);
        world.neighborChanged(pos, world.getBlockState(pos).getBlock(), pos);
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    public Set<BlockPos> getTargetBlocks(World worldIn, BlockPos pos, @Nullable EntityPlayer playerIn) {
        Set<BlockPos> targetBlocks = new HashSet<BlockPos>();
        if (playerIn == null) {
            return new HashSet<BlockPos>();
        }
        RayTraceResult raytrace = rayTrace(worldIn, playerIn, false);
        if(raytrace == null || raytrace.sideHit == null){
            return Collections.emptySet();
        }
        EnumFacing enumfacing = raytrace.sideHit;
        if (enumfacing == EnumFacing.DOWN){
            return Collections.emptySet();
        }

        if (worldIn.getBlockState(pos).getBlock().isWood(worldIn, pos)){
            for (int i = 1; i < 80; i++) {
                BlockPos nextPos = pos.up(i);
                IBlockState nextState = worldIn.getBlockState(nextPos);
                if (nextState.getBlock().isWood(worldIn, nextPos)) {
                    targetBlocks.add(nextPos);
                } else {
                    int leaves = 0;
                    if (nextState.getBlock().isLeaves(nextState, worldIn, nextPos)){
                        leaves++;
                    }
                    nextPos = pos.add(1, i - 1, 0);
                    nextState = worldIn.getBlockState(nextPos);
                    if (nextState.getBlock().isLeaves(nextState, worldIn, nextPos)){
                        leaves++;
                    }
                    nextPos = pos.add(0, i - 1, 1);
                    nextState = worldIn.getBlockState(nextPos);
                    if (nextState.getBlock().isLeaves(nextState, worldIn, nextPos)){
                        leaves++;
                    }
                    nextPos = pos.add(-1, i - 1, 0);
                    nextState = worldIn.getBlockState(nextPos);
                    if (nextState.getBlock().isLeaves(nextState, worldIn, nextPos)){
                        leaves++;
                    }
                    nextPos = pos.add(0, i - 1, -1);
                    nextState = worldIn.getBlockState(nextPos);
                    if (nextState.getBlock().isLeaves(nextState, worldIn, nextPos)){
                        leaves++;
                    }
                    if (leaves >= 3){
                        break;
                    } else {
                        return Collections.emptySet();
                    }
                }
            }
        }
        return targetBlocks;
    }

    public BlockPos left(EnumFacing facing, BlockPos old){
        if (facing == EnumFacing.NORTH){
            return old.add( 1, 0, 0);
        }
        if (facing == EnumFacing.WEST){
            return old.add( 0, 0, -1);
        }
        if (facing == EnumFacing.SOUTH){
            return old.add( -1, 0, 0);
        }
        if (facing == EnumFacing.EAST){
            return old.add( 0, 0, 1);
        }
        return old;
    }

    public BlockPos right(EnumFacing facing, BlockPos old){
        if (facing == EnumFacing.NORTH){
            return old.add( -1, 0, 0);
        }
        if (facing == EnumFacing.WEST){
            return old.add( 0, 0, 1);
        }
        if (facing == EnumFacing.SOUTH){
            return old.add( 1, 0, 0);
        }
        if (facing == EnumFacing.EAST){
            return old.add( 0, 0, -1);
        }
        return old;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        ItemStack stack = player.getHeldItem(handIn);
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        if (IC2.platform.isSimulating() && IC2.keyboard.isModeSwitchKeyDown(player)) {
            if (tag.getBoolean("noShear")) {
                tag.setBoolean("noShear", false);
                IC2.platform.messagePlayer(player, TextFormatting.GREEN, GTCXLang.MESSAGE_DIAMOND_CHAINSAW_NORMAL);
            } else {
                tag.setBoolean("noShear", true);
                IC2.platform.messagePlayer(player, TextFormatting.RED, GTCXLang.MESSAGE_DIAMOND_CHAINSAW_NO_SHEAR);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        } else {
            return super.onItemRightClick(worldIn, player, handIn);
        }
    }

    @Override
    public EnumEnchantmentType getType(ItemStack item) {
        return EnumEnchantmentType.DIGGER;
    }

    @Override
    public boolean isSpecialSupported(ItemStack item, Enchantment ench) {
        return ench instanceof EnchantmentDamage;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!(attacker instanceof EntityPlayer)) {
            return true;
        } else {
            if (ElectricItem.manager.use(stack, this.operationEnergyCost, (EntityPlayer)attacker)) {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), 15.0F);
            } else {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), 1.0F);
            }

            if (target.getHealth() <= 0.0F) {
                if (target instanceof EntityCreeper) {
                    IC2.achievements.issueStat((EntityPlayer)attacker, "killCreeperChainsaw");
                }

                IC2.achievements.issueStat((EntityPlayer)attacker, "chainsawKills");
            }

            return false;
        }
    }

    static {
        diamondAxe = new ItemStack(Items.DIAMOND_AXE);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[26];
    }
}
