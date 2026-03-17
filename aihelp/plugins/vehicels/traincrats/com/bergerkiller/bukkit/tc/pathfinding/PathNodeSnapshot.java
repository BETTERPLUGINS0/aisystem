package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.MessageBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;

public class PathNodeSnapshot implements Comparable<PathNodeSnapshot> {
   private final Set<String> names;
   private final BlockLocation location;
   private final boolean isRailSwitchable;

   public PathNodeSnapshot(Set<String> names, BlockLocation location, boolean isRailSwitchable) {
      this.names = names;
      this.location = location;
      this.isRailSwitchable = isRailSwitchable;
   }

   public String getDisplayName() {
      return PathNode.formatDisplayName(this.location, this.names);
   }

   public Set<String> getNames() {
      return this.names;
   }

   public BlockLocation getRailLocation() {
      return this.location;
   }

   public String getWorldName() {
      return this.location.world;
   }

   public boolean containsSwitcher() {
      return this.isRailSwitchable;
   }

   public MessageBuilder getUpdateMessage(PathNode node) {
      if (!this.isRailSwitchable && this.names.isEmpty()) {
         return null;
      } else if (node == null) {
         MessageBuilder message = new MessageBuilder();
         this.appendInfo(message, ChatColor.RED);
         message.red(new Object[]{" was removed"});
         return message;
      } else {
         List<String> removedNames = Collections.emptyList();
         List<String> addedNames = Collections.emptyList();
         Iterator var4 = this.names.iterator();

         String newName;
         while(var4.hasNext()) {
            newName = (String)var4.next();
            if (!node.containsName(newName)) {
               if (((List)removedNames).isEmpty()) {
                  removedNames = new ArrayList();
               }

               ((List)removedNames).add(newName);
            }
         }

         var4 = node.getNames().iterator();

         while(var4.hasNext()) {
            newName = (String)var4.next();
            if (!this.names.contains(newName)) {
               if (((List)addedNames).isEmpty()) {
                  addedNames = new ArrayList();
               }

               ((List)addedNames).add(newName);
            }
         }

         MessageBuilder message;
         if (node.getRailLocation().equals(this.getRailLocation())) {
            if (((List)removedNames).isEmpty() && ((List)addedNames).isEmpty()) {
               return null;
            } else {
               message = new MessageBuilder();
               this.appendInfo(message, ChatColor.YELLOW);
               message.yellow(new Object[]{" was changed:"});
               if (!((List)removedNames).isEmpty()) {
                  message.newLine().yellow(new Object[]{" - "}).red(new Object[]{"Destinations removed: "});
                  appendNames(message, ChatColor.RED, (Collection)removedNames);
               }

               if (!((List)addedNames).isEmpty()) {
                  message.newLine().yellow(new Object[]{" - "}).green(new Object[]{"Destinations added: "});
                  appendNames(message, ChatColor.GREEN, (Collection)addedNames);
               }

               return message;
            }
         } else if (!this.names.isEmpty()) {
            message = new MessageBuilder();
            this.appendInfo(message, ChatColor.YELLOW);
            message.yellow(new Object[]{" was moved:"});
            message.newLine().yellow(new Object[]{" - Now at: "});
            appendLocation(message, node.getRailLocation());
            if (!((List)removedNames).isEmpty()) {
               message.newLine().yellow(new Object[]{" - "}).red(new Object[]{"Destinations removed: "});
               appendNames(message, ChatColor.RED, (Collection)removedNames);
            }

            if (!((List)addedNames).isEmpty()) {
               message.newLine().yellow(new Object[]{" - "}).green(new Object[]{"Destinations added: "});
               appendNames(message, ChatColor.GREEN, (Collection)addedNames);
            }

            return message;
         } else {
            return null;
         }
      }
   }

   public int compareTo(PathNodeSnapshot pathNodeSnapshot) {
      int comp;
      if ((comp = Integer.compare(this.names.size(), pathNodeSnapshot.names.size())) != 0) {
         return comp;
      } else if ((comp = Boolean.compare(this.isRailSwitchable, pathNodeSnapshot.isRailSwitchable)) != 0) {
         return comp;
      } else {
         return this.names.size() == 1 ? ((String)this.names.iterator().next()).compareTo((String)pathNodeSnapshot.names.iterator().next()) : 0;
      }
   }

   public int hashCode() {
      return this.location.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PathNodeSnapshot)) {
         return false;
      } else {
         PathNodeSnapshot other = (PathNodeSnapshot)o;
         return this.names.equals(other.names) && this.location.equals(other.location) && this.isRailSwitchable == other.isRailSwitchable;
      }
   }

   public String toString() {
      return "PathNodeSnapshot{rail=" + this.location + ", names=" + this.names + "}";
   }

   private void appendInfo(MessageBuilder message, ChatColor color) {
      if (this.isRailSwitchable) {
         message.append(color, new String[]{"Switched "});
      }

      if (this.names.isEmpty()) {
         message.append(color, new String[]{"Node"});
      } else if (this.names.size() == 1) {
         message.append(color, new String[]{"Destination "});
         appendNames(message, color, this.names);
      } else {
         message.append(color, new String[]{"Destinations ["});
         appendNames(message, color, this.names);
         message.append(color, new String[]{"]"});
      }

      message.append(color, new String[]{" at "});
      appendLocation(message, this.location);
   }

   private static void appendLocation(MessageBuilder message, BlockLocation location) {
      message.white(new Object[]{"[", location.x, "/", location.y, "/", location.z, "]"});
   }

   private static void appendNames(MessageBuilder message, ChatColor color, Collection<String> names) {
      boolean first = true;

      String name;
      for(Iterator var4 = names.iterator(); var4.hasNext(); message.white(new Object[]{name})) {
         name = (String)var4.next();
         if (first) {
            first = false;
         } else {
            message.append(color, new String[]{", "});
         }
      }

   }
}
