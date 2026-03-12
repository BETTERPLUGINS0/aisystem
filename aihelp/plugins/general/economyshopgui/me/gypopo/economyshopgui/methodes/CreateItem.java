package me.gypopo.economyshopgui.methodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.files.lang.TranslatableRaw;
import me.gypopo.economyshopgui.objects.ActionItem;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.TransactionItem;
import me.gypopo.economyshopgui.objects.TransactionScreen;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.FireworkUtil;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.XEnchantment;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.persistence.PersistentDataType;

public class CreateItem {
   private final EconomyShopGUI plugin;
   private final CreateItemMethodes itemMethodes;
   private Map<String, Double> buyPrices = new HashMap();
   private Map<String, Double> sellPrices = new HashMap();
   public ItemStack fillItem;

   public CreateItem(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.itemMethodes = new CreateItemMethodes();
   }

   public Double getBaseBuyPrice(String itemPath) {
      return (Double)this.buyPrices.get(itemPath);
   }

   public Double getBaseSellPrice(String itemPath) {
      return (Double)this.sellPrices.get(itemPath);
   }

   public void addNewBuyPrice(String itemPath, Double buyPrice) {
      this.buyPrices.put(itemPath, buyPrice);
   }

   public void addNewSellPrice(String itemPath, Double sellPrice) {
      this.sellPrices.put(itemPath, sellPrice);
   }

   public void reloadPrices() {
      if (!this.buyPrices.isEmpty()) {
         this.buyPrices.clear();
      }

      if (!this.sellPrices.isEmpty()) {
         this.sellPrices.clear();
      }

   }

   public ItemStack loadShopSectionItem(ShopItem shopItem) throws ItemLoadException {
      ConfigurationSection config;
      if (ConfigManager.getShop(shopItem.section).contains("pages." + shopItem.itemLoc + ".display-item")) {
         config = ConfigManager.getShop(shopItem.section).getConfigurationSection("pages." + shopItem.itemLoc + ".display-item");
      } else {
         config = ConfigManager.getShop(shopItem.section).getConfigurationSection("pages." + shopItem.itemLoc);
      }

      if (config != null && !config.getKeys(false).isEmpty()) {
         ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
         if (item.getType() == Material.AIR) {
            return item;
         } else {
            if (config.contains("buy")) {
               this.addNewBuyPrice(shopItem.getItemPath(), config.getDouble("buy"));
            }

            if (config.contains("sell")) {
               this.addNewSellPrice(shopItem.getItemPath(), config.getDouble("sell"));
            }

            Translatable displayname = this.itemMethodes.getDisplayname(config);
            List<Translatable> lore = this.getDisplayLore(config);
            if (displayname != null || !lore.isEmpty()) {
               ItemMeta meta = item.getItemMeta();
               if (displayname != null) {
                  this.plugin.getMetaUtils().setDisplayName(meta, displayname);
               }

               if (!lore.isEmpty()) {
                  this.plugin.getMetaUtils().setLore(meta, lore);
               }

               item.setItemMeta(meta);
            }

            item = this.itemMethodes.setOption(shopItem, item, config, shopItem.section, true);

            try {
               if (EconomyShopGUI.getInstance().allowIllegalStacks && ServerInfo.supportsComponents()) {
                  item = (ItemStack)ServerInfo.invokeModernVersionMethod("setComponent", new Class[]{ItemStack.class, String.class, Object.class}, item, "max_stack_size", 64);
               }
            } catch (Exception var7) {
            }

            return this.plugin.versionHandler.setPathToItem(item, shopItem.getItemPath());
         }
      } else {
         throw new ItemLoadException(Lang.ITEM_NULL.get());
      }
   }

