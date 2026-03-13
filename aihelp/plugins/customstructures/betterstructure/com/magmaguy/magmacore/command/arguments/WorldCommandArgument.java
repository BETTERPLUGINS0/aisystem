/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.generator.WorldInfo
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;

public class WorldCommandArgument
extends ListStringCommandArgument {
    public WorldCommandArgument(List<String> validValues, String hint) {
        super(validValues, hint);
    }

    public WorldCommandArgument(String hint) {
        super(new ArrayList<String>(), hint);
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, String partialInput) {
        if (this.validValues.isEmpty()) {
            return Bukkit.getWorlds().stream().map(WorldInfo::getName).toList();
        }
        return super.getSuggestions(sender, partialInput);
    }
}

