package fr.xephi.authme.data.limbo.persistence;

public enum SegmentSize {
   ONE(1, 1),
   FOUR(4, 1),
   EIGHT(8, 1),
   SIXTEEN(16, 1),
   THIRTY_TWO(2, 5),
   SIXTY_FOUR(4, 3),
   ONE_TWENTY(2, 7),
   TWO_FIFTY(16, 2);

   private final int distribution;
   private final int length;

   private SegmentSize(int param3, int param4) {
      this.distribution = distribution;
      this.length = length;
   }

   public int getDistribution() {
      return this.distribution;
   }

   public int getLength() {
      return this.length;
   }

   public int getTotalSegments() {
      return (int)Math.pow((double)this.distribution, (double)this.length);
   }

   // $FF: synthetic method
   private static SegmentSize[] $values() {
      return new SegmentSize[]{ONE, FOUR, EIGHT, SIXTEEN, THIRTY_TWO, SIXTY_FOUR, ONE_TWENTY, TWO_FIFTY};
   }
}
