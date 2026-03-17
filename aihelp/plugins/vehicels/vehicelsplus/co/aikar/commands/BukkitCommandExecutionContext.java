/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package co.aikar.commands;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandParameter;
import co.aikar.commands.RegisteredCommand;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandExecutionContext
extends CommandExecutionContext<BukkitCommandExecutionContext, BukkitCommandIssuer> {
    protected BukkitCommandExecutionContext(RegisteredCommand registeredCommand, CommandParameter commandParameter, BukkitCommandIssuer bukkitCommandIssuer, List<String> list, int n, Map<String, Object> map) {
        super(registeredCommand, commandParameter, bukkitCommandIssuer, list, n, map);
    }

    public CommandSender getSender() {
        return ((BukkitCommandIssuer)this.issuer).getIssuer();
    }

    public Player getPlayer() {
        return ((BukkitCommandIssuer)this.issuer).getPlayer();
    }
}

