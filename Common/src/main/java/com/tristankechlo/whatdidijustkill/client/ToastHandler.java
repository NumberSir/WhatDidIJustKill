package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.command.ResponseHelper;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.EntityOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ToastHandler {

    public static boolean toastsEnabled = true; // TODO save somewhere

    public static void toggleVisibility(Minecraft instance) {
        toastsEnabled = !toastsEnabled;
        MutableComponent start = ResponseHelper.start();
        MutableComponent message = Component.translatable("key.whatdidijustkill.toggle_toasts." + (toastsEnabled ? "enabled" : "disabled"));
        if (instance.player != null) {
            instance.player.sendSystemMessage(start.append(message.withStyle(ChatFormatting.WHITE)));
        }
    }

    public static void showToastEntity(Component entityName, ResourceLocation entityType, double distance, boolean hasSpecialName) {
        if (!toastsEnabled || WhatDidIJustKillConfig.get().entity().showToast() == EntityOptions.ShowToastOption.NONE) {
            return;
        }
        if (WhatDidIJustKillConfig.get().entity().isEntityExcluded(entityType)) {
            return;
        }

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
        if (toastsEnabled && WhatDidIJustKillConfig.get().player().showToast()) {
            final ToastComponent toastManager = Minecraft.getInstance().getToasts();
            toastManager.addToast(PlayerKilledToast.make(uuid, playerName, distance));
        }
    }

}
