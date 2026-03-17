package com.bergerkiller.bukkit.tc.attachments.ui.models;

import com.bergerkiller.bukkit.common.block.InputDialogSubmitText;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.DialogBuilder;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.DialogResult;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedEntry;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedItemModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

class ResourcePackModelListingDialog implements Listener {
   private static final int DISPLAYED_ITEM_COUNT = 36;
   private static final CommonItemStack BG_ITEM1 = CommonItemStack.empty();
   private static final CommonItemStack BG_ITEM2;
   private static final CommonItemStack BG_ITEM3;
   private static Map<Player, ResourcePackModelListingDialog> shownTo;
   private final DialogBuilder options;
   private final CompletableFuture<DialogResult> future;
   private final ResourcePackModelListingDialog.UIButton btnPrevPage;
   private final ResourcePackModelListingDialog.UIButton btnBetweenpages;
   private final ResourcePackModelListingDialog.UIButton btnNextPage;
   private final ResourcePackModelListingDialog.UIButton btnBack;
   private final ResourcePackModelListingDialog.UIButton btnSearch;
   private final List<ResourcePackModelListingDialog.UIButton> buttons;
   private Inventory inventory;
   private ResourcePackModelListing currentListing;
   private ListedEntry current;
   private List<? extends ListedEntry> currentItems;
   private boolean futureDisabled;

   public static CompletableFuture<DialogResult> show(DialogBuilder dialogOptions) {
      ResourcePackModelListingDialog dialog = new ResourcePackModelListingDialog(dialogOptions.clone());
      ResourcePackModelListingDialog prev = (ResourcePackModelListingDialog)shownTo.put(dialog.player(), dialog);
      if (prev != null) {
         prev.close();
      }

      dialog.open();
      return dialog.future;
   }

   public static void closeAll() {
      List<ResourcePackModelListingDialog> dialogs = new ArrayList(shownTo.values());
      shownTo.clear();
      dialogs.forEach(ResourcePackModelListingDialog::close);
   }

   public static void closeAllByPlugin(Plugin plugin) {
      Iterator var1 = (new ArrayList(shownTo.values())).iterator();

      while(var1.hasNext()) {
         ResourcePackModelListingDialog dialog = (ResourcePackModelListingDialog)var1.next();
         if (dialog.options.plugin() == plugin) {
            shownTo.remove(dialog.options.player());
            dialog.close();
         }
      }

   }

   public static void close(Player player) {
      ResourcePackModelListingDialog prev = (ResourcePackModelListingDialog)shownTo.remove(player);
      if (prev != null) {
         prev.close();
      }

   }

   private ResourcePackModelListingDialog(DialogBuilder options) {
      this(options, new CompletableFuture());
   }

   private ResourcePackModelListingDialog(DialogBuilder options, CompletableFuture<DialogResult> future) {
      this.btnPrevPage = new ResourcePackModelListingDialog.PrevPageButton();
      this.btnBetweenpages = new ResourcePackModelListingDialog.BetweenPageButton();
      this.btnNextPage = new ResourcePackModelListingDialog.NextPageButton();
      this.btnBack = new ResourcePackModelListingDialog.BackButton();
      this.btnSearch = new ResourcePackModelListingDialog.SearchButton();
      this.buttons = Arrays.asList(this.btnPrevPage, this.btnBetweenpages, this.btnNextPage, this.btnBack, this.btnSearch);
      this.futureDisabled = false;
      this.options = options;
      this.future = future;
   }

   public void open() {
      if (this.options.getQuery().isEmpty()) {
         this.currentListing = this.options.listing();
      } else {
         this.currentListing = this.options.listing().filter(this.options.getQuery());
      }

      Bukkit.getPluginManager().registerEvents(this, this.options.plugin());
      this.inventory = Bukkit.createInventory(this.player(), 54, this.options.getTitle());
      ListedEntry initialEntry = ((ListedEntry)this.currentListing.root().findAtPath(ListedEntry.tokenizePath(this.options.getBrowsedPath())).orElse(this.currentListing.root())).compactIf(this.options.isCompactingEnabled());
      this.navigate(initialEntry, this.options.getBrowsedPage());
      this.player().openInventory(this.inventory);
   }

