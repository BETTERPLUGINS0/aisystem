package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ConfigureAnimationNodeDialog extends MapWidgetMenu {
   private final AnimationNode _average;
   private List<ConfigureAnimationNodeDialog.Node> _nodes;
   private MapWidgetSubmitText sceneMarkerSubmit = null;

   public ConfigureAnimationNodeDialog(List<AnimationNode> nodes) {
      this.setBackgroundColor((byte)30);
      this._average = AnimationNode.average(nodes);
      this._nodes = (List)nodes.stream().map((x$0) -> {
         return new ConfigureAnimationNodeDialog.Node(x$0);
      }).collect(Collectors.toList());
   }

   public void onChanged() {
   }

   public void onDuplicate() {
   }

   public void onCopy() {
   }

   public void onPaste() {
   }

   public void onReorder() {
   }

   public void onDelete() {
   }

   public void onMultiSelect() {
   }

   public AnimationNode getAverage() {
      return this._average;
   }

   public List<AnimationNode> getNodes() {
      return (List)this._nodes.stream().map((n) -> {
         return n.node;
      }).collect(Collectors.toList());
   }

   public void onAttached() {
      super.onAttached();
      this.setBounds(5 - this.parent.getX(), 15 - this.parent.getY(), 105, 88);
      int slider_width = 72;
      int x_offset = 31;
      int y_offset = 4;
      int y_step = 10;
      int mtmpx = x_offset - 25;
      int mtmpx_step = true;
      final ConfigureAnimationNodeDialog.MapWidgetSceneBlinkyButton sceneMarkerButton = (ConfigureAnimationNodeDialog.MapWidgetSceneBlinkyButton)this.addWidget(new ConfigureAnimationNodeDialog.MapWidgetSceneBlinkyButton());
      sceneMarkerButton.setTooltip("Scene marker").setPosition(mtmpx, y_offset);
      this.sceneMarkerSubmit = (MapWidgetSubmitText)this.addWidget(new MapWidgetSubmitText() {
         public void onAttached() {
            super.onAttached();
            this.setDescription("Enter a scene start marker name\nPut empty space to remove");
         }

         public ChatText getTitle() {
            return ChatText.fromMessage("Enter marker name");
         }

         public void onAccept(String text) {
            ConfigureAnimationNodeDialog.this.updateScene(text);
            sceneMarkerButton.updateIcon();
         }
      });
      mtmpx += 12;
      this.addWidget((new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.updateView();
         }

         public void onClick() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.ACTIVE, this.isCurrentlyActive() ? 0.0D : 1.0D);
            this.updateView();
         }

         private void updateView() {
            boolean active = this.isCurrentlyActive();
            this.setIcon(active ? "attachments/anim_node_active.png" : "attachments/anim_node_inactive.png");
            this.setTooltip(active ? "Active" : "Inactive");
         }

         private boolean isCurrentlyActive() {
            if (ConfigureAnimationNodeDialog.this._nodes.size() == 1) {
               return ((ConfigureAnimationNodeDialog.Node)ConfigureAnimationNodeDialog.this._nodes.get(0)).node.isActive();
            } else {
               int num_active = 0;
               Iterator var2 = ConfigureAnimationNodeDialog.this._nodes.iterator();

               while(var2.hasNext()) {
                  ConfigureAnimationNodeDialog.Node n = (ConfigureAnimationNodeDialog.Node)var2.next();
                  if (n.node.isActive()) {
                     ++num_active;
                  }
               }

               return num_active >= ConfigureAnimationNodeDialog.this._nodes.size() >> 1;
            }
         }
      }).setPosition(mtmpx, y_offset));
      mtmpx += 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onClick() {
            ConfigureAnimationNodeDialog.this.onMultiSelect();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Multi-select").setIcon("attachments/anim_node_multiselect.png").setPosition(mtmpx, y_offset);
      mtmpx += 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onClick() {
            ConfigureAnimationNodeDialog.this.onReorder();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Change order").setIcon("attachments/anim_node_reorder.png").setPosition(mtmpx, y_offset);
      mtmpx += 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onActivate() {
            this.onClick();
         }

         public void onClick() {
            ConfigureAnimationNodeDialog.this.onCopy();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Copy to Clipboard").setIcon("attachments/anim_node_copy.png").setPosition(mtmpx, y_offset);
      mtmpx += 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            boolean hasClipboard = false;
            Iterator var2 = this.display.getOwners().iterator();

            while(var2.hasNext()) {
               Player player = (Player)var2.next();
               if (this.display.isControlling(player)) {
                  hasClipboard |= AnimationNodeClipboard.hasClipboard(player);
               }
            }

            this.setEnabled(hasClipboard);
         }

         public void onActivate() {
            this.onClick();
         }

         public void onClick() {
            ConfigureAnimationNodeDialog.this.onPaste();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Paste from Clipboard").setIcon("attachments/anim_node_paste.png").setPosition(mtmpx, y_offset);
      mtmpx += 12;
      final MapWidget duplicateButton = ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onActivate() {
            this.onClick();
         }

         public void onClick() {
            ConfigureAnimationNodeDialog.this.onDuplicate();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Duplicate").setIcon("attachments/anim_node_duplicate.png").setPosition(mtmpx, y_offset);
      mtmpx += 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onClick() {
            ConfigureAnimationNodeDialog.this.onDelete();
            ConfigureAnimationNodeDialog.this.close();
         }
      })).setTooltip("Delete").setIcon("attachments/anim_node_delete.png").setPosition(mtmpx, y_offset);
      int y_offset = y_offset + 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getDuration());
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.DURATION, this.getValue());
         }

         public String getAcceptedPropertyName() {
            return "Delta Time";
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() == Key.UP) {
               duplicateButton.focus();
            } else {
               super.onKeyPressed(event);
            }

         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Delta T");
      y_offset += y_step;
      MapWidget posXWidget = ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getPosition().getX());
         }

         public String getAcceptedPropertyName() {
            return "Position X-Coordinate";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.POS_X, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Pos.X");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getPosition().getY());
         }

         public String getAcceptedPropertyName() {
            return "Position Y-Coordinate";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.POS_Y, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Pos.Y");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getPosition().getZ());
         }

         public String getAcceptedPropertyName() {
            return "Position Z-Coordinate";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.POS_Z, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Pos.Z");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setIncrement(0.1D);
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getRotationVector().getX());
         }

         public String getAcceptedPropertyName() {
            return "Rotation Pitch";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.ROT_X, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Pitch");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setIncrement(0.1D);
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getRotationVector().getY());
         }

         public String getAcceptedPropertyName() {
            return "Rotation Yaw";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.ROT_Y, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Yaw");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setIncrement(0.1D);
            this.setInitialValue(ConfigureAnimationNodeDialog.this.getAverage().getRotationVector().getZ());
         }

         public String getAcceptedPropertyName() {
            return "Rotation Roll";
         }

         public void onValueChanged() {
            ConfigureAnimationNodeDialog.this.updateNode(ConfigureAnimationNodeDialog.ChangeMode.ROT_Z, this.getValue());
         }
      })).setBounds(x_offset, y_offset, slider_width, 9);
      this.addLabel(5, y_offset + 3, "Roll");
      int var10000 = y_offset + y_step;
      int initialFocusedIndex = (Integer)this.attachment.getEditorOption("animNodeSelectedOption", -1);
      if (initialFocusedIndex >= 0 && initialFocusedIndex < this.getWidgetCount()) {
         this.getWidget(initialFocusedIndex).focus();
      } else {
         posXWidget.focus();
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      super.onKeyPressed(event);
      if (this.display != null) {
         int index = this.getWidgets().indexOf(this.display.getFocusedWidget());
         if (index != -1) {
            this.attachment.setEditorOption("animNodeSelectedOption", -1, index);
         }
      }

   }

   private void updateNode(ConfigureAnimationNodeDialog.ChangeMode mode, double new_value) {
      Iterator var4 = this._nodes.iterator();

      while(var4.hasNext()) {
         ConfigureAnimationNodeDialog.Node n = (ConfigureAnimationNodeDialog.Node)var4.next();
         n.update(mode, new_value);
      }

      this.onChanged();
   }

   private void updateScene(String newSceneName) {
      for(int i = 0; i < this._nodes.size(); ++i) {
         if (i == 0) {
            ((ConfigureAnimationNodeDialog.Node)this._nodes.get(i)).updateScene(newSceneName);
         } else {
            ((ConfigureAnimationNodeDialog.Node)this._nodes.get(i)).updateScene((String)null);
         }
      }

      this.onChanged();
   }

   private static enum ChangeMode {
      POS_X,
      POS_Y,
      POS_Z,
      ROT_X,
      ROT_Y,
      ROT_Z,
      DURATION,
      ACTIVE;

      // $FF: synthetic method
      private static ConfigureAnimationNodeDialog.ChangeMode[] $values() {
         return new ConfigureAnimationNodeDialog.ChangeMode[]{POS_X, POS_Y, POS_Z, ROT_X, ROT_Y, ROT_Z, DURATION, ACTIVE};
      }
   }

   private class MapWidgetSceneBlinkyButton extends MapWidgetBlinkyButton {
      private MapWidgetSceneBlinkyButton() {
      }

      public void onAttached() {
         this.updateIcon();
      }

      public void onClick() {
         ConfigureAnimationNodeDialog.this.sceneMarkerSubmit.activate();
      }

      public void updateIcon() {
         if (!ConfigureAnimationNodeDialog.this._nodes.isEmpty() && ((ConfigureAnimationNodeDialog.Node)ConfigureAnimationNodeDialog.this._nodes.get(0)).node.hasSceneMarker()) {
            this.setIcon("attachments/anim_node_scene_set.png");
         } else {
            this.setIcon("attachments/anim_node_scene.png");
         }

      }

      // $FF: synthetic method
      MapWidgetSceneBlinkyButton(Object x1) {
         this();
      }
   }

   private class Node {
      public final AnimationNode original;
      public AnimationNode node;

      public Node(AnimationNode node) {
         this.original = node.clone();
         this.node = node;
      }

      public void updateScene(String newSceneName) {
         this.node = this.node.setSceneMarker(newSceneName);
      }

      public void update(ConfigureAnimationNodeDialog.ChangeMode mode, double new_value) {
         Vector pos = this.node.getPosition().clone();
         Vector rot = this.node.getRotationVector().clone();
         boolean active = this.node.isActive();
         double duration = this.node.getDuration();
         if (ConfigureAnimationNodeDialog.this._nodes.size() > 1) {
            Vector opos = this.original.getPosition();
            Vector orot = this.original.getRotationVector();
            Vector apos = ConfigureAnimationNodeDialog.this.getAverage().getPosition();
            Vector arot = ConfigureAnimationNodeDialog.this.getAverage().getRotationVector();
            switch(mode) {
            case POS_X:
               pos.setX(opos.getX() + new_value - apos.getX());
               break;
            case POS_Y:
               pos.setY(opos.getY() + new_value - apos.getY());
               break;
            case POS_Z:
               pos.setZ(opos.getZ() + new_value - apos.getZ());
               break;
            case ROT_X:
               rot.setX(orot.getX() + new_value - arot.getX());
               break;
            case ROT_Y:
               rot.setY(orot.getY() + new_value - arot.getY());
               break;
            case ROT_Z:
               rot.setZ(orot.getZ() + new_value - arot.getZ());
               break;
            case DURATION:
               duration = this.original.getDuration() + new_value - ConfigureAnimationNodeDialog.this.getAverage().getDuration();
               break;
            case ACTIVE:
               active = new_value != 0.0D;
            }
         } else {
            switch(mode) {
            case POS_X:
               pos.setX(new_value);
               break;
            case POS_Y:
               pos.setY(new_value);
               break;
            case POS_Z:
               pos.setZ(new_value);
               break;
            case ROT_X:
               rot.setX(new_value);
               break;
            case ROT_Y:
               rot.setY(new_value);
               break;
            case ROT_Z:
               rot.setZ(new_value);
               break;
            case DURATION:
               duration = new_value;
               break;
            case ACTIVE:
               active = new_value != 0.0D;
            }
         }

         this.node = new AnimationNode(pos, rot, active, duration, this.node.getSceneMarker());
      }
   }
}
