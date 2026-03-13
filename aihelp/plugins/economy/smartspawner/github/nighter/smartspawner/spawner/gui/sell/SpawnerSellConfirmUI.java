package github.nighter.smartspawner.spawner.gui.sell;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.config.SpawnerMobHeadTexture;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerSellConfirmUI {
   private static final int GUI_SIZE = 27;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private GuiLayout cachedLayout;

   public SpawnerSellConfirmUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.loadLayout();
   }

   private void loadLayout() {
      this.cachedLayout = this.plugin.getGuiLayoutConfig().getCurrentSellConfirmLayout();
   }

   public void reload() {
      this.loadLayout();
   }

   public void openSellConfirmGui(Player player, SpawnerData spawner, SpawnerSellConfirmUI.PreviousGui previousGui, boolean collectExp) {
      if (player != null && spawner != null) {
         if (spawner.getVirtualInventory().getUsedSlots() == 0) {
            this.plugin.getMessageService().sendMessage(player, "no_items");
         } else if (this.plugin.getGuiLayoutConfig().isSkipSellConfirmation()) {
            spawner.markInteracted();
            if (collectExp) {
               this.plugin.getSpawnerMenuAction().handleExpBottleClick(player, spawner, true);
            }

            spawner.clearInteracted();
            this.plugin.getSpawnerSellManager().sellAllItems(player, spawner);
            player.closeInventory();
         } else {
            spawner.markInteracted();
            String title = this.languageManager.getGuiTitle("gui_title_sell_confirm", (Map)null);
            Inventory gui = Bukkit.createInventory(new SpawnerSellConfirmHolder(spawner, previousGui, collectExp), 27, title);
            this.populateSellConfirmGui(gui, player, spawner, collectExp);
            player.openInventory(gui);
         }
      }
   }

   private void populateSellConfirmGui(Inventory gui, Player player, SpawnerData spawner, boolean collectExp) {
      Map<String, String> placeholders = this.createPlaceholders(spawner, collectExp);
      if (this.cachedLayout == null) {
         this.plugin.getLogger().warning("Sell confirm layout not loaded, using empty GUI");
      } else {
         Iterator var6 = this.cachedLayout.getAllButtons().values().iterator();

         while(true) {
            GuiButton button;
            ItemStack buttonItem;
            label52:
            while(true) {
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  button = (GuiButton)var6.next();
               } while(!button.isEnabled());

               if (button.isInfoButton()) {
                  buttonItem = this.createSpawnerInfoButton(player, placeholders);
                  break;
               }

               String action = this.getAnyActionFromButton(button);
               if (action != null && !action.isEmpty()) {
                  byte var11 = -1;
                  switch(action.hashCode()) {
                  case -1367724422:
                     if (action.equals("cancel")) {
                        var11 = 0;
                     }
                     break;
                  case 3387192:
                     if (action.equals("none")) {
                        var11 = 2;
                     }
                     break;
                  case 951117504:
                     if (action.equals("confirm")) {
                        var11 = 1;
                     }
                  }

                  switch(var11) {
                  case 0:
                     buttonItem = this.createCancelButton(button.getMaterial(), placeholders);
                     break label52;
                  case 1:
                     buttonItem = this.createConfirmButton(button.getMaterial(), placeholders, collectExp);
                     break label52;
                  case 2:
                     buttonItem = this.createSpawnerInfoButton(player, placeholders);
                     break label52;
                  default:
                     this.plugin.getLogger().warning("Unknown action in sell confirm GUI: " + action);
                  }
               }
            }

            gui.setItem(button.getSlot(), buttonItem);
         }
      }
   }

   private ItemStack createCancelButton(Material material, Map<String, String> placeholders) {
      String name = this.languageManager.getGuiItemName("button_sell_cancel.name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore("button_sell_cancel.lore", placeholders);
      return this.createButton(material, name, lore);
   }

   private ItemStack createConfirmButton(Material material, Map<String, String> placeholders, boolean collectExp) {
      String buttonKey = collectExp ? "button_sell_confirm_with_exp" : "button_sell_confirm";
      String name = this.languageManager.getGuiItemName(buttonKey + ".name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore(buttonKey + ".lore", placeholders);
      return this.createButton(material, name, lore);
   }

   private ItemStack createSpawnerInfoButton(Player player, Map<String, String> placeholders) {
      Consumer<ItemMeta> metaModifier = (meta) -> {
         meta.setDisplayName(this.languageManager.getGuiItemName("button_sell_info.name", placeholders));
         String[] lore = this.languageManager.getGuiItemLore("button_sell_info.lore", placeholders);
         if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
         }

         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
      };
      ItemStack spawnerItem;
      if (placeholders.containsKey("spawnedItem")) {
         spawnerItem = SpawnerMobHeadTexture.getItemSpawnerHead(Material.valueOf((String)placeholders.get("spawnedItem")), player, metaModifier);
      } else {
         spawnerItem = SpawnerMobHeadTexture.getCustomHead(EntityType.valueOf((String)placeholders.get("entityType")), player, metaModifier);
      }

      if (spawnerItem.getType() == Material.SPAWNER) {
         VersionInitializer.hideTooltip(spawnerItem);
      }

      return spawnerItem;
   }

   private Map<String, String> createPlaceholders(SpawnerData spawner, boolean collectExp) {
      Map<String, String> placeholders = new HashMap(8);
      boolean isItemSpawner = spawner.isItemSpawner();
      String entityName;
      if (isItemSpawner) {
         Material spawnedItem = spawner.getSpawnedItemMaterial();
         entityName = this.languageManager.getVanillaItemName(spawnedItem);
         placeholders.put("spawnedItem", spawnedItem.name());
      } else {
         EntityType entityType = spawner.getEntityType();
         entityName = this.languageManager.getFormattedMobName(entityType);
         placeholders.put("entityType", entityType.name());
      }

      placeholders.put("entity", entityName);
      placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityName));
      if (spawner.isSellValueDirty()) {
         spawner.recalculateSellValue();
      }

      double totalSellPrice = spawner.getAccumulatedSellValue();
      int currentItems = spawner.getVirtualInventory().getUsedSlots();
      int currentExp = spawner.getSpawnerExp();
      placeholders.put("total_sell_price", this.languageManager.formatNumber(totalSellPrice));
      placeholders.put("current_items", Integer.toString(currentItems));
      placeholders.put("current_exp", Integer.toString(currentExp));
      return placeholders;
   }

   private ItemStack createButton(Material material, String name, String[] lore) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         if (name != null) {
            meta.setDisplayName(name);
         }

         if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
         }

         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
      }

      return item;
   }

   private String getAnyActionFromButton(GuiButton button) {
      String action = button.getDefaultAction();
      if (action != null && !action.isEmpty()) {
         return action;
      } else {
         action = button.getAction("left_click");
         if (action != null && !action.isEmpty()) {
            return action;
         } else {
            action = button.getAction("right_click");
            return action != null && !action.isEmpty() ? action : null;
         }
      }
   }

   public static enum PreviousGui {
      MAIN_MENU,
      STORAGE;

      // $FF: synthetic method
      private static SpawnerSellConfirmUI.PreviousGui[] $values() {
         return new SpawnerSellConfirmUI.PreviousGui[]{MAIN_MENU, STORAGE};
      }
   }
}
