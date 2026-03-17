/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.utils.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.Pagination;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.SlotIterator;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class PaginationInventory
extends Inventory {
    protected final int paginationRows;
    private final int paginationRow;
    private final List<ClickableItem> items = new ArrayList<ClickableItem>();
    private final List<List<ClickableItem>> rows = new ArrayList<List<ClickableItem>>();
    private final boolean hasSeparateRowPagination;
    private final int staticRows;
    private final boolean filler;

    protected PaginationInventory(int n, String string) {
        this(n, 0, 0, string, false, false);
    }

    protected PaginationInventory(int n, String string, boolean bl) {
        this(n, 0, 0, string, false, bl);
    }

    protected PaginationInventory(int n, int n2, String string, boolean bl, boolean bl2) {
        this(n, 0, n2, string, bl, bl2);
    }

    protected PaginationInventory(int n, int n2, int n3, String string, boolean bl, boolean bl2) {
        super(n + 1 + n3, string, false);
        this.paginationRows = n;
        this.paginationRow = n2;
        this.staticRows = n3;
        this.filler = bl;
        this.hasSeparateRowPagination = bl2;
    }

    public void addItem(ClickableItem clickableItem) {
        this.items.add(clickableItem);
    }

    public void addItem(int n, ClickableItem clickableItem) {
        while (this.rows.size() <= n) {
            this.rows.add(new ArrayList());
        }
        this.rows.get(n).add(clickableItem);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        int n;
        if (this.filler) {
            for (int i = this.paginationRows + 1; i <= this.paginationRows + this.staticRows; ++i) {
                inventoryContents.fillRow(i, ClickableItem.empty(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).displayname(ChatColor.RESET.toString()).getItemStack()));
            }
        }
        this.addStaticItems(player, inventoryContents);
        Pagination pagination = inventoryContents.pagination();
        ArrayList arrayList = new ArrayList();
        if (this.hasSeparateRowPagination) {
            n = this.rows.stream().mapToInt(List::size).max().orElse(0);
            for (int i = 0; i < n; i += 9) {
                for (List<ClickableItem> list : this.rows) {
                    int n2 = Math.min(i + 9, list.size());
                    if (i >= n2) continue;
                    arrayList.addAll(list.subList(i, n2));
                    for (int j = n2; j < i + 9; ++j) {
                        arrayList.add(ClickableItem.empty(new ItemBuilder(XMaterial.AIR).getItemStack()));
                    }
                }
            }
            pagination.setItems((ClickableItem[])arrayList.toArray(ClickableItem[]::new));
        } else {
            pagination.setItems((ClickableItem[])this.items.toArray(ClickableItem[]::new));
        }
        pagination.setItemsPerPage(9 * this.paginationRows);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, this.paginationRow, 0));
        inventoryContents.set(this.paginationRows, 0, ClickableItem.of(new ItemBuilder(XMaterial.ARROW).displayname(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_FIRST_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_FIRST_LORE)).getItemStack(), inventoryClickEvent -> this.open(player, pagination.first().getPage())));
        inventoryContents.set(this.paginationRows, 3, ClickableItem.of(new ItemBuilder(XMaterial.OAK_SIGN).displayname(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_PREVIOUS_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_PREVIOUS_LORE, (Map<String, String>)Map.of((Object)"%page%", (Object)String.valueOf(pagination.isFirst() ? pagination.getPage() + 1 : pagination.getPage())))).getItemStack(), inventoryClickEvent -> this.open(player, pagination.isFirst() ? pagination.getPage() : pagination.getPage() - 1)));
        inventoryContents.set(this.paginationRows, 5, ClickableItem.of(new ItemBuilder(XMaterial.OAK_SIGN).displayname(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_NEXT_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_NEXT_LORE, (Map<String, String>)Map.of((Object)"%page%", (Object)String.valueOf(pagination.isLast() ? pagination.getPage() + 1 : pagination.getPage() + 2)))).getItemStack(), inventoryClickEvent -> this.open(player, pagination.isLast() ? pagination.getPage() : pagination.getPage() + 1)));
        n = (int)Math.ceil(this.hasSeparateRowPagination ? (double)arrayList.size() / (9.0 * (double)this.paginationRows) : (double)this.items.size() / (9.0 * (double)this.paginationRows));
        inventoryContents.set(this.paginationRows, 8, ClickableItem.of(new ItemBuilder(XMaterial.ARROW).displayname(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_LAST_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_PAGINATION_LAST_LORE, (Map<String, String>)Map.of((Object)"%page%", (Object)String.valueOf(n)))).getItemStack(), inventoryClickEvent -> this.open(player, n - 1)));
    }

    public void addStaticItems(Player player, InventoryContents inventoryContents) {
    }
}

