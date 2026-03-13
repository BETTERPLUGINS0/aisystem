package tntrun;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.arena.Arena;
import tntrun.arena.handlers.BungeeHandler;
import tntrun.arena.handlers.SoundHandler;
import tntrun.arena.handlers.VaultHandler;
import tntrun.bukkit.Metrics;
import tntrun.charts.SimplePie;
import tntrun.commands.AutoTabCompleter;
import tntrun.commands.ConsoleCommands;
import tntrun.commands.GameCommands;
import tntrun.commands.setup.SetupCommandsHandler;
import tntrun.commands.setup.SetupTabCompleter;
import tntrun.datahandler.ArenasManager;
import tntrun.datahandler.PlayerDataStore;
import tntrun.datahandler.ScoreboardManager;
import tntrun.eventhandler.HeadsPlusHandler;
import tntrun.eventhandler.MenuHandler;
import tntrun.eventhandler.PlayerLeaveArenaChecker;
import tntrun.eventhandler.PlayerStatusHandler;
import tntrun.eventhandler.RestrictionHandler;
import tntrun.eventhandler.ShopHandler;
import tntrun.kits.Kits;
import tntrun.lobby.GlobalLobby;
import tntrun.menu.Menus;
import tntrun.messages.Language;
import tntrun.messages.Messages;
import tntrun.parties.Parties;
import tntrun.signs.SignHandler;
import tntrun.signs.editor.SignEditor;
import tntrun.utils.Bars;
import tntrun.utils.Shop;
import tntrun.utils.Sounds;
import tntrun.utils.Stats;
import tntrun.utils.TitleMsg;
import tntrun.utils.Utils;

public class TNTRun extends JavaPlugin {
   private Logger log;
   private boolean mcMMO = false;
   private boolean headsplus = false;
   private boolean usestats = false;
   private boolean needupdate = false;
   private boolean placeholderapi = false;
   private boolean adpParties = false;
   private boolean file = false;
   private VaultHandler vaultHandler;
   private BungeeHandler bungeeHandler;
   private GlobalLobby globallobby;
   private Menus menus;
   private PlayerDataStore pdata;
   private SignEditor signEditor;
   private Kits kitmanager;
   private Sounds sound;
   private Language language;
   private Parties parties;
   private Stats stats;
   private MySQL mysql;
   private Shop shop;
   public ArenasManager amanager;
   private ScoreboardManager scoreboardManager;
   private static TNTRun instance;
   private String version;
   private String latestRelease;
   private static final int SPIGOT_ID = 53359;
   private static final int BSTATS_PLUGIN_ID = 2192;
   private static final String SPIGOT_URL = "https://www.spigotmc.org/resources/TNTRun_reloaded.53359/";

   public void onEnable() {
      instance = this;
      this.log = this.getLogger();
      this.saveDefaultConfig();
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      this.signEditor = new SignEditor(this);
      this.globallobby = new GlobalLobby(this);
      this.kitmanager = new Kits();
      this.language = new Language(this);
      Messages.loadMessages(this);
      Bars.loadBars(this);
      TitleMsg.loadTitles(this);
      this.pdata = new PlayerDataStore(this);
      this.amanager = new ArenasManager();
      this.menus = new Menus(this);
      this.parties = new Parties(this);
      this.version = this.getDescription().getVersion();
      this.setupPlugin();
      this.setupScoreboards();
      this.loadArenas();
      this.checkUpdate();
      this.sound = new SoundHandler(this);
      if (this.isBungeecord()) {
         this.log.info("Bungeecord is enabled");
         this.bungeeHandler = new BungeeHandler(this);
      }

      if (this.getConfig().getBoolean("special.Metrics", true)) {
         this.log.info("Attempting to start metrics (bStats)...");
         Metrics metrics = new Metrics(this, 2192);
         metrics.addCustomChart(new SimplePie("server_software", () -> {
            return this.getServer().getName() == "CraftBukkit" ? "Spigot" : this.getServer().getName();
         }));
      }

      this.setStorage();
      if (this.usestats) {
         this.stats = new Stats(this);
      }

   }

   public static TNTRun getInstance() {
      return instance;
   }

   public void onDisable() {
      if (!this.file) {
         this.mysql.close();
      }

      this.saveArenas();
      this.globallobby.saveToConfig();
      this.globallobby = null;
      this.kitmanager.saveToConfig();
      this.kitmanager = null;
      this.signEditor.saveConfiguration();
      this.signEditor = null;
      this.amanager = null;
      this.pdata = null;
      this.stats = null;
      this.log = null;
   }

   private void saveArenas() {
      Iterator var2 = this.amanager.getArenas().iterator();

      while(var2.hasNext()) {
         Arena arena = (Arena)var2.next();
         arena.getStructureManager().getGameZone().regenNow();
         arena.getStatusManager().disableArena();
         arena.getStructureManager().saveToConfig();
         Bars.removeAll(arena.getArenaName());
      }

   }

