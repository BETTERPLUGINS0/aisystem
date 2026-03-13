package libs.com.ryderbelserion.vital.paper.api.builders.gui.listeners;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.GuiKeys;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GuiListener implements Listener {
   private static final Set<InventoryAction> ITEM_TAKE_ACTIONS;
   private static final Set<InventoryAction> ITEM_DROP_ACTIONS;
   private static final Set<InventoryAction> ITEM_PLACE_ACTIONS;
   private static final Set<InventoryAction> ITEM_SWAP_ACTIONS;

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof BaseGui) {
         BaseGui gui = (BaseGui)var3;
         if (gui.isInteractionsDisabled()) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
         } else if (!gui.canPlaceItems() && this.isPlaceItemEvent(event) || !gui.canTakeItems() && this.isTakeItemEvent(event) || !gui.canSwapItems() && this.isSwapItemEvent(event) || !gui.canDropItems() && this.isDropItemEvent(event) || !gui.canPerformOtherActions() && this.isOtherEvent(event)) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
         }

         if (event.getClickedInventory() != null) {
            GuiAction<InventoryClickEvent> defaultTopClick = gui.getDefaultTopClickAction();
            if (defaultTopClick != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
               defaultTopClick.execute(event);
            }

            GuiAction<InventoryClickEvent> playerInventoryClick = gui.getPlayerInventoryAction();
            if (playerInventoryClick != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
               playerInventoryClick.execute(event);
            }

            GuiAction<InventoryClickEvent> defaultClick = gui.getDefaultClickAction();
            if (defaultClick != null) {
               defaultClick.execute(event);
            }

            GuiAction<InventoryClickEvent> slotAction = gui.getSlotAction(event.getSlot());
            if (slotAction != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
               slotAction.execute(event);
            }

            GuiItem guiItem;
            if (gui instanceof PaginatedGui) {
               PaginatedGui paginatedGui = (PaginatedGui)gui;
               guiItem = paginatedGui.getGuiItem(event.getSlot());
               if (guiItem == null) {
                  guiItem = paginatedGui.getPageItem(event.getSlot());
               }
            } else {
               guiItem = gui.getGuiItem(event.getSlot());
            }

            if (this.isGuiItem(event.getCurrentItem(), guiItem)) {
               GuiAction<InventoryClickEvent> itemAction = guiItem.getAction();
               if (itemAction != null) {
                  itemAction.execute(event);
               }

            }
         }
      }
   }

   @EventHandler
   public void onInventoryDrag(InventoryDragEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof BaseGui) {
         BaseGui gui = (BaseGui)var3;
         if (gui.isInteractionsDisabled()) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
         } else if (!gui.canPlaceItems() && this.isDraggingOnGui(event)) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
         }
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof BaseGui) {
         BaseGui gui = (BaseGui)var3;
         GuiAction closeAction = gui.getCloseGuiAction();
         if (closeAction != null && !gui.isUpdating()) {
            closeAction.execute(event);
         }

      }
   }

   @EventHandler
   public void onGuiOpen(InventoryOpenEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder();
      if (var3 instanceof BaseGui) {
         BaseGui gui = (BaseGui)var3;
         GuiAction openAction = gui.getOpenGuiAction();
         if (openAction != null && !gui.isUpdating()) {
            openAction.execute(event);
         }

      }
   }

   private boolean isGuiItem(@Nullable ItemStack currentItem, @Nullable GuiItem guiItem) {
      if (currentItem != null && guiItem != null) {
         String nbt = GuiKeys.getUUID(currentItem);
         return !nbt.isEmpty() && !nbt.isBlank() ? nbt.equalsIgnoreCase(guiItem.getUuid().toString()) : false;
      } else {
         return false;
      }
   }

   private boolean isTakeItemEvent(InventoryClickEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      Inventory inventory = event.getInventory();
      Inventory clickedInventory = event.getClickedInventory();
      InventoryAction action = event.getAction();
      if ((clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER) {
         return action == InventoryAction.MOVE_TO_OTHER_INVENTORY || this.isTakeAction(action);
      } else {
         return false;
      }
   }

   private boolean isPlaceItemEvent(InventoryClickEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      Inventory inventory = event.getInventory();
      Inventory clickedInventory = event.getClickedInventory();
      InventoryAction action = event.getAction();
      if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY && clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER && inventory.getType() != clickedInventory.getType()) {
         return true;
      } else {
         return this.isPlaceAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
      }
   }

   private boolean isSwapItemEvent(InventoryClickEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      Inventory inventory = event.getInventory();
      Inventory clickedInventory = event.getClickedInventory();
      InventoryAction action = event.getAction();
      return this.isSwapAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
   }

   private boolean isDropItemEvent(InventoryClickEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      Inventory inventory = event.getInventory();
      Inventory clickedInventory = event.getClickedInventory();
      InventoryAction action = event.getAction();
      return this.isDropAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
   }

   private boolean isOtherEvent(InventoryClickEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      Inventory inventory = event.getInventory();
      Inventory clickedInventory = event.getClickedInventory();
      InventoryAction action = event.getAction();
      return this.isOtherAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
   }

   private boolean isDraggingOnGui(InventoryDragEvent event) {
      Preconditions.checkNotNull(event, "event cannot be null");
      int topSlots = event.getView().getTopInventory().getSize();
      return event.getRawSlots().stream().anyMatch((slot) -> {
         return slot < topSlots;
      });
   }

   private boolean isTakeAction(InventoryAction action) {
      Preconditions.checkNotNull(action, "action cannot be null");
      return ITEM_TAKE_ACTIONS.contains(action);
   }

   private boolean isPlaceAction(InventoryAction action) {
      Preconditions.checkNotNull(action, "action cannot be null");
      return ITEM_PLACE_ACTIONS.contains(action);
   }

   private boolean isSwapAction(InventoryAction action) {
      Preconditions.checkNotNull(action, "action cannot be null");
      return ITEM_SWAP_ACTIONS.contains(action);
   }

   private boolean isDropAction(InventoryAction action) {
      Preconditions.checkNotNull(action, "action cannot be null");
      return ITEM_DROP_ACTIONS.contains(action);
   }

   private boolean isOtherAction(InventoryAction action) {
      Preconditions.checkNotNull(action, "action cannot be null");
      return action == InventoryAction.CLONE_STACK || action == InventoryAction.UNKNOWN;
   }

   static {
      ITEM_TAKE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ALL, InventoryAction.COLLECT_TO_CURSOR, InventoryAction.HOTBAR_SWAP, InventoryAction.MOVE_TO_OTHER_INVENTORY));
      ITEM_DROP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ALL_CURSOR));
      ITEM_PLACE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL));
      ITEM_SWAP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.HOTBAR_SWAP, InventoryAction.SWAP_WITH_CURSOR));
   }
}
