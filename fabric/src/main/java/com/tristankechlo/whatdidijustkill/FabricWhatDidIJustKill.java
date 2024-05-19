package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.network.ClientBoundEntityKilledPacket;
import com.tristankechlo.whatdidijustkill.network.ClientBoundPlayerKilledPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class FabricWhatDidIJustKill implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(ClientBoundEntityKilledPacket.TYPE, ClientBoundEntityKilledPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ClientBoundPlayerKilledPacket.TYPE, ClientBoundPlayerKilledPacket.CODEC);
    }

}
