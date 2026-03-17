package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public abstract class AttachmentConfigModelTracker extends AttachmentConfigTrackerBase {
   private AttachmentConfigModelTracker.DeepAttachmentConfig root;
   private final AttachmentConfigModelTracker.DeepAttachmentTrackerProxy proxy;
   private int numProxiesSynchronizing;
   private int modificationCount;

   public AttachmentConfigModelTracker(AttachmentConfigTracker tracker) {
      this(tracker, (Plugin)null);
   }

   public AttachmentConfigModelTracker(AttachmentConfigTracker tracker, Plugin plugin) {
      super(plugin == null ? Logger.getGlobal() : plugin.getLogger());
      this.numProxiesSynchronizing = 0;
      this.modificationCount = 0;
      this.root = null;
      this.proxy = new AttachmentConfigModelTracker.DeepAttachmentTrackerProxy(tracker, true) {
         public AttachmentConfigModelTracker.DeepAttachmentConfig getRoot() {
            return AttachmentConfigModelTracker.this.root;
         }

         public void setRoot(AttachmentConfig baseRoot) {
            AttachmentConfigModelTracker.this.root = baseRoot == null ? null : AttachmentConfigModelTracker.this.createNewRoot(baseRoot, (List)null);
         }
      };
   }

   public abstract AttachmentConfigTracker findModelConfig(String var1);

   protected void startTracking() {
      this.proxy.start();
      ++this.modificationCount;
   }

   protected void stopTracking() {
      this.root.onRemoved();
      this.proxy.stop();
      this.root = null;
      ++this.modificationCount;
   }

   protected AttachmentConfig.RootReference createRootReference() {
      if (this.isTracking()) {
         int currModCount = this.modificationCount;
         return new AttachmentConfig.RootReference(this.root, () -> {
            return this.modificationCount == currModCount;
         });
      } else {
         AttachmentConfig.RootReference mainBaseRootRef = this.proxy.tracker.createRootReference();
         List<AttachmentConfig.RootReference> allRootsUsed = new ArrayList();
         allRootsUsed.add(mainBaseRootRef);
         AttachmentConfigModelTracker.DeepAttachmentConfig tempRoot = this.createNewRoot(mainBaseRootRef.get(), allRootsUsed);
         return allRootsUsed.size() == 1 ? new AttachmentConfig.RootReference(tempRoot, mainBaseRootRef.getValidChecker()) : new AttachmentConfig.RootReference(tempRoot, () -> {
            Iterator var1 = allRootsUsed.iterator();

            AttachmentConfig.RootReference ref;
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               ref = (AttachmentConfig.RootReference)var1.next();
            } while(ref.valid());

            return false;
         });
      }
   }

   public void sync() {
   }

   private AttachmentConfigModelTracker.DeepAttachmentConfig createNewRoot(AttachmentConfig base, List<AttachmentConfig.RootReference> modelRoots) {
      return this.createAttachmentConfig(AttachmentConfigModelTracker.PositionAccess.DEFAULT, (AttachmentConfigModelTracker.DeepAttachmentConfig)null, base, modelRoots);
   }

   private AttachmentConfigModelTracker.DeepAttachmentConfig createAttachmentConfig(AttachmentConfigModelTracker.PositionAccess position, AttachmentConfigModelTracker.DeepAttachmentConfig parent, AttachmentConfig base, List<AttachmentConfig.RootReference> modelRoots) {
      if (base == null) {
         throw new IllegalArgumentException("Base attachment configuration cannot be null");
      } else {
         Object config;
         if (base instanceof AttachmentConfig.Model) {
            config = new AttachmentConfigModelTracker.DeepModelAttachmentConfig(position, parent, (AttachmentConfig.Model)base, modelRoots);
         } else {
            config = new AttachmentConfigModelTracker.DeepAttachmentConfig(position, parent, base);
         }

         ((AttachmentConfigModelTracker.DeepAttachmentConfig)config).initChildren(modelRoots);
         return (AttachmentConfigModelTracker.DeepAttachmentConfig)config;
      }
   }

   private interface PositionAccess {
      AttachmentConfigModelTracker.PositionAccess DEFAULT = new AttachmentConfigModelTracker.PositionAccess() {
         public int childIndex(AttachmentConfig base) {
            return base.childIndex();
         }

         public YamlPath path(AttachmentConfig base) {
            return base.path();
         }

         public AttachmentConfigModelTracker.PositionAccess forChildren() {
            return this;
         }
      };

      int childIndex(AttachmentConfig var1);

      YamlPath path(AttachmentConfig var1);

      AttachmentConfigModelTracker.PositionAccess forChildren();

      default boolean isRemoved() {
         return false;
      }

      default AttachmentConfigModelTracker.PositionAccess removed(AttachmentConfig base) {
         return new AttachmentConfigModelTracker.PositionAccessRemoved(this.childIndex(base), this.path(base));
      }
   }

   private class DeepAttachmentConfig implements AttachmentConfig {
      protected AttachmentConfigModelTracker.PositionAccess position;
      private final AttachmentConfigModelTracker.DeepAttachmentConfig parent;
      private final AttachmentConfig base;
      protected final ArrayList<AttachmentConfigModelTracker.DeepAttachmentConfig> children;

      public DeepAttachmentConfig(AttachmentConfigModelTracker.PositionAccess position, AttachmentConfigModelTracker.DeepAttachmentConfig parent, AttachmentConfig base) {
         this.position = position;
         this.parent = parent;
         this.base = base;
         this.children = new ArrayList(base.children().size() + 1);
      }

      public void initChildren(List<AttachmentConfig.RootReference> modelRoots) {
         Iterator var2 = this.base.children().iterator();

         while(var2.hasNext()) {
            AttachmentConfig baseChild = (AttachmentConfig)var2.next();
            this.children.add(AttachmentConfigModelTracker.this.createAttachmentConfig(this.position.forChildren(), this, baseChild, modelRoots));
         }

      }

      public AttachmentConfig parent() {
         return this.parent;
      }

      public List<AttachmentConfig> children() {
         return Collections.unmodifiableList(this.children);
      }

      public AttachmentConfigModelTracker.DeepAttachmentConfig child(int childIndex) {
         List<AttachmentConfigModelTracker.DeepAttachmentConfig> children = this.children;
         return childIndex >= 0 && childIndex < children.size() ? (AttachmentConfigModelTracker.DeepAttachmentConfig)children.get(childIndex) : null;
      }

      public AttachmentConfigModelTracker.DeepAttachmentConfig nonModelChild(int childIndex) {
         return this.child(childIndex);
      }

      public final AttachmentConfigModelTracker.DeepAttachmentConfig nonModelChild(int[] childPath) {
         AttachmentConfigModelTracker.DeepAttachmentConfig p = this;
         int len = childPath.length;
         if (len > 0) {
            int i = 0;

            do {
               p = p.nonModelChild(childPath[i]);
               ++i;
            } while(i < len && p != null);
         }

         return p;
      }

      public AttachmentConfig addChild(int childIndex, ConfigurationNode config) {
         throw new UnsupportedOperationException("Model attachment configurations cannot be added");
      }

      public boolean isRemoved() {
         return this.position.isRemoved();
      }

      public void remove() {
         throw new UnsupportedOperationException("Model attachment configurations cannot be removed");
      }

      public int childIndex() {
         return this.position.childIndex(this.base);
      }

      public YamlPath path() {
         return this.position.path(this.base);
      }

      public String typeId() {
         return this.base.typeId();
      }

      public ConfigurationNode config() {
         return this.base.config();
      }

      public boolean isEmptyConfig() {
         return this.base.isEmptyConfig();
      }

      public void runAction(Consumer<Attachment> action) {
         if (!this.position.isRemoved()) {
            AttachmentConfigModelTracker.this.runAttachmentAction(this, action);
         }

      }

      public String toString() {
         return "Attachment{" + this.typeId() + " at " + Arrays.toString(this.childPath()) + "}";
      }

      public final boolean isModelUsed(String name) {
         for(AttachmentConfigModelTracker.DeepAttachmentConfig att = this; att != null; att = att.parent) {
            if (att.isModelUsedSelf(name)) {
               return true;
            }
         }

         return false;
      }

      protected boolean isModelUsedSelf(String name) {
         return false;
      }

      public void addDeepChildAtPath(int[] path, AttachmentConfig base) {
         this.runActionForChild(path, (parent, childIndex) -> {
            parent.addDeepChild(childIndex, base);
         });
      }

      protected void addDeepChild(int childIndex, AttachmentConfig baseConfig) {
         if (childIndex >= 0 && childIndex <= this.children.size()) {
            AttachmentConfigModelTracker.DeepAttachmentConfig deepConfig = AttachmentConfigModelTracker.this.createAttachmentConfig(this.position.forChildren(), this, baseConfig, (List)null);
            this.children.add(childIndex, deepConfig);
            AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.ADDED, deepConfig);
         } else {
            throw new IndexOutOfBoundsException("Child index out of bounds: " + childIndex);
         }
      }

      public void removeDeepChildAtPath(int[] childPath) {
         this.runActionForChild(childPath, AttachmentConfigModelTracker.DeepAttachmentConfig::removeDeepChild);
      }

      protected void removeDeepChild(int childIndex) {
         AttachmentConfigModelTracker.DeepAttachmentConfig deepConfig = (AttachmentConfigModelTracker.DeepAttachmentConfig)this.children.get(childIndex);
         deepConfig.onRemoved();
         this.children.remove(childIndex);
         AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.REMOVED, deepConfig);
      }

      private void runActionForChild(int[] path, AttachmentConfigModelTracker.ChildActionConsumer action) {
         AttachmentConfigModelTracker.DeepAttachmentConfig parent = this;
         int limit = path.length - 1;

         int i;
         for(i = 0; i < limit; ++i) {
            parent = (AttachmentConfigModelTracker.DeepAttachmentConfig)parent.children.get(path[i]);
         }

         action.accept(parent, path[i]);
      }

      protected void onRemoved() {
         this.position = this.position.removed(this.base);
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            AttachmentConfigModelTracker.DeepAttachmentConfig child = (AttachmentConfigModelTracker.DeepAttachmentConfig)var1.next();
            child.onRemoved();
         }

      }
   }

   private abstract class DeepAttachmentTrackerProxy implements AttachmentConfigListener {
      private final AttachmentConfigTracker tracker;
      private final boolean allowEmptyRootConfig;
      private boolean isSynchronizing = false;

      public abstract AttachmentConfigModelTracker.DeepAttachmentConfig getRoot();

      public abstract void setRoot(AttachmentConfig var1);

      public DeepAttachmentTrackerProxy(AttachmentConfigTracker tracker, boolean allowEmptyRootConfig) {
         this.tracker = tracker;
         this.allowEmptyRootConfig = allowEmptyRootConfig;
      }

      public final void start() {
         AttachmentConfig newRoot = this.tracker.startTracking(this);
         this.setRoot(!this.allowEmptyRootConfig && newRoot.isEmptyConfig() ? null : newRoot);
      }

      public final void stop() {
         this.tracker.stopTracking(this);
         this.notifyDoneSynchronizing();
      }

      private void notifyStartSynchronizing() {
         if (!this.isSynchronizing) {
            this.isSynchronizing = true;
            AttachmentConfigModelTracker.this.numProxiesSynchronizing++;
         }

      }

      private void notifyDoneSynchronizing() {
         if (this.isSynchronizing) {
            this.isSynchronizing = false;
            int numNowSynchronizing = --AttachmentConfigModelTracker.this.numProxiesSynchronizing;
            if (numNowSynchronizing < 0) {
               AttachmentConfigModelTracker.this.numProxiesSynchronizing = 0;
               throw new IllegalStateException("Number of trackers synchronizing went negative");
            }

            if (numNowSynchronizing == 0 && AttachmentConfigModelTracker.this.root != null) {
               AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.SYNCHRONIZED, AttachmentConfigModelTracker.this.root);
            }
         }

      }

      public void onChange(AttachmentConfig.Change change) {
         AttachmentConfigModelTracker.this.modificationCount++;
         if (change.changeType() == AttachmentConfig.ChangeType.SYNCHRONIZED) {
            this.notifyDoneSynchronizing();
         } else {
            this.notifyStartSynchronizing();
         }

         change.changeType().callback().accept(this, change.attachment());
      }

      public void onAttachmentAdded(AttachmentConfig attachment) {
         int[] path = attachment.childPath();
         if (path.length == 0) {
            if (this.getRoot() != null) {
               throw new IllegalStateException("Root being re-added while one already exists");
            }

            AttachmentConfig newRoot = !this.allowEmptyRootConfig && attachment.isEmptyConfig() ? null : attachment;
            this.setRoot(newRoot);
            if (newRoot != null) {
               AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.ADDED, this.getRoot());
            }
         } else {
            AttachmentConfigModelTracker.DeepAttachmentConfig root = this.getRoot();
            if (root == null) {
               throw new IllegalStateException("Root child being added while root was removed");
            }

            root.addDeepChildAtPath(path, attachment);
         }

      }

      public void onAttachmentRemoved(AttachmentConfig attachment) {
         int[] path = attachment.childPath();
         AttachmentConfigModelTracker.DeepAttachmentConfig root = this.getRoot();
         if (path.length == 0) {
            if (root == null) {
               if (!this.allowEmptyRootConfig && attachment.isEmptyConfig()) {
                  return;
               }

               throw new IllegalStateException("Root being removed, but root was already removed");
            }

            root.onRemoved();
            this.setRoot((AttachmentConfig)null);
            AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.REMOVED, root);
         } else {
            if (root == null) {
               throw new IllegalStateException("Root child being removed, but root was already removed");
            }

            root.removeDeepChildAtPath(path);
         }

      }

      public void onAttachmentChanged(AttachmentConfig attachment) {
         AttachmentConfigModelTracker.DeepAttachmentConfig root = this.getRoot();
         if (root == null) {
            if (!this.allowEmptyRootConfig && attachment.isRoot()) {
               if (!attachment.isEmptyConfig()) {
                  this.onAttachmentAdded(attachment);
               }

            } else {
               throw new IllegalStateException("Root changed, but root was already removed");
            }
         } else if (attachment.isRoot() && !this.allowEmptyRootConfig && attachment.isEmptyConfig()) {
            this.onAttachmentRemoved(attachment);
         } else {
            AttachmentConfigModelTracker.DeepAttachmentConfig child = root.nonModelChild(attachment.childPath());
            if (child == null) {
               throw new IllegalStateException("An attachment changed that did not exist in this tracker");
            } else {
               AttachmentConfigModelTracker.this.notifyChange(AttachmentConfig.ChangeType.CHANGED, child);
            }
         }
      }

      public void onAttachmentAction(AttachmentConfig attachment, Consumer<Attachment> action) {
         AttachmentConfigModelTracker.DeepAttachmentConfig root = this.getRoot();
         if (root == null) {
            if (this.allowEmptyRootConfig || !attachment.isEmptyConfig() || attachment.childPath().length != 0) {
               throw new IllegalStateException("Action on an attachment of root, but root was already removed");
            }
         } else {
            AttachmentConfigModelTracker.DeepAttachmentConfig child = root.nonModelChild(attachment.childPath());
            if (child == null) {
               throw new IllegalStateException("Action on an attachment that did not exist in this tracker");
            } else {
               AttachmentConfigModelTracker.this.runAttachmentAction(child, action);
            }
         }
      }
   }

   private class DeepModelAttachmentConfig extends AttachmentConfigModelTracker.DeepAttachmentConfig implements AttachmentConfig.Model {
      private final AttachmentConfig.Model baseModel;
      private final AttachmentConfigTracker modelTracker;
      private final AttachmentConfigModelTracker.DeepAttachmentTrackerProxy proxy;
      private AttachmentConfigModelTracker.DeepAttachmentConfig modelChild;

      public DeepModelAttachmentConfig(AttachmentConfigModelTracker.PositionAccess position, AttachmentConfigModelTracker.DeepAttachmentConfig parent, AttachmentConfig.Model base, List<AttachmentConfig.RootReference> modelRoots) {
         super(position, parent, base);
         this.baseModel = base;
         if (parent != null && parent.isModelUsed(base.modelName())) {
            this.modelChild = null;
            this.proxy = null;
            this.modelTracker = null;
         } else {
            this.modelTracker = AttachmentConfigModelTracker.this.findModelConfig(base.modelName());
            if (modelRoots != null) {
               this.proxy = null;
            } else {
               this.proxy = new AttachmentConfigModelTracker.DeepAttachmentTrackerProxy(this.modelTracker, false) {
                  public AttachmentConfigModelTracker.DeepAttachmentConfig getRoot() {
                     return DeepModelAttachmentConfig.this.modelChild;
                  }

                  public void setRoot(AttachmentConfig baseRoot) {
                     if (baseRoot == null) {
                        if (!DeepModelAttachmentConfig.this.children.isEmpty() && DeepModelAttachmentConfig.this.children.get(DeepModelAttachmentConfig.this.children.size() - 1) == DeepModelAttachmentConfig.this.modelChild) {
                           DeepModelAttachmentConfig.this.children.remove(DeepModelAttachmentConfig.this.children.size() - 1);
                        }

                        DeepModelAttachmentConfig.this.modelChild = null;
                     } else {
                        DeepModelAttachmentConfig.this.modelChild = AttachmentConfigModelTracker.this.createAttachmentConfig(new AttachmentConfigModelTracker.PositionAccessModelChild(DeepModelAttachmentConfig.this), DeepModelAttachmentConfig.this, baseRoot, (List)null);
                        DeepModelAttachmentConfig.this.children.add(DeepModelAttachmentConfig.this.modelChild);
                     }

                  }
               };
            }
         }
      }

      public void initChildren(List<AttachmentConfig.RootReference> modelRoots) {
         if (this.modelTracker != null) {
            super.initChildren(modelRoots);
            if (modelRoots != null) {
               AttachmentConfig.RootReference modelRoot = this.modelTracker.getRoot();
               modelRoots.add(modelRoot);
               if (modelRoot.get().isEmptyConfig()) {
                  this.modelChild = null;
                  return;
               }

               this.modelChild = AttachmentConfigModelTracker.this.createAttachmentConfig(new AttachmentConfigModelTracker.PositionAccessModelChild(this), this, modelRoot.get(), modelRoots);
               this.children.add(this.modelChild);
            }

            if (this.proxy != null) {
               this.proxy.start();
            }

         }
      }

      protected boolean isModelUsedSelf(String name) {
         return this.baseModel.modelName().equals(name);
      }

      protected void onRemoved() {
         super.onRemoved();
         if (this.proxy != null) {
            this.proxy.stop();
            this.modelChild = null;
         }

      }

      public int getModelChildIndex() {
         int index = this.children.size() - 1;
         if (this.modelChild != null && index != -1) {
            return index;
         } else {
            throw new IllegalStateException("Model configuration does not store a model attachment");
         }
      }

      public YamlPath getModelPath() {
         int index = this.getModelChildIndex();
         if (index == 0) {
            return this.path().child("attachments").child("0");
         } else {
            YamlPath siblingPath = ((AttachmentConfigModelTracker.DeepAttachmentConfig)this.children.get(0)).path();
            return siblingPath.isListElement() ? siblingPath.parent().listChild(index) : siblingPath.parent().child(Integer.toString(index));
         }
      }

      protected void addDeepChild(int childIndex, AttachmentConfig baseConfig) {
         if (this.modelChild != null && childIndex == this.children.size()) {
            throw new IndexOutOfBoundsException("Child index out of bounds: " + childIndex);
         } else {
            super.addDeepChild(childIndex, baseConfig);
         }
      }

      protected void removeDeepChild(int childIndex) {
         if (this.modelChild != null && childIndex == this.children.size() - 1) {
            throw new IndexOutOfBoundsException("Child index out of bounds: " + childIndex);
         } else {
            super.removeDeepChild(childIndex);
         }
      }

      public AttachmentConfigModelTracker.DeepAttachmentConfig nonModelChild(int childIndex) {
         List<AttachmentConfigModelTracker.DeepAttachmentConfig> children = this.children;
         return childIndex >= 0 && childIndex < (this.modelChild == null ? children.size() : children.size() - 1) ? (AttachmentConfigModelTracker.DeepAttachmentConfig)children.get(childIndex) : null;
      }

      public String modelName() {
         return this.baseModel.modelName();
      }
   }

   private static class PositionAccessModelChild implements AttachmentConfigModelTracker.PositionAccess {
      private final AttachmentConfigModelTracker.DeepModelAttachmentConfig parent;
      private final AttachmentConfigModelTracker.PositionAccess forChildren;

      public PositionAccessModelChild(AttachmentConfigModelTracker.DeepModelAttachmentConfig parent) {
         this.parent = parent;
         this.forChildren = new AttachmentConfigModelTracker.PositionAccess() {
            public int childIndex(AttachmentConfig base) {
               return base.childIndex();
            }

            public YamlPath path(AttachmentConfig base) {
               YamlPath parentPath = PositionAccessModelChild.this.parent.getModelPath();
               return YamlPath.join(parentPath, base.path());
            }

            public AttachmentConfigModelTracker.PositionAccess forChildren() {
               return this;
            }
         };
      }

      public int childIndex(AttachmentConfig base) {
         return this.parent.getModelChildIndex();
      }

      public YamlPath path(AttachmentConfig base) {
         return this.parent.getModelPath();
      }

      public AttachmentConfigModelTracker.PositionAccess forChildren() {
         return this.forChildren;
      }
   }

   private static class PositionAccessRemoved implements AttachmentConfigModelTracker.PositionAccess {
      private final int childIndex;
      private final YamlPath path;

      public PositionAccessRemoved(int childIndex, YamlPath path) {
         this.childIndex = childIndex;
         this.path = path;
      }

      public int childIndex(AttachmentConfig base) {
         return this.childIndex;
      }

      public YamlPath path(AttachmentConfig base) {
         return this.path;
      }

      public AttachmentConfigModelTracker.PositionAccess forChildren() {
         throw new UnsupportedOperationException("Can't add children to removed attachments");
      }

      public boolean isRemoved() {
         return true;
      }

      public AttachmentConfigModelTracker.PositionAccess removed(AttachmentConfig base) {
         return this;
      }
   }

   @FunctionalInterface
   private interface ChildActionConsumer {
      void accept(AttachmentConfigModelTracker.DeepAttachmentConfig var1, int var2);
   }
}
