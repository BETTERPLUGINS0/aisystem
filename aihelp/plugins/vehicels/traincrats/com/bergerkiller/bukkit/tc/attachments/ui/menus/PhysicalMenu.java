package com.bergerkiller.bukkit.tc.attachments.ui.menus;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.events.map.MapStatusEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.particle.PhysicalMemberPreview;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class PhysicalMenu extends MapWidgetMenu {
   private static final int PREVIEW_OFFSET = 5;
   private static final int PREVIEW_HEIGHT = 10;
   private static final int NUMBERBOX_OFFSET = 27;
   private static final int NUMBERBOX_STEP = 21;
   private static final int NUMBERBOX_HEIGHT = 11;
   private final MapTexture wheelTexture;
   private PhysicalMemberPreview preview;
   private int ticksPreviewVisible = 0;

   public PhysicalMenu() {
      this.setBounds(5, 15, 118, 107);
      this.setBackgroundColor((byte)62);
      this.wheelTexture = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/wheel.png");
   }

   public void onAttached() {
      super.onAttached();
      if (this.getAttachment().getEditor().getEditedCart() != null) {
         this.preview = new PhysicalMemberPreview(this.getAttachment().getEditor().getEditedCart(), () -> {
            return (Collection)(this.ticksPreviewVisible > 0 && this.display != null ? this.display.getOwners() : Collections.emptySet());
         });
      }

      byte lblColor = MapColorPalette.getColor(152, 89, 36);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setRange(0.0D, Double.POSITIVE_INFINITY);
            this.setValue((Double)PhysicalMenu.this.getConfig().get("cartLength", 1.0D));
         }

         public String getAcceptedPropertyName() {
            return "Cart Length";
         }

         public void onResetValue() {
            this.setValue(0.9800000190734863D);
         }

         public void onValueChanged() {
            PhysicalMenu.this.getConfig().set("cartLength", this.getValue());
            PhysicalMenu.this.onChanged();
         }
      })).setBounds(10, 27, 100, 11);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Cart Length").setPosition(20, 19);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setRange(-100.0D, Math.max(0.1D, TCConfig.cartDistanceGapMax));
            this.setInitialValue((Double)PhysicalMenu.this.getConfig().getOrDefault("cartCouplerLength", 0.5D * TCConfig.cartDistanceGap));
         }

         public String getAcceptedPropertyName() {
            return "Coupler Length";
         }

         public void onResetValue() {
            this.setValue(0.5D * TCConfig.cartDistanceGap);
         }

         public void onValueChanged() {
            if (this.getValue() == 0.5D * TCConfig.cartDistanceGap) {
               PhysicalMenu.this.getConfig().remove("cartCouplerLength");
            } else {
               PhysicalMenu.this.getConfig().set("cartCouplerLength", this.getValue());
            }

            PhysicalMenu.this.onChanged();
         }
      })).setBounds(10, 48, 100, 11);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Coupler Length").setPosition(20, 40);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setRange(0.0D, Double.POSITIVE_INFINITY);
            this.setValue((Double)PhysicalMenu.this.getConfig().get("wheelDistance", 0.0D));
         }

         public String getAcceptedPropertyName() {
            return "Wheel Distance";
         }

         public void onValueChanged() {
            PhysicalMenu.this.getConfig().set("wheelDistance", this.getValue());
            PhysicalMenu.this.onChanged();
         }
      })).setBounds(10, 69, 100, 11);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Wheel Distance").setPosition(20, 61);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setValue((Double)PhysicalMenu.this.getConfig().get("wheelCenter", 0.0D));
         }

         public String getAcceptedPropertyName() {
            return "Wheel Center Offset";
         }

         public void onValueChanged() {
            PhysicalMenu.this.getConfig().set("wheelCenter", this.getValue());
            PhysicalMenu.this.onChanged();
         }
      })).setBounds(10, 90, 100, 11);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Wheel Offset").setPosition(20, 82);
   }

   public void onDetached() {
      super.onDetached();
      if (this.preview != null) {
         this.preview.hide();
      }

   }

   public void onTick() {
      super.onTick();
      if (this.preview != null) {
         this.preview.update();
      }

      if (--this.ticksPreviewVisible < 0) {
         this.ticksPreviewVisible = 0;
      }

   }

   private void onChanged() {
      this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
      this.ticksPreviewVisible = 100;
   }

   public ConfigurationNode getConfig() {
      return this.attachment.getConfig().getNode("physical");
   }

   public MapWidgetAttachmentNode getAttachment() {
      return this.attachment;
   }

   public void onStatusChanged(MapStatusEvent event) {
      if (event.isName("changed")) {
         this.invalidate();
      }

   }

   public void onDraw() {
      super.onDraw();
      double cartLength = (Double)this.getConfig().get("cartLength", 1.0D);
      double wheelDistance = (Double)this.getConfig().get("wheelDistance", 0.0D);
      double wheelCenter = (Double)this.getConfig().get("wheelCenter", 0.0D);
      double cartLengthFactor = 20.0D;
      double maxCartLength = 5.0D;
      boolean isMaxScale = cartLength > maxCartLength;
      double scaleFactor = cartLengthFactor * (isMaxScale ? maxCartLength / cartLength : 1.0D);
      cartLength *= scaleFactor;
      wheelDistance *= scaleFactor;
      wheelCenter *= scaleFactor;
      if (isMaxScale) {
         cartLength = cartLengthFactor * maxCartLength;
      }

      int hull_x = MathUtil.floor(0.5D * (double)this.getWidth() - 0.5D * cartLength);
      int hull_y = 5;
      int hull_w = MathUtil.ceil(cartLength);
      int hull_h = 10;
      this.view.drawRectangle(hull_x, hull_y, hull_w, hull_h, (byte)119);
      Random rand = new Random(12345678L);

      int px;
      int py;
      for(px = 1; px < hull_w - 1; ++px) {
         for(py = 1; py < hull_h - 1; ++py) {
            byte color = (byte)(128 + rand.nextInt(40));
            this.view.drawPixel(hull_x + px, hull_y + py, MapColorPalette.getColor(color, color, color));
         }
      }

      int wheel_x1 = MathUtil.floor(0.5D * (double)this.getWidth() - 0.5D * wheelDistance + wheelCenter);
      px = MathUtil.ceil(0.5D * (double)this.getWidth() + 0.5D * wheelDistance + wheelCenter);
      py = hull_y + hull_h - 1;
      this.drawWheel(MathUtil.clamp(wheel_x1, hull_x, hull_x + hull_w - 1), py);
      this.drawWheel(MathUtil.clamp(px, hull_x, hull_x + hull_w - 1), py);
   }

   private final void drawWheel(int x, int y) {
      this.view.draw(this.wheelTexture, x - MathUtil.floor(0.5D * (double)this.wheelTexture.getWidth()), y - MathUtil.floor(0.5D * (double)this.wheelTexture.getHeight()));
   }
}
