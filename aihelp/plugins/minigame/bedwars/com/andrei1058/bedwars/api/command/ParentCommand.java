/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.command;

import com.andrei1058.bedwars.api.command.SubCommand;
import java.util.List;
import org.bukkit.entity.Player;

public interface ParentCommand {
    public boolean hasSubCommand(String var1);

    public void addSubCommand(SubCommand var1);

    public void sendSubCommands(Player var1);

    public List<SubCommand> getSubCommands();

    public String getName();
}

