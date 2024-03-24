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

    private static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(WhatDidIJustKill.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .clientAcceptedVersions((s, i) -> true)
            .serverAcceptedVersions((s, i) -> true)
            .simpleChannel();

    public static void registerPackets() {
        INSTANCE.messageBuilder(ClientBoundPlayerKilledEntityPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundPlayerKilledEntityPacket::decode)
                .encoder(ClientBoundPlayerKilledEntityPacket::encode)
                // handle packet execution directly on the main thread
                .consumerMainThread(ForgePacketHandler::handle)
                .add();
    }

    @Override
    public void sendPacketEntityKilled(ServerPlayer player, ClientBoundPlayerKilledEntityPacket packet) {
        INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    private static void handle(ClientBoundPlayerKilledEntityPacket packet, CustomPayloadEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // handle packet only when on client main thread
            ClientBoundPlayerKilledEntityPacket.handle(packet);
        });
    }

}
