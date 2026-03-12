package libs.com.ryderbelserion.vital.paper.api.builders.gui.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types.IPaginatedGui;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaginatedGui extends BaseGui implements IPaginatedGui {
   private final List<GuiItem> pageItems = new ArrayList();
   private final Map<Integer, GuiItem> currentPage;
   private int pageNumber = 1;
   private int pageSize;

   public PaginatedGui(@NotNull String title, int pageSize, int rows, @NotNull Set<InteractionComponent> components) {
      super(title, rows, components);
      if (pageSize == 0) {
         this.calculatePageSize();
      } else {
         this.pageSize = pageSize;
      }

      int size = rows * 9;
      this.currentPage = new LinkedHashMap(size);
   }

   public final PaginatedGui setPageSize(int pageSize) {
      this.pageSize = pageSize;
      return this;
   }

   public void addItem(@NotNull GuiItem guiItem) {
      this.pageItems.add(guiItem);
   }

   public void addItem(@NotNull GuiItem... items) {
      this.pageItems.addAll(Arrays.asList(items));
   }

   public void removePageItem(@NotNull GuiItem guiItem) {
      this.pageItems.remove(guiItem);
      this.updatePage();
   }

   public void removePageItem(@NotNull ItemStack itemStack) {
      String key = GuiKeys.getUUID(itemStack);
      Optional<GuiItem> guiItem = this.pageItems.stream().filter((it) -> {
         String pair = it.getUuid().toString();
         return key.equalsIgnoreCase(pair);
      }).findFirst();
      guiItem.ifPresent(this::removePageItem);
   }

   public void updatePageItem(int slot, @NotNull ItemStack itemStack) {
      if (this.currentPage.containsKey(slot)) {
         GuiItem guiItem = (GuiItem)this.currentPage.get(slot);
         guiItem.setItemStack(itemStack);
         this.getInventory().setItem(slot, guiItem.getItemStack());
      }
   }

   public void updatePageItem(int row, int col, @NotNull ItemStack itemStack) {
      this.updatePageItem(this.getSlotFromRowColumn(row, col), itemStack);
   }

   public void updatePageItem(int row, int col, @NotNull GuiItem guiItem) {
      this.updatePageItem(this.getSlotFromRowColumn(row, col), guiItem);
   }

   public void updatePageItem(int slot, @NotNull GuiItem guiItem) {
      if (this.currentPage.containsKey(slot)) {
         int index = this.pageItems.indexOf(this.currentPage.get(slot));
         this.currentPage.put(slot, guiItem);
         this.pageItems.set(index, guiItem);
         this.getInventory().setItem(slot, guiItem.getItemStack());
      }
   }

   public void open(@NotNull Player player, boolean purge) {
      this.open(player, 1, (Consumer)null);
   }

   public void open(@NotNull Player player) {
      this.open(player, 1, (Consumer)null);
   }

   public void open(@NotNull Player player, @NotNull Consumer<PaginatedGui> consumer) {
      this.open(player, 1, consumer);
   }

   public void open(@NotNull Player player, int openPage, @Nullable Consumer<PaginatedGui> consumer) {
      if (!player.isSleeping()) {
         if (openPage <= this.getMaxPages() || openPage > 0) {
            this.pageNumber = openPage;
         }

         this.getInventory().clear();
         this.currentPage.clear();
         this.populate();
         if (this.pageSize == 0 || this.pageSize == this.getSize()) {
            this.calculatePageSize();
         }

         this.populatePage();
         if (consumer != null) {
            consumer.accept(this);
         }

         player.openInventory(this.getInventory());
      }
   }

   public final int getNextPageNumber() {
      return this.pageNumber + 1 > this.getMaxPages() ? this.pageNumber : this.pageNumber + 1;
   }

   public final int getPreviousPageNumber() {
      return this.pageNumber - 1 == 0 ? this.pageNumber : this.pageNumber - 1;
   }

   public final boolean next() {
      if (this.pageNumber + 1 > this.getMaxPages()) {
         return false;
      } else {
         ++this.pageNumber;
         this.updatePage();
         return true;
      }
   }

   public final boolean previous() {
      if (this.pageNumber - 1 == 0) {
         return false;
      } else {
         --this.pageNumber;
         this.updatePage();
         return true;
      }
   }

   public final GuiItem getPageItem(int slot) {
      return (GuiItem)this.currentPage.get(slot);
   }

   public void setPageNumber(int pageNumber) {
      this.pageNumber = pageNumber;
   }

   public final int getCurrentPageNumber() {
      return this.pageNumber;
   }

   public final List<GuiItem> getItemsFromPage(int givenPage) {
      int page = givenPage - 1;
      List<GuiItem> guiPage = new ArrayList();
      int max = page * this.pageSize + this.pageSize;
      if (max > this.pageItems.size()) {
         max = this.pageItems.size();
      }

      for(int i = page * this.pageSize; i < max; ++i) {
         guiPage.add((GuiItem)this.pageItems.get(i));
      }

      return guiPage;
   }

   public final Map<Integer, GuiItem> getCurrentPageItems() {
      return this.currentPage;
   }

   public void clearPageContents() {
      Iterator var1 = this.currentPage.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<Integer, GuiItem> entry = (Entry)var1.next();
         this.getInventory().setItem((Integer)entry.getKey(), (ItemStack)null);
      }

   }

   public void clearPageItems(boolean update) {
      this.pageItems.clear();
      if (update) {
         this.updatePage();
      }

   }

   public void clearPageItems() {
      this.clearPageItems(false);
   }

   public final int getMaxPages() {
      return (int)Math.ceil((double)this.pageItems.size() / (double)this.pageSize);
   }

   public void populatePage() {
      int slot = 0;
      int inventorySize = this.getInventory().getSize();
      Iterator iterator = this.getItemsFromPage(this.pageNumber).iterator();

      while(iterator.hasNext() && slot < inventorySize) {
         if (this.getGuiItem(slot) == null && this.getInventory().getItem(slot) == null) {
            GuiItem guiItem = (GuiItem)iterator.next();
            this.currentPage.put(slot, guiItem);
            this.getInventory().setItem(slot, guiItem.getItemStack());
            ++slot;
         } else {
            ++slot;
         }
      }

   }

   public void updatePage() {
      this.clearPageContents();
      this.populatePage();
   }

   public final void calculatePageSize() {
      int counter = 0;

      for(int slot = 0; slot < this.getSize(); ++slot) {
         if (this.getInventory().getItem(slot) == null) {
            ++counter;
         }
      }

      this.pageSize = counter;
   }

   public void updateInventory(Player player) {
      this.getInventory().clear();
      this.populate();
   }
}
