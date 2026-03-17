package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfig;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfigListener;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfigTracker;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import java.util.Iterator;
import java.util.List;

public abstract class MapWidgetAttachmentTree extends MapWidget {
   private static final int MAX_VISIBLE_DEPTH = 3;
   private MapWidgetAttachmentNode root = MapWidgetAttachmentNode.createNewRoot(this, (new AttachmentConfigTracker(new ConfigurationNode())).getRoot().get());
   private int offset = 0;
   private int count = 6;
   private int lastSelIdx = 0;
   private int column_offset = 0;
   private boolean resetNeeded;
   private AttachmentModel model = null;
   private final AttachmentConfigListener changeListener = new AttachmentConfigListener() {
      private boolean addedOrRemovedAttachments = false;

      public void onChange(AttachmentConfig.Change change) {
         if (change.changeType() != AttachmentConfig.ChangeType.REMOVED && change.changeType() != AttachmentConfig.ChangeType.ADDED) {
            if (change.changeType() == AttachmentConfig.ChangeType.SYNCHRONIZED && this.addedOrRemovedAttachments) {
               this.addedOrRemovedAttachments = false;
               if (MapWidgetAttachmentTree.this.root.sync(change.attachment())) {
                  MapWidgetAttachmentTree.this.updateView(MapWidgetAttachmentTree.this.offset);
               }

               MapWidgetAttachmentTree.this.getWidgets().forEach(MapWidget::invalidate);
            }
         } else {
            this.addedOrRemovedAttachments = true;
         }

      }
   };

   public AttachmentModel getModel() {
      return this.model;
   }

   public void setModel(AttachmentModel model) {
      if (this.model != model) {
         if (this.model != null) {
            this.model.getConfigTracker().stopTracking(this.changeListener);
         }

         this.model = model;
         AttachmentConfig rootConfig = model.getConfigTracker().startTracking(this.changeListener);
         this.root = MapWidgetAttachmentNode.createNewRoot(this, rootConfig);
         this.lastSelIdx = (Integer)this.root.getEditorOption("selectedIndex", 0);
         this.updateView((Integer)this.root.getEditorOption("scrollOffset", 0));
      }
   }

   public void sync() {
      this.model.sync();
      this.getEditor().onSelectedNodeChanged();
   }

   public abstract void onMenuOpen(MapWidgetAttachmentNode var1, MapWidgetAttachmentNode.MenuItem var2);

   public MapWidgetAttachmentNode getRoot() {
      return this.root;
   }

   public MapWidgetAttachmentNode getSelectedNode() {
      return this.lastSelIdx >= 0 && this.lastSelIdx < this.getWidgetCount() ? (MapWidgetAttachmentNode)this.getWidget(this.lastSelIdx) : this.root;
   }

   public void onAttached() {
      this.updateView();
   }

   public void onTick() {
      if (this.resetNeeded) {
         this.updateView(this.offset);
      }

   }

   public void onDraw() {
   }

   public List<MapWidgetAttachmentNode> getVisibleNodes() {
      return (List)CommonUtil.unsafeCast(this.getWidgets());
   }

