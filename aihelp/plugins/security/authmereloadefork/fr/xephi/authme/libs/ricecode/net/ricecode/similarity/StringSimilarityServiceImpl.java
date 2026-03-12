package fr.xephi.authme.libs.ricecode.net.ricecode.similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class StringSimilarityServiceImpl implements StringSimilarityService {
   private SimilarityStrategy strategy;

   public StringSimilarityServiceImpl(SimilarityStrategy strategy) {
      this.strategy = strategy;
   }

   public List<SimilarityScore> scoreAll(List<String> features, String target) {
      ArrayList<SimilarityScore> scores = new ArrayList();
      Iterator i$ = features.iterator();

      while(i$.hasNext()) {
         String feature = (String)i$.next();
         double score = this.strategy.score(feature, target);
         scores.add(new SimilarityScore(feature, score));
      }

      return scores;
   }

   public double score(String feature, String target) {
      return this.strategy.score(feature, target);
   }

   public SimilarityScore findTop(List<String> features, String target) {
      return this.findTop(features, target, new DescendingSimilarityScoreComparator());
   }

   public SimilarityScore findTop(List<String> features, String target, Comparator<SimilarityScore> comparator) {
      if (features.size() == 0) {
         return null;
      } else {
         List<SimilarityScore> scores = this.scoreAll(features, target);
         Collections.sort(scores, comparator);
         return (SimilarityScore)scores.get(0);
      }
   }
}
