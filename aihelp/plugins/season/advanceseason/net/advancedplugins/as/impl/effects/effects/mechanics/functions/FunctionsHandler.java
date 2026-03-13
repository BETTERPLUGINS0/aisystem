/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.ChanceFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.ConditionFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.IfEndsFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.IfStartsFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.IntFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.MathFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.NiceFormatFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.RandomNumberFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.RandomWordFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.RoundFunction;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal.ScrambleFunction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class FunctionsHandler {
    private final LinkedHashMap<Pattern, FunctionType> functionMap = new LinkedHashMap();

    public FunctionsHandler(JavaPlugin javaPlugin) {
        this.register(javaPlugin, new RandomNumberFunction());
        this.register(javaPlugin, new RandomWordFunction());
        this.register(javaPlugin, new ChanceFunction());
        this.register(javaPlugin, new MathFunction());
        this.register(javaPlugin, new RoundFunction());
        this.register(javaPlugin, new IfEndsFunction());
        this.register(javaPlugin, new IfStartsFunction());
        this.register(javaPlugin, new ConditionFunction());
        this.register(javaPlugin, new NiceFormatFunction());
        this.register(javaPlugin, new ScrambleFunction());
        this.register(javaPlugin, new IntFunction());
    }

    public void register(JavaPlugin javaPlugin, FunctionType functionType) {
        if (!javaPlugin.equals((Object)EffectsHandler.getInstance())) {
            EffectsHandler.getInstance().getLogger().info(javaPlugin.getName() + " register a new function: " + functionType.getName());
        }
        String string = functionType.getName();
        this.functionMap.put(Pattern.compile("<" + string + ">(.+?)</" + string + ">", 32), functionType);
    }

    public String parseEffectLine(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        for (Map.Entry<Pattern, FunctionType> entry : this.functionMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(string);
            while (matcher.find()) {
                String string2 = matcher.group(1);
                String string3 = entry.getValue().parse(string2, livingEntity, executionTask);
                string = string.replace("<" + entry.getValue().getName() + ">" + string2 + "</" + entry.getValue().getName() + ">", string3);
            }
        }
        return string;
    }
}

