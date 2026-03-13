package tntrun.utils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.messages.Messages;

public class Stats {
   private TNTRun plugin;
   private File file;
   private String lbcolour;
   private String lbentry;
   private String lbrank;
   private String lbplaceholdervalue;
   private static Map<String, Integer> pmap = new HashMap();
   private static Map<String, Integer> wmap = new HashMap();
   private static Map<String, Integer> lmap = new HashMap();
   private static Map<String, Integer> smap = new HashMap();
   private static List<String> sortedPlayed = new ArrayList();
   private static List<String> sortedWins = new ArrayList();
   private static List<String> sortedLosses = new ArrayList();

   public Stats(TNTRun plugin) {
      this.plugin = plugin;
      this.file = new File(plugin.getDataFolder(), "stats.yml");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      this.loadStats();
   }

   private void loadStats() {
      if (this.plugin.isFile()) {
         this.getStatsFromFile();
      } else {
         final String table = this.plugin.getConfig().getString("MySQL.table");
         if (this.plugin.getMysql().isConnected()) {
            this.getStatsFromDB(table);
         } else {
            (new BukkitRunnable() {
               public void run() {
                  if (Stats.this.plugin.getMysql().isConnected()) {
                     Stats.this.getStatsFromDB(table);
                  } else {
                     Stats.this.plugin.setUseStats(false);
                     Stats.this.plugin.getLogger().info("Failure connecting to MySQL database, disabling stats");
                  }

               }
            }).runTaskLaterAsynchronously(this.plugin, 60L);
         }
      }
   }

   public void addPlayedGames(Player player, int value) {
      String uuid = this.getPlayerUUID(player);
      if (pmap.containsKey(uuid)) {
         pmap.put(uuid, (Integer)pmap.get(uuid) + value);
      } else {
         pmap.put(uuid, value);
      }

      this.saveStats(uuid, "played");
   }

   public void addWins(Player player, int value) {
      String uuid = this.getPlayerUUID(player);
      if (wmap.containsKey(uuid)) {
         wmap.put(uuid, (Integer)wmap.get(uuid) + value);
      } else {
         wmap.put(uuid, value);
      }

      this.saveStats(uuid, "wins");
      sortedWins.clear();
      sortedLosses.clear();
   }

   public int getLosses(String uuid) {
      return this.getPlayedGames(uuid) - this.getWins(uuid);
   }

   public int getPlayedGames(String uuid) {
      return pmap.containsKey(uuid) ? (Integer)pmap.get(uuid) : 0;
   }

   public int getWins(String uuid) {
      return wmap.containsKey(uuid) ? (Integer)wmap.get(uuid) : 0;
   }

   public int getStreak(String uuid) {
      return smap.containsKey(uuid) ? (Integer)smap.get(uuid) : 0;
   }

   public void getLeaderboard(CommandSender sender, int entries) {
      String type = "wins";
      this.getWorkingList("wins").stream().limit((long)entries).forEach((uuid) -> {
         if (this.plugin.useUuid()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            this.lbentry = player.getName();
            this.lbrank = Utils.getRank(player);
            this.lbcolour = Utils.getColourMeta(player);
         } else {
            this.lbentry = uuid;
            this.lbrank = Utils.getRank((OfflinePlayer)Bukkit.getPlayer(uuid));
            this.lbcolour = Utils.getColourMeta((OfflinePlayer)Bukkit.getPlayer(uuid));
         }

         Messages.sendMessage(sender, Messages.leaderboard.replace("{POSITION}", String.valueOf(this.getPosition(uuid, "wins"))).replace("{PLAYER}", this.lbentry).replace("{RANK}", this.lbrank).replace("{COLOR}", this.lbcolour).replace("{WINS}", String.valueOf(wmap.get(uuid))), false);
      });
   }

   private boolean isValidUuid(String uuid) {
      try {
         UUID.fromString(uuid);
         return true;
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }

   private boolean isKnownPlayer(String identity) {
      OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(identity));
      return player.hasPlayedBefore();
   }

