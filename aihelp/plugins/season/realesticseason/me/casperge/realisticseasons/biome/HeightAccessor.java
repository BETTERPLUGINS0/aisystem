package me.casperge.realisticseasons.biome;

import java.util.HashMap;

public class HeightAccessor {
   private HashMap<Integer, Integer> heights = new HashMap();

   public Integer getSectionYFromSectionIndex(Integer var1) {
      return (Integer)this.heights.get(var1);
   }

   public void add(Integer var1, Integer var2) {
      this.heights.put(var1, var2);
   }
}
