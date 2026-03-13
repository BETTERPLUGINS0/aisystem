package org.terraform.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;

public class MazeSpawner {
   @NotNull
   public final List<PathPopulatorData> pathPopDatas = new ArrayList();
   private final Map<SimpleLocation, MazeSpawner.MazeCell> cellGrid = new HashMap();
   private SimpleBlock core;
   private int widthX = -1;
   private int widthZ = -1;
   private Random rand;
   private int mazeHeight = 3;
   private MazeSpawner.MazeCell center;
   private int mazePathWidth = 1;
   private int mazePeriod = 1;
   private PathPopulatorAbstract pathPop;
   private boolean covered = false;

   public MazeSpawner() {
   }

   public MazeSpawner(Random rand, SimpleBlock core, int widthX, int widthZ) {
      this.rand = rand;
      this.core = core;
      this.widthX = widthX;
      this.widthZ = widthZ;
   }

   @NotNull
   private Map<BlockFace, MazeSpawner.MazeCell> getValidNeighbours(@NotNull MazeSpawner.MazeCell target) {
      Map<BlockFace, MazeSpawner.MazeCell> neighbours = new EnumMap(BlockFace.class);
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         MazeSpawner.MazeCell neighbour = this.getAdjacentCell(target, face);
         if (neighbour != null && neighbour.hasAllWalls()) {
            neighbours.put(face, neighbour);
         }
      }

