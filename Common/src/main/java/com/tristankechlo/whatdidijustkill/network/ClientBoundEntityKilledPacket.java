package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundEntityKilledPacket(Component entityName, ResourceLocation entityType, double distance, boolean hasSpecialName) implements CustomPacketPayload {

    public static final Type<ClientBoundEntityKilledPacket> TYPE = new Type<>(WhatDidIJustKill.ENTITY_KILLED);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundEntityKilledPacket> CODEC = StreamCodec.of(
            ClientBoundEntityKilledPacket::encode,
            ClientBoundEntityKilledPacket::decode
    );

    public static void encode(RegistryFriendlyByteBuf buffer, ClientBoundEntityKilledPacket packet) {
        ComponentSerialization.STREAM_CODEC.encode(buffer, packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
        buffer.writeDouble(packet.distance());
        buffer.writeBoolean(packet.hasSpecialName());
    }

    public static ClientBoundEntityKilledPacket decode(RegistryFriendlyByteBuf buffer) {
        Component entityName = ComponentSerialization.STREAM_CODEC.decode(buffer);
        ResourceLocation entityType = buffer.readResourceLocation();
        double distance = buffer.readDouble();
        boolean hasCustomName = buffer.readBoolean();
        return new ClientBoundEntityKilledPacket(entityName, entityType, distance, hasCustomName);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundEntityKilledPacket packet) {
        ToastHandler.showToastEntity(packet.entityName, packet.entityType, packet.distance, packet.hasSpecialName);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
