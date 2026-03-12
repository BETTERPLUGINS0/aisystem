package com.nisovin.shopkeepers.tradelog.base;

import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import java.nio.file.Path;
import org.bukkit.plugin.Plugin;

public abstract class AbstractFileTradeLogger extends AbstractSingleWriterTradeLogger {
   public static final String TRADE_LOGS_FOLDER = "trade-logs";
   protected final Path tradeLogsFolder;

   public AbstractFileTradeLogger(Plugin plugin, TradeLogStorageType storageType) {
      super(plugin, storageType);
      this.tradeLogsFolder = plugin.getDataFolder().toPath().resolve("trade-logs");
   }
}
