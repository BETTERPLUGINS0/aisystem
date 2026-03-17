package advancedplugins.pm2.cv.models.api.lod;

import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;
import lombok.Generated;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class JointSnapshotHandler {
   private final DisplayRenderer.Joint joint;
   private final Object2ObjectMap<UUID, JointSnapshotHandler.Snapshot> snapshots = new Object2ObjectOpenHashMap();
   private JointSnapshotHandler.Snapshot snapshot;

   public void recordSnapshot() {
      this.snapshot = new JointSnapshotHandler.Snapshot(((Vector3f)this.joint.getPosition().get()).hashCode(), ((Quaternionf)this.joint.getLeftRotation().get()).hashCode(), ((Vector3f)this.joint.getScale().get()).hashCode(), ((Quaternionf)this.joint.getRightRotation().get()).hashCode());
   }

   public byte getUpdate(UUID var1) {
      JointSnapshotHandler.Snapshot var2 = (JointSnapshotHandler.Snapshot)this.snapshots.put(var1, this.snapshot);
      return var2 == null ? 15 : var2.compare(this.snapshot);
   }

   public void remove(UUID var1) {
      this.snapshots.remove(var1);
   }

   @Generated
   public JointSnapshotHandler(DisplayRenderer.Joint var1) {
      this.joint = var1;
   }

   public static record Snapshot(int position, int leftRotation, int scale, int rightRotation) {
      public Snapshot(int position, int leftRotation, int scale, int rightRotation) {
         this.position = var1;
         this.leftRotation = var2;
         this.scale = var3;
         this.rightRotation = var4;
      }

      public byte compare(JointSnapshotHandler.Snapshot var1) {
         byte var2 = MathUtils.setBit((byte)0, 0, this.position != var1.position);
         var2 = MathUtils.setBit(var2, 1, this.leftRotation != var1.leftRotation);
         var2 = MathUtils.setBit(var2, 2, this.scale != var1.scale);
         var2 = MathUtils.setBit(var2, 3, this.rightRotation != var1.rightRotation);
         return var2;
      }

      public int position() {
         return this.position;
      }

      public int leftRotation() {
         return this.leftRotation;
      }

      public int scale() {
         return this.scale;
      }

      public int rightRotation() {
         return this.rightRotation;
      }
   }
}
