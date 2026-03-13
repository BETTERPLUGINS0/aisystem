package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class Migrate extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   private final Map<String, List<String>> supported = this.getSupportedFiles();
   String pluginName;
   String image;

   public Migrate(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "migrate";
   }

   public String getDescription() {
      return Lang.EDITSHOP_MIGRATE_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_MIGRATE_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 1) {
         if (this.supported.containsKey(args[1].toLowerCase(Locale.ENGLISH))) {
            this.pluginName = args[1].toLowerCase(Locale.ENGLISH);
            if (args.length > 2) {
               this.image = args[2].toLowerCase(Locale.ENGLISH);
               boolean override = false;
               boolean confirm = false;
               String[] var5 = args;
               int var6 = args.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  String arg = var5[var7];
                  if (arg.equalsIgnoreCase("--override")) {
                     override = true;
                  } else if (arg.equalsIgnoreCase("--confirm")) {
                     confirm = true;
                  }
               }

               if (override && !confirm) {
                  SendMessage.warnMessage(logger, "This command will override any existing shop data. We suggest creating backups of your current shops.yml and sections.yml before using this command.");
                  SendMessage.infoMessage(logger, "Use this command with the " + ChatColor.WHITE + "--confirm " + ChatColor.GREEN + "flag to continue migration using this command.");
               } else {
                  this.plugin.runTaskAsync(() -> {
                     this.migrateData(logger, override);
                  });
               }
            } else {
               SendMessage.sendMessage(logger, this.getSyntax());
            }
         } else {
            SendMessage.sendMessage(logger, Lang.PLUGIN_NOT_FOUND.get());
         }
      } else {
         SendMessage.sendMessage(logger, this.getSyntax());
      }
   }

   private Map<String, List<String>> getSupportedFiles() {
      Map<String, List<String>> supported = new HashMap();
      supported.put("economyshopgui", Arrays.asList("V1toV2"));
      return supported;
   }

   private void migrateData(Object logger, boolean override) {
      if (this.pluginName.equalsIgnoreCase("economyshopgui")) {
         int migrated = 0;
         boolean changes = false;
         Set<String> exist = new HashSet();
         File shops = new File(this.plugin.getDataFolder(), "shops.yml");
         int page;
         if (shops.exists()) {
            SendMessage.logDebugMessage("Started migrating data from shops.yml....");
            YamlConfiguration config = this.plugin.loadConfiguration(shops, "shops.yml");
            if (override) {
               File shopFolder = new File(this.plugin.getDataFolder() + File.separator + "shops");
               if (shopFolder.exists() && shopFolder.isDirectory()) {
                  File[] var9 = shopFolder.listFiles();
                  int var10 = var9.length;

                  for(page = 0; page < var10; ++page) {
                     File conf = var9[page];

                     try {
                        Files.delete(conf.toPath());
                        SendMessage.infoMessage("Deleted " + shopFolder.getName() + " shops config while migrating using the --override flag.");
                     } catch (IOException var21) {
                        SendMessage.warnMessage("A unknown exception occurred while removing " + shopFolder.getName() + " shops config while migrating using the --override flag.");
                        var21.printStackTrace();
                     }
                  }
               }
            }

            Iterator var25 = config.getKeys(false).iterator();

            label200:
            while(true) {
               while(true) {
                  if (!var25.hasNext()) {
                     break label200;
                  }

                  String section = (String)var25.next();
                  if (ConfigManager.getShop(section) != null) {
                     if (!override) {
                        exist.add(section);
                        SendMessage.logDebugMessage("Skipping section " + section + " because it already exists...");
                        continue;
                     }

                     SendMessage.warnMessage(logger, "Overriding " + section + " from shops.yml");
                  }

                  YamlConfiguration newConfig = new YamlConfiguration();
                  page = 1;
                  Map<String, Object> items = new LinkedHashMap();
                  Map<Integer, Map<String, Object>> pages = new HashMap();
                  boolean max = false;
                  Iterator var15 = config.getConfigurationSection(section).getKeys(false).iterator();

                  String s;
                  while(var15.hasNext()) {
                     s = (String)var15.next();
                     int slot = config.getInt(section + "." + s + ".slot");
                     if (slot != 0) {
                        max = true;
                        int p = (int)Math.ceil((double)slot / 45.0D);
                        if (pages.containsKey(p)) {
                           ((Map)pages.get(p)).put(s, config.get(section + "." + s));
                        } else {
                           for(int i = 1; i < p; ++i) {
                              if (!pages.containsKey(i)) {
                                 pages.put(i, new HashMap());
                              }
                           }

                           Map<String, Object> item = new LinkedHashMap();
                           item.put(s, config.get(section + "." + s));
                           pages.put(p, item);
                        }
                     }

                     if (items.size() == 45) {
                        pages.put(page, items);
                        items = new LinkedHashMap();
                        items.put(s, config.get(section + "." + s));
                        ++page;
                     } else {
                        items.put(s, config.get(section + "." + s));
                     }
                  }

                  if (!items.isEmpty()) {
                     if (pages.containsKey(page)) {
                        ((Map)pages.get(page)).putAll(items);
                     } else {
                        pages.put(page, items);
                     }
                  }

                  if (pages.isEmpty()) {
                     SendMessage.warnMessage("Skipping " + section + " from shops.yml because no data to migrate could be found.");
                  } else {
                     changes = true;
                     SendMessage.logDebugMessage("Migrating section " + section + " with " + pages.values().stream().mapToInt((px) -> {
                        return px.size();
                     }).sum() + " items.");

                     for(int i = 0; i < pages.size(); ++i) {
                        s = "page" + (i + 1);
                        newConfig.set("pages." + s + ".gui-rows", max ? 6 : CalculateAmount.getSlots(((Map)pages.get(i + 1)).size()) / 9);
                        newConfig.set("pages." + s + ".items", pages.get(i + 1));
                        SendMessage.logDebugMessage("Created new page for section " + section + " with " + ((Map)pages.get(i + 1)).size() + " items.");
                     }

                     File newShop = new File(this.plugin.getDataFolder() + File.separator + "shops" + File.separator, section + ".yml");

                     try {
                        newConfig.save(newShop);
                        ++migrated;
                        SendMessage.infoMessage("Migrated " + section + " with " + pages.size() + " pages and a total of " + pages.values().stream().mapToInt((px) -> {
                           return px.size();
                        }).sum() + " items and saved to " + newShop.getAbsolutePath());
                     } catch (IOException var23) {
                        if (logger instanceof Player) {
                           SendMessage.warnMessage("A unknown error occurred while migrating shop '" + section + "', look in your console for more details.");
                        }

                        SendMessage.warnMessage("A unknown error occurred while saving new shop file " + newShop.getName());
                        var23.printStackTrace();
                     }
                  }
               }
            }
         }

         File sections = new File(this.plugin.getDataFolder(), "sections.yml");
         if (sections.exists()) {
            SendMessage.logDebugMessage("Started migrating data from sections.yml....");
            YamlConfiguration config = this.plugin.loadConfiguration(sections, "sections.yml");
            File newSection;
            if (override) {
               File sectionFolder = new File(this.plugin.getDataFolder() + File.separator + "sections");
               if (sectionFolder.exists() && sectionFolder.isDirectory()) {
                  File[] var31 = sectionFolder.listFiles();
                  page = var31.length;

                  for(int var35 = 0; var35 < page; ++var35) {
                     newSection = var31[var35];

                     try {
                        Files.delete(newSection.toPath());
                        SendMessage.infoMessage("Deleted " + sectionFolder.getName() + " sections config while migrating using the --override flag.");
                     } catch (IOException var20) {
                        SendMessage.warnMessage("A unknown exception occurred while removing " + sectionFolder.getName() + " sections config while migrating using the --override flag.");
                        var20.printStackTrace();
                     }
                  }
               }
            }

            if (!config.contains("ShopSections")) {
               SendMessage.warnMessage("No sections found in sections.yml");
            } else {
               Iterator var29 = config.getConfigurationSection("ShopSections").getKeys(false).iterator();

               label145:
               while(true) {
                  while(true) {
                     if (!var29.hasNext()) {
                        break label145;
                     }

                     String section = (String)var29.next();
                     if (ConfigManager.getSection(section) != null) {
                        if (!override) {
                           exist.add(section);
                           SendMessage.logDebugMessage("Skipping section " + section + " because it already exists...");
                           continue;
                        }

                        SendMessage.warnMessage(logger, "Overriding " + section + " from sections.yml");
                     }

                     YamlConfiguration newConfig = new YamlConfiguration();
                     SendMessage.logDebugMessage("Migrating section " + section + "...");
                     Map<String, Object> newData = this.getSectionData(config, section);
                     if (newData.isEmpty()) {
                        SendMessage.warnMessage("Skipping " + section + " from sections.yml because no data to migrate could be found.");
                     } else {
                        changes = true;
                        Iterator var38 = newData.keySet().iterator();

                        while(var38.hasNext()) {
                           String key = (String)var38.next();
                           newConfig.set(key, newData.get(key));
                        }

                        newSection = new File(this.plugin.getDataFolder() + File.separator + "sections" + File.separator, section + ".yml");

                        try {
                           newConfig.save(newSection);
                           ++migrated;
                           SendMessage.infoMessage("Migrated " + section + " and saved to " + newSection.getAbsolutePath());
                        } catch (IOException var22) {
                           if (logger instanceof Player) {
                              SendMessage.warnMessage("A unknown error occurred while migrating shop '" + section + "', look in your console for more details.");
                           }

                           SendMessage.warnMessage("A unknown error occurred while saving new shop file " + newSection.getName());
                           var22.printStackTrace();
                        }
                     }
                  }
               }
            }
         }

         if (!override && !exist.isEmpty()) {
            SendMessage.warnMessage(logger, "Failed to migrate data for shops " + Arrays.toString(exist.toArray()) + " because they already exist. If you wish to override them, you can use this command with the " + ChatColor.WHITE + "--override " + ChatColor.RED + "flag.");
         }

         if (!changes) {
            SendMessage.warnMessage(logger, "No changes found to migrate!");
            return;
         }

         SendMessage.infoMessage(logger, "Completed migrating data for " + migrated + " shops and sections which can now be found in " + ChatColor.WHITE + "/sections/<shopName>.yml" + ChatColor.GREEN + " and " + ChatColor.WHITE + "/shops/<shopName.yml>");
         SendMessage.infoMessage(logger, "Use command " + ChatColor.WHITE + "/sreload" + ChatColor.GREEN + " to load the migrated shops.");
      }

   }

   private Map<String, Object> getSectionData(YamlConfiguration config, String section) {
      Set<String> keys = config.getConfigurationSection("ShopSections." + section).getKeys(false);
      Map<String, Object> newData = new LinkedHashMap();
      keys.remove("enable");
      newData.put("enable", config.getBoolean("ShopSections." + section + ".enable", true));
      keys.remove("slot");
      newData.put("slot", config.getInt("ShopSections." + section + ".slot"));
      keys.remove("title");
      newData.put("title", config.getString("ShopSections." + section + ".title", ""));
      keys.remove("hidden");
      newData.put("hidden", config.getBoolean("ShopSections." + section + ".hidden", false));
      keys.remove("subSection");
      newData.put("sub-section", config.getBoolean("ShopSections." + section + ".subSection", false));
      keys.remove("fill-item");
      newData.put("display-item", config.getBoolean("ShopSections." + section + ".fill-item", false));
      if (keys.remove("economy")) {
         newData.put("economy", config.getString("ShopSections." + section + ".economy"));
      }

      if (!keys.contains("fillItem")) {
         newData.put("fill-item.material", "AIR");
      } else {
         Iterator var5 = config.getConfigurationSection("ShopSections." + section + ".fillItem").getKeys(false).iterator();

         while(var5.hasNext()) {
            String key = (String)var5.next();
            newData.put("fill-item." + key, config.get("ShopSections." + section + ".fillItem." + key));
         }
      }

      newData.put("nav-bar.mode", "INHERIT");
      Map<String, Object> data = new HashMap();

      String key;
      for(Iterator var9 = keys.iterator(); var9.hasNext(); data.put(key, config.get("ShopSections." + section + "." + key))) {
         key = (String)var9.next();
         if (key.equalsIgnoreCase("displayname")) {
            data.put("name", config.getString("ShopSections." + section + ".displayname"));
         }
      }

      newData.put("item", data);
      return newData;
   }

   public List<String> getTabCompletion(String[] args) {
      ArrayList completions;
      switch(args.length) {
      case 2:
         if (!args[1].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], this.supported.keySet(), completions);
            Collections.sort(completions);
            return completions;
         }

         return new ArrayList(this.supported.keySet());
      case 3:
         if (this.supported.containsKey(args[1].toLowerCase(Locale.ENGLISH))) {
            if (!args[2].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[2], (Iterable)this.supported.get(args[1].toLowerCase(Locale.ENGLISH)), completions);
               Collections.sort(completions);
               return completions;
            }

            return (List)this.supported.get(args[1].toLowerCase(Locale.ENGLISH));
         }

         return null;
      default:
         return null;
      }
   }
}
