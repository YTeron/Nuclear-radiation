package net.yteron.nucrad.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.entity.custom.BuffZombieEntity;
import net.yteron.nucrad.entity.custom.LargoEntity;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, NucRad.MOD_ID);

    public static final RegistryObject<EntityType<BuffZombieEntity>> BUFF_ZOMBIE =
            ENTITY_TYPES.register("buff_zombie",
                    () -> EntityType.Builder.of(BuffZombieEntity::new,
                                    EntityClassification.MONSTER).sized(1f, 3f)
                            .build(new ResourceLocation(NucRad.MOD_ID, "buff_zombie").toString()));

    public static final RegistryObject<EntityType<LargoEntity>> LARGO =
            ENTITY_TYPES.register("largo",
                    () -> EntityType.Builder.of(LargoEntity::new,
                                    EntityClassification.MONSTER).sized(2f, 0.5f)
                            .build(new ResourceLocation(NucRad.MOD_ID, "largo").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
