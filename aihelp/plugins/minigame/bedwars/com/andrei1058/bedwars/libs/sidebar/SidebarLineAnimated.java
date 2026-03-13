/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import org.jetbrains.annotations.NotNull;

public class SidebarLineAnimated
extends SidebarLine {
    private final String[] lines;
    private int pos = -1;

    public SidebarLineAnimated(String[] lines) {
        this.lines = lines;
    }

    @Override
    @NotNull
    public String getLine() {
        return this.lines[++this.pos == this.lines.length ? (this.pos = 0) : this.pos];
    }

    public String[] getLines() {
        return this.lines;
    }
}

