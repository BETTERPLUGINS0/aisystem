package me.SuperRonanCraft.BetterRTP.player.rtp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.PermissionGroup;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldCustom;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldDefault;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class RTPLoader {
   static void loadWorlds(@NotNull WorldDefault defaultWorld, @NotNull HashMap<String, RTPWorld> customWorlds) {
      defaultWorld.load();
      customWorlds.clear();
      BetterRTP.debug("Loading Custom Worlds...");

      try {
         FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
         List<Map<?, ?>> map = config.getMapList("CustomWorlds");
         Iterator var4 = map.iterator();

         while(var4.hasNext()) {
            Map<?, ?> m = (Map)var4.next();
            Iterator var6 = m.entrySet().iterator();

            while(var6.hasNext()) {
               Entry<?, ?> entry = (Entry)var6.next();
               String world = entry.getKey().toString();
               AtomicBoolean exists = new AtomicBoolean(false);
               Bukkit.getWorlds().forEach((w) -> {
                  if (w.getName().equals(world)) {
                     exists.set(true);
                  }

               });
               if (exists.get()) {
                  BetterRTP.debug("Custom World '" + world + "' registered:");
                  customWorlds.put(world, new WorldCustom(Bukkit.getWorld(world)));
               } else {
                  BetterRTP.debug("[WARN] - Custom World '" + world + "' was not registered because world does NOT exist");
               }
            }
         }
      } catch (Exception var10) {
      }

   }

   static void loadOverrides(@NotNull HashMap<String, String> overriden) {
      BetterRTP.debug("Loading Overrides...");
      overriden.clear();

      try {
         FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
         List<Map<?, ?>> override_map = config.getMapList("Overrides");
         Iterator var3 = override_map.iterator();

         while(var3.hasNext()) {
            Map<?, ?> m = (Map)var3.next();
            Iterator var5 = m.entrySet().iterator();

            while(var5.hasNext()) {
               Entry<?, ?> entry = (Entry)var5.next();
               overriden.put(entry.getKey().toString(), entry.getValue().toString());
               if (getPl().getSettings().isDebug()) {
                  getPl().getLogger().info("- Override '" + entry.getKey() + "' -> '" + entry.getValue() + "' added");
               }

               if (Bukkit.getWorld(entry.getValue().toString()) == null) {
                  getPl().getLogger().warning("The world `" + entry.getValue() + "` doesn't seem to exist! Please update `" + entry.getKey() + "'s` override! Maybe there are capital letters?");
               }
            }
         }
      } catch (Exception var7) {
      }

   }

   static void loadWorldTypes(@NotNull HashMap<String, WORLD_TYPE> world_type) {
      BetterRTP.debug("Loading World Types...");
      world_type.clear();

      try {
         FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
         List<Map<?, ?>> world_map = config.getMapList("WorldType");
         Iterator var3 = world_map.iterator();

         while(var3.hasNext()) {
            Map<?, ?> m = (Map)var3.next();
            Iterator var5 = m.entrySet().iterator();

            while(var5.hasNext()) {
               Entry entry = (Entry)var5.next();

               try {
                  String world = entry.getKey().toString();
                  WORLD_TYPE type = WORLD_TYPE.valueOf(entry.getValue().toString().toUpperCase());
                  world_type.put(world, type);
                  BetterRTP.debug("- World Type for '" + world + "' set to '" + type + "'");
               } catch (IllegalArgumentException var13) {
                  StringBuilder valids = new StringBuilder();
                  WORLD_TYPE[] var9 = WORLD_TYPE.values();
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     WORLD_TYPE type = var9[var11];
                     valids.append(type.name()).append(", ");
                  }

                  valids.replace(valids.length() - 2, valids.length(), "");
                  getPl().getLogger().severe("World Type for '" + entry.getKey() + "' is INVALID '" + entry.getValue() + "'. Valid ID's are: " + valids);
               }
            }
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      }

   }

   static void loadLocations(@NotNull HashMap<String, RTPWorld> worlds) {
      worlds.clear();
      FileOther.FILETYPE config = FileOther.FILETYPE.LOCATIONS;
      if (BetterRTP.getInstance().getSettings().isLocationEnabled()) {
         BetterRTP.debug("Loading Locations...");
         List<Map<?, ?>> map = config.getMapList("Locations");
         Iterator var3 = map.iterator();

         while(var3.hasNext()) {
            Map<?, ?> m = (Map)var3.next();
            Iterator var5 = m.entrySet().iterator();

            while(var5.hasNext()) {
               Entry<?, ?> entry = (Entry)var5.next();
               WorldLocation location = new WorldLocation(entry.getKey().toString());
               if (location.isValid()) {
                  worlds.put(entry.getKey().toString(), location);
                  BetterRTP.debug("- Location '" + entry.getKey() + "' registered");
               }
            }
         }

      }
   }

   static void loadPermissionGroups(@NotNull HashMap<String, PermissionGroup> permissionGroup) {
      permissionGroup.clear();
      FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
      if (getPl().getSettings().isPermissionGroupEnabled()) {
         BetterRTP.debug("Loading Permission Groups...");

         try {
            List<Map<?, ?>> map = config.getMapList("PermissionGroup.Groups");
            Iterator var3 = map.iterator();

            while(var3.hasNext()) {
               Map<?, ?> m = (Map)var3.next();
               Iterator var5 = m.entrySet().iterator();

               while(var5.hasNext()) {
                  Entry<?, ?> entry = (Entry)var5.next();
                  String group = entry.getKey().toString();
                  permissionGroup.put(group, new PermissionGroup(entry));
               }
            }
         } catch (Exception var8) {
         }

      }
   }

   private static BetterRTP getPl() {
      return BetterRTP.getInstance();
   }
}
