package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.EntityOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundEntityKilledPacket(Component entityName, ResourceLocation entityType, double distance, boolean hasSpecialName) {

    /* decode for forge and fabric */
    public static void encode(ClientBoundEntityKilledPacket packet, FriendlyByteBuf buffer) {
        buffer.writeComponent(packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
        buffer.writeDouble(packet.distance());
        buffer.writeBoolean(packet.hasSpecialName());
    }

    /* encode for forge and fabric */
    public static ClientBoundEntityKilledPacket decode(FriendlyByteBuf buffer) {
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        double distance = buffer.readDouble();
        boolean hasCustomName = buffer.readBoolean();
        return new ClientBoundEntityKilledPacket(entityName, entityType, distance, hasCustomName);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundEntityKilledPacket packet) {
        if (WhatDidIJustKillConfig.get().entity().showToast() == EntityOptions.ShowToastOption.NONE) {
            return;
        }

        if (WhatDidIJustKillConfig.get().entity().isEntityExcluded(packet.entityType)) {
            return;
        }
        ToastHandler.showToastEntity(packet.entityName, packet.entityType, packet.distance, packet.hasSpecialName);
    }

}
