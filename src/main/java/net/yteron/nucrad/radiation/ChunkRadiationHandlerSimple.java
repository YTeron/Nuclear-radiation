package net.yteron.nucrad.radiation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.yteron.nucrad.init.Modblock;

import java.util.HashMap;
import java.util.Map;

public class ChunkRadiationHandlerSimple extends ChunkRadiationHandler{

    private Map<World, RadChunk> perWorld = new HashMap<>();
    private static final float maxRad = 100_000F;
    private static final String NBT_KEY_CHUNK_RADIATION = "chunk_radiation";

    @Override
    public void updateSystem() {
        for(Map.Entry<World, RadChunk> entry : perWorld.entrySet()) {

            Map<ChunkPos, Float> radiation = entry.getValue().radiation;
            Map<ChunkPos, Float> buff = new HashMap<>(radiation);
            radiation.clear();

            for (Map.Entry<ChunkPos, Float> chunk : buff.entrySet()) {

                if (chunk.getValue() == 0)
                    continue;

                ChunkPos coord = chunk.getKey();

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int type = Math.abs(i) + Math.abs(j);
                        float percent = type == 0 ? 0.6F : type == 1 ? 0.075F : 0.025F;
                        ChunkPos newCoord = new ChunkPos(coord.x+i,coord.z+j);

                        if(buff.containsKey(newCoord)) {
                            Float val = radiation.get(newCoord);
                            float rad = val == null ? 0 : val;
                            float newRad = rad + chunk.getValue() * percent;
                            radiation.put(newCoord, newRad);
                        } else {
                            radiation.put(newCoord, chunk.getValue() * percent);
                        }
                    }
                }
            }

        }
    }

    @Override
    public float getRadiation(World world, int x, int y, int z) {

        if(!world.isClientSide)
        {
            RadChunk radWorld = perWorld.get(world);
            if (radWorld == null) return 0;
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            Float rad = radWorld.radiation.get(chunkPos);
            if (rad == null) {
                return 0;
            }
//            System.out.println("📊 [ПОЛУЧЕНИЕ] Чанк " + chunkPos + " → " + String.format("%.2f", rad) + " рад");
            return rad;
        }
        return 0;
    }