   public void logSevere(String message) {
      this.log.severe(message);
   }

   public boolean isHeadsPlus() {
      return this.headsplus;
   }

   public boolean isMCMMO() {
      return this.mcMMO;
   }

   public boolean isPlaceholderAPI() {
      return this.placeholderapi;
   }

   public boolean isParties() {
      return this.getConfig().getBoolean("parties.enabled");
   }

   public boolean isAdpParties() {
      return this.adpParties && this.getConfig().getBoolean("parties.usePartiesPlugin") && !this.isParties();
   }

   public boolean useStats() {
      return this.usestats;
   }

   public void setUseStats(boolean usestats) {
      this.usestats = usestats;
   }

   public boolean needUpdate() {
      return this.needupdate;
   }

   public void setNeedUpdate(boolean needupdate) {
      this.needupdate = needupdate;
   }

   public boolean isFile() {
      return this.file;
   }

   public boolean useUuid() {
      return Bukkit.getOnlineMode() || this.isBungeecord() && this.getConfig().getBoolean("bungeecord.useUUID");
   }

   public boolean isBungeecord() {
      return this.getConfig().getBoolean("bungeecord.enabled");
   }

   private void checkUpdate() {
      if (this.getConfig().getBoolean("special.CheckForNewVersion", true)) {
         (new VersionChecker(this, 53359)).getVersion((latestVersion) -> {
            this.latestRelease = latestVersion;
            if (this.version.equals(latestVersion)) {
               this.log.info("You are running the most recent version");
               this.setNeedUpdate(false);
            } else if (!this.version.contains("beta") && !this.version.toLowerCase().contains("snapshot")) {
               if (Character.isDigit(latestVersion.charAt(0))) {
                  this.log.info("Current version: " + this.version);
                  this.log.info("Latest release: " + latestVersion);
                  this.log.info("Latest release available from Spigot: https://www.spigotmc.org/resources/TNTRun_reloaded.53359/");
                  this.setNeedUpdate(true);
                  Iterator var3 = Bukkit.getOnlinePlayers().iterator();

                  while(var3.hasNext()) {
                     Player p = (Player)var3.next();
                     Utils.displayUpdate(p);
                  }
               }
            } else {
               this.log.info("You are running dev build: " + this.version);
               this.log.info("Latest release: " + latestVersion);
               this.setNeedUpdate(false);
            }

         });
      }
   }

