/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.utils.inventories;

import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryManager;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInventory;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryProvider;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Inventory
implements InventoryProvider {
    private static InventoryManager manager;
    protected SmartInventory inventory;
    private final boolean filler;

    public Inventory(int n, String string, boolean bl) {
        this(n, string, bl, true);
    }

    public Inventory(int n, String string, boolean bl, boolean bl2) {
        if (n < 1 || n > 6) {
            throw new IllegalArgumentException("Invalid amount of rows provided for Inventory: " + n);
        }
        this.inventory = SmartInventory.builder().id(string).provider(this).manager(manager).size(n, 9).closeable(bl2).title(ColorUtil.__("&8" + string)).build();
        this.filler = bl;
    }

    protected void open(Player player) {
        this.inventory.open(player);
    }

    protected void open(Player player, int n) {
        this.inventory.open(player, n);
    }

    public void close(Player player) {
        this.inventory.close(player);
    }

    public static void init(JavaPlugin javaPlugin) {
        manager = new InventoryManager(javaPlugin);
        manager.init();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        if (this.filler) {
            inventoryContents.fill(ClickableItem.empty(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).displayname(ColorUtil.__("&r")).getItemStack()));
        }
        this.addItems(player, inventoryContents);
    }

    public abstract void addItems(Player var1, InventoryContents var2);
}

