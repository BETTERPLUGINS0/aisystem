package com.bergerkiller.bukkit.tc.attachments.ui.menus;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.ui.AnimationFramesImportExport;
import com.bergerkiller.bukkit.tc.attachments.ui.AttachmentEditor;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetToggleButton;
import com.bergerkiller.bukkit.tc.attachments.ui.animation.AnimationNodeClipboard;
import com.bergerkiller.bukkit.tc.attachments.ui.animation.ConfigureAnimationDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.animation.ConfigureAnimationNodeDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.animation.ConfirmAnimationDeleteDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.animation.MapWidgetAnimationView;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.entity.Player;

public class AnimationMenu extends MapWidgetMenu implements AnimationFramesImportExport {
   private AnimationMenu.PlaybackMode playbackMode;
   private boolean playForAll;
   private final MapWidgetSelectionBox animSelectionBox;
   private final MapWidgetAnimationView animView;
   private final MapWidgetSubmitText animNameBox;
   private final MapWidgetSubmitText animRenameBox;
   private final MapWidgetBlinkyButton animDelete;
   private final MapWidgetBlinkyButton animConfig;
   private final MapWidgetBlinkyButton animPlayRev;
   private final MapWidgetBlinkyButton animPlayFwd;
   private final MapWidgetBlinkyButton animPlayOpt;

   public AnimationMenu() {
      this.playbackMode = AnimationMenu.PlaybackMode.ENTIRE_ANIMATION;
      this.playForAll = false;
      this.animSelectionBox = new MapWidgetSelectionBox() {
         public void onAttached() {
            super.onAttached();
            Iterator var1 = AnimationMenu.this.getAnimRootConfig().getKeys().iterator();

            while(var1.hasNext()) {
               String name = (String)var1.next();
               this.addItem(name);
            }

            if (!this.getItems().isEmpty()) {
               this.setSelectedItem((String)this.getItems().get(0));
            }

            this.onSelectedItemChanged();
         }

         public void onSelectedItemChanged() {
            boolean menuEnabled = !this.getItems().isEmpty();
            AnimationMenu.this.animView.setAnimation(AnimationMenu.this.loadAnimation());
            AnimationMenu.this.animDelete.setEnabled(menuEnabled);
            AnimationMenu.this.animConfig.setEnabled(menuEnabled);
            AnimationMenu.this.animPlayFwd.setEnabled(menuEnabled);
            AnimationMenu.this.animPlayRev.setEnabled(menuEnabled);
         }

         public void onActivate() {
            if (!this.getItems().isEmpty()) {
               AnimationMenu.this.animRenameBox.activate();
            }

         }
      };
      this.animView = new MapWidgetAnimationView() {
         public void onAnimationChanged(Animation animation) {
            if (animation != null) {
               String old_name = AnimationMenu.this.animSelectionBox.getSelectedItem();
               String new_name = animation.getOptions().getName();
               boolean is_name_change = old_name != null && !old_name.equals(new_name);
               if (is_name_change) {
                  AnimationMenu.this.getAnimRootConfig().remove(old_name);
               }

               animation.saveToParentConfig(AnimationMenu.this.getAnimRootConfig());
               if (is_name_change) {
                  AnimationMenu.this.animSelectionBox.addItem(new_name);
                  AnimationMenu.this.animSelectionBox.setSelectedItem(new_name);
                  AnimationMenu.this.animSelectionBox.removeItem(old_name);
               } else if (!AnimationMenu.this.animSelectionBox.getItems().contains(new_name)) {
                  AnimationMenu.this.animSelectionBox.addItem(new_name);
                  AnimationMenu.this.animSelectionBox.setSelectedItem(new_name);
               }

               AnimationMenu.this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }
         }

         public void onSelectionActivated() {
            List<AnimationNode> nodes = this.getSelectedNodes();
            if (!nodes.isEmpty()) {
               ((<undefinedtype>)this.addWidget(new ConfigureAnimationNodeDialog(nodes) {
                  public void onChanged() {
                     updateAnimationNodes(this.getNodes());
                  }

                  public void onMultiSelect() {
                     startMultiSelect();
                  }

                  public void onReorder() {
                     startReordering();
                  }

                  public void onDuplicate() {
                     duplicateAnimationNodes();
                  }

                  public void onCopy() {
                     AnimationMenu.this.copyAnimationNodes();
                  }

                  public void onPaste() {
                     AnimationMenu.this.pasteAnimationNodes();
                  }

                  public void onDelete() {
                     AnimationMenu.this.deleteAnimationNodes();
                  }

                  public void onDeactivate() {
                     super.onDeactivate();
                     this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
                  }
               })).setAttachment(AnimationMenu.this.attachment);
            }

         }

         public void onSelectionChanged() {
            AnimationNode node = this.getSelectedNode();
            if (node != null) {
               AnimationMenu.this.previewAnimationNode(this.getSelectedIndex(), node);
            }

         }

         public void onPlayAnimation(boolean reverse, boolean looped) {
            AnimationMenu.this.playAnimation(reverse, looped);
         }

         public void onReorder(int offset) {
            AnimationMenu.this.moveAnimationNodes(offset);
         }
      };
      this.animNameBox = new MapWidgetSubmitText() {
         public void onAttached() {
            super.onAttached();
            this.setDescription("Enter the animation name");
         }

         public void onAccept(String text) {
            AnimationMenu.this.createAnimation(text);
         }
      };
      this.animRenameBox = new MapWidgetSubmitText() {
         public void onAttached() {
            super.onAttached();
            this.setDescription("Enter the new animation name");
         }

         public void onAccept(String text) {
            AnimationMenu.this.renameAnimation(text);
         }
      };
      this.animDelete = new MapWidgetBlinkyButton() {
         public void onClick() {
            AnimationMenu.this.addWidget(new ConfirmAnimationDeleteDialog() {
               public void onConfirmDelete() {
                  AnimationMenu.this.deleteAnimation();
               }
            });
         }
      };
      this.animConfig = new MapWidgetBlinkyButton() {
         public void onClick() {
            ConfigureAnimationDialog dialog = new ConfigureAnimationDialog(AnimationMenu.this);
            dialog.setAttachment(AnimationMenu.this.attachment);
            AnimationMenu.this.addWidget(dialog);
         }
      };
      this.animPlayRev = new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.setRepeatClickEnabled(true);
         }

