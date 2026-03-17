package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;

public abstract class MapWidgetSoundAutoResumeToggle extends MapWidgetSoundButton {
   private boolean autoResume = false;
   private final MapTexture icon_disabled;
   private final MapTexture icon_enabled;
   private final MapWidgetTooltip tooltip = new MapWidgetTooltip();

   public MapWidgetSoundAutoResumeToggle() {
      MapTexture icon = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sound_autoresume.png");
      this.icon_disabled = icon.getView(0, 0, icon.getWidth() / 2, icon.getHeight()).clone();
      this.icon_enabled = icon.getView(icon.getWidth() / 2, 0, icon.getWidth() / 2, icon.getHeight()).clone();
      this.updateTooltip();
      this.setSize(this.icon_enabled.getWidth(), this.icon_enabled.getHeight());
   }

   public abstract void onAutoResumeChanged(boolean var1);

   public MapWidgetSoundAutoResumeToggle setAutoResume(boolean autoResume) {
      if (this.autoResume != autoResume) {
         this.autoResume = autoResume;
         this.updateTooltip();
         this.invalidate();
      }

      return this;
   }

   public boolean isAutoResume() {
      return this.autoResume;
   }

   private void updateTooltip() {
      this.tooltip.setText(this.autoResume ? "auto-resume: ON" : "auto-resume: OFF");
   }

   public void onClick() {
      this.setAutoResume(!this.isAutoResume());
      this.onAutoResumeChanged(this.isAutoResume());
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
      this.view.draw(this.autoResume ? this.icon_enabled : this.icon_disabled, 0, 0);
   }
}
