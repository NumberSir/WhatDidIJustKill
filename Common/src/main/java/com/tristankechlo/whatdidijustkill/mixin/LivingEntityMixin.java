package com.tristankechlo.whatdidijustkill.mixin;

import com.tristankechlo.whatdidijustkill.network.IPacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected boolean dead;

    @Shadow
    public abstract LivingEntity getKillCredit();

    @Inject(method = "die", at = @At("HEAD"))
    private void onDeath$WhatDidIJustKill(DamageSource source, CallbackInfo ci) {
        LivingEntity self = ((LivingEntity) (Object) this);
        if (self.isRemoved() || this.dead || self.level().isClientSide()) {
            return;
        }
        Entity causingEntity = source.getEntity();
        Entity directEntity = source.getDirectEntity();
        LivingEntity killCredit = this.getKillCredit();
        if (killCredit instanceof ServerPlayer player) {
            IPacketHandler.INSTANCE.sendPacketEntityKilled(player);
        }
    }

}
