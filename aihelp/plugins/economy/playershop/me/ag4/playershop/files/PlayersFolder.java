package me.ag4.playershop.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PlayersFolder {
   private static File dataFolder;
   private static PluginDescriptionFile pdf = ((PlayerShop)PlayerShop.getPlugin(PlayerShop.class)).getDescription();

   public static void setup() {
      dataFolder = new File(((Plugin)Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(pdf.getName()))).getDataFolder(), "userdata");
      if (!dataFolder.exists()) {
         dataFolder.mkdirs();
      }

   }

   public static FileConfiguration get(String playerUUID) {
      File playerFile = new File(dataFolder, playerUUID + ".yml");
      return YamlConfiguration.loadConfiguration(playerFile);
   }

   public static List<String> getAllUUIDs() {
      List<String> uuidsAsStrings = new ArrayList();
      File[] files = dataFolder.listFiles();
      if (files != null) {
         File[] var2 = files;
         int var3 = files.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File file = var2[var4];
            if (file.isFile() && file.getName().endsWith(".yml")) {
               String fileName = file.getName();
               String playerUUIDString = fileName.substring(0, fileName.length() - 4);
               uuidsAsStrings.add(playerUUIDString);
            }
         }
      }

      return uuidsAsStrings;
   }

   public static void save(String playerUUID, FileConfiguration customFile) {
      File playerFile = new File(dataFolder, playerUUID + ".yml");

      try {
         customFile.save(playerFile);
      } catch (IOException var4) {
         ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
         String var10001 = pdf.getName();
         var10000.sendMessage(Utils.hex("&6" + var10001 + " &cCouldn't save " + playerUUID + ".yml file"));
      }

   }

   public static void reload(UUID playerUUID) {
      File playerFile = new File(dataFolder, playerUUID.toString() + ".yml");
      YamlConfiguration customFile = YamlConfiguration.loadConfiguration(playerFile);
   }
}
