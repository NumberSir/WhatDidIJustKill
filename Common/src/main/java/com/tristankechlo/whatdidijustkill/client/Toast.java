package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Toast {

    private static final ResourceLocation TOAST_TEXTURE = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/toast.png");
    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");
    private static final int MIN_TOAST_WIDTH = 120;
    private static final int MAX_TOAST_WIDTH = 160;
    private static final int WIDTH = 160; // texture width
    private static final int HEIGHT = 32; // texture height

    private final Component entityName;
    private final ResourceLocation entityType;
    private final Font font;
    private final int toastWidth;
    private final boolean showEntityType;

    private Toast(Component entityName, ResourceLocation entityType, Font font, boolean showEntityType) {
        this.entityName = entityName;
        this.entityType = entityType;
        this.font = font;
        this.showEntityType = showEntityType;

        int entityNameLength = font.width(entityName);
        int entityTypeLength = font.width(entityType.toString());
        int toastTextLength = showEntityType ? Math.max(entityNameLength, entityTypeLength) : entityNameLength;
        this.toastWidth = Math.min(Math.max(8 + 16 + 6 + toastTextLength + 8, MIN_TOAST_WIDTH), MAX_TOAST_WIDTH);
    }

    public static Toast create(Component entityName, ResourceLocation entityType) {
        Font font = Minecraft.getInstance().font;
        boolean showEntityType = true; // TODO read from config
        return new Toast(entityName, entityType, font, showEntityType);
    }

    public void render(GuiGraphics graphics, float partialTicks) {
        // screen coordinates
        int x = 0; // TODO use offset from config
        int y = 0;

        if (showEntityType) {
            // render texture
            graphics.blit(TOAST_TEXTURE, x, y, 0, 0, toastWidth - 8, HEIGHT, WIDTH, HEIGHT); // upper half
            graphics.blit(TOAST_TEXTURE, x + toastWidth - 8, y, WIDTH - 8, 0, 8, HEIGHT, WIDTH, HEIGHT); // lower half
            // draw text
            graphics.drawString(font, entityType.toString(), 30, 17, ChatFormatting.DARK_GRAY.getColor());
        } else { // do not render entityType
            // render texture
            graphics.blit(TOAST_TEXTURE, x, y, 0, 0, toastWidth - 5, 21, WIDTH, HEIGHT); // top left
            graphics.blit(TOAST_TEXTURE, x, y + 21, 0, 27, toastWidth - 5, 5, WIDTH, HEIGHT); // bottom left
            graphics.blit(TOAST_TEXTURE, x + toastWidth - 5, y, WIDTH - 5, 0, 5, 21, WIDTH, HEIGHT); // top right
            graphics.blit(TOAST_TEXTURE, x + toastWidth - 5, y + 21, WIDTH - 5, 27, 5, 5, WIDTH, HEIGHT); // bottom right
        }
        // draw text
        int x1 = x + (showEntityType ? 30 : 26);
        int y1 = y + (showEntityType ? 7 : 9);
        graphics.drawString(font, entityName, x1, y1, ChatFormatting.WHITE.getColor());
        // render entity icon
        int x2 = x + (showEntityType ? 8 : 6);
        int y2 = y + (showEntityType ? 8 : 5);
        graphics.blit(UNKNOWN_ENTITY, x2, y2, 0, 0, 16, 16, 16, 16);
    }

}
