package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.ch.jalu.configme.Comment;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.PropertyInitializer;

public final class PurgeSettings implements SettingsHolder {
   @Comment({"If enabled, AuthMe automatically purges old, unused accounts"})
   public static final Property<Boolean> USE_AUTO_PURGE = PropertyInitializer.newProperty("Purge.useAutoPurge", false);
   @Comment({"Number of days after which an account should be purged"})
   public static final Property<Integer> DAYS_BEFORE_REMOVE_PLAYER = PropertyInitializer.newProperty("Purge.daysBeforeRemovePlayer", (int)60);
   @Comment({"Do we need to remove the player.dat file during purge process?"})
   public static final Property<Boolean> REMOVE_PLAYER_DAT = PropertyInitializer.newProperty("Purge.removePlayerDat", false);
   @Comment({"Do we need to remove the Essentials/userdata/player.yml file during purge process?"})
   public static final Property<Boolean> REMOVE_ESSENTIALS_FILES = PropertyInitializer.newProperty("Purge.removeEssentialsFile", false);
   @Comment({"World in which the players.dat are stored"})
   public static final Property<String> DEFAULT_WORLD = PropertyInitializer.newProperty("Purge.defaultWorld", "world");
   @Comment({"Remove LimitedCreative/inventories/player.yml, player_creative.yml files during purge?"})
   public static final Property<Boolean> REMOVE_LIMITED_CREATIVE_INVENTORIES = PropertyInitializer.newProperty("Purge.removeLimitedCreativesInventories", false);
   @Comment({"Do we need to remove the AntiXRayData/PlayerData/player file during purge process?"})
   public static final Property<Boolean> REMOVE_ANTI_XRAY_FILE = PropertyInitializer.newProperty("Purge.removeAntiXRayFile", false);
   @Comment({"Do we need to remove permissions?"})
   public static final Property<Boolean> REMOVE_PERMISSIONS = PropertyInitializer.newProperty("Purge.removePermissions", false);

   private PurgeSettings() {
   }
}
