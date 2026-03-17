/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.guis;

import me.zombie_striker.qav.gui.components.GuiAction;
import me.zombie_striker.qav.gui.components.util.ItemNbt;
import me.zombie_striker.qav.gui.guis.BaseGui;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.gui.guis.PaginatedGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class GuiListener
implements Listener {
    @EventHandler
    public void onGuiClick(InventoryClickEvent inventoryClickEvent) {
        GuiItem guiItem;
        GuiAction<InventoryClickEvent> guiAction;
        GuiAction<InventoryClickEvent> guiAction2;
        GuiAction<InventoryClickEvent> guiAction3;
        GuiAction<InventoryClickEvent> guiAction4;
        if (!(inventoryClickEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryClickEvent.getInventory().getHolder();
        GuiAction<InventoryClickEvent> guiAction5 = baseGui.getOutsideClickAction();
        if (guiAction5 != null && inventoryClickEvent.getClickedInventory() == null) {
            guiAction5.execute(inventoryClickEvent);
            return;
        }
        if (inventoryClickEvent.getClickedInventory() == null) {
            return;
        }
        GuiAction<InventoryClickEvent> guiAction6 = baseGui.getDefaultTopClickAction();
        if (guiAction6 != null && inventoryClickEvent.getClickedInventory().getType() != InventoryType.PLAYER) {
            guiAction6.execute(inventoryClickEvent);
        }
        if ((guiAction4 = baseGui.getPlayerInventoryAction()) != null && inventoryClickEvent.getClickedInventory().getType() == InventoryType.PLAYER) {
            guiAction4.execute(inventoryClickEvent);
        }
        if ((guiAction3 = baseGui.getDefaultClickAction()) != null) {
            guiAction3.execute(inventoryClickEvent);
        }
        if ((guiAction2 = baseGui.getSlotAction(inventoryClickEvent.getSlot())) != null && inventoryClickEvent.getClickedInventory().getType() != InventoryType.PLAYER) {
            guiAction2.execute(inventoryClickEvent);
        }
        if (baseGui instanceof PaginatedGui) {
            guiAction = (PaginatedGui)baseGui;
            guiItem = ((BaseGui)((Object)guiAction)).getGuiItem(inventoryClickEvent.getSlot());
            if (guiItem == null) {
                guiItem = ((PaginatedGui)((Object)guiAction)).getPageItem(inventoryClickEvent.getSlot());
            }
        } else {
            guiItem = baseGui.getGuiItem(inventoryClickEvent.getSlot());
        }
        if (!this.isGuiItem(inventoryClickEvent.getCurrentItem(), guiItem)) {
            return;
        }
        guiAction = guiItem.getAction();
        if (guiAction != null) {
            guiAction.execute(inventoryClickEvent);
        }
    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent inventoryDragEvent) {
        if (!(inventoryDragEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryDragEvent.getInventory().getHolder();
        GuiAction<InventoryDragEvent> guiAction = baseGui.getDragAction();
        if (guiAction != null) {
            guiAction.execute(inventoryDragEvent);
        }
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent inventoryCloseEvent) {
        if (!(inventoryCloseEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryCloseEvent.getInventory().getHolder();
        GuiAction<InventoryCloseEvent> guiAction = baseGui.getCloseGuiAction();
        if (guiAction != null && !baseGui.isUpdating() && baseGui.shouldRunCloseAction()) {
            guiAction.execute(inventoryCloseEvent);
        }
    }

    @EventHandler
    public void onGuiOpen(InventoryOpenEvent inventoryOpenEvent) {
        if (!(inventoryOpenEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryOpenEvent.getInventory().getHolder();
        GuiAction<InventoryOpenEvent> guiAction = baseGui.getOpenGuiAction();
        if (guiAction != null && !baseGui.isUpdating()) {
            guiAction.execute(inventoryOpenEvent);
        }
    }

    private boolean isGuiItem(@Nullable ItemStack itemStack, @Nullable GuiItem guiItem) {
        if (itemStack == null || guiItem == null) {
            return false;
        }
        String string = ItemNbt.getString(itemStack, "mf-gui");
        if (string == null) {
            return false;
        }
        return string.equals(guiItem.getUuid().toString());
    }
}

