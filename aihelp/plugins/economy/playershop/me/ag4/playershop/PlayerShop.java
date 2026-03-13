package me.ag4.playershop;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import me.ag4.playershop.api.DataUtils;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.commands.Reload;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.files.PlayersFolder;
import me.ag4.playershop.hooks.HookManager;
import me.ag4.playershop.hooks.PlaceholderAPI;
import me.ag4.playershop.hooks.WorldGuardAPI;
import me.ag4.playershop.listener.BreakEvent;
import me.ag4.playershop.listener.ExplodeEvent;
import me.ag4.playershop.listener.InteractEvent;
import me.ag4.playershop.listener.JoinEvent;
import me.ag4.playershop.listener.PlaceEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerShop extends JavaPlugin {
   private static PlayerShop plugin;
   private static FileConfiguration config;
   private static Logger log;
   private static Economy econ;
   private static HookManager hookManager;

   public void onEnable() {
      log = this.getLogger();
      this.saveDefaultConfig();
      config = this.getConfig();
      config.options().copyDefaults(true);
      PlayersFolder.setup();
      ((PluginCommand)Objects.requireNonNull(this.getCommand("playershop"))).setExecutor(new Reload());
      PluginManager pluginManager = Bukkit.getPluginManager();
      pluginManager.registerEvents(new BreakEvent(), this);
      pluginManager.registerEvents(new PlaceEvent(), this);
      pluginManager.registerEvents(new ExplodeEvent(), this);
      pluginManager.registerEvents(new InteractEvent(), this);
      pluginManager.registerEvents(new JoinEvent(), this);
      if (!this.setupEconomy()) {
         Bukkit.getConsoleSender().sendMessage("No Economy Found / Disable Vault");
      }

      this.setupPlaceholderAPI();
      if (this.getConfig().getBoolean("Craft.Enable")) {
         this.playerShopCrafting();
      }

      this.updateConfigFile();
      this.loadLang();
      this.enableMsg();
      DataUtils.restoreDataFile();
      DataUtils.runShopsChecker();
      DataUtils.autoSave(5);
      PlayerShopAPI.broadCastPurchaseSetUp();
   }

   public void onLoad() {
      plugin = this;
      hookManager = new HookManager();
      config = this.getConfig();
      if (this.getHookManager().isWorldGuardEnabled()) {
         WorldGuardAPI.setupWorldGuardFlags();
      }

   }

   public void onDisable() {
      this.disableMsg();
      DataUtils.saveDataFile();
   }

   public static PlayerShop getInstance() {
      return plugin;
   }

   public FileConfiguration config() {
      return config;
   }

   public Logger getLog() {
      return log;
   }

   public Economy getEcon() {
      return econ;
   }

   public HookManager getHookManager() {
      return hookManager;
   }

   private boolean setupEconomy() {
      if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
         return false;
      } else {
         RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
         if (rsp == null) {
            return false;
         } else {
            econ = (Economy)rsp.getProvider();
            return true;
         }
      }
   }

   private void setupPlaceholderAPI() {
      if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new PlaceholderAPI()).register();
      }

   }

   public void playerShopCrafting() {
      ItemStack items = new ItemStack(Material.CHEST);
      ItemMeta meta1 = items.getItemMeta();

      assert meta1 != null;

      meta1.setDisplayName(Utils.hex(((PlayerShop)getPlugin(PlayerShop.class)).getConfig().getString("Craft.Name")));
      items.setItemMeta(meta1);
      NamespacedKey key = new NamespacedKey(getPlugin(PlayerShop.class), "player_shop");
      ShapedRecipe shop = new ShapedRecipe(key, items);
      shop.shape(new String[]{"***", "*B*", "***"});
      String itemToCraft = ((PlayerShop)getPlugin(PlayerShop.class)).getConfig().getString("Craft.Items-Craft");
      if (itemToCraft != null) {
         Material materialToCraft = Material.getMaterial(itemToCraft);
         if (materialToCraft != null) {
            shop.setIngredient('*', materialToCraft);
            shop.setIngredient('B', Material.CHEST);
            if (Bukkit.getRecipe(shop.getKey()) == null) {
               Bukkit.getServer().addRecipe(shop);
            }

         } else {
            ((PlayerShop)getPlugin(PlayerShop.class)).getLogger().severe("Invalid material name: " + itemToCraft);
         }
      } else {
         ((PlayerShop)getPlugin(PlayerShop.class)).getLogger().severe("Missing item to craft in config file");
      }
   }

   private void loadLang() {
      File lang = new File(this.getDataFolder(), "lang.yml");
      YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
      if (!lang.exists()) {
         try {
            langConfig.save(lang);
         } catch (IOException var8) {
            var8.printStackTrace();
            log.severe("[PlayerShop] Couldn't create language file.");
            this.setEnabled(false);
         }
      }

      Lang[] var3 = Lang.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Lang item = var3[var5];
         if (langConfig.getString(item.getPath()) == null) {
            langConfig.set(item.getPath(), item.getDefault());
         }
      }

      Set<String> validKeys = (Set)Arrays.stream(Lang.values()).map(Lang::getPath).collect(Collectors.toSet());
      Set<String> yamlKeys = langConfig.getKeys(false);
      Iterator var11 = yamlKeys.iterator();

      while(var11.hasNext()) {
         String key = (String)var11.next();
         if (!validKeys.contains(key)) {
            langConfig.set(key, (Object)null);
         }
      }

      Lang.setFile(langConfig);

      try {
         langConfig.save(lang);
      } catch (IOException var7) {
         log.info("PluginName: Failed to save lang.yml.");
         log.info("PluginName: Report this stack trace to <your name>.");
         var7.printStackTrace();
      }

   }

   private void updateConfigFile() {
      File configFile = new File(this.getDataFolder(), "config.yml");
      FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);
      Map<String, Object> existingValues = new HashMap();
      Iterator var4 = currentConfig.getKeys(true).iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();
         existingValues.put(key, currentConfig.get(key));
      }

      this.saveResource("config.yml", true);
      this.reloadConfig();
      FileConfiguration updatedConfig = this.getConfig();
      Iterator var8 = existingValues.entrySet().iterator();

      while(var8.hasNext()) {
         Entry<String, Object> entry = (Entry)var8.next();
         updatedConfig.set((String)entry.getKey(), entry.getValue());
      }

      this.saveConfig();
   }

   public void reloadConfig() {
      super.reloadConfig();
      config = this.getConfig();
   }

   public void reloadFiles() {
      File configFile = new File(this.getDataFolder(), "config.yml");
      config = YamlConfiguration.loadConfiguration(configFile);
   }

   private void enableMsg() {
      Bukkit.getConsoleSender().sendMessage(Utils.hex("&8========================================================"));
      Bukkit.getConsoleSender().sendMessage(Utils.hex("                   &8| &6&l" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getName() + ": &a&lEnable &8|"));
      ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
      String var10001 = (String)((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getAuthors().get(0);
      var10000.sendMessage(Utils.hex("&eCreator: &b" + var10001 + " &8| &eVersion: &b" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getVersion() + " &8| &eWebsite: &bhttps://" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getWebsite()));
      Bukkit.getConsoleSender().sendMessage(Utils.hex("&8========================================================"));
   }

   private void disableMsg() {
      Bukkit.getConsoleSender().sendMessage(Utils.hex("&8========================================================"));
      Bukkit.getConsoleSender().sendMessage(Utils.hex("                   &8| &6&l" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getName() + ": &c&lDisable &8|"));
      ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
      String var10001 = (String)((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getAuthors().get(0);
      var10000.sendMessage(Utils.hex("&eCreator: &b" + var10001 + " &8| &eVersion: &b" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getVersion() + " &8| &eWebsite: &bhttps://" + ((PlayerShop)getPlugin(PlayerShop.class)).getDescription().getWebsite()));
      Bukkit.getConsoleSender().sendMessage(Utils.hex("&8========================================================"));
   }
}
