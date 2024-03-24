package com.tristankechlo.whatdidijustkill;

import java.nio.file.Path;

public interface IPlatformHelper {

    public static final IPlatformHelper INSTANCE = WhatDidIJustKill.load(IPlatformHelper.class);

    Path getConfigDirectory();

    boolean isModLoaded(String modid);

}
