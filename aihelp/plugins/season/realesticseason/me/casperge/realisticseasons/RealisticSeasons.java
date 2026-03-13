package me.casperge.realisticseasons;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import me.casperge.interfaces.CustomBiome;
import me.casperge.interfaces.GameRuleGetter;
import me.casperge.interfaces.NmsCode;
import me.casperge.realisticseasons.api.MMobs;
import me.casperge.realisticseasons.api.PAPI;
import me.casperge.realisticseasons.api.SeasonBiome;
import me.casperge.realisticseasons.api.SeasonsAPI;
import me.casperge.realisticseasons.api.landplugins.Factions;
import me.casperge.realisticseasons.api.landplugins.GriefPrevention;
import me.casperge.realisticseasons.api.landplugins.LandPlugin;
import me.casperge.realisticseasons.api.landplugins.Lands;
import me.casperge.realisticseasons.api.landplugins.WGuard;
import me.casperge.realisticseasons.api.maps.BMap;
import me.casperge.realisticseasons.api.maps.DMap;
import me.casperge.realisticseasons.api.maps.MapPlugin;
import me.casperge.realisticseasons.biome.BiomeRegister;
import me.casperge.realisticseasons.blockscanner.ChunkSupplier;
import me.casperge.realisticseasons.blockscanner.blocksaver.BlockStorage;
import me.casperge.realisticseasons.calendar.TimeManager;
import me.casperge.realisticseasons.commands.BiomeCommand;
import me.casperge.realisticseasons.commands.DumpCommand;
import me.casperge.realisticseasons.commands.RealisticSeasonsCommand;
import me.casperge.realisticseasons.commands.RealisticSeasonsTabCompl;
import me.casperge.realisticseasons.commands.SeasonCommand;
import me.casperge.realisticseasons.commands.ToggleFahrenheitCommand;
import me.casperge.realisticseasons.commands.ToggleParticleCommand;
import me.casperge.realisticseasons.commands.ToggleSeasonsCommand;
import me.casperge.realisticseasons.commands.ToggleTemperatureCommand;
import me.casperge.realisticseasons.crop.CropFileLoader;
import me.casperge.realisticseasons.crop.CropSettings;
import me.casperge.realisticseasons.data.BiomeFileLoader;
import me.casperge.realisticseasons.data.DataHandler;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.Settings;
import me.casperge.realisticseasons.data.chunksaver.ChunkDataHandler;
import me.casperge.realisticseasons.dump.DumpCreator;
import me.casperge.realisticseasons.event.ActionbarListener;
import me.casperge.realisticseasons.event.BlockEvents;
import me.casperge.realisticseasons.event.ChunkLoadManager;
import me.casperge.realisticseasons.event.EntityEvents;
import me.casperge.realisticseasons.event.WorldEvents;
import me.casperge.realisticseasons.metrics.bukkit.Metrics;
import me.casperge.realisticseasons.metrics.charts.SimplePie;
import me.casperge.realisticseasons.particle.ParticleManager;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonCycle;
import me.casperge.realisticseasons.season.SeasonManager;
import me.casperge.realisticseasons.season.SubSeason;
import me.casperge.realisticseasons.seasonevent.EventManager;
import me.casperge.realisticseasons.temperature.AnimalTemperature;
import me.casperge.realisticseasons.temperature.TemperatureManager;
import me.casperge.realisticseasons.utils.AnimalUtils;
import me.casperge.realisticseasons.utils.BiomeMappings;
import me.casperge.realisticseasons.utils.BlockUtils;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import me.casperge.realisticseasons.utils.LitterGeneration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitWorker;

