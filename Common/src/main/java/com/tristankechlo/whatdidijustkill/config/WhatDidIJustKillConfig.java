package com.tristankechlo.whatdidijustkill.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.types.EntityOptions;
import com.tristankechlo.whatdidijustkill.config.types.PlayerOptions;

import java.util.Optional;

public record WhatDidIJustKillConfig(PlayerOptions player, EntityOptions entity) {

    public static final WhatDidIJustKillConfig DEFAULT = new WhatDidIJustKillConfig(PlayerOptions.DEFAULT, EntityOptions.DEFAULT);

    public static final Codec<WhatDidIJustKillConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    PlayerOptions.CODEC.fieldOf("player").forGetter(WhatDidIJustKillConfig::player),
                    EntityOptions.CODEC.fieldOf("entity").forGetter(WhatDidIJustKillConfig::entity)
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

}
