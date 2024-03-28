package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public interface IPacketHandler {

    public static final IPacketHandler INSTANCE = WhatDidIJustKill.load(IPacketHandler.class);

    void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet);

    default void sendPacketEntityKilled(ServerPlayer player, Entity killed) {
        BlockPos pos2 = player.blockPosition();
        Component entityName = killed.getDisplayName();
        BlockPos pos1 = killed.blockPosition();
        ResourceLocation entityType = BuiltInRegistries.ENTITY_TYPE.getKey(killed.getType());
        double distance = Math.sqrt(pos1.distSqr(pos2));
        this.sendPacketEntityKilledByPlayer(player, new ClientBoundEntityKilledPacket(entityName, entityType, distance, killed.hasCustomName()));
    }

    void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet);

    default void sendPacketPlayerKilled(ServerPlayer player, ServerPlayer deadPlayer) {
        Component playerName = deadPlayer.getDisplayName();
        UUID uuid = deadPlayer.getUUID();
        double distance = Math.sqrt(player.blockPosition().distSqr(deadPlayer.blockPosition()));
        this.sendPacketPlayerKilledByPlayer(player, new ClientBoundPlayerKilledPacket(uuid, playerName, distance));
    }

}
