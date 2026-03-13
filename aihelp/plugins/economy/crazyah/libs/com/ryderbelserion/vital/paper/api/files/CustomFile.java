package libs.com.ryderbelserion.vital.paper.api.files;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

public class CustomFile {
   private final VitalAPI api = Provider.getApi();
   private final ComponentLogger logger;
   private final boolean isVerbose;
   private final String cleanName;
   private final File file;
   private YamlConfiguration configuration;

   public CustomFile(String name, File file) {
      this.logger = this.api.getComponentLogger();
      this.isVerbose = this.api.isVerbose();
      this.cleanName = name.replace(".yml", "");
      this.file = file;
   }

   public final CustomFile load() {
      if (this.file.isDirectory()) {
         if (this.isVerbose) {
            this.logger.warn("Cannot load, as it is a directory.");
         }

         return this;
      } else {
         try {
            this.configuration = (YamlConfiguration)CompletableFuture.supplyAsync(() -> {
               return YamlConfiguration.loadConfiguration(this.file);
            }).join();
         } catch (Exception var2) {
            if (this.isVerbose) {
               this.logger.error("Failed to load {}", this.file.getName(), var2);
            }
         }

         return this;
      }
   }

   public final CustomFile save() {
      if (this.file.isDirectory()) {
         if (this.isVerbose) {
            this.logger.warn("Cannot save, as it is a directory.");
         }

         return this;
      } else if (this.configuration == null) {
         if (this.isVerbose) {
            this.logger.error("File configuration is null, cannot save!");
         }

         return this;
      } else {
         CompletableFuture.runAsync(() -> {
            try {
               this.configuration.save(this.file);
            } catch (Exception var2) {
               if (this.isVerbose) {
                  this.logger.error("Failed to save: {}...", this.file.getName(), var2);
               }
            }

         });
         return this;
      }
   }

   @Nullable
   public final YamlConfiguration getConfiguration() {
      return this.configuration;
   }

   public final boolean exists() {
      return this.getConfiguration() != null;
   }

   public final String getCleanName() {
      return this.cleanName;
   }

   public final File getFile() {
      return this.file;
   }
}
