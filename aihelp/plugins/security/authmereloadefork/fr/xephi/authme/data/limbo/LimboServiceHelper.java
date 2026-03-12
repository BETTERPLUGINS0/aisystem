package fr.xephi.authme.data.limbo;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.LimboSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class LimboServiceHelper {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(LimboServiceHelper.class);
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private Settings settings;

   LimboPlayer createLimboPlayer(Player player, boolean isRegistered, Location location) {
      boolean isOperator = isRegistered && player.isOp();
      boolean flyEnabled = player.getAllowFlight();
      float walkSpeed = player.getWalkSpeed();
      float flySpeed = player.getFlySpeed();
      Collection<UserGroup> playerGroups = this.permissionsManager.hasGroupSupport() ? this.permissionsManager.getGroups(player) : Collections.emptyList();
      List<String> groupNames = (List)((Collection)playerGroups).stream().map(UserGroup::getGroupName).collect(Collectors.toList());
      this.logger.debug("Player `{0}` has groups `{1}`", player.getName(), String.join(", ", groupNames));
      return new LimboPlayer(location, isOperator, (Collection)playerGroups, flyEnabled, walkSpeed, flySpeed);
   }

   void revokeLimboStates(Player player) {
      player.setOp(false);
      ((AllowFlightRestoreType)this.settings.getProperty(LimboSettings.RESTORE_ALLOW_FLIGHT)).processPlayer(player);
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.ALLOW_UNAUTHED_MOVEMENT)) {
         player.setFlySpeed(0.0F);
         player.setWalkSpeed(0.0F);
      }

   }

   LimboPlayer merge(LimboPlayer newLimbo, LimboPlayer oldLimbo) {
      if (newLimbo == null) {
         return oldLimbo;
      } else if (oldLimbo == null) {
         return newLimbo;
      } else {
         boolean isOperator = newLimbo.isOperator() || oldLimbo.isOperator();
         boolean canFly = newLimbo.isCanFly() || oldLimbo.isCanFly();
         float flySpeed = Math.max(newLimbo.getFlySpeed(), oldLimbo.getFlySpeed());
         float walkSpeed = Math.max(newLimbo.getWalkSpeed(), oldLimbo.getWalkSpeed());
         Collection<UserGroup> groups = this.getLimboGroups(oldLimbo.getGroups(), newLimbo.getGroups());
         Location location = firstNotNull(oldLimbo.getLocation(), newLimbo.getLocation());
         return new LimboPlayer(location, isOperator, groups, canFly, walkSpeed, flySpeed);
      }
   }

   private static Location firstNotNull(Location first, Location second) {
      return first == null ? second : first;
   }

   private Collection<UserGroup> getLimboGroups(Collection<UserGroup> oldLimboGroups, Collection<UserGroup> newLimboGroups) {
      this.logger.debug("Limbo merge: new and old groups are `{0}` and `{1}`", newLimboGroups, oldLimboGroups);
      return Utils.isCollectionEmpty(oldLimboGroups) ? newLimboGroups : oldLimboGroups;
   }
}
