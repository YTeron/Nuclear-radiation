package net.yteron.nucrad.radiation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityRadiationHandlerSimple extends EntityRadiationHandler{
    private Map<World, EntityRadiationHandlerSimple.SimpleRadiationPerEntity> perEntity = new HashMap<>();
    private static final float maxRad = 100_000F;
    private ChunkRadiationHandlerSimple chunkHandler = new ChunkRadiationHandlerSimple();
    private static final String NBT_KEY_ENTITY_RADIATION = "hfr_entity_simple_radiation";
    @Override
    public void updateSystem() {
        System.out.println("🔄 Обновление системы радиации...");


        for(Map.Entry<World, SimpleRadiationPerEntity> entry : perEntity.entrySet()) {
            Map<UUID, Float> radiation = entry.getValue().radiation;
            Map<UUID, Float> buff = new HashMap<>(radiation);
            World world = entry.getKey();
            if (!(world instanceof ServerWorld)) {
                System.out.println("no world");
                continue;
            }
            radiation.clear();
            for(Map.Entry<UUID, Float> entitymap : buff.entrySet()){
                System.out.println("Yes");
                UUID uuid = entitymap.getKey();
                ServerWorld serverWorld = (ServerWorld) world;
                Entity entity = serverWorld.getEntity(uuid);
                System.out.println("entity: "+entity);
                float rad =chunkHandler.getRadiationAtEntity(entity);
                System.out.println("Yes2 " + rad);
                radiation.put(uuid, rad);
            }
        }
    }
    @Override
    public float getRadiation(Entity entity,World world) {
        EntityRadiationHandlerSimple.SimpleRadiationPerEntity radWorld = perEntity.get(world);

        if(radWorld != null) {
            UUID entityUUID = entity.getUUID();
            Float rad = radWorld.radiation.get(entityUUID);
            return rad == null ? 0F : MathHelper.clamp(rad, 0, maxRad);
        }
        return 0;
    }

    @Override
    public void setRadiation(Entity entity,World world, float rad) {
        EntityRadiationHandlerSimple.SimpleRadiationPerEntity radWorld = perEntity.get(world);

        if(radWorld != null) {
            if(entity.isAlive()) {
                UUID entityUUID = entity.getUUID();

                radWorld.radiation.put(entityUUID, MathHelper.clamp(rad, 0, maxRad));
            }
        }
    }

    @Override
    public void clearSystem(Entity entity,World world) {
        EntityRadiationHandlerSimple.SimpleRadiationPerEntity radWorld = perEntity.get(world);
        if(radWorld != null) {
            radWorld.radiation.clear();
        }
    }
    public void entityJoin(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if(!world.isClientSide&& entity!=null) {
            SimpleRadiationPerEntity radWorld = perEntity.computeIfAbsent(world,
                    k -> new SimpleRadiationPerEntity());
            UUID entityUUID = entity.getUUID();
            String uuidString = entityUUID.toString();

            if (entity.getPersistentData().contains(uuidString))
            {
                float rad = entity.getPersistentData().getFloat(uuidString);
                radWorld.radiation.put(entityUUID, rad);
                System.out.println("Загружена: " + uuidString.substring(0, 8) +
                        " Рад: " + rad);
            }
            else
            {
                entity.getPersistentData().putFloat(entityUUID.toString(), 0);
                radWorld.radiation.put(entityUUID, 0f);
                System.out.println("добавлен "+ entityUUID.toString().substring(0, 8));
            }

        }
    }
    @Override
    public void entityLeave(EntityLeaveWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (!world.isClientSide && entity != null) {
            UUID entityUUID = entity.getUUID();
            String uuidString = entityUUID.toString();

            SimpleRadiationPerEntity radWorld = perEntity.get(world);
            if (radWorld != null) {
                Float currentRad = radWorld.radiation.get(entityUUID);
                if (currentRad != null) {
                    entity.getPersistentData().putFloat(uuidString, currentRad);
                    radWorld.radiation.remove(entityUUID);
                }
            }
        }
    }
    @Override
    public void onEntityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        EntityType<?> entityType = entity.getType();
        World world = entity.level;
        UUID uuid = entity.getUUID();
        if (!world.isClientSide) {
            SimpleRadiationPerEntity radWorld = perEntity.get(world);
            if (radWorld != null) {

                radWorld.radiation.remove(uuid);
                System.out.println("UUID "+uuid.toString().substring(0, 8)+" смерть сущности " + entityType);
            }
        }
    }

    @Override
    public void heartDamage(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        World world = entity.level;

        if (world.isClientSide) return;

        float radiation = getRadiation(entity, world);
        if (radiation > 0) {
            event.setAmount(event.getAmount() + radiation * 100);
        }
    }
    public static class SimpleRadiationPerEntity {

        public Map<UUID, Float> radiation = new HashMap<>();
    }
}
