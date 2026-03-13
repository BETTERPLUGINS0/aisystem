/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import org.bukkit.entity.LivingEntity;

public class RandomWordFunction
implements FunctionType {
    @Override
    public String getName() {
        return "random word";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        List<String> list = Arrays.asList(string.split(","));
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}

