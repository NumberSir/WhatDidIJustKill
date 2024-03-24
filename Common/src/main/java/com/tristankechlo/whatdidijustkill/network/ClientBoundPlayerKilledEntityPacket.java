package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.EntityKilledToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundPlayerKilledEntityPacket(Component entityName, ResourceLocation entityType) {

    /* decode for forge and fabric */
    public static void encode(ClientBoundPlayerKilledEntityPacket packet, FriendlyByteBuf buffer) {
        // add data to packet here
        buffer.writeComponent(packet.entityName());
        buffer.writeResourceLocation(packet.entityType());
    }

    /* encode for forge and fabric */
    public static ClientBoundPlayerKilledEntityPacket decode(FriendlyByteBuf buffer) {
        // read data from packet
        Component entityName = buffer.readComponent();
        ResourceLocation entityType = buffer.readResourceLocation();
        return new ClientBoundPlayerKilledEntityPacket(entityName, entityType);
    }

    /* handle the packet; forge, fabric and neoforge */
    public static void handle(ClientBoundPlayerKilledEntityPacket packet) {
        // TODO only add toasts if enabled
        Minecraft.getInstance().getToasts().addToast(new EntityKilledToast(packet.entityName(), packet.entityType()));
    }

}
