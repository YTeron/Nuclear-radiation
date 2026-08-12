    package net.yteron.nucrad.radiation;
    
    import net.minecraft.block.Block;
    import net.minecraft.block.BlockState;
    import net.minecraft.block.Blocks;
    import net.minecraft.block.material.Material;
    import net.minecraft.entity.Entity;
    import net.minecraft.nbt.CompoundNBT;
    import net.minecraft.network.datasync.EntityDataManager;
    import net.minecraft.util.math.BlockPos;
    import net.minecraft.util.math.ChunkPos;
    import net.minecraft.util.math.MathHelper;
    import net.minecraft.world.IWorld;
    import net.minecraft.world.World;
    import net.minecraft.world.chunk.IChunk;
    import net.minecraft.world.gen.Heightmap;
    import net.minecraft.world.server.ServerWorld;
    import net.minecraftforge.event.entity.EntityEvent;
    import net.minecraftforge.event.world.ChunkDataEvent;
    import net.minecraftforge.event.world.ChunkEvent;
    import net.minecraftforge.event.world.WorldEvent;
    
    import java.util.HashMap;
    import java.util.Map;
    
    import net.yteron.nucrad.init.Modblock;
    
    public class ChunkRadiationHandlerSimple extends ChunkRadiationHandler{
    
        private Map<World, SimpleRadiationPerWorld> perWorld = new HashMap<>();
        private static final float maxRad = 100_000F;
        private static final String NBT_KEY_CHUNK_RADIATION = "hfr_simple_radiation";
    
        @Override
        public float getRadiation(World world, int x, int y, int z) {
            SimpleRadiationPerWorld radWorld = perWorld.get(world);
    
            if(radWorld != null) {
                BlockPos pos = new BlockPos(x, 0, z);
                if(world.isLoaded(pos)) {
                    ChunkPos coords = new ChunkPos(x >> 4, z >> 4);

                    Float rad = radWorld.radiation.get(coords);
                    world.getChunk(coords.x, coords.z).markUnsaved();

                    return rad == null ? 0F : MathHelper.clamp(rad, 0, maxRad);
                }
            }
            return 0;
        }
    
        @Override
        public void setRadiation(World world, int x, int y, int z, float rad) {
            SimpleRadiationPerWorld radWorld = perWorld.get(world);
    
            if(radWorld != null) {
                BlockPos pos = new BlockPos(x, 0, z);
                if(world.isLoaded(pos)) {
    
                    ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
                    radWorld.radiation.put(coords, MathHelper.clamp(rad, 0, maxRad));
    
                    world.getChunk(coords.x, coords.z).markUnsaved();
                }
            }
        }
    
        @Override
        public void incrementRad(World world, int x, int y, int z, float rad) {
            setRadiation(world, x, y, z, getRadiation(world, x, y, z) + rad);
        }
    
        @Override
        public void decrementRad(World world, int x, int y, int z, float rad) {
            setRadiation(world, x, y, z, Math.max(getRadiation(world, x, y, z) - rad, 0));
        }
        @Override
        public void updateSystem() {

            for(Map.Entry<World, SimpleRadiationPerWorld> entry : perWorld.entrySet()) {

                Map<ChunkPos, Float> radiation = entry.getValue().radiation;
                Map<ChunkPos, Float> buff = new HashMap<>(radiation);
                radiation.clear();

                for (Map.Entry<ChunkPos, Float> chunk : buff.entrySet()) {

                    if (chunk.getValue() == 0||chunk.getValue() < 0.01f)
                        continue;

                    ChunkPos coord = chunk.getKey();
    
                    for(int i = -1; i <= 1; i++) {
                        for(int j = -1; j<= 1; j++) {
    
                            int type = Math.abs(i) + Math.abs(j);
                            float percent = type == 0 ? 0.6F : type == 1 ? 0.075F : 0.025F;
                            ChunkPos newCoord = new ChunkPos(coord.x + i, coord.z + j);
    
                            if(buff.containsKey(newCoord)) {
                                Float val = radiation.get(newCoord);
    
                                float rad = val == null ? 0 : val;
                                float newRad = rad + chunk.getValue() * percent;

                                newRad = MathHelper.clamp(0F, newRad * 0.99F - 0.05F, maxRad);
                                radiation.put(newCoord, newRad);
                            } else {
                                radiation.put(newCoord, chunk.getValue() * percent);
                            }
    
    //                        float rad = radiation.get(newCoord);
    //                        if(rad > 100) {
    //
    //                            int x = coord.x * 16 + world.random.nextInt(16);
    //                            int z = coord.z * 16 + world.random.nextInt(16);
    //                            int y = world.getHeightValue(x, z) + world.random.nextInt(5);
    //
    //                            CompoundNBT data = new CompoundNBT ();
    //
    //                            data.putString("type", "radFog");
    //                            data.putDouble("posX", x);
    //                            data.putDouble("posY", y);
    //                            data.putDouble("posZ", z);
    //                            MainRegistry.proxy.effectNT(data);
    //                        }
                        }
                    }
                }
            }
        }
    
        @Override
        public void clearSystem(World world) {
            SimpleRadiationPerWorld radWorld = perWorld.get(world);
            if(radWorld != null) {
                radWorld.radiation.clear();
            }
        }
    
        @Override
        public void receiveWorldLoad(WorldEvent.Load event) {
            World world = (World) event.getWorld();
            if(!world.isClientSide)
                perWorld.put(world, new SimpleRadiationPerWorld());
        }
    
        @Override
        public void receiveWorldUnload(WorldEvent.Unload event) {
            World world = (World) event.getWorld();
            if(!world.isClientSide)
                perWorld.remove(world);
        }
    
        @Override
        public void receiveChunkLoad(ChunkDataEvent.Load event) {
            IWorld world = event.getWorld();
            if (world == null || world.isClientSide()) return;
            if (event.getChunk() == null) return;
            World worldObj = (World) world;

            SimpleRadiationPerWorld radWorld = perWorld.computeIfAbsent(worldObj,
                    k -> new SimpleRadiationPerWorld());

            float rad = event.getData().getFloat(NBT_KEY_CHUNK_RADIATION);
            if (rad > 0) {
                radWorld.radiation.put(event.getChunk().getPos(), rad);
                //System.out.println("мир "+worldObj+" позиция "+event.getChunk().getPos()+" радиация " + rad);
            }
        }
    
        @Override
        public void receiveChunkSave(ChunkDataEvent.Save event) {
            World world = (World) event.getWorld();
            if(!world.isClientSide) {
                SimpleRadiationPerWorld radWorld = perWorld.get(world);
    
                if(radWorld != null) {
                    Float val = radWorld.radiation.get(event.getChunk().getPos());
                    float rad = val == null ? 0F : val;
                    event.getData().putFloat(NBT_KEY_CHUNK_RADIATION, rad);
                    //System.out.println("мир сох "+world+" позиция сох "+event.getChunk().getPos()+" радиация сох " + rad);
                }
            }
        }
    
        @Override
        public void receiveChunkUnload(ChunkEvent.Unload event) {
            World world = (World) event.getWorld();
            if(!world.isClientSide) {
                SimpleRadiationPerWorld radWorld = perWorld.get(world);
    
                if(radWorld != null) {
                    radWorld.radiation.remove(event.getChunk().getPos());
                }
            }
        }
        @Override
        public void handleWorldDestruction() {
    
            int count = 10;
            int threshold = 10;
            int chunks = 5;

            for(Map.Entry<World, SimpleRadiationPerWorld> per : perWorld.entrySet()) {
    
                World world = per.getKey();
                SimpleRadiationPerWorld list = per.getValue();
    
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
    
                            for(int a = 0; a < 16; a++) {
                                for(int b = 0; b < 16; b++) {
    
                                    if(world.random.nextInt(4) != 0)
                                        continue;
    
                                    int x = coords.x * 16 + a;
                                    int z = coords.z * 16 + b;
                                    int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) - world.random.nextInt(2);
    
                                    BlockPos pos = new BlockPos(x, y, z);
                                    BlockState state = world.getBlockState(pos);
                                    Block block = state.getBlock();
                                    if(block == Blocks.DIRT ||
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
    public static class SimpleRadiationPerWorld {

        public Map<ChunkPos, Float> radiation = new HashMap<>();
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
    public float getRadiationAtEntity(Entity entity) {
        World world = entity.level;
        int x = (int) entity.getX();
        int z = (int) entity.getZ();
        return getRadiation(world, x, 0, z);
    }
}
