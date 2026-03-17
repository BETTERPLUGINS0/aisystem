package advancedplugins.pm2.cv.api.interfaces;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDeyed {
   static boolean isValidId(@NotNull String id) {
      return StringUtils.isNotBlank(id) && id.toLowerCase().matches("[a-z0-9\\-_.]+");
   }

   static String idCheck(@NotNull String id) {
      if (StringUtils.isBlank(id)) {
         throw new IllegalArgumentException("id cannot be blank");
      } else if (!id.toLowerCase().matches("[a-z0-9\\-_.]+")) {
         throw new IllegalArgumentException("id contains invalid characters");
      } else {
         return id.toLowerCase();
      }
   }

   @NotNull
   static String loadId(@NotNull ConfigurationSection section) throws InvalidConfigurationException {
      String id = section.getString("id");
      if (id == null) {
         throw new InvalidConfigurationException("id must be set");
      } else {
         try {
            return idCheck(id.toLowerCase());
         } catch (IllegalArgumentException var3) {
            throw new InvalidConfigurationException(var3.getMessage());
         }
      }
   }

   @Nullable
   static String loadId(@NotNull ConfigurationSection section, @Nullable String defaultID) {
      String id = section.getString("id");
      if (id == null) {
         return defaultID;
      } else {
         try {
            return idCheck(id.toLowerCase());
         } catch (IllegalArgumentException var4) {
            return defaultID;
         }
      }
   }

   static void writeId(IDeyed ideyed, @NotNull ConfigurationSection section) {
      section.set("id", idCheck(ideyed.getId()));
   }

   static void writeId(@NotNull String id, @NotNull ConfigurationSection section) {
      section.set("id", idCheck(id));
   }

   @NotNull
   String getId();
}
