/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.magmaguy.magmacore.command.arguments;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface ICommandArgument {
    public String hint();

    public boolean matchesInput(String var1);

    public List<String> literals();

    public List<String> getSuggestions(CommandSender var1, String var2);

    public boolean isLiteral();
}

