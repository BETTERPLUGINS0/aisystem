package advancedplugins.pm2.cv.models.api.model.rpc.animation.property;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.Timeline;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.ScriptKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SimpleProperty implements IAnimationProperty {
   private final IVisualModel model;
   private final BlueprintAnimation blueprintAnimation;
   private final double lerpIn;
   private final double lerpOut;
   private double lerpInTime;
   private double lerpOutTime;
   private double lastTime;
   private double time;
   private double speed;
   @NotNull
   private IAnimationProperty.Phase phase;
   private BlueprintAnimation.LoopMode forceLoopMode;
   private boolean forceOverride;
   private boolean skipLastFrame;
   private boolean stopping;
   private boolean ended;

   public SimpleProperty(IVisualModel var1, BlueprintAnimation var2) {
      this(var1, var2, 0.0D, 0.0D, 1.0D);
   }

   public SimpleProperty(IVisualModel var1, BlueprintAnimation var2, double var3, double var5, double var7) {
      this.lerpInTime = 0.0D;
      this.lerpOutTime = 0.0D;
      this.lastTime = -1.0D;
      this.time = -1.0D;
      this.phase = IAnimationProperty.Phase.LERPIN;
      this.forceLoopMode = null;
      this.forceOverride = false;
      this.model = var1;
      this.blueprintAnimation = var2;
      this.lerpIn = var3;
      this.lerpOut = var5;
      this.speed = var7;
   }

   public static SimpleProperty create(AnimationHandler var0, SavedData var1) {
      IVisualModel var2 = var0.getVisualModel();
      ModelBlueprint var3 = var2.getBlueprint();
      BlueprintAnimation var4 = (BlueprintAnimation)var3.getAnimations().get(var1.getString("name"));
      SimpleProperty var5 = new SimpleProperty(var2, var4, var1.getDouble("lerp_in", 0.0D), var1.getDouble("lerp_out", 0.0D), var1.getDouble("speed", 1.0D));
      var5.load(var1);
      return var5;
   }

   public boolean update() {
      this.lastTime = this.time;
      boolean var1;
      switch(this.phase) {
      case LERPIN:
         var1 = this.updateLerpIn();
         break;
      case PLAY:
         var1 = this.updateTime();
         break;
      case LERPOUT:
         var1 = this.updateLerpOut();
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var1;
   }

   private boolean updateLerpIn() {
      if (this.lerpInTime >= this.lerpIn - 1.0E-5D) {
         this.time = 0.0D;
         return this.stopping ? this.updateLerpOut() : this.updateTime();
      } else {
         this.lerpInTime += this.speed * 0.05D;
         return this.playingOrLerpOut();
      }
   }

   private boolean updateTime() {
      if (this.phase == IAnimationProperty.Phase.LERPIN) {
         this.phase = IAnimationProperty.Phase.PLAY;
         return this.playingOrLerpOut();
      } else {
         BlueprintAnimation.LoopMode var1 = this.getLoopMode();
         switch(var1) {
         case ONCE:
            if (this.time < this.blueprintAnimation.getLength()) {
               this.time = Math.min(this.time + this.speed * 0.05D, this.blueprintAnimation.getLength());
               return this.playingOrLerpOut();
            }

            return this.updateLerpOut();
         case HOLD:
            this.time = Math.min(this.time + this.speed * 0.05D, this.blueprintAnimation.getLength());
            return this.playingOrLerpOut();
         case LOOP:
            this.time = (this.time + this.speed * 0.05D) % (this.skipLastFrame ? this.blueprintAnimation.getLength() : this.blueprintAnimation.getLength() + this.speed * 0.05D);
            return this.playingOrLerpOut();
         default:
            return false;
         }
      }
   }

   private boolean updateLerpOut() {
      if (this.phase != IAnimationProperty.Phase.LERPOUT && this.lerpOut > 1.0E-5D) {
         this.phase = IAnimationProperty.Phase.LERPOUT;
         return true;
      } else if (this.lerpOutTime >= this.lerpOut - 1.0E-5D) {
         this.ended = true;
         return false;
      } else {
         this.lerpOutTime += this.speed * 0.05D;
         return true;
      }
   }

   private boolean playingOrLerpOut() {
      return !this.stopping || this.updateLerpOut();
   }

   public void stop() {
      this.stopping = true;
   }

   public boolean canReplace() {
      return this.stopping || this.phase == IAnimationProperty.Phase.LERPOUT || this.ended;
   }

   public String getName() {
      return this.blueprintAnimation.getName();
   }

   public boolean containsKeyframe(KeyframeType<?, ?> var1, UUID var2) {
      Timeline var3 = (Timeline)this.blueprintAnimation.getTimelines().get(var2);
      if (var3 == null) {
         return false;
      } else {
         return var3.hasInterpolator(var1) && !var3.getInterpolator(var1).isEmpty();
      }
   }

   public Vector3f getPositionFrame(IJoint var1) {
      return this.blueprintAnimation.getPosition(var1, this);
   }

   public Vector3f getVelocityFrame(IJoint var1) {
      return this.blueprintAnimation.getVelocity(var1, this);
   }

   public Vector3f getRotationFrame(IJoint var1) {
      return this.blueprintAnimation.getRotation(var1, this);
   }

   public Vector3f getScaleFrame(IJoint var1) {
      return this.blueprintAnimation.getScale(var1, this);
   }

   public List<ScriptKeyframe.Script> getScriptFrame() {
      return this.blueprintAnimation.getScript(this);
   }

   public double getLerpInRatio() {
      return MathUtils.clamp(this.lerpInTime / this.lerpIn, 0.0D, 1.0D);
   }

   public double getLerpOutRatio() {
      return MathUtils.clamp(this.lerpOutTime / this.lerpOut, 0.0D, 1.0D);
   }

   public boolean isFinished() {
      return this.phase == IAnimationProperty.Phase.LERPOUT || this.time >= this.blueprintAnimation.getLength();
   }

   public BlueprintAnimation.LoopMode getLoopMode() {
      return this.forceLoopMode == null ? this.blueprintAnimation.getLoopMode() : this.forceLoopMode;
   }

   public boolean isOverride() {
      return this.blueprintAnimation.isOverride() || this.forceOverride;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.lerpIn, this.lerpOut, this.time, this.speed, this.phase, this.forceLoopMode, this.forceOverride});
   }

   public void save(SavedData var1) {
      var1.putString("id", "simple");
      var1.putString("name", this.getName());
      var1.putDouble("lerp_in", this.lerpIn);
      var1.putDouble("lerp_out", this.lerpOut);
      var1.putDouble("lerp_in_time", this.lerpInTime);
      var1.putDouble("lerp_out_time", this.lerpOutTime);
      var1.putDouble("last_time", this.lastTime);
      var1.putDouble("time", this.time);
      var1.putDouble("speed", this.speed);
      var1.putString("phase", this.phase.name());
      if (this.forceLoopMode != null) {
         var1.putString("force_loop_mode", this.forceLoopMode.name());
      }

      var1.putBoolean("force_override", this.forceOverride);
   }

   public void load(SavedData var1) {
      this.lerpInTime = var1.getDouble("lerp_in_time");
      this.lerpOutTime = var1.getDouble("lerp_out_time");
      this.lastTime = var1.getDouble("last_time");
      this.time = var1.getDouble("time");
      this.phase = IAnimationProperty.Phase.valueOf(var1.getString("phase"));
      var1.loadIfExist("force_loop_mode", SavedData::getString, (var1x) -> {
         this.forceLoopMode = BlueprintAnimation.LoopMode.getOrNull(var1x);
      });
      this.forceOverride = var1.getBoolean("force_override");
   }

   public String toString() {
      String var1 = String.valueOf(this.getModel());
      return "SimpleProperty(model=" + var1 + ", blueprintAnimation=" + String.valueOf(this.getBlueprintAnimation()) + ", lerpIn=" + this.getLerpIn() + ", lerpOut=" + this.getLerpOut() + ", lerpInTime=" + this.getLerpInTime() + ", lerpOutTime=" + this.getLerpOutTime() + ", lastTime=" + this.getLastTime() + ", time=" + this.getTime() + ", speed=" + this.getSpeed() + ", phase=" + String.valueOf(this.getPhase()) + ", forceLoopMode=" + String.valueOf(this.getForceLoopMode()) + ", forceOverride=" + this.isForceOverride() + ", skipLastFrame=" + this.isSkipLastFrame() + ", stopping=" + this.isStopping() + ", ended=" + this.isEnded() + ")";
   }

   public IVisualModel getModel() {
      return this.model;
   }

   public BlueprintAnimation getBlueprintAnimation() {
      return this.blueprintAnimation;
   }

   public double getLerpIn() {
      return this.lerpIn;
   }

   public double getLerpOut() {
      return this.lerpOut;
   }

   public double getLerpInTime() {
      return this.lerpInTime;
   }

   public void setLerpInTime(double var1) {
      this.lerpInTime = var1;
   }

   public double getLerpOutTime() {
      return this.lerpOutTime;
   }

   public void setLerpOutTime(double var1) {
      this.lerpOutTime = var1;
   }

   public double getLastTime() {
      return this.lastTime;
   }

   public double getTime() {
      return this.time;
   }

   public double getSpeed() {
      return this.speed;
   }

   public void setSpeed(double var1) {
      this.speed = var1;
   }

   @NotNull
   public IAnimationProperty.Phase getPhase() {
      return this.phase;
   }

   public void setPhase(@NotNull IAnimationProperty.Phase var1) {
      this.phase = var1;
   }

   public BlueprintAnimation.LoopMode getForceLoopMode() {
      return this.forceLoopMode;
   }

   public void setForceLoopMode(BlueprintAnimation.LoopMode var1) {
      this.forceLoopMode = var1;
   }

   public boolean isForceOverride() {
      return this.forceOverride;
   }

   public void setForceOverride(boolean var1) {
      this.forceOverride = var1;
   }

   public boolean isSkipLastFrame() {
      return this.skipLastFrame;
   }

   public void setSkipLastFrame(boolean var1) {
      this.skipLastFrame = var1;
   }

   public boolean isStopping() {
      return this.stopping;
   }

   public boolean isEnded() {
      return this.ended;
   }
}