public class RealisticSeasons extends JavaPlugin {
   private ChunkUtils chunkutils;
   private BlockUtils blockutils;
   private SeasonManager seasonManager;
   private DataHandler datareader;
   private PAPI papi;
   private DumpCreator dumpCreator;
   private MapPlugin mapplugin;
   private NmsCode nmsCode;
   private Settings settings;
   private AnimalUtils animalutils;
   private ActionbarListener actionbarlistener;
   private RealisticSeasonsCommand command;
   private RealisticSeasonsTabCompl tabcompleter;
   private boolean isPaper = false;
   private boolean hasLandPlugin = false;
   private List<LandPlugin> landplugins;
   private LanguageManager lman;
   private BiomeRegister breg;
   public boolean hasTerra = false;
   private SeasonCycle seasonCycle;
   private TimeManager timemanager;
   private static RealisticSeasons main;
   private TemperatureManager tempman;
   private EventManager eventman;
   private ParticleManager pman;
   private boolean dynmapOnScheduled = false;
   public boolean hasTimePauser = false;
   public static AtomicBoolean isEnabled = new AtomicBoolean(true);
   public boolean isFreshInstall;
   private HashMap<String, SeasonBiome> seasonBiomesAPI = new HashMap();
   private ViaAPI api;
   private BlockStorage blockStorage;
   private List<BukkitRunnable> activeRunnables = new ArrayList();
   private Metrics metrics;
   private boolean hasViaVersion = false;
   private ZonedDateTime loadedTime;
   private ChunkDataHandler asyncChunkHandler;
   public File configFile;
   private FileConfiguration config;
   public boolean christmasTreesEnabled = false;
   public static boolean hasReloaded = false;
   private GameRuleGetter gamerulegetter;
   private CropFileLoader cropFileLoader;
   private LitterGeneration litterGeneration;
   public boolean debugMode = false;
   private List<CustomBiome> customBiomes = new ArrayList();
   private static final Logger logger = (Logger)LogManager.getRootLogger();
   public static String fsdfdv = "137548";

   public void onLoad() {
      main = this;
      this.loadLandPlugins();
   }

