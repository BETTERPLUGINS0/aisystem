package org.apache.commons.io;

import java.nio.charset.Charset;
import java.util.Objects;

public enum StandardLineSeparator {
   CR("\r"),
   CRLF("\r\n"),
   LF("\n");

   private final String lineSeparator;

   private StandardLineSeparator(final String param3) {
      this.lineSeparator = (String)Objects.requireNonNull(var3, "lineSeparator");
   }

   public byte[] getBytes(Charset var1) {
      return this.lineSeparator.getBytes(var1);
   }

   public String getString() {
      return this.lineSeparator;
   }

   // $FF: synthetic method
   private static StandardLineSeparator[] $values() {
      return new StandardLineSeparator[]{CR, CRLF, LF};
   }
}
