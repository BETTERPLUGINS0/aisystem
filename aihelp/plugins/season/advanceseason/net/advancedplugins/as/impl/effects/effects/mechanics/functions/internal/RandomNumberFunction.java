/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;

public class RandomNumberFunction
implements FunctionType {
    public static int lastRandom = 0;
    Pattern pattern = Pattern.compile("(-?\\d+)(?:-(-?\\d+))?");

    @Override
    public String getName() {
        return "random number";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        int n;
        block5: {
            Matcher matcher = this.pattern.matcher(string);
            if (matcher.find()) {
                try {
                    int n2 = ASManager.parseInt(matcher.group(1));
                    String string2 = matcher.group(2);
                    if (string2 != null) {
                        int n3 = ASManager.parseInt(string2);
                        n = ThreadLocalRandom.current().nextInt(n2, n3 + 1);
                        break block5;
                    }
                    n = n2;
                } catch (Exception exception) {
                    n = 0;
                }
            } else {
                n = 0;
            }
        }
        lastRandom = n;
        return Integer.toString(n);
    }
}