   public void onEnable() {
      loadConfig0();
      this.dumpCreator = new DumpCreator(this);
      this.getCommand("seasondump").setExecutor(new DumpCommand(this));
      Version.version = Version.craftBukkitVersionFromMinecraftRelease(Bukkit.getServer().getBukkitVersion().split("-")[0]);

      try {
         Class.forName("com.viaversion.viaversion.api.Via");
         this.hasViaVersion = true;
         this.api = Via.getAPI();
      } catch (ClassNotFoundException var2) {
      }

      short var1 = 15602;
      this.metrics = new Metrics(this, var1);
      new SeasonsAPI(this);
      Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
         public void run() {
            RealisticSeasons.this.enable();
         }
      });
   }

   public boolean loadConfig() {
      boolean var1 = true;
      this.configFile = new File(main.getDataFolder(), "config.yml");
      if (!this.configFile.exists()) {
         var1 = false;

         try {
            InputStream var2 = this.getResource("config.yml");
            FileUtils.copyInputStreamToFile(var2, this.configFile);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      JavaUtils.saveDefaultConfigValues("/config.yml", "config.yml");
      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      return var1;
   }

   public FileConfiguration getRSConfig() {
      return this.config;
   }

   public GameRuleGetter getGameRuleGetter() {
      if (this.gamerulegetter == null) {
         this.gamerulegetter = Version.setupGameRuleGetter();
      }

      return this.gamerulegetter;
   }

   public void enable() {
      try {
         Class.forName("com.destroystokyo.paper.ParticleBuilder");
         this.isPaper = true;
      } catch (ClassNotFoundException var7) {
      }

      boolean var1 = this.loadConfig();
      this.settings = new Settings(this, var1);
      this.blockStorage = new BlockStorage(this);
      if (!this.getPlugin().getDescription().getVersion().matches(".*[a-zA-Z]+.*")) {
         this.lman = new LanguageManager(this);
         this.nmsCode = Version.setupBiomeRegister();
         if (this.nmsCode == null) {
            this.getLogger().severe("Failed to load RealisticSeasons!");
            this.getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
         }

         this.tempman = new TemperatureManager(this);
         this.breg = new BiomeRegister();
         new BiomeFileLoader(this);
         this.seasonManager = new SeasonManager(this);
         if (this.getServer().getPluginManager().isPluginEnabled("dynmap")) {
            this.mapplugin = new DMap(this);
         } else if (this.getServer().getPluginManager().isPluginEnabled("BlueMap")) {
            this.mapplugin = new BMap(this);
         }

         if (this.getServer().getPluginManager().isPluginEnabled("Terra")) {
            this.hasTerra = true;
         }

         this.chunkutils = new ChunkUtils(this);
         this.blockutils = new BlockUtils(this);
         this.animalutils = new AnimalUtils(this);
         this.timemanager = new TimeManager(this);
         this.seasonCycle = new SeasonCycle(this);
         this.litterGeneration = new LitterGeneration(this);
         this.datareader = new DataHandler(this);
         this.eventman = new EventManager(this);
         this.getCommand("season").setExecutor(new SeasonCommand(this));
         this.command = new RealisticSeasonsCommand(this);
         this.getCommand("realisticseasons").setExecutor(this.command);
         this.getCommand("toggleseasoncolors").setExecutor(new ToggleSeasonsCommand(this));
         this.getCommand("toggletemperature").setExecutor(new ToggleTemperatureCommand(this));
         this.getCommand("toggleseasonparticles").setExecutor(new ToggleParticleCommand(this));
         this.getCommand("togglefahrenheit").setExecutor(new ToggleFahrenheitCommand(this));
         this.getCommand("rs").setExecutor(this.command);
         this.getCommand("currentbiome").setExecutor(new BiomeCommand(this));
         this.tabcompleter = new RealisticSeasonsTabCompl(this);
         this.getCommand("realisticseasons").setTabCompleter(this.tabcompleter);
         this.getCommand("rs").setTabCompleter(this.tabcompleter);

         try {
            Version.setupChunkPacketEvent(this);
         } catch (NoClassDefFoundError var8) {
            if (var8.getMessage() != null && var8.getMessage().contains("comphenix")) {
               EntityEvents.plissue = true;
               Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                  public void run() {
                     Bukkit.getLogger().severe("[RealisticSeasons] An error occurred while loading ProtocolLib. Please make sure you've installed the latest dev build of ProtocolLib found here: https://ci.dmulloy2.net/job/ProtocolLib/");
                  }
               }, 1L);
            }
         }

         new ChunkLoadManager(this);
         this.seasonManager.setup();
         new BlockEvents(this);
         new EntityEvents(this);
         new WorldEvents(this);
         this.seasonCycle.runTaskTimer(this, 200L, 200L);
         this.loadPAPI();
         this.tempman.load();
         this.cropFileLoader = new CropFileLoader(this);
         ChunkSupplier var2 = new ChunkSupplier(this);
         this.actionbarlistener = new ActionbarListener(this);
         if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            new MMobs(this);
         }

         var2.runTaskTimer(this, 100L, 20L);
         this.pman = new ParticleManager(this);
         this.asyncChunkHandler = new ChunkDataHandler(main);
         AnimalTemperature var3 = new AnimalTemperature(main);
         var3.runTaskTimer(main, 0L, 0L);
         Bukkit.getLogger().info("[RealisticSeasons] Total biomes registered on server: " + String.valueOf(this.nmsCode.getAllBiomes().size()));
         Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
            public void run() {
               List var1 = RealisticSeasons.this.getActiveAsyncTasks();
               if (var1.size() > 200) {
                  HashMap var2 = new HashMap();
                  Iterator var3 = var1.iterator();

                  String var4;
                  while(var3.hasNext()) {
                     var4 = (String)var3.next();
                     boolean var5 = false;
                     Iterator var6 = var2.keySet().iterator();

                     while(var6.hasNext()) {
                        String var7 = (String)var6.next();
                        if (var4.equals(var7)) {
                           var2.put(var7, (Integer)var2.get(var7) + 1);
                           var5 = true;
                        }
                     }

                     if (!var5) {
                        var2.put(var4, 1);
                     }
                  }

                  Bukkit.getLogger().severe("[RealisticSeasons] WARNING: over " + String.valueOf(var1.size()) + " async tasks created by RealisticSeasons are active. Please report this message and the contents below to the developer.");
                  Bukkit.getLogger().severe("[RealisticSeasons] Active tasks: ");
                  var3 = var2.keySet().iterator();

                  while(var3.hasNext()) {
                     var4 = (String)var3.next();
                     Bukkit.getLogger().severe("[RealisticSeasons] " + var4 + ": " + var2.get(var4) + " instances");
                  }
               }

            }
         }, 0L, 600L);
         Callable var4 = new Callable<String>() {
            public String call() {
               Boolean var1 = RealisticSeasons.main.getTemperatureManager().getTempData().isEnabled();
               return var1.toString();
            }
         };
         this.metrics.addCustomChart(new SimplePie("temperature_system_enabled", var4));
         Callable var5 = new Callable<String>() {
            public String call() {
               if (!RealisticSeasons.main.getSettings().calendarEnabled) {
                  return "false";
               } else {
                  Boolean var1 = RealisticSeasons.this.settings.syncWorldTimeWithRealWorld;
                  return var1.toString();
               }
            }
         };
         this.metrics.addCustomChart(new SimplePie("time_synchronized_with_real_time", var5));
         Callable var6 = new Callable<String>() {
            public String call() {
               Boolean var1 = RealisticSeasons.this.getNMSUtils().getCustomBiomes(RealisticSeasons.this.settings.biomeDisplayName).size() > 0;
               return var1.toString();
            }
         };
         this.metrics.addCustomChart(new SimplePie("custom_world_generator", var6));
         this.seasonManager.checkWorlds();
      }
   }

   public void onDisable() {
      isEnabled.set(false);
      if (this.dynmapOnScheduled) {
         Iterator var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            World var2 = (World)var1.next();
            this.mapplugin.setFullRenderPause(false, var2);
         }
      }

      this.datareader.save();
   }

   public boolean supportsCustomBiomes(Player var1) {
      if (!this.hasViaVersion) {
         return true;
      } else {
         return this.api.getPlayerVersion(var1) >= 751;
      }
   }

   public void reload() {
      this.settings.reload();
      Iterator var1 = this.seasonManager.worldData.keySet().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         if (!this.seasonManager.getSeason(var2).equals(Season.DISABLED)) {
            this.seasonManager.clearChunkCheckedList(var2, this.seasonManager.getSeason(var2));
            this.seasonManager.clearChunkQueue(var2, this.seasonManager.getSeason(var2));
         }
      }

      this.lman.reload();
      this.tempman.getTempData().load();
      this.timemanager.load();
      this.eventman.load();
   }

   public static RealisticSeasons getInstance() {
      return main;
   }

   public ParticleManager getParticleManager() {
      return this.pman;
   }

   public void addRunnable(BukkitRunnable var1) {
      this.activeRunnables.add(var1);
   }

   public ChunkUtils getChunkUtils() {
      return this.chunkutils;
   }

   public BlockUtils getBlockUtils() {
      return this.blockutils;
   }

   public SeasonManager getSeasonManager() {
      return this.seasonManager;
   }

   public SeasonCycle getSeasonCycle() {
      return this.seasonCycle;
   }

   public DataHandler getDataReader() {
      return this.datareader;
   }

   public TemperatureManager getTemperatureManager() {
      return this.tempman;
   }

   public LanguageManager getLangManager() {
      return this.lman;
   }

   public NmsCode getNMSUtils() {
      return this.nmsCode;
   }

   public EventManager getEventManager() {
      return this.eventman;
   }

   public TimeManager getTimeManager() {
      return this.timemanager;
   }

   public Settings getSettings() {
      return this.settings;
   }

   public BiomeRegister getBiomeRegister() {
      return this.breg;
   }

   public AnimalUtils getAnimalUtils() {
      return this.animalutils;
   }

   public BlockStorage getBlockStorage() {
      return this.blockStorage;
   }

   public void createDump(UUID var1, String var2) {
      this.dumpCreator.createDump(var1, var2);
   }

   public void createConsoleDump(String var1) {
      this.dumpCreator.createConsoleDump(var1);
   }

   public JavaPlugin getPlugin() {
      return this;
   }

   public boolean isPaper() {
      return this.isPaper;
   }

   public void loadPAPI() {
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         this.papi = new PAPI(this);
         this.papi.register();
      }

   }

   public String setPlaceHolders(String var1, Player var2) {
      return this.papi != null ? this.papi.setPlaceHolders(var2, var1) : var1;
   }

   public void updatemapplugins(Season var1, SubSeason var2, World var3) {
      if (this.mapplugin != null) {
         if (main.getSettings().pauseMapRenderInWinter) {
            if (var1 == Season.WINTER) {
               if (!this.mapplugin.isFullRenderPause(var3)) {
                  this.mapplugin.setFullRenderPause(true, var3);
               }
            } else if (var1 == Season.SPRING) {
               if (var2.getPhase() >= 2) {
                  if (this.mapplugin.isFullRenderPause(var3)) {
                     this.mapplugin.setFullRenderPause(false, var3);
                  }
               } else if (!this.mapplugin.isFullRenderPause(var3)) {
                  this.mapplugin.setFullRenderPause(true, var3);
               }
            } else if (this.mapplugin.isFullRenderPause(var3)) {
               this.mapplugin.setFullRenderPause(false, var3);
            }

         }
      }
   }

   public void loadLandPlugins() {
      this.landplugins = new ArrayList();
      if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
         if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            WGuard var1 = new WGuard(this);
            this.hasLandPlugin = true;
            this.landplugins.add(var1);
         } else {
            Bukkit.getLogger().info("You'll need WorldEdit installed on your server to use WorldGuard with RealisticSeasons!");
         }
      }

      if (Bukkit.getPluginManager().getPlugin("Lands") != null) {
         Lands var2 = new Lands(this);
         this.hasLandPlugin = true;
         this.landplugins.add(var2);
      }

      if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
         GriefPrevention var3 = new GriefPrevention(this);
         this.hasLandPlugin = true;
         this.landplugins.add(var3);
      }

      if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
         Factions var4 = new Factions(this);
         this.hasLandPlugin = true;
         this.landplugins.add(var4);
      }

   }

   public boolean hasLandPlugin() {
      return this.hasLandPlugin;
   }

   public List<LandPlugin> getLandPluginAPIs() {
      return this.landplugins;
   }

   public boolean hasSeasons(int var1, int var2, World var3) {
      if (!this.hasLandPlugin) {
         return true;
      } else {
         boolean var4 = true;
         Iterator var5 = this.landplugins.iterator();

         while(var5.hasNext()) {
            LandPlugin var6 = (LandPlugin)var5.next();
            if (!var6.hasSeasonEffects(var1, var2, var3)) {
               var4 = false;
            }
         }

         return var4;
      }
   }

   public boolean hasBlockChanges(int var1, int var2, World var3) {
      if (!this.settings.modifyBlocks) {
         return false;
      } else if (!this.hasLandPlugin) {
         return true;
      } else {
         boolean var4 = true;
         Iterator var5 = this.landplugins.iterator();

         while(var5.hasNext()) {
            LandPlugin var6 = (LandPlugin)var5.next();
            if (!var6.hasBlockChanges(var1, var2, var3)) {
               var4 = false;
            }
         }

         return var4;
      }
   }

   public boolean hasMobSpawns(int var1, int var2, World var3) {
      if (!this.hasLandPlugin) {
         return true;
      } else {
         boolean var4 = true;
         Iterator var5 = this.landplugins.iterator();

         while(var5.hasNext()) {
            LandPlugin var6 = (LandPlugin)var5.next();
            if (!var6.hasMobSpawns(var1, var2, var3)) {
               var4 = false;
            }
         }

         return var4;
      }
   }

   public int createSeasonsBiome(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9) {
      if (this.settings.optimizedBiomes) {
         var2 = String.valueOf(this.customBiomes.size());
      }

      CustomBiome var10 = createCustomBiome(var2.toLowerCase(), this.settings.biomeDisplayName, var1);
      if (!var3.equals("")) {
         var10.setSkyColor(var3);
      }

      if (!var4.equals("")) {
         var10.setWaterColor(var4);
      }

      if (!var5.equals("")) {
         var10.setWaterFogColor(var5);
      }

      if (!var6.equals("")) {
         var10.setGrassColor(var6);
      } else {
         String var11 = BiomeMappings.getGrassHex(var1);
         if (!var11.equals("CUSTOM")) {
            var10.setGrassColor(BiomeMappings.getGrassHex(var1));
         } else {
            var10.setGrassColor(var11);
         }
      }

      if (!var7.equals("")) {
         var10.setFoliageColor(var7);
      }

      if (!var8.equals("")) {
         var10.setFogColor(var8);
      }

      if (var9) {
         var10.setFrozen(true);
         var10.setTemperature(0.0F);
      }

      if (this.settings.optimizedBiomes) {
         Iterator var13 = this.customBiomes.iterator();

         while(var13.hasNext()) {
            CustomBiome var12 = (CustomBiome)var13.next();
            if (var12.getGrassColor().equalsIgnoreCase(var10.getGrassColor()) && var12.getSkyColor().equalsIgnoreCase(var10.getSkyColor()) && var12.getWaterColor().equalsIgnoreCase(var10.getWaterColor()) && var12.getWaterFogColor().equalsIgnoreCase(var10.getWaterFogColor()) && var12.getFoliageColor().equalsIgnoreCase(var10.getFoliageColor()) && var12.getFogColor().equalsIgnoreCase(var10.getFogColor()) && var12.isFrozen() == var10.isFrozen() && var12.getGrassType() == var10.getGrassType()) {
               return var12.getBiomeID();
            }
         }
      }

      var10.register();
      this.customBiomes.add(var10);
      return var10.getBiomeID();
   }

   public HashMap<String, SeasonBiome> getSeasonBiomesForAPI() {
      return this.seasonBiomesAPI;
   }

   public void addSeasonBiomeForAPI(String var1, SeasonBiome var2) {
      this.seasonBiomesAPI.put(var1, var2);
   }

   public ZonedDateTime getLoadedTime() {
      return this.loadedTime;
   }

   public void setLoadedTime(ZonedDateTime var1) {
      this.loadedTime = var1;
   }

   public ChunkDataHandler getAsyncChunkHandler() {
      return this.asyncChunkHandler;
   }

   public static CustomBiome createCustomBiome(String var0, String var1, String var2) {
      return Version.createCustomBiome(var0, var1, var2);
   }

   public List<String> getActiveAsyncTasks() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = Bukkit.getScheduler().getActiveWorkers().iterator();

      while(var2.hasNext()) {
         BukkitWorker var3 = (BukkitWorker)var2.next();
         if (var3.getOwner().equals(this)) {
            var1.add(var3.getThread().getName());
         }
      }

      return var1;
   }

   public ActionbarListener getActionbarListener() {
      return this.actionbarlistener;
   }

   public CropSettings getCropSettings() {
      return this.cropFileLoader.getCropSettings();
   }

   public LitterGeneration getLitterGeneration() {
      return this.litterGeneration;
   }
}
