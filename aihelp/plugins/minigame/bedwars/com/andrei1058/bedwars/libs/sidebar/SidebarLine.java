/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.ScoredLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLineAnimated;
import com.andrei1058.bedwars.libs.sidebar.SidebarManager;
import java.util.Collection;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SidebarLine {
    private boolean internalPlaceholders = false;
    private boolean papiPlaceholders = false;

    @NotNull
    public abstract String getLine();

    @Deprecated(forRemoval=true)
    public void setHasPlaceholders(boolean value) {
        this.setInternalPlaceholders(value);
    }

    @Deprecated
    public boolean isHasPlaceholders() {
        return this.isInternalPlaceholders();
    }

    public static void markHasPlaceholders(@NotNull SidebarLine text, Collection<PlaceholderProvider> placeholders) {
        if (text.isPapiPlaceholders() && text.isInternalPlaceholders()) {
            return;
        }
        if (text instanceof SidebarLineAnimated) {
            block0: for (String line : ((SidebarLineAnimated)text).getLines()) {
                if (SidebarManager.getInstance().getPapiSupport().hasPlaceholders(line)) {
                    text.setPapiPlaceholders(true);
                    break;
                }
                for (PlaceholderProvider provider : placeholders) {
                    if (!text.getLine().contains(provider.getPlaceholder())) continue;
                    text.setInternalPlaceholders(true);
                    continue block0;
                }
            }
        } else {
            for (PlaceholderProvider provider : placeholders) {
                if (!text.getLine().contains(provider.getPlaceholder())) continue;
                text.setInternalPlaceholders(true);
            }
            if (SidebarManager.getInstance().getPapiSupport().hasPlaceholders(text.getLine())) {
                text.setPapiPlaceholders(true);
            }
        }
    }

    public String getTrimReplacePlaceholders(@Nullable Player papiSubject, @Nullable Integer limit, Collection<PlaceholderProvider> placeholders) {
        return SidebarLine.getTrimReplacePlaceholders(this.getLine(), papiSubject, limit, placeholders);
    }

    @ApiStatus.Experimental
    public String getTrimReplacePlaceholdersScore(@Nullable Player papiSubject, @Nullable Integer limit, Collection<PlaceholderProvider> placeholders) {
        if (this instanceof ScoredLine) {
            return SidebarLine.getTrimReplacePlaceholders(((ScoredLine)((Object)this)).getScore(), papiSubject, limit, placeholders);
        }
        return "";
    }

    @NotNull
    public static String getTrimReplacePlaceholders(String scope, @Nullable Player papiSubject, @Nullable Integer limit, Collection<PlaceholderProvider> placeholders) {
        String t = scope;
        if (null != placeholders) {
            for (PlaceholderProvider placeholderProvider : placeholders) {
                if (!t.contains(placeholderProvider.getPlaceholder())) continue;
                t = t.replace(placeholderProvider.getPlaceholder(), placeholderProvider.getReplacement());
            }
        }
        if (null != papiSubject) {
            t = ChatColor.translateAlternateColorCodes((char)'&', (String)SidebarManager.getInstance().getPapiSupport().replacePlaceholders(papiSubject, t));
        }
        if (null != limit && t.length() > limit) {
            t = t.substring(0, limit);
        }
        return t;
    }

    public boolean isPapiPlaceholders() {
        return this.papiPlaceholders;
    }

    public void setPapiPlaceholders(boolean papiPlaceholders) {
        this.papiPlaceholders = papiPlaceholders;
    }

    public boolean isInternalPlaceholders() {
        return this.internalPlaceholders;
    }

    public void setInternalPlaceholders(boolean internalPlaceholders) {
        this.internalPlaceholders = internalPlaceholders;
    }
}

