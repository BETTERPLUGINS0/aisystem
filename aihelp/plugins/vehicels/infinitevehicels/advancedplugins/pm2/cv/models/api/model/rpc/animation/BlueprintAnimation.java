package advancedplugins.pm2.cv.models.api.model.rpc.animation;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.KeyframeInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.ScriptKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BlueprintAnimation {
   private final ModelBlueprint modelBlueprint;
   private final Map<UUID, Timeline> timelines = new ConcurrentHashMap();
   private final String name;
   private final Timeline globalTimeline = new Timeline(this, false);
   private double length;
   private BlueprintAnimation.LoopMode loopMode;
   private boolean override;

   public BlueprintAnimation(ModelBlueprint var1, String var2) {
      this.modelBlueprint = var1;
      this.name = var2;
   }

   public Vector3f getPosition(IJoint var1, IAnimationProperty var2) {
      Timeline var3 = (Timeline)this.timelines.get(var1.getBlueprintJoint().getUuid());
      return var3 == null ? null : (Vector3f)var3.getInterpolator(KeyframeTypes.POSITION).interpolate(var1, var2);
   }

   public Vector3f getVelocity(IJoint var1, IAnimationProperty var2) {
      Timeline var3 = (Timeline)this.timelines.get(var1.getBlueprintJoint().getUuid());
      return var3 == null ? null : (Vector3f)var3.getInterpolator(KeyframeTypes.POSITION).interpolateAndDerive(var1, var2);
   }

   public Vector3f getRotation(IJoint var1, IAnimationProperty var2) {
      Timeline var3 = (Timeline)this.timelines.get(var1.getBlueprintJoint().getUuid());
      return var3 == null ? null : (Vector3f)var3.getInterpolator(KeyframeTypes.ROTATION).interpolate(var1, var2);
   }

   public Vector3f getScale(IJoint var1, IAnimationProperty var2) {
      Timeline var3 = (Timeline)this.timelines.get(var1.getBlueprintJoint().getUuid());
      return var3 == null ? null : (Vector3f)var3.getInterpolator(KeyframeTypes.SCALE).interpolate(var1, var2);
   }

   public List<ScriptKeyframe.Script> getScript(IAnimationProperty var1) {
      return (List)this.globalTimeline.getInterpolator(KeyframeTypes.SCRIPT).interpolate((IJoint)null, var1);
   }

   public String getInterpolation(KeyframeType<?, ?> var1, UUID var2, float var3) {
      Timeline var4 = (Timeline)this.timelines.get(var2);
      if (var4 == null) {
         return null;
      } else {
         KeyframeInterpolator var5 = var4.getInterpolator(var1);
         AbstractKeyframe var6 = (AbstractKeyframe)var5.get(var3);
         if (var6 != null) {
            return var6.getInterpolation();
         } else {
            var6 = (AbstractKeyframe)var5.get(var5.getLowerKey(var3));
            return var6 == null ? null : var6.getInterpolation();
         }
      }
   }

   public ModelBlueprint getModelBlueprint() {
      return this.modelBlueprint;
   }

   public Map<UUID, Timeline> getTimelines() {
      return this.timelines;
   }

   public String getName() {
      return this.name;
   }

   public Timeline getGlobalTimeline() {
      return this.globalTimeline;
   }

   public double getLength() {
      return this.length;
   }

   public void setLength(double var1) {
      this.length = var1;
   }

   public BlueprintAnimation.LoopMode getLoopMode() {
      return this.loopMode;
   }

   public void setLoopMode(BlueprintAnimation.LoopMode var1) {
      this.loopMode = var1;
   }

   public boolean isOverride() {
      return this.override;
   }

   public void setOverride(boolean var1) {
      this.override = var1;
   }

   public static enum LoopMode {
      ONCE,
      HOLD,
      LOOP;

      public static BlueprintAnimation.LoopMode get(String var0) {
         try {
            return valueOf(var0.toUpperCase(Locale.ENGLISH));
         } catch (IllegalArgumentException var2) {
            return ONCE;
         }
      }

      @Nullable
      public static BlueprintAnimation.LoopMode getOrNull(String var0) {
         if (var0 == null) {
            return null;
         } else {
            try {
               return valueOf(var0.toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException var2) {
               return null;
            }
         }
      }

      private static BlueprintAnimation.LoopMode[] $values() {
         return new BlueprintAnimation.LoopMode[]{ONCE, HOLD, LOOP};
      }

      // $FF: synthetic method
      private static BlueprintAnimation.LoopMode[] $values$() {
         return new BlueprintAnimation.LoopMode[]{ONCE, HOLD, LOOP};
      }
   }
}
