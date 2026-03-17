package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class OfflineGroupWorld implements Iterable<OfflineGroup> {
   protected final OfflineWorld world;

   public OfflineGroupWorld(OfflineWorld world) {
      this.world = world;
   }

   public abstract Collection<OfflineGroup> getGroups();

   public OfflineWorld getWorld() {
      return this.world;
   }

   public Iterator<OfflineGroup> iterator() {
      return this.getGroups().iterator();
   }

   public boolean isEmpty() {
      return this.getGroups().isEmpty();
   }

   public int totalGroupCount() {
      return this.getGroups().size();
   }

   public int totalMemberCount() {
      int count = 0;

      OfflineGroup group;
      for(Iterator var2 = this.getGroups().iterator(); var2.hasNext(); count += group.members.length) {
         group = (OfflineGroup)var2.next();
      }

      return count;
   }

   public static OfflineGroupWorld snapshot(OfflineWorld world, Collection<OfflineGroup> groups) {
      final List<OfflineGroup> snapshotGroups = Collections.unmodifiableList(new ArrayList(groups));
      return new OfflineGroupWorld(world) {
         public Collection<OfflineGroup> getGroups() {
            return snapshotGroups;
         }
      };
   }

   public static List<OfflineGroupWorld> snapshot(Map<OfflineWorld, List<OfflineGroup>> groupsByWorld) {
      List<OfflineGroupWorld> worldsList = new ArrayList(groupsByWorld.size());
      Iterator var2 = groupsByWorld.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<OfflineWorld, List<OfflineGroup>> entry = (Entry)var2.next();
         worldsList.add(snapshot((OfflineWorld)entry.getKey(), (Collection)entry.getValue()));
      }

      return Collections.unmodifiableList(worldsList);
   }

   public static List<OfflineGroupWorld> mergeSnapshots(List<OfflineGroupWorld> first, List<OfflineGroupWorld> second) {
      if (first.isEmpty()) {
         return second;
      } else if (second.isEmpty()) {
         return first;
      } else {
         Map<OfflineWorld, List<OfflineGroup>> merged = new IdentityHashMap(Math.max(first.size(), second.size()));
         Iterator var3 = first.iterator();

         OfflineGroupWorld world;
         while(var3.hasNext()) {
            world = (OfflineGroupWorld)var3.next();
            ((List)merged.computeIfAbsent(world.getWorld(), (w) -> {
               return new ArrayList(world.getGroups().size());
            })).addAll(world.getGroups());
         }

         var3 = second.iterator();

         while(var3.hasNext()) {
            world = (OfflineGroupWorld)var3.next();
            ((List)merged.computeIfAbsent(world.getWorld(), (w) -> {
               return new ArrayList(world.getGroups().size());
            })).addAll(world.getGroups());
         }

         return snapshot(merged);
      }
   }
}
