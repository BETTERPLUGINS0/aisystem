/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets;

import java.util.Arrays;
import java.util.Locale;
import net.advancedplugins.as.impl.utils.ASManager;

public enum TargetArgument {
    IGNORE_TOOL("it", "ignoretool"),
    RADIUS("r", "radius"),
    RADIUS_CUSTOM("rc", "radiuscustom"),
    TARGET("t", "target"),
    LIMIT("l", "limit"),
    DISTANCE("d", "distance"),
    DEPTH("dp", "depth"),
    POINTS("p", "points"),
    X("x"),
    Y("y"),
    Z("z"),
    ANGLE("a", "angle"),
    MODE("m", "mode");

    private final String[] args;

    private TargetArgument(String ... stringArray) {
        this.args = stringArray;
    }

    public static TargetArgument matchFromArg(String string) {
        return Arrays.stream(TargetArgument.values()).filter(targetArgument -> ASManager.contains(string.toLowerCase(Locale.ROOT), targetArgument.args)).findAny().orElse(null);
    }
}

