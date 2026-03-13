/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplodeEffect
extends AdvancedEffect {
    public ExplodeEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "EXPLODE", "Create an explosion", "%e:<POWER>:<FIRE (true/false)>:<BREAK BLOCKS (true/false>");
        this.addArgument(0, Float.class);
        this.addArgument(1, Boolean.class);
        this.addArgument(2, Boolean.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        World world = location.getWorld();
        float f = Float.parseFloat(stringArray[0]);
        boolean bl = Boolean.parseBoolean(stringArray[1]);
        boolean bl2 = Boolean.parseBoolean(stringArray[2]);
        world.createExplosion(location.getX(), location.getY(), location.getZ(), f, bl, bl2);
        return true;
    }
}

