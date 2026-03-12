package me.gypopo.economyshopgui.files;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.JsonUtil;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TransactionLog {
   private File file;
   private TransactionLog.Database db;
   private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private final ArrayList<String> cache = new ArrayList();
   private final EconomyShopGUI plugin;
   private ArrayList<TransactionLog.SimpleTransaction> TRANSACTION_CACHE;

   public TransactionLog(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public boolean setup() {
      if (this.db != null) {
         this.saveUnsaved();
      }

      this.file = new File(this.plugin.getDataFolder(), "transaction-log.txt");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
            SendMessage.errorMessage("Failed to create the transactions log file");
            return false;
         }
      }

      try {
         this.db = new TransactionLog.Database();
         return true;
      } catch (IOException var2) {
         SendMessage.warnMessage("Failed to create transaction directory...");
         return false;
      }
   }

   public void log(String transaction) {
      this.cache.add("[" + this.dateFormatter.format((new Date()).getTime()) + "] - " + transaction);
   }

   public void log(UUID uuid, ShopItem item, Map<EcoType, Double> prices, int amount, Transaction.Mode action, Transaction.Type method) {
      this.db.cacheSingle(uuid.toString(), item.getItemPath(), (new Gson()).toJson(prices), amount, method, action);
   }

   public void log(UUID uuid, Map<ShopItem, Integer> items, Map<EcoType, Double> prices, int amount, Transaction.Mode action, Transaction.Type method) {
      String i = (new Gson()).toJson(items.entrySet().stream().map((x$0) -> {
         return new TransactionLog.Item(x$0);
      }).collect(Collectors.toList()));
      String p = (new Gson()).toJson(prices);
      this.db.cacheMultiple(uuid.toString(), i, p, amount, method, action);
   }

   public Collection<TransactionLog.SimpleTransaction> getTransactions(TransactionLog.TimeFrame timeFrame, Transaction.Mode mode) {
      if (timeFrame == TransactionLog.TimeFrame.MONTH) {
         return (Collection)this.TRANSACTION_CACHE.stream().filter((transaction) -> {
            return transaction.action == mode;
         }).collect(Collectors.toList());
      } else {
         long day = TimeUnit.DAYS.toMillis(1L);
         long min;
         long max;
         if (timeFrame == TransactionLog.TimeFrame.WEEK) {
            min = this.getStartOfWeek();
            max = this.getStartOfWeek() + day * 7L;
            return (Collection)this.TRANSACTION_CACHE.stream().filter((transaction) -> {
               return transaction.action == mode && transaction.date >= min && transaction.date <= max;
            }).collect(Collectors.toList());
         } else if (timeFrame == TransactionLog.TimeFrame.DAY) {
            min = this.getStartOfDay();
            max = this.getStartOfDay() + day;
            return (Collection)this.TRANSACTION_CACHE.stream().filter((transaction) -> {
               return transaction.action == mode && transaction.date >= min && transaction.date <= max;
            }).collect(Collectors.toList());
         } else {
            return null;
         }
      }
   }

   private long getStartOfWeek() {
      LocalDateTime current = LocalDateTime.now();
      LocalDateTime start = current.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
      Instant instant = ZonedDateTime.of(start, ZoneId.systemDefault()).toInstant();
      return instant.toEpochMilli();
   }

   private long getStartOfDay() {
      LocalDateTime current = LocalDateTime.now();
      LocalDateTime start = current.toLocalDate().atTime(LocalTime.MIN);
      Instant instant = start.atZone(ZoneId.systemDefault()).toInstant();
      return instant.toEpochMilli();
   }

   private void save() {
      if (!this.cache.isEmpty()) {
         try {
            Files.write(this.file.toPath(), this.cache, StandardOpenOption.APPEND);
            this.cache.clear();
         } catch (IOException var2) {
            SendMessage.errorMessage("Failed to create the transactions log file");
         }
      }

   }

   public void saveUnsaved() {
      if (this.db != null) {
         this.db.saveUnsaved();
      }

      this.save();
   }

   public void loadCache() {
      this.saveUnsaved();

      try {
         this.TRANSACTION_CACHE = this.db.getAllSimple();
      } catch (Exception var2) {
         SendMessage.warnMessage("A unknown error occurred while loading transaction cache from database:\n" + this.stackTraceToString(var2));
      }

   }

   public String exportLogs() {
      this.loadCache();
      ArrayList<TransactionLog.AdvancedTransaction> logs = this.plugin.getTransactionLog().retrieveAllTransactions();
      Gson gson = new Gson();
      StringBuilder generalStats = new StringBuilder();
      generalStats.append("{\"generalStats\":" + gson.toJson(new TransactionLog.GeneralStats(logs)) + "},");
      StringBuilder topPlayers = new StringBuilder();
      topPlayers.append("{\"topPlayers\":[{\"PBM\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.MONTH, Transaction.Mode.BUY)) + "},");
      topPlayers.append("{\"PBW\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.WEEK, Transaction.Mode.BUY)) + "},");
      topPlayers.append("{\"PBD\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.DAY, Transaction.Mode.BUY)) + "},");
      topPlayers.append("{\"PSM\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.MONTH, Transaction.Mode.SELL)) + "},");
      topPlayers.append("{\"PSW\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.WEEK, Transaction.Mode.SELL)) + "},");
      topPlayers.append("{\"PSD\":" + gson.toJson(this.getTopThreePlayers(TransactionLog.TimeFrame.DAY, Transaction.Mode.SELL)) + "}]},");
      StringBuilder topItems = new StringBuilder();
      topItems.append("{\"topItems\":[{\"IBM\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.MONTH, Transaction.Mode.BUY, true)) + "},");
      topItems.append("{\"IBW\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.WEEK, Transaction.Mode.BUY, true)) + "},");
      topItems.append("{\"IBD\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.DAY, Transaction.Mode.BUY, true)) + "},");
      topItems.append("{\"ISM\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.MONTH, Transaction.Mode.SELL, true)) + "},");
      topItems.append("{\"ISW\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.WEEK, Transaction.Mode.SELL, true)) + "},");
      topItems.append("{\"ISD\":" + gson.toJson(this.getTopThreeItems(TransactionLog.TimeFrame.DAY, Transaction.Mode.SELL, true)) + "}]}");
      String rawLogs = gson.toJson(logs, ArrayList.class);
      return "{\"stats\":[" + generalStats + topPlayers + topItems + "],\"dates\":" + Arrays.toString(this.db.getAllLoaded().stream().map((m) -> {
         return "\"" + m.toString() + "\"";
      }).toArray()) + ",\"logs\":" + rawLogs + "}";
   }

   private List<TransactionLog.SimplePrice> getCashFlow(ArrayList<TransactionLog.AdvancedTransaction> transactions, Transaction.Mode action) {
      Map<String, Double> sorted = (Map)transactions.stream().filter((trans) -> {
         return trans.action == action;
      }).flatMap((trans) -> {
         return trans.prices.stream();
      }).collect(Collectors.groupingBy(TransactionLog.Price::getEcoType, Collectors.summingDouble(TransactionLog.Price::getAmount)));
      List<TransactionLog.SimplePrice> prices = new ArrayList();
      Iterator var5 = sorted.keySet().iterator();

      while(var5.hasNext()) {
         String ecoType = (String)var5.next();
         String formatted = this.plugin.formatNullablePrice(ecoType, (Double)sorted.get(ecoType));
         if (formatted != null) {
            prices.add(new TransactionLog.SimplePrice(this.formatHTMLColors(formatted)));
         }
      }

      return prices;
   }

   public ArrayList<TransactionLog.TopPlayer> getTopThreePlayers(TransactionLog.TimeFrame timeFrame, Transaction.Mode mode) {
      Map<UUID, Integer> map = new HashMap();
      Iterator var4 = this.getTransactions(timeFrame, mode).iterator();

      while(var4.hasNext()) {
         TransactionLog.SimpleTransaction transaction = (TransactionLog.SimpleTransaction)var4.next();
         UUID uuid = transaction.uuid;
         int amount = transaction.amount;
         map.put(uuid, (Integer)map.getOrDefault(uuid, 0) + amount);
      }

      ArrayList<Entry<UUID, Integer>> entryList = new ArrayList(map.entrySet());
      entryList.sort(Entry.comparingByValue().reversed());
      ArrayList<TransactionLog.TopPlayer> topThree = new ArrayList();

      for(int i = 0; i < 3; ++i) {
         try {
            Entry<UUID, Integer> e = (Entry)entryList.get(i);
            topThree.add(new TransactionLog.TopPlayer((UUID)e.getKey(), (Integer)e.getValue()));
         } catch (IndexOutOfBoundsException var8) {
            break;
         }
      }

      return topThree;
   }

   public ArrayList<TransactionLog.TopItem> getTopThreeItems(TransactionLog.TimeFrame timeFrame, Transaction.Mode mode) {
      return this.getTopThreeItems(timeFrame, mode, false);
   }

   private ArrayList<TransactionLog.TopItem> getTopThreeItems(TransactionLog.TimeFrame timeFrame, Transaction.Mode mode, boolean html) {
      Map<String, Integer> map = new HashMap();
      Iterator var5 = this.getTransactions(timeFrame, mode).iterator();

      while(var5.hasNext()) {
         TransactionLog.SimpleTransaction transaction = (TransactionLog.SimpleTransaction)var5.next();
         String item = transaction.itemPath;
         int amount = transaction.amount;
         map.put(item, (Integer)map.getOrDefault(item, 0) + amount);
      }

      ArrayList<Entry<String, Integer>> entryList = new ArrayList(map.entrySet());
      entryList.sort(Entry.comparingByValue().reversed());
      ArrayList<TransactionLog.TopItem> topThree = new ArrayList();

      for(int i = 0; i < 3; ++i) {
         try {
            Entry<String, Integer> e = (Entry)entryList.get(i);
            topThree.add(new TransactionLog.TopItem((String)e.getKey(), (Integer)e.getValue(), html));
         } catch (IndexOutOfBoundsException var9) {
            break;
         }
      }

      return topThree;
   }

   public ArrayList<TransactionLog.AdvancedTransaction> retrieveAllTransactions() {
      ArrayList<TransactionLog.AdvancedTransaction> transactions = new ArrayList();
      Iterator var2 = this.db.getAllLoaded().iterator();

      while(var2.hasNext()) {
         TransactionLog.SimpleMonth date = (TransactionLog.SimpleMonth)var2.next();

         try {
            transactions.addAll(this.db.getAllAdvanced(date));
         } catch (Exception var5) {
            SendMessage.warnMessage("Failed to retrieve logged transactions from " + date.month + "/" + date.year);
            var5.printStackTrace();
         }
      }

      Collections.sort(transactions, new Comparator<TransactionLog.AdvancedTransaction>() {
         public int compare(TransactionLog.AdvancedTransaction first, TransactionLog.AdvancedTransaction second) {
            return Long.compare(second.date, first.date);
         }
      });
      return transactions;
   }

   private String stackTraceToString(Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return sw.toString();
   }

   private String formatHTMLColors(String s) {
      StringBuilder builder = new StringBuilder();
      TransactionLog.Line currentText = new TransactionLog.Line();
      TransactionLog.Color lastColor = new TransactionLog.Color();
      TransactionLog.Format lastFormat = new TransactionLog.Format();
      boolean var6 = false;

      try {
         for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == 167) {
               char id = s.charAt(i + 1);
               if (id == 'x' && s.charAt(i + 8) == 167) {
                  if (!lastColor.c.isEmpty()) {
                     if (!lastFormat.start.isEmpty()) {
                        builder.append(lastFormat.complete(currentText.get()));
                     } else {
                        builder.append(currentText.get());
                     }

                     builder.append(lastColor.get());
                  }

                  builder.append("<a style=\"color:" + this.getRGB(s, i) + "\">");
                  lastColor.c = "</a>";
                  i += 13;
               } else {
                  ChatColor color = ChatColor.getByChar(id);
                  if (color != null) {
                     if (color.isColor()) {
                        if (!lastColor.c.isEmpty()) {
                           if (!lastFormat.start.isEmpty()) {
                              builder.append(lastFormat.complete(currentText.get()));
                           } else {
                              builder.append(currentText.get());
                           }

                           builder.append(lastColor.get());
                        }

                        builder.append("<a style=\"color:" + color.name().toLowerCase(Locale.ROOT).replace("_", "") + "\">");
                        lastColor.c = "</a>";
                        ++i;
                     } else if (color.isFormat()) {
                        String f = this.getFormat(color);
                        if (f != null) {
                           if (!lastFormat.start.isEmpty()) {
                              builder.append(lastFormat.complete(currentText.get()));
                           } else {
                              builder.append(currentText.get());
                           }

                           lastFormat.start = lastFormat.start + "<" + f + ">";
                           lastFormat.ending = "</" + f + ">" + lastFormat.ending;
                           ++i;
                        } else {
                           currentText.text = currentText.text + c;
                        }
                     } else if (color == ChatColor.RESET) {
                        this.closeElement(builder, lastColor, lastFormat, currentText);
                        ++i;
                     }
                  }
               }
            } else {
               currentText.text = currentText.text + c;
            }
         }

         if (!lastFormat.start.isEmpty()) {
            builder.append(lastFormat.complete(currentText.get()));
         } else {
            builder.append(currentText.get());
         }

         if (!lastColor.c.isEmpty()) {
            builder.append(lastColor.get());
         }
      } catch (IndexOutOfBoundsException var12) {
      }

      return builder.toString();
   }

   private void closeElement(StringBuilder builder, TransactionLog.Color lastColor, TransactionLog.Format lastFormat, TransactionLog.Line currentText) {
      if (!lastColor.c.isEmpty()) {
         if (!lastFormat.start.isEmpty()) {
            builder.append(lastFormat.complete(currentText.get()));
         } else {
            builder.append(currentText.get());
         }

         builder.append(lastColor.get());
      } else {
         if (!lastFormat.start.isEmpty()) {
            builder.append(lastFormat.complete(currentText.get()));
         } else {
            builder.append(currentText.get());
         }

         builder.append(currentText.get());
      }

   }

   private String getFormat(ChatColor color) {
      switch(color) {
      case ITALIC:
         return "i";
      case BOLD:
         return "b";
      case STRIKETHROUGH:
         return "del";
      case UNDERLINE:
         return "u";
      default:
         return null;
      }
   }

   private String getRGB(String s, int i) {
      return s.substring(i, i + 14).replace("§", "").replace("x", "#");
   }

   private ArrayList<TransactionLog.Price> deserializePrices(String prices) {
      JsonObject obj = (JsonObject)(new Gson()).fromJson(prices, JsonObject.class);
      return (ArrayList)obj.entrySet().stream().map((e) -> {
         return new TransactionLog.Price((String)e.getKey(), Double.parseDouble(((JsonElement)e.getValue()).getAsString()));
      }).collect(Collectors.toCollection(ArrayList::new));
   }

   private List<TransactionLog.MultipleItem> deserializeItems(String items) {
      try {
         JSONArray list = JsonUtil.parseArray(items);
         return (List)list.stream().map((e) -> {
            try {
               JSONObject obj = JsonUtil.parseJson(e.toString());
               return new TransactionLog.MultipleItem(Integer.valueOf(obj.get("amount").toString()), obj.get("itemPath").toString());
            } catch (ParseException var4) {
               SendMessage.warnMessage("Failed to parse item '" + items + "' for transaction, skipping...");
               var4.printStackTrace();
               return new TransactionLog.MultipleItem(0, "unknown");
            }
         }).collect(Collectors.toList());
      } catch (ParseException var3) {
         SendMessage.errorMessage("Failed to parse item(s) '" + items + "' for transaction, skipping...");
         var3.printStackTrace();
         return null;
      }
   }

   private class Database {
      private Connection conn;
      private final String table;
      private final LinkedList<TransactionLog.Table> tables = new LinkedList();
      private final ArrayList<String> multiple_item_transactions = new ArrayList();
      private final ArrayList<String> single_item_transactions = new ArrayList();

      public List<TransactionLog.SimpleMonth> getAllLoaded() {
         return (List)this.tables.stream().map((t) -> {
            return t.date;
         }).collect(Collectors.toList());
      }

      public List<TransactionLog.SimpleMonth> getAllSince(TransactionLog.SimpleMonth since) {
         return (List)this.getAllLoaded().stream().filter((d) -> {
            return d.equals(since) || d.isLaterThen(since);
         }).collect(Collectors.toList());
      }

      public Database() throws IOException {
         Calendar date = Calendar.getInstance();
         this.table = "TRANSACTIONS_" + (date.get(2) + 1) + "_" + date.get(1);

         try {
            Files.createDirectory(Paths.get(TransactionLog.this.plugin.getDataFolder() + File.separator + "transactions"));
         } catch (FileAlreadyExistsException var4) {
         }

         this.connect();
      }

      public boolean connect() {
         try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + TransactionLog.this.plugin.getDataFolder() + File.separator + "transactions" + File.separator + "transactions.db");
            this.createTable();
            this.loadTables();
            this.checkTableConstraints();
            return true;
         } catch (Exception var2) {
            SendMessage.errorMessage("A error occurred while trying to connect to the transactions database");
            var2.printStackTrace();
            return false;
         }
      }

      private void checkTableConstraints() {
         try {
            boolean updated = false;
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("PRAGMA index_list('MULTIPLE_ITEM_" + this.table + "');");
            if (rs.next()) {
               stmt.executeUpdate("CREATE TABLE IF NOT EXISTS MULTIPLE_ITEM_" + this.table + "_NEW (date REAL NOT NULL,uuid TEXT NOT NULL,items TEXT NOT NULL,prices TEXT NOT NULL,amount INTEGER NOT NULL,action TEXT NOT NULL,method TEXT NOT NULL);");
               stmt.executeUpdate("INSERT INTO MULTIPLE_ITEM_" + this.table + "_NEW (date, uuid, items, prices, amount, action, method) SELECT date, uuid, items, prices, amount, action, method FROM MULTIPLE_ITEM_" + this.table + ";");
               stmt.executeUpdate("DROP TABLE MULTIPLE_ITEM_" + this.table + ";");
               stmt.executeUpdate("ALTER TABLE MULTIPLE_ITEM_" + this.table + "_NEW RENAME TO MULTIPLE_ITEM_" + this.table + ";");
               updated = true;
            }

            ResultSet rs2 = stmt.executeQuery("PRAGMA index_list('SINGLE_ITEM_" + this.table + "');");
            if (rs2.next()) {
               stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SINGLE_ITEM_" + this.table + "_NEW (date REAL NOT NULL,uuid TEXT NOT NULL,item TEXT NOT NULL,prices TEXT NOT NULL,amount INTEGER NOT NULL,action TEXT NOT NULL,method TEXT NOT NULL);");
               stmt.executeUpdate("INSERT INTO SINGLE_ITEM_" + this.table + "_NEW (date, uuid, item, prices, amount, action, method) SELECT date, uuid, item, prices, amount, action, method FROM SINGLE_ITEM_" + this.table + ";");
               stmt.executeUpdate("DROP TABLE SINGLE_ITEM_" + this.table + ";");
               stmt.executeUpdate("ALTER TABLE SINGLE_ITEM_" + this.table + "_NEW RENAME TO SINGLE_ITEM_" + this.table + ";");
               updated = true;
            }

            if (updated) {
               SendMessage.infoMessage("Successfully removed unique constraint from transaction db tables");
            }
         } catch (SQLException var5) {
            SendMessage.warnMessage("Failed to migrate database to new format for " + this.table);
            var5.printStackTrace();
         }

      }

      private void createTable() {
         try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS MULTIPLE_ITEM_" + this.table + " (date REAL NOT NULL,uuid TEXT NOT NULL,items TEXT NOT NULL,prices TEXT NOT NULL,amount INTEGER NOT NULL,action TEXT NOT NULL,method TEXT NOT NULL);");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SINGLE_ITEM_" + this.table + " (date REAL NOT NULL,uuid TEXT NOT NULL,item TEXT NOT NULL,prices TEXT NOT NULL,amount INTEGER NOT NULL,action TEXT NOT NULL,method TEXT NOT NULL);");
            stmt.close();
         } catch (SQLException var2) {
            var2.printStackTrace();
         }

      }

      private void loadTables() {
         HashSet tables = new HashSet();

         try {
            Statement stmt = this.conn.createStatement();

            try {
               ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");

               try {
                  while(rs.next()) {
                     tables.add(rs.getString(1));
                  }
               } catch (Throwable var9) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var10) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (SQLException var11) {
            SendMessage.errorMessage("Failed to load transaction tables");
            var11.printStackTrace();
         }

         Iterator var12 = tables.iterator();

         while(var12.hasNext()) {
            String s = (String)var12.next();

            try {
               TransactionLog.SimpleMonth date = TransactionLog.this.new SimpleMonth(s);
               TransactionLog.Table table = (TransactionLog.Table)this.tables.stream().filter((t) -> {
                  return t.date.equals(date);
               }).findFirst().orElse(TransactionLog.this.new Table(date));
               if (s.contains("MULTIPLE")) {
                  table.multiple_items = s;
               } else if (s.contains("SINGLE")) {
                  table.single_items = s;
               }

               this.tables.remove(table);
               this.tables.add(table);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException var6) {
            }
         }

         Collections.sort(this.tables, new Comparator<TransactionLog.Table>() {
            public int compare(TransactionLog.Table first, TransactionLog.Table second) {
               return first.date.compareTo(second.date);
            }
         });
      }

      private void executeBatch(List<String> queries) {
         try {
            Statement stmt = this.conn.createStatement();
            Iterator var3 = (new ArrayList(queries)).iterator();

            while(var3.hasNext()) {
               String query = (String)var3.next();
               stmt.executeUpdate(query);
            }

            queries.clear();
            stmt.close();
         } catch (SQLException var5) {
            var5.printStackTrace();
         }

      }

      private void cacheMultiple(String uuid, String items, String prices, int amount, Transaction.Type method, Transaction.Mode action) {
         this.multiple_item_transactions.add(String.format("INSERT INTO %1$s VALUES(%2$d,'%3$s','%4$s','%5$s',%6$d,'%7$s','%8$s')", "MULTIPLE_ITEM_" + this.table, Instant.now().toEpochMilli(), uuid, items, prices, amount, action.name(), method.name()));
         if (this.multiple_item_transactions.size() == 10) {
            TransactionLog.this.plugin.runTaskAsync(() -> {
               this.executeBatch(this.multiple_item_transactions);
            });
         }

      }

      private void cacheSingle(String uuid, String item, String prices, int amount, Transaction.Type method, Transaction.Mode action) {
         this.single_item_transactions.add(String.format("INSERT INTO %1$s VALUES(%2$d,'%3$s','%4$s','%5$s',%6$d,'%7$s','%8$s')", "SINGLE_ITEM_" + this.table, Instant.now().toEpochMilli(), uuid, item, prices, amount, action.name(), method.name()));
         if (this.single_item_transactions.size() == 10) {
            TransactionLog.this.plugin.runTaskAsync(() -> {
               this.executeBatch(this.single_item_transactions);
            });
         }

      }

      private ArrayList<TransactionLog.SimpleTransaction> getAllSimple() throws Exception {
         ArrayList<TransactionLog.SimpleTransaction> CACHE = new ArrayList();
         Statement stmt = null;
         ResultSet rs = null;

         try {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM SINGLE_ITEM_" + this.table + ";");

            while(rs.next()) {
               CACHE.add(TransactionLog.this.new SimpleTransaction(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(5), rs.getString(6)));
            }

            rs = stmt.executeQuery("SELECT * FROM MULTIPLE_ITEM_" + this.table + ";");

            while(rs.next()) {
               long date = rs.getLong(1);
               String uuid = rs.getString(2);
               String action = rs.getString(6);
               TransactionLog.Item[] var8 = (TransactionLog.Item[])(new Gson()).fromJson(rs.getString(3), TransactionLog.Item[].class);
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  TransactionLog.Item item = var8[var10];
                  CACHE.add(TransactionLog.this.new SimpleTransaction(date, uuid, item.itemPath, item.amount, action));
               }
            }

            return CACHE;
         } finally {
            if (rs != null) {
               rs.close();
            }

            if (stmt != null) {
               stmt.close();
            }

         }
      }

      private TransactionLog.Table getTableFrom(TransactionLog.SimpleMonth date) throws NoSuchElementException {
         return (TransactionLog.Table)this.tables.stream().filter((t) -> {
            return t.date.equals(date);
         }).findFirst().get();
      }

      private ArrayList<TransactionLog.AdvancedTransaction> getAllAdvanced(TransactionLog.SimpleMonth timestamp) throws Exception {
         TransactionLog.Table table = this.getTableFrom(timestamp);
         ArrayList<TransactionLog.AdvancedTransaction> CACHE = new ArrayList();
         Statement stmt = null;
         ResultSet rs = null;

         try {
            stmt = this.conn.createStatement();
            if (table.single_items != null) {
               rs = stmt.executeQuery("SELECT * FROM " + table.single_items + ";");

               while(rs.next()) {
                  CACHE.add(TransactionLog.this.new AdvancedTransaction(rs.getLong(1), rs.getString(2), TransactionLog.this.new SingleItem(rs.getString(3), true), TransactionLog.this.deserializePrices(rs.getString(4)), rs.getInt(5), rs.getString(6), rs.getString(7)));
               }
            }

            if (table.multiple_items != null) {
               rs = stmt.executeQuery("SELECT * FROM " + table.multiple_items + ";");

               while(rs.next()) {
                  CACHE.add(TransactionLog.this.new AdvancedTransaction(rs.getLong(1), rs.getString(2), TransactionLog.this.deserializeItems(rs.getString(3)), TransactionLog.this.deserializePrices(rs.getString(4)), rs.getInt(5), rs.getString(6), rs.getString(7)));
               }
            }
         } finally {
            if (rs != null) {
               rs.close();
            }

            if (stmt != null) {
               stmt.close();
            }

         }

         return CACHE;
      }

      public void saveUnsaved() {
         if (!this.multiple_item_transactions.isEmpty()) {
            this.executeBatch(this.multiple_item_transactions);
         }

         if (!this.single_item_transactions.isEmpty()) {
            this.executeBatch(this.single_item_transactions);
         }

      }
   }

   public static enum TimeFrame {
      DAY,
      WEEK,
      MONTH;

      // $FF: synthetic method
      private static TransactionLog.TimeFrame[] $values() {
         return new TransactionLog.TimeFrame[]{DAY, WEEK, MONTH};
      }
   }

   public final class GeneralStats {
      int transactions;
      List<TransactionLog.SimplePrice> cashIn;
      List<TransactionLog.SimplePrice> cashOut;

      public GeneralStats(ArrayList<TransactionLog.AdvancedTransaction> param2) {
         this.transactions = logs.size();
         this.cashIn = TransactionLog.this.getCashFlow(logs, Transaction.Mode.BUY);
         this.cashOut = TransactionLog.this.getCashFlow(logs, Transaction.Mode.SELL);
      }
   }

   private final class SimplePrice {
      String formatted;

      public SimplePrice(String param2) {
         this.formatted = formatted;
      }
   }

   public final class SimpleTransaction {
      public final long date;
      public final UUID uuid;
      public final int amount;
      public final String itemPath;
      public final Transaction.Mode action;

      public SimpleTransaction(long param2, String param4, String param5, int param6, String param7) {
         this.date = date;
         this.uuid = UUID.fromString(uuid);
         this.itemPath = itemPath;
         this.amount = amount;
         this.action = Transaction.Mode.valueOf(action);
      }
   }

   public class TopPlayer {
      public final TransactionLog.Player player;
      public final int amount;

      public TopPlayer(UUID param2, int param3) {
         this.player = TransactionLog.this.new Player(uuid);
         this.amount = amount;
      }
   }

   public class TopItem {
      public final TransactionLog.SingleItem item;
      public final int amount;

      public TopItem(String param2, int param3, boolean param4) {
         this.item = TransactionLog.this.new SingleItem(item, html);
         this.amount = amount;
      }
   }

   public class SimpleMonth implements Comparable<TransactionLog.SimpleMonth> {
      private final int month;
      private final int year;

      public SimpleMonth(String param2) throws NumberFormatException {
         String date = table.split("_ITEM_TRANSACTIONS_")[1];
         this.month = Integer.parseInt(date.split("_")[0]);
         this.year = Integer.parseInt(date.split("_")[1]);
      }

      public boolean isLaterThen(TransactionLog.SimpleMonth date) {
         return this.year > date.year || this.year == date.year && this.month > date.month;
      }

      public boolean isOlderThen(TransactionLog.SimpleMonth date) {
         return this.year < date.year || this.year == date.year && this.month < date.month;
      }

      public int compareTo(@NotNull TransactionLog.SimpleMonth o) {
         return this.isLaterThen(o) ? 1 : (this.isOlderThen(o) ? -1 : 0);
      }

      public String toString() {
         return this.month + "/" + this.year;
      }

      public boolean equals(Object object) {
         if (this == object) {
            return true;
         } else if (object != null && this.getClass() == object.getClass()) {
            TransactionLog.SimpleMonth date = (TransactionLog.SimpleMonth)object;
            return this.month == date.month && this.year == date.year;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.month, this.year});
      }
   }

   private final class Line {
      String text = "";

      public Line() {
      }

      public String get() {
         String s = this.text;
         this.text = "";
         return s;
      }
   }

   private final class Color {
      String c = "";

      public Color() {
      }

      public String get() {
         String s = this.c;
         this.c = "";
         return s;
      }

      public void addColor(String c) {
         this.c = c + this.c;
      }
   }

   private final class Format {
      String start = "";
      String ending = "";

      public Format() {
      }

      public String complete(String currentText) {
         String s = this.start + currentText + this.ending;
         this.start = "";
         this.ending = "";
         return s;
      }
   }

   public final class MultipleItem {
      int amount;
      String name;
      String item;
      Material mat;

      public MultipleItem(int param2, String param3) {
         this.amount = amount;

         try {
            ShopItem shopItem = TransactionLog.this.plugin.getShopItem(item);
            this.name = TransactionLog.this.formatHTMLColors(shopItem.getDisplayname());
            this.mat = shopItem.getItemToGive().getType();
         } catch (NullPointerException var5) {
         }

         this.item = item;
      }
   }

   public final class Price {
      String formatted;
      String ecoType;
      double amount;

      public Price(String param2, Double param3) {
         try {
            this.formatted = TransactionLog.this.formatHTMLColors(TransactionLog.this.plugin.getEcoHandler().getLoaded(ecoType).formatPrice(amount));
         } catch (NullPointerException var5) {
            this.formatted = amount + " " + ecoType;
         }

         this.amount = amount;
         this.ecoType = ecoType;
      }

      public String getEcoType() {
         return this.ecoType;
      }

      public double getAmount() {
         return this.amount;
      }
   }

   public final class AdvancedTransaction {
      public final long date;
      public final TransactionLog.Player player;
      public final Object items;
      public final ArrayList<TransactionLog.Price> prices;
      public final int amount;
      public final Transaction.Mode action;
      public final Transaction.Type type;

      public AdvancedTransaction(long param2, String param4, Object param5, ArrayList<TransactionLog.Price> param6, int param7, String param8, String param9) {
         this.date = date;
         this.player = TransactionLog.this.new Player(UUID.fromString(uuid));
         this.items = items;
         this.prices = prices;
         this.amount = amount;
         this.action = Transaction.Mode.valueOf(mode);
         this.type = Transaction.Type.valueOf(type);
      }
   }

   private class Item {
      private int amount;
      private String itemPath;

      public Item(Entry<ShopItem, Integer> param2) {
         this.amount = (Integer)e.getValue();
         this.itemPath = ((ShopItem)e.getKey()).getItemPath();
      }
   }

   public class Table {
      public TransactionLog.SimpleMonth date;
      public String single_items;
      public String multiple_items;

      public Table(TransactionLog.SimpleMonth param2) {
         this.date = date;
      }

      public TransactionLog.Table withSingle(String single_items) {
         this.single_items = single_items;
         return this;
      }

      public TransactionLog.Table withMultiple(String multiple_items) {
         this.multiple_items = multiple_items;
         return this;
      }
   }

   public final class SingleItem {
      public String name;
      String item;
      Material mat;

      public SingleItem(String param2, boolean param3) {
         try {
            ShopItem shopItem = TransactionLog.this.plugin.getShopItem(item);
            this.name = html ? TransactionLog.this.formatHTMLColors(shopItem.getDisplayname()) : shopItem.getDisplayname();
            this.mat = shopItem.getItemToGive().getType();
         } catch (NullPointerException var5) {
         }

         this.item = item;
      }
   }

   public final class Player {
      public String name;
      UUID uuid;

      public Player(UUID param2) {
         try {
            this.name = Bukkit.getOfflinePlayer(uuid).getName();
         } catch (NullPointerException var4) {
            this.name = "Unknown";
         }

         this.uuid = uuid;
      }
   }
}
