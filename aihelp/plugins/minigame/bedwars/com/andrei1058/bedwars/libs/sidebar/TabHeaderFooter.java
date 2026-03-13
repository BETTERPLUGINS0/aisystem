/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabHeaderFooter {
    private LinkedList<SidebarLine> header;
    private LinkedList<SidebarLine> footer;
    private Collection<PlaceholderProvider> placeholders;

    public TabHeaderFooter(LinkedList<SidebarLine> header, LinkedList<SidebarLine> footer, @Nullable Collection<PlaceholderProvider> placeholders) {
        this.header = header;
        this.footer = footer;
        this.setPlaceholders(placeholders);
    }

    @NotNull
    public Collection<PlaceholderProvider> getPlaceholders() {
        return this.placeholders;
    }

    public LinkedList<SidebarLine> getHeader() {
        return this.header;
    }

    public LinkedList<SidebarLine> getFooter() {
        return this.footer;
    }

    public void setPlaceholders(@Nullable Collection<PlaceholderProvider> placeholders) {
        if (null == placeholders) {
            this.placeholders = new ConcurrentLinkedQueue<PlaceholderProvider>();
            return;
        }
        this.placeholders = placeholders;
        for (SidebarLine line : this.footer) {
            SidebarLine.markHasPlaceholders(line, placeholders);
        }
        for (SidebarLine line : this.header) {
            SidebarLine.markHasPlaceholders(line, placeholders);
        }
    }

    public void setFooter(@NotNull LinkedList<SidebarLine> footer) {
        this.footer = footer;
        for (SidebarLine line : footer) {
            SidebarLine.markHasPlaceholders(line, this.placeholders);
        }
    }

    public void setHeader(@NotNull LinkedList<SidebarLine> header) {
        this.header = header;
        for (SidebarLine line : header) {
            SidebarLine.markHasPlaceholders(line, this.placeholders);
        }
    }
}

