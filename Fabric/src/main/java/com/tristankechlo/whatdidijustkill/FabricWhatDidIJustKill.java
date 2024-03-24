package com.tristankechlo.whatdidijustkill;

import com.tristankechlo.whatdidijustkill.network.FabricPacketHandler;
import net.fabricmc.api.ModInitializer;

public class FabricWhatDidIJustKill implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricPacketHandler.registerPackets();
    }

}
