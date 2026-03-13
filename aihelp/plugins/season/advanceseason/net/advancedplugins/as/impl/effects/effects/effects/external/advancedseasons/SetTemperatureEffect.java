/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.external.advancedseasons;

import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.temperature.TemperatureHandler;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetTemperatureEffect
extends AdvancedEffect {
    public SetTemperatureEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SET_TEMPERATURE", "Increase the player's temperature", "%e:<AMOUNT (can be negative)>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        ActionExecutionBuilder actionExecutionBuilder = executionTask.getBuilder();
        LivingEntity livingEntity = actionExecutionBuilder.getMain();
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        Player player = (Player)livingEntity;
        TemperatureHandler temperatureHandler = Core.getTemperatureHandler();
        int n = temperatureHandler.getPlayerTemperature(player);
        int n2 = Integer.parseInt(stringArray[0]);
        temperatureHandler.setTemperature(player, n + n2);
        return super.executeEffect(executionTask, location, stringArray);
    }
}

