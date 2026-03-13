package com.nisovin.shopkeepers.trading.commandtrading;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class CommandTrading implements Listener {
   private final ShopkeepersPlugin plugin;

   public CommandTrading(ShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      ignoreCancelled = true
   )
   void onShopkeeperTrade(ShopkeeperTradeEvent event) {
      UnmodifiableItemStack item1 = event.getReceivedItem1();
      UnmodifiableItemStack item2 = event.getReceivedItem2();
      UnmodifiableItemStack resultItem = event.getResultItem();
      String item1Command = CommandTradingUtils.getTradedCommand(item1);
      String item2Command = CommandTradingUtils.getTradedCommand(item2);
      String resultItemCommand = CommandTradingUtils.getTradedCommand(resultItem);
      if (item1Command != null || item2Command != null || resultItemCommand != null) {
         if (item1Command != null) {
            Log.debug("First item contains traded command \"" + item1Command + "\"");
            event.setReceivedItem1((UnmodifiableItemStack)null);
         }

         if (item2Command != null) {
            Log.debug("Second item contains traded command \"" + item2Command + "\"");
            event.setReceivedItem2((UnmodifiableItemStack)null);
         }

         if (resultItemCommand != null) {
            Log.debug("Result item contains traded command \"" + resultItemCommand + "\"");
            event.setResultItem((UnmodifiableItemStack)null);
         }

         TradedCommandsTradeEffect tradeEffect = new TradedCommandsTradeEffect(item1Command, item1Command != null ? ((UnmodifiableItemStack)Unsafe.assertNonNull(item1)).getAmount() : 0, item2Command, item2Command != null ? ((UnmodifiableItemStack)Unsafe.assertNonNull(item2)).getAmount() : 0, resultItemCommand, resultItem != null ? ((UnmodifiableItemStack)Unsafe.assertNonNull(resultItem)).getAmount() : 0);
         event.getTradeEffects().add(tradeEffect);
      }
   }
}
