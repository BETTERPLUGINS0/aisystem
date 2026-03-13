package me.gypopo.economyshopgui.util;

import java.util.Locale;
import java.util.Optional;
import org.bukkit.entity.EntityType;

public class EntityTypes {
   public static Optional<EntityType> matchEntityType(String name) {
      name = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
      if (ServerInfo.supportsComponents()) {
         byte var2 = -1;
         switch(name.hashCode()) {
         case -1337905961:
            if (name.equals("SNOWMAN")) {
               var2 = 1;
            }
            break;
         case -875444988:
            if (name.equals("MUSHROOM_COW")) {
               var2 = 0;
            }
         }

         switch(var2) {
         case 0:
            name = "MOOSHROOM";
            break;
         case 1:
            name = "SNOW_GOLEM";
         }
      }

      try {
         return Optional.of(EntityType.valueOf(name));
      } catch (IllegalArgumentException var3) {
         return Optional.empty();
      }
   }
}
