package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import com.bergerkiller.bukkit.tc.attachments.config.ObjectPosition;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachment;
import com.bergerkiller.bukkit.tc.attachments.helper.ActiveChangeHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

public class AttachmentInternalState {
   protected AttachmentManager manager = null;
   protected Plugin plugin = null;
   protected Attachment rootParent = null;
   protected Attachment parent = null;
   protected List<Attachment> children = new ArrayList(1);
   protected ConfigurationNode config = new ConfigurationNode();
   public Set<String> names = Collections.emptySet();
   public Map<String, Animation> animations = new HashMap();
   public Animation currentAnimation = null;
   public List<Animation> nextAnimationQueue = Collections.emptyList();
   public AnimationNode lastAnimationState = null;
   protected boolean active = true;
   protected boolean focused = false;
   public boolean attached = false;
   public ObjectPosition position = new ObjectPosition();
   private AttachmentInternalState.UpdateTask transformUpdateTask;
   public Matrix4x4 last_transform = null;
   public Matrix4x4 curr_transform = null;

   public void onLoad(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType, ConfigurationNode config) {
      this.resetEffectsAndAnimations();

      try {
         attachmentType.migrateConfiguration(config);
      } catch (Throwable var9) {
         attachmentType.getPlugin().getLogger().log(Level.SEVERE, "Failed to migrate attachment configuration of " + attachmentType.getName(), var9);
      }

      this.plugin = attachmentType.getPlugin();
      this.config = config;
      this.position.load(managerType, attachmentType, config.getNodeIfExists("position"));
      List effectNamesList;
      if (config.contains("names") && !(effectNamesList = config.getList("names", String.class)).isEmpty()) {
         this.names = new LinkedHashSet(effectNamesList);
      } else {
         this.names = Collections.emptySet();
      }

      if (config.isNode("animations")) {
         ConfigurationNode animations = config.getNode("animations");
         Iterator var6 = animations.getNodes().iterator();

         while(var6.hasNext()) {
            ConfigurationNode animationConfig = (ConfigurationNode)var6.next();
            Animation anim = Animation.loadFromConfig(animationConfig);
            if (anim != null) {
               this.animations.put(anim.getOptions().getName(), anim);
               if (anim.getOptions().isAutoPlay()) {
                  this.currentAnimation = anim;
               }
            }
         }
      }

   }

   public void reset() {
      this.resetEffectsAndAnimations();
      this.last_transform = null;
      this.curr_transform = null;
      this.transformUpdateTask = null;
   }

   private void resetEffectsAndAnimations() {
      this.animations.clear();
      this.currentAnimation = null;
   }

   protected void assignParent(Attachment self, Attachment parent) {
      this.parent = parent;
      this.rootParent = parent.getRootParent();
      updateRootParentOfChildrenRecurse(self, this.rootParent);
   }

   protected void makeNewSubtree(Attachment self) {
      this.parent = null;
      this.rootParent = self;
      updateRootParentOfChildrenRecurse(self, self);
   }

   private static void updateRootParentOfChildrenRecurse(Attachment attachment, Attachment rootParent) {
      Iterator var2 = attachment.getChildren().iterator();

      while(var2.hasNext()) {
         Attachment child = (Attachment)var2.next();
         child.getInternalState().rootParent = rootParent;
         updateRootParentOfChildrenRecurse(child, rootParent);
      }

   }

   public void updateTransform(Attachment attachment, Matrix4x4 initialTransform, ActiveChangeHandler activeChangeHandler) {
      boolean hasLastTransform = this.last_transform != null;
      if (this.curr_transform != null) {
         if (hasLastTransform) {
            this.last_transform.set(this.curr_transform);
         } else {
            this.last_transform = this.curr_transform.clone();
         }

         hasLastTransform = true;
      }

      if (this.curr_transform == null) {
         this.curr_transform = initialTransform.clone();
      } else {
         this.curr_transform.set(initialTransform);
      }

      if (this.position.anchor.appliedLate()) {
         this.curr_transform.multiply(this.position.transform);
         this.position.anchor.apply(attachment, this.curr_transform);
      } else {
         this.position.anchor.apply(attachment, this.curr_transform);
         this.curr_transform.multiply(this.position.transform);
      }

      if (!this.nextAnimationQueue.isEmpty() && (this.currentAnimation == null || this.currentAnimation.hasReachedEnd())) {
         this.currentAnimation = (Animation)this.nextAnimationQueue.remove(0);
         this.currentAnimation.start();
      }

      if (this.currentAnimation == null) {
         this.lastAnimationState = null;
      } else if (!this.currentAnimation.hasReachedEnd()) {
         CartAttachment cart = (CartAttachment)attachment;
         double dt = cart.hasController() ? cart.getController().getAnimationDeltaTime() : 0.05D;
         AnimationNode animNode = this.currentAnimation.update(dt, initialTransform);
         if (animNode != null) {
            this.lastAnimationState = animNode;
         }
      }

      if (this.lastAnimationState != null) {
         boolean active = this.lastAnimationState.isActive();
         this.lastAnimationState.apply(this.curr_transform);
         if (active != attachment.isActive()) {
            activeChangeHandler.scheduleActiveChange(attachment, active);
         }
      }

      if (!hasLastTransform) {
         this.last_transform = this.curr_transform.clone();
      }

      try {
         attachment.onTransformChanged(this.curr_transform);
      } catch (Throwable var9) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to execute onTransformChanged() on attachment " + attachment.getClass().getName(), var9);
      }

