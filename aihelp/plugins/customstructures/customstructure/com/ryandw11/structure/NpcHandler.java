package com.ryandw11.structure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class NpcHandler {
   private final Map<String, NpcHandler.NpcInfo> npcInfoMap = new HashMap();

   public NpcHandler(File dataFolder, CustomStructures plugin) {
      YamlConfiguration yamlConfiguration = new YamlConfiguration();
      File npcFile = new File(dataFolder, "npcs.yml");
      if (npcFile.exists()) {
         try {
            yamlConfiguration.load(new File(dataFolder, "npcs.yml"));
         } catch (Exception var11) {
            plugin.getLogger().severe("There is a configuration error with: npcs.yml.");
            if (plugin.isDebug()) {
               var11.printStackTrace();
            }
         }

         Iterator var5 = yamlConfiguration.getKeys(false).iterator();

         while(var5.hasNext()) {
            String npcKey = (String)var5.next();
            ConfigurationSection section = yamlConfiguration.getConfigurationSection(npcKey);
            if (section != null) {
               NpcHandler.NpcInfo npcInfo = new NpcHandler.NpcInfo();
               npcInfo.name = (String)this.getValueWithDefault(section, "name", "");
               npcInfo.skinUrl = (String)this.getValueWithDefault(section, "skinUrl", (Object)null);
               npcInfo.movesAround = (Boolean)this.getValueWithDefault(section, "movesAround", false);
               npcInfo.looksAtPlayer = (Boolean)this.getValueWithDefault(section, "looksAtPlayer", false);
               npcInfo.isProtected = (Boolean)this.getValueWithDefault(section, "isProtected", false);
               npcInfo.commandsSequential = (Boolean)this.getValueWithDefault(section, "commandsSequential", false);
               npcInfo.entityType = (String)this.getValueWithDefault(section, "entityType", "VILLAGER");
               List<String> commandsOnCreate = section.getStringList("commandsOnCreate");
               if (!commandsOnCreate.isEmpty()) {
                  npcInfo.commandsOnCreate = commandsOnCreate;
               }

               List<String> commandsOnClick = section.getStringList("commandsOnClick");
               if (!commandsOnClick.isEmpty()) {
                  npcInfo.commandsOnClick = commandsOnClick;
               }

               this.npcInfoMap.put(npcKey, npcInfo);
            }
         }

      }
   }

   private <T> T getValueWithDefault(ConfigurationSection npc, String attributeName, T defaultValue) {
      return npc.contains(attributeName) ? npc.get(attributeName) : defaultValue;
   }

   public void cleanUp() {
      this.npcInfoMap.clear();
   }

   public NpcHandler.NpcInfo getNPCByName(String name) {
      return (NpcHandler.NpcInfo)this.npcInfoMap.get(name);
   }

   public static class NpcInfo {
      public String name = "";
      public String skinUrl = "";
      public boolean movesAround = false;
      public boolean looksAtPlayer = false;
      public boolean isProtected = false;
      public String entityType = "VILLAGER";
      public List<String> commandsOnCreate = new ArrayList();
      public List<String> commandsOnClick = new ArrayList();
      public boolean commandsSequential = false;
   }
}
