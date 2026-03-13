package com.volmit.iris.util.nbt.tag;

public class FloatTag extends NumberTag<Float> implements Comparable<FloatTag> {
   public static final byte ID = 5;
   public static final float ZERO_VALUE = 0.0F;

   public FloatTag() {
      super(0.0F);
   }

   public FloatTag(float value) {
      super(var1);
   }

   public byte getID() {
      return 5;
   }

   public void setValue(float value) {
      super.setValue(var1);
   }

   public boolean equals(Object other) {
      return super.equals(var1) && ((Float)this.getValue()).equals(((FloatTag)var1).getValue());
   }

   public int compareTo(FloatTag other) {
      return ((Float)this.getValue()).compareTo((Float)var1.getValue());
   }

   public FloatTag clone() {
      return new FloatTag((Float)this.getValue());
   }
}
