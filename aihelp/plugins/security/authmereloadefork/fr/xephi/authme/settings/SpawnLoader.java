package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpawnLoader implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(SpawnLoader.class);
   private final File authMeConfigurationFile;
   private final Settings settings;
   private final PluginHookService pluginHookService;
   private FileConfiguration authMeConfiguration;
   private String[] spawnPriority;
   private Location essentialsSpawn;
   private Location cmiSpawn;

   @Inject
   SpawnLoader(@DataFolder File pluginFolder, Settings settings, PluginHookService pluginHookService) {
      File spawnFile = new File(pluginFolder, "spawn.yml");
      FileUtils.copyFileFromResource(spawnFile, "spawn.yml");
      this.authMeConfigurationFile = spawnFile;
      this.settings = settings;
      this.pluginHookService = pluginHookService;
      this.reload();
   }

   public void reload() {
      this.spawnPriority = ((String)this.settings.getProperty(RestrictionSettings.SPAWN_PRIORITY)).split(",");
      this.authMeConfiguration = YamlConfiguration.loadConfiguration(this.authMeConfigurationFile);
      this.loadEssentialsSpawn();
   }

   public Location getSpawn() {
      return getLocationFromConfiguration(this.authMeConfiguration, "spawn");
   }

   public boolean setSpawn(Location location) {
      return this.setLocation("spawn", location);
   }

   public Location getFirstSpawn() {
      return getLocationFromConfiguration(this.authMeConfiguration, "firstspawn");
   }

   public boolean setFirstSpawn(Location location) {
      return this.setLocation("firstspawn", location);
   }

   public void loadEssentialsSpawn() {
      File essentialsFolder = this.pluginHookService.getEssentialsDataFolder();
      if (essentialsFolder != null) {
         File essentialsSpawnFile = new File(essentialsFolder, "spawn.yml");
         if (essentialsSpawnFile.exists()) {
            this.essentialsSpawn = getLocationFromConfiguration(YamlConfiguration.loadConfiguration(essentialsSpawnFile), "spawns.default");
         } else {
            this.essentialsSpawn = null;
            this.logger.info("Essentials spawn file not found: '" + essentialsSpawnFile.getAbsolutePath() + "'");
         }

      }
   }

   public void unloadEssentialsSpawn() {
      this.essentialsSpawn = null;
   }

   public void loadCmiSpawn() {
      File cmiFolder = this.pluginHookService.getCmiDataFolder();
      if (cmiFolder != null) {
         File cmiConfig = new File(cmiFolder, "config.yml");
         if (cmiConfig.exists()) {
            this.cmiSpawn = getLocationFromCmiConfiguration(YamlConfiguration.loadConfiguration(cmiConfig));
         } else {
            this.cmiSpawn = null;
            this.logger.info("CMI config file not found: '" + cmiConfig.getAbsolutePath() + "'");
         }

      }
   }

   public void unloadCmiSpawn() {
      this.cmiSpawn = null;
   }

   public Location getSpawnLocation(Player player) {
      if (player != null && player.getWorld() != null) {
         World world = player.getWorld();
         Location spawnLoc = null;
         String[] var4 = this.spawnPriority;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String priority = var4[var6];
            String var8 = priority.toLowerCase(Locale.ROOT).trim();
            byte var9 = -1;
            switch(var8.hashCode()) {
            case -1406328512:
               if (var8.equals("authme")) {
                  var9 = 4;
               }
               break;
            case 98623:
               if (var8.equals("cmi")) {
                  var9 = 3;
               }
               break;
            case 1190983397:
               if (var8.equals("essentials")) {
                  var9 = 2;
               }
               break;
            case 1270415260:
               if (var8.equals("multiverse")) {
                  var9 = 1;
               }
               break;
            case 1544803905:
               if (var8.equals("default")) {
                  var9 = 0;
               }
            }

            switch(var9) {
            case 0:
               if (world.getSpawnLocation() == null) {
                  break;
               }

               if (!this.isValidSpawnPoint(world.getSpawnLocation())) {
                  Iterator var10 = Bukkit.getWorlds().iterator();

                  while(var10.hasNext()) {
                     World spawnWorld = (World)var10.next();
                     if (this.isValidSpawnPoint(spawnWorld.getSpawnLocation())) {
                        world = spawnWorld;
                        break;
                     }
                  }

                  this.logger.warning("Seems like AuthMe is unable to find a proper spawn location. Set a location with the command '/authme setspawn'");
               }

               spawnLoc = world.getSpawnLocation();
               break;
            case 1:
               if ((Boolean)this.settings.getProperty(HooksSettings.MULTIVERSE)) {
                  spawnLoc = this.pluginHookService.getMultiverseSpawn(world);
               }
               break;
            case 2:
               spawnLoc = this.essentialsSpawn;
               break;
            case 3:
               spawnLoc = this.cmiSpawn;
               break;
            case 4:
               spawnLoc = this.getSpawn();
            }

            if (spawnLoc != null) {
               this.logger.debug("Spawn location determined as `{0}` for world `{1}`", spawnLoc, world.getName());
               return spawnLoc;
            }
         }

         this.logger.debug("Fall back to default world spawn location. World: `{0}`", (Object)world.getName());
         return world.getSpawnLocation();
      } else {
         return null;
      }
   }

   private boolean isValidSpawnPoint(Location location) {
      return location.getX() != 0.0D || location.getY() != 0.0D || location.getZ() != 0.0D;
   }

   private boolean setLocation(String prefix, Location location) {
      if (location != null && location.getWorld() != null) {
         this.authMeConfiguration.set(prefix + ".world", location.getWorld().getName());
         this.authMeConfiguration.set(prefix + ".x", location.getX());
         this.authMeConfiguration.set(prefix + ".y", location.getY());
         this.authMeConfiguration.set(prefix + ".z", location.getZ());
         this.authMeConfiguration.set(prefix + ".yaw", location.getYaw());
         this.authMeConfiguration.set(prefix + ".pitch", location.getPitch());
         return this.saveAuthMeConfig();
      } else {
         return false;
      }
   }

   private boolean saveAuthMeConfig() {
      try {
         this.authMeConfiguration.save(this.authMeConfigurationFile);
         return true;
      } catch (IOException var2) {
         this.logger.logException("Could not save spawn config (" + this.authMeConfigurationFile + ")", var2);
         return false;
      }
   }

   public Location getPlayerLocationOrSpawn(Player player) {
      return player.getHealth() <= 0.0D ? this.getSpawnLocation(player) : player.getLocation();
   }

   private static Location getLocationFromConfiguration(FileConfiguration configuration, String pathPrefix) {
      if (containsAllSpawnFields(configuration, pathPrefix)) {
         String prefix = pathPrefix + ".";
         String worldName = configuration.getString(prefix + "world");
         World world = Bukkit.getWorld(worldName);
         if (!StringUtils.isBlank(worldName) && world != null) {
            return new Location(world, configuration.getDouble(prefix + "x"), configuration.getDouble(prefix + "y"), configuration.getDouble(prefix + "z"), getFloat(configuration, prefix + "yaw"), getFloat(configuration, prefix + "pitch"));
         }
      }

      return null;
   }

   private static Location getLocationFromCmiConfiguration(FileConfiguration configuration) {
      String pathPrefix = "Spawn.Main";
      if (isLocationCompleteInCmiConfig(configuration, "Spawn.Main")) {
         String prefix = "Spawn.Main.";
         String worldName = configuration.getString(prefix + "World");
         World world = Bukkit.getWorld(worldName);
         if (!StringUtils.isBlank(worldName) && world != null) {
            return new Location(world, configuration.getDouble(prefix + "X"), configuration.getDouble(prefix + "Y"), configuration.getDouble(prefix + "Z"), getFloat(configuration, prefix + "Yaw"), getFloat(configuration, prefix + "Pitch"));
         }
      }

      return null;
   }

   private static boolean containsAllSpawnFields(FileConfiguration configuration, String pathPrefix) {
      String[] fields = new String[]{"world", "x", "y", "z", "yaw", "pitch"};
      String[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String field = var3[var5];
         if (!configuration.contains(pathPrefix + "." + field)) {
            return false;
         }
      }

      return true;
   }

   private static boolean isLocationCompleteInCmiConfig(FileConfiguration cmiConfiguration, String pathPrefix) {
      String[] fields = new String[]{"World", "X", "Y", "Z", "Yaw", "Pitch"};
      String[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String field = var3[var5];
         if (!cmiConfiguration.contains(pathPrefix + "." + field)) {
            return false;
         }
      }

      return true;
   }

   private static float getFloat(FileConfiguration configuration, String path) {
      Object value = configuration.get(path);
      return value instanceof Number ? ((Number)value).floatValue() : 0.0F;
   }
}
