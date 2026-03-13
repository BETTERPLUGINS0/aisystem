package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ui.UISession;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class View implements UISession {
   private final ViewProvider provider;
   private final Player player;
   private final UIState initialUIState;
   private boolean valid = true;
   private boolean uiActive = true;
   @Nullable
   private InventoryView inventoryView;
   private static final long AUTOMATIC_SHIFT_LEFT_CLICK_NANOS;
   private long lastManualClickNanos = 0L;
   private int lastManualClickedSlotId = -1;
   private boolean isAutomaticShiftLeftClick = false;

   protected View(ViewProvider provider, Player player, UIState uiState) {
      Validate.notNull(provider, (String)"provider is null");
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(uiState, (String)"uiState is null");
      this.provider = provider;
      this.player = player;
      this.validateState(uiState);
      this.initialUIState = uiState;
   }

   public final AbstractUIType getUIType() {
      return this.provider.getUIType();
   }

   public final ViewProvider getProvider() {
      return this.provider;
   }

   public final Player getPlayer() {
      return this.player;
   }

   public final ViewContext getContext() {
      return this.provider.getContext();
   }

   @Nullable
   public final AbstractShopkeeper getShopkeeper() {
      Object var2 = this.getContext().getObject();
      if (var2 instanceof AbstractShopkeeper) {
         AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var2;
         return shopkeeper;
      } else {
         return null;
      }
   }

   public AbstractShopkeeper getShopkeeperNonNull() {
      return (AbstractShopkeeper)Validate.State.notNull(this.getShopkeeper(), (String)"Shopkeeper is null!");
   }

   protected final UIState getInitialUIState() {
      return this.initialUIState;
   }

   public final boolean isValid() {
      return this.valid;
   }

   protected final void validateIsValid() {
      Validate.State.isTrue(this.isValid(), "This operation is not allowed because this view is no longer valid!");
   }

   final void onSessionEnd() {
      this.markInvalid();
   }

   private final void markInvalid() {
      this.valid = false;
   }

   public final boolean isUIActive() {
      return this.uiActive;
   }

   public final void deactivateUI() {
      this.uiActive = false;
   }

   public final void activateUI() {
      this.uiActive = true;
   }

   public final void close() {
      if (this.isValid()) {
         this.player.closeInventory();
      }
   }

   public final void closeDelayed() {
      this.closeDelayedAndRunTask((Runnable)null);
   }

   public final void closeDelayedAndRunTask(@Nullable Runnable task) {
      if (this.isValid()) {
         this.deactivateUI();
         SchedulerUtils.runTaskOrOmit(ShopkeepersPlugin.getInstance(), () -> {
            if (this.isValid()) {
               this.close();
               if (task != null) {
                  task.run();
               }

            }
         });
      }
   }

   public final void abort() {
      UISessionManager.getInstance().abort(this);
   }

   public final void abortDelayed() {
      this.abortDelayedAndRunTask((Runnable)null);
   }

   public final void abortDelayedAndRunTask(@Nullable Runnable task) {
      if (this.isValid()) {
         this.deactivateUI();
         SchedulerUtils.runTaskOrOmit(ShopkeepersPlugin.getInstance(), () -> {
            if (this.isValid()) {
               this.abort();
               if (task != null) {
                  task.run();
               }

            }
         });
      }
   }

   public final boolean open() {
      this.validateIsValid();
      Validate.State.isTrue(this.inventoryView == null, "Already opened!");
      InventoryView inventoryView = this.openInventoryView();
      if (inventoryView == null) {
         Log.debug("Failed to open the inventory view.");
         this.markInvalid();
         this.player.closeInventory();
         return false;
      } else {
         this.inventoryView = inventoryView;
         return true;
      }
   }

   @Nullable
   protected abstract InventoryView openInventoryView();

   public final InventoryView getInventoryView() {
      return (InventoryView)Validate.State.notNull(this.inventoryView, (String)"The view has not been opened yet!");
   }

   public final boolean hasInventoryView() {
      return this.inventoryView != null;
   }

   protected Inventory getInventory() {
      InventoryView inventoryView = this.getInventoryView();
      return inventoryView.getTopInventory();
   }

   protected final void debugNotOpeningUI(Player player, String reason) {
      Validate.notEmpty(reason, "reason is null or empty");
      this.getProvider().debugNotOpeningUI(this.getPlayer(), reason);
   }

   public boolean isHandling(InventoryView view) {
      return this.inventoryView == view;
   }

   public final boolean isOpenFor(Player player) {
      if (this.inventoryView == null) {
         return false;
      } else {
         View view = UISessionManager.getInstance().getUISession(player);
         if (view != this) {
            return false;
         } else {
            return Settings.disableInventoryVerification || this.isHandling(player.getOpenInventory());
         }
      }
   }

   public final boolean isOpen() {
      return this.isOpenFor(this.getPlayer());
   }

   public final boolean abortIfContextInvalid() {
      ViewContext viewContext = this.getContext();
      if (viewContext.isValid()) {
         return false;
      } else {
         TextUtils.sendMessage(this.player, (Text)viewContext.getNoLongerValidMessage());
         if (this.isValid()) {
            Log.debug(() -> {
               String var10000 = viewContext.getLogPrefix();
               return var10000 + "Closing '" + this.getUIType().getIdentifier() + "' for " + this.player.getName() + ": View context is no longer valid.";
            });
            this.abortDelayed();
         }

         return true;
      }
   }

   public final void syncInventory() {
      this.getPlayer().updateInventory();
   }

   public void updateSlot(int slot) {
      this.updateInventory();
   }

   public final void updateSlotInAllViews(int slot) {
      UISessionManager.getInstance().getUISessionsForContext(this.getContext().getObject(), this.getUIType()).forEach((view) -> {
         view.updateSlot(slot);
      });
   }

   public void updateArea(String area) {
      this.updateInventory();
   }

   public final void updateAreaInAllViews(String area) {
      UISessionManager.getInstance().getUISessionsForContext(this.getContext().getObject(), this.getUIType()).forEach((view) -> {
         view.updateArea(area);
      });
   }

   public abstract void updateInventory();

   public final void updateAllViews() {
      UISessionManager.getInstance().getUISessionsForContext(this.getContext().getObject(), this.getUIType()).forEach((view) -> {
         view.updateInventory();
      });
   }

   public UIState captureState() {
      return this.getInitialUIState();
   }

   public boolean isAcceptedState(UIState uiState) {
      return uiState == UIState.EMPTY;
   }

   public final void validateState(UIState uiState) {
      Validate.notNull(uiState, (String)"uiState is null");
      Validate.isTrue(this.isAcceptedState(uiState), () -> {
         String var10000 = uiState.getClass().getName();
         return "uiState of type '" + var10000 + "' is not accepted by view '" + this.getClass().getName() + "'";
      });
   }

   public void restoreState(UIState uiState) {
      this.validateState(uiState);
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
   }

   protected Set<? extends Class<? extends InventoryEvent>> getAdditionalInventoryEvents() {
      return Collections.emptySet();
   }

   boolean informOnInventoryEventEarly(InventoryEvent event) {
      if (!this.isInventoryEventHandled(event)) {
         return false;
      } else {
         debugInventoryEvent(event);
         this.onInventoryEventEarly(event);
         if (event instanceof InventoryClickEvent) {
            this.informOnInventoryClickEarly((InventoryClickEvent)event);
         } else if (event instanceof InventoryDragEvent) {
            this.informOnInventoryDragEarly((InventoryDragEvent)event);
         }

         return true;
      }
   }

   private boolean isInventoryEventHandled(InventoryEvent event) {
      Player player = this.getPlayer();
      ViewContext viewContext = this.getContext();
      InventoryView inventoryView = event.getView();

      assert player.equals(inventoryView.getPlayer());

      if (!this.isUIActive()) {
         Log.debug(() -> {
            String var10000 = viewContext.getLogPrefix();
            return var10000 + "Ignoring " + event.getEventName() + " of " + player.getName() + ": UI '" + this.getUIType().getIdentifier() + "' has been deactivated and is probably about to get closed.";
         });
         EventUtils.setCancelled(event, true);
         return false;
      } else if (this.abortIfContextInvalid()) {
         EventUtils.setCancelled(event, true);
         return false;
      } else if (!Settings.disableInventoryVerification && !this.isHandling(inventoryView)) {
         Log.debug(() -> {
            String var10000 = viewContext.getLogPrefix();
            return var10000 + "Closing inventory of type " + String.valueOf(inventoryView.getType()) + " with title '" + inventoryView.getTitle() + "' for " + player.getName() + ", because a different open inventory was expected for '" + this.getUIType().getIdentifier() + "'.";
         });
         EventUtils.setCancelled(event, true);
         this.abortDelayed();
         return false;
      } else {
         return true;
      }
   }

   private static void debugInventoryEvent(InventoryEvent event) {
      if (event instanceof InventoryClickEvent) {
         debugInventoryClickEvent((InventoryClickEvent)event);
      } else if (event instanceof InventoryDragEvent) {
         debugInventoryDragEvent((InventoryDragEvent)event);
      } else {
         debugOtherInventoryEvent(event);
      }

   }

   private static void debugInventoryClickEvent(InventoryClickEvent event) {
      InventoryView view = event.getView();
      Player player = (Player)view.getPlayer();
      Log.debug(() -> {
         String var10000 = player.getName();
         return "Inventory click: player=" + var10000 + ", view-type=" + String.valueOf(view.getType()) + ", view-title=" + view.getTitle() + ", raw-slot-id=" + event.getRawSlot() + ", slot-id=" + event.getSlot() + ", slot-type=" + String.valueOf(event.getSlotType()) + ", shift=" + event.isShiftClick() + ", hotbar key=" + event.getHotbarButton() + ", left-or-right=" + (event.isLeftClick() ? "left" : (event.isRightClick() ? "right" : "unknown")) + ", click-type=" + String.valueOf(event.getClick()) + ", action=" + String.valueOf(event.getAction()) + ", time: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
      });
   }

   private static void debugInventoryDragEvent(InventoryDragEvent event) {
      InventoryView view = event.getView();
      Player player = (Player)view.getPlayer();
      Log.debug(() -> {
         String var10000 = player.getName();
         return "Inventory dragging: player=" + var10000 + ", view-type=" + String.valueOf(view.getType()) + ", view-title=" + view.getTitle() + ", drag-type=" + String.valueOf(event.getType());
      });
   }

   private static void debugOtherInventoryEvent(InventoryEvent event) {
      InventoryView view = event.getView();
      Player player = (Player)view.getPlayer();
      Log.debug(() -> {
         String var10000 = event.getClass().getSimpleName();
         return "Inventory event (" + var10000 + "): player=" + player.getName() + ", view-type=" + String.valueOf(view.getType()) + ", view-title=" + view.getTitle();
      });
   }

   protected void onInventoryEventEarly(InventoryEvent event) {
   }

   void informOnInventoryEventLate(InventoryEvent event) {
      if (!this.isValid()) {
         Log.debug(() -> {
            String var10000 = this.getContext().getLogPrefix();
            return var10000 + "Ignoring late inventory event (" + event.getEventName() + "): UI '" + this.getUIType().getIdentifier() + "' of player " + this.getPlayer().getName() + " is no longer valid. Some plugin might have unexpectedly closed the inventory while the event was still being processed!";
         });
      } else {
         this.onInventoryEventLate(event);
         if (event instanceof InventoryClickEvent) {
            this.informOnInventoryClickLate((InventoryClickEvent)event);
         } else if (event instanceof InventoryDragEvent) {
            this.informOnInventoryDragLate((InventoryDragEvent)event);
         }

      }
   }

   protected void onInventoryEventLate(InventoryEvent event) {
   }

   protected final boolean isAutomaticShiftLeftClick() {
      return this.isAutomaticShiftLeftClick;
   }

   private void informOnInventoryClickEarly(InventoryClickEvent event) {
      this.isAutomaticShiftLeftClick = false;
      long nowNanos = System.nanoTime();
      if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getRawSlot() != this.lastManualClickedSlotId && nowNanos - this.lastManualClickNanos < AUTOMATIC_SHIFT_LEFT_CLICK_NANOS) {
         this.isAutomaticShiftLeftClick = true;
         Log.debug("  Detected automatically triggered shift left-click! (on different slot)");
      }

      if (!this.isAutomaticShiftLeftClick) {
         this.lastManualClickNanos = nowNanos;
         this.lastManualClickedSlotId = event.getRawSlot();
      }

      this.onInventoryClickEarly(event);
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
   }

   private void informOnInventoryClickLate(InventoryClickEvent event) {
      this.onInventoryClickLate(event);
   }

   protected void onInventoryClickLate(InventoryClickEvent event) {
   }

   private void informOnInventoryDragEarly(InventoryDragEvent event) {
      this.onInventoryDragEarly(event);
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
   }

   private void informOnInventoryDragLate(InventoryDragEvent event) {
      this.onInventoryDragLate(event);
   }

   protected void onInventoryDragLate(InventoryDragEvent event) {
   }

   static {
      AUTOMATIC_SHIFT_LEFT_CLICK_NANOS = TimeUnit.MILLISECONDS.toNanos(250L);
   }
}
