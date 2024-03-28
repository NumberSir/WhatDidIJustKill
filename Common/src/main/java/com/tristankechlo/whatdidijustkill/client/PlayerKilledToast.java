package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PlayerKilledToast implements Toast {

    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
    private static final ResourceLocation UNKNOWN_PLAYER = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/player.png");

    private final int displayTime;
    private final Component killMessage;
    private final ResourceLocation texture;

    private PlayerKilledToast(UUID uuid, Component killMessage) {
        this.killMessage = killMessage;
        this.displayTime = WhatDidIJustKillConfig.get().timeout();

        this.texture = getTexture(uuid);
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long displayTime) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

        // draw text
        graphics.drawString(parent.getMinecraft().font, killMessage, 30, 12, ChatFormatting.WHITE.getColor());

        // draw entity texture
        if (this.texture == UNKNOWN_PLAYER) {
            graphics.blit(this.texture, 8, 8, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.pose().pushPose();
            graphics.pose().scale(2, 2, 2);
            graphics.blit(this.texture, 4, 4, 8, 8, 8, 8, 64, 64);
            graphics.pose().pushPose();
        }

        // remove toast when time is over
        return (double) displayTime >= this.displayTime * parent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }

    public static PlayerKilledToast makeToast(UUID uuid, Component playerName) {
        if (playerName.getStyle().getColor() == null) {
            playerName = playerName.copy().withStyle(ChatFormatting.WHITE);
        }
        MutableComponent message = Component.translatable("screen." + WhatDidIJustKill.MOD_ID + ".killed", playerName)
                .withStyle(ChatFormatting.GRAY);
        return new PlayerKilledToast(uuid, message);
    }


    public static PlayerKilledToast makeToast(UUID uuid, Component playerName, double distance) {
        if (playerName.getStyle().getColor() == null) {
            playerName = playerName.copy().withStyle(ChatFormatting.WHITE);
        }
        distance = ((double) Mth.floor(distance * 10)) / 10.0D;
        MutableComponent message = Component.translatable("screen." + WhatDidIJustKill.MOD_ID + ".killed.distance", playerName, distance)
                .withStyle(ChatFormatting.GRAY);
        return new PlayerKilledToast(uuid, message);
    }

    private static ResourceLocation getTexture(UUID uuid) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return UNKNOWN_PLAYER;
        }
        Player player = level.getPlayerByUUID(uuid);
        if (player instanceof AbstractClientPlayer p) {
            return p.getSkin().texture();
        }
        return UNKNOWN_PLAYER;
    }

}
