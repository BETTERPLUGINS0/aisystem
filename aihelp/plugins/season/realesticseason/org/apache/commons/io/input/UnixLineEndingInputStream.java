package org.apache.commons.io.input;

import java.io.InputStream;

public class UnixLineEndingInputStream extends InputStream {
   private boolean slashNSeen;
   private boolean slashRSeen;
   private boolean eofSeen;
   private final InputStream target;
   private final boolean ensureLineFeedAtEndOfFile;

   public UnixLineEndingInputStream(InputStream var1, boolean var2) {
      this.target = var1;
      this.ensureLineFeedAtEndOfFile = var2;
   }

   private int readWithUpdate() {
      int var1 = this.target.read();
      this.eofSeen = var1 == -1;
      if (this.eofSeen) {
         return var1;
      } else {
         this.slashNSeen = var1 == 10;
         this.slashRSeen = var1 == 13;
         return var1;
      }
   }

   public int read() {
      boolean var1 = this.slashRSeen;
      if (this.eofSeen) {
         return this.eofGame(var1);
      } else {
         int var2 = this.readWithUpdate();
         if (this.eofSeen) {
            return this.eofGame(var1);
         } else if (this.slashRSeen) {
            return 10;
         } else {
            return var1 && this.slashNSeen ? this.read() : var2;
         }
      }
   }

   private int eofGame(boolean var1) {
      if (!var1 && this.ensureLineFeedAtEndOfFile) {
         if (!this.slashNSeen) {
            this.slashNSeen = true;
            return 10;
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public void close() {
      super.close();
      this.target.close();
   }

   public synchronized void mark(int var1) {
      throw UnsupportedOperationExceptions.mark();
   }
}
