/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.party;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Party {
    public boolean hasParty(Player var1);

    public int partySize(Player var1);

    public boolean isOwner(Player var1);

    public List<Player> getMembers(Player var1);

    public void createParty(Player var1, Player ... var2);

    public void addMember(Player var1, Player var2);

    public void removeFromParty(Player var1);

    public void disband(Player var1);

    public boolean isMember(Player var1, Player var2);

    public void removePlayer(Player var1, Player var2);

    default public Player getOwner(Player member) {
        for (Player m : this.getMembers(member)) {
            if (!this.isOwner(m)) continue;
            return m;
        }
        return null;
    }

    default public void promote(@NotNull Player owner, @NotNull Player target) {
        String msg = String.valueOf(ChatColor.RED) + "Not implemented! Contact an administrator";
        owner.sendMessage(msg);
        target.sendMessage(msg);
    }

    public boolean isInternal();
}

