package com.nisovin.shopkeepers.tradelog;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeCompletedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.tradelog.csv.CsvTradeLogger;
import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryProvider;
import com.nisovin.shopkeepers.tradelog.sqlite.SQLiteTradeLogger;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.trading.MergedTrades;
import com.nisovin.shopkeepers.util.trading.TradeMerger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradeLoggers implements Listener {
   private final SKShopkeepersPlugin plugin;
   private final List<TradeLogger> loggers = new ArrayList();
   @Nullable
   private TradeMerger tradeMerger;
   private boolean enabled = false;

   public TradeLoggers(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      this.enabled = true;
      int mergeDuration = Settings.tradeLogMergeDurationTicks;
      if (mergeDuration == 1) {
         this.tradeMerger = new TradeMerger(this.plugin, TradeMerger.MergeMode.SAME_CLICK_EVENT, this::processTrades);
      } else {
         this.tradeMerger = (new TradeMerger(this.plugin, TradeMerger.MergeMode.DURATION, this::processTrades)).withMergeDurations((long)mergeDuration, (long)Settings.tradeLogNextMergeTimeoutTicks);
      }

      assert this.tradeMerger != null;

      this.tradeMerger.onEnable();
      switch(Settings.tradeLogStorage) {
      case CSV:
         this.loggers.add(new CsvTradeLogger(this.plugin));
         break;
      case SQLITE:
         this.loggers.add(new SQLiteTradeLogger(this.plugin));
      case DISABLED:
      }

      this.loggers.forEach(TradeLogger::setup);
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void onDisable() {
      if (this.enabled) {
         this.enabled = false;
         HandlerList.unregisterAll(this);
         ((TradeMerger)Unsafe.assertNonNull(this.tradeMerger)).onDisable();
         this.loggers.forEach(TradeLogger::flush);
         this.loggers.clear();
      }
   }

   @Nullable
   public TradingHistoryProvider getTradingHistoryProvider() {
      Iterator var1 = this.loggers.iterator();

      TradeLogger logger;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         logger = (TradeLogger)var1.next();
      } while(!(logger instanceof TradingHistoryProvider));

      TradingHistoryProvider tradingHistoryProvider = (TradingHistoryProvider)logger;
      return tradingHistoryProvider;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onTradeCompleted(ShopkeeperTradeCompletedEvent event) {
      if (!this.loggers.isEmpty()) {
         ((TradeMerger)Unsafe.assertNonNull(this.tradeMerger)).mergeTrade(event.getCompletedTrade());
      }
   }

   private void processTrades(MergedTrades trades) {
      TradeRecord trade = TradeRecord.create(trades);
      this.loggers.forEach((logger) -> {
         logger.logTrade(trade);
      });
   }
}