   public ItemStack loadActionItem(ActionItem actionItem) throws ItemLoadException {
      ConfigurationSection config = ConfigManager.getShop(actionItem.section).getConfigurationSection("pages." + actionItem.itemLoc);
      ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
      if (item.getType() == Material.AIR) {
         return null;
      } else {
         Translatable displayname = this.itemMethodes.getDisplayname(config);
         List<Translatable> lore = this.getDisplayLore(config);
         if (displayname != null || !lore.isEmpty()) {
            ItemMeta meta = item.getItemMeta();
            if (displayname != null) {
               this.plugin.getMetaUtils().setDisplayName(meta, displayname);
            }

            if (!lore.isEmpty()) {
               this.plugin.getMetaUtils().setLore(meta, lore);
            }

            item.setItemMeta(meta);
         }

         item = this.itemMethodes.setOption(actionItem, item, config, actionItem.section, true);

         try {
            if (EconomyShopGUI.getInstance().allowIllegalStacks && ServerInfo.supportsComponents()) {
               item = (ItemStack)ServerInfo.invokeModernVersionMethod("setComponent", new Class[]{ItemStack.class, String.class, Object.class}, item, "max_stack_size", 64);
            }
         } catch (Exception var7) {
         }

         return this.plugin.versionHandler.setPathToItem(item, actionItem.getItemPath());
      }
   }

   public ItemStack loadSectionItem(String section, String path) throws ItemLoadException {
      ConfigurationSection config = ConfigManager.getSection(section).getConfigurationSection(path);
      if (config != null && !config.getKeys(false).isEmpty()) {
         ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
         if (item.getType() == Material.AIR) {
            throw new ItemLoadException("Item material cannot be AIR");
         } else {
            Translatable displayname = this.itemMethodes.getDisplayname(config);
            List<Translatable> lore = this.getDisplayLore(config);
            if (displayname != null || !lore.isEmpty() || config.getBoolean("hideDefaultLore", true)) {
               ItemMeta meta = item.getItemMeta();
               if (displayname != null) {
                  this.plugin.getMetaUtils().setDisplayName(meta, displayname);
               }

               if (!lore.isEmpty()) {
                  this.plugin.getMetaUtils().setLore(meta, lore);
               }

               if (config.getBoolean("hideDefaultLore", true)) {
                  meta.addItemFlags(ItemFlag.values());
               }

               item.setItemMeta(meta);
            }

            item = this.itemMethodes.setSectionItemOption(item, section, config);
            return item;
         }
      } else {
         throw new ItemLoadException(Lang.ITEM_NULL.get());
      }
   }

   public ItemStack loadItemToGive(ShopItem shopItem) throws ItemLoadException {
      ConfigurationSection config = ConfigManager.getShop(shopItem.section).getConfigurationSection("pages." + shopItem.itemLoc);
      CreateItemMethodes var10000 = this.itemMethodes;
      ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
      if (item.getType() == Material.AIR) {
         return item;
      } else {
         Translatable displayname = this.getName(config);
         List<Translatable> lore = this.getLore(config);
         if (displayname != null || !lore.isEmpty()) {
            ItemMeta meta = item.getItemMeta();
            if (displayname != null) {
               this.plugin.getMetaUtils().setDisplayName(meta, displayname);
            }

            if (!lore.isEmpty() && this.plugin.boughtItemsLore) {
               this.plugin.getMetaUtils().setLore(meta, lore);
            }

            item.setItemMeta(meta);
         }

         item = this.itemMethodes.setOption(shopItem, item, config, shopItem.section, false);
         return item;
      }
   }

   public ItemStack getInvalidShopItem(String section, String locateItem) {
      ItemStack item = (new ItemBuilder(XMaterial.BARRIER.parseMaterial())).withDisplayName(Lang.ITEM_ERROR.get()).build();
      item = this.plugin.versionHandler.setPathToItem(item, section + "." + locateItem);
      return item;
   }

   public ItemStack getInvalidShopItem(String section, String locateItem, ItemLoadException e) {
      List<String> lore = CalculateAmount.splitLongString(e.getMessage());
      lore.replaceAll((s) -> {
         return ChatUtil.formatColors("&c" + s);
      });
      ItemStack item = new ItemStack(Material.BARRIER);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName((String)lore.remove(0));
      if (!lore.isEmpty()) {
         meta.setLore(lore);
      }

      item.setItemMeta(meta);
      return item;
   }

