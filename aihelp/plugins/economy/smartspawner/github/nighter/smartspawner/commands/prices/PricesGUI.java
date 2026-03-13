package github.nighter.smartspawner.commands.prices;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.prices.holders.PricesHolder;
import github.nighter.smartspawner.hooks.economy.ItemPriceManager;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PricesGUI implements Listener {
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final ItemPriceManager priceManager;
   private static final int ITEMS_PER_PAGE = 45;
   private static final int GUI_SIZE = 54;

   public PricesGUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.priceManager = plugin.getItemPriceManager();
   }

   public void openPricesGUI(Player player, int page) {
      if (!player.hasPermission("smartspawner.command.prices")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
         Map<Material, PricesGUI.PriceInfo> allItems = this.collectAllPriceableItems();
         if (allItems.isEmpty()) {
            this.messageService.sendMessage(player, "no_priceable_items");
         } else {
            List<Entry<Material, PricesGUI.PriceInfo>> sortedItems = allItems.entrySet().stream().sorted(Entry.comparingByKey(Comparator.comparing(Enum::name))).toList();
            int totalPages = (int)Math.ceil((double)sortedItems.size() / 45.0D);
            if (page > totalPages) {
               page = totalPages;
            }

            if (page < 1) {
               page = 1;
            }

            Inventory inventory = Bukkit.createInventory(new PricesHolder(page, totalPages), 54, this.languageManager.getGuiTitle("prices_gui_title", Map.of("current_page", String.valueOf(page), "total_pages", String.valueOf(totalPages))));
            int startIndex = (page - 1) * 45;
            int endIndex = Math.min(startIndex + 45, sortedItems.size());

            for(int i = startIndex; i < endIndex; ++i) {
               Entry<Material, PricesGUI.PriceInfo> entry = (Entry)sortedItems.get(i);
               ItemStack itemStack = this.createPriceDisplayItem((Material)entry.getKey(), (PricesGUI.PriceInfo)entry.getValue());
               inventory.setItem(i - startIndex, itemStack);
            }

            this.addNavigationButtons(inventory, page, totalPages);
            player.openInventory(inventory);
         }
      }
   }

   private Map<Material, PricesGUI.PriceInfo> collectAllPriceableItems() {
      Map<Material, PricesGUI.PriceInfo> allItems = new HashMap();
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType entityType = var2[var4];
         EntityLootConfig lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(entityType);
         if (lootConfig != null) {
            Iterator var7 = lootConfig.getAllItems().iterator();

            while(var7.hasNext()) {
               LootItem lootItem = (LootItem)var7.next();
               if (lootItem.isAvailable()) {
                  Material material = lootItem.material();
                  double finalPrice = this.priceManager.getPrice(material);
                  if (finalPrice > 0.0D) {
                     double customPrice = this.getCustomPrice(material);
                     double shopPrice = this.getShopPrice(material);
                     String priceSource = this.determinePriceSource(material, finalPrice, customPrice, shopPrice);
                     allItems.put(material, new PricesGUI.PriceInfo(finalPrice, priceSource, customPrice, shopPrice));
                  }
               }
            }
         }
      }

      return allItems;
   }

   private double getCustomPrice(Material material) {
      Map<String, Double> allPrices = this.priceManager.getAllPrices();
      return (Double)allPrices.getOrDefault(material.name(), 0.0D);
   }

   private double getShopPrice(Material material) {
      return this.priceManager.getShopIntegrationManager() == null ? 0.0D : this.priceManager.getShopIntegrationManager().getPrice(material);
   }

   private String determinePriceSource(Material material, double finalPrice, double customPrice, double shopPrice) {
      if (Math.abs(finalPrice - customPrice) < 0.001D && customPrice > 0.0D) {
         return "Custom";
      } else if (Math.abs(finalPrice - shopPrice) < 0.001D && shopPrice > 0.0D) {
         return "Shop";
      } else {
         return finalPrice > 0.0D ? "Default" : "Unknown";
      }
   }

   private ItemStack createPriceDisplayItem(Material material, PricesGUI.PriceInfo priceInfo) {
      ItemStack item = new ItemStack(material, 1);
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return item;
      } else {
         String materialName = this.languageManager.getVanillaItemName(material);
         Map<String, String> placeholders = new HashMap();
         placeholders.put("item_name", materialName);
         placeholders.put("ɪᴛᴇᴍ_ɴᴀᴍᴇ", this.languageManager.getSmallCaps(materialName));
         placeholders.put("price", this.languageManager.formatNumber(priceInfo.finalPrice));
         placeholders.put("price_source", priceInfo.source);
         meta.setDisplayName(this.languageManager.getGuiItemName("price_item.name", placeholders));
         List<String> lore = new ArrayList();
         placeholders.put("custom_price", this.languageManager.formatNumber(priceInfo.customPrice));
         placeholders.put("shop_price", this.languageManager.formatNumber(priceInfo.shopPrice));
         lore.addAll(this.languageManager.getGuiItemLoreAsList("price_item.lore", placeholders));
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
         return item;
      }
   }

   private void addNavigationButtons(Inventory inventory, int currentPage, int totalPages) {
      ItemStack closeButton;
      ItemMeta closeMeta;
      if (currentPage > 1) {
         closeButton = new ItemStack(Material.ARROW);
         closeMeta = closeButton.getItemMeta();
         if (closeMeta != null) {
            closeMeta.setDisplayName(this.languageManager.getGuiItemName("navigation.previous_page"));
            closeButton.setItemMeta(closeMeta);
         }

         inventory.setItem(45, closeButton);
      }

      closeButton = new ItemStack(Material.BARRIER);
      closeMeta = closeButton.getItemMeta();
      if (closeMeta != null) {
         closeMeta.setDisplayName(this.languageManager.getGuiItemName("navigation.close"));
         closeButton.setItemMeta(closeMeta);
      }

      inventory.setItem(49, closeButton);
      if (currentPage < totalPages) {
         ItemStack nextButton = new ItemStack(Material.ARROW);
         ItemMeta nextMeta = nextButton.getItemMeta();
         if (nextMeta != null) {
            nextMeta.setDisplayName(this.languageManager.getGuiItemName("navigation.next_page"));
            nextButton.setItemMeta(nextMeta);
         }

         inventory.setItem(53, nextButton);
      }

   }

   @EventHandler
   public void onPricesGUIClick(InventoryClickEvent event) {
      if (event.getInventory().getHolder(false) instanceof PricesHolder) {
         HumanEntity var3 = event.getWhoClicked();
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            event.setCancelled(true);
            PricesHolder holder = (PricesHolder)event.getInventory().getHolder(false);
            if (holder != null) {
               int slot = event.getSlot();
               int currentPage = holder.getCurrentPage();
               int totalPages = holder.getTotalPages();
               if (slot == 45 && currentPage > 1) {
                  this.openPricesGUI(player, currentPage - 1);
               } else if (slot == 53 && currentPage < totalPages) {
                  this.openPricesGUI(player, currentPage + 1);
               } else if (slot == 49) {
                  player.closeInventory();
                  player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
               }

            }
         }
      }
   }

   private static class PriceInfo {
      final double finalPrice;
      final String source;
      final double customPrice;
      final double shopPrice;

      PriceInfo(double finalPrice, String source, double customPrice, double shopPrice) {
         this.finalPrice = finalPrice;
         this.source = source;
         this.customPrice = customPrice;
         this.shopPrice = shopPrice;
      }
   }
}
