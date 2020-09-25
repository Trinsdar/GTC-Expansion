package gtc_expansion.util;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.audio.PositionSpec;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GTCXWrenchUtils {
    public static boolean arePosEqual(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() & pos1.getY() == pos2.getY() & pos1.getZ() == pos2.getZ();
    }

    @Nullable
    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, World world, BlockPos ignore)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && !arePosEqual(ignore, blockpos))
                {
                    return iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && !arePosEqual(blockpos, ignore))
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {

                            return iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public static RayTraceResult rayTraceIgnoreBB(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, World world, BlockPos ignore)
    {
        AxisAlignedBB bb = Block.FULL_BLOCK_AABB;
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && !arePosEqual(ignore, blockpos))
                {
                    return collisionRayTrace(blockpos, vec31, vec32, bb);
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && !arePosEqual(blockpos, ignore))
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            return collisionRayTrace(blockpos, vec31, vec32, bb);
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static RayTraceResult getBlockLookingAtIgnoreBB(EntityPlayer liv) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = rayTraceIgnoreBB(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true, liv.world, new BlockPos(0, -1, 0));
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    public static RayTraceResult getBlockLookingat1(EntityPlayer liv, BlockPos exclude) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = liv.world.rayTraceBlocks(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true);
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    public static RayTraceResult getBlockLookingat2(EntityPlayer liv, BlockPos exclude) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = rayTraceBlocks(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true, liv.world, exclude);
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    public static Vec3d posToVec(BlockPos pos) {return new Vec3d(pos.getX(), pos.getY(), pos.getZ());}

    public static Vec3d getVecHitFromPos(BlockPos pos, EnumFacing direction) {
        return ((posToVec(pos.offset(direction, 1)).subtract(posToVec(pos))).scale(0.3)).add((posToVec(pos)).addVector(0.5, 0.5, 0.5));
    }

    @Nullable
    protected static RayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
    {
        Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }

    public static EnumFacing getDirection(EnumFacing overlaySide, Vec3d vec) {
        double x = vec.x - Math.floor(vec.x);
        double y = vec.y - Math.floor(vec.y);
        double z = vec.z - Math.floor(vec.z);

        switch(overlaySide) {
            case DOWN:
                if (x >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && z >= 0.75) return EnumFacing.SOUTH;
                if (0.25 <= x && x <= 0.75 && z <= 0.25) return EnumFacing.NORTH;
                if (0.25 < x && x < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.DOWN;
                return EnumFacing.UP;
            case UP:
                if (x >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && z >= 0.75) return EnumFacing.SOUTH;
                if (0.25 <= x && x <= 0.75 && z <= 0.25) return EnumFacing.NORTH;
                if (0.25 < x && x < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.UP;
                return EnumFacing.DOWN;
            case NORTH:
                if (x >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && y >= 0.75) return EnumFacing.UP;
                if (0.25 <= x && x <= 0.75 && y <= 0.25) return EnumFacing.DOWN;
                if (0.25 < x && x < 0.75 && 0.25 < y && z < 0.75) return EnumFacing.NORTH;
                return EnumFacing.SOUTH;
            case SOUTH:
                if (x >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && y >= 0.75) return EnumFacing.UP;
                if (0.25 <= x && x <= 0.75 && y <= 0.25) return EnumFacing.DOWN;
                if (0.25 < x && x < 0.75 && 0.25 < y && z < 0.75) return EnumFacing.SOUTH;
                return EnumFacing.NORTH;
            case WEST:
                if (z <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.NORTH;
                if (z >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.SOUTH;
                if (y >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.UP;
                if (y <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.DOWN;
                if (0.25 < y && y < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.WEST;
                return EnumFacing.EAST;
            case EAST:
                if (z <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.NORTH;
                if (z >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.SOUTH;
                if (y >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.UP;
                if (y <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.DOWN;
                if (0.25 < y && y < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.EAST;
                return EnumFacing.WEST;
        }
        return null;
    }

    public static int[] toIntArr(ArrayList<EnumFacing> connections) {
        int[] arr = new int[6];
        for (EnumFacing e : EnumFacing.VALUES) {
            if (connections.contains(e)) arr[e.getIndex()] = 1;
            else arr[e.getIndex()] = 0;
        }
        return arr;
    }

    public static ArrayList<EnumFacing> toArrayList(int[] arr) {
        ArrayList<EnumFacing> arrayList = new ArrayList<>();
        for (EnumFacing e : EnumFacing.VALUES) {
            if (arr[e.getIndex()] == 1) arrayList.add(e);
        }
        return arrayList;
    }

    public static ArrayList<EnumFacing> fromGTCEBitmask(int mask) {
        ArrayList<EnumFacing> list = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            int current = (int) Math.pow(2, facing.getIndex());
            if ((mask & current) == current) list.add(facing);
        }
        return list;
    }

    public static EnumFacing fromIndex(int index) {
        switch(index) {
            case 0:
                return EnumFacing.DOWN;
            case 1:
                return EnumFacing.UP;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.SOUTH;
            case 4:
                return EnumFacing.WEST;
            case 5:
                return EnumFacing.EAST;
        }
        return null;
    }

    public static boolean wrenchUse(RayTraceResult lookingAt, GTCXTileBasePipe pipe, EntityPlayer player, World worldIn, boolean offhand) {
        BlockPos pos = lookingAt.getBlockPos();
        boolean setConnection = false;
        if (!player.isSneaking()) {
            EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
            EnumFacing opposite = sideToggled.getOpposite();
            BlockPos offset = pos.offset(sideToggled);
            TileEntity offsetTile = worldIn.getTileEntity(offset);
            if (pipe.connection.contains(sideToggled)) {
                pipe.removeConnection(sideToggled);
                if (offsetTile instanceof GTCXTileBasePipe && ((GTCXTileBasePipe)offsetTile).connection.contains(opposite)){
                    ((GTCXTileBasePipe)offsetTile).removeConnection(opposite);
                }
                setConnection = true;
            } else {
                if (worldIn.isAirBlock(offset) || pipe.canConnect(offsetTile, sideToggled)){
                    pipe.addConnection(sideToggled);
                    if (offsetTile instanceof GTCXTileBasePipe && ((GTCXTileBasePipe)offsetTile).connection.notContains(opposite)){
                        ((GTCXTileBasePipe)offsetTile).addConnection(opposite);
                    }
                    setConnection = true;
                }

            }
            if (setConnection){
                IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                player.swingArm(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            }
            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 3);
            pipe.markDirty();

        }
        return setConnection;
    }
}
