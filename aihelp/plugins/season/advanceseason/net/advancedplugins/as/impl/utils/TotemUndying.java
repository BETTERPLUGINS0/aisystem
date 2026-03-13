/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.EntityEffect
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityResurrectEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class TotemUndying {
    public boolean playEffect(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot, int n, int n2, int n3, boolean bl) {
        Object object;
        if (bl) {
            object = new EntityResurrectEvent(livingEntity, equipmentSlot);
            Bukkit.getPluginManager().callEvent((Event)object);
            if (object.isCancelled()) {
                return false;
            }
        }
        object = new ArrayList(livingEntity.getActivePotionEffects());
        object.forEach(potionEffect -> livingEntity.removePotionEffect(potionEffect.getType()));
        livingEntity.playEffect(EntityEffect.TOTEM_RESURRECT);
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, n, 2));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, n2, 1));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, n3, 2));
        return true;
    }
}

