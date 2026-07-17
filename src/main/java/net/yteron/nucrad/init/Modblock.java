package net.yteron.nucrad.init;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.block.block.*;
import net.yteron.nucrad.block.machine.ConcereteMixer;
import net.yteron.nucrad.block.machine.LightningChannelerBlock;
import net.yteron.nucrad.block.structurblock.Lamp;

public class Modblock {
    public static final DeferredRegister<Block> REGISTRY;
    public static final RegistryObject<Block> BARBEDWIRE;
    public static final RegistryObject<Block> WALL_WIRE;
    public static final RegistryObject<Block> LAMP;
    public static final RegistryObject<Block> LIGHTNING_CHANNELER;
    public static final RegistryObject<Block> IRONFENCEE;
    public static final RegistryObject<Block> CONCERETE_MIXER;
    public static final RegistryObject<Block> DEAD_SAND;
    public static final RegistryObject<Block> DEAD_EARH;
    public static final RegistryObject<Block> CONCERETE_DEFEAT;

    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, NucRad.MOD_ID);
        BARBEDWIRE = REGISTRY.register("barbedwire",()-> new BarbedWire());
        LIGHTNING_CHANNELER = REGISTRY.register("li",()-> new LightningChannelerBlock());
        WALL_WIRE = REGISTRY.register("wall_provolka",()-> new WallWire());
        LAMP = REGISTRY.register("lamp",()-> new Lamp());
        IRONFENCEE = REGISTRY.register("ironfencee",()-> new IronFencee());
        DEAD_SAND = REGISTRY.register("dead_sand",()-> new DeadSand());
        DEAD_EARH = REGISTRY.register("dead_earth",()-> new DeadEarth());
        CONCERETE_MIXER = REGISTRY.register("concrete_mixer",()-> new ConcereteMixer());
        CONCERETE_DEFEAT = REGISTRY.register("concerete_defeat",()-> new ConcereteDefeat());
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
