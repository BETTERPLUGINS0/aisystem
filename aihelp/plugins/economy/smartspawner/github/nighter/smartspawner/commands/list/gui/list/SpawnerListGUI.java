package github.nighter.smartspawner.commands.list.gui.list;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.ListSubCommand;
import github.nighter.smartspawner.commands.list.gui.list.enums.FilterOption;
import github.nighter.smartspawner.commands.list.gui.list.enums.SortOption;
import github.nighter.smartspawner.commands.list.gui.worldselection.WorldSelectionHolder;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SpawnerListGUI implements Listener {
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final ListSubCommand listSubCommand;
   private static final Set<Material> SPAWNER_MATERIALS;
   private static final String patternString = "#([A-Za-z0-9]+)";

   public SpawnerListGUI(SmartSpawner plugin) {
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.listSubCommand = plugin.getListSubCommand();
   }

   @EventHandler
   public void onWorldSelectionClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof WorldSelectionHolder) {
         WorldSelectionHolder holder = (WorldSelectionHolder)var3;
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            if (!player.hasPermission("smartspawner.command.list")) {
               this.messageService.sendMessage(player, "no_permission");
            } else {
               event.setCancelled(true);
               ItemStack clickedItem = event.getCurrentItem();
               if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                  String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                  String targetServer = holder.getTargetServer();
                  boolean isRemote = holder.isRemoteServer();
                  if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
                     this.listSubCommand.openServerSelectionGUI(player);
                  } else if (isRemote) {
                     String worldName = this.extractWorldNameFromDisplay(displayName);
                     if (worldName != null) {
                        this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, 1);
                     }

                  } else if (event.getSlot() == 11 && displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.overworld.name")))) {
                     this.listSubCommand.openSpawnerListGUI(player, "world", 1);
                  } else if (event.getSlot() == 13 && displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.nether.name")))) {
                     this.listSubCommand.openSpawnerListGUI(player, "world_nether", 1);
                  } else if (event.getSlot() == 15 && displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.end.name")))) {
                     this.listSubCommand.openSpawnerListGUI(player, "world_the_end", 1);
                  } else {
                     if (displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.overworld.name")))) {
                        this.listSubCommand.openSpawnerListGUI(player, "world", 1);
                     } else if (displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.nether.name")))) {
                        this.listSubCommand.openSpawnerListGUI(player, "world_nether", 1);
                     } else if (displayName.equals(ChatColor.stripColor(this.languageManager.getGuiTitle("world_buttons.end.name")))) {
                        this.listSubCommand.openSpawnerListGUI(player, "world_the_end", 1);
                     } else {
                        Iterator var8 = Bukkit.getWorlds().iterator();

                        while(var8.hasNext()) {
                           World world = (World)var8.next();
                           String worldDisplayName = this.formatWorldName(world.getName());
                           if (this.spawnerManager.countSpawnersInWorld(world.getName()) > 0 && displayName.contains(worldDisplayName)) {
                              this.listSubCommand.openSpawnerListGUI(player, world.getName(), 1);
                              break;
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }

   private String extractWorldNameFromDisplay(String displayName) {
      if (!displayName.equalsIgnoreCase("Overworld") && !displayName.equalsIgnoreCase("World")) {
         if (!displayName.equalsIgnoreCase("Nether") && !displayName.equalsIgnoreCase("The Nether")) {
            return !displayName.equalsIgnoreCase("The End") && !displayName.equalsIgnoreCase("End") ? displayName.toLowerCase().replace(' ', '_') : "world_the_end";
         } else {
            return "world_nether";
         }
      } else {
         return "world";
      }
   }

   private String formatWorldName(String worldName) {
      return (String)Arrays.stream(worldName.replace('_', ' ').split(" ")).map((word) -> {
         String var10000 = word.substring(0, 1).toUpperCase();
         return var10000 + word.substring(1).toLowerCase();
      }).collect(Collectors.joining(" "));
   }

   @EventHandler
   public void onSpawnerListClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof SpawnerListHolder) {
         SpawnerListHolder holder = (SpawnerListHolder)var3;
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            if (!player.hasPermission("smartspawner.list")) {
               this.messageService.sendMessage(player, "no_permission");
            } else {
               event.setCancelled(true);
               if (event.getCurrentItem() != null) {
                  String worldName = holder.getWorldName();
                  int currentPage = holder.getCurrentPage();
                  int totalPages = holder.getTotalPages();
                  FilterOption currentFilter = holder.getFilterOption();
                  SortOption currentSort = holder.getSortType();
                  String targetServer = holder.getTargetServer();
                  boolean isRemote = holder.isRemoteServer();
                  if (event.getSlot() == 48) {
                     FilterOption nextFilter = currentFilter.getNextOption();
                     if (!isRemote) {
                        this.listSubCommand.saveUserPreference(player, worldName, nextFilter, currentSort);
                     }

                     if (isRemote) {
                        this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, 1, nextFilter, currentSort);
                     } else {
                        this.listSubCommand.openSpawnerListGUI(player, worldName, 1, nextFilter, currentSort);
                     }

                  } else if (event.getSlot() == 50) {
                     SortOption nextSort = currentSort.getNextOption();
                     if (!isRemote) {
                        this.listSubCommand.saveUserPreference(player, worldName, currentFilter, nextSort);
                     }

                     if (isRemote) {
                        this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, 1, currentFilter, nextSort);
                     } else {
                        this.listSubCommand.openSpawnerListGUI(player, worldName, 1, currentFilter, nextSort);
                     }

                  } else if (event.getSlot() == 45 && currentPage > 1) {
                     if (isRemote) {
                        this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, currentPage - 1);
                     } else {
                        this.listSubCommand.openSpawnerListGUI(player, worldName, currentPage - 1, currentFilter, currentSort);
                     }

                  } else if (event.getSlot() == 49) {
                     if (!isRemote) {
                        this.listSubCommand.saveUserPreference(player, worldName, currentFilter, currentSort);
                     }

                     if (isRemote) {
                        this.listSubCommand.openWorldSelectionGUIForServer(player, targetServer);
                     } else {
                        this.listSubCommand.openWorldSelectionGUI(player);
                     }

                  } else if (event.getSlot() == 53 && currentPage < totalPages) {
                     if (isRemote) {
                        this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, currentPage + 1);
                     } else {
                        this.listSubCommand.openSpawnerListGUI(player, worldName, currentPage + 1, currentFilter, currentSort);
                     }

                  } else {
                     if (this.isSpawnerItemSlot(event.getSlot()) && this.isSpawnerItem(event.getCurrentItem())) {
                        this.handleSpawnerItemClick(player, event.getCurrentItem(), holder);
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof SpawnerListHolder) {
         SpawnerListHolder holder = (SpawnerListHolder)var3;
         HumanEntity var4 = event.getPlayer();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            String worldName = holder.getWorldName();
            FilterOption currentFilter = holder.getFilterOption();
            SortOption currentSort = holder.getSortType();
            this.listSubCommand.saveUserPreference(player, worldName, currentFilter, currentSort);
         }
      }
   }

   private boolean isSpawnerItemSlot(int slot) {
      return slot < 45;
   }

   private boolean isSpawnerItem(ItemStack item) {
      return item != null && SPAWNER_MATERIALS.contains(item.getType()) && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
   }

   private void handleSpawnerItemClick(Player player, ItemStack item, SpawnerListHolder holder) {
      String displayName = item.getItemMeta().getDisplayName();
      Pattern pattern = Pattern.compile("#([A-Za-z0-9]+)");
      Matcher matcher = pattern.matcher(displayName);
      if (matcher.find()) {
         String spawnerId = matcher.group(1);
         String targetServer = holder.getTargetServer();
         boolean isRemote = holder.isRemoteServer();
         if (isRemote) {
            this.listSubCommand.openSpawnerManagementGUI(player, spawnerId, holder.getWorldName(), holder.getCurrentPage(), targetServer);
         } else {
            SpawnerData spawner = this.spawnerManager.getSpawnerById(spawnerId);
            if (spawner != null) {
               this.listSubCommand.openSpawnerManagementGUI(player, spawnerId, holder.getWorldName(), holder.getCurrentPage(), (String)null);
            } else {
               this.messageService.sendMessage(player, "spawner_not_found");
            }
         }
      }

   }

   static {
      SPAWNER_MATERIALS = EnumSet.of(Material.PLAYER_HEAD, Material.SPAWNER, Material.ZOMBIE_HEAD, Material.SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.CREEPER_HEAD, Material.PIGLIN_HEAD);
   }
}
