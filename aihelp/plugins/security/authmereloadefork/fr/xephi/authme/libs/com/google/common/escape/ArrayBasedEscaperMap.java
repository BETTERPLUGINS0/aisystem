package fr.xephi.authme.libs.com.google.common.escape;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class ArrayBasedEscaperMap {
   private final char[][] replacementArray;
   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];

   public static ArrayBasedEscaperMap create(Map<Character, String> replacements) {
      return new ArrayBasedEscaperMap(createReplacementArray(replacements));
   }

   private ArrayBasedEscaperMap(char[][] replacementArray) {
      this.replacementArray = replacementArray;
   }

   char[][] getReplacementArray() {
      return this.replacementArray;
   }

   @VisibleForTesting
   static char[][] createReplacementArray(Map<Character, String> map) {
      Preconditions.checkNotNull(map);
      if (map.isEmpty()) {
         return EMPTY_REPLACEMENT_ARRAY;
      } else {
         char max = (Character)Collections.max(map.keySet());
         char[][] replacements = new char[max + 1][];

         Character c;
         for(Iterator var3 = map.keySet().iterator(); var3.hasNext(); replacements[c] = ((String)map.get(c)).toCharArray()) {
            c = (Character)var3.next();
         }

         return replacements;
      }
   }
}
