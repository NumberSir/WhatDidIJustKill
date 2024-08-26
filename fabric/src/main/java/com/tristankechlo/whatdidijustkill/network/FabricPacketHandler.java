package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketHandler implements IPacketHandler {

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        ClientBoundEntityKilledPacket.encode(packet, buffer);
        ServerPlayNetworking.send(player, WhatDidIJustKill.ENTITY_KILLED, buffer);
    }

    public static void handleEntityKilled(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender sender) {
        ClientBoundEntityKilledPacket packet = ClientBoundEntityKilledPacket.decode(buffer);
        client.execute(() -> {
            // execute the handling on the client thread
            ClientBoundEntityKilledPacket.handle(packet);
        });
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        ClientBoundPlayerKilledPacket.encode(packet, buffer);
        ServerPlayNetworking.send(player, WhatDidIJustKill.PLAYER_KILLED, buffer);
    }

    public static void handlePlayerKilled(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender sender) {
        ClientBoundPlayerKilledPacket packet = ClientBoundPlayerKilledPacket.decode(buffer);
        client.execute(() -> {
            // execute the handling on the client thread
            ClientBoundPlayerKilledPacket.handle(packet);
        });
    }

}
