package advancedplugins.pm2.cv.util;

import advancedplugins.pm2.cv.InfiniteVehiclesPlugin;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LeaderboardUtil {
   private static final String KILLS_KEY = "kills_";

   public static void addKills(Player player, Vehicle vehicle, int amount) {
      World var3 = var0.getWorld();
      PersistentDataContainer var4 = var3.getPersistentDataContainer();
      NamespacedKey var5 = getKey(var0, var1.getConfiguration().getId());
      int var6 = (Integer)var4.getOrDefault(var5, PersistentDataType.INTEGER, 0);
      var4.set(var5, PersistentDataType.INTEGER, var6 + var2);
   }

   public static int getKills(Player player, Vehicle vehicle) {
      World var2 = var0.getWorld();
      PersistentDataContainer var3 = var2.getPersistentDataContainer();
      NamespacedKey var4 = getKey(var0, var1.getConfiguration().getId());
      return (Integer)var3.getOrDefault(var4, PersistentDataType.INTEGER, 0);
   }

   public static HashMap<OfflinePlayer, Integer> getKills(Vehicle vehicle) {
      HashMap var1 = new HashMap();
      Iterator var2 = Bukkit.getWorlds().iterator();

      while(var2.hasNext()) {
         World var3 = (World)var2.next();
         PersistentDataContainer var4 = var3.getPersistentDataContainer();
         OfflinePlayer[] var5 = Bukkit.getOfflinePlayers();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            OfflinePlayer var8 = var5[var7];
            NamespacedKey var9 = getKey(var8, var0.getConfiguration().getId());
            int var10 = (Integer)var4.getOrDefault(var9, PersistentDataType.INTEGER, 0);
            var1.computeIfPresent(var8, (var1x, var2x) -> {
               return var2x + var10;
            });
            var1.putIfAbsent(var8, var10);
         }
      }

      return (HashMap)var1.entrySet().stream().sorted(Entry.comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (var0x, var1x) -> {
         return var0x;
      }, LinkedHashMap::new));
   }

   private static NamespacedKey getKey(OfflinePlayer player, String vehicleID) {
      return new NamespacedKey(InfiniteVehiclesPlugin.getInstance(), "kills_" + var1 + "_" + String.valueOf(var0.getUniqueId()));
   }
}
