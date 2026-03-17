/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.utils.inventories;

import java.util.function.Consumer;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfirmationInventory
extends Inventory {
    private final Consumer<Void> confirm;
    private final Consumer<Void> deny;
    private final boolean closeInventory;

    public ConfirmationInventory(Player player, String string, Consumer<Void> consumer, Consumer<Void> consumer2, boolean bl) {
        super(3, string, true);
        this.confirm = consumer;
        this.deny = consumer2;
        this.closeInventory = bl;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).displayname(Locale.getMessage(PluginMessage.INVENTORIES_CONFIRMATION_CONFIRM_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_CONFIRMATION_CONFIRM_LORE)).getItemStack(), inventoryClickEvent -> {
            this.confirm.accept(null);
            if (this.closeInventory) {
                this.close(player);
            }
        }));
        inventoryContents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).displayname(Locale.getMessage(PluginMessage.INVENTORIES_CONFIRMATION_CANCEL_TITLE)).lore(Locale.getMessage(PluginMessage.INVENTORIES_CONFIRMATION_CANCEL_LORE)).getItemStack(), inventoryClickEvent -> {
            this.deny.accept(null);
            if (this.closeInventory) {
                this.close(player);
            }
        }));
    }
}

