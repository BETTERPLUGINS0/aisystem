/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryListener;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryManager;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInvsPlugin;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryProvider;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SmartInventory {
    private String id;
    private String title;
    private InventoryType type;
    private int rows;
    private int columns;
    private boolean closeable;
    private InventoryProvider provider;
    private SmartInventory parent;
    private List<InventoryListener<? extends Event>> listeners;
    private InventoryManager manager;

    private SmartInventory(InventoryManager inventoryManager) {
        this.manager = inventoryManager;
    }

    public Inventory open(Player player) {
        return this.open(player, 0);
    }

    public Inventory open(Player player, int n) {
        Optional<SmartInventory> optional = this.manager.getInventory(player);
        optional.ifPresent(smartInventory -> {
            smartInventory.getListeners().stream().filter(inventoryListener -> inventoryListener.getType() == InventoryCloseEvent.class).forEach(inventoryListener -> inventoryListener.accept(new InventoryCloseEvent(player.getOpenInventory())));
            this.manager.setInventory(player, null);
        });
        InventoryContents.Impl impl = new InventoryContents.Impl(this, player.getUniqueId());
        impl.pagination().page(n);
        this.manager.setContents(player, impl);
        try {
            this.provider.init(player, impl);
            if (!this.manager.getContents(player).equals(Optional.of(impl))) {
                return null;
            }
            InventoryOpener inventoryOpener = this.manager.findOpener(this.type).orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + this.type.name()));
            Inventory inventory = inventoryOpener.open(this, player);
            this.manager.setInventory(player, this);
            return inventory;
        } catch (Exception exception) {
            this.manager.handleInventoryOpenError(this, player, exception);
            return null;
        }
    }

    public void close(Player player) {
        this.listeners.stream().filter(inventoryListener -> inventoryListener.getType() == InventoryCloseEvent.class).forEach(inventoryListener -> inventoryListener.accept(new InventoryCloseEvent(player.getOpenInventory())));
        this.manager.setInventory(player, null);
        player.closeInventory();
        this.manager.setContents(player, null);
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public InventoryType getType() {
        return this.type;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public boolean isCloseable() {
        return this.closeable;
    }

    public void setCloseable(boolean bl) {
        this.closeable = bl;
    }

    public InventoryProvider getProvider() {
        return this.provider;
    }

    public Optional<SmartInventory> getParent() {
        return Optional.ofNullable(this.parent);
    }

    public InventoryManager getManager() {
        return this.manager;
    }

    List<InventoryListener<? extends Event>> getListeners() {
        return this.listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id = "unknown";
        private String title = "";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6;
        private int columns = 9;
        private boolean closeable = true;
        private InventoryManager manager;
        private InventoryProvider provider;
        private SmartInventory parent;
        private List<InventoryListener<? extends Event>> listeners = new ArrayList<InventoryListener<? extends Event>>();

        private Builder() {
        }

        public Builder id(String string) {
            this.id = string;
            return this;
        }

        public Builder title(String string) {
            this.title = string;
            return this;
        }

        public Builder type(InventoryType inventoryType) {
            this.type = inventoryType;
            return this;
        }

        public Builder size(int n, int n2) {
            this.rows = n;
            this.columns = n2;
            return this;
        }

        public Builder closeable(boolean bl) {
            this.closeable = bl;
            return this;
        }

        public Builder provider(InventoryProvider inventoryProvider) {
            this.provider = inventoryProvider;
            return this;
        }

        public Builder parent(SmartInventory smartInventory) {
            this.parent = smartInventory;
            return this;
        }

        public Builder listener(InventoryListener<? extends Event> inventoryListener) {
            this.listeners.add(inventoryListener);
            return this;
        }

        public Builder manager(InventoryManager inventoryManager) {
            this.manager = inventoryManager;
            return this;
        }

        public SmartInventory build() {
            InventoryManager inventoryManager;
            if (this.provider == null) {
                throw new IllegalStateException("The provider of the SmartInventory.Builder must be set.");
            }
            InventoryManager inventoryManager2 = inventoryManager = this.manager != null ? this.manager : SmartInvsPlugin.manager();
            if (inventoryManager == null) {
                throw new IllegalStateException("The manager of the SmartInventory.Builder must be set, or the SmartInvs should be loaded as a plugin.");
            }
            SmartInventory smartInventory = new SmartInventory(inventoryManager);
            smartInventory.id = this.id;
            smartInventory.title = this.title;
            smartInventory.type = this.type;
            smartInventory.rows = this.rows;
            smartInventory.columns = this.columns;
            smartInventory.closeable = this.closeable;
            smartInventory.provider = this.provider;
            smartInventory.parent = this.parent;
            smartInventory.listeners = this.listeners;
            return smartInventory;
        }
    }
}

