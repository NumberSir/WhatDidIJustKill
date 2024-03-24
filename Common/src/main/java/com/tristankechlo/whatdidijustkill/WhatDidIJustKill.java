package com.tristankechlo.whatdidijustkill;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class WhatDidIJustKill {

    public static final String MOD_NAME = "WhatDidIJustKill";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final String MOD_ID = "whatdidijustkill";
    public static final String GITHUB_URL = "https://github.com/tristankechlo/WhatDidIJustKill";
    public static final String GITHUB_ISSUE_URL = GITHUB_URL + "/issues";
    public static final String DISCORD_URL = "https://discord.gg/bhUaWhq";
    public static final String CURSEFORGE_URL = "https://curseforge.com/minecraft/mc-mods/what-did-i-just-kill";
    public static final String MODRINTH_URL = "https://modrinth.com/mod/what-did-i-just-kill";

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    public static final ResourceLocation CHANNEL = new ResourceLocation(WhatDidIJustKill.MOD_ID, "entity_killed");

}
