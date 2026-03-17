package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.utils.ListCallbackCollector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface AttachmentConfig {
   AttachmentConfig parent();

   default boolean isRoot() {
      return this.parent() == null;
   }

   List<AttachmentConfig> children();

   default AttachmentConfig child(int childIndex) {
      List<AttachmentConfig> children = this.children();
      return childIndex >= 0 && childIndex < children.size() ? (AttachmentConfig)children.get(childIndex) : null;
   }

   default AttachmentConfig child(int[] childPath) {
      AttachmentConfig p = this;
      int len = childPath.length;
      if (len > 0) {
         int i = 0;

         do {
            p = p.child(childPath[i]);
            ++i;
         } while(i < len && p != null);
      }

      return p;
   }

   default AttachmentConfig child(YamlPath path) {
      if (path.isRoot()) {
         return this;
      } else {
         AttachmentConfig currentAttachment = this;
         YamlPath resultPath = path;
         YamlPath searchPath = YamlPath.join(this.path(), path);

         boolean found;
         do {
            found = false;
            Iterator var6 = currentAttachment.children().iterator();

            while(var6.hasNext()) {
               AttachmentConfig child = (AttachmentConfig)var6.next();
               YamlPath childRelativePath = searchPath.makeRelative(child.path());
               if (childRelativePath != null) {
                  currentAttachment = child;
                  resultPath = childRelativePath;
                  found = true;
                  break;
               }
            }
         } while(found);

         if (!resultPath.isRoot()) {
            while(!resultPath.parent().isRoot()) {
               resultPath = resultPath.parent();
            }

            if (resultPath.name().equals("attachments")) {
               return null;
            }
         }

         return currentAttachment;
      }
   }

   default AttachmentConfig removeChild(int childIndex) {
      AttachmentConfig child = this.child(childIndex);
      if (child == null) {
         throw new IndexOutOfBoundsException("Child index out of bounds: " + childIndex);
      } else {
         child.remove();
         return child;
      }
   }

   default AttachmentConfig addChild(ConfigurationNode config) {
      return this.addChild(this.children().size(), config);
   }

   AttachmentConfig addChild(int var1, ConfigurationNode var2);

   boolean isRemoved();

   void remove();

   int childIndex();

   default int[] childPath() {
      ArrayList<AttachmentConfig> parents = new ArrayList(10);

      for(AttachmentConfig a = this; a.parent() != null; a = a.parent()) {
         parents.add(a);
      }

      int[] path = new int[parents.size()];
      int i = 0;

      for(int j = path.length - 1; j >= 0; ++i) {
         path[i] = ((AttachmentConfig)parents.get(j)).childIndex();
         --j;
      }

      return path;
   }

   YamlPath path();

   String typeId();

   ConfigurationNode config();

   boolean isEmptyConfig();

   default void setConfig(ConfigurationNode config) {
      this.config().setToExcept(config, Collections.singletonList("attachments"));
   }

   void runAction(Consumer<Attachment> var1);

   default List<Attachment> liveAttachments() {
      ListCallbackCollector<Attachment> collector = new ListCallbackCollector();
      this.runAction(collector);
      return collector.result();
   }

   default <T extends Attachment> List<T> liveAttachmentsOfType(Class<T> type) {
      ListCallbackCollector<T> collector = new ListCallbackCollector();
      this.runAction((attachment) -> {
         if (type.isInstance(attachment)) {
            collector.accept(attachment);
         }

      });
      return collector.result();
   }

   public static final class RootReference {
      public static final AttachmentConfig.RootReference NONE = new AttachmentConfig.RootReference((AttachmentConfig)null, () -> {
         return false;
      });
      private AttachmentConfig root;
      private AttachmentConfig.RootReference.ValidChecker validChecker;

      RootReference(AttachmentConfig root, AttachmentConfig.RootReference.ValidChecker validChecker) {
         this.root = root;
         this.validChecker = validChecker;
      }

      public AttachmentConfig get() {
         if (this.valid()) {
            return this.root;
         } else {
            throw new IllegalStateException("This root reference is no longer valid");
         }
      }

      public boolean valid() {
         AttachmentConfig.RootReference.ValidChecker checker = this.validChecker;
         if (checker != null) {
            if (checker.valid()) {
               return true;
            }

            this.invalidate();
         }

         return false;
      }

      public void invalidate() {
         AttachmentConfig.RootReference.ValidChecker checker = this.validChecker;
         this.root = null;
         this.validChecker = null;
         if (checker != null) {
            checker.close();
         }

      }

      AttachmentConfig.RootReference.ValidChecker getValidChecker() {
         AttachmentConfig.RootReference.ValidChecker checker = this.validChecker;
         return checker != null ? checker : () -> {
            return false;
         };
      }

      @FunctionalInterface
      interface ValidChecker {
         boolean valid();

         default void close() {
         }
      }
   }

   public static enum ChangeType {
      ADDED(AttachmentConfigListener::onAttachmentAdded),
      REMOVED(AttachmentConfigListener::onAttachmentRemoved),
      CHANGED(AttachmentConfigListener::onAttachmentChanged),
      SYNCHRONIZED(AttachmentConfigListener::onSynchronized);

      private final BiConsumer<AttachmentConfigListener, AttachmentConfig> callback;

      private ChangeType(BiConsumer<AttachmentConfigListener, AttachmentConfig> callback) {
         this.callback = callback;
      }

      public BiConsumer<AttachmentConfigListener, AttachmentConfig> callback() {
         return this.callback;
      }

      // $FF: synthetic method
      private static AttachmentConfig.ChangeType[] $values() {
         return new AttachmentConfig.ChangeType[]{ADDED, REMOVED, CHANGED, SYNCHRONIZED};
      }
   }

   public static final class Change {
      private final AttachmentConfig.ChangeType changeType;
      private final AttachmentConfig attachment;

      public Change(AttachmentConfig.ChangeType changeType, AttachmentConfig attachment) {
         this.changeType = changeType;
         this.attachment = attachment;
      }

      public AttachmentConfig attachment() {
         return this.attachment;
      }

      public AttachmentConfig.ChangeType changeType() {
         return this.changeType;
      }

      public String toString() {
         return "{" + this.changeType.name() + " " + this.attachment.path() + "}";
      }
   }

   public interface Model extends AttachmentConfig {
      String MODEL_NAME_CONFIG_KEY = "modelName";

      String modelName();
   }
}
