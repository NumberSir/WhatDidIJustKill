package com.tristankechlo.whatdidijustkill.config.types;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum FormatOption implements StringRepresentable {

    KILLED("KILLED", (theme, entityName, entityType, distance) ->
            Component.translatable("screen.whatdidijustkill.killed", entityName).withStyle(theme.getColorText())
    ),

    KILLED_DISTANCE("KILLED_DISTANCE", (theme, entityName, entityType, distance) ->
            Component.translatable("screen.whatdidijustkill.killed.distance", entityName, distance).withStyle(theme.getColorText())
    ),

    DISTANCE("DISTANCE", (theme, entityName, entityType, distance) ->
            Component.translatable("screen.whatdidijustkill.distance", distance).withStyle(theme.getColorText())
    ),

    ENTITY_TYPE("ENTITY_TYPE", (theme, entityName, entityType, distance) ->
            Component.literal(entityType.toString()).withStyle(theme.getColorEntityType())
    ),

    NONE("NONE", (theme, entityName, entityType, distance) -> null);

    public static final Codec<FormatOption> CODEC = StringRepresentable.fromEnum(FormatOption::values);
    private final LineFormatter formatter;
    private final String key;

    FormatOption(String key, LineFormatter formatter) {
        this.key = key;
        this.formatter = formatter;
    }

    public MutableComponent makeLine(ToastTheme theme, Component entityName, ResourceLocation entityType, double distance) {
        return this.formatter.format(theme, entityName, entityType, distance);
    }

    @Override
    public String getSerializedName() {
        return this.key;
    }

    @FunctionalInterface
    private interface LineFormatter {

        MutableComponent format(ToastTheme theme, Component entityName, ResourceLocation entityType, double distance);

    }

}
