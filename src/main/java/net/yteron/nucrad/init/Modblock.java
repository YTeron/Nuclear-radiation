package net.yteron.nucrad.init;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.block.BarbedWire;
import net.yteron.nucrad.block.Lamp;
import net.yteron.nucrad.block.WallWire;

public class Modblock {
    public static final DeferredRegister<Block> REGISTRY;
    public static final RegistryObject<Block> BARBEDWIRE;
    public static final RegistryObject<Block> WALL_WIRE;
    public static final RegistryObject<Block> LAMP;
    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, NucRad.MOD_ID);
        BARBEDWIRE = REGISTRY.register("barbedwire",()-> new BarbedWire());
        WALL_WIRE = REGISTRY.register("wall_provolka",()-> new WallWire());
        LAMP = REGISTRY.register("lamp",()-> new Lamp());
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
