/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface Sidebar {
    public void addLine(SidebarLine var1);

    public void setLine(SidebarLine var1, int var2);

    public void setTitle(SidebarLine var1);

    public void refreshPlaceholders();

    public void refreshTitle();

    public void remove(Player var1);

    public void add(Player var1);

    public void refreshAnimatedLines();

    public void removeLine(int var1);

    public void clearLines();

    public int lineCount();

    public Collection<PlaceholderProvider> getPlaceholders();

    public void setPlayerHealth(Player var1, int var2);

    public void hidePlayersHealth();

    public void showPlayersHealth(SidebarLine var1, boolean var2);

    default public PlayerTab playerTabCreate(String identifier, @Nullable Player player, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule) {
        return this.playerTabCreate(identifier, player, prefix, suffix, pushingRule, null);
    }

    public PlayerTab playerTabCreate(String var1, @Nullable Player var2, SidebarLine var3, SidebarLine var4, PlayerTab.PushingRule var5, @Nullable Collection<PlaceholderProvider> var6);

    public void removeTab(String var1);

    public void removeTabs();

    public void playerTabRefreshAnimation();

    public void playerHealthRefreshAnimation();

    public void addPlaceholder(PlaceholderProvider var1);

    public void removePlaceholder(String var1);
}

