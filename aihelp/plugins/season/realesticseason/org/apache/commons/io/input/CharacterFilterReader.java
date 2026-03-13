package org.apache.commons.io.input;

import java.io.Reader;
import java.util.function.IntPredicate;

public class CharacterFilterReader extends AbstractCharacterFilterReader {
   public CharacterFilterReader(Reader var1, int var2) {
      super(var1, (var1x) -> {
         return var1x == var2;
      });
   }

   public CharacterFilterReader(Reader var1, IntPredicate var2) {
      super(var1, var2);
   }
}
