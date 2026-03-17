package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.collections.ImplicitlySharedList;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.utils.ListCallbackCollector;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AttachmentConfigTrackerBase {
   private final ImplicitlySharedList<AttachmentConfigTrackerBase.RemovableListener> listeners;
   private AttachmentConfigTrackerBase.WeakRootReference cachedRoot;
   protected final Logger logger;

   public AttachmentConfigTrackerBase(Logger logger) {
      this.logger = logger;
      this.listeners = new ImplicitlySharedList();
      this.cachedRoot = new AttachmentConfigTrackerBase.WeakRootReference();
   }

   protected abstract void startTracking();

   protected abstract void stopTracking();

   protected abstract AttachmentConfig.RootReference createRootReference();

   public abstract void sync();

   public final AttachmentConfig.RootReference getRoot() {
      AttachmentConfig.RootReference root = this.cachedRoot.getIfValid();
      if (root == null) {
         this.sync();
         root = this.createRootReference();
         this.cachedRoot = new AttachmentConfigTrackerBase.WeakRootReference(root);
      }

      return root;
   }

   public AttachmentConfig startTracking(AttachmentConfigListener listener) {
      AttachmentConfigTrackerBase.RemovableListener removableListener = new AttachmentConfigTrackerBase.RemovableListener(listener);
      if (this.listeners.isEmpty()) {
         this.startTracking();
         this.listeners.add(removableListener);
         AttachmentConfig.RootReference ref = this.createRootReference();
         this.cachedRoot.close();
         this.cachedRoot = new AttachmentConfigTrackerBase.WeakRootReference(ref);
         return ref.get();
      } else if (this.listeners.contains(removableListener)) {
         throw new IllegalStateException("Listener already added");
      } else {
         this.sync();
         this.listeners.add(removableListener);
         return this.getRoot().get();
      }
   }

   public void stopTracking(AttachmentConfigListener listener) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         AttachmentConfigTrackerBase.RemovableListener rl = (AttachmentConfigTrackerBase.RemovableListener)iter.next();
         if (rl.listener.equals(listener)) {
            rl.removed = true;
            iter.remove();
            if (this.listeners.isEmpty()) {
               this.stopTracking();
            }

            this.cachedRoot.close();
            this.cachedRoot = new AttachmentConfigTrackerBase.WeakRootReference();
            break;
         }
      }

   }

   public boolean isTracking() {
      return !this.listeners.isEmpty();
   }

   protected void notifyChanges(Collection<AttachmentConfig.Change> changes) {
      ImplicitlySharedList listeners = this.listeners.clone();

      try {
         Iterator var3 = listeners.iterator();

         while(var3.hasNext()) {
            AttachmentConfigTrackerBase.RemovableListener removableListener = (AttachmentConfigTrackerBase.RemovableListener)var3.next();
            Iterator var5 = changes.iterator();

            while(var5.hasNext()) {
               AttachmentConfig.Change change = (AttachmentConfig.Change)var5.next();
               if (removableListener.removed) {
                  break;
               }

               try {
                  removableListener.listener.onChange(change);
               } catch (Throwable var9) {
                  this.logger.log(Level.SEVERE, "Failed to notify an attachment was " + change.changeType(), var9);
               }
            }
         }
      } catch (Throwable var10) {
         if (listeners != null) {
            try {
               listeners.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }
         }

         throw var10;
      }

      if (listeners != null) {
         listeners.close();
      }

   }

   protected void notifyChange(AttachmentConfig.ChangeType changeType, AttachmentConfig attachment) {
      this.notifyChanges(Collections.singleton(new AttachmentConfig.Change(changeType, attachment)));
   }

   protected void runAttachmentAction(AttachmentConfig attachment, Consumer<Attachment> action) {
      if (!this.listeners.isEmpty()) {
         ImplicitlySharedList listeners = this.listeners.clone();

         try {
            Iterator var4 = listeners.iterator();

            while(var4.hasNext()) {
               AttachmentConfigTrackerBase.RemovableListener removableListener = (AttachmentConfigTrackerBase.RemovableListener)var4.next();
               if (!removableListener.removed) {
                  try {
                     removableListener.listener.onAttachmentAction(attachment, action);
                  } catch (Throwable var8) {
                     this.logger.log(Level.SEVERE, "Failed to run attachment action", var8);
                  }
               }
            }
         } catch (Throwable var9) {
            if (listeners != null) {
               try {
                  listeners.close();
               } catch (Throwable var7) {
                  var9.addSuppressed(var7);
               }
            }

            throw var9;
         }

         if (listeners != null) {
            listeners.close();
         }

      }
   }

   public void runAction(int[] childPath, Consumer<Attachment> action) {
      if (!this.listeners.isEmpty()) {
         this.sync();
         if (!this.listeners.isEmpty()) {
            AttachmentConfig config = this.getRoot().get().child(childPath);
            if (config != null) {
               config.runAction(action);
            }
         }
      }

   }

   public void runAction(YamlPath relativePath, Consumer<Attachment> action) {
      if (!this.listeners.isEmpty()) {
         this.sync();
         if (!this.listeners.isEmpty()) {
            AttachmentConfig config = this.getRoot().get().child(relativePath);
            if (config != null) {
               config.runAction(action);
            }
         }
      }

   }

   public List<Attachment> liveAttachments(int[] childPath) {
      ListCallbackCollector<Attachment> collector = new ListCallbackCollector();
      this.runAction((int[])childPath, collector);
      return collector.result();
   }

   public List<Attachment> liveAttachments(YamlPath relativePath) {
      ListCallbackCollector<Attachment> collector = new ListCallbackCollector();
      this.runAction((YamlPath)relativePath, collector);
      return collector.result();
   }

   private static class WeakRootReference {
      private final WeakReference<AttachmentConfig.RootReference> reference;
      private final AttachmentConfig.RootReference.ValidChecker checker;

      public WeakRootReference() {
         this.reference = LogicUtil.nullWeakReference();
         this.checker = () -> {
            return false;
         };
      }

      public WeakRootReference(AttachmentConfig.RootReference ref) {
         this.reference = new WeakReference(ref);
         this.checker = ref.getValidChecker();
      }

      public AttachmentConfig.RootReference getIfValid() {
         AttachmentConfig.RootReference ref = (AttachmentConfig.RootReference)this.reference.get();
         if (ref == null) {
            this.checker.close();
            return null;
         } else {
            return ref.valid() ? ref : null;
         }
      }

      public void close() {
         AttachmentConfig.RootReference ref = (AttachmentConfig.RootReference)this.reference.get();
         if (ref != null) {
            ref.invalidate();
         } else {
            this.checker.close();
         }

      }
   }

   private static class RemovableListener {
      public final AttachmentConfigListener listener;
      public boolean removed;

      public RemovableListener(AttachmentConfigListener listener) {
         this.listener = listener;
         this.removed = false;
      }

      public boolean equals(Object o) {
         return this.listener.equals(((AttachmentConfigTrackerBase.RemovableListener)o).listener);
      }
   }
}
