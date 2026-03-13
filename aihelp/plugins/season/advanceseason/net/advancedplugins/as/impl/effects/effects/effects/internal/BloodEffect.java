/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class BloodEffect
extends AdvancedEffect {
    public BloodEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BLOOD", "Display blood effect", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        ASManager.playEffect(Bukkit.getVersion().contains("1.8") ? "LAVADRIP" : "DRIP_LAVA", 0.0f, 10, location);
        location.getWorld().playEffect(location.clone().add(0.0, 0.8, 0.0), Effect.STEP_SOUND, (Object)Material.REDSTONE_BLOCK);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
    }
}

