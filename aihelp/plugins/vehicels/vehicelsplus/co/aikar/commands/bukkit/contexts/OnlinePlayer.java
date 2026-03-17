/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package co.aikar.commands.bukkit.contexts;

import java.util.Objects;
import org.bukkit.entity.Player;

public class OnlinePlayer {
    public final Player player;

    public OnlinePlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        OnlinePlayer onlinePlayer = (OnlinePlayer)object;
        return Objects.equals(this.player, onlinePlayer.player);
    }

    public int hashCode() {
        return Objects.hash(this.player);
    }

    public String toString() {
        return "OnlinePlayer{player=" + this.player + '}';
    }
}

