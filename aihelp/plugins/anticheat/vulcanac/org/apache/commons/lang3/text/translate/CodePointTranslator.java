package org.apache.commons.lang3.text.translate;

import java.io.Writer;

/** @deprecated */
@Deprecated
public abstract class CodePointTranslator extends CharSequenceTranslator {
   public final int translate(CharSequence var1, int var2, Writer var3) {
      int var4 = Character.codePointAt(var1, var2);
      boolean var5 = this.translate(var4, var3);
      return var5 ? 1 : 0;
   }

   public abstract boolean translate(int var1, Writer var2);
}
