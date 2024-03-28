package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class EntityKilledToast implements Toast {

    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");

    private final int displayTime;
    private final Component killMessage;
    private final ResourceLocation entityType;
    private final ResourceLocation textureLocation;

    private EntityKilledToast(Component killMessage, ResourceLocation entityType) {
        this.killMessage = killMessage;
        this.entityType = entityType;

        this.displayTime = WhatDidIJustKillConfig.get().timeout();

        ResourceLocation location = makeTextureLoc(entityType);
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
        if (texture != MissingTextureAtlasSprite.getTexture()) {
            this.textureLocation = location;
        } else {
            WhatDidIJustKill.LOGGER.warn("Did not find icon for '{}' at '{}' using fallback icon.", entityType, location);
            this.textureLocation = UNKNOWN_ENTITY;
        }
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long displayTime) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

        // draw text
        int mainTextY = 12;
        if (!WhatDidIJustKillConfig.get().hideEntityType()) {
            mainTextY = 7;
            graphics.drawString(parent.getMinecraft().font, entityType.toString(), 30, 17, ChatFormatting.DARK_GRAY.getColor());
        }
        graphics.drawString(parent.getMinecraft().font, killMessage, 30, mainTextY, ChatFormatting.WHITE.getColor());

        // draw entity texture
        // TODO allow textures with different sizes
        graphics.blit(this.textureLocation, 8, 8, 0, 0, 16, 16, 16, 16);

        // remove toast when time is over
        return (double) displayTime >= this.displayTime * parent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }

    public static EntityKilledToast makeToast(Component entityName, ResourceLocation entityType) {
        if (entityName.getStyle().getColor() == null) {
            entityName = entityName.copy().withStyle(ChatFormatting.WHITE);
        }
        MutableComponent message = Component.translatable("screen." + WhatDidIJustKill.MOD_ID + ".killed", entityName)
                .withStyle(ChatFormatting.GRAY);
        return new EntityKilledToast(message, entityType);
    }


    public static EntityKilledToast makeToast(Component entityName, ResourceLocation entityType, double distance) {
        if (entityName.getStyle().getColor() == null) {
            entityName = entityName.copy().withStyle(ChatFormatting.WHITE);
        }
        distance = ((double) Mth.floor(distance * 10)) / 10.0D;
        MutableComponent message = Component.translatable("screen." + WhatDidIJustKill.MOD_ID + ".killed.distance", entityName, distance)
                .withStyle(ChatFormatting.GRAY);
        return new EntityKilledToast(message, entityType);
    }

    private static ResourceLocation makeTextureLoc(ResourceLocation entityType) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityType);
        String category = type.getCategory().getName().toLowerCase();
        String path = String.format("textures/entity_icon/%s/%s.png", category, entityType.getPath());
        return new ResourceLocation(entityType.getNamespace(), path);
    }

}
