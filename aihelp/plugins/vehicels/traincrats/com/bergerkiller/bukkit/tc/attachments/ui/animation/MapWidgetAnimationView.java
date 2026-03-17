package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import com.bergerkiller.bukkit.tc.attachments.ui.AnimationFramesImportExport;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.util.Vector;

public class MapWidgetAnimationView extends MapWidget implements AnimationFramesImportExport {
   private static final int SCROLL_WIDTH = 3;
   private static final int LOOP_TICK_DELAY = 20;
   private MapWidgetAnimationHeader _header;
   private MapWidgetAnimationNode[] _nodes;
   private int _scrollOffset = 0;
   private int _selectedNodeIndex = 0;
   private int _selectedNodeRange = 0;
   private boolean _multiSelectActive = false;
   private boolean _reorderActive = false;
   private boolean _isLoopingAnimation = false;
   private Animation _animation = null;
   private MapTexture _scrollbarBG = MapTexture.createEmpty();
   private final MapWidgetTooltip sceneMarkerTooltip = new MapWidgetTooltip();
   private final MapWidget focusEditTooltip = new MapWidget() {
      public void onDraw() {
         this.view.fill((byte)119);
         this.view.setAlignment(Alignment.MIDDLE);
         this.view.draw(MapFont.MINECRAFT, this.getWidth() / 2, 1, (byte)34, "Enter [space] to edit");
      }
   };

   public MapWidgetAnimationView() {
      this.focusEditTooltip.setDepthOffset(2);
      this.focusEditTooltip.setBounds(1, 28, 106, 10);
   }

   public void onAnimationChanged(Animation animation) {
   }

   public void onSelectionActivated() {
   }

   public void onSelectionChanged() {
   }

   public void onPlayAnimation(boolean reverse, boolean looped) {
   }

   public void onReorder(int offset) {
   }

   public MapWidgetAnimationView setAnimation(Animation animation) {
      if (animation != null && (this._animation == null || !this._animation.isSame(animation))) {
         this._scrollOffset = 0;
      }

      this._animation = animation;
      this.setFocusable(animation != null && animation.getNodeCount() > 0);
      this.updateView();
      this.onAnimationChanged(animation);
      return this;
   }

   public Animation getAnimation() {
      return this._animation;
   }

   public void onAttached() {
      super.onAttached();
      int pos_y = 1;
      this._header = new MapWidgetAnimationHeader();
      this._header.setBounds(1, pos_y, this.getWidth() - 3 - 3, 5);
      this.addWidget(this._header);
      int pos_y = pos_y + 6;
      int num_rows = (this.getHeight() - 1) / 6 - 1;
      this._nodes = new MapWidgetAnimationNode[num_rows];

      for(int i = 0; i < num_rows; ++i) {
         this._nodes[i] = new MapWidgetAnimationNode();
         this._nodes[i].setBounds(1, pos_y, this.getWidth() - 3 - 3, 5);
         this.addWidget(this._nodes[i]);
         pos_y += 6;
      }

      this.updateView();
   }

   public void onDetached() {
      super.onDetached();
      this.clearWidgets();
      this._header = null;
      this._nodes = null;
   }

   public void onFocus() {
      this.focusEditTooltip.removeWidget();
      this.addWidget(this.focusEditTooltip);
   }

   public void onBlur() {
      this.focusEditTooltip.removeWidget();
   }

