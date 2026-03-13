/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.variables;

import java.util.HashMap;
import java.util.Map;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.VariableType;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.external.EnchantLevelVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.AttackerNameVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.CurrentTimeVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.DamageCauseVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.DamageVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.LastRandomVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.PlayerNameVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.RawDamageVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.SystemTimeVariable;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.VictimNameVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class VariablesHandler {
    private final HashMap<String, VariableType> variableMap = new HashMap();

    public VariablesHandler(JavaPlugin javaPlugin) {
        this.register(javaPlugin, new SystemTimeVariable());
        this.register(javaPlugin, new AttackerNameVariable());
        this.register(javaPlugin, new VictimNameVariable());
        this.register(javaPlugin, new LastRandomVariable());
        this.register(javaPlugin, new DamageCauseVariable());
        this.register(javaPlugin, new RawDamageVariable());
        this.register(javaPlugin, new DamageVariable());
        this.register(javaPlugin, new PlayerNameVariable());
        this.register(javaPlugin, new CurrentTimeVariable());
        if (Bukkit.getPluginManager().isPluginEnabled("AdvancedEnchantments")) {
            this.register(javaPlugin, new EnchantLevelVariable());
        }
    }

    public void register(JavaPlugin javaPlugin, VariableType variableType) {
        this.register(javaPlugin, null, variableType);
    }

    public void register(JavaPlugin javaPlugin, String string, VariableType variableType) {
        if (!javaPlugin.equals((Object)EffectsHandler.getInstance())) {
            EffectsHandler.getInstance().getLogger().info(javaPlugin.getName() + " register a new variable: " + variableType.getVariable());
        }
        this.variableMap.put(variableType.getVariable(), variableType);
    }

    public String parseEffectLine(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        for (Map.Entry<String, VariableType> entry : this.variableMap.entrySet()) {
            if (!string.contains(entry.getKey())) continue;
            string = entry.getValue().parse(string, livingEntity, executionTask);
        }
        return string;
    }
}