   private ItemStack getTransactionItem(String path, Translatable displayname) {
      ItemStack item;
      try {
         item = CreateItemMethodes.createItemMaterialFromString(ConfigManager.getConfig().getString(path + ".material"));
         return item.getType() != Material.AIR ? (new ItemBuilder(item)).setAmount(1).withDisplayName(displayname).build() : item;
      } catch (ItemLoadException var5) {
         SendMessage.logDebugMessage(var5.getMessage());
         SendMessage.errorItemConfig(path);
         item = ((XMaterial)XMaterial.matchXMaterial(ConfigManager.getConfig().getDef().getString(path + ".material")).get()).parseItem();
         return (new ItemBuilder(item)).setAmount(1).withDisplayName(displayname).build();
      }
   }

   public List<ItemStack> getAllowedTools() {
      List<ItemStack> allowedTools = new ArrayList();
      ConfigurationSection section = ConfigManager.getConfig().getConfigurationSection("spawner-break-tools");
      if (section == null) {
         return new ArrayList();
      } else {
         Iterator var3 = section.getKeys(false).iterator();

         while(var3.hasNext()) {
            String key = (String)var3.next();

            try {
               CreateItemMethodes var10000 = this.itemMethodes;
               ItemStack item = CreateItemMethodes.createItemMaterialFromString(section.getString(key + ".material"));
               allowedTools.add(this.itemMethodes.setEnchantments(item, (String)null, section.getConfigurationSection(key)));
            } catch (ItemLoadException var6) {
               SendMessage.logDebugMessage(var6.getMessage());
               SendMessage.errorItemConfig("spawner-break-tools." + key);
            }
         }

         return allowedTools;
      }
   }

   public boolean isShopItem(ItemStack itemToMatch, ItemStack shopItem) {
      if (!itemToMatch.getType().equals(shopItem.getType())) {
         return false;
      } else {
         return itemToMatch.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? this.plugin.getSpawnerManager().getProvider().isShopSpawner(itemToMatch, shopItem) : this.plugin.isSimilar(itemToMatch, shopItem);
      }
   }

   public double getShopItemSellPrice(ItemStack itemToSell, String itemPath) {
      double price = this.getBaseSellPrice(itemPath);
      return itemToSell.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? this.plugin.getSpawnerManager().getProvider().getSpawnerSellPrice(itemToSell, price) : price * (double)itemToSell.getAmount();
   }

   /** @deprecated */
   @Deprecated
   public ShopItem matchShopItem(Player player, ItemStack itemToLookFor) {
      if (this.plugin.shopItemsByMaterialName.containsKey(itemToLookFor.getType().name())) {
         Iterator var3 = ((List)this.plugin.shopItemsByMaterialName.get(itemToLookFor.getType().name())).iterator();

         while(var3.hasNext()) {
            ShopItem shopItem = (ShopItem)var3.next();
            if (PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + shopItem.section) && shopItem.match(itemToLookFor) && shopItem.meetsRequirements(player, true)) {
               return shopItem;
            }
         }
      }

