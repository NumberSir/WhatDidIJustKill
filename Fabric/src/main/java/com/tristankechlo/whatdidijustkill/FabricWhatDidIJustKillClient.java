package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import com.tristankechlo.whatdidijustkill.fabric_command.FabricWhatDidIJustKillCommand;
import com.tristankechlo.whatdidijustkill.network.FabricPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class FabricWhatDidIJustKillClient implements ClientModInitializer {

    private static KeyMapping KEYMAPPING;

    @Override
    public void onInitializeClient() {
        ConfigManager.loadAndVerifyConfig();

        // register keybindings and listener
        KEYMAPPING = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.whatdidijustkill.toggle_toasts", GLFW.GLFW_KEY_V, "key.categories.ui"));
        ClientTickEvents.END_CLIENT_TICK.register(FabricWhatDidIJustKillClient::keyBindingListener);

        // register packet listener
        ClientPlayNetworking.registerGlobalReceiver(WhatDidIJustKill.ENTITY_KILLED, FabricPacketHandler::handleEntityKilled);
        ClientPlayNetworking.registerGlobalReceiver(WhatDidIJustKill.PLAYER_KILLED, FabricPacketHandler::handlePlayerKilled);

        // register mod command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            FabricWhatDidIJustKillCommand.register(dispatcher);
        });
    }

    private static void keyBindingListener(Minecraft client) {
        while (KEYMAPPING.consumeClick() && Screen.hasControlDown()) {
            ToastHandler.toggleVisibility(client);
        }
    }

}
