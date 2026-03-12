package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.trade.TradingPlayerShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.SKDefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.offers.SKTradeOffer;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
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

public class SKTradingPlayerShopkeeper extends AbstractPlayerShopkeeper implements TradingPlayerShopkeeper {
   private final List<TradeOffer> offers = new ArrayList();
   private final List<? extends TradeOffer> offersView;
   private static final String DATA_KEY_OFFERS = "offers";
   public static final Property<List<? extends TradeOffer>> OFFERS;

   protected SKTradingPlayerShopkeeper() {
      this.offersView = Collections.unmodifiableList(this.offers);
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.EDITOR(), () -> {
         return new TradingPlayerShopEditorViewProvider(this);
      });
      this.registerViewProviderIfMissing(DefaultUITypes.TRADING(), () -> {
         return new TradingPlayerShopTradingViewProvider(this);
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
         ArrayList<TradeOffer> updatedOffers = new ArrayList((Collection)shopkeeperData.get(OFFERS));
         int updatedItems = SKTradeOffer.updateItems(updatedOffers, logPrefix);
         if (updatedItems > 0) {
            shopkeeperData.set(OFFERS, updatedOffers);
            return updatedItems;
         }
      } catch (InvalidDataException var4) {
         Log.warning((String)(logPrefix + "Failed to load '" + OFFERS.getName() + "'!"), (Throwable)var4);
      }

      return 0;
   }

   public TradingPlayerShopType getType() {
      return SKDefaultShopTypes.PLAYER_TRADING();
   }

   public boolean hasTradingRecipes(@Nullable Player player) {
      return !this.getOffers().isEmpty();
   }

   public List<? extends TradingRecipe> getTradingRecipes(Player player) {
      ItemStack[] containerContents = this.getContainerContents();
      List<? extends TradeOffer> offers = this.getOffers();
      List<TradingRecipe> recipes = new ArrayList(offers.size());
      offers.forEach((offer) -> {
         UnmodifiableItemStack resultItem = offer.getResultItem();
         boolean outOfStock = !InventoryUtils.containsAtLeast(containerContents, resultItem, resultItem.getAmount());
         TradingRecipe recipe = SKTradeOffer.toTradingRecipe(offer, outOfStock);
         recipes.add(recipe);
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

   public List<? extends TradeOffer> getOffers() {
      return this.offersView;
   }

   public boolean hasOffer(ItemStack resultItem) {
      Validate.notNull(resultItem, (String)"resultItem is null");
      Iterator var2 = this.getOffers().iterator();

      TradeOffer offer;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         offer = (TradeOffer)var2.next();
      } while(!offer.getResultItem().isSimilar(resultItem));

      return true;
   }

   @Nullable
   public TradeOffer getOffer(TradingRecipe tradingRecipe) {
      Iterator var2 = this.getOffers().iterator();

      TradeOffer offer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         offer = (TradeOffer)var2.next();
      } while(!offer.areItemsEqual(tradingRecipe));

      return offer;
   }

   public void clearOffers() {
      this._clearOffers();
      this.markDirty();
   }

   private void _clearOffers() {
      this.offers.clear();
   }

   public void setOffers(List<? extends TradeOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._setOffers(offers);
      this.markDirty();
   }

   private void _setOffers(List<? extends TradeOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      this._clearOffers();
      this._addOffers(offers);
   }

   public void addOffer(TradeOffer offer) {
      Validate.notNull(offer, (String)"offer is null");
      this._addOffer(offer);
      this.markDirty();
   }

   private void _addOffer(TradeOffer offer) {
      assert offer != null;

      Validate.isTrue(offer instanceof SKTradeOffer, "offer is not of type SKTradeOffer");
      SKTradeOffer skOffer = (SKTradeOffer)offer;
      this.offers.add(skOffer);
   }

   public void addOffers(List<? extends TradeOffer> offers) {
      Validate.notNull(offers, (String)"offers is null");
      Validate.noNullElements(offers, (String)"offers contains null");
      this._addOffers(offers);
      this.markDirty();
   }

   private void _addOffers(List<? extends TradeOffer> offers) {
      assert offers != null && !CollectionUtils.containsNull(offers);

      offers.forEach(this::_addOffer);
   }

   static {
      OFFERS = (new BasicProperty()).dataKeyAccessor("offers", SKTradeOffer.LIST_SERIALIZER).useDefaultIfMissing().defaultValue(Collections.emptyList()).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("trading-offers", MigrationPhase.ofShopkeeperClass(SKTradingPlayerShopkeeper.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            return SKTradeOffer.migrateOffers(shopkeeperData.getDataValue("offers"), logPrefix);
         }
      });
   }
}