      return null;
   }

   public ShopItem matchShopItem(Player player, ItemStack itemToLookFor, Transaction.Mode method) {
      if (this.plugin.shopItemsByMaterialName.containsKey(itemToLookFor.getType().name())) {
         Iterator var4 = ((List)this.plugin.shopItemsByMaterialName.get(itemToLookFor.getType().name())).iterator();

         ShopItem shopItem;
         do {
            while(true) {
               do {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  shopItem = (ShopItem)var4.next();
               } while(!PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + shopItem.section));

               if (method == Transaction.Mode.BUY) {
                  if (shopItem.getBuyPrice() < 0.0D) {
                     continue;
                  }
               } else if (shopItem.getSellPrice() < 0.0D) {
                  continue;
               }
               break;
            }
         } while(shopItem.isDisplayItem() || !shopItem.match(itemToLookFor) || !shopItem.meetsRequirements(player, true));

         return shopItem;
      } else {
         return null;
      }
   }

   public ShopItem matchShopItem(ItemStack itemToLookFor) {
      if (this.plugin.shopItemsByMaterialName.containsKey(itemToLookFor.getType().name())) {
         Iterator var2 = ((List)this.plugin.shopItemsByMaterialName.get(itemToLookFor.getType().name())).iterator();

         while(var2.hasNext()) {
            ShopItem shopItem = (ShopItem)var2.next();
            if (!shopItem.isDisplayItem() && shopItem.match(itemToLookFor)) {
               return shopItem;
            }
         }
      }

      return null;
   }

   public ShopItem matchShopItem(ItemStack itemToLookFor, Player player, String root) {
      if (this.plugin.shopItemsByMaterialName.containsKey(itemToLookFor.getType().name())) {
         Iterator var4 = ((List)this.plugin.shopItemsByMaterialName.get(itemToLookFor.getType().name())).iterator();

         while(var4.hasNext()) {
            ShopItem shopItem = (ShopItem)var4.next();
            if (PermissionsCache.hasPermission(player, "EconomyShopGUI." + root + "." + shopItem.section) && !(shopItem.getSellPrice() < 0.0D) && !shopItem.isDisplayItem() && shopItem.match(itemToLookFor) && shopItem.meetsRequirements(player, true)) {
               return shopItem;
            }
         }
      }

      return null;
   }

   public ItemStack createSpawner(EntityType type) {
      ItemBuilder builder = new ItemBuilder(XMaterial.SPAWNER.parseItem());
      builder.setDisplayName(SpawnerManager.getDefaultName(type));
      return builder.build();
   }

   public ItemStack createItem(ItemStack item, String Displayname, String... lore) {
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(Displayname);
      if (lore != null) {
         List<String> customlore = new ArrayList();
         customlore.addAll(Arrays.asList(lore));
         meta.setLore(customlore);
      }

      item.setItemMeta(meta);
      return item;
   }

   public ItemStack createFillItem(String section, ConfigurationSection config) {
      if (config == null) {
         return null;
      } else {
         try {
            ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
            if (item.getType() == Material.AIR) {
               return item;
            } else {
               ItemMeta meta = item.getItemMeta();
               if (config.contains("name")) {
                  meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("name")));
               }

               if (config.contains("lore")) {
                  meta.setLore((List)config.getStringList("lore").stream().map((s) -> {
                     return ChatColor.translateAlternateColorCodes('&', s);
                  }).collect(Collectors.toList()));
               }

               if (config.contains("enchantment-glint")) {
                  meta.addEnchant(XEnchantment.WATER_WORKER.parseEnchantment(), 1, false);
                  meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
               }

               if (config.contains("skullowner")) {
                  if (!XMaterial.matchXMaterial(item).equals(XMaterial.PLAYER_HEAD)) {
                     throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
                  }

                  SkullUtil.setSkullTexture(item, (SkullMeta)meta, config.getString("skullowner"), false);
               }

               item.setItemMeta(meta);
               return item;
            }
         } catch (ItemLoadException var5) {
            SendMessage.warnMessage(var5.getMessage());
            if (section == null) {
               SendMessage.errorItemConfig(config.getCurrentPath());
            } else {
               SendMessage.errorSections(section, config.getCurrentPath());
            }

            return null;
         }
      }
   }

   public HashMap<String, Object> getShopItem(ItemStack item, double buyPrice, double sellPrice) {
      HashMap<String, Object> keys = new HashMap();
      if (this.plugin.version > 112) {
         keys.put("material", item.getType().name());
      } else if (XMaterial.matchXMaterial(item.getType().name() + ":" + item.getDurability()).isPresent()) {
         keys.put("material", item.getType().name() + ":" + item.getDurability());
      } else {
         SendMessage.warnMessage(Lang.ITEM_MATERIAL_NULL.get().replace("%material%", item.getType().name() + ":  " + item.getDurability()));
      }

      keys.put("buy", buyPrice);
      keys.put("sell", sellPrice);
      if (item.getAmount() > 1) {
         keys.put("stack-size", item.getAmount());
      }

      if (item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         if (meta.hasDisplayName()) {
            keys.put("name", meta.getDisplayName());
         }

         if (this.plugin.version >= 112 && meta instanceof KnowledgeBookMeta) {
            KnowledgeBookMeta bookMeta = (KnowledgeBookMeta)item.getItemMeta();
            List<String> recipes = new ArrayList();

            for(int i = 0; i < bookMeta.getRecipes().size(); ++i) {
               recipes.add(((NamespacedKey)bookMeta.getRecipes().get(i)).getKey());
            }

            keys.put("recipes", recipes);
         } else if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
            keys.put("skullowner", skullMeta.getOwner());
         } else if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)item.getItemMeta();
            Color color = leatherArmorMeta.getColor();
            keys.put("armorcolor", "#" + Integer.toHexString((new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue())).getRGB()).substring(2).toUpperCase());
         } else if (meta instanceof FireworkMeta) {
            keys.putAll(FireworkUtil.serialize((FireworkMeta)meta));
         } else if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_19_R3) && meta instanceof MusicInstrumentMeta) {
            MusicInstrumentMeta mm = (MusicInstrumentMeta)meta;
            keys.put("instrument", mm.getInstrument().getKey().getKey().replace("_goat_horn", ""));
         } else if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R1) && meta instanceof ArmorMeta) {
            ArmorMeta am = (ArmorMeta)meta;
            if (am.hasTrim()) {
               ArmorTrim trim = am.getTrim();
               keys.put("armor-trim.type", trim.getMaterial().getKey().getKey());
               keys.put("armor-trim.pattern", trim.getPattern().getKey().getKey());
            }
         } else if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R4) && meta instanceof OminousBottleMeta) {
            OminousBottleMeta bm = (OminousBottleMeta)meta;
            if (bm.hasAmplifier()) {
               keys.put("ominous-strength", bm.getAmplifier());
            }
         }

         ArrayList lore;
         if (!meta.getEnchants().isEmpty() || item.getType() == Material.ENCHANTED_BOOK) {
            lore = new ArrayList();
            (item.getType() == Material.ENCHANTED_BOOK ? ((EnchantmentStorageMeta)item.getItemMeta()).getStoredEnchants() : item.getEnchantments()).forEach((enchant, strength) -> {
               if (strength > enchant.getMaxLevel() && !this.plugin.allowUnsafeEnchants) {
                  strength = enchant.getMaxLevel();
               }

               lore.add((ServerInfo.supportsComponents() ? enchant.getKey().getKey().toUpperCase(Locale.ROOT) : enchant.getName()) + ":" + strength);
            });
            keys.put("enchantments", lore);
         }

         Iterator var20;
         if (PotionTypes.canHaveEffects(item.getType())) {
            lore = new ArrayList();
            var20 = PotionTypes.getPotionTypes(item).iterator();

            while(var20.hasNext()) {
               PotionTypes effect = (PotionTypes)var20.next();
               lore.add(effect.name());
            }

            keys.put("potiontypes", lore);
         }

         if (meta.hasLore()) {
            lore = new ArrayList();
            var20 = meta.getLore().iterator();

            while(var20.hasNext()) {
               String line = (String)var20.next();
               lore.add(line.replaceAll(String.valueOf('§'), "&"));
            }

            keys.put("lore", lore);
         }
      }

      if (this.plugin.version >= 116 && item.hasItemMeta()) {
         try {
            if ((Integer)item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Bukkit.getPluginManager().getPlugin("AutoSellChests"), "autosell"), PersistentDataType.INTEGER) == 1) {
               keys.put("autosell", true);
            }
         } catch (Exception var11) {
         }
      }

      if (item.getType().equals(XMaterial.SPAWNER.parseMaterial())) {
         keys.put("spawnertype", this.plugin.getSpawnerManager().getProvider().getSpawnedType(item));
      }

      return keys;
   }

   public TransactionScreen getTransactionScreen(Transaction.Type menu) {
      List<TransactionItem> items = new ArrayList();
      String section = menu.name().toLowerCase().replace("_", "-");
      int size = CalculateAmount.getInvSlots(ConfigManager.getConfig().getInt(section + ".menu-size", 5));
      if (ConfigManager.getConfig().contains(section + ".fill-item")) {
         ItemStack item = this.createFillItem((String)null, ConfigManager.getConfig().getConfigurationSection(section + ".fill-item"));
         items.add(new TransactionItem(CreateItem.TransactionItemAction.NONE, CreateItem.TransactionItemType.FILLITEM, item, new ArrayList(), false));
      }

      Iterator var14 = ConfigManager.getConfig().getConfigurationSection(section + ".items").getKeys(false).iterator();

      while(true) {
         while(var14.hasNext()) {
            String s = (String)var14.next();
            CreateItem.TransactionItemAction action = new CreateItem.TransactionItemAction(ConfigManager.getConfig().getString(section + ".items." + s + ".action", "NONE"));
            CreateItem.TransactionItemType type = CreateItem.TransactionItemType.getFromString(action, ConfigManager.getConfig().getString(section + ".items." + s + ".type", "NORMAL"));
            if (type != CreateItem.TransactionItemType.NORMAL && type != CreateItem.TransactionItemType.DECREASE_AMOUNT && type != CreateItem.TransactionItemType.INCREASE_AMOUNT && items.stream().filter((ix) -> {
               return ix.getType() == type;
            }).findFirst().orElse((Object)null) != null) {
               SendMessage.warnMessage("The " + section + " cannot contain multiple items from type " + type.name() + ", instead use the ability to copy this item into multiple slots by using 'slot: 9,13,14,15', skipping...");
               SendMessage.errorItemConfig(section + ".items." + s);
            } else {
               boolean dynamicItem = ConfigManager.getConfig().getString(section + ".items." + s + ".material", "").equalsIgnoreCase("%SELECTED_ITEM%") || type == CreateItem.TransactionItemType.SELECTED_ITEM;
               ItemStack item = this.getTransactionItem(type, section + ".items." + s, dynamicItem);
               List<Integer> slots = this.getSlots(ConfigManager.getConfig().getString(section + ".items." + s + ".slot", ConfigManager.getConfig().getDef().getString(section + ".items." + s + ".slot", (String)null)));
               if (slots == null) {
                  SendMessage.warnMessage("Invalid slot for item, skipping...");
                  SendMessage.errorItemConfig(section + ".items." + s);
               } else {
                  for(int i = slots.size() - 1; i > 0; --i) {
                     int slot = (Integer)slots.get(i);
                     if (slot >= (this.plugin.navBar.isEnableTransactionNav() ? size - 9 : size) || slot < 0) {
                        SendMessage.warnMessage("Item slot " + slot + " is out of bounds for menu size of " + size + ", skipping ...");
                        SendMessage.errorItemConfig(section + ".items." + s);
                        slots.remove(i);
                     }
                  }

                  items.add(new TransactionItem(action, type, item, slots, dynamicItem));
               }
            }
         }

         if (!items.stream().anyMatch((ix) -> {
            return ix.getAction().type == CreateItem.TransactionItemActionType.INSTA_BUY || ix.getAction().type == CreateItem.TransactionItemActionType.INSTA_SELL;
         }) && !items.stream().anyMatch((ix) -> {
            return ix.getType() == CreateItem.TransactionItemType.CONFIRM;
         })) {
            SendMessage.warnMessage("The transaction screen inside the config.yml at path '" + section + "' needs to contain atleast one item with type CONFIRM, adding default...");
            items.add(13, new TransactionItem(new CreateItem.TransactionItemAction(CreateItem.TransactionItemActionType.CONFIRM_TRANSACTION), CreateItem.TransactionItemType.CONFIRM, this.getTransactionItem(CreateItem.TransactionItemType.CONFIRM, section + ".items.7", false), Collections.singletonList(13), false));
         }

         return new TransactionScreen(items, size);
      }
   }

   private List<Integer> getSlots(String slots) {
      if (slots == null) {
         return null;
      } else {
         try {
            return new ArrayList(Collections.singletonList(Integer.parseInt(slots)));
         } catch (NumberFormatException var10) {
            ArrayList<Integer> pos = new ArrayList();
            slots = StringUtils.deleteWhitespace(slots);
            String[] var4 = slots.split(",");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String index = var4[var6];
               String[] params = index.split("-");
               if (params.length == 2) {
                  for(int i = Integer.parseInt(params[0]); i < Integer.parseInt(params[1]); ++i) {
                     pos.add(i);
                  }
               } else if (params.length == 1) {
                  pos.add(Integer.parseInt(params[0]));
               }
            }

            if (!pos.isEmpty()) {
               return pos;
            } else {
               return null;
            }
         }
      }
   }

   private ItemStack getTransactionItem(CreateItem.TransactionItemType type, String path, boolean dynamicItem) {
      ItemStack item;
      try {
         CreateItemMethodes var10000 = this.itemMethodes;
         item = CreateItemMethodes.createItemMaterialFromString(dynamicItem ? "BARRIER" : ConfigManager.getConfig().getString(path + ".material"));
         if (item.getType() == Material.AIR) {
            return null;
         } else {
            int amount = ConfigManager.getConfig().getInt(path + ".stack-size", 1);
            ItemBuilder builder = new ItemBuilder(item);
            if (amount >= 1) {
               builder.setAmount(amount);
            }

            if (ConfigManager.getConfig().contains(path + ".name")) {
               builder.setDisplayName(this.getTransactionItemName(ConfigManager.getConfig().getString(path + ".name")));
            }

            if (ConfigManager.getConfig().contains(path + ".lore")) {
               builder.withCLore(this.getTransactionItemLore(ConfigManager.getConfig().getStringList(path + ".lore")));
            }

            if (type == CreateItem.TransactionItemType.SELECTED_ITEM && !ConfigManager.getConfig().contains(path + ".lore")) {
               builder.setCLore(Lang.TRANSACTION_SCREEN_TOTAL_AMOUNT.get().getLines());
            }

            if (ConfigManager.getConfig().getBoolean(path + ".enchantment-glint")) {
               builder.withEnchantGlint();
            }

            if (ConfigManager.getConfig().getBoolean(path + ".hideDefaultLore")) {
               builder.hideFlags();
            }

            if (ConfigManager.getConfig().contains(path + ".skullowner")) {
               if (!XMaterial.matchXMaterial(item).equals(XMaterial.PLAYER_HEAD)) {
                  throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
               }

               builder.setOwner(ConfigManager.getConfig().getString(path + ".skullowner"));
            }

            item = builder.build();
            return item;
         }
      } catch (ItemLoadException var7) {
         SendMessage.warnMessage(var7.getMessage());
         SendMessage.errorItemConfig(path);
         if (!ConfigManager.getConfig().getDef().contains(path)) {
            return null;
         } else {
            item = ((XMaterial)XMaterial.matchXMaterial(ConfigManager.getConfig().getDef().getString(path + ".material")).get()).parseItem();
            return (new ItemBuilder(item)).setAmount(ConfigManager.getConfig().getDef().getInt(path + ".stack-size", 1)).withDisplayName(this.getTransactionItemName(ConfigManager.getConfig().getDef().getString(path + ".name"))).withCLore(this.getTransactionItemLore(ConfigManager.getConfig().getDef().getStringList(path + ".lore"))).build();
         }
      }
   }

   private Translatable getTransactionItemName(String s) {
      if (s == null) {
         return new TranslatableRaw("");
      } else {
         Translatable base = Lang.fromConfig(s);
         if (s.contains("%translations-")) {
            try {
               Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
               base = base.replace("%translations-" + placeholder.getKey() + "%", placeholder.get());
            } catch (IllegalArgumentException var4) {
            }
         }

         return base;
      }
   }

   private List<Translatable> getTransactionItemLore(List<String> lore) {
      return (List)(lore.isEmpty() ? new ArrayList() : (List)lore.stream().map((s) -> {
         Translatable base = Lang.fromConfig(s);
         if (s.contains("%translations-")) {
            try {
               Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
               base = base.replace("%translations-" + placeholder.getKey() + "%", placeholder.get());
            } catch (IllegalArgumentException var3) {
            }
         }

         return base;
      }).collect(Collectors.toList()));
   }

   public List<Translatable> getDisplayLore(ConfigurationSection itemConfig) {
      List<String> l = new ArrayList();
      if (itemConfig.contains("displaylore")) {
         l = itemConfig.getStringList("displaylore");
      } else if (itemConfig.contains("lore")) {
         l = itemConfig.getStringList("lore");
      }

      return (List)((List)l).stream().map(Lang::fromConfig).collect(Collectors.toList());
   }

   public Translatable getName(ConfigurationSection itemConfig) throws ItemLoadException {
      return itemConfig.contains("name") ? Lang.fromConfig(itemConfig.getString("name")) : null;
   }

   public List<Translatable> getLore(ConfigurationSection itemConfig) {
      List<String> l = new ArrayList();
      if (itemConfig.contains("lore")) {
         l = itemConfig.getStringList("lore");
      }

      return (List)((List)l).stream().map(Lang::fromConfig).collect(Collectors.toList());
   }

   public static final class TransactionItemAction {
      public static final CreateItem.TransactionItemAction NONE;
      public CreateItem.TransactionItemActionType type;
      public int amount = 1;

      public TransactionItemAction(CreateItem.TransactionItemActionType type) {
         this.type = type;
      }

      public TransactionItemAction(String s) {
         try {
            s = s.toUpperCase(Locale.ENGLISH).replace(" ", "_");
            if (s.startsWith("ADD")) {
               this.getFromStringAmount(CreateItem.TransactionItemActionType.ADD, s);
            } else if (s.startsWith("REMOVE")) {
               this.getFromStringAmount(CreateItem.TransactionItemActionType.REMOVE, s);
            } else {
               this.type = CreateItem.TransactionItemActionType.valueOf(s);
            }
         } catch (IllegalArgumentException var3) {
            SendMessage.warnMessage("Failed to load action for transaction item like '" + s + "'");
            this.type = CreateItem.TransactionItemActionType.NONE;
         }

      }

      public void getFromStringAmount(CreateItem.TransactionItemActionType action, String s) {
         this.type = action;

         try {
            this.amount = Math.max(Integer.parseInt(s.replace(action.name(), "")), 1);
         } catch (NumberFormatException var4) {
            SendMessage.warnMessage("Failed to get amount for transaction item action " + action.name() + " like '" + s + "'");
         }

      }

      static {
         NONE = new CreateItem.TransactionItemAction(CreateItem.TransactionItemActionType.NONE);
      }
   }

   public static enum TransactionItemType {
      INCREASE_AMOUNT,
      DECREASE_AMOUNT,
      SELECTED_ITEM,
      CONFIRM,
      FILLITEM,
      NORMAL;

      public static CreateItem.TransactionItemType getFromString(CreateItem.TransactionItemAction action, String s) {
         try {
            switch(action.type.ordinal()) {
            case 0:
               return DECREASE_AMOUNT;
            case 1:
               return INCREASE_AMOUNT;
            default:
               return valueOf(s.toUpperCase(Locale.ENGLISH).replace(" ", "_"));
            }
         } catch (NullPointerException | IllegalArgumentException var3) {
            SendMessage.warnMessage("Failed to load item type for transaction item like '" + s + "'");
            return NORMAL;
         }
      }

      // $FF: synthetic method
      private static CreateItem.TransactionItemType[] $values() {
         return new CreateItem.TransactionItemType[]{INCREASE_AMOUNT, DECREASE_AMOUNT, SELECTED_ITEM, CONFIRM, FILLITEM, NORMAL};
      }
   }

   public static enum TransactionItemActionType {
      REMOVE,
      ADD,
      CONFIRM_TRANSACTION,
      INSTA_BUY,
      INSTA_SELL,
      OPEN_BUY_STACKS,
      SELL_ALL,
      BACK,
      COMMAND,
      NONE;

      public static CreateItem.TransactionItemActionType get(String s) {
         return valueOf(s);
      }

      // $FF: synthetic method
      private static CreateItem.TransactionItemActionType[] $values() {
         return new CreateItem.TransactionItemActionType[]{REMOVE, ADD, CONFIRM_TRANSACTION, INSTA_BUY, INSTA_SELL, OPEN_BUY_STACKS, SELL_ALL, BACK, COMMAND, NONE};
      }
   }
}
