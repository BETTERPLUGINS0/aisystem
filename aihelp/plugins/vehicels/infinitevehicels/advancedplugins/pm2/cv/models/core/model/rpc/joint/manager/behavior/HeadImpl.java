package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Head;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Optional;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class HeadImpl extends AbstractJointAction<HeadImpl> implements Head {
   protected boolean shouldRotate;
   protected boolean inherited;
   protected boolean local;

   public HeadImpl(IJoint var1, JointActionType<HeadImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.onParentSwap(var1.getParent());
      this.local = (Boolean)var3.get("local", false);
      this.inherited = (Boolean)var3.get("inherited", false);
   }

   public void onParentSwap(@Nullable IJoint var1) {
      for(this.shouldRotate = true; var1 != null; var1 = var1.getParent()) {
         Optional var2 = var1.getJointAction(this.type);
         this.shouldRotate = var2.isEmpty() || var2.get() instanceof HeadForcedImpl;
         if (!this.shouldRotate) {
            return;
         }
      }

   }

   public void postGlobalCalculation() {
      if (this.shouldRotate) {
         IVisualModel var1 = this.joint.getVisualModel();
         IModelContainer var2 = var1.getModeledEntity();
         float var3 = MathUtils.degreeDifference(var2.getYBodyRot(), var1.getYHeadRot());
         float var4 = var1.getXHeadRot();
         Quaternionf var5 = (new Quaternionf()).rotateY(-var3 * 0.017453292F).rotateX(-var4 * 0.017453292F);
         if (this.local) {
            this.joint.getGlobalLeftRotation().mul(var5);
         } else {
            var5.mul(this.joint.getGlobalLeftRotation(), this.joint.getGlobalLeftRotation());
         }
      }

   }

   @Generated
   public boolean isInherited() {
      return this.inherited;
   }

   @Generated
   public void setInherited(boolean var1) {
      this.inherited = var1;
   }

   @Generated
   public boolean isLocal() {
      return this.local;
   }

   @Generated
   public void setLocal(boolean var1) {
      this.local = var1;
   }
}
