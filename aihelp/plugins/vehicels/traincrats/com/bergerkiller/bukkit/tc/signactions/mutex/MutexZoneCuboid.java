package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.debug.particles.DebugParticles;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MutexZoneCuboid extends MutexZone {
   public final IntVector3 start;
   public final IntVector3 end;
   private final OrientedBoundingBox bb;

   protected MutexZoneCuboid(OfflineBlock signBlock, boolean signFront, IntVector3 start, IntVector3 end, MutexZoneSlotType type, String name, String statement) {
      super(signBlock, signFront, type, name, statement);
      this.start = start;
      this.end = end;
      this.bb = OrientedBoundingBox.naturalFromTo(new Vector(start.x, start.y, start.z), new Vector((double)end.x + 1.0D, (double)end.y + 1.0D, (double)end.z + 1.0D));
   }

   protected void addToWorld(MutexZoneCacheWorld world) {
      world.bySignPosition.put(MutexZoneCacheWorld.SignSidePositionKey.ofZone(this), this);
   }

   public boolean containsBlock(IntVector3 block) {
      return block.x >= this.start.x && block.y >= this.start.y && block.z >= this.start.z && block.x <= this.end.x && block.y <= this.end.y && block.z <= this.end.z;
   }

   public boolean isNearby(IntVector3 block, int radius) {
      return block.x >= this.start.x - radius && block.y >= this.start.y - radius && block.z >= this.start.z - radius && block.x <= this.end.x + radius && block.y <= this.end.y + radius && block.z <= this.end.z + radius;
   }

   public void forAllContainedChunks(MutexZone.ChunkCoordConsumer action) {
      int chunkMinX = this.start.getChunkX();
      int chunkMaxX = this.end.getChunkX();
      int chunkMinZ = this.start.getChunkZ();
      int chunkMaxZ = this.end.getChunkZ();

      for(int cz = chunkMinZ; cz <= chunkMaxZ; ++cz) {
         for(int cx = chunkMinX; cx <= chunkMaxX; ++cx) {
            action.accept(cx, cz);
         }
      }

   }

   public long showDebugColorSeed() {
      return MathUtil.longHashToLong(this.start.hashCode(), this.end.hashCode());
   }

   public void showDebug(Player player, Color color) {
      DebugParticles.of(player).cube(color, (double)this.start.x, (double)this.start.y, (double)this.start.z, (double)this.end.x + 1.0D, (double)this.end.y + 1.0D, (double)this.end.z + 1.0D);
   }

   protected void setLeversDown(boolean down) {
      Block signBlock = this.getSignBlock();
      if (signBlock != null) {
         signBlock.getChunk();
         BlockData data = WorldUtil.getBlockData(signBlock);
         if (MaterialUtil.ISSIGN.get(data)) {
            BlockUtil.setLeversAroundBlock(signBlock.getRelative(data.getAttachedFace()), down);
         }
      }

   }

   public double hitTest(double posX, double posY, double posZ, double motX, double motY, double motZ) {
      return this.bb.hitTest(posX, posY, posZ, motX, motY, motZ);
   }
}
