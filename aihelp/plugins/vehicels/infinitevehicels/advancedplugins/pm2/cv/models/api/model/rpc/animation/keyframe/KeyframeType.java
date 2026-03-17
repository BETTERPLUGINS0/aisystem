package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.Timeline;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.KeyframeInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyframeType<KEY extends AbstractKeyframe<DATA>, DATA> {
   private final String id;
   private final Supplier<KEY> keyframeSupplier;
   private final Function<Timeline, KeyframeInterpolator<KEY, DATA>> interpolatorSupplier;
   private final Map<Class<?>, KeyframeType.ModelUpdater> modelUpdaters;
   private final Map<Class<?>, KeyframeType.JointUpdater> jointUpdaters;
   private final boolean global;

   protected KeyframeType(String var1, Supplier<KEY> var2, Function<Timeline, KeyframeInterpolator<KEY, DATA>> var3, Map<Class<?>, KeyframeType.ModelUpdater> var4, Map<Class<?>, KeyframeType.JointUpdater> var5, boolean var6) {
      this.id = var1;
      this.keyframeSupplier = var2;
      this.interpolatorSupplier = var3;
      this.modelUpdaters = var4;
      this.jointUpdaters = var5;
      this.global = var6;
   }

   public KeyframeInterpolator<KEY, DATA> createInterpolator(Timeline var1) {
      return (KeyframeInterpolator)this.interpolatorSupplier.apply(var1);
   }

   public KEY createKeyframe() {
      return (AbstractKeyframe)this.keyframeSupplier.get();
   }

   public void updateModel(Class<?> var1, AnimationHandler var2, Object... var3) {
      KeyframeType.ModelUpdater var4 = (KeyframeType.ModelUpdater)this.modelUpdaters.get(var1);
      if (var4 != null) {
         var4.update(var2, var2.getVisualModel(), var3);
      }

   }

   public void updateJoint(Class<?> var1, AnimationHandler var2, IJoint var3, Object... var4) {
      KeyframeType.JointUpdater var5 = (KeyframeType.JointUpdater)this.jointUpdaters.get(var1);
      if (var5 != null) {
         var5.update(var2, var3, var4);
      }

   }

   public String getId() {
      return this.id;
   }

   public Supplier<KEY> getKeyframeSupplier() {
      return this.keyframeSupplier;
   }

   public Function<Timeline, KeyframeInterpolator<KEY, DATA>> getInterpolatorSupplier() {
      return this.interpolatorSupplier;
   }

   public Map<Class<?>, KeyframeType.ModelUpdater> getModelUpdaters() {
      return this.modelUpdaters;
   }

   public Map<Class<?>, KeyframeType.JointUpdater> getJointUpdaters() {
      return this.jointUpdaters;
   }

   public boolean isGlobal() {
      return this.global;
   }

   @FunctionalInterface
   public interface ModelUpdater {
      void update(AnimationHandler var1, IVisualModel var2, Object... var3);
   }

   @FunctionalInterface
   public interface JointUpdater {
      void update(AnimationHandler var1, IJoint var2, Object... var3);
   }

   public static class Builder<KEY extends AbstractKeyframe<DATA>, DATA> {
      private final String id;
      private final Supplier<KEY> keyframeSupplier;
      private final Map<Class<?>, KeyframeType.ModelUpdater> modelUpdaters = new ConcurrentHashMap();
      private final Map<Class<?>, KeyframeType.JointUpdater> jointUpdaters = new ConcurrentHashMap();
      private Function<Timeline, KeyframeInterpolator<KEY, DATA>> interpolatorSupplier = (var0) -> {
         return new KeyframeInterpolator();
      };
      private boolean global;

      protected Builder(String var1, Supplier<KEY> var2) {
         this.id = var1;
         this.keyframeSupplier = var2;
      }

      public static <KEY extends AbstractKeyframe<DATA>, DATA> KeyframeType.Builder<KEY, DATA> of(String var0, Supplier<KEY> var1) {
         return new KeyframeType.Builder(var0, var1);
      }

      public KeyframeType.Builder<KEY, DATA> interpolator(Function<Timeline, KeyframeInterpolator<KEY, DATA>> var1) {
         this.interpolatorSupplier = var1;
         return this;
      }

      public KeyframeType.Builder<KEY, DATA> registerModelUpdater(Class<?> var1, KeyframeType.ModelUpdater var2) {
         this.modelUpdaters.put(var1, var2);
         return this;
      }

      public KeyframeType.Builder<KEY, DATA> registerJointUpdater(Class<?> var1, KeyframeType.JointUpdater var2) {
         this.jointUpdaters.put(var1, var2);
         return this;
      }

      public KeyframeType.Builder<KEY, DATA> global() {
         this.global = true;
         return this;
      }

      public KeyframeType<KEY, DATA> build() {
         return new KeyframeType(this.id, this.keyframeSupplier, this.interpolatorSupplier, this.modelUpdaters, this.jointUpdaters, this.global);
      }
   }
}
