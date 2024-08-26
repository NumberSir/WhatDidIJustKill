package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgePacketHandler implements IPacketHandler {


    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WhatDidIJustKill.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        INSTANCE.registerMessage(1,
                ClientBoundEntityKilledPacket.class,
                ClientBoundEntityKilledPacket::encode,
                ClientBoundEntityKilledPacket::decode,
                ForgePacketHandler::handleEntityKilled
        );

        INSTANCE.registerMessage(2,
                ClientBoundPlayerKilledPacket.class,
                ClientBoundPlayerKilledPacket::encode,
                ClientBoundPlayerKilledPacket::decode,
                ForgePacketHandler::handlePlayerKilled
        );
    }

    @Override
    public void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet) {
        Connection connection = player.connection.connection;
        INSTANCE.sendTo(packet, connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static void handleEntityKilled(ClientBoundEntityKilledPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // handle packet only when on client main thread
                ClientBoundEntityKilledPacket.handle(packet);
            });
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet) {
        Connection connection = player.connection.connection;
        INSTANCE.sendTo(packet, connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static void handlePlayerKilled(ClientBoundPlayerKilledPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // handle packet only when on client main thread
                ClientBoundPlayerKilledPacket.handle(packet);
            });
        });
        context.get().setPacketHandled(true);
    }

}
