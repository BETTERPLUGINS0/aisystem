/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class DisableKnockbackEffect
extends AdvancedEffect {
    private final HashMap<UUID, Long> entityMap = new HashMap();

    public DisableKnockbackEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DISABLE_KNOCKBACK", "Disable entity's knockback for time", "%e:<TICKS>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        this.entityMap.put(livingEntity.getUniqueId(), System.currentTimeMillis() + (long)n * 50L);
        return true;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (!(entityDamageEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityDamageEvent.getEntity();
        if (!this.entityMap.containsKey(livingEntity.getUniqueId())) {
            return;
        }
        long l = this.entityMap.get(livingEntity.getUniqueId());
        if (l - System.currentTimeMillis() <= 0L) {
            this.entityMap.remove(livingEntity.getUniqueId());
            return;
        }
        SchedulerUtils.runTaskLater(() -> livingEntity.setVelocity(new Vector(0.0f, 0.0f, 0.0f)));
    }
}

