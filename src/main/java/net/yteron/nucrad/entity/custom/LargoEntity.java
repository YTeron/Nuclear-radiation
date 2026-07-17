package net.yteron.nucrad.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargoEntity extends MonsterEntity {

    public LargoEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()  // <-- ИСПРАВЛЕНО: func_233666_p_() → createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)      // <-- ИСПРАВЛЕНО: createMutableAttribute → add
                .add(Attributes.MOVEMENT_SPEED, 0.53D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 1500.0D)
                .add(Attributes.ATTACK_KNOCKBACK,5.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE,0.2D); // <-- УДАЛЕНО (не существует в 1.16.5)

    }

    @Override
    protected void registerGoals() {
        // Атака игроков
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));

        // Основные цели
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }
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
                target.addEffect(new EffectInstance(Effects.POISON, 200, 1));
            }

            // 5. Возвращаем true (атака успешна)
            return true;
        }

        // 6. Атака не удалась
        return false;
    }

}