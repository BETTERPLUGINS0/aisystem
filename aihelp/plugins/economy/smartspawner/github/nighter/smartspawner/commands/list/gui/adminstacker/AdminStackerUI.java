package github.nighter.smartspawner.commands.list.gui.adminstacker;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.gui.CrossServerSpawnerData;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminStackerUI {
   private static final int GUI_SIZE = 27;
   private static final int[] DECREASE_SLOTS = new int[]{9, 10, 11};
   private static final int[] INCREASE_SLOTS = new int[]{17, 16, 15};
   private static final int SPAWNER_INFO_SLOT = 13;
   private static final int BACK_SLOT = 22;
   private static final int[] STACK_AMOUNTS = new int[]{64, 10, 1};
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;

   public AdminStackerUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
   }

   public void openAdminStackerGui(Player player, SpawnerData spawner, String worldName, int listPage) {
      if (player != null && spawner != null) {
         String title = this.languageManager.getGuiTitle("gui_title_stacker");
         Inventory gui = Bukkit.createInventory(new AdminStackerHolder(spawner, worldName, listPage), 27, title);
         this.populateStackerGui(gui, spawner);
         player.openInventory(gui);
      }
   }

   public void openRemoteAdminStackerGui(Player player, CrossServerSpawnerData spawnerData, String targetServer, String worldName, int listPage) {
      if (player != null && spawnerData != null) {
         String title = this.languageManager.getGuiTitle("gui_title_stacker") + " §7[Remote]";
         RemoteAdminStackerHolder holder = new RemoteAdminStackerHolder(spawnerData, targetServer, worldName, listPage);
         Inventory gui = Bukkit.createInventory(holder, 27, title);
         this.populateRemoteStackerGui(gui, holder);
         player.openInventory(gui);
      }
   }

   public void refreshRemoteStackerGui(Inventory gui, RemoteAdminStackerHolder holder) {
      this.populateRemoteStackerGui(gui, holder);
   }

   private void populateRemoteStackerGui(Inventory gui, RemoteAdminStackerHolder holder) {
      CrossServerSpawnerData spawnerData = holder.getSpawnerData();
      int currentSize = holder.getCurrentStackSize();

      int i;
      for(i = 0; i < STACK_AMOUNTS.length; ++i) {
         gui.setItem(DECREASE_SLOTS[i], this.createRemoteActionButton("remove", spawnerData, currentSize, STACK_AMOUNTS[i]));
      }

      for(i = 0; i < STACK_AMOUNTS.length; ++i) {
         gui.setItem(INCREASE_SLOTS[i], this.createRemoteActionButton("add", spawnerData, currentSize, STACK_AMOUNTS[i]));
      }

      gui.setItem(13, this.createRemoteSpawnerInfoButton(spawnerData, currentSize));
      gui.setItem(22, this.createSaveAndBackButton());
   }

   private ItemStack createRemoteActionButton(String action, CrossServerSpawnerData spawnerData, int currentSize, int amount) {
      Map<String, String> placeholders = this.createRemotePlaceholders(spawnerData, currentSize, amount);
      String name = this.languageManager.getGuiItemName("button_" + action + ".name", placeholders);
      String[] lore = this.languageManager.getGuiItemLore("button_" + action + ".lore", placeholders);
      Material material = action.equals("add") ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
      ItemStack button = this.createButton(material, name, lore);
      button.setAmount(Math.max(1, Math.min(amount, 64)));
      return button;
   }

   private ItemStack createRemoteSpawnerInfoButton(CrossServerSpawnerData spawnerData, int currentSize) {
      Map<String, String> placeholders = this.createRemotePlaceholders(spawnerData, currentSize, 0);
      String name = this.languageManager.getGuiItemName("button_spawner.name", placeholders);
      List<String> lore = new ArrayList();
      String var10001 = String.valueOf(ChatColor.GRAY);
      lore.add(var10001 + "Current Stack: " + String.valueOf(ChatColor.WHITE) + currentSize);
      var10001 = String.valueOf(ChatColor.GRAY);
      lore.add(var10001 + "Original: " + String.valueOf(ChatColor.WHITE) + spawnerData.getStackSize());
      lore.add("");
      var10001 = String.valueOf(ChatColor.YELLOW);
      lore.add(var10001 + "Remote Server: " + spawnerData.getServerName());
      lore.add(String.valueOf(ChatColor.GRAY) + "Changes save when you click Back");
      return this.createButtonWithLore(Material.SPAWNER, name, lore);
   }

   private ItemStack createSaveAndBackButton() {
      String name = String.valueOf(ChatColor.GREEN) + "Save & Back";
      List<String> lore = new ArrayList();
      lore.add(String.valueOf(ChatColor.GRAY) + "Click to save changes");
      lore.add(String.valueOf(ChatColor.GRAY) + "and return to management menu");
      return this.createButtonWithLore(Material.LIME_STAINED_GLASS_PANE, name, lore);
   }

   private Map<String, String> createRemotePlaceholders(CrossServerSpawnerData spawnerData, int currentSize, int amount) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("amount", String.valueOf(amount));
      placeholders.put("plural", amount > 1 ? "s" : "");
      placeholders.put("stack_size", String.valueOf(currentSize));
      placeholders.put("max_stack_size", "∞");
      placeholders.put("entity", this.languageManager.getFormattedMobName(spawnerData.getEntityType()));
      placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps((String)placeholders.get("entity")));
      return placeholders;
   }

   private ItemStack createButtonWithLore(Material material, String name, List<String> lore) {
      ItemStack button = new ItemStack(material);
      ItemMeta meta = button.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(name);
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         button.setItemMeta(meta);
      }

      VersionInitializer.hideTooltip(button);
      return button;
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
      gui.setItem(22, this.createBackButton());
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

   private ItemStack createBackButton() {
      String name = this.languageManager.getGuiItemName("spawner_management.back.name");
      String[] lore = this.languageManager.getGuiItemLore("spawner_management.back.lore");
      return this.createButton(Material.RED_STAINED_GLASS_PANE, name, lore);
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
