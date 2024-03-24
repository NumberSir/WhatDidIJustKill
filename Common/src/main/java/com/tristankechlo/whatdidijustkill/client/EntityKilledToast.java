package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class EntityKilledToast implements Toast {

    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");
    public static final int DISPLAY_TIME = 3000; // TODO read value from config

    private final Component entityName;
    private final ResourceLocation entityType;
    private final ResourceLocation textureLocation;

    public EntityKilledToast(Component entityName, ResourceLocation entityType) {
        this.entityName = makeComponent(entityName);
        this.entityType = entityType;
        ResourceLocation location = new ResourceLocation(entityType.getNamespace(), "textures/entity_icon/" + entityType.getPath() + ".png");

        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
        boolean found = texture == MissingTextureAtlasSprite.getTexture();
        this.textureLocation = found ? UNKNOWN_ENTITY : location;

        WhatDidIJustKill.LOGGER.info("{}", textureLocation);
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long l) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

        // draw text
        int mainTextY = 12;
        if (parent.getMinecraft().options.advancedItemTooltips) {
            mainTextY = 7;
            graphics.drawString(parent.getMinecraft().font, entityType.toString(), 30, 17, ChatFormatting.DARK_GRAY.getColor());
        }
        graphics.drawString(parent.getMinecraft().font, entityName, 30, mainTextY, ChatFormatting.WHITE.getColor());

        // draw entity texture
        graphics.blit(this.textureLocation, 8, 8, 0, 0, 16, 16, 16, 16);

        // remove toast when time is over
        return (double) l >= DISPLAY_TIME * parent.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE
                : Toast.Visibility.SHOW;
    }

    private static MutableComponent makeComponent(Component entityName) {
        if (entityName.getStyle().getColor() == null) {
            entityName = entityName.copy().withStyle(ChatFormatting.WHITE);
        }
        return Component.translatable("screen." + WhatDidIJustKill.MOD_ID + ".killed", entityName).withStyle(ChatFormatting.GRAY);
    }

}
