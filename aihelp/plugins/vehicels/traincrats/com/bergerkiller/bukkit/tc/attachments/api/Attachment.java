package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.attachments.config.ObjectPosition;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Attachment extends AttachmentNameLookup.Supplier {
   AttachmentInternalState getInternalState();

   default AttachmentManager getManager() {
      return this.getInternalState().manager;
   }

   default ConfigurationNode getConfig() {
      return this.getInternalState().config;
   }

   default Set<String> getNames() {
      return this.getInternalState().names;
   }

   default Plugin getPlugin() {
      return this.getInternalState().plugin;
   }

   void onAttached();

   void onDetached();

   void onLoad(ConfigurationNode var1);

   default boolean checkCanReload(ConfigurationNode config) {
      return true;
   }

   void onTransformChanged(Matrix4x4 var1);

   void onTick();

   void onMove(boolean var1);

   default boolean isAttached() {
      return this.getInternalState().attached;
   }

   default boolean isFocused() {
      return this.getInternalState().focused;
   }

   default void setFocused(boolean focused) {
      AttachmentInternalState state = this.getInternalState();
      if (state.focused != focused) {
         state.focused = focused;
         if (state.attached) {
            if (focused) {
               this.onFocus();
            } else {
               this.onBlur();
            }
         }
      }

   }

   default void onFocus() {
   }

   default void onBlur() {
   }

   default void onActiveChanged(boolean active) {
   }

   void makeVisible(Player var1);

   default void makeVisible(AttachmentViewer viewer) {
      this.makeVisible(viewer.getPlayer());
   }

   void makeHidden(Player var1);

   default void makeHidden(AttachmentViewer viewer) {
      this.makeHidden(viewer.getPlayer());
   }

   default ObjectPosition getConfiguredPosition() {
      return this.getInternalState().position;
   }

   default Matrix4x4 getPreviousTransform() {
      AttachmentInternalState state = this.getInternalState();
      return state.last_transform == null ? state.curr_transform : state.last_transform;
   }

   default Matrix4x4 getTransform() {
      return this.getInternalState().curr_transform;
   }

   default void addChild(Attachment child) {
      this.getInternalState().children.add(child);
      child.getInternalState().assignParent(child, this);
   }

   default void addChild(int index, Attachment child) {
      this.getInternalState().children.add(index, child);
      child.getInternalState().assignParent(child, this);
   }

   default boolean removeChild(Attachment child) {
      if (this.getInternalState().children.remove(child)) {
         child.getInternalState().makeNewSubtree(child);
         return true;
      } else {
         return false;
      }
   }

   default List<Attachment> getChildren() {
      return this.getInternalState().children;
   }

   default AttachmentNameLookup getNameLookup() {
      return this.getManager().getNameLookup(this);
   }

   default AttachmentNameLookup getNameLookup(AttachmentSelector.SearchStrategy strategy) {
      return strategy == AttachmentSelector.SearchStrategy.ROOT_CHILDREN ? this.getRootParent().getNameLookup() : this.getNameLookup();
   }

   default Set<Attachment> getSelfFilterOfNameLookup() {
      return Collections.singleton(this);
   }

   default Attachment getParent() {
      return this.getInternalState().parent;
   }

   default Attachment getRootParent() {
      return this.getInternalState().rootParent;
   }

   Collection<Player> getViewers();

   Collection<AttachmentViewer> getAttachmentViewers();

   default boolean isHiddenWhenInactive() {
      return true;
   }

   default boolean isActive() {
      return this.getInternalState().active;
   }

   default void setActive(boolean active) {
      AttachmentInternalState state = this.getInternalState();
      if (state.active != active) {
         state.active = active;
         if (!HelperMethods.hasInactiveParent(this)) {
            HelperMethods.updateActiveRecursive(this, active, this.getViewers());
         }
      }

   }

   default void applyPassengerSeatTransform(Matrix4x4 transform) {
   }

   default void addAnimation(Animation animation) {
      this.getInternalState().animations.put(animation.getOptions().getName(), animation);
   }

   default List<String> getAnimationNames() {
      return Collections.unmodifiableList(new ArrayList(this.getInternalState().animations.keySet()));
   }

   default Set<String> getAnimationScenes(String animationName) {
      Animation animation = (Animation)this.getInternalState().animations.get(animationName);
      return animation == null ? Collections.emptySet() : animation.getSceneNames();
   }

   default List<String> getAnimationNamesRecursive() {
      HashSet<String> tmp = new LinkedHashSet();
      HelperMethods.addAnimationNamesToSetRecursive(tmp, this);
      return Collections.unmodifiableList(new ArrayList(tmp));
   }

   default Set<String> getAnimationScenesRecursive(String animationName) {
      HashSet<String> tmp = new LinkedHashSet();
      HelperMethods.addAnimationScenesToSetRecursive(tmp, animationName, this);
      return Collections.unmodifiableSet(tmp);
   }

   default Collection<Animation> getAnimations() {
      return this.getInternalState().animations.values();
   }

   default void clearAnimations() {
      this.getInternalState().animations.clear();
   }

   default Animation getCurrentAnimation() {
      return this.getInternalState().currentAnimation;
   }

   default void stopAnimation() {
      AttachmentInternalState state = this.getInternalState();
      state.currentAnimation = null;
      state.nextAnimationQueue = Collections.emptyList();
   }

   default void startAnimation(Animation animation) {
      if (animation == null) {
         this.stopAnimation();
      } else {
         AttachmentInternalState state = this.getInternalState();
         if (state.currentAnimation != null && !animation.getOptions().getReset()) {
            if (animation.getOptions().getQueue()) {
               if (state.nextAnimationQueue.isEmpty()) {
                  state.nextAnimationQueue = new ArrayList(1);
               }

               state.nextAnimationQueue.add(animation);
            } else if (state.currentAnimation.isSame(animation)) {
               state.currentAnimation.setOptions(animation.getOptions().clone());
               state.nextAnimationQueue = Collections.emptyList();
            } else {
               state.currentAnimation = animation;
               state.currentAnimation.start();
               state.nextAnimationQueue = Collections.emptyList();
            }
         } else {
            state.currentAnimation = animation;
            state.currentAnimation.start();
            state.nextAnimationQueue = Collections.emptyList();
         }

      }
   }

   default boolean containsEntityId(int entityId) {
      return false;
   }

   default boolean playNamedAnimationRecursive(String name) {
      return this.playNamedAnimationRecursive(new AnimationOptions(name));
   }

   default boolean playNamedAnimationRecursive(AnimationOptions options) {
      return HelperMethods.playAnimationRecursive(this, options);
   }

   default boolean playNamedAnimation(String name) {
      return this.playNamedAnimation(new AnimationOptions(name));
   }

   default boolean playNamedAnimation(AnimationOptions options) {
      return HelperMethods.playAnimation(this, options);
   }

   default Attachment findChild(int[] targetPath) {
      Attachment target = this;
      int[] var3 = targetPath;
      int var4 = targetPath.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int index = var3[var5];
         List<Attachment> children = target.getChildren();
         if (index < 0 || index >= children.size()) {
            return null;
         }

         target = (Attachment)children.get(index);
      }

      return target;
   }

   default int[] getPath() {
      Attachment parent = this.getParent();
      if (parent == null) {
         return new int[0];
      } else {
         int[] result = parent.getPath();
         int len = result.length;
         result = Arrays.copyOf(result, len + 1);
         result[len] = parent.getChildren().indexOf(this);
         return result;
      }
   }

   public interface EffectAttachment extends Attachment, Attachment.EffectSink {
      void playEffect(Attachment.EffectAttachment.EffectOptions var1);

      void stopEffect();

      public static class EffectOptions {
         public static final Attachment.EffectAttachment.EffectOptions DEFAULT = new Attachment.EffectAttachment.EffectOptions(1.0D, 1.0D);
         private final double volume;
         private final double speed;

         protected EffectOptions(double volume, double speed) {
            this.volume = volume;
            this.speed = speed;
         }

         public double volume() {
            return this.volume;
         }

         public double speed() {
            return this.speed;
         }

         public Attachment.EffectAttachment.EffectOptions withVolume(double newVolume) {
            return new Attachment.EffectAttachment.EffectOptions(newVolume, this.speed);
         }

         public Attachment.EffectAttachment.EffectOptions withSpeed(double newSpeed) {
            return new Attachment.EffectAttachment.EffectOptions(this.volume, newSpeed);
         }

         public Attachment.EffectAttachment.EffectOptions multiply(double multVolume, double multSpeed) {
            return new Attachment.EffectAttachment.EffectOptions(this.volume * multVolume, this.speed * multSpeed);
         }

         public static Attachment.EffectAttachment.EffectOptions of(double volume, double speed) {
            return new Attachment.EffectAttachment.EffectOptions(volume, speed);
         }
      }
   }

   public interface EffectSink {
      Attachment.EffectSink DISABLED_EFFECT_SINK = new Attachment.EffectSink() {
         public void playEffect(Attachment.EffectAttachment.EffectOptions options) {
         }

         public void stopEffect() {
         }
      };

      void playEffect(Attachment.EffectAttachment.EffectOptions var1);

      void stopEffect();

      static Attachment.EffectSink combineEffects(final Iterable<? extends Attachment.EffectAttachment> effectAttachments) {
         return new Attachment.EffectSink() {
            public void playEffect(Attachment.EffectAttachment.EffectOptions options) {
               effectAttachments.forEach((e) -> {
                  e.playEffect(options);
               });
            }

            public void stopEffect() {
               effectAttachments.forEach(Attachment.EffectAttachment::stopEffect);
            }
         };
      }

      static Attachment.EffectSink combineEffects(final Collection<Iterable<? extends Attachment.EffectAttachment>> effectAttachments) {
         return new Attachment.EffectSink() {
            public void playEffect(Attachment.EffectAttachment.EffectOptions options) {
               effectAttachments.forEach((n) -> {
                  n.forEach((e) -> {
                     e.playEffect(options);
                  });
               });
            }

            public void stopEffect() {
               effectAttachments.forEach((n) -> {
                  n.forEach(Attachment.EffectAttachment::stopEffect);
               });
            }
         };
      }
   }
}
