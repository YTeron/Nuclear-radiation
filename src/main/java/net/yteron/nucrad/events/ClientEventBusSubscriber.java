package net.yteron.nucrad.events;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.init.Modblock;


@Mod.EventBusSubscriber(modid = NucRad.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Регистрируем рендер как у стекла
        RenderTypeLookup.setRenderLayer(Modblock.IRONFENCEE.get(), RenderType.translucent());
    }
}