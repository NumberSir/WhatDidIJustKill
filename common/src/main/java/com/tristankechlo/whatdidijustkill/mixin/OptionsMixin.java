package com.tristankechlo.whatdidijustkill.mixin;

import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.client.ToastHandler;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin {

    @Inject(method = "processOptions", at = @At("HEAD"))
    private void processOptions(Options.FieldAccess access, CallbackInfo ci) {
        ToastHandler.toastsEnabled = access.process(WhatDidIJustKill.MOD_ID + ".toastsVisible", ToastHandler.toastsEnabled);
    }

}
