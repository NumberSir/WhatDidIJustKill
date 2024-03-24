package com.tristankechlo.whatdidijustkill.mixin;

import com.tristankechlo.whatdidijustkill.network.EntityKilledPacket;
import com.tristankechlo.whatdidijustkill.network.IPacketHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
        Entity causingEntity = source.getEntity(); // entity doing the action
        Entity directEntity = source.getDirectEntity(); // arrow / player / potion / ...
        Component entityName = self.getDisplayName();
        ResourceLocation entityType = BuiltInRegistries.ENTITY_TYPE.getKey(self.getType());

        if (causingEntity == null && directEntity == null) {
            LivingEntity killCredit = this.getKillCredit(); // the entity that got the kill credited
            if (killCredit instanceof ServerPlayer player) {
                IPacketHandler.INSTANCE.sendPacketEntityKilled(player, new EntityKilledPacket(entityName, entityType));
            }
        } else if (causingEntity instanceof ServerPlayer player) {
            IPacketHandler.INSTANCE.sendPacketEntityKilled(player, new EntityKilledPacket(entityName, entityType));
        }
    }

}
