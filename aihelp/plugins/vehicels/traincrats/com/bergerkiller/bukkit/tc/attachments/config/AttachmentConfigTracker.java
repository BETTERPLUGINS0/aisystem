package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.RunOnceTask;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.yaml.YamlChangeListener;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public class AttachmentConfigTracker extends AttachmentConfigTrackerBase implements YamlChangeListener {
   private final Supplier<ConfigurationNode> completeConfigSupplier;
   private ConfigurationNode completeConfig;
   private final AttachmentConfigTracker.SyncTask syncTask;
   private final Map<ConfigurationNode, AttachmentConfigTracker.TrackedAttachmentConfig> byConfig;
   private final List<AttachmentConfig.Change> pendingChanges;
   private final Set<AttachmentConfigTracker.TrackedAttachmentConfig> attachmentsWithChanges;
   private boolean isSynchronizing;
   private AttachmentConfigTracker.TrackedAttachmentConfig root;
   private int modificationCount;

   public AttachmentConfigTracker(ConfigurationNode completeConfig) {
      this((ConfigurationNode)completeConfig, (Plugin)null);
   }

   public AttachmentConfigTracker(ConfigurationNode completeConfig, Plugin plugin) {
      this(LogicUtil.constantSupplier(completeConfig), plugin);
   }

   public AttachmentConfigTracker(Supplier<ConfigurationNode> completeConfigSupplier) {
      this((Supplier)completeConfigSupplier, (Plugin)null);
   }

   public AttachmentConfigTracker(Supplier<ConfigurationNode> completeConfigSupplier, Plugin plugin) {
      super(plugin == null ? Logger.getGlobal() : plugin.getLogger());
      this.attachmentsWithChanges = new LinkedHashSet();
      this.isSynchronizing = false;
      this.modificationCount = 0;
      this.completeConfigSupplier = completeConfigSupplier;
      this.completeConfig = null;
      this.syncTask = plugin == null ? null : new AttachmentConfigTracker.SyncTask(plugin);
      this.byConfig = new IdentityHashMap();
      this.pendingChanges = new ArrayList();
      this.root = null;
   }

   protected void startTracking() {
      this.completeConfig = (ConfigurationNode)this.completeConfigSupplier.get();
      if (this.completeConfig == null) {
         this.completeConfig = new ConfigurationNode();
      }

      this.root = this.createNewRoot(this.completeConfig);
      this.root.addToTracker();
      this.completeConfig.addChangeListener(this);
      this.resetChanges();
      ++this.modificationCount;
   }

   protected void stopTracking() {
      ++this.modificationCount;
      this.completeConfig.removeChangeListener(this);
      this.completeConfig = null;
      this.resetChanges();
      this.root = null;
      if (this.syncTask != null) {
         this.syncTask.cancel();
      }

   }

   private void resetChanges() {
      this.pendingChanges.clear();
      this.attachmentsWithChanges.clear();
   }

   protected AttachmentConfig.RootReference createRootReference() {
      if (this.isTracking()) {
         int currModCount = this.modificationCount;
         return new AttachmentConfig.RootReference(this.root, () -> {
            return this.modificationCount == currModCount;
         });
      } else {
         ConfigurationNode configSnapshot = (ConfigurationNode)this.completeConfigSupplier.get();
         if (configSnapshot == null) {
            configSnapshot = new ConfigurationNode();
         }

         AttachmentConfig tempRoot = this.createNewRoot(configSnapshot);
         return new AttachmentConfig.RootReference(tempRoot, new AttachmentConfigTracker.ConfigInvalidChecker(configSnapshot));
      }
   }

   public ConfigurationNode getConfig() {
      ConfigurationNode config = this.completeConfig;
      return config != null ? config : (ConfigurationNode)this.completeConfigSupplier.get();
   }

   public void sync() {
      if (this.syncTask != null) {
         this.syncTask.cancel();
      }

      this.handleSync();
   }

   private void handleSync() {
      List<AttachmentConfig.Change> pendingChanges = this.pendingChanges;
      if (this.isTracking() && !this.isSynchronizing) {
         this.isSynchronizing = true;

         try {
            this.processYamlChanges();
            if (!this.attachmentsWithChanges.isEmpty()) {
               Iterator var2 = this.attachmentsWithChanges.iterator();

               while(var2.hasNext()) {
                  AttachmentConfigTracker.TrackedAttachmentConfig config = (AttachmentConfigTracker.TrackedAttachmentConfig)var2.next();
                  if (!config.isRemoved()) {
                     this.addChange(AttachmentConfig.ChangeType.CHANGED, config);
                  }
               }

               this.attachmentsWithChanges.clear();
            }

            if (!pendingChanges.isEmpty()) {
               pendingChanges.add(new AttachmentConfig.Change(AttachmentConfig.ChangeType.SYNCHRONIZED, this.root));
               this.notifyChanges(pendingChanges);
            }
         } finally {
            this.isSynchronizing = false;
            this.resetChanges();
         }

      }
   }

   private void processYamlChanges() {
      ConfigurationNode config = (ConfigurationNode)this.completeConfigSupplier.get();
      if (config != this.completeConfig) {
         this.resetChanges();
         this.completeConfig.removeChangeListener(this);
         this.completeConfig = config;
         this.root.swap(this.createNewRoot(config));
         this.completeConfig.addChangeListener(this);
      } else {
         this.root.sync(this.completeConfig.getYamlPath());
      }
   }

   public void onNodeChanged(YamlPath yamlPath) {
      YamlPath attachmentPath;
      AttachmentConfigTracker.TrackedAttachmentConfig attachment;
      if (yamlPath.name().equals("attachments")) {
         attachmentPath = yamlPath.parent();
         attachment = this.findAttachment(attachmentPath);
         if (attachment != null && !attachment.childrenRefreshNeeded) {
            attachment.childrenRefreshNeeded = true;
            attachment.markChanged();
         }
      } else {
         attachmentPath = getAttachmentPath(yamlPath);
         if (yamlPath != attachmentPath) {
            YamlPath tmp = yamlPath;
            int depthDiff = yamlPath.depth() - attachmentPath.depth();

            while(true) {
               --depthDiff;
               if (depthDiff <= 0) {
                  if (tmp.name().equals("editor")) {
                     return;
                  }
                  break;
               }

               tmp = tmp.parent();
            }
         }

         attachment = this.findAttachment(attachmentPath);
         if (attachment != null && !attachment.configChanged) {
            attachment.configChanged = true;
            attachment.markChanged();
         }
      }

      ++this.modificationCount;
      if (this.syncTask != null && this.syncTask.getPlugin().isEnabled()) {
         this.syncTask.start();
      }

   }

   private void addChange(AttachmentConfig.ChangeType changeType, AttachmentConfigTracker.TrackedAttachmentConfig attachment) {
      this.pendingChanges.add(new AttachmentConfig.Change(changeType, attachment));
   }

   private AttachmentConfigTracker.TrackedAttachmentConfig findAttachment(YamlPath path) {
      return (AttachmentConfigTracker.TrackedAttachmentConfig)this.byConfig.get(this.completeConfig.getNodeIfExists(path));
   }

   private static YamlPath getAttachmentPath(YamlPath path) {
      while(true) {
         if (!path.isRoot()) {
            YamlPath parent = path.parent();
            if (!parent.name().equals("attachments")) {
               path = parent;
               continue;
            }
         }

         return path;
      }
   }

   private static boolean isEmptyConfiguration(ConfigurationNode config) {
      return !config.contains("type") && config.getNodeList("attachments").isEmpty();
   }

   private static String readAttachmentTypeId(ConfigurationNode config, String defaultType) {
      Object typeIdObj = config.get("type");
      return typeIdObj == null ? defaultType : typeIdObj.toString();
   }

   private static String readModelName(ConfigurationNode config) {
      Object modelNameObj = config.get("modelName");
      return modelNameObj == null ? "" : modelNameObj.toString();
   }

   private AttachmentConfigTracker.TrackedAttachmentConfig createNewRoot(ConfigurationNode config) {
      return this.createNewConfig((AttachmentConfigTracker.TrackedAttachmentConfig)null, config.getYamlPath(), config, 0);
   }

   private AttachmentConfigTracker.TrackedAttachmentConfig createNewConfig(AttachmentConfigTracker.TrackedAttachmentConfig parent, YamlPath rootPath, ConfigurationNode config, int childIndex) {
      String typeId = readAttachmentTypeId(config, (String)null);
      if (typeId == null) {
         typeId = "EMPTY";
         if (config.getNodeList("attachments").isEmpty()) {
            return new AttachmentConfigTracker.TrackedEmptyAttachmentConfig(parent, rootPath, config, typeId, childIndex);
         }
      }

      if (typeId.equals("MODEL")) {
         String modelName = readModelName(config);
         return (AttachmentConfigTracker.TrackedAttachmentConfig)(modelName.isEmpty() ? new AttachmentConfigTracker.TrackedEmptyModelAttachmentConfig(parent, rootPath, config, typeId, childIndex) : new AttachmentConfigTracker.TrackedModelAttachmentConfig(parent, rootPath, config, typeId, modelName, childIndex));
      } else {
         return new AttachmentConfigTracker.TrackedAttachmentConfig(parent, rootPath, config, typeId, childIndex);
      }
   }

   private class TrackedAttachmentConfig implements AttachmentConfig {
      private final AttachmentConfigTracker.TrackedAttachmentConfig parent;
      private final List<AttachmentConfigTracker.TrackedAttachmentConfig> children;
      private YamlPath path;
      private final ConfigurationNode config;
      private final String typeId;
      private int childIndex;
      private boolean changed;
      private boolean configChanged;
      private boolean childrenRefreshNeeded;
      private boolean removed;

      private TrackedAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig parent, YamlPath rootPath, ConfigurationNode config, String typeId, int childIndex) {
         this.parent = parent;
         this.children = new ArrayList();
         this.path = config.getYamlPath().makeRelative(rootPath);
         this.config = config;
         this.typeId = typeId;
         this.childIndex = childIndex;
         this.changed = false;
         this.configChanged = false;
         this.childrenRefreshNeeded = false;
         this.removed = true;
         int index = -1;
         Iterator var8 = config.getNodeList("attachments").iterator();

         while(var8.hasNext()) {
            ConfigurationNode childNode = (ConfigurationNode)var8.next();
            ++index;
            this.children.add(AttachmentConfigTracker.this.createNewConfig(this, rootPath, childNode, index));
         }

      }

      public AttachmentConfig parent() {
         return this.parent;
      }

      public List<AttachmentConfig> children() {
         return Collections.unmodifiableList(this.children);
      }

      public AttachmentConfig addChild(int childIndex, ConfigurationNode config) {
         if (!this.removed && this.childrenRefreshNeeded) {
            AttachmentConfigTracker.this.processYamlChanges();
         }

         if (this.removed) {
            throw new UnsupportedOperationException("Cannot add a child because the parent attachment has already been removed");
         } else if (childIndex >= 0 && childIndex <= this.children.size()) {
            this.config.getNodeList("attachments").add(childIndex, config);
            AttachmentConfig added = this.addTrackedChild(AttachmentConfigTracker.this.completeConfig.getYamlPath(), childIndex, config);

            for(int i = childIndex + 1; i < this.children.size(); ((AttachmentConfigTracker.TrackedAttachmentConfig)this.children.get(i)).childIndex = i++) {
            }

            return added;
         } else {
            throw new IndexOutOfBoundsException("Child add index out of bounds: " + childIndex);
         }
      }

      private AttachmentConfigTracker.TrackedAttachmentConfig addTrackedChild(YamlPath rootPath, int childIndex, ConfigurationNode childConfig) {
         AttachmentConfigTracker.TrackedAttachmentConfig attachment = AttachmentConfigTracker.this.createNewConfig(this, rootPath, childConfig, childIndex);
         this.children.add(childIndex, attachment);
         attachment.addToTracker();
         AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.ADDED, attachment);
         return attachment;
      }

      public boolean isRemoved() {
         return this.removed;
      }

      public void remove() {
         if (!this.removed) {
            if (this.parent == null) {
               throw new UnsupportedOperationException("Cannot remove a root attachment");
            } else {
               this.config.remove();
               this.parent.children.remove(this);
               this.removeFromTracker();
               int size = this.parent.children.size();

               for(int i = 0; i < size; ((AttachmentConfigTracker.TrackedAttachmentConfig)this.parent.children.get(i)).childIndex = i++) {
               }

               if (AttachmentConfigTracker.this.isTracking()) {
                  AttachmentConfigTracker.this.modificationCount++;
                  AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.REMOVED, this);
               }

            }
         }
      }

      public int childIndex() {
         return this.childIndex;
      }

      public YamlPath path() {
         return this.path;
      }

      public String typeId() {
         return this.typeId;
      }

      public ConfigurationNode config() {
         return this.config;
      }

      public boolean isEmptyConfig() {
         return false;
      }

      public void runAction(Consumer<Attachment> action) {
         if (!this.removed) {
            AttachmentConfigTracker.this.runAttachmentAction(this, action);
         }

      }

      public String toString() {
         return "Attachment{" + this.typeId() + " at " + Arrays.toString(this.childPath()) + "}";
      }

      private void swap(AttachmentConfigTracker.TrackedAttachmentConfig replacement) {
         AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.REMOVED, this);
         if (this.parent == null) {
            AttachmentConfigTracker.this.byConfig.clear();
            this.markRemovedRecurse();
            AttachmentConfigTracker.this.root = replacement;
         } else {
            if (this.parent.children.get(this.childIndex) != this) {
               throw new IllegalStateException("Self not found as child in parent");
            }

            this.removeFromTracker();
            this.parent.children.set(this.childIndex, replacement);
         }

         replacement.addToTracker();
         AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.ADDED, replacement);
      }

      private void markRemovedRecurse() {
         this.childrenRefreshNeeded = false;
         this.removed = true;
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            AttachmentConfigTracker.TrackedAttachmentConfig child = (AttachmentConfigTracker.TrackedAttachmentConfig)var1.next();
            child.markRemovedRecurse();
         }

      }

      private void sync(YamlPath rootPath) {
         this.updatePath(rootPath);
         if (this.changed) {
            this.changed = false;
            if (this.configChanged) {
               this.configChanged = false;
               if (this.handleLoad()) {
                  AttachmentConfigTracker.this.attachmentsWithChanges.add(this);
                  if (!this.childrenRefreshNeeded && this.children.isEmpty() == this.config.contains("attachments")) {
                     this.childrenRefreshNeeded = true;
                  }
               } else {
                  this.swap(AttachmentConfigTracker.this.createNewConfig(this.parent, rootPath, this.config, this.childIndex));
               }
            }

            if (this.childrenRefreshNeeded) {
               this.childrenRefreshNeeded = false;
               this.updateChildren(rootPath);
            }

            if (!this.removed) {
               Iterator var2 = this.children.iterator();

               while(var2.hasNext()) {
                  AttachmentConfigTracker.TrackedAttachmentConfig child = (AttachmentConfigTracker.TrackedAttachmentConfig)var2.next();
                  child.sync(rootPath);
               }
            }
         }

      }

      private void updateChildren(YamlPath rootPath) {
         List<ConfigurationNode> currChildNodes = this.config.getNodeList("attachments");
         int childIndex = 0;
         AttachmentConfigTracker.EfficientListContainsChecker<ConfigurationNode> checker = new AttachmentConfigTracker.EfficientListContainsChecker(currChildNodes);
         Iterator iter = this.children.iterator();

         AttachmentConfigTracker.TrackedAttachmentConfig attachment;
         while(iter.hasNext()) {
            attachment = (AttachmentConfigTracker.TrackedAttachmentConfig)iter.next();
            attachment.childIndex = childIndex;
            if (checker.test(attachment.config)) {
               ++childIndex;
            } else {
               iter.remove();
               attachment.removeFromTracker();
               AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.REMOVED, attachment);
            }
         }

         childIndex = 0;
         Iterator var8 = currChildNodes.iterator();

         while(true) {
            while(var8.hasNext()) {
               ConfigurationNode childConfig = (ConfigurationNode)var8.next();
               if (childIndex < this.children.size()) {
                  attachment = (AttachmentConfigTracker.TrackedAttachmentConfig)this.children.get(childIndex);
                  if (attachment.config == childConfig) {
                     attachment.childIndex = childIndex++;
                     continue;
                  }

                  for(int i = childIndex + 1; i < this.children.size(); ++i) {
                     attachment = (AttachmentConfigTracker.TrackedAttachmentConfig)this.children.get(i);
                     if (attachment.config == childConfig) {
                        attachment.childIndex = i;
                        this.children.remove(i);
                        attachment.removeFromTracker();
                        AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.REMOVED, attachment);
                        break;
                     }
                  }
               }

               this.addTrackedChild(rootPath, childIndex, childConfig);
               ++childIndex;
            }

            while(childIndex < this.children.size()) {
               AttachmentConfigTracker.TrackedAttachmentConfig attachmentx = (AttachmentConfigTracker.TrackedAttachmentConfig)this.children.remove(childIndex);
               attachmentx.childIndex = childIndex;
               attachmentx.removeFromTracker();
               AttachmentConfigTracker.this.addChange(AttachmentConfig.ChangeType.REMOVED, attachmentx);
            }

            return;
         }
      }

      private void updatePath(YamlPath rootPath) {
         if (this.parent != null) {
            this.path = this.config.getYamlPath().makeRelative(rootPath);
         }

      }

      protected boolean handleLoad() {
         return AttachmentConfigTracker.isEmptyConfiguration(this.config) ? false : this.typeId.equals(AttachmentConfigTracker.readAttachmentTypeId(this.config, "EMPTY"));
      }

      private void addToTracker() {
         AttachmentConfigTracker.this.byConfig.put(this.config, this);
         this.removed = false;
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            AttachmentConfigTracker.TrackedAttachmentConfig child = (AttachmentConfigTracker.TrackedAttachmentConfig)var1.next();
            child.addToTracker();
         }

      }

      private void removeFromTracker() {
         this.removed = true;
         this.childrenRefreshNeeded = false;
         AttachmentConfigTracker.this.byConfig.remove(this.config, this);
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            AttachmentConfigTracker.TrackedAttachmentConfig child = (AttachmentConfigTracker.TrackedAttachmentConfig)var1.next();
            child.removeFromTracker();
         }

      }

      private void markChanged() {
         for(AttachmentConfigTracker.TrackedAttachmentConfig att = this; att != null && !att.changed; att = att.parent) {
            att.changed = true;
         }

      }

      // $FF: synthetic method
      TrackedAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig x1, YamlPath x2, ConfigurationNode x3, String x4, int x5, Object x6) {
         this(x1, x2, x3, x4, x5);
      }
   }

   private class SyncTask extends RunOnceTask {
      public SyncTask(Plugin plugin) {
         super(plugin);
      }

      public void run() {
         AttachmentConfigTracker.this.handleSync();
      }
   }

   private class ConfigInvalidChecker implements AttachmentConfig.RootReference.ValidChecker, YamlChangeListener {
      private final ConfigurationNode configSnapshot;
      private final int modCountSnapshot;
      private boolean valid;

      public ConfigInvalidChecker(ConfigurationNode configSnapshot) {
         this.configSnapshot = configSnapshot;
         this.configSnapshot.addChangeListener(this);
         this.modCountSnapshot = AttachmentConfigTracker.this.modificationCount;
         this.valid = true;
      }

      public boolean valid() {
         if (!this.valid) {
            return false;
         } else if (!AttachmentConfigTracker.this.isTracking() && this.modCountSnapshot == AttachmentConfigTracker.this.modificationCount && this.configSnapshot == AttachmentConfigTracker.this.completeConfigSupplier.get()) {
            return true;
         } else {
            this.close();
            return false;
         }
      }

      public void close() {
         if (this.valid) {
            this.valid = false;
            this.configSnapshot.removeChangeListener(this);
         }

      }

      public void onNodeChanged(YamlPath yamlPath) {
         this.close();
      }
   }

   private class TrackedEmptyAttachmentConfig extends AttachmentConfigTracker.TrackedAttachmentConfig {
      public TrackedEmptyAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig parent, YamlPath rootPath, ConfigurationNode config, String typeId, int childIndex) {
         super(parent, rootPath, config, typeId, childIndex, null);
      }

      public boolean isEmptyConfig() {
         return true;
      }

      protected boolean handleLoad() {
         return super.handleLoad() && AttachmentConfigTracker.isEmptyConfiguration(this.config());
      }
   }

   private class TrackedEmptyModelAttachmentConfig extends AttachmentConfigTracker.TrackedAttachmentConfig {
      public TrackedEmptyModelAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig parent, YamlPath rootPath, ConfigurationNode config, String typeId, int childIndex) {
         super(parent, rootPath, config, typeId, childIndex, null);
      }

      protected boolean handleLoad() {
         return super.handleLoad() && AttachmentConfigTracker.readModelName(this.config()).isEmpty();
      }
   }

   private class TrackedModelAttachmentConfig extends AttachmentConfigTracker.TrackedAttachmentConfig implements AttachmentConfig.Model {
      private final String modelName;

      private TrackedModelAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig parent, YamlPath rootPath, ConfigurationNode config, String typeId, String modelName, int childIndex) {
         super(parent, rootPath, config, typeId, childIndex, null);
         this.modelName = modelName;
      }

      protected boolean handleLoad() {
         return super.handleLoad() && this.modelName.equals(AttachmentConfigTracker.readModelName(this.config()));
      }

      public String modelName() {
         return this.modelName;
      }

      // $FF: synthetic method
      TrackedModelAttachmentConfig(AttachmentConfigTracker.TrackedAttachmentConfig x1, YamlPath x2, ConfigurationNode x3, String x4, String x5, int x6, Object x7) {
         this(x1, x2, x3, x4, x5, x6);
      }
   }

   private static class EfficientListContainsChecker<T> implements Predicate<T> {
      private final List<T> list;
      private int currentIndex;

      private EfficientListContainsChecker(List<T> list) {
         this.list = list;
         this.currentIndex = 0;
      }

      public boolean test(T t) {
         int size = this.list.size();
         if (size == 0) {
            return false;
         } else {
            int i = this.currentIndex;

            while(this.list.get(i) != t) {
               ++i;
               i %= size;
               if (i == this.currentIndex) {
                  return false;
               }
            }

            this.currentIndex = (i + 1) % size;
            return true;
         }
      }

      // $FF: synthetic method
      EfficientListContainsChecker(List x0, Object x1) {
         this(x0);
      }
   }
}