   public void close() {
      this.cancelDialog(false);
      if (this.player().getOpenInventory() != null && this.player().getOpenInventory().getTopInventory() == this.inventory) {
         this.player().closeInventory();
      }

   }

   public void closeAndShowSearchDialog(String initialQuery) {
      this.futureDisabled = true;
      this.close();
      final DialogBuilder newOptions = this.options.clone();
      final CompletableFuture<DialogResult> future = this.future;
      (new InputDialogSubmitText(this.options.plugin(), this.options.player()) {
         public void onAccept(String text) {
            newOptions.query(text);
            newOptions.navigate("", 0);
            CompletableFuture var10000 = newOptions.show();
            CompletableFuture var10001 = future;
            Objects.requireNonNull(var10001);
            var10000.thenAccept(var10001::complete);
         }

         public void onCancel() {
            CompletableFuture var10000 = newOptions.show();
            CompletableFuture var10001 = future;
            Objects.requireNonNull(var10001);
            var10000.thenAccept(var10001::complete);
         }
      }).setDescription("Enter search query").setInitialText(newOptions.getQuery()).setAcceptEmptyText(true).open();
   }

   private Player player() {
      return this.options.player();
   }

   private void complete(DialogResult result) {
      if (!this.futureDisabled) {
         this.future.complete(result);
      }

   }

   private ResourcePackModelListingDialog.ClickAction onItemClicked(ListedItemModel item) {
      if (this.options.isCreativeMenu()) {
         return ResourcePackModelListingDialog.ClickAction.CREATIVE_CLICK_PICKUP;
      } else {
         this.complete(new DialogResult(this.options, item));
         return ResourcePackModelListingDialog.ClickAction.CLOSE_DIALOG;
      }
   }

   private boolean tryNavigateBack(boolean toRoot) {
      ListedEntry e = this.current;
      if (!toRoot) {
         while(e.parent() != null) {
            e = e.parent();
            if (e.compact() == e) {
               break;
            }
         }
      } else {
         while(true) {
            if (e.parent() == null) {
               e = e.compactIf(this.options.isCompactingEnabled());
               break;
            }

            e = e.parent();
         }
      }

      if (e != this.current) {
         this.navigate(e, 0);
         return true;
      } else {
         return false;
      }
   }

   private ResourcePackModelListingDialog.ClickAction handleClick(int clickedSlot, boolean isRightClick, ItemStack cursorItem) {
      if (this.options.isCreativeMenu() && !ItemUtil.isEmpty(cursorItem) && clickedSlot >= 0 && clickedSlot < 54) {
         ItemStack itemInSlot = this.inventory.getItem(clickedSlot);
         if (!isRightClick && itemInSlot != null && ItemUtil.equalsIgnoreAmount(itemInSlot, cursorItem)) {
            return cursorItem.getAmount() < cursorItem.getMaxStackSize() ? ResourcePackModelListingDialog.ClickAction.CREATIVE_CLICK_INCREASE_COUNT : ResourcePackModelListingDialog.ClickAction.HANDLED;
         } else {
            return ResourcePackModelListingDialog.ClickAction.CREATIVE_CLICK_CONSUME;
         }
      } else {
         Iterator var4 = this.buttons.iterator();

         ResourcePackModelListingDialog.UIButton button;
         do {
            if (!var4.hasNext()) {
               if (isRightClick) {
                  if (!this.tryNavigateBack(false) && this.options.isCancelOnRootRightClick()) {
                     this.complete(new DialogResult(this.options, true));
                     return ResourcePackModelListingDialog.ClickAction.CLOSE_DIALOG;
                  }

                  return ResourcePackModelListingDialog.ClickAction.HANDLED;
               }

               int offset = this.options.getBrowsedPage() * 36;
               int limit = Math.min(36, this.currentItems.size() - offset);
               if (clickedSlot < limit) {
                  ListedEntry e = (ListedEntry)this.currentItems.get(clickedSlot + offset);
                  if (e instanceof ListedItemModel) {
                     return this.onItemClicked((ListedItemModel)e);
                  }

                  this.navigate(e, 0);
               }

               return ResourcePackModelListingDialog.ClickAction.HANDLED;
            }

            button = (ResourcePackModelListingDialog.UIButton)var4.next();
         } while(button.slot != clickedSlot);

         button.click(isRightClick);
         return ResourcePackModelListingDialog.ClickAction.HANDLED;
      }
   }

