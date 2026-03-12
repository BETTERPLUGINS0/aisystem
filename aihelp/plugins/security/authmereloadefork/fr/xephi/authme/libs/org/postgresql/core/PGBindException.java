package fr.xephi.authme.libs.org.postgresql.core;

import java.io.IOException;

public class PGBindException extends IOException {
   private final IOException ioe;

   public PGBindException(IOException ioe) {
      this.ioe = ioe;
   }

   public IOException getIOException() {
      return this.ioe;
   }
}
