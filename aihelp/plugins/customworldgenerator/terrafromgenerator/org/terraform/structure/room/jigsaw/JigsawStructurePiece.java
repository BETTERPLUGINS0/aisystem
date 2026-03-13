package org.terraform.structure.room.jigsaw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;

public abstract class JigsawStructurePiece implements Cloneable {
   protected final JigsawType type;
   protected CubeRoom room;
   protected HashMap<BlockFace, Boolean> validDirections = new HashMap();
   protected ArrayList<BlockFace> walledFaces = new ArrayList();
   protected JigsawStructurePiece[] allowedPieces;
   protected int depth = 0;
   protected BlockFace rotation;
   protected boolean unique;
   protected int elevation;

   public JigsawStructurePiece(int widthX, int height, int widthZ, JigsawType type, boolean unique, @NotNull BlockFace... validDirs) {
      this.rotation = BlockFace.NORTH;
      this.unique = false;
      this.elevation = 0;
      this.room = new CubeRoom(widthX, widthZ, height, 0, 0, 0);
      this.type = type;
      this.unique = unique;
      BlockFace[] var7 = validDirs;
      int var8 = validDirs.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         BlockFace face = var7[var9];
         this.validDirections.put(face, false);
      }

   }

   public JigsawStructurePiece(int widthX, int height, int widthZ, JigsawType type, @NotNull BlockFace... validDirs) {
      this.rotation = BlockFace.NORTH;
      this.unique = false;
      this.elevation = 0;
      this.room = new CubeRoom(widthX, widthZ, height, 0, 0, 0);
      this.type = type;
      BlockFace[] var6 = validDirs;
      int var7 = validDirs.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockFace face = var6[var8];
         this.validDirections.put(face, false);
      }

   }

   public void postBuildDecoration(Random random, PopulatorDataAbstract data) {
   }

   @Nullable
   public JigsawStructurePiece getInstance(@NotNull Random rand, int depth) {
      try {
         JigsawStructurePiece clone = (JigsawStructurePiece)this.clone();
         clone.room = new CubeRoom(this.room.getWidthX(), this.room.getWidthZ(), this.room.getHeight(), 0, 0, 0);
         clone.validDirections = (HashMap)this.validDirections.clone();
         Iterator var4 = this.validDirections.keySet().iterator();

         while(var4.hasNext()) {
            BlockFace face = (BlockFace)var4.next();
            clone.validDirections.put(face, false);
         }

         clone.walledFaces = new ArrayList();
         clone.setRotation(BlockUtils.getDirectBlockFace(rand));
         clone.elevation = 0;
         clone.setDepth(depth);
         return clone;
      } catch (CloneNotSupportedException var6) {
         TerraformGeneratorPlugin.logger.stackTrace(var6);
         return null;
      }
   }

   public abstract void build(PopulatorDataAbstract var1, Random var2);

   @Nullable
   public BlockFace getNextUnpopulatedBlockFace() {
      Iterator var1 = this.validDirections.entrySet().iterator();

      Entry entry;
      do {
         if (!var1.hasNext()) {
            TerraformGeneratorPlugin.logger.error("Tried to get unpopulated block face when there aren't any left!");
            return null;
         }

         entry = (Entry)var1.next();
      } while((Boolean)entry.getValue());

      return (BlockFace)entry.getKey();
   }

   public void setPopulated(BlockFace face) {
      if (this.type != JigsawType.END) {
         if (!this.validDirections.containsKey(face)) {
            TerraformGeneratorPlugin.logger.error("Tried to set an invalid blockface as populated for a jigsaw piece.");
         }

         this.validDirections.put(face, true);
      }
   }

   public boolean hasUnpopulatedDirections() {
      if (this.type == JigsawType.END) {
         return false;
      } else {
         Iterator var1 = this.validDirections.values().iterator();

         Boolean populated;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            populated = (Boolean)var1.next();
         } while(populated);

         return true;
      }
   }

   public CubeRoom getRoom() {
      return this.room;
   }

   public HashMap<BlockFace, Boolean> getValidDirections() {
      return this.type == JigsawType.END ? new HashMap() : this.validDirections;
   }

   public JigsawStructurePiece[] getAllowedPieces() {
      return this.allowedPieces;
   }

   public JigsawType getType() {
      return this.type;
   }

   public int getDepth() {
      return this.depth;
   }

   public void setDepth(int depth) {
      this.depth = depth;
   }

   public int getElevation() {
      return this.elevation;
   }

   public void setElevation(int elevation) {
      this.elevation = elevation;
   }

   public BlockFace getRotation() {
      return this.rotation;
   }

   public void setRotation(BlockFace rotation) {
      this.rotation = rotation;
   }

   public CubeRoom getExtendedRoom(int extraSize) {
      return this.walledFaces.isEmpty() ? this.room : new CubeRoom(this.room.getWidthX() + extraSize * 2, this.room.getWidthZ() + extraSize * 2, this.room.getHeight(), this.room.getX(), this.room.getY(), this.room.getZ());
   }

   @NotNull
   public String toString() {
      StringBuilder directions = new StringBuilder();
      Iterator var2 = this.validDirections.keySet().iterator();

      while(var2.hasNext()) {
         BlockFace face = (BlockFace)var2.next();
         directions.append(face).append(",");
      }

      String var10000 = this.getClass().getSimpleName();
      return var10000 + "::" + this.room.getX() + "," + this.room.getY() + "," + this.room.getZ() + "::" + this.hasUnpopulatedDirections() + "::" + String.valueOf(directions);
   }

   public boolean isUnique() {
      return this.unique;
   }

   public void setUnique(boolean unique) {
      this.unique = unique;
   }

   public ArrayList<BlockFace> getWalledFaces() {
      return this.walledFaces;
   }

   public void setWalledFaces(ArrayList<BlockFace> walledFaces) {
      this.walledFaces = walledFaces;
   }
}
