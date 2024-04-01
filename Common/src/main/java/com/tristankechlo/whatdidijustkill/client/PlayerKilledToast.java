package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.FormatOption;
import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

public class PlayerKilledToast extends AbstractEntityToast {

    private static final ResourceLocation UNKNOWN_PLAYER = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/player.png");
    private final ResourceLocation playerTexture;

    private PlayerKilledToast(Component firstLine, Component secondLine, ResourceLocation texture) {
        super(firstLine, secondLine);
        this.playerTexture = texture;
        this.displayTime = WhatDidIJustKillConfig.get().player().timeout();
        this.backgroundTexture = WhatDidIJustKillConfig.get().player().theme().getBackgroundTexture();
        this.textShadow = WhatDidIJustKillConfig.get().player().theme() == ToastTheme.ADVANCEMENT;
    }

    @Override
    protected void renderEntityImage(GuiGraphics graphics) {
        if (this.playerTexture == UNKNOWN_PLAYER) {
            graphics.blit(this.playerTexture, 8, 8, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.pose().pushPose();
            graphics.pose().scale(2, 2, 2);
            graphics.blit(this.playerTexture, 4, 4, 8, 8, 8, 8, 64, 64);
            graphics.pose().popPose();
        }
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
