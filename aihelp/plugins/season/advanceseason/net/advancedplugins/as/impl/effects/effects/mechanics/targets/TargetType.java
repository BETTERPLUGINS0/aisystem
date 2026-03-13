/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets;

import java.util.Collections;
import java.util.HashMap;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;

public class TargetType {
    private final String name;
    private String prefix = "@";

    public TargetType(String string) {
        this.name = string;
    }

    public TargetType setPrefix(String string) {
        this.prefix = string;
        return this;
    }

    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        return null;
    }

    public String replaceTargetFromEffect(String string) {
        String string2 = this.extractTargetString(string);
        String string3 = this.getPrefix() + this.name;
        if (string2 != null) {
            string3 = string3 + "{" + string2 + "}";
        }
        return string.replace(string3, "");
    }

    public String extractTargetString(String string) {
        for (String string2 : string.split(" ")) {
            if (!string2.startsWith(this.getPrefix() + this.name) || !string2.contains("{") || !string2.contains("}") || !(string2 = string2.replace(this.getPrefix() + this.name + "{", "").replace(this.getPrefix() + this.name, "")).endsWith("}")) continue;
            return string2.substring(0, string2.length() - 1);
        }
        return null;
    }

    public HashMap<TargetArgument, String> classifyTarget(String string) {
        HashMap<TargetArgument, String> hashMap = new HashMap<TargetArgument, String>();
        if (string == null) {
            return hashMap;
        }
        for (String string2 : string.split(",")) {
            String[] stringArray = string2.split("=");
            hashMap.put(TargetArgument.matchFromArg(stringArray[0]), stringArray[1]);
        }
        return hashMap;
    }

    public TargetResults noTarget() {
        return new TargetResults(Collections.emptyList());
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }
}

