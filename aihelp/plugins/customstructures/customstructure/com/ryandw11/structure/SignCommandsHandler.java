package com.ryandw11.structure;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class SignCommandsHandler {
   private final Map<String, List<String>> signCommands = new HashMap();

   public SignCommandsHandler(File dataFolder, CustomStructures plugin) {
      YamlConfiguration yamlConfiguration = new YamlConfiguration();
      File signCommandsFile = new File(dataFolder, "signcommands.yml");
      if (!signCommandsFile.exists()) {
         plugin.getLogger().warning("Warning: Cannot find signcommands.yml. This might be a configuration error.");
      } else {
         try {
            yamlConfiguration.load(signCommandsFile);
         } catch (InvalidConfigurationException | IOException var8) {
            plugin.getLogger().severe("Error: Unable to load signcommands.yml file.");
            plugin.getLogger().severe("Please make sure signcommands.yml is configured correctly.");
            if (plugin.isDebug()) {
               var8.printStackTrace();
            }
         }

         String sectionKey;
         List commands;
         for(Iterator var5 = yamlConfiguration.getKeys(false).iterator(); var5.hasNext(); this.signCommands.put(sectionKey, commands)) {
            sectionKey = (String)var5.next();
            commands = yamlConfiguration.getStringList(sectionKey);
            if (commands.isEmpty()) {
               plugin.getLogger().warning("Sign command " + sectionKey + " has no commands! This may be a configuration error.");
            }
         }

      }
   }

   public void cleanUp() {
      this.signCommands.clear();
   }

   public List<String> getCommands(String name) {
      return (List)this.signCommands.get(name);
   }
}
