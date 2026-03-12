package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPaginatedGui {
   PaginatedGui setPageSize(int var1);

   void addItem(@NotNull GuiItem var1);

   void removePageItem(@NotNull GuiItem var1);

   void removePageItem(@NotNull ItemStack var1);

   void updatePageItem(int var1, @NotNull ItemStack var2);

   void updatePageItem(int var1, int var2, @NotNull ItemStack var3);

   void updatePageItem(int var1, int var2, @NotNull GuiItem var3);

   void updatePageItem(int var1, @NotNull GuiItem var2);

   void open(@NotNull Player var1, @NotNull Consumer<PaginatedGui> var2);

   void open(@NotNull Player var1, int var2, @Nullable Consumer<PaginatedGui> var3);

   int getNextPageNumber();

   int getPreviousPageNumber();

   boolean next();

   boolean previous();

   GuiItem getPageItem(int var1);

   void setPageNumber(int var1);

   int getCurrentPageNumber();

   List<GuiItem> getItemsFromPage(int var1);

   Map<Integer, GuiItem> getCurrentPageItems();

   void clearPageContents();

   void clearPageItems(boolean var1);

   void clearPageItems();

   int getMaxPages();

   void populatePage();

   void updatePage();

   void calculatePageSize();
}
