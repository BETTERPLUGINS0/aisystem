package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.monster.EntityShulkerHandle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentPlatform extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "PLATFORM";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/platform.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentPlatform();
      }

      public void createAppearanceTab(Tab tab, final MapWidgetAttachmentNode attachment) {
         ((MapWidgetText)tab.addWidget(new MapWidgetText())).setText("Shulker Color").setFont(MapFont.MINECRAFT).setColor((byte)18).setBounds(15, 6, 50, 11);
         MapWidget boatTypeSelector = ((<undefinedtype>)tab.addWidget(new MapWidgetSelectionBox() {
            public void onAttached() {
               super.onAttached();
               this.addItem(CartAttachmentPlatform.Color.DEFAULT.name());
               CartAttachmentPlatform.Color[] var1 = CartAttachmentPlatform.Color.values();
               int var2 = var1.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  CartAttachmentPlatform.Color color = var1[var3];
                  if (color != CartAttachmentPlatform.Color.DEFAULT) {
                     this.addItem(color.name());
                  }
               }

               this.setSelectedItem(((CartAttachmentPlatform.Color)attachment.getConfig().getOrDefault("shulkerColor", CartAttachmentPlatform.Color.DEFAULT)).name());
            }

            public void onSelectedItemChanged() {
               attachment.getConfig().set("shulkerColor", this.getSelectedItem());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }
         })).setBounds(0, 15, 100, 12);
      }
   };
   private VirtualEntity actual;
   private VirtualEntity entity;

   public void onDetached() {
      super.onDetached();
      this.entity = null;
      this.actual = null;
   }

   public void onAttached() {
      super.onAttached();
      this.actual = new VirtualEntity(this.getManager());
      this.actual.setEntityType(EntityType.SHULKER);
      this.actual.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
      this.actual.getMetaData().setClientByteDefault(EntityShulkerHandle.DATA_COLOR, CartAttachmentPlatform.Color.DEFAULT.ordinal());
      this.entity = new VirtualEntity(this.getManager());
      this.entity.setEntityType(EntityType.CHICKEN);
      this.entity.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
      this.entity.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
      this.entity.setRelativeOffset(0.0D, -0.32D, 0.0D);
   }

   public void onLoad(ConfigurationNode config) {
      CartAttachmentPlatform.Color color = (CartAttachmentPlatform.Color)config.getOrDefault("shulkerColor", CartAttachmentPlatform.Color.DEFAULT);
      this.actual.getMetaData().set(EntityShulkerHandle.DATA_COLOR, (byte)color.ordinal());
   }

   public boolean containsEntityId(int entityId) {
      return this.entity.getEntityId() == entityId || this.actual.getEntityId() == entityId;
   }

   public int getMountEntityId() {
      return this.actual.getEntityId();
   }

   public void applyPassengerSeatTransform(Matrix4x4 transform) {
      Matrix4x4 relativeMatrix = new Matrix4x4();
      relativeMatrix.translate(0.0D, 1.0D, 0.0D);
      Matrix4x4.multiply(relativeMatrix, transform, transform);
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
      this.actual.spawn(viewer, new Vector());
      this.entity.spawn(viewer, new Vector());
      viewer.getVehicleMountController().mount(this.entity.getEntityId(), this.actual.getEntityId());
   }

   public void makeHidden(AttachmentViewer viewer) {
      this.actual.destroy(viewer);
      this.entity.destroy(viewer);
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.entity.updatePosition(transform);
      this.actual.updatePosition(transform);
      this.actual.syncMetadata();
   }

   public void onMove(boolean absolute) {
      this.entity.syncPosition(absolute);
      this.actual.syncPositionSilent();
   }

   public void onTick() {
   }

   public static enum Color {
      WHITE,
      ORANGE,
      MAGENTA,
      LIGHT_BLUE,
      YELLOW,
      LIME,
      PINK,
      GRAY,
      LIGHT_GRAY,
      CYAN,
      PURPLE,
      BLUE,
      BROWN,
      GREEN,
      RED,
      BLACK,
      DEFAULT;

      // $FF: synthetic method
      private static CartAttachmentPlatform.Color[] $values() {
         return new CartAttachmentPlatform.Color[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK, DEFAULT};
      }
   }
}
