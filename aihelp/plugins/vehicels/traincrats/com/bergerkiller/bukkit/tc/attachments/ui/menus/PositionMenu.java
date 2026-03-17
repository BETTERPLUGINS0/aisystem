package com.bergerkiller.bukkit.tc.attachments.ui.menus;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentAnchor;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetScroller;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSizeBox;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PositionMenu extends MapWidgetMenu {
   private final MapWidgetScroller scroller = new MapWidgetScroller();

   public PositionMenu() {
      this.setBounds(5, 15, 118, 108);
      this.setPositionAbsolute(true);
      this.setBackgroundColor((byte)30);
      this.scroller.setBounds(5, 5, this.getWidth() - 7, this.getHeight() - 10);
      this.scroller.setScrollPadding(20);
      this.addWidget(this.scroller);
   }

   public int getSliderWidth() {
      return 86;
   }

   public void onAttached() {
      super.onAttached();
      PositionMenu.Builder builder = new PositionMenu.Builder();
      builder.addRow((menu) -> {
         return (new MapWidgetSelectionBox() {
            public void onAttached() {
               super.onAttached();
               AttachmentAnchor current = this.getCurrentAnchor();
               AttachmentType attachmentType = menu.getMenuAttachmentType();
               boolean foundCurrent = false;
               Iterator var4 = AttachmentAnchor.values().iterator();

               while(var4.hasNext()) {
                  AttachmentAnchor type = (AttachmentAnchor)var4.next();
                  if (type.supports(AttachmentControllerMember.class, attachmentType)) {
                     this.addItem(type.getName());
                     if (type.equals(current)) {
                        foundCurrent = true;
                     }
                  }
               }

               if (!foundCurrent) {
                  this.addItem(current.getName());
               }

               this.setSelectedItem(current.getName());
            }

            public void onSelectedItemChanged() {
               AttachmentAnchor newAnchor = AttachmentAnchor.find(AttachmentControllerMember.class, menu.getMenuAttachmentType(), this.getSelectedItem());
               if (!this.getCurrentAnchor().equals(newAnchor)) {
                  menu.updatePositionConfigValue("anchor", newAnchor.getName());
               }

            }

            private AttachmentAnchor getCurrentAnchor() {
               String name = (String)menu.getPositionConfigValue("anchor", AttachmentAnchor.DEFAULT.getName());
               return AttachmentAnchor.find(AttachmentControllerMember.class, menu.getMenuAttachmentType(), name);
            }
         }).setBounds(25, 0, menu.getSliderWidth(), 11);
      }).addLabel(0, 3, "Anchor");
      builder.addPositionSlider("posX", "Pos.X", "Position X-Coordinate").setSpacingAbove(3);
      builder.addPositionSlider("posY", "Pos.Y", "Position Y-Coordinate");
      builder.addPositionSlider("posZ", "Pos.Z", "Position Z-Coordinate");
      builder.addRotationSlider("rotX", "Pitch", "Rotation Pitch");
      builder.addRotationSlider("rotY", "Yaw", "Rotation Yaw");
      builder.addRotationSlider("rotZ", "Roll", "Rotation Roll");
      this.getMenuAttachmentType().createPositionMenu(builder);
      PositionMenu.Row prevRow = null;
      int yPos = 0;

      int rowHeight;
      for(Iterator var4 = builder.getRows().iterator(); var4.hasNext(); yPos += rowHeight) {
         PositionMenu.Row row = (PositionMenu.Row)var4.next();
         if (prevRow != null) {
            yPos += Math.max(prevRow.spacingBelow, row.spacingAbove);
         }

         prevRow = row;
         MapWidget widget = (MapWidget)row.creator.apply(this);
         rowHeight = widget.getY() + widget.getHeight();
         widget.setPosition(widget.getX(), widget.getY() + yPos);
         this.scroller.addContainerWidget(widget);
         Iterator var8 = row.labels.iterator();

         while(var8.hasNext()) {
            PositionMenu.Row.Label label = (PositionMenu.Row.Label)var8.next();
            MapWidgetText textWidget = new MapWidgetText();
            textWidget.setFont(MapFont.TINY);
            textWidget.setText(label.text);
            textWidget.setPosition(label.x, label.y + yPos);
            textWidget.setColor(MapColorPalette.getSpecular(this.labelColor, 0.5F));
            this.scroller.addContainerWidget(textWidget);
         }
      }

   }

   protected AttachmentType getMenuAttachmentType() {
      return this.getAttachment().getType();
   }

   public <T> T getPositionConfigValue(String key, T def) {
      ConfigurationNode config = this.getPositionConfig();
      return config.contains(key) ? config.getOrDefault(key, def) : def;
   }

   public void updatePositionConfigValue(String key, Object value) {
      this.updatePositionConfig((config) -> {
         config.set(key, value);
      });
   }

   public void updatePositionConfig(Consumer<ConfigurationNode> manipulator) {
      ConfigurationNode config = this.getPositionConfig();
      manipulator.accept(config);
      this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
   }

   public void updateConfigValue(String key, Object value) {
      this.updateConfig((config) -> {
         config.set(key, value);
      });
   }

   public void updateConfig(Consumer<ConfigurationNode> manipulator) {
      ConfigurationNode config = this.getConfig();
      manipulator.accept(config);
      this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", this.attachment);
   }

   public ConfigurationNode getConfig() {
      return this.attachment.getConfig();
   }

   public final ConfigurationNode getPositionConfig() {
      return this.getConfig().getNode("position");
   }

   public MapWidgetAttachmentNode getAttachment() {
      return this.attachment;
   }

   public static class Builder {
      private final ArrayList<PositionMenu.Row> rows = new ArrayList();

      public List<PositionMenu.Row> getRows() {
         return this.rows;
      }

      public PositionMenu.Row addRow(Function<PositionMenu, MapWidget> creator) {
         PositionMenu.Row row = new PositionMenu.Row(creator);
         this.rows.add(row);
         return row;
      }

      public PositionMenu.Row addRow(int index, Function<PositionMenu, MapWidget> creator) {
         PositionMenu.Row row = new PositionMenu.Row(creator);
         this.rows.add(index, row);
         return row;
      }

      public PositionMenu.Row addSizeBox() {
         return this.addRow((menu) -> {
            return (new MapWidgetSizeBox() {
               public void onAttached() {
                  super.onAttached();
                  this.setInitialSize((Double)menu.getPositionConfigValue("sizeX", 1.0D), (Double)menu.getPositionConfigValue("sizeY", 1.0D), (Double)menu.getPositionConfigValue("sizeZ", 1.0D));
               }

               public void onSizeChanged() {
                  menu.updatePositionConfig((config) -> {
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
            }).setBounds(25, 0, menu.getSliderWidth(), 35);
         }).addLabel(0, 3, "Size X").addLabel(0, 15, "Size Y").addLabel(0, 27, "Size Z").setSpacingAbove(3);
      }

      public PositionMenu.Row addPositionSlider(String settingName, String shortName, String propertyName) {
         return this.addPositionSlider(settingName, shortName, propertyName, Double.NaN);
      }

      public PositionMenu.Row addPositionSlider(String settingName, String shortName, String propertyName, double defaultValue) {
         return this.addRow((menu) -> {
            return (new MapWidgetNumberBox() {
               public void onAttached() {
                  super.onAttached();
                  this.setInitialValue((Double)menu.getPositionConfigValue(settingName, 0.0D));
               }

               public String getAcceptedPropertyName() {
                  return propertyName;
               }

               public void onValueChanged() {
                  if (this.getValue() == defaultValue) {
                     menu.updatePositionConfig((cfg) -> {
                        cfg.remove(settingName);
                     });
                  } else {
                     menu.updatePositionConfigValue(settingName, this.getValue());
                  }

               }
            }).setBounds(25, 0, menu.getSliderWidth(), 11);
         }).addLabel(0, 3, shortName);
      }

      public PositionMenu.Row addRotationSlider(String settingName, String shortName, String propertyName) {
         return this.addRow((menu) -> {
            return (new PositionMenu.RotationNumberBox() {
               public void onAttached() {
                  super.onAttached();
                  this.setInitialValue((Double)menu.getPositionConfigValue(settingName, 0.0D));
               }

               public String getAcceptedPropertyName() {
                  return propertyName;
               }

               public void onValueChanged() {
                  menu.updatePositionConfigValue(settingName, this.getValue());
               }
            }).setBounds(25, 0, menu.getSliderWidth(), 11);
         }).addLabel(0, 3, shortName);
      }
   }

   public static class Row {
      public final Function<PositionMenu, MapWidget> creator;
      public final List<PositionMenu.Row.Label> labels = new ArrayList();
      public int spacingAbove = 1;
      public int spacingBelow = 1;

      public Row(Function<PositionMenu, MapWidget> creator) {
         this.creator = creator;
      }

      public PositionMenu.Row addLabel(int x, int y, String text) {
         this.labels.add(new PositionMenu.Row.Label(x, y, text));
         return this;
      }

      public PositionMenu.Row setSpacingBelow(int spacing) {
         this.spacingBelow = spacing;
         return this;
      }

      public PositionMenu.Row setSpacingAbove(int spacing) {
         this.spacingAbove = spacing;
         return this;
      }

      public static class Label {
         public final int x;
         public final int y;
         public final String text;

         public Label(int x, int y, String text) {
            this.x = x;
            this.y = y;
            this.text = text;
         }
      }
   }

   private static class RotationNumberBox extends MapWidgetNumberBox {
      public RotationNumberBox() {
         this.setIncrement(0.1D);
      }

      public void onResetSpecial(Key key) {
         if (key == Key.RIGHT) {
            this.setValue(MathUtil.wrapAngle(this.getValue() + 90.0D));
         } else if (key == Key.LEFT) {
            this.setValue(MathUtil.wrapAngle(this.getValue() - 90.0D));
         } else {
            this.setValue(MathUtil.wrapAngle(this.getValue() + 180.0D));
         }

      }
   }
}