//    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            PlayerEntity player = event.player;
//            float rad = getRadiationAtPlayer(player);
//            if (rad > 0.1f) {
//                System.out.println("☢️ [РАДИАЦИЯ] " + player.getName().getString() +
//                        " → " + String.format("%.2f", rad) + " рад");
//            }
//        }
//    }
    @Override
    public void setRadiation(World world, int x, int y, int z, float rad) {
        if(!world.isClientSide)
        {
            BlockPos pos = new BlockPos(x, 0, z);
            if (!world.isLoaded(pos)) return;

            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            RadChunk radWorld = perWorld.computeIfAbsent(world,
                    k -> new RadChunk());
            if (rad < 0.05f) {
                radWorld.radiation.remove(chunkPos);
            } else {
                radWorld.radiation.put(chunkPos,rad);
            }
            world.getChunk(chunkPos.x, chunkPos.z).markUnsaved();
        }
    }

    @Override
    public void incrementRad(World world, int x, int y, int z, float rad) {
        float current = getRadiation(world, x, y, z);
        setRadiation(world, x, y, z, current + rad);
    }

    @Override
    public void decrementRad(World world, int x, int y, int z, float rad) {
        float current = getRadiation(world, x, y, z);
        if (current-rad >0)
            setRadiation(world, x, y, z, current - rad);
        else
            setRadiation(world, x, y, z, 0);
    }

    @Override
    public void clearSystem(World world) {

    }

    @Override
    public void handleWorldDestruction() {

        int count = 10;
        int threshold = 10;
        int chunks = 5;

        for(Map.Entry<World, RadChunk> per : perWorld.entrySet()) {

            World world = per.getKey();
            RadChunk list = per.getValue();

            Object[] entries = list.radiation.entrySet().toArray();

            if(entries.length == 0)
                continue;

            for(int c = 0; c < chunks; c++) {

                Map.Entry<ChunkPos, Float> randEnt = (Map.Entry<ChunkPos, Float>) entries[world.random.nextInt(entries.length)];

                ChunkPos coords = randEnt.getKey();

                for(int i = 0; i < count; i++) {

                    if(randEnt == null || randEnt.getValue() < threshold)
                        continue;

                    if(world.getChunkSource().hasChunk(coords.x, coords.z)) {

                        if(world.random.nextInt(4) != 0)
                            continue;
                        for (int j = 0; j < 16; j++) {
                            for (int k = 0; k < 16; k++) {

                                int x = coords.x * 16 + j;
                                int z = coords.z * 16 + k;
                                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) - world.random.nextInt(2);

                                BlockPos pos = new BlockPos(x, y, z);
                                BlockState state = world.getBlockState(pos);
                                Block block = state.getBlock();
                                if (block == Blocks.DIRT ||
                                        block == Blocks.GRASS_BLOCK ||
                                        block == Blocks.SAND ||
                                        block == Blocks.TALL_GRASS ||
                                        state.getMaterial() == Material.LEAVES) {
                                    transformBlock(world, pos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //сохранния
    @Override
    public void receiveWorldLoad(WorldEvent.Load event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        perWorld.put(world, new RadChunk());
        System.out.println("🌍 [ЗАГРУЗКА МИРА] " + world.dimension().location() +
                " (ID: " + world.dimension().location() + ")");
    }

    @Override
    public void receiveWorldUnload(WorldEvent.Unload event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        RadChunk radWorld = perWorld.get(world);
        int chunkCount = radWorld != null ? radWorld.radiation.size() : 0;
        perWorld.remove(world);
        System.out.println("🌍 [ВЫГРУЗКА МИРА] " + world.dimension().location() +
                ", чанков с радиацией: " + chunkCount);
    }
    public void receiveWorldTick(TickEvent.ServerTickEvent event) {

    }

    public void receiveChunkLoad(ChunkDataEvent.Load event) {
        World world =(World) event.getWorld();
        if (world == null || world.isClientSide()) return;
        if (event.getChunk() == null) return;
        if(!world.isClientSide) {

            RadChunk radWorld = perWorld.computeIfAbsent(world,
                    k -> new RadChunk());

            float rad = event.getData().getFloat(NBT_KEY_CHUNK_RADIATION);
            if (rad > 0) {
                radWorld.radiation.put(event.getChunk().getPos(), rad);
                //System.out.println("мир "+world+" позиция "+event.getChunk().getPos()+" радиация " +String.format("%.2f", rad));
            }
        }
    }
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        World world =(World) event.getWorld();
        if(!world.isClientSide) {
            RadChunk radWorld = perWorld.get(world);

            if(radWorld != null) {
                Float val = radWorld.radiation.get(event.getChunk().getPos());
                float rad = val == null ? 0F : val;
                if (rad >-0.5F) {
                    event.getData().putFloat(NBT_KEY_CHUNK_RADIATION, rad);
//                    System.out.println("💾 [СОХРАНЕНИЕ ЧАНКА] " + event.getChunk().getPos() + " радиация " +
//                            String.format("%.2f", rad));
                }
            }
        }
    }
    public void receiveChunkUnload(ChunkEvent.Unload event) {
        World world =(World) event.getWorld();
        if(!world.isClientSide) {
            RadChunk radWorld = perWorld.get(world);

            if(radWorld != null) {
                radWorld.radiation.remove(event.getChunk().getPos());
            }
        }
    }
    public void transformBlock(World world, BlockPos pos) {
        if (world.isClientSide) return;
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK ) {
            world.setBlock(pos, Modblock.DEAD_EARH.get().defaultBlockState(), 3);
        } else if (block == Blocks.SAND) {
            world.setBlock(pos, Modblock.DEAD_SAND.get().defaultBlockState(), 3);
        }else if (state.getMaterial() == Material.LEAVES) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }

    }
    public static class RadChunk{
        public Map<ChunkPos, Float> radiation = new HashMap<>();
    }
    public float getRadiationAtPlayer(PlayerEntity player) {
        if (player == null || player.isDeadOrDying()) return 0;
        if (player.level == null) return 0;
        return getRadiation(player.level,
                (int) player.getX(),
                (int) player.getY(),
                (int) player.getZ());
    }
}
