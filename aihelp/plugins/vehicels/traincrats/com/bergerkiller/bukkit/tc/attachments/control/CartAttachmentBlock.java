package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayBlockEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.block.BlockDataTextureCache;
import com.bergerkiller.bukkit.tc.attachments.ui.block.MapWidgetBlockDataSelector;
import com.bergerkiller.bukkit.tc.attachments.ui.item.MapWidgetBrightnessDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentBlock extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "BLOCK_DISPLAY";
      }

      public String getName() {
         return "BLOCK";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         BlockData blockData = CartAttachmentBlock.deserializeBlockData(config);
         return blockData != null ? BlockDataTextureCache.get(16, 16).get(blockData) : MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/unknown_block.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentBlock();
      }

      public void getDefaultConfig(ConfigurationNode config) {
         config.set("blockData", BlockData.fromMaterial(MaterialUtil.getFirst(new String[]{"COBBLESTONE", "LEGACY_COBBLESTONE"})).serializeToString());
      }

      public void createAppearanceTab(final Tab tab, final MapWidgetAttachmentNode attachment) {
         MapWidgetBlockDataSelector selector = new MapWidgetBlockDataSelector() {
            public void onAttached() {
               this.setSelectedBlockData(CartAttachmentBlock.deserializeBlockData(attachment.getConfig()));
            }

            public void onSelectedBlockDataChanged(BlockData blockData) {
               attachment.getConfig().set("blockData", blockData == null ? null : blockData.serializeToString());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", attachment);
               attachment.resetIcon();
            }

            public void onBrightnessClicked() {
               ((MapWidgetBrightnessDialog.AttachmentBrightnessDialog)tab.addWidget(new MapWidgetBrightnessDialog.AttachmentBrightnessDialog(attachment))).setPosition(13, 3).activate();
            }
         };
         selector.showBrightnessButton();
         tab.addWidget(selector);
      }

      public void createPositionMenu(PositionMenu.Builder builder) {
         builder.addSizeBox();
      }
   };
   private VirtualDisplayBlockEntity entity;

   private static BlockData deserializeBlockData(ConfigurationNode config) {
      String blockDataStr = (String)config.get("blockData", String.class);
      return blockDataStr != null ? BlockData.fromString(blockDataStr) : null;
   }

   public void onAttached() {
      this.entity = new VirtualDisplayBlockEntity(this.getManager());
   }

   public void onLoad(ConfigurationNode config) {
      this.entity.setBlockData(deserializeBlockData(config));
      this.entity.setScale(this.getConfiguredPosition().size);
      this.entity.setBrightness(VirtualDisplayEntity.loadBrightnessFromConfig(config));
   }

   public void onDetached() {
      this.entity = null;
   }

   public boolean containsEntityId(int entityId) {
      return this.entity != null && this.entity.containsEntityId(entityId);
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

   public void onFocus() {
      this.entity.setGlowColor(HelperMethods.getFocusGlowColor(this));
   }

   public void onBlur() {
      this.entity.setGlowColor((ChatColor)null);
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.entity.updatePosition(transform);
   }

   public void onTick() {
   }

   public void onMove(boolean absolute) {
      this.entity.syncPosition(absolute);
   }
}
