package net.yteron.nucrad.gui.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.gui.tileentity.LightningChannelerTile;
import net.yteron.nucrad.init.Modblock;

public class ModTileEntities {

    // ✅ final
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, NucRad.MOD_ID);

    // ✅ final, используем Builder.of() вместо Builder.create()
    public static final RegistryObject<TileEntityType<LightningChannelerTile>> LIGHTNING_CHANNELER_TILE =
            TILE_ENTITIES.register("lightning_channeler_tile",
                    () -> TileEntityType.Builder.of(
                            LightningChannelerTile::new,
                            Modblock.LIGHTNING_CHANNELER.get()
                    ).build(null)
            );
    public static final RegistryObject<TileEntityType<ConcereteMixerTile>> CONCRETE_MIXER_TILE =
            TILE_ENTITIES.register("conc_gui",
                    () -> TileEntityType.Builder.of(
                            ConcereteMixerTile::new,
                            Modblock.CONCERETE_MIXER.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}