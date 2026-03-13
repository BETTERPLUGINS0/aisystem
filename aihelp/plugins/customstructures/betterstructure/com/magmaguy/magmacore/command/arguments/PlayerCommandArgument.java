/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ICommandArgument;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommandArgument
implements ICommandArgument {
    @Override
    public String hint() {
        return "";
    }

    @Override
    public boolean matchesInput(String input) {
        return this.getPlayerByName(input) != null;
    }

    @Override
    public List<String> literals() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, String partialInput) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(partialInput.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public boolean isLiteral() {
        return false;
    }

    private Player getPlayerByName(String name) {
        return Bukkit.getPlayerExact((String)name);
    }
}

