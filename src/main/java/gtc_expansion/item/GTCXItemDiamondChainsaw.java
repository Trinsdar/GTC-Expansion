package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtclassic.GTMod;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.item.base.ItemElectricTool;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase entity,
                                            EnumHand hand) {
        if (entity.world.isRemote) {
            return false;
        } else if (!(entity instanceof IShearable)) {
            return false;
        } else {
            IShearable target = (IShearable) entity;
            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (target.isShearable(stack, entity.world, pos)
                    && ElectricItem.manager.canUse(stack, this.getEnergyCost(stack) * 2)) {
                List<ItemStack> drops = target.onSheared(stack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
                EntityItem ent;
                for (Iterator<ItemStack> var8 = drops.iterator(); var8.hasNext(); ent.motionZ += (entity.world.rand.nextFloat()
                        - entity.world.rand.nextFloat()) * 0.1F) {
                    ItemStack item = (ItemStack) var8.next();
                    ent = entity.entityDropItem(item, 1.0F);
                    ent.motionY += entity.world.rand.nextFloat() * 0.05F;
                    ent.motionX += (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.1F;
                }
                ElectricItem.manager.use(stack, this.getEnergyCost(stack) * 2, playerIn);
            }
            return true;
        }
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
        }
        if (!player.world.isRemote && !player.capabilities.isCreativeMode) {
            Block block = player.world.getBlockState(pos).getBlock();
            if (block instanceof IShearable) {
                IShearable target = (IShearable) block;
                if (target.isShearable(itemstack, player.world, pos)
                        && ElectricItem.manager.canUse(itemstack, this.getEnergyCost(itemstack))) {
                    List<ItemStack> drops = target.onSheared(itemstack, player.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
                    Iterator<ItemStack> var7 = drops.iterator();
                    while (var7.hasNext()) {
                        ItemStack stack = (ItemStack) var7.next();
                        float f = 0.7F;
                        double d = player.world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d1 = player.world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        double d2 = player.world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        EntityItem entityitem = new EntityItem(player.world, pos.getX() + d, pos.getY() + d1, pos.getZ()
                                + d2, stack);
                        entityitem.setDefaultPickupDelay();
                        player.world.spawnEntity(entityitem);
                    }
                    ElectricItem.manager.use(itemstack, this.getEnergyCost(itemstack), player);
                    player.addStat(StatList.getBlockStats(block));
                    if (block == Blocks.WEB) {
                        player.world.setBlockToAir(pos);
                        IC2.achievements.issueStat(player, "blocksSawed");
                        return true;
                    }
                }
            }
            return false;
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
    public EnumEnchantmentType getType(ItemStack item) {
        return EnumEnchantmentType.DIGGER;
    }

    @Override
    public boolean isSpecialSupported(ItemStack item, Enchantment ench) {
        return ench instanceof EnchantmentDamage;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return Ic2Items.chainSaw.getItem().hitEntity(stack, target, attacker);
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
