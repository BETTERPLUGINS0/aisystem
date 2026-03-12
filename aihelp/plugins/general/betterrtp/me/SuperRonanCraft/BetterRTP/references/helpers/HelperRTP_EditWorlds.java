package me.SuperRonanCraft.BetterRTP.references.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdEdit;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class HelperRTP_EditWorlds {
   public static void editCustomWorld(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String world, String val) {
      String path = "CustomWorlds";
      if (editSingleMap(sendi, cmd, world, val, path, FileOther.FILETYPE.CONFIG)) {
         BetterRTP.getInstance().getRTP().loadWorlds();
      }

   }

   public static void editLocation(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String location, String val) {
      String path = "Locations";
      if (editSingleMap(sendi, cmd, location, val, path, FileOther.FILETYPE.LOCATIONS)) {
         BetterRTP.getInstance().getRTP().loadLocations();
      }

   }

   private static boolean editSingleMap(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String field, String val, String path, FileOther.FILETYPE file) {
      Object value;
      try {
         value = cmd.getResult(val);
      } catch (Exception var16) {
         var16.printStackTrace();
         MessagesCore.EDIT_ERROR.send(sendi);
         return false;
      }

      if (value == null) {
         MessagesCore.EDIT_ERROR.send(sendi);
         return false;
      } else {
         YamlConfiguration config = file.getConfig();
         List<Map<?, ?>> map = config.getMapList(path);
         boolean found = false;
         Iterator var10 = map.iterator();

         label44:
         while(var10.hasNext()) {
            Map<?, ?> m = (Map)var10.next();
            if (m.keySet().toArray()[0].equals(field)) {
               found = true;
               Iterator var12 = m.values().iterator();

               while(true) {
                  if (!var12.hasNext()) {
                     break label44;
                  }

                  Object map2 = var12.next();
                  Map<Object, Object> values = (Map)map2;
                  values.put(cmd.get(), value);
                  Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", cmd.get()).replace("%value%", val));
               }
            }
         }

         if (!found) {
            Map<Object, Object> map2 = new HashMap();
            Map<Object, Object> values = new HashMap();
            values.put(cmd.get(), value);
            map2.put(field, values);
            map.add(map2);
         }

         config.set(path, map);

         try {
            config.save(file.getFile());
            return true;
         } catch (IOException var15) {
            var15.printStackTrace();
            return false;
         }
      }
   }

   public static void editPermissionGroup(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String group, String world, String val) {
      Object value;
      try {
         value = cmd.getResult(val);
      } catch (Exception var23) {
         var23.printStackTrace();
         MessagesCore.EDIT_ERROR.send(sendi);
         return;
      }

      if (value == null) {
         MessagesCore.EDIT_ERROR.send(sendi);
      } else {
         String path = "PermissionGroup.Groups";
         FileOther.FILETYPE file = FileOther.FILETYPE.CONFIG;
         YamlConfiguration config = file.getConfig();
         List<Map<?, ?>> map = config.getMapList(path);
         Iterator var10 = map.iterator();

         label61:
         while(var10.hasNext()) {
            Map<?, ?> m = (Map)var10.next();
            Iterator var12 = m.entrySet().iterator();

            while(true) {
               Entry entry;
               String _group;
               do {
                  if (!var12.hasNext()) {
                     continue label61;
                  }

                  entry = (Entry)var12.next();
                  _group = entry.getKey().toString();
               } while(!_group.equals(group));

               BetterRTP.getInstance().getLogger().info("Group: " + group);
               Object _value = entry.getValue();
               Iterator var16 = ((ArrayList)_value).iterator();

               while(var16.hasNext()) {
                  Object worldList = var16.next();
                  BetterRTP.getInstance().getLogger().info("World: " + worldList.toString());
                  Iterator var18 = ((HashMap)worldList).entrySet().iterator();

                  while(var18.hasNext()) {
                     Object hash = var18.next();
                     Entry worldFields = (Entry)hash;
                     BetterRTP.getInstance().getLogger().info("Hash: " + hash);
                     if (world.equals(worldFields.getKey().toString())) {
                        Map<Object, Object> values = (Map)worldFields.getValue();
                        values.put(cmd.get(), value);
                        Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", cmd.get()).replace("%value%", val));
                     }
                  }
               }
            }
         }

         config.set(path, map);

         try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().loadPermissionGroups();
         } catch (IOException var22) {
            var22.printStackTrace();
         }

      }
   }

   public static void editDefault(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String val) {
      Object value;
      try {
         value = cmd.getResult(val);
      } catch (Exception var8) {
         var8.printStackTrace();
         MessagesCore.EDIT_ERROR.send(sendi);
         return;
      }

      if (value == null) {
         MessagesCore.EDIT_ERROR.send(sendi);
      } else {
         FileOther.FILETYPE file = FileOther.FILETYPE.CONFIG;
         YamlConfiguration config = file.getConfig();
         config.set("Default." + cmd.get(), value);

         try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().loadWorlds();
            Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", cmd.get()).replace("%value%", val));
         } catch (IOException var7) {
            var7.printStackTrace();
         }

      }
   }

   public static void editWorldtype(CommandSender sendi, String world, String val) {
      WORLD_TYPE type;
      try {
         type = WORLD_TYPE.valueOf(val.toUpperCase());
      } catch (Exception var13) {
         MessagesCore.EDIT_ERROR.send(sendi);
         return;
      }

      FileOther.FILETYPE file = FileOther.FILETYPE.CONFIG;
      YamlConfiguration config = file.getConfig();
      List<Map<?, ?>> world_map = config.getMapList("WorldType");
      List<Map<?, ?>> removeList = new ArrayList();
      Iterator var8 = world_map.iterator();

      Map m;
      while(var8.hasNext()) {
         m = (Map)var8.next();
         Iterator var10 = m.entrySet().iterator();

         while(var10.hasNext()) {
            Entry<?, ?> entry = (Entry)var10.next();
            if (entry.getKey().equals(world)) {
               removeList.add(m);
            }
         }
      }

      var8 = removeList.iterator();

      while(var8.hasNext()) {
         m = (Map)var8.next();
         world_map.remove(m);
      }

      Map<String, String> newIndex = new HashMap();
      newIndex.put(world, type.name());
      world_map.add(newIndex);
      config.set("WorldType", world_map);

      try {
         config.save(file.getFile());
         BetterRTP.getInstance().getRTP().load();
         Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", CmdEdit.RTP_CMD_EDIT.WORLD_TYPE.name()).replace("%value%", val));
      } catch (IOException var12) {
         var12.printStackTrace();
      }

   }

   public static void editOverride(CommandSender sendi, String world, String val) {
      FileOther.FILETYPE file = FileOther.FILETYPE.CONFIG;
      YamlConfiguration config = file.getConfig();
      List<Map<?, ?>> world_map = config.getMapList("Overrides");
      List<Map<?, ?>> removeList = new ArrayList();
      Iterator var7 = world_map.iterator();

      Map m;
      while(var7.hasNext()) {
         m = (Map)var7.next();
         Iterator var9 = m.entrySet().iterator();

         while(var9.hasNext()) {
            Entry<?, ?> entry = (Entry)var9.next();
            if (entry.getKey().equals(world)) {
               removeList.add(m);
            }
         }
      }

      var7 = removeList.iterator();

      while(var7.hasNext()) {
         m = (Map)var7.next();
         world_map.remove(m);
      }

      if (!val.equals("REMOVE_OVERRIDE")) {
         Map<String, String> newIndex = new HashMap();
         newIndex.put(world, val);
         world_map.add(newIndex);
      } else {
         val = "(removed override)";
      }

      config.set("Overrides", world_map);

      try {
         config.save(file.getFile());
         BetterRTP.getInstance().getRTP().load();
         Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", CmdEdit.RTP_CMD_EDIT.OVERRIDE.name()).replace("%value%", val));
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public static void editBlacklisted(CommandSender sendi, String block, boolean add) {
      FileOther.FILETYPE file = FileOther.FILETYPE.CONFIG;
      YamlConfiguration config = file.getConfig();
      List<String> world_map = config.getStringList("BlacklistedBlocks");
      List<String> removeList = new ArrayList();
      Iterator var7 = world_map.iterator();

      String o;
      while(var7.hasNext()) {
         o = (String)var7.next();
         if (o.equals(block)) {
            removeList.add(o);
         }
      }

      var7 = removeList.iterator();

      while(var7.hasNext()) {
         o = (String)var7.next();
         world_map.remove(o);
      }

      if (add) {
         world_map.add(block);
      } else {
         block = "(removed " + block + ")";
      }

      config.set("BlacklistedBlocks", world_map);

      try {
         config.save(file.getFile());
         BetterRTP.getInstance().getRTP().load();
         Message_RTP.sms(sendi, MessagesCore.EDIT_SET.get(sendi, (Object)null).replace("%type%", CmdEdit.RTP_CMD_EDIT.BLACKLISTEDBLOCKS.name()).replace("%value%", block));
      } catch (IOException var9) {
         var9.printStackTrace();
      }

   }
}
