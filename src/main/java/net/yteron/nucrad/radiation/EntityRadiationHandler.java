package net.yteron.nucrad.radiation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public abstract class EntityRadiationHandler {

    /**
     * Updates the radiation system, i.e. all worlds.
     * Doesn't need parameters because it governs the ENTIRE system.
     */

    public abstract float getRadiation(Entity entity, World world);
    public abstract void setRadiation(Entity entity,World world, float rad);
    public abstract void clearSystem(Entity entity,World world);

    /*
     * ProxyЕ event handlers
     */
    public void entityJoin(EntityJoinWorldEvent event) { }
    public void entityLeave(EntityLeaveWorldEvent event) { }
    public void onEntityDeath(LivingDeathEvent event) { }
    public void heartDamage(LivingHurtEvent event) { }
    public void updateSystem() { };
}