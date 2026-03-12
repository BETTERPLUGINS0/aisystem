package me.gypopo.economyshopgui.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.config.Config;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.yaml.snakeyaml.Yaml;

public class ConfigUtil {
   public static void updateConfigs() {
      int configVer;
      try {
         configVer = Integer.parseInt(ConfigManager.getConfig().getString("config-version", "2.0.0.0").replace(".", ""));
         if (String.valueOf(configVer).length() < 4) {
            while(String.valueOf(configVer).length() != 4) {
               configVer = Integer.parseInt(configVer + "0");
            }
         } else if (String.valueOf(configVer).length() > 4) {
            while(String.valueOf(configVer).length() != 4) {
               configVer = Integer.parseInt(String.valueOf(configVer).substring(0, String.valueOf(configVer).length() - 1));
            }
         }
      } catch (NumberFormatException var4) {
         SendMessage.errorMessage("Failed to check for config updates because the version is invalid(" + ConfigManager.getConfig().getString("config-version", "1.0.0") + "), please do not change this version as it might break the plugin");
         return;
      }

      int pluginVer = Integer.parseInt(EconomyShopGUI.getInstance().loadConfiguration(new BufferedReader(new InputStreamReader(EconomyShopGUI.getInstance().getResource("config.yml"), StandardCharsets.UTF_8)), "config.yml").getString("config-version").replace(".", ""));
      if (configVer != pluginVer) {
         SendMessage.infoMessage(Lang.UPDATING_CONFIGS.get());

         try {
            if (configVer <= 1270) {
               migrateV1toV2();
               configVer = 2000;
            }

            updateV2Config(configVer);
         } catch (Exception var3) {
            SendMessage.errorMessage("Failed to update the configs");
            var3.printStackTrace();
         }
      }

   }

