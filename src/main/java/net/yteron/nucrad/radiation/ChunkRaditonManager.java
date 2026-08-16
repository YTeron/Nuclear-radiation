package net.yteron.nucrad.radiation;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class ChunkRaditonManager {
    public static ChunkRadiationHandler proxy = new ChunkRadiationHandlerSimple();
    public static EntityRadiationHandler proxyY;
    static {
        // ✅ ПРАВИЛЬНО: передаем ChunkRadiationHandler
        if (proxy instanceof ChunkRadiationHandlerSimple) {
            proxyY = new EntityRadiationHandlerSimple((ChunkRadiationHandlerSimple) proxy);
            System.out.println("✅ Обработчики инициализированы");
        }

    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        proxy.receiveWorldLoad(event);
    }
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        proxy.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        proxy.receiveChunkLoad(event);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        proxy.receiveChunkSave(event);
    }
    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        proxy.receiveChunkUnload(event);
    }
//    @SubscribeEvent
//    public void onPlayerTickc(TickEvent.PlayerTickEvent event) {
//        proxy.onPlayerTick(event);
//    }

    //----------------------------- Entity
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {proxyY.onPlayerTick(event);}
    @SubscribeEvent
    public void onEntityTick(TickEvent.ServerTickEvent event) {proxyY.onEntityTick(event);}
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {proxyY.onEntityJoin(event);}
    @SubscribeEvent
    public void onEntityLeave(WorldEvent.Unload event) {
        proxyY.onEntityLeave(event);
    }
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {proxyY.onEntityDeath(event);}

    int eggTimer = 0;

    @SubscribeEvent
    public void updateSystem(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            eggTimer++;
            if (eggTimer >= 20) {
                proxy.updateSystem();
                proxyY.updateSystem();
                eggTimer = 0;
            }
            proxy.handleWorldDestruction();
            proxy.receiveWorldTick(event);
        }
    }
}
