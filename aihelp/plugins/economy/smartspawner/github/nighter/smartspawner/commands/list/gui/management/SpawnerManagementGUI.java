package github.nighter.smartspawner.commands.list.gui.management;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerManagementGUI {
   private static final int INVENTORY_SIZE = 27;
   private static final int TELEPORT_SLOT = 10;
   private static final int OPEN_SPAWNER_SLOT = 12;
   private static final int STACK_SLOT = 14;
   private static final int REMOVE_SLOT = 16;
   private static final int BACK_SLOT = 26;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;

   public SpawnerManagementGUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
   }

   public void openManagementMenu(Player player, String spawnerId, String worldName, int listPage) {
      this.openManagementMenu(player, spawnerId, worldName, listPage, (String)null);
   }

   public void openManagementMenu(Player player, String spawnerId, String worldName, int listPage, String targetServer) {
      boolean isRemote = targetServer != null && !targetServer.equals(this.getCurrentServerName());
      if (!isRemote) {
         SpawnerData spawner = this.spawnerManager.getSpawnerById(spawnerId);
         if (spawner == null) {
            this.messageService.sendMessage(player, "spawner_not_found");
            return;
         }
      }

      String title = this.languageManager.getGuiTitle("spawner_management.title");
      Inventory inv = Bukkit.createInventory(new SpawnerManagementHolder(spawnerId, worldName, listPage, targetServer), 27, title);
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      if (isRemote) {
         this.createDisabledTeleportItem(inv, 10, targetServer);
      } else {
         this.createActionItem(inv, 10, "spawner_management.teleport", Material.ENDER_PEARL);
      }

      if (isRemote) {
         this.createRemoteActionItem(inv, 12, "spawner_management.open_spawner", Material.ENDER_EYE, "View Info");
      } else {
         this.createActionItem(inv, 12, "spawner_management.open_spawner", Material.ENDER_EYE);
      }

      if (isRemote) {
         this.createRemoteActionItem(inv, 14, "spawner_management.stack", Material.SPAWNER, "Edit Stack Size");
      } else {
         this.createActionItem(inv, 14, "spawner_management.stack", Material.SPAWNER);
      }

      if (isRemote) {
         this.createRemoteActionItem(inv, 16, "spawner_management.remove", Material.BARRIER, "Remove from DB");
      } else {
         this.createActionItem(inv, 16, "spawner_management.remove", Material.BARRIER);
      }

      this.createActionItem(inv, 26, "spawner_management.back", Material.RED_STAINED_GLASS_PANE);
      player.openInventory(inv);
   }

   private void createActionItem(Inventory inv, int slot, String langKey, Material material) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(this.languageManager.getGuiItemName(langKey + ".name"));
         List<String> lore = Arrays.asList(this.languageManager.getGuiItemLore(langKey + ".lore"));
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
      }

      if (item.getType() == Material.SPAWNER) {
         VersionInitializer.hideTooltip(item);
      }

      inv.setItem(slot, item);
   }

   private void createDisabledTeleportItem(Inventory inv, int slot, String serverName) {
      ItemStack item = new ItemStack(Material.BARRIER);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(String.valueOf(ChatColor.RED) + "Teleport Disabled");
         List<String> lore = new ArrayList();
         lore.add(String.valueOf(ChatColor.GRAY) + "Must be on the same server");
         lore.add(String.valueOf(ChatColor.GRAY) + "to teleport to this spawner.");
         lore.add("");
         String var10001 = String.valueOf(ChatColor.DARK_GRAY);
         lore.add(var10001 + "Spawner Server: " + String.valueOf(ChatColor.WHITE) + serverName);
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
      }

      inv.setItem(slot, item);
   }

   private void createDisabledActionItem(Inventory inv, int slot, String langKey, Material originalMaterial, String reason) {
      ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         String name = this.languageManager.getGuiItemName(langKey + ".name");
         String var10001 = String.valueOf(ChatColor.GRAY);
         meta.setDisplayName(var10001 + ChatColor.stripColor(name) + " (Disabled)");
         List<String> lore = new ArrayList();
         lore.add(String.valueOf(ChatColor.RED) + "Not available for remote servers");
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
      }

      inv.setItem(slot, item);
   }

   private void createRemoteActionItem(Inventory inv, int slot, String langKey, Material material, String action) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(this.languageManager.getGuiItemName(langKey + ".name"));
         List<String> lore = new ArrayList(Arrays.asList(this.languageManager.getGuiItemLore(langKey + ".lore")));
         lore.add("");
         lore.add(String.valueOf(ChatColor.YELLOW) + "Remote Server Action");
         lore.add(String.valueOf(ChatColor.GRAY) + "Changes are saved to database.");
         lore.add(String.valueOf(ChatColor.GRAY) + "Target server will sync on next refresh.");
         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         item.setItemMeta(meta);
      }

      if (item.getType() == Material.SPAWNER) {
         VersionInitializer.hideTooltip(item);
      }

      inv.setItem(slot, item);
   }

   private String getCurrentServerName() {
      return this.plugin.getConfig().getString("database.server_name", "server1");
   }
}
