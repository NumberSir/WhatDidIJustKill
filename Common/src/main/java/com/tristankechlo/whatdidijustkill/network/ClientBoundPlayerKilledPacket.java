package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.PlayerKilledToast;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public record ClientBoundPlayerKilledPacket(UUID uuid, Component playerName, double distance) {

    /* decode for forge and fabric */
    public static void encode(ClientBoundPlayerKilledPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.uuid());
        buffer.writeComponent(packet.playerName());
        buffer.writeDouble(packet.distance());
    }

    /* encode for forge and fabric */
    public static ClientBoundPlayerKilledPacket decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        Component entityName = buffer.readComponent();
        double distance = buffer.readDouble();
        return new ClientBoundPlayerKilledPacket(uuid, entityName, distance);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundPlayerKilledPacket packet) {
        if (!WhatDidIJustKillConfig.get().enabled()) {
            return;
        }

        final boolean wasLongDistance = packet.distance() > WhatDidIJustKillConfig.get().longDistance().threshold();

        final ToastComponent toastManager = Minecraft.getInstance().getToasts();

        if (wasLongDistance) {
            toastManager.addToast(PlayerKilledToast.makeToast(packet.uuid, packet.playerName, packet.distance));
        } else {
            toastManager.addToast(PlayerKilledToast.makeToast(packet.uuid, packet.playerName));
        }

    }

}
