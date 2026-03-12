package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

public class JaroStrategy implements SimilarityStrategy {
   public double score(String first, String second) {
      String shorter;
      String longer;
      if (first.length() > second.length()) {
         longer = first.toLowerCase();
         shorter = second.toLowerCase();
      } else {
         longer = second.toLowerCase();
         shorter = first.toLowerCase();
      }

      int halflength = shorter.length() / 2 + 1;
      String m1 = this.getSetOfMatchingCharacterWithin(shorter, longer, halflength);
      String m2 = this.getSetOfMatchingCharacterWithin(longer, shorter, halflength);
      if (m1.length() != 0 && m2.length() != 0) {
         if (m1.length() != m2.length()) {
            return 0.0D;
         } else {
            int transpositions = this.transpositions(m1, m2);
            double dist = ((double)m1.length() / (double)shorter.length() + (double)m2.length() / (double)longer.length() + (double)(m1.length() - transpositions) / (double)m1.length()) / 3.0D;
            return dist;
         }
      } else {
         return 0.0D;
      }
   }

   private String getSetOfMatchingCharacterWithin(String first, String second, int limit) {
      StringBuilder common = new StringBuilder();
      StringBuilder copy = new StringBuilder(second);

      for(int i = 0; i < first.length(); ++i) {
         char ch = first.charAt(i);
         boolean found = false;

         for(int j = Math.max(0, i - limit); !found && j < Math.min(i + limit, second.length()); ++j) {
            if (copy.charAt(j) == ch) {
               found = true;
               common.append(ch);
               copy.setCharAt(j, '*');
            }
         }
      }

      return common.toString();
   }

   private int transpositions(String first, String second) {
      int transpositions = 0;

      for(int i = 0; i < first.length(); ++i) {
         if (first.charAt(i) != second.charAt(i)) {
            ++transpositions;
         }
      }

      transpositions /= 2;
      return transpositions;
   }
}
