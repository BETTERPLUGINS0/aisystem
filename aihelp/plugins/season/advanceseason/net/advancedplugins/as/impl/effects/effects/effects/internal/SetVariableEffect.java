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
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class SetVariableEffect
extends AdvancedEffect {
    public SetVariableEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SET_VARIABLE", "Set a custom variable", "%e:<NAME>:<VALUE>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        boolean bl;
        if (stringArray.length > 2 && !(bl = Boolean.parseBoolean(stringArray[2])) && DynamicVariable.getCustomVariables().containsKey(stringArray[0])) {
            return true;
        }
        String string = stringArray[1];
        String string2 = stringArray[0];
        DynamicVariable.getCustomVariables().put(string2, string);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        return this.executeEffect(executionTask, executionTask.getBuilder().getMain(), stringArray);
    }
}

