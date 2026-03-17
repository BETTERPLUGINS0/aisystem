package me.PM2.infinitevehicles.commands.contexts;

import me.PM2.infinitevehicles.commands.CommandExecutionContext;
import me.PM2.infinitevehicles.commands.CommandIssuer;

/** @deprecated */
@Deprecated
public interface SenderAwareContextResolver<T, C extends CommandExecutionContext<?, ? extends CommandIssuer>> extends IssuerAwareContextResolver<T, C> {
}
