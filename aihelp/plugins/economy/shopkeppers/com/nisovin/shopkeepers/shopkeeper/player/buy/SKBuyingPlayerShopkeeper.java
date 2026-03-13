package com.nisovin.shopkeepers.shopkeeper.player.buy;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.buy.BuyingPlayerShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.SKDefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.offers.SKPriceOffer;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKBuyingPlayerShopkeeper extends AbstractPlayerShopkeeper implements BuyingPlayerShopkeeper {
   private final List<PriceOffer> offers = new ArrayList();
   private final List<? extends PriceOffer> offersView;
   private static final String DATA_KEY_OFFERS = "offers";
   public static final Property<List<? extends PriceOffer>> OFFERS;

   protected SKBuyingPlayerShopkeeper() {
      this.offersView = Collections.unmodifiableList(this.offers);
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.EDITOR(), () -> {
         return new BuyingPlayerShopEditorViewProvider(this);
      });
      this.registerViewProviderIfMissing(DefaultUITypes.TRADING(), () -> {
         return new BuyingPlayerShopTradingViewProvider(this);
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
      int updatedItems = super.updateItems(logPrefix, shopkeeperData);
      updatedItems += updateOfferItems(logPrefix, shopkeeperData);
      return updatedItems;
   }

   private static int updateOfferItems(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      try {
         ArrayList<PriceOffer> updatedOffers = new ArrayList((Collection)shopkeeperData.get(OFFERS));
         int updatedItems = SKPriceOffer.updateItems(updatedOffers, logPrefix);
         if (updatedItems > 0) {
            shopkeeperData.set(OFFERS, updatedOffers);
            return updatedItems;
         }
      } catch (InvalidDataException var4) {
         Log.warning((String)(logPrefix + "Failed to load '" + OFFERS.getName() + "'!"), (Throwable)var4);
      }

      return 0;
   }

   public BuyingPlayerShopType getType() {
      return SKDefaultShopTypes.PLAYER_BUYING();
   }

   public boolean hasTradingRecipes(@Nullable Player player) {
      return !this.getOffers().isEmpty();
   }

   public List<? extends TradingRecipe> getTradingRecipes(@Nullable Player player) {
      int currencyInContainer = this.getCurrencyInContainer();
      List<? extends PriceOffer> offers = this.getOffers();
      List<TradingRecipe> recipes = new ArrayList(offers.size());
      offers.forEach((offer) -> {
         UnmodifiableItemStack tradedItem = offer.getItem();
         boolean outOfStock = currencyInContainer < offer.getPrice();
         TradingRecipe recipe = this.createBuyingRecipe(tradedItem, offer.getPrice(), outOfStock);
         if (recipe != null) {
            recipes.add(recipe);
         }

      });
      return Collections.unmodifiableList(recipes);
   }

   private void loadOffers(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setOffers((List)shopkeeperData.get(OFFERS));
   }

   private void saveOffers(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(OFFERS, this.getOffers());
   }

   public List<? extends PriceOffer> getOffers() {
      return this.offersView;
   }

   @Nullable
   public PriceOffer getOffer(@ReadOnly ItemStack tradedItem) {
      Validate.notNull(tradedItem, (String)"tradedItem is null");
      Iterator var2 = this.getOffers().iterator();

      PriceOffer offer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         offer = (PriceOffer)var2.next();
      } while(!offer.getItem().isSimilar(tradedItem));

      return offer;
   }

   @Nullable
   public PriceOffer getOffer(UnmodifiableItemStack tradedItem) {
      Validate.notNull(tradedItem, (String)"tradedItem is null");
      return this.getOffer(ItemUtils.asItemStack(tradedItem));
   }

   public void removeOffer(@ReadOnly ItemStack tradedItem) {
      Validate.notNull(tradedItem, (String)"tradedItem is null");
      Iterator iterator = this.offers.iterator();

      while(iterator.hasNext()) {
         PriceOffer offer = (PriceOffer)iterator.next();
         if (offer.getItem().isSimilar(tradedItem)) {
            iterator.remove();
            this.markDirty();
            break;
         }
      }

   }

   public void removeOffer(UnmodifiableItemStack tradedItem) {
      Validate.notNull(tradedItem, (String)"tradedItem is null");
      this.removeOffer(ItemUtils.asItemStack(tradedItem));
   }

   public void clearOffers() {
      this._clearOffers();
      this.markDirty();
   }

   private void _clearOffers() {
      this.offers.clear();
   }

   public void setOffers(@ReadOnly List<? extends PriceOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._setOffers(offers);
      this.markDirty();
   }

   private void _setOffers(@ReadOnly List<? extends PriceOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      this._clearOffers();
      this._addOffers(offers);
   }

   public void addOffer(PriceOffer offer) {
      Validate.notNull(offer, (String)"offer is null");
      this._addOffer(offer);
      this.markDirty();
   }

   private void _addOffer(PriceOffer offer) {
      assert offer != null;

      Validate.isTrue(offer instanceof SKPriceOffer, "offer is not of type SKPriceOffer");
      SKPriceOffer skOffer = (SKPriceOffer)offer;
      this.removeOffer(skOffer.getItem());
      this.offers.add(skOffer);
   }

   public void addOffers(@ReadOnly List<? extends PriceOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._addOffers(offers);
      this.markDirty();
   }

   private void _addOffers(@ReadOnly List<? extends PriceOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      offers.forEach(this::_addOffer);
   }

   static {
      OFFERS = (new BasicProperty()).dataKeyAccessor("offers", SKPriceOffer.LIST_SERIALIZER).useDefaultIfMissing().defaultValue(Collections.emptyList()).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("buying-offers", MigrationPhase.ofShopkeeperClass(SKBuyingPlayerShopkeeper.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            return SKPriceOffer.migrateOffers(shopkeeperData.getDataValue("offers"), logPrefix);
         }
      });
   }
}
