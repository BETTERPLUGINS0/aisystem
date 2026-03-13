/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.reader;

import java.util.List;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;

public interface AbilitiesReader {
    public List<AdvancedAbility> getRawAbilities(ActionExecutionBuilder var1);
}

