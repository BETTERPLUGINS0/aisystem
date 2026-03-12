package com.nisovin.shopkeepers.shopkeeper.admin.regular;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.admin.regular.RegularAdminShopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.shopkeeper.SKDefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.admin.AbstractAdminShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.offers.SKTradeOffer;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKRegularAdminShopkeeper extends AbstractAdminShopkeeper implements RegularAdminShopkeeper {
   private final List<SKTradeOffer> offers = new ArrayList();
   private final List<? extends SKTradeOffer> offersView;
   private static final String DATA_KEY_OFFERS = "recipes";
   public static final Property<List<? extends TradeOffer>> OFFERS;

   protected SKRegularAdminShopkeeper() {
      this.offersView = Collections.unmodifiableList(this.offers);
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.EDITOR(), () -> {
         return new RegularAdminShopEditorViewProvider(this);
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

   public RegularAdminShopType getType() {
      return SKDefaultShopTypes.ADMIN_REGULAR();
   }

   public boolean hasTradingRecipes(@Nullable Player player) {
      return !this.getOffers().isEmpty();
   }

   public List<? extends TradingRecipe> getTradingRecipes(@Nullable Player player) {
      return this.offersView;
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
      OFFERS = (new BasicProperty()).dataKeyAccessor("recipes", SKTradeOffer.LIST_SERIALIZER).useDefaultIfMissing().defaultValue(Collections.emptyList()).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("admin-offers", MigrationPhase.ofShopkeeperClass(SKRegularAdminShopkeeper.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            return SKTradeOffer.migrateOffers(shopkeeperData.getDataValue("recipes"), logPrefix);
         }
      });
   }
}
