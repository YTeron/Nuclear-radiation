package net.yteron.nucrad.events;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.entity.ModEntityTypes;
import net.yteron.nucrad.entity.custom.BuffZombieEntity;
import net.yteron.nucrad.init.ModSpawnEggItem;


@Mod.EventBusSubscriber(modid = NucRad.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BUFF_ZOMBIE.get(), BuffZombieEntity.setCustomAttributes().build());
        }
    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}
