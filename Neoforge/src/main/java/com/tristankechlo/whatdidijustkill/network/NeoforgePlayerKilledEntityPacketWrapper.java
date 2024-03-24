package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgePlayerKilledEntityPacketWrapper(ClientBoundPlayerKilledEntityPacket packet) implements CustomPacketPayload {

    public static NeoforgePlayerKilledEntityPacketWrapper decode(FriendlyByteBuf buffer) {
        // read data from packet
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        ClientBoundPlayerKilledEntityPacket packet = new ClientBoundPlayerKilledEntityPacket(entityName, entityType);
        return new NeoforgePlayerKilledEntityPacketWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        // add data to packet here
        buffer.writeComponent(packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
    }

    @Override
    public ResourceLocation id() {
        return WhatDidIJustKill.CHANNEL;
    }

}
