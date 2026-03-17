package advancedplugins.pm2.cv.models.core.animation.handler;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationPropertyArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.Timeline;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.IPriorityHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.SimpleProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.AnimationEndEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.events.AnimationPlayEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.state.StateMachine;
import advancedplugins.pm2.cv.models.api.utils.state.StateNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.function.BiConsumer;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class PriorityHandler implements IPriorityHandler {
   private final IVisualModel visualModel;
   private final ModelBlueprint blueprint;
   private final Map<String, IAnimationProperty> properties = Maps.newConcurrentMap();
   private final Map<String, IAnimationProperty> updatedProperties = Maps.newConcurrentMap();
   private final Map<ModelState, AnimationHandler.DefaultProperty> defaultProperties = Maps.newConcurrentMap();
   private final StateMachine<BaseEntity<?>> stateMachine = new StateMachine();
   private boolean firstSpawn = true;

   public PriorityHandler(IVisualModel var1) {
      this.visualModel = var1;
      this.blueprint = var1.getBlueprint();
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.IDLE, 0.25D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.WALK, 0.25D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.STRAFE, 0.25D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP_START, 0.0D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP, 0.0D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP_END, 0.0D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.HOVER, 0.25D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.FLY, 0.25D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.SPAWN, 0.0D, 0.25D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.DEATH, 0.0D, 0.0D, 1.0D));
      this.configureAnimation();
   }

   public static PriorityHandler create(IVisualModel var0, SavedData var1) {
      PriorityHandler var2 = new PriorityHandler(var0);
      var2.load(var1);
      return var2;
   }

   private void configureAnimation() {
      StateNode var1 = this.stateMachine.createNode();
      StateNode var2 = this.stateMachine.createNode();
      StateNode var3 = this.stateMachine.createNode();
      StateNode var4 = this.stateMachine.createNode();
      StateNode var5 = this.stateMachine.createNode();
      StateNode var6 = this.stateMachine.createNode();
      StateNode var7 = this.stateMachine.createNode();
      StateNode var8 = this.stateMachine.createNode();
      StateNode var9 = this.stateMachine.createNode();
      StateNode var10 = this.stateMachine.createNode();
      var1.setExitAction((var1x) -> {
         this.playState(ModelState.SPAWN);
      });
      var1.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var1.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var1.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var1.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var5);
      var1.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var1.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var3);
      var1.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var2);
      var2.setEntryAction((var1x) -> {
         this.playState(ModelState.IDLE);
      });
      var2.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var2.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var2.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var2.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var5);
      var2.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var6);
      var2.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var2.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var3);
      var2.setExitAction((var1x) -> {
         this.stopState(ModelState.IDLE);
      });
      var3.setEntryAction((var1x) -> {
         this.playState(ModelState.WALK);
      });
      var3.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var3.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var3.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var3.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var5);
      var3.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var6);
      var3.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var3.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var2);
      var3.setExitAction((var1x) -> {
         this.stopState(ModelState.WALK);
      });
      var4.setEntryAction((var1x) -> {
         this.playState(ModelState.STRAFE);
      });
      var4.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var4.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var4.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var4.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var5);
      var4.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var6);
      var4.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var3);
      var4.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var2);
      var4.setExitAction((var1x) -> {
         this.stopState(ModelState.STRAFE);
      });
      var5.setEntryAction((var1x) -> {
         this.playState(ModelState.JUMP_START);
      });
      var5.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var5.setCommonPredicate((var1x) -> {
         return this.hasFinishedPlaying(ModelState.JUMP_START);
      });
      var5.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var5.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var5.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.hasAnimation(ModelState.JUMP_END);
      }, var7);
      var5.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.isWalking(this.visualModel);
      }, var3);
      var5.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && !this.isWalking(this.visualModel);
      }, var2);
      var5.addConnectedNode((var1x) -> {
         return this.hasFinishedPlaying(ModelState.JUMP_START) && this.hasAnimation(ModelState.JUMP);
      }, var6);
      var6.setEntryAction((var1x) -> {
         this.playState(ModelState.JUMP);
      });
      var6.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var6.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var6.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var6.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.hasAnimation(ModelState.JUMP_END);
      }, var7);
      var6.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.isWalking(this.visualModel);
      }, var3);
      var6.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && !this.isWalking(this.visualModel);
      }, var2);
      var6.setExitAction((var1x) -> {
         this.stopState(ModelState.JUMP);
      });
      var7.setEntryAction((var1x) -> {
         this.playState(ModelState.JUMP_END);
      });
      var7.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var7.setCommonPredicate((var1x) -> {
         return this.hasFinishedPlaying(ModelState.JUMP_END);
      });
      var7.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var7.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var7.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var5);
      var7.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var7.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var3);
      var7.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var2);
      var8.setEntryAction((var1x) -> {
         this.playState(ModelState.HOVER);
      });
      var8.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var8.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var9);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && this.isWalking(this.visualModel);
      }, var3);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && !this.isWalking(this.visualModel);
      }, var2);
      var8.setExitAction((var1x) -> {
         this.stopState(ModelState.HOVER);
      });
      var9.setEntryAction((var1x) -> {
         this.playState(ModelState.FLY);
      });
      var9.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var10);
      var9.addConnectedNode((var1x) -> {
         return var1x.isFlying() && !this.isWalking(this.visualModel) && this.hasAnimation(ModelState.HOVER);
      }, var8);
      var9.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var4);
      var9.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && this.isWalking(this.visualModel);
      }, var3);
      var9.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && !this.isWalking(this.visualModel);
      }, var2);
      var9.setExitAction((var1x) -> {
         this.stopState(ModelState.FLY);
      });
      var10.setEntryAction((var1x) -> {
         this.forceStopAllAnimations();
         this.playState(ModelState.DEATH);
      });
      this.stateMachine.setEntryNode(var1);
   }

   private boolean hasAnimation(ModelState var1) {
      return this.blueprint.getAnimations().containsKey(var1.getString());
   }

   private boolean hasFinishedPlaying(ModelState var1) {
      IAnimationProperty var2 = this.getAnimation(var1.getString());
      return var2 == null || var2.getPhase() == IAnimationProperty.Phase.LERPOUT;
   }

   private void stopState(ModelState var1) {
      AnimationHandler.DefaultProperty var2 = this.getDefaultProperty(var1);
      this.stopAnimation(var2.getAnimation());
   }

   public void prepare() {
      this.stateMachine.execute(this.visualModel.getModeledEntity().getBase());
      this.firstSpawn = false;
      this.updatedProperties.clear();
      this.updatedProperties.putAll(this.properties);
      Iterator var1 = this.blueprint.getAnimations().keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         IAnimationProperty var3 = (IAnimationProperty)this.updatedProperties.get(var2);
         if (var3 != null && !var3.update()) {
            this.updatedProperties.remove(var2);
            this.forceStopAnimation(var2);
         }
      }

   }

   public void updateJoint(IJoint var1) {
      var1.setHasGlobalRotation(false);
      KeyframeTypeArchive var2 = ModelAPI.getAPI().getKeyframeTypeArchive();
      Iterator var3 = var2.getKeys().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         KeyframeType var5 = (KeyframeType)var2.get(var4);
         if (!var5.isGlobal()) {
            Stack var6 = this.getUpdateStack(var5, var1);
            Collections.reverse(var6);
            var6.forEach((var3x) -> {
               Timeline var4 = (Timeline)var3x.getBlueprintAnimation().getTimelines().get(var1.getBlueprintJoint().getUuid());
               if (var4 != null && var4.isGlobalRotation()) {
                  var1.setHasGlobalRotation(true);
               }

               var5.updateJoint(IPriorityHandler.class, this, var1, var3x);
            });
         }
      }

   }

   public boolean hasFinishedAllAnimations() {
      Iterator var1 = this.properties.values().iterator();

      IAnimationProperty var2;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         var2 = (IAnimationProperty)var1.next();
      } while(var2.isFinished());

      return false;
   }

   public void setDefaultProperty(AnimationHandler.DefaultProperty var1) {
      this.defaultProperties.put(var1.getState(), var1);
   }

   public AnimationHandler.DefaultProperty getDefaultProperty(ModelState var1) {
      return (AnimationHandler.DefaultProperty)this.defaultProperties.get(var1);
   }

   private Stack<IAnimationProperty> getUpdateStack(KeyframeType<?, ?> var1, IJoint var2) {
      Stack var3 = new Stack();
      UUID var4 = var2.getBlueprintJoint().getUuid();
      Iterator var5 = this.blueprint.getAnimationDescendingPriority().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         IAnimationProperty var7 = (IAnimationProperty)this.updatedProperties.get(var6);
         if (var7 != null) {
            var3.push(var7);
            if (var7.isOverride() && var7.containsKeyframe(var1, var4) && var7.getPhase() == IAnimationProperty.Phase.PLAY) {
               break;
            }
         }
      }

      return var3;
   }

   public void tickGlobal() {
      Iterator var1 = this.blueprint.getAnimations().keySet().iterator();

      while(true) {
         IAnimationProperty var3;
         do {
            if (!var1.hasNext()) {
               return;
            }

            String var2 = (String)var1.next();
            var3 = (IAnimationProperty)this.properties.get(var2);
         } while(var3 == null);

         KeyframeTypeArchive var4 = ModelAPI.getAPI().getKeyframeTypeArchive();
         Iterator var5 = var4.getKeys().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            KeyframeType var7 = (KeyframeType)var4.get(var6);
            if (var7.isGlobal()) {
               var7.updateModel(IPriorityHandler.class, this, var3);
            }
         }
      }
   }

   public void forEachProperty(BiConsumer<String, IAnimationProperty> var1) {
      this.properties.forEach(var1);
   }

   @Nullable
   public IAnimationProperty getAnimation(String var1) {
      return (IAnimationProperty)this.properties.get(var1);
   }

   public Map<String, IAnimationProperty> getAnimations() {
      return ImmutableMap.copyOf(this.properties);
   }

   @Nullable
   public IAnimationProperty playAnimation(String var1, double var2, double var4, double var6, boolean var8) {
      BlueprintAnimation var9 = (BlueprintAnimation)this.blueprint.getAnimations().get(var1);
      if (var9 == null) {
         return null;
      } else {
         SimpleProperty var10 = new SimpleProperty(this.visualModel, var9, var2, var4, var6);
         return this.playAnimation(var10, var8) ? var10 : null;
      }
   }

   public boolean playAnimation(IAnimationProperty var1, boolean var2) {
      AnimationPlayEvent var3 = new AnimationPlayEvent(this.visualModel, var1);
      ModelAPI.callEvent(var3);
      if (var3.isCancelled()) {
         return false;
      } else {
         String var4 = var1.getName();
         if (!this.properties.containsKey(var4)) {
            this.properties.put(var4, var1);
            return true;
         } else {
            IAnimationProperty var5 = (IAnimationProperty)this.properties.get(var4);
            if (!var2 && !var5.canReplace()) {
               return false;
            } else {
               this.properties.put(var4, var1);
               return true;
            }
         }
      }
   }

   public void playState(ModelState var1) {
      AnimationHandler.DefaultProperty var2 = this.getDefaultProperty(var1);
      IAnimationProperty var3 = this.firstSpawn ? var2.build(this.visualModel, 0.0D, var2.getLerpOut(), var2.getSpeed()) : var2.build(this.visualModel);
      if (var3 != null) {
         var3.setForceLoopMode(var1.getLoopMode());
         var3.setForceOverride(var1.isOverride());
         this.playAnimation(var3, false);
      }

   }

   public boolean isPlayingAnimation(String var1) {
      return this.properties.containsKey(var1);
   }

   public void stopAnimation(String var1) {
      IAnimationProperty var2 = (IAnimationProperty)this.properties.get(var1);
      if (var2 != null) {
         if (var2.getLerpOut() > 1.0E-5D) {
            var2.stop();
         } else {
            this.forceStopAnimation(var1);
         }
      }

   }

   public void forceStopAnimation(String var1) {
      IAnimationProperty var2 = (IAnimationProperty)this.properties.remove(var1);
      if (var2 != null) {
         AnimationEndEvent var3 = new AnimationEndEvent(this.visualModel, var2);
         ModelAPI.callEvent(var3);
      }

   }

   public void forceStopAllAnimations() {
      Iterator var1 = this.properties.values().iterator();

      while(var1.hasNext()) {
         IAnimationProperty var2 = (IAnimationProperty)var1.next();
         AnimationEndEvent var3 = new AnimationEndEvent(this.visualModel, var2);
         ModelAPI.callEvent(var3);
      }

      this.properties.clear();
   }

   public void save(SavedData var1) {
      IPriorityHandler.super.save(var1);
      SavedData var2 = new SavedData();
      this.getAnimations().forEach((var1x, var2x) -> {
         var2x.save().ifPresent((var2xx) -> {
            var2.putData(var1x, var2xx);
         });
      });
      var1.putData("animations", var2);
   }

   public void load(SavedData var1) {
      IPriorityHandler.super.load(var1);
      AnimationPropertyArchive var2 = ModelAPI.getAnimationPropertyArchive();
      var1.getData("animations").ifPresent((var2x) -> {
         Iterator var3 = var2x.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2x.getData(var4).ifPresent((var2xx) -> {
               IAnimationProperty var3 = var2.createAnimationProperty(this, var2xx);
               this.playAnimation(var3, true);
            });
         }

      });
   }

   @Generated
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @Generated
   public ModelBlueprint getBlueprint() {
      return this.blueprint;
   }

   @Generated
   public Map<String, IAnimationProperty> getProperties() {
      return this.properties;
   }

   @Generated
   public Map<String, IAnimationProperty> getUpdatedProperties() {
      return this.updatedProperties;
   }

   @Generated
   public Map<ModelState, AnimationHandler.DefaultProperty> getDefaultProperties() {
      return this.defaultProperties;
   }

   @Generated
   public StateMachine<BaseEntity<?>> getStateMachine() {
      return this.stateMachine;
   }

   @Generated
   public boolean isFirstSpawn() {
      return this.firstSpawn;
   }

   @Generated
   public void setFirstSpawn(boolean var1) {
      this.firstSpawn = var1;
   }
}
