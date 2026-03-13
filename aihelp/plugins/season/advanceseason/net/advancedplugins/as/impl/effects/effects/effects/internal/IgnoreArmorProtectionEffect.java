/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class IgnoreArmorProtectionEffect
extends AdvancedEffect {
    public IgnoreArmorProtectionEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "IGNORE_ARMOR_PROTECTION", "Ignores armor damage reduction", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent)executionTask.getBuilder().getEvent();
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity)) {
            return false;
        }
        LivingEntity livingEntity2 = (LivingEntity)entity;
        LivingEntity livingEntity3 = (LivingEntity)entityDamageByEntityEvent.getEntity();
        executionTask.getDamageHandler().damageIgnoringArmor((Damageable)livingEntity3, livingEntity3, livingEntity2, entityDamageByEntityEvent.getDamage());
        entityDamageByEntityEvent.setDamage(0.0);
        return true;
    }
}

