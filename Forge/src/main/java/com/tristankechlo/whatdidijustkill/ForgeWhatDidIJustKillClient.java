package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WhatDidIJustKill.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeWhatDidIJustKillClient {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ConfigManager.loadAndVerifyConfig();
    }

}
