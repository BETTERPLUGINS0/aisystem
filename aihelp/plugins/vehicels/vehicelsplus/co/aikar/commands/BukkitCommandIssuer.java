/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package co.aikar.commands;

import co.aikar.commands.ACFBukkitUtil;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandManager;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandIssuer
implements CommandIssuer {
    private final BukkitCommandManager manager;
    private final CommandSender sender;

    protected BukkitCommandIssuer(BukkitCommandManager bukkitCommandManager, CommandSender commandSender) {
        this.manager = bukkitCommandManager;
        this.sender = commandSender;
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    public CommandSender getIssuer() {
        return this.sender;
    }

    public Player getPlayer() {
        return this.isPlayer() ? (Player)this.sender : null;
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        if (this.isPlayer()) {
            return ((Player)this.sender).getUniqueId();
        }
        return UUID.nameUUIDFromBytes(this.sender.getName().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public CommandManager getManager() {
        return this.manager;
    }

    @Override
    public void sendMessageInternal(String string) {
        this.sender.sendMessage(ACFBukkitUtil.color(string));
    }

    @Override
    public boolean hasPermission(String string) {
        return this.sender.hasPermission(string);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        BukkitCommandIssuer bukkitCommandIssuer = (BukkitCommandIssuer)object;
        return Objects.equals(this.sender, bukkitCommandIssuer.sender);
    }

    public int hashCode() {
        return Objects.hash(this.sender);
    }
}

