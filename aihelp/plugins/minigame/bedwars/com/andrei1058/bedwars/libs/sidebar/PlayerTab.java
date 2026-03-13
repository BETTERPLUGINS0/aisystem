/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface PlayerTab {
    public void add(Player var1);

    public void remove(Player var1);

    public void setSubject(@Nullable Player var1);

    @Nullable
    public Player getSubject();

    public void setPushingRule(PushingRule var1);

    public void setNameTagVisibility(NameTagVisibility var1);

    public String getIdentifier();

    public static enum NameTagVisibility {
        ALWAYS,
        NEVER,
        HIDE_FOR_OTHER_TEAMS,
        HIDE_FOR_OWN_TEAM;

    }

    public static enum PushingRule {
        ALWAYS,
        NEVER,
        PUSH_OTHER_TEAMS,
        PUSH_OWN_TEAM;

    }
}

