package fr.xephi.authme.libs.org.postgresql.util;

import java.util.Arrays;

public final class IntList {
   private static final int[] EMPTY_INT_ARRAY = new int[0];
   private int[] ints;
   private int size;

   public IntList() {
      this.ints = EMPTY_INT_ARRAY;
   }

   public void add(int i) {
      int size = this.size;
      this.ensureCapacity(size);
      this.ints[size] = i;
      this.size = size + 1;
   }

   private void ensureCapacity(int size) {
      int length = this.ints.length;
      if (size >= length) {
         int newLength = length == 0 ? 8 : (length < 1024 ? length << 1 : length + (length >> 1));
         this.ints = Arrays.copyOf(this.ints, newLength);
      }

   }

   public int size() {
      return this.size;
   }

   public int get(int i) {
      if (i >= 0 && i < this.size) {
         return this.ints[i];
      } else {
         throw new ArrayIndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
      }
   }

   public void clear() {
      this.size = 0;
   }

   public int[] toArray() {
      return this.size == 0 ? EMPTY_INT_ARRAY : Arrays.copyOf(this.ints, this.size);
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("[");

      for(int i = 0; i < this.size; ++i) {
         if (i > 0) {
            sb.append(", ");
         }

         sb.append(this.ints[i]);
      }

      sb.append("]");
      return sb.toString();
   }
}
