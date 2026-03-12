package fr.xephi.authme.libs.org.postgresql.core;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public class Tuple {
   private final boolean forUpdate;
   final byte[][] data;

   public Tuple(int length) {
      this(new byte[length][], true);
   }

   public Tuple(byte[][] data) {
      this(data, false);
   }

   private Tuple(byte[][] data, boolean forUpdate) {
      this.data = data;
      this.forUpdate = forUpdate;
   }

   @NonNegative
   public int fieldCount() {
      return this.data.length;
   }

   @NonNegative
   public int length() {
      int length = 0;
      byte[][] var2 = this.data;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte[] field = var2[var4];
         if (field != null) {
            length += field.length;
         }
      }

      return length;
   }

   @Pure
   @Nullable
   public byte[] get(@NonNegative int index) {
      return this.data[index];
   }

   public Tuple updateableCopy() {
      return this.copy(true);
   }

   public Tuple readOnlyCopy() {
      return this.copy(false);
   }

   private Tuple copy(boolean forUpdate) {
      byte[][] dataCopy = new byte[this.data.length][];
      System.arraycopy(this.data, 0, dataCopy, 0, this.data.length);
      return new Tuple(dataCopy, forUpdate);
   }

   public void set(@NonNegative int index, @Nullable byte[] fieldData) {
      if (!this.forUpdate) {
         throw new IllegalArgumentException("Attempted to write to readonly tuple");
      } else {
         this.data[index] = fieldData;
      }
   }
}
