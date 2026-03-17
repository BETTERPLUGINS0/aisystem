package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.SignActionHeader;
import com.bergerkiller.bukkit.tc.SignRedstoneMode;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignActionMode;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class EditedSign {
   private Sign _sign;
   private SignActionHeader _header;

   public void load(Sign sign) {
      this._sign = sign;
      this._header = SignActionHeader.parseFromSign(sign);
   }

   public boolean isValid() {
      return this._sign != null && this._header != null;
   }

   public void save() {
      this._sign.setLine(0, this._header.toString());
      this._sign.update();
      Block rails = Util.getRailsFromSign(this._sign.getBlock());
      if (rails != null) {
         MinecartMember<?> member = MinecartMemberStore.getAt(rails);
         if (member != null) {
            member.getSignTracker().update();
         }
      }

   }

   public String getName() {
      return "Unknown Sign";
   }

   public void setMode(SignActionMode mode) {
      this._header.setMode(mode);
      this.save();
   }

   public SignActionMode getMode() {
      return this._header.getMode();
   }

   public Direction[] getDirections() {
      return this._header.getDirections();
   }

   public void setDirections(Direction[] directions) {
      this._header.setDirections(directions);
      this.save();
   }

   public void setRedstoneMode(SignRedstoneMode mode) {
      this._header.setRedstoneMode(mode);
      this.save();
   }

   public SignRedstoneMode getRedstoneMode() {
      return this._header.getRedstoneMode();
   }

   public void initEditor(final TCMapEditor editor) {
      editor.addControl(new MapControl() {
         private final SignRedstoneMode[] modes;

         {
            this.modes = new SignRedstoneMode[]{SignRedstoneMode.ON, SignRedstoneMode.OFF, SignRedstoneMode.ALWAYS, SignRedstoneMode.PULSE_ON, SignRedstoneMode.PULSE_OFF, SignRedstoneMode.PULSE_ALWAYS};
         }

         public void onInit() {
            this.setLocation(5, 20);
            this.setBackground(this.display.loadTexture("com/bergerkiller/bukkit/tc/textures/redstone/bg.png"));
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() != Key.DOWN && event.getKey() != Key.ENTER) {
               if (event.getKey() == Key.UP) {
                  EditedSign.this.setRedstoneMode((SignRedstoneMode)EditedSign.nextElement(this.modes, EditedSign.this.getRedstoneMode(), -1));
                  this.draw();
               }
            } else {
               EditedSign.this.setRedstoneMode((SignRedstoneMode)EditedSign.nextElement(this.modes, EditedSign.this.getRedstoneMode(), 1));
               this.draw();
            }

         }

         public void onDraw() {
            MapTexture texture = editor.loadTexture("com/bergerkiller/bukkit/tc/textures/redstone/" + EditedSign.this.getRedstoneMode().name().toLowerCase(Locale.ENGLISH) + ".png");
            this.display.getLayer(2).setBlendMode(MapBlendMode.NONE);
            this.display.getLayer(2).draw(texture, this.x, this.y);
         }
      });
      editor.addControl(new MapControl() {
         private final SignActionMode[] modes;

         {
            this.modes = new SignActionMode[]{SignActionMode.CART, SignActionMode.TRAIN, SignActionMode.RCTRAIN};
         }

         public void onInit() {
            this.setLocation(40, 20);
            this.setBackground(this.display.loadTexture("com/bergerkiller/bukkit/tc/textures/modes/bg.png"));
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() != Key.DOWN && event.getKey() != Key.ENTER) {
               if (event.getKey() == Key.UP) {
                  EditedSign.this.setMode((SignActionMode)EditedSign.nextElement(this.modes, EditedSign.this.getMode(), -1));
                  this.draw();
               }
            } else {
               EditedSign.this.setMode((SignActionMode)EditedSign.nextElement(this.modes, EditedSign.this.getMode(), 1));
               this.draw();
            }

         }

         public void onDraw() {
            MapTexture texture = editor.loadTexture("com/bergerkiller/bukkit/tc/textures/modes/" + EditedSign.this.getMode().name().toLowerCase(Locale.ENGLISH) + ".png");
            this.display.getLayer(2).setBlendMode(MapBlendMode.NONE);
            this.display.getLayer(2).draw(texture, this.x, this.y);
         }
      });
      editor.addControl(new MapRailsControl() {
         public void onInit() {
            this.setLocation(80, 20);
            if (editor.getRailsBlock() != null) {
               Iterator var1 = RailType.values().iterator();

               while(var1.hasNext()) {
                  RailType type = (RailType)var1.next();
                  if (type.isRail(editor.getRailsBlock())) {
                     this.setRails(type, editor.getRailsBlock());
                     break;
                  }
               }
            }

            super.onInit();
         }
      });
   }

   private static <T> T nextElement(T[] elements, T value, int n) {
      int i;
      for(i = 0; i < elements.length && elements[i] != value; ++i) {
      }

      for(i += n; i >= elements.length; i -= elements.length) {
      }

      while(i < 0) {
         i += elements.length;
      }

      return elements[i];
   }
}
