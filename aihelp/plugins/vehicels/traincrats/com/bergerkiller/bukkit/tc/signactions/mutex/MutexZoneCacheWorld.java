package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.LongHashMap;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import org.bukkit.World;

public class MutexZoneCacheWorld {
   private static final MutexZone[] NO_ZONES = new MutexZone[0];
   private final OfflineWorld world;
   protected final Map<MutexZoneCacheWorld.SignSidePositionKey, MutexZone> bySignPosition = new HashMap();
   protected final Map<MutexZoneCacheWorld.PathingSignKey, MutexZonePath> byPathingKey = new HashMap();
   private final LongHashMap<MutexZone[]> byChunk = new LongHashMap();
   private final Set<MutexZone> newZonesLive = new HashSet();
   private List<MutexZone> newZones = Collections.emptyList();

   public MutexZoneCacheWorld(OfflineWorld world) {
      this.world = world;
   }

   public World getWorld() {
      return this.world.getLoadedWorld();
   }

   public OfflineWorld getOfflineWorld() {
      return this.world;
   }

   public MutexZoneCacheWorld.MovingPoint track(IntVector3 blockPosition) {
      LongHashMap var10002 = this.byChunk;
      Objects.requireNonNull(var10002);
      return new MutexZoneCacheWorld.MovingPoint(var10002::get, blockPosition.getChunkX(), blockPosition.getChunkZ());
   }

   public MutexZone find(IntVector3 position) {
      MutexZone[] inChunk = (MutexZone[])this.byChunk.get(position.getChunkX(), position.getChunkZ());
      if (inChunk != null) {
         MutexZone[] var3 = inChunk;
         int var4 = inChunk.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MutexZone zone = var3[var5];
            if (zone.containsBlock(position)) {
               return zone;
            }
         }
      }

