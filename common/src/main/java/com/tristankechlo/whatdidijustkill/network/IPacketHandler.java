package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public interface IPacketHandler {

    public static final IPacketHandler INSTANCE = WhatDidIJustKill.load(IPacketHandler.class);

    void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet);

    default void sendPacketEntityKilled(ServerPlayer player, Entity killed) {
        Component entityName = killed.getDisplayName();
        ResourceLocation entityType = Registry.ENTITY_TYPE.getKey(killed.getType());
        double distance = this.calcDistance(player, killed);
        this.sendPacketEntityKilledByPlayer(player, new ClientBoundEntityKilledPacket(entityName, entityType, distance, killed.hasCustomName()));
    }

    void sendPacketPlayerKilledByPlayer(ServerPlayer player, ClientBoundPlayerKilledPacket packet);

    default void sendPacketPlayerKilled(ServerPlayer player, ServerPlayer deadPlayer) {
        Component playerName = deadPlayer.getDisplayName();
        UUID uuid = deadPlayer.getUUID();
        double distance = this.calcDistance(player, deadPlayer);
        this.sendPacketPlayerKilledByPlayer(player, new ClientBoundPlayerKilledPacket(uuid, playerName, distance));
    }

    default double calcDistance(Entity entity1, Entity entity2) {
        return Math.sqrt(entity1.blockPosition().distSqr(entity2.blockPosition()));
    }

}
