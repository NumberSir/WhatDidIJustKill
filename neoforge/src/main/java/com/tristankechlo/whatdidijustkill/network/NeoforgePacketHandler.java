package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class NeoforgePacketHandler implements IPacketHandler {

    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(WhatDidIJustKill.MOD_ID).versioned("1.0").optional();
        registrar.play(
                WhatDidIJustKill.ENTITY_KILLED,
                NeoforgeEntityKilledPacketWrapper::decode,
                handler -> handler.client(NeoforgePacketHandler::handleEntityKilled)
        );
        registrar.play(
                WhatDidIJustKill.PLAYER_KILLED,
                NeoforgePlayerKilledPacketWrapper::decode,
                handler -> handler.client(NeoforgePacketHandler::handlePlayerKilled)
        );
    }

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        NeoforgeEntityKilledPacketWrapper message = new NeoforgeEntityKilledPacketWrapper(packet);
        PacketDistributor.PLAYER.with(player).send(message);
    }

    private static void handleEntityKilled(NeoforgeEntityKilledPacketWrapper packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ClientBoundEntityKilledPacket.handle(packet.packet());
        });
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        NeoforgePlayerKilledPacketWrapper message = new NeoforgePlayerKilledPacketWrapper(packet);
        PacketDistributor.PLAYER.with(player).send(message);
    }

    private static void handlePlayerKilled(NeoforgePlayerKilledPacketWrapper packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ClientBoundPlayerKilledPacket.handle(packet.packet());
        });
    }

}
