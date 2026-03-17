package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Optional;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class HeadForcedImpl extends HeadImpl {
   protected boolean inherited;

   public HeadForcedImpl(IJoint var1, JointActionType<HeadImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.onParentSwap(var1.getParent());
   }

   public void onParentSwap(@Nullable IJoint var1) {
      this.shouldRotate = false;
      if (var1 != null) {
         Optional var2 = var1.getJointAction(this.type);
         var2.ifPresent((var1x) -> {
            this.inherited = var1x.inherited;
            if (!this.inherited && !(var1x instanceof HeadForcedImpl)) {
               this.shouldRotate = true;
               this.local = var1x.isLocal();
            }

         });
      }

   }

   public void postGlobalCalculation() {
      if (this.shouldRotate) {
         IVisualModel var1 = this.joint.getVisualModel();
         IModelContainer var2 = var1.getModeledEntity();
         float var3 = -MathUtils.degreeDifference(var2.getYBodyRot(), var1.getYHeadRot());
         float var4 = -var1.getXHeadRot();
         Quaternionf var5 = (new Quaternionf()).rotateX(-var4 * 0.017453292F).rotateY(-var3 * 0.017453292F);
         if (this.local) {
            this.joint.getGlobalLeftRotation().mul(var5);
         } else {
            var5.mul(this.joint.getGlobalLeftRotation(), this.joint.getGlobalLeftRotation());
         }
      }

   }

   public boolean isHidden() {
      return true;
   }

   @Generated
   public void setInherited(boolean var1) {
      this.inherited = var1;
   }

   @Generated
   public boolean isInherited() {
      return this.inherited;
   }
}
