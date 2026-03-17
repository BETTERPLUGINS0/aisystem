package com.bergerkiller.bukkit.tc.attachments.control.effect;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;

public class SimpleScheduledEffectLoopDialog extends MapWidgetMenu {
   private final ConfigurationNode config;

   public SimpleScheduledEffectLoopDialog(ConfigurationNode config) {
      this.config = config;
      this.setPositionAbsolute(true);
      this.setBounds(39, 40, 70, 30);
      this.setBackgroundColor(MapColorPalette.getColor(72, 108, 152));
      this.labelColor = 119;
   }

   public void onAttached() {
      this.addLabel(5, 6, "Delay (s):");
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            this.setIncrement(0.01D);
            this.setRange(0.0D, 10000.0D);
            this.setInitialValue((Double)SimpleScheduledEffectLoopDialog.this.config.getOrDefault("delay", 0.0D));
            super.onAttached();
         }

         public void onValueChanged() {
            SimpleScheduledEffectLoopDialog.this.config.set("delay", this.getValue() == 0.0D ? null : this.getValue());
         }
      })).setBounds(5, 13, this.getWidth() - 10, 11);
      super.onAttached();
   }
}
