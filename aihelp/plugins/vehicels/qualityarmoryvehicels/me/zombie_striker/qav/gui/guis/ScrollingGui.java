/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.HumanEntity
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.guis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.components.ScrollType;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.gui.guis.PaginatedGui;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class ScrollingGui
extends PaginatedGui {
    private final ScrollType scrollType;
    private int scrollSize = 0;

    public ScrollingGui(int n, int n2, @NotNull String string, @NotNull ScrollType scrollType, @NotNull Set<InteractionModifier> set) {
        super(n, n2, string, set);
        this.scrollType = scrollType;
    }

    @Deprecated
    public ScrollingGui(int n, int n2, @NotNull String string, @NotNull ScrollType scrollType) {
        super(n, n2, string);
        this.scrollType = scrollType;
    }

    @Deprecated
    public ScrollingGui(int n, int n2, @NotNull String string) {
        this(n, n2, string, ScrollType.VERTICAL);
    }

    @Deprecated
    public ScrollingGui(int n, @NotNull String string) {
        this(n, 0, string, ScrollType.VERTICAL);
    }

    @Deprecated
    public ScrollingGui(int n, @NotNull String string, @NotNull ScrollType scrollType) {
        this(n, 0, string, scrollType);
    }

    @Deprecated
    public ScrollingGui(@NotNull String string) {
        this(2, string);
    }

    @Deprecated
    public ScrollingGui(@NotNull String string, @NotNull ScrollType scrollType) {
        this(2, string, scrollType);
    }

    @Override
    public boolean next() {
        if (this.getPageNum() * this.scrollSize + this.getPageSize() > this.getPageItems().size() + this.scrollSize) {
            return false;
        }
        this.setPageNum(this.getPageNum() + 1);
        this.updatePage();
        return true;
    }

    @Override
    public boolean previous() {
        if (this.getPageNum() - 1 == 0) {
            return false;
        }
        this.setPageNum(this.getPageNum() - 1);
        this.updatePage();
        return true;
    }

    @Override
    public void open(@NotNull HumanEntity humanEntity) {
        this.open(humanEntity, 1);
    }

    @Override
    public void open(@NotNull HumanEntity humanEntity, int n) {
        if (humanEntity.isSleeping()) {
            return;
        }
        this.getInventory().clear();
        this.getMutableCurrentPageItems().clear();
        this.populateGui();
        if (this.getPageSize() == 0) {
            this.setPageSize(this.calculatePageSize());
        }
        if (this.scrollSize == 0) {
            this.scrollSize = this.calculateScrollSize();
        }
        if (n > 0 && n * this.scrollSize + this.getPageSize() <= this.getPageItems().size() + this.scrollSize) {
            this.setPageNum(n);
        }
        this.populatePage();
        humanEntity.openInventory(this.getInventory());
    }

    @Override
    void updatePage() {
        this.clearPage();
        this.populatePage();
    }

    private void populatePage() {
        for (GuiItem guiItem : this.getPage(this.getPageNum())) {
            if (this.scrollType == ScrollType.HORIZONTAL) {
                this.putItemHorizontally(guiItem);
                continue;
            }
            this.putItemVertically(guiItem);
        }
    }

    private int calculateScrollSize() {
        int n = 0;
        if (this.scrollType == ScrollType.VERTICAL) {
            boolean bl = false;
            for (int i = 1; i <= this.getRows(); ++i) {
                for (int j = 1; j <= 9; ++j) {
                    int n2 = this.getSlotFromRowCol(i, j);
                    if (this.getInventory().getItem(n2) != null) continue;
                    if (!bl) {
                        bl = true;
                    }
                    ++n;
                }
                if (!bl) continue;
                return n;
            }
            return n;
        }
        boolean bl = false;
        for (int i = 1; i <= 9; ++i) {
            for (int j = 1; j <= this.getRows(); ++j) {
                int n3 = this.getSlotFromRowCol(j, i);
                if (this.getInventory().getItem(n3) != null) continue;
                if (!bl) {
                    bl = true;
                }
                ++n;
            }
            if (!bl) continue;
            return n;
        }
        return n;
    }

    private void putItemVertically(GuiItem guiItem) {
        for (int i = 0; i < this.getRows() * 9; ++i) {
            if (this.getGuiItem(i) != null || this.getInventory().getItem(i) != null) continue;
            this.getMutableCurrentPageItems().put(i, guiItem);
            this.getInventory().setItem(i, guiItem.getItemStack());
            break;
        }
    }

    private void putItemHorizontally(GuiItem guiItem) {
        for (int i = 1; i < 10; ++i) {
            for (int j = 1; j <= this.getRows(); ++j) {
                int n = this.getSlotFromRowCol(j, i);
                if (this.getGuiItem(n) != null || this.getInventory().getItem(n) != null) continue;
                this.getMutableCurrentPageItems().put(n, guiItem);
                this.getInventory().setItem(n, guiItem.getItemStack());
                return;
            }
        }
    }

    private List<GuiItem> getPage(int n) {
        int n2 = n - 1;
        int n3 = this.getPageItems().size();
        ArrayList<GuiItem> arrayList = new ArrayList<GuiItem>();
        int n4 = n2 * this.scrollSize + this.getPageSize();
        if (n4 > n3) {
            n4 = n3;
        }
        for (int i = n2 * this.scrollSize; i < n4; ++i) {
            arrayList.add(this.getPageItems().get(i));
        }
        return arrayList;
    }
}