   private void getStatsFromFile() {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);
      ConfigurationSection stats = config.getConfigurationSection("stats");
      if (stats != null) {
         Iterator var3;
         String uuid;
         if (this.plugin.useUuid()) {
            var3 = stats.getKeys(false).iterator();

            while(var3.hasNext()) {
               uuid = (String)var3.next();
               if (this.isValidUuid(uuid) && this.isKnownPlayer(uuid)) {
                  wmap.put(uuid, config.getInt("stats." + uuid + ".wins", 0));
                  pmap.put(uuid, config.getInt("stats." + uuid + ".played", 0));
               }
            }
         } else {
            var3 = stats.getKeys(false).iterator();

            while(var3.hasNext()) {
               uuid = (String)var3.next();
               if (!this.isValidUuid(uuid)) {
                  wmap.put(uuid, config.getInt("stats." + uuid + ".wins", 0));
                  pmap.put(uuid, config.getInt("stats." + uuid + ".played", 0));
               }
            }
         }
      }

   }

   private void getStatsFromDB(String table) {
      Stream.of("wins", "played", "streak").forEach((stat) -> {
         HashMap workingMap = new HashMap();

         try {
            ResultSet rs = this.plugin.getMysql().query("SELECT * FROM `" + table + "` ORDER BY " + stat + " DESC LIMIT 99999").getResultSet();

            while(true) {
               String playerName;
               while(true) {
                  if (!rs.next()) {
                     if (stat.equalsIgnoreCase("wins")) {
                        wmap.putAll(workingMap);
                        return;
                     } else {
                        if (stat.equalsIgnoreCase("played")) {
                           pmap.putAll(workingMap);
                        } else {
                           smap.putAll(workingMap);
                        }

                        return;
                     }
                  }

                  playerName = rs.getString("username");
                  if (this.plugin.useUuid()) {
                     if (this.isValidUuid(playerName) && this.isKnownPlayer(playerName)) {
                        break;
                     }
                  } else {
                     if (this.isValidUuid(playerName)) {
                        continue;
                     }
                     break;
                  }
               }

               workingMap.put(playerName, rs.getInt(stat));
            }
         } catch (SQLException var6) {
            var6.printStackTrace();
         }
      });
   }

   public Map<String, Integer> getWinMap() {
      return wmap;
   }

   private void saveStats(String uuid, String statname) {
      if (this.plugin.isFile()) {
         this.saveStatsToFile(uuid, statname);
      } else {
         this.saveStatsToDB(uuid, statname);
      }
   }

   private void saveStatsToFile(String uuid, String statname) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);
      if (statname.equalsIgnoreCase("played")) {
         config.set("stats." + uuid + ".played", pmap.get(uuid));
      } else if (statname.equalsIgnoreCase("wins")) {
         config.set("stats." + uuid + ".wins", wmap.get(uuid));
      } else {
         config.set("stats." + uuid, (Object)null);
      }

      try {
         config.save(this.file);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void addStreakToDB(OfflinePlayer player, int value) {
      String uuid = this.getPlayerUUID(player);
      smap.put(uuid, value);
      this.saveStatsToDB(uuid, "streak");
   }

   private void saveStatsToDB(String uuid, String statname) {
      if (statname.equalsIgnoreCase("played") || statname.equalsIgnoreCase("reset")) {
         this.updateDB("played", uuid, (Integer)pmap.getOrDefault(uuid, 0));
      }

      if (statname.equalsIgnoreCase("wins") || statname.equalsIgnoreCase("reset")) {
         this.updateDB("wins", uuid, (Integer)wmap.getOrDefault(uuid, 0));
      }

      if (statname.equalsIgnoreCase("streak") || statname.equalsIgnoreCase("reset")) {
         this.updateDB("streak", uuid, (Integer)smap.getOrDefault(uuid, 0));
      }

   }

   private void updateDB(final String statname, final String player, final Integer value) {
      final String table = this.plugin.getConfig().getString("MySQL.table");
      (new BukkitRunnable() {
         public void run() {
            Stats.this.plugin.getMysql().query("UPDATE `" + table + "` SET `" + statname + "`='" + value + "' WHERE `username`='" + player + "';");
         }
      }).runTaskAsynchronously(this.plugin);
   }

   public String getPlayerUUID(OfflinePlayer player) {
      return this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
   }

   public String getLeaderboardPosition(int position, String type, String item) {
      List<String> workingList = this.getWorkingList(type);
      if (position > workingList.size()) {
         return "";
      } else {
         String uuid = (String)workingList.get(position - 1);
         if (item.equalsIgnoreCase("score")) {
            if (type.equalsIgnoreCase("wins")) {
               this.lbplaceholdervalue = String.valueOf(wmap.get(uuid));
            } else if (type.equalsIgnoreCase("played")) {
               this.lbplaceholdervalue = String.valueOf(pmap.get(uuid));
            } else {
               this.lbplaceholdervalue = String.valueOf(lmap.get(uuid));
            }
         } else if (this.plugin.useUuid()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            this.lbplaceholdervalue = item.equalsIgnoreCase("player") ? p.getName() : Utils.getRank(p);
         } else {
            this.lbplaceholdervalue = item.equalsIgnoreCase("player") ? uuid : Utils.getRank((OfflinePlayer)Bukkit.getPlayer(uuid));
         }

         return this.lbplaceholdervalue != null ? this.lbplaceholdervalue : "";
      }
   }

   private List<String> getWorkingList(String type) {
      String var2 = type.toLowerCase();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1096968431:
         if (var2.equals("losses")) {
            var3 = 2;
         }
         break;
      case -985752877:
         if (var2.equals("played")) {
            var3 = 1;
         }
         break;
      case 3649559:
         if (var2.equals("wins")) {
            var3 = 0;
         }
      }

      List var10000;
      switch(var3) {
      case 0:
         var10000 = this.getSortedWins();
         break;
      case 1:
         var10000 = this.getSortedPlayed();
         break;
      case 2:
         var10000 = this.getSortedLosses();
         break;
      default:
         var10000 = List.of();
      }

      return var10000;
   }

   private List<String> getSortedWins() {
      return sortedWins.isEmpty() ? this.createSortedList(wmap) : sortedWins;
   }

   private List<String> getSortedPlayed() {
      return sortedPlayed.isEmpty() ? this.createSortedList(pmap) : sortedPlayed;
   }

   private List<String> getSortedLosses() {
      return sortedLosses.isEmpty() ? this.createSortedList(this.getLossMap()) : sortedLosses;
   }

   private List<String> createSortedList(Map<String, Integer> map) {
      List<String> sorted = new ArrayList();
      map.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).forEach((e) -> {
         sorted.add((String)e.getKey());
      });
      return sorted;
   }

   private Map<String, Integer> getLossMap() {
      sortedLosses.clear();
      pmap.entrySet().forEach((e) -> {
         int wins = 0;
         if (wmap.containsKey(e.getKey())) {
            wins = (Integer)wmap.get(e.getKey());
         }

         lmap.put((String)e.getKey(), (Integer)e.getValue() - wins);
      });
      return lmap;
   }

   public boolean hasDatabaseEntry(OfflinePlayer player) {
      return pmap.containsKey(this.getPlayerUUID(player));
   }

   public void resetStats(String uuid) {
      pmap.remove(uuid);
      wmap.remove(uuid);
      lmap.remove(uuid);
      smap.remove(uuid);
      sortedWins.remove(uuid);
      sortedPlayed.remove(uuid);
      sortedLosses.remove(uuid);
      this.saveStats(uuid, "reset");
   }

   public boolean hasStats(String uuid) {
      return pmap.containsKey(uuid) || wmap.containsKey(uuid);
   }

   public void clearPlayedList() {
      sortedPlayed.clear();
      sortedLosses.clear();
   }

   public int getPosition(String uuid, String type) {
      return this.getWorkingList(type).indexOf(uuid) + 1;
   }
}
