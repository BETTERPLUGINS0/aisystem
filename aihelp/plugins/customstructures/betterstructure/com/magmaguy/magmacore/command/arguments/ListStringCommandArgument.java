/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ICommandArgument;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class ListStringCommandArgument
implements ICommandArgument {
    protected final List<String> validValues;
    protected String hint;

    public ListStringCommandArgument(List<String> validValues, String hint) {
        this.validValues = validValues;
        this.hint = hint;
    }

    public ListStringCommandArgument(String hint) {
        this.validValues = new ArrayList<String>();
        this.hint = hint;
    }

    @Override
    public String hint() {
        return this.hint;
    }

    @Override
    public boolean matchesInput(String input) {
        return this.validValues.stream().anyMatch(value -> value.equalsIgnoreCase(input));
    }

    @Override
    public List<String> literals() {
        return this.validValues;
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, String partialInput) {
        if (this.validValues.isEmpty()) {
            return partialInput.isEmpty() ? List.of((Object)this.hint) : List.of();
        }
        String lower = partialInput.toLowerCase();
        return this.validValues.stream().filter(value -> value.toLowerCase().startsWith(lower)).toList();
    }

    @Override
    public boolean isLiteral() {
        return false;
    }
}

