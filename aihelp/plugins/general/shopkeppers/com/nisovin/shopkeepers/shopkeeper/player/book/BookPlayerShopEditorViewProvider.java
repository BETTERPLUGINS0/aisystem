package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopEditorViewProvider;
import com.nisovin.shopkeepers.ui.editor.DefaultTradingRecipesAdapter;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.inventory.BookItems;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BookPlayerShopEditorViewProvider extends PlayerShopEditorViewProvider {
   protected BookPlayerShopEditorViewProvider(SKBookPlayerShopkeeper shopkeeper) {
      super(shopkeeper, new BookPlayerShopEditorViewProvider.TradingRecipesAdapter(shopkeeper));
   }

   public SKBookPlayerShopkeeper getShopkeeper() {
      return (SKBookPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new BookPlayerShopEditorView(this, player, uiState);
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<BookOffer> {
      private final SKBookPlayerShopkeeper shopkeeper;

      private TradingRecipesAdapter(SKBookPlayerShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeeper = shopkeeper;
      }

      public List<TradingRecipeDraft> getTradingRecipes() {
         Set<String> bookTitles = new HashSet();
         Map<? extends String, ? extends ItemStack> containerBooksByTitle = this.shopkeeper.getCopyableBooksFromContainer();
         List<? extends BookOffer> offers = this.shopkeeper.getOffers();
         List<TradingRecipeDraft> recipes = new ArrayList(Math.max(offers.size(), containerBooksByTitle.size()));
         offers.forEach((bookOffer) -> {
            String bookTitle = bookOffer.getBookTitle();
            bookTitles.add(bookTitle);
            ItemStack bookItem = (ItemStack)containerBooksByTitle.get(bookTitle);
            if (bookItem == null) {
               bookItem = this.shopkeeper.createDummyBook(bookTitle);
            } else {
               bookItem = ItemUtils.copySingleItem(bookItem);
            }

            TradingRecipeDraft recipe = BookPlayerShopEditorViewProvider.createTradingRecipeDraft(bookItem, bookOffer.getPrice());
            recipes.add(recipe);
         });
         containerBooksByTitle.forEach((bookTitle, bookItem) -> {
            assert bookTitle != null;

            if (bookTitles.add(bookTitle)) {
               ItemStack bookItemCopy = ItemUtils.copySingleItem(bookItem);
               TradingRecipeDraft recipe = BookPlayerShopEditorViewProvider.createTradingRecipeDraft(bookItemCopy, 0);
               recipes.add(recipe);
            }
         });
         return recipes;
      }

      protected List<? extends BookOffer> getOffers() {
         return this.shopkeeper.getOffers();
      }

      protected void setOffers(List<? extends BookOffer> newOffers) {
         this.shopkeeper.setOffers(newOffers);
      }

      @Nullable
      protected BookOffer createOffer(TradingRecipeDraft recipe) {
         assert recipe != null && recipe.isValid();

         UnmodifiableItemStack bookItem = recipe.getResultItem();
         BookMeta bookMeta = BookItems.getBookMeta(bookItem);
         if (bookMeta == null) {
            return null;
         } else if (!SKBookPlayerShopkeeper.isDummyBook(bookMeta) && !BookItems.isCopyable(bookMeta)) {
            return null;
         } else {
            String bookTitle = BookItems.getTitle(bookMeta);
            if (bookTitle == null) {
               return null;
            } else {
               int price = BookPlayerShopEditorViewProvider.getPrice(this.shopkeeper, recipe);
               return price <= 0 ? null : BookOffer.create(bookTitle, price);
            }
         }
      }
   }
}
