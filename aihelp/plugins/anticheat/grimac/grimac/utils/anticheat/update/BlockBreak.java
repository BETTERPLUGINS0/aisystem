package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.HitboxData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public final class BlockBreak {
   public final Vector3i position;
   public final BlockFace face;
   public final int faceId;
   public final DiggingAction action;
   public final int sequence;
   public final WrappedBlockState block;
   private final GrimPlayer player;
   private boolean cancelled;

   public BlockBreak(GrimPlayer player, Vector3i position, BlockFace face, int faceId, DiggingAction action, int sequence, WrappedBlockState block) {
      this.player = player;
      this.position = position;
      this.face = face;
      this.faceId = faceId;
      this.action = action;
      this.sequence = sequence;
      this.block = block;
   }

   public void cancel() {
      this.cancelled = true;
   }

   public SimpleCollisionBox getCombinedBox() {
      CollisionBox placedOn = HitboxData.getBlockHitbox(this.player, this.player.inventory.getHeldItem().getType().getPlacedType(), this.player.getClientVersion(), this.block, true, this.position.x, this.position.y, this.position.z);
      List<SimpleCollisionBox> boxes = new ArrayList();
      placedOn.downCast((List)boxes);
      SimpleCollisionBox combined = new SimpleCollisionBox((double)this.position.x, (double)this.position.y, (double)this.position.z);

      double maxZ;
      double minX;
      double minY;
      double minZ;
      double maxX;
      double maxY;
      for(Iterator var4 = boxes.iterator(); var4.hasNext(); combined = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ)) {
         SimpleCollisionBox box = (SimpleCollisionBox)var4.next();
         minX = Math.max(box.minX, combined.minX);
         minY = Math.max(box.minY, combined.minY);
         minZ = Math.max(box.minZ, combined.minZ);
         maxX = Math.min(box.maxX, combined.maxX);
         maxY = Math.min(box.maxY, combined.maxY);
         maxZ = Math.min(box.maxZ, combined.maxZ);
      }

      return combined;
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }
}
