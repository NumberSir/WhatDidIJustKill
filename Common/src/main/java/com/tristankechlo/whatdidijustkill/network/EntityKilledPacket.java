package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;

public record EntityKilledPacket() {

    /* decode for forge and fabric */
    public static void encode(EntityKilledPacket packet, FriendlyByteBuf friendlyByteBuf) {
        // add data to packet here
    }

    /* encode for forge and fabric */
    public static EntityKilledPacket decode(FriendlyByteBuf buffer) {
        // read data from packet
        return new EntityKilledPacket();
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(EntityKilledPacket packet) {
        WhatDidIJustKill.LOGGER.info("packet got handled");
    }

}