   private void connectToMySQL() {
      this.log.info("Connecting to MySQL database...");
      this.mysql = new MySQL(this.getConfig().getString("MySQL.host"), this.getConfig().getInt("MySQL.port"), this.getConfig().getString("MySQL.name"), this.getConfig().getString("MySQL.user"), this.getConfig().getString("MySQL.pass"), this.getConfig().getString("MySQL.useSSL"), this.getConfig().getString("MySQL.flags"), this.getConfig().getBoolean("MySQL.legacyDriver"), this);
      (new BukkitRunnable() {
         public void run() {
            MySQL var10000 = TNTRun.this.mysql;
            FileConfiguration var10001 = TNTRun.this.getConfig();
            var10000.query("CREATE TABLE IF NOT EXISTS `" + var10001.getString("MySQL.table") + "` ( `username` varchar(50) NOT NULL, `streak` int(16) NOT NULL, `wins` int(16) NOT NULL, `played` int(16) NOT NULL, UNIQUE KEY `username` (`username`) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            var10000 = TNTRun.this.mysql;
            var10001 = TNTRun.this.getConfig();
            var10000.query("ALTER TABLE `" + var10001.getString("MySQL.table") + "` RENAME COLUMN `looses` TO `streak`");
            TNTRun.this.log.info("Connected to MySQL database!");
         }
      }).runTaskAsynchronously(this);
   }

   private void setupPlugin() {
      this.getCommand("tntrun").setExecutor(new GameCommands(this));
      this.getCommand("tntrunsetup").setExecutor(new SetupCommandsHandler(this));
      this.getCommand("tntrunconsole").setExecutor(new ConsoleCommands(this));
      this.getCommand("tntrun").setTabCompleter(new AutoTabCompleter());
      this.getCommand("tntrunsetup").setTabCompleter(new SetupTabCompleter());
      PluginManager pm = this.getServer().getPluginManager();
      pm.registerEvents(new PlayerStatusHandler(this), this);
      pm.registerEvents(new RestrictionHandler(this), this);
      pm.registerEvents(new PlayerLeaveArenaChecker(this), this);
      pm.registerEvents(new SignHandler(this), this);
      pm.registerEvents(new MenuHandler(this), this);
      pm.registerEvents(new ShopHandler(this), this);
      this.setupShop();
      Plugin HeadsPlus = pm.getPlugin("HeadsPlus");
      if (HeadsPlus != null && HeadsPlus.isEnabled()) {
         pm.registerEvents(new HeadsPlusHandler(this), this);
         this.headsplus = true;
         this.log.info("Successfully linked with HeadsPlus, version " + HeadsPlus.getDescription().getVersion());
      }

      Plugin MCMMO = pm.getPlugin("mcMMO");
      if (MCMMO != null && MCMMO.isEnabled()) {
         this.mcMMO = true;
         this.log.info("Successfully linked with mcMMO, version " + MCMMO.getDescription().getVersion());
      }

      Plugin PlaceholderAPI = pm.getPlugin("PlaceholderAPI");
      if (PlaceholderAPI != null && PlaceholderAPI.isEnabled()) {
         this.placeholderapi = true;
         this.log.info("Successfully linked with PlaceholderAPI, version " + PlaceholderAPI.getDescription().getVersion());
         (new TNTRunPlaceholders(this)).register();
      }

      Plugin Parties = this.getServer().getPluginManager().getPlugin("Parties");
      if (Parties != null && Parties.isEnabled()) {
         this.adpParties = true;
         this.log.info("Successfully linked with Parties, version " + Parties.getDescription().getVersion());
      }

      this.vaultHandler = new VaultHandler(this);
   }

   public void setupShop() {
      if (this.isGlobalShop()) {
         this.shop = new Shop(this);
      }

   }

   public boolean isGlobalShop() {
      return this.getConfig().getBoolean("shop.enabled");
   }

   public VaultHandler getVaultHandler() {
      return this.vaultHandler;
   }

   public BungeeHandler getBungeeHandler() {
      return this.bungeeHandler;
   }

   public Parties getParties() {
      return this.parties;
   }

   private void loadArenas() {
      String var10002 = String.valueOf(this.getDataFolder());
      final File arenasfolder = new File(var10002 + File.separator + "arenas");
      arenasfolder.mkdirs();
      (new BukkitRunnable() {
         public void run() {
            TNTRun.this.globallobby.loadFromConfig();
            TNTRun.this.kitmanager.loadFromConfig();
            List<String> arenaList = Arrays.asList(arenasfolder.list());
            Iterator var3 = arenaList.iterator();

            while(var3.hasNext()) {
               String file = (String)var3.next();
               Arena arena = new Arena(file.substring(0, file.length() - 4), TNTRun.instance);
               arena.getStructureManager().loadFromConfig();
               TNTRun.this.amanager.registerArena(arena);
               Bars.createBar(arena.getArenaName());
               if (arena.getStructureManager().isEnableOnRestart()) {
                  arena.getStatusManager().enableArena();
               }
            }

            if (TNTRun.this.isBungeecord()) {
               TNTRun.this.amanager.setBungeeArena();
            }

            TNTRun.this.signEditor.loadConfiguration();
         }
      }).runTaskLater(this, 20L);
   }

   private void setStorage() {
      label33: {
         String storage = this.getConfig().getString("database");
         switch(storage.hashCode()) {
         case 114126:
            if (storage.equals("sql")) {
               break label33;
            }
            break;
         case 3143036:
            if (storage.equals("file")) {
               this.usestats = true;
               this.file = true;
               return;
            }
            break;
         case 104382626:
            if (storage.equals("mysql")) {
               break label33;
            }
         }

         this.log.info("The database " + storage + " is not supported, supported database types: sql, mysql, file");
         this.usestats = false;
         this.file = false;
         this.log.info("Disabling stats...");
         return;
      }

      this.connectToMySQL();
      this.usestats = true;
      this.file = false;
   }

   public Menus getMenus() {
      return this.menus;
   }

   public PlayerDataStore getPData() {
      return this.pdata;
   }

   public Stats getStats() {
      return this.stats;
   }

   public Kits getKitManager() {
      return this.kitmanager;
   }

   public GlobalLobby getGlobalLobby() {
      return this.globallobby;
   }

   public Sounds getSound() {
      return this.sound;
   }

   public Language getLanguage() {
      return this.language;
   }

   public SignEditor getSignEditor() {
      return this.signEditor;
   }

   public ScoreboardManager getScoreboardManager() {
      return this.scoreboardManager;
   }

   public MySQL getMysql() {
      return this.mysql;
   }

   public Shop getShop() {
      return this.shop;
   }

   public String getLatestRelease() {
      return this.latestRelease;
   }

   public String getSpigotURL() {
      return "https://www.spigotmc.org/resources/TNTRun_reloaded.53359/";
   }

   public void setupScoreboards() {
      if (this.getConfig().getBoolean("special.UseScoreboard")) {
         this.scoreboardManager = new ScoreboardManager(this);
      }

   }
}
