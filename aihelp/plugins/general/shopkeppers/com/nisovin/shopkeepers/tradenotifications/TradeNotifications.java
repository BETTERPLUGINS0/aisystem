package com.nisovin.shopkeepers.tradenotifications;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeCompletedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.ClickEventText;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Lazy;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import com.nisovin.shopkeepers.util.trading.MergedTrades;
import com.nisovin.shopkeepers.util.trading.TradeMerger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradeNotifications implements Listener {
   private static final long TRADE_MERGE_DURATION_TICKS = 300L;
   private static final long NEXT_MERGE_TIMEOUT_TICKS = 100L;
   private final Plugin plugin;
   private final NotificationUserPreferences userPreferences;
   private final TradeMerger tradeMerger;
   private boolean enabled;

   private static Map<String, Object> createTradeMessageArguments(TradeNotifications.TradeContext tradeContext) {
      Player player = tradeContext.getTradingPlayer();
      Map<String, Object> msgArgs = new HashMap();
      msgArgs.put("player", Unsafe.assertNonNull(player.getName()));
      msgArgs.put("playerId", () -> {
         return player.getUniqueId().toString();
      });
      msgArgs.put("resultItem", () -> {
         return TextUtils.getItemText(tradeContext.getResultItem());
      });
      msgArgs.put("resultItemAmount", () -> {
         return tradeContext.getResultItem().getAmount();
      });
      msgArgs.put("item1", () -> {
         return TextUtils.getItemText(tradeContext.getOfferedItem1());
      });
      msgArgs.put("item1Amount", () -> {
         return tradeContext.getOfferedItem1().getAmount();
      });
      msgArgs.put("item2", () -> {
         return TextUtils.getItemText(tradeContext.getOfferedItem2());
      });
      msgArgs.put("item2Amount", () -> {
         return ItemUtils.getItemStackAmount(tradeContext.getOfferedItem2());
      });
      return msgArgs;
   }

   public TradeNotifications(Plugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.userPreferences = new NotificationUserPreferences(plugin);
      TradeMerger.MergeMode var10004 = TradeMerger.MergeMode.DURATION;
      TradeNotifications var10005 = (TradeNotifications)Unsafe.initialized(this);
      Objects.requireNonNull(var10005);
      this.tradeMerger = (new TradeMerger(plugin, var10004, var10005::onTradesCompleted)).withMergeDurations(300L, 100L);
   }

   public void onEnable() {
      this.enabled = Settings.notifyPlayersAboutTrades || Settings.notifyShopOwnersAboutTrades;
      if (this.enabled) {
         Bukkit.getPluginManager().registerEvents(this, this.plugin);
         this.userPreferences.onEnable();
         this.tradeMerger.onEnable();
      }
   }

   public void onDisable() {
      if (this.enabled) {
         this.enabled = false;
         this.tradeMerger.onDisable();
         this.userPreferences.onDisable();
         HandlerList.unregisterAll(this);
      }
   }

   public NotificationUserPreferences getUserPreferences() {
      return this.userPreferences;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onTradeCompleted(ShopkeeperTradeCompletedEvent event) {
      this.tradeMerger.mergeTrade(event.getCompletedTrade());
   }

   private void onTradesCompleted(MergedTrades mergedTrades) {
      TradeNotifications.TradeContext tradeContext = new TradeNotifications.TradeContext(mergedTrades);
      this.sendTradeNotifications(tradeContext);
      this.sendOwnerTradeNotifications(tradeContext);
   }

   private void sendTradeNotifications(TradeNotifications.TradeContext tradeContext) {
      assert tradeContext != null;

      if (Settings.notifyPlayersAboutTrades) {
         Player shopOwner = null;
         String tradeNotificationPermission = "shopkeeper.trade-notifications.admin";
         if (tradeContext.getShopkeeper() instanceof PlayerShopkeeper) {
            tradeNotificationPermission = "shopkeeper.trade-notifications.player";
            shopOwner = ((PlayerShopkeeper)tradeContext.getShopkeeper()).getOwner();
         }

         Lazy<Text> tradeNotification = new Lazy(() -> {
            return this.getTradeNotificationMessage(tradeContext);
         });
         Iterator var5 = Bukkit.getOnlinePlayers().iterator();

         while(true) {
            Player player;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               player = (Player)var5.next();

               assert player != null;
            } while(player == shopOwner && Settings.notifyShopOwnersAboutTrades);

            if (this.userPreferences.isNotifyOnTrades(player) && PermissionUtils.hasPermission(player, tradeNotificationPermission)) {
               TextUtils.sendMessage(player, (Text)((Text)tradeNotification.get()));
               Settings.tradeNotificationSound.play(player);
               this.sendDisableTradeNotificationsHint(player);
            }
         }
      }
   }

   private Text getTradeNotificationMessage(TradeNotifications.TradeContext tradeContext) {
      assert tradeContext != null;

      Shopkeeper shopkeeper = tradeContext.getShopkeeper();
      Text message;
      if (tradeContext.isResultItemCurrency()) {
         if (tradeContext.hasOfferedItem2()) {
            message = Messages.buyNotificationTwoItems;
         } else {
            message = Messages.buyNotificationOneItem;
         }
      } else if (tradeContext.hasOfferedItem2()) {
         message = Messages.tradeNotificationTwoItems;
      } else {
         message = Messages.tradeNotificationOneItem;
      }

      Text shopText;
      if (shopkeeper.getName().isEmpty()) {
         if (shopkeeper instanceof PlayerShopkeeper) {
            shopText = Messages.tradeNotificationPlayerShop;
         } else {
            shopText = Messages.tradeNotificationAdminShop;
         }
      } else if (shopkeeper instanceof PlayerShopkeeper) {
         shopText = Messages.tradeNotificationNamedPlayerShop;
      } else {
         shopText = Messages.tradeNotificationNamedAdminShop;
      }

      Text tradeCountText = Text.EMPTY;
      if (tradeContext.getTradeCount() > 1) {
         tradeCountText = Messages.tradeNotificationTradeCount;
      }

      return this.getTradeNotificationMessage(tradeContext, message, shopText, tradeCountText);
   }

   private Text getTradeNotificationMessage(TradeNotifications.TradeContext tradeContext, Text message, Text shopText, Text tradeCountText) {
      MessageArguments shopMsgArgs = tradeContext.getShopMessageArguments();
      Map<String, Object> tradeMsgArgs = tradeContext.getTradeMessageArguments();
      shopText.setPlaceholderArguments(shopMsgArgs);
      tradeMsgArgs.put("shop", shopText);
      tradeCountText.setPlaceholderArguments("count", tradeContext.getTradeCount());
      tradeMsgArgs.put("trade_count", tradeCountText);
      message.setPlaceholderArguments(tradeMsgArgs);
      message.setPlaceholderArguments(shopMsgArgs);
      return message;
   }

   private void sendOwnerTradeNotifications(TradeNotifications.TradeContext tradeContext) {
      assert tradeContext != null;

      if (Settings.notifyShopOwnersAboutTrades) {
         if (tradeContext.getShopkeeper() instanceof PlayerShopkeeper) {
            PlayerShopkeeper playerShop = (PlayerShopkeeper)tradeContext.getShopkeeper();
            if (playerShop.isNotifyOnTrades()) {
               Player owner = playerShop.getOwner();
               if (owner != null) {
                  if (this.userPreferences.isNotifyOnTrades(owner)) {
                     Text message = this.getOwnerTradeNotificationMessage(tradeContext);
                     TextUtils.sendMessage(owner, (Text)message);
                     Settings.shopOwnerTradeNotificationSound.play(owner);
                     this.sendDisableTradeNotificationsHint(owner);
                  }
               }
            }
         }
      }
   }

   private Text getOwnerTradeNotificationMessage(TradeNotifications.TradeContext tradeContext) {
      assert tradeContext != null;

      Shopkeeper shopkeeper = tradeContext.getShopkeeper();
      boolean isBuy = tradeContext.isResultItemCurrency();
      Text message;
      if (isBuy) {
         if (tradeContext.hasOfferedItem2()) {
            message = Messages.ownerBuyNotificationTwoItems;
         } else {
            message = Messages.ownerBuyNotificationOneItem;
         }
      } else if (tradeContext.hasOfferedItem2()) {
         message = Messages.ownerTradeNotificationTwoItems;
      } else {
         message = Messages.ownerTradeNotificationOneItem;
      }

      Text shopText;
      if (isBuy) {
         if (shopkeeper.getName().isEmpty()) {
            shopText = Messages.ownerBuyNotificationShop;
         } else {
            shopText = Messages.ownerBuyNotificationNamedShop;
         }
      } else if (shopkeeper.getName().isEmpty()) {
         shopText = Messages.ownerTradeNotificationShop;
      } else {
         shopText = Messages.ownerTradeNotificationNamedShop;
      }

      Text tradeCountText = Text.EMPTY;
      if (tradeContext.getTradeCount() > 1) {
         tradeCountText = Messages.ownerTradeNotificationTradeCount;
      }

      return this.getTradeNotificationMessage(tradeContext, message, shopText, tradeCountText);
   }

   private void sendDisableTradeNotificationsHint(Player player) {
      if (PermissionUtils.hasPermission(player, "shopkeeper.notify.trades")) {
         if (!this.userPreferences.hasReceivedDisableTradeNotificationsHint(player)) {
            this.userPreferences.setReceivedDisableTradeNotificationsHint(player, true);
            Text command = Messages.disableTradeNotificationsHintCommand.copy();
            Text commandText = Text.clickEvent(ClickEventText.Action.SUGGEST_COMMAND, command.toPlainText()).next(command).getRoot();
            TextUtils.sendMessage(player, (Text)Messages.disableTradeNotificationsHint, (Object[])("command", commandText));
         }
      }
   }

   private static class TradeContext {
      private final MergedTrades mergedTrades;
      private final Lazy<MessageArguments> shopMessageArguments;
      private final Lazy<Map<String, Object>> tradeMessageArguments;
      private final Lazy<Boolean> isResultItemCurrency;

      TradeContext(MergedTrades mergedTrades) {
         this.mergedTrades = mergedTrades;
         this.shopMessageArguments = new Lazy(() -> {
            return ((AbstractShopkeeper)((TradeNotifications.TradeContext)Unsafe.initialized(this)).getShopkeeper()).getMessageArguments("shop_");
         });
         this.tradeMessageArguments = new Lazy(() -> {
            return TradeNotifications.createTradeMessageArguments((TradeNotifications.TradeContext)Unsafe.initialized(this));
         });
         this.isResultItemCurrency = new Lazy(() -> {
            return Currencies.getBase().getItemData().matches(((TradeNotifications.TradeContext)Unsafe.initialized(this)).getResultItem());
         });
      }

      public Player getTradingPlayer() {
         return this.mergedTrades.getInitialTrade().getPlayer();
      }

      public Shopkeeper getShopkeeper() {
         return this.mergedTrades.getInitialTrade().getShopkeeper();
      }

      public UnmodifiableItemStack getResultItem() {
         return this.mergedTrades.getResultItem();
      }

      public boolean isResultItemCurrency() {
         return (Boolean)this.isResultItemCurrency.get();
      }

      public UnmodifiableItemStack getOfferedItem1() {
         return this.mergedTrades.getOfferedItem1();
      }

      @Nullable
      public UnmodifiableItemStack getOfferedItem2() {
         return this.mergedTrades.getOfferedItem2();
      }

      private boolean hasOfferedItem2() {
         return this.mergedTrades.getInitialTrade().hasOfferedItem2();
      }

      public int getTradeCount() {
         return this.mergedTrades.getTradeCount();
      }

      public MessageArguments getShopMessageArguments() {
         return (MessageArguments)this.shopMessageArguments.get();
      }

      public Map<String, Object> getTradeMessageArguments() {
         return (Map)this.tradeMessageArguments.get();
      }
   }
}
