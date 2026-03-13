package me.gypopo.economyshopgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.commands.SellGUI;
import me.gypopo.economyshopgui.commands.Sellall;
import me.gypopo.economyshopgui.commands.Shop;
import me.gypopo.economyshopgui.commands.ShopGive;
import me.gypopo.economyshopgui.commands.Sreload;
import me.gypopo.economyshopgui.commands.editshop.CommandManager;
import me.gypopo.economyshopgui.events.JoinEvent;
import me.gypopo.economyshopgui.events.LevelEvent;
import me.gypopo.economyshopgui.events.MenuHandler;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.TransactionLog;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.methodes.StartupReload;
import me.gypopo.economyshopgui.methodes.UpdateChecker;
import me.gypopo.economyshopgui.metrics.Metrics;
import me.gypopo.economyshopgui.objects.DisplayItem;
import me.gypopo.economyshopgui.objects.LoreFormatter;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.mappings.ClickMappings;
import me.gypopo.economyshopgui.objects.navbar.NavBar;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.providers.GeyserModule;
import me.gypopo.economyshopgui.providers.ModifierManager;
import me.gypopo.economyshopgui.providers.RequirementManager;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.StandProvider;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.providers.placeholders.PAPIExpansion;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.ConfigUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.EconomyHandler;
import me.gypopo.economyshopgui.util.EconomyType;
import me.gypopo.economyshopgui.util.MetaUtils;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.meta.AdventureMeta;
import me.gypopo.economyshopgui.util.meta.BukkitMeta;
import me.gypopo.economyshopgui.util.meta.PaperMeta;
import me.gypopo.economyshopgui.util.scheduler.ScheduledTask;
import me.gypopo.economyshopgui.util.scheduler.ServerScheduler;
import me.gypopo.economyshopgui.util.scheduler.schedulers.BukkitScheduler;
import me.gypopo.economyshopgui.util.scheduler.schedulers.FoliaScheduler;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EconomyShopGUI extends JavaPlugin {
   public final int version = this.getVersion();
   public final boolean spigot = this.isSpigotServer();
   private Map<Integer, ItemStack> sectionItems = new HashMap();
   private Map<Integer, String> mainMenuItemSlots = new HashMap();
   private Map<String, ShopSection> sections = new HashMap();
   private List<String> shopSections = new ArrayList();
   private Map<String, Translatable> sectionTitles = new HashMap();
   public final List<String> supportedMatNames = this.setSupportedMatNames();
   public Map<String, List<ShopItem>> shopItemsByMaterialName;
   public List<String> ignoredNBTData;
   private final Map<String, HashMap<String, Double>> sellMultipliers = new HashMap();
   private final Map<String, HashMap<String, Double>> discounts = new HashMap();
   public List<GameMode> bannedGamemodes = new ArrayList();
   public Map<EntityType, Translatable> spawnerNames = new HashMap();
   private final EconomyHandler ecoHandler = new EconomyHandler(this);
   public Permission permissions;
   public boolean boughtItemsLore;
   public boolean dropItemsOnGround;
   public boolean allowIllegalStacks;
   public int maxShopSize;
   public int mainMenuSize;
   public boolean shopStands;
   public boolean allowUnsafeEnchants;
   public boolean discountsActive;
   public boolean multipliers;
   public boolean useItemName;
   public boolean bedrock;
   public boolean resizeGUI;
   public boolean prefixSuffix = true;
   public boolean prioritizeItemLore;
   public boolean sellShulkers;
   public boolean MMB;
   public boolean seasonalLore;
   public boolean matchMeta;
   public boolean paperMeta;
   public String badYMLParse = null;
   private final Pattern rgbPattern = Pattern.compile("#[a-fA-F0-9]{6}");
   private static EconomyShopGUI instance;
   public VersionHandler versionHandler;
   public StartupReload startupReload = new StartupReload(this);
   private SendMessage sendMessage = new SendMessage(this);
   public CalculateAmount calculateAmount = new CalculateAmount(this);
   public CreateItem createItem = new CreateItem(this);
   public NavBar navBar = new NavBar(this);
   private JoinEvent joinEvent;
   public UpdateChecker updateChecker;
   private final EconomyShopGUIHook api = new EconomyShopGUIHook(this);
   private final TransactionLog transactionLog = new TransactionLog(this);
   private final UserManager userManager = new UserManager(this);
   private final ConfigManager configManager = new ConfigManager(this);
   private final ServerScheduler scheduler = this.getScheduler();
   private final RequirementManager requirementManager = new RequirementManager(this);
   private StandProvider standProvider;
   private GeyserModule geyserModule;
   private PAPIExpansion papiExpansion;
   private LoreFormatter loreFormatter;
   private ModifierManager modifierManager;
   private SkullUtil skullUtil;
   private MetaUtils metaUtils;
   private ClickMappings mappings;
   private Chat chat;
   private final SpawnerManager spawnerManager = new SpawnerManager(this);

   public static EconomyShopGUI getInstance() {
      return instance;
   }

   public void onEnable() {
      long start = System.currentTimeMillis();
      instance = this;
      this.loadMetaUtils();
      this.configManager.loadConfig();
      Lang.CONFIGS_RELOADED.reload();
      this.metaUtils.update();
      this.configManager.init();
      this.getConfigManager().validate();
      this.enableGeyserExtension();
      if (this.badYMLParse == null) {
         if (!this.ecoHandler.registerPaymentSolutions()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
         }

         ConfigUtil.updateConfigs();
         this.mappings = new ClickMappings((String)null);
         this.startupReload.setupPluginVersion();
         this.spawnerManager.init();
         this.setSkullUtil(new SkullUtil(this));
         this.userManager.init();
         this.setModifierManager(new ModifierManager(this));
         this.setupPlaceholderExpansion();
         this.startupReload.checkDebugMode();
         this.startupReload.loadInventoryTitles();
         this.startupReload.loadItems();
         if (ConfigManager.getConfig().getBoolean("update-checking", true)) {
            this.updateChecker = new UpdateChecker();
         }

         SendMessage.infoMessage(Lang.DONE.get().replace("%millis%", String.valueOf(System.currentTimeMillis() - start)));
      } else {
         SendMessage.errorMessage("Skipping all settings and item loading because the configuration files could not be loaded...");
      }

      if (this.checkForGson()) {
         int pluginId = 5282;
         new Metrics(this, pluginId);
      }

      this.registerPermissions();
      this.registerEvents();
      this.registerCommands();
      this.reloadPlayerData();
   }

   public void onDisable() {
      this.closeOpenGUIS();
      this.transactionLog.saveUnsaved();
      if (this.skullUtil != null) {
         this.skullUtil.saveCache();
      }

      if (this.standProvider != null) {
         this.standProvider.stop();
      }

      this.sections.clear();
      this.supportedMatNames.clear();
      this.discounts.clear();
      this.spawnerNames.clear();
      Lang.DISABLING_PLUGIN.clearMessages();
      Bukkit.getPluginManager().disablePlugin(instance);
      instance = null;
   }

   public ShopItem getShopItem(String itemPath) {
      return ((ShopSection)this.sections.get(itemPath.split("\\.")[0])).getShopItem(itemPath);
   }

   public DisplayItem getDisplayItem(String itemPath) {
      return ((ShopSection)this.sections.get(itemPath.split("\\.")[0])).getDisplayItem(itemPath);
   }

   public DisplayItem getDisplayItem(String section, String itemLoc) {
      return ((ShopSection)this.sections.get(section)).getDisplayItem(section + "." + itemLoc);
   }

   public TransactionLog getTransactionLog() {
      return this.transactionLog;
   }

   public Map<Integer, String> getMainMenuItemSlots() {
      return this.mainMenuItemSlots;
   }

   public String getMainMenuSectionForSlot(int slot) {
      return (String)this.mainMenuItemSlots.get(slot);
   }

   public void setMainMenuItemSlots(Map<Integer, String> newMainMenuItemSlots) {
      this.mainMenuItemSlots = newMainMenuItemSlots;
   }

   public Map<Integer, ItemStack> getSectionItems() {
      return this.sectionItems;
   }

   public void setSectionItems(Map<Integer, ItemStack> newSectionItems) {
      this.sectionItems = newSectionItems;
   }

   public void addSection(String section, ShopSection shopSection) {
      this.sections.put(section, shopSection);
   }

   public Map<String, ShopSection> getSections() {
      return this.sections;
   }

   public ShopSection getSection(String section) {
      return (ShopSection)this.sections.get(section);
   }

   public void setShopSections(List<String> newShopSections) {
      this.shopSections = newShopSections;
   }

   public List<String> getShopSections() {
      return this.shopSections;
   }

   public void removeShopSection(String section) {
      this.shopSections.remove(section);
   }

   public List<String> getSupportedMatNames() {
      return this.supportedMatNames;
   }

   public void addSpawnerName(EntityType entityType, Translatable name) {
      this.spawnerNames.put(entityType, name);
   }

   public Translatable getSpawnerName(EntityType entityType) {
      return (Translatable)this.spawnerNames.get(entityType);
   }

   public boolean isSeasonalPricing() {
      return this.modifierManager.hasSeasonalPrices();
   }

   private void setModifierManager(ModifierManager modifierManager) {
      this.modifierManager = modifierManager;
   }

   public void reloadModifiers() {
      this.modifierManager.reloadModifiers();
   }

   public void setStandProvider(StandProvider standProvider) {
      this.standProvider = standProvider;
   }

   public StandProvider getStandProvider() {
      return this.standProvider;
   }

   private void setSkullUtil(SkullUtil skullUtil) {
      this.skullUtil = skullUtil;
   }

   public HashMap<String, Double> getDiscounts(String section) {
      return (HashMap)this.discounts.get(section);
   }

   public void setDiscounts(HashMap<String, HashMap<String, Double>> discounts) {
      if (!this.discounts.isEmpty()) {
         this.discounts.clear();
      }

      this.discounts.putAll(discounts);
   }

   public boolean hasDiscount(String section) {
      return this.discounts.containsKey(section);
   }

   public HashMap<String, Double> getMultipliers(String section) {
      return (HashMap)this.sellMultipliers.get(section);
   }

   public boolean hasMultiplier(String section) {
      return this.sellMultipliers.containsKey(section);
   }

   public void setSellMultipliers(HashMap<String, HashMap<String, Double>> sellMultipliers) {
      if (!this.sellMultipliers.isEmpty()) {
         this.sellMultipliers.clear();
      }

      this.sellMultipliers.putAll(sellMultipliers);
   }

   public Map<String, Translatable> getSectionTitles() {
      return this.sectionTitles;
   }

   public void setSectionTitles(HashMap<String, Translatable> newSectionTitles) {
      this.sectionTitles = newSectionTitles;
   }

   public Translatable getSectionTitle(String section) {
      return (Translatable)this.sectionTitles.get(section);
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public SpawnerManager getSpawnerManager() {
      return this.spawnerManager;
   }

   public Chat getChat() {
      return this.chat;
   }

   public void setLoreFormatter(LoreFormatter loreFormatter) {
      this.loreFormatter = loreFormatter;
   }

   public LoreFormatter getLoreFormatter() {
      return this.loreFormatter;
   }

   public MetaUtils getMetaUtils() {
      return this.metaUtils;
   }

   private void enableGeyserExtension() {
      if (ConfigManager.getConfig().getBoolean("pre-register-skulls", false)) {
         if (this.getServer().getPluginManager().getPlugin("Geyser-Spigot") == null) {
            SendMessage.errorMessage("Cannot enable Geyser hook: Failed to find Geyser-Spigot");
         } else {
            this.geyserModule = new GeyserModule(this);
         }
      }
   }

   public String getDisplayName(Player p, String s) {
      try {
         return this.chat != null && this.prefixSuffix ? this.getPlayerPrefix(p) + ChatColor.getLastColors(s.split("%player_displayname%")[0]) + p.getDisplayName() + this.getPlayerSuffix(p) : p.getDisplayName();
      } catch (ArrayIndexOutOfBoundsException var4) {
         return this.getPlayerPrefix(p) + p.getDisplayName() + this.getPlayerSuffix(p);
      }
   }

   public String getDisplayName(Player p) {
      return this.chat != null && this.prefixSuffix ? this.getPlayerPrefix(p) + p.getDisplayName() + this.getPlayerSuffix(p) : p.getDisplayName();
   }

   private String getPlayerPrefix(Player p) {
      return !this.chat.getPlayerPrefix(p).isEmpty() ? ChatUtil.formatColors(this.chat.getPlayerPrefix(p) + "&r ") : "";
   }

   private String getPlayerSuffix(Player p) {
      return !this.chat.getPlayerSuffix(p).isEmpty() ? ChatUtil.formatColors("&r " + this.chat.getPlayerSuffix(p)) : "";
   }

   public RequirementManager getRequirementManager() {
      return this.requirementManager;
   }

   public EconomyHandler getEcoHandler() {
      return this.ecoHandler;
   }

   public void updateClickMappings() {
      this.mappings = new ClickMappings((String)null);
   }

   public ClickMappings getClickMappings() {
      return this.mappings;
   }

   private void setupPlaceholderExpansion() {
      if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
         this.papiExpansion = new PAPIExpansion(this);
      }

   }

   public void reloadPlayerData() {
      this.userManager.reloadPlayerData();
   }

   public void reloadPermissions() {
      org.bukkit.permissions.Permission perm = this.getServer().getPluginManager().getPermission("EconomyShopGUI.shop.all");
      if (perm != null) {
         this.shopSections.forEach((s) -> {
            String node = "EconomyShopGUI.shop." + s.toLowerCase(Locale.ENGLISH);
            if (!perm.getChildren().containsKey(node)) {
               perm.getChildren().put(node, true);
            }

         });
      }

   }

   private void registerPermissions() {
      try {
         Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("EconomyShopGUI.shop.all", "Gives access to all shop category's", PermissionDefault.TRUE, (Map)this.shopSections.stream().map((s) -> {
            return "EconomyShopGUI.shop." + s.toLowerCase(Locale.ENGLISH);
         }).collect(Collectors.toMap(Function.identity(), (s) -> {
            return true;
         }))));
         Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("EconomyShopGUI.sellall.all", "Allows players to sell items to every shop section with the /sellall command", PermissionDefault.TRUE, (Map)this.shopSections.stream().map((s) -> {
            return "EconomyShopGUI.sellall." + s.toLowerCase(Locale.ENGLISH);
         }).collect(Collectors.toMap(Function.identity(), (s) -> {
            return true;
         }))));
         Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("EconomyShopGUI.sellallitem.all", "Allows players to sell items to every shop section with the /sellall <item> command", PermissionDefault.TRUE, (Map)this.shopSections.stream().map((s) -> {
            return "EconomyShopGUI.sellallitem." + s.toLowerCase(Locale.ENGLISH);
         }).collect(Collectors.toMap(Function.identity(), (s) -> {
            return true;
         }))));
         Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("EconomyShopGUI.sellallhand.all", "Allows players to sell items to every shop section with the /sellall hand command", PermissionDefault.TRUE, (Map)this.shopSections.stream().map((s) -> {
            return "EconomyShopGUI.sellallhand." + s.toLowerCase(Locale.ENGLISH);
         }).collect(Collectors.toMap(Function.identity(), (s) -> {
            return true;
         }))));
         Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("EconomyShopGUI.sellgui.all", "Allows players to sell items to every shop section with the SellGUI screen", PermissionDefault.TRUE, (Map)this.shopSections.stream().map((s) -> {
            return "EconomyShopGUI.sellgui." + s.toLowerCase(Locale.ENGLISH);
         }).collect(Collectors.toMap(Function.identity(), (s) -> {
            return true;
         }))));
      } catch (IllegalArgumentException var2) {
         SendMessage.logDebugMessage("Failed to register permissions, ignore this if you reloaded the plugin.");
         SendMessage.logDebugMessage(var2.getMessage());
      }

   }

   private void registerEvents() {
      this.getServer().getPluginManager().registerEvents(new MenuHandler(this), this);
      if (this.badYMLParse == null) {
         if (ConfigManager.getConfig().getBoolean("enable-levelevent")) {
            this.getServer().getPluginManager().registerEvents(new LevelEvent(), this);
         }

         this.getServer().getPluginManager().registerEvents(this.joinEvent = new JoinEvent(this, ConfigManager.getConfig().getBoolean("enable-joinmessage")), this);
      }

   }

   private void registerCommands() {
      try {
         Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
         bukkitCommandMap.setAccessible(true);
         CommandMap commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.shopgive", true)) {
            commandMap.register("economyshopgui", new ShopGive(this, this.getDisabledWorlds("shopgive")));
         }

         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.editshop", true)) {
            commandMap.register("economyshopgui", new CommandManager(this, this.getDisabledWorlds("editshop")));
         }

         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.sreload", true)) {
            commandMap.register("economyshopgui", new Sreload(this, this.getDisabledWorlds("sreload")));
         }

         Object sellguiAliases;
         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.shop", true)) {
            sellguiAliases = this.badYMLParse != null ? new ArrayList() : ConfigManager.getConfig().getStringList("shop-commands");
            if (((List)sellguiAliases).isEmpty()) {
               ((List)sellguiAliases).add("shop");
            }

            commandMap.register("economyshopgui", new Shop(this, (List)sellguiAliases, this.getDisabledWorlds("shop")));
         }

         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.sellall", true)) {
            sellguiAliases = this.badYMLParse != null ? new ArrayList() : ConfigManager.getConfig().getStringList("sellall-commands");
            if (((List)sellguiAliases).isEmpty()) {
               ((List)sellguiAliases).add("sellall");
            }

            if (this.badYMLParse == null && Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
               this.overrideSellCommand(ConfigManager.getConfig().getBoolean("override-sell-command", true));
            }

            commandMap.register("economyshopgui", new Sellall(this, (List)sellguiAliases, this.getDisabledWorlds("sellall")));
         }

         if (this.badYMLParse != null || ConfigManager.getConfig().getBoolean("commands.sellgui", true)) {
            sellguiAliases = this.badYMLParse != null ? new ArrayList() : ConfigManager.getConfig().getStringList("sellgui-commands");
            if (((List)sellguiAliases).isEmpty()) {
               ((List)sellguiAliases).add("sellgui");
            }

            commandMap.register("economyshopgui", new SellGUI(this, (List)sellguiAliases, this.getDisabledWorlds("sellgui")));
         }
      } catch (IllegalAccessException | NoSuchFieldException var4) {
         SendMessage.errorMessage("Error registering commands.");
         var4.printStackTrace();
      }

   }

   private List<String> getDisabledWorlds(String commandName) {
      return (List)(this.badYMLParse == null && ConfigManager.getConfig().getBoolean("enable-disabled-worlds", false) ? ConfigManager.getConfig().getStringList("disabled-worlds-per-command." + commandName) : new ArrayList());
   }

   public void loadChat() {
      RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
      if (rsp != null) {
         this.chat = (Chat)rsp.getProvider();
      }

   }

   public String initLore(String s) {
      ItemStack item = new ItemStack(Material.BARRIER);
      ItemMeta meta = item.getItemMeta();
      meta.setLore(Collections.singletonList(s));
      item.setItemMeta(meta);
      return this.versionHandler.getLoreAsNBT(item);
   }

   public YamlConfiguration loadConfiguration(File file, String fileName) {
      Validate.notNull(file, "Cannot load " + fileName + " config file.");
      YamlConfiguration config = new YamlConfiguration();

      try {
         config.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
         if (this.badYMLParse != null && this.badYMLParse.equals(fileName)) {
            this.badYMLParse = null;
         }

         return config;
      } catch (FileNotFoundException var5) {
      } catch (IOException var6) {
         var6.printStackTrace();
      } catch (InvalidConfigurationException var7) {
         SendMessage.errorMessage("Cannot read " + fileName + " config because it is mis-configured, use a online Yaml parser with the error underneath here to find out the cause of the problem and to solve it. If you cannot find the cause yourself, join our discord support server that can be found at a plugin page of EconomyShopGUI.");
         var7.printStackTrace();
      }

      this.badYMLParse = fileName;
      return null;
   }

   public YamlConfiguration loadConfiguration(BufferedReader reader, String fileName) {
      Validate.notNull(reader, "Cannot load " + fileName + " config file.");
      YamlConfiguration config = new YamlConfiguration();

      try {
         config.load(reader);
         if (this.badYMLParse != null && this.badYMLParse.equals(fileName)) {
            this.badYMLParse = null;
         }

         return config;
      } catch (FileNotFoundException var5) {
      } catch (IOException var6) {
         var6.printStackTrace();
      } catch (InvalidConfigurationException var7) {
         SendMessage.errorMessage("Cannot read " + fileName + " config because it is mis-configured, use a online Yaml parser with the error underneath here to find out the cause of the problem and to solve it. If you cannot find the cause yourself, join our discord support server that can be found at a plugin page of EconomyShopGUI.");
         var7.printStackTrace();
      }

      this.badYMLParse = fileName;
      return null;
   }

   public void createConfigBackup() {
      String fileName = "config.yml";
      SendMessage.infoMessage(Lang.CREATING_BACKUP.get().replace("%fileName%", fileName));
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
      LocalDateTime now = LocalDateTime.now();
      Path backupDir = Paths.get(getInstance().getDataFolder() + "/backups/");
      if (!Files.exists(backupDir, new LinkOption[0]) || !Files.isDirectory(backupDir, new LinkOption[0])) {
         try {
            Files.createDirectory(backupDir);
         } catch (FileAlreadyExistsException var9) {
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }

      File file = new File(getInstance().getDataFolder(), fileName);

      try {
         try {
            Files.copy(file.toPath(), Paths.get(getInstance().getDataFolder() + "/backups/", "config " + dtf.format(now) + ".yml"));
         } catch (FileAlreadyExistsException var7) {
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void createConfigBackup(String fileName) {
      SendMessage.logDebugMessage(Lang.CREATING_BACKUP.get().replace("%fileName%", fileName));
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
      LocalDateTime now = LocalDateTime.now();
      Path backupDir = Paths.get(getInstance().getDataFolder() + "/backups/");
      if (!Files.exists(backupDir, new LinkOption[0]) || !Files.isDirectory(backupDir, new LinkOption[0])) {
         try {
            Files.createDirectory(backupDir);
         } catch (FileAlreadyExistsException var9) {
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }

      File file = new File(getInstance().getDataFolder(), fileName);

      try {
         try {
            Files.copy(file.toPath(), Paths.get(getInstance().getDataFolder() + "/backups/", fileName.substring(0, fileName.indexOf(".")) + " " + dtf.format(now) + ".yml"));
         } catch (FileAlreadyExistsException var7) {
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public ScheduledTask runTask(Runnable run) {
      return this.scheduler.runTask(this, run);
   }

   public ScheduledTask runTaskLater(Runnable run, long delay) {
      return this.scheduler.runTaskLater(this, run, delay);
   }

   public ScheduledTask runTaskLaterAsync(Runnable run, long delay) {
      return this.scheduler.runTaskLaterAsync(this, run, delay);
   }

   public ScheduledTask runTaskAsyncTimer(Runnable run, long delay, long period) {
      return this.scheduler.runTaskAsyncTimer(this, run, delay, period);
   }

   public ScheduledTask runTaskAsync(Runnable run) {
      return this.scheduler.runTaskAsync(this, run);
   }

   public void runKillableTask(Runnable run) {
      this.scheduler.runKillableTask(run);
   }

   public void runKillableTaskLater(Runnable run, long delay) {
      this.scheduler.runKillableTaskLater(run, delay);
   }

   public String formatNullablePrice(String ecoType, double amount) {
      EcoType type = EconomyType.getFromString(ecoType);
      if (type == null) {
         return null;
      } else {
         EconomyProvider provider = this.ecoHandler.getEcon(type);
         return provider == null ? null : provider.formatPrice(amount);
      }
   }

   public String formatPrice(EcoType type, Double amount) {
      if (type != null && !this.ecoHandler.getEcon(type).isDecimal()) {
         amount = (double)Math.round(amount);
      }

      return this.ecoHandler.getEcon(type).formatPrice(amount);
   }

   private boolean checkForGson() {
      return ServerInfo.getVersion() != ServerInfo.Version.v1_8_R1 && ServerInfo.getVersion() != ServerInfo.Version.v1_8_R2;
   }

   private boolean isSpigotServer() {
      try {
         Class.forName("org.spigotmc.SpigotConfig");
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   private void overrideSellCommand(boolean override) {
      boolean modified = false;
      String path = (new File("commands.yml")).getAbsolutePath();
      FileConfiguration config = this.loadConfiguration(new File(path), path);
      List aliases;
      if (override) {
         if (!config.contains("aliases.sell")) {
            config.set("aliases.sell", Collections.singletonList("economyshopgui:sellall $1-"));
            modified = true;
         } else if (!config.getStringList("aliases.sell").contains("economyshopgui:sellall $1-")) {
            aliases = config.getStringList("aliases.sell");
            aliases.add("economyshopgui:sellall $1-");
            config.set("aliases.sell", aliases);
            modified = true;
         }
      } else if (config.getStringList("aliases.sell").contains("economyshopgui:sellall $1-")) {
         if (config.getStringList("aliases.sell").size() == 1) {
            config.set("aliases.sell", (Object)null);
            modified = true;
         } else {
            aliases = config.getStringList("aliases.sell");
            aliases.remove("economyshopgui:sellall $1-");
            config.set("aliases.sell", aliases);
            modified = true;
         }
      }

      if (modified) {
         try {
            config.save(path);
         } catch (IOException var6) {
            SendMessage.errorMessage("Failed to save " + path);
         }

      }
   }

   private Integer getVersion() {
      String version = Bukkit.getBukkitVersion().split("-")[0];
      return StringUtils.countMatches(version, ".") == 2 ? Integer.valueOf(version.substring(0, version.length() - 2).replace(".", "")) : Integer.valueOf(version.replace(".", ""));
   }

   public String getInvTitle(String title) {
      if (this.version == 18) {
         return title.length() > 32 ? title.substring(0, 32) : title;
      } else {
         return this.version == 113 ? this.getTitle(title) : title;
      }
   }

   private String getTitle(String title) {
      return this.versionHandler.getTitle(Bukkit.createInventory((InventoryHolder)null, 9, title));
   }

   private ServerScheduler getScheduler() {
      return (ServerScheduler)(ServerInfo.isFolia() ? new FoliaScheduler(this) : new BukkitScheduler(this));
   }

   private void loadMetaUtils() {
      if (ServerInfo.supportsPaper() && ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_19_R1)) {
         this.metaUtils = new PaperMeta(this);
      } else if (ChatUtil.getAdventureUtils() != null && ServerInfo.supportsMiniMessageBukkit()) {
         this.metaUtils = new AdventureMeta(this);
      } else {
         this.metaUtils = new BukkitMeta(this);
      }

      SendMessage.infoMessage("Initializing " + (this.metaUtils instanceof PaperMeta ? "paper" : (this.metaUtils instanceof AdventureMeta ? "adventure" : "bukkit")) + " meta...");
   }

   public boolean isSimilar(ItemStack itemOne, ItemStack itemTwo) {
      return itemOne == itemTwo ? true : this.versionHandler.isSimilar(new ItemStack(itemOne), new ItemStack(itemTwo), this.ignoredNBTData);
   }

   public boolean isSimilar(ItemStack itemOne, ItemStack itemTwo, List<String> ignoredNBTData) {
      if (itemOne == itemTwo) {
         return true;
      } else {
         ignoredNBTData.addAll(this.ignoredNBTData);
         return this.versionHandler.isSimilar(new ItemStack(itemOne), new ItemStack(itemTwo), ignoredNBTData);
      }
   }

   public String getMaterialName(String mat) {
      String[] arr = mat.toLowerCase().replace("_", " ").split(" ");
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < arr.length; ++i) {
         sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
      }

      return sb.toString().trim();
   }

   public List<String> getExamplePrices() {
      return new ArrayList(Arrays.asList("-1", "24.99", "100"));
   }

   private List<String> setSupportedMatNames() {
      List<String> supportedMatNames = new ArrayList();
      XMaterial[] var2 = XMaterial.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         XMaterial mat = var2[var4];
         if (mat.isSupported()) {
            supportedMatNames.add(mat.name());
         }
      }

      return supportedMatNames;
   }

   public void closeOpenGUIS() {
      try {
         Iterator var1 = this.getServer().getOnlinePlayers().iterator();

         while(var1.hasNext()) {
            Player p = (Player)var1.next();
            if (this.getTopInventory(p).getHolder() instanceof ShopInventory) {
               p.closeInventory();
            }
         }
      } catch (Exception var3) {
         SendMessage.logDebugMessage("Failed to close shop menu(s) for currently browsing player(s)");
         var3.printStackTrace();
      }

   }

   private Inventory getTopInventory(Player p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method getOpenInv = Player.class.getMethod("getOpenInventory");
      getOpenInv.setAccessible(true);
      Object invView = getOpenInv.invoke(p);
      Method getTopInv = invView.getClass().getDeclaredMethod("getTopInventory");
      getTopInv.setAccessible(true);
      return (Inventory)getTopInv.invoke(invView);
   }
}
