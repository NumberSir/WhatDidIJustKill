package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgePacketHandler implements IPacketHandler {

    private static final SimpleChannel INSTANCE = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(WhatDidIJustKill.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .clientAcceptedVersions((s, i) -> true)
            .serverAcceptedVersions((s, i) -> true)
            .simpleChannel();

    public static void registerPackets() {
        INSTANCE.messageBuilder(ClientBoundEntityKilledPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundEntityKilledPacket::decode)
                .encoder((msg, buf) -> ClientBoundEntityKilledPacket.encode(buf, msg))
                // handle packet execution directly on the main thread
                .consumerMainThread(ForgePacketHandler::handleEntityKilled)
                .add();

        INSTANCE.messageBuilder(ClientBoundPlayerKilledPacket.class, 2, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundPlayerKilledPacket::decode)
                .encoder((msg, buf) -> ClientBoundPlayerKilledPacket.encode(buf, msg))
                .consumerMainThread(ForgePacketHandler::handlePlayerKilled)
                .add();
    }

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    private static void handleEntityKilled(ClientBoundEntityKilledPacket packet, CustomPayloadEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // handle packet only when on client main thread
            ClientBoundEntityKilledPacket.handle(packet);
        });
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    private static void handlePlayerKilled(ClientBoundPlayerKilledPacket packet, CustomPayloadEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // handle packet only when on client main thread
            ClientBoundPlayerKilledPacket.handle(packet);
        });
    }

}
