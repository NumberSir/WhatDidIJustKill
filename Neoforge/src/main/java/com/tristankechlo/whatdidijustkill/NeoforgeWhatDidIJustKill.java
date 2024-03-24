package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.network.NeoforgePacketHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(WhatDidIJustKill.MOD_ID)
public class NeoforgeWhatDidIJustKill {

    public NeoforgeWhatDidIJustKill(IEventBus modEventBus, Dist dist) {
        modEventBus.addListener(NeoforgePacketHandler::registerPackets);
    }

}
