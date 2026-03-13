package com.nisovin.shopkeepers.api;

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
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShopkeepersPlugin extends Plugin {
   String HELP_PERMISSION = "shopkeeper.help";
   String TRADE_PERMISSION = "shopkeeper.trade";
   String CREATE_PERMISSION = "shopkeeper.create";
   String RELOAD_PERMISSION = "shopkeeper.reload";
   String DEBUG_PERMISSION = "shopkeeper.debug";
   String DELETE_UNSPAWNABLE_SHOPKEEPERS = "shopkeeper.delete-unspawnable-shopkeepers";
   String CLEANUP_CITIZEN_SHOPKEEPERS = "shopkeeper.cleanup-citizen-shopkeepers";
   String LIST_OWN_PERMISSION = "shopkeeper.list.own";
   String LIST_OTHERS_PERMISSION = "shopkeeper.list.others";
   String LIST_ADMIN_PERMISSION = "shopkeeper.list.admin";
   String HISTORY_OWN_PERMISSION = "shopkeeper.history.own";
   String HISTORY_ADMIN_PERMISSION = "shopkeeper.history.admin";
   String REMOVE_OWN_PERMISSION = "shopkeeper.remove.own";
   String REMOVE_OTHERS_PERMISSION = "shopkeeper.remove.others";
   String REMOVE_ADMIN_PERMISSION = "shopkeeper.remove.admin";
   String REMOVE_ALL_OWN_PERMISSION = "shopkeeper.remove-all.own";
   String REMOVE_ALL_OTHERS_PERMISSION = "shopkeeper.remove-all.others";
   String REMOVE_ALL_PLAYER_PERMISSION = "shopkeeper.remove-all.player";
   String REMOVE_ALL_ADMIN_PERMISSION = "shopkeeper.remove-all.admin";
   String NOTIFY_TRADES_PERMISSION = "shopkeeper.notify.trades";
   String GIVE_PERMISSION = "shopkeeper.give";
   String GIVE_CURRENCY_PERMISSION = "shopkeeper.givecurrency";
   String SET_CURRENCY_PERMISSION = "shopkeeper.setcurrency";
   String UPDATE_ITEMS_PERMISSION = "shopkeeper.updateitems";
   String REMOTE_PERMISSION = "shopkeeper.remote";
   String REMOTE_OTHER_PLAYERS_PERMISSION = "shopkeeper.remote.otherplayers";
   String REMOTE_EDIT_PERMISSION = "shopkeeper.remoteedit";
   String TRANSFER_PERMISSION = "shopkeeper.transfer";
   String TELEPORT_PERMISSION = "shopkeeper.teleport";
   String TELEPORT_OTHERS_PERMISSION = "shopkeeper.teleport.others";
   String SET_TRADE_PERM_PERMISSION = "shopkeeper.settradeperm";
   String SET_TRADED_COMMAND_PERMISSION = "shopkeeper.settradedcommand";
   String SET_FOR_HIRE_PERMISSION = "shopkeeper.setforhire";
   String HIRE_PERMISSION = "shopkeeper.hire";
   String SNAPSHOT_PERMISSION = "shopkeeper.snapshot";
   String EDIT_VILLAGERS_PERMISSION = "shopkeeper.edit-villagers";
   String EDIT_WANDERING_TRADERS_PERMISSION = "shopkeeper.edit-wandering-traders";
   String BYPASS_PERMISSION = "shopkeeper.bypass";
   String MAXSHOPS_UNLIMITED_PERMISSION = "shopkeeper.maxshops.unlimited";
   String TRADE_NOTIFICATIONS_ADMIN = "shopkeeper.trade-notifications.admin";
   String TRADE_NOTIFICATIONS_PLAYER = "shopkeeper.trade-notifications.player";
   String ADMIN_PERMISSION = "shopkeeper.admin";
   String PLAYER_SELL_PERMISSION = "shopkeeper.player.sell";
   String PLAYER_BUY_PERMISSION = "shopkeeper.player.buy";
   String PLAYER_TRADE_PERMISSION = "shopkeeper.player.trade";
   String PLAYER_BOOK_PERMISSION = "shopkeeper.player.book";

   static ShopkeepersPlugin getInstance() {
      return ShopkeepersAPI.getPlugin();
   }

   boolean hasCreatePermission(Player var1);

   ShopTypesRegistry<?> getShopTypeRegistry();

   DefaultShopTypes getDefaultShopTypes();

   ShopObjectTypesRegistry<?> getShopObjectTypeRegistry();

   DefaultShopObjectTypes getDefaultShopObjectTypes();

   UIRegistry<?> getUIRegistry();

   DefaultUITypes getDefaultUITypes();

   ShopkeeperRegistry getShopkeeperRegistry();

   ShopkeeperStorage getShopkeeperStorage();

   int updateItems();

   @Nullable
   Shopkeeper handleShopkeeperCreation(ShopCreationData var1);

   /** @deprecated */
   @Deprecated
   default PriceOffer createPriceOffer(ItemStack item, int price) {
      return PriceOffer.create(item, price);
   }

   /** @deprecated */
   @Deprecated
   default PriceOffer createPriceOffer(UnmodifiableItemStack item, int price) {
      return PriceOffer.create(item, price);
   }

   /** @deprecated */
   @Deprecated
   default TradeOffer createTradeOffer(ItemStack resultItem, ItemStack item1, @Nullable ItemStack item2) {
      return TradeOffer.create(resultItem, item1, item2);
   }

   /** @deprecated */
   @Deprecated
   default TradeOffer createTradeOffer(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      return TradeOffer.create(resultItem, item1, item2);
   }

   /** @deprecated */
   @Deprecated
   default BookOffer createBookOffer(String bookTitle, int price) {
      return BookOffer.create(bookTitle, price);
   }
}
