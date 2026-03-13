/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.commands.Command;
import net.advancedplugins.as.impl.utils.commands.SubCommand;
import net.advancedplugins.as.impl.utils.commands.argument.Argument;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentHandler;
import net.advancedplugins.as.impl.utils.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SimpleCommand<T extends CommandSender>
extends Command<T> {
    private final String command;
    private Integer pageCount;
    private final int COMMANDS_PER_PAGE = 9;
    private LinkedList<SubCommand<? extends CommandSender>> subCommands = new LinkedList();
    private List<Argument<?>> arguments = new ArrayList();
    private LinkedList<ShowcaseCommand> showcaseCommands = new LinkedList();

    public SimpleCommand(JavaPlugin javaPlugin, String string, String string2, boolean bl) {
        super(javaPlugin, string2, bl);
        this.command = string;
    }

    public SimpleCommand(JavaPlugin javaPlugin, String string, boolean bl) {
        this(javaPlugin, string, "", bl);
    }

    public SimpleCommand(JavaPlugin javaPlugin, String string, String string2) {
        this(javaPlugin, string, string2, true);
    }

    public SimpleCommand(JavaPlugin javaPlugin, String string) {
        this(javaPlugin, string, true);
    }

    public void setSubCommands(LinkedList<SubCommand<? extends CommandSender>> linkedList) {
        this.subCommands = linkedList;
    }

    public void addShowcaseCommand(String string, String string2) {
        this.showcaseCommands.add(new ShowcaseCommand(string, string2));
    }

    protected void setSubCommands(SubCommand<? extends CommandSender> ... subCommandArray) {
        this.subCommands.addAll(Arrays.asList(subCommandArray));
    }

    public void sendHelpMessage(Plugin plugin, CommandSender commandSender) {
        PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
        Text.sendMessage(commandSender, "&f".concat(pluginDescriptionFile.getName()).concat(" &7v").concat(pluginDescriptionFile.getVersion()));
        Text.sendMessage(commandSender, "&7Use &f&n".concat(this.command).concat(" to view usage information."));
    }

    public void sendHelpPage(CommandSender commandSender, String string, String[] stringArray) {
        int n = (stringArray.length == 0 ? 0 : (StringUtils.isNumeric(stringArray[0]) ? Math.max(0, Integer.parseInt(stringArray[0])) : 1)) - 1;
        int n2 = this.subCommands.size();
        if (this.pageCount == null) {
            this.pageCount = (int)Math.ceil((float)n2 / 9.0f);
        }
        if ((n = Math.min(Math.max(0, n), this.pageCount)) + 1 > this.pageCount) {
            n = this.pageCount - 1;
        }
        PluginDescriptionFile pluginDescriptionFile = this.plugin.getDescription();
        Text.sendMessage(commandSender, string + "[<] &8+-------< " + string + "&l" + pluginDescriptionFile.getName().concat(" &7Page " + (n + 1) + "/" + this.pageCount) + " &8>-------+ " + string + "[>]");
        Text.sendMessage(commandSender, " ");
        if (n == 0 && !this.showcaseCommands.isEmpty()) {
            for (ShowcaseCommand object : this.showcaseCommands) {
                Text.sendMessage(commandSender, "  /" + object.name + " &8-&e " + object.description);
            }
        }
        for (SubCommand subCommand : this.subCommands.subList(n * 9, Math.min(n2, (n + 1) * 9))) {
            Text.sendMessage(commandSender, "  " + subCommand.getFormatted(this.command));
        }
        Text.sendMessage(commandSender, " ");
        Text.sendMessage(commandSender, "  &2<> &f- Required Arguments&7; &9[] &f- Optional Arguments");
        Text.sendMessage(commandSender, string + "[<] &8+-------< " + string + "&l" + pluginDescriptionFile.getName().concat(" &7v" + pluginDescriptionFile.getVersion() + " &8>-------+ " + string + "[>]"));
    }

    public void sendUsage(CommandSender commandSender) {
        Text.sendMessage(commandSender, Text.modify("&cUsage: " + this.getFormatted()));
    }

    public String getFormatted() {
        StringBuilder stringBuilder = new StringBuilder().append("/").append(this.command).append(" ");
        for (Argument<?> argument : this.arguments) {
            if (argument.getType() == null) {
                stringBuilder.append(argument.getArgument());
            } else if (argument.isOptional()) {
                stringBuilder.append("[").append(argument.getArgument()).append("]");
            } else {
                stringBuilder.append("<").append(argument.getArgument()).append(">");
            }
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public void setArguments(List<Argument<?>> list) {
        this.arguments = list;
    }

    public void addFlat(String string) {
        this.arguments.add(new Argument(null, string, new String[0]));
    }

    public void addFlatWithAliases(String string, String ... stringArray) {
        this.arguments.add(new Argument(null, string, stringArray));
    }

    public void addFlats(String ... stringArray) {
        for (String string : stringArray) {
            this.addFlat(string);
        }
    }

    protected <S> Argument<S> addArgument(Class<S> clazz, String string, String ... stringArray) {
        if (string.equalsIgnoreCase("player")) {
            return this.addArgument(clazz, string, (Function<CommandSender, Collection<String>>)null, stringArray);
        }
        Argument argument = new Argument(ArgumentHandler.getArgumentType(clazz), string, stringArray);
        this.arguments.add(argument);
        return argument;
    }

    protected <S> Argument<S> addArgument(Class<S> clazz, String string, Function<CommandSender, Collection<String>> function, String ... stringArray) {
        if (string.equalsIgnoreCase("player")) {
            function = commandSender -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        Argument argument = new Argument(ArgumentHandler.getArgumentType(clazz), string, function, stringArray);
        this.arguments.add(argument);
        return argument;
    }

    public int getArgumentsSize() {
        return (int)this.arguments.stream().filter(argument -> !argument.isOptional()).count();
    }

    public int getArgumentsSizeReal() {
        return this.arguments.size();
    }

    public <U> U parseArgument(String[] stringArray, int n) {
        return this.parseArgument(stringArray, n, null);
    }

    public <U> U parseArgument(String[] stringArray, int n, U u) {
        String string;
        String string2 = string = stringArray.length - 1 < n ? null : stringArray[n];
        if (string == null) {
            return u;
        }
        return (U)this.arguments.get(n).getType().parse(string);
    }

    public boolean isMatch(String[] stringArray) {
        return this.isMatchUntilIndex(stringArray, stringArray.length);
    }

    public String[] getEnd(String[] stringArray) {
        LinkedHashSet<String> linkedHashSet = Sets.newLinkedHashSet();
        for (int i = 0; i < stringArray.length; ++i) {
            if (i < this.arguments.size() - 1) continue;
            linkedHashSet.add(stringArray[i]);
        }
        return linkedHashSet.toArray(new String[0]);
    }

    public boolean isMatchUntilIndex(String[] stringArray, int n) {
        for (int i = 0; i < n; ++i) {
            if (this.isArgumentValid(stringArray, i)) continue;
            return false;
        }
        return true;
    }

    public Collection<String> tabCompletionSuggestion(CommandSender commandSender, int n) {
        if (n > this.arguments.size() - 1) {
            return Lists.newArrayList();
        }
        return this.arguments.get(n).getOnTabComplete().apply(commandSender);
    }

    private boolean isArgumentValid(String[] stringArray, int n) {
        if (this.getArgumentsSize() - 1 < n) {
            return false;
        }
        Argument<?> argument = this.arguments.get(n);
        if (argument.getType() == null) {
            String string = stringArray[n];
            for (String string2 : argument.getAliases()) {
                if (!string.equalsIgnoreCase(string2)) continue;
                return true;
            }
            return stringArray[n].equalsIgnoreCase(argument.getArgument());
        }
        return true;
    }

    public String getCommand() {
        return this.command;
    }

    public LinkedList<SubCommand<? extends CommandSender>> getSubCommands() {
        return this.subCommands;
    }

    public List<Argument<?>> getArguments() {
        return this.arguments;
    }

    class ShowcaseCommand {
        private String name;
        private String description;

        public ShowcaseCommand(String string, String string2) {
            this.name = string;
            this.description = string2;
        }
    }
}

