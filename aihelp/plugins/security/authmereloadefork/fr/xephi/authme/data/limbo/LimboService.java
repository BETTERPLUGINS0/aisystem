package fr.xephi.authme.data.limbo;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.persistence.LimboPersistence;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.LimboSettings;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LimboService {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(LimboService.class);
   private final Map<String, LimboPlayer> entries = new ConcurrentHashMap();
   @Inject
   private Settings settings;
   @Inject
   private LimboPlayerTaskManager taskManager;
   @Inject
   private LimboServiceHelper helper;
   @Inject
   private LimboPersistence persistence;
   @Inject
   private AuthGroupHandler authGroupHandler;
   @Inject
   private SpawnLoader spawnLoader;

   LimboService() {
   }

   public void createLimboPlayer(Player player, boolean isRegistered) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      LimboPlayer limboFromDisk = this.persistence.getLimboPlayer(player);
      if (limboFromDisk != null) {
         this.logger.debug("LimboPlayer for `{0}` already exists on disk", (Object)name);
      }

      LimboPlayer existingLimbo = (LimboPlayer)this.entries.remove(name);
      if (existingLimbo != null) {
         existingLimbo.clearTasks();
         this.logger.debug("LimboPlayer for `{0}` already present in memory", (Object)name);
      }

      Location location = this.spawnLoader.getPlayerLocationOrSpawn(player);
      LimboPlayer limboPlayer = this.helper.merge(existingLimbo, limboFromDisk);
      limboPlayer = this.helper.merge(this.helper.createLimboPlayer(player, isRegistered, location), limboPlayer);
      this.taskManager.registerMessageTask(player, limboPlayer, isRegistered ? LimboMessageType.LOG_IN : LimboMessageType.REGISTER);
      this.taskManager.registerTimeoutTask(player, limboPlayer);
      this.helper.revokeLimboStates(player);
      this.authGroupHandler.setGroup(player, limboPlayer, isRegistered ? AuthGroupType.REGISTERED_UNAUTHENTICATED : AuthGroupType.UNREGISTERED);
      this.entries.put(name, limboPlayer);
      this.persistence.saveLimboPlayer(player, limboPlayer);
   }

   public LimboPlayer getLimboPlayer(String name) {
      return (LimboPlayer)this.entries.get(name.toLowerCase(Locale.ROOT));
   }

   public boolean hasLimboPlayer(String name) {
      return this.entries.containsKey(name.toLowerCase(Locale.ROOT));
   }

   public void restoreData(Player player) {
      String lowerName = player.getName().toLowerCase(Locale.ROOT);
      LimboPlayer limbo = (LimboPlayer)this.entries.remove(lowerName);
      if (limbo == null) {
         this.logger.debug("No LimboPlayer found for `{0}` - cannot restore", (Object)lowerName);
      } else {
         player.setOp(limbo.isOperator());
         ((AllowFlightRestoreType)this.settings.getProperty(LimboSettings.RESTORE_ALLOW_FLIGHT)).restoreAllowFlight(player, limbo);
         ((WalkFlySpeedRestoreType)this.settings.getProperty(LimboSettings.RESTORE_FLY_SPEED)).restoreFlySpeed(player, limbo);
         ((WalkFlySpeedRestoreType)this.settings.getProperty(LimboSettings.RESTORE_WALK_SPEED)).restoreWalkSpeed(player, limbo);
         limbo.clearTasks();
         this.logger.debug("Restored LimboPlayer stats for `{0}`", (Object)lowerName);
         this.persistence.removeLimboPlayer(player);
      }

      this.authGroupHandler.setGroup(player, limbo, AuthGroupType.LOGGED_IN);
   }

   public void replaceTasksAfterRegistration(Player player) {
      Optional<LimboPlayer> limboPlayer = this.getLimboOrLogError(player, "reset tasks");
      limboPlayer.ifPresent((limbo) -> {
         this.taskManager.registerTimeoutTask(player, limbo);
         this.taskManager.registerMessageTask(player, limbo, LimboMessageType.LOG_IN);
      });
      this.authGroupHandler.setGroup(player, (LimboPlayer)limboPlayer.orElse((Object)null), AuthGroupType.REGISTERED_UNAUTHENTICATED);
   }

   public void resetMessageTask(Player player, LimboMessageType messageType) {
      this.getLimboOrLogError(player, "reset message task").ifPresent((limbo) -> {
         this.taskManager.registerMessageTask(player, limbo, messageType);
      });
   }

   public void muteMessageTask(Player player) {
      this.getLimboOrLogError(player, "mute message task").ifPresent((limbo) -> {
         LimboPlayerTaskManager.setMuted(limbo.getMessageTask(), true);
      });
   }

   public void unmuteMessageTask(Player player) {
      this.getLimboOrLogError(player, "unmute message task").ifPresent((limbo) -> {
         LimboPlayerTaskManager.setMuted(limbo.getMessageTask(), false);
      });
   }

   private Optional<LimboPlayer> getLimboOrLogError(Player player, String context) {
      LimboPlayer limbo = (LimboPlayer)this.entries.get(player.getName().toLowerCase(Locale.ROOT));
      if (limbo == null) {
         this.logger.debug("No LimboPlayer found for `{0}`. Action: {1}", player.getName(), context);
      }

      return Optional.ofNullable(limbo);
   }
}
