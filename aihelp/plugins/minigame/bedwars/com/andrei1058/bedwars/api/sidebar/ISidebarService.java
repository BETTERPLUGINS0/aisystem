/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.sidebar;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.sidebar.ISidebar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISidebarService {
    public void giveSidebar(@NotNull Player var1, @Nullable IArena var2, boolean var3);

    public void remove(@NotNull Player var1);

    public void refreshTitles();

    public void refreshPlaceholders();

    public void refreshPlaceholders(IArena var1);

    public void refreshTabList();

    public void refreshHealth();

    @Nullable
    public ISidebar getSidebar(@NotNull Player var1);
}

