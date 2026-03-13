package org.terraform.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.NMSInjectorAbstract;
import org.terraform.coregen.TerraformPopulator;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.reflection.Post14PrivateFieldHandler;
import org.terraform.reflection.Pre14PrivateFieldHandler;
import org.terraform.reflection.PrivateFieldHandler;
import org.terraform.schematic.SchematicListener;
import org.terraform.structure.StructureRegistry;
import org.terraform.tree.SaplingOverrider;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.bstats.TerraformGeneratorMetricsHandler;
import org.terraform.utils.datastructs.ConcurrentLRUCache;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.Version;
import org.terraform.watchdog.TfgWatchdogSuppressant;

public class TerraformGeneratorPlugin extends JavaPlugin implements Listener {
   public static final Set<String> INJECTED_WORLDS = new HashSet();
   @NotNull
   public static final PrivateFieldHandler privateFieldHandler;
   public static TLogger logger;
   @NotNull
   public static NMSInjectorAbstract injector;
   public static TfgWatchdogSuppressant watchdogSuppressant;
   private static TerraformGeneratorPlugin instance;
   private LanguageManager lang;

   public static TerraformGeneratorPlugin get() {
      return instance;
   }

   public void onEnable() {
      super.onEnable();
      instance = this;

      try {
         TConfig.init(new File(this.getDataFolder(), "config.yml"));
      } catch (IOException var5) {
         this.getLogger().severe("Failed to load config.yml: " + var5.getMessage());
         this.getPluginLoader().disablePlugin(this);
         return;
      }

      logger = new TLogger();
      this.lang = new LanguageManager(this, TConfig.c);
      GenUtils.initGenUtils();
      BlockUtils.initBlockUtils();
      HeightMap.spawnFlatRadiusSquared = TConfig.c.HEIGHT_MAP_SPAWN_FLAT_RADIUS;
      if (HeightMap.spawnFlatRadiusSquared > 0) {
         HeightMap.spawnFlatRadiusSquared *= HeightMap.spawnFlatRadiusSquared;
      }

      BiomeBank.initSinglesConfig();
      TerraformGenerator.CHUNK_CACHE = new ConcurrentLRUCache("CHUNK_CACHE", TConfig.c.DEVSTUFF_CHUNKCACHE_SIZE, (key) -> {
         return new ChunkCache(key.tw(), key.x(), key.z());
      });
      GenUtils.biomeQueryCache = new ConcurrentLRUCache("biomeQueryCache", TConfig.c.DEVSTUFF_CHUNKBIOMES_SIZE, (key) -> {
         EnumSet<BiomeBank> banks = EnumSet.noneOf(BiomeBank.class);
         int gridX = key.chunkX * 16;
         int gridZ = key.chunkZ * 16;

         for(int x = gridX; x < gridX + 16; ++x) {
            for(int z = gridZ; z < gridZ + 16; ++z) {
               BiomeBank bank = key.tw.getBiomeBank(x, z);
               if (!banks.contains(bank)) {
                  banks.add(bank);
               }
            }
         }

         return banks;
      });
      LangOpt.init(this);
      watchdogSuppressant = new TfgWatchdogSuppressant();
      new TerraformGeneratorMetricsHandler(this);
      TerraformGenerator.updateSeaLevelFromConfig();
      new TerraformCommandManager(this, new String[]{"terraform", "terra"});
      Bukkit.getPluginManager().registerEvents(this, this);
      Bukkit.getPluginManager().registerEvents(new SchematicListener(), this);
      String version = Version.VERSION.getPackName();
      logger.stdout("Detected version: " + version + ", packName: " + Version.VERSION.getPackName());

      try {
         injector = Version.getInjector();
         if (injector == null) {
            throw new ClassNotFoundException();
         }

         injector.startupTasks();
      } catch (ClassNotFoundException var3) {
         logger.stackTrace(var3);
         logger.stdout("&cNo support for this version has been made yet!");
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var4) {
         logger.stackTrace(var4);
         logger.stdout("&cSomething went wrong initiating the injector!");
      }

      if (TConfig.c.MISC_SAPLING_CUSTOM_TREES_ENABLED) {
         Bukkit.getPluginManager().registerEvents(new SaplingOverrider(), this);
      }

      StructureRegistry.init();
   }

   public void onDisable() {
   }

   /** @deprecated */
   @Deprecated
   @EventHandler
   public void onWorldLoad(@NotNull WorldLoadEvent event) {
      if (event.getWorld().getGenerator() instanceof TerraformGenerator) {
         logger.stdout(event.getWorld().getName() + " loaded.");
         if (!TerraformGenerator.preWorldInitGen.isEmpty()) {
            if (!TConfig.c.DEVSTUFF_ATTEMPT_FIXING_PREMATURE) {
               logger.stdout("&cIgnoring " + TerraformGenerator.preWorldInitGen.size() + " pre-maturely generated chunks. You may see a patch of plain land.");
               return;
            }

            logger.stdout("&6Trying to decorate " + TerraformGenerator.preWorldInitGen.size() + " pre-maturely generated chunks.");
            int fixed = 0;
            TerraformWorld tw = TerraformWorld.get(event.getWorld());
            Iterator var4 = TerraformGenerator.preWorldInitGen.iterator();

            while(var4.hasNext()) {
               SimpleChunkLocation sc = (SimpleChunkLocation)var4.next();
               if (sc.getWorld().equals(event.getWorld().getName())) {
                  logger.stdout("Populating " + String.valueOf(sc));
                  PopulatorDataPostGen data = new PopulatorDataPostGen(sc.toChunk());
                  (new TerraformPopulator()).populate(tw, data);
                  ++fixed;
               }
            }

            logger.stdout("&aSuccessfully finished fixing " + fixed + " pre-mature chunks!");
         }
      }

   }

   @EventHandler
   public void onWorldInit(@NotNull WorldInitEvent event) {
      if (event.getWorld().getGenerator() instanceof TerraformGenerator) {
         logger.stdout("Detected world: " + event.getWorld().getName() + ", commencing injection... ");
         TerraformWorld tw = TerraformWorld.forceOverrideSeed(event.getWorld());
         if (injector != null && injector.attemptInject(event.getWorld())) {
            INJECTED_WORLDS.add(event.getWorld().getName());
            tw.minY = injector.getMinY();
            tw.maxY = injector.getMaxY();
            logger.stdout("&aInjection success! Proceeding with generation.");
         } else {
            logger.stdout("&cInjection failed.");
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      if (INJECTED_WORLDS.contains(event.getWorld().getName())) {
         logger.stdout("Flushing noise cache for world " + event.getWorld().getName());
         NoiseCacheHandler.flushNoiseCaches(TerraformWorld.get(event.getWorld()));
      }

   }

   public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
      return new TerraformGenerator();
   }

   public LanguageManager getLang() {
      return this.lang;
   }

   static {
      Object handler;
      try {
         Field _discard = Field.class.getDeclaredField("modifiers");
         handler = new Pre14PrivateFieldHandler();
      } catch (SecurityException | NoSuchFieldException var2) {
         handler = new Post14PrivateFieldHandler();
      }

      privateFieldHandler = (PrivateFieldHandler)handler;
   }
}
