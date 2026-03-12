package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

import java.util.Comparator;
import java.util.List;

public interface StringSimilarityService {
   List<SimilarityScore> scoreAll(List<String> var1, String var2);

   double score(String var1, String var2);

   SimilarityScore findTop(List<String> var1, String var2);

   SimilarityScore findTop(List<String> var1, String var2, Comparator<SimilarityScore> var3);
}
