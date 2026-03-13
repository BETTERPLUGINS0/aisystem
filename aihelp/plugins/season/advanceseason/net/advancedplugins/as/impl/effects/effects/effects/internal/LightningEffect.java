/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningEffect
extends AdvancedEffect {
    public LightningEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "LIGHTNING", "Strike a lightning", "%e<REAL (true/false)>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        boolean bl = false;
        if (stringArray.length >= 1) {
            bl = Boolean.parseBoolean(stringArray[0]);
        }
        if (bl) {
            location.getWorld().strikeLightning(location);
        } else {
            location.getWorld().strikeLightningEffect(location);
        }
        return true;
    }
}

