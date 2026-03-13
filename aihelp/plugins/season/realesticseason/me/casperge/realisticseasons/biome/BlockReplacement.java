package me.casperge.realisticseasons.biome;

import java.util.List;
import org.bukkit.Material;

public class BlockReplacement {
   private List<Integer> seasons;
   private List<Integer> phases;
   private Material original;
   private Material replacent;

   public Material getReplacent() {
      return this.replacent;
   }

   public List<Integer> getPhases() {
      return this.phases;
   }

   public List<Integer> getSeasons() {
      return this.seasons;
   }

   public BlockReplacement(List<Integer> var1, List<Integer> var2, Material var3, Material var4) {
      this.seasons = var1;
      this.phases = var2;
      this.replacent = var4;
      this.original = var3;
   }

   public Material getOriginal() {
      return this.original;
   }
}
