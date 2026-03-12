package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import java.util.List;
import lombok.Generated;

public final class BlockPrediction {
   private List<Vector3i> forBlockUpdate;
   private Vector3i blockPosition;
   private int originalBlockId;
   private final Vector3d playerPosition;

   @Generated
   public BlockPrediction(List<Vector3i> forBlockUpdate, Vector3i blockPosition, int originalBlockId, Vector3d playerPosition) {
      this.forBlockUpdate = forBlockUpdate;
      this.blockPosition = blockPosition;
      this.originalBlockId = originalBlockId;
      this.playerPosition = playerPosition;
   }

   @Generated
   public List<Vector3i> getForBlockUpdate() {
      return this.forBlockUpdate;
   }

   @Generated
   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   @Generated
   public int getOriginalBlockId() {
      return this.originalBlockId;
   }

   @Generated
   public Vector3d getPlayerPosition() {
      return this.playerPosition;
   }

   @Generated
   public void setForBlockUpdate(List<Vector3i> forBlockUpdate) {
      this.forBlockUpdate = forBlockUpdate;
   }

   @Generated
   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   @Generated
   public void setOriginalBlockId(int originalBlockId) {
      this.originalBlockId = originalBlockId;
   }
}
