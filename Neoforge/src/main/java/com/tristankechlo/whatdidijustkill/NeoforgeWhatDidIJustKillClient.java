package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NeoforgeWhatDidIJustKillClient {

    public static final Lazy<KeyMapping> KEYMAPPING = Lazy.of(() -> new KeyMapping("key.whatdidijustkill.toggle_toasts", GLFW.GLFW_KEY_V, "key.categories.ui"));

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ConfigManager.loadAndVerifyConfig();
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KEYMAPPING.get());
    }

    @EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    private static class ClientTick {

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            while (KEYMAPPING.get().consumeClick() && Screen.hasControlDown()) {
                ToastHandler.toggleVisibility(Minecraft.getInstance());
            }
        }
    }

}
