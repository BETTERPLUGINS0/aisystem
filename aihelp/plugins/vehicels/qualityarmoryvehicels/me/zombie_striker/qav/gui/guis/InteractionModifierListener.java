/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 */
package me.zombie_striker.qav.gui.guis;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import me.zombie_striker.qav.gui.guis.BaseGui;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public final class InteractionModifierListener
implements Listener {
    private static final Set<InventoryAction> ITEM_TAKE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PICKUP_ONE, new InventoryAction[]{InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ALL, InventoryAction.COLLECT_TO_CURSOR, InventoryAction.HOTBAR_SWAP, InventoryAction.MOVE_TO_OTHER_INVENTORY}));
    private static final Set<InventoryAction> ITEM_PLACE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL));
    private static final Set<InventoryAction> ITEM_SWAP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.HOTBAR_SWAP, InventoryAction.SWAP_WITH_CURSOR, InventoryAction.HOTBAR_MOVE_AND_READD));
    private static final Set<InventoryAction> ITEM_DROP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ALL_CURSOR));

    @EventHandler
    public void onGuiClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryClickEvent.getInventory().getHolder();
        if (baseGui.allInteractionsDisabled()) {
            inventoryClickEvent.setCancelled(true);
            inventoryClickEvent.setResult(Event.Result.DENY);
            return;
        }
        if (!baseGui.canPlaceItems() && this.isPlaceItemEvent(inventoryClickEvent) || !baseGui.canTakeItems() && this.isTakeItemEvent(inventoryClickEvent) || !baseGui.canSwapItems() && this.isSwapItemEvent(inventoryClickEvent) || !baseGui.canDropItems() && this.isDropItemEvent(inventoryClickEvent) || !baseGui.allowsOtherActions() && this.isOtherEvent(inventoryClickEvent)) {
            inventoryClickEvent.setCancelled(true);
            inventoryClickEvent.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent inventoryDragEvent) {
        if (!(inventoryDragEvent.getInventory().getHolder() instanceof BaseGui)) {
            return;
        }
        BaseGui baseGui = (BaseGui)inventoryDragEvent.getInventory().getHolder();
        if (baseGui.allInteractionsDisabled()) {
            inventoryDragEvent.setCancelled(true);
            inventoryDragEvent.setResult(Event.Result.DENY);
            return;
        }
        if (baseGui.canPlaceItems() || !this.isDraggingOnGui(inventoryDragEvent)) {
            return;
        }
        inventoryDragEvent.setCancelled(true);
        inventoryDragEvent.setResult(Event.Result.DENY);
    }

    private boolean isTakeItemEvent(InventoryClickEvent inventoryClickEvent) {
        Preconditions.checkNotNull(inventoryClickEvent, "event cannot be null");
        Inventory inventory = inventoryClickEvent.getInventory();
        Inventory inventory2 = inventoryClickEvent.getClickedInventory();
        InventoryAction inventoryAction = inventoryClickEvent.getAction();
        if (inventory2 != null && inventory2.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.PLAYER) {
            return false;
        }
        return inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY || this.isTakeAction(inventoryAction);
    }

    private boolean isPlaceItemEvent(InventoryClickEvent inventoryClickEvent) {
        Preconditions.checkNotNull(inventoryClickEvent, "event cannot be null");
        Inventory inventory = inventoryClickEvent.getInventory();
        Inventory inventory2 = inventoryClickEvent.getClickedInventory();
        InventoryAction inventoryAction = inventoryClickEvent.getAction();
        if (inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY && inventory2 != null && inventory2.getType() == InventoryType.PLAYER && inventory.getType() != inventory2.getType()) {
            return true;
        }
        return this.isPlaceAction(inventoryAction) && (inventory2 == null || inventory2.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    private boolean isSwapItemEvent(InventoryClickEvent inventoryClickEvent) {
        Preconditions.checkNotNull(inventoryClickEvent, "event cannot be null");
        Inventory inventory = inventoryClickEvent.getInventory();
        Inventory inventory2 = inventoryClickEvent.getClickedInventory();
        InventoryAction inventoryAction = inventoryClickEvent.getAction();
        return this.isSwapAction(inventoryAction) && (inventory2 == null || inventory2.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    private boolean isDropItemEvent(InventoryClickEvent inventoryClickEvent) {
        Preconditions.checkNotNull(inventoryClickEvent, "event cannot be null");
        Inventory inventory = inventoryClickEvent.getInventory();
        Inventory inventory2 = inventoryClickEvent.getClickedInventory();
        InventoryAction inventoryAction = inventoryClickEvent.getAction();
        return this.isDropAction(inventoryAction) && (inventory2 != null || inventory.getType() != InventoryType.PLAYER);
    }

    private boolean isOtherEvent(InventoryClickEvent inventoryClickEvent) {
        Preconditions.checkNotNull(inventoryClickEvent, "event cannot be null");
        Inventory inventory = inventoryClickEvent.getInventory();
        Inventory inventory2 = inventoryClickEvent.getClickedInventory();
        InventoryAction inventoryAction = inventoryClickEvent.getAction();
        return this.isOtherAction(inventoryAction) && (inventory2 != null || inventory.getType() != InventoryType.PLAYER);
    }

    private boolean isDraggingOnGui(InventoryDragEvent inventoryDragEvent) {
        Preconditions.checkNotNull(inventoryDragEvent, "event cannot be null");
        int n = inventoryDragEvent.getView().getTopInventory().getSize();
        return inventoryDragEvent.getRawSlots().stream().anyMatch(n2 -> n2 < n);
    }

    private boolean isTakeAction(InventoryAction inventoryAction) {
        Preconditions.checkNotNull(inventoryAction, "action cannot be null");
        return ITEM_TAKE_ACTIONS.contains(inventoryAction);
    }

    private boolean isPlaceAction(InventoryAction inventoryAction) {
        Preconditions.checkNotNull(inventoryAction, "action cannot be null");
        return ITEM_PLACE_ACTIONS.contains(inventoryAction);
    }

    private boolean isSwapAction(InventoryAction inventoryAction) {
        Preconditions.checkNotNull(inventoryAction, "action cannot be null");
        return ITEM_SWAP_ACTIONS.contains(inventoryAction);
    }

    private boolean isDropAction(InventoryAction inventoryAction) {
        Preconditions.checkNotNull(inventoryAction, "action cannot be null");
        return ITEM_DROP_ACTIONS.contains(inventoryAction);
    }

    private boolean isOtherAction(InventoryAction inventoryAction) {
        Preconditions.checkNotNull(inventoryAction, "action cannot be null");
        return inventoryAction == InventoryAction.CLONE_STACK || inventoryAction == InventoryAction.UNKNOWN;
    }
}

