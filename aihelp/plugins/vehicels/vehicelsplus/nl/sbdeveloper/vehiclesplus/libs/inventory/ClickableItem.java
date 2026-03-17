/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory;

import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickableItem {
    private ItemStack item;
    private Consumer<InventoryClickEvent> consumer;

    private ClickableItem(ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        this.item = itemStack;
        this.consumer = consumer;
    }

    public static ClickableItem empty(ItemStack itemStack) {
        return ClickableItem.of(itemStack, inventoryClickEvent -> {});
    }

    public static ClickableItem of(ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        return new ClickableItem(itemStack, consumer);
    }

    public void run(InventoryClickEvent inventoryClickEvent) {
        this.consumer.accept(inventoryClickEvent);
    }

    public ItemStack getItem() {
        return this.item;
    }
}

