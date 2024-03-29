package com.tristankechlo.whatdidijustkill.config.types;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum FormatOption implements StringRepresentable {

    KILLED("KILLED"),
    KILLED_DISTANCE("KILLED_DISTANCE"),
    DISTANCE("DISTANCE"),
    ENTITY_TYPE("ENTITY_TYPE"),
    NONE("NONE");

    public static final Codec<FormatOption> CODEC = StringRepresentable.fromEnum(FormatOption::values);
    private final String key;

    FormatOption(String key) {
        this.key = key;
    }

    @Override
    public String getSerializedName() {
        return this.key;
    }

    public static MutableComponent makeLine(ToastTheme theme, FormatOption option, Component entityName, ResourceLocation entityType, double distance) {
        if (option == KILLED) {
            return Component.translatable("screen.whatdidijustkill.killed", entityName).withStyle(theme.getColorText());
        } else if (option == KILLED_DISTANCE) {
            return Component.translatable("screen.whatdidijustkill.killed.distance", entityName, distance).withStyle(theme.getColorText());
        } else if (option == DISTANCE) {
            return Component.translatable("screen.whatdidijustkill.distance", distance).withStyle(theme.getColorText());
        } else if (option == ENTITY_TYPE) {
            return Component.literal(entityType.toString()).withStyle(theme.getColorEntityType());
        }
        return null;
    }

}
