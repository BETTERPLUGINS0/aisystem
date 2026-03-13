/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.commands.argument;

import com.google.common.collect.Maps;
import java.util.Map;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentType;

public class ArgumentHandler {
    private static Map<Class<?>, ArgumentType<?>> argumentTypes = Maps.newHashMap();

    public static void register(Class<?> clazz, ArgumentType<?> argumentType) {
        argumentTypes.put(clazz, argumentType);
    }

    public static <T> ArgumentType<T> getArgumentType(Class<?> clazz) {
        return argumentTypes.get(clazz);
    }
}

