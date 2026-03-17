/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandOperationContext;
import co.aikar.commands.CommandRouter;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.RootCommand;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForwardingCommand
extends BaseCommand {
    private final BaseCommand command;
    private final String[] baseArgs;
    private final RegisteredCommand regCommand;

    ForwardingCommand(BaseCommand baseCommand, RegisteredCommand registeredCommand, String[] stringArray) {
        this.regCommand = registeredCommand;
        this.commandName = baseCommand.commandName;
        this.command = baseCommand;
        this.baseArgs = stringArray;
        this.manager = baseCommand.manager;
        this.subCommands.put("__default", registeredCommand);
    }

    @Override
    public List<RegisteredCommand> getRegisteredCommands() {
        return Collections.singletonList(this.regCommand);
    }

    @Override
    public CommandOperationContext getLastCommandOperationContext() {
        return this.command.getLastCommandOperationContext();
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return this.command.getRequiredPermissions();
    }

    @Override
    public boolean hasPermission(Object object) {
        return this.command.hasPermission(object);
    }

    @Override
    public boolean requiresPermission(String string) {
        return this.command.requiresPermission(string);
    }

    @Override
    public boolean hasPermission(CommandIssuer commandIssuer) {
        return this.command.hasPermission(commandIssuer);
    }

    @Override
    public List<String> tabComplete(CommandIssuer commandIssuer, RootCommand rootCommand, String[] stringArray, boolean bl) {
        return this.command.tabComplete(commandIssuer, rootCommand, stringArray, bl);
    }

    @Override
    public void execute(CommandIssuer commandIssuer, CommandRouter.CommandRouteResult commandRouteResult) {
        commandRouteResult = new CommandRouter.CommandRouteResult(this.regCommand, commandRouteResult.args, ACFUtil.join(this.baseArgs), commandRouteResult.commandLabel);
        this.command.execute(commandIssuer, commandRouteResult);
    }

    BaseCommand getCommand() {
        return this.command;
    }
}

