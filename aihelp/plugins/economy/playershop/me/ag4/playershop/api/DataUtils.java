package me.ag4.playershop.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.files.PlayersFolder;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class DataUtils {
   public static final List<Shop> shopList = new ArrayList();
   public static HashMap<UUID, Double> purchase = new HashMap();

   public static void autoSave(int minutes) {
      int ticksPerMinute = 1200;
      int delayTicks = ticksPerMinute * minutes;
      Bukkit.getScheduler().runTaskTimerAsynchronously(PlayerShop.getPlugin(PlayerShop.class), DataUtils::saveDataFile, (long)delayTicks, (long)delayTicks);
   }

   public static void runShopsChecker() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(PlayerShop.getInstance(), () -> {
         if (!shopList.isEmpty()) {
            synchronized(shopList) {
               Iterator iterator = shopList.iterator();

               while(iterator.hasNext()) {
                  Shop shopAPI = (Shop)iterator.next();
                  Block block = shopAPI.getLocation().getBlock();
                  if (block.getType() != Material.CHEST) {
                     iterator.remove();
                     shopAPI.removeShop();
                  }
               }

            }
         }
      }, 20L, 20L);
   }

   public static void saveDataFile() {
      Iterator var0 = PlayersFolder.getAllUUIDs().iterator();

      String username;
      while(var0.hasNext()) {
         String uuid = (String)var0.next();

         try {
            FileConfiguration dataFile = PlayersFolder.get(uuid);
            Iterator var3 = dataFile.getKeys(false).iterator();

            while(var3.hasNext()) {
               username = (String)var3.next();
               if (PlayerShopAPI.getPlayerShop(username) == null) {
                  dataFile.set(username, (Object)null);
                  Class var5 = PlayersFolder.class;
                  synchronized(PlayersFolder.class) {
                     PlayersFolder.save(uuid, dataFile);
                  }
               }
            }
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      }

      if (!PlayerShopAPI.getPlayerShopList().isEmpty()) {
         var0 = shopList.iterator();

         while(var0.hasNext()) {
            Shop shopAPI = (Shop)var0.next();

            try {
               String uniqueKey = shopAPI.getKey();
               String owner = shopAPI.getOwner().toString();
               username = shopAPI.getUsername();
               Location location = shopAPI.getLocation();
               if (location != null && location.getWorld() != null) {
                  FileConfiguration dataFile = PlayersFolder.get(owner);
                  dataFile.set(uniqueKey + ".uuid", owner);
                  dataFile.set(uniqueKey + ".owner", username);
                  dataFile.set(uniqueKey + ".location.world", location.getWorld().getName());
                  dataFile.set(uniqueKey + ".location.x", location.getBlockX());
                  dataFile.set(uniqueKey + ".location.y", location.getBlockY());
                  dataFile.set(uniqueKey + ".location.z", location.getBlockZ());
                  dataFile.set(uniqueKey + ".price", shopAPI.getPrice());
                  int amount = 0;
                  ItemStack[] var8 = shopAPI.getInventory().getContents();
                  int var9 = var8.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     ItemStack items = var8[var10];
                     dataFile.set(uniqueKey + ".items." + amount, items);
                     ++amount;
                  }

                  Class var21 = PlayersFolder.class;
                  synchronized(PlayersFolder.class) {
                     PlayersFolder.save(owner, dataFile);
                  }
               }
            } catch (Exception var15) {
               var15.printStackTrace();
            }
         }

      }
   }

   public static void restoreDataFile() {
      Iterator var0 = PlayersFolder.getAllUUIDs().iterator();

      while(var0.hasNext()) {
         String uuidFile = (String)var0.next();

         try {
            FileConfiguration playerData = PlayersFolder.get(uuidFile);
            Iterator var3 = playerData.getKeys(false).iterator();

            while(var3.hasNext()) {
               String uniqueKey = (String)var3.next();

               try {
                  String owner = playerData.getString(uniqueKey + ".uuid");
                  String username = playerData.getString(uniqueKey + ".owner");
                  double price = playerData.getDouble(uniqueKey + ".price");
                  if (owner != null) {
                     String worldName = playerData.getString(uniqueKey + ".location.world");
                     if (worldName == null || Bukkit.getWorld(worldName) == null) {
                        WorldCreator worldCreator = new WorldCreator(worldName);
                        worldCreator.createWorld();
                     }

                     int x = playerData.getInt(uniqueKey + ".location.x");
                     int y = playerData.getInt(uniqueKey + ".location.y");
                     int z = playerData.getInt(uniqueKey + ".location.z");
                     Location location = new Location(Bukkit.getWorld(worldName), (double)x, (double)y, (double)z);
                     Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 27, "");

                     for(int i = 0; i < 27; ++i) {
                        ItemStack itemStack = playerData.getItemStack(uniqueKey + ".items." + i);
                        if (itemStack != null) {
                           inventory.setItem(i, itemStack);
                        }
                     }

                     new Shop(uniqueKey, UUID.fromString(owner), username, price, location, inventory);
                  }
               } catch (Exception var17) {
                  var17.printStackTrace();
               }
            }
         } catch (Exception var18) {
            var18.printStackTrace();
         }
      }

   }
}