      return null;
   }

   public MutexZone findBySign(IntVector3 signPosition, boolean signFront) {
      return (MutexZone)this.bySignPosition.get(new MutexZoneCacheWorld.SignSidePositionKey(signPosition, signFront));
   }

   public List<MutexZone> getNewZones() {
      return this.newZones;
   }

   public boolean isMutexZoneNearby(IntVector3 block, int radius) {
      int chunkMinX = MathUtil.toChunk(block.x - radius);
      int chunkMaxX = MathUtil.toChunk(block.x + radius);
      int chunkMinZ = MathUtil.toChunk(block.z - radius);
      int chunkMaxZ = MathUtil.toChunk(block.z + radius);

      for(int cz = chunkMinZ; cz <= chunkMaxZ; ++cz) {
         for(int cx = chunkMinX; cx <= chunkMaxX; ++cx) {
            MutexZone[] zonesAtChunk = (MutexZone[])this.byChunk.get(cx, cz);
            if (zonesAtChunk != null) {
               MutexZone[] var10 = zonesAtChunk;
               int var11 = zonesAtChunk.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  MutexZone zone = var10[var12];
                  if (zone.isNearby(block, radius)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public List<MutexZone> findNearbyZones(IntVector3 block, int radius) {
      List<MutexZone> result = Collections.emptyList();
      int chunkMinX = block.x - radius >> 4;
      int chunkMaxX = block.x + radius >> 4;
      int chunkMinZ = block.z - radius >> 4;
      int chunkMaxZ = block.z + radius >> 4;

      for(int cz = chunkMinZ; cz <= chunkMaxZ; ++cz) {
         for(int cx = chunkMinX; cx <= chunkMaxX; ++cx) {
            MutexZone[] zonesAtChunk = (MutexZone[])this.byChunk.get(cx, cz);
            if (zonesAtChunk != null) {
               MutexZone[] var11 = zonesAtChunk;
               int var12 = zonesAtChunk.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  MutexZone zone = var11[var13];
                  if (zone.isNearby(block, radius)) {
                     if (((List)result).isEmpty()) {
                        result = new ArrayList();
                     }

                     ((List)result).add(zone);
                  }
               }
            }
         }
      }

      return (List)result;
   }

   public void add(MutexZone zone) {
      zone.addToWorld(this);
      this.newZonesLive.add(zone);
      this.mapToChunks(zone, false);
   }

   protected void remove(MutexZone zone) {
      this.newZonesLive.remove(zone);
      int newZoneIdx = this.newZones.indexOf(zone);
      if (newZoneIdx != -1) {
         List<MutexZone> copy = new ArrayList(this.newZones);
         copy.remove(newZoneIdx);
         this.newZones = copy;
      }

      this.unmapFromChunks(zone);
   }

   protected void addNewChunks(MutexZone zone) {
      this.mapToChunks(zone, true);
   }

   private void mapToChunks(MutexZone zone, boolean checkDuplicates) {
      MutexZone[] singleZone = new MutexZone[]{zone};
      zone.forAllContainedChunks((cx, cz) -> {
         long key = MathUtil.longHashToLong(cx, cz);
         MutexZone[] atChunk = (MutexZone[])this.byChunk.get(key);
         if (atChunk == null) {
            this.byChunk.put(key, singleZone);
         } else if (!checkDuplicates || !this.isChunkInArray(atChunk, zone)) {
            int len = atChunk.length;
            atChunk = (MutexZone[])Arrays.copyOf(atChunk, len + 1);
            atChunk[len] = zone;
            this.byChunk.put(key, atChunk);
         }

      });
   }

   private void unmapFromChunks(MutexZone zone) {
      zone.forAllContainedChunks((cx, cz) -> {
         long key = MathUtil.longHashToLong(cx, cz);
         MutexZone[] atChunk = (MutexZone[])this.byChunk.remove(key);
         if (atChunk != null && (atChunk.length > 1 || atChunk[0] != zone)) {
            for(int i = atChunk.length - 1; i >= 0; --i) {
               if (atChunk[i] == zone) {
                  atChunk = (MutexZone[])LogicUtil.removeArrayElement(atChunk, i);
               }
            }

            this.byChunk.put(key, atChunk);
         }

      });
   }

   private boolean isChunkInArray(MutexZone[] zones, MutexZone zone) {
      MutexZone[] var3 = zones;
      int var4 = zones.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         MutexZone zoneInZones = var3[var5];
         if (zoneInZones == zone) {
            return true;
         }
      }

      return false;
   }

   public MutexZone removeAtSign(IntVector3 signPosition, boolean front) {
      MutexZone zone = (MutexZone)this.bySignPosition.remove(new MutexZoneCacheWorld.SignSidePositionKey(signPosition, front));
      if (zone != null) {
         this.remove(zone);
      }

      return zone;
   }

   public MutexZonePath getOrCreatePathingMutex(RailLookup.TrackedSign sign, MinecartGroup group, IntVector3 initialBlock, UnaryOperator<MutexZonePath.OptionsBuilder> optionsBuilder) {
      TrainProperties trainProperties = group.getProperties();
      MutexZonePath path = (MutexZonePath)this.byPathingKey.get(MutexZoneCacheWorld.PathingSignKey.of(sign.getUniqueKey(), trainProperties));
      if (path != null) {
         return path;
      } else {
         path = new MutexZonePath(group.getTrainCarts(), sign, trainProperties, (MutexZonePath.OptionsBuilder)optionsBuilder.apply(MutexZonePath.createOptions()));
         path.addBlock(initialBlock);
         this.add(path);
         return path;
      }
   }

   public void clear() {
      this.bySignPosition.clear();
      this.byPathingKey.clear();
      this.byChunk.clear();
   }

   public void onTick() {
      this.updatePathingMutexes();
      if (this.newZonesLive.isEmpty()) {
         this.newZones = Collections.emptyList();
      } else {
         this.newZones = new ArrayList(this.newZonesLive);
         this.newZonesLive.clear();
      }

   }

   private void updatePathingMutexes() {
      if (!this.byPathingKey.isEmpty()) {
         int expireTick = CommonUtil.getServerTicks() - 2;
         Iterator iter = this.byPathingKey.values().iterator();

         while(iter.hasNext()) {
            MutexZonePath zonePath = (MutexZonePath)iter.next();
            if (zonePath.isExpired(expireTick)) {
               iter.remove();
               this.remove(zonePath);
            }
         }

      }
   }

   public static final class MovingPoint {
      private final MutexZoneCacheWorld.MutexZoneByChunkGetter byChunkGetter;
      private int chunkX;
      private int chunkZ;
      private MutexZone[] chunkZones;

      public MovingPoint(MutexZoneCacheWorld.MutexZoneByChunkGetter byChunkGetter, int chunkX, int chunkZ) {
         this.byChunkGetter = byChunkGetter;
         this.chunkX = chunkX;
         this.chunkZ = chunkZ;
         MutexZone[] zones = byChunkGetter.getAt(chunkX, chunkZ);
         this.chunkZones = zones == null ? MutexZoneCacheWorld.NO_ZONES : zones;
      }

      public MutexZoneCacheWorld.MutexZoneResult get(TrackWalkingPoint walker) {
         RailPath.Position p1 = walker.state.position();
         RailPath.Position p2 = walker.currentRailPath.getEndOfPath(walker.state.railBlock(), p1);
         return this.get(p1, p2);
      }

      public MutexZoneCacheWorld.MutexZoneResult get(RailPath.Position p1, RailPath.Position p2) {
         p1.assertAbsolute();
         p2.assertAbsolute();
         int cx1 = MathUtil.toChunk(p1.posX);
         int cz1 = MathUtil.toChunk(p1.posZ);
         int cx2 = MathUtil.toChunk(p2.posX);
         int cz2 = MathUtil.toChunk(p2.posZ);
         Object zones;
         if (cx1 == cx2 && cz1 == cz2) {
            zones = Arrays.asList(this.findZonesInChunk(cx1, cz1));
         } else {
            zones = Collections.emptyList();
            int cx_step = cx1 > cx2 ? -1 : 1;
            int cz_step = cz1 > cz2 ? -1 : 1;
            int cz = cz1;

            label84:
            while(true) {
               int cx = cx1;

               while(true) {
                  MutexZone[] var12 = this.findZonesInChunk(cx, cz);
                  int var13 = var12.length;

                  for(int var14 = 0; var14 < var13; ++var14) {
                     MutexZone zone = var12[var14];
                     if (((List)zones).isEmpty()) {
                        zones = new ArrayList(4);
                        ((List)zones).add(zone);
                     } else if (!((List)zones).contains(zone)) {
                        ((List)zones).add(zone);
                     }
                  }

                  if (cx == cx2) {
                     if (cz == cz2) {
                        break label84;
                     }

                     cz += cz_step;
                     break;
                  }

                  cx += cx_step;
               }
            }
         }

         if (((List)zones).isEmpty()) {
            return null;
         } else {
            double motX = p2.posX - p1.posX;
            double motY = p2.posY - p1.posY;
            double motZ = p2.posZ - p1.posZ;
            double distance = p2.distance(p1);
            if (distance <= 1.0E-10D) {
               IntVector3 blockPos = new IntVector3(p1.posX, p1.posY, p1.posZ);
               Iterator var17 = ((List)zones).iterator();

               MutexZone zone;
               do {
                  if (!var17.hasNext()) {
                     return null;
                  }

                  zone = (MutexZone)var17.next();
               } while(!zone.containsBlock(blockPos));

               return new MutexZoneCacheWorld.MutexZoneResult(zone, 0.0D);
            } else {
               double f = 1.0D / distance;
               motX *= f;
               motY *= f;
               motZ *= f;
               MutexZoneCacheWorld.MutexZoneResult best = new MutexZoneCacheWorld.MutexZoneResult((MutexZone)null, distance);
               Iterator var19 = ((List)zones).iterator();

               while(var19.hasNext()) {
                  MutexZone zone = (MutexZone)var19.next();
                  double dist = zone.hitTest(p1.posX, p1.posY, p1.posZ, motX, motY, motZ);
                  if (dist < best.distance) {
                     best = new MutexZoneCacheWorld.MutexZoneResult(zone, dist);
                  }
               }

               return best.zone == null ? null : best;
            }
         }
      }

      private MutexZone[] findZonesInChunk(int cx, int cz) {
         if (cx == this.chunkX && cz == this.chunkZ) {
            return this.chunkZones;
         } else {
            this.chunkX = cx;
            this.chunkZ = cz;
            MutexZone[] zones = this.byChunkGetter.getAt(cx, cz);
            if (zones == null) {
               zones = MutexZoneCacheWorld.NO_ZONES;
            }

            this.chunkZones = zones;
            return zones;
         }
      }

      public boolean isNear() {
         if (this.chunkZones != MutexZoneCacheWorld.NO_ZONES) {
            return true;
         } else {
            for(int cz = -1; cz <= 1; ++cz) {
               for(int cx = -1; cx <= 1; ++cx) {
                  if ((cx != 0 || cz != 0) && this.byChunkGetter.getAt(this.chunkX + cx, this.chunkZ + cz) != null) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   @FunctionalInterface
   public interface MutexZoneByChunkGetter {
      MutexZone[] getAt(int var1, int var2);
   }

   protected static class SignSidePositionKey {
      public final IntVector3 position;
      public final boolean front;

      public static MutexZoneCacheWorld.SignSidePositionKey ofZone(MutexZone zone) {
         return new MutexZoneCacheWorld.SignSidePositionKey(zone.signBlock.getPosition(), zone.signFront);
      }

      public SignSidePositionKey(IntVector3 position, boolean front) {
         this.position = position;
         this.front = front;
      }

      public int hashCode() {
         return this.position.hashCode();
      }

      public boolean equals(Object o) {
         MutexZoneCacheWorld.SignSidePositionKey other = (MutexZoneCacheWorld.SignSidePositionKey)o;
         return this.position.equals(other.position) && this.front == other.front;
      }
   }

   protected static final class PathingSignKey {
      public final Object uniqueKey;
      public final TrainProperties trainProperties;

      private PathingSignKey(Object signUniqueKey, TrainProperties trainProperties) {
         this.uniqueKey = signUniqueKey;
         this.trainProperties = trainProperties;
      }

      public static MutexZoneCacheWorld.PathingSignKey of(Object signUniqueKey, TrainProperties trainProperties) {
         return new MutexZoneCacheWorld.PathingSignKey(signUniqueKey, trainProperties);
      }

      public static Optional<MutexZoneCacheWorld.PathingSignKey> readFrom(TrainCarts plugin, DataInputStream stream) throws IOException {
         Object signUniqueKey = plugin.getTrackedSignLookup().deserializeUniqueKey(Util.readByteArray(stream));
         TrainProperties trainProperties = TrainPropertiesStore.get(stream.readUTF());
         return signUniqueKey != null && trainProperties != null ? Optional.of(of(signUniqueKey, trainProperties)) : Optional.empty();
      }

      public boolean writeTo(TrainCarts plugin, DataOutputStream stream) throws IOException {
         if (this.trainProperties.isRemoved()) {
            return false;
         } else {
            byte[] data = plugin.getTrackedSignLookup().serializeUniqueKey(this.uniqueKey);
            if (data == null) {
               return false;
            } else {
               Util.writeByteArray(stream, data);
               stream.writeUTF(this.trainProperties.getTrainName());
               return true;
            }
         }
      }

      public int hashCode() {
         return this.uniqueKey.hashCode();
      }

      public boolean equals(Object o) {
         MutexZoneCacheWorld.PathingSignKey other = (MutexZoneCacheWorld.PathingSignKey)o;
         return this.uniqueKey.equals(other.uniqueKey) && this.trainProperties == other.trainProperties;
      }
   }

   public static class MutexZoneResult {
      public final MutexZone zone;
      public final double distance;

      public MutexZoneResult(MutexZone zone, double distance) {
         this.zone = zone;
         this.distance = distance;
      }
   }
}
