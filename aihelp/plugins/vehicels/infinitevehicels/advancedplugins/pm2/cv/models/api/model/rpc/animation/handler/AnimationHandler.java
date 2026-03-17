package advancedplugins.pm2.cv.models.api.model.rpc.animation.handler;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.SimpleProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import java.util.Iterator;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public interface AnimationHandler extends DataIO {
   IVisualModel getVisualModel();

   void prepare();

   void updateJoint(IJoint var1);

   boolean hasFinishedAllAnimations();

   void setDefaultProperty(AnimationHandler.DefaultProperty var1);

   AnimationHandler.DefaultProperty getDefaultProperty(ModelState var1);

   void tickGlobal();

   @Nullable
   IAnimationProperty playAnimation(String var1, double var2, double var4, double var6, boolean var8);

   boolean playAnimation(IAnimationProperty var1, boolean var2);

   boolean isPlayingAnimation(String var1);

   void stopAnimation(String var1);

   void forceStopAnimation(String var1);

   void forceStopAllAnimations();

   @Nullable
   IAnimationProperty getAnimation(String var1);

   Map<String, IAnimationProperty> getAnimations();

   String getId();

   default boolean isWalking(IVisualModel visualModel) {
      IModelContainer modelContainer = visualModel.getModeledEntity();
      BaseEntity<?> baseEntity = modelContainer.getBase();
      return !modelContainer.getRootMotionHandler().isQueued() && baseEntity.isWalking();
   }

   default void save(SavedData data) {
      SavedData defaultData = new SavedData();
      ModelState[] var3 = ModelState.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ModelState state = var3[var5];
         AnimationHandler.DefaultProperty property = this.getDefaultProperty(state);
         SavedData propertyData = new SavedData();
         propertyData.putDouble("lerp_in", property.lerpIn);
         propertyData.putDouble("lerp_out", property.lerpOut);
         propertyData.putDouble("speed", property.speed);
         defaultData.putData(state.name(), propertyData);
      }

      data.putData("defaults", defaultData);
      data.putString("id", this.getId());
   }

   default void load(SavedData data) {
      data.getData("defaults").ifPresent((defaultData) -> {
         Iterator var2 = defaultData.keySet().iterator();

         while(var2.hasNext()) {
            String key = (String)var2.next();
            ModelState state = ModelState.get(key);
            defaultData.getData(key).ifPresent((propertyData) -> {
               this.setDefaultProperty(new AnimationHandler.DefaultProperty(state, propertyData.getDouble("lerp_in"), propertyData.getDouble("lerp_out"), propertyData.getDouble("speed")));
            });
         }

      });
   }

   public static class DefaultProperty {
      private final ModelState state;
      private final String animation;
      private final double lerpIn;
      private final double lerpOut;
      private final double speed;

      public DefaultProperty(ModelState var1, double var2, double var4, double var6) {
         this(var1, var1.getString(), var2, var4, var6);
      }

      public DefaultProperty(ModelState var1, String var2, double var3, double var5, double var7) {
         this.state = var1;
         this.animation = var2;
         this.lerpIn = var3;
         this.lerpOut = var5;
         this.speed = var7;
      }

      public IAnimationProperty build(IVisualModel var1) {
         return this.build(var1, this.lerpIn, this.lerpOut, this.speed);
      }

      public IAnimationProperty build(IVisualModel var1, double var2, double var4, double var6) {
         BlueprintAnimation var8 = (BlueprintAnimation)var1.getBlueprint().getAnimations().get(this.animation);
         if (var8 == null) {
            return null;
         } else {
            SimpleProperty var9 = new SimpleProperty(var1, var8, var2, var4, var6);
            var9.setSkipLastFrame(true);
            return var9;
         }
      }

      public ModelState getState() {
         return this.state;
      }

      public String getAnimation() {
         return this.animation;
      }

      public double getLerpIn() {
         return this.lerpIn;
      }

      public double getLerpOut() {
         return this.lerpOut;
      }

      public double getSpeed() {
         return this.speed;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof AnimationHandler.DefaultProperty)) {
            return false;
         } else {
            AnimationHandler.DefaultProperty var2 = (AnimationHandler.DefaultProperty)var1;
            return Double.compare(this.lerpIn, var2.lerpIn) == 0 && Double.compare(this.lerpOut, var2.lerpOut) == 0 && Double.compare(this.speed, var2.speed) == 0 && this.state.equals(var2.state) && this.animation.equals(var2.animation);
         }
      }

      public int hashCode() {
         byte var1 = 1;
         long var2 = Double.doubleToLongBits(this.lerpIn);
         long var4 = Double.doubleToLongBits(this.lerpOut);
         long var6 = Double.doubleToLongBits(this.speed);
         int var8 = 31 * var1 + Long.hashCode(var2);
         var8 = 31 * var8 + Long.hashCode(var4);
         var8 = 31 * var8 + Long.hashCode(var6);
         var8 = 31 * var8 + this.state.hashCode();
         var8 = 31 * var8 + this.animation.hashCode();
         return var8;
      }

      public String toString() {
         return String.format("DefaultProperty(state=%s, animation=%s, lerpIn=%s, lerpOut=%s, speed=%s)", this.state, this.animation, this.lerpIn, this.lerpOut, this.speed);
      }
   }
}
