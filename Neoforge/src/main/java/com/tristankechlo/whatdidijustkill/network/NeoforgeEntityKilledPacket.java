package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoforgeEntityKilledPacket() implements CustomPacketPayload {

    public static NeoforgeEntityKilledPacket decode(FriendlyByteBuf buffer) {
        return new NeoforgeEntityKilledPacket();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        // add data to packet here
    }

    public EntityKilledPacket packet() {
        return new EntityKilledPacket();
    }

    @Override
    public ResourceLocation id() {
        return WhatDidIJustKill.CHANNEL;
    }

}
