package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.ResourceKey;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutCustomSoundEffectHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutStopSoundHandle;
import java.util.Iterator;
import org.bukkit.entity.Player;

public abstract class MapWidgetSoundSelector extends MapWidget {
   private final MapWidgetSoundNameSelector name = new MapWidgetSoundNameSelector() {
      public void onSoundChanged(ResourceKey<SoundEffect> sound) {
         MapWidgetSoundSelector.this.playSound(this.display);
         MapWidgetSoundSelector.this.onSoundChanged(sound);
      }

      public void onFocus() {
         MapWidgetSoundSelector.this.playSound(this.display);
      }

      public void onBlur() {
         MapWidgetSoundSelector.this.stopSound(this.display);
      }

      public void onDetached() {
         super.onDetached();
         MapWidgetSoundSelector.this.stopSound(this.display);
      }
   };
   private final MapWidgetSoundCategorySelector category = new MapWidgetSoundCategorySelector() {
      public void onCategoryChanged(String categoryName) {
         MapWidgetSoundSelector.this.onCategoryChanged(categoryName);
      }
   };
   private MapWidgetSoundSelector.Mode mode;
   private ResourceKey<SoundEffect> lastPreviewedSound;
   private String lastPreviewedCategory;

   public MapWidgetSoundSelector() {
      this.mode = MapWidgetSoundSelector.Mode.FIRST_PERSPECTIVE;
      this.lastPreviewedSound = null;
      this.lastPreviewedCategory = "master";
   }

   public abstract void onSoundChanged(ResourceKey<SoundEffect> var1);

   public abstract void onCategoryChanged(String var1);

   public MapWidgetSoundSelector setMode(MapWidgetSoundSelector.Mode mode) {
      if (this.mode != mode) {
         this.mode = mode;
         this.setVisible(mode != MapWidgetSoundSelector.Mode.NONE);
         this.invalidate();
      }

      return this;
   }

   public MapWidgetSoundSelector setSoundPath(String soundPath) {
      ResourceKey<SoundEffect> key = soundPath == null ? null : SoundEffect.fromName(soundPath);
      return this.setSound(key);
   }

   public String getSoundPath() {
      return this.name.getSound() == null ? null : this.name.getSound().getPath();
   }

   public MapWidgetSoundSelector setSound(ResourceKey<SoundEffect> sound) {
      this.name.setSound(sound);
      return this;
   }

   public ResourceKey<SoundEffect> getSound() {
      return this.name.getSound();
   }

   public MapWidgetSoundSelector setCategory(String categoryName) {
      this.category.setCategory(categoryName);
      return this;
   }

   public String getCategory() {
      return this.category.getCategory();
   }

   public void onAttached() {
      this.onBoundsChanged();
      this.addWidget(this.name);
      this.addWidget(this.category);
   }

   public void onBoundsChanged() {
      int modeSpace = 7;
      this.name.setBounds(modeSpace, 0, this.getWidth() - this.category.getWidth() - 1 - modeSpace, this.getHeight());
      this.category.setBounds(this.name.getX() + this.name.getWidth() + 1, 0, this.category.getWidth(), this.getHeight());
   }

   public void onDraw() {
      switch(this.mode) {
      case FIRST_PERSPECTIVE:
         this.draw1p(0, 3, (byte)18);
         break;
      case THIRD_PERSPECTIVE:
         this.draw3p(0, 3, (byte)18);
         break;
      case ALL_PERSPECTIVE:
         this.draw1p(0, 0, (byte)18);
         this.draw3p(0, 6, (byte)18);
      }

   }

   private void draw1p(int x, int y, byte color) {
      this.view.drawPixel(x, y + 1, color);
      this.view.drawLine(x + 1, y, x + 1, y + 4, color);
      this.view.draw(MapFont.TINY, x + 3, y, color, "p");
   }

   private void draw3p(int x, int y, byte color) {
      this.view.drawPixel(x, y, color);
      this.view.drawPixel(x, y + 2, color);
      this.view.drawPixel(x, y + 4, color);
      this.view.drawLine(x + 1, y, x + 1, y + 4, color);
      this.view.draw(MapFont.TINY, x + 3, y, color, "p");
   }

   private void playSound(MapDisplay display) {
      this.stopSound(display);
      this.lastPreviewedSound = this.getSound();
      this.lastPreviewedCategory = this.getCategory();
      if (this.lastPreviewedSound != null) {
         Iterator var2 = display.getOwners().iterator();

         while(var2.hasNext()) {
            Player player = (Player)var2.next();
            PacketPlayOutCustomSoundEffectHandle packet = PacketPlayOutCustomSoundEffectHandle.createNew(this.lastPreviewedSound, this.lastPreviewedCategory, player.getLocation(), 1.0F, 1.0F);
            PacketUtil.sendPacket(player, packet);
         }
      }

   }

   private void stopSound(MapDisplay display) {
      if (Common.hasCapability("Common:Sound:StopSoundPacket")) {
         this.stopSoundImpl(display);
      }

   }

   private void stopSoundImpl(MapDisplay display) {
      if (this.lastPreviewedSound != null) {
         PacketPlayOutStopSoundHandle packet = PacketPlayOutStopSoundHandle.createNew(this.lastPreviewedSound, this.lastPreviewedCategory);
         Iterator var3 = display.getOwners().iterator();

         while(var3.hasNext()) {
            Player player = (Player)var3.next();
            PacketUtil.sendPacket(player, packet);
         }

         this.lastPreviewedSound = null;
      }

   }

   public static enum Mode {
      NONE,
      FIRST_PERSPECTIVE,
      THIRD_PERSPECTIVE,
      ALL_PERSPECTIVE;

      // $FF: synthetic method
      private static MapWidgetSoundSelector.Mode[] $values() {
         return new MapWidgetSoundSelector.Mode[]{NONE, FIRST_PERSPECTIVE, THIRD_PERSPECTIVE, ALL_PERSPECTIVE};
      }
   }
}
