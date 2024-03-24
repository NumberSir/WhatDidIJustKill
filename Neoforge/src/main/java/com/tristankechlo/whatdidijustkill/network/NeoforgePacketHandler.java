package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class NeoforgePacketHandler implements IPacketHandler {

    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(WhatDidIJustKill.MOD_ID).versioned("1.0").optional();
        registrar.play(
                WhatDidIJustKill.CHANNEL,
                NeoforgeEntityKilledPacket::decode,
                handler -> handler.client(NeoforgePacketHandler::handle)
        );
    }

    @Override
    public void sendPacketEntityKilled(ServerPlayer player, EntityKilledPacket packet) {
        NeoforgeEntityKilledPacket message = new NeoforgeEntityKilledPacket(packet);
        PacketDistributor.PLAYER.with(player).send(message);
    }

    private static void handle(NeoforgeEntityKilledPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            EntityKilledPacket.handle(packet.packet());
        });
    }

}
