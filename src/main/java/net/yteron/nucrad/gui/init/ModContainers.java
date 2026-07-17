package net.yteron.nucrad.gui.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.gui.container.LightningChannelerContainer;

public class ModContainers {

    // ✅ Добавляем final
    public static final DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, NucRad.MOD_ID);

    // ✅ Регистрация контейнера для канала молний
    public static final RegistryObject<ContainerType<LightningChannelerContainer>> LIGHTNING_CHANNELER_CONTAINER =
            CONTAINERS.register("lightning_channeler",
                    () -> IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        // ✅ Исправлено: inv.player.level вместо inv.player.getEntity()
                        return new LightningChannelerContainer(windowId, inv.player.level, pos, inv, inv.player);
                    })
            );

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}