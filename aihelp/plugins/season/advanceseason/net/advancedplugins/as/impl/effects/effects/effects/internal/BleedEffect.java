/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BleedEffect
extends AdvancedEffect {
    public BleedEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BLEED", "Steal entity bleed", "%e:<DAMAGE>:<TICKS>:<INTERVAL_TICKS>");
    }

    @Override
    public boolean executeEffect(final ExecutionTask executionTask, final LivingEntity livingEntity, String[] stringArray) {
        final double d = stringArray.length >= 1 ? Double.parseDouble(stringArray[0]) : 1.0;
        final int n = stringArray.length >= 2 ? ASManager.parseInt(stringArray[1]) : 5;
        int n2 = stringArray.length >= 3 ? ASManager.parseInt(stringArray[2]) : 20;
        new BukkitRunnable(){
            private int loop = 0;
            private final long lastDeath = livingEntity.getTicksLived();

            public void run() {
                if (livingEntity.isDead() || (long)livingEntity.getTicksLived() < this.lastDeath) {
                    this.cancel();
                    return;
                }
                if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
                    livingEntity.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                }
                executionTask.getDamageHandler().damage((Damageable)livingEntity, (Entity)BleedEffect.this.getOtherEntity(livingEntity, executionTask), d);
                if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
                    livingEntity.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
                }
                Location location = livingEntity.getLocation();
                ASManager.playEffect(Bukkit.getVersion().contains("1.8") ? "LAVADRIP" : "DRIP_LAVA", 0.0f, 10, location);
                location.getWorld().playEffect(location.add(0.0, 0.8, 0.0), Effect.STEP_SOUND, (Object)Material.REDSTONE_BLOCK);
                ++this.loop;
                if (this.loop >= n) {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)EffectsHandler.getInstance(), (long)n2, (long)n2);
        return true;
    }
}

