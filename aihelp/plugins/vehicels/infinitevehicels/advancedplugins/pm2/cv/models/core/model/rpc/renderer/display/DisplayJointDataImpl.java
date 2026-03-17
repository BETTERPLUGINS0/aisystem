package advancedplugins.pm2.cv.models.core.model.rpc.renderer.display;

import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import java.util.Objects;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayJointDataImpl implements DisplayRenderer.JointData {
   private final int id;
   private final UUID uuid = UUID.randomUUID();
   private final DisplayRenderer.Joint joint;
   private final DataTracker<ItemStack> model = new DataTracker();
   private final ProcessedJoint.Cube cube;
   private DataTracker<Vector3f> scale = new DataTracker(new Vector3f(1.0F, 1.0F, 1.0F));
   private DataTracker<Quaternionf> rotation = new DataTracker(new Quaternionf());
   private DataTracker<Vector3f> position = new DataTracker(new Vector3f());

   public DisplayJointDataImpl(int var1, DisplayRenderer.Joint var2) {
      this.id = var1;
      this.joint = var2;
      this.cube = null;
   }

   public DisplayJointDataImpl(int var1, DisplayRenderer.Joint var2, ProcessedJoint.Cube var3) {
      this.id = var1;
      this.joint = var2;
      this.cube = var3;
   }

   public boolean equals(Object var1) {
      if (var1 != null && this.getClass() == var1.getClass()) {
         DisplayJointDataImpl var2 = (DisplayJointDataImpl)var1;
         return Objects.equals(this.model, var2.model);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.model);
   }

   public int getModelHash() {
      return ((ItemStack)this.model.get()).hashCode();
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public DisplayRenderer.Joint getJoint() {
      return this.joint;
   }

   @Generated
   public DataTracker<ItemStack> getModel() {
      return this.model;
   }

   @Generated
   public ProcessedJoint.Cube getCube() {
      return this.cube;
   }

   @Generated
   public DataTracker<Vector3f> getScale() {
      return this.scale;
   }

   @Generated
   public DataTracker<Quaternionf> getRotation() {
      return this.rotation;
   }

   @Generated
   public DataTracker<Vector3f> getPosition() {
      return this.position;
   }

   @Generated
   public void setScale(DataTracker<Vector3f> var1) {
      this.scale = var1;
   }

   @Generated
   public void setRotation(DataTracker<Quaternionf> var1) {
      this.rotation = var1;
   }

   @Generated
   public void setPosition(DataTracker<Vector3f> var1) {
      this.position = var1;
   }
}
