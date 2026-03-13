package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.InventoryViewUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class EditorView extends View {
   protected static final String AREA_BUTTONS = "buttons";
   @Nullable
   private List<TradingRecipeDraft> recipes;
   @Nullable
   private Inventory inventory;
   private int currentPage = 1;

   public EditorView(AbstractEditorViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   protected AbstractEditorViewProvider getEditorViewProvider() {
      return (AbstractEditorViewProvider)this.getProvider();
   }

   protected final EditorLayout getLayout() {
      return this.getEditorViewProvider().getLayout();
   }

   protected final TradingRecipesAdapter getTradingRecipesAdapter() {
      return this.getEditorViewProvider().tradingRecipesAdapter;
   }

   public final Inventory getInventory() {
      return (Inventory)Validate.State.notNull(this.inventory, (String)"Inventory not yet set up!");
   }

   public final List<TradingRecipeDraft> getRecipes() {
      return (List)Validate.State.notNull(this.recipes, (String)"Recipes not yet set up!");
   }

   public UIState captureState() {
      return new EditorUIState(this.getCurrentPage());
   }

   public boolean isAcceptedState(UIState uiState) {
      return uiState == UIState.EMPTY || uiState instanceof EditorUIState;
   }

   public void restoreState(UIState uiState) {
      super.restoreState(uiState);
      EditorUIState editorState = (EditorUIState)uiState;
      this.switchPage(editorState.getCurrentPage(), true);
   }

   @Nullable
   protected InventoryView openInventoryView() {
      Player player = this.getPlayer();
      EditorLayout layout = this.getLayout();
      List<TradingRecipeDraft> recipes = this.getTradingRecipesAdapter().getTradingRecipes();
      Inventory inventory = Bukkit.createInventory((InventoryHolder)null, layout.getInventorySize(), this.getTitle());
      this.inventory = inventory;
      this.recipes = recipes;
      int page = 1;
      UIState var7 = this.getInitialUIState();
      if (var7 instanceof EditorUIState) {
         EditorUIState editorState = (EditorUIState)var7;
         page = this.getValidPage(editorState.getCurrentPage());
      }

      this.setPage(page);
      this.setupCurrentPage();
      return player.openInventory(inventory);
   }

   protected abstract String getTitle();

   protected void setupCurrentPage() {
      this.setupTradeColumns();
      this.setupTradesPageBar();
      this.setupButtons();
   }

   protected void setupTradeColumns() {
      Inventory inventory = this.getInventory();
      int page = this.getCurrentPage();

      assert page >= 1;

      List<TradingRecipeDraft> recipes = this.getRecipes();
      int recipeStartIndex = (page - 1) * 9;

      for(int column = 0; column < 9; ++column) {
         int recipeIndex = recipeStartIndex + column;
         if (recipeIndex < recipes.size()) {
            TradingRecipeDraft recipe = (TradingRecipeDraft)recipes.get(recipeIndex);
            this.setTradeColumn(inventory, column, recipe);
         } else {
            this.setTradeColumn(inventory, column, TradingRecipeDraft.EMPTY);
         }
      }

   }

   protected void setupTradesPageBar() {
      Inventory inventory = this.getInventory();

      for(int i = 27; i <= 35; ++i) {
         inventory.setItem(i, (ItemStack)null);
      }

      Button[] buttons = this.getLayout().getTradesPageBarButtons();

      for(int i = 0; i < buttons.length; ++i) {
         Button button = buttons[i];
         if (button != null) {
            ItemStack icon = button.getIcon(this);
            if (icon != null) {
               inventory.setItem(button.getSlot(), icon);
            }
         }
      }

   }

   protected void setupButtons() {
      Inventory inventory = this.getInventory();
      int inventorySize = inventory.getSize();
      Button[] buttons = this.getLayout().getBakedButtons();

      for(int buttonIndex = 0; buttonIndex < buttons.length; ++buttonIndex) {
         int slot = 36 + buttonIndex;
         if (slot >= inventorySize) {
            break;
         }

         ItemStack icon = null;
         Button button = buttons[buttonIndex];
         if (button != null) {
            icon = button.getIcon(this);
         }

         inventory.setItem(slot, icon);
      }

   }

   protected TradingRecipeDraft getEmptyTrade() {
      return TradingRecipeDraft.EMPTY;
   }

   protected TradingRecipeDraft getEmptyTradeSlotItems() {
      return TradingRecipeDraft.EMPTY;
   }

   private boolean isEmptyResultItem(@Nullable @ReadOnly ItemStack slotItem) {
      ItemStack item = ItemUtils.getNullIfEmpty(slotItem);
      if (item == null) {
         return true;
      } else if (ItemUtils.equals(this.getEmptyTrade().getResultItem(), item)) {
         return true;
      } else {
         return ItemUtils.equals(this.getEmptyTradeSlotItems().getResultItem(), item);
      }
   }

   private boolean isEmptyItem1(@Nullable @ReadOnly ItemStack slotItem) {
      ItemStack item = ItemUtils.getNullIfEmpty(slotItem);
      if (item == null) {
         return true;
      } else if (ItemUtils.equals(this.getEmptyTrade().getItem1(), item)) {
         return true;
      } else {
         return ItemUtils.equals(this.getEmptyTradeSlotItems().getItem1(), item);
      }
   }

   private boolean isEmptyItem2(@Nullable @ReadOnly ItemStack slotItem) {
      ItemStack item = ItemUtils.getNullIfEmpty(slotItem);
      if (item == null) {
         return true;
      } else if (ItemUtils.equals(this.getEmptyTrade().getItem2(), item)) {
         return true;
      } else {
         return ItemUtils.equals(this.getEmptyTradeSlotItems().getItem2(), item);
      }
   }

   @Nullable
   protected final ItemStack getTradeResultItem(Inventory inventory, int column) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      ItemStack item = inventory.getItem(this.getLayout().getResultItemSlot(column));
      return this.isEmptyResultItem(item) ? null : item;
   }

   @Nullable
   protected final ItemStack getTradeItem1(Inventory inventory, int column) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      ItemStack item = inventory.getItem(this.getLayout().getItem1Slot(column));
      return this.isEmptyItem1(item) ? null : item;
   }

   @Nullable
   protected final ItemStack getTradeItem2(Inventory inventory, int column) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      ItemStack item = inventory.getItem(this.getLayout().getItem2Slot(column));
      return this.isEmptyItem2(item) ? null : item;
   }

   protected void setTradeColumn(Inventory inventory, int column, TradingRecipeDraft recipe) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      assert recipe != null;

      TradingRecipeDraft emptySlotItems;
      if (recipe.isEmpty()) {
         emptySlotItems = this.getEmptyTrade();
      } else {
         emptySlotItems = this.getEmptyTradeSlotItems();
      }

      UnmodifiableItemStack resultItem = ItemUtils.getFallbackIfNull(recipe.getResultItem(), emptySlotItems.getResultItem());
      UnmodifiableItemStack item1 = ItemUtils.getFallbackIfNull(recipe.getItem1(), emptySlotItems.getItem1());
      UnmodifiableItemStack item2 = ItemUtils.getFallbackIfNull(recipe.getItem2(), emptySlotItems.getItem2());
      EditorLayout layout = this.getLayout();
      inventory.setItem(layout.getResultItemSlot(column), ItemUtils.asItemStackOrNull(resultItem));
      inventory.setItem(layout.getItem1Slot(column), ItemUtils.asItemStackOrNull(item1));
      inventory.setItem(layout.getItem2Slot(column), ItemUtils.asItemStackOrNull(item2));
   }

   protected TradingRecipeDraft getTradingRecipe(Inventory inventory, int column) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      ItemStack resultItem = this.getTradeResultItem(inventory, column);
      ItemStack item1 = this.getTradeItem1(inventory, column);
      ItemStack item2 = this.getTradeItem2(inventory, column);
      return new TradingRecipeDraft(resultItem, item1, item2);
   }

   protected final void updateTradeColumn(Inventory inventory, int column) {
      TradingRecipeDraft recipe = this.getTradingRecipe(inventory, column);
      this.setTradeColumn(inventory, column, recipe);
   }

   protected final boolean isEmptyTrade(Inventory inventory, int column) {
      assert inventory != null;

      assert this.getLayout().isTradeColumn(column);

      ItemStack resultItem = this.getTradeResultItem(inventory, column);
      if (resultItem != null) {
         return false;
      } else {
         ItemStack item1 = this.getTradeItem1(inventory, column);
         if (item1 != null) {
            return false;
         } else {
            ItemStack item2 = this.getTradeItem2(inventory, column);
            return item2 == null;
         }
      }
   }

   public final int getCurrentPage() {
      return this.currentPage;
   }

   void setPage(int newPage) {
      this.currentPage = newPage;
   }

   protected final int getValidPage(int targetPage) {
      int maxPage = this.getLayout().getMaxTradesPages();
      return Math.max(1, Math.min(maxPage, targetPage));
   }

   protected boolean switchPage(int targetPage, boolean saveCurrentPage) {
      int newPage = this.getValidPage(targetPage);
      int currentPage = this.getCurrentPage();
      if (newPage == currentPage) {
         return false;
      } else {
         if (saveCurrentPage) {
            this.saveEditorPage();
         }

         this.setPage(newPage);
         this.setupCurrentPage();
         this.updateInventory();
         return true;
      }
   }

   public void updateInventory() {
      this.updateButtons();
      this.syncInventory();
   }

   public void updateArea(String area) {
      if ("buttons".equals(area)) {
         this.updateButtons();
         this.syncInventory();
      }

   }

   public void updateSlot(int slot) {
      Button button = this.getLayout()._getButton(slot);
      if (button != null) {
         ItemStack icon = button.getIcon(this);
         this.getInventory().setItem(slot, icon);
         this.syncInventory();
      }
   }

   protected void updateButtons() {
      this.setupButtons();
   }

   void updateButtonsInAllViews() {
      this.updateAreaInAllViews("buttons");
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      if (!event.isCancelled()) {
         InventoryView view = event.getView();
         Set<Integer> slots = (Set)Unsafe.castNonNull(event.getRawSlots());
         Iterator var4 = slots.iterator();

         while(var4.hasNext()) {
            Integer rawSlotInteger = (Integer)var4.next();
            int rawSlot = rawSlotInteger;
            if (!this.getLayout().isTradesArea(rawSlot) && !InventoryViewUtils.isPlayerInventory(view, rawSlot)) {
               event.setCancelled(true);
               break;
            }
         }

      }
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      if (!this.isAutomaticShiftLeftClick()) {
         EditorLayout layout = this.getLayout();
         int rawSlot = event.getRawSlot();
         if (layout.isTradesArea(rawSlot)) {
            this.handleTradesClick(event);
         } else if (layout.isTradesPageBar(rawSlot)) {
            this.handleTradesPageBarClick(event);
         } else if (layout.isButtonArea(rawSlot)) {
            this.handleButtonClick(event);
         } else if (InventoryViewUtils.isPlayerInventory(event.getView(), rawSlot)) {
            this.handlePlayerInventoryClick(event);
         }

      }
   }

   protected void handleTradesClick(InventoryClickEvent event) {
      assert this.getLayout().isTradesArea(event.getRawSlot());

   }

   protected void handleTradesPageBarClick(InventoryClickEvent event) {
      assert this.getLayout().isTradesPageBar(event.getRawSlot());

      event.setCancelled(true);
      int rawSlot = event.getRawSlot();
      Button button = this.getLayout()._getTradesPageBarButton(rawSlot);
      if (button != null) {
         button.onClick(this, event);
      }

   }

   protected void handleButtonClick(InventoryClickEvent event) {
      assert this.getLayout().isButtonArea(event.getRawSlot());

      event.setCancelled(true);
      int rawSlot = event.getRawSlot();
      Button button = this.getLayout()._getButton(rawSlot);
      if (button != null) {
         button.onClick(this, event);
      }

   }

   protected void handlePlayerInventoryClick(InventoryClickEvent event) {
      assert InventoryViewUtils.isPlayerInventory(event.getView(), event.getRawSlot());

   }

   protected int getNewAmountAfterEditorClick(InventoryClickEvent event, int currentAmount, int minAmount, int maxAmount) {
      if (minAmount > maxAmount) {
         return currentAmount;
      } else if (minAmount == maxAmount) {
         return minAmount;
      } else {
         int newAmount = currentAmount;
         ClickType clickType = event.getClick();
         switch(clickType) {
         case LEFT:
            newAmount = currentAmount + 1;
            break;
         case SHIFT_LEFT:
            newAmount = currentAmount + 10;
            break;
         case RIGHT:
            newAmount = currentAmount - 1;
            break;
         case SHIFT_RIGHT:
            newAmount = currentAmount - 10;
            break;
         case MIDDLE:
            newAmount = minAmount;
            break;
         case NUMBER_KEY:
            assert event.getHotbarButton() >= 0;

            newAmount = event.getHotbarButton() + 1;
         }

         if (newAmount < minAmount) {
            newAmount = minAmount;
         }

         if (newAmount > maxAmount) {
            newAmount = maxAmount;
         }

         return newAmount;
      }
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
      if (closeEvent != null) {
         this.saveEditor();
      }

   }

   protected void saveEditorPage() {
      Inventory inventory = this.getInventory();
      int page = this.getCurrentPage();

      assert page >= 1;

      List<TradingRecipeDraft> recipes = this.getRecipes();
      int recipesPerPage = 9;
      int startIndex = (page - 1) * recipesPerPage;
      int endIndex = startIndex + 9 - 1;

      int column;
      for(column = recipes.size(); column <= endIndex; ++column) {
         recipes.add(TradingRecipeDraft.EMPTY);
      }

      for(column = 0; column < 9; ++column) {
         TradingRecipeDraft recipeDraft = this.getTradingRecipe(inventory, column);
         int recipeIndex = startIndex + column;
         recipes.set(recipeIndex, recipeDraft);
      }

   }

   protected void saveEditor() {
      this.saveEditorPage();
      this.saveRecipes();
   }

   protected abstract void saveRecipes();
}
