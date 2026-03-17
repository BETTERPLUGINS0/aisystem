package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentText extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "TEXT";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/text.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentText();
      }

      public void createAppearanceTab(Tab tab, final MapWidgetAttachmentNode attachment) {
         final MapWidgetSubmitText textBox = new MapWidgetSubmitText() {
            public void onAttached() {
               this.setDescription("Enter text");
            }

            public void onAccept(String text) {
               attachment.getConfig().set("text", text);
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", attachment);
               attachment.resetIcon();
            }
         };
         tab.addWidget(textBox);
         ((MapWidgetText)tab.addWidget((new MapWidgetText()).setText("Current Text:"))).setBounds(0, 10, 100, 16);
         ((<undefinedtype>)tab.addWidget(new MapWidgetText() {
            public void onTick() {
               this.setText("\"" + (String)attachment.getConfig().get("text", "") + "\"");
            }
         })).setAlignment(Alignment.MIDDLE).setBounds(0, 30, 100, 16);
         ((<undefinedtype>)tab.addWidget(new MapWidgetButton() {
            public void onAttached() {
               this.setText("Edit Text");
            }

            public void onActivate() {
               textBox.activate();
            }
         })).setBounds(0, 60, 100, 16);
      }
   };
   private VirtualEntity entity;

   public void onAttached() {
      super.onAttached();
      this.entity = new VirtualEntity(this.getManager());
      this.entity.setEntityType(EntityType.ARMOR_STAND);
      this.entity.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
      this.entity.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
      this.entity.getMetaData().set(EntityHandle.DATA_CUSTOM_NAME_VISIBLE, true);
      this.entity.setRelativeOffset(0.0D, -1.6D, 0.0D);
   }

   public void onLoad(ConfigurationNode config) {
      super.onLoad(config);
      String text = (String)this.getConfig().get("text", " ");
      if (text.length() == 0) {
         text = " ";
      }

      this.entity.getMetaData().set(EntityHandle.DATA_CUSTOM_NAME, ChatText.fromMessage(text));
      this.entity.syncMetadata();
   }

   public void onTick() {
   }

   public boolean containsEntityId(int entityId) {
      return this.entity != null && this.entity.getEntityId() == entityId;
   }

   public int getMountEntityId() {
      return this.entity.getEntityId();
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.entity.updatePosition(transform);
   }

   public void onMove(boolean absolute) {
      this.entity.syncPosition(absolute);
   }

   /** @deprecated */
   @Deprecated
   public void makeVisible(Player player) {
      this.makeVisible(this.getManager().asAttachmentViewer(player));
   }

   /** @deprecated */
   @Deprecated
   public void makeHidden(Player player) {
      this.makeHidden(this.getManager().asAttachmentViewer(player));
   }

   public void makeVisible(AttachmentViewer viewer) {
      this.entity.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
   }

   public void makeHidden(AttachmentViewer viewer) {
      this.entity.destroy(viewer);
   }
}
