package advancedplugins.pm2.cv.models.core.animation.handler;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationPropertyArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.IStateMachineHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.SimpleProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.AnimationEndEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.events.AnimationPlayEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.state.StateMachine;
import advancedplugins.pm2.cv.models.api.utils.state.StateNode;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class StateMachineHandler implements IStateMachineHandler {
   private final IVisualModel visualModel;
   private final ModelBlueprint blueprint;
   private final Map<ModelState, AnimationHandler.DefaultProperty> defaultProperties = Maps.newConcurrentMap();
   private final TreeMap<Integer, StateMachineHandler.AnimationStateMachine> stateMachines = new TreeMap();
   private final Queue<Runnable> actionQueue = new ConcurrentLinkedQueue();
   private boolean firstSpawn = true;

   public StateMachineHandler(IVisualModel var1) {
      this.visualModel = var1;
      this.blueprint = var1.getBlueprint();
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.IDLE, 0.25D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.WALK, 0.25D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.STRAFE, 0.25D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP_START, 0.0D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP, 0.0D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP_END, 0.0D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.HOVER, 0.25D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.FLY, 0.25D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.SPAWN, 0.0D, 0.0D, 1.0D));
      this.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.DEATH, 0.0D, 0.0D, 1.0D));
      this.configureAnimation();
   }

   public static StateMachineHandler create(IVisualModel var0, SavedData var1) {
      StateMachineHandler var2 = new StateMachineHandler(var0);
      var2.load(var1);
      return var2;
   }

   private void configureAnimation() {
      StateMachineHandler.AnimationStateMachine var1 = new StateMachineHandler.AnimationStateMachine(false);
      StateNode var2 = var1.getRootNode();
      StateNode var3 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.SPAWN);
      });
      StateNode var4 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.IDLE);
      });
      StateNode var5 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.WALK);
      });
      StateNode var6 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.STRAFE);
      });
      StateNode var7 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.JUMP_START);
      });
      StateNode var8 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.JUMP);
      });
      StateNode var9 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.JUMP_END);
      });
      StateNode var10 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.HOVER);
      });
      StateNode var11 = var1.createAnimationNode(() -> {
         return this.createStateProperty(ModelState.FLY);
      });
      StateNode var12 = var1.createAnimationNode(() -> {
         this.forceStopAllAnimations();
         return this.createStateProperty(ModelState.DEATH);
      });
      var2.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var2.addConnectedNode((var1x) -> {
         return this.hasAnimation(ModelState.SPAWN);
      }, var3);
      var2.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var2.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var2.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var2.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var8);
      var2.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var2.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var5);
      var2.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var4);
      var3.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var3.setCommonPredicate((var1x) -> {
         return var1.hasFinishedPlaying(ModelState.SPAWN);
      });
      var3.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var3.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var3.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var3.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var8);
      var3.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var3.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var5);
      var3.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var4);
      var4.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var4.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var4.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var4.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var4.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var8);
      var4.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var4.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var5);
      var5.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var5.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var5.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var5.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var5.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var8);
      var5.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var5.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var4);
      var6.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var6.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var6.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var6.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var6.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP);
      }, var8);
      var6.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var5);
      var6.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var4);
      var7.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var7.setCommonPredicate((var1x) -> {
         return var1.hasFinishedPlaying(ModelState.JUMP_START);
      });
      var7.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var7.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var7.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.hasAnimation(ModelState.JUMP_END);
      }, var9);
      var7.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.isWalking(this.visualModel);
      }, var5);
      var7.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && !this.isWalking(this.visualModel);
      }, var4);
      var8.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var8.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var8.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.hasAnimation(ModelState.JUMP_END);
      }, var9);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && this.isWalking(this.visualModel);
      }, var5);
      var8.addConnectedNode((var1x) -> {
         return !var1x.isJumping() && !this.isWalking(this.visualModel);
      }, var4);
      var9.addForceConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var9.setCommonPredicate((var1x) -> {
         return var1.hasFinishedPlaying(ModelState.JUMP_END);
      });
      var9.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var9.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var9.addConnectedNode((var1x) -> {
         return var1x.isJumping() && this.hasAnimation(ModelState.JUMP_START);
      }, var7);
      var9.addConnectedNode((var1x) -> {
         return var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var9.addConnectedNode((var1x) -> {
         return this.isWalking(this.visualModel);
      }, var5);
      var9.addConnectedNode((var1x) -> {
         return !this.isWalking(this.visualModel);
      }, var4);
      var10.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var10.addConnectedNode((var1x) -> {
         return var1x.isFlying() && this.isWalking(this.visualModel) && this.hasAnimation(ModelState.FLY);
      }, var11);
      var10.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var10.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && this.isWalking(this.visualModel);
      }, var5);
      var10.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && !this.isWalking(this.visualModel);
      }, var4);
      var11.addConnectedNode((var0) -> {
         return !var0.isAlive();
      }, var12);
      var11.addConnectedNode((var1x) -> {
         return var1x.isFlying() && !this.isWalking(this.visualModel) && this.hasAnimation(ModelState.HOVER);
      }, var10);
      var11.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && var1x.isStrafing() && this.hasAnimation(ModelState.STRAFE);
      }, var6);
      var11.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && this.isWalking(this.visualModel);
      }, var5);
      var11.addConnectedNode((var1x) -> {
         return !var1x.isFlying() && !this.isWalking(this.visualModel);
      }, var4);
      this.stateMachines.put(0, var1);
   }

   public void prepare() {
      while(!this.actionQueue.isEmpty()) {
         ((Runnable)this.actionQueue.poll()).run();
      }

      synchronized(this.stateMachines) {
         this.stateMachines.values().forEach((var1) -> {
            var1.execute(this.visualModel.getModeledEntity().getBase());
         });
      }

      this.firstSpawn = false;
   }

   public void updateJoint(IJoint var1) {
      var1.setHasGlobalRotation(false);
      KeyframeTypeArchive var2 = ModelAPI.getAPI().getKeyframeTypeArchive();
      Iterator var3 = var2.getKeys().iterator();

      while(true) {
         KeyframeType var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            String var5 = (String)var3.next();
            var4 = (KeyframeType)var2.get(var5);
         } while(var4.isGlobal());

         Stack var9 = this.getUpdateStack(var4, var1);

         while(!var9.isEmpty()) {
            StateMachineHandler.AnimationStateMachine var6 = (StateMachineHandler.AnimationStateMachine)var9.pop();
            IAnimationProperty var7 = var6.currentAnimation;
            IAnimationProperty var8 = var6.lastAnimation;
            var4.updateJoint(IStateMachineHandler.class, this, var1, var7, var8);
         }
      }
   }

   public boolean hasFinishedAllAnimations() {
      synchronized(this.stateMachines) {
         Iterator var2 = this.stateMachines.values().iterator();

         StateMachineHandler.AnimationStateMachine var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = (StateMachineHandler.AnimationStateMachine)var2.next();
         } while(var3.currentAnimation == null || var3.currentAnimation.isFinished());

         return false;
      }
   }

   public void setDefaultProperty(AnimationHandler.DefaultProperty var1) {
      this.defaultProperties.put(var1.getState(), var1);
   }

   public AnimationHandler.DefaultProperty getDefaultProperty(ModelState var1) {
      return (AnimationHandler.DefaultProperty)this.defaultProperties.get(var1);
   }

   public void tickGlobal() {
      synchronized(this.stateMachines) {
         Iterator var2 = this.stateMachines.values().iterator();

         while(true) {
            StateMachineHandler.AnimationStateMachine var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (StateMachineHandler.AnimationStateMachine)var2.next();
            } while(var3.currentAnimation == null);

            KeyframeTypeArchive var4 = ModelAPI.getAPI().getKeyframeTypeArchive();
            Iterator var5 = var4.getKeys().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               KeyframeType var7 = (KeyframeType)var4.get(var6);
               if (var7.isGlobal()) {
                  var7.updateModel(IStateMachineHandler.class, this, var3.currentAnimation, var3.lastAnimation);
               }
            }
         }
      }
   }

   @Nullable
   public IAnimationProperty playAnimation(String var1, double var2, double var4, double var6, boolean var8) {
      return this.playAnimation(1, var1, var2, var4, var6, var8);
   }

   public boolean playAnimation(IAnimationProperty var1, boolean var2) {
      return this.playAnimation(1, var1, var2);
   }

   private Stack<StateMachineHandler.AnimationStateMachine> getUpdateStack(KeyframeType<?, ?> var1, IJoint var2) {
      Stack var3 = new Stack();
      if (!this.stateMachines.isEmpty()) {
         UUID var4 = var2.getBlueprintJoint().getUuid();

         for(Entry var5 = this.stateMachines.lastEntry(); var5 != null; var5 = this.stateMachines.lowerEntry((Integer)var5.getKey())) {
            StateMachineHandler.AnimationStateMachine var6 = (StateMachineHandler.AnimationStateMachine)var5.getValue();
            IAnimationProperty var7 = var6.currentAnimation;
            IAnimationProperty var8 = var6.lastAnimation;
            if (var7 != null) {
               var3.push(var6);
               if (this.isLastProperty(var7, var1, var4) && (var8 == null || this.isLastProperty(var8, var1, var4))) {
                  break;
               }
            }
         }
      }

      return var3;
   }

   private boolean isLastProperty(IAnimationProperty var1, KeyframeType<?, ?> var2, UUID var3) {
      return var1.isOverride() && var1.containsKeyframe(var2, var3) && var1.getPhase() == IAnimationProperty.Phase.PLAY;
   }

   @Nullable
   public IAnimationProperty getAnimation(String var1) {
      synchronized(this.stateMachines) {
         Iterator var3 = this.stateMachines.values().iterator();

         while(var3.hasNext()) {
            StateMachineHandler.AnimationStateMachine var4 = (StateMachineHandler.AnimationStateMachine)var3.next();
            if (var4.isPlaying(var1)) {
               return var4.currentAnimation;
            }
         }

         return null;
      }
   }

   public Map<String, IAnimationProperty> getAnimations() {
      ConcurrentHashMap var1 = new ConcurrentHashMap();
      Iterator var2 = this.stateMachines.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         StateMachineHandler.AnimationStateMachine var5 = (StateMachineHandler.AnimationStateMachine)var3.getValue();
         IAnimationProperty var6 = var5.getCurrentAnimation();
         if (var6 != null) {
            var1.put(var4 + ":" + var6.getName(), var6);
         }
      }

      return var1;
   }

   @Nullable
   public IAnimationProperty getAnimation(int var1, String var2) {
      StateMachineHandler.AnimationStateMachine var3 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.get(var1);
      return var3 != null && var3.isPlaying(var2) ? var3.currentAnimation : null;
   }

   @Nullable
   public IAnimationProperty playAnimation(int var1, String var2, double var3, double var5, double var7, boolean var9) {
      BlueprintAnimation var10 = (BlueprintAnimation)this.blueprint.getAnimations().get(var2);
      if (var10 == null) {
         return null;
      } else {
         SimpleProperty var11 = new SimpleProperty(this.visualModel, var10, var3, var5, var7);
         return this.playAnimation(var1, var11, var9) ? var11 : null;
      }
   }

   public boolean playAnimation(int var1, IAnimationProperty var2, boolean var3) {
      AnimationPlayEvent var4 = new AnimationPlayEvent(this.visualModel, var2);
      ModelAPI.callEvent(var4);
      if (var4.isCancelled()) {
         return false;
      } else {
         synchronized(this.stateMachines) {
            StateMachineHandler.AnimationStateMachine var6 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.computeIfAbsent(var1, (var1x) -> {
               return new StateMachineHandler.AnimationStateMachine(true);
            });
            if (!var3 && var6.isPlaying(var2.getName())) {
               return false;
            } else {
               StateNode var7 = var6.getCurrentNode();
               StateNode var8 = var6.createAnimationNode(() -> {
                  return var2;
               });
               var8.addConnectedNode((var1x) -> {
                  return var2.isEnded();
               }, var6.getRootNode());
               var7.addForceConnectedNode((var0) -> {
                  return true;
               }, var8);
               return true;
            }
         }
      }
   }

   public void refreshState(AnimationHandler.DefaultProperty var1) {
      StateMachineHandler.AnimationStateMachine var2 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.get(0);
      if (var2 != null && var2.isPlaying(var1.getAnimation())) {
         var2.forceReentry(this.visualModel.getModeledEntity().getBase());
      }

   }

   public boolean isPlayingAnimation(String var1) {
      return this.getAnimation(var1) != null;
   }

   public boolean isPlayingAnimation(int var1, String var2) {
      return this.getAnimation(var1, var2) != null;
   }

   public void stopAnimation(String var1) {
      this.stateMachines.keySet().forEach((var2) -> {
         this.stopAnimation(var2, var1);
      });
   }

   public void stopAnimation(int var1, String var2) {
      StateMachineHandler.AnimationStateMachine var3 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.get(var1);
      if (var3 != null && var3.isPlaying(var2)) {
         if (((IAnimationProperty)Objects.requireNonNull(var3.currentAnimation)).getLerpOut() > 1.0E-5D) {
            var3.currentAnimation.stop();
         } else {
            var3.getCurrentNode().addForceConnectedNode((var0) -> {
               return true;
            }, var3.getRootNode());
         }
      }

   }

   public void forceStopAnimation(String var1) {
      this.stateMachines.keySet().forEach((var2) -> {
         this.forceStopAnimation(var2, var1);
      });
   }

   public void forceStopAnimation(int var1, String var2) {
      StateMachineHandler.AnimationStateMachine var3 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.get(var1);
      if (var3 != null && var3.isPlaying(var2)) {
         var3.getCurrentNode().addForceConnectedNode((var0) -> {
            return true;
         }, var3.getRootNode());
      }

   }

   public void forceStopAllAnimations() {
      this.actionQueue.add(() -> {
         StateMachineHandler.AnimationStateMachine var1 = (StateMachineHandler.AnimationStateMachine)this.stateMachines.get(0);
         this.stateMachines.clear();
         if (var1 != null) {
            this.stateMachines.put(0, var1);
         }

      });
   }

   private boolean hasAnimation(ModelState var1) {
      return this.blueprint.getAnimations().containsKey(var1.getString());
   }

   @Nullable
   private IAnimationProperty createStateProperty(ModelState var1) {
      AnimationHandler.DefaultProperty var2 = this.getDefaultProperty(var1);
      IAnimationProperty var3 = this.firstSpawn ? var2.build(this.visualModel, 0.0D, var2.getLerpOut(), var2.getSpeed()) : var2.build(this.visualModel);
      if (var3 != null) {
         var3.setForceLoopMode(var1.getLoopMode());
         var3.setForceOverride(var1.isOverride());
      }

      return var3;
   }

   public void save(SavedData var1) {
      IStateMachineHandler.super.save(var1);
      SavedData var2 = new SavedData();
      this.stateMachines.forEach((var1x, var2x) -> {
         var2x.save().ifPresent((var2xx) -> {
            var2.putData(Integer.toString(var1x), var2xx);
         });
      });
      var1.putData("state_machines", var2);
   }

   public void load(SavedData var1) {
      IStateMachineHandler.super.load(var1);
      var1.getData("state_machines").ifPresent((var1x) -> {
         Iterator var2 = var1x.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            int var4 = Integer.parseInt(var3);
            var1x.getData(var3).ifPresent((var2x) -> {
               StateMachineHandler.AnimationStateMachine var3 = new StateMachineHandler.AnimationStateMachine(true);
               var3.load(var2x);
               this.stateMachines.put(var4, var3);
            });
         }

      });
   }

   @Generated
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @Generated
   public TreeMap<Integer, StateMachineHandler.AnimationStateMachine> getStateMachines() {
      return this.stateMachines;
   }

   public class AnimationStateMachine extends StateMachine<BaseEntity<?>> implements DataIO {
      protected final boolean saved;
      protected StateNode<BaseEntity<?>> rootNode;
      @Nullable
      protected IAnimationProperty lastAnimation;
      @Nullable
      protected IAnimationProperty currentAnimation;

      public AnimationStateMachine(boolean param2) {
         this.saved = var2;
      }

      public StateNode<BaseEntity<?>> getCurrentNode() {
         return this.currentNode == null ? this.getRootNode() : this.currentNode;
      }

      public StateNode<BaseEntity<?>> getRootNode() {
         if (this.rootNode == null) {
            this.rootNode = new StateNode(this);
            this.rootNode.setEntryAction((var1) -> {
               this.currentAnimation = null;
            });
            this.rootNode.setExitAction((var1) -> {
               this.rootNode.clearForceConnectedNodes();
               this.rootNode.clearConnectedNodes();
               this.lastAnimation = this.currentAnimation;
            });
            this.setEntryNode(this.rootNode);
         }

         return this.rootNode;
      }

      public StateNode<BaseEntity<?>> createAnimationNode(Supplier<IAnimationProperty> var1) {
         StateNode var2 = new StateNode(this);
         var2.setEntryAction((var2x) -> {
            this.currentAnimation = (IAnimationProperty)var1.get();
         });
         var2.setAction((var1x) -> {
            if (this.currentAnimation != null) {
               this.currentAnimation.update();
            }

         });
         var2.setExitAction((var1x) -> {
            AnimationEndEvent var2 = new AnimationEndEvent(StateMachineHandler.this.visualModel, this.currentAnimation);
            ModelAPI.callEvent(var2);
            this.lastAnimation = this.currentAnimation;
         });
         return var2;
      }

      public boolean hasFinishedPlaying(ModelState var1) {
         return !this.isPlaying(StateMachineHandler.this.getDefaultProperty(var1).getAnimation());
      }

      public boolean isPlaying(String var1) {
         if (this.currentAnimation != null && this.currentAnimation.getName().equals(var1) && this.currentAnimation.getPhase() != IAnimationProperty.Phase.LERPOUT) {
            double var2 = this.currentAnimation.getTime();
            double var4 = this.currentAnimation.getBlueprintAnimation().getLength();
            boolean var10000;
            switch(this.currentAnimation.getLoopMode()) {
            case ONCE:
               var10000 = var2 < var4;
               break;
            case LOOP:
               var10000 = var2 < var4 + 0.05D;
               break;
            default:
               var10000 = true;
            }

            return var10000;
         } else {
            return false;
         }
      }

      public boolean isPlayingOrEnding(String var1) {
         return this.currentAnimation != null && this.currentAnimation.getName().equals(var1) && !this.currentAnimation.isEnded();
      }

      public void forceReentry(BaseEntity<?> var1) {
         this.getCurrentNode().acceptEntry(var1);
      }

      public void save(SavedData var1) {
         if (this.saved) {
            if (this.lastAnimation != null) {
               this.lastAnimation.save().ifPresent((var1x) -> {
                  var1.putData("last_animation", var1x);
               });
            }

            if (this.currentAnimation != null) {
               this.currentAnimation.save().ifPresent((var1x) -> {
                  var1.putData("current_animation", var1x);
               });
            }
         }

      }

      public void load(SavedData var1) {
         AnimationPropertyArchive var2 = ModelAPI.getAnimationPropertyArchive();
         var1.getData("last_animation").ifPresent((var2x) -> {
            this.lastAnimation = var2.createAnimationProperty(StateMachineHandler.this, var2x);
         });
         var1.getData("current_animation").ifPresent((var2x) -> {
            this.currentAnimation = var2.createAnimationProperty(StateMachineHandler.this, var2x);
         });
         StateNode var3 = this.getCurrentNode();
         if (this.currentAnimation != null) {
            StateNode var4 = this.createAnimationNode(() -> {
               return this.currentAnimation;
            });
            var4.addConnectedNode((var1x) -> {
               return this.currentAnimation.isEnded();
            }, this.getRootNode());
            var3.addForceConnectedNode((var0) -> {
               return true;
            }, var4);
         }

      }

      @Nullable
      public IAnimationProperty getLastAnimation() {
         return this.lastAnimation;
      }

      public void setLastAnimation(@Nullable IAnimationProperty var1) {
         this.lastAnimation = var1;
      }

      @Nullable
      public IAnimationProperty getCurrentAnimation() {
         return this.currentAnimation;
      }

      public void setCurrentAnimation(@Nullable IAnimationProperty var1) {
         this.currentAnimation = var1;
      }
   }
}
