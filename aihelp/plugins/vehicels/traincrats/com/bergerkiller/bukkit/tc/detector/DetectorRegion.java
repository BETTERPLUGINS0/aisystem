package com.bergerkiller.bukkit.tc.detector;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.collections.BlockMap;
import com.bergerkiller.bukkit.common.collections.ImplicitlySharedList;
import com.bergerkiller.bukkit.common.config.DataReader;
import com.bergerkiller.bukkit.common.config.DataWriter;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.WorldRailLookup;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class DetectorRegion {
   private static boolean hasChanges = false;
   private static HashMap<UUID, DetectorRegion> regionsById = new HashMap();
   private static BlockMap<DetectorRegion[]> regions = new BlockMap();
   private final UUID id;
   private final String world;
   private final Set<IntVector3> coordinates;
   private final Set<MinecartMember<?>> members;
   private final ImplicitlySharedList<DetectorListener> listeners;

   private DetectorRegion(UUID uniqueId, String world, Set<IntVector3> coordinates) {
      this.members = new HashSet();
      this.listeners = new ImplicitlySharedList();
      this.world = world;
      this.id = uniqueId;
      this.coordinates = coordinates;
      regionsById.put(this.id, this);
      hasChanges = true;
      WorldRailLookup lookup = RailLookup.forWorldIfInitialized(Bukkit.getWorld(world));
      DetectorRegion[] singleRegion = new DetectorRegion[]{this};
      Iterator var6 = this.coordinates.iterator();

      while(var6.hasNext()) {
         IntVector3 coord = (IntVector3)var6.next();
         BlockLocation block_coord = new BlockLocation(world, coord);
         DetectorRegion[] regionsAtBlock = (DetectorRegion[])regions.compute(block_coord, (key, array) -> {
            if (array == null) {
               return singleRegion;
            } else {
               int len = array.length;
               array = (DetectorRegion[])Arrays.copyOf(array, len + 1);
               array[len] = this;
               return array;
            }
         });
         if (lookup.isValid()) {
            lookup.storeDetectorRegions(coord, regionsAtBlock);
         }
      }

   }

   public void detectMinecarts() {
      World w = Bukkit.getServer().getWorld(this.world);
      if (w != null) {
         WorldRailLookup railLookup = RailLookup.forWorld(w);
         Iterator var3 = this.coordinates.iterator();

         while(true) {
            List members;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               IntVector3 coord = (IntVector3)var3.next();
               members = railLookup.findMembersOnRail(coord);
            } while(members.isEmpty());

            Iterator var6 = (new ArrayList(members)).iterator();

            while(var6.hasNext()) {
               MinecartMember<?> mm = (MinecartMember)var6.next();
               mm.getSignTracker().addToDetectorRegion(this);
            }
         }
      }
   }

   public static void fillRailLookup(WorldRailLookup railLookup) {
      String worldName = railLookup.getWorld().getName();
      Iterator var2 = regions.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<BlockLocation, DetectorRegion[]> entry = (Entry)var2.next();
         BlockLocation block = (BlockLocation)entry.getKey();
         if (worldName.equals(block.world)) {
            railLookup.storeDetectorRegions(block.getCoordinates(), (DetectorRegion[])entry.getValue());
         }
      }

   }

   public static List<DetectorRegion> getRegions(Block at) {
      DetectorRegion[] regionsAtBlock = (DetectorRegion[])regions.get(at);
      return regionsAtBlock == null ? Collections.emptyList() : Arrays.asList(regionsAtBlock);
   }

   public static void detectAllMinecarts() {
      Iterator var0 = regionsById.values().iterator();

      while(var0.hasNext()) {
         DetectorRegion region = (DetectorRegion)var0.next();
         region.detectMinecarts();
      }

   }

   public static DetectorRegion create(Collection<Block> blocks) {
      if (blocks.isEmpty()) {
         return null;
      } else {
         World world = null;
         Set<IntVector3> coords = new HashSet(blocks.size());
         Iterator var3 = blocks.iterator();

         while(true) {
            Block b;
            do {
               if (!var3.hasNext()) {
                  return create((World)world, coords);
               }

               b = (Block)var3.next();
               if (world == null) {
                  world = b.getWorld();
                  break;
               }
            } while(world != b.getWorld());

            coords.add(new IntVector3(b));
         }
      }
   }

   public static DetectorRegion create(World world, Set<IntVector3> coordinates) {
      return create(world.getName(), coordinates);
   }

   public static DetectorRegion create(String world, Set<IntVector3> coordinates) {
      Iterator var2 = coordinates.iterator();
      if (var2.hasNext()) {
         IntVector3 coord = (IntVector3)var2.next();
         DetectorRegion[] list = (DetectorRegion[])regions.get(world, coord);
         if (list != null) {
            DetectorRegion[] var5 = list;
            int var6 = list.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               DetectorRegion region = var5[var7];
               if (region.coordinates.containsAll(coordinates) && coordinates.containsAll(region.coordinates)) {
                  return region;
               }
            }
         }
      }

      return new DetectorRegion(UUID.randomUUID(), world, coordinates);
   }

   public static DetectorRegion getRegion(UUID uniqueId) {
      return (DetectorRegion)regionsById.get(uniqueId);
   }

   public static void init(final TrainCarts plugin) {
      regionsById.clear();
      regions.clear();
      (new DataReader(plugin, "detectorregions.dat") {
         public void read(DataInputStream stream) throws IOException {
            for(int count = stream.readInt(); count > 0; --count) {
               UUID id = StreamUtil.readUUID(stream);
               String world = stream.readUTF();
               int coordcount = stream.readInt();

               HashSet coords;
               for(coords = new HashSet(coordcount); coordcount > 0; --coordcount) {
                  coords.add(IntVector3.read(stream));
               }

               new DetectorRegion(id, world, coords);
            }

            if (DetectorRegion.regionsById.size() == 1) {
               plugin.log(Level.INFO, DetectorRegion.regionsById.size() + " detector rail region loaded covering " + DetectorRegion.regions.size() + " blocks");
            } else {
               plugin.log(Level.INFO, DetectorRegion.regionsById.size() + " detector rail regions loaded covering " + DetectorRegion.regions.size() + " blocks");
            }

         }
      }).read();
      hasChanges = false;
   }

   public static void save(TrainCarts plugin, boolean autosave) {
      if (!autosave || hasChanges) {
         (new DataWriter(plugin, "detectorregions.dat") {
            public void write(DataOutputStream stream) throws IOException {
               stream.writeInt(DetectorRegion.regionsById.size());
               Iterator var2 = DetectorRegion.regionsById.values().iterator();

               while(var2.hasNext()) {
                  DetectorRegion region = (DetectorRegion)var2.next();
                  StreamUtil.writeUUID(stream, region.id);
                  stream.writeUTF(region.world);
                  stream.writeInt(region.coordinates.size());
                  Iterator var4 = region.coordinates.iterator();

                  while(var4.hasNext()) {
                     IntVector3 coord = (IntVector3)var4.next();
                     coord.write(stream);
                  }
               }

            }
         }).write();
         hasChanges = false;
      }
   }

   public String getWorldName() {
      return this.world;
   }

   public Set<IntVector3> getCoordinates() {
      return this.coordinates;
   }

   private void cleanUnloadedMembers() {
      Iterator iter = this.members.iterator();

      while(iter.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)iter.next();
         if (mm.isUnloaded()) {
            iter.remove();
            Block pos = null;
            if (mm.getEntity() != null) {
               pos = ((CommonMinecart)mm.getEntity()).getLocation().getBlock();
            } else {
               pos = mm.getRailTracker().getBlock();
               if (pos == null) {
                  pos = mm.getRailTracker().getLastBlock();
               }
            }

            String posStr = "Unknown";
            if (pos != null) {
               posStr = "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
            }

            mm.getTrainCarts().getLogger().warning("[Detector] Purged unloaded Minecart at " + posStr);
         }
      }

   }

   public Set<MinecartMember<?>> getMembers() {
      return this.members;
   }

   public boolean hasMembers() {
      return !this.members.isEmpty();
   }

   public Set<MinecartGroup> getGroups() {
      Set<MinecartGroup> rval = new HashSet();
      this.cleanUnloadedMembers();
      Iterator var2 = this.members.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var2.next();
         if (mm.getGroup() != null) {
            rval.add(mm.getGroup());
         }
      }

      return rval;
   }

   public boolean hasGroups() {
      return !this.members.isEmpty();
   }

   public UUID getUniqueId() {
      return this.id;
   }

   public void register(DetectorListener listener) {
      this.listeners.add(listener);
      listener.onRegister(this);
      Iterator var2 = this.members.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var2.next();
         listener.onEnter(mm);
      }

      var2 = this.getGroups().iterator();

      while(var2.hasNext()) {
         MinecartGroup group = (MinecartGroup)var2.next();
         listener.onEnter(group);
      }

   }

   public void unregister(DetectorListener listener) {
      this.listeners.remove(listener);
      Iterator var2 = this.members.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var2.next();
         listener.onLeave(mm);
      }

      var2 = this.getGroups().iterator();

      while(var2.hasNext()) {
         MinecartGroup group = (MinecartGroup)var2.next();
         listener.onLeave(group);
      }

      listener.onUnregister(this);
   }

   public boolean isRegistered() {
      return !this.listeners.isEmpty();
   }

   private void onLeave(MinecartMember<?> mm) {
      Iterator var2 = this.listeners.cloneAsIterable().iterator();

      while(var2.hasNext()) {
         DetectorListener listener = (DetectorListener)var2.next();
         listener.onLeave(mm);
      }

      if (!mm.isUnloaded()) {
         this.cleanUnloadedMembers();
         MinecartGroup group = mm.getGroup();
         Iterator var6 = this.members.iterator();

         while(var6.hasNext()) {
            MinecartMember<?> ex = (MinecartMember)var6.next();
            if (ex != mm && ex.getGroup() == group) {
               return;
            }
         }

         var6 = this.listeners.cloneAsIterable().iterator();

         while(var6.hasNext()) {
            DetectorListener listener = (DetectorListener)var6.next();
            listener.onLeave(group);
         }

      }
   }

   private void onEnter(MinecartMember<?> mm) {
      Iterator var2 = this.listeners.cloneAsIterable().iterator();

      while(var2.hasNext()) {
         DetectorListener listener = (DetectorListener)var2.next();
         listener.onEnter(mm);
      }

      if (!mm.isUnloaded()) {
         this.cleanUnloadedMembers();
         MinecartGroup group = mm.getGroup();
         Iterator var6 = this.members.iterator();

         while(var6.hasNext()) {
            MinecartMember<?> ex = (MinecartMember)var6.next();
            if (ex != mm && ex.getGroup() == group) {
               return;
            }
         }

         var6 = this.listeners.cloneAsIterable().iterator();

         while(var6.hasNext()) {
            DetectorListener listener = (DetectorListener)var6.next();
            listener.onEnter(group);
         }

      }
   }

   public void unload(MinecartGroup group) {
      if (this.members.removeAll(group)) {
         Iterator var2 = this.listeners.cloneAsIterable().iterator();

         while(var2.hasNext()) {
            DetectorListener listener = (DetectorListener)var2.next();
            listener.onUnload(group);
         }
      }

   }

   public void remove(MinecartMember<?> mm) {
      if (this.members.remove(mm)) {
         this.onLeave(mm);
      }

   }

   public boolean add(MinecartMember<?> mm) {
      if (this.members.add(mm)) {
         this.onEnter(mm);
         return true;
      } else {
         return false;
      }
   }

   public void update(MinecartMember<?> member) {
      Iterator var2 = this.listeners.cloneAsIterable().iterator();

      while(var2.hasNext()) {
         DetectorListener list = (DetectorListener)var2.next();
         list.onUpdate(member);
      }

   }

   public void update(MinecartGroup group) {
      Iterator var2 = this.listeners.cloneAsIterable().iterator();

      while(var2.hasNext()) {
         DetectorListener list = (DetectorListener)var2.next();
         list.onUpdate(group);
      }

   }

   public void remove() {
      this.cleanUnloadedMembers();
      Iterator iter = this.members.iterator();

      while(iter.hasNext()) {
         this.onLeave((MinecartMember)iter.next());
         iter.remove();
      }

      regionsById.remove(this.id);
      hasChanges = true;
      WorldRailLookup lookup = RailLookup.forWorldIfInitialized(Bukkit.getWorld(this.world));
      Iterator var3 = this.coordinates.iterator();

      while(var3.hasNext()) {
         IntVector3 coord = (IntVector3)var3.next();
         BlockLocation block_coord = new BlockLocation(this.world, coord);
         DetectorRegion[] regionsAtBlock = (DetectorRegion[])regions.computeIfPresent(block_coord, (key, list) -> {
            return list.length == 1 && list[0] == this ? null : (DetectorRegion[])LogicUtil.removeArrayElement(list, this);
         });
         if (lookup.isValid()) {
            lookup.storeDetectorRegions(coord, regionsAtBlock);
         }
      }

   }

   // $FF: synthetic method
   DetectorRegion(UUID x0, String x1, Set x2, Object x3) {
      this(x0, x1, x2);
   }
}
