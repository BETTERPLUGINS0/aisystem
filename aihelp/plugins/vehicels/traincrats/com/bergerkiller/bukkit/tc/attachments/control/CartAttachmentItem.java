package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.attachments.VirtualSpawnableObject;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.config.transform.ItemTransformType;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetItemTransformTypeSelector;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSizeBox;
import com.bergerkiller.bukkit.tc.attachments.ui.item.MapWidgetBrightnessDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.item.MapWidgetItemSelector;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CartAttachmentItem extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "ITEM";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         ItemStack item = (ItemStack)config.get("item", new ItemStack(Material.MINECART));
         return TCConfig.resourcePack.getItemTexture(item, 16, 16);
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentItem();
      }

      public void getDefaultConfig(ConfigurationNode config) {
         config.set("item", new ItemStack(MaterialUtil.getMaterial("LEGACY_WOOD")));
      }

      public void createAppearanceTab(final Tab tab, final MapWidgetAttachmentNode attachment) {
         tab.addWidget(new MapWidgetItemSelector() {
            public void onAttached() {
               super.onAttached();
               this.setSelectedItem((ItemStack)attachment.getConfig().get("item", new ItemStack(Material.PUMPKIN)));
               ItemTransformType type = ItemTransformType.deserialize(attachment.getConfig(), "position.transform");
               this.setShowBrightnessButton(type.category() != ItemTransformType.Category.ARMORSTAND);
            }

            public void onSelectedItemChanged() {
               attachment.getConfig().set("item", this.getSelectedItem());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", attachment);
               attachment.resetIcon();
            }

            public void onBrightnessClicked() {
               ((MapWidgetBrightnessDialog.AttachmentBrightnessDialog)tab.addWidget(new MapWidgetBrightnessDialog.AttachmentBrightnessDialog(attachment))).setPosition(13, 3).activate();
            }
         });
      }

      public void createPositionMenu(PositionMenu.Builder builder) {
         PositionMenu.Row transformRow = builder.addRow(1, (menu) -> {
            return (new MapWidgetItemTransformTypeSelector() {
               public void onAttached() {
                  this.setSelectedType(ItemTransformType.deserialize(menu.getPositionConfig(), "transform"));
                  super.onAttached();
               }

               public void onSelectedTypeChanged(ItemTransformType type) {
                  menu.updatePositionConfig((config) -> {
                     config.set("transform", type.serializedName());
                  });
                  Iterator var2 = this.getParent().getWidgets().iterator();

                  while(var2.hasNext()) {
                     MapWidget widget = (MapWidget)var2.next();
                     if (widget instanceof CartAttachmentItem.ScaleWidget) {
                        widget.setEnabled(type.category() == ItemTransformType.Category.DISPLAY);
                     } else if (widget instanceof CartAttachmentItem.ClipWidget) {
                        widget.setEnabled(type.category() != ItemTransformType.Category.ARMORSTAND);
                     }
                  }

               }
            }).setBounds(25, 0, menu.getSliderWidth(), MapWidgetItemTransformTypeSelector.defaultHeight());
         });
         transformRow.addLabel(0, 3, "Mode");
         if (CommonCapabilities.HAS_DISPLAY_ENTITY) {
            transformRow.addLabel(0, 15, "Tr.form");
         }

         if (CommonCapabilities.HAS_DISPLAY_ENTITY) {
            builder.addRow((menu) -> {
               return (new CartAttachmentItem.ClipWidget(menu)).setBounds(25, 0, menu.getSliderWidth(), 11);
            }).addLabel(0, 3, "Clip").setSpacingAbove(3);
            builder.addRow((menu) -> {
               return (new CartAttachmentItem.ScaleWidget(menu)).setBounds(25, 0, menu.getSliderWidth(), 35);
            }).addLabel(0, 3, "Size X").addLabel(0, 15, "Size Y").addLabel(0, 27, "Size Z");
         }

      }
   };
   private VirtualSpawnableObject entity;

   public void onAttached() {
      super.onAttached();
      ItemTransformType type = ItemTransformType.deserialize(this.getConfig(), "position.transform");
      this.entity = type.create(this.getManager(), (ItemStack)null);
   }

   public void onDetached() {
      super.onDetached();
      this.entity = null;
   }

   public boolean checkCanReload(ConfigurationNode config) {
      if (!super.checkCanReload(config)) {
         return false;
      } else {
         ItemTransformType type = ItemTransformType.deserialize(config, "position.transform");
         return type.canUpdate(this.entity);
      }
   }

   public void onLoad(ConfigurationNode config) {
      super.onLoad(config);
      ItemTransformType.deserialize(config, "position.transform").load(this.entity, config, this.getConfiguredPosition());
   }

   public boolean containsEntityId(int entityId) {
      return this.entity != null && this.entity.containsEntityId(entityId);
   }

   public int getMountEntityId() {
      return -1;
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

   private static class ScaleWidget extends MapWidgetSizeBox {
      private final PositionMenu menu;

      public ScaleWidget(PositionMenu menu) {
         this.menu = menu;
      }

      public void onAttached() {
         super.onAttached();
         this.setInitialSize((Double)this.menu.getPositionConfigValue("sizeX", 1.0D), (Double)this.menu.getPositionConfigValue("sizeY", 1.0D), (Double)this.menu.getPositionConfigValue("sizeZ", 1.0D));
         this.setEnabled(ItemTransformType.deserialize(this.menu.getPositionConfig(), "transform").category() == ItemTransformType.Category.DISPLAY);
      }

      public void onSizeChanged() {
         this.menu.updatePositionConfig((config) -> {
            if (this.x.getValue() == 1.0D && this.y.getValue() == 1.0D && this.z.getValue() == 1.0D) {
               config.remove("sizeX");
               config.remove("sizeY");
               config.remove("sizeZ");
            } else {
               config.set("sizeX", this.x.getValue());
               config.set("sizeY", this.y.getValue());
               config.set("sizeZ", this.z.getValue());
            }

         });
      }
   }

   private static class ClipWidget extends MapWidgetNumberBox {
      private final PositionMenu menu;

      public ClipWidget(PositionMenu menu) {
         this.menu = menu;
      }

      public void onAttached() {
         super.onAttached();
         this.setRange(0.0D, 1000.0D);
         this.setInitialValue((Double)this.menu.getPositionConfigValue("clip", 0.0D));
         if (this.getValue() == 0.0D) {
            this.setTextOverride("<Disabled>");
         }

         this.setEnabled(ItemTransformType.deserialize(this.menu.getPositionConfig(), "transform").category() != ItemTransformType.Category.ARMORSTAND);
      }

      public void onValueChanged() {
         this.setTextOverride(this.getValue() == 0.0D ? "<Disabled>" : null);
         this.menu.updatePositionConfigValue("clip", this.getValue() == 0.0D ? null : this.getValue());
      }
   }
}