      if (!hasLastTransform) {
         this.last_transform = this.curr_transform.clone();
      }

   }

   public ForkJoinTask<Void> updateTransformRecurseAsync(Attachment attachment, Matrix4x4 initialTransform, ActiveChangeHandler activeChangeHandler) {
      if (this.transformUpdateTask instanceof AttachmentInternalState.UpdateRootAttachmentTask) {
         ((AttachmentInternalState.UpdateRootAttachmentTask)this.transformUpdateTask).initialTransform = initialTransform;
      } else {
         this.transformUpdateTask = new AttachmentInternalState.UpdateRootAttachmentTask(attachment, initialTransform, activeChangeHandler);
      }

      this.transformUpdateTask.reinitialize();
      return this.transformUpdateTask;
   }

   private abstract static class UpdateTask extends ForkJoinTask<Void> {
      private static final long serialVersionUID = 2077912465035575092L;
      public final Attachment attachment;
      public final ActiveChangeHandler activeChangeHandler;

      public UpdateTask(Attachment attachment, ActiveChangeHandler activeChangeHandler) {
         this.attachment = attachment;
         this.activeChangeHandler = activeChangeHandler;
      }

      public final Void getRawResult() {
         return null;
      }

      protected final void setRawResult(Void value) {
      }

      protected final boolean exec() {
         this.performUpdates();
         return true;
      }

      public abstract void performUpdates();

      protected final void updateChildren(AttachmentInternalState state) {
         int nrOfChildren = state.children.size();
         if (nrOfChildren != 0) {
            int i = nrOfChildren - 1;

            while(true) {
               Attachment child = (Attachment)state.children.get(i);
               AttachmentInternalState childState = child.getInternalState();
               if (childState.transformUpdateTask == null) {
                  childState.transformUpdateTask = new AttachmentInternalState.UpdateRelativeToParentTask(child, this.activeChangeHandler);
               }

               if (i == 0) {
                  childState.transformUpdateTask.performUpdates();

                  for(i = 1; i < nrOfChildren; ++i) {
                     ((Attachment)state.children.get(i)).getInternalState().transformUpdateTask.join();
                  }

                  return;
               }

               childState.transformUpdateTask.reinitialize();
               childState.transformUpdateTask.fork();
               --i;
            }
         }
      }
   }

   private static final class UpdateRootAttachmentTask extends AttachmentInternalState.UpdateTask {
      private static final long serialVersionUID = -758729101023975440L;
      public Matrix4x4 initialTransform;

      public UpdateRootAttachmentTask(Attachment attachment, Matrix4x4 initialTransform, ActiveChangeHandler activeChangeHandler) {
         super(attachment, activeChangeHandler);
         this.initialTransform = initialTransform;
      }

      public void performUpdates() {
         AttachmentInternalState state = this.attachment.getInternalState();
         state.updateTransform(this.attachment, this.initialTransform, this.activeChangeHandler);
         this.updateChildren(state);
      }
   }

   private static final class UpdateRelativeToParentTask extends AttachmentInternalState.UpdateTask {
      private static final long serialVersionUID = 8242564088493119093L;

      public UpdateRelativeToParentTask(Attachment attachment, ActiveChangeHandler activeChangeHandler) {
         super(attachment, activeChangeHandler);
      }

      public void performUpdates() {
         AttachmentInternalState state = this.attachment.getInternalState();

         Matrix4x4 initialTransform;
         try {
            initialTransform = state.parent.getTransform();
         } catch (NullPointerException var4) {
            if (state.parent == null) {
               throw new IllegalStateException("Attachment has no parent");
            }

            throw var4;
         }

         state.updateTransform(this.attachment, initialTransform, this.activeChangeHandler);
         this.updateChildren(state);
      }
   }
}