   public void onDraw() {
      int scroll_height = this.getHeight() - 8;
      int scroll_x = this.getWidth() - 3 - 1;
      int scroll_y = this.getHeight() - scroll_height - 1;
      byte frameColor = this.isFocused() ? 122 : 119;
      this.view.drawRectangle(0, 0, this.getWidth() - 3 - 1, this.getHeight(), (byte)frameColor);

      for(int y = 6; y < this.getHeight() - 1; y += 6) {
         this.view.drawLine(1, y, this.getWidth() - 3 - 3, y, (byte)119);
      }

      this.view.drawRectangle(scroll_x - 1, scroll_y - 1, 5, scroll_height + 2, (byte)frameColor);
      byte scrollbar_color_lft = MapColorPalette.getColor(115, 164, 174);
      byte scrollbar_color_mid = MapColorPalette.getColor(140, 201, 213);
      byte scrollbar_color_rgt = MapColorPalette.getColor(163, 233, 247);
      int scrollbar_pos = 0;
      int scrollbar_height = scroll_height;
      int num_possible_steps;
      int max_position;
      if (this._animation != null && this._animation.getNodeCount() > this._nodes.length) {
         if (this._scrollbarBG.getWidth() != 3 || this._scrollbarBG.getHeight() != scroll_height) {
            this._scrollbarBG = generateNoise(3, scroll_height, MapColorPalette.getColor(25, 93, 131), MapColorPalette.getColor(19, 70, 98), MapColorPalette.getColor(31, 114, 160));
         }

         this.view.draw(this._scrollbarBG, scroll_x, scroll_y);
         scrollbar_height = Math.max(2, this._nodes.length * scroll_height / this._animation.getNodeCount());
         num_possible_steps = this._animation.getNodeCount() - this._nodes.length;
         max_position = scroll_height - scrollbar_height;
         if (this._scrollOffset < 0) {
            scrollbar_pos = 0;
         } else if (this._scrollOffset > num_possible_steps) {
            scrollbar_pos = max_position;
         } else {
            scrollbar_pos = this._scrollOffset * max_position / num_possible_steps;
         }
      }

      num_possible_steps = scroll_y + scrollbar_pos;
      max_position = num_possible_steps + scrollbar_height - 1;
      this.view.drawLine(scroll_x, num_possible_steps, scroll_x, max_position, scrollbar_color_lft);
      this.view.fillRectangle(scroll_x + 1, num_possible_steps, 1, scrollbar_height, scrollbar_color_mid);
      this.view.drawLine(scroll_x + 3 - 1, num_possible_steps, scroll_x + 3 - 1, max_position, scrollbar_color_rgt);
   }

   public void onActivate() {
      super.onActivate();
      this.updateSelectedNodes();
      this.updateSceneMarkerTooltip();
      this.onSelectionChanged();
   }

   public void onDeactivate() {
      super.onDeactivate();
      if (this._multiSelectActive) {
         this.stopMultiSelect();
      }

      if (this._reorderActive) {
         this.stopReordering();
      }

      MapWidgetAnimationNode[] var1 = this._nodes;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MapWidgetAnimationNode node = var1[var3];
         node.setSelected(false);
      }

