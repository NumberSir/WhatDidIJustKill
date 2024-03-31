package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeWhatDidIJustKillClient {

    public static final Lazy<KeyMapping> KEYMAPPING = Lazy.of(() -> new KeyMapping("key.whatdidijustkill.toggle_toasts", GLFW.GLFW_KEY_V, "key.categories.ui"));

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ConfigManager.loadAndVerifyConfig();
        ClientRegistry.registerKeyBinding(KEYMAPPING.get());
    }

    @Mod.EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class ClientTick {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (KEYMAPPING.get().consumeClick() && Screen.hasControlDown()) {
                    ToastHandler.toggleVisibility(Minecraft.getInstance());
                }
            }
        }
    }

}
