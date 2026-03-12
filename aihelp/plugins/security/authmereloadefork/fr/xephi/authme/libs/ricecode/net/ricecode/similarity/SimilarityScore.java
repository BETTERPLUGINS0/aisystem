package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

public class SimilarityScore {
   private String key;
   private double score;

   public SimilarityScore(String key, double score) {
      this.key = key;
      this.score = score;
   }

   public String getKey() {
      return this.key;
   }

   public double getScore() {
      return this.score;
   }

   public int hashCode() {
      int hash = 11;
      int hash = 23 * hash + this.key.hashCode();
      hash = 23 * hash + (int)(this.score * 1000000.0D);
      return hash;
   }

   public boolean equals(Object o) {
      if (o != null && o.getClass() == this.getClass()) {
         SimilarityScore other = (SimilarityScore)o;
         return this.key.equals(other.key) && this.score == other.score;
      } else {
         return false;
      }
   }
}
