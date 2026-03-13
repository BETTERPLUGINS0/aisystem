package org.terraform.structure.pillager.mansion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public abstract class MansionStandardRoomPiece extends JigsawStructurePiece {
   public static int spawnedGuards = 0;
   @NotNull
   public HashMap<BlockFace, MansionStandardRoomPiece> adjacentPieces = new HashMap();
   @NotNull
   public HashMap<BlockFace, MansionInternalWallState> internalWalls = new HashMap();
   @Nullable
   private MansionRoomPopulator roomPopulator = null;
   private boolean isPopulating = false;

   public MansionStandardRoomPiece(int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
   }

   public void setupInternalAttributes(@NotNull PopulatorDataAbstract data, @NotNull HashMap<SimpleLocation, JigsawStructurePiece> pieces) {
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         SimpleLocation otherLoc = this.getRoom().getSimpleLocation().getRelative(face, 9);
         if (!pieces.containsKey(otherLoc)) {
            SimpleBlock center = this.getRoom().getCenterSimpleBlock(data).getUp();
            if (center.getRelative(face, 5).isSolid()) {
               this.internalWalls.put(face, MansionInternalWallState.WINDOW);
            } else {
               this.internalWalls.put(face, MansionInternalWallState.EXIT);
            }
         } else {
            this.adjacentPieces.put(face, (MansionStandardRoomPiece)pieces.get(otherLoc));
            this.internalWalls.put(face, MansionInternalWallState.SOLID);
         }
      }

   }

   public void buildWalls(Random random, @NotNull PopulatorDataAbstract data) {
      Iterator var3 = this.internalWalls.keySet().iterator();

      while(true) {
         BlockFace face;
         do {
            do {
               if (!var3.hasNext()) {
                  return;
               }

               face = (BlockFace)var3.next();
            } while(this.internalWalls.get(face) == MansionInternalWallState.WINDOW);
         } while(this.internalWalls.get(face) == MansionInternalWallState.EXIT);

         Entry<Wall, Integer> entry = this.getRoom().getWall(data, face, 0);
         Wall w = (Wall)entry.getKey();
         Wall center = null;

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.Pillar(this.getRoom().getHeight(), new Material[]{Material.DARK_OAK_PLANKS});
            if (i == (Integer)entry.getValue() / 2 && this.internalWalls.get(face) == MansionInternalWallState.ROOM_ENTRANCE) {
               center = w.clone();
            }

            w = w.getLeft();
         }

         if (center != null) {
            center.Pillar(5, new Material[]{Material.AIR});
            center.getLeft().Pillar(5, new Material[]{Material.AIR});
            center.getRight().Pillar(5, new Material[]{Material.AIR});
            center.getLeft(2).Pillar(5, new Material[]{Material.AIR});
            center.getRight(2).Pillar(5, new Material[]{Material.AIR});
            (new SlabBuilder(Material.DARK_OAK_SLAB)).setType(Type.TOP).apply(center.getUp(5));
            (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(center.getDirection())).apply(center.getUp(4).getLeft(2)).setFacing(BlockUtils.getRight(center.getDirection())).apply(center.getUp(4).getRight(2));
            center.getLeft(3).Pillar(this.getRoom().getHeight(), new Material[]{Material.DARK_OAK_LOG});
            center.getRight(3).Pillar(this.getRoom().getHeight(), new Material[]{Material.DARK_OAK_LOG});
         }
      }
   }

   public void decorateInternalRoom(Random random, PopulatorDataAbstract data) {
      if (this.roomPopulator != null && this.isPopulating) {
         this.roomPopulator.decorateRoom(data, random);
      }

   }

   public boolean areInternalWallsFullyBlocked() {
      Iterator var1 = this.internalWalls.keySet().iterator();

      BlockFace face;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         face = (BlockFace)var1.next();
      } while(this.internalWalls.get(face) != MansionInternalWallState.ROOM_ENTRANCE);

      return false;
   }

   @Nullable
   public JigsawStructurePiece getInstance(@NotNull Random rand, int depth) {
      MansionStandardRoomPiece clone = (MansionStandardRoomPiece)super.getInstance(rand, depth);
      if (clone == null) {
         return null;
      } else {
         clone.adjacentPieces = new HashMap();
         clone.internalWalls = new HashMap();
         return clone;
      }
   }

   @NotNull
   public Collection<BlockFace> getShuffledInternalWalls() {
      ArrayList<BlockFace> shuffled = new ArrayList(this.internalWalls.keySet());
      Collections.shuffle(shuffled);
      return shuffled;
   }

   @Nullable
   public MansionRoomPopulator getRoomPopulator() {
      return this.roomPopulator;
   }

   public void setRoomPopulator(MansionRoomPopulator roomPopulator) {
      this.setRoomPopulator(roomPopulator, true);
   }

   public void setRoomPopulator(MansionRoomPopulator roomPopulator, boolean isPopulating) {
      this.roomPopulator = roomPopulator;
      this.isPopulating = isPopulating;
   }

   public void decorateWalls(Random random, @NotNull PopulatorDataAbstract data) {
      if (this.roomPopulator != null) {
         BlockFace[] var3 = BlockUtils.directBlockFaces;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];
            if (this.internalWalls.containsKey(face)) {
               Wall target;
               switch((MansionInternalWallState)this.internalWalls.get(face)) {
               case EXIT:
                  target = new Wall(this.getRoom().getCenterSimpleBlock(data).getUp(), face.getOppositeFace());
                  this.roomPopulator.decorateExit(random, target.getRear(4));
                  break;
               case ROOM_ENTRANCE:
                  target = new Wall(this.getRoom().getCenterSimpleBlock(data).getUp(), face.getOppositeFace());
                  this.roomPopulator.decorateEntrance(random, target.getRear(3));
                  break;
               case SOLID:
                  target = new Wall(this.getRoom().getCenterSimpleBlock(data).getUp(), face.getOppositeFace());
                  this.roomPopulator.decorateWall(random, target.getRear(3));
                  break;
               case WINDOW:
                  target = new Wall(this.getRoom().getCenterSimpleBlock(data).getUp(), face.getOppositeFace());
                  this.roomPopulator.decorateWindow(random, target.getRear(4));
               }
            }
         }

      }
   }

   public boolean isPopulating() {
      return this.isPopulating;
   }

   public void spawnGuards(@NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      if (this.roomPopulator != null) {
         EntityType type = EntityType.VINDICATOR;
         int[] spawnLoc = this.roomPopulator.getSpawnLocation();
         if (this.isPopulating && (this.roomPopulator.getSize().equals(new MansionRoomSize(3, 3)) || this.roomPopulator.getSize().equals(new MansionRoomSize(2, 2)))) {
            type = EntityType.EVOKER;
         }

         if (!this.roomPopulator.getSize().equals(new MansionRoomSize(1, 1)) || !GenUtils.chance(rand, 4, 5)) {
            SimpleBlock target = new SimpleBlock(data, spawnLoc[0], spawnLoc[1], spawnLoc[2]);
            int limit = 5;

            for(BlockFace dir = BlockUtils.getDirectBlockFace(rand); limit > 0 && (target.getType() != Material.AIR && target.getType() != Material.RED_CARPET || target.getUp().getType() != Material.AIR); --limit) {
               target = target.getRelative(dir).getUp();
            }

            if (limit > 0) {
               target.addEntity(type);
               ++spawnedGuards;
               if (!this.roomPopulator.getSize().equals(new MansionRoomSize(1, 1))) {
                  for(int i = 0; i < TConfig.c.STRUCTURES_MANSION_SPAWNAGGRESSION; ++i) {
                     if (rand.nextBoolean()) {
                        target.addEntity(EntityType.VINDICATOR);
                        ++spawnedGuards;
                     }
                  }
               }
            }

         }
      }
   }
}
