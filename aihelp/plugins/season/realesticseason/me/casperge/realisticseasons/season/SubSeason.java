package me.casperge.realisticseasons.season;

public enum SubSeason {
   START(0),
   EARLY(1),
   MIDDLE(2),
   LATE(3),
   END(4);

   int phase;

   private SubSeason(int param3) {
      this.phase = var3;
   }

   public int getPhase() {
      return this.phase;
   }

   // $FF: synthetic method
   private static SubSeason[] $values() {
      return new SubSeason[]{START, EARLY, MIDDLE, LATE, END};
   }
}
