package advancedplugins.pm2.cv.api.interfaces;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Named {
   static boolean isValidName(@NotNull String name) {
      return StringUtils.isNotBlank(name) && name.toLowerCase().matches("[a-zA-Z0-9\\s\\-_.]+");
   }

   static String nameCheck(@NotNull String name) {
      if (StringUtils.isBlank(name)) {
         throw new IllegalArgumentException("name cannot be blank");
      } else if (!name.matches("[a-zA-Z0-9\\s\\-_.]+")) {
         throw new IllegalArgumentException("name contains invalid characters");
      } else {
         return name;
      }
   }

   static String loadName(@NotNull ConfigurationSection section, @Nullable String defaultName) throws InvalidConfigurationException {
      String name = section.getString("name", defaultName);
      if (name == null) {
         throw new InvalidConfigurationException("name must be set");
      } else {
         try {
            return nameCheck(name);
         } catch (IllegalArgumentException var4) {
            throw new InvalidConfigurationException(var4.getMessage());
         }
      }
   }

   static String loadName(@NotNull ConfigurationSection section) throws InvalidConfigurationException {
      return loadName(section, (String)null);
   }

   static void writeName(Named named, @NotNull ConfigurationSection section) {
      section.set("name", nameCheck(named.getName()));
   }

   @NotNull
   String getName();
}
