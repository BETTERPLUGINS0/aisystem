/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.guis.BaseGui;
import me.zombie_striker.qav.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaginatedGui
extends BaseGui {
    private final List<GuiItem> pageItems = new ArrayList<GuiItem>();
    private final Map<Integer, GuiItem> currentPage;
    private int pageSize;
    private int pageNum = 1;

    public PaginatedGui(int n, int n2, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        super(n, string, set);
        this.pageSize = n2;
        int n3 = n * 9;
        this.currentPage = new LinkedHashMap<Integer, GuiItem>(n3);
    }

    @Deprecated
    public PaginatedGui(int n, int n2, @NotNull String string) {
        super(n, string);
        this.pageSize = n2;
        int n3 = n * 9;
        this.currentPage = new LinkedHashMap<Integer, GuiItem>(n3);
    }

    @Deprecated
    public PaginatedGui(int n, @NotNull String string) {
        this(n, 0, string);
    }

    @Deprecated
    public PaginatedGui(@NotNull String string) {
        this(2, string);
    }

    public BaseGui setPageSize(int n) {
        this.pageSize = n;
        return this;
    }

    public void addItem(@NotNull GuiItem guiItem) {
        this.pageItems.add(guiItem);
    }

    @Override
    public void addItem(@NotNull GuiItem ... guiItemArray) {
        this.pageItems.addAll(Arrays.asList(guiItemArray));
    }

    @Override
    public void update() {
        this.getInventory().clear();
        this.populateGui();
        this.updatePage();
    }

    public void updatePageItem(int n, @NotNull ItemStack itemStack) {
        if (!this.currentPage.containsKey(n)) {
            return;
        }
        GuiItem guiItem = this.currentPage.get(n);
        guiItem.setItemStack(itemStack);
        this.getInventory().setItem(n, guiItem.getItemStack());
    }

    public void updatePageItem(int n, int n2, @NotNull ItemStack itemStack) {
        this.updateItem(this.getSlotFromRowCol(n, n2), itemStack);
    }

    public void updatePageItem(int n, @NotNull GuiItem guiItem) {
        if (!this.currentPage.containsKey(n)) {
            return;
        }
        GuiItem guiItem2 = this.currentPage.get(n);
        int n2 = this.pageItems.indexOf(this.currentPage.get(n));
        this.currentPage.put(n, guiItem);
        this.pageItems.set(n2, guiItem);
        this.getInventory().setItem(n, guiItem.getItemStack());
    }

    public void updatePageItem(int n, int n2, @NotNull GuiItem guiItem) {
        this.updateItem(this.getSlotFromRowCol(n, n2), guiItem);
    }

    public void removePageItem(@NotNull GuiItem guiItem) {
        this.pageItems.remove(guiItem);
        this.updatePage();
    }

    public void removePageItem(@NotNull ItemStack itemStack) {
        Optional<GuiItem> optional = this.pageItems.stream().filter(guiItem -> guiItem.getItemStack().equals((Object)itemStack)).findFirst();
        optional.ifPresent(this::removePageItem);
    }

    @Override
    public void open(@NotNull HumanEntity humanEntity) {
        this.open(humanEntity, 1);
    }

    public void open(@NotNull HumanEntity humanEntity, int n) {
        if (humanEntity.isSleeping()) {
            return;
        }
        if (n <= this.getPagesNum() || n > 0) {
            this.pageNum = n;
        }
        this.getInventory().clear();
        this.currentPage.clear();
        this.populateGui();
        if (this.pageSize == 0) {
            this.pageSize = this.calculatePageSize();
        }
        this.populatePage();
        humanEntity.openInventory(this.getInventory());
    }

    @Override
    @NotNull
    public BaseGui updateTitle(@NotNull String string) {
        this.setUpdating(true);
        ArrayList arrayList = new ArrayList(this.getInventory().getViewers());
        this.setInventory(Bukkit.createInventory((InventoryHolder)this, (int)this.getInventory().getSize(), (String)string));
        for (HumanEntity humanEntity : arrayList) {
            this.open(humanEntity, this.getPageNum());
        }
        this.setUpdating(false);
        return this;
    }

    @NotNull
    public @NotNull Map<@NotNull Integer, @NotNull GuiItem> getCurrentPageItems() {
        return Collections.unmodifiableMap(this.currentPage);
    }

    @NotNull
    public @NotNull List<@NotNull GuiItem> getPageItems() {
        return Collections.unmodifiableList(this.pageItems);
    }

    public int getCurrentPageNum() {
        return this.pageNum;
    }

    public int getNextPageNum() {
        if (this.pageNum + 1 > this.getPagesNum()) {
            return this.pageNum;
        }
        return this.pageNum + 1;
    }

    public int getPrevPageNum() {
        if (this.pageNum - 1 == 0) {
            return this.pageNum;
        }
        return this.pageNum - 1;
    }

    public boolean next() {
        if (this.pageNum + 1 > this.getPagesNum()) {
            return false;
        }
        ++this.pageNum;
        this.updatePage();
        return true;
    }

    public boolean previous() {
        if (this.pageNum - 1 == 0) {
            return false;
        }
        --this.pageNum;
        this.updatePage();
        return true;
    }

    GuiItem getPageItem(int n) {
        return this.currentPage.get(n);
    }

    private List<GuiItem> getPageNum(int n) {
        int n2 = n - 1;
        ArrayList<GuiItem> arrayList = new ArrayList<GuiItem>();
        int n3 = n2 * this.pageSize + this.pageSize;
        if (n3 > this.pageItems.size()) {
            n3 = this.pageItems.size();
        }
        for (int i = n2 * this.pageSize; i < n3; ++i) {
            arrayList.add(this.pageItems.get(i));
        }
        return arrayList;
    }

    public int getPagesNum() {
        return (int)Math.ceil((double)this.pageItems.size() / (double)this.pageSize);
    }

    private void populatePage() {
        int n = 0;
        Iterator<GuiItem> iterator = this.getPageNum(this.pageNum).iterator();
        while (iterator.hasNext()) {
            if (this.getGuiItem(n) != null || this.getInventory().getItem(n) != null) {
                ++n;
                continue;
            }
            GuiItem guiItem = iterator.next();
            this.currentPage.put(n, guiItem);
            this.getInventory().setItem(n, guiItem.getItemStack());
            ++n;
        }
    }

    Map<Integer, GuiItem> getMutableCurrentPageItems() {
        return this.currentPage;
    }

    void clearPage() {
        for (Map.Entry<Integer, GuiItem> entry : this.currentPage.entrySet()) {
            this.getInventory().setItem(entry.getKey().intValue(), null);
        }
    }

    public void clearPageItems(boolean bl) {
        this.pageItems.clear();
        if (bl) {
            this.update();
        }
    }

    public void clearPageItems() {
        this.clearPageItems(false);
    }

    int getPageSize() {
        return this.pageSize;
    }

    int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int n) {
        this.pageNum = n;
    }

    void updatePage() {
        this.clearPage();
        this.populatePage();
    }

    int calculatePageSize() {
        int n = 0;
        for (int i = 0; i < this.getRows() * 9; ++i) {
            if (this.getInventory().getItem(i) != null) continue;
            ++n;
        }
        return n;
    }
}

