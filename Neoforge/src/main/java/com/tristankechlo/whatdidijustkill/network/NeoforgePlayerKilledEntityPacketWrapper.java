package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgePlayerKilledEntityPacketWrapper(ClientBoundPlayerKilledEntityPacket packet) implements CustomPacketPayload {

    public static NeoforgePlayerKilledEntityPacketWrapper decode(FriendlyByteBuf buffer) {
        ClientBoundPlayerKilledEntityPacket packet = ClientBoundPlayerKilledEntityPacket.decode(buffer);
        return new NeoforgePlayerKilledEntityPacketWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        ClientBoundPlayerKilledEntityPacket.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return WhatDidIJustKill.CHANNEL;
    }

}
