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

    public static final RegistryObject<Item> BARBEDWIRE = ITEMS.register("kprovolkaf",
            () -> new BlockItem(Modblock.BARBEDWIRE.get(),
                    new Item.Properties().tab(ModTabs.NUCRAD)));

    // Исправлено: ItemGroup вместо IemGroup, добавлена точка с запятой
    public static final RegistryObject<Item> METALP = ITEMS.register("metalp",
            () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));
    public static final RegistryObject<Item> METAL_WEB = ITEMS.register("metalweb",
            () -> new Item(new Item.Properties().tab(ModTabs.NUCRAD)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus); // <-- Исправлено: убраны лишние скобки
    }
}