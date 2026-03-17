package advancedplugins.pm2.cv.api.configuration;

import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import advancedplugins.pm2.cv.api.util.MathUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class LeaderboardGuiConfiguration {
   public static String TITLE;
   public static int ROWS;
   public static GuiConfiguration.IndexedItem EXIT_ITEM;
   public static GuiConfiguration.IndexedItem PLAYER_HEAD_ITEM;
   public static List<Integer> SLOTS;
   public static GuiConfiguration.IndexedItem NEXT_ITEM;
   public static GuiConfiguration.IndexedItem PREVIOUS_ITEM;
   public static final String DEFAULT_TITLE = "&6{name}'s Leaderboard &7(Page: &8{page}&7)";
   public static final int DEFAULT_ROWS = 6;
   @NotNull
   public static final GuiConfiguration.IndexedItem DEFAULT_EXIT_ITEM;
   @NotNull
   public static final GuiConfiguration.IndexedItem DEFAULT_PLAYER_HEAD_ITEM;
   @NotNull
   public static final GuiConfiguration.IndexedItem DEFAULT_NEXT_ITEM;
   @NotNull
   public static final GuiConfiguration.IndexedItem DEFAULT_PREVIOUS_ITEM;
   public static final List<Integer> DEFAULT_SLOTS = new ArrayList(MathUtil.range(0, 45));

   public static void load(@NotNull InfiniteVehiclesPluginBase var0) {
      File var1 = new File(var0.getDataFolder(), "LeaderboardGuiConfiguration.yml");
      boolean var2 = !var1.exists();
      if (!var1.exists()) {
         var1.getParentFile().mkdirs();

         try {
            Files.createFile(var1.toPath());
         } catch (IOException var11) {
            throw new IllegalStateException("couldn't generate leaderboard configuration file", var11);
         }
      }

      YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var1);
      if (var2) {
         var3.set("title", "&6{name}'s Leaderboard &7(Page: &8{page}&7)");
         var3.set("rows", 6);
         writeSlots(var3);
         DEFAULT_EXIT_ITEM.write(var3.createSection("exit-item"));
         DEFAULT_PLAYER_HEAD_ITEM.write(var3.createSection("player-head-item"));
         DEFAULT_NEXT_ITEM.write(var3.createSection("next-item"));
         DEFAULT_PREVIOUS_ITEM.write(var3.createSection("previous-item"));

         try {
            var3.save(var1);
         } catch (IOException var10) {
            throw new IllegalStateException("couldn't save leaderboard configuration file", var10);
         }
      }

      TITLE = var3.getString("title");
      ROWS = MathUtil.clamp(var3.getInt("rows"), 1, 6);
      loadSlots(var3);
      ConfigurationSection var4 = var3.getConfigurationSection("exit-item");
      ConfigurationSection var5 = var3.getConfigurationSection("player-head-item");
      ConfigurationSection var6 = var3.getConfigurationSection("next-item");
      ConfigurationSection var7 = var3.getConfigurationSection("previous-item");
      EXIT_ITEM = var4 == null ? null : new GuiConfiguration.IndexedItem(var4);
      NEXT_ITEM = var6 == null ? null : new GuiConfiguration.IndexedItem(var6);
      PREVIOUS_ITEM = var7 == null ? null : new GuiConfiguration.IndexedItem(var7);

      try {
         PLAYER_HEAD_ITEM = var5 == null ? null : new GuiConfiguration.IndexedItem(var5);
      } catch (InvalidConfigurationException var9) {
      }

   }

   private static void writeSlots(YamlConfiguration var0) {
      Stream var10001 = DEFAULT_SLOTS.stream();
      String var1 = "[" + String.join(",", var10001.map(Object::toString).toList()) + "]";
      var0.set("slots", var1);
   }

   private static void loadSlots(YamlConfiguration var0) {
      List var1 = var0.getIntegerList("slots");
      if (var1.isEmpty() && var0.contains("slots")) {
         String var2 = var0.getString("slots");
         boolean var3 = var2 != null && var2.startsWith("[") && var2.endsWith("]");
         if (var3) {
            var2 = var2.replace("[", "").replace("]", "").replace(" ", "");
            SLOTS = Arrays.stream(var2.split(",")).filter(NumberUtils::isNumber).map(Integer::parseInt).toList();
            return;
         }
      }

      SLOTS = var1;
   }

   static {
      DEFAULT_EXIT_ITEM = new GuiConfiguration.IndexedItem(53, Material.ARROW, "&6Close", List.of("&7Exit the menu."), (Integer)null, (String)null);
      DEFAULT_PLAYER_HEAD_ITEM = new GuiConfiguration.IndexedItem(1, Material.PLAYER_HEAD, "&6{player_name} - {player_kills}", new ArrayList(), (Integer)null, (String)null);
      DEFAULT_NEXT_ITEM = new GuiConfiguration.IndexedItem(48, Material.ARROW, "&6Next", List.of("&7Go to the next page."), (Integer)null, (String)null);
      DEFAULT_PREVIOUS_ITEM = new GuiConfiguration.IndexedItem(50, Material.ARROW, "&6Previous", List.of("&7Go to the previous page."), (Integer)null, (String)null);
   }
}
