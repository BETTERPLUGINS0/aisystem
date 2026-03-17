package com.bergerkiller.bukkit.tc.attachments.control.sequencer;

import com.bergerkiller.bukkit.common.map.MapTexture;

public enum SequencerMode {
   START("start", "start"),
   LOOP("loop", "loop"),
   STOP("stop", "stop");

   private final String title;
   private final String configKey;
   private final MapTexture icon;

   private SequencerMode(String title, String configKey) {
      this.title = title;
      this.configKey = configKey;
      this.icon = MapWidgetSequencerEffect.TEXTURE_ATLAS.getView(7 * this.ordinal(), 35, 7, 5).clone();
   }

   public String title() {
      return this.title;
   }

   public String configKey() {
      return this.configKey;
   }

   public MapTexture icon() {
      return this.icon;
   }

   public SequencerMode next() {
      switch(this) {
      case START:
      case LOOP:
         return LOOP;
      default:
         return START;
      }
   }

   // $FF: synthetic method
   private static SequencerMode[] $values() {
      return new SequencerMode[]{START, LOOP, STOP};
   }
}
