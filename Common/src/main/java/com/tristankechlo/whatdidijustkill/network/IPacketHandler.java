package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface IPacketHandler {

    public static final IPacketHandler INSTANCE = WhatDidIJustKill.load(IPacketHandler.class);

    void sendPacketEntityKilledByPlayer(ServerPlayer player, ClientBoundEntityKilledPacket packet);

    default void sendPacketEntityKilled(ServerPlayer player, Entity killed) {
        BlockPos pos2 = player.blockPosition();
        Component entityName = killed.getDisplayName();
        BlockPos pos1 = killed.blockPosition();
        ResourceLocation entityType = BuiltInRegistries.ENTITY_TYPE.getKey(killed.getType());
        this.sendPacketEntityKilledByPlayer(player, makePacket(entityName, entityType, pos1, pos2, killed.hasCustomName()));
    }

    private static ClientBoundEntityKilledPacket makePacket(Component entityName, ResourceLocation entityType, BlockPos pos1, BlockPos pos2, boolean hasSpecialName) {
        double distance = Math.sqrt(pos1.distSqr(pos2));
        return new ClientBoundEntityKilledPacket(entityName, entityType, distance, hasSpecialName);
    }

}
