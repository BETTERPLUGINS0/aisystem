package me.gypopo.economyshopgui.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.mappings.ClickAction;
import me.gypopo.economyshopgui.objects.mappings.ClickMappings;
import me.gypopo.economyshopgui.objects.shops.ShopType;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ShopSection extends CreateItem implements me.gypopo.economyshopgui.objects.shops.ShopSection {
   private final String section;
   private final Translatable title;
   private final boolean subSection;
   private final boolean navBar;
   private ItemStack fillItem;
   private final List<String> pages;
   private final Map<Integer, Translatable> pageTitles = new HashMap();
   private final Map<Integer, ShopPageItems> shopPages = new HashMap();
   private final Map<String, DisplayItem> displayItems = new HashMap();
   private final ClickMappings mappings;
   private final EconomyShopGUI plugin;
   private final boolean nms;
   public final List<String> itemLocs;

   public ShopSection(EconomyShopGUI plugin, String section) throws Exception {
      super(plugin);
      this.nms = EconomyShopGUI.getInstance().versionHandler != null && EconomyShopGUI.getInstance().version >= 113 && ConfigManager.getConfig().getBoolean("use-nms", true) && Bukkit.getPluginManager().getPlugin("EcoEnchants") == null;
      this.itemLocs = new ArrayList();
      this.plugin = plugin;
      this.section = section;
      this.title = EconomyShopGUI.getInstance().getSectionTitle(section);
      this.navBar = this.plugin.navBar.isEnabled(this.section);
      this.itemLocs.addAll(this.plugin.getConfigManager().getItemsRaw(this.section));
      this.subSection = ConfigManager.getSection(section).getBoolean("sub-section");
      this.pages = this.plugin.getConfigManager().getPages(this.section, false);
      this.mappings = ConfigManager.getSection(this.section).contains("click-mappings") ? new ClickMappings(section) : plugin.getClickMappings();
      this.loadShopItems();
   }

   public Collection<DisplayItem> getDisplayItems() {
      return this.displayItems.values();
   }

   public DisplayItem getDisplayItem(String itemPath) {
      return (DisplayItem)this.displayItems.get(itemPath);
   }

   public boolean isActionItem(String section, String itemLoc) {
      return this.displayItems.get(section + "." + itemLoc) instanceof ActionItem;
   }

   public Collection<ShopItem> getShopItems() {
      Stream var10000 = this.displayItems.values().stream().filter((i) -> {
         return ShopItem.class.equals(i.getClass());
      });
      Objects.requireNonNull(ShopItem.class);
      return (Collection)var10000.map(ShopItem.class::cast).collect(Collectors.toCollection(ArrayList::new));
   }

   public ShopItem getShopItem(String itemPath) {
      DisplayItem item = (DisplayItem)this.displayItems.get(itemPath);
      return item instanceof ShopItem ? (ShopItem)item : null;
   }

   public String getSection() {
      return this.section;
   }

   public ShopPageItems getShopPageItems(int page) {
      return (ShopPageItems)this.shopPages.get(page);
   }

   public List<ShopPageItems> getShopPageItems() {
      return new ArrayList(this.shopPages.values());
   }

   public int getPages() {
      return this.pages.size();
   }

   public Set<String> getValidItemPaths() {
      return this.displayItems.keySet();
   }

   public boolean isSubSection() {
      return this.subSection;
   }

   public boolean isHidden() {
      return false;
   }

   public boolean isCloseMenu() {
      return false;
   }

   public ShopType getType() {
      return ShopType.CATEGORY;
   }

   public List<String> getItemLocs() {
      return this.itemLocs;
   }

   public List<String> getShopItemLocs() {
      return (List)this.displayItems.values().stream().filter((i) -> {
         return ShopItem.class.equals(i.getClass());
      }).map(DisplayItem::itemLoc).collect(Collectors.toList());
   }

   public ClickAction getClickAction(ClickType clickType) {
      return this.mappings.getAction(clickType);
   }

   public int getPageForShopItem(String location) {
      return this.pages.indexOf(location.split("\\.")[0]) + 1;
   }

   private void loadShopItems() {
      try {
         Iterator var1 = this.itemLocs.iterator();

         while(var1.hasNext()) {
            String itemLoc = (String)var1.next();
            this.displayItems.put(this.section + "." + itemLoc, ConfigManager.getShop(this.section).contains("pages." + itemLoc + ".action") ? new ActionItem(this.section, itemLoc) : new ShopItem(this.section, itemLoc));
         }

         if (!this.pages.isEmpty()) {
            this.loadPages();
         } else {
            SendMessage.errorMessage("No pages found for shop " + this.section + ", adding empty page...");
            this.shopPages.put(1, new ShopPageItems(this.section));
            this.pageTitles.put(1, this.title);
         }
      } catch (NullPointerException var3) {
         var3.printStackTrace();
      }

      if (ConfigManager.getSection(this.section).get("fill-item") != null) {
         this.loadFillItem();
      }

   }

   private void loadFillItem() {
      this.fillItem = this.plugin.createItem.createFillItem(this.section, ConfigManager.getSection(this.section).getConfigurationSection("fill-item"));
   }

   private void loadPages() {
      try {
         int pageN = 1;

         for(Iterator var2 = this.pages.iterator(); var2.hasNext(); ++pageN) {
            String page = (String)var2.next();
            Integer size = this.getPageSize(page);
            List<String> displayItems = this.plugin.calculateAmount.loadItemsInOrder(this.section, page, this.plugin.navBar.isEnabled(this.section) ? (size == null ? 45 : size) : (size == null ? 54 : size));
            if (size == null) {
               size = displayItems.size() <= 45 && !displayItems.isEmpty() ? CalculateAmount.getSlots(displayItems.size()) : 54;
            }

            List<String> itemLocs = new ArrayList();
            Map<Integer, ItemStack> items = new LinkedHashMap();

            for(int i = 0; i < displayItems.size(); ++i) {
               String itemLoc = (String)displayItems.get(i);
               if (itemLoc != null) {
                  items.put(i, ((DisplayItem)this.displayItems.get(this.section + "." + itemLoc)).getShopItem());
               }

               itemLocs.add(itemLoc);
            }

            this.shopPages.put(pageN, new ShopPageItems(items, itemLocs, this.section, size));
            this.pageTitles.put(pageN, this.getPageTitle(page));
         }
      } catch (Exception var10) {
         SendMessage.warnMessage("Error occurred while loading shop items for section " + this.section);
         var10.printStackTrace();
      }

   }

   private Integer getPageSize(String page) {
      if (!ConfigManager.getShop(this.section).contains("pages." + page + ".gui-rows")) {
         return null;
      } else {
         try {
            int size = Integer.parseInt(ConfigManager.getShop(this.section).getString("pages." + page + ".gui-rows"));
            if (size >= 1 && size <= 6) {
               return size * 9;
            }

            SendMessage.warnMessage("Inventory size for page '" + page + "' from shop '" + this.section + "' has to be in range from 1-6, using default...");
         } catch (NumberFormatException var3) {
            SendMessage.warnMessage("Invalid inventory size for page '" + page + "' from shop '" + this.section + "', using default...");
         }

         return null;
      }
   }

   private Translatable getPageTitle(String page) {
      if (ConfigManager.getShop(this.section).contains("pages." + page + ".title")) {
         String title = ConfigManager.getShop(this.section).getString("pages." + page + ".title");
         return Lang.fromConfig(this.plugin.getInvTitle(title));
      } else {
         return this.title;
      }
   }

   public void openShopSection(Player p, boolean disabledBackButton) {
      this.openShopSection(p, 1, disabledBackButton);
   }

   public void openShopSection(Player p, boolean disabledBackButton, String rootSection) {
      this.openShopSection(p, 1, disabledBackButton, rootSection);
   }

   public void openShopSection(Player p, int page, boolean disabledBackButton) {
      this.openShopSection(p, page, disabledBackButton, (String)null);
   }

   public void openShopSection(Player p, int page, boolean disabledBackButton, @Nullable String rootSection) {
      Map<Integer, ItemStack> items = ((ShopPageItems)this.shopPages.get(page)).getItems();
      User user = UserManager.getUser(p);
      boolean itemIndex = PermissionsCache.hasPermission(p, "EconomyShopGUI.itemindexes");
      boolean discounts = this.plugin.discountsActive && this.plugin.hasDiscount(this.section);
      boolean multipliers = this.plugin.multipliers && this.plugin.hasMultiplier(this.section);
      this.updateLore(items, p, user.isBedrock() || user.isPr(), itemIndex, discounts, multipliers, page);
      user.setOpenNewGUI(true);
      p.openInventory((new ShopPage(items, this.fillItem, p, this.getTitle(page), this.section, page, this.pages.size(), disabledBackButton, this.navBar, this.plugin.resizeGUI && user.isBedrock() ? 54 : ((ShopPageItems)this.shopPages.get(page)).getSize(), rootSection)).getInventory());
   }

   public void reloadItem(String itemPath) {
   }

   private void updateLore(Map<Integer, ItemStack> items, Player p, boolean pr, boolean itemIndex, boolean discounts, boolean multipliers, int page) {
      items.replaceAll((slot, item) -> {
         if (item.getType().equals(Material.AIR)) {
            return item;
         } else {
            DisplayItem displayItem = (DisplayItem)this.displayItems.get(this.section + "." + (String)((ShopPageItems)this.shopPages.get(page)).getItemOrder().get(slot));
            return !(displayItem instanceof ShopItem) ? this.updateLore((ActionItem)displayItem, item, p, pr, itemIndex, page) : this.updateLore((ShopItem)displayItem, new ItemStack(item), p, pr, discounts, multipliers, itemIndex, this.plugin.seasonalLore && ((ShopItem)displayItem).hasSeasonModifier(p.getWorld().getName()), page);
         }
      });
   }

   private ItemStack updateLore(ShopItem shopItem, ItemStack item, Player p, boolean pr, boolean discounts, boolean multipliers, boolean itemIndex, boolean seasonal, int page) {
      try {
         return this.plugin.getLoreFormatter().format(shopItem, item, p, this.section, this.nms && !shopItem.rgb, pr, discounts, multipliers, itemIndex, seasonal, page, this.getPages());
      } catch (Exception var11) {
         SendMessage.warnMessage("Error occurred while updating shop lore of item '" + shopItem.getItemPath() + "'");
         var11.printStackTrace();
         return item;
      }
   }

   private ItemStack updateLore(ActionItem actionItem, ItemStack item, Player p, boolean pr, boolean itemIndex, int page) {
      try {
         return this.plugin.getLoreFormatter().format(actionItem, item, p, this.plugin.getEcoHandler().getEcon(this.section).getType(), this.section, this.nms, pr, itemIndex, page, this.getPages());
      } catch (Exception var8) {
         SendMessage.logDebugMessage("Error occurred while updating shop lore of action item '" + actionItem.getItemPath() + "'");
         var8.printStackTrace();
         return item;
      }
   }

   private Translatable getTitle(int page) {
      return ((Translatable)this.pageTitles.get(page)).replace("%page%", String.valueOf(page)).replace("%pages%", String.valueOf(this.pages));
   }

   public ItemStack updateItem(Player p, ShopItem shopItem) {
      ItemStack item = new ItemStack(shopItem.getShopItem());
      boolean nbt = this.nms && !UserManager.getUser(p).isBedrock() && !UserManager.getUser(p).isPr();
      boolean itemIndex = PermissionsCache.hasPermission(p, "EconomyShopGUI.itemindexes");
      boolean discounts = this.plugin.discountsActive && this.plugin.hasDiscount(this.section);
      boolean multipliers = this.plugin.multipliers && this.plugin.hasMultiplier(this.section);
      return this.updateLore(shopItem, item, p, nbt, discounts, multipliers, itemIndex, this.plugin.seasonalLore && shopItem.hasSeasonModifier(p.getWorld().getName()), this.getPageForShopItem(shopItem.itemLoc()));
   }
}
