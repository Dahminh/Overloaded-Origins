package dahminh.overloadedorigins.entity.custom;

import com.google.common.base.MoreObjects;
import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.entity.OOEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SpectreEntity extends ProjectileEntity {
    @Nullable
    private Entity target;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;
    @Nullable
    private UUID targetUuid;

    private int life;

    public SpectreEntity(EntityType<? extends SpectreEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.accelerationX = this.random.nextDouble() * 0.1 + 0.05;
        this.accelerationY = this.random.nextDouble() * 0.1 + 0.05;
        this.accelerationZ = this.random.nextDouble() * 0.1 + 0.05;
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        if ((entity instanceof LivingEntity)) {
            this.getTargetFromOwner((LivingEntity) entity);
        }
    }

    public void getTargetFromOwner(@Nullable LivingEntity owner) {
        LivingEntity potentialTarget = owner.getAttacking();
        int lastAttackedTime         = owner.getLastAttackedTime();
        int lastAttackTime           = owner.getLastAttackTime();
        //Set Target to Attacking
        if (potentialTarget != null && owner.age - lastAttackTime <= 100 && potentialTarget.isAlive()) {
            this.target = potentialTarget;
            return;
        }
        //Set Target to Attacked
        potentialTarget = owner.getAttacker();
        if (potentialTarget != null && owner.age - lastAttackedTime <= 100 && potentialTarget.isAlive()) {
            this.target = potentialTarget;
            return;
        }
        //Set Target to Random Feared Entity
        getRandomTarget(false);
    }

    public void getRandomTarget(boolean retarget) {
        BlockPos blockPos;
        if (retarget) {
            blockPos = this.getBlockPos();
        } else {
            blockPos = this.getOwner().getBlockPos();
        }
        double fearSearchRadius = 25;
        Box box = new Box(blockPos).expand(fearSearchRadius);
        List<LivingEntity> nearbyFearedEntities = this.getWorld().getEntitiesByClass(LivingEntity.class, box, o -> o.hasStatusEffect(OOEffects.FEAR) && o != this.getOwner());
        if (nearbyFearedEntities.isEmpty()) return;
        int randomNumber = this.random.nextInt(nearbyFearedEntities.size());
        this.target = nearbyFearedEntities.get(randomNumber);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putShort("life", (short)this.life);
        if (this.target != null) {
            nbt.putUuid("Target", this.target.getUuid());
        }
        nbt.putDouble("TXD", this.targetX);
        nbt.putDouble("TYD", this.targetY);
        nbt.putDouble("TZD", this.targetZ);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.life = nbt.getShort("life");
        this.targetX = nbt.getDouble("TXD");
        this.targetY = nbt.getDouble("TYD");
        this.targetZ = nbt.getDouble("TZD");
        if (nbt.containsUuid("Target")) {
            this.targetUuid = nbt.getUuid("Target");
        }
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
        this.life = 0;
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        super.setVelocityClient(x, y, z);
        this.life = 0;
    }

    protected void age() {
        ++this.life;
        if (this.life >= 1200) {
            this.discard();
        }
    }

    private void changeTargetDirection() {
        BlockPos blockPos;
        double d = 0;
        if (this.target == null) {
            blockPos = this.getBlockPos().down();
        } else {
            d = (double)this.target.getHeight() * 0.5;
            blockPos = BlockPos.ofFloored(this.target.getX(), this.target.getY() + d, this.target.getZ());
        }
        double e = (double)blockPos.getX() + 0.5;
        double f = (double)blockPos.getY() + d;
        double g = (double)blockPos.getZ() + 0.5;

        double h = e - this.getX();
        double j = f - this.getY();
        double k = g - this.getZ();
        double l = Math.sqrt(h * h + j * j + k * k);
        if (l == 0.0) {
            this.targetX = 0.0;
            this.targetY = 0.0;
            this.targetZ = 0.0;
        } else {
            this.targetX = h / l * 0.3;
            this.targetY = j / l * 0.3;
            this.targetZ = k / l * 0.3;
        }
    }

    @Override
    public void tick() {
        // TODO: Sound Effects
        Vec3d vec3d;
        super.tick();
        Entity entity = this.getOwner();

        if (!this.getWorld().isClient) {
            this.age();
            if (this.target == null && this.targetUuid != null) {
                this.target = ((ServerWorld) this.getWorld()).getEntity(this.targetUuid);
                if (this.target == null) {
                    this.targetUuid = null;
                }
            }
            if (!(this.target == null || !this.target.isAlive() || this.target instanceof PlayerEntity && this.target.isSpectator())) {
                // Slope
                this.targetX = MathHelper.clamp(this.targetX * 1.025, -1.0, 1.0);
                this.targetY = MathHelper.clamp(this.targetY * 1.025, -1.0, 1.0);
                this.targetZ = MathHelper.clamp(this.targetZ * 1.025, -1.0, 1.0);
                vec3d = this.getVelocity();
                this.setVelocity(vec3d.add(
                        (this.targetX - vec3d.x) * this.accelerationX,
                        (this.targetY - vec3d.y) * this.accelerationY,
                        (this.targetZ - vec3d.z) * this.accelerationZ)
                );

            } else if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
            }
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
        }
        this.checkBlockCollision();
        vec3d = this.getVelocity();
        this.setPosition(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
        //Maybe change delta?
        ProjectileUtil.setRotationFromVelocity(this, 0.5f);
        if (this.getWorld().isClient) {
            //this.getWorld().addParticle(ParticleTypes.END_ROD, this.getX() - vec3d.x, this.getY() - vec3d.y + 0.15, this.getZ() - vec3d.z, 0.0, 0.0, 0.0);
        } else if (this.target != null) {
            if (this.target.isRemoved()) getRandomTarget(true);
            if (this.age % 5 == 0) {
                this.changeTargetDirection();
            }
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.noClip;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        if (this.age < 5) {
            return false;
        }
        return distance < 16384.0;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity owner = this.getOwner();
        LivingEntity livingEntity = owner instanceof LivingEntity ? (LivingEntity)owner : null;

        Box box = new Box(this.getBlockPos()).expand(1.5);
        List<LivingEntity> affectedEntities = this.getWorld().getEntitiesByClass(LivingEntity.class, box, o -> o != this.getOwner() && o.damage(this.getDamageSources().indirectMagic(this, livingEntity), 4.0f));
        for (LivingEntity affectedEntity : affectedEntities) {
            this.applyDamageEffects(livingEntity, affectedEntity);
            affectedEntity.addStatusEffect(new StatusEffectInstance(OOEffects.FEAR, 200), MoreObjects.firstNonNull(owner, this));
        }
    }
    private void destroy() {
        this.discard();
        this.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, this.getPos(), GameEvent.Emitter.of(this));
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)hitResult).getEntity();
            this.onEntityHit((EntityHitResult)hitResult);
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
            this.destroy();
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient) {
            if (source.getAttacker() != null && source.getAttacker() == this.getOwner()) return false;
            // TODO: Sound Effects
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0f, 1.0f);
            ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 0.5, this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.destroy();
        }
        return true;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double d = packet.getVelocityX();
        double e = packet.getVelocityY();
        double f = packet.getVelocityZ();
        this.setVelocity(d, e, f);
    }
}