   private static void updateV2Config(int configVer) {
      Iterator var1;
      String i;
      Iterator var4;
      String itemLoc;
      if (configVer == 2000) {
         var1 = ConfigManager.getShops().iterator();

         while(var1.hasNext()) {
            i = (String)var1.next();
            boolean updated = false;
            var4 = EconomyShopGUI.getInstance().getConfigManager().getItemsRaw(i).iterator();

            while(var4.hasNext()) {
               itemLoc = (String)var4.next();
               Set<String> keys = ConfigManager.getShop(i).getConfigurationSection("pages." + itemLoc).getKeys(false);
               if (keys.contains("max-buy") && ConfigManager.getShop(i).getInt("pages." + itemLoc + ".max-buy") == 1) {
                  ConfigManager.getShop(i).set("pages." + itemLoc + ".max-buy", -1);
                  updated = true;
               }

               if (keys.contains("max-sell") && ConfigManager.getShop(i).getInt("pages." + itemLoc + ".max-sell") == 1) {
                  ConfigManager.getShop(i).set("pages." + itemLoc + ".max-sell", -1);
                  updated = true;
               }
            }

            if (updated) {
               ConfigManager.saveShop(i);
            }
         }

         configVer = 2001;
      }

      boolean updated;
      if (configVer == 2001) {
         if (ConfigManager.getConfig().contains("season-price-modifiers")) {
            updated = false;
            Iterator var11 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers").getKeys(false).iterator();

            label212:
            while(true) {
               if (!var11.hasNext()) {
                  if (updated) {
                     ConfigManager.saveConfig();
                  }
                  break;
               }

               String season = (String)var11.next();
               var4 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers." + season).getKeys(false).iterator();

               while(true) {
                  while(true) {
                     while(true) {
                        do {
                           do {
                              if (!var4.hasNext()) {
                                 continue label212;
                              }

                              itemLoc = (String)var4.next();
                           } while(ConfigManager.getConfig().getConfigurationSection("season-price-modifiers." + season + "." + itemLoc) != null);
                        } while(!EconomyShopGUI.getInstance().getShopSections().contains(itemLoc.split(":")[0]));

                        if (itemLoc.split(":").length == 2) {
                           List<String> keys = EconomyShopGUI.getInstance().getConfigManager().getItemsRaw(itemLoc.split(":")[0]);
                           Iterator var7 = keys.iterator();

                           while(var7.hasNext()) {
                              String item = (String)var7.next();
                              if (item.split("\\.")[2].equalsIgnoreCase(itemLoc.split(":")[1])) {
                                 updated = true;
                                 ConfigManager.getConfig().set("season-price-modifiers." + season + "." + itemLoc.split(":")[0] + "." + item.replace(".", ":"), ConfigManager.getConfig().get("season-price-modifiers." + season + "." + itemLoc));
                                 ConfigManager.getConfig().set("season-price-modifiers." + season + "." + itemLoc, (Object)null);
                                 break;
                              }
                           }
                        } else if (itemLoc.split(":").length == 1) {
                           updated = true;
                           ConfigManager.getConfig().set("season-price-modifiers." + season + "." + itemLoc + ".all", ConfigManager.getConfig().get("season-price-modifiers." + season + "." + itemLoc));
                        }
                     }
                  }
               }
            }
         }

         configVer = 2002;
      }

      if (configVer == 2002) {
         createBackup(ConfigManager.getConfig().getFilePath());
         ConfigurationSection contents;
         if (!ConfigManager.getConfig().contains("main-menu-nav-bar.items")) {
            contents = ConfigManager.getConfig().getConfigurationSection("main-menu-nav-bar");
            if (contents != null) {
               ConfigManager.getConfig().set("main-menu-nav-bar", (Object)null);
               ConfigManager.getConfig().set("main-menu-nav-bar.enabled", contents.getBoolean("enabled", true));
               contents.set("enabled", (Object)null);
               setSection(ConfigManager.getConfig(), contents, "main-menu-nav-bar.items");
            }
         }

         if (!ConfigManager.getConfig().contains("shops-nav-bar.items")) {
            contents = ConfigManager.getConfig().getConfigurationSection("shops-nav-bar");
            if (contents != null) {
               ConfigManager.getConfig().set("shops-nav-bar", (Object)null);
               ConfigManager.getConfig().set("shops-nav-bar.enabled", true);
               setSection(ConfigManager.getConfig(), contents, "shops-nav-bar.items");
            }
         }

         if (!ConfigManager.getConfig().contains("transaction-screens-nav-bar.items")) {
            contents = ConfigManager.getConfig().getConfigurationSection("transaction-screens-nav-bar");
            if (contents != null) {
               ConfigManager.getConfig().set("transaction-screens-nav-bar", (Object)null);
               ConfigManager.getConfig().set("transaction-screens-nav-bar.enabled", true);
               setSection(ConfigManager.getConfig(), contents, "transaction-screens-nav-bar.items");
            }
         }

         if (!ConfigManager.getConfig().contains("sellgui-nav-bar.items")) {
            contents = ConfigManager.getConfig().getConfigurationSection("sellgui-nav-bar");
            if (contents != null) {
               ConfigManager.getConfig().set("sellgui-nav-bar", (Object)null);
               ConfigManager.getConfig().set("sellgui-nav-bar.enabled", true);
               setSection(ConfigManager.getConfig(), contents, "sellgui-nav-bar.items");
            }
         }

         configVer = 2010;
      }

      if (configVer == 2010) {
         if (ConfigManager.getConfig().getBoolean("disableBackButtonWithShopSectionCommand")) {
            ConfigManager.getConfig().set("disable-back.direct-shop-command", true);
            ConfigManager.getConfig().set("disable-back.direct-shop-command-console", true);
            ConfigManager.getConfig().set("disable-back.sub-sections", false);
         }

         configVer = 2011;
      }

      if (configVer == 2011) {
         var1 = ConfigManager.getConfig().getConfigurationSection("buy-screen.items").getKeys(false).iterator();

         ConfigurationSection item;
         while(var1.hasNext()) {
            i = (String)var1.next();
            item = ConfigManager.getConfig().getConfigurationSection("buy-screen.items." + i);
            if (item != null && item.getString("type", "NORMAL").equals("SELECTED_ITEM") && !item.contains("lore")) {
               ConfigManager.getConfig().set("buy-screen.items." + i + ".lore", Lang.TRANSACTION_SCREEN_TOTAL_AMOUNT.get().getLines());
            }
         }

         var1 = ConfigManager.getConfig().getConfigurationSection("sell-screen.items").getKeys(false).iterator();

         while(var1.hasNext()) {
            i = (String)var1.next();
            item = ConfigManager.getConfig().getConfigurationSection("sell-screen.items." + i);
            if (item != null && item.getString("type", "NORMAL").equals("SELECTED_ITEM") && !item.contains("lore")) {
               ConfigManager.getConfig().set("sell-screen.items." + i + ".lore", Lang.TRANSACTION_SCREEN_TOTAL_AMOUNT.get().getLines());
            }
         }

         var1 = ConfigManager.getConfig().getConfigurationSection("buy-stacks-screen.items").getKeys(false).iterator();

         while(var1.hasNext()) {
            i = (String)var1.next();
            item = ConfigManager.getConfig().getConfigurationSection("buy-stacks-screen.items." + i);
            if (item != null && item.getString("type", "NORMAL").equals("SELECTED_ITEM") && !item.contains("lore")) {
               ConfigManager.getConfig().set("buy-stacks-screen.items." + i + ".lore", Lang.TRANSACTION_SCREEN_TOTAL_AMOUNT.get().getLines());
            }
         }

         configVer = 2012;
      }

      if (configVer == 2012) {
         var1 = ConfigManager.getSections().iterator();

         while(var1.hasNext()) {
            i = (String)var1.next();

            try {
               Config c = ConfigManager.getSection(i);
               String slots = c.getString("slot");
               if (slots != null) {
                  ArrayList<Integer> slot = EconomyShopGUI.getInstance().calculateAmount.getSlots(slots);
                  if (slot.size() == 1) {
                     c.set("slot", (Integer)slot.get(0) + 2);
                  } else {
                     c.set("slot", slot.stream().map((ix) -> {
                        return String.valueOf(ix + 2);
                     }).collect(Collectors.joining(",")));
                  }
               }

               c.save();
            } catch (Exception var9) {
            }
         }

         configVer = 2013;
      }

      if (configVer == 2013) {
         ConfigManager.getConfig().set("use-paper-meta", false);
         configVer = 2014;
      }

      if (configVer == 2014) {
         ConfigManager.getConfig().set("sort-items-by", "SHOP_LOAD_ORDER");
         configVer = 2015;
      }

      if (configVer == 2015) {
         updated = ConfigManager.getConfig().getBoolean("shop-stands");
         ConfigManager.getConfig().set("shop-stands.enable", updated);
         ConfigManager.getConfig().set("shop-stands.holograms", true);
         configVer = 2016;
      }

      String cVer = getConfigVersion(configVer);
      ConfigManager.getConfig().set("config-version", cVer);
      ConfigManager.saveConfig();
      SendMessage.infoMessage("Successfully updated configs to v" + cVer);
   }

