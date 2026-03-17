package advancedplugins.pm2.cv.vehicle.model.compound;

import advancedplugins.pm2.cv.api.enums.EnumLoopMode;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.model.AnimationPlayer;
import advancedplugins.pm2.cv.vehicle.model.compound.animPlayer.BlockBenchAnimationPlayer;
import advancedplugins.pm2.cv.vehicle.model.compound.animPlayer.MatrixAnimationPlayer;

public class AnimationTicker {
   private final CompoundModel model;
   private final Animation animation;
   private final AnimationPlayer player;
   private final EnumLoopMode loopMode;
   private int currentFrame;
   private int waitTicks;
   private boolean started;
   private boolean finished;

   AnimationTicker(AnimationConfiguration config, CompoundModel model) {
      this.model = var2;
      this.animation = new Animation(var1, var2);
      this.loopMode = var1.getLoopMode();
      this.player = (AnimationPlayer)(this.isBlockBenchAnimation(var2) ? new BlockBenchAnimationPlayer(var2, this.animation) : new MatrixAnimationPlayer(var2, this.animation, var1.getInterpolationMode()));
   }

   void tick() {
      if (!this.finished && this.started) {
         if (this.animation.keyframes.length == 0) {
            this.finish();
         } else if (this.waitTicks > 0) {
            --this.waitTicks;
            this.player.updateProgress(this.currentFrame, this.getProgress());
         } else {
            if (this.advanceFrame()) {
               this.playCurrentFrame();
            }

         }
      }
   }

   void start() {
      if (!this.started) {
         this.started = true;
         this.player.start();
      }
   }

   void stop() {
      this.player.stop();
   }

   private boolean advanceFrame() {
      if (this.currentFrame + 1 < this.animation.keyframes.length) {
         ++this.currentFrame;
         return true;
      } else if (this.loopMode == EnumLoopMode.LOOP) {
         this.currentFrame = 0;
         return true;
      } else {
         if (this.loopMode == EnumLoopMode.NONE) {
            this.finish();
         }

         return false;
      }
   }

   private void playCurrentFrame() {
      Animation.Keyframe var1 = this.animation.keyframes[this.currentFrame];
      this.waitTicks = Math.max(var1.duration, 1);
      if (this.currentFrame == this.animation.keyframes.length - 1) {
         this.waitTicks += this.animation.endDelay;
      }

      this.player.playFrame(this.currentFrame);
   }

   private float getProgress() {
      Animation.Keyframe var1 = this.animation.keyframes[this.currentFrame];
      int var2 = Math.max(var1.duration, 1);
      return 1.0F - (float)this.waitTicks / (float)var2;
   }

   private void finish() {
      this.finished = true;
      this.stop();
   }

   private boolean isBlockBenchAnimation(CompoundModel model) {
      return ((CompoundModelConfiguration)var1.getConfiguration()).getModelID() != null;
   }

   public CompoundModel getModel() {
      return this.model;
   }

   public Animation getAnimation() {
      return this.animation;
   }

   public AnimationPlayer getPlayer() {
      return this.player;
   }

   public EnumLoopMode getLoopMode() {
      return this.loopMode;
   }

   public int getCurrentFrame() {
      return this.currentFrame;
   }

   public int getWaitTicks() {
      return this.waitTicks;
   }

   public boolean isStarted() {
      return this.started;
   }

   public boolean isFinished() {
      return this.finished;
   }
}
