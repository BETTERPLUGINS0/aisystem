/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package co.aikar.commands;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.CommandCompletionContext;
import co.aikar.commands.RegisteredCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandCompletionContext
extends CommandCompletionContext<BukkitCommandIssuer> {
    protected BukkitCommandCompletionContext(RegisteredCommand registeredCommand, BukkitCommandIssuer bukkitCommandIssuer, String string, String string2, String[] stringArray) {
        super(registeredCommand, bukkitCommandIssuer, string, string2, stringArray);
    }

    public CommandSender getSender() {
        return (CommandSender)this.getIssuer().getIssuer();
    }

    public Player getPlayer() {
        return ((BukkitCommandIssuer)this.issuer).getPlayer();
    }
}

