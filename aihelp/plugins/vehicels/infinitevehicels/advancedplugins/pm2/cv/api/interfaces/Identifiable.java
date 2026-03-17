package advancedplugins.pm2.cv.api.interfaces;

import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Identifiable {
   @Nullable
   static UUID loadIdentifier(@NotNull ConfigurationSection section) {
      String identifierString = section.getString("identifier");
      if (identifierString != null) {
         try {
            return UUID.fromString(identifierString);
         } catch (IllegalArgumentException var3) {
         }
      }

      return null;
   }

   @NotNull
   static UUID loadIdentifierOrGenerate(@NotNull ConfigurationSection section) {
      UUID identifier = loadIdentifier(section);
      return identifier != null ? identifier : UUID.randomUUID();
   }

   static void writeIdentifier(@NotNull Identifiable identifiable, @NotNull ConfigurationSection section) {
      section.set("identifier", identifiable.getIdentifier().toString());
   }

   static void writeIdentifier(@NotNull UUID identifier, @NotNull ConfigurationSection section) {
      section.set("identifier", identifier.toString());
   }

   @NotNull
   UUID getIdentifier();
}
