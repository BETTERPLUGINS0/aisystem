package me.casperge.realisticseasons.api.landplugins;

public enum Priority {
   LOWEST(0),
   LOW(1),
   MEDIUM(2),
   HIGH(3),
   HIGHEST(4);

   int val;

   private Priority(int param3) {
      this.val = var3;
   }

   public int intValue() {
      return this.val;
   }

   public boolean isHigherThan(Priority var1) {
      return this.val > var1.intValue();
   }

   // $FF: synthetic method
   private static Priority[] $values() {
      return new Priority[]{LOWEST, LOW, MEDIUM, HIGH, HIGHEST};
   }
}
