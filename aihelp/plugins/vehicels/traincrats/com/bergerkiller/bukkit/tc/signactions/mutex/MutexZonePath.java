package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.debug.particles.DebugParticles;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MutexZonePath extends MutexZone {
   private final TrainCarts plugin;
   protected final MutexZoneCacheWorld.PathingSignKey key;
   private MutexZoneCacheWorld world;
   private RailLookup.TrackedSign sign;
   private final double spacing;
   private final double maxDistance;
   private final Set<IntVector3> blocks;
   private int tickLastUsed;
   private int minX;
   private int minY;
   private int minZ;
   private int maxX;
   private int maxY;
   private int maxZ;
   private int minCX;
   private int minCZ;
   private int maxCX;
   private int maxCZ;
   private final List<OrientedBoundingBox> cubes;

   protected MutexZonePath(TrainCarts plugin, RailLookup.TrackedSign sign, TrainProperties trainProperties, MutexZonePath.OptionsBuilder options) {
      this(plugin, OfflineBlock.of(sign.signBlock), MutexZoneCacheWorld.PathingSignKey.of(sign.getUniqueKey(), trainProperties), sign, options);
   }

   private MutexZonePath(TrainCarts plugin, OfflineBlock signBlock, MutexZoneCacheWorld.PathingSignKey key, RailLookup.TrackedSign sign, MutexZonePath.OptionsBuilder options) {
      super(signBlock, true, options.type, options.name, options.statement);
      this.blocks = new LinkedHashSet(128);
      this.cubes = new ArrayList(128);
      this.plugin = plugin;
      this.key = key;
      this.sign = sign;
      this.spacing = options.spacing;
      this.maxDistance = options.maxDistance;
      this.tickLastUsed = -1;
   }

   public static List<MutexZonePath> readAll(TrainCarts plugin, OfflineDataBlock root) {
      List<OfflineDataBlock> pathDataBlockList = root.findChildren("path-mutex");
      if (pathDataBlockList.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<MutexZonePath> paths = new ArrayList(pathDataBlockList.size());
         Iterator var4 = pathDataBlockList.iterator();

         while(var4.hasNext()) {
            OfflineDataBlock pathDataBlock = (OfflineDataBlock)var4.next();

            MutexZonePath path;
            try {
               DataInputStream stream;
               label79: {
                  stream = pathDataBlock.readData();

                  try {
                     int version = Util.readVariableLengthInt(stream);
                     if (version != 1) {
                        throw new UnsupportedOperationException("Unsupported data version: " + version);
                     }

                     OfflineBlock signBlock = OfflineBlock.readFrom(stream);
                     Optional<MutexZoneCacheWorld.PathingSignKey> key = MutexZoneCacheWorld.PathingSignKey.readFrom(plugin, stream);
                     if (key.isPresent()) {
                        MutexZonePath.OptionsBuilder options = createOptions();
                        options.type(MutexZoneSlotType.readFrom(stream));
                        options.name(stream.readUTF());
                        options.statement(stream.readUTF());
                        options.spacing(stream.readDouble());
                        options.maxDistance(stream.readDouble());
                        path = new MutexZonePath(plugin, signBlock, (MutexZoneCacheWorld.PathingSignKey)key.get(), (RailLookup.TrackedSign)null, options);
                        int blockCount = Util.readVariableLengthInt(stream);
                        if (blockCount == 0) {
                           throw new IllegalStateException("Pathing mutex at " + signBlock + " has zero rail blocks");
                        }

                        int i = 0;

                        while(true) {
                           if (i >= blockCount) {
                              break label79;
                           }

                           path.addBlock(IntVector3.read(stream));
                           ++i;
                        }
                     }
                  } catch (Throwable var15) {
                     if (stream != null) {
                        try {
                           stream.close();
                        } catch (Throwable var14) {
                           var15.addSuppressed(var14);
                        }
                     }

                     throw var15;
                  }

                  if (stream != null) {
                     stream.close();
                  }
                  continue;
               }

               if (stream != null) {
                  stream.close();
               }
            } catch (Throwable var16) {
               plugin.getLogger().log(Level.SEVERE, "Failed to load pathing mutex", var16);
               continue;
            }

            paths.add(path);
         }

         return Collections.unmodifiableList(paths);
      }
   }

   public void writeTo(OfflineDataBlock root) {
      try {
         root.addChildOrAbort("path-mutex", (stream) -> {
            Util.writeVariableLengthInt(stream, 1);
            OfflineBlock.writeTo(stream, this.signBlock);
            if (!this.key.writeTo(this.plugin, stream)) {
               throw new OfflineDataBlock.AbortChildException();
            } else {
               this.type.writeTo(stream);
               stream.writeUTF(this.slot.getName());
               stream.writeUTF(this.statement);
               stream.writeDouble(this.spacing);
               stream.writeDouble(this.getMaxDistance());
               Util.writeVariableLengthInt(stream, this.blocks.size());
               Iterator var2 = this.blocks.iterator();

               while(var2.hasNext()) {
                  IntVector3 block = (IntVector3)var2.next();
                  block.write(stream);
               }

            }
         });
      } catch (Throwable var3) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to save pathing mutex at " + this.signBlock, var3);
      }

   }

   public String getTrainName() {
      return this.key.trainProperties.getTrainName();
   }

   public boolean isByGroup(MinecartGroup group) {
      return this.key.trainProperties == group.getProperties();
   }

   protected void addToWorld(MutexZoneCacheWorld world) {
      world.byPathingKey.put(this.key, this);
      this.world = world;
   }

   public void remove() {
      if (this.world.byPathingKey.remove(this.key, this)) {
         this.world.remove(this);
      }

   }

   public double getSpacing(MinecartGroup group) {
      return this.isByGroup(group) ? 0.0D : this.spacing;
   }

   public double getMaxDistance() {
      return this.maxDistance;
   }

   public void addBlock(IntVector3 block) {
      if (this.blocks.add(block)) {
         this.updateBB(block);
         boolean chunksChanged = false;
         if (this.blocks.size() == 1) {
            this.minX = this.maxX = block.x;
            this.minY = this.maxY = block.y;
            this.minZ = this.maxZ = block.z;
            this.minCX = this.maxCX = block.getChunkX();
            this.minCZ = this.maxCZ = block.getChunkZ();
            chunksChanged = true;
         } else {
            if (block.x < this.minX) {
               this.minX = block.x;
            } else if (block.x > this.maxX) {
               this.maxX = block.x;
            }

            if (block.y < this.minY) {
               this.minY = block.y;
            } else if (block.y > this.maxY) {
               this.maxY = block.y;
            }

            if (block.z < this.minZ) {
               this.minZ = block.z;
            } else if (block.z > this.maxZ) {
               this.maxZ = block.z;
            }

            int cx = block.getChunkX();
            int cz = block.getChunkZ();
            if (cx < this.minCX) {
               this.minCX = cx;
               chunksChanged = true;
            } else if (cx > this.maxCX) {
               this.maxCX = cx;
               chunksChanged = true;
            }

            if (cz < this.minCZ) {
               this.minCZ = cz;
               chunksChanged = true;
            } else if (cz > this.maxCZ) {
               this.maxCZ = cz;
               chunksChanged = true;
            }
         }

         if (chunksChanged && this.world != null && this.world.byPathingKey.get(this.key) == this) {
            this.world.addNewChunks(this);
         }

      }
   }

   private void updateBB(IntVector3 coord) {
      this.cubes.add(OrientedBoundingBox.naturalFromTo(new Vector(coord.x, coord.y, coord.z), new Vector((double)coord.x + 1.0D, (double)coord.y + 1.0D, (double)coord.z + 1.0D)));
   }

   public boolean containsBlock(IntVector3 block) {
      return this.blocks.contains(block);
   }

   public boolean isNearby(IntVector3 block, int radius) {
      return block.x >= this.minX - radius && block.y >= this.minY - radius && block.z >= this.minZ - radius && block.x <= this.maxX + radius && block.y <= this.maxY + radius && block.z <= this.maxZ + radius;
   }

   public void forAllContainedChunks(MutexZone.ChunkCoordConsumer action) {
      int chunkMinX = this.minCX;
      int chunkMaxX = this.maxCX;
      int chunkMinZ = this.minCZ;
      int chunkMaxZ = this.maxCZ;

      for(int cz = chunkMinZ; cz <= chunkMaxZ; ++cz) {
         for(int cx = chunkMinX; cx <= chunkMaxX; ++cx) {
            action.accept(cx, cz);
         }
      }

   }

   public long showDebugColorSeed() {
      return (long)this.signBlock.hashCode();
   }

   public void showDebug(Player player, Color color) {
      DebugParticles particles = DebugParticles.of(player);
      Iterator var4 = this.blocks.iterator();

      while(var4.hasNext()) {
         IntVector3 block = (IntVector3)var4.next();
         Vector pos = MathUtil.addToVector(block.toVector(), 0.5D, 0.5D, 0.5D);
         particles.point(color, pos);
      }

   }

   protected void setLeversDown(boolean down) {
      if (this.sign == null) {
         this.sign = this.plugin.getTrackedSignLookup().getTrackedSign(this.key.uniqueKey);
      }

      if (this.sign != null) {
         this.sign.setOutput(down);
      }

   }

   public void onUsed(MinecartGroup group) {
      if (this.isByGroup(group)) {
         this.tickLastUsed = CommonUtil.getServerTicks();
      }

   }

   public boolean isExpired(int expireTick) {
      if (this.key.trainProperties.isRemoved()) {
         return true;
      } else if (!this.key.trainProperties.isLoaded()) {
         this.tickLastUsed = -1;
         return false;
      } else if (this.tickLastUsed == -1) {
         this.tickLastUsed = CommonUtil.getServerTicks();
         return false;
      } else {
         return this.tickLastUsed < expireTick;
      }
   }

   public double hitTest(double posX, double posY, double posZ, double motX, double motY, double motZ) {
      double result = Double.MAX_VALUE;

      OrientedBoundingBox bb;
      for(Iterator var15 = this.cubes.iterator(); var15.hasNext(); result = Math.min(result, bb.hitTest(posX, posY, posZ, motX, motY, motZ))) {
         bb = (OrientedBoundingBox)var15.next();
      }

      return result;
   }

   public static MutexZonePath.OptionsBuilder createOptions() {
      return new MutexZonePath.OptionsBuilder();
   }

   public static final class OptionsBuilder {
      private double spacing;
      private double maxDistance;
      private MutexZoneSlotType type;
      private String name;
      private String statement;

      private OptionsBuilder() {
         this.spacing = 1.0D;
         this.maxDistance = 64.0D;
         this.type = MutexZoneSlotType.NORMAL;
         this.name = "";
         this.statement = "";
      }

      public double spacing() {
         return this.spacing;
      }

      public MutexZonePath.OptionsBuilder spacing(double spacing) {
         this.spacing = MathUtil.clamp(spacing, 0.0D, (double)TCConfig.maxMutexSize);
         return this;
      }

      public double maxDistance() {
         return this.maxDistance;
      }

      public MutexZonePath.OptionsBuilder maxDistance(double maxDistance) {
         this.maxDistance = MathUtil.clamp(maxDistance, 0.0D, (double)TCConfig.maxMutexSize);
         return this;
      }

      public MutexZonePath.OptionsBuilder type(MutexZoneSlotType type) {
         this.type = type;
         return this;
      }

      public MutexZonePath.OptionsBuilder name(String name) {
         this.name = name;
         return this;
      }

      public MutexZonePath.OptionsBuilder statement(String statement) {
         this.statement = statement;
         return this;
      }

      // $FF: synthetic method
      OptionsBuilder(Object x0) {
         this();
      }
   }
}
