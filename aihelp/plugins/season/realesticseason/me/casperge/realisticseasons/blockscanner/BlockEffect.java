package me.casperge.realisticseasons.blockscanner;

public class BlockEffect {
   private int range;
   private int modifier;

   public BlockEffect(int var1, int var2) {
      this.range = var1;
      this.modifier = var2;
   }

   public int getRange() {
      return this.range;
   }

   public int getModifier() {
      return this.modifier;
   }
}
