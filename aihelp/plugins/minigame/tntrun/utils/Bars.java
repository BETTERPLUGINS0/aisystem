package tntrun.utils;

import com.google.common.base.Enums;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class Bars {
   private static HashMap<String, BossBar> barmap = new HashMap();
   public static String waiting = "&6Minimum players:&r {MIN}&6, current player count:&r {COUNT}";
   public static String starting = "&6Arena starts in:&r {SECONDS} seconds";
   public static String playing = "&6Time left:&r {SECONDS} &6Players in game count:&r {COUNT}";

   public static void createBar(String arena) {
      BossBar bar = Bukkit.createBossBar((String)null, getBarColor(), BarStyle.SOLID, new BarFlag[0]);
      barmap.put(arena, bar);
   }

   private static BarColor getBarColor() {
      int index = false;
      String col = TNTRun.getInstance().getConfig().getString("special.BossBarColor");
      int index;
      if (col != null && !col.equalsIgnoreCase("RANDOM") && Enums.getIfPresent(BarColor.class, col).orNull() != null) {
         index = Arrays.asList(BarColor.values()).indexOf(BarColor.valueOf(col));
      } else {
         Random random = ThreadLocalRandom.current();
         index = random.nextInt(BarColor.values().length);
      }

      return BarColor.values()[index];
   }

   private static void setBarColor(String arena) {
      ((BossBar)barmap.get(arena)).setColor(getBarColor());
   }

   public static void addPlayerToBar(Player player, String arena) {
      ((BossBar)barmap.get(arena)).addPlayer(player);
      if (((BossBar)barmap.get(arena)).getPlayers().size() == 1) {
         setBarColor(arena);
      }

   }

   public static void setBar(Arena arena, String message, int count, int seconds, double progress, TNTRun plugin) {
      if (plugin.getConfig().getBoolean("special.UseBossBar")) {
         message = message.replace("{COUNT}", String.valueOf(count));
         message = message.replace("{MIN}", String.valueOf(arena.getStructureManager().getMinPlayers()));
         message = message.replace("{SECONDS}", String.valueOf(seconds));
         message = FormattingCodesParser.parseFormattingCodes(message);
         ((BossBar)barmap.get(arena.getArenaName())).setTitle(message);
         ((BossBar)barmap.get(arena.getArenaName())).setProgress(progress);
      }
   }

   public static void removeBar(Player player, String arena) {
      ((BossBar)barmap.get(arena)).removePlayer(player);
   }

   public static void removeAll(String arena) {
      if (barmap.containsKey(arena)) {
         ((BossBar)barmap.get(arena)).removeAll();
      }

   }

   public static void loadBars(TNTRun plugin) {
      File messageconfig = new File(plugin.getDataFolder(), "configbars.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(messageconfig);
      waiting = config.getString("waiting", waiting);
      starting = config.getString("starting", starting);
      playing = config.getString("playing", playing);
      saveBars(messageconfig);
   }

   private static void saveBars(File messageconfig) {
      FileConfiguration config = new YamlConfiguration();
      config.set("waiting", waiting);
      config.set("starting", starting);
      config.set("playing", playing);

      try {
         config.save(messageconfig);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
