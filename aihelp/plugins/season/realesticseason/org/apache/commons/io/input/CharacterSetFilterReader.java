package org.apache.commons.io.input;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public class CharacterSetFilterReader extends AbstractCharacterFilterReader {
   private static IntPredicate toIntPredicate(Set<Integer> var0) {
      if (var0 == null) {
         return SKIP_NONE;
      } else {
         Set var1 = Collections.unmodifiableSet(var0);
         return (var1x) -> {
            return var1.contains(var1x);
         };
      }
   }

   public CharacterSetFilterReader(Reader var1, Integer... var2) {
      this(var1, (Set)(new HashSet(Arrays.asList(var2))));
   }

   public CharacterSetFilterReader(Reader var1, Set<Integer> var2) {
      super(var1, toIntPredicate(var2));
   }
}
