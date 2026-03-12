package com.nisovin.shopkeepers.tradelog.sqlite;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import com.nisovin.shopkeepers.tradelog.TradeLogUtils;
import com.nisovin.shopkeepers.tradelog.base.AbstractFileTradeLogger;
import com.nisovin.shopkeepers.tradelog.base.AbstractSingleWriterTradeLogger;
import com.nisovin.shopkeepers.tradelog.data.PlayerRecord;
import com.nisovin.shopkeepers.tradelog.data.ShopRecord;
import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.tradelog.history.PlayerSelector;
import com.nisovin.shopkeepers.tradelog.history.ShopSelector;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryProvider;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryRequest;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryResult;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.FileUtils;
import com.nisovin.shopkeepers.util.java.JdbcUtils;
import com.nisovin.shopkeepers.util.java.Range;
import com.nisovin.shopkeepers.util.java.Retry;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SQLiteTradeLogger extends AbstractFileTradeLogger implements TradingHistoryProvider {
   private static final int TRANSACTION_RETRY_MAX_ATTEMPTS = 20;
   private static final long TRANSACTION_RETRY_DELAY_MILLIS = 50L;
   private static final String FILE_NAME = "trades.db";
   private static final String TABLE_TRADE = "trade";
   private static final String COLUMN_TIMESTAMP = "timestamp";
   private static final String COLUMN_PLAYER_UUID = "player_uuid";
   private static final String COLUMN_PLAYER_NAME = "player_name";
   private static final String COLUMN_SHOP_UUID = "shop_uuid";
   private static final String COLUMN_SHOP_TYPE = "shop_type";
   private static final String COLUMN_SHOP_WORLD = "shop_world";
   private static final String COLUMN_SHOP_X = "shop_x";
   private static final String COLUMN_SHOP_Y = "shop_y";
   private static final String COLUMN_SHOP_Z = "shop_z";
   private static final String COLUMN_SHOP_OWNER_UUID = "shop_owner_uuid";
   private static final String COLUMN_SHOP_OWNER_NAME = "shop_owner_name";
   private static final String COLUMN_ITEM_1_TYPE = "item_1_type";
   private static final String COLUMN_ITEM_1_AMOUNT = "item_1_amount";
   private static final String COLUMN_ITEM_1_METADATA = "item_1_metadata";
   private static final String COLUMN_ITEM_2_TYPE = "item_2_type";
   private static final String COLUMN_ITEM_2_AMOUNT = "item_2_amount";
   private static final String COLUMN_ITEM_2_METADATA = "item_2_metadata";
   private static final String COLUMN_RESULT_ITEM_TYPE = "result_item_type";
   private static final String COLUMN_RESULT_ITEM_AMOUNT = "result_item_amount";
   private static final String COLUMN_RESULT_ITEM_METADATA = "result_item_metadata";
   private static final String COLUMN_TRADE_COUNT = "trade_count";
   private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS trade (timestamp VARCHAR(30) NOT NULL, player_uuid CHARACTER(36) NOT NULL, player_name VARCHAR(16) NOT NULL, shop_uuid CHARACTER(36) NOT NULL, shop_type VARCHAR(32) NOT NULL, shop_world VARCHAR(32), shop_x INTEGER NOT NULL, shop_y INTEGER NOT NULL, shop_z INTEGER NOT NULL, shop_owner_uuid CHARACTER(36), shop_owner_name VARCHAR(16), item_1_type VARCHAR(64) NOT NULL, item_1_amount TINYINT UNSIGNED NOT NULL, item_1_metadata TEXT NOT NULL, item_2_type VARCHAR(64), item_2_amount TINYINT UNSIGNED, item_2_metadata TEXT, result_item_type VARCHAR(64) NOT NULL, result_item_amount TINYINT UNSIGNED NOT NULL, result_item_metadata TEXT NOT NULL, trade_count SMALLINT UNSIGNED NOT NULL);";
   private static final String INSERT_TRADE_SQL;
   private final String connectionURL;
   @Nullable
   private volatile String setupFailureReason = null;
   private volatile boolean performSetupAgain = false;

   private static String getSelectTradesSql(boolean filterByPlayer, boolean filterByShop, boolean filterByOwner, boolean filterByAdminShop, boolean filterByPlayerShop) {
      String query = "SELECT * FROM trade";
      ArrayList<String> filters = new ArrayList();
      if (filterByPlayer) {
         filters.add("player_uuid=?");
      }

      if (filterByShop) {
         filters.add("shop_uuid=?");
      }

      if (filterByOwner) {
         filters.add("shop_owner_uuid=?");
      }

      if (filterByAdminShop) {
         filters.add("shop_owner_uuid IS NULL");
      }

      if (filterByPlayerShop) {
         filters.add("shop_owner_uuid IS NOT NULL");
      }

      if (!filters.isEmpty()) {
         query = query + " WHERE " + String.join(" AND ", filters);
      }

      query = query + " ORDER BY timestamp DESC LIMIT ? OFFSET ?;";
      return query;
   }

   private static String toTradesCountSql(String selectTradesSql) {
      String tradesCountSql = StringUtils.replaceFirst(selectTradesSql, "SELECT *", "SELECT COUNT(*)");
      tradesCountSql = StringUtils.replaceFirst(tradesCountSql, " ORDER BY timestamp DESC", "");
      tradesCountSql = StringUtils.replaceFirst(tradesCountSql, " LIMIT ? OFFSET ?", "");
      return tradesCountSql;
   }

   public SQLiteTradeLogger(SKShopkeepersPlugin plugin) {
      super(plugin, TradeLogStorageType.SQLITE);
      this.connectionURL = "jdbc:sqlite:" + String.valueOf(this.tradeLogsFolder.resolve("trades.db"));
   }

   private Connection getConnection() throws SQLException, IOException {
      FileUtils.createDirectories(this.tradeLogsFolder);
      return DriverManager.getConnection(this.connectionURL);
   }

   private <T> T runTransaction(SQLiteTradeLogger.SqlTransaction<T> transaction) throws Exception {
      boolean done = false;
      Object result = null;

      try {
         Connection connection = this.getConnection();

         Object var5;
         try {
            if (this.performSetupAgain) {
               this.performSetup(connection);
            }

            result = transaction.execute(connection);
            done = true;
            var5 = Unsafe.cast(result);
         } catch (Throwable var8) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (connection != null) {
            connection.close();
         }

         return var5;
      } catch (Exception var9) {
         if (done) {
            Log.severe((String)"Failed to close the database connection!", (Throwable)var9);
            return Unsafe.cast(result);
         } else {
            this.performSetupAgain = true;
            throw var9;
         }
      }
   }

   private <T> T retryTransaction(SQLiteTradeLogger.SqlTransaction<T> transaction) throws Exception {
      return Retry.retry(() -> {
         return this.runTransaction(transaction);
      }, 20, (attemptNumber, exception, retry) -> {
         if (retry) {
            try {
               Thread.sleep(50L);
            } catch (InterruptedException var4) {
               Thread.currentThread().interrupt();
            }
         }

      });
   }

   protected void asyncSetup() {
      super.asyncSetup();

      try {
         Connection connection = this.getConnection();

         try {
            this.performSetup(connection);
         } catch (Throwable var5) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (connection != null) {
            connection.close();
         }
      } catch (Exception var6) {
         this.setupFailureReason = var6.getMessage();
         Log.severe((String)(this.logPrefix + this.setupFailureReason), (Throwable)var6);
      }

   }

   protected void postSetup() {
      String setupFailureReason = this.setupFailureReason;
      if (setupFailureReason != null) {
         this.disable(setupFailureReason);
      }

   }

   private void performSetup(Connection connection) throws Exception {
      this.createTable(connection);
   }

   private void createTable(Connection connection) throws Exception {
      try {
         Statement statement = connection.createStatement();

         try {
            statement.execute("CREATE TABLE IF NOT EXISTS trade (timestamp VARCHAR(30) NOT NULL, player_uuid CHARACTER(36) NOT NULL, player_name VARCHAR(16) NOT NULL, shop_uuid CHARACTER(36) NOT NULL, shop_type VARCHAR(32) NOT NULL, shop_world VARCHAR(32), shop_x INTEGER NOT NULL, shop_y INTEGER NOT NULL, shop_z INTEGER NOT NULL, shop_owner_uuid CHARACTER(36), shop_owner_name VARCHAR(16), item_1_type VARCHAR(64) NOT NULL, item_1_amount TINYINT UNSIGNED NOT NULL, item_1_metadata TEXT NOT NULL, item_2_type VARCHAR(64), item_2_amount TINYINT UNSIGNED, item_2_metadata TEXT, result_item_type VARCHAR(64) NOT NULL, result_item_amount TINYINT UNSIGNED NOT NULL, result_item_metadata TEXT NOT NULL, trade_count SMALLINT UNSIGNED NOT NULL);");
         } catch (Throwable var6) {
            if (statement != null) {
               try {
                  statement.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (statement != null) {
            statement.close();
         }

      } catch (Exception var7) {
         throw new Exception("Could not create table 'trade'!", var7);
      }
   }

   protected void writeTrades(AbstractSingleWriterTradeLogger.SaveContext saveContext) throws Exception {
      if (saveContext.hasUnsavedTrades()) {
         this.runTransaction((connection) -> {
            boolean done = false;

            try {
               PreparedStatement insertStatement = connection.prepareStatement(INSERT_TRADE_SQL);

               Object var6;
               try {
                  TradeRecord trade = saveContext.getNextUnsavedTrade();

                  while(true) {
                     if (trade == null) {
                        var6 = null;
                        break;
                     }

                     this.insertTrade(insertStatement, trade);
                     saveContext.onTradeSuccessfullySaved();
                     trade = saveContext.getNextUnsavedTrade();
                  }
               } catch (Throwable var8) {
                  if (insertStatement != null) {
                     try {
                        insertStatement.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (insertStatement != null) {
                  insertStatement.close();
               }

               return var6;
            } catch (Exception var9) {
               if (done) {
                  Log.severe((String)"Failed to close the database statement", (Throwable)var9);
                  return null;
               } else {
                  throw var9;
               }
            }
         });
      }
   }

   private void insertTrade(PreparedStatement insertStatement, TradeRecord trade) throws SQLException {
      Instant timestamp = trade.getTimestamp();
      PlayerRecord player = trade.getPlayer();
      ShopRecord shop = trade.getShop();
      PlayerRecord shopOwner = shop.getOwner();
      String shopOwnerId = null;
      String shopOwnerName = null;
      if (shopOwner != null) {
         shopOwnerId = shopOwner.getUniqueId().toString();
         shopOwnerName = shopOwner.getName();
      }

      UnmodifiableItemStack resultItem = trade.getResultItem();
      UnmodifiableItemStack item1 = trade.getItem1();
      UnmodifiableItemStack item2 = trade.getItem2();
      String item2Type = null;
      Integer item2Amount = null;
      String item2Metadata = null;
      if (item2 != null) {
         item2Type = item2.getType().name();
         item2Amount = item2.getAmount();
         item2Metadata = this.getItemMetadata(item2);
      }

      insertStatement.setString(1, timestamp.toString());
      insertStatement.setString(2, player.getUniqueId().toString());
      insertStatement.setString(3, player.getName());
      insertStatement.setString(4, shop.getUniqueId().toString());
      insertStatement.setString(5, shop.getTypeId());
      insertStatement.setString(6, shop.getWorldName());
      insertStatement.setInt(7, shop.getX());
      insertStatement.setInt(8, shop.getY());
      insertStatement.setInt(9, shop.getZ());
      insertStatement.setString(10, shopOwnerId);
      insertStatement.setString(11, shopOwnerName);
      insertStatement.setString(12, item1.getType().name());
      insertStatement.setInt(13, item1.getAmount());
      insertStatement.setString(14, this.getItemMetadata(item1));
      insertStatement.setString(15, item2Type);
      insertStatement.setObject(16, item2Amount, -6);
      insertStatement.setString(17, item2Metadata);
      insertStatement.setString(18, resultItem.getType().name());
      insertStatement.setInt(19, resultItem.getAmount());
      insertStatement.setString(20, this.getItemMetadata(resultItem));
      insertStatement.setInt(21, trade.getTradeCount());
      insertStatement.executeUpdate();
   }

   public CompletableFuture<TradingHistoryResult> getTradingHistory(TradingHistoryRequest request) {
      return CompletableFuture.supplyAsync(() -> {
         PlayerSelector playerSelector = request.playerSelector;
         ShopSelector shopSelector = request.shopSelector;
         Range range = request.range;
         ArrayList<Object> filterParameters = new ArrayList();
         boolean filterByPlayer;
         if (playerSelector == PlayerSelector.ALL) {
            filterByPlayer = false;
         } else {
            if (!(playerSelector instanceof PlayerSelector.ByUUID)) {
               throw Validate.State.error("Unexpected player selector: " + playerSelector.getClass().getName());
            }

            PlayerSelector.ByUUID playerByUUIDSelector = (PlayerSelector.ByUUID)playerSelector;
            filterByPlayer = true;
            filterParameters.add(playerByUUIDSelector.getPlayerUUID().toString());
         }

         boolean filterByShop;
         boolean filterByOwner;
         boolean filterByAdminShop;
         boolean filterByPlayerShop;
         if (shopSelector == ShopSelector.ALL) {
            filterByShop = false;
            filterByOwner = false;
            filterByAdminShop = false;
            filterByPlayerShop = false;
         } else if (shopSelector == ShopSelector.ADMIN_SHOPS) {
            filterByShop = false;
            filterByOwner = false;
            filterByAdminShop = true;
            filterByPlayerShop = false;
         } else if (shopSelector == ShopSelector.PLAYER_SHOPS) {
            filterByShop = false;
            filterByOwner = false;
            filterByAdminShop = false;
            filterByPlayerShop = true;
         } else if (shopSelector instanceof ShopSelector.ByOwnerUUID) {
            ShopSelector.ByOwnerUUID byOwnerUUIDSelector = (ShopSelector.ByOwnerUUID)shopSelector;
            filterByShop = false;
            filterByOwner = true;
            filterByAdminShop = false;
            filterByPlayerShop = false;
            filterParameters.add(byOwnerUUIDSelector.getOwnerUUID().toString());
         } else {
            if (!(shopSelector instanceof ShopSelector.ByShopUUID)) {
               throw Validate.State.error("Unexpected shop selector: " + shopSelector.getClass().getName());
            }

            ShopSelector.ByShopUUID byShopUUIDSelector = (ShopSelector.ByShopUUID)shopSelector;
            filterByShop = true;
            filterByOwner = false;
            filterByAdminShop = false;
            filterByPlayerShop = false;
            filterParameters.add(byShopUUIDSelector.getShopUUID().toString());
            UUID ownerUUID = byShopUUIDSelector.getOwnerUUID();
            if (ownerUUID != null) {
               filterByOwner = true;
               filterParameters.add(ownerUUID.toString());
            }
         }

         String selectTradesSql = getSelectTradesSql(filterByPlayer, filterByShop, filterByOwner, filterByAdminShop, filterByPlayerShop);
         String tradesCountSql = toTradesCountSql(selectTradesSql);

         try {
            return (TradingHistoryResult)this.retryTransaction((connection) -> {
               int totalTradesCount = 0;
               List<TradeRecord> trades = new ArrayList();
               PreparedStatement tradesCountStatement = connection.prepareStatement(tradesCountSql);

               try {
                  JdbcUtils.setParameters(tradesCountStatement, 0, filterParameters.toArray());
                  ResultSet resultSet = tradesCountStatement.executeQuery();

                  try {
                     if (resultSet.next()) {
                        totalTradesCount = resultSet.getInt(1);
                     }
                  } catch (Throwable var20) {
                     if (resultSet != null) {
                        try {
                           resultSet.close();
                        } catch (Throwable var19) {
                           var20.addSuppressed(var19);
                        }
                     }

                     throw var20;
                  }

                  if (resultSet != null) {
                     resultSet.close();
                  }
               } catch (Throwable var23) {
                  if (tradesCountStatement != null) {
                     try {
                        tradesCountStatement.close();
                     } catch (Throwable var18) {
                        var23.addSuppressed(var18);
                     }
                  }

                  throw var23;
               }

               if (tradesCountStatement != null) {
                  tradesCountStatement.close();
               }

               if (totalTradesCount == 0) {
                  return new TradingHistoryResult(trades, totalTradesCount);
               } else {
                  int startIndex = range.getStartIndex(totalTradesCount);
                  int endIndex = range.getEndIndex(totalTradesCount);
                  int offset = startIndex;
                  int limit = endIndex - startIndex;
                  PreparedStatement selectTradesStatement = connection.prepareStatement(selectTradesSql);

                  try {
                     JdbcUtils.setParameters(selectTradesStatement, 0, filterParameters.toArray());
                     JdbcUtils.setParameters(selectTradesStatement, filterParameters.size(), limit, offset);
                     ResultSet resultSetx = selectTradesStatement.executeQuery();

                     try {
                        while(resultSetx.next()) {
                           trades.add(this.readTradeRecord(resultSetx));
                        }
                     } catch (Throwable var21) {
                        if (resultSetx != null) {
                           try {
                              resultSetx.close();
                           } catch (Throwable var17) {
                              var21.addSuppressed(var17);
                           }
                        }

                        throw var21;
                     }

                     if (resultSetx != null) {
                        resultSetx.close();
                     }
                  } catch (Throwable var22) {
                     if (selectTradesStatement != null) {
                        try {
                           selectTradesStatement.close();
                        } catch (Throwable var16) {
                           var22.addSuppressed(var16);
                        }
                     }

                     throw var22;
                  }

                  if (selectTradesStatement != null) {
                     selectTradesStatement.close();
                  }

                  return new TradingHistoryResult(trades, totalTradesCount);
               }
            });
         } catch (Exception var16) {
            throw new RuntimeException("Failed to fetch trading history: " + request.toString(), var16);
         }
      }, ((SKShopkeepersPlugin)this.plugin).getAsyncExecutor());
   }

   private TradeRecord readTradeRecord(ResultSet resultSet) throws SQLException {
      assert resultSet != null;

      Instant timestamp = Instant.parse((CharSequence)Validate.notNull(resultSet.getString("timestamp")));
      UUID playerUniqueId = UUID.fromString((String)Validate.notNull(resultSet.getString("player_uuid")));
      String playerName = (String)Validate.notNull(resultSet.getString("player_name"));
      PlayerRecord player = PlayerRecord.of(playerUniqueId, playerName);
      UUID shopUniqueId = UUID.fromString((String)Validate.notNull(resultSet.getString("shop_uuid")));
      String shopType = (String)Validate.notNull(resultSet.getString("shop_type"));
      PlayerRecord owner = null;
      String ownerUniqueIdString = resultSet.getString("shop_owner_uuid");
      String worldName;
      if (ownerUniqueIdString != null) {
         UUID ownerUniqueId = UUID.fromString(ownerUniqueIdString);
         worldName = (String)Validate.notNull(resultSet.getString("shop_owner_name"));
         owner = PlayerRecord.of(ownerUniqueId, worldName);
      }

      String shopName = "";
      worldName = resultSet.getString("shop_world");
      int shopX = resultSet.getInt("shop_x");
      int shopY = resultSet.getInt("shop_y");
      int shopZ = resultSet.getInt("shop_z");
      ShopRecord shop = new ShopRecord(shopUniqueId, shopType, owner, shopName, worldName, shopX, shopY, shopZ);
      String item1Type = resultSet.getString("item_1_type");
      int item1Amount = resultSet.getInt("item_1_amount");
      String item1Metadata = resultSet.getString("item_1_metadata");
      UnmodifiableItemStack item1 = loadItemStack(item1Type, item1Amount, item1Metadata);
      if (item1 == null) {
         throw new RuntimeException("item1 is empty!");
      } else {
         String item2Type = resultSet.getString("item_2_type");
         int item2Amount = resultSet.getInt("item_2_amount");
         String item2Metadata = resultSet.getString("item_2_metadata");
         UnmodifiableItemStack item2 = loadItemStack(item2Type, item2Amount, item2Metadata);
         String resultItemType = resultSet.getString("result_item_type");
         int resultItemAmount = resultSet.getInt("result_item_amount");
         String resultItemMetadata = resultSet.getString("result_item_metadata");
         UnmodifiableItemStack resultItem = loadItemStack(resultItemType, resultItemAmount, resultItemMetadata);
         if (resultItem == null) {
            throw new RuntimeException("resultItem is empty!");
         } else {
            int tradeCount = resultSet.getInt("trade_count");
            return new TradeRecord(timestamp, player, shop, resultItem, item1, item2, tradeCount);
         }
      }
   }

   @Nullable
   private static UnmodifiableItemStack loadItemStack(@Nullable String itemType, int amount, @Nullable String metadata) {
      if (itemType != null && !itemType.isEmpty() && amount > 0) {
         Material material = ItemUtils.parseMaterial(itemType);
         if (material != null && material.isItem()) {
            ItemStack itemStack;
            try {
               itemStack = TradeLogUtils.loadItemStack(material, amount, metadata);
            } catch (Exception var6) {
               Log.debug((String)"Failed to load item stack metadata from history!", (Throwable)var6);
               itemStack = new ItemStack(material, amount);
            }

            return UnmodifiableItemStack.ofNonNull(itemStack);
         } else {
            throw new RuntimeException("Invalid item type: " + itemType);
         }
      } else {
         return null;
      }
   }

   static {
      CharSequence[] var10001 = new CharSequence[]{"timestamp", "player_uuid", "player_name", "shop_uuid", "shop_type", "shop_world", "shop_x", "shop_y", "shop_z", "shop_owner_uuid", "shop_owner_name", "item_1_type", "item_1_amount", "item_1_metadata", "item_2_type", "item_2_amount", "item_2_metadata", "result_item_type", "result_item_amount", "result_item_metadata", "trade_count"};
      INSERT_TRADE_SQL = "INSERT INTO trade(" + String.join(", ", var10001) + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
   }

   @FunctionalInterface
   public interface SqlTransaction<T> {
      T execute(Connection var1) throws Exception;
   }
}
