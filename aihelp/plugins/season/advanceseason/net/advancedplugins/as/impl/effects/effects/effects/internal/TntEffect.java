/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.List;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.PlayASound;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TntEffect
extends AdvancedEffect {
    public TntEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TNT", "Create a TNT explosion", "%e:[DAMAGE]:[RADIUS]");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        double d = stringArray.length > 0 ? Double.parseDouble(stringArray[0]) : 3.0;
        double d2 = stringArray.length > 1 ? Double.parseDouble(stringArray[1]) : 3.0;
        List list = location.getWorld().getNearbyEntities(location, d2, d2, d2).stream().filter(entity -> entity instanceof Damageable).map(entity -> (Damageable)entity).toList();
        list.forEach(damageable -> {
            if (damageable.getType().isAlive()) {
                if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
                    damageable.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                }
                executionTask.getDamageHandler().damage((Damageable)damageable, d);
                if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
                    damageable.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
                }
            }
        });
        PlayASound.playSound(MinecraftVersion.getVersionNumber() >= 190 ? "ENTITY_GENERIC_EXPLODE" : "EXPLOSION", location);
        ASManager.playEffect("EXPLOSION_HUGE", 0.0f, 3, location);
        return true;
    }
}

