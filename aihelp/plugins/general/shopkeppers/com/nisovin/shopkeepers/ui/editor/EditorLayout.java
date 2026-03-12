package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class EditorLayout {
   private static final SoundEffect PAGE_TURN_SOUND;
   public static final int COLUMNS_PER_ROW = 9;
   public static final int TRADES_COLUMNS = 9;
   public static final int TRADES_ROW_1_START = 0;
   public static final int TRADES_ROW_1_END = 8;
   public static final int TRADES_ROW_2_START = 9;
   public static final int TRADES_ROW_2_END = 17;
   public static final int TRADES_ROW_3_START = 18;
   public static final int TRADES_ROW_3_END = 26;
   public static final int TRADES_PAGE_BAR_START = 27;
   public static final int TRADES_PAGE_BAR_END = 35;
   public static final int TRADES_PAGE_ICON = 31;
   public static final int TRADES_SETUP_ICON = 30;
   public static final int SHOP_INFORMATION_ICON = 32;
   public static final int BUTTONS_START = 36;
   public static final int BUTTON_MAX_ROWS = 2;
   public static final int RESULT_ITEM_OFFSET = 0;
   public static final int ITEM_1_OFFSET = 18;
   public static final int ITEM_2_OFFSET = 9;
   private final Button[] tradesPageBarButtons = new Button[9];
   private final List<Button> buttons = new ArrayList();
   private int buttonRows = 1;
   private final Button[] bakedButtons = new Button[18];
   private boolean dirtyButtons = false;

   public int getInventorySize() {
      return 9 * (4 + this.getButtonRows());
   }

   public boolean isResultRow(int rawSlot) {
      return rawSlot >= 0 && rawSlot <= 8;
   }

   public boolean isItem1Row(int rawSlot) {
      return rawSlot >= 18 && rawSlot <= 26;
   }

   public boolean isItem2Row(int rawSlot) {
      return rawSlot >= 9 && rawSlot <= 17;
   }

   public boolean isTradesArea(int rawSlot) {
      return this.isResultRow(rawSlot) || this.isItem1Row(rawSlot) || this.isItem2Row(rawSlot);
   }

   public int getTradeColumn(int rawSlot) {
      return rawSlot % 9;
   }

   public boolean isTradeColumn(int column) {
      return column >= 0 && column < 9;
   }

   public int getResultItemSlot(int column) {
      return column + 0;
   }

   public int getItem1Slot(int column) {
      return column + 18;
   }

   public int getItem2Slot(int column) {
      return column + 9;
   }

   public boolean isTradesPageBar(int rawSlot) {
      return rawSlot >= 27 && rawSlot <= 35;
   }

   public boolean isButtonArea(int rawSlot) {
      return rawSlot >= 36 && rawSlot <= this.getButtonsEnd();
   }

   public int getButtonsEnd() {
      return 36 + this.getButtonRows() * 9 - 1;
   }

   public int getMaxTradesPages() {
      return Settings.maxTradesPages;
   }

   Button[] getTradesPageBarButtons() {
      this.setupTradesPageBarButtons();
      return this.tradesPageBarButtons;
   }

   @Nullable
   private Button getTradesPageBarButton(int rawSlot) {
      return !this.isTradesPageBar(rawSlot) ? null : this._getTradesPageBarButton(rawSlot);
   }

   @Nullable
   Button _getTradesPageBarButton(int rawSlot) {
      assert this.isTradesPageBar(rawSlot);

      return this.tradesPageBarButtons[rawSlot - 27];
   }

   public void setupTradesPageBarButtons() {
      Button prevPageButton = this.createPrevPageButton();
      prevPageButton.setSlot(27);
      this.tradesPageBarButtons[0] = prevPageButton;
      Button tradeSetupButton = this.createTradeSetupButton();
      tradeSetupButton.setSlot(30);
      this.tradesPageBarButtons[3] = tradeSetupButton;
      Button currentPageButton = this.createCurrentPageButton();
      currentPageButton.setSlot(31);
      this.tradesPageBarButtons[4] = currentPageButton;
      Button shopInformationButton = this.createShopInformationButton();
      shopInformationButton.setSlot(32);
      this.tradesPageBarButtons[5] = shopInformationButton;
      Button nextPageButton = this.createNextPageButton();
      nextPageButton.setSlot(35);
      this.tradesPageBarButtons[8] = nextPageButton;
   }

   protected Button createPrevPageButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            int page = editorView.getCurrentPage();
            return page <= 1 ? null : EditorLayout.this.createPrevPageIcon(page);
         }

         protected void playButtonClickSound(Player player, boolean actionSuccess) {
            if (actionSuccess) {
               EditorLayout.PAGE_TURN_SOUND.play(player);
            }

         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            if (clickEvent.getClick() == ClickType.DOUBLE_CLICK) {
               return false;
            } else {
               int currentPage = editorView.getCurrentPage();
               return editorView.switchPage(currentPage - 1, true);
            }
         }
      };
   }

   protected Button createNextPageButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            int page = editorView.getCurrentPage();
            return page >= EditorLayout.this.getMaxTradesPages() ? null : EditorLayout.this.createNextPageIcon(page);
         }

         protected void playButtonClickSound(Player player, boolean actionSuccess) {
            if (actionSuccess) {
               EditorLayout.PAGE_TURN_SOUND.play(player);
            }

         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            if (clickEvent.getClick() == ClickType.DOUBLE_CLICK) {
               return false;
            } else {
               int currentPage = editorView.getCurrentPage();
               return editorView.switchPage(currentPage + 1, true);
            }
         }
      };
   }

   protected Button createCurrentPageButton() {
      return new Button() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            int page = editorView.getCurrentPage();
            return EditorLayout.this.createCurrentPageIcon(page);
         }

         protected void onClick(EditorView editorView, InventoryClickEvent clickEvent) {
         }
      };
   }

   protected Button createShopInformationButton() {
      return new Button() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return EditorLayout.this.createShopInformationIcon();
         }

         protected void onClick(EditorView editorView, InventoryClickEvent clickEvent) {
         }
      };
   }

   protected Button createTradeSetupButton() {
      return new Button() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return EditorLayout.this.createTradeSetupIcon();
         }

         protected void onClick(EditorView editorView, InventoryClickEvent clickEvent) {
         }
      };
   }

   protected ItemStack createPrevPageIcon(int page) {
      int prevPage = 1;
      String prevPageText = "-";
      if (page > 1) {
         prevPage = page - 1;
         prevPageText = String.valueOf(prevPage);
      }

      String itemName = StringUtils.replaceArguments(Messages.buttonPreviousPage, "prev_page", prevPageText, "page", page, "max_page", this.getMaxTradesPages());
      ItemStack item = Settings.previousPageItem.createItemStack();
      ItemUtils.setItemMeta(item, itemName, Messages.buttonPreviousPageLore, 64);
      item.setAmount(MathUtils.clamp(prevPage, 1, 64));
      return item;
   }

   protected ItemStack createNextPageIcon(int page) {
      int nextPage = 1;
      String nextPageText = "-";
      if (page < this.getMaxTradesPages()) {
         nextPage = page + 1;
         nextPageText = String.valueOf(nextPage);
      }

      String itemName = StringUtils.replaceArguments(Messages.buttonNextPage, "next_page", nextPageText, "page", page, "max_page", this.getMaxTradesPages());
      ItemStack item = Settings.nextPageItem.createItemStack();
      ItemUtils.setItemMeta(item, itemName, Messages.buttonNextPageLore, 64);
      item.setAmount(MathUtils.clamp(nextPage, 1, 64));
      return item;
   }

   protected ItemStack createCurrentPageIcon(int page) {
      String itemName = StringUtils.replaceArguments(Messages.buttonCurrentPage, "page", page, "max_page", this.getMaxTradesPages());
      ItemStack item = Settings.currentPageItem.createItemStack();
      ItemUtils.setItemMeta(item, itemName, Messages.buttonCurrentPageLore, 64);
      item.setAmount(MathUtils.clamp(page, 1, 64));
      return item;
   }

   protected abstract ItemStack createShopInformationIcon();

   protected abstract ItemStack createTradeSetupIcon();

   public void addButton(Button button) {
      Validate.notNull(button, (String)"button is null");
      Validate.isTrue(button.isApplicable(this), "button is not applicable to this layout");
      button.setEditorLayout(this);
      this.buttons.add(button);
      this.dirtyButtons = true;
   }

   public final void addButtons(Iterable<? extends Button> buttons) {
      Validate.notNull(buttons, (String)"buttons is null");
      Iterator var2 = buttons.iterator();

      while(var2.hasNext()) {
         Button button = (Button)var2.next();
         this.addButton(button);
      }

   }

   public void addButtonOrIgnore(@Nullable Button button) {
      if (button != null) {
         this.addButton(button);
      }
   }

   public final void addButtonsOrIgnore(Iterable<? extends Button> buttons) {
      Validate.notNull(buttons, (String)"buttons is null");
      Iterator var2 = buttons.iterator();

      while(var2.hasNext()) {
         Button button = (Button)var2.next();
         this.addButtonOrIgnore(button);
      }

   }

   private void bakeButtons() {
      if (this.dirtyButtons) {
         this.buttons.forEach((buttonx) -> {
            buttonx.setSlot(-1);
         });
         Arrays.fill(this.bakedButtons, (Object)null);
         int frontIndex = 0;
         this.buttonRows = Math.min(2, (this.buttons.size() - 1) / 9 + 1);
         int endIndex = this.buttonRows * 9 - 1;

         for(int i = 0; i < this.buttons.size(); ++i) {
            Button button = (Button)this.buttons.get(i);
            int buttonIndex;
            if (button.isPlaceAtEnd()) {
               buttonIndex = endIndex--;
            } else {
               buttonIndex = frontIndex++;
            }

            if (this.bakedButtons[buttonIndex] != null) {
               break;
            }

            this.bakedButtons[buttonIndex] = button;
            button.setSlot(36 + buttonIndex);
         }

      }
   }

   int getButtonRows() {
      this.bakeButtons();
      return this.buttonRows;
   }

   Button[] getBakedButtons() {
      this.bakeButtons();
      return this.bakedButtons;
   }

   @Nullable
   private Button getButton(int rawSlot) {
      return !this.isButtonArea(rawSlot) ? null : this._getButton(rawSlot);
   }

   @Nullable
   Button _getButton(int rawSlot) {
      assert this.isButtonArea(rawSlot);

      this.bakeButtons();
      return this.bakedButtons[rawSlot - 36];
   }

   static {
      PAGE_TURN_SOUND = new SoundEffect(Sound.ITEM_BOOK_PAGE_TURN);
   }
}
