package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;

public abstract class MapWidgetSoundPositionMode extends MapWidgetSoundButton {
   private MapWidgetSoundPositionMode.SoundPositionMode mode;
   private boolean isSamePerspective;
   public final MapWidgetTooltip tooltip;

   public MapWidgetSoundPositionMode() {
      this.mode = MapWidgetSoundPositionMode.SoundPositionMode.DEFAULT;
      this.isSamePerspective = false;
      this.tooltip = (new MapWidgetTooltip()).setText(this.mode.getTooltip());
      this.setSize(this.mode.getIcon().getWidth(), this.mode.getIcon().getHeight());
   }

   public abstract void onModeChanged(MapWidgetSoundPositionMode.SoundPositionMode var1);

   public MapWidgetSoundPositionMode setIsSamePerspective(boolean isSamePerspective) {
      if (this.isSamePerspective != isSamePerspective) {
         this.isSamePerspective = isSamePerspective;
         if (this.mode.isAtPlayer1P() != this.mode.isAtPlayer3P()) {
            this.tooltip.setText(this.getDisplayedMode().getTooltip());
            this.invalidate();
         }
      }

      return this;
   }

   public MapWidgetSoundPositionMode setMode(boolean atPerson1P, boolean atPerson3P) {
      return this.setMode(MapWidgetSoundPositionMode.SoundPositionMode.fromPerspectives(atPerson1P, atPerson3P));
   }

   public MapWidgetSoundPositionMode setMode(MapWidgetSoundPositionMode.SoundPositionMode mode) {
      if (this.mode != mode) {
         this.mode = mode;
         this.tooltip.setText(this.getDisplayedMode().getTooltip());
         this.invalidate();
      }

      return this;
   }

   public MapWidgetSoundPositionMode.SoundPositionMode getMode() {
      return this.mode;
   }

   public void onClick() {
      MapWidgetSoundPositionMode.SoundPositionMode[] values;
      if (this.isSamePerspective) {
         values = new MapWidgetSoundPositionMode.SoundPositionMode[]{MapWidgetSoundPositionMode.SoundPositionMode.DEFAULT, MapWidgetSoundPositionMode.SoundPositionMode.AT_PLAYER};
      } else {
         values = MapWidgetSoundPositionMode.SoundPositionMode.values();
      }

      this.mode = values[(this.getDisplayedMode().ordinal() + 1) % values.length];
      this.tooltip.setText(this.getDisplayedMode().getTooltip());
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
      this.view.draw(this.getDisplayedMode().getIcon(), 0, 0);
   }

   private MapWidgetSoundPositionMode.SoundPositionMode getDisplayedMode() {
      if (this.isSamePerspective && this.mode.isAtPlayer1P() != this.mode.isAtPlayer3P()) {
         return this.mode.isAtPlayer1P() ? MapWidgetSoundPositionMode.SoundPositionMode.AT_PLAYER : MapWidgetSoundPositionMode.SoundPositionMode.DEFAULT;
      } else {
         return this.mode;
      }
   }

   public static enum SoundPositionMode {
      DEFAULT("play at position", false, false),
      AT_PLAYER("play at player", true, true),
      FIRST_PERSON_AT_PLAYER("play 1p at player\nplay 3p at position", true, false),
      THIRD_PERSON_AT_PLAYER("play 1p at position\nplay 3p at player", false, true);

      private final MapTexture icon;
      private final String tooltip;
      private final boolean firstPersonAtPlayer;
      private final boolean thirdPersonAtPlayer;

      private SoundPositionMode(String tooltip, boolean firstPersonAtPlayer, boolean thirdPersonAtPlayer) {
         MapTexture tex = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sound_positions.png");
         this.icon = tex.getView(tex.getHeight() * this.ordinal(), 0, tex.getHeight(), tex.getHeight()).clone();
         this.tooltip = tooltip;
         this.firstPersonAtPlayer = firstPersonAtPlayer;
         this.thirdPersonAtPlayer = thirdPersonAtPlayer;
      }

      public String getTooltip() {
         return this.tooltip;
      }

      public MapTexture getIcon() {
         return this.icon;
      }

      public boolean isAtPlayer1P() {
         return this.firstPersonAtPlayer;
      }

      public boolean isAtPlayer3P() {
         return this.thirdPersonAtPlayer;
      }

      public static MapWidgetSoundPositionMode.SoundPositionMode fromPerspectives(boolean atPlayer1P, boolean atPlayer3P) {
         if (atPlayer1P) {
            return atPlayer3P ? AT_PLAYER : FIRST_PERSON_AT_PLAYER;
         } else {
            return atPlayer3P ? THIRD_PERSON_AT_PLAYER : DEFAULT;
         }
      }

      // $FF: synthetic method
      private static MapWidgetSoundPositionMode.SoundPositionMode[] $values() {
         return new MapWidgetSoundPositionMode.SoundPositionMode[]{DEFAULT, AT_PLAYER, FIRST_PERSON_AT_PLAYER, THIRD_PERSON_AT_PLAYER};
      }
   }
}
