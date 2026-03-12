package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AbstractTeleportEvent;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.events.FirstSpawnTeleportEvent;
import fr.xephi.authme.events.SpawnTeleportEvent;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.TeleportUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportationService implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(TeleportationService.class);
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private DataSource dataSource;
   private Set<String> spawnOnLoginWorlds;

   TeleportationService() {
   }

   @PostConstruct
   public void reload() {
      this.spawnOnLoginWorlds = new HashSet((Collection)this.settings.getProperty(RestrictionSettings.FORCE_SPAWN_ON_WORLDS));
   }

   public void teleportOnJoin(Player player) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT) && (Boolean)this.settings.getProperty(RestrictionSettings.TELEPORT_UNAUTHED_TO_SPAWN)) {
         this.logger.debug("Teleport on join for player `{0}`", (Object)player.getName());
         this.teleportToSpawn(player, this.playerCache.isAuthenticated(player.getName()));
      }

   }

   public Location prepareOnJoinSpawnLocation(Player player) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT) && (Boolean)this.settings.getProperty(RestrictionSettings.TELEPORT_UNAUTHED_TO_SPAWN)) {
         Location location = this.spawnLoader.getSpawnLocation(player);
         SpawnTeleportEvent event = new SpawnTeleportEvent(player, location, this.playerCache.isAuthenticated(player.getName()));
         this.bukkitService.callEvent(event);
         if (!isEventValid(event)) {
            return null;
         } else {
            this.logger.debug("Returning custom location for >1.9 join event for player `{0}`", (Object)player.getName());
            return location;
         }
      } else {
         return null;
      }
   }

   public void teleportNewPlayerToFirstSpawn(Player player) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT)) {
         Location firstSpawn = this.spawnLoader.getFirstSpawn();
         if (firstSpawn != null) {
            if (!player.hasPlayedBefore() || !this.dataSource.isAuthAvailable(player.getName())) {
               this.logger.debug("Attempting to teleport player `{0}` to first spawn", (Object)player.getName());
               this.performTeleportation(player, new FirstSpawnTeleportEvent(player, firstSpawn));
            }

         }
      }
   }

   public void teleportOnLogin(Player player, PlayerAuth auth, LimboPlayer limbo) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT)) {
         String worldName = limbo != null && limbo.getLocation() != null ? limbo.getLocation().getWorld().getName() : null;
         if (this.mustForceSpawnAfterLogin(worldName)) {
            this.logger.debug("Teleporting `{0}` to spawn because of 'force-spawn after login'", (Object)player.getName());
            this.teleportToSpawn(player, true);
         } else if ((Boolean)this.settings.getProperty(RestrictionSettings.TELEPORT_UNAUTHED_TO_SPAWN)) {
            if ((Boolean)this.settings.getProperty(RestrictionSettings.SAVE_QUIT_LOCATION)) {
               Location location = this.buildLocationFromAuth(player, auth);
               Location playerLoc = player.getLocation();
               if (location.getX() == playerLoc.getX() && location.getY() == playerLoc.getY() && location.getZ() == playerLoc.getZ() && location.getWorld() == playerLoc.getWorld()) {
                  return;
               }

               this.logger.debug("Teleporting `{0}` after login, based on the player auth", (Object)player.getName());
               this.teleportBackFromSpawn(player, location);
            } else if (limbo != null && limbo.getLocation() != null) {
               this.logger.debug("Teleporting `{0}` after login, based on the limbo player", (Object)player.getName());
               this.teleportBackFromSpawn(player, limbo.getLocation());
            }
         }

      }
   }

   private boolean mustForceSpawnAfterLogin(String worldName) {
      return worldName != null && (Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SPAWN_LOCATION_AFTER_LOGIN) && this.spawnOnLoginWorlds.contains(worldName);
   }

   private Location buildLocationFromAuth(Player player, PlayerAuth auth) {
      World world = this.bukkitService.getWorld(auth.getWorld());
      if (world == null) {
         world = player.getWorld();
      }

      return new Location(world, auth.getQuitLocX(), auth.getQuitLocY(), auth.getQuitLocZ(), auth.getYaw(), auth.getPitch());
   }

   private void teleportBackFromSpawn(Player player, Location location) {
      this.performTeleportation(player, new AuthMeTeleportEvent(player, location));
   }

   private void teleportToSpawn(Player player, boolean isAuthenticated) {
      Location spawnLoc = this.spawnLoader.getSpawnLocation(player);
      this.performTeleportation(player, new SpawnTeleportEvent(player, spawnLoc, isAuthenticated));
   }

   private void performTeleportation(Player player, AbstractTeleportEvent event) {
      this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
         this.bukkitService.callEvent(event);
         if (player.isOnline() && isEventValid(event)) {
            TeleportUtils.teleport(player, event.getTo());
         }

      });
   }

   private static boolean isEventValid(AbstractTeleportEvent event) {
      return !event.isCancelled() && event.getTo() != null && event.getTo().getWorld() != null;
   }
}
