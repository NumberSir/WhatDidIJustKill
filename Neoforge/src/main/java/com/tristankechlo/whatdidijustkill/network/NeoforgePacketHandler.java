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
                WhatDidIJustKill.CHANNEL,
                NeoforgePlayerKilledEntityPacketWrapper::decode,
                handler -> handler.client(NeoforgePacketHandler::handle)
        );
    }

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledEntityPacket packet) {
        NeoforgePlayerKilledEntityPacketWrapper message = new NeoforgePlayerKilledEntityPacketWrapper(packet);
        PacketDistributor.PLAYER.with(player).send(message);
    }

    private static void handle(NeoforgePlayerKilledEntityPacketWrapper packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ClientBoundPlayerKilledEntityPacket.handle(packet.packet());
        });
    }

}
