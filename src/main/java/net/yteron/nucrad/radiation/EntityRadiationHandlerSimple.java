package net.yteron.nucrad.radiation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.fixes.EntityUUID;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static sun.audio.AudioPlayer.player;

public class EntityRadiationHandlerSimple extends EntityRadiationHandler{
    private ChunkRadiationHandlerSimple chunkHandler;
    private int ticks =0;
    private Map<World, RadEntity> perEntity = new HashMap<>();
    private static final float maxRad = 100_000F;
    private static final String NBT_KEY_ENTITY_RADIATION = "entity_radiation";

    public EntityRadiationHandlerSimple(ChunkRadiationHandlerSimple chunkHandler) {
        this.chunkHandler = chunkHandler;
    }
    public EntityRadiationHandlerSimple() {}

    @Override
    public void updateSystem() {
        for(Map.Entry<World, EntityRadiationHandlerSimple.RadEntity> entry : perEntity.entrySet()) {

            Map<UUID, Float> radiation = entry.getValue().radiation;
            Map<UUID, Float> buff = new HashMap<>(radiation);
            radiation.clear();
            World world = entry.getKey();
            for (Map.Entry<UUID, Float> entity : buff.entrySet()) {
                if (entity == null) continue;
                float rad = entity.getValue();
                if (!(world instanceof ServerWorld)) continue;
                ServerWorld serverWorld = (ServerWorld) world;
                Entity entity1 = serverWorld.getEntity(entity.getKey());
                if (entity1 instanceof LivingEntity) {
                    float radFormChunk = chunkHandler.getRadiation(world,
                            (int) entity1.getX(),
                            (int) entity1.getY(),
                            (int) entity1.getZ());
                    float newRad = rad + radFormChunk * 0.5f;
                    radiation.put(entity.getKey(), newRad);
                    System.out.println("uuid " + entity.getKey().toString().substring(0, 6) + " тип " + entity1.getType() + " радиация " + newRad);
                    logicEntityRad(world, entity1, newRad);

                }
                if (entity1 instanceof PlayerEntity) {
                    PlayerEntity player =(PlayerEntity) entity1;
                    if (!player.isCreative() && !player.isSpectator()) {
                        float radFormChunk = chunkHandler.getRadiation(world,
                                (int) entity1.getX(),
                                (int) entity1.getY(),
                                (int) entity1.getZ());
                        float newRad = rad + radFormChunk * 0.5f;

                        if (rad > 0.1f) {
                            System.out.println("☢️ [РАДИАЦИЯ] " + player.getName().getString() +
                                    " → " + String.format("%.2f", newRad) + " рад");
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getRadiation(World world, Entity entity) {
        if (world.isClientSide||entity ==null) return 0;
        float rad = chunkHandler.getRadiation(entity.level,
                (int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ());
        return rad;

    }

    @Override
    public void setRadiation(World world, Entity entity, float rad) {
        if (world.isClientSide||entity ==null|| rad<0F) return;
        UUID uuid =entity.getUUID();
        RadEntity radWorld = perEntity.computeIfAbsent(world,
                k -> new RadEntity());
        radWorld.radiation.put(uuid, rad);
        entity.getPersistentData().putFloat(NBT_KEY_ENTITY_RADIATION, rad);
    }

    @Override
    public void debuff(World world, Entity entity, float rad) {

    }

    @Override
    public void logicEntityRad(World world, Entity entity, float rad) {
        if (world.isClientSide|| entity == null) return;
        ticks++;
        LivingEntity living = (LivingEntity) entity;
        if (ticks >60) {
            if (rad >=20&& rad<=30&&entity instanceof PlayerEntity) {
                living.addEffect(new EffectInstance(Effects.HEAL, 20, 0));
                living.addEffect(new EffectInstance(Effects.HUNGER, 20, 0));
            }
            if (rad >=100f) {
                living.addEffect(new EffectInstance(Effects.CONFUSION, 60, 0));
                living.addEffect(new EffectInstance(Effects.POISON, 60, 0));

            }
            if (rad >=200f) {
                living.addEffect(new EffectInstance(Effects.WITHER, 100, 0));
                living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 0));
                living.hurt(new DamageSource("radiation"), rad*0.1f);
            }
            if (rad >=1000f) {
                living.hurt(new DamageSource("radiation"), rad);
            }
            if (ticks >70)ticks =0;
        }
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            PlayerEntity player = event.player;
//            float rad = chunkHandler.getRadiationAtPlayer(player);
//            if (rad > 0.1f) {
//                System.out.println("☢️ [РАДИАЦИЯ] " + player.getName().getString() +
//                        " → " + String.format("%.2f", rad) + " рад");
//            }
//        }
    }
    @Override
    public void onEntityTick(TickEvent.ServerTickEvent event) {

    }
    @Override
    public void onEntityJoin(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (entity == null || world.isClientSide) return;

        UUID entityUUID = entity.getUUID();
        RadEntity radEntity = perEntity.computeIfAbsent(world,
                k -> new RadEntity());

        CompoundNBT persistentData = entity.getPersistentData();

        if (persistentData.contains(NBT_KEY_ENTITY_RADIATION)) {
            float rad = persistentData.getFloat(NBT_KEY_ENTITY_RADIATION);
            System.out.println("📂 [ЗАГРУЗКА] " +
                    entityUUID.toString().substring(0, 6) +
                    " → ☢️ " + String.format("%.2f", rad) + " рад");

            radEntity.radiation.put(entityUUID, rad);
            persistentData.remove(NBT_KEY_ENTITY_RADIATION);
        } else radEntity.radiation.put(entityUUID, 0f);
    }
    @Override
    public void onEntityLeave(WorldEvent.Unload event) {
        for(Map.Entry<World, EntityRadiationHandlerSimple.RadEntity> entry : perEntity.entrySet()) {
            System.out.println(" выгрузка ");
            World world = (World) event.getWorld();
            if (world.isClientSide) return;
            ServerWorld serverWorld = (ServerWorld) world;
            Map<UUID, Float> entity1 = entry.getValue().radiation;
            for (Map.Entry<UUID, Float> entity2 : entity1.entrySet()) {
                Entity entity = serverWorld.getEntity(entity2.getKey());
                if (entity instanceof LivingEntity) {
                    if (entity == null||!entity.isAlive()) continue;
                    if(entity2.getValue()>0f) {
                        entity.getPersistentData().putFloat(NBT_KEY_ENTITY_RADIATION, entity2.getValue());
                        System.out.println("entity " + entity.getUUID().toString().substring(0, 6) + " " + entity2.getValue());
                    }
                }
            }
        }

    }
    @Override
    public void onEntityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        World world = entity.level;
        if (world.isClientSide) return;

        UUID entityUUID = entity.getUUID();
        RadEntity radWorld = perEntity.get(world);

        if (radWorld != null) {
            Float removed = radWorld.radiation.remove(entityUUID);
            if (removed != null && removed > 0f) {
                System.out.println("💀 [СМЕРТЬ] " +
                        entityUUID.toString().substring(0, 6) +
                        " → ☢️ " + String.format("%.2f", removed) + " рад (удалено)");
            }
            entity.getPersistentData().remove(NBT_KEY_ENTITY_RADIATION);
        }
    }

    @Override
    public void clearSystem(World world, Entity entity) {

    }

    @Override
    public void handleEntityDestruction() {

    }
    public static class RadEntity{
        public Map<UUID, Float> radiation = new HashMap<>();
    }
}
