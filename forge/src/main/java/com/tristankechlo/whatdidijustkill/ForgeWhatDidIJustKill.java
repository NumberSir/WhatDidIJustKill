package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.command.WhatDidIJustKillCommand;
import com.tristankechlo.whatdidijustkill.network.ForgePacketHandler;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WhatDidIJustKill.MOD_ID)
public class ForgeWhatDidIJustKill {

    public ForgeWhatDidIJustKill() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ForgePacketHandler.registerPackets();
    }

    private void registerCommands(RegisterClientCommandsEvent event) {
        WhatDidIJustKillCommand.register(event.getDispatcher());
    }

}
