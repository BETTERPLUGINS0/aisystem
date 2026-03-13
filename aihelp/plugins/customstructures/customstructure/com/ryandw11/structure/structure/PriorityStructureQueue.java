package com.ryandw11.structure.structure;

import com.ryandw11.structure.structure.properties.StructureYSpawning;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PriorityStructureQueue {
   private final TreeSet<PriorityStructureQueue.PriorityStructure> priorityStructureSet = new TreeSet();

   public PriorityStructureQueue(@NotNull List<Structure> structures, @NotNull Block block, @NotNull Chunk chunk) {
      Iterator var4 = structures.iterator();

      while(var4.hasNext()) {
         Structure structure = (Structure)var4.next();
         StructureYSpawning structureSpawnSettings = structure.getStructureLocation().getSpawnSettings();
         Block structureBlock = structureSpawnSettings.getHighestBlock(block.getLocation());
         if (structureBlock.getType() == Material.VOID_AIR) {
            structureBlock = null;
         }

         if (structure.canSpawn(structureBlock, chunk)) {
            this.priorityStructureSet.add(new PriorityStructureQueue.PriorityStructure(structure));
         }
      }

   }

   public boolean hasNextStructure() {
      return !this.priorityStructureSet.isEmpty();
   }

   @Nullable
   public Structure getNextStructure() {
      return this.priorityStructureSet.isEmpty() ? null : ((PriorityStructureQueue.PriorityStructure)Objects.requireNonNull((PriorityStructureQueue.PriorityStructure)this.priorityStructureSet.pollFirst())).getStructure();
   }

   private static class PriorityStructure implements Comparable<PriorityStructureQueue.PriorityStructure> {
      private final Structure structure;
      private final double probability;

      public PriorityStructure(@NotNull Structure structure) {
         this.structure = structure;
         this.probability = (double)structure.getProbabilityNumerator() / (double)structure.getProbabilityDenominator();
      }

      @NotNull
      public Structure getStructure() {
         return this.structure;
      }

      public int compareTo(@NotNull PriorityStructureQueue.PriorityStructure pStructure) {
         if (this.structure.getPriority() == pStructure.structure.getPriority()) {
            return this.probability < pStructure.probability ? -1 : 1;
         } else {
            return this.structure.getPriority() < pStructure.structure.getPriority() ? -1 : 1;
         }
      }
   }
}
