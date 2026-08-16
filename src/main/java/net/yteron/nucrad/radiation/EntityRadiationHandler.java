package net.yteron.nucrad.radiation;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

public abstract class EntityRadiationHandler {

    /**
     * Updates the radiation system, i.e. all worlds.
     * Doesn't need parameters because it governs the ENTIRE system.
     */
    public abstract void updateSystem();
    public abstract float getRadiation(World world, Entity entity);
    public abstract void setRadiation(World world, Entity entity, float rad);
    public abstract void debuff(World world, Entity entity, float rad);
    public abstract void logicEntityRad(World world, Entity entity, float rad);
    public abstract void clearSystem(World world,Entity entity);
    public abstract void handleEntityDestruction();
    /*
     * Proxy'd event handlers
     */

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {}
    public void onEntityTick(TickEvent.ServerTickEvent event) {}
    public void onEntityJoin(EntityJoinWorldEvent event) {}
    public void onEntityLeave(WorldEvent.Unload event) {}
    public void onEntityDeath(LivingDeathEvent event) {}
}