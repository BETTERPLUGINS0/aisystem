package ac.grim.grimac.api.plugin;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

public class BasicGrimPlugin implements GrimPlugin {
   private final Logger logger;
   private final File dataFolder;
   private final BasicGrimPlugin.BasicGrimPluginDescription description;

   public BasicGrimPlugin(Logger logger, File dataFolder, String version, String description, Collection<String> authors) {
      this.logger = logger;
      this.dataFolder = dataFolder;
      this.description = new BasicGrimPlugin.BasicGrimPluginDescription(version, description, authors);
   }

   public GrimPluginDescription getDescription() {
      return this.description;
   }

   public Logger getLogger() {
      return this.logger;
   }

   public File getDataFolder() {
      return this.dataFolder;
   }

   private static class BasicGrimPluginDescription implements GrimPluginDescription {
      private final String version;
      private final String description;
      private final Collection<String> authors;

      public BasicGrimPluginDescription(String version, String description, Collection<String> authors) {
         this.version = version;
         this.description = description;
         this.authors = authors;
      }

      public String getVersion() {
         return this.version;
      }

      public String getDescription() {
         return this.description;
      }

      @NotNull
      public Collection<String> getAuthors() {
         return this.authors;
      }
   }
}
