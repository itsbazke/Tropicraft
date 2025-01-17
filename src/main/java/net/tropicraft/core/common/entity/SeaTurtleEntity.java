package net.tropicraft.core.common.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.egg.SeaTurtleEggEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class SeaTurtleEntity extends Turtle {

    private static final EntityDataAccessor<Boolean> IS_MATURE = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TURTLE_TYPE = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> NO_BRAKES = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_FLY = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DIGGING = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(SeaTurtleEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int NUM_TYPES = 6;

    private double lastPosY;
    private int digCounter;

    public SeaTurtleEntity(EntityType<? extends Turtle> type, Level world) {
        super(type, world);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        setRandomTurtleType();
        lastPosY = getY();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // goals
        Set<WrappedGoal> goalSet = goalSelector.getAvailableGoals();

        Optional<WrappedGoal> eggGoal = goalSet.stream().filter(p -> p.getGoal().toString().contains("Egg")).findFirst();
        if (eggGoal.isPresent()) {
            goalSelector.removeGoal(eggGoal.get().getGoal());
            goalSelector.addGoal(1, new BetterLayEggGoal(this, 1.0));
        }

        Optional<WrappedGoal> mateGoal = goalSet.stream().filter(p -> p.getGoal().toString().contains("Mate")).findFirst();
        if (mateGoal.isPresent()) {
            goalSelector.removeGoal(mateGoal.get().getGoal());
            goalSelector.addGoal(1, new BetterMateGoal(this, 1.0));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_MATURE, true);
        builder.define(TURTLE_TYPE, 1);
        builder.define(NO_BRAKES, false);
        builder.define(CAN_FLY, false);
        builder.define(IS_DIGGING, false);
        builder.define(HAS_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
        nbt.putBoolean("NoBrakesOnThisTrain", getNoBrakes());
        nbt.putBoolean("LongsForTheSky", getCanFly());
        nbt.putBoolean("HasEgg", hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("TurtleType")) {
            setTurtleType(nbt.getInt("TurtleType"));
        } else {
            setRandomTurtleType();
        }
        if (nbt.contains("IsMature")) {
            setIsMature(nbt.getBoolean("IsMature"));
        } else {
            setIsMature(true);
        }
        setNoBrakes(nbt.getBoolean("NoBrakesOnThisTrain"));
        setCanFly(nbt.getBoolean("LongsForTheSky"));
        setHasEgg(nbt.getBoolean("HasEgg"));
        lastPosY = getY();
    }

    public boolean isMature() {
        return getEntityData().get(IS_MATURE);
    }

    public SeaTurtleEntity setIsMature(boolean mature) {
        getEntityData().set(IS_MATURE, mature);
        return this;
    }

    public int getTurtleType() {
        return getEntityData().get(TURTLE_TYPE);
    }

    public void setRandomTurtleType() {
        setTurtleType(random.nextInt(NUM_TYPES) + 1);
    }

    public SeaTurtleEntity setTurtleType(int type) {
        getEntityData().set(TURTLE_TYPE, Mth.clamp(type, 1, NUM_TYPES));
        return this;
    }

    public boolean getNoBrakes() {
        return getEntityData().get(NO_BRAKES);
    }

    public SeaTurtleEntity setNoBrakes(boolean noBrakes) {
        getEntityData().set(NO_BRAKES, noBrakes);
        return this;
    }

    public boolean getCanFly() {
        return getEntityData().get(CAN_FLY);
    }

    public SeaTurtleEntity setCanFly(boolean canFly) {
        getEntityData().set(CAN_FLY, canFly);
        return this;
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity living ? living : null;
    }

    public static boolean canSpawnOnLand(EntityType<SeaTurtleEntity> turtle, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        return pos.getY() < TropicraftDimension.getSeaLevel(world) + 4 && world.getBlockState(pos.below()).is(Blocks.SAND) && world.getRawBrightness(pos, 0) > 8;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob partner) {
        return TropicraftEntities.SEA_TURTLE.get().create(level())
                .setTurtleType(random.nextBoolean() && partner instanceof SeaTurtleEntity ? ((SeaTurtleEntity) partner).getTurtleType() : getTurtleType())
                .setIsMature(false);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = super.mobInteract(player, hand);
        if (result != InteractionResult.PASS) {
            return result;
        }

        if (!level().isClientSide && !player.isShiftKeyDown() && canAddPassenger(player) && isMature()) {
            player.startRiding(this);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        Entity controller = getControllingPassenger();
        if (controller != null) {
            return controller.shouldRender(x, y, z);
        }
        return super.shouldRender(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        lastPosY = getY();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isAlive() && isLayingEgg() && digCounter >= 1 && digCounter % 5 == 0) {
            BlockPos pos = blockPosition();
            if (level().getBlockState(pos.below()).is(BlockTags.SAND)) {
                level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.SAND.defaultBlockState()));
            }
        }

        if (level().isClientSide) {
            if (isVehicle() && hasControllingPassenger()) {
                if (isInWater() || getCanFly()) {
                    Vec3 movement = new Vec3(getX(), getY(), getZ()).subtract(xo, yo, zo);
                    double speed = movement.length();
                    Vec3 particleOffset = movement.reverse().scale(2);
                    if (speed > 0.05) {
                        int maxParticles = Mth.ceil(speed * 5);
                        int particlesToSpawn = random.nextInt(1 + maxParticles);
                        ParticleOptions particle = isInWater() ? ParticleTypes.BUBBLE : ParticleTypes.END_ROD;
                        for (int i = 0; i < particlesToSpawn; i++) {
                            Vec3 particleMotion = movement.scale(1);
                            level().addParticle(particle, true,
                                    particleOffset.x() + getX() - 0.25 + random.nextDouble() * 0.5,
                                    particleOffset.y() + getY() + 0.1 + random.nextDouble() * 0.1,
                                    particleOffset.z() + getZ() - 0.25 + random.nextDouble() * 0.5, particleMotion.x, particleMotion.y, particleMotion.z);
                        }
                    }
                }
            }
        }
    }

    public float lerp(float x1, float x2, float t) {
        return x1 + (t * 0.03f) * Mth.wrapDegrees(x2 - x1);
    }

    private float swimSpeedCurrent;

    @Override
    public void positionRider(Entity passenger, MoveFunction function) {
        super.positionRider(passenger, function);
        if (hasPassenger(passenger)) {
            if (passenger instanceof Player p) {
                if (isInWater()) {
                    if (p.zza > 0.0f) {
                        setXRot(lerp(getXRot(), -(passenger.getXRot() * 0.5f), 6.0f));
                        setYRot(lerp(getYRot(), -passenger.getYRot(), 6.0f));
//                        this.targetVector = null;
//                        this.targetVectorHeading = null;
                        swimSpeedCurrent += 0.05f;
                        if (swimSpeedCurrent > 4.0f) {
                            swimSpeedCurrent = 4.0f;
                        }
                    }
                    if (p.zza < 0.0f) {
                        swimSpeedCurrent *= 0.89f;
                        if (swimSpeedCurrent < 0.1f) {
                            swimSpeedCurrent = 0.1f;
                        }
                    }
                    if (p.zza == 0.0f) {
                        if (swimSpeedCurrent > 1.0f) {
                            swimSpeedCurrent *= 0.94f;
                            if (swimSpeedCurrent <= 1.0f) {
                                swimSpeedCurrent = 1.0f;
                            }
                        }
                        if (swimSpeedCurrent < 1.0f) {
                            swimSpeedCurrent *= 1.06f;
                            if (swimSpeedCurrent >= 1.0f) {
                                swimSpeedCurrent = 1.0f;
                            }
                        }
                        //this.swimSpeedCurrent = 1.0f;
                    }
                    //    this.swimYaw = -passenger.rotationYaw;
                }
                //p.rotationYaw = this.rotationYaw;
            } else if (passenger instanceof Mob mobentity) {
                yBodyRot = mobentity.yBodyRot;
                yHeadRotO = mobentity.yHeadRotO;
            }
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 input) {
        super.tickRidden(player, input);
        setRot(player.getYRot(), player.getXRot());
        yRotO = yBodyRot = yHeadRot = getYRot();

        if (!isInWater()) {
            double gravity = getGravity();
            if (getCanFly()) {
                setDeltaMovement(getDeltaMovement().add(0, -gravity * 0.05, 0));
            } else {
                // Lower max speed when breaching, as a penalty to uncareful driving
                setDeltaMovement(getDeltaMovement().multiply(0.9, 0.99, 0.9).add(0, -gravity, 0));
            }
        }

        if (!isControlledByLocalInstance()) {
            fallDistance = (float) Math.max(0, (getY() - lastPosY) * -8);
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 mobInput) {
        double strafe = player.xxa;
        double forward = getNoBrakes() ? 1.0 : player.zza;

        double vertical = -Math.sin(Math.toRadians(getXRot())) * forward;
        forward *= Mth.clamp(1.0 - Math.abs(getXRot()) / 90.0, 0.01, 1.0);

        return new Vec3(strafe, vertical + player.yya, forward);
    }

    @Override
    public void travel(Vec3 input) {
        if (hasControllingPassenger()) {
            Vec3 travel = input.scale(getAttribute(Attributes.MOVEMENT_SPEED).getValue())
                    // This scale controls max speed. We reduce it significantly here so that the range of speed is higher
                    // This is compensated for by the high value passed to moveRelative
                    .scale(0.025f);
            // This is the effective speed modifier, controls the post-scaling of the movement vector
            moveRelative(1.0f, travel);
            move(MoverType.SELF, getDeltaMovement());
            // This value controls how much speed is "dampened" which effectively controls how much drift there is, and the max speed
            setDeltaMovement(getDeltaMovement().scale(input.z > 0 || !isInWater() ? 0.975 : 0.9));
            calculateEntityAnimation(true);
            return;
        }
        super.travel(input);
    }

    @Override
    protected float getFlyingSpeed() {
        LivingEntity passenger = getControllingPassenger();
        return passenger != null ? getSpeed() * 0.1f : 0.02f;
    }

    static class BetterLayEggGoal extends MoveToBlockGoal {
        private final SeaTurtleEntity turtle;

        BetterLayEggGoal(SeaTurtleEntity turtle, double speedIn) {
            super(turtle, speedIn, 16);
            this.turtle = turtle;
        }

        @Override
        public boolean canUse() {
            return turtle.hasEgg() && turtle.getHomePos().closerToCenterThan(turtle.position(), 9.0) && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && turtle.hasEgg() && turtle.getHomePos().closerToCenterThan(turtle.position(), 9.0);
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos blockpos = turtle.blockPosition();
            if (!turtle.isInWater() && isReachedTarget()) {
                if (!turtle.isLayingEgg()) {
                    turtle.setDigging(true);
                } else if (turtle.digCounter > 200) {
                    Level world = turtle.level();
                    world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3f, 0.9f + world.random.nextFloat() * 0.2f);
                    //world.setBlockState(this.destinationBlock.up(), Blocks.TURTLE_EGG.defaultBlockState().with(TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.rand.nextInt(4) + 1)), 3);
                    SeaTurtleEggEntity egg = TropicraftEntities.SEA_TURTLE_EGG.get().create(world);
                    BlockPos spawnPos = blockPos.above();
                    egg.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    world.addFreshEntity(egg);
                    turtle.setHasEgg(false);
                    turtle.setDigging(false);
                    turtle.setInLoveTime(600);
                }

                if (turtle.isLayingEgg()) {
                    turtle.digCounter++;
                }
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            if (!worldIn.isEmptyBlock(pos.above())) {
                return false;
            } else {
                return worldIn.getBlockState(pos).is(BlockTags.SAND);
            }
        }
    }

    static class BetterMateGoal extends BreedGoal {
        private final SeaTurtleEntity turtle;

        BetterMateGoal(SeaTurtleEntity turtle, double speedIn) {
            super(turtle, speedIn);
            this.turtle = turtle;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !turtle.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayer serverplayerentity = animal.getLoveCause();
            if (serverplayerentity == null && partner.getLoveCause() != null) {
                serverplayerentity = partner.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, animal, partner, null);
            }

            turtle.setHasEgg(true);
            animal.resetLove();
            partner.resetLove();
            RandomSource random = animal.getRandom();
            if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                level.addFreshEntity(new ExperienceOrb(level, animal.getX(), animal.getY(), animal.getZ(), random.nextInt(7) + 1));
            }
        }
    }

    private void setDigging(boolean digging) {
        digCounter = digging ? 1 : 0;
        entityData.set(IS_DIGGING, digging);
    }

    @Override
    public boolean isLayingEgg() {
        return digCounter > 0;
    }

    private void setHasEgg(boolean hasEgg) {
        entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean hasEgg() {
        return entityData.get(HAS_EGG);
    }
}
