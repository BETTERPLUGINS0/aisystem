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
import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISidebar {
    public Player getPlayer();

    @Nullable
    public IArena getArena();

    public Sidebar getHandle();

    public void setContent(List<String> var1, List<String> var2, @Nullable IArena var3);

    public SidebarLine normalizeTitle(@Nullable List<String> var1);

    @NotNull
    public List<SidebarLine> normalizeLines(@NotNull List<String> var1);

    public void giveUpdateTabFormat(@NotNull Player var1, boolean var2, @Nullable Boolean var3);

    default public void giveUpdateTabFormat(@NotNull Player player, boolean skipStateCheck) {
        this.giveUpdateTabFormat(player, skipStateCheck, null);
    }

    @Deprecated(forRemoval=true)
    public boolean isTabFormattingDisabled();

    public boolean registerPersistentPlaceholder(PlaceholderProvider var1);
}

