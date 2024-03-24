package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.network.ForgePacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WhatDidIJustKill.MOD_ID)
public class ForgeWhatDidIJustKill {

    public ForgeWhatDidIJustKill() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ForgePacketHandler.registerPackets();
    }

}
