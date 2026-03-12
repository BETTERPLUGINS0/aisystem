package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.ch.jalu.configme.Comment;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public final class ProtectionSettings implements SettingsHolder {
   @Comment({"Enable some servers protection (country based login, antibot)"})
   public static final Property<Boolean> ENABLE_PROTECTION = PropertyInitializer.newProperty("Protection.enableProtection", false);
   @Comment({"Apply the protection also to registered usernames"})
   public static final Property<Boolean> ENABLE_PROTECTION_REGISTERED = PropertyInitializer.newProperty("Protection.enableProtectionRegistered", false);
   @Comment({"Countries allowed to join the server and register. For country codes, see", "https://dev.maxmind.com/geoip/legacy/codes/iso3166/", "Use \"LOCALHOST\" for local addresses.", "PLEASE USE QUOTES!"})
   public static final Property<List<String>> COUNTRIES_WHITELIST = PropertyInitializer.newListProperty("Protection.countries", "LOCALHOST");
   @Comment({"Countries not allowed to join the server and register", "PLEASE USE QUOTES!"})
   public static final Property<List<String>> COUNTRIES_BLACKLIST = PropertyInitializer.newListProperty("Protection.countriesBlacklist", "A1");
   @Comment({"Do we need to enable automatic antibot system?"})
   public static final Property<Boolean> ENABLE_ANTIBOT = PropertyInitializer.newProperty("Protection.enableAntiBot", true);
   @Comment({"The interval in seconds"})
   public static final Property<Integer> ANTIBOT_INTERVAL = PropertyInitializer.newProperty("Protection.antiBotInterval", (int)5);
   @Comment({"Max number of players allowed to login in the interval", "before the AntiBot system is enabled automatically"})
   public static final Property<Integer> ANTIBOT_SENSIBILITY = PropertyInitializer.newProperty("Protection.antiBotSensibility", (int)10);
   @Comment({"Duration in minutes of the antibot automatic system"})
   public static final Property<Integer> ANTIBOT_DURATION = PropertyInitializer.newProperty("Protection.antiBotDuration", (int)10);
   @Comment({"Delay in seconds before the antibot activation"})
   public static final Property<Integer> ANTIBOT_DELAY = PropertyInitializer.newProperty("Protection.antiBotDelay", (int)60);
   @Comment({"Kicks the player that issued a command before the defined time after the join process"})
   public static final Property<Integer> QUICK_COMMANDS_DENIED_BEFORE_MILLISECONDS = PropertyInitializer.newProperty("Protection.quickCommands.denyCommandsBeforeMilliseconds", (int)1000);

   private ProtectionSettings() {
   }
}
