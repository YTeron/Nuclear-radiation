package net.yteron.nucrad.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.block.block.IronFencee;
import net.yteron.nucrad.entity.ModEntityTypes;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NucRad.MOD_ID);
    public static final RegistryObject<Item> METALP;
    public static final RegistryObject<Item> METAL_WEB;
    public static final RegistryObject<Item> CONCP;
    public static final RegistryObject<Item> LIGHT;
    public static final RegistryObject<Item> BARBEDWIRE;
    public static final RegistryObject<Item> WALL_WIRE;
    public static final RegistryObject<Item> IRONFENCEE;
    public static final RegistryObject<Item> LAMP;
    public static final RegistryObject<Item> CONCERETE_MIXER;
    public static final RegistryObject<Item> CONCERETE_DEFEAT;
    public static final RegistryObject<Item> CONCERETE_IRON_DEFEAT;
    public static final RegistryObject<Item> DEAD_SAND;
    public static final RegistryObject<Item> DEAD_EARTH;
    public static final RegistryObject<Item> BUFF_ZOMBIE_SPAWN_EGG;
    public static final RegistryObject<Item> LARGO;
    static {
        // Точный порядок регистрации
        METALP = ITEMS.register("a_metalp",
                () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));
        CONCP = ITEMS.register("a_sandconcerete",
                () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));
        LIGHT = ITEMS.register("aaaaaa",
                () -> new BlockItem(Modblock.LIGHTNING_CHANNELER.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));

        METAL_WEB = ITEMS.register("ab_metalweb",
                () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));

        BARBEDWIRE = ITEMS.register("kprovolkaf",
                () -> new BlockItem(Modblock.BARBEDWIRE.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        WALL_WIRE = ITEMS.register("wall_provolka",
                () -> new BlockItem(Modblock.WALL_WIRE.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        LAMP = ITEMS.register("lamp",
                () -> new BlockItem(Modblock.LAMP.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        IRONFENCEE = ITEMS.register("ironfenceei",
                () -> new BlockItem(Modblock.IRONFENCEE.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));

        DEAD_SAND = ITEMS.register("dead_sand",
                () -> new BlockItem(Modblock.DEAD_SAND.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        DEAD_EARTH = ITEMS.register("dead_earth",
                () -> new BlockItem(Modblock.DEAD_EARH.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        CONCERETE_MIXER = ITEMS.register("concrete_mixer",
                () -> new BlockItem(Modblock.CONCERETE_MIXER.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));

        CONCERETE_DEFEAT = ITEMS.register("concerete_defeat",
                () -> new BlockItem(Modblock.CONCERETE_DEFEAT.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        CONCERETE_IRON_DEFEAT = ITEMS.register("concerete_iron_defeat",
                () -> new BlockItem(Modblock.CONCERETE_IRON_DEFEAT.get(),
                        new Item.Properties().tab(ModTabs.NUCRAD)));


        BUFF_ZOMBIE_SPAWN_EGG = ITEMS.register("buff_zombie_spawn_egg",
                () -> new ModSpawnEggItem(ModEntityTypes.BUFF_ZOMBIE,
                        0x666666,  // Цвет 1
                        0xAAAAAA,  // Цвет 2
                        new Item.Properties().tab(ModTabs.NUCRAD)));
        LARGO = ITEMS.register("largo_spawn_egg",
                () -> new ModSpawnEggItem(ModEntityTypes.LARGO,
                        0x666666,  // Цвет 1
                        0xAAAAAA,  // Цвет 2
                        new Item.Properties().tab(ModTabs.NUCRAD)));

    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus); // <-- Исправлено: убраны лишние скобки
    }
}