package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.EntityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ToastHandler {

    public static void showToastEntity(Component entityName, ResourceLocation entityType, double distance, boolean hasSpecialName) {
        final boolean showOnlyMobsWithSpecialNames = WhatDidIJustKillConfig.get().entity().showToast() == EntityOptions.ShowToastOption.ONLY_NAMED;
        final ToastComponent toastManager = Minecraft.getInstance().getToasts();

        if (showOnlyMobsWithSpecialNames) {
            if (hasSpecialName) {
                // only show mobs with special names
                toastManager.addToast(EntityKilledToast.make(entityName, entityType, distance));
            }
        } else {
            toastManager.addToast(EntityKilledToast.make(entityName, entityType, distance));
        }
    }

    public static void showToastPlayer(UUID uuid, Component playerName, double distance) {
        final ToastComponent toastManager = Minecraft.getInstance().getToasts();
        toastManager.addToast(PlayerKilledToast.make(uuid, playerName, distance));
    }

}