      this.sceneMarkerTooltip.removeWidget();
      this.focusEditTooltip.removeWidget();
   }

   public void onKeyPressed(MapKeyEvent event) {
      boolean activated = this.isActivated();
      if (activated && this._multiSelectActive && event.getKey() == Key.BACK) {
         this.stopMultiSelect();
      } else if (activated && this._reorderActive && event.getKey() == Key.BACK) {
         this.stopReordering();
      } else {
         super.onKeyPressed(event);
         if (activated) {
            if (event.getKey() != Key.LEFT && event.getKey() != Key.RIGHT) {
               if (this._reorderActive) {
                  if (event.getKey() == Key.UP) {
                     if (this.getSelectionStart() >= 1) {
                        this.onReorder(-1);
                     }
                  } else if (event.getKey() == Key.DOWN) {
                     if (this.getSelectionEnd() < this._animation.getNodeCount() - 1) {
                        this.onReorder(1);
                     }
                  } else if (event.getKey() == Key.ENTER) {
                     this.stopReordering();
                  }
               } else if (this._multiSelectActive) {
                  if (event.getKey() == Key.UP) {
                     this.setSelectedItemRange(this.getSelectedItemRange() - 1);
                  } else if (event.getKey() == Key.DOWN) {
                     this.setSelectedItemRange(this.getSelectedItemRange() + 1);
                  } else if (event.getKey() == Key.ENTER) {
                     this.stopMultiSelect();
                     this.onSelectionActivated();
                  }
               } else if (event.getKey() == Key.UP) {
                  this.setSelectedItemRange(0);
                  this.setSelectedIndex(this.getSelectedIndex() - 1);
               } else if (event.getKey() == Key.DOWN) {
                  this.setSelectedItemRange(0);
                  this.setSelectedIndex(this.getSelectedIndex() + 1);
               } else if (event.getKey() == Key.ENTER) {
                  this.onSelectionActivated();
               }

            } else {
               boolean loopModeActivated = event.getRepeat() == 20;
               if (event.getRepeat() == 1 || loopModeActivated) {
                  if (loopModeActivated) {
                     this.display.playSound(SoundEffect.CLICK);
                     this._isLoopingAnimation = true;
                  } else {
                     this.display.playSound(SoundEffect.EXTINGUISH);
                  }

                  if (event.getKey() == Key.LEFT) {
                     this.onPlayAnimation(true, loopModeActivated);
                  } else if (event.getKey() == Key.RIGHT) {
                     this.onPlayAnimation(false, loopModeActivated);
                  }
               }

            }
         }
      }
   }

   public void onKeyReleased(MapKeyEvent event) {
      super.onKeyReleased(event);
      if (this._isLoopingAnimation) {
         this._isLoopingAnimation = false;
         this.display.playSound(SoundEffect.CLICK_WOOD);
         if (event.getKey() == Key.LEFT) {
            this.onPlayAnimation(true, false);
         } else if (event.getKey() == Key.RIGHT) {
            this.onPlayAnimation(false, false);
         }
      }

   }

   public int getSelectedIndex() {
      return this._selectedNodeIndex;
   }

   public AnimationNode getSelectedNode() {
      if (this._animation == null) {
         return null;
      } else if (this._selectedNodeIndex >= this._animation.getNodeCount()) {
         return null;
      } else {
         return this._selectedNodeIndex < 0 ? null : this._animation.getNode(this._selectedNodeIndex);
      }
   }

   public String getSelectedScene() {
      for(int i = this._selectedNodeIndex; i >= 0; --i) {
         if (i < this._animation.getNodeCount()) {
            AnimationNode node = this._animation.getNode(i);
            if (node.hasSceneMarker()) {
               return node.getSceneMarker();
            }
         }
      }

      return null;
   }

   public List<AnimationNode> getSelectedNodes() {
      if (this._animation == null) {
         return Collections.emptyList();
      } else if (this._selectedNodeIndex >= this._animation.getNodeCount()) {
         return Collections.emptyList();
      } else if (this._selectedNodeIndex < 0) {
         return Collections.emptyList();
      } else if (this._selectedNodeRange == 0) {
         return Collections.singletonList(this._animation.getNode(this._selectedNodeIndex));
      } else {
         List<AnimationNode> result = new ArrayList(Math.abs(this._selectedNodeRange) + 1);
         int startIndex = this.getSelectionStart();
         int endIndex = this.getSelectionEnd();

         for(int i = startIndex; i <= endIndex; ++i) {
            if (i >= 0 && i < this._animation.getNodeCount()) {
               result.add(this._animation.getNode(i));
            }
         }

         return Collections.unmodifiableList(result);
      }
   }

   public List<AnimationNode> getAllNodes() {
      return this._animation == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(Arrays.asList(this._animation.getNodeArray())));
   }

   public void updateAnimationNodes(List<AnimationNode> nodes) {
      this.updateAnimationNodes(nodes, false);
   }

   public void updateAnimationNodes(List<AnimationNode> nodes, boolean replaceAllNodes) {
      Animation old_anim = this.getAnimation();
      if (old_anim != null) {
         int start = this.getSelectionStart();
         int end = this.getSelectionEnd();
         AnimationNode[] old_nodes = old_anim.getNodeArray();
         AnimationNode[] new_nodes;
         if (replaceAllNodes) {
            new_nodes = (AnimationNode[])nodes.toArray(new AnimationNode[0]);
         } else {
            int i;
            if (end - start + 1 == nodes.size()) {
               new_nodes = (AnimationNode[])old_nodes.clone();

               for(int i = 0; i < nodes.size(); ++i) {
                  i = start + i;
                  if (i >= 0 && i <= end && i < new_nodes.length) {
                     new_nodes[i] = (AnimationNode)nodes.get(i);
                  }
               }
            } else {
               List<AnimationNode> new_nodes_list = new ArrayList(old_nodes.length + nodes.size());

               for(i = 0; i < start; ++i) {
                  new_nodes_list.add(old_nodes[i]);
               }

               new_nodes_list.addAll(nodes);

               for(i = end + 1; i < old_nodes.length; ++i) {
                  new_nodes_list.add(old_nodes[i]);
               }

               new_nodes = (AnimationNode[])new_nodes_list.toArray(new AnimationNode[0]);
            }
         }

         Animation replacement = new Animation(old_anim.getOptions().getName(), new_nodes);
         replacement.setOptions(old_anim.getOptions().clone());
         this.setAnimation(replacement);
         this.onSelectionChanged();
      }
   }

   public void duplicateAnimationNodes() {
      this.insertNewAnimationNodes(this.getSelectedNodes());
   }

   public void insertNewAnimationNodes(List<AnimationNode> nodes) {
      if (!nodes.isEmpty()) {
         Animation old_anim = this.getAnimation();
         if (old_anim != null) {
            HashSet<String> usedSceneNames = new HashSet(old_anim.getSceneNames());
            List<AnimationNode> originalNodes = Arrays.asList(old_anim.getNodeArray());
            int newGroupStartIndex = this.getSelectionEnd() + 1;
            ArrayList<AnimationNode> tmp = new ArrayList(originalNodes.size() + nodes.size());
            tmp.addAll(originalNodes.subList(0, newGroupStartIndex));
            Iterator var7 = nodes.iterator();

            while(true) {
               while(var7.hasNext()) {
                  AnimationNode node = (AnimationNode)var7.next();
                  if (node.hasSceneMarker() && !usedSceneNames.add(node.getSceneMarker())) {
                     tmp.add(node.cloneWithoutSceneMarker());
                  } else {
                     tmp.add(node.clone());
                  }
               }

               tmp.addAll(originalNodes.subList(newGroupStartIndex, originalNodes.size()));
               AnimationNode[] new_nodes = (AnimationNode[])LogicUtil.toArray(tmp, AnimationNode.class);
               Animation replacement = new Animation(old_anim.getOptions().getName(), new_nodes);
               replacement.setOptions(old_anim.getOptions().clone());
               this.setAnimation(replacement);
               this.setSelection(newGroupStartIndex, nodes.size() - 1);
               this.stopReordering();
               this.stopMultiSelect();
               if (CommonCapabilities.KEYED_EFFECTS) {
                  this.display.playSound(SoundEffect.fromName("block.note_block.snare"));
               } else {
                  this.display.playSound(SoundEffect.fromName("note.snare"));
               }

               return;
            }
         }
      }
   }

   public MapWidgetAnimationView setSelectedIndex(int index) {
      if (index >= 0 && this._animation != null) {
         if (index >= this._animation.getNodeCount()) {
            index = this._animation.getNodeCount() - 1;
         }
      } else {
         index = 0;
      }

      if (index != this._selectedNodeIndex) {
         this._selectedNodeIndex = index;
         this.updateView();
         this.updateSceneMarkerTooltip();
         this.onSelectionChanged();
         this.display.playSound(SoundEffect.CLICK_WOOD);
      }

      return this;
   }

   public int getSelectedItemRange() {
      return this._selectedNodeRange;
   }

   public MapWidgetAnimationView setSelectedItemRange(int count) {
      int numAfter;
      if (count < 0) {
         numAfter = this._selectedNodeIndex;
         if (-count > numAfter) {
            count = -numAfter;
         }
      } else if (count > 0) {
         numAfter = this._animation.getNodeCount() - this._selectedNodeIndex - 1;
         if (count > numAfter) {
            count = numAfter;
         }
      }

      if (count != this._selectedNodeRange) {
         this.setSelection(this._selectedNodeIndex, count);
         this.display.playSound(SoundEffect.CLICK_WOOD);
      }

      return this;
   }

   public void setSelection(int selectionNodeIndex, int selectedNodeRange) {
      if (this._selectedNodeIndex != selectionNodeIndex || this._selectedNodeRange != selectedNodeRange) {
         this._selectedNodeIndex = selectionNodeIndex;
         this._selectedNodeRange = selectedNodeRange;
         this.updateView();
         this.updateSceneMarkerTooltip();
         this.onSelectionChanged();
      }

   }

   public int getSelectionStart() {
      return this._selectedNodeRange < 0 ? this._selectedNodeIndex + this._selectedNodeRange : this._selectedNodeIndex;
   }

   public int getSelectionEnd() {
      return this._selectedNodeRange > 0 ? this._selectedNodeIndex + this._selectedNodeRange : this._selectedNodeIndex;
   }

   public void startMultiSelect() {
      if (!this._multiSelectActive) {
         this._multiSelectActive = true;
         this.display.playSound(SoundEffect.PISTON_EXTEND);
         this.updateView();
      }

   }

   public void stopMultiSelect() {
      if (this._multiSelectActive) {
         this._multiSelectActive = false;
         this.display.playSound(SoundEffect.PISTON_CONTRACT);
         this.updateView();
      }

   }

   public void startReordering() {
      if (!this._reorderActive) {
         this._reorderActive = true;
         this.display.playSound(SoundEffect.PISTON_EXTEND);
         this.updateView();
      }

   }

   public void stopReordering() {
      if (this._reorderActive) {
         this._reorderActive = false;
         this.display.playSound(SoundEffect.PISTON_CONTRACT);
         this.updateView();
      }

   }

   private void updateView() {
      boolean selectionWasWrong = false;
      int focusedIndex;
      if (this._animation == null) {
         if (this._selectedNodeIndex != 0 || this._selectedNodeRange != 0) {
            this._selectedNodeIndex = 0;
            this._selectedNodeRange = 0;
            selectionWasWrong = true;
         }
      } else {
         if (this._selectedNodeIndex >= this._animation.getNodeCount()) {
            this._selectedNodeIndex = this._animation.getNodeCount() - 1;
            selectionWasWrong = true;
         }

         if (this._selectedNodeRange < 0) {
            focusedIndex = this._selectedNodeIndex;
            if (-this._selectedNodeRange > focusedIndex) {
               this._selectedNodeRange = -focusedIndex;
               selectionWasWrong = true;
            }
         } else if (this._selectedNodeRange > 0) {
            focusedIndex = this._animation.getNodeCount() - this._selectedNodeIndex - 1;
            if (this._selectedNodeRange > focusedIndex) {
               this._selectedNodeRange = focusedIndex;
               selectionWasWrong = true;
            }
         }
      }

      if (selectionWasWrong) {
         this.onSelectionChanged();
      }

      int relIndex;
      if (this._nodes != null) {
         focusedIndex = this._selectedNodeIndex + this._selectedNodeRange;
         relIndex = focusedIndex - this._scrollOffset;
         if (relIndex < 0) {
            this._scrollOffset = focusedIndex;
         } else if (relIndex >= this._nodes.length) {
            this._scrollOffset = focusedIndex - this._nodes.length + 1;
         }
      }

      if (this._scrollOffset < 0) {
         this._scrollOffset = 0;
      } else if (this._animation != null && this._scrollOffset >= this._animation.getNodeCount()) {
         this._scrollOffset = this._animation.getNodeCount() - 1;
      }

      this.invalidate();
      if (this._nodes != null) {
         int anim_idx;
         if (this._animation == null) {
            MapWidgetAnimationNode[] var9 = this._nodes;
            relIndex = var9.length;

            for(anim_idx = 0; anim_idx < relIndex; ++anim_idx) {
               MapWidgetAnimationNode node = var9[anim_idx];
               node.setValue((AnimationNode)null);
               node.setIsMultiSelectRoot(false);
               node.setSelected(false);
            }
         } else {
            double max_position = 0.0D;
            AnimationNode[] var11 = this._animation.getNodeArray();
            int var12 = var11.length;

            int var6;
            for(var6 = 0; var6 < var12; ++var6) {
               AnimationNode node = var11[var6];
               Vector pos = node.getPosition();
               max_position = Math.max(max_position, Math.abs(pos.getX()));
               max_position = Math.max(max_position, Math.abs(pos.getY()));
               max_position = Math.max(max_position, Math.abs(pos.getZ()));
            }

            anim_idx = this._scrollOffset;
            MapWidgetAnimationNode[] var13 = this._nodes;
            var6 = var13.length;

            for(int var14 = 0; var14 < var6; ++var14) {
               MapWidgetAnimationNode node = var13[var14];
               node.setMaximumPosition(max_position);
               node.setValue(anim_idx >= this._animation.getNodeCount() ? null : this._animation.getNode(anim_idx));
               ++anim_idx;
            }

            this.updateSelectedNodes();
         }
      }

   }

   private void updateSelectedNodes() {
      int relIndex;
      if (this.isActivated()) {
         relIndex = this._selectedNodeIndex - this._scrollOffset;
         int relIndexStart = this.getSelectionStart() - this._scrollOffset;
         int relIndexEnd = this.getSelectionEnd() - this._scrollOffset;

         for(int i = 0; i < this._nodes.length; ++i) {
            this._nodes[i].setIsMultiSelectRoot(this._multiSelectActive && i == relIndex);
            this._nodes[i].setSelected(i >= relIndexStart && i <= relIndexEnd);
         }
      } else {
         for(relIndex = 0; relIndex < this._nodes.length; ++relIndex) {
            this._nodes[relIndex].setIsMultiSelectRoot(false);
            this._nodes[relIndex].setSelected(false);
         }
      }

   }

   private void updateSceneMarkerTooltip() {
      this.sceneMarkerTooltip.removeWidget();
      int relIndex = this._selectedNodeIndex - this._scrollOffset;
      if (relIndex >= 0 && relIndex < this._nodes.length) {
         MapWidgetAnimationNode nodeWidget = this._nodes[relIndex];
         AnimationNode node = nodeWidget.getValue();
         if (node != null && node.hasSceneMarker()) {
            this.sceneMarkerTooltip.setText(node.getSceneMarker());
            nodeWidget.addWidget(this.sceneMarkerTooltip);
         }
      }

   }

   private static MapTexture generateNoise(int width, int height, byte... colors) {
      Random rand = new Random(55792434L);
      MapTexture result = MapTexture.createEmpty(width, height);
      byte[] buffer = result.getBuffer();

      for(int i = 0; i < buffer.length; ++i) {
         buffer[i] = colors[rand.nextInt(colors.length)];
      }

      return result;
   }

   public String getAnimationName() {
      Animation anim = this.getAnimation();
      return anim != null ? anim.getOptions().getName() : null;
   }

   public List<AnimationNode> exportAnimationFrames() {
      List<AnimationNode> selected = this.getSelectedNodes();
      return selected.size() > 1 ? selected : this.getAllNodes();
   }

   public void importAnimationFrames(List<AnimationNode> frames, boolean insert) {
      if (insert) {
         this.insertNewAnimationNodes(frames);
      } else if (this.getSelectedNodes().size() > 1) {
         int start = this.getSelectionStart();
         this.updateAnimationNodes(frames, false);
         this.setSelection(start, frames.size() - 1);
      } else {
         this.updateAnimationNodes(frames, true);
      }

      this.stopReordering();
      this.stopMultiSelect();
   }
}
