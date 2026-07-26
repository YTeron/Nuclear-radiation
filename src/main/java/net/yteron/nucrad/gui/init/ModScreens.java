package net.yteron.nucrad.gui.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.yteron.nucrad.gui.screen.LightningChannelerScreen;

public class ModScreens {

    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // ✅ Регистрируем экран для канала молний
            ScreenManager.register(ModContainers.LIGHTNING_CHANNELER_CONTAINER.get(),
                    LightningChannelerScreen::new);
        });
    }
}