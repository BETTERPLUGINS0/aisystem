package github.nighter.smartspawner.commands.list;

import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import github.nighter.smartspawner.commands.list.gui.CrossServerSpawnerData;
import github.nighter.smartspawner.commands.list.gui.list.SpawnerListHolder;
import github.nighter.smartspawner.commands.list.gui.list.UserPreferenceCache;
import github.nighter.smartspawner.commands.list.gui.list.enums.FilterOption;
import github.nighter.smartspawner.commands.list.gui.list.enums.SortOption;
import github.nighter.smartspawner.commands.list.gui.management.SpawnerManagementGUI;
import github.nighter.smartspawner.commands.list.gui.serverselection.ServerSelectionHolder;
import github.nighter.smartspawner.commands.list.gui.worldselection.WorldSelectionHolder;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.config.SpawnerMobHeadTexture;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.data.database.SpawnerDatabaseHandler;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ListSubCommand extends BaseSubCommand {
   private final SpawnerManager spawnerManager;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final UserPreferenceCache userPreferenceCache;
   private final SpawnerManagementGUI spawnerManagementGUI;
   private static final int SPAWNERS_PER_PAGE = 45;

   public ListSubCommand(SmartSpawner plugin) {
      super(plugin);
      this.spawnerManager = plugin.getSpawnerManager();
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.userPreferenceCache = plugin.getUserPreferenceCache();
      this.spawnerManagementGUI = new SpawnerManagementGUI(plugin);
   }

   public String getName() {
      return "list";
   }

   public String getPermission() {
      return "smartspawner.command.list";
   }

   public String getDescription() {
      return "Open the spawner list GUI";
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      if (!this.isPlayer(((CommandSourceStack)context.getSource()).getSender())) {
         return 0;
      } else {
         Player player = this.getPlayer(((CommandSourceStack)context.getSource()).getSender());
         if (this.isCrossServerEnabled()) {
            this.openServerSelectionGUI(player);
         } else {
            this.openWorldSelectionGUI(player);
         }

         return 1;
      }
   }

   public boolean isCrossServerEnabled() {
      String modeStr = this.plugin.getConfig().getString("database.mode", "YAML").toUpperCase();

      try {
         StorageMode mode = StorageMode.valueOf(modeStr);
         if (mode != StorageMode.MYSQL) {
            return false;
         }
      } catch (IllegalArgumentException var3) {
         return false;
      }

      return this.plugin.getConfig().getBoolean("database.sync_across_servers", false);
   }

   public String getCurrentServerName() {
      return this.plugin.getConfig().getString("database.server_name", "server1");
   }

   public void openServerSelectionGUI(Player player) {
      if (!player.hasPermission("smartspawner.command.list")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         SpawnerDatabaseHandler dbHandler = this.getDbHandler();
         if (dbHandler == null) {
            this.openWorldSelectionGUI(player);
         } else {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            dbHandler.getDistinctServerNamesAsync((servers) -> {
               if (servers.isEmpty()) {
                  this.messageService.sendMessage(player, "no_spawners_found");
               } else {
                  int size = Math.max(9, (int)Math.ceil((double)servers.size() / 7.0D) * 9);
                  size = Math.min(54, size);
                  String title = this.languageManager.getGuiTitle("gui_title_server_selection");
                  if (title == null || title.isEmpty()) {
                     title = String.valueOf(ChatColor.DARK_GRAY) + "Select Server";
                  }

                  Inventory inv = Bukkit.createInventory(new ServerSelectionHolder(), size, title);
                  String currentServer = this.getCurrentServerName();
                  int slot = 0;

                  for(Iterator var8 = servers.iterator(); var8.hasNext(); ++slot) {
                     String serverName = (String)var8.next();
                     if (slot >= size) {
                        break;
                     }

                     while(slot < size && (slot % 9 == 0 || slot % 9 == 8)) {
                        ++slot;
                     }

                     if (slot >= size) {
                        break;
                     }

                     Material material = serverName.equals(currentServer) ? Material.EMERALD_BLOCK : Material.IRON_BLOCK;
                     ItemStack serverItem = this.createServerButton(serverName, material, serverName.equals(currentServer));
                     inv.setItem(slot, serverItem);
                  }

                  player.openInventory(inv);
               }
            });
         }
      }
   }

   private ItemStack createServerButton(String serverName, Material material, boolean isCurrentServer) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         String var10000 = String.valueOf(isCurrentServer ? ChatColor.GREEN : ChatColor.GOLD);
         String displayName = var10000 + serverName;
         meta.setDisplayName(displayName);
         List<String> lore = new ArrayList();
         if (isCurrentServer) {
            lore.add(String.valueOf(ChatColor.GRAY) + "Current Server");
         }

         lore.add(String.valueOf(ChatColor.YELLOW) + "Click to view spawners");
         meta.setLore(lore);
         item.setItemMeta(meta);
      }

      return item;
   }

   public void openWorldSelectionGUIForServer(Player player, String targetServer) {
      if (!player.hasPermission("smartspawner.command.list")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         String currentServer = this.getCurrentServerName();
         if (targetServer.equals(currentServer)) {
            this.openWorldSelectionGUI(player);
         } else {
            SpawnerDatabaseHandler dbHandler = this.getDbHandler();
            if (dbHandler == null) {
               this.messageService.sendMessage(player, "action_failed");
            } else {
               player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
               dbHandler.getWorldsForServerAsync(targetServer, (worldCounts) -> {
                  if (worldCounts.isEmpty()) {
                     this.messageService.sendMessage(player, "no_spawners_found");
                  } else {
                     int size = Math.max(27, (int)Math.ceil((double)(worldCounts.size() + 2) / 7.0D) * 9);
                     size = Math.min(54, size);
                     Map<String, String> titlePlaceholders = new HashMap();
                     titlePlaceholders.put("server", targetServer);
                     String title = this.languageManager.getGuiTitle("gui_title_world_selection_server", titlePlaceholders);
                     if (title == null || title.isEmpty()) {
                        String var10000 = String.valueOf(ChatColor.DARK_GRAY);
                        title = var10000 + "Worlds - " + targetServer;
                     }

                     Inventory inv = Bukkit.createInventory(new WorldSelectionHolder(targetServer), size, title);
                     int slot = 10;
                     Iterator var9 = worldCounts.entrySet().iterator();

                     while(var9.hasNext()) {
                        Entry<String, Integer> entry = (Entry)var9.next();
                        if (slot >= size - 9) {
                           break;
                        }

                        if (slot % 9 != 0 && slot % 9 != 8) {
                           String worldName = (String)entry.getKey();
                           int count = (Integer)entry.getValue();
                           Material material = this.getMaterialForWorldName(worldName);
                           ItemStack worldItem = this.createRemoteWorldButton(worldName, material, count, targetServer);
                           inv.setItem(slot, worldItem);
                           ++slot;
                        } else {
                           ++slot;
                        }
                     }

                     ItemStack backButton = this.createNavigationButton(Material.RED_STAINED_GLASS_PANE, "navigation.back");
                     inv.setItem(size - 5, backButton);
                     player.openInventory(inv);
                  }
               });
            }
         }
      }
   }

   private ItemStack createRemoteWorldButton(String worldName, Material material, int spawnerCount, String serverName) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         String var10001 = String.valueOf(ChatColor.GREEN);
         meta.setDisplayName(var10001 + this.formatWorldName(worldName));
         List<String> lore = new ArrayList();
         var10001 = String.valueOf(ChatColor.GRAY);
         lore.add(var10001 + "Server: " + String.valueOf(ChatColor.WHITE) + serverName);
         var10001 = String.valueOf(ChatColor.GRAY);
         lore.add(var10001 + "Spawners: " + String.valueOf(ChatColor.WHITE) + spawnerCount);
         lore.add("");
         lore.add(String.valueOf(ChatColor.YELLOW) + "Click to view spawners");
         meta.setLore(lore);
         item.setItemMeta(meta);
      }

      return item;
   }

   private Material getMaterialForWorldName(String worldName) {
      if (worldName.contains("nether")) {
         return Material.NETHERRACK;
      } else {
         return worldName.contains("end") ? Material.END_STONE : Material.GRASS_BLOCK;
      }
   }

   private SpawnerDatabaseHandler getDbHandler() {
      SpawnerStorage var2 = this.plugin.getSpawnerStorage();
      if (var2 instanceof SpawnerDatabaseHandler) {
         SpawnerDatabaseHandler dbHandler = (SpawnerDatabaseHandler)var2;
         return dbHandler;
      } else {
         return null;
      }
   }

   public void openWorldSelectionGUI(Player player) {
      if (!player.hasPermission("smartspawner.command.list")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
         List<World> worlds = (List)Bukkit.getWorlds().stream().filter((worldx) -> {
            return this.spawnerManager.countSpawnersInWorld(worldx.getName()) > 0;
         }).collect(Collectors.toList());
         List<World> customWorlds = (List)worlds.stream().filter((worldx) -> {
            return !this.isDefaultWorld(worldx.getName());
         }).collect(Collectors.toList());
         boolean hasCustomWorlds = !customWorlds.isEmpty();
         int size = hasCustomWorlds ? Math.max(27, (int)Math.ceil((double)(worlds.size() + 2) / 7.0D) * 9) : 27;
         Inventory inv = Bukkit.createInventory(new WorldSelectionHolder(), size, this.languageManager.getGuiTitle("gui_title_world_selection"));
         ItemStack backButton;
         if (!hasCustomWorlds) {
            backButton = this.createWorldButtonIfWorldExists("world", Material.GRASS_BLOCK, this.languageManager.getGuiTitle("world_buttons.overworld.name"));
            ItemStack netherButton = this.createWorldButtonIfWorldExists("world_nether", Material.NETHERRACK, this.languageManager.getGuiTitle("world_buttons.nether.name"));
            ItemStack endButton = this.createWorldButtonIfWorldExists("world_the_end", Material.END_STONE, this.languageManager.getGuiTitle("world_buttons.end.name"));
            if (backButton != null) {
               inv.setItem(11, backButton);
            }

            if (netherButton != null) {
               inv.setItem(13, netherButton);
            }

            if (endButton != null) {
               inv.setItem(15, endButton);
            }
         } else {
            int slot = 10;
            int row = 1;
            if (this.addWorldButtonIfExists(inv, "world", Material.GRASS_BLOCK, this.languageManager.getGuiTitle("world_buttons.overworld.name"), slot)) {
               ++slot;
            }

            if (this.addWorldButtonIfExists(inv, "world_nether", Material.NETHERRACK, this.languageManager.getGuiTitle("world_buttons.nether.name"), slot)) {
               ++slot;
            }

            if (this.addWorldButtonIfExists(inv, "world_the_end", Material.END_STONE, this.languageManager.getGuiTitle("world_buttons.end.name"), slot)) {
               ++slot;
            }

            Iterator var14 = customWorlds.iterator();

            while(var14.hasNext()) {
               World world = (World)var14.next();
               if (slot % 9 == 8) {
                  ++row;
                  slot = 9 * row + 1;
               }

               if (slot >= size) {
                  break;
               }

               Material material = this.getMaterialForWorldType(world.getEnvironment());
               this.addWorldButton(inv, world.getName(), material, this.formatWorldName(world.getName()), slot++);
            }
         }

         if (this.isCrossServerEnabled()) {
            backButton = this.createNavigationButton(Material.RED_STAINED_GLASS_PANE, "navigation.back");
            inv.setItem(size - 5, backButton);
         }

         player.openInventory(inv);
      }
   }

   private boolean isDefaultWorld(String worldName) {
      return worldName.equals("world") || worldName.equals("world_nether") || worldName.equals("world_the_end");
   }

   private ItemStack createWorldButtonIfWorldExists(String worldName, Material material, String displayName) {
      World world = Bukkit.getWorld(worldName);
      return world != null && this.spawnerManager.countSpawnersInWorld(worldName) > 0 ? this.createWorldButton(material, displayName, this.getWorldDescription(worldName)) : null;
   }

   private boolean addWorldButtonIfExists(Inventory inv, String worldName, Material material, String displayName, int slot) {
      World world = Bukkit.getWorld(worldName);
      if (world != null && this.spawnerManager.countSpawnersInWorld(worldName) > 0) {
         this.addWorldButton(inv, worldName, material, displayName, slot);
         return true;
      } else {
         return false;
      }
   }

   private void addWorldButton(Inventory inv, String worldName, Material material, String displayName, int slot) {
      if (!this.isDefaultWorld(worldName)) {
         World world = Bukkit.getWorld(worldName);
         if (world != null) {
            Environment environment = world.getEnvironment();
            String namePath;
            switch(environment) {
            case NORMAL:
               namePath = "world_buttons.custom_overworld.name";
               break;
            case NETHER:
               namePath = "world_buttons.custom_nether.name";
               break;
            case THE_END:
               namePath = "world_buttons.custom_end.name";
               break;
            default:
               namePath = "world_buttons.custom_default.name";
            }

            Map<String, String> placeholders = new HashMap();
            placeholders.put("world_name", displayName);
            displayName = this.languageManager.getGuiTitle(namePath, placeholders);
         }
      }

      ItemStack button = this.createWorldButton(material, displayName, this.getWorldDescription(worldName));
      inv.setItem(slot, button);
   }

   private Material getMaterialForWorldType(Environment environment) {
      Material var10000;
      switch(environment) {
      case NORMAL:
         var10000 = Material.GRASS_BLOCK;
         break;
      case NETHER:
         var10000 = Material.NETHERRACK;
         break;
      case THE_END:
         var10000 = Material.END_STONE;
         break;
      default:
         var10000 = Material.ENDER_PEARL;
      }

      return var10000;
   }

   private String formatWorldName(String worldName) {
      return (String)Arrays.stream(worldName.replace('_', ' ').split(" ")).map((word) -> {
         String var10000 = word.substring(0, 1).toUpperCase();
         return var10000 + word.substring(1).toLowerCase();
      }).collect(Collectors.joining(" "));
   }

   private List<String> getWorldDescription(String worldName) {
      new ArrayList();
      int physicalSpawners = this.spawnerManager.countSpawnersInWorld(worldName);
      int totalWithStacks = this.spawnerManager.countTotalSpawnersWithStacks(worldName);
      String path;
      if (worldName.equals("world")) {
         path = "world_buttons.overworld.lore";
      } else if (worldName.equals("world_nether")) {
         path = "world_buttons.nether.lore";
      } else if (worldName.equals("world_the_end")) {
         path = "world_buttons.end.lore";
      } else {
         World world = Bukkit.getWorld(worldName);
         Environment environment = world != null ? world.getEnvironment() : Environment.NORMAL;
         switch(environment) {
         case NORMAL:
            path = "world_buttons.overworld.lore";
            break;
         case NETHER:
            path = "world_buttons.nether.lore";
            break;
         case THE_END:
            path = "world_buttons.end.lore";
            break;
         default:
            path = "world_buttons.custom_default.lore";
         }
      }

      Map<String, String> placeholders = new HashMap();
      placeholders.put("total", String.valueOf(physicalSpawners));
      placeholders.put("total_stacked", this.languageManager.formatNumber((double)totalWithStacks));
      String[] loreArray = this.languageManager.getGuiItemLore(path, placeholders);
      return Arrays.asList(loreArray);
   }

   private ItemStack createWorldButton(Material material, String name, List<String> lore) {
      ItemStack button = new ItemStack(material);
      ItemMeta meta = button.getItemMeta();
      meta.setDisplayName(name);
      meta.setLore(lore);
      button.setItemMeta(meta);
      return button;
   }

   public void openSpawnerListGUI(Player player, String worldName, int page) {
      UserPreferenceCache.UserPreference preference = this.userPreferenceCache.getPreference(player.getUniqueId(), worldName);
      if (preference != null) {
         this.openSpawnerListGUI(player, worldName, page, preference.getFilterOption(), preference.getSortOption());
      } else {
         this.openSpawnerListGUI(player, worldName, page, FilterOption.ALL, SortOption.DEFAULT);
      }

   }

   public void saveUserPreference(Player player, String worldName, FilterOption filter, SortOption sort) {
      this.userPreferenceCache.savePreference(player.getUniqueId(), worldName, filter, sort);
   }

   public void openSpawnerListGUI(Player player, String worldName, int page, FilterOption filter, SortOption sortType) {
      if (!player.hasPermission("smartspawner.command.list")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
         List<SpawnerData> worldSpawners = (List)this.spawnerManager.getAllSpawners().stream().filter((spawnerx) -> {
            return spawnerx.getSpawnerLocation().getWorld().getName().equals(worldName);
         }).collect(Collectors.toList());
         if (filter == FilterOption.ACTIVE) {
            worldSpawners = (List)worldSpawners.stream().filter((spawnerx) -> {
               return !spawnerx.getSpawnerStop().get();
            }).collect(Collectors.toList());
         } else if (filter == FilterOption.INACTIVE) {
            worldSpawners = (List)worldSpawners.stream().filter((spawnerx) -> {
               return spawnerx.getSpawnerStop().get();
            }).collect(Collectors.toList());
         }

         switch(sortType) {
         case STACK_SIZE_ASC:
            worldSpawners.sort(Comparator.comparingInt(SpawnerData::getStackSize));
            break;
         case STACK_SIZE_DESC:
            worldSpawners.sort(Comparator.comparingInt(SpawnerData::getStackSize).reversed());
         }

         int totalPages = (int)Math.ceil((double)worldSpawners.size() / 45.0D);
         page = Math.max(1, Math.min(page, totalPages));
         byte var10 = -1;
         switch(worldName.hashCode()) {
         case -1198266272:
            if (worldName.equals("world_the_end")) {
               var10 = 2;
            }
            break;
         case 113318802:
            if (worldName.equals("world")) {
               var10 = 0;
            }
            break;
         case 1865466277:
            if (worldName.equals("world_nether")) {
               var10 = 1;
            }
         }

         String worldTitle;
         switch(var10) {
         case 0:
            worldTitle = this.languageManager.getGuiTitle("world_buttons.overworld.name");
            break;
         case 1:
            worldTitle = this.languageManager.getGuiTitle("world_buttons.nether.name");
            break;
         case 2:
            worldTitle = this.languageManager.getGuiTitle("world_buttons.end.name");
            break;
         default:
            worldTitle = this.formatWorldName(worldName);
         }

         Map<String, String> titlePlaceholders = new HashMap();
         worldTitle = ChatColor.stripColor(worldTitle);
         titlePlaceholders.put("world", worldTitle);
         titlePlaceholders.put("current", String.valueOf(page));
         titlePlaceholders.put("total", String.valueOf(totalPages));
         String title = this.languageManager.getGuiTitle("gui_title_spawner_list", titlePlaceholders);
         Inventory inv = Bukkit.createInventory(new SpawnerListHolder(page, totalPages, worldName, filter, sortType), 54, title);
         int startIndex = (page - 1) * 45;
         int endIndex = Math.min(startIndex + 45, worldSpawners.size());

         for(int i = startIndex; i < endIndex; ++i) {
            SpawnerData spawner = (SpawnerData)worldSpawners.get(i);
            inv.addItem(new ItemStack[]{this.createSpawnerInfoItem(spawner)});
         }

         this.addControlButtons(inv, filter, sortType);
         if (page > 1) {
            inv.setItem(45, this.createNavigationButton(Material.SPECTRAL_ARROW, "navigation.previous_page"));
         }

         inv.setItem(49, this.createNavigationButton(Material.RED_STAINED_GLASS_PANE, "navigation.back"));
         if (page < totalPages) {
            inv.setItem(53, this.createNavigationButton(Material.SPECTRAL_ARROW, "navigation.next_page"));
         }

         player.openInventory(inv);
      }
   }

   private void addControlButtons(Inventory inv, FilterOption currentFilter, SortOption currentSort) {
      ItemStack filterButton = this.createEnhancedControlButton(Material.CAULDRON, "filter", currentFilter);
      ItemStack sortButton = this.createEnhancedControlButton(Material.HOPPER, "sort", currentSort);
      inv.setItem(48, filterButton);
      inv.setItem(50, sortButton);
   }

   private ItemStack createEnhancedControlButton(Material material, String controlType, Enum<?> currentOption) {
      ItemStack button = new ItemStack(material);
      ItemMeta meta = button.getItemMeta();
      if (meta == null) {
         return button;
      } else {
         Map<String, String> placeholders = new HashMap();
         String selectedFormat = this.languageManager.getGuiItemName(controlType + ".selected_option");
         String unselectedFormat = this.languageManager.getGuiItemName(controlType + ".unselected_option");
         StringBuilder availableOptions = new StringBuilder();
         boolean first = true;
         int var13;
         int var14;
         LanguageManager var10000;
         String optionName;
         String var10001;
         String format;
         String formattedOption;
         if (controlType.equals("filter")) {
            FilterOption currentFilter = (FilterOption)currentOption;
            FilterOption[] var12 = FilterOption.values();
            var13 = var12.length;

            for(var14 = 0; var14 < var13; ++var14) {
               FilterOption option = var12[var14];
               if (!first) {
                  availableOptions.append("\n");
               }

               var10000 = this.languageManager;
               var10001 = option.getName();
               optionName = var10000.getGuiItemName("filter." + var10001);
               format = option == currentFilter ? selectedFormat : unselectedFormat;
               formattedOption = format.replace("{option_name}", optionName);
               availableOptions.append(formattedOption);
               first = false;
            }

            meta.setDisplayName(this.languageManager.getGuiItemName("filter.button.name"));
         } else if (controlType.equals("sort")) {
            SortOption currentSort = (SortOption)currentOption;
            SortOption[] var21 = SortOption.values();
            var13 = var21.length;

            for(var14 = 0; var14 < var13; ++var14) {
               SortOption option = var21[var14];
               if (!first) {
                  availableOptions.append("\n");
               }

               var10000 = this.languageManager;
               var10001 = option.getName();
               optionName = var10000.getGuiItemName("sort." + var10001);
               format = option == currentSort ? selectedFormat : unselectedFormat;
               formattedOption = format.replace("{option_name}", optionName);
               availableOptions.append(formattedOption);
               first = false;
            }

            meta.setDisplayName(this.languageManager.getGuiItemName("sort.button.name"));
         }

         placeholders.put("available_options", availableOptions.toString());
         String lorePath = controlType + ".button.lore";
         List<String> lore = this.languageManager.getGuiItemLoreWithMultilinePlaceholders(lorePath, placeholders);
         meta.setLore(lore);
         button.setItemMeta(meta);
         return button;
      }
   }

   private ItemStack createNavigationButton(Material material, String namePath) {
      ItemStack button = new ItemStack(material);
      ItemMeta meta = button.getItemMeta();
      meta.setDisplayName(this.languageManager.getGuiItemName(namePath));
      button.setItemMeta(meta);
      return button;
   }

   private ItemStack createSpawnerInfoItem(SpawnerData spawner) {
      EntityType entityType = spawner.getEntityType();
      Location loc = spawner.getSpawnerLocation();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("id", String.valueOf(spawner.getSpawnerId()));
      placeholders.put("entity", this.languageManager.getFormattedMobName(entityType));
      placeholders.put("size", String.valueOf(spawner.getStackSize()));
      if (spawner.getSpawnerStop().get()) {
         placeholders.put("status_color", "&#ff6b6b");
         placeholders.put("status_text", "Inactive");
      } else {
         placeholders.put("status_color", "&#00E689");
         placeholders.put("status_text", "Active");
      }

      placeholders.put("x", String.valueOf(loc.getBlockX()));
      placeholders.put("y", String.valueOf(loc.getBlockY()));
      placeholders.put("z", String.valueOf(loc.getBlockZ()));
      String lastPlayer = spawner.getLastInteractedPlayer();
      placeholders.put("last_player", lastPlayer != null ? lastPlayer : "None");
      ItemStack spawnerItem;
      if (entityType == null) {
         spawnerItem = new ItemStack(Material.SPAWNER);
         spawnerItem.editMeta((meta) -> {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
            meta.setDisplayName(this.languageManager.getGuiItemName("spawner_item_list.name", placeholders));
            List<String> lore = Arrays.asList(this.languageManager.getGuiItemLore("spawner_item_list.lore", placeholders));
            meta.setLore(lore);
         });
      } else {
         spawnerItem = SpawnerMobHeadTexture.getCustomHead(entityType, (meta) -> {
            meta.setDisplayName(this.languageManager.getGuiItemName("spawner_item_list.name", placeholders));
            List<String> lore = Arrays.asList(this.languageManager.getGuiItemLore("spawner_item_list.lore", placeholders));
            meta.setLore(lore);
         });
      }

      VersionInitializer.hideTooltip(spawnerItem);
      return spawnerItem;
   }

   public void openSpawnerManagementGUI(Player player, String spawnerId, String worldName, int listPage) {
      this.spawnerManagementGUI.openManagementMenu(player, spawnerId, worldName, listPage);
   }

   public void openSpawnerManagementGUI(Player player, String spawnerId, String worldName, int listPage, String targetServer) {
      this.spawnerManagementGUI.openManagementMenu(player, spawnerId, worldName, listPage, targetServer);
   }

   public void openSpawnerListGUIForServer(Player player, String targetServer, String worldName, int page) {
      this.openSpawnerListGUIForServer(player, targetServer, worldName, page, FilterOption.ALL, SortOption.DEFAULT);
   }

   public void openSpawnerListGUIForServer(Player player, String targetServer, String worldName, int page, FilterOption filter, SortOption sort) {
      if (!player.hasPermission("smartspawner.command.list")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         String currentServer = this.getCurrentServerName();
         if (targetServer.equals(currentServer)) {
            this.openSpawnerListGUI(player, worldName, page, filter, sort);
         } else {
            SpawnerDatabaseHandler dbHandler = this.getDbHandler();
            if (dbHandler == null) {
               this.messageService.sendMessage(player, "action_failed");
            } else {
               player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
               dbHandler.getCrossServerSpawnersAsync(targetServer, worldName, filter.name(), sort.name(), (spawners) -> {
                  if (spawners.isEmpty()) {
                     this.messageService.sendMessage(player, "no_spawners_found");
                  } else {
                     int totalPages = (int)Math.ceil((double)spawners.size() / 45.0D);
                     int currentPage = Math.max(1, Math.min(page, totalPages));
                     String worldTitle = this.formatWorldName(worldName);
                     Map<String, String> titlePlaceholders = new HashMap();
                     titlePlaceholders.put("world", worldTitle);
                     titlePlaceholders.put("current", String.valueOf(currentPage));
                     titlePlaceholders.put("total", String.valueOf(totalPages));
                     String title = this.languageManager.getGuiTitle("gui_title_spawner_list", titlePlaceholders);
                     Inventory inv = Bukkit.createInventory(new SpawnerListHolder(currentPage, totalPages, worldName, filter, sort, targetServer), 54, title);
                     int startIndex = (currentPage - 1) * 45;
                     int endIndex = Math.min(startIndex + 45, spawners.size());

                     for(int i = startIndex; i < endIndex; ++i) {
                        CrossServerSpawnerData spawner = (CrossServerSpawnerData)spawners.get(i);
                        inv.addItem(new ItemStack[]{this.createCrossServerSpawnerItem(spawner, targetServer)});
                     }

                     if (currentPage > 1) {
                        inv.setItem(45, this.createNavigationButton(Material.SPECTRAL_ARROW, "navigation.previous_page"));
                     }

                     this.addControlButtons(inv, filter, sort);
                     inv.setItem(49, this.createNavigationButton(Material.RED_STAINED_GLASS_PANE, "navigation.back"));
                     if (currentPage < totalPages) {
                        inv.setItem(53, this.createNavigationButton(Material.SPECTRAL_ARROW, "navigation.next_page"));
                     }

                     player.openInventory(inv);
                  }
               });
            }
         }
      }
   }

   private ItemStack createCrossServerSpawnerItem(CrossServerSpawnerData spawner, String serverName) {
      EntityType entityType = spawner.getEntityType();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("id", spawner.getSpawnerId());
      placeholders.put("entity", this.languageManager.getFormattedMobName(entityType));
      placeholders.put("size", String.valueOf(spawner.getStackSize()));
      if (!spawner.isActive()) {
         placeholders.put("status_color", "&#ff6b6b");
         placeholders.put("status_text", "Inactive");
      } else {
         placeholders.put("status_color", "&#00E689");
         placeholders.put("status_text", "Active");
      }

      placeholders.put("x", String.valueOf(spawner.getLocX()));
      placeholders.put("y", String.valueOf(spawner.getLocY()));
      placeholders.put("z", String.valueOf(spawner.getLocZ()));
      String lastPlayer = spawner.getLastInteractedPlayer();
      placeholders.put("last_player", lastPlayer != null ? lastPlayer : "None");
      ItemStack spawnerItem;
      if (entityType == null) {
         spawnerItem = new ItemStack(Material.SPAWNER);
         spawnerItem.editMeta((meta) -> {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
            meta.setDisplayName(this.languageManager.getGuiItemName("spawner_item_list.name", placeholders));
            List<String> lore = new ArrayList(Arrays.asList(this.languageManager.getGuiItemLore("spawner_item_list.lore", placeholders)));
            lore.add("");
            String var10001 = String.valueOf(ChatColor.DARK_GRAY);
            lore.add(var10001 + "Server: " + String.valueOf(ChatColor.WHITE) + serverName);
            meta.setLore(lore);
         });
      } else {
         spawnerItem = SpawnerMobHeadTexture.getCustomHead(entityType, (meta) -> {
            meta.setDisplayName(this.languageManager.getGuiItemName("spawner_item_list.name", placeholders));
            List<String> lore = new ArrayList(Arrays.asList(this.languageManager.getGuiItemLore("spawner_item_list.lore", placeholders)));
            lore.add("");
            String var10001 = String.valueOf(ChatColor.DARK_GRAY);
            lore.add(var10001 + "Server: " + String.valueOf(ChatColor.WHITE) + serverName);
            meta.setLore(lore);
         });
      }

      VersionInitializer.hideTooltip(spawnerItem);
      return spawnerItem;
   }

   public FilterOption getUserFilter(Player player, String worldName) {
      return this.userPreferenceCache.getUserFilter(player, worldName);
   }

   public SortOption getUserSort(Player player, String worldName) {
      return this.userPreferenceCache.getUserSort(player, worldName);
   }
}
