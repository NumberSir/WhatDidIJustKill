package com.tristankechlo.whatdidijustkill.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.FormatOption;
import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class EntityKilledToast extends AbstractEntityToast {

    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");
    private final ResourceLocation entityTexture;

    private EntityKilledToast(Component firstLine, Component secondLine, ResourceLocation entityType) {
        super(firstLine, secondLine);
        this.entityTexture = getTextureLocationSafe(entityType);
        this.displayTime = WhatDidIJustKillConfig.get().entity().timeout();
        this.backgroundTextureOffsetY = WhatDidIJustKillConfig.get().entity().theme().getOffsetY();
        this.textShadow = WhatDidIJustKillConfig.get().entity().theme() == ToastTheme.ADVANCEMENT;
    }

    @Override
    protected void renderEntityImage(PoseStack poseStack) {
        // TODO allow textures with different sizes
        RenderSystem.setShaderTexture(0, this.entityTexture);
        GuiComponent.blit(poseStack, 8, 8, 0, 0, 16, 16, 16, 16);
    }

    public static EntityKilledToast make(Component entityName, ResourceLocation entityType, double distance) {
        ToastTheme theme = WhatDidIJustKillConfig.get().entity().theme();

        if (entityName.getStyle().getColor() == null) {
            entityName = entityName.copy().withStyle(theme.getColorHighlight());
        }
        distance = ((double) Mth.floor(distance * 10)) / 10.0D;

        FormatOption firstLineFormat = WhatDidIJustKillConfig.get().entity().firstLine();
        FormatOption secondLineFormat = WhatDidIJustKillConfig.get().entity().secondLine();

        MutableComponent firstLine = firstLineFormat.makeLine(theme, entityName, entityType, distance);
        MutableComponent secondLine = secondLineFormat.makeLine(theme, entityName, entityType, distance);
        return new EntityKilledToast(firstLine, secondLine, entityType);
    }

    private static ResourceLocation getTextureLocationSafe(ResourceLocation entityType) {
        ResourceLocation expectedLocation = makeExpectedLocation(entityType);
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(expectedLocation);
        if (texture != MissingTextureAtlasSprite.getTexture()) {
            return expectedLocation;
        } else {
            WhatDidIJustKill.LOGGER.warn("Did not find icon for '{}' at '{}' using fallback icon.", entityType, expectedLocation);
            return UNKNOWN_ENTITY;
        }
    }

    private static ResourceLocation makeExpectedLocation(ResourceLocation entityType) {
        EntityType<?> type = Registry.ENTITY_TYPE.get(entityType);
        String category = type.getCategory().getName().toLowerCase();
        String path = String.format("textures/entity_icon/%s/%s.png", category, entityType.getPath());
        return new ResourceLocation(entityType.getNamespace(), path);
    }

}
