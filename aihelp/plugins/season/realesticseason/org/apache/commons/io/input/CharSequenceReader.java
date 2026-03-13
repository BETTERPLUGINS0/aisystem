package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;
import java.util.Objects;

public class CharSequenceReader extends Reader implements Serializable {
   private static final long serialVersionUID = 3724187752191401220L;
   private final CharSequence charSequence;
   private int idx;
   private int mark;
   private final int start;
   private final Integer end;

   public CharSequenceReader(CharSequence var1) {
      this(var1, 0);
   }

   public CharSequenceReader(CharSequence var1, int var2) {
      this(var1, var2, Integer.MAX_VALUE);
   }

   public CharSequenceReader(CharSequence var1, int var2, int var3) {
      if (var2 < 0) {
         throw new IllegalArgumentException("Start index is less than zero: " + var2);
      } else if (var3 < var2) {
         throw new IllegalArgumentException("End index is less than start " + var2 + ": " + var3);
      } else {
         this.charSequence = (CharSequence)(var1 != null ? var1 : "");
         this.start = var2;
         this.end = var3;
         this.idx = var2;
         this.mark = var2;
      }
   }

   private int start() {
      return Math.min(this.charSequence.length(), this.start);
   }

   private int end() {
      return Math.min(this.charSequence.length(), this.end == null ? Integer.MAX_VALUE : this.end);
   }

   public void close() {
      this.idx = this.start;
      this.mark = this.start;
   }

   public boolean ready() {
      return this.idx < this.end();
   }

   public void mark(int var1) {
      this.mark = this.idx;
   }

   public boolean markSupported() {
      return true;
   }

   public int read() {
      return this.idx >= this.end() ? -1 : this.charSequence.charAt(this.idx++);
   }

   public int read(char[] var1, int var2, int var3) {
      if (this.idx >= this.end()) {
         return -1;
      } else {
         Objects.requireNonNull(var1, "array");
         if (var3 >= 0 && var2 >= 0 && var2 + var3 <= var1.length) {
            int var4;
            if (this.charSequence instanceof String) {
               var4 = Math.min(var3, this.end() - this.idx);
               ((String)this.charSequence).getChars(this.idx, this.idx + var4, var1, var2);
               this.idx += var4;
               return var4;
            } else if (this.charSequence instanceof StringBuilder) {
               var4 = Math.min(var3, this.end() - this.idx);
               ((StringBuilder)this.charSequence).getChars(this.idx, this.idx + var4, var1, var2);
               this.idx += var4;
               return var4;
            } else if (this.charSequence instanceof StringBuffer) {
               var4 = Math.min(var3, this.end() - this.idx);
               ((StringBuffer)this.charSequence).getChars(this.idx, this.idx + var4, var1, var2);
               this.idx += var4;
               return var4;
            } else {
               var4 = 0;

               for(int var5 = 0; var5 < var3; ++var5) {
                  int var6 = this.read();
                  if (var6 == -1) {
                     return var4;
                  }

                  var1[var2 + var5] = (char)var6;
                  ++var4;
               }

               return var4;
            }
         } else {
            throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
         }
      }
   }

   public void reset() {
      this.idx = this.mark;
   }

   public long skip(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Number of characters to skip is less than zero: " + var1);
      } else if (this.idx >= this.end()) {
         return 0L;
      } else {
         int var3 = (int)Math.min((long)this.end(), (long)this.idx + var1);
         int var4 = var3 - this.idx;
         this.idx = var3;
         return (long)var4;
      }
   }

   public String toString() {
      CharSequence var1 = this.charSequence.subSequence(this.start(), this.end());
      return var1.toString();
   }
}
