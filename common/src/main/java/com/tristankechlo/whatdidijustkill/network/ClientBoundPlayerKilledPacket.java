package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record ClientBoundPlayerKilledPacket(UUID uuid, Component playerName, double distance) implements CustomPacketPayload {

    public static final Type<ClientBoundPlayerKilledPacket> TYPE = new Type<>(WhatDidIJustKill.PLAYER_KILLED);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundPlayerKilledPacket> CODEC = StreamCodec.of(
            ClientBoundPlayerKilledPacket::encode,
            ClientBoundPlayerKilledPacket::decode
    );

    /* decode for forge and fabric */
    public static void encode(RegistryFriendlyByteBuf buffer, ClientBoundPlayerKilledPacket packet) {
        buffer.writeUUID(packet.uuid());
        ComponentSerialization.STREAM_CODEC.encode(buffer, packet.playerName());
        buffer.writeDouble(packet.distance());
    }

    /* encode for forge and fabric */
    public static ClientBoundPlayerKilledPacket decode(RegistryFriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        Component entityName = ComponentSerialization.STREAM_CODEC.decode(buffer);
        double distance = buffer.readDouble();
        return new ClientBoundPlayerKilledPacket(uuid, entityName, distance);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundPlayerKilledPacket packet) {
        ToastHandler.showToastPlayer(packet.uuid, packet.playerName, packet.distance);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
