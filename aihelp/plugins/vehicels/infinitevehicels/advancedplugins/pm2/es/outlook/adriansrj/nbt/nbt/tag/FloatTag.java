package es.outlook.adriansrj.nbt.nbt.tag;

public class FloatTag extends NumberTag<Float> implements Comparable<FloatTag> {
   public static final byte ID = 5;
   public static final float ZERO_VALUE = 0.0F;

   public FloatTag() {
      super(0.0F);
   }

   public FloatTag(float var1) {
      super(var1);
   }

   public byte getID() {
      return 5;
   }

   public void setValue(float var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && ((Float)this.getValue()).equals(((FloatTag)var1).getValue());
   }

   public int compareTo(FloatTag var1) {
      return ((Float)this.getValue()).compareTo((Float)var1.getValue());
   }

   public FloatTag clone() {
      return new FloatTag((Float)this.getValue());
   }
}
