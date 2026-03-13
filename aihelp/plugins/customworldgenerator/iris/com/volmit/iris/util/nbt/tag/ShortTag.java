package com.volmit.iris.util.nbt.tag;

public class ShortTag extends NumberTag<Short> implements Comparable<ShortTag> {
   public static final byte ID = 2;
   public static final short ZERO_VALUE = 0;

   public ShortTag() {
      super(Short.valueOf((short)0));
   }

   public ShortTag(short value) {
      super(var1);
   }

   public byte getID() {
      return 2;
   }

   public void setValue(short value) {
      super.setValue(var1);
   }

   public boolean equals(Object other) {
      return super.equals(var1) && this.asShort() == ((ShortTag)var1).asShort();
   }

   public int compareTo(ShortTag other) {
      return ((Short)this.getValue()).compareTo((Short)var1.getValue());
   }

   public ShortTag clone() {
      return new ShortTag((Short)this.getValue());
   }
}
