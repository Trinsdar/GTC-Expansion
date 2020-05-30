package gtc_expansion.item.overrides;

import gtc_expansion.entity.GTCXEntityElectricBoat;
import ic2.core.entity.boat.EntityCarbonBoat;
import ic2.core.entity.boat.EntityRubberBoat;
import ic2.core.item.misc.ItemIC2Boat;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GTCXItemElectricBoat extends ItemIC2Boat {

    public static Entity createBoat(World world, Vec3d vec, int meta) {
        switch(meta) {
            case 0:
                return new EntityCarbonBoat(world, vec.x, vec.y, vec.z);
            case 1:
                return new EntityRubberBoat(world, vec.x, vec.y, vec.z);
            case 2:
            default:
                return null;
            case 3:
                return new GTCXEntityElectricBoat(world, vec.x, vec.y, vec.z);
        }
    }

    @Override
    public ActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        float f = 1.0F;
        float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
        float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
        double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double)f;
        double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double)f + (double)playerIn.getEyeHeight();
        double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double)f;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);
        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            Vec3d vec3d2 = playerIn.getLook(f);
            boolean flag = false;
            List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d2.x * d3, vec3d2.y * d3, vec3d2.z * d3).grow(1.0D));

            for(int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (entity.canBeCollidedWith()) {
                    AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double)entity.getCollisionBorderSize());
                    if (axisalignedbb.contains(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            } else {
                Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
                boolean var10000;
                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                    var10000 = false;
                } else {
                    var10000 = true;
                }

                Entity entityboat = createBoat(worldIn, raytraceresult.hitVec, stack.getMetadata());
                if (entityboat == null) {
                    return new ActionResult<>(EnumActionResult.PASS, stack);
                } else {
                    entityboat.rotationYaw = playerIn.rotationYaw;
                    if (!worldIn.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().grow(-0.1D)).isEmpty()) {
                        return new ActionResult<>(EnumActionResult.FAIL, stack);
                    } else {
                        if (!worldIn.isRemote) {
                            worldIn.spawnEntity(entityboat);
                        }

                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }

                        playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                    }
                }
            }
        }
    }
}
