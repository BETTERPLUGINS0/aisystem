package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPermissionGroup;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class PermissionGroup {
   String groupName;
   private final HashMap<String, WorldPermissionGroup> worlds = new HashMap();

   public PermissionGroup(Entry<?, ?> fields) {
      this.groupName = fields.getKey().toString();
      BetterRTP.debug("- Permission Group: " + this.groupName);
      Object value = fields.getValue();
      Iterator var3 = ((ArrayList)value).iterator();

      while(var3.hasNext()) {
         Object worldList = var3.next();
         Iterator var5 = ((HashMap)worldList).entrySet().iterator();

         while(var5.hasNext()) {
            Object hash = var5.next();
            Entry worldFields = (Entry)hash;
            BetterRTP.debug("- -- World: " + worldFields.getKey());
            World world = Bukkit.getWorld(worldFields.getKey().toString());
            if (world != null) {
               WorldPermissionGroup permissionGroup = new WorldPermissionGroup(this.groupName, world, worldFields);
               this.worlds.put(worldFields.getKey().toString(), permissionGroup);
            } else {
               BetterRTP.debug("- - The Permission Group '" + this.groupName + "'s world '" + worldFields.getKey() + "' does not exist! Permission Group not loaded...");
            }
         }
      }

   }

   public HashMap<String, WorldPermissionGroup> getWorlds() {
      return this.worlds;
   }
}
