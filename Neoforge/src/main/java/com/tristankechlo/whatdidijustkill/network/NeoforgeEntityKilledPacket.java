package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgeEntityKilledPacket(EntityKilledPacket packet) implements CustomPacketPayload {

    public static NeoforgeEntityKilledPacket decode(FriendlyByteBuf buffer) {
        // read data from packet
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        EntityKilledPacket packet = new EntityKilledPacket(entityName, entityType);
        return new NeoforgeEntityKilledPacket(packet);
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
