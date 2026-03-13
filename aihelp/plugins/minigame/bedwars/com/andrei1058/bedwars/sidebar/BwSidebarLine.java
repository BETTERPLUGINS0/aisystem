/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.libs.sidebar.ScoredLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BwSidebarLine
extends SidebarLine
implements ScoredLine {
    public final String content;
    public final String score;

    public BwSidebarLine(String content, @Nullable String score) {
        this.content = content;
        this.score = score == null ? "" : score;
    }

    @Override
    public String getScore() {
        return this.score;
    }

    @Override
    @NotNull
    public String getLine() {
        return this.content;
    }
}

