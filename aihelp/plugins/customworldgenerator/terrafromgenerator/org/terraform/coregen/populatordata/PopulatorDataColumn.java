package org.terraform.coregen.populatordata;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.TerraformWorld;

public class PopulatorDataColumn extends PopulatorDataAbstract {
   private final PopulatorDataAbstract delegate;
   int constrainX = 0;
   int constrainZ = 0;

   public PopulatorDataColumn(PopulatorDataAbstract delegate) {
      this.delegate = delegate;
   }

   public void setConstraints(int constrainX, int constrainZ) {
      this.constrainX = constrainX;
      this.constrainZ = constrainZ;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      if (x == this.constrainX && z == this.constrainZ) {
         return this.delegate.getType(x, y, z);
      } else {
         throw new IllegalArgumentException("Column Constraint Read Violation");
      }
   }

   public BlockData getBlockData(int x, int y, int z) {
      if (x == this.constrainX && z == this.constrainZ) {
         return this.delegate.getBlockData(x, y, z);
      } else {
         throw new IllegalArgumentException("Column Constraint Read Violation");
      }
   }

   public void setType(int x, int y, int z, Material type) {
      if (x == this.constrainX && z == this.constrainZ) {
         this.delegate.setType(x, y, z, type);
      } else {
         throw new IllegalArgumentException("Column Constraint Write Violation");
      }
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      if (x == this.constrainX && z == this.constrainZ) {
         this.delegate.setBlockData(x, y, z, data);
      } else {
         throw new IllegalArgumentException("Column Constraint Write Violation");
      }
   }

   public Biome getBiome(int rawX, int rawZ) {
      return this.delegate.getBiome(rawX, rawZ);
   }

   public void addEntity(int rawX, int rawY, int rawZ, EntityType type) {
      if (rawX == this.constrainX && rawZ == this.constrainZ) {
         this.delegate.addEntity(rawX, rawY, rawZ, type);
      } else {
         throw new IllegalArgumentException("Column Constraint Write Violation");
      }
   }

   public int getChunkX() {
      return this.delegate.getChunkX();
   }

   public int getChunkZ() {
      return this.delegate.getChunkZ();
   }

   public void setSpawner(int rawX, int rawY, int rawZ, EntityType type) {
      if (rawX == this.constrainX && rawZ == this.constrainZ) {
         this.delegate.setSpawner(rawX, rawY, rawZ, type);
      } else {
         throw new IllegalArgumentException("Column Constraint Write Violation");
      }
   }

   public void lootTableChest(int x, int y, int z, TerraLootTable table) {
      if (x == this.constrainX && z == this.constrainZ) {
         this.delegate.lootTableChest(x, y, z, table);
      } else {
         throw new IllegalArgumentException("Column Constraint Write Violation");
      }
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.delegate.getTerraformWorld();
   }
}
