package com.tristankechlo.whatdidijustkill.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tristankechlo.whatdidijustkill.IPlatformHelper;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Optional;

public record WhatDidIJustKillConfig(boolean enabled, boolean onlyNamedMobs, int timeout, boolean hideEntityType, LongDistance longDistance, List<Either<ResourceLocation, ModWildcard>> excludes) {

    public static final WhatDidIJustKillConfig DEFAULT = new WhatDidIJustKillConfig(true, false, 2000, false, LongDistance.DEFAULT, List.of(Either.left(new ResourceLocation("bat"))));

    public static final Codec<WhatDidIJustKillConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("enabled").forGetter(WhatDidIJustKillConfig::enabled),
                    Codec.BOOL.fieldOf("only_named_mobs").forGetter(WhatDidIJustKillConfig::onlyNamedMobs),
                    Codec.intRange(250, 10000).fieldOf("timeout").forGetter(WhatDidIJustKillConfig::timeout),
                    Codec.BOOL.fieldOf("hide_entity_type").forGetter(WhatDidIJustKillConfig::hideEntityType),
                    LongDistance.CODEC.fieldOf("long_distance").forGetter(WhatDidIJustKillConfig::longDistance),
                    Codec.either(ResourceLocation.CODEC, ModWildcard.CODEC).listOf().fieldOf("excludes").forGetter(WhatDidIJustKillConfig::excludes)
            ).apply(instance, WhatDidIJustKillConfig::new)
    );

    private static WhatDidIJustKillConfig INSTANCE = DEFAULT;

    public static WhatDidIJustKillConfig get() {
        return INSTANCE;
    }

    public static void setToDefault() {
        INSTANCE = WhatDidIJustKillConfig.DEFAULT;
    }

    public static JsonElement toJson() {
        DataResult<JsonElement> result = WhatDidIJustKillConfig.CODEC.encodeStart(JsonOps.INSTANCE, INSTANCE);
        return result
                .resultOrPartial((string) -> {
                    WhatDidIJustKill.LOGGER.error("An error occurred while attempting to serialize the config.");
                    WhatDidIJustKill.LOGGER.error("==> {}", string);
                })
                .orElseThrow(() -> new JsonParseException("The serialized config appears to be null."));
    }

    public static void fromJson(JsonElement json, Runnable setUnSuccessful) {
        DataResult<WhatDidIJustKillConfig> result = WhatDidIJustKillConfig.CODEC.parse(JsonOps.INSTANCE, json);
        Optional<WhatDidIJustKillConfig> config = result
                .resultOrPartial((string) -> {
                    WhatDidIJustKill.LOGGER.error("An error occurred while attempting to deserialize the config.");
                    WhatDidIJustKill.LOGGER.error("==> {}", string);
                    setUnSuccessful.run();
                });
        if (config.isEmpty()) {
            setUnSuccessful.run();
            return;
        }
        INSTANCE = config.get();
    }

    public boolean isEntityExcluded(ResourceLocation entityType) {
        for (Either<ResourceLocation, ModWildcard> element : excludes()) {
            Optional<ResourceLocation> string = element.left();
            if (string.isPresent() && string.get().equals(entityType)) {
                return true;
            }

            Optional<ModWildcard> wildcard = element.right();
            if (wildcard.isPresent() && wildcard.get().modid().equalsIgnoreCase(entityType.getNamespace())) {
                return true;
            }
        }
        return false;
    }

    public record LongDistance(boolean alwaysShow, double threshold) {

        private static final LongDistance DEFAULT = new LongDistance(true, 10.0);

        private static final Codec<LongDistance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.BOOL.fieldOf("always_show").forGetter(LongDistance::alwaysShow),
                        Codec.doubleRange(0, Double.MAX_VALUE).fieldOf("threshold").forGetter(LongDistance::threshold)
                ).apply(instance, LongDistance::new)
        );

    }

    public record ModWildcard(String modid) {

        private static final Codec<ModWildcard> CODEC = ExtraCodecs.validate(RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.STRING.fieldOf("wildcard").forGetter(ModWildcard::modid)
                ).apply(instance, ModWildcard::new)
        ), ModWildcard::verify);

        private static DataResult<ModWildcard> verify(ModWildcard wildcard) {
            if (!IPlatformHelper.INSTANCE.isModLoaded(wildcard.modid())) {
                return DataResult.error(() -> String.format("Not a valid wildcard: \"%s\"", wildcard.modid()));
            }
            return DataResult.success(wildcard);
        }

    }

}
