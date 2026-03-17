package me.PM2.infinitevehicles.commands.contexts;

import me.PM2.infinitevehicles.commands.CommandExecutionContext;
import me.PM2.infinitevehicles.commands.CommandIssuer;

public interface OptionalContextResolver<T, C extends CommandExecutionContext<?, ? extends CommandIssuer>> extends ContextResolver<T, C> {
}