         public void onClick() {
            AnimationMenu.this.playAnimation(true, false);
         }

         public void onClickHold() {
            AnimationMenu.this.playAnimation(true, true);
         }

         public void onClickHoldRelease() {
            this.onClick();
         }
      };
      this.animPlayFwd = new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.setRepeatClickEnabled(true);
         }

         public void onClick() {
            AnimationMenu.this.playAnimation(false, false);
         }

         public void onClickHold() {
            AnimationMenu.this.playAnimation(false, true);
         }

         public void onClickHoldRelease() {
            this.onClick();
         }
      };
      this.animPlayOpt = new MapWidgetBlinkyButton() {
         public void onClick() {
            AnimationMenu.this.addWidget(AnimationMenu.this.new MapWidgetPlaybackOptionsMenu());
         }
      };
      this.setBounds(5, 15, 118, 108);
      this.setBackgroundColor((byte)126);
   }

   public void onAttached() {
      super.onAttached();
      this.playbackMode = (AnimationMenu.PlaybackMode)((AttachmentEditor)this.display).getProperties().get("tcAnimPlaybackMode", AnimationMenu.PlaybackMode.class);
      if (this.playbackMode == null) {
         this.playbackMode = AnimationMenu.PlaybackMode.ENTIRE_ANIMATION;
      }

      this.playForAll = (Boolean)((AttachmentEditor)this.display).getProperties().get("tcAnimPlayForAll", false);
      int top_menu_x = 3;
      int top_menu_y = 3;
      this.addWidget(this.animSelectionBox.setBounds(top_menu_x, top_menu_y, this.getWidth() - 6, 11));
      top_menu_x = 8;
      int top_menu_y = top_menu_y + 13;
      this.addWidget((new MapWidgetBlinkyButton() {
         public void onClick() {
            AnimationMenu.this.animNameBox.activate();
         }
      }).setTooltip("New animation").setIcon("attachments/anim_new.png").setPosition(top_menu_x, top_menu_y));
      int top_menu_x = top_menu_x + 17;
      this.addWidget(this.animDelete.setTooltip("Delete animation").setIcon("attachments/anim_delete.png").setPosition(top_menu_x, top_menu_y));
      top_menu_x += 17;
      this.addWidget(this.animConfig.setTooltip("Configure").setIcon("attachments/anim_config.png").setPosition(top_menu_x, top_menu_y));
      top_menu_x += 17;
      top_menu_x += 5;
      this.addWidget(this.animPlayRev.setTooltip("Play in reverse").setIcon("attachments/anim_play_rev.png").setPosition(top_menu_x, top_menu_y));
      top_menu_x += 17;
      this.addWidget(this.animPlayFwd.setTooltip("Play forwards").setIcon("attachments/anim_play_fwd.png").setPosition(top_menu_x, top_menu_y));
      top_menu_x += 17;
      this.addWidget(this.animPlayOpt.setTooltip("Playback options").setIcon("attachments/anim_play_opt.png").setPosition(top_menu_x, top_menu_y));
      top_menu_x = 3;
      top_menu_y += 18;
      this.addWidget(this.animNameBox);
      this.addWidget(this.animRenameBox);
      this.addWidget(this.animView.setBounds(top_menu_x, top_menu_y, this.getWidth() - 2 * top_menu_x, 67));
   }

   public void playAnimation(boolean reverse, boolean looped) {
      this.playAnimation((opt) -> {
         opt.setSpeed(reverse ? -1.0D : 1.0D);
         opt.setLooped(looped);
         opt.setReset(!looped);
      });
   }

   public void playAnimation(Consumer<AnimationOptions> optionFunc) {
      AnimationMenu.PlaybackMode var10000;
      Iterator var2;
      MapWidgetAnimationView var10002;
      AnimationOptions options;
      if (this.playForAll) {
         var2 = this.attachment.getMembersUsingAttachment().iterator();

         while(var2.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var2.next();
            options = new AnimationOptions(this.animSelectionBox.getSelectedItem());
            optionFunc.accept(options);
            var10000 = this.playbackMode;
            var10002 = this.animView;
            Objects.requireNonNull(var10002);
            var10000.applyOptions(options, var10002::getSelectedScene);
            member.playNamedAnimation(options);
         }
      } else {
         var2 = this.attachment.getAttachments().iterator();

         while(var2.hasNext()) {
            Attachment liveAttachment = (Attachment)var2.next();
            options = new AnimationOptions(this.animSelectionBox.getSelectedItem());
            optionFunc.accept(options);
            var10000 = this.playbackMode;
            var10002 = this.animView;
            Objects.requireNonNull(var10002);
            var10000.applyOptions(options, var10002::getSelectedScene);
            liveAttachment.playNamedAnimation(options);
         }
      }

   }

   public void deleteAnimation() {
      String item = this.animSelectionBox.getSelectedItem();
      this.animSelectionBox.removeItem(item);
      this.getAnimRootConfig().remove(item);
      this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
   }

   public void renameAnimation(String newName) {
      if (!this.animSelectionBox.getItems().contains(newName)) {
         Animation anim = this.getAnimation().clone();
         anim.getOptions().setName(newName);
         this.setAnimation(anim);
      }
   }

   public void createAnimation(String name) {
      if (this.animSelectionBox.getItems().contains(name)) {
         this.animSelectionBox.setSelectedItem(name);
      } else {
         Animation newAnimation = new Animation(name, new String[]{"t=0.25 x=0.0 y=0.0 z=0.0 yaw=0.0 pitch=0.0 roll=0.0", "t=0.25 x=0.0 y=0.0 z=0.0 yaw=90.0 pitch=0.0 roll=0.0", "t=0.25 x=0.0 y=0.0 z=0.0 yaw=180.0 pitch=0.0 roll=0.0", "t=0.25 x=0.0 y=0.0 z=0.0 yaw=270.0 pitch=0.0 roll=0.0"});
         newAnimation.saveToParentConfig(this.getAnimRootConfig());
         this.animSelectionBox.addItem(name);
         this.animSelectionBox.setSelectedItem(name);
         this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
      }
   }

   public void copyAnimationNodes() {
      Iterator var1 = this.display.getOwners().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         if (this.display.isControlling(player)) {
            AnimationNodeClipboard.of(player).store(this.animView.getSelectedNodes());
            if (CommonCapabilities.KEYED_EFFECTS) {
               this.display.playSound(SoundEffect.fromName("block.note_block.hat"));
            } else {
               this.display.playSound(SoundEffect.fromName("note.hat"));
            }
         }
      }

   }

   public void pasteAnimationNodes() {
      Iterator var1 = this.display.getOwners().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         if (this.display.isControlling(player)) {
            List<AnimationNode> contents = AnimationNodeClipboard.of(player).contents();
            if (!contents.isEmpty()) {
               this.insertNewAnimationNodes(contents);
               break;
            }
         }
      }

   }

   public void duplicateAnimationNodes() {
      this.animView.duplicateAnimationNodes();
   }

   public void insertNewAnimationNodes(List<AnimationNode> nodes) {
      this.animView.insertNewAnimationNodes(nodes);
   }

   public void deleteAnimationNodes() {
      Animation old_anim = this.animView.getAnimation();
      if (old_anim != null && old_anim.getNodeCount() > 1) {
         int start = this.animView.getSelectionStart();
         int end = this.animView.getSelectionEnd();
         int count = end - start + 1;
         ArrayList<AnimationNode> tmp = new ArrayList(Arrays.asList(old_anim.getNodeArray()));

         for(int n = 0; n < count && !tmp.isEmpty(); ++n) {
            tmp.remove(start);
         }

         AnimationNode[] new_nodes = (AnimationNode[])LogicUtil.toArray(tmp, AnimationNode.class);
         Animation replacement = new Animation(old_anim.getOptions().getName(), new_nodes);
         replacement.setOptions(old_anim.getOptions().clone());
         this.setAnimation(replacement);
         this.animView.setSelectedItemRange(0);
      }
   }

   public void updateAnimationNodes(List<AnimationNode> nodes) {
      this.animView.updateAnimationNodes(nodes);
   }

   public void updateAnimationNodes(List<AnimationNode> nodes, boolean replaceAllNodes) {
      this.animView.updateAnimationNodes(nodes, replaceAllNodes);
   }

   public void moveAnimationNodes(int offset) {
      Animation old_anim = this.animView.getAnimation();
      if (old_anim != null) {
         int start = this.animView.getSelectionStart();
         int end = this.animView.getSelectionEnd();
         int count = end - start + 1;
         AnimationNode[] old_nodes = old_anim.getNodeArray();
         ArrayList<AnimationNode> tmp = new ArrayList(Arrays.asList(old_nodes));

         int n;
         for(n = 0; n < count; ++n) {
            tmp.remove(start);
         }

         for(n = 0; n < count; ++n) {
            tmp.add(start + offset + n, old_nodes[start + n]);
         }

         AnimationNode[] new_nodes = (AnimationNode[])LogicUtil.toArray(tmp, AnimationNode.class);
         Animation replacement = new Animation(old_anim.getOptions().getName(), new_nodes);
         replacement.setOptions(old_anim.getOptions().clone());
         this.setAnimation(replacement);
         this.animView.setSelectedIndex(this.animView.getSelectedIndex() + offset);
      }
   }

   public void previewAnimationNode(int index, AnimationNode node) {
      Iterator var3 = this.attachment.getAttachments().iterator();

      while(var3.hasNext()) {
         Attachment liveAttachment = (Attachment)var3.next();
         AnimationNode[] nodes;
         if (node.isActive()) {
            nodes = new AnimationNode[]{node};
         } else {
            nodes = new AnimationNode[]{new AnimationNode(node.getPosition(), node.getRotationVector(), true, 0.5D), new AnimationNode(node.getPosition(), node.getRotationVector(), false, 0.5D)};
         }

         Animation anim_preview = new Animation("DUMMY_DO_NOT_USE", nodes);
         anim_preview.getOptions().setReset(true);
         anim_preview.getOptions().setLooped(true);
         liveAttachment.startAnimation(anim_preview);
      }

   }

   public Animation getAnimation() {
      return this.animView.getAnimation();
   }

   public Animation loadAnimation() {
      String item = this.animSelectionBox.getSelectedItem();
      return item == null ? null : Animation.loadFromConfig(this.getAnimRootConfig().getNode(item));
   }

   public void setAnimation(Animation animation) {
      this.animView.setAnimation(animation);
   }

   public ConfigurationNode getAnimRootConfig() {
      return this.attachment.getConfig().getNode("animations");
   }

   public MapWidgetAttachmentNode getAttachment() {
      return this.attachment;
   }

   public String getAnimationName() {
      return this.animSelectionBox.getSelectedItem();
   }

   public List<AnimationNode> exportAnimationFrames() {
      return this.animView.exportAnimationFrames();
   }

   public void importAnimationFrames(List<AnimationNode> frames, boolean insert) {
      this.animView.importAnimationFrames(frames, insert);
   }

   private static enum PlaybackMode {
      ENTIRE_ANIMATION("Entire animation") {
         public void applyOptions(AnimationOptions options, Supplier<String> sceneGetter) {
            options.resetScene();
         }
      },
      SCENE("Current scene") {
         public void applyOptions(AnimationOptions options, Supplier<String> sceneGetter) {
            String scene = (String)sceneGetter.get();
            options.setScene(scene, scene);
         }
      },
      BEGIN_TO_SCENE("Begin-to-scene") {
         public void applyOptions(AnimationOptions options, Supplier<String> sceneGetter) {
            options.setScene((String)null, (String)sceneGetter.get());
         }
      },
      SCENE_TO_END("Scene-to-end") {
         public void applyOptions(AnimationOptions options, Supplier<String> sceneGetter) {
            options.setScene((String)sceneGetter.get(), (String)null);
         }
      };

      private final String _desc;

      private PlaybackMode(String description) {
         this._desc = description;
      }

      public String description() {
         return this._desc;
      }

      public abstract void applyOptions(AnimationOptions var1, Supplier<String> var2);

      // $FF: synthetic method
      private static AnimationMenu.PlaybackMode[] $values() {
         return new AnimationMenu.PlaybackMode[]{ENTIRE_ANIMATION, SCENE, BEGIN_TO_SCENE, SCENE_TO_END};
      }

      // $FF: synthetic method
      PlaybackMode(String x2, Object x3) {
         this(x2);
      }
   }

   private class MapWidgetPlaybackOptionsMenu extends MapWidgetMenu {
      public MapWidgetPlaybackOptionsMenu() {
         this.setBounds(8, 31, 100, 64);
         this.setBackgroundColor((byte)62);
      }

      public void onAttached() {
         ((MapWidgetText)this.addWidget(new MapWidgetText())).setFont(MapFont.MINECRAFT).setText("Playback mode:").setColor((byte)119).setPosition(6, 5);
         ((<undefinedtype>)this.addWidget(new MapWidgetToggleButton<AnimationMenu.PlaybackMode>() {
            public void onSelectionChanged() {
               AnimationMenu.this.playbackMode = (AnimationMenu.PlaybackMode)this.getSelectedOption();
               ((AttachmentEditor)this.display).getProperties().set("tcAnimPlaybackMode", AnimationMenu.this.playbackMode);
               this.display.playSound(SoundEffect.CLICK);
            }
         })).addOptions(AnimationMenu.PlaybackMode::description, AnimationMenu.PlaybackMode.class).setSelectedOption(AnimationMenu.this.playbackMode).setBounds(5, 15, 90, 13);
         ((MapWidgetText)this.addWidget(new MapWidgetText())).setFont(MapFont.MINECRAFT).setText("Play for:").setColor((byte)119).setPosition(6, 36);
         ((<undefinedtype>)this.addWidget(new MapWidgetToggleButton<Boolean>() {
            public void onSelectionChanged() {
               AnimationMenu.this.playForAll = (Boolean)this.getSelectedOption();
               ((AttachmentEditor)this.display).getProperties().set("tcAnimPlayForAll", AnimationMenu.this.playForAll);
               this.display.playSound(SoundEffect.CLICK);
            }
         })).addOptions((opt) -> {
            return opt ? "All attachments" : "This attachment";
         }, new Boolean[]{true, false}).setSelectedOption(AnimationMenu.this.playForAll).setBounds(5, 46, 90, 13);
         super.onAttached();
      }
   }
}
