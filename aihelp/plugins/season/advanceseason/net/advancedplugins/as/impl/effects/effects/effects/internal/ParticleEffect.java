/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleEffect
extends AdvancedEffect {
    public ParticleEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PARTICLE", "Play particles at a location", "%e:<PARTICLE>:<AMOUNT>:<OFFSET>");
        this.addArgument(0, Particle.class);
        this.addArgument(1, Integer.class);
        this.addArgument(2, Double.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        String string = stringArray[0];
        int n = ASManager.parseInt(stringArray[1]);
        float f = Float.parseFloat(stringArray[2]);
        ASManager.playEffect(string, f, n, location);
        return true;
    }
}

