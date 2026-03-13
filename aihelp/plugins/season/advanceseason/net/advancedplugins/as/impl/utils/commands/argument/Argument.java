/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package net.advancedplugins.as.impl.utils.commands.argument;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentType;
import org.bukkit.command.CommandSender;

public class Argument<T> {
    private final ArgumentType<T> type;
    private final String argument;
    private final Set<String> aliases;
    private final Function<CommandSender, Collection<String>> onTabComplete;
    private boolean optional;

    public Argument(ArgumentType<T> argumentType, String string, Function<CommandSender, Collection<String>> function, String ... stringArray) {
        this.type = argumentType;
        this.argument = string;
        this.aliases = Sets.newHashSet(stringArray);
        this.onTabComplete = function;
    }

    public Argument(ArgumentType<T> argumentType, String string, String ... stringArray) {
        this(argumentType, string, (CommandSender commandSender) -> Lists.newArrayList(string), stringArray);
    }

    public Argument asOptional() {
        this.optional = true;
        return this;
    }

    public ArgumentType<T> getType() {
        return this.type;
    }

    public String getArgument() {
        return this.argument;
    }

    public Set<String> getAliases() {
        return this.aliases;
    }

    public Function<CommandSender, Collection<String>> getOnTabComplete() {
        return this.onTabComplete;
    }

    public boolean isOptional() {
        return this.optional;
    }
}

