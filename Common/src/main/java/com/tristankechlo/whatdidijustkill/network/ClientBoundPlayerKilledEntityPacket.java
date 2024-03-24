package com.tristankechlo.whatdidijustkill.network;

import com.tristankechlo.whatdidijustkill.client.Toast;
import com.tristankechlo.whatdidijustkill.client.ToastRenderer;
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
        if (ToastRenderer.TOASTS.isEmpty()) {
            ToastRenderer.TOASTS.add(Toast.create(packet.entityName(), packet.entityType()));
        } else {
            ToastRenderer.TOASTS.set(0, Toast.create(packet.entityName(), packet.entityType()));
        }
    }

}
