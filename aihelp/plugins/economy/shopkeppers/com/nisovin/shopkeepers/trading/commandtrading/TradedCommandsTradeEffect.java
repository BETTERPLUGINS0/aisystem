package com.nisovin.shopkeepers.trading.commandtrading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.trading.TradeEffect;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradedCommandsTradeEffect implements TradeEffect {
   @Nullable
   private final String item1Command;
   private final int item1CommandCount;
   @Nullable
   private final String item2Command;
   private final int item2CommandCount;
   @Nullable
   private final String resultItemCommand;
   private final int resultItemCommandCount;

   public TradedCommandsTradeEffect(@Nullable String item1Command, int item1CommandCount, @Nullable String item2CommandCount, int item2Amount, @Nullable String resultItemCommand, int resultItemCommandCount) {
      this.item1Command = item1Command;
      this.item1CommandCount = item1CommandCount;
      this.item2Command = item2CommandCount;
      this.item2CommandCount = item2Amount;
      this.resultItemCommand = resultItemCommand;
      this.resultItemCommandCount = resultItemCommandCount;
   }

   public void onTradeAborted(ShopkeeperTradeEvent tradeEvent) {
   }

   public void onTradeApplied(ShopkeeperTradeEvent tradeEvent) {
      this.dispatchTradedCommand(tradeEvent, this.item1Command, this.item1CommandCount);
      this.dispatchTradedCommand(tradeEvent, this.item2Command, this.item2CommandCount);
      this.dispatchTradedCommand(tradeEvent, this.resultItemCommand, this.resultItemCommandCount);
   }

   private void dispatchTradedCommand(ShopkeeperTradeEvent tradeEvent, @Nullable String command, int count) {
      if (command != null && count > 0) {
         Player tradingPlayer = tradeEvent.getPlayer();
         Shopkeeper shopkeeper = tradeEvent.getShopkeeper();
         String preparedCommand = StringUtils.replaceArguments(command, "player_name", Unsafe.assertNonNull(tradingPlayer.getName()), "player_uuid", tradingPlayer.getUniqueId(), "player_displayname", tradingPlayer.getDisplayName(), "shop_uuid", shopkeeper.getUniqueId());
         Log.debug("Dispatching " + count + "x traded command \"" + preparedCommand + "\"");

         for(int i = 0; i < count; ++i) {
            try {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), preparedCommand);
            } catch (Exception var9) {
               Log.warning((String)("Error during the execution of the traded command \"" + preparedCommand + "\""), (Throwable)var9);
            }
         }

      }
   }
}