   private static void setSection(Config config, ConfigurationSection section, String path) {
      Iterator var3 = section.getValues(true).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> key = (Entry)var3.next();
         if (!(key.getValue() instanceof MemorySection)) {
            config.set(path + "." + (String)key.getKey(), key.getValue());
         }
      }

   }

   private static void migrateV1toV2() {
      if (ConfigManager.getConfig().contains("mainshop-size")) {
         ConfigManager.getConfig().set("main-menu.gui-rows", ConfigManager.getConfig().get("mainshop-size"));
         ConfigManager.getConfig().set("mainshop-size", (Object)null);
      }

      if (!ConfigManager.getConfig().contains("main-menu-nav-bar.enabled")) {
         ConfigManager.getConfig().set("main-menu-nav-bar.enabled", true);
      }

      if (ConfigManager.getConfig().contains("mainshop-size")) {
         Map<String, Map<String, String>> modifiers = new HashMap();
         Iterator var1 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers").getKeys(false).iterator();

         String shop;
         while(var1.hasNext()) {
            shop = (String)var1.next();
            Iterator var3 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers." + shop).getKeys(false).iterator();

            while(var3.hasNext()) {
               String modifier = (String)var3.next();
               Map shopModifiers;
               if (modifier.contains(":")) {
                  shopModifiers = (Map)modifiers.getOrDefault(shop + "." + modifier.split(":")[0], new HashMap());
                  shopModifiers.put(modifier.split(":")[1], ConfigManager.getConfig().getString(modifier));
                  modifiers.put(shop + "." + modifier.split(":")[0], shopModifiers);
               } else {
                  shopModifiers = (Map)modifiers.getOrDefault(shop + "." + modifier, new HashMap());
                  shopModifiers.put("all", ConfigManager.getConfig().getString(modifier));
                  modifiers.put(shop + "." + modifier, shopModifiers);
               }
            }
         }

         if (!modifiers.isEmpty()) {
            var1 = modifiers.keySet().iterator();

            while(var1.hasNext()) {
               shop = (String)var1.next();
               ConfigManager.getConfig().set("season-price-modifiers." + shop, modifiers.get(shop));
            }
         }
      }

      ConfigManager.getConfig().set("config-version", "2.0.0.0");
      ConfigManager.saveConfig();
   }

   private static String getConfigVersion(int version) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < String.valueOf(version).toCharArray().length; ++i) {
         if (String.valueOf(version).toCharArray().length != i + 1) {
            builder.append(String.valueOf(version).toCharArray()[i]).append(".");
         } else {
            builder.append(String.valueOf(version).toCharArray()[i]);
         }
      }

      return builder.toString();
   }

   public static void createBackup(Path path) {
      String fileName = path.getFileName().toString();
      SendMessage.infoMessage(Lang.CREATING_BACKUP.get().replace("%fileName%", fileName));
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
      LocalDateTime now = LocalDateTime.now();
      Path backupDir = Paths.get(EconomyShopGUI.getInstance().getDataFolder() + "/backups/");
      if (!Files.exists(backupDir, new LinkOption[0]) || !Files.isDirectory(backupDir, new LinkOption[0])) {
         try {
            Files.createDirectory(backupDir);
         } catch (FileAlreadyExistsException var8) {
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }

      try {
         try {
            Files.copy(path, Paths.get(backupDir.toString(), fileName.split("\\.")[0] + " " + dtf.format(now) + ".yml"));
         } catch (FileAlreadyExistsException var10) {
            int i = 1;

            String newFileName;
            for(newFileName = fileName.split("\\.")[0] + " " + dtf.format(now) + " " + i + ".yml"; (new File(backupDir.toString(), newFileName)).exists(); ++i) {
            }

            Files.copy(path, Paths.get(backupDir.toString(), newFileName));
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static boolean loadConfig() {
      if ((new File(EconomyShopGUI.getInstance().getDataFolder(), "config.yml")).exists()) {
         try {
            FileConfiguration config = EconomyShopGUI.getInstance().loadConfiguration(new File(EconomyShopGUI.getInstance().getDataFolder(), "config.yml"), "config.yml");
            if (config == null) {
               return false;
            }

            BufferedReader input = new BufferedReader(new InputStreamReader(EconomyShopGUI.getInstance().getResource("config.yml"), StandardCharsets.UTF_8));
            List<String> defaults = (List)input.lines().collect(Collectors.toList());
            input.close();
            StringBuilder builder = new StringBuilder();
            Iterator var4 = defaults.iterator();

            while(var4.hasNext()) {
               String s = (String)var4.next();
               builder.append(s).append("\n");
            }

            Files.write(Paths.get(EconomyShopGUI.getInstance().getDataFolder() + "/config.yml"), builder.toString().getBytes(), new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING});
            File file = new File(EconomyShopGUI.getInstance().getDataFolder(), "config.yml");
            FileConfiguration conf = EconomyShopGUI.getInstance().loadConfiguration(file, "config.yml");
            Iterator var6 = config.getKeys(false).iterator();

            while(var6.hasNext()) {
               String str = (String)var6.next();
               conf.set(str, config.get(str));
            }

            save(conf, file);
            EconomyShopGUI.getInstance().reloadConfig();
            if (EconomyShopGUI.getInstance().badYMLParse != null && EconomyShopGUI.getInstance().badYMLParse.equals("config.yml")) {
               EconomyShopGUI.getInstance().badYMLParse = null;
            }
         } catch (IOException var8) {
            SendMessage.errorMessage("Cannot read config.yml config because it is mis-configured, use a online Yaml parser with the error underneath here to find out the cause of the problem and to solve it. If you cannot find the cause yourself, join our discord support server that can be found at a plugin page of EconomyShopGUI.");
            var8.printStackTrace();
            EconomyShopGUI.getInstance().badYMLParse = "config.yml";
            return false;
         }
      } else {
         EconomyShopGUI.getInstance().saveDefaultConfig();
      }

      return true;
   }

   public static void save(FileConfiguration fileConfiguration, File file) {
      if (fileConfiguration == null) {
         SendMessage.errorMessage(Lang.COULD_NOT_SAVE_CONFIG.get().replace("%fileName%", file.getName()));
      } else {
         try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            Map<String, String> comments = getComments((List)input.lines().collect(Collectors.toList()));
            input.close();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            Iterator var5 = fileConfiguration.getKeys(true).iterator();

            while(true) {
               while(var5.hasNext()) {
                  String key = (String)var5.next();
                  String[] keys = key.split("\\.");
                  String actualKey = keys[keys.length - 1];
                  String comment = (String)comments.remove(key);
                  StringBuilder prefixBuilder = new StringBuilder();
                  int indents = keys.length - 1;
                  appendPrefixSpaces(prefixBuilder, indents);
                  String prefixSpaces = prefixBuilder.toString();
                  if (comment != null) {
                     writer.write(comment);
                  }

                  Object obj = fileConfiguration.get(key);
                  if (obj instanceof ConfigurationSerializable) {
                     writer.write(prefixSpaces + actualKey + ": " + (new Yaml()).dump(((ConfigurationSerializable)obj).serialize()));
                  } else if (!(obj instanceof String) && !(obj instanceof Character)) {
                     if (obj instanceof List) {
                        writer.write(getListAsString((List)obj, actualKey, prefixSpaces));
                     } else if (obj instanceof MemorySection) {
                        writer.write(prefixSpaces + actualKey + ":\n");
                     } else {
                        writer.write(prefixSpaces + actualKey + ": " + (new Yaml()).dump(obj));
                     }
                  } else {
                     if (obj instanceof String) {
                        String s = (String)obj;
                        obj = s.replace("\n", "\\n");
                     }

                     writer.write(prefixSpaces + actualKey + ": " + (new Yaml()).dump(obj).replace("\n ", ""));
                  }
               }

               String danglingComments = (String)comments.get((Object)null);
               if (danglingComments != null) {
                  writer.write(danglingComments);
               }

               writer.close();
               break;
            }
         } catch (IOException var15) {
            SendMessage.errorMessage(Lang.COULD_NOT_SAVE_CONFIG.get().replace("%fileName%", file.getName()));
            var15.printStackTrace();
         }

      }
   }

   private static Map<String, String> getComments(List<String> lines) {
      Map<String, String> comments = new HashMap();
      StringBuilder builder = new StringBuilder();
      StringBuilder keyBuilder = new StringBuilder();
      int lastLineIndentCount = 0;
      Iterator var5 = lines.iterator();

      while(true) {
         while(true) {
            String line;
            do {
               if (!var5.hasNext()) {
                  if (builder.length() > 0) {
                     comments.put((Object)null, builder.toString());
                  }

                  return comments;
               }

               line = (String)var5.next();
            } while(line != null && line.trim().startsWith("-"));

            if (line != null && !line.trim().equals("") && !line.trim().startsWith("#")) {
               lastLineIndentCount = setFullKey(keyBuilder, line, lastLineIndentCount);
               if (keyBuilder.length() > 0) {
                  comments.put(keyBuilder.toString(), builder.toString());
                  builder.setLength(0);
               }
            } else {
               builder.append(line).append("\n");
            }
         }
      }
   }

   private static int setFullKey(StringBuilder keyBuilder, String configLine, int lastLineIndentCount) {
      int currentIndents = countIndents(configLine);
      String key = configLine.trim().split(":")[0];
      if (keyBuilder.length() == 0) {
         keyBuilder.append(key);
      } else if (currentIndents == lastLineIndentCount) {
         removeLastKey(keyBuilder);
         if (keyBuilder.length() > 0) {
            keyBuilder.append(".");
         }

         keyBuilder.append(key);
      } else if (currentIndents > lastLineIndentCount) {
         keyBuilder.append(".").append(key);
      } else {
         int difference = lastLineIndentCount - currentIndents;

         for(int i = 0; i < difference + 1; ++i) {
            removeLastKey(keyBuilder);
         }

         if (keyBuilder.length() > 0) {
            keyBuilder.append(".");
         }

         keyBuilder.append(key);
      }

      return currentIndents;
   }

   private static String getListAsString(List list, String actualKey, String prefixSpaces) {
      StringBuilder builder = (new StringBuilder(prefixSpaces)).append(actualKey).append(":");
      if (list.isEmpty()) {
         builder.append(" []\n");
         return builder.toString();
      } else {
         builder.append("\n");

         for(int i = 0; i < list.size(); ++i) {
            Object o = list.get(i);
            if (!(o instanceof String) && !(o instanceof Character)) {
               if (o instanceof List) {
                  builder.append(prefixSpaces).append("- ").append((new Yaml()).dump(o));
               } else {
                  builder.append(prefixSpaces).append("- ").append(o);
               }
            } else {
               String s = o.toString();
               if (s.contains("\"") || s.contains("'")) {
                  s = s.replace("'", "''");
               }

               builder.append(prefixSpaces).append("- '").append(s).append("'");
            }

            if (i != list.size()) {
               builder.append("\n");
            }
         }

         return builder.toString();
      }
   }

   private static int countIndents(String s) {
      int spaces = 0;
      char[] var2 = s.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         if (c != ' ') {
            break;
         }

         ++spaces;
      }

      return spaces / 2;
   }

   private static void removeLastKey(StringBuilder keyBuilder) {
      String temp = keyBuilder.toString();
      String[] keys = temp.split("\\.");
      if (keys.length == 1) {
         keyBuilder.setLength(0);
      } else {
         temp = temp.substring(0, temp.length() - keys[keys.length - 1].length() - 1);
         keyBuilder.setLength(temp.length());
      }
   }

   private static String getPrefixSpaces(int indents) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < indents; ++i) {
         builder.append("  ");
      }

      return builder.toString();
   }

   private static void appendPrefixSpaces(StringBuilder builder, int indents) {
      builder.append(getPrefixSpaces(indents));
   }
}
