/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.CommandSender
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ICommandArgument;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.CommandSender;

public class LiteralCommandArgument
implements ICommandArgument {
    private final String literal;

    public LiteralCommandArgument(String literal) {
        this.literal = literal;
    }

    @Override
    public String hint() {
        return "";
    }

    @Override
    public boolean matchesInput(String input) {
        return this.literal.equalsIgnoreCase(input);
    }

    @Override
    public List<String> literals() {
        return List.of((Object)this.literal);
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, String partialInput) {
        if (this.literal.toLowerCase().startsWith(partialInput.toLowerCase())) {
            return List.of((Object)this.literal);
        }
        return List.of();
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Generated
    public String getLiteral() {
        return this.literal;
    }
}

