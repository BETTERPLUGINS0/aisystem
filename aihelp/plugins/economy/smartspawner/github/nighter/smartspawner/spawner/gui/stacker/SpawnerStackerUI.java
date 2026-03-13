package github.nighter.smartspawner.spawner.gui.stacker;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.logging.SpawnerEventType;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerStackerUI {
   private static final int GUI_SIZE = 27;
   private static final int[] DECREASE_SLOTS = new int[]{9, 10, 11};
   private static final int[] INCREASE_SLOTS = new int[]{17, 16, 15};
   private static final int SPAWNER_INFO_SLOT = 13;
   private static final int[] STACK_AMOUNTS = new int[]{64, 10, 1};
   private static final int REMOVE_ALL_SLOT = 22;
   private static final int ADD_ALL_SLOT = 4;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;

   public SpawnerStackerUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
   }

   public void openStackerGui(Player player, SpawnerData spawner) {
      if (player != null && spawner != null) {
         String title = this.languageManager.getGuiTitle("gui_title_stacker");
         Inventory gui = Bukkit.createInventory(new SpawnerStackerHolder(spawner), 27, title);
         this.populateStackerGui(gui, spawner);
         if (this.plugin.getSpawnerActionLogger() != null) {
            this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_STACKER_OPEN, (builder) -> {
               builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("current_stack_size", spawner.getStackSize()).metadata("max_stack_size", spawner.getMaxStackSize());
            });
         }

         player.openInventory(gui);
      }
   }

   private void populateStackerGui(Inventory gui, SpawnerData spawner) {
      int i;
      for(i = 0; i < STACK_AMOUNTS.length; ++i) {
         gui.setItem(DECREASE_SLOTS[i], this.createActionButton("remove", spawner, STACK_AMOUNTS[i]));
      }

      for(i = 0; i < STACK_AMOUNTS.length; ++i) {
         gui.setItem(INCREASE_SLOTS[i], this.createActionButton("add", spawner, STACK_AMOUNTS[i]));
      }

      gui.setItem(13, this.createSpawnerInfoButton(spawner));
      gui.setItem(22, this.createAllActionButton("remove_all", spawner));
      gui.setItem(4, this.createAllActionButton("add_all", spawner));
   }

   private ItemStack createActionButton(String action, SpawnerData spawner, int amount) {
      Map<String, String> placeholders = this.createPlaceholders(spawner, amount);
      String name = this.languageManager.getGuiItemName("button_" + action + ".name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore("button_" + action + ".lore", placeholders);
      Material material = action.equals("add") ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
      ItemStack button = this.createButton(material, name, lore);
      button.setAmount(Math.max(1, Math.min(amount, 64)));
      return button;
   }

   private ItemStack createSpawnerInfoButton(SpawnerData spawner) {
      Map<String, String> placeholders = this.createPlaceholders(spawner, 0);
      String name = this.languageManager.getGuiItemName("button_spawner.name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore("button_spawner.lore", placeholders);
      return this.createButton(Material.SPAWNER, name, lore);
   }

   private ItemStack createAllActionButton(String action, SpawnerData spawner) {
      Map<String, String> placeholders = this.createPlaceholders(spawner, 0);
      String name = this.languageManager.getGuiItemName("button_" + action + ".name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore("button_" + action + ".lore", placeholders);
      Material material = action.equals("add_all") ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
      return this.createButton(material, name, lore);
   }

   private Map<String, String> createPlaceholders(SpawnerData spawner, int amount) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("amount", String.valueOf(amount));
      placeholders.put("plural", amount > 1 ? "s" : "");
      placeholders.put("stack_size", String.valueOf(spawner.getStackSize()));
      placeholders.put("max_stack_size", String.valueOf(spawner.getMaxStackSize()));
      placeholders.put("entity", this.languageManager.getFormattedMobName(spawner.getEntityType()));
      placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps((String)placeholders.get("entity")));
      return placeholders;
   }

   private ItemStack createButton(Material material, String name, String[] lore) {
      ItemStack button = new ItemStack(material);
      ItemMeta meta = button.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(name);
         if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
         }

         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         button.setItemMeta(meta);
      }

      VersionInitializer.hideTooltip(button);
      return button;
   }
}
