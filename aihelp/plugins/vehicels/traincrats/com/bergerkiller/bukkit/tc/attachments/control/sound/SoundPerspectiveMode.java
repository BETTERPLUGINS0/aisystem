package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TrainCarts;

public enum SoundPerspectiveMode {
   SAME("play same sound for\nall perspectives"),
   CART("1p cart passengers\n3p outside cart"),
   TRAIN("1p train passengers\n3p outside train"),
   SEAT("1p seat passenger\n3p outside seat");

   private final MapTexture icon;
   private final String tooltip;

   private SoundPerspectiveMode(String tooltip) {
      MapTexture tex = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sound_perspectives.png");
      this.icon = tex.getView(tex.getHeight() * this.ordinal(), 0, tex.getHeight(), tex.getHeight()).clone();
      this.tooltip = tooltip;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   public MapTexture getIcon() {
      return this.icon;
   }

   public MapWidgetSoundSelector.Mode getSoundMode() {
      return this == SAME ? MapWidgetSoundSelector.Mode.ALL_PERSPECTIVE : MapWidgetSoundSelector.Mode.FIRST_PERSPECTIVE;
   }

   public MapWidgetSoundSelector.Mode getSoundAltMode() {
      return this == SAME ? MapWidgetSoundSelector.Mode.NONE : MapWidgetSoundSelector.Mode.THIRD_PERSPECTIVE;
   }

   // $FF: synthetic method
   private static SoundPerspectiveMode[] $values() {
      return new SoundPerspectiveMode[]{SAME, CART, TRAIN, SEAT};
   }
}
