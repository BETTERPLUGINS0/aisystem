package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.HeldItem;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HeldItemImpl extends AbstractJointAction<HeldItemImpl> implements HeldItem {
   private final Vector3f location = new Vector3f();
   private final Quaternionf rotation = new Quaternionf();
   private ItemDisplayTransform display;
   private HeldItem.ItemStackSupplier itemProvider;

   public HeldItemImpl(IJoint var1, JointActionType<HeldItemImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.display = (ItemDisplayTransform)var3.get("display");
   }

   public void onApply() {
      if (this.joint.getVisualModel().getModelRenderer() instanceof DisplayRenderer) {
         this.joint.setRenderer(true);
         this.joint.setModel(EMPTY);
      }

   }

   public void onFinalize() {
      float var1 = (180.0F - this.joint.getYaw()) * 0.017453292F;
      this.joint.getGlobalPosition().rotateY(var1, this.location);
      this.joint.getGlobalLeftRotation().premul(this.rotation.rotationZYX(0.0F, var1, 0.0F), this.rotation);
      if (this.itemProvider == null) {
         this.joint.setModel(EMPTY);
      } else {
         this.joint.setModel(this.itemProvider.supply());
      }

   }

   public void save(SavedData var1) {
      if (this.itemProvider != null) {
         this.itemProvider.save().ifPresent((var1x) -> {
            var1.putData("supplier", var1x);
         });
      }

   }

   public void load(SavedData var1) {
      var1.getData("supplier").ifPresent((var1x) -> {
         String var2 = var1x.getString("type", "");
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -892481938:
            if (var2.equals("static")) {
               var3 = 0;
            }
            break;
         case 1076356494:
            if (var2.equals("equipment")) {
               var3 = 1;
            }
         }

         Object var10000;
         switch(var3) {
         case 0:
            var10000 = new HeldItem.StaticItemStackSupplier();
            break;
         case 1:
            var10000 = new HeldItem.EquipmentSupplier();
            break;
         default:
            var10000 = null;
         }

         Object var4 = var10000;
         this.itemProvider = (HeldItem.ItemStackSupplier)var4;
         if (this.itemProvider != null) {
            this.itemProvider.load(var1x);
         }

      });
   }

   public void clearItemProvider() {
      this.itemProvider = null;
   }

   public ItemStack getItem() {
      return this.joint.getModel();
   }

   public void setItemProvider(Supplier<ItemStack> var1) {
      this.setItemProvider((HeldItem.ItemStackSupplier)(new HeldItem.TemporaryItemStackSupplier(var1)));
   }

   public void setItemProvider(HeldItem.ItemStackSupplier var1) {
      this.itemProvider = var1;
   }

   @Generated
   public Vector3f getLocation() {
      return this.location;
   }

   @Generated
   public Quaternionf getRotation() {
      return this.rotation;
   }

   @Generated
   public ItemDisplayTransform getDisplay() {
      return this.display;
   }

   @Generated
   public HeldItem.ItemStackSupplier getItemProvider() {
      return this.itemProvider;
   }

   @Generated
   public void setDisplay(ItemDisplayTransform var1) {
      this.display = var1;
   }
}
