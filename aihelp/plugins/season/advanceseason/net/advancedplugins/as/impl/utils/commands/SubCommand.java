/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.commands.Command;
import net.advancedplugins.as.impl.utils.commands.argument.Argument;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SubCommand<T extends CommandSender>
extends Command<T> {
    private final boolean endless;
    private List<Argument<?>> arguments = Lists.newArrayList();
    private boolean inheritPermission;
    private String description;

    public SubCommand(JavaPlugin javaPlugin, String string, boolean bl) {
        this(javaPlugin, string, bl, false);
    }

    public SubCommand(JavaPlugin javaPlugin, String string, boolean bl, boolean bl2) {
        super(javaPlugin, string, bl);
        this.endless = bl2;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public String getDescription() {
        return this.description;
    }

    public SubCommand(JavaPlugin javaPlugin) {
        this(javaPlugin, "", true);
    }

    public SubCommand(JavaPlugin javaPlugin, String string) {
        this(javaPlugin, string, true);
    }

    public SubCommand(JavaPlugin javaPlugin, boolean bl) {
        this(javaPlugin, "", bl);
    }

    protected void inheritPermission() {
        this.inheritPermission = true;
    }

    public boolean doesInheritPermission() {
        return this.inheritPermission;
    }

    public boolean isEndless() {
        return this.endless;
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

    public <U> U parseArgument(String[] stringArray, int n, Supplier<U> supplier) {
        String string;
        String string2 = string = stringArray.length - 1 < n ? null : stringArray[n];
        if (string == null) {
            return supplier == null ? null : (U)supplier.get();
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
        if (this.getArgumentsSizeReal() - 1 < n) {
            return this.endless;
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

    public String getFormatted(String string) {
        StringBuilder stringBuilder = new StringBuilder().append("/").append(string).append(" ");
        for (Argument<?> argument : this.arguments) {
            if (argument.getType() == null) {
                stringBuilder.append(argument.getArgument());
            } else if (argument.isOptional()) {
                stringBuilder.append("&9[").append(argument.getArgument()).append("]&r");
            } else {
                stringBuilder.append("&2<").append(argument.getArgument()).append(">&r");
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append("&8-&e ");
        stringBuilder.append(this.getDescription());
        return stringBuilder.toString();
    }
}

