package com.volmit.iris.util.nbt.tag;

import java.util.Arrays;

public class LongArrayTag extends ArrayTag<long[]> implements Comparable<LongArrayTag> {
   public static final byte ID = 12;
   public static final long[] ZERO_VALUE = new long[0];

   public LongArrayTag() {
      super(ZERO_VALUE);
   }

   public LongArrayTag(long[] value) {
      super(var1);
   }

   public byte getID() {
      return 12;
   }

   public boolean equals(Object other) {
      return super.equals(var1) && Arrays.equals((long[])this.getValue(), (long[])((LongArrayTag)var1).getValue());
   }

   public int hashCode() {
      return Arrays.hashCode((long[])this.getValue());
   }

   public int compareTo(LongArrayTag other) {
      return Integer.compare(this.length(), var1.length());
   }

   public LongArrayTag clone() {
      return new LongArrayTag(Arrays.copyOf((long[])this.getValue(), this.length()));
   }
}
