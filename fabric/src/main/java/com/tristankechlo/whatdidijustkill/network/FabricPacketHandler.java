package com.tristankechlo.whatdidijustkill.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketHandler implements IPacketHandler {

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        ServerPlayNetworking.send(player, packet);
    }

    public static void handleEntityKilled(ClientBoundEntityKilledPacket payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            // execute the handling on the client thread
            ClientBoundEntityKilledPacket.handle(payload);
        });
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        ServerPlayNetworking.send(player, packet);
    }

    public static void handlePlayerKilled(ClientBoundPlayerKilledPacket payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            // execute the handling on the client thread
            ClientBoundPlayerKilledPacket.handle(payload);
        });
    }

}
