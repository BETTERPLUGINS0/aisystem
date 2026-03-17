/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.contexts;

import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.InvalidCommandArgument;

@FunctionalInterface
public interface ContextResolver<T, C extends CommandExecutionContext<?, ? extends CommandIssuer>> {
    public T getContext(C var1) throws InvalidCommandArgument;
}

