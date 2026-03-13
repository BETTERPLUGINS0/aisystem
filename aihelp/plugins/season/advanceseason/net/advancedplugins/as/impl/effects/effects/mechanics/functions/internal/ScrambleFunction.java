/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import org.bukkit.entity.LivingEntity;

public class ScrambleFunction
implements FunctionType {
    @Override
    public String getName() {
        return "scramble";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        return this.scrambleString(string);
    }

    private String scrambleString(String string) {
        ArrayList<Character> arrayList = new ArrayList<Character>();
        for (char c : string.toCharArray()) {
            arrayList.add(Character.valueOf(c));
        }
        Collections.shuffle(arrayList);
        Object object = new StringBuilder();
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            int n = ((Character)iterator.next()).charValue();
            ((StringBuilder)object).append((char)n);
        }
        return ((StringBuilder)object).toString();
    }
}

