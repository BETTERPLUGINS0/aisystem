package ac.grim.grimac.shaded.maps;

import java.util.Iterator;

public class LiteJoiner {
   private final String separator;

   public static LiteJoiner on(String separator) {
      return new LiteJoiner(separator);
   }

   private LiteJoiner(String separator) {
      this.separator = separator;
   }

   public String join(Iterable<?> parts) {
      StringBuilder joined = new StringBuilder();
      Iterator partIterator = parts.iterator();

      while(partIterator.hasNext()) {
         joined.append(partIterator.next());
         if (partIterator.hasNext()) {
            joined.append(this.separator);
         }
      }

      return joined.toString();
   }
}
