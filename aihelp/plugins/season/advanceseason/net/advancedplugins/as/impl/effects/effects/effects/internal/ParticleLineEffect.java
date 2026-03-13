/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ParticleLineEffect
extends AdvancedEffect {
    public ParticleLineEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PARTICLE_LINE", "Draw particles line from one entity to other", "%e:<PARTICLE>:<AMOUNT>:<POINTS>");
        this.addArgument(0, Particle.class);
        this.addArgument(1, Integer.class);
        this.addArgument(2, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        this.executeEffect(executionTask, livingEntity.getEyeLocation(), stringArray);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        String string = stringArray[0];
        int n = ASManager.parseInt(stringArray[1]);
        int n2 = ASManager.parseInt(stringArray[2]);
        Location location2 = executionTask.getBuilder().getMain().getLocation().equals((Object)location) ? this.getOtherEntity(executionTask.getBuilder().getMain(), executionTask).getLocation() : executionTask.getBuilder().getMain().getLocation();
        double d = location2.distance(location) / (double)n2;
        for (int i = 0; i < n2; ++i) {
            Location location3 = location2.clone();
            Vector vector = location.toVector().subtract(location2.toVector()).normalize();
            Vector vector2 = vector.multiply((double)i * d);
            location3.add(vector2.getX(), vector2.getY(), vector2.getZ());
            ASManager.playEffect(string, 0.0f, n, location3);
        }
        return true;
    }
}

