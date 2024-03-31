package com.tristankechlo.whatdidijustkill.config.types;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ToastTheme implements StringRepresentable {

    ADVANCEMENT("ADVANCEMENT", 0, ChatFormatting.GRAY, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE),
    RECIPE("RECIPE", 32, ChatFormatting.BLACK, ChatFormatting.DARK_GRAY, ChatFormatting.DARK_GRAY),
    TUTORIAL("TUTORIAL", 96, ChatFormatting.BLACK, ChatFormatting.DARK_GRAY, ChatFormatting.DARK_GRAY);

    public static final Codec<ToastTheme> CODEC = StringRepresentable.fromEnum(ToastTheme::values, ToastTheme::byName);
    private static final Map<String, ToastTheme> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(ToastTheme::getSerializedName, ($$0) -> $$0));
    private final String key;
    private final int offsetY;
    private final ChatFormatting colorText; // color to use for most of the text
    private final ChatFormatting colorEntityType; // color to use when rendering the entity_type
    private final ChatFormatting colorHighlight; // color to use for highlighting the entity name

    ToastTheme(String key, int offsetY, ChatFormatting colorText, ChatFormatting colorEntityType, ChatFormatting colorHighlight) {
        this.key = key;
        this.offsetY = offsetY;
        this.colorText = colorText;
        this.colorEntityType = colorEntityType;
        this.colorHighlight = colorHighlight;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public ChatFormatting getColorText() {
        return colorText;
    }

    public ChatFormatting getColorEntityType() {
        return colorEntityType;
    }

    public ChatFormatting getColorHighlight() {
        return colorHighlight;
    }

    @Override
    public String getSerializedName() {
        return this.key;
    }

    public static ToastTheme byName(String key) {
        return BY_NAME.get(key);
    }

}
