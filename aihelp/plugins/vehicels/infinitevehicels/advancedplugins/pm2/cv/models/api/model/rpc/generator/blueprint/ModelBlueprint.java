package advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorIncompatibleJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorUnknownJointBehavior;
import advancedplugins.pm2.cv.models.api.model.rpc.error.WarnIncompatibleJointBehavior;
import advancedplugins.pm2.cv.models.api.model.rpc.error.WarningDuplicateJointName;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.DefaultRenderType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.IRenderType;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import lombok.Generated;

public class ModelBlueprint {
   private final Map<String, BlueprintJoint> joints = new LinkedHashMap();
   private final Map<String, BlueprintAnimation> animations = new LinkedHashMap();
   private final transient Set<String> animationDescendingPriority = new LinkedHashSet();
   private final Map<String, String> animationsPlaceholders = new LinkedHashMap();
   private BlockbenchModel blockbenchModel;
   private String name;
   private Hitbox mainHitbox = new Hitbox(0.6D, 1.8D, 0.6D, 1.44D);
   private float shadowRadius = 0.0F;
   private transient Map<String, BlueprintJoint> flatMap;
   private transient boolean flatViewConstructed;
   private transient boolean descendingAnimationConstructed;
   private transient boolean jointBehaviorCached;

   public void finalizeModel(ErrorCollector var1) {
      this.constructFlatJointMap(var1);
      this.constructDescendingAnimation(var1);
      this.cacheJointActions(var1);
   }

   public void constructFlatJointMap(ErrorCollector var1) {
      if (!this.flatViewConstructed) {
         this.flatViewConstructed = true;
         LinkedHashMap var2 = new LinkedHashMap();
         LinkedHashSet var3 = new LinkedHashSet(this.joints.values());
         LinkedHashSet var4 = new LinkedHashSet();

         while(!var3.isEmpty()) {
            BlueprintJoint var5;
            for(Iterator var6 = var3.iterator(); var6.hasNext(); var2.put(var5.getName(), var5)) {
               var5 = (BlueprintJoint)var6.next();
               var4.addAll(var5.getChildren().values());
               if (var2.containsKey(var5.getName())) {
                  (new WarningDuplicateJointName(var5.getName(), var5.getUuid())).log(var1);
                  var5.setName(var5.getUuid().toString());
               }
            }

            var3.clear();
            var3.addAll(var4);
            var4.clear();
         }

         this.flatMap = ImmutableMap.copyOf(var2);
      }

   }

   public void constructDescendingAnimation(ErrorCollector var1) {
      if (!this.descendingAnimationConstructed) {
         this.descendingAnimationConstructed = true;
         ArrayList var2 = new ArrayList();
         this.animations.keySet().forEach((var1x) -> {
            var2.add(0, var1x);
         });
         this.animationDescendingPriority.addAll(var2);
      }

   }

   public void cacheJointActions(ErrorCollector var1) {
      if (!this.jointBehaviorCached) {
         this.jointBehaviorCached = true;

         assert this.flatMap != null : "Joint flat view map is null.";

         JointActionArchive var2 = ModelAPI.getAPI().getJointActionArchive();
         Iterator var3 = this.flatMap.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            String var5 = (String)var4.getKey();
            BlueprintJoint var6 = (BlueprintJoint)var4.getValue();
            Iterator var7 = var6.getBehaviors().entrySet().iterator();

            while(var7.hasNext()) {
               Entry var8 = (Entry)var7.next();
               String var9 = (String)var8.getKey();
               JointActionType var10 = var2.getById(var9);
               if (var10 == null) {
                  (new ErrorUnknownJointBehavior(var5, var9)).log(var1);
               } else if (!this.canJointUseBehavior(var6, var10)) {
                  (new ErrorIncompatibleJoint(var6.isRenderer(), var5, var9)).log(var1);
               } else {
                  if (!var10.test(var6.getCachedBehaviorProvider().keySet())) {
                     (new WarnIncompatibleJointBehavior(var5, var9)).log(var1);
                  }

                  var10.assignCachedProvider(var6, (Map)var8.getValue());
               }
            }

            var7 = var2.getIds().iterator();

            while(var7.hasNext()) {
               String var11 = (String)var7.next();
               JointActionType var12 = var2.getById(var11);
               if (var12 != null) {
                  var12.assignForcedCachedProvider(var6);
               }
            }
         }
      }

   }

   private boolean canJointUseBehavior(BlueprintJoint var1, JointActionType<?> var2) {
      IRenderType var3 = var2.getRenderType();
      if (var3 == DefaultRenderType.ANY) {
         return true;
      } else if (var3 == DefaultRenderType.MODEL) {
         return var1.isRenderer();
      } else {
         return !var1.isRenderer();
      }
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setMainHitbox(Hitbox var1) {
      this.mainHitbox = var1;
   }

   public void setShadowRadius(float var1) {
      this.shadowRadius = var1;
   }

   public void setFlatViewConstructed(boolean var1) {
      this.flatViewConstructed = var1;
   }

   public void setDescendingAnimationConstructed(boolean var1) {
      this.descendingAnimationConstructed = var1;
   }

   public void setJointBehaviorCached(boolean var1) {
      this.jointBehaviorCached = var1;
   }

   @Generated
   public Map<String, BlueprintJoint> getJoints() {
      return this.joints;
   }

   @Generated
   public Map<String, BlueprintAnimation> getAnimations() {
      return this.animations;
   }

   @Generated
   public Set<String> getAnimationDescendingPriority() {
      return this.animationDescendingPriority;
   }

   @Generated
   public Map<String, String> getAnimationsPlaceholders() {
      return this.animationsPlaceholders;
   }

   @Generated
   public BlockbenchModel getBlockbenchModel() {
      return this.blockbenchModel;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Hitbox getMainHitbox() {
      return this.mainHitbox;
   }

   @Generated
   public float getShadowRadius() {
      return this.shadowRadius;
   }

   @Generated
   public Map<String, BlueprintJoint> getFlatMap() {
      return this.flatMap;
   }

   @Generated
   public boolean isFlatViewConstructed() {
      return this.flatViewConstructed;
   }

   @Generated
   public boolean isDescendingAnimationConstructed() {
      return this.descendingAnimationConstructed;
   }

   @Generated
   public boolean isJointBehaviorCached() {
      return this.jointBehaviorCached;
   }

   @Generated
   public void setBlockbenchModel(BlockbenchModel var1) {
      this.blockbenchModel = var1;
   }

   @Generated
   public void setFlatMap(Map<String, BlueprintJoint> var1) {
      this.flatMap = var1;
   }
}
