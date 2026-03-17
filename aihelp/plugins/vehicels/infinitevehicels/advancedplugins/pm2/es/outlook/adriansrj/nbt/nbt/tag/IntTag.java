package es.outlook.adriansrj.nbt.nbt.tag;

public class IntTag extends NumberTag<Integer> implements Comparable<IntTag> {
   public static final byte ID = 3;
   public static final int ZERO_VALUE = 0;

   public IntTag() {
      super(0);
   }

   public IntTag(int var1) {
      super(var1);
   }

   public byte getID() {
      return 3;
   }

   public void setValue(int var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && this.asInt() == ((IntTag)var1).asInt();
   }

   public int compareTo(IntTag var1) {
      return ((Integer)this.getValue()).compareTo((Integer)var1.getValue());
   }

   public IntTag clone() {
      return new IntTag((Integer)this.getValue());
   }
}
