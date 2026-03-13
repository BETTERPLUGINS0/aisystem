/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SidebarProvider {
    public abstract Sidebar createSidebar(SidebarLine var1, Collection<SidebarLine> var2, Collection<PlaceholderProvider> var3);

    public abstract SidebarObjective createObjective(@NotNull WrappedSidebar var1, String var2, boolean var3, SidebarLine var4, int var5);

    public abstract ScoreLine createScoreLine(WrappedSidebar var1, SidebarLine var2, int var3, String var4);

    public abstract void sendScore(@NotNull WrappedSidebar var1, String var2, int var3);

    public abstract VersionedTabGroup createPlayerTab(WrappedSidebar var1, String var2, SidebarLine var3, SidebarLine var4, PlayerTab.PushingRule var5, PlayerTab.NameTagVisibility var6, @Nullable Collection<PlaceholderProvider> var7);

    public abstract void sendHeaderFooter(Player var1, String var2, String var3);
}

