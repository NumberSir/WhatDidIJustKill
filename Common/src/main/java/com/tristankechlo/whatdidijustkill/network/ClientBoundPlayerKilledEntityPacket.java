package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.EntityKilledToast;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundPlayerKilledEntityPacket(Component entityName, ResourceLocation entityType, BlockPos pos1, BlockPos pos2, boolean hasSpecialName) {

    /* decode for forge and fabric */
    public static void encode(ClientBoundPlayerKilledEntityPacket packet, FriendlyByteBuf buffer) {
        // add data to packet here
        buffer.writeComponent(packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
        buffer.writeBlockPos(packet.pos1());
        buffer.writeBlockPos(packet.pos2());
        buffer.writeBoolean(packet.hasSpecialName());
    }

    /* encode for forge and fabric */
    public static ClientBoundPlayerKilledEntityPacket decode(FriendlyByteBuf buffer) {
        // read data from packet
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        BlockPos pos1 = buffer.readBlockPos();
        BlockPos pos2 = buffer.readBlockPos();
        boolean hasCustomName = buffer.readBoolean();
        return new ClientBoundPlayerKilledEntityPacket(entityName, entityType, pos1, pos2, hasCustomName);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundPlayerKilledEntityPacket packet) {
        if (!WhatDidIJustKillConfig.get().enabled()) {
            return;
        }

        final double distance = Math.sqrt(packet.pos1.distSqr(packet.pos2));

        final boolean excluded = WhatDidIJustKillConfig.get().isEntityExcluded(packet.entityType);
        final boolean showLongDistance = WhatDidIJustKillConfig.get().longDistance().alwaysShow();
        final boolean wasLongDistance = distance > WhatDidIJustKillConfig.get().longDistance().threshold();
        final boolean showOnlyMobsWithSpecialNames = WhatDidIJustKillConfig.get().onlyNamedMobs();
        final boolean hasSpecialName = packet.hasSpecialName();

        final ToastComponent toastManager = Minecraft.getInstance().getToasts();

        if (showOnlyMobsWithSpecialNames && hasSpecialName && !excluded) {
            // only show mobs with special names and also filter out excluded mobs
            toastManager.addToast(EntityKilledToast.makeToast(packet.entityName(), packet.entityType()));

        } else if (showLongDistance && wasLongDistance) {
            // show mobs that got killed over a large distance, can bypass excluded mobs
            toastManager.addToast(EntityKilledToast.makeToast(packet.entityName(), packet.entityType(), distance));

        } else if (!excluded) {
            // only add toasts if mob not excluded
            toastManager.addToast(EntityKilledToast.makeToast(packet.entityName(), packet.entityType()));
        }
    }

}
