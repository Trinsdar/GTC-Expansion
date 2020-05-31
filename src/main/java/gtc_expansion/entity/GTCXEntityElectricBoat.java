package gtc_expansion.entity;

import com.google.common.collect.Lists;
import ic2.api.item.ElectricItem;
import ic2.core.entity.boat.EntityElectricBoat;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class GTCXEntityElectricBoat extends EntityElectricBoat {
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[]{EntityDataManager.createKey(GTCXEntityElectricBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(GTCXEntityElectricBoat.class, DataSerializers.BOOLEAN)};
    private float deltaRotation;
    private double waterLevel;
    private float boatGlide;

    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;

    private EntityBoat.Status status;
    private EntityBoat.Status previousStatus;
    private double lastYd;

    public GTCXEntityElectricBoat(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public GTCXEntityElectricBoat(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        for (DataParameter<Boolean> dataparameter : DATA_ID_PADDLE) {
            this.dataManager.register(dataparameter, Boolean.FALSE);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (this.isImmuneToFire && source.isFireDamage()) {
            return false;
        } else if (!this.world.isRemote && !this.isDead) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + damage * 10.0F);
            this.markVelocityChanged();
            boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0F) {

                if (!flag) {
                    this.onPlayerBreaking();
                }

                this.setDead();
            }

            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onUpdate() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        Entity rider = this.getRider();
        if (this.chest.getStackInSlot(0).isEmpty() || ElectricItem.manager.getCharge(this.chest.getStackInSlot(0)) == 0) {
            if (rider instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) rider;
                this.updateInputs(player.moveStrafing > 0, player.moveStrafing < 0, player.moveForward > 0, player.moveForward < 0);
            }
            if (this.canPassengerSteer() && (forwardInputDown || leftInputDown || rightInputDown || backInputDown)) {
                if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                    this.setPaddleState(false, false);
                    this.updateInputs(false, false, false, false);
                }
                this.updateMotion();


                if (this.world.isRemote) {
                    this.controlBoat();
                    this.world.sendPacketToServer(new CPacketSteerBoat(this.getPaddleState(0), this.getPaddleState(1)));
                }

                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.doBlockCollisions();
            } else {
                this.setPaddleState(false, false);
                super.onUpdate();
            }
        } else {
            super.onUpdate();
        }
    }

    public void update() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();
    }

    private EntityBoat.Status getBoatStatus() {
        EntityBoat.Status status = this.getUnderwaterStatus();

        if (status != null) {
            this.waterLevel = this.getEntityBoundingBox().maxY;
            return status;
        } else if (this.checkInWater()) {
            return EntityBoat.Status.IN_WATER;
        } else {
            float f = this.getBoatGlide();

            if (f > 0.0F) {
                this.boatGlide = f;
                return EntityBoat.Status.ON_LAND;
            } else {
                return EntityBoat.Status.IN_AIR;
            }
        }
    }

    @Nullable
    private EntityBoat.Status getUnderwaterStatus() {
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        double d0 = axisAlignedBB.maxY + 0.001D;
        int minX = MathHelper.floor(axisAlignedBB.minX);
        int maxX = MathHelper.ceil(axisAlignedBB.maxX);
        int minY = MathHelper.floor(axisAlignedBB.maxY);
        int maxY = MathHelper.ceil(d0);
        int minZ = MathHelper.floor(axisAlignedBB.minZ);
        int maxZ = MathHelper.ceil(axisAlignedBB.maxZ);
        boolean flag = false;
        BlockPos.PooledMutableBlockPos pooledMutableBlockPos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        pooledMutableBlockPos.setPos(x, y, z);
                        IBlockState state = this.world.getBlockState(pooledMutableBlockPos);
                        Boolean result = state.getBlock().isAABBInsideMaterial(world, pooledMutableBlockPos, axisAlignedBB, Material.WATER);
                        if (result != null) {
                            if (!result) continue;

                            if (state.getBlock().getBlockLiquidHeight(world, pooledMutableBlockPos, state, Material.WATER) > 0) {
                                pooledMutableBlockPos.release();
                                return EntityBoat.Status.UNDER_FLOWING_WATER;
                            } else
                                continue;
                        }

                        if (state.getMaterial() == Material.WATER && d0 < (double) BlockLiquid.getLiquidHeight(state, this.world, pooledMutableBlockPos)) {
                            if (state.getValue(BlockLiquid.LEVEL) != 0) {
                                return EntityBoat.Status.UNDER_FLOWING_WATER;
                            }

                            flag = true;
                        }
                    }
                }
            }
        } finally {
            pooledMutableBlockPos.release();
        }

        return flag ? EntityBoat.Status.UNDER_WATER : null;
    }

    public float getWaterLevelAbove() {
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        int minX = MathHelper.floor(axisAlignedBB.minX);
        int maxX = MathHelper.ceil(axisAlignedBB.maxX);
        int maxY = MathHelper.floor(axisAlignedBB.maxY);
        int minY = MathHelper.ceil(axisAlignedBB.maxY - this.lastYd);
        int minZ = MathHelper.floor(axisAlignedBB.minZ);
        int maxZ = MathHelper.ceil(axisAlignedBB.maxZ);
        BlockPos.PooledMutableBlockPos pooledMutableBlockPos = BlockPos.PooledMutableBlockPos.retain();

        try {
            label108:

            for (int y = maxY; y < minY; ++y) {
                float f = 0.0F;
                int x = minX;

                while (true) {
                    if (x >= maxX) {
                        if (f < 1.0F) {
                            return (float) pooledMutableBlockPos.getY() + f;
                        }

                        break;
                    }

                    for (int z = minZ; z < maxZ; ++z) {
                        pooledMutableBlockPos.setPos(x, y, z);
                        IBlockState state = this.world.getBlockState(pooledMutableBlockPos);

                        Boolean result = state.getBlock().isAABBInsideMaterial(world, pooledMutableBlockPos, new AxisAlignedBB(pooledMutableBlockPos), Material.WATER);
                        if (result != null) {
                            if (!result) continue;
                            f = Math.max(f, state.getBlock().getBlockLiquidHeight(world, pooledMutableBlockPos, state, Material.WATER) + pooledMutableBlockPos.getY());
                        }

                        if (state.getMaterial() == Material.WATER) {
                            f = Math.max(f, BlockLiquid.getBlockLiquidHeight(state, this.world, pooledMutableBlockPos));
                        }

                        if (f >= 1.0F) {
                            continue label108;
                        }
                    }

                    ++x;
                }
            }

            return (float) (minY + 1);
        } finally {
            pooledMutableBlockPos.release();
        }
    }

    public float getBoatGlide() {
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.minY - 0.001D, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        int minX = MathHelper.floor(axisAlignedBB1.minX) - 1;
        int maxX = MathHelper.ceil(axisAlignedBB1.maxX) + 1;
        int minY = MathHelper.floor(axisAlignedBB1.minY) - 1;
        int maxY = MathHelper.ceil(axisAlignedBB1.maxY) + 1;
        int minZ = MathHelper.floor(axisAlignedBB1.minZ) - 1;
        int maxZ = MathHelper.ceil(axisAlignedBB1.maxZ) + 1;
        List<AxisAlignedBB> list = Lists.newArrayList();
        float f = 0.0F;
        int i = 0;
        BlockPos.PooledMutableBlockPos pooledMutableBlockPos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int x = minX; x < maxX; ++x) {
                for (int z = minZ; z < maxZ; ++z) {
                    int j = (x != minX && x != maxX - 1 ? 0 : 1) + (z != minZ && z != maxZ - 1 ? 0 : 1);

                    if (j != 2) {
                        for (int y = minY; y < maxY; ++y) {
                            if (j <= 0 || y != minY && y != maxY - 1) {
                                pooledMutableBlockPos.setPos(x, y, z);
                                IBlockState state = this.world.getBlockState(pooledMutableBlockPos);
                                state.addCollisionBoxToList(this.world, pooledMutableBlockPos, axisAlignedBB1, list, this, false);

                                if (!list.isEmpty()) {
                                    f += state.getBlock().getSlipperiness(state, this.world, pooledMutableBlockPos, this);
                                    ++i;
                                }

                                list.clear();
                            }
                        }
                    }
                }
            }
        } finally {
            pooledMutableBlockPos.release();
        }

        return f / (float) i;
    }

    private boolean checkInWater() {
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        int minX = MathHelper.floor(axisAlignedBB.minX);
        int maxX = MathHelper.ceil(axisAlignedBB.maxX);
        int minY = MathHelper.floor(axisAlignedBB.minY);
        int maxY = MathHelper.ceil(axisAlignedBB.minY + 0.001D);
        int minZ = MathHelper.floor(axisAlignedBB.minZ);
        int maxZ = MathHelper.ceil(axisAlignedBB.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos pooledMutableBlockPos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        pooledMutableBlockPos.setPos(x, y, z);
                        IBlockState state = this.world.getBlockState(pooledMutableBlockPos);

                        Boolean result = state.getBlock().isAABBInsideMaterial(world, pooledMutableBlockPos, axisAlignedBB, Material.WATER);
                        if (result != null) {
                            if (!result) continue;

                            float f = state.getBlock().getBlockLiquidHeight(world, pooledMutableBlockPos, state, Material.WATER) + pooledMutableBlockPos.getY();
                            this.waterLevel = Math.max(f, this.waterLevel);
                            flag |= axisAlignedBB.minY < (double) f;
                        }

                        if (state.getMaterial() == Material.WATER) {
                            float f = BlockLiquid.getLiquidHeight(state, this.world, pooledMutableBlockPos);
                            this.waterLevel = Math.max(f, this.waterLevel);
                            flag |= axisAlignedBB.minY < (double) f;
                        }
                    }
                }
            }
        } finally {
            pooledMutableBlockPos.release();
        }

        return flag;
    }

    private void controlBoat() {
        if (this.isBeingRidden()) {
            float f = 0.0F;

            if (this.leftInputDown) {
                this.deltaRotation += -1.0F;
            }

            if (this.rightInputDown) {
                ++this.deltaRotation;
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.005F;
            }

            this.rotationYaw += this.deltaRotation;

            if (this.forwardInputDown) {
                f += 0.04F;
            }

            if (this.backInputDown) {
                f -= 0.005F;
            }

            this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * f;
            this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * f;
            this.setPaddleState(this.rightInputDown && !this.leftInputDown || this.forwardInputDown, this.leftInputDown && !this.rightInputDown || this.forwardInputDown);
        }
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        this.lastYd = this.motionY;

        if (onGroundIn) {
            if (this.fallDistance > 3.0F) {
                if (this.status != EntityBoat.Status.ON_LAND) {
                    this.fallDistance = 0.0F;
                    return;
                }

                this.fall(this.fallDistance, 1.0F);

                if (!this.world.isRemote && !this.isDead) {
                    this.onFallingBreaking(this.fallDistance);
                    this.setDead();
                }
                this.fallDistance = 0.0F;
            }
        } else if (this.world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && y < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - y);
        }
    }

    private void updateMotion() {
        double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;
        float momentum = 0.05F;

        if (this.previousStatus == EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.ON_LAND) {
            this.waterLevel = this.getEntityBoundingBox().minY + (double) this.height;
            double waterLevelAbove = this.getWaterLevelAbove();
            double partialY = waterLevelAbove - this.height;
            double y = partialY + 0.101D;
            this.setPosition(this.posX, y, this.posZ);
            this.motionY = 0.0D;
            this.lastYd = 0.0D;
            this.status = EntityBoat.Status.IN_WATER;
        } else {
            if (this.status == EntityBoat.Status.IN_WATER) {
                d2 = (this.waterLevel - this.getEntityBoundingBox().minY) / (double) this.height;
                momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4D;
                momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_WATER) {
                d2 = 0.009999999776482582D;
                momentum = 0.45F;
            } else if (this.status == EntityBoat.Status.IN_AIR) {
                momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.ON_LAND) {
                momentum = this.boatGlide;

                if (this.getControllingPassenger() instanceof EntityPlayer) {
                    this.boatGlide /= 2.0F;
                }
            }

            this.motionX *= momentum;
            this.motionZ *= momentum;
            this.deltaRotation *= momentum;
            this.motionY += d1;

            if (d2 > 0.0D) {
                this.motionY += d2 * 0.06153846016296973D;
                this.motionY *= 0.75D;
            }
        }
    }

    public void updatePassenger(Entity passenger) {
        if (chest.getStackInSlot(0).isEmpty() || ElectricItem.manager.getCharge(this.chest.getStackInSlot(0)) == 0) {
            if (this.isPassenger(passenger)) {
                float f = 0.0F;
                float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

                Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
                passenger.setPosition(this.posX + vec3d.x, this.posY + (double) f1, this.posZ + vec3d.z);
                passenger.rotationYaw += this.deltaRotation;
                passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
                this.applyYawToEntity(passenger);
            }
        } else {
            super.updatePassenger(passenger);
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void applyOrientationToEntity(Entity entityToUpdate) {
        if (chest.getStackInSlot(0).isEmpty() || ElectricItem.manager.getCharge(this.chest.getStackInSlot(0)) == 0) {
            this.applyYawToEntity(entityToUpdate);
        } else {
            super.applyOrientationToEntity(entityToUpdate);
        }
    }


    public void setPaddleState(boolean left, boolean right) {
        this.dataManager.set(DATA_ID_PADDLE[0], left);
        this.dataManager.set(DATA_ID_PADDLE[1], right);
    }

    public boolean getPaddleState(int side) {
        return this.dataManager.get(DATA_ID_PADDLE[side]) && this.getControllingPassenger() != null;
    }

    @SideOnly(Side.CLIENT)
    public void updateInputs(boolean leftInputDown, boolean rightInputDown, boolean forwardInputDown, boolean backInputDown) {
        this.leftInputDown = leftInputDown;
        this.rightInputDown = rightInputDown;
        this.forwardInputDown = forwardInputDown;
        this.backInputDown = backInputDown;
    }
}
