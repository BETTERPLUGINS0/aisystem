package org.apache.commons.io.input;

import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class SequenceReader extends Reader {
   private Reader reader;
   private Iterator<? extends Reader> readers;

   public SequenceReader(Iterable<? extends Reader> var1) {
      this.readers = ((Iterable)Objects.requireNonNull(var1, "readers")).iterator();
      this.reader = this.nextReader();
   }

   public SequenceReader(Reader... var1) {
      this((Iterable)Arrays.asList(var1));
   }

   public void close() {
      this.readers = null;
      this.reader = null;
   }

   private Reader nextReader() {
      return this.readers.hasNext() ? (Reader)this.readers.next() : null;
   }

   public int read() {
      int var1;
      for(var1 = -1; this.reader != null; this.reader = this.nextReader()) {
         var1 = this.reader.read();
         if (var1 != -1) {
            break;
         }
      }

      return var1;
   }

   public int read(char[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "cbuf");
      if (var3 >= 0 && var2 >= 0 && var2 + var3 <= var1.length) {
         int var4 = 0;

         while(this.reader != null) {
            int var5 = this.reader.read(var1, var2, var3);
            if (var5 == -1) {
               this.reader = this.nextReader();
            } else {
               var4 += var5;
               var2 += var5;
               var3 -= var5;
               if (var3 <= 0) {
                  break;
               }
            }
         }

         return var4 > 0 ? var4 : -1;
      } else {
         throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
      }
   }
}