   private void navigate(ListedEntry current, int page) {
      this.current = current;
      this.currentItems = current.displayedItems(36, this.options.isCompactingEnabled());
      this.options.navigate(current.fullPath(), this.clampPage(page));
      this.updateItems();
   }

   private void incrementPage(int incr) {
      int newPage = this.clampPage(this.options.getBrowsedPage() + incr);
      if (newPage != this.options.getBrowsedPage()) {
         this.options.navigate(this.current.fullPath(), newPage);
         this.updateItems();
      }

   }

   private int clampPage(int newPage) {
      return newPage < 0 ? 0 : Math.min(newPage, this.currentItems.size() / 36);
   }

   private void updateItemsNextTick() {
      Bukkit.getScheduler().scheduleSyncDelayedTask(this.options.plugin(), this::updateItems);
   }

   private void updateItems() {
      int page = this.options.getBrowsedPage();
      int offset = page * 36;
      int limit = Math.min(36, this.currentItems.size() - offset);
      this.btnPrevPage.enabled = page > 0;
      this.btnNextPage.enabled = this.currentItems.size() - offset > 36;

      int i;
      for(i = 0; i < limit; ++i) {
         this.inventory.setItem(i, ((ListedEntry)this.currentItems.get(i + offset)).createIconItem(this.options).toBukkit());
      }

      for(i = limit; i < 36; ++i) {
         this.inventory.setItem(i, BG_ITEM1.toBukkit());
      }

      for(i = 36; i < 45; ++i) {
         this.inventory.setItem(i, BG_ITEM2.toBukkit());
      }

      Iterator var9 = this.buttons.iterator();

      while(var9.hasNext()) {
         ResourcePackModelListingDialog.UIButton button = (ResourcePackModelListingDialog.UIButton)var9.next();
         this.inventory.setItem(button.slot, button.item().toBukkit());
      }

      for(i = 45; i < 54; ++i) {
         boolean isButtonSlot = false;
         Iterator var6 = this.buttons.iterator();

         while(var6.hasNext()) {
            ResourcePackModelListingDialog.UIButton button = (ResourcePackModelListingDialog.UIButton)var6.next();
            if (button.slot == i) {
               isButtonSlot = true;
               break;
            }
         }

         if (!isButtonSlot) {
            this.inventory.setItem(i, BG_ITEM3.toBukkit());
         }
      }

   }

