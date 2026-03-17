/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandParameter;
import co.aikar.commands.RegisteredCommand;

public class HelpEntry {
    private final CommandHelp commandHelp;
    private final RegisteredCommand command;
    private int searchScore = 1;

    HelpEntry(CommandHelp commandHelp, RegisteredCommand registeredCommand) {
        this.commandHelp = commandHelp;
        this.command = registeredCommand;
    }

    RegisteredCommand getRegisteredCommand() {
        return this.command;
    }

    public String getCommand() {
        return this.command.command;
    }

    public String getCommandPrefix() {
        return this.commandHelp.getCommandPrefix();
    }

    public String getParameterSyntax() {
        return this.getParameterSyntax(null);
    }

    public String getParameterSyntax(CommandIssuer commandIssuer) {
        String string = this.command.getSyntaxText(commandIssuer);
        return string != null ? string : "";
    }

    public String getDescription() {
        return this.command.getHelpText();
    }

    public void setSearchScore(int n) {
        this.searchScore = n;
    }

    public boolean shouldShow() {
        return this.searchScore > 0;
    }

    public int getSearchScore() {
        return this.searchScore;
    }

    public String getSearchTags() {
        return this.command.helpSearchTags;
    }

    public CommandParameter[] getParameters() {
        return this.command.parameters;
    }
}

