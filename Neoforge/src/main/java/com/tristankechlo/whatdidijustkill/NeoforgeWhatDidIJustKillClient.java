package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.client.ToastRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@Mod.EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NeoforgeWhatDidIJustKillClient {

    @SubscribeEvent
    public static void renderGui(final RenderGuiEvent.Post event) {
        ToastRenderer.render(event.getGuiGraphics(), event.getPartialTick());
    }

}
