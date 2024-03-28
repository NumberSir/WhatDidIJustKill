package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgePlayerKilledEntityPacketWrapper(ClientBoundEntityKilledPacket packet) implements CustomPacketPayload {

    public static NeoforgePlayerKilledEntityPacketWrapper decode(FriendlyByteBuf buffer) {
        ClientBoundEntityKilledPacket packet = ClientBoundEntityKilledPacket.decode(buffer);
        return new NeoforgePlayerKilledEntityPacketWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        ClientBoundEntityKilledPacket.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return WhatDidIJustKill.CHANNEL;
    }

}
