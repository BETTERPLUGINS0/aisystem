package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.events.map.MapStatusEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapDisplayProperties;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetWindow;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil.ItemSynchronizer;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttachmentEditor extends MapDisplay {
   private static final int SNEAK_DEBOUNCE_TICKS = 5;
   public CartProperties editedCart;
   public AttachmentModel model;
   private boolean _hasPermission;
   private int blinkCounter = 0;
   private int sneakCounter = 0;
   private List<Attachment> _lastSelectedAttachments = new ArrayList();
   private MapWidgetWindow window = new MapWidgetWindow();
   private MapWidgetAttachmentTree tree = new MapWidgetAttachmentTree() {
      public void onKeyPressed(MapKeyEvent event) {
         if (!AttachmentEditor.this.updateSneakWalking(event)) {
            super.onKeyPressed(event);
         }

      }

      public void onMenuOpen(MapWidgetAttachmentNode node, MapWidgetAttachmentNode.MenuItem menu) {
         if (node.checkModifyPermissions()) {
            AttachmentEditor.this.addWidget(menu.createMenu(node));
         }

      }
   };

   public MapDisplayProperties getProperties() {
      return this.properties;
   }

   public void onTick() {
      Player player = (Player)this.getViewers().get(0);
      if (this.sneakCounter > 0) {
         if (player.isSneaking()) {
            this.sneakCounter = 5;
         } else if (--this.sneakCounter == 0) {
            this.setReceiveInputWhenHolding(true);
         }
      }

      if (this._hasPermission != Permission.COMMAND_GIVE_EDITOR.has((CommandSender)this.getOwners().get(0))) {
         this.setRunning(false);
         this.setRunning(true);
      } else if (this.editedCart != null && this.editedCart.isRemoved()) {
         this.setRunning(false);
         this.setRunning(true);
      } else {
         this.syncSelectedLiveAttachments();
         if (!this._lastSelectedAttachments.isEmpty()) {
            ++this.blinkCounter;
            AttachmentEditor.FocusMode nextMode = AttachmentEditor.FocusMode.fromPhase(this.blinkCounter);
            if (nextMode != null) {
               this.updateFocus(nextMode);
               if (nextMode == AttachmentEditor.FocusMode.NONE) {
                  this.blinkCounter = 0;
               }
            }
         }

      }
   }

   public boolean updateSneakWalking(MapKeyEvent event) {
      if (event.getKey() == Key.BACK) {
         MapWidget activated = this.getActivatedWidget();
         if (activated instanceof MapWidgetAttachmentNode && ((MapWidgetAttachmentNode)activated).isChangingOrder()) {
            return false;
         }

         if (activated == this.getRootWidget() || activated == this.tree || activated instanceof MapWidgetAttachmentNode) {
            if (TCConfig.enableSneakingInAttachmentEditor) {
               this.sneakCounter = 5;
               this.setReceiveInputWhenHolding(false);
            }

            return true;
         }
      }

      return false;
   }

   public void onKeyPressed(MapKeyEvent event) {
      this.updateSneakWalking(event);
   }

   public void onStatusChanged(MapStatusEvent event) {
      if (this.tree.getDisplay() != null) {
         if (!event.isName("changed") && !event.isName("sync")) {
            if (event.isName("reset")) {
               this.tree.updateView();
               this.tree.sync();
               this.pauseBlinking(AttachmentEditor.FocusMode.NONE, 30);
            }
         } else {
            this.tree.sync();
            this.pauseBlinking(AttachmentEditor.FocusMode.SELECTED, 5);
         }

      }
   }

   private void syncSelectedLiveAttachments() {
      LogicUtil.synchronizeList(this._lastSelectedAttachments, this.tree.getSelectedNode().getAttachments(), new ItemSynchronizer<Attachment, Attachment>() {
         public boolean isItem(Attachment o, Attachment o2) {
            return o == o2;
         }

         public Attachment onAdded(Attachment added) {
            AttachmentEditor.FocusMode.fromCounter(AttachmentEditor.this.blinkCounter).applyTo(added);
            return added;
         }

         public void onRemoved(Attachment removed) {
            removed.setFocused(false);
            AttachmentEditor.setChildrenFocused(removed, false);
         }
      });
   }

   public void onSelectedNodeChanged() {
      if (this.getFocusedWidget() instanceof MapWidgetAttachmentNode) {
         this.pauseBlinking(AttachmentEditor.FocusMode.SELECTED, 2);
      } else {
         this.pauseBlinking(AttachmentEditor.FocusMode.NONE, 30);
      }

   }

   public void onAttached() {
      this.setGlobal(false);
      this.setUpdateWithoutViewers(false);
      this.setSessionMode(MapSessionMode.HOLDING);
      this.setMasterVolume(0.3F);
      this.reload();
   }

   public void onDetached() {
      this.getRootWidget().deactivate();
      this.updateFocus(AttachmentEditor.FocusMode.NONE);
      this._lastSelectedAttachments.clear();
   }

   public CartProperties getEditedCartProperties() {
      return this.editedCart;
   }

   public MinecartMember<?> getEditedCart() {
      return this.editedCart == null ? null : this.editedCart.getHolder();
   }

   public boolean isEditingSavedModel() {
      return this.model instanceof SavedAttachmentModel;
   }

   public static void reloadAttachmentEditorFor(UUID playerUUID) {
      Player player = Bukkit.getPlayer(playerUUID);
      if (player != null) {
         AttachmentEditor editor = (AttachmentEditor)MapDisplay.getHeldDisplay(player, AttachmentEditor.class);
         if (editor != null) {
            editor.reload();
         }
      }

   }

   public void reload() {
      this.clearWidgets();
      this.window = new MapWidgetWindow();
      this.window.setBounds(0, 0, this.getWidth(), this.getHeight());
      this.window.getTitle().setText("Attachment Editor");
      this.addWidget(this.window);
      this._hasPermission = Permission.COMMAND_GIVE_EDITOR.has((CommandSender)this.getOwners().get(0));
      if (!this._hasPermission) {
         this.setReceiveInputWhenHolding(false);
         this.editedCart = null;
         this.model = AttachmentModel.getDefaultModel(EntityType.MINECART);
         ((MapWidgetText)this.window.addWidget(new MapWidgetText())).setText("You do not have\npermission!").setColor((byte)18).setShadowColor(MapColorPalette.getSpecular((byte)18, 0.5F)).setPosition(20, 60);
      } else {
         TrainCarts traincarts = TrainCarts.plugin;
         Player owner = (Player)this.getOwners().get(0);
         TrainCartsPlayer tcOwner = traincarts.getPlayer(owner);
         SavedAttachmentModel editedModel = tcOwner.getEditedModelInit();
         if (editedModel != null) {
            this.editedCart = null;
            this.sneakCounter = owner.isSneaking() ? 5 : 0;
            this.setReceiveInputWhenHolding(this.sneakCounter == 0);
            this.model = editedModel;
            this.tree.setModel(this.model);
            this.tree.setBounds(5, 13, 119, 102);
            this.window.getTitle().setText("Attachment Model Editor");
            this.window.setBackgroundColor(MapColorPalette.getColor(54, 168, 176));
            this.window.addWidget(this.tree);
         } else if ((this.editedCart = tcOwner.getEditedCart()) != null) {
            this.sneakCounter = owner.isSneaking() ? 5 : 0;
            this.setReceiveInputWhenHolding(this.sneakCounter == 0);
            this.model = this.editedCart.getModel();
            this.tree.setModel(this.model);
            this.tree.setBounds(5, 13, 119, 102);
            this.window.addWidget(this.tree);
         } else {
            this.setReceiveInputWhenHolding(false);
            this.model = AttachmentModel.getDefaultModel(EntityType.MINECART);
            ((MapWidgetText)this.window.addWidget(new MapWidgetText())).setText("Please select the\nMinecart to edit!").setColor((byte)18).setShadowColor(MapColorPalette.getSpecular((byte)18, 0.5F)).setPosition(20, 60);
         }
      }

   }

   private static void setChildrenFocused(Attachment attachment, boolean focused) {
      if (attachment != null) {
         Iterator var2 = attachment.getChildren().iterator();

         while(var2.hasNext()) {
            Attachment child = (Attachment)var2.next();
            child.setFocused(focused);
            setChildrenFocused(child, focused);
         }

      }
   }

   public boolean onItemDrop(Player player, ItemStack item) {
      if (item == null) {
         return false;
      } else {
         MapWidget activated = this.getActivatedWidget();
         return activated instanceof ItemDropTarget ? ((ItemDropTarget)activated).acceptItem(item) : false;
      }
   }

   private void pauseBlinking(AttachmentEditor.FocusMode mode, int time) {
      this.updateFocus(mode);
      this.blinkCounter = -time;
   }

   private void updateFocus(AttachmentEditor.FocusMode mode) {
      List var10000 = this._lastSelectedAttachments;
      Objects.requireNonNull(mode);
      var10000.forEach(mode::applyTo);
   }

   private static enum FocusMode {
      SELECTED(10),
      SELECTED_AND_CHILDREN(12),
      NONE(20);

      public final int phase;

      private FocusMode(int phase) {
         this.phase = phase;
      }

      public void applyTo(Attachment attachment) {
         switch(this) {
         case NONE:
            HelperMethods.setFocusedRecursive(attachment, false);
            break;
         case SELECTED:
            attachment.setFocused(true);
            break;
         case SELECTED_AND_CHILDREN:
            HelperMethods.setFocusedRecursive(attachment, true);
         }

      }

      public static AttachmentEditor.FocusMode fromPhase(int phase) {
         AttachmentEditor.FocusMode[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            AttachmentEditor.FocusMode mode = var1[var3];
            if (mode.phase == phase) {
               return mode;
            }
         }

         return null;
      }

      public static AttachmentEditor.FocusMode fromCounter(int counter) {
         AttachmentEditor.FocusMode result = SELECTED;
         AttachmentEditor.FocusMode[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            AttachmentEditor.FocusMode mode = var2[var4];
            if (mode.phase > counter) {
               break;
            }

            result = mode;
         }

         return result;
      }

      // $FF: synthetic method
      private static AttachmentEditor.FocusMode[] $values() {
         return new AttachmentEditor.FocusMode[]{SELECTED, SELECTED_AND_CHILDREN, NONE};
      }
   }
}
