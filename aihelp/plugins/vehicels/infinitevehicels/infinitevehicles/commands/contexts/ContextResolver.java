package me.PM2.infinitevehicles.commands.contexts;

import me.PM2.infinitevehicles.commands.CommandExecutionContext;
import me.PM2.infinitevehicles.commands.CommandIssuer;
import me.PM2.infinitevehicles.commands.InvalidCommandArgument;

@FunctionalInterface
public interface ContextResolver<T, C extends CommandExecutionContext<?, ? extends CommandIssuer>> {
   T getContext(C c) throws InvalidCommandArgument;
}
