package com.nisovin.shopkeepers.api;

import com.nisovin.shopkeepers.api.internal.InternalShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopTypesRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectTypesRegistry;
import com.nisovin.shopkeepers.api.storage.ShopkeeperStorage;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.ui.UIRegistry;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ShopkeepersAPI {
   private ShopkeepersAPI() {
   }

   public static boolean isEnabled() {
      return InternalShopkeepersAPI.isEnabled();
   }

   public static ShopkeepersPlugin getPlugin() {
      return InternalShopkeepersAPI.getPlugin();
   }

   public static boolean hasCreatePermission(Player player) {
      return getPlugin().hasCreatePermission(player);
   }

   public static ShopTypesRegistry<?> getShopTypeRegistry() {
      return getPlugin().getShopTypeRegistry();
   }

   public static DefaultShopTypes getDefaultShopTypes() {
      return getPlugin().getDefaultShopTypes();
   }

   public static ShopObjectTypesRegistry<?> getShopObjectTypeRegistry() {
      return getPlugin().getShopObjectTypeRegistry();
   }

   public static DefaultShopObjectTypes getDefaultShopObjectTypes() {
      return getPlugin().getDefaultShopObjectTypes();
   }

   public static UIRegistry<?> getUIRegistry() {
      return getPlugin().getUIRegistry();
   }

   public static DefaultUITypes getDefaultUITypes() {
      return getPlugin().getDefaultUITypes();
   }

   public static ShopkeeperRegistry getShopkeeperRegistry() {
      return getPlugin().getShopkeeperRegistry();
   }

   public static ShopkeeperStorage getShopkeeperStorage() {
      return getPlugin().getShopkeeperStorage();
   }

   public static int updateItems() {
      return getPlugin().updateItems();
   }

   @Nullable
   public static Shopkeeper handleShopkeeperCreation(ShopCreationData shopCreationData) {
      return getPlugin().handleShopkeeperCreation(shopCreationData);
   }

   /** @deprecated */
   @Deprecated
   public static PriceOffer createPriceOffer(ItemStack item, int price) {
      return getPlugin().createPriceOffer(item, price);
   }

   /** @deprecated */
   @Deprecated
   public static PriceOffer createPriceOffer(UnmodifiableItemStack item, int price) {
      return getPlugin().createPriceOffer(item, price);
   }

   /** @deprecated */
   @Deprecated
   public static TradeOffer createTradeOffer(ItemStack resultItem, ItemStack item1, ItemStack item2) {
      return getPlugin().createTradeOffer(resultItem, item1, item2);
   }

   /** @deprecated */
   @Deprecated
   public static TradeOffer createTradeOffer(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, UnmodifiableItemStack item2) {
      return getPlugin().createTradeOffer(resultItem, item1, item2);
   }

   /** @deprecated */
   @Deprecated
   public static BookOffer createBookOffer(String bookTitle, int price) {
      return getPlugin().createBookOffer(bookTitle, price);
   }
}
