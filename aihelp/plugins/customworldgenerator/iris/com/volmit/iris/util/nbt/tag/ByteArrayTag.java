package com.volmit.iris.util.nbt.tag;

import java.util.Arrays;

public class ByteArrayTag extends ArrayTag<byte[]> implements Comparable<ByteArrayTag> {
   public static final byte ID = 7;
   public static final byte[] ZERO_VALUE = new byte[0];

   public ByteArrayTag() {
      super(ZERO_VALUE);
   }

   public ByteArrayTag(byte[] value) {
      super(var1);
   }

   public byte getID() {
      return 7;
   }

   public boolean equals(Object other) {
      return super.equals(var1) && Arrays.equals((byte[])this.getValue(), (byte[])((ByteArrayTag)var1).getValue());
   }

   public int hashCode() {
      return Arrays.hashCode((byte[])this.getValue());
   }

   public int compareTo(ByteArrayTag other) {
      return Integer.compare(this.length(), var1.length());
   }

   public ByteArrayTag clone() {
      return new ByteArrayTag(Arrays.copyOf((byte[])this.getValue(), this.length()));
   }
}
