package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.command.WhatDidIJustKillCommand;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@Mod.EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeoforgeWhatDidIJustKillClient {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ConfigManager.loadAndVerifyConfig();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        WhatDidIJustKillCommand.register(event.getDispatcher());
    }

}
