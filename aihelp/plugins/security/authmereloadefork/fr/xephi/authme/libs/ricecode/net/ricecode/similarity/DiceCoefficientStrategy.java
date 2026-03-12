package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class DiceCoefficientStrategy implements SimilarityStrategy {
   public double score(String first, String second) {
      Set<String> s1 = this.splitIntoBigrams(first);
      Set<String> s2 = this.splitIntoBigrams(second);
      int n1 = s1.size();
      int n2 = s2.size();
      s1.retainAll(s2);
      int nt = s1.size();
      return 2.0D * (double)nt / (double)(n1 + n2);
   }

   private Set<String> splitIntoBigrams(String s) {
      ArrayList<String> bigrams = new ArrayList();
      if (s.length() < 2) {
         bigrams.add(s);
      } else {
         for(int i = 1; i < s.length(); ++i) {
            StringBuilder sb = new StringBuilder();
            sb.append(s.charAt(i - 1));
            sb.append(s.charAt(i));
            bigrams.add(sb.toString());
         }
      }

      return new TreeSet(bigrams);
   }
}
