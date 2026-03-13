package com.volmit.iris.util.nbt.tag;

import java.util.Arrays;

public class IntArrayTag extends ArrayTag<int[]> implements Comparable<IntArrayTag> {
   public static final byte ID = 11;
   public static final int[] ZERO_VALUE = new int[0];

   public IntArrayTag() {
      super(ZERO_VALUE);
   }

   public IntArrayTag(int[] value) {
      super(var1);
   }

   public byte getID() {
      return 11;
   }

   public boolean equals(Object other) {
      return super.equals(var1) && Arrays.equals((int[])this.getValue(), (int[])((IntArrayTag)var1).getValue());
   }

   public int hashCode() {
      return Arrays.hashCode((int[])this.getValue());
   }

   public int compareTo(IntArrayTag other) {
      return Integer.compare(this.length(), var1.length());
   }

   public IntArrayTag clone() {
      return new IntArrayTag(Arrays.copyOf((int[])this.getValue(), this.length()));
   }
}
