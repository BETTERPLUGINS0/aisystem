/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package co.aikar.commands;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitConditionContext
extends ConditionContext<BukkitCommandIssuer> {
    protected BukkitConditionContext(BukkitCommandIssuer bukkitCommandIssuer, String string) {
        super(bukkitCommandIssuer, string);
    }

    public CommandSender getSender() {
        return ((BukkitCommandIssuer)this.getIssuer()).getIssuer();
    }

    public Player getPlayer() {
        return ((BukkitCommandIssuer)this.getIssuer()).getPlayer();
    }
}

