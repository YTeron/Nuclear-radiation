package net.yteron.nucrad.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NucRad.MOD_ID);
    public static final RegistryObject<Item> METALP;
    public static final RegistryObject<Item> METAL_WEB;
    public static final RegistryObject<Item> BARBEDWIRE;
    public static final RegistryObject<Item> WALL_WIRE;
    public static final RegistryObject<Item> LAMP;

    static {
        // Точный порядок регистрации
        METALP = ITEMS.register("a_metalp",
                () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));

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
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus); // <-- Исправлено: убраны лишние скобки
    }
}