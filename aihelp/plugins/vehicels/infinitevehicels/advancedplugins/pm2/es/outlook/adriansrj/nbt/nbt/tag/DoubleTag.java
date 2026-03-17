package es.outlook.adriansrj.nbt.nbt.tag;

public class DoubleTag extends NumberTag<Double> implements Comparable<DoubleTag> {
   public static final byte ID = 6;
   public static final double ZERO_VALUE = 0.0D;

   public DoubleTag() {
      super(0.0D);
   }

   public DoubleTag(double var1) {
      super(var1);
   }

   public byte getID() {
      return 6;
   }

   public void setValue(double var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && ((Double)this.getValue()).equals(((DoubleTag)var1).getValue());
   }

   public int compareTo(DoubleTag var1) {
      return ((Double)this.getValue()).compareTo((Double)var1.getValue());
   }

   public DoubleTag clone() {
      return new DoubleTag((Double)this.getValue());
   }
}
