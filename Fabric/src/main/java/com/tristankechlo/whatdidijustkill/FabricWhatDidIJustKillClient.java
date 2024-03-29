package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import com.tristankechlo.whatdidijustkill.fabric_command.FabricWhatDidIJustKillCommand;
import com.tristankechlo.whatdidijustkill.network.FabricPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricWhatDidIJustKillClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.loadAndVerifyConfig();

        ClientPlayNetworking.registerGlobalReceiver(WhatDidIJustKill.ENTITY_KILLED, FabricPacketHandler::handleEntityKilled);
        ClientPlayNetworking.registerGlobalReceiver(WhatDidIJustKill.PLAYER_KILLED, FabricPacketHandler::handlePlayerKilled);

        // register mod command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            FabricWhatDidIJustKillCommand.register(dispatcher);
        });
    }

}
