package dahminh.overloadedorigins.entity.custom;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.sound.OOSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

public class ShadowDecoyEntity extends HostileEntity implements Ownable {
    private static final DustColorTransitionParticleEffect PARTICLE_EFFECT = new DustColorTransitionParticleEffect(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.15f, 0.15f, 0.15f), 2);
    private static final double DASH_SOUND_CHANCE = 0.6;
    private static final double APPEAR_SOUND_CHANCE = 0.5;
    private static final TrackedData<Byte> SHADOW_DECOY_FLAGS = DataTracker.registerData(ShadowDecoyEntity.class, TrackedDataHandlerRegistry.BYTE);
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;

    public ShadowDecoyEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }

    @Override
    @Nullable
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        }
        if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            this.owner = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            return this.owner;
        }
        return null;
    }

    public boolean isOwner(Entity entity) {
        return entity.getUuid().equals(this.ownerUuid);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
            this.owner = null;
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerTargetGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, o -> o != this.getOwner()));
    }
    public static DefaultAttributeContainer.Builder createShadowDecoyAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new SpiderNavigation(this, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHADOW_DECOY_FLAGS, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        this.setInvisible(true);
        if (this.age == 600) this.kill();
        if (!this.getWorld().isClient) {
            getWorld().addParticle(PARTICLE_EFFECT,
                    this.getX() + this.random.nextGaussian() * 0.3,
                    this.getY() + 0.1 + this.random.nextGaussian() * 0.1,
                    this.getZ() + this.random.nextGaussian() * 0.3,
                    0, 0, 0);
            ((ServerWorld) this.getWorld()).spawnParticles(
                    PARTICLE_EFFECT,
                    this.getX(),
                    this.getY() + 0.1,
                    this.getZ(),
                    1,
                    0.3,
                    0.1,
                    0.3,
                    0);
            this.setClimbingWall(this.horizontalCollision);
        }
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    @Override
    public boolean shouldDropXp() {
        return false;
    }

    @Override
    public void playAmbientSound() {
        this.playSound(OOSounds.DARK_ELF_AMBIENT, 0.75f, 0);
        if (this.random.nextDouble() <= DASH_SOUND_CHANCE) {
            this.playSound(OOSounds.DARK_ELF_DASH, 1, 1);
        } else if (this.random.nextDouble() <= APPEAR_SOUND_CHANCE) {
            this.playSound(OOSounds.DARK_ELF_APPEARS, 1, 2);
        }
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected void updatePostDeath() {
        this.remove(Entity.RemovalReason.KILLED);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.playSound(OOSounds.DARK_ELF_VANISHES, 1, 2);
        if (!this.getWorld().isClient()) {
            ((ServerWorld) this.getWorld()).spawnParticles(
                    LARGE_SMOKE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    150,
                    1.5,
                    0.25,
                    1.5,
                    0.05
            );

            Box box = new Box(this.getBlockPos()).expand(3.5);
            List<LivingEntity> affectedEntities = this.getWorld().getEntitiesByClass(LivingEntity.class, box, o -> o != this.getOwner());
            for (LivingEntity affectedEntity : affectedEntities) {
                affectedEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 9));
            }
        }
        super.onDeath(damageSource);
    }

    @Override
    protected float getJumpVelocity() {
        return 1.5f * super.getJumpVelocity();
    }

    @Override
    public boolean isPushable() { return false; }

//    @Override
//    public boolean isInvulnerableTo(DamageSource damageSource) {
//        return damageSource.isOf(DamageTypes.IN_WALL) && super.isInvulnerableTo(damageSource);
//    }

    @Override
    public boolean isClimbing() {
        return this.isClimbingWall();
    }

    public boolean isClimbingWall() {
        return (this.dataTracker.get(SHADOW_DECOY_FLAGS) & 1) != 0;
    }

    public void setClimbingWall(boolean climbing) {
        byte b = this.dataTracker.get(SHADOW_DECOY_FLAGS);
        b = climbing ? (byte)(b | 1) : (byte)(b & 0xFFFFFFFE);
        this.dataTracker.set(SHADOW_DECOY_FLAGS, b);
    }

    class TrackOwnerTargetGoal extends TrackTargetGoal {
        private final TargetPredicate targetPredicate = TargetPredicate.createNonAttackable().ignoreVisibility().ignoreDistanceScalingFactor();
        private LivingEntity attacking;

        public TrackOwnerTargetGoal(PathAwareEntity mob) {
            super(mob, false);
        }

        public boolean canStart() {
            if (ShadowDecoyEntity.this.owner == null) return false;
            if (!(ShadowDecoyEntity.this.owner instanceof LivingEntity)) return false;

            LivingEntity livingEntityOwner = (LivingEntity) ShadowDecoyEntity.this.owner;
            LivingEntity attacking = livingEntityOwner.getAttacking();
            this.attacking = attacking;

            return attacking != null && this.canTrack(attacking, this.targetPredicate);
        }

        public void start() {
            ShadowDecoyEntity.this.setTarget(this.attacking);
            super.start();
        }
    }
}
