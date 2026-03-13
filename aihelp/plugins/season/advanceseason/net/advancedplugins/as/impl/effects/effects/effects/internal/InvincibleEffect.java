/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InvincibleEffect
extends AdvancedEffect {
    private final List<UUID> entityMap = new ArrayList<UUID>();

    public InvincibleEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "INVINCIBLE", "Toggle entity's invincibility", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (executionTask.getBuilder().isPermanent()) {
            if (executionTask.getBuilder().isRemoved()) {
                livingEntity.setInvulnerable(false);
                this.entityMap.remove(livingEntity.getUniqueId());
                return true;
            }
            livingEntity.setInvulnerable(true);
            this.entityMap.add(livingEntity.getUniqueId());
            return true;
        }
        if (livingEntity.isInvulnerable()) {
            livingEntity.setInvulnerable(false);
            this.entityMap.remove(livingEntity.getUniqueId());
        } else {
            livingEntity.setInvulnerable(true);
            this.entityMap.add(livingEntity.getUniqueId());
        }
        return true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (!this.entityMap.contains(entityDamageEvent.getEntity().getUniqueId())) {
            return;
        }
        entityDamageEvent.setCancelled(true);
    }
}

