package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapResourcePack;
import com.bergerkiller.bukkit.common.map.MapResourcePack.ResourceType;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

public abstract class BasicModularConfiguration<T> extends ModularConfiguration<T> {
   public static final String KEY_SAVED_NAME = "savedName";
   public final ModularConfigurationFile<T> DEFAULT;
   public final ModularConfigurationDirectory<T> MODULES;

   public BasicModularConfiguration(Plugin plugin, String mainFilePath, String moduleDirectoryPath) {
      super(plugin.getLogger());
      File data = plugin.getDataFolder();
      this.DEFAULT = this.addFileModule("DEFAULT", new File(data, mainFilePath), false);
      this.MODULES = this.addDirectoryModule(new File(data, moduleDirectoryPath));
   }

   public void addResourcePack(MapResourcePack resourcePack, String namespace, String directory) {
      Set yamlFiles;
      try {
         yamlFiles = resourcePack.listResources(ResourceType.YAML, namespace, directory);
      } catch (Throwable var10) {
         return;
      }

      Iterator var5 = yamlFiles.iterator();

      while(true) {
         String name;
         ConfigurationNode config;
         while(true) {
            if (!var5.hasNext()) {
               return;
            }

            String resource = (String)var5.next();
            name = resource;
            if (resource.startsWith(directory + "/")) {
               name = resource.substring(directory.length() + 1);
            }

            try {
               config = resourcePack.getConfig(namespace + ":" + resource);
               int trainCount = config.getKeys().size();
               if (trainCount != 0) {
                  this.logger.info("[Resource Pack] Loaded " + trainCount + " saved train properties from '" + name + "'");
                  break;
               }
            } catch (Throwable var11) {
               this.logger.log(Level.WARNING, "Failed to load resource pack saved train properties '" + name + "'", var11);
            }
         }

         this.addBlock(new ModularConfigurationModule(this, "RESOURCEPACK:" + name, config, true), false);
      }
   }

   public ModularConfigurationModule<T> getDefaultModule() {
      return this.DEFAULT;
   }

   public ModularConfigurationModule<T> createModule(String name) {
      return name != null && !name.isEmpty() ? this.MODULES.createFile(name) : this.DEFAULT;
   }

   public ModularConfigurationEntry<T> add(String name, ConfigurationNode config, String moduleName) throws ReadOnlyModuleException {
      ModularConfigurationEntry<T> entry = this.get(name);
      entry.createWithConfigInModule(config, this.createModule(moduleName));
      return entry;
   }
}
