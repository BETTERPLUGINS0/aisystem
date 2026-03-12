package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.collect.HashMultimap;
import fr.xephi.authme.libs.com.google.common.collect.Multimap;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValidationService implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ValidationService.class);
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private GeoIpService geoIpService;
   private Pattern passwordRegex;
   private Pattern emailRegex;
   private Multimap<String, String> restrictedNames;

   ValidationService() {
   }

   @PostConstruct
   public void reload() {
      this.passwordRegex = Utils.safePatternCompile((String)this.settings.getProperty(RestrictionSettings.ALLOWED_PASSWORD_REGEX));
      this.restrictedNames = (Multimap)((Boolean)this.settings.getProperty(RestrictionSettings.ENABLE_RESTRICTED_USERS) ? this.loadNameRestrictions((Set)this.settings.getProperty(RestrictionSettings.RESTRICTED_USERS)) : HashMultimap.create());
      this.emailRegex = Utils.safePatternCompile((String)this.settings.getProperty(RestrictionSettings.ALLOWED_EMAIL_REGEX));
   }

   public ValidationService.ValidationResult validatePassword(String password, String username) {
      String passLow = password.toLowerCase(Locale.ROOT);
      if (!this.passwordRegex.matcher(passLow).matches()) {
         return new ValidationService.ValidationResult(MessageKey.PASSWORD_CHARACTERS_ERROR, new String[]{this.passwordRegex.pattern()});
      } else if (passLow.equalsIgnoreCase(username)) {
         return new ValidationService.ValidationResult(MessageKey.PASSWORD_IS_USERNAME_ERROR, new String[0]);
      } else if (password.length() >= (Integer)this.settings.getProperty(SecuritySettings.MIN_PASSWORD_LENGTH) && password.length() <= (Integer)this.settings.getProperty(SecuritySettings.MAX_PASSWORD_LENGTH)) {
         if (((Set)this.settings.getProperty(SecuritySettings.UNSAFE_PASSWORDS)).contains(passLow)) {
            return new ValidationService.ValidationResult(MessageKey.PASSWORD_UNSAFE_ERROR, new String[0]);
         } else {
            if ((Boolean)this.settings.getProperty(SecuritySettings.HAVE_I_BEEN_PWNED_CHECK)) {
               ValidationService.HaveIBeenPwnedResults results = this.validatePasswordHaveIBeenPwned(password);
               if (results != null && results.isPwned() && results.getPwnCount() > (Integer)this.settings.getProperty(SecuritySettings.HAVE_I_BEEN_PWNED_LIMIT)) {
                  return new ValidationService.ValidationResult(MessageKey.PASSWORD_PWNED_ERROR, new String[]{String.valueOf(results.getPwnCount())});
               }
            }

            return new ValidationService.ValidationResult();
         }
      } else {
         return new ValidationService.ValidationResult(MessageKey.INVALID_PASSWORD_LENGTH, new String[0]);
      }
   }

   public boolean validateEmail(String email) {
      return this.emailRegex.matcher(email).matches();
   }

   public boolean isEmailFreeForRegistration(String email, CommandSender sender) {
      return this.permissionsManager.hasPermission(sender, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS) || this.dataSource.countAuthsByEmail(email) < (Integer)this.settings.getProperty(EmailSettings.MAX_REG_PER_EMAIL);
   }

   public boolean isCountryAdmitted(String hostAddress) {
      if (((List)this.settings.getProperty(ProtectionSettings.COUNTRIES_WHITELIST)).isEmpty() && ((List)this.settings.getProperty(ProtectionSettings.COUNTRIES_BLACKLIST)).isEmpty()) {
         return true;
      } else {
         String countryCode = this.geoIpService.getCountryCode(hostAddress);
         boolean isCountryAllowed = this.validateWhitelistAndBlacklist(countryCode, ProtectionSettings.COUNTRIES_WHITELIST, ProtectionSettings.COUNTRIES_BLACKLIST);
         this.logger.debug("Country code `{0}` for `{1}` is allowed: {2}", countryCode, hostAddress, isCountryAllowed);
         return isCountryAllowed;
      }
   }

   public boolean isUnrestricted(String name) {
      return ((Set)this.settings.getProperty(RestrictionSettings.UNRESTRICTED_NAMES)).contains(name.toLowerCase(Locale.ROOT));
   }

   public boolean fulfillsNameRestrictions(Player player) {
      Collection<String> restrictions = this.restrictedNames.get(player.getName().toLowerCase(Locale.ROOT));
      if (Utils.isCollectionEmpty(restrictions)) {
         return true;
      } else {
         String ip = PlayerUtils.getPlayerIp(player);
         String domain = this.getHostName(player.getAddress());
         Iterator var5 = restrictions.iterator();

         String restriction;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            restriction = (String)var5.next();
            if (restriction.startsWith("regex:")) {
               restriction = restriction.replace("regex:", "");
            } else {
               restriction = restriction.replace("*", "(.*)");
            }

            if (ip.matches(restriction)) {
               return true;
            }
         } while(!domain.matches(restriction));

         return true;
      }
   }

   @VisibleForTesting
   protected String getHostName(InetSocketAddress inetSocketAddr) {
      return inetSocketAddr.getHostName();
   }

   private boolean validateWhitelistAndBlacklist(String value, Property<List<String>> whitelist, Property<List<String>> blacklist) {
      List<String> whitelistValue = (List)this.settings.getProperty(whitelist);
      if (!Utils.isCollectionEmpty(whitelistValue)) {
         return containsIgnoreCase(whitelistValue, value);
      } else {
         List<String> blacklistValue = (List)this.settings.getProperty(blacklist);
         return Utils.isCollectionEmpty(blacklistValue) || !containsIgnoreCase(blacklistValue, value);
      }
   }

   private static boolean containsIgnoreCase(Collection<String> coll, String needle) {
      Iterator var2 = coll.iterator();

      String entry;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         entry = (String)var2.next();
      } while(!entry.equalsIgnoreCase(needle));

      return true;
   }

   private Multimap<String, String> loadNameRestrictions(Set<String> configuredRestrictions) {
      Multimap<String, String> restrictions = HashMultimap.create();
      Iterator var3 = configuredRestrictions.iterator();

      while(var3.hasNext()) {
         String restriction = (String)var3.next();
         if (StringUtils.isInsideString(';', restriction)) {
            String[] data = restriction.split(";");
            restrictions.put(data[0].toLowerCase(Locale.ROOT), data[1]);
         } else {
            this.logger.warning("Restricted user rule must have a ';' separating name from restriction, but found: '" + restriction + "'");
         }
      }

      return restrictions;
   }

   public ValidationService.HaveIBeenPwnedResults validatePasswordHaveIBeenPwned(String password) {
      String hash = HashUtils.sha1(password);
      String hashPrefix = hash.substring(0, 5);

      try {
         String url = String.format("https://api.pwnedpasswords.com/range/%s", hashPrefix);
         HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
         connection.setRequestMethod("GET");
         connection.setRequestProperty("User-Agent", "AuthMeReloaded");
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         connection.setDoInput(true);
         StringBuilder outStr = new StringBuilder();
         DataInputStream input = new DataInputStream(connection.getInputStream());

         try {
            for(int c = input.read(); c != -1; c = input.read()) {
               outStr.append((char)c);
            }
         } catch (Throwable var14) {
            try {
               input.close();
            } catch (Throwable var13) {
               var14.addSuppressed(var13);
            }

            throw var14;
         }

         input.close();
         String[] var16 = outStr.toString().split("\n");
         String[] var17 = var16;
         int var9 = var16.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String hashSuffix = var17[var10];
            String[] hashSuffixParts = hashSuffix.trim().split(":");
            if (hashSuffixParts[0].equalsIgnoreCase(hash.substring(5))) {
               return new ValidationService.HaveIBeenPwnedResults(true, Integer.parseInt(hashSuffixParts[1]));
            }
         }

         return new ValidationService.HaveIBeenPwnedResults(false, 0);
      } catch (IOException var15) {
         this.logger.warning("Error occurred while checking password online, check your connection.\nWhen this error shows, the player's password won't be check");
         return null;
      }
   }

   public static final class ValidationResult {
      private final MessageKey messageKey;
      private final String[] args;

      public ValidationResult() {
         this.messageKey = null;
         this.args = null;
      }

      public ValidationResult(MessageKey messageKey, String... args) {
         this.messageKey = messageKey;
         this.args = args;
      }

      public boolean hasError() {
         return this.messageKey != null;
      }

      public MessageKey getMessageKey() {
         return this.messageKey;
      }

      public String[] getArgs() {
         return this.args;
      }
   }

   public static final class HaveIBeenPwnedResults {
      private final boolean isPwned;
      private final int pwnCount;

      public HaveIBeenPwnedResults(boolean isPwned, int pwnCount) {
         this.isPwned = isPwned;
         this.pwnCount = pwnCount;
      }

      public boolean isPwned() {
         return this.isPwned;
      }

      public int getPwnCount() {
         return this.pwnCount;
      }
   }
}
