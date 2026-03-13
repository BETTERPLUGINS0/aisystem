package com.volmit.iris.util.nbt.tag;

public class IntTag extends NumberTag<Integer> implements Comparable<IntTag> {
   public static final byte ID = 3;
   public static final int ZERO_VALUE = 0;

   public IntTag() {
      super(0);
   }

   public IntTag(int value) {
      super(var1);
   }

   public byte getID() {
      return 3;
   }

   public void setValue(int value) {
      super.setValue(var1);
   }

   public boolean equals(Object other) {
      return super.equals(var1) && this.asInt() == ((IntTag)var1).asInt();
   }

   public int compareTo(IntTag other) {
      return ((Integer)this.getValue()).compareTo((Integer)var1.getValue());
   }

   public IntTag clone() {
      return new IntTag((Integer)this.getValue());
   }
}
