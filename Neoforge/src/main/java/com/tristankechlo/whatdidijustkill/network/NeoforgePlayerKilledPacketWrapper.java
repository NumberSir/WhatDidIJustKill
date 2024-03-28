package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgePlayerKilledPacketWrapper(ClientBoundPlayerKilledPacket packet) implements CustomPacketPayload {

    public static NeoforgePlayerKilledPacketWrapper decode(FriendlyByteBuf buffer) {
        ClientBoundPlayerKilledPacket packet = ClientBoundPlayerKilledPacket.decode(buffer);
        return new NeoforgePlayerKilledPacketWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        ClientBoundPlayerKilledPacket.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return WhatDidIJustKill.PLAYER_KILLED;
    }

}
