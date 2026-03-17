package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MapRailsControl extends MapControl {
   private static final byte[] MARKER_COLORS = new byte[]{18, 30, 50, 122, 66, 42};
   private final boolean[] _directions = new boolean[6];
   private RailsTexture _texture = new RailsTexture();
   private BlockFace face;
   private int rotation;
   private int index;
   private int blinkCtr;
   private boolean blinkOn;

   public MapRailsControl() {
      this.face = BlockFace.NORTH;
      this.rotation = 0;
      this.index = -1;
      this.blinkCtr = 0;
      this.blinkOn = false;
   }

   public void setRails(RailType type, Block railsBlock) {
      this._texture = type.getRailsTexture(railsBlock);
   }

   public void setDirection(BlockFace direction, boolean enabled) {
      int idx = faceToIdx(direction);
      if (this._directions[idx] != enabled) {
         this._directions[idx] = enabled;
         this.draw();
      }

   }

   public boolean getDirection(BlockFace direction) {
      return this._directions[faceToIdx(direction)];
   }

   public BlockFace[] getDirections() {
      ArrayList<BlockFace> faces = new ArrayList(2);
      BlockFace[] var2 = FaceUtil.BLOCK_SIDES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         if (this.getDirection(face)) {
            faces.add(face);
         }
      }

      return (BlockFace[])LogicUtil.toArray(faces, BlockFace.class);
   }

   public void onInit() {
      Arrays.fill(this._directions, true);
      this.updateView();
   }

   public void onTick() {
      if (this.updateView()) {
         this.draw();
      }

      if (this.isSelected()) {
         if (this.index == -1) {
            this.nextIndex(1);
         }

         if (++this.blinkCtr >= 6) {
            this.blinkCtr = 0;
            this.blinkOn = !this.blinkOn;
            this.draw();
         }
      } else if (this.index != -1) {
         this.index = -1;
         this.draw();
      }

   }

   public void onDraw() {
      MapTexture texture = MapTexture.rotate(this._texture.get(this.face), this.rotation);
      this.display.getLayer(2).setBlendMode(MapBlendMode.NONE);
      this.display.getLayer(2).draw(texture, this.x, this.y);
      this.display.getLayer(3).setBlendMode(MapBlendMode.NONE);
      this.display.getLayer(3).clearRectangle(this.x, this.y, texture.getWidth(), texture.getHeight());
      MapTexture arrow = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/arrow.png");
      BlockFace[] var3 = FaceUtil.BLOCK_SIDES;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         BlockFace markerFace = this.getMarkerFace(face);
         if (markerFace != null) {
            MapTexture tex = null;
            int i = faceToIdx(face);
            if (i == this.index && this.blinkOn) {
               tex = arrow;
            } else if (this._directions[i]) {
               tex = arrow.clone();
               tex.setBlendMode(MapBlendMode.MULTIPLY);
               tex.fill(MARKER_COLORS[i]);
            }

            if (tex != null) {
               tex = MapTexture.rotate(tex, 270 - FaceUtil.faceToYaw(markerFace));
               int arrow_dx = (texture.getWidth() - tex.getWidth()) / 2;
               int arrow_dy = (texture.getHeight() - tex.getHeight()) / 2;
               if (markerFace == BlockFace.NORTH) {
                  arrow_dy = 0;
               } else if (markerFace == BlockFace.EAST) {
                  arrow_dx = texture.getWidth() - tex.getWidth();
               } else if (markerFace == BlockFace.SOUTH) {
                  arrow_dy = texture.getHeight() - tex.getHeight();
               } else if (markerFace == BlockFace.WEST) {
                  arrow_dx = 0;
               }

               this.display.getLayer(3).setBlendMode(MapBlendMode.NONE);
               this.display.getLayer(3).draw(tex, this.x + arrow_dx, this.y + arrow_dy);
            }
         }
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.UP) {
         this.nextIndex(1);
      } else if (event.getKey() == Key.DOWN) {
         this.nextIndex(-1);
      } else if (event.getKey() == Key.ENTER && this.index >= 0 && this.index < this._directions.length) {
         this._directions[this.index] = !this._directions[this.index];
         this.draw();
      }

   }

   private void nextIndex(int n) {
      do {
         this.index += n;
         if (this.index < 0) {
            this.index = this._directions.length - 1;
         } else if (this.index >= this._directions.length) {
            this.index = 0;
         }
      } while(this.getMarkerFace(idxToFace(this.index)) == null);

      this.draw();
   }

   private BlockFace getMarkerFace(BlockFace face) {
      if (FaceUtil.isVertical(this.face) && FaceUtil.isVertical(face)) {
         return null;
      } else if (this.face == BlockFace.UP) {
         return FaceUtil.yawToFace((float)(FaceUtil.faceToYaw(face) - this.rotation));
      } else {
         BlockFace combined;
         if (this.face == BlockFace.DOWN) {
            combined = FaceUtil.yawToFace((float)(FaceUtil.faceToYaw(face) + this.rotation));
            if (FaceUtil.isAlongZ(combined)) {
               combined = combined.getOppositeFace();
            }

            return combined;
         } else if (face == BlockFace.UP) {
            return BlockFace.NORTH;
         } else if (face == BlockFace.DOWN) {
            return BlockFace.SOUTH;
         } else {
            combined = FaceUtil.yawToFace((float)(FaceUtil.faceToYaw(face) - FaceUtil.faceToYaw(this.face)));
            return FaceUtil.isAlongX(combined) ? combined : null;
         }
      }
   }

   private boolean updateView() {
      Location loc = ((TCMapEditor)this.display).getOwner().getLocation();
      BlockFace face_new = FaceUtil.yawToFace(loc.getYaw() + 90.0F, false);
      int rotation_new = false;
      int rotation_new;
      if (loc.getPitch() > 70.0F) {
         rotation_new = FaceUtil.faceToNotch(face_new) * 45;
         face_new = BlockFace.UP;
      } else if (loc.getPitch() < -70.0F) {
         rotation_new = -FaceUtil.faceToNotch(face_new) * 45;
         face_new = BlockFace.DOWN;
      } else {
         rotation_new = 0;
      }

      if (face_new == this.face && rotation_new == this.rotation) {
         return false;
      } else {
         this.face = face_new;
         this.rotation = rotation_new;
         return true;
      }
   }

   private static final int faceToIdx(BlockFace face) {
      switch(face) {
      case NORTH:
         return 0;
      case EAST:
         return 1;
      case SOUTH:
         return 2;
      case WEST:
         return 3;
      case UP:
         return 4;
      case DOWN:
         return 5;
      default:
         return 0;
      }
   }

   private static final BlockFace idxToFace(int idx) {
      switch(idx) {
      case 0:
         return BlockFace.NORTH;
      case 1:
         return BlockFace.EAST;
      case 2:
         return BlockFace.SOUTH;
      case 3:
         return BlockFace.WEST;
      case 4:
         return BlockFace.UP;
      case 5:
         return BlockFace.DOWN;
      default:
         return BlockFace.NORTH;
      }
   }
}