      return neighbours;
   }

   public void prepareMaze() {
      int mazeCellsWidthX = this.widthX / (this.mazePathWidth + this.mazePeriod);
      int mazeCellsWidthZ = this.widthZ / (this.mazePathWidth + this.mazePeriod);

      int x;
      MazeSpawner.MazeCell currentCell;
      for(x = -mazeCellsWidthX / 2; x <= mazeCellsWidthX / 2; ++x) {
         for(int z = -mazeCellsWidthZ / 2; z <= mazeCellsWidthZ / 2; ++z) {
            currentCell = new MazeSpawner.MazeCell(x, z);
            this.cellGrid.put(new SimpleLocation(x, this.core.getY(), z), currentCell);
            if (x == 0 && z == 0) {
               this.center = currentCell;
            }
         }
      }

      x = mazeCellsWidthX * mazeCellsWidthZ;
      Stack<MazeSpawner.MazeCell> cellStack = new Stack();
      currentCell = this.center;
      int nv = 1;

      while(nv < x) {
         Map<BlockFace, MazeSpawner.MazeCell> neighbours = this.getValidNeighbours(currentCell);
         if (neighbours.isEmpty()) {
            if (cellStack.isEmpty()) {
               break;
            }

            currentCell = (MazeSpawner.MazeCell)cellStack.pop();
         } else {
            Entry<BlockFace, MazeSpawner.MazeCell> entry = (Entry)neighbours.entrySet().toArray()[this.rand.nextInt(neighbours.size())];
            currentCell.knockDownWall((MazeSpawner.MazeCell)entry.getValue(), (BlockFace)entry.getKey());
            cellStack.push(currentCell);
            currentCell = (MazeSpawner.MazeCell)entry.getValue();
            ++nv;
         }
      }

   }

   public void carveMaze(boolean carveInSolid, Material... materials) {
      int cellRadius = (this.mazePathWidth - 1) / 2;
      Iterator var4 = this.cellGrid.values().iterator();

      while(var4.hasNext()) {
         MazeSpawner.MazeCell cell = (MazeSpawner.MazeCell)var4.next();
         int realWorldX = cell.x * (this.mazePathWidth + this.mazePeriod);
         int realWorldZ = cell.z * (this.mazePathWidth + this.mazePeriod);
         Wall cellCore = new Wall(this.core.getRelative(realWorldX, 0, realWorldZ));
         this.pathPopDatas.add(new PathPopulatorData(cellCore.getDown().get(), BlockFace.UP, this.mazePathWidth, false));

         for(int nx = -cellRadius; nx <= cellRadius; ++nx) {
            for(int nz = -cellRadius; nz <= cellRadius; ++nz) {
               cellCore.getRelative(nx, 0, nz).Pillar(this.mazeHeight, this.rand, new Material[]{Material.CAVE_AIR});
               if (this.covered) {
                  cellCore.getRelative(nx, this.mazeHeight, nz).setType((Material)GenUtils.randChoice((Object[])materials));
                  cellCore.getRelative(nx, -1, nz).setType((Material)GenUtils.randChoice((Object[])materials));
               }
            }
         }

         Set<BlockFace> wallllllllless = cell.getWalllessFaces();
         BlockFace[] var18 = BlockUtils.directBlockFaces;
         int var11 = var18.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            BlockFace dir = var18[var12];
            Wall startPoint = (new Wall(this.core.getRelative(realWorldX, 0, realWorldZ), dir)).getRelative(dir, cellRadius + 1);
            int w;
            if (!wallllllllless.contains(dir)) {
               startPoint.Pillar(this.mazeHeight, this.rand, materials);

               for(w = 1; w <= cellRadius; ++w) {
                  startPoint.getLeft(w).Pillar(this.mazeHeight, this.rand, materials);
                  startPoint.getRight(w).Pillar(this.mazeHeight, this.rand, materials);
               }
            } else {
               for(w = 0; (double)w < Math.ceil((double)((float)this.mazePeriod / 2.0F)); ++w) {
                  this.pathPopDatas.add(new PathPopulatorData(startPoint.getDown().get(), dir, this.mazePathWidth, false));
                  startPoint.Pillar(this.mazeHeight, this.rand, new Material[]{Material.CAVE_AIR});
                  if (this.covered) {
                     startPoint.getRelative(0, this.mazeHeight, 0).setType((Material)GenUtils.randChoice((Object[])materials));
                     startPoint.getDown().setType((Material)GenUtils.randChoice((Object[])materials));
                  }

                  for(int w = 1; w <= cellRadius; ++w) {
                     startPoint.getLeft(w).Pillar(this.mazeHeight, this.rand, new Material[]{Material.CAVE_AIR});
                     startPoint.getRight(w).Pillar(this.mazeHeight, this.rand, new Material[]{Material.CAVE_AIR});
                     if (this.covered) {
                        startPoint.getLeft(w).getDown().setType((Material)GenUtils.randChoice((Object[])materials));
                        startPoint.getRight(w).getDown().setType((Material)GenUtils.randChoice((Object[])materials));
                        startPoint.getLeft(w).getRelative(0, this.mazeHeight, 0).setType((Material)GenUtils.randChoice((Object[])materials));
                        startPoint.getRight(w).getRelative(0, this.mazeHeight, 0).setType((Material)GenUtils.randChoice((Object[])materials));
                     }
                  }

                  startPoint.getLeft(cellRadius + 1).Pillar(this.mazeHeight, this.rand, materials);
                  startPoint.getRight(cellRadius + 1).Pillar(this.mazeHeight, this.rand, materials);
                  startPoint = startPoint.getRelative(dir);
               }
            }
         }
      }

   }

   private MazeSpawner.MazeCell getAdjacentCell(@NotNull MazeSpawner.MazeCell target, @NotNull BlockFace face) {
      int neighbourX = target.x + face.getModX();
      int neighbourZ = target.z + face.getModZ();
      return (MazeSpawner.MazeCell)this.cellGrid.get(new SimpleLocation(neighbourX, this.core.getY(), neighbourZ));
   }

   public int getMazeHeight() {
      return this.mazeHeight;
   }

   public void setMazeHeight(int mazeHeight) {
      this.mazeHeight = mazeHeight;
   }

   public int getWidthX() {
      return this.widthX;
   }

   public void setWidthX(int widthX) {
      this.widthX = widthX;
   }

   public int getWidthZ() {
      return this.widthZ;
   }

   public void setWidthZ(int widthZ) {
      this.widthZ = widthZ;
   }

   public void setWidth(int width) {
      this.widthX = width;
      this.widthZ = width;
   }

   public Random getRand() {
      return this.rand;
   }

   public void setRand(Random rand) {
      this.rand = rand;
   }

   public SimpleBlock getCore() {
      return this.core;
   }

   public void setCore(SimpleBlock core) {
      this.core = core;
   }

   public int getMazePathWidth() {
      return this.mazePathWidth;
   }

   public void setMazePathWidth(int mazePathWidth) {
      if (mazePathWidth % 2 == 0) {
         throw new IllegalArgumentException("Maze Path Width must be odd!");
      } else {
         this.mazePathWidth = mazePathWidth;
      }
   }

   public int getMazePeriod() {
      return this.mazePeriod;
   }

   public void setMazePeriod(int mazePeriod) {
      this.mazePeriod = mazePeriod;
   }

   public PathPopulatorAbstract getPathPop() {
      return this.pathPop;
   }

   public void setPathPop(PathPopulatorAbstract pathPop) {
      this.pathPop = pathPop;
   }

   public boolean isCovered() {
      return this.covered;
   }

   public void setCovered(boolean covered) {
      this.covered = covered;
   }

   private static class MazeCell {
      protected final int x;
      protected final int z;
      @NotNull
      protected final Map<BlockFace, Boolean> walls = new EnumMap(BlockFace.class);

      public MazeCell(int x, int z) {
         this.x = x;
         this.z = z;
         this.walls.put(BlockFace.NORTH, true);
         this.walls.put(BlockFace.SOUTH, true);
         this.walls.put(BlockFace.EAST, true);
         this.walls.put(BlockFace.WEST, true);
      }

      @NotNull
      public Set<BlockFace> getWalllessFaces() {
         Set<BlockFace> faces = EnumSet.noneOf(BlockFace.class);
         Iterator var2 = this.walls.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<BlockFace, Boolean> entry = (Entry)var2.next();
            if (!(Boolean)entry.getValue()) {
               faces.add((BlockFace)entry.getKey());
            }
         }

         return faces;
      }

      public boolean hasAllWalls() {
         Iterator var1 = this.walls.values().iterator();

         Boolean bool;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            bool = (Boolean)var1.next();
         } while(bool);

         return false;
      }

      public void knockDownWall(@NotNull MazeSpawner.MazeCell other, @NotNull BlockFace side) {
         this.walls.put(side, false);
         other.walls.put(side.getOppositeFace(), false);
      }
   }
}
