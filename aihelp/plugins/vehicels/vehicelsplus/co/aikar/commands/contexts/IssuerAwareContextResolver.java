/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.contexts;

import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.contexts.ContextResolver;

public interface IssuerAwareContextResolver<T, C extends CommandExecutionContext<?, ? extends CommandIssuer>>
extends ContextResolver<T, C> {
}

