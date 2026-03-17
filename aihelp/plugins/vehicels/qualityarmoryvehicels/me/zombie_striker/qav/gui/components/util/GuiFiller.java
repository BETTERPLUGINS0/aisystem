/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.components.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.zombie_striker.qav.gui.components.GuiType;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.guis.BaseGui;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.gui.guis.PaginatedGui;
import org.jetbrains.annotations.NotNull;

public final class GuiFiller {
    private final BaseGui gui;

    public GuiFiller(BaseGui baseGui) {
        this.gui = baseGui;
    }

    public void fillTop(@NotNull GuiItem guiItem) {
        this.fillTop(Collections.singletonList(guiItem));
    }

    public void fillTop(@NotNull List<GuiItem> list) {
        List<GuiItem> list2 = this.repeatList(list);
        for (int i = 0; i < 9; ++i) {
            if (this.gui.getGuiItems().containsKey(i)) continue;
            this.gui.setItem(i, list2.get(i));
        }
    }

    public void fillBottom(@NotNull GuiItem guiItem) {
        this.fillBottom(Collections.singletonList(guiItem));
    }

    public void fillBottom(@NotNull List<GuiItem> list) {
        int n = this.gui.getRows();
        List<GuiItem> list2 = this.repeatList(list);
        for (int i = 9; i > 0; --i) {
            if (this.gui.getGuiItems().get(n * 9 - i) != null) continue;
            this.gui.setItem(n * 9 - i, list2.get(i));
        }
    }

    public void fillBorder(@NotNull GuiItem guiItem) {
        this.fillBorder(Collections.singletonList(guiItem));
    }

    public void fillBorder(@NotNull List<GuiItem> list) {
        int n = this.gui.getRows();
        if (n <= 2) {
            return;
        }
        List<GuiItem> list2 = this.repeatList(list);
        for (int i = 0; i < n * 9; ++i) {
            if (i > 8 && (i < n * 9 - 8 || i > n * 9 - 2) && i % 9 != 0 && i % 9 != 8) continue;
            this.gui.setItem(i, list2.get(i));
        }
    }

    public void fillBetweenPoints(int n, int n2, int n3, int n4, @NotNull GuiItem guiItem) {
        this.fillBetweenPoints(n, n2, n3, n4, Collections.singletonList(guiItem));
    }

    public void fillBetweenPoints(int n, int n2, int n3, int n4, @NotNull List<GuiItem> list) {
        int n5 = Math.min(n, n3);
        int n6 = Math.max(n, n3);
        int n7 = Math.min(n2, n4);
        int n8 = Math.max(n2, n4);
        int n9 = this.gui.getRows();
        List<GuiItem> list2 = this.repeatList(list);
        for (int i = 1; i <= n9; ++i) {
            for (int j = 1; j <= 9; ++j) {
                int n10 = this.getSlotFromRowCol(i, j);
                if (i < n5 || i > n6 || j < n7 || j > n8) continue;
                this.gui.setItem(n10, list2.get(n10));
            }
        }
    }

    public void fill(@NotNull GuiItem guiItem) {
        this.fill(Collections.singletonList(guiItem));
    }

    public void fill(@NotNull List<GuiItem> list) {
        if (this.gui instanceof PaginatedGui) {
            throw new GuiException("Full filling a GUI is not supported in a Paginated GUI!");
        }
        GuiType guiType = this.gui.guiType();
        int n = guiType == GuiType.CHEST ? this.gui.getRows() * guiType.getLimit() : guiType.getLimit();
        List<GuiItem> list2 = this.repeatList(list);
        for (int i = 0; i < n; ++i) {
            if (this.gui.getGuiItems().get(i) != null) continue;
            this.gui.setItem(i, list2.get(i));
        }
    }

    public void fillSide(@NotNull Side side, @NotNull List<GuiItem> list) {
        switch (side) {
            case LEFT: {
                this.fillBetweenPoints(1, 1, this.gui.getRows(), 1, list);
            }
            case RIGHT: {
                this.fillBetweenPoints(1, 9, this.gui.getRows(), 9, list);
            }
            case BOTH: {
                this.fillBetweenPoints(1, 1, this.gui.getRows(), 1, list);
                this.fillBetweenPoints(1, 9, this.gui.getRows(), 9, list);
            }
        }
    }

    private List<GuiItem> repeatList(@NotNull List<GuiItem> list) {
        ArrayList<GuiItem> arrayList = new ArrayList<GuiItem>();
        Collections.nCopies(this.gui.getRows() * 9, list).forEach(arrayList::addAll);
        return arrayList;
    }

    private int getSlotFromRowCol(int n, int n2) {
        return n2 + (n - 1) * 9 - 1;
    }

    public static enum Side {
        LEFT,
        RIGHT,
        BOTH;

    }
}

