package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;

public abstract class MapWidgetSoundPerspectiveMode extends MapWidgetSoundButton {
   private SoundPerspectiveMode mode;
   public final MapWidgetTooltip tooltip;

   public MapWidgetSoundPerspectiveMode() {
      this.mode = SoundPerspectiveMode.SAME;
      this.tooltip = (new MapWidgetTooltip()).setText(this.mode.getTooltip());
      this.setSize(this.mode.getIcon().getWidth(), this.mode.getIcon().getHeight());
   }

   public abstract void onModeChanged(SoundPerspectiveMode var1);

   public MapWidgetSoundPerspectiveMode setMode(SoundPerspectiveMode mode) {
      if (this.mode != mode) {
         this.mode = mode;
         this.tooltip.setText(mode.getTooltip());
         this.invalidate();
      }

      return this;
   }

   public SoundPerspectiveMode getMode() {
      return this.mode;
   }

   public void onClick() {
      SoundPerspectiveMode[] values = SoundPerspectiveMode.values();
      this.mode = values[(this.mode.ordinal() + 1) % values.length];
      this.tooltip.setText(this.mode.getTooltip());
      this.onModeChanged(this.mode);
      this.invalidate();
   }

   public void onFocus() {
      super.onFocus();
      this.addWidget(this.tooltip);
   }

   public void onBlur() {
      super.onBlur();
      this.removeWidget(this.tooltip);
   }

   public void onDraw() {
      super.onDraw();
      this.view.draw(this.mode.getIcon(), 0, 0);
   }
}
