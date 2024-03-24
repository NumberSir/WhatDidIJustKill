package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BetterToast implements Toast {

    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");
    public static final int DISPLAY_TIME = 3000; // TODO read value from config

    private final Component entityName;
    private final ResourceLocation entityType;

    public BetterToast(Component entityName, ResourceLocation entityType) {
        this.entityName = entityName;
        this.entityType = entityType;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long l) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        // draw text
        graphics.drawString(parent.getMinecraft().font, entityName, 30, 7, ChatFormatting.WHITE.getColor());
        graphics.drawString(parent.getMinecraft().font, entityType.toString(), 30, 17, ChatFormatting.DARK_GRAY.getColor());
        // draw entity texture
        graphics.blit(UNKNOWN_ENTITY, 8, 8, 0, 0, 16, 16, 16, 16);

        // remove toast when time is over
        return (double) l >= DISPLAY_TIME * parent.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE
                : Toast.Visibility.SHOW;
    }

}
