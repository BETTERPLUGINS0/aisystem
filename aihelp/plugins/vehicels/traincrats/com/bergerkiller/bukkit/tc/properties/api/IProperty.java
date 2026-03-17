package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public interface IProperty<T> {
   T getDefault();

   Optional<T> readFromConfig(ConfigurationNode var1);

   void writeToConfig(ConfigurationNode var1, Optional<T> var2);

   default void onConfigurationChanged(CartProperties properties) {
   }

   default void onConfigurationChanged(TrainProperties properties) {
   }

   T get(CartProperties var1);

   void set(CartProperties var1, T var2);

   T get(TrainProperties var1);

   void set(TrainProperties var1, T var2);

   default boolean isAppliedAsDefault() {
      return true;
   }

   default String getPermissionName() {
      Method[] var1 = this.getClass().getDeclaredMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method m = var1[var3];
         PropertyParser parser = (PropertyParser)m.getAnnotation(PropertyParser.class);
         if (parser != null) {
            String name = parser.value();
            int sepIdx = name.indexOf(124);
            if (sepIdx > 0) {
               name = name.substring(0, sepIdx);
            }

            return name;
         }
      }

      String name = this.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
      if (name.endsWith("property")) {
         name = name.substring(0, name.length() - 8);
      }

      return name;
   }

   default String getListedName() {
      return this.getPermissionName();
   }

   default boolean isListed() {
      return true;
   }

   default boolean hasPermission(CommandSender sender, String name) {
      return true;
   }
}
