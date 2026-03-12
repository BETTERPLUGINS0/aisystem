package com.nisovin.shopkeepers.tradelog.csv;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import com.nisovin.shopkeepers.tradelog.base.AbstractFileTradeLogger;
import com.nisovin.shopkeepers.tradelog.base.AbstractSingleWriterTradeLogger;
import com.nisovin.shopkeepers.tradelog.data.PlayerRecord;
import com.nisovin.shopkeepers.tradelog.data.ShopRecord;
import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.util.csv.CsvFormatter;
import com.nisovin.shopkeepers.util.java.FileUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.plugin.Plugin;

public class CsvTradeLogger extends AbstractFileTradeLogger {
   private static final String FILE_NAME_PREFIX = "trades-";
   private static final List<? extends String> CSV_HEADER = Collections.unmodifiableList(Arrays.asList("time", "player_uuid", "player_name", "shop_uuid", "shop_type", "shop_world", "shop_x", "shop_y", "shop_z", "shop_owner_uuid", "shop_owner_name", "item1_type", "item1_amount", "item1_metadata", "item2_type", "item2_amount", "item2_metadata", "result_item_type", "result_item_amount", "result_item_metadata", "trade_count"));
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone((ZoneId)Unsafe.assertNonNull(ZoneId.systemDefault()));
   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss").withZone((ZoneId)Unsafe.assertNonNull(ZoneId.systemDefault()));
   private final CsvFormatter csv = (new CsvFormatter()).escapeNewlines(false).warnOnNewlines();

   public CsvTradeLogger(Plugin plugin) {
      super(plugin, TradeLogStorageType.CSV);
   }

   private Path getLogFile(Instant timestamp) {
      assert timestamp != null;

      String fileName = "trades-" + DATE_FORMAT.format(timestamp) + ".csv";
      return this.tradeLogsFolder.resolve(fileName);
   }

   private String toCSVRecord(TradeRecord trade) {
      Instant timestamp = trade.getTimestamp();
      PlayerRecord player = trade.getPlayer();
      ShopRecord shop = trade.getShop();
      String worldName = StringUtils.getOrEmpty(shop.getWorldName());
      PlayerRecord shopOwner = shop.getOwner();
      String shopOwnerId = "";
      String shopOwnerName = "";
      if (shopOwner != null) {
         shopOwnerId = shopOwner.getUniqueId().toString();
         shopOwnerName = shopOwner.getName();
      }

      UnmodifiableItemStack resultItem = trade.getResultItem();
      UnmodifiableItemStack item1 = trade.getItem1();
      UnmodifiableItemStack item2 = trade.getItem2();
      String item2Type = "";
      String item2Amount = "";
      String item2Metadata = "";
      if (item2 != null) {
         item2Type = item2.getType().name();
         item2Amount = String.valueOf(item2.getAmount());
         item2Metadata = this.getItemMetadata(item2);
      }

      return this.csv.formatRecord((Iterable)Arrays.asList(TIME_FORMAT.format(timestamp), player.getUniqueId(), player.getName(), shop.getUniqueId(), shop.getTypeId(), worldName, shop.getX(), shop.getY(), shop.getZ(), shopOwnerId, shopOwnerName, item1.getType().name(), item1.getAmount(), this.getItemMetadata(item1), item2Type, item2Amount, item2Metadata, resultItem.getType().name(), resultItem.getAmount(), this.getItemMetadata(resultItem), trade.getTradeCount()));
   }

   protected void writeTrades(AbstractSingleWriterTradeLogger.SaveContext saveContext) throws Exception {
      TradeRecord trade = saveContext.getNextUnsavedTrade();
      if (trade != null) {
         Path logFile = this.getLogFile(trade.getTimestamp());
         FileUtils.createParentDirectories(logFile);
         Path parent = logFile.getParent();
         if (parent != null) {
            FileUtils.checkIsDirectoryWritable(parent);
         }

         boolean isNew = !Files.exists(logFile, new LinkOption[0]);
         boolean isEmpty = isNew || Files.size(logFile) == 0L;
         if (!isNew) {
            FileUtils.checkIsFileWritable(logFile);
         }

         OpenOption[] openOptions;
         if (isNew) {
            openOptions = new OpenOption[]{StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.DSYNC};
         } else {
            openOptions = new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.DSYNC};
         }

         boolean done = false;

         try {
            Writer writer = FileUtils.newUnbufferedWriter(logFile, (Charset)Unsafe.assertNonNull(StandardCharsets.UTF_8), openOptions);

            try {
               if (isNew) {
                  FileUtils.fsyncParentDirectory(logFile);
               }

               if (isEmpty) {
                  writer.write(this.csv.formatRecord((Iterable)CSV_HEADER));
                  writer.flush();
               }

               while(true) {
                  writer.write(this.toCSVRecord(trade));
                  writer.flush();
                  saveContext.onTradeSuccessfullySaved();
                  trade = saveContext.getNextUnsavedTrade();
                  if (trade != null) {
                     Path nextLogFile = this.getLogFile(trade.getTimestamp());
                     if (logFile.equals(nextLogFile)) {
                        continue;
                     }
                  }

                  done = true;
                  break;
               }
            } catch (Throwable var13) {
               if (writer != null) {
                  try {
                     writer.close();
                  } catch (Throwable var12) {
                     var13.addSuppressed(var12);
                  }
               }

               throw var13;
            }

            if (writer != null) {
               writer.close();
            }
         } catch (IOException var14) {
            if (!done) {
               throw var14;
            }

            Log.severe((String)"Failed to close the CSV trade log file!", (Throwable)var14);
         }

         if (saveContext.hasUnsavedTrades()) {
            this.writeTrades(saveContext);
         }

      }
   }
}
