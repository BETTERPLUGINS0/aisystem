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
import net.advancedplugins.as.impl.effects.effects.variables.DynamicVariable;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class InvertVariableEffect
extends AdvancedEffect {
    public InvertVariableEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "INVERT_VARIABLE", "Invert boolean's status between true/false", "%e:<NAME>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = stringArray[0];
        String string2 = DynamicVariable.getCustomVariables().getOrDefault(string, "false");
        if (!string2.equalsIgnoreCase("true") & !string2.equalsIgnoreCase("false")) {
            this.warn("Invalid value for " + ASManager.join(stringArray, "") + " effect - must be a boolean.");
            return true;
        }
        DynamicVariable.getCustomVariables().put(string, "" + !Boolean.parseBoolean(string2));
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        return this.executeEffect(executionTask, executionTask.getBuilder().getMain(), stringArray);
    }
}

