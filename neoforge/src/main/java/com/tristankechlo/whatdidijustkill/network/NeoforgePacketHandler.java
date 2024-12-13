package com.tristankechlo.whatdidijustkill.network;

import com.google.auto.service.AutoService;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@AutoService(IPacketHandler.class)
public class NeoforgePacketHandler implements IPacketHandler {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WhatDidIJustKill.MOD_ID).versioned("1.0").optional();

        registrar.playToClient(
                ClientBoundEntityKilledPacket.TYPE,
                ClientBoundEntityKilledPacket.CODEC,
                NeoforgePacketHandler::handleEntityKilled
        );
        registrar.playToClient(
                ClientBoundPlayerKilledPacket.TYPE,
                ClientBoundPlayerKilledPacket.CODEC,
                NeoforgePacketHandler::handlePlayerKilled
        );

    }

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    private static void handleEntityKilled(ClientBoundEntityKilledPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientBoundEntityKilledPacket.handle(packet);
        });
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    private static void handlePlayerKilled(ClientBoundPlayerKilledPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientBoundPlayerKilledPacket.handle(packet);
        });
    }

}
