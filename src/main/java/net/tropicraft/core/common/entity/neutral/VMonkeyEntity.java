package net.tropicraft.core.common.entity.neutral;

import com.google.common.base.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.TropicraftDrinks;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeyAngryThrowGoal;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeyFollowNearestPinaColadaHolderGoal;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeyPickUpPinaColadaGoal;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeySitAndDrinkGoal;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeySitInChairGoal;
import net.tropicraft.core.common.entity.ai.vmonkey.MonkeyStealDrinkGoal;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;

public class VMonkeyEntity extends TamableAnimal {

    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(VMonkeyEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_CLIMBING = 1 << 0;

    public static final Predicate<LivingEntity> FOLLOW_PREDICATE = ent -> {
        if (ent == null) return false;
        if (!(ent instanceof Player player)) return false;

        return CocktailItem.hasDrink(player.getMainHandItem(), TropicraftDrinks.PINA_COLADA)
                || CocktailItem.hasDrink(player.getOffhandItem(), TropicraftDrinks.PINA_COLADA);
    };

    /**
     * Entity this monkey is following around
     */
    @Nullable
    private LivingEntity following;
    private boolean madAboutStolenAlcohol;

    public VMonkeyEntity(EntityType<? extends TamableAnimal> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS, (byte) 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new MonkeyFollowNearestPinaColadaHolderGoal(this, 1.0, 2.0f, 10.0f));
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4f));
        goalSelector.addGoal(3, new MonkeyPickUpPinaColadaGoal(this));
        goalSelector.addGoal(2, new MonkeyStealDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeySitAndDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeyAngryThrowGoal(this));
        goalSelector.addGoal(4, new MonkeySitInChairGoal(this));
        goalSelector.addGoal(4, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f));
        goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0));
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("MonkeyFlags", getMonkeyFlags());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setMonkeyFlags(compound.getByte("MonkeyFlags"));
    }

    @Nullable
    public LivingEntity getFollowing() {
        return following;
    }

    public void setFollowing(@Nullable LivingEntity following) {
        this.following = following;
    }

    public boolean selfHoldingDrink(ResourceKey<Drink> drink) {
        return CocktailItem.hasDrink(getMainHandItem(), drink);
    }

    private void setMonkeyFlags(byte flags) {
        getEntityData().set(DATA_FLAGS, flags);
    }

    private byte getMonkeyFlags() {
        return getEntityData().get(DATA_FLAGS);
    }

    public boolean isClimbing() {
        return getMonkeyFlag(FLAG_CLIMBING);
    }

    private void setClimbing(boolean state) {
        setMonkeyFlag(FLAG_CLIMBING, state);
    }

    public void setMonkeyFlag(int id, boolean flag) {
        if (flag) {
            entityData.set(DATA_FLAGS, (byte) (entityData.get(DATA_FLAGS) | id));
        } else {
            entityData.set(DATA_FLAGS, (byte) (entityData.get(DATA_FLAGS) & ~id));
        }
    }

    private boolean getMonkeyFlag(int flag) {
        return (entityData.get(DATA_FLAGS) & flag) != 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTame()) {
            if (isOwnedBy(player) && !level().isClientSide) {
                setOrderedToSit(!isOrderedToSit());
                jumping = false;
                navigation.stop();
                setTarget(null);
                setAggressive(false);
            }
        } else if (!stack.isEmpty() && isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (!level().isClientSide) {
                if (random.nextInt(3) == 0) {
                    setTame(true, true);
                    navigation.stop();
                    setTarget(null);
                    setOrderedToSit(true);
                    setHealth(20.0f);
                    setOwnerUUID(player.getUUID());
                    level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    level().broadcastEntityEvent(this, (byte) 6);
                }
            }

            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Holder<Drink> drink = CocktailItem.getDrink(stack);
        return drink != null && drink.is(TropicraftDrinks.PINA_COLADA);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        // Only attack players, and only when not tamed
        // NOTE: Maybe we want to attack other players though?
        return !isTame() && target.getType() == EntityType.PLAYER;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            setOrderedToSit(false);

            if (entity != null && entity.getType() != EntityType.PLAYER && !(entity instanceof Arrow)) {
                amount = (amount + 1.0f) / 2.0f;
            }

            return super.hurt(source, amount);
        }
    }

    public boolean isMadAboutStolenAlcohol() {
        return madAboutStolenAlcohol;
    }

    public void setMadAboutStolenAlcohol(boolean madAboutStolenAlcohol) {
        this.madAboutStolenAlcohol = madAboutStolenAlcohol;
    }
}
