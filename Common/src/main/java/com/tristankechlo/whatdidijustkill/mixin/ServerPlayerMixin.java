package com.tristankechlo.whatdidijustkill.mixin;

import com.tristankechlo.whatdidijustkill.network.IPacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void onDeath$WhatDidIJustKill(DamageSource source, CallbackInfo ci) {
        ServerPlayer self = ((ServerPlayer) (Object) this);
        if (self.isRemoved() || self.level().isClientSide()) {
            return;
        }

        Entity causingEntity = source.getEntity(); // entity doing the action

        if (causingEntity instanceof ServerPlayer player) {
            IPacketHandler.INSTANCE.sendPacketPlayerKilled(player, self);
        }
    }

}
