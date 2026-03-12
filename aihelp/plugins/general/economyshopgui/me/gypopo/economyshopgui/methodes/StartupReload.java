package me.gypopo.economyshopgui.methodes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.VersionHandler;
import me.gypopo.economyshopgui.api.events.ShopItemsLoadEvent;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.objects.LoreFormatter;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.ShopSection;
import me.gypopo.economyshopgui.objects.TransactionMenus;
import me.gypopo.economyshopgui.providers.StandProvider;
import me.gypopo.economyshopgui.util.BackButton;
import me.gypopo.economyshopgui.util.NBTMaps;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.SortOrder;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class StartupReload {
   private EconomyShopGUI plugin;

   public StartupReload(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public void loadInventoryTitles() {
      this.plugin.getSectionTitles().clear();
      HashMap<String, Translatable> newSectionTitles = new HashMap();
      Iterator var2 = this.plugin.getShopSections().iterator();

      while(true) {
         while(var2.hasNext()) {
            String section = (String)var2.next();
            String title = ConfigManager.getSection(section).getString("title");
            if (title == null || title.isEmpty()) {
               title = ConfigManager.getSection(section).getString("item.name");
            }

            if (title != null && !title.isEmpty()) {
               newSectionTitles.put(section, Lang.fromConfig(this.plugin.getInvTitle(title)));
            } else {
               newSectionTitles.put(section, Lang.DISPLAYNAME_NULL.get());
            }
         }

         this.plugin.setSectionTitles(newSectionTitles);
         return;
      }
   }

   public void checkIfLanguageFilesExist() {
      String[] supported = new String[]{"lang-cz.yml", "lang-de.yml", "lang-en.yml", "lang-es.yml", "lang-fr.yml", "lang-hu.yml", "lang-it.yml", "lang-jp.yml", "lang-ko.yml", "lang-nl.yml", "lang-pl.yml", "lang-pt.yml", "lang-ru.yml", "lang-sk.yml", "lang-sv.yml", "lang-tr.yml", "lang-uk.yml", "lang-vi.yml", "lang-zh_cn.yml", "lang-zh_tw.yml"};
      String[] var2 = supported;
      int var3 = supported.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String fileName = var2[var4];
         if (!(new File(this.plugin.getDataFolder() + File.separator + "LanguageFiles", fileName)).exists()) {
            this.plugin.saveResource("LanguageFiles" + File.separator + fileName, false);
         }
      }

   }

   public void loadItems() {
      SendMessage.infoMessage(Lang.LOADING_ITEMS.get());
      if (ConfigManager.getConfig().getBoolean("shop-stands.enable")) {
         if (this.plugin.getStandProvider() == null) {
            this.plugin.setStandProvider(new StandProvider(this.plugin));
            this.plugin.shopStands = true;
         } else {
            this.plugin.getStandProvider().reload();
         }
      }

      this.plugin.createItem.reloadPrices();
      this.plugin.boughtItemsLore = ConfigManager.getConfig().getBoolean("bought-items-lore");
      this.plugin.allowUnsafeEnchants = ConfigManager.getConfig().getBoolean("allow-unsafe-enchants");
      this.plugin.discountsActive = ConfigManager.getConfig().getBoolean("enable-discounts");
      this.plugin.multipliers = ConfigManager.getConfig().getBoolean("enable-sell-multipliers");
      this.plugin.ignoredNBTData = (List)ConfigManager.getConfig().getStringList("sold-items-ignored-NBTtags").stream().map((s) -> {
         return NBTMaps.getTag(s, (String)null, "sold-items-ignored-NBTtags");
      }).collect(Collectors.toList());
      if (ConfigManager.getConfig().getBoolean("allow-renamed-items")) {
         this.plugin.ignoredNBTData.add(ServerInfo.supportsComponents() ? "custom_name" : "display::Name");
      }

      if (ConfigManager.getConfig().getBoolean("allow-lore-items")) {
         this.plugin.ignoredNBTData.add(ServerInfo.supportsComponents() ? "lore" : "display::Lore");
      }

      this.plugin.dropItemsOnGround = ConfigManager.getConfig().getBoolean("drop-remaining-items-on-ground");
      this.plugin.prioritizeItemLore = ConfigManager.getConfig().getBoolean("prioritize-item-lore");
      this.plugin.MMB = ConfigManager.getConfig().getBoolean("shift-right-click-sell-all", true);
      this.plugin.maxShopSize = ConfigManager.getConfig().getInt("max-shop-pages", 50);
      this.plugin.allowIllegalStacks = ConfigManager.getConfig().getBoolean("allow-illegal-stacks");
      this.plugin.useItemName = ConfigManager.getConfig().getBoolean("use-item-name", true);
      this.plugin.matchMeta = ConfigManager.getConfig().getBoolean("match-item-meta", true);
      this.plugin.navBar.loadNavBars();
      this.plugin.navBar.createCustomPlayerProfile();
      this.plugin.mainMenuSize = CalculateAmount.getInvSlots(ConfigManager.getConfig().getInt("main-menu.gui-rows", 6));
      this.plugin.setMainMenuItemSlots(this.plugin.calculateAmount.getMainMenuItemSlots());
      this.plugin.seasonalLore = ConfigManager.getConfig().getBoolean("seasonal-lore.enabled");
      this.plugin.getTransactionLog().saveUnsaved();
      SendMessage.init();
      BackButton.init();
      this.plugin.bedrock = this.plugin.getServer().getPluginManager().getPlugin("floodgate") != null;
      this.plugin.resizeGUI = ConfigManager.getConfig().getBoolean("resize-gui-for-bedrock", true);
      this.plugin.bannedGamemodes = (List)ConfigManager.getConfig().getStringList("banned-gamemodes").stream().filter((s) -> {
         try {
            GameMode.valueOf(s);
            return true;
         } catch (IllegalArgumentException var2) {
            SendMessage.warnMessage("Failed to load banned gamemode like '" + s + "'");
            return false;
         }
      }).map(GameMode::valueOf).collect(Collectors.toList());
      if (this.plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
         this.plugin.loadChat();
      }

      if (this.plugin.getServer().getPluginManager().isPluginEnabled("Essentials") && this.plugin.getServer().getPluginManager().getPlugin("Essentials").getConfig().getBoolean("change-displayname") && this.plugin.getServer().getPluginManager().getPlugin("Essentials").getConfig().getBoolean("add-prefix-suffix", this.plugin.getServer().getPluginManager().isPluginEnabled("EssentialsChat"))) {
         this.plugin.prefixSuffix = false;
      }

      this.plugin.sellShulkers = ConfigManager.getConfig().getBoolean("sell-shulker-boxes", true);
      this.plugin.setLoreFormatter(new LoreFormatter(this.plugin));
      HashMap<Integer, ItemStack> newSectionItems = new HashMap();
      Iterator var2 = this.plugin.getMainMenuItemSlots().keySet().iterator();

      while(var2.hasNext()) {
         int slot = (Integer)var2.next();
         String section = this.plugin.getMainMenuSectionForSlot(slot);

         ItemStack item;
         try {
            item = this.plugin.createItem.loadSectionItem(section, "item");
         } catch (ItemLoadException var8) {
            item = this.plugin.createItem.createItem(XMaterial.BARRIER.parseItem(), ChatColor.RED + var8.getMessage());
            SendMessage.logDebugMessage(var8.getMessage());
            SendMessage.logDebugMessage(Lang.ITEMS_PATH_IN_SECTIONS_CONFIG.get().replace("%location%", section));
         }

         newSectionItems.put(slot, item);
         if (ConfigManager.getSection(section).getBoolean("display-item")) {
            this.plugin.removeShopSection(section);
         }
      }

      this.plugin.setSectionItems(newSectionItems);
      var2 = this.plugin.getShopSections().iterator();

      while(var2.hasNext()) {
         String section = (String)var2.next();

         try {
            this.plugin.addSection(section, new ShopSection(this.plugin, section));
         } catch (Exception var7) {
            SendMessage.errorMessage("Failed to load shop " + section);
            var7.printStackTrace();
         }
      }

      TransactionMenus.load(this.plugin);
      if (ConfigManager.getConfig().getBoolean("main-menu.fill-item.enable", true)) {
         ItemStack fillitem = this.plugin.createItem.createFillItem((String)null, ConfigManager.getConfig().getConfigurationSection("main-menu.fill-item"));
         if (fillitem != null) {
            this.plugin.createItem.fillItem = fillitem;
         } else {
            SendMessage.warnMessage("Failed to find fill item inside config.yml");
            SendMessage.logDebugMessage(Lang.ITEMS_PATH_IN_CONFIG.get().replace("%location%", "main-menu.fill-item"));
         }
      } else {
         this.plugin.createItem.fillItem = null;
      }

      if (this.plugin.discountsActive) {
         this.setupDiscounts();
      }

      if (this.plugin.multipliers) {
         this.loadSellMultipliers();
      }

      this.plugin.shopItemsByMaterialName = this.getShopItemsByMaterialName();
      this.putShopItemsByOrder();
      Bukkit.getPluginManager().callEvent(new ShopItemsLoadEvent());
   }

   private void setupDiscounts() {
      HashMap<String, HashMap<String, Double>> sectionDiscounts = new HashMap();
      Iterator var2 = ConfigManager.getConfig().getConfigurationSection("discounts").getKeys(false).iterator();

      while(true) {
         while(true) {
            while(var2.hasNext()) {
               String section = (String)var2.next();
               if (!section.equals("*") && !section.equals("all")) {
                  if (this.plugin.getShopSections().contains(section)) {
                     HashMap<String, Double> discounts = (HashMap)sectionDiscounts.getOrDefault(section, new HashMap());
                     Iterator var12 = ConfigManager.getConfig().getConfigurationSection("discounts." + section).getKeys(false).iterator();

                     while(var12.hasNext()) {
                        String group = (String)var12.next();
                        double discount = ConfigManager.getConfig().getDouble("discounts." + section + "." + group);
                        if (discount != 0.0D) {
                           discounts.put(group, discount);
                        }
                     }

                     if (!discounts.isEmpty()) {
                        sectionDiscounts.put(section, discounts);
                     }
                  } else {
                     SendMessage.warnMessage(Lang.CANNOT_FIND_DISCOUNTED_SECTION.get().replace("%section%", section));
                  }
               } else {
                  Iterator var4 = this.plugin.getShopSections().iterator();

                  while(var4.hasNext()) {
                     String s = (String)var4.next();
                     HashMap<String, Double> discounts = (HashMap)sectionDiscounts.getOrDefault(s, new HashMap());
                     Iterator var7 = ConfigManager.getConfig().getConfigurationSection("discounts." + section).getKeys(false).iterator();

                     while(var7.hasNext()) {
                        String group = (String)var7.next();
                        double discount = ConfigManager.getConfig().getDouble("discounts." + section + "." + group);
                        if (discount != 0.0D) {
                           discounts.put(group, discount);
                        }
                     }

                     if (!discounts.isEmpty()) {
                        sectionDiscounts.put(s, discounts);
                     }
                  }
               }
            }

            if (!sectionDiscounts.isEmpty()) {
               this.plugin.setDiscounts(sectionDiscounts);
            }

            return;
         }
      }
   }

   private void loadSellMultipliers() {
      HashMap<String, HashMap<String, Double>> sectionMultipliers = new HashMap();
      Iterator var2 = ConfigManager.getConfig().getConfigurationSection("sell-multipliers").getKeys(false).iterator();

      while(true) {
         while(true) {
            while(var2.hasNext()) {
               String section = (String)var2.next();
               if (!section.equals("*") && !section.equals("all")) {
                  if (this.plugin.getShopSections().contains(section)) {
                     HashMap<String, Double> multipliers = (HashMap)sectionMultipliers.getOrDefault(section, new HashMap());
                     Iterator var12 = ConfigManager.getConfig().getConfigurationSection("sell-multipliers." + section).getKeys(false).iterator();

                     while(var12.hasNext()) {
                        String group = (String)var12.next();
                        double multiplier = ConfigManager.getConfig().getDouble("sell-multipliers." + section + "." + group);
                        if (multiplier != 0.0D) {
                           multipliers.put(group, multiplier);
                        }
                     }

                     if (!multipliers.isEmpty()) {
                        sectionMultipliers.put(section, multipliers);
                     }
                  } else {
                     SendMessage.warnMessage("Cannot add sell multipliers for section '%section%', either the shop section is disabled or not found.".replace("%section%", section));
                  }
               } else {
                  Iterator var4 = this.plugin.getShopSections().iterator();

                  while(var4.hasNext()) {
                     String s = (String)var4.next();
                     HashMap<String, Double> multipliers = (HashMap)sectionMultipliers.getOrDefault(s, new HashMap());
                     Iterator var7 = ConfigManager.getConfig().getConfigurationSection("sell-multipliers." + section).getKeys(false).iterator();

                     while(var7.hasNext()) {
                        String group = (String)var7.next();
                        double multiplier = ConfigManager.getConfig().getDouble("sell-multipliers." + section + "." + group);
                        if (multiplier != 0.0D) {
                           multipliers.put(group, multiplier);
                        }
                     }

                     if (!multipliers.isEmpty()) {
                        sectionMultipliers.put(s, multipliers);
                     }
                  }
               }
            }

            if (!sectionMultipliers.isEmpty()) {
               this.plugin.setSellMultipliers(sectionMultipliers);
            }

            return;
         }
      }
   }

   private Map<String, List<ShopItem>> getShopItemsByMaterialName() {
      Map<String, List<ShopItem>> mats = new HashMap();
      Iterator var2 = this.plugin.getShopSections().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         me.gypopo.economyshopgui.objects.shops.ShopSection section = this.plugin.getSection(s);
         Iterator var5 = section.getShopItems().iterator();

         while(var5.hasNext()) {
            ShopItem shopItem = (ShopItem)var5.next();
            if (!shopItem.hasItemError() && !shopItem.isDisplayItem()) {
               String mat = shopItem.getShopItem().getType().name();
               List<ShopItem> items = mats.containsKey(mat) ? (List)mats.get(mat) : new ArrayList();
               ((List)items).add(shopItem);
               mats.put(mat, items);
            }
         }
      }

      return mats;
   }

   public void putShopItemsByOrder() {
      SortOrder order = SortOrder.fromConfig();
      if (order != SortOrder.SHOP_LOAD_ORDER) {
         Iterator var2 = this.plugin.shopItemsByMaterialName.keySet().iterator();

         while(var2.hasNext()) {
            String mat = (String)var2.next();
            List<ShopItem> items = (List)this.plugin.shopItemsByMaterialName.get(mat);
            if (items.size() >= 2) {
               switch(order) {
               case DESCENDING_SELL_PRICE:
                  items.sort((i1, i2) -> {
                     return Double.compare(i2.getSellPrice(), i1.getSellPrice());
                  });
                  break;
               case ASCENDING_SELL_PRICE:
                  items.sort((i1, i2) -> {
                     return Double.compare(i1.getSellPrice(), i2.getSellPrice());
                  });
               }
            }
         }

      }
   }

   public void updateAvailable() {
      if (this.plugin.updateChecker != null) {
         this.plugin.updateChecker.checkForUpdates();
      }

   }

   public void setupPluginVersion() {
      ServerInfo.Version version = ServerInfo.getVersion();
      if (version == null) {
         SendMessage.errorMessage("===================================================");
         SendMessage.errorMessage(" ");
         SendMessage.errorMessage(Lang.COULD_NOT_FIND_VALID_VERSION.get());
         SendMessage.errorMessage("Version: " + (ServerInfo.supportsPaper() && ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R6) && ServerInfo.usesMojangMappings() ? "paper_" : "") + Bukkit.getBukkitVersion());
         SendMessage.errorMessage(" ");
         SendMessage.errorMessage("===================================================");
      } else {
         try {
            String packageName = EconomyShopGUI.class.getPackage().getName() + ".versions";
            this.plugin.versionHandler = (VersionHandler)Class.forName(packageName + "." + (ServerInfo.supportsPaper() && ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R6) && ServerInfo.usesMojangMappings() ? "paper_" : "") + version.name() + "." + version.name()).newInstance();
            SendMessage.infoMessage(Lang.MINECRAFT_VERSION_USING.get().replace("%version%", Bukkit.getBukkitVersion().split("-")[0]));
         } catch (InstantiationException | IllegalAccessException | ClassCastException | ClassNotFoundException var3) {
            SendMessage.errorMessage("===================================================");
            SendMessage.errorMessage(" ");
            SendMessage.errorMessage("You are running a unsupported server version, please use a supported server version or update the plugin");
            SendMessage.errorMessage("Version: " + Bukkit.getBukkitVersion());
            SendMessage.errorMessage(" ");
            SendMessage.errorMessage("===================================================");
            var3.printStackTrace();
         }

      }
   }

   public void checkDebugMode() {
      if (ConfigManager.getConfig().getBoolean("debug", true)) {
         SendMessage.infoMessage(Lang.DEBUG_MODE_ENABLED.get());
      }

   }
}