   public void onKeyPressed(MapKeyEvent event) {
      List<MapWidgetAttachmentNode> widgets = this.getVisibleNodes();
      if (!widgets.isEmpty()) {
         this.lastSelIdx = widgets.indexOf(this.getNextInputWidget());
         MapWidgetAttachmentNode currentlySelected = this.getSelectedNode();
         MapWidgetAttachmentNode selected;
         if (currentlySelected != null && currentlySelected.isChangingOrder()) {
            selected = (MapWidgetAttachmentNode)widgets.get(this.lastSelIdx);
            if (event.getKey() == Key.ENTER || event.getKey() == Key.BACK) {
               selected.setChangingOrder(false);
               this.display.playSound(SoundEffect.CLICK);
               return;
            }

            Key action = event.getKey();
            MapWidgetAttachmentNode parent;
            List attachments;
            int from_index;
            int to_index;
            if (action == Key.UP || action == Key.DOWN) {
               parent = selected.getParentAttachment();
               if (parent != null) {
                  attachments = parent.getChildAttachmentNodes();
                  from_index = attachments.indexOf(selected);
                  to_index = from_index + (action == Key.UP ? -1 : 1);
                  if (to_index >= 0 && to_index < attachments.size()) {
                     attachments.remove(from_index);
                     selected.getAttachmentConfig().remove();
                     selected = parent.addAttachment(to_index, selected.getConfig());
                     selected.setChangingOrder(true);
                     this.updateView(this.offset);
                     this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "sync");
                  }
               }
            }

            if (action == Key.LEFT) {
               parent = selected.getParentAttachment();
               if (parent != null && parent.getParentAttachment() != null) {
                  attachments = parent.getChildAttachmentNodes();
                  from_index = attachments.indexOf(selected);
                  MapWidgetAttachmentNode removed = (MapWidgetAttachmentNode)attachments.remove(from_index);
                  selected.getAttachmentConfig().remove();
                  List<MapWidgetAttachmentNode> parentAttachments = parent.getParentAttachment().getChildAttachmentNodes();
                  selected = parent.getParentAttachment().addAttachment(parentAttachments.indexOf(parent) + 1, selected.getConfig());
                  selected.setChangingOrder(true);
                  this.updateView(this.offset);
                  this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "sync");
               }
            }

            if (action == Key.RIGHT) {
               parent = selected.getParentAttachment();
               if (parent != null) {
                  attachments = parent.getChildAttachmentNodes();
                  from_index = attachments.indexOf(selected);
                  to_index = from_index - 1;
                  if (to_index >= 0 && to_index < attachments.size()) {
                     MapWidgetAttachmentNode new_parent = (MapWidgetAttachmentNode)attachments.get(to_index);
                     attachments.remove(from_index);
                     selected.getAttachmentConfig().remove();
                     selected = new_parent.addAttachment(selected.getConfig());
                     selected.setChangingOrder(true);
                     new_parent.setExpanded(true);
                     this.updateView(this.offset);
                     this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "sync");
                  }
               }
            }

