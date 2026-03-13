/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PAPIAdapter;
import com.andrei1058.bedwars.libs.sidebar.PAPISupport;
import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarProvider;
import com.andrei1058.bedwars.libs.sidebar.TabHeaderFooter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidebarManager {
    private static SidebarManager instance;
    private final SidebarProvider sidebarProvider;
    private PAPISupport papiSupport = new PAPISupport(){

        @Override
        public String replacePlaceholders(Player p, String s) {
            return s;
        }

        @Override
        public boolean hasPlaceholders(String s) {
            return false;
        }
    };

    public SidebarManager() throws InstantiationException {
        instance = this;
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            this.papiSupport = new PAPIAdapter();
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        String serverVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        String className = "com.andrei1058.bedwars.libs.sidebar." + serverVersion + ".ProviderImpl";
        try {
            Class<?> c = Class.forName(className);
            this.sidebarProvider = (SidebarProvider)c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ignored) {
            throw new InstantiationException();
        }
    }

    @Nullable
    public static SidebarManager init() {
        if (null != instance) {
            return instance;
        }
        try {
            instance = new SidebarManager();
        } catch (InstantiationException e) {
            return null;
        }
        return instance;
    }

    public Sidebar createSidebar(SidebarLine title, @NotNull Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProviders) {
        lines.forEach(sidebarLine -> SidebarLine.markHasPlaceholders(sidebarLine, placeholderProviders));
        return this.sidebarProvider.createSidebar(title, lines, placeholderProviders);
    }

    public void sendHeaderFooter(Player player, String header, String footer) {
        this.sidebarProvider.sendHeaderFooter(player, header, footer);
    }

    public void sendHeaderFooter(Player player, TabHeaderFooter headerFooter) {
        this.sendHeaderFooter(player, this.buildTabContent(player, headerFooter.getHeader(), headerFooter), this.buildTabContent(player, headerFooter.getFooter(), headerFooter));
    }

    @Contract(pure=true)
    private String buildTabContent(Player player, @NotNull List<SidebarLine> lines, TabHeaderFooter headerFooter) {
        Object[] data = new String[lines.size()];
        for (int i = 0; i < data.length; ++i) {
            SidebarLine line = lines.get(i);
            String currentLine = line.getLine();
            if (line.isInternalPlaceholders()) {
                for (PlaceholderProvider placeholderProvider : headerFooter.getPlaceholders()) {
                    currentLine = currentLine.replace(placeholderProvider.getPlaceholder(), placeholderProvider.getReplacement());
                }
            }
            if (line.isPapiPlaceholders()) {
                currentLine = ChatColor.translateAlternateColorCodes((char)'&', (String)SidebarManager.getInstance().getPapiSupport().replacePlaceholders(player, currentLine));
            }
            data[i] = currentLine;
        }
        return StringUtils.join((Object[])data, (String)"\n");
    }

    public PAPISupport getPapiSupport() {
        return this.papiSupport;
    }

    public SidebarProvider getSidebarProvider() {
        return this.sidebarProvider;
    }

    public static SidebarManager getInstance() {
        return instance;
    }
}

