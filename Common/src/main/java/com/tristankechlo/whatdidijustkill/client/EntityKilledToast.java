package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.WhatDidIJustKillConfig;
import com.tristankechlo.whatdidijustkill.config.types.FormatOption;
import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
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

    private static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(WhatDidIJustKill.MOD_ID, "textures/entity_unknown.png");

    private final int displayTime;
    private final Component firstLine; // not null
    private final Component secondLine; // might be null
    private final ResourceLocation entityTexture;
    private final ResourceLocation backgroundTexture;
    private final boolean textShadow;

    private EntityKilledToast(Component firstLine, Component secondLine, ResourceLocation entityType) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;

        this.displayTime = WhatDidIJustKillConfig.get().entity().timeout();
        this.backgroundTexture = WhatDidIJustKillConfig.get().entity().theme().getBackgroundTexture();
        this.textShadow = WhatDidIJustKillConfig.get().entity().theme() == ToastTheme.ADVANCEMENT;

        ResourceLocation location = makeTextureLoc(entityType);
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
        if (texture != MissingTextureAtlasSprite.getTexture()) {
            this.entityTexture = location;
        } else {
            WhatDidIJustKill.LOGGER.warn("Did not find icon for '{}' at '{}' using fallback icon.", entityType, location);
            this.entityTexture = UNKNOWN_ENTITY;
        }
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
        // TODO allow textures with different sizes
        graphics.blit(this.entityTexture, 8, 8, 0, 0, 16, 16, 16, 16);

        // remove toast when time is over
        return (double) displayTime >= this.displayTime * parent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
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

    private static ResourceLocation makeTextureLoc(ResourceLocation entityType) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityType);
        String category = type.getCategory().getName().toLowerCase();
        String path = String.format("textures/entity_icon/%s/%s.png", category, entityType.getPath());
        return new ResourceLocation(entityType.getNamespace(), path);
    }

}
