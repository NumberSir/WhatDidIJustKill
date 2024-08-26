package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.command.WhatDidIJustKillCommand;
import com.tristankechlo.whatdidijustkill.network.NeoforgePacketHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(WhatDidIJustKill.MOD_ID)
public class NeoforgeWhatDidIJustKill {

    public NeoforgeWhatDidIJustKill(IEventBus modEventBus, Dist dist) {
        modEventBus.addListener(NeoforgePacketHandler::registerPackets);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterClientCommandsEvent event) {
        WhatDidIJustKillCommand.register(event.getDispatcher());
    }

}
