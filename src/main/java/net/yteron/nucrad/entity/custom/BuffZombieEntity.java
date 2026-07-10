package net.yteron.nucrad.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BuffZombieEntity extends ZombieEntity {

    public BuffZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    // ========================================
    // 1. АТРИБУТЫ (ИСПРАВЛЕНО!)
    // ========================================
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()  // <-- ИСПРАВЛЕНО: func_233666_p_() → createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)      // <-- ИСПРАВЛЕНО: createMutableAttribute → add
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
        // .add(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS); // <-- УДАЛЕНО (не существует в 1.16.5)
    }

    // ========================================
    // 2. ЦЕЛИ
    // ========================================
    @Override
    protected void registerGoals() {
        // super.registerGoals(); // <-- УБРАНО (чтобы не дублировать цели)

        // Цели для атаки (targetSelector)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this)
                .setAlertOthers(ZombifiedPiglinEntity.class));  // <-- ИСПРАВЛЕНО: setCallsForHelp → setAlertOthers

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));  // <-- ИСПРАВЛЕНО

        // Поведение (goalSelector)
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
    }

    // ========================================
    // 3. ОПЫТ
    // ========================================
    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return 3 + this.random.nextInt(5);  // <-- ИСПРАВЛЕНО: world.rand → random
    }

    // ========================================
    // 4. ЗВУКИ
    // ========================================
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ZOGLIN_STEP, 0.20F, 0.5F);
    }

    // ========================================
    // 5. АТАКА С ЭФФЕКТАМИ
    // ========================================
    @Override
    public boolean doHurtTarget(Entity entity) {
        // 1. Вызываем родительский метод для нанесения урона
        boolean attacked = super.doHurtTarget(entity);

        // 2. Если атака успешна
        if (attacked) {
            // 3. Проверяем, что цель - живое существо
            if (entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entity;

                // 4. Применяем эффекты
                target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 3));
                target.addEffect(new EffectInstance(Effects.WEAKNESS, 200, 0));
                target.addEffect(new EffectInstance(Effects.POISON, 200, 0));
            }

            // 5. Возвращаем true (атака успешна)
            return true;
        }

        // 6. Атака не удалась
        return false;
    }
}
