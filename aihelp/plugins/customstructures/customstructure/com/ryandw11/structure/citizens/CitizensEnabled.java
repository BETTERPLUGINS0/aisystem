package com.ryandw11.structure.citizens;

import com.google.gson.Gson;
import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.NpcHandler;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.CommandTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.CommandTrait.ExecutionMode;
import net.citizensnpcs.trait.CommandTrait.Hand;
import net.citizensnpcs.trait.CommandTrait.NPCCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CitizensEnabled implements CitizensNpcHook {
   private static final Map<String, Map<String, Object>> skinDataCache = new HashMap();
   private static final Map<String, Object> INVALID = new HashMap();
   private final CustomStructures plugin;

   public CitizensEnabled(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public void spawnNpc(NpcHandler npcHandler, String name, Location loc) {
      NpcHandler.NpcInfo info = npcHandler.getNPCByName(name);
      if (info == null) {
         this.plugin.getLogger().warning("Failed to spawn NPC '" + name + "', no configuration found.");
      } else {
         EntityType type = EntityType.VILLAGER;

         try {
            type = EntityType.valueOf(info.entityType);
         } catch (IllegalArgumentException var15) {
            this.plugin.getLogger().warning("Unsupported NPC entity-type '" + info.entityType + "'! Spawning a villager instead.");
         }

         String npcName = CustomStructures.replacePAPIPlaceholders(info.name);
         NPC npc = CitizensAPI.getNPCRegistry().createNPC(type, npcName);
         int npcId = npc.getId();
         if (!npc.isSpawned()) {
            npc.spawn(loc.add(0.5D, 0.0D, 0.5D));
         }

         npc.setProtected(info.isProtected);
         npc.setUseMinecraftAI(info.movesAround);
         LookClose lookClose = (LookClose)npc.getOrAddTrait(LookClose.class);
         lookClose.lookClose(info.looksAtPlayer);
         Iterator var10;
         String command;
         if (!info.commandsOnCreate.isEmpty()) {
            var10 = info.commandsOnCreate.iterator();

            while(var10.hasNext()) {
               command = (String)var10.next();
               command = command.trim();
               command = command.replace("<npcid>", String.valueOf(npcId));
               command = CustomStructures.replacePAPIPlaceholders(command);
               if (command.toUpperCase().startsWith("[PLAYER]")) {
                  command = command.substring(8);
                  this.plugin.getLogger().warning("Ignoring [PLAYER] prefix for 'commandsOnCreate' commands!");
               }

               this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
               if (this.plugin.isDebug()) {
                  this.plugin.getLogger().info("[DEBUG] Executing command for newly created NPC: '" + command + "'");
               }
            }
         }

         if (!info.commandsOnClick.isEmpty()) {
            var10 = info.commandsOnClick.iterator();

            while(var10.hasNext()) {
               command = (String)var10.next();
               command = command.trim();
               command = command.replace("<npcid>", String.valueOf(npcId));
               boolean isPlayerCommand = command.toUpperCase().startsWith("[PLAYER]");
               if (isPlayerCommand) {
                  command = command.substring(8);
               }

               CommandTrait commandTrait = (CommandTrait)npc.getOrAddTrait(CommandTrait.class);
               NPCCommandBuilder cmdBuilder = new NPCCommandBuilder(command, Hand.RIGHT);
               cmdBuilder.op(true);
               if (isPlayerCommand) {
                  cmdBuilder.player(true);
               }

               commandTrait.addCommand(cmdBuilder);
               commandTrait.setExecutionMode(info.commandsSequential ? ExecutionMode.SEQUENTIAL : ExecutionMode.LINEAR);
               if (this.plugin.isDebug()) {
                  this.plugin.getLogger().info("[DEBUG] Set on-click command for NPC: '" + command + "'");
               }
            }
         }

         if (type == EntityType.PLAYER && info.skinUrl != null && !info.skinUrl.isEmpty()) {
            this.changeSkin(npc, info.skinUrl);
         }

         npc.setBukkitEntityType(EntityType.valueOf(info.entityType));
      }
   }

   private void changeSkin(NPC npc, String url) {
      Map<String, Object> skinData = (Map)skinDataCache.get(url);
      if (skinData == null) {
         skinData = this.downloadFromMineskinOrg(url);
         if (skinData == null) {
            skinDataCache.put(url, INVALID);
         } else {
            skinDataCache.put(url, skinData);
         }
      }

      if (skinData != null && skinData != INVALID) {
         try {
            Map<String, Object> data = (Map)skinData.get("data");
            String uuid = (String)data.get("uuid");
            Map<String, Object> texture = (Map)data.get("texture");
            String textureEncoded = (String)texture.get("value");
            String signature = (String)texture.get("signature");
            SkinTrait trait = (SkinTrait)npc.getOrAddTrait(SkinTrait.class);
            trait.setSkinPersistent(uuid, signature, textureEncoded);
         } catch (Exception var10) {
            this.plugin.getLogger().warning("Failed to set skin for " + npc.getName() + ", probably invalid / corrupt skin data.");
            if (this.plugin.isDebug()) {
               var10.printStackTrace();
            }
         }
      } else {
         this.plugin.getLogger().warning("Failed to retrieve skin for npc: " + npc.getName());
      }

   }

   private Map<String, Object> downloadFromMineskinOrg(String url) {
      DataOutputStream out = null;
      BufferedReader reader = null;

      try {
         if (this.plugin.isDebug()) {
            this.plugin.getLogger().info("[DEBUG] Downloading NPC skin: " + url + " from ");
         }

         URL target = new URL("https://api.mineskin.org/generate/url");
         HttpURLConnection con = (HttpURLConnection)target.openConnection();
         con.setRequestMethod("POST");
         con.setDoOutput(true);
         con.setConnectTimeout(1000);
         con.setReadTimeout(30000);
         out = new DataOutputStream(con.getOutputStream());
         out.writeBytes("url=" + URLEncoder.encode(url, "UTF-8"));
         out.close();
         reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
         Map<String, Object> skinInfo = (Map)(new Gson()).fromJson(reader, Map.class);
         con.disconnect();
         Map var7 = skinInfo;
         return var7;
      } catch (Exception var21) {
         this.plugin.getLogger().warning("Failed to download NPC skin: " + url + ".");
         if (this.plugin.isDebug()) {
            var21.printStackTrace();
         }
      } finally {
         if (out != null) {
            try {
               out.close();
            } catch (IOException var20) {
            }
         }

         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var19) {
            }
         }

      }

      return null;
   }
}