            this.setSelectedNode(selected);
         } else if (event.getKey() == Key.UP) {
            if (this.lastSelIdx > 0) {
               this.setSelectedIndex(this.lastSelIdx - 1);
            } else if (this.offset > 0) {
               this.lastSelIdx = -1;
               this.updateView(this.offset - 1);
               widgets = this.getVisibleNodes();
               this.setSelectedIndex(0);
            }
         } else if (event.getKey() == Key.DOWN) {
            if (this.lastSelIdx < widgets.size() - 1) {
               this.setSelectedIndex(this.lastSelIdx + 1);
            } else {
               selected = (MapWidgetAttachmentNode)widgets.get(widgets.size() - 1);
               MapWidgetAttachmentNode lastTreeNode = findLastNode(this.root);
               if (selected != lastTreeNode) {
                  this.lastSelIdx = -1;
                  this.updateView(this.offset + 1);
                  widgets = this.getVisibleNodes();
                  this.setSelectedIndex(widgets.size() - 1);
               }
            }
         } else {
            super.onKeyPressed(event);
         }

         if (this.resetNeeded) {
            this.updateView(this.offset);
            widgets = this.getVisibleNodes();
         }

         if (this.getSelectedNode() != currentlySelected) {
            this.getEditor().onSelectedNodeChanged();
         }

      }
   }

   public void setSelectedNode(MapWidgetAttachmentNode node) {
      boolean changed = this.getSelectedNode() != node;
      int new_index = this.findIndexOf(node) - this.offset;
      if (new_index != this.lastSelIdx) {
         this.resetNeeded = true;
      }

      int a = new_index - this.getWidgetCount() + 1;
      if (a > 0) {
         this.offset += a;
         new_index -= a;
      }

      if (new_index < 0) {
         this.offset += new_index;
         new_index = 0;
      }

      this.setSelectedIndex(new_index);
      if (changed) {
         this.getEditor().onSelectedNodeChanged();
      }

   }

   public AttachmentEditor getEditor() {
      return (AttachmentEditor)super.getDisplay();
   }

   public void updateView() {
      this.resetNeeded = true;
   }

   public void updateView(int offset) {
      this.offset = offset;
      this.root.setEditorOption("scrollOffset", 0, offset);
      this.clearWidgets();
      MapWidgetAttachmentTree.UpdateViewOp op = new MapWidgetAttachmentTree.UpdateViewOp();
      op.offset = this.offset;
      op.count = this.count;
      op.num_visible_nodes = 0;
      op.col = 0;
      op.row = 0;
      op.min_col = Integer.MAX_VALUE;
      op.max_col = 0;
      this.column_offset = 0;
      this.updateView(this.root, op);
      int new_selidx;
      if (op.num_visible_nodes <= this.count && this.offset > 0) {
         int new_offset = 0;
         new_selidx = this.lastSelIdx - (new_offset - offset);
         this.updateView(new_offset);
         this.setSelectedIndex(new_selidx);
      } else if (op.count > 0 && op.num_visible_nodes > this.count) {
         int new_offset = op.num_visible_nodes - this.count;
         new_selidx = this.lastSelIdx - (new_offset - offset);
         this.updateView(new_offset);
         this.setSelectedIndex(new_selidx);
      } else {
         this.resetNeeded = false;
         if (this.lastSelIdx >= 0 && this.getWidgetCount() > 0) {
            if (this.lastSelIdx >= this.getWidgetCount()) {
               this.setSelectedIndex(this.getWidgetCount() - 1);
            } else {
               this.setSelectedIndex(this.lastSelIdx);
            }
         }

      }
   }

   private void updateView(MapWidgetAttachmentNode node, MapWidgetAttachmentTree.UpdateViewOp op) {
      ++op.num_visible_nodes;
      if (op.offset > 0) {
         --op.offset;
      } else {
         if (op.count <= 0) {
            return;
         }

         if (op.col < op.min_col) {
            op.min_col = op.col;
         }

         if (op.col > op.max_col) {
            op.max_col = op.col;
         }

         node.setCell(op.col, op.row);
         node.setPosition(0, this.getWidgets().size() * 17);
         this.addWidget(node);
         --op.count;
      }

      ++op.row;
      if (node.isExpanded()) {
         ++op.col;
         Iterator var3 = node.getChildAttachmentNodes().iterator();

         while(var3.hasNext()) {
            MapWidgetAttachmentNode childAttachment = (MapWidgetAttachmentNode)var3.next();
            this.updateView(childAttachment, op);
         }

         --op.col;
      }

   }

   private void setSelectedIndex(int newIndex) {
      if (this.lastSelIdx != newIndex) {
         this.lastSelIdx = newIndex;
         this.root.setEditorOption("selectedIndex", 0, newIndex);
      }

      if (this.lastSelIdx >= 0 && this.lastSelIdx < this.getWidgetCount()) {
         this.computeColumnOffset();
         this.getWidget(this.lastSelIdx).focus();
      }

   }

   private void computeColumnOffset() {
      MapWidgetAttachmentNode selectedNode = (MapWidgetAttachmentNode)this.getWidget(this.lastSelIdx);
      int new_column_offset = 3 - (selectedNode.getCellColumn() - this.column_offset);
      if (new_column_offset > 0) {
         new_column_offset = 0;
      }

      if (new_column_offset != this.column_offset) {
         Iterator var3 = this.getVisibleNodes().iterator();

         while(var3.hasNext()) {
            MapWidgetAttachmentNode node = (MapWidgetAttachmentNode)var3.next();
            node.setCell(node.getCellColumn() - this.column_offset + new_column_offset, node.getCellRow());
            node.invalidate();
         }

         this.column_offset = new_column_offset;
      }

   }

   private static MapWidgetAttachmentNode findLastNode(MapWidgetAttachmentNode node) {
      List<MapWidgetAttachmentNode> children = node.getChildAttachmentNodes();
      return node.isExpanded() && !children.isEmpty() ? findLastNode((MapWidgetAttachmentNode)children.get(children.size() - 1)) : node;
   }

   private int findIndexOf(MapWidgetAttachmentNode node) {
      MapWidgetAttachmentTree.FindIndexOp op = new MapWidgetAttachmentTree.FindIndexOp();
      op.index = 0;
      op.node = node;
      return searchForNode(this.root, op) ? op.index : -1;
   }

   private static boolean searchForNode(MapWidgetAttachmentNode parent, MapWidgetAttachmentTree.FindIndexOp op) {
      if (parent == op.node) {
         return true;
      } else {
         ++op.index;
         if (parent.isExpanded()) {
            Iterator var2 = parent.getChildAttachmentNodes().iterator();

            while(var2.hasNext()) {
               MapWidgetAttachmentNode child = (MapWidgetAttachmentNode)var2.next();
               if (searchForNode(child, op)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private static class UpdateViewOp {
      public int offset;
      public int count;
      public int num_visible_nodes;
      public int col;
      public int row;
      public int max_col;
      public int min_col;

      private UpdateViewOp() {
      }

      // $FF: synthetic method
      UpdateViewOp(Object x0) {
         this();
      }
   }

   private static class FindIndexOp {
      public int index;
      MapWidgetAttachmentNode node;

      private FindIndexOp() {
      }

      // $FF: synthetic method
      FindIndexOp(Object x0) {
         this();
      }
   }
}
