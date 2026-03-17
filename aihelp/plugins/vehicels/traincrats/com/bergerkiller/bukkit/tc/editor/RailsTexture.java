package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import java.util.Arrays;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

public class RailsTexture {
   private static MapTexture default_texture = null;
   private final MapTexture[] textures;
   private final JavaPlugin owner;
   private final String root;

   public RailsTexture() {
      this(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/rails/");
   }

   public RailsTexture(JavaPlugin owner, String textureRoot) {
      this.textures = new MapTexture[6];
      this.owner = owner;
      this.root = textureRoot;
      if (default_texture == null) {
         default_texture = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/rails/unknown.png");
      }

      Arrays.fill(this.textures, default_texture);
   }

   public RailsTexture setOpposites(BlockFace face, String filename) {
      return this.setOpposites(face, this.load(filename));
   }

   public RailsTexture setOpposites(BlockFace face, MapTexture texture) {
      return this.set(face, texture).set(face.getOppositeFace(), FaceUtil.isVertical(face) ? MapTexture.flipV(texture) : MapTexture.flipH(texture));
   }

   public RailsTexture set(BlockFace face, String filename) {
      return this.set(face, this.load(filename));
   }

   public RailsTexture set(BlockFace face, MapTexture texture) {
      this.textures[faceToIdx(face)] = texture;
      return this;
   }

   public MapTexture get(BlockFace face) {
      return this.textures[faceToIdx(face)];
   }

   private final MapTexture load(String filename) {
      return MapTexture.loadPluginResource(this.owner, this.root + filename);
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
}
