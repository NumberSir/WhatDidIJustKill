package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record EntityKilledPacket(Component entityName, ResourceLocation entityType) {

    /* decode for forge and fabric */
    public static void encode(EntityKilledPacket packet, FriendlyByteBuf buffer) {
        // add data to packet here
        buffer.writeComponent(packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
    }

    /* encode for forge and fabric */
    public static EntityKilledPacket decode(FriendlyByteBuf buffer) {
        // read data from packet
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        return new EntityKilledPacket(entityName, entityType);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(EntityKilledPacket packet) {
        WhatDidIJustKill.LOGGER.info("packet got handled");
        WhatDidIJustKill.LOGGER.info(packet.entityName().getString());
        WhatDidIJustKill.LOGGER.info(packet.entityType().toString());
    }

}
