package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.server.level.ServerPlayer;

public interface IPacketHandler {

    public static final IPacketHandler INSTANCE = WhatDidIJustKill.load(IPacketHandler.class);

    void sendPacketEntityKilled(ServerPlayer player, ClientBoundPlayerKilledEntityPacket packet);

}
