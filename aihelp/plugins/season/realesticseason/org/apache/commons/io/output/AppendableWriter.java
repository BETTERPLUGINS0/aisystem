package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Objects;

public class AppendableWriter<T extends Appendable> extends Writer {
   private final T appendable;

   public AppendableWriter(T var1) {
      this.appendable = var1;
   }

   public Writer append(char var1) {
      this.appendable.append(var1);
      return this;
   }

   public Writer append(CharSequence var1) {
      this.appendable.append(var1);
      return this;
   }

   public Writer append(CharSequence var1, int var2, int var3) {
      this.appendable.append(var1, var2, var3);
      return this;
   }

   public void close() {
   }

   public void flush() {
   }

   public T getAppendable() {
      return this.appendable;
   }

   public void write(char[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "Character array is missing");
      if (var3 >= 0 && var2 + var3 <= var1.length) {
         for(int var4 = 0; var4 < var3; ++var4) {
            this.appendable.append(var1[var2 + var4]);
         }

      } else {
         throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
      }
   }

   public void write(int var1) {
      this.appendable.append((char)var1);
   }

   public void write(String var1, int var2, int var3) {
      Objects.requireNonNull(var1, "String is missing");
      this.appendable.append(var1, var2, var2 + var3);
   }
}
