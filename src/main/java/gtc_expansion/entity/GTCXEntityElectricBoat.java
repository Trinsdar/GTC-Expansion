package gtc_expansion.entity;

import gtc_expansion.GTCExpansion;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.entity.boat.EntityClassicBoat;
import ic2.core.entity.boat.EntityElectricBoat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXEntityElectricBoat extends EntityElectricBoat {
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[]{EntityDataManager.createKey(GTCXEntityElectricBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(GTCXEntityElectricBoat.class, DataSerializers.BOOLEAN)};
    private float deltaRotation;

    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;

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
        Entity rider = this.getRider();
        if (IC2.platform.isRendering()){
            if (rider instanceof EntityPlayerSP){
                EntityPlayerSP playerSP = (EntityPlayerSP) rider;
                this.updateInputs(playerSP.movementInput.leftKeyDown, playerSP.movementInput.rightKeyDown, playerSP.movementInput.forwardKeyDown, playerSP.movementInput.backKeyDown);
            } else if (leftInputDown || rightInputDown || forwardInputDown || backInputDown){
                this.updateInputs(false, false, false, false);
            }
        }
        if (IC2.platform.isSimulating()) {

            if (this.chest.getStackInSlot(0).isEmpty() || ElectricItem.manager.getCharge(this.chest.getStackInSlot(0)) == 0) {

                if (forwardInputDown && rider != null && this.canPassengerSteer()) {
                    GTCExpansion.logger.info("forward being pressed");
                    if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                        this.setPaddleState(false, false);
                    }
                    //this.updateMotion();

                    if (this.world.isRemote) {
                        this.controlBoat();
                        this.world.sendPacketToServer(new CPacketSteerBoat(this.getPaddleState(0), this.getPaddleState(1)));
                    }

                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    ((EntityClassicBoat)this).onUpdate();
                } else {
                    this.setPaddleState(false, false);
                    super.onUpdate();
                }
            } else {
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

    public void setPaddleState(boolean left, boolean right) {
        this.dataManager.set(DATA_ID_PADDLE[0], left);
        this.dataManager.set(DATA_ID_PADDLE[1], right);
    }

    public boolean getPaddleState(int side) {
        return this.dataManager.get(DATA_ID_PADDLE[side]) && this.getControllingPassenger() != null;
    }

    @SideOnly(Side.CLIENT)
    public void updateInputs(boolean leftInputDown, boolean rightInputDown, boolean forwardInputDown, boolean backInputDown)
    {
        this.leftInputDown = leftInputDown;
        this.rightInputDown = rightInputDown;
        this.forwardInputDown = forwardInputDown;
        this.backInputDown = backInputDown;
    }
}
