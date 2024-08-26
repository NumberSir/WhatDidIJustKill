package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public record ClientBoundPlayerKilledPacket(UUID uuid, Component playerName, double distance) {

    /* decode for forge and fabric */
    public static void encode(ClientBoundPlayerKilledPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.uuid());
        buffer.writeComponent(packet.playerName());
        buffer.writeDouble(packet.distance());
    }

    /* encode for forge and fabric */
    public static ClientBoundPlayerKilledPacket decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        Component entityName = buffer.readComponent();
        double distance = buffer.readDouble();
        return new ClientBoundPlayerKilledPacket(uuid, entityName, distance);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundPlayerKilledPacket packet) {
        ToastHandler.showToastPlayer(packet.uuid, packet.playerName, packet.distance);
    }

}
