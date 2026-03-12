package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

public class JaroWinklerStrategy extends JaroStrategy implements SimilarityStrategy {
   final double DEFAULT_SCALING_FACTOR = 0.1D;
   private double scalingFactor;

   public JaroWinklerStrategy(double scalingFactor) {
      if (scalingFactor > 0.25D) {
         scalingFactor = 0.25D;
      }

      this.scalingFactor = scalingFactor;
   }

   public JaroWinklerStrategy() {
      this.scalingFactor = 0.1D;
   }

   public double score(String first, String second) {
      double jaro = super.score(first, second);
      int cl = this.commonPrefixLength(first, second);
      double winkler = jaro + this.scalingFactor * (double)cl * (1.0D - jaro);
      return winkler;
   }

   private int commonPrefixLength(String first, String second) {
      String shorter;
      String longer;
      if (first.length() > second.length()) {
         longer = first.toLowerCase();
         shorter = second.toLowerCase();
      } else {
         longer = second.toLowerCase();
         shorter = first.toLowerCase();
      }

      int result = 0;

      for(int i = 0; i < shorter.length() && shorter.charAt(i) == longer.charAt(i); ++i) {
         ++result;
      }

      return result > 4 ? 4 : result;
   }
}
