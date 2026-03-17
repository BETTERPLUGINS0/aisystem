package advancedplugins.pm2.cv.models.core.model.rpc.renderer.display;

import advancedplugins.pm2.cv.models.api.lod.JointSnapshotHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.data.UpdateScheme;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayJointImpl implements DisplayRenderer.Joint {
   private final JointSnapshotHandler snapshotHandler = new JointSnapshotHandler(this);
   private final DataTracker<Boolean> render = new DataTracker(true);
   private final DataTracker<Boolean> step = new DataTracker(false);
   private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set, Vector3f::new);
   private final DataTracker<Quaternionf> leftRotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set, Quaternionf::new);
   private final DataTracker<Vector3f> scale = new UpdateDataTracker(new Vector3f(), Vector3f::set, Vector3f::new);
   private final DataTracker<Quaternionf> rightRotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set, Quaternionf::new);
   private final DataTracker<ItemDisplayTransform> display;
   private final DataTracker<Boolean> visibility;
   private final DataTracker<Boolean> glowing;
   private final DataTracker<Integer> glowColor;
   private final DataTracker<Integer> brightness;
   private final Map<Integer, DisplayRenderer.JointData> model;
   private final UpdateScheme<DisplayRenderer.JointData> modelUpdateScheme;
   private IJoint joint;

   public DisplayJointImpl() {
      this.display = new DataTracker(ItemDisplayTransform.NONE);
      this.visibility = new DataTracker(true);
      this.glowing = new DataTracker(false);
      this.glowColor = new DataTracker(-1);
      this.brightness = new DataTracker(-1);
      this.model = new Int2ObjectOpenHashMap();
      this.modelUpdateScheme = new UpdateScheme();
   }

   public void updateJointData(EntityHandler var1, DisplayRenderer.Pivot var2, JointItems var3) {
      Map var4 = var3.getItems();
      Set var5 = this.model.keySet();
      Set var6 = var4.keySet();
      HashSet var7 = new HashSet(Sets.difference(var6, var5));
      HashSet var8 = new HashSet(Sets.difference(var5, var6));
      HashSet var9 = new HashSet();
      var8.forEach((var2x) -> {
         var9.add((DisplayRenderer.JointData)this.model.remove(var2x));
      });
      Iterator var10 = var9.iterator();
      var7.forEach((var5x) -> {
         ItemStack var6 = ((ItemStack)var4.get(var5x)).clone();
         if (var10.hasNext()) {
            DisplayRenderer.JointData var7 = (DisplayRenderer.JointData)var10.next();
            int var8 = var7.getModelHash();
            var7.getModel().set(var6);
            this.modelUpdateScheme.addUpdated(var7);
            this.model.remove(var8);
            this.model.put(var5x, var7);
         } else {
            DisplayJointDataImpl var9 = new DisplayJointDataImpl(var1.getNextEntityId(), this);
            var9.getModel().set(var6);
            this.model.put(var5x, var9);
            this.modelUpdateScheme.addAdded(var9);
            var2.getPassengers().add(var9.getId());
         }

      });
      var10.forEachRemaining((var2x) -> {
         this.modelUpdateScheme.addRemove(var2x);
         var2.getPassengers().remove(var2x.getId());
      });
   }

   @Generated
   public JointSnapshotHandler getSnapshotHandler() {
      return this.snapshotHandler;
   }

   @Generated
   public DataTracker<Boolean> getRender() {
      return this.render;
   }

   @Generated
   public DataTracker<Boolean> getStep() {
      return this.step;
   }

   @Generated
   public DataTracker<Vector3f> getPosition() {
      return this.position;
   }

   @Generated
   public DataTracker<Quaternionf> getLeftRotation() {
      return this.leftRotation;
   }

   @Generated
   public DataTracker<Vector3f> getScale() {
      return this.scale;
   }

   @Generated
   public DataTracker<Quaternionf> getRightRotation() {
      return this.rightRotation;
   }

   @Generated
   public DataTracker<ItemDisplayTransform> getDisplay() {
      return this.display;
   }

   @Generated
   public DataTracker<Boolean> getVisibility() {
      return this.visibility;
   }

   @Generated
   public DataTracker<Boolean> getGlowing() {
      return this.glowing;
   }

   @Generated
   public DataTracker<Integer> getGlowColor() {
      return this.glowColor;
   }

   @Generated
   public DataTracker<Integer> getBrightness() {
      return this.brightness;
   }

   @Generated
   public Map<Integer, DisplayRenderer.JointData> getModel() {
      return this.model;
   }

   @Generated
   public UpdateScheme<DisplayRenderer.JointData> getModelUpdateScheme() {
      return this.modelUpdateScheme;
   }

   @Generated
   public IJoint getJoint() {
      return this.joint;
   }

   @Generated
   public void setJoint(IJoint var1) {
      this.joint = var1;
   }
}