   private void cancelDialog(boolean delayEvent) {
      CommonUtil.unregisterListener(this);
      ResourcePackModelListingDialog dialog = (ResourcePackModelListingDialog)shownTo.remove(this.player());
      if (dialog != null && dialog != this) {
         shownTo.put(this.player(), dialog);
      }

      DialogResult result = new DialogResult(this.options, false);
      if (delayEvent) {
         Bukkit.getScheduler().scheduleSyncDelayedTask(this.options.plugin(), () -> {
            this.complete(result);
         });
      } else {
         this.complete(result);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onPlayerQuit(PlayerQuitEvent event) {
      if (event.getPlayer() == this.player()) {
         this.cancelDialog(false);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void onPlayerTeleport(PlayerTeleportEvent event) {
      if (event.getPlayer() == this.player()) {
         this.close();
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onInventoryClose(InventoryCloseEvent event) {
      if (event.getPlayer() == this.player() && event.getInventory() == this.inventory) {
         this.cancelDialog(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   private void onInventoryDrag(InventoryDragEvent event) {
      if (event.getWhoClicked() == this.player() && event.getInventory() == this.inventory) {
         long numDraggedInDialog = event.getRawSlots().stream().mapToInt(Integer::intValue).filter((i) -> {
            return i >= 0 && i < 54;
         }).count();
         if (this.options.isCreativeMenu() && numDraggedInDialog == (long)event.getRawSlots().size()) {
            ItemStack cursorAfterTmp = event.getCursor();
            if (!ItemUtil.isEmpty(event.getOldCursor()) && event.getType() == DragType.EVEN && numDraggedInDialog == 1L && ItemUtil.equalsIgnoreAmount(ItemUtil.createItem(event.getOldCursor()), this.inventory.getItem((Integer)event.getInventorySlots().iterator().next()))) {
               cursorAfterTmp = event.getOldCursor().clone();
               if (cursorAfterTmp.getAmount() < cursorAfterTmp.getMaxStackSize()) {
                  cursorAfterTmp.setAmount(cursorAfterTmp.getAmount() + 1);
               }
            }

            ItemStack cursorAfter = ItemUtil.cloneItem(cursorAfterTmp);
            ItemStack cursorExpected = ItemUtil.cloneItem(event.getOldCursor());
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.options.plugin(), () -> {
               if (LogicUtil.bothNullOrEqual(this.player().getItemOnCursor(), cursorExpected)) {
                  this.player().setItemOnCursor(cursorAfter);
               }

            });
            event.setCursor(cursorAfter);
            event.setResult(Result.DENY);
         } else if (numDraggedInDialog > 0L) {
            event.setResult(Result.DENY);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   private void onInventoryClick(InventoryClickEvent event) {
      if (event.getWhoClicked() == this.player()) {
         Inventory clickedInventory = ItemUtil.getClickedInventory(event);
         if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getInventory() == this.inventory && clickedInventory != this.inventory) {
            if (this.options.isCreativeMenu()) {
               event.setResult(Result.DENY);
               event.setCurrentItem((ItemStack)null);
            } else {
               event.setResult(Result.DENY);
            }

         } else if ((event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) && this.options.isCreativeMenu() && !ItemUtil.isEmpty(event.getCursor()) && clickedInventory == this.inventory && event.getSlot() >= 0 && event.getSlot() < 54) {
            if (event.getCursor().getAmount() > 1) {
               ItemStack updated = event.getCursor().clone();
               updated.setAmount(updated.getAmount() - 1);
               event.setCursor(updated);
               event.setResult(Result.DENY);
            } else {
               event.setCursor((ItemStack)null);
               event.setResult(Result.DENY);
            }

         } else {
            boolean hasItemThatMatches;
            if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR && clickedInventory != this.inventory && !ItemUtil.isEmpty(event.getCursor())) {
               hasItemThatMatches = false;
               ItemStack match = ItemUtil.createItem(event.getCursor());

               for(int i = 0; i < 54; ++i) {
                  ItemStack invItem = this.inventory.getItem(i);
                  if (invItem != null && ItemUtil.equalsIgnoreAmount(invItem, match)) {
                     hasItemThatMatches = true;
                     this.inventory.setItem(i, (ItemStack)null);
                  }
               }

               if (hasItemThatMatches) {
                  this.updateItemsNextTick();
               }
            }

            if (clickedInventory == this.inventory) {
               hasItemThatMatches = event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT;
               ResourcePackModelListingDialog.ClickAction action = this.handleClick(event.getSlot(), hasItemThatMatches, event.getCursor());
               ItemStack pickedItem;
               switch(action) {
               case CREATIVE_CLICK_CONSUME:
                  event.setCursor((ItemStack)null);
                  event.setResult(Result.DENY);
                  break;
               case CREATIVE_CLICK_PICKUP:
                  if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                     event.setResult(Result.ALLOW);
                     this.updateItemsNextTick();
                  } else if (event.getCurrentItem() != null) {
                     pickedItem = event.getCurrentItem().clone();
                     if (event.getAction() == InventoryAction.CLONE_STACK) {
                        pickedItem.setAmount(pickedItem.getMaxStackSize());
                     }

                     event.setCursor(pickedItem);
                     event.setResult(Result.DENY);
                  } else {
                     this.updateItemsNextTick();
                     event.setCursor((ItemStack)null);
                     event.setResult(Result.DENY);
                  }
                  break;
               case CREATIVE_CLICK_INCREASE_COUNT:
                  pickedItem = event.getCursor().clone();
                  pickedItem.setAmount(pickedItem.getAmount() + 1);
                  event.setCursor(pickedItem);
                  event.setResult(Result.DENY);
                  break;
               case CLOSE_DIALOG:
                  event.setResult(Result.DENY);
                  this.close();
                  break;
               case HANDLED:
                  event.setResult(Result.DENY);
               }

            }
         }
      }
   }

   private CommonItemStack applyPageInfo(CommonItemStack item, boolean isMiddleCountItem) {
      if (item.isEmpty()) {
         return CommonItemStack.empty();
      } else {
         int pageCount = 1 + this.currentItems.size() / 36;
         if (pageCount == 1) {
            return item;
         } else {
            item = item.clone();
            int currPage = this.options.getBrowsedPage() + 1;
            if (isMiddleCountItem) {
               item.setCustomNameMessage(ChatColor.DARK_GRAY + "Currently on");
               if (currPage <= 64) {
                  item.setAmount(currPage);
               }
            } else {
               item.addLoreLine().addLoreMessage(ChatColor.DARK_GRAY + "Currently on");
            }

            item.addLoreMessage(ChatColor.DARK_GRAY + "page " + ChatColor.GRAY + currPage + ChatColor.DARK_GRAY + " of " + ChatColor.GRAY + pageCount);
            return item;
         }
      }
   }

   private static CommonItemStack createItem(String... materialNames) {
      return CommonItemStack.create(MaterialUtil.getFirst(materialNames), 1);
   }

   private static CommonItemStack createGlassPaneItem(DyeColor color) {
      try {
         CommonItemStack item;
         if (CommonCapabilities.MATERIAL_ENUM_CHANGES) {
            item = CommonItemStack.create(MaterialUtil.getMaterial(color.name() + "_STAINED_GLASS_PANE"), 1);
         } else {
            item = BlockData.fromMaterialData(MaterialUtil.getMaterial("LEGACY_STAINED_GLASS_PANE"), color.getWoolData()).createCommonItem(1);
         }

         return item.setEmptyCustomName();
      } catch (Throwable var2) {
         return null;
      }
   }

   static {
      BG_ITEM2 = createGlassPaneItem(DyeColor.BROWN);
      BG_ITEM3 = createGlassPaneItem(DyeColor.GRAY);
      shownTo = new HashMap();
   }

   private class PrevPageButton extends ResourcePackModelListingDialog.UIButton {
      private final CommonItemStack enabledIconItem;
      private final CommonItemStack disabledIconItem;

      public PrevPageButton() {
         super(3);
         this.enabledIconItem = ResourcePackModelListingDialog.createItem("DIAMOND_BLOCK", "LEGACY_DIAMOND_BLOCK").setCustomNameMessage(ChatColor.GREEN + "Previous Page");
         this.disabledIconItem = ResourcePackModelListingDialog.createItem("CLAY", "LEGACY_CLAY").setCustomNameMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "Previous Page");
      }

      public CommonItemStack item() {
         return ResourcePackModelListingDialog.this.applyPageInfo(this.enabled ? this.enabledIconItem : this.disabledIconItem, false);
      }

      public void click(boolean isRightClick) {
         ResourcePackModelListingDialog.this.incrementPage(-1);
      }
   }

   private abstract static class UIButton {
      public final int slot;
      public boolean enabled = true;

      public UIButton(int slot) {
         this.slot = 45 + slot;
      }

      public abstract CommonItemStack item();

      public abstract void click(boolean var1);
   }

   private class BetweenPageButton extends ResourcePackModelListingDialog.UIButton {
      public BetweenPageButton() {
         super(4);
      }

      public CommonItemStack item() {
         return ResourcePackModelListingDialog.this.applyPageInfo(ResourcePackModelListingDialog.BG_ITEM3, true);
      }

      public void click(boolean isRightClick) {
      }
   }

   private class NextPageButton extends ResourcePackModelListingDialog.UIButton {
      private final CommonItemStack enabledIconItem;
      private final CommonItemStack disabledIconItem;

      public NextPageButton() {
         super(5);
         this.enabledIconItem = ResourcePackModelListingDialog.createItem("DIAMOND_BLOCK", "LEGACY_DIAMOND_BLOCK").setCustomNameMessage(ChatColor.GREEN + "Next Page");
         this.disabledIconItem = ResourcePackModelListingDialog.createItem("CLAY", "LEGACY_CLAY").setCustomNameMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "Next Page");
      }

      public CommonItemStack item() {
         return ResourcePackModelListingDialog.this.applyPageInfo(this.enabled ? this.enabledIconItem : this.disabledIconItem, false);
      }

      public void click(boolean isRightClick) {
         ResourcePackModelListingDialog.this.incrementPage(1);
      }
   }

   private class BackButton extends ResourcePackModelListingDialog.UIButton {
      private final CommonItemStack backIconItem;

      public BackButton() {
         super(0);
         this.backIconItem = ResourcePackModelListingDialog.createItem("BOOK", "LEGACY_BOOK").setCustomNameMessage(ChatColor.YELLOW + "Back").addLoreLine().addLoreMessage(ChatColor.BLUE.toString() + ChatColor.ITALIC + "Right-click to go").addLoreMessage(ChatColor.BLUE.toString() + ChatColor.ITALIC + "all the way back");
      }

      public CommonItemStack item() {
         return this.backIconItem;
      }

      public void click(boolean isRightClick) {
         if (!ResourcePackModelListingDialog.this.tryNavigateBack(isRightClick) && !isRightClick && ResourcePackModelListingDialog.this.options.isCancelOnRootRightClick()) {
            ResourcePackModelListingDialog.this.complete(new DialogResult(ResourcePackModelListingDialog.this.options, true));
            ResourcePackModelListingDialog.this.close();
         }

      }
   }

   private class SearchButton extends ResourcePackModelListingDialog.UIButton {
      private final CommonItemStack searchIconItem;

      public SearchButton() {
         super(8);
         this.searchIconItem = ResourcePackModelListingDialog.createItem("COMPASS", "LEGACY_COMPASS").setCustomNameMessage(ChatColor.YELLOW + "Enter search query");
      }

      public CommonItemStack item() {
         CommonItemStack item = this.searchIconItem.clone();
         if (!ResourcePackModelListingDialog.this.options.getQuery().isEmpty()) {
            item.addLoreLine().addLoreMessage(ChatColor.DARK_GRAY + "Current: " + ChatColor.GRAY + ChatColor.ITALIC + "\"" + ResourcePackModelListingDialog.this.options.getQuery() + "\"").addLoreMessage(ChatColor.BLUE.toString() + ChatColor.ITALIC + "Right-click to clear");
         }

         return item;
      }

      public void click(boolean isRightClick) {
         if (isRightClick) {
            ResourcePackModelListingDialog.this.options.query("");
            ResourcePackModelListingDialog.this.navigate(ResourcePackModelListingDialog.this.options.listing().root().compactIf(ResourcePackModelListingDialog.this.options.isCompactingEnabled()), 0);
         } else {
            ResourcePackModelListingDialog.this.closeAndShowSearchDialog(ResourcePackModelListingDialog.this.options.getQuery());
         }

      }
   }

   private static enum ClickAction {
      HANDLED,
      CLOSE_DIALOG,
      CREATIVE_CLICK_PICKUP,
      CREATIVE_CLICK_CONSUME,
      CREATIVE_CLICK_INCREASE_COUNT;

      // $FF: synthetic method
      private static ResourcePackModelListingDialog.ClickAction[] $values() {
         return new ResourcePackModelListingDialog.ClickAction[]{HANDLED, CLOSE_DIALOG, CREATIVE_CLICK_PICKUP, CREATIVE_CLICK_CONSUME, CREATIVE_CLICK_INCREASE_COUNT};
      }
   }
}
