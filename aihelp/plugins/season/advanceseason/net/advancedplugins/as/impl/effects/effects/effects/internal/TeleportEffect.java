/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportEffect
extends AdvancedEffect {
    public TeleportEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TELEPORT", "Teleport to location", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Location location2 = location.clone();
        Location location3 = executionTask.getBuilder().getMain().getLocation();
        location2.setYaw(location3.getYaw());
        location2.setPitch(location3.getPitch());
        executionTask.getBuilder().getMain().teleport(location2);
        return true;
    }
}

