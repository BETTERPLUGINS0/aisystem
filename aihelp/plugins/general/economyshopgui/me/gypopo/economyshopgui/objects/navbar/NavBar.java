package me.gypopo.economyshopgui.objects.navbar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ActionItem;
import me.gypopo.economyshopgui.objects.MainMenu;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.ShopPage;
import me.gypopo.economyshopgui.objects.TransactionMenu;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.BackButton;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NavBar extends CreateItem {
   private final EconomyShopGUI plugin;
   private final Map<String, NavBar.InventoryNavBar> shopNavBars = new HashMap();
   private NavBar.InventoryNavBar mainMenuNavBar;
   private NavBar.InventoryNavBar shopsNavBar;
   private NavBar.InventoryNavBar transactionNavBar;
   private NavBar.InventoryNavBar shopStandsNavBar;
   private NavBar.InventoryNavBar sellGUINavBar;
   private ItemStack customPlayerProfile;

   public NavBar(EconomyShopGUI plugin) {
      super(plugin);
      this.plugin = plugin;
   }

   public boolean isEnabled(String shop) {
      return this.shopNavBars.containsKey(shop) ? ((NavBar.InventoryNavBar)this.shopNavBars.get(shop)).enabled : this.shopsNavBar.enabled;
   }

   public boolean isEnableMainNav() {
      return this.mainMenuNavBar.enabled;
   }

   public boolean isEnableTransactionNav() {
      return this.transactionNavBar.enabled;
   }

   public boolean isEnableShopStandsNav() {
      return this.shopStandsNavBar.enabled;
   }

   public boolean isEnableSellGUINav() {
      return this.sellGUINavBar.enabled;
   }

   public ItemStack getCustomPlayerProfile() {
      return this.customPlayerProfile != null ? new ItemStack(this.customPlayerProfile) : null;
   }

   public void execute(Player player, ShopPage page, int i, int cp, boolean disabledBackButton) {
      NavItem item = this.getItem(page.getSection(), i - (page.getSize() - 9));
      if (item != null && item.getAction() != null) {
         String var7 = item.getAction(cp, page.getAllPages());
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -595485545:
            if (var7.equals("PAGE_BACK")) {
               var8 = 1;
            }
            break;
         case -595123549:
            if (var7.equals("PAGE_NEXT")) {
               var8 = 2;
            }
            break;
         case 2030823:
            if (var7.equals("BACK")) {
               var8 = 0;
            }
            break;
         case 64218584:
            if (var7.equals("CLOSE")) {
               var8 = 3;
            }
         }

         switch(var8) {
         case 0:
            if (!disabledBackButton || page.isSubSection() && !BackButton.SUB_SECTIONS) {
               if (page.isSubSection()) {
                  this.plugin.getSection(page.getRootSection()).openShopSection(player, disabledBackButton);
               } else {
                  new MainMenu(player);
               }
            }
            break;
         case 1:
            if (cp > 1) {
               this.plugin.getSection(page.getSection()).openShopSection(player, cp - 1, disabledBackButton);
            }
            break;
         case 2:
            if (cp != page.getAllPages()) {
               this.plugin.getSection(page.getSection()).openShopSection(player, cp + 1, disabledBackButton);
            }
            break;
         case 3:
            this.close(player);
         }

      }
   }

   public void execute(Player player, ShopPage page, ActionItem.Action action, int cp, boolean disabledBackButton) {
      switch(action) {
      case BACK:
         if (!disabledBackButton || page.isSubSection() && !BackButton.SUB_SECTIONS) {
            if (page.isSubSection()) {
               this.plugin.getSection(page.getRootSection()).openShopSection(player, disabledBackButton);
            } else {
               new MainMenu(player);
            }
         }
         break;
      case PAGE_BACK:
         if (cp > 1) {
            this.plugin.getSection(page.getSection()).openShopSection(player, cp - 1, disabledBackButton);
         }
         break;
      case PAGE_NEXT:
         if (cp != page.getAllPages()) {
            this.plugin.getSection(page.getSection()).openShopSection(player, cp + 1, disabledBackButton);
         }
         break;
      case CLOSE:
         this.close(player);
      }

   }

   private NavItem getItem(String shop, int i) {
      return this.shopNavBars.containsKey(shop) ? ((NavBar.InventoryNavBar)this.shopNavBars.get(shop)).get(i) : this.shopsNavBar.get(i);
   }

   public void execute(InventoryHolder holder, Player player, int i) {
      NavItem item = holder instanceof MainMenu ? this.mainMenuNavBar.get(i) : this.sellGUINavBar.get(i);
      if (item != null) {
         String var5 = item.getAction();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case 2030823:
            if (var5.equals("BACK")) {
               var6 = 0;
            }
            break;
         case 64218584:
            if (var5.equals("CLOSE")) {
               var6 = 1;
            }
         }

         switch(var6) {
         case 0:
         case 1:
            this.close(player);
         default:
         }
      }
   }

   public void execute(Player player, ShopItem shopItem, int i, String rootSection, boolean disabledBackButton, Transaction.Mode transactionMode, Transaction.Type transactionType, int amount) {
      NavItem item = this.transactionNavBar.get(i);
      if (item != null) {
         String var10 = item.getAction();
         byte var11 = -1;
         switch(var10.hashCode()) {
         case 2030823:
            if (var10.equals("BACK")) {
               var11 = 1;
            }
            break;
         case 64218584:
            if (var10.equals("CLOSE")) {
               var11 = 2;
            }
            break;
         case 1536798638:
            if (var10.equals("TOGGLE_MODE")) {
               var11 = 0;
            }
         }

         switch(var11) {
         case 0:
            (new TransactionMenu(player, shopItem, rootSection, disabledBackButton, transactionMode == Transaction.Mode.BUY ? Transaction.Mode.SELL : Transaction.Mode.BUY, transactionMode == Transaction.Mode.BUY ? Transaction.Type.SELL_SCREEN : Transaction.Type.BUY_SCREEN, amount)).open();
            break;
         case 1:
            if (transactionType != Transaction.Type.BUY_STACKS_SCREEN) {
               ShopSection section = this.plugin.getSection(shopItem.section);
               section.openShopSection(player, section.getPageForShopItem(shopItem.itemLoc), disabledBackButton, rootSection);
            } else {
               (new TransactionMenu(player, shopItem, rootSection, disabledBackButton, transactionMode, Transaction.Type.BUY_SCREEN, amount)).open();
            }
            break;
         case 2:
            this.close(player);
         }

      }
   }

   public void execute(Player player, ShopItem shopItem, int i, Transaction.Mode transactionMode, int amount) {
      NavItem item = this.transactionNavBar.get(i);
      if (item != null) {
         String var7 = item.getAction();
         byte var8 = -1;
         switch(var7.hashCode()) {
         case 64218584:
            if (var7.equals("CLOSE")) {
               var8 = 1;
            }
            break;
         case 1536798638:
            if (var7.equals("TOGGLE_MODE")) {
               var8 = 0;
            }
         }

         switch(var8) {
         case 0:
            (new TransactionMenu(player, shopItem, (String)null, false, transactionMode == Transaction.Mode.BUY ? Transaction.Mode.SELL : Transaction.Mode.BUY, transactionMode == Transaction.Mode.BUY ? Transaction.Type.SHOPSTAND_SELL_SCREEN : Transaction.Type.SHOPSTAND_BUY_SCREEN, amount)).open();
            break;
         case 1:
            this.close(player);
         }

      }
   }

   private void close(Player player) {
      UserManager.getUser(player).setOpenNewGUI(true);
      player.closeInventory();
   }

   public void loadNavBars() {
      this.mainMenuNavBar = new NavBar.InventoryNavBar(ConfigManager.getConfig().getConfigurationSection("main-menu-nav-bar"));
      this.transactionNavBar = new NavBar.InventoryNavBar(ConfigManager.getConfig().getConfigurationSection("transaction-screens-nav-bar"));
      this.sellGUINavBar = new NavBar.InventoryNavBar(ConfigManager.getConfig().getConfigurationSection("sellgui-nav-bar"));
      this.loadShopNavBars();
      if (this.plugin.shopStands) {
         this.shopStandsNavBar = new NavBar.InventoryNavBar(ConfigManager.getConfig().getConfigurationSection("shopstands-transaction-screens-nav-bar"));
      }

   }

   private void loadShopNavBars() {
      if (!this.shopNavBars.isEmpty()) {
         this.shopNavBars.clear();
      }

      this.shopsNavBar = new NavBar.InventoryNavBar(ConfigManager.getConfig().getConfigurationSection("shops-nav-bar"));
      this.plugin.getShopSections().forEach(this::loadShopNavBar);
   }

   private void loadShopNavBar(String section) {
      if (!ConfigManager.getSection(section).getString("nav-bar.mode", "INHERIT").equalsIgnoreCase("INHERIT")) {
         this.shopNavBars.put(section, new NavBar.InventoryNavBar(section));
      }
   }

   private NavItem getDefaultItem(String path, ItemStack fillItem) throws ItemLoadException {
      try {
         return this.getNavBarItem(ConfigManager.getConfig().getConfigurationSection(path), fillItem);
      } catch (ItemLoadException var4) {
         SendMessage.logDebugMessage(var4.getMessage());
         SendMessage.errorItemConfig(path);
         return new NavBarItem(ConfigManager.getConfig().getDef().getConfigurationSection(path), fillItem);
      }
   }

   private NavItem getShopItem(String shop, String path, ItemStack fillItem) {
      try {
         return this.getNavBarItem(ConfigManager.getSection(shop).getConfigurationSection(path), fillItem);
      } catch (ItemLoadException var5) {
         SendMessage.logDebugMessage(var5.getMessage());
         SendMessage.errorSections(shop, path);
         return null;
      }
   }

   private NavItem getNavBarItem(ConfigurationSection section, ItemStack fillItem) throws ItemLoadException {
      if (section != null && !section.getKeys(false).isEmpty()) {
         String skull = section.getString("skullowner");
         return (NavItem)(skull != null && skull.equals("%player_name%") ? new PlayerProfile(section, fillItem) : new NavBarItem(section, fillItem));
      } else {
         throw new ItemLoadException("Could not create the navigation-bar item because it was not found.");
      }
   }

   public Inventory addShopsNavBar(Inventory inv, Player p, String section, int page, int allpages) {
      return this.shopNavBars.containsKey(section) ? ((NavBar.InventoryNavBar)this.shopNavBars.get(section)).load(inv, p, section, page, allpages) : this.shopsNavBar.load(inv, p, section, page, allpages);
   }

   public Inventory addMainMenuNavBar(Inventory inv, Player p) {
      return this.mainMenuNavBar.load(inv, p, (EcoType)null);
   }

   public Inventory addTransactionNavBar(Inventory inv, Player p, EcoType type) {
      return this.transactionNavBar.load(inv, p, type);
   }

   public Inventory addShopStandsNavBar(Inventory inv, Player p, EcoType type) {
      return this.shopStandsNavBar.load(inv, p, type);
   }

   public Inventory addSellGUINavBar(Inventory inv, Player p) {
      return this.sellGUINavBar.load(inv, p, (EcoType)null);
   }

   public void createCustomPlayerProfile() {
      if (!ConfigManager.getConfig().getBoolean("custom-bedrock-player-profile.enabled", true)) {
         this.customPlayerProfile = null;
      } else {
         ConfigurationSection section = ConfigManager.getConfig().getConfigurationSection("custom-bedrock-player-profile.item");
         if (section != null && !section.getKeys(false).isEmpty()) {
            ItemStack item = this.createFillItem((String)null, section);
            this.customPlayerProfile = this.translateItem(item);
         } else {
            this.customPlayerProfile = null;
         }
      }
   }

   private ItemStack translateItem(ItemStack item) {
      if (!item.hasItemMeta()) {
         return item;
      } else {
         ItemMeta meta = item.getItemMeta();
         if (meta.hasDisplayName()) {
            this.plugin.getMetaUtils().setDisplayName(meta, this.replaceTranslations(meta.getDisplayName()));
         }

         if (meta.hasLore()) {
            this.plugin.getMetaUtils().setLore(meta, (List)meta.getLore().stream().map(this::replaceTranslations).collect(Collectors.toList()));
         }

         item.setItemMeta(meta);
         return item;
      }
   }

   private Translatable replaceTranslations(String s) {
      return Lang.fromConfig(s.replace("%translations-previous-page%", Lang.PREVIOUS_PAGE.get().toString()).replace("%translations-current-page%", Lang.CURRENT_PAGE.get().getLegacy().contains("%page%") ? Lang.CURRENT_PAGE.get().toString() : Lang.CURRENT_PAGE.get().builder().append(" %page%/%pages%").build().toString()).replace("%translations-next-page%", Lang.NEXT_PAGE.get().toString()).replace("%translations-money%", Lang.MONEY.get().toString()).replace("%translations-cancel%", Lang.CANCEL.get().toString()).replace("%translations-back%", Lang.BACK.get().toString()));
   }

   private class InventoryNavBar {
      private boolean enabled;
      private final ItemStack fillItem;
      private final Map<Integer, NavItem> items = new HashMap();

      public NavItem get(int i) {
         return (NavItem)this.items.get(i);
      }

      public InventoryNavBar(String param2) {
         this.enabled = !ConfigManager.getSection(shop).getString("nav-bar.mode", "INHERIT").equalsIgnoreCase("DISABLED");
         this.fillItem = ConfigManager.getSection(shop).contains("nav-bar.fill-item") ? NavBar.this.plugin.createItem.createFillItem(shop, ConfigManager.getSection(shop).getConfigurationSection("nav-bar.fill-item")) : null;
         ConfigurationSection section = ConfigManager.getSection(shop).getConfigurationSection("nav-bar.items");
         if (section != null && !section.getKeys(false).isEmpty()) {
            int i = 1;
            Iterator var5 = section.getKeys(false).iterator();

            while(true) {
               while(var5.hasNext()) {
                  String item = (String)var5.next();
                  NavItem nbi = NavBar.this.getShopItem(shop, "nav-bar.items." + item, this.fillItem);
                  if (nbi == null) {
                     nbi = NavBar.this.shopsNavBar.get(i);
                  }

                  int slot = 0;

                  try {
                     if (ConfigManager.getSection(shop).contains("nav-bar.items." + item + ".slot")) {
                        slot = ConfigManager.getSection(shop).getInt("nav-bar.items." + item + ".slot");
                     } else {
                        try {
                           for(slot = Integer.parseInt(item); this.items.containsKey(slot - 1); ++slot) {
                           }
                        } catch (NumberFormatException var10) {
                           for(slot = i; this.items.containsKey(slot - 1); ++slot) {
                           }
                        }
                     }
                  } catch (NumberFormatException var11) {
                     SendMessage.warnMessage("Cannot create navigation bar item for shop '" + shop + "' at 'nav-bar.items." + item + "' because the slot is not a valid integer. Got '" + ConfigManager.getSection(shop).getInt("nav-bar.items." + item + ".slot") + "'");
                  }

                  if (slot <= 9 && slot >= 1) {
                     this.items.put(slot - 1, nbi);
                     ++i;
                  } else {
                     SendMessage.warnMessage("Cannot create navigation bar item for shop '" + shop + "' at 'nav-bar.items." + item + "' because the slot is out of bounds for " + slot + ". Valid values should be in range of 1-9");
                  }
               }

               if (this.items.isEmpty()) {
                  this.enabled = false;
               }

               return;
            }
         } else {
            this.enabled = false;
         }
      }

      public InventoryNavBar(ConfigurationSection param2) {
         this.enabled = section.getBoolean("enabled", true);
         this.fillItem = section.contains("fill-item") ? NavBar.this.plugin.createItem.createFillItem((String)null, section.getConfigurationSection("fill-item")) : null;
         section = section.getConfigurationSection("items");
         if (section != null && !section.getKeys(false).isEmpty()) {
            int i = 1;
            Iterator var4 = section.getKeys(false).iterator();

            while(var4.hasNext()) {
               String item = (String)var4.next();

               try {
                  NavItem nbi = NavBar.this.getDefaultItem(section.getCurrentPath() + "." + item, this.fillItem);
                  int slot = 0;

                  try {
                     if (section.contains(item + ".slot")) {
                        slot = section.getInt(item + ".slot");
                     } else {
                        try {
                           for(slot = Integer.parseInt(item); this.items.containsKey(slot - 1); ++slot) {
                           }
                        } catch (NumberFormatException var9) {
                           for(slot = i; this.items.containsKey(slot - 1); ++slot) {
                           }
                        }
                     }
                  } catch (NumberFormatException var10) {
                     SendMessage.warnMessage("Cannot create navigation bar item at '" + section.getCurrentPath() + "." + item + "' because the slot is not a valid integer. Got '" + section.getString(item + ".slot") + "'");
                  }

                  if (slot <= 9 && slot >= 1) {
                     this.items.put(slot - 1, nbi);
                     ++i;
                  } else {
                     SendMessage.warnMessage("Cannot create navigation bar item at '" + section.getCurrentPath() + "." + item + "' because the slot is out of bounds for " + slot + ". Valid values should be in range of 1-9");
                  }
               } catch (ItemLoadException var11) {
               }
            }

            if (this.items.isEmpty()) {
               this.enabled = false;
            }

         } else {
            this.enabled = false;
         }
      }

      public Inventory load(Inventory inv, Player p, String section, int page, int allpages) {
         if (!this.enabled) {
            return inv;
         } else {
            Iterator var6 = this.items.entrySet().iterator();

            while(var6.hasNext()) {
               Entry<Integer, NavItem> e = (Entry)var6.next();
               inv.setItem(inv.getSize() - 9 + (Integer)e.getKey(), ((NavItem)e.getValue()).getItem(p, NavBar.this.plugin.getEcoHandler().getEcon(section).getType(), page, allpages));
            }

            if (this.fillItem != null) {
               for(int i = 0; i < 9; ++i) {
                  if (!this.items.containsKey(i)) {
                     inv.setItem(inv.getSize() - 9 + i, this.fillItem);
                  }
               }
            }

            return inv;
         }
      }

      public Inventory load(Inventory inv, Player p, EcoType type) {
         if (!this.enabled) {
            return inv;
         } else {
            Iterator var4 = this.items.entrySet().iterator();

            while(var4.hasNext()) {
               Entry<Integer, NavItem> e = (Entry)var4.next();
               NavItem item = (NavItem)e.getValue();
               if (!item.isDisabled()) {
                  inv.setItem(inv.getSize() - 9 + (Integer)e.getKey(), item.getItem(p, type));
               }
            }

            if (this.fillItem != null) {
               for(int i = 0; i < 9; ++i) {
                  if (!this.items.containsKey(i)) {
                     inv.setItem(inv.getSize() - 9 + i, this.fillItem);
                  }
               }
            }

            return inv;
         }
      }
   }
}
