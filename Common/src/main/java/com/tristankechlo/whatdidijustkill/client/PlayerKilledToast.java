package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.FormatOption;
import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PlayerKilledToast implements Toast {

    private static final ResourceLocation UNKNOWN_PLAYER = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/player.png");

    private final int displayTime;
    private final Component firstLine; // not null
    private final Component secondLine; // might be null
    private final ResourceLocation playerTexture;
    private final ResourceLocation backgroundTexture;
    private final boolean textShadow;

    private PlayerKilledToast(Component firstLine, Component secondLine, ResourceLocation texture) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.playerTexture = texture;
        this.displayTime = WhatDidIJustKillConfig.get().player().timeout();
        this.backgroundTexture = WhatDidIJustKillConfig.get().player().theme().getBackgroundTexture();
        this.textShadow = WhatDidIJustKillConfig.get().player().theme() == ToastTheme.ADVANCEMENT;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long displayTime) {
        graphics.blitSprite(this.backgroundTexture, 0, 0, this.width(), this.height());

        // draw text
        if (this.secondLine != null) {
            graphics.drawString(parent.getMinecraft().font, secondLine, 30, 17, 16777215, this.textShadow);
        }
        int y = this.secondLine == null ? 12 : 7;
        graphics.drawString(parent.getMinecraft().font, firstLine, 30, y, 16777215, this.textShadow);

        // draw entity texture
        if (this.playerTexture == UNKNOWN_PLAYER) {
            graphics.blit(this.playerTexture, 8, 8, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.pose().pushPose();
            graphics.pose().scale(2, 2, 2);
            graphics.blit(this.playerTexture, 4, 4, 8, 8, 8, 8, 64, 64);
            graphics.pose().pushPose();
        }

        // remove toast when time is over
        return (double) displayTime >= this.displayTime * parent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }

    public static PlayerKilledToast make(UUID uuid, Component entityName, double distance) {
        ToastTheme theme = WhatDidIJustKillConfig.get().player().theme();

        if (entityName.getStyle().getColor() == null) {
            entityName = entityName.copy().withStyle(theme.getColorHighlight());
        }
        distance = ((double) Mth.floor(distance * 10)) / 10.0D;
        ResourceLocation entityType = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER);
        ResourceLocation texture = getTextureLocation(uuid);

        FormatOption firstLineFormat = WhatDidIJustKillConfig.get().player().firstLine();
        FormatOption secondLineFormat = WhatDidIJustKillConfig.get().player().secondLine();

        MutableComponent firstLine = firstLineFormat.makeLine(theme, entityName, entityType, distance);
        MutableComponent secondLine = secondLineFormat.makeLine(theme, entityName, entityType, distance);
        return new PlayerKilledToast(firstLine, secondLine, texture);
    }

    private static ResourceLocation getTextureLocation(UUID uuid) {
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
