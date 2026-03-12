package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.book.BookPlayerShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.SKDefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.offers.SKBookOffer;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.inventory.BookItems;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKBookPlayerShopkeeper extends AbstractPlayerShopkeeper implements BookPlayerShopkeeper {
   private final List<BookOffer> offers = new ArrayList();
   private final List<? extends BookOffer> offersView;
   private static final String DATA_KEY_OFFERS = "offers";
   public static final Property<List<? extends BookOffer>> OFFERS;

   protected SKBookPlayerShopkeeper() {
      this.offersView = Collections.unmodifiableList(this.offers);
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.EDITOR(), () -> {
         return new BookPlayerShopEditorViewProvider(this);
      });
      this.registerViewProviderIfMissing(DefaultUITypes.TRADING(), () -> {
         return new BookPlayerShopTradingViewProvider(this);
      });
      super.setup();
   }

   public void loadDynamicState(ShopkeeperData shopkeeperData) throws InvalidDataException {
      super.loadDynamicState(shopkeeperData);
      this.loadOffers(shopkeeperData);
   }

   public void saveDynamicState(ShopkeeperData shopkeeperData, boolean saveAll) {
      super.saveDynamicState(shopkeeperData, saveAll);
      this.saveOffers(shopkeeperData);
   }

   protected int updateItems(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      return super.updateItems(logPrefix, shopkeeperData);
   }

   public BookPlayerShopType getType() {
      return SKDefaultShopTypes.PLAYER_BOOK();
   }

   public boolean hasTradingRecipes(@Nullable Player player) {
      return !this.getOffers().isEmpty();
   }

   public List<? extends TradingRecipe> getTradingRecipes(@Nullable Player player) {
      Map<? extends String, ? extends ItemStack> containerBooksByTitle = this.getCopyableBooksFromContainer();
      boolean hasBlankBooks = this.hasContainerBlankBooks();
      List<? extends BookOffer> offers = this.getOffers();
      List<TradingRecipe> recipes = new ArrayList(offers.size());
      offers.forEach((bookOffer) -> {
         String bookTitle = bookOffer.getBookTitle();
         ItemStack bookItem = (ItemStack)containerBooksByTitle.get(bookTitle);
         boolean outOfStock = !hasBlankBooks;
         if (bookItem == null) {
            outOfStock = true;
            bookItem = this.createDummyBook(bookTitle);
         } else {
            assert BookItems.isCopyableBook(bookItem);

            bookItem = BookItems.copyBook(bookItem);
         }

         assert bookItem != null;

         UnmodifiableItemStack unmodifiableBookItem = UnmodifiableItemStack.ofNonNull(bookItem);
         TradingRecipe recipe = this.createSellingRecipe(unmodifiableBookItem, bookOffer.getPrice(), outOfStock);
         if (recipe != null) {
            recipes.add(recipe);
         }

      });
      return Collections.unmodifiableList(recipes);
   }

   protected Map<? extends String, ? extends ItemStack> getCopyableBooksFromContainer() {
      Map<String, ItemStack> booksByTitle = new LinkedHashMap();
      ItemStack[] contents = this.getContainerContents();
      ItemStack[] var3 = contents;
      int var4 = contents.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack itemStack = var3[var5];
         if (itemStack != null) {
            BookMeta bookMeta = BookItems.getBookMeta(itemStack);
            if (bookMeta != null && BookItems.isCopyable(bookMeta)) {
               String title = BookItems.getTitle(bookMeta);
               if (title != null) {
                  booksByTitle.putIfAbsent(title, itemStack);
               }
            }
         }
      }

      return booksByTitle;
   }

   protected boolean hasContainerBlankBooks() {
      Inventory containerInventory = this.getContainerInventory();
      return containerInventory == null ? false : containerInventory.contains(Material.WRITABLE_BOOK);
   }

   protected ItemStack createDummyBook(String title) {
      ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
      BookMeta bookMeta = (BookMeta)Unsafe.castNonNull(bookItem.getItemMeta());
      bookMeta.setTitle(title);
      bookMeta.setAuthor(Messages.unknownBookAuthor);
      bookMeta.setGeneration(Generation.TATTERED);
      bookItem.setItemMeta(bookMeta);
      return bookItem;
   }

   protected static boolean isDummyBook(@ReadOnly BookMeta bookMeta) {
      assert bookMeta != null;

      Generation generation = BookItems.getGeneration(bookMeta);
      return generation == Generation.TATTERED;
   }

   private void loadOffers(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setOffers((List)shopkeeperData.get(OFFERS));
   }

   private void saveOffers(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(OFFERS, this.getOffers());
   }

   public List<? extends BookOffer> getOffers() {
      return this.offersView;
   }

   @Nullable
   public BookOffer getOffer(@ReadOnly ItemStack bookItem) {
      Validate.notNull(bookItem, (String)"bookItem is null");
      String bookTitle = BookItems.getBookTitle(bookItem);
      return bookTitle == null ? null : this.getOffer(bookTitle);
   }

   @Nullable
   public BookOffer getOffer(UnmodifiableItemStack bookItem) {
      Validate.notNull(bookItem, (String)"bookItem is null");
      return this.getOffer(ItemUtils.asItemStack(bookItem));
   }

   @Nullable
   public BookOffer getOffer(String bookTitle) {
      Validate.notNull(bookTitle, (String)"bookTitle is null");
      Iterator var2 = this.getOffers().iterator();

      BookOffer offer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         offer = (BookOffer)var2.next();
      } while(!offer.getBookTitle().equals(bookTitle));

      return offer;
   }

   public void removeOffer(String bookTitle) {
      Validate.notNull(bookTitle, (String)"bookTitle is null");
      Iterator iterator = this.offers.iterator();

      while(iterator.hasNext()) {
         if (((BookOffer)iterator.next()).getBookTitle().equals(bookTitle)) {
            iterator.remove();
            this.markDirty();
            break;
         }
      }

   }

   public void clearOffers() {
      this._clearOffers();
      this.markDirty();
   }

   private void _clearOffers() {
      this.offers.clear();
   }

   public void setOffers(@ReadOnly List<? extends BookOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._setOffers(offers);
      this.markDirty();
   }

   private void _setOffers(@ReadOnly List<? extends BookOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      this._clearOffers();
      this._addOffers(offers);
   }

   public void addOffer(BookOffer offer) {
      Validate.notNull(offer, (String)"offer is null");
      this._addOffer(offer);
      this.markDirty();
   }

   private void _addOffer(BookOffer offer) {
      assert offer != null;

      Validate.isTrue(offer instanceof SKBookOffer, "offer is not of type SKBookOffer");
      SKBookOffer skOffer = (SKBookOffer)offer;
      String bookTitle = offer.getBookTitle();
      this.removeOffer(bookTitle);
      this.offers.add(skOffer);
   }

   public void addOffers(@ReadOnly List<? extends BookOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._addOffers(offers);
      this.markDirty();
   }

   private void _addOffers(@ReadOnly List<? extends BookOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      offers.forEach(this::_addOffer);
   }

   static {
      OFFERS = (new BasicProperty()).dataKeyAccessor("offers", SKBookOffer.LIST_SERIALIZER).useDefaultIfMissing().defaultValue(Collections.emptyList()).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("book-offers", MigrationPhase.ofShopkeeperClass(SKBookPlayerShopkeeper.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            return SKBookOffer.migrateOffers(shopkeeperData.getDataValue("offers"), logPrefix);
         }
      });
   }
}
