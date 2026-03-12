package fr.xephi.authme.libs.com.icetar.tar;

import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class TarEntryEnumerator implements Enumeration {
   private TarInputStream tis = null;
   private boolean eof = false;
   private TarEntry readAhead = null;

   public TarEntryEnumerator(TarInputStream var1) {
      this.tis = var1;
      this.eof = false;
   }

   public Object nextElement() throws NoSuchElementException {
      if (this.eof && this.readAhead == null) {
         throw new NoSuchElementException();
      } else {
         TarEntry var1 = null;
         if (this.readAhead != null) {
            var1 = this.readAhead;
            this.readAhead = null;
         } else {
            var1 = this.getNext();
         }

         return var1;
      }
   }

   public boolean hasMoreElements() {
      if (this.eof) {
         return false;
      } else {
         boolean var1 = false;
         this.readAhead = this.getNext();
         if (this.readAhead != null) {
            var1 = true;
         }

         return var1;
      }
   }

   private TarEntry getNext() {
      TarEntry var1 = null;

      try {
         var1 = this.tis.getNextEntry();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      if (var1 == null) {
         this.eof = true;
      }

      return var1;
   }
}
