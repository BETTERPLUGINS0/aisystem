package org.apache.commons.lang3.text.translate;

import java.io.Writer;

/** @deprecated */
@Deprecated
public class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
   public boolean translate(int var1, Writer var2) {
      return var1 >= 55296 && var1 <= 57343;
   }
}
