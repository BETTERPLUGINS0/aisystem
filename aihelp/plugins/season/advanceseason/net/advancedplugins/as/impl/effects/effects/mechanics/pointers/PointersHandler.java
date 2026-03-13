/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.pointers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import org.bukkit.plugin.java.JavaPlugin;

public class PointersHandler {
    private final Pattern POINTER_PATTERN;

    public PointersHandler(JavaPlugin javaPlugin) {
        String string = EffectsHandler.getTriggerHandler().getTriggersAsString().stream().map(Pattern::quote).collect(Collectors.joining("|"));
        this.POINTER_PATTERN = Pattern.compile("~(!)?(" + string + ")\\b");
    }

    public String parseEffectLine(String string, String string2, ExecutionTask executionTask) {
        Matcher matcher = this.POINTER_PATTERN.matcher(string);
        while (matcher.find()) {
            String string3 = matcher.group().trim();
            boolean bl = string3.contains("~!");
            String string4 = string3.replaceAll("~!?", "");
            if (bl && string4.equals(string2) || !bl && !string4.equals(string2)) {
                return "";
            }
            string = string.replace(string3, "");
        }
        return string;
    }
}

