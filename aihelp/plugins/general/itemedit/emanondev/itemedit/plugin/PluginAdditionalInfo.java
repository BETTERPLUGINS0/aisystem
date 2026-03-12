package emanondev.itemedit.plugin;

import emanondev.itemedit.APlugin;
import java.io.InputStream;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

public class PluginAdditionalInfo {
   private final String modrinthProjectId;
   private final String modrinthProjectName;
   private final Integer spigotResourceId;
   private final boolean foliaSupported;
   private final Integer bstatsPluginId;
   private final APlugin plugin;

   public PluginAdditionalInfo(@NotNull APlugin plugin) {
      this.plugin = plugin;
      Yaml yaml = new Yaml();
      Integer spigotResourceId = null;
      String modrinthResourceId = null;
      String modrinthResourceName = null;
      boolean foliaSupported = false;
      Integer bstatsPluginId = null;

      try {
         InputStream inputStream = plugin.getResource("plugin.yml");

         try {
            if (inputStream == null) {
               throw new IllegalArgumentException("YAML file not found inside JAR");
            }

            Map<String, Object> data = (Map)yaml.load(inputStream);
            spigotResourceId = (Integer)data.get("spigot-resource-id");
            modrinthResourceId = (String)data.get("modrinth-project-id");
            modrinthResourceName = (String)data.get("modrinth-project-name");
            foliaSupported = (Boolean)data.getOrDefault("folia-supported", false);
            bstatsPluginId = (Integer)data.get("bstats-plugin-id");
         } catch (Throwable var12) {
            if (inputStream != null) {
               try {
                  inputStream.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (inputStream != null) {
            inputStream.close();
         }
      } catch (Throwable var13) {
         var13.printStackTrace();
      }

      this.spigotResourceId = spigotResourceId;
      this.modrinthProjectId = modrinthResourceId;
      this.modrinthProjectName = modrinthResourceName;
      this.foliaSupported = foliaSupported;
      this.bstatsPluginId = bstatsPluginId;
   }

   public <T> T get(@NotNull String path) {
      return this.get(path, (Object)null);
   }

   public <T> T get(@NotNull String path, @Nullable T def) {
      Yaml yaml = new Yaml();

      try {
         InputStream inputStream = this.plugin.getResource("plugin.yml");

         Object var6;
         try {
            if (inputStream == null) {
               throw new IllegalArgumentException("YAML file not found inside JAR");
            }

            Map<String, Object> data = (Map)yaml.load(inputStream);
            var6 = data.getOrDefault(path, def);
         } catch (Throwable var8) {
            if (inputStream != null) {
               try {
                  inputStream.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (inputStream != null) {
            inputStream.close();
         }

         return var6;
      } catch (Exception var9) {
         var9.printStackTrace();
         return def;
      }
   }

   public String getModrinthProjectId() {
      return this.modrinthProjectId;
   }

   public String getModrinthProjectName() {
      return this.modrinthProjectName;
   }

   public Integer getSpigotResourceId() {
      return this.spigotResourceId;
   }

   public boolean isFoliaSupported() {
      return this.foliaSupported;
   }

   public Integer getBstatsPluginId() {
      return this.bstatsPluginId;
   }

   public APlugin getPlugin() {
      return this.plugin;
   }
}
