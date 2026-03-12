package fr.xephi.authme.util;

import fr.xephi.authme.libs.ricecode.net.ricecode.similarity.LevenshteinDistanceStrategy;
import fr.xephi.authme.libs.ricecode.net.ricecode.similarity.StringSimilarityService;
import fr.xephi.authme.libs.ricecode.net.ricecode.similarity.StringSimilarityServiceImpl;
import java.util.Iterator;

public final class StringUtils {
   private StringUtils() {
   }

   public static double getDifference(String first, String second) {
      if (first != null && second != null) {
         StringSimilarityService service = new StringSimilarityServiceImpl(new LevenshteinDistanceStrategy());
         return Math.abs(service.score(first, second) - 1.0D);
      } else {
         return 1.0D;
      }
   }

   public static boolean containsAny(String str, Iterable<String> pieces) {
      if (str == null) {
         return false;
      } else {
         Iterator var2 = pieces.iterator();

         String piece;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            piece = (String)var2.next();
         } while(piece == null || !str.contains(piece));

         return true;
      }
   }

   public static boolean isBlank(String str) {
      return str == null || str.trim().isEmpty();
   }

   public static boolean isInsideString(char needle, String haystack) {
      int index = haystack.indexOf(needle);
      return index > 0 && index < haystack.length() - 1;
   }
}
