/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInventory;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.Pagination;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.SlotIterator;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryContents {
    public SmartInventory inventory();

    public Pagination pagination();

    public Optional<SlotIterator> iterator(String var1);

    public SlotIterator newIterator(String var1, SlotIterator.Type var2, int var3, int var4);

    public SlotIterator newIterator(SlotIterator.Type var1, int var2, int var3);

    public SlotIterator newIterator(String var1, SlotIterator.Type var2, SlotPos var3);

    public SlotIterator newIterator(SlotIterator.Type var1, SlotPos var2);

    public ClickableItem[][] all();

    public Optional<SlotPos> firstEmpty();

    public Optional<ClickableItem> get(int var1, int var2);

    public Optional<ClickableItem> get(SlotPos var1);

    public InventoryContents set(int var1, int var2, ClickableItem var3);

    public InventoryContents set(SlotPos var1, ClickableItem var2);

    public InventoryContents add(ClickableItem var1);

    public InventoryContents fill(ClickableItem var1);

    public InventoryContents fillRow(int var1, ClickableItem var2);

    public InventoryContents fillColumn(int var1, ClickableItem var2);

    public InventoryContents fillBorders(ClickableItem var1);

    public InventoryContents fillRect(int var1, int var2, int var3, int var4, ClickableItem var5);

    public InventoryContents fillRect(SlotPos var1, SlotPos var2, ClickableItem var3);

    public <T> T property(String var1);

    public <T> T property(String var1, T var2);

    public InventoryContents setProperty(String var1, Object var2);

    public static class Impl
    implements InventoryContents {
        private SmartInventory inv;
        private UUID player;
        private ClickableItem[][] contents;
        private Pagination pagination = new Pagination.Impl();
        private Map<String, SlotIterator> iterators = new HashMap<String, SlotIterator>();
        private Map<String, Object> properties = new HashMap<String, Object>();

        public Impl(SmartInventory smartInventory, UUID uUID) {
            this.inv = smartInventory;
            this.player = uUID;
            this.contents = new ClickableItem[smartInventory.getRows()][smartInventory.getColumns()];
        }

        @Override
        public SmartInventory inventory() {
            return this.inv;
        }

        @Override
        public Pagination pagination() {
            return this.pagination;
        }

        @Override
        public Optional<SlotIterator> iterator(String string) {
            return Optional.ofNullable(this.iterators.get(string));
        }

        @Override
        public SlotIterator newIterator(String string, SlotIterator.Type type, int n, int n2) {
            SlotIterator.Impl impl = new SlotIterator.Impl(this, this.inv, type, n, n2);
            this.iterators.put(string, impl);
            return impl;
        }

        @Override
        public SlotIterator newIterator(String string, SlotIterator.Type type, SlotPos slotPos) {
            return this.newIterator(string, type, slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, int n, int n2) {
            return new SlotIterator.Impl(this, this.inv, type, n, n2);
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, SlotPos slotPos) {
            return this.newIterator(type, slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public ClickableItem[][] all() {
            return this.contents;
        }

        @Override
        public Optional<SlotPos> firstEmpty() {
            for (int i = 0; i < this.contents.length; ++i) {
                for (int j = 0; j < this.contents[0].length; ++j) {
                    if (this.get(i, j).isPresent()) continue;
                    return Optional.of(new SlotPos(i, j));
                }
            }
            return Optional.empty();
        }

        @Override
        public Optional<ClickableItem> get(int n, int n2) {
            if (n >= this.contents.length) {
                return Optional.empty();
            }
            if (n2 >= this.contents[n].length) {
                return Optional.empty();
            }
            return Optional.ofNullable(this.contents[n][n2]);
        }

        @Override
        public Optional<ClickableItem> get(SlotPos slotPos) {
            return this.get(slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public InventoryContents set(int n, int n2, ClickableItem clickableItem) {
            if (n >= this.contents.length) {
                return this;
            }
            if (n2 >= this.contents[n].length) {
                return this;
            }
            this.contents[n][n2] = clickableItem;
            this.update(n, n2, clickableItem != null ? clickableItem.getItem() : null);
            return this;
        }

        @Override
        public InventoryContents set(SlotPos slotPos, ClickableItem clickableItem) {
            return this.set(slotPos.getRow(), slotPos.getColumn(), clickableItem);
        }

        @Override
        public InventoryContents add(ClickableItem clickableItem) {
            for (int i = 0; i < this.contents.length; ++i) {
                for (int j = 0; j < this.contents[0].length; ++j) {
                    if (this.contents[i][j] != null) continue;
                    this.set(i, j, clickableItem);
                    return this;
                }
            }
            return this;
        }

        @Override
        public InventoryContents fill(ClickableItem clickableItem) {
            for (int i = 0; i < this.contents.length; ++i) {
                for (int j = 0; j < this.contents[i].length; ++j) {
                    this.set(i, j, clickableItem);
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRow(int n, ClickableItem clickableItem) {
            if (n >= this.contents.length) {
                return this;
            }
            for (int i = 0; i < this.contents[n].length; ++i) {
                this.set(n, i, clickableItem);
            }
            return this;
        }

        @Override
        public InventoryContents fillColumn(int n, ClickableItem clickableItem) {
            for (int i = 0; i < this.contents.length; ++i) {
                this.set(i, n, clickableItem);
            }
            return this;
        }

        @Override
        public InventoryContents fillBorders(ClickableItem clickableItem) {
            this.fillRect(0, 0, this.inv.getRows() - 1, this.inv.getColumns() - 1, clickableItem);
            return this;
        }

        @Override
        public InventoryContents fillRect(int n, int n2, int n3, int n4, ClickableItem clickableItem) {
            for (int i = n; i <= n3; ++i) {
                for (int j = n2; j <= n4; ++j) {
                    if (i != n && i != n3 && j != n2 && j != n4) continue;
                    this.set(i, j, clickableItem);
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRect(SlotPos slotPos, SlotPos slotPos2, ClickableItem clickableItem) {
            return this.fillRect(slotPos.getRow(), slotPos.getColumn(), slotPos2.getRow(), slotPos2.getColumn(), clickableItem);
        }

        @Override
        public <T> T property(String string) {
            return (T)this.properties.get(string);
        }

        @Override
        public <T> T property(String string, T t) {
            return (T)(this.properties.containsKey(string) ? this.properties.get(string) : t);
        }

        @Override
        public InventoryContents setProperty(String string, Object object) {
            this.properties.put(string, object);
            return this;
        }

        private void update(int n, int n2, ItemStack itemStack) {
            Player player = Bukkit.getPlayer((UUID)this.player);
            if (!this.inv.getManager().getOpenedPlayers(this.inv).contains(player)) {
                return;
            }
            Inventory inventory = player.getOpenInventory().getTopInventory();
            inventory.setItem(this.inv.getColumns() * n + n2, itemStack);
        }
    }
}

