package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import com.tristankechlo.whatdidijustkill.fabric_command.FabricWhatDidIJustKillCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class FabricWhatDidIJustKillClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.loadAndVerifyConfig();

        // register mod command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            FabricWhatDidIJustKillCommand.register(dispatcher);
        });
    }

}
