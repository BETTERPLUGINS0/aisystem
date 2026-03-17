/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandManager;
import co.aikar.commands.CommandRouter;
import co.aikar.commands.RegisteredCommand;
import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface RootCommand {
    public void addChild(BaseCommand var1);

    public CommandManager getManager();

    public SetMultimap<String, RegisteredCommand> getSubCommands();

    public List<BaseCommand> getChildren();

    public String getCommandName();

    default public void addChildShared(List<BaseCommand> children, SetMultimap<String, RegisteredCommand> subCommands, BaseCommand command) {
        command.subCommands.entries().forEach(e -> subCommands.put((String)e.getKey(), (RegisteredCommand)e.getValue()));
        if (!children.contains(command)) {
            children.add(command);
        }
    }

    default public String getUniquePermission() {
        HashSet<String> permissions = new HashSet<String>();
        for (BaseCommand child : this.getChildren()) {
            for (RegisteredCommand value : child.subCommands.values()) {
                Set<String> requiredPermissions = value.getRequiredPermissions();
                if (requiredPermissions.isEmpty()) {
                    return null;
                }
                permissions.addAll(requiredPermissions);
            }
        }
        return permissions.size() == 1 ? (String)permissions.iterator().next() : null;
    }

    default public boolean hasAnyPermission(CommandIssuer issuer) {
        List<BaseCommand> children = this.getChildren();
        if (children.isEmpty()) {
            return true;
        }
        for (BaseCommand child : children) {
            if (!child.hasPermission(issuer)) continue;
            for (RegisteredCommand value : child.getRegisteredCommands()) {
                if (!value.hasPermission(issuer)) continue;
                return true;
            }
        }
        return false;
    }

    default public BaseCommand execute(CommandIssuer sender, String commandLabel, String[] args) {
        CommandRouter router = this.getManager().getRouter();
        CommandRouter.RouteSearch search = router.routeCommand(this, commandLabel, args, false);
        BaseCommand defCommand = this.getDefCommand();
        if (search != null) {
            CommandRouter.CommandRouteResult result = router.matchCommand(search, false);
            if (result != null) {
                BaseCommand scope = result.cmd.scope;
                scope.execute(sender, result);
                return scope;
            }
            RegisteredCommand firstElement = ACFUtil.getFirstElement(search.commands);
            if (firstElement != null) {
                defCommand = firstElement.scope;
            }
        }
        defCommand.help(sender, args);
        return defCommand;
    }

    default public List<String> getTabCompletions(CommandIssuer sender, String alias, String[] args) {
        return this.getTabCompletions(sender, alias, args, false);
    }

    default public List<String> getTabCompletions(CommandIssuer sender, String alias, String[] args, boolean commandsOnly) {
        return this.getTabCompletions(sender, alias, args, commandsOnly, false);
    }

    default public List<String> getTabCompletions(CommandIssuer sender, String alias, String[] args, boolean commandsOnly, boolean isAsync) {
        HashSet completions = new HashSet();
        this.getChildren().forEach(child -> {
            if (!commandsOnly) {
                completions.addAll(child.tabComplete(sender, this, args, isAsync));
            }
            completions.addAll(child.getCommandsForCompletion(sender, args));
        });
        return new ArrayList<String>(completions);
    }

    default public RegisteredCommand getDefaultRegisteredCommand() {
        BaseCommand defCommand = this.getDefCommand();
        if (defCommand != null) {
            return defCommand.getDefaultRegisteredCommand();
        }
        return null;
    }

    default public BaseCommand getDefCommand() {
        return null;
    }

    default public String getDescription() {
        RegisteredCommand cmd = this.getDefaultRegisteredCommand();
        if (cmd != null) {
            return cmd.getHelpText();
        }
        BaseCommand defCommand = this.getDefCommand();
        if (defCommand != null && defCommand.description != null) {
            return defCommand.description;
        }
        return "";
    }

    default public String getUsage() {
        RegisteredCommand cmd = this.getDefaultRegisteredCommand();
        if (cmd != null) {
            return cmd.syntaxText != null ? cmd.syntaxText : "";
        }
        return "";
    }
}

