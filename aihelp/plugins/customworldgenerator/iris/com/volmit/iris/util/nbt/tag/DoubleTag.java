package com.volmit.iris.util.nbt.tag;

public class DoubleTag extends NumberTag<Double> implements Comparable<DoubleTag> {
   public static final byte ID = 6;
   public static final double ZERO_VALUE = 0.0D;

   public DoubleTag() {
      super(0.0D);
   }

   public DoubleTag(double value) {
      super(var1);
   }

   public byte getID() {
      return 6;
   }

   public void setValue(double value) {
      super.setValue(var1);
   }

   public boolean equals(Object other) {
      return super.equals(var1) && ((Double)this.getValue()).equals(((DoubleTag)var1).getValue());
   }

   public int compareTo(DoubleTag other) {
      return ((Double)this.getValue()).compareTo((Double)var1.getValue());
   }

   public DoubleTag clone() {
      return new DoubleTag((Double)this.getValue());
   }
}
