package es.outlook.adriansrj.nbt.nbt.tag;

public class ShortTag extends NumberTag<Short> implements Comparable<ShortTag> {
   public static final byte ID = 2;
   public static final short ZERO_VALUE = 0;

   public ShortTag() {
      super(Short.valueOf((short)0));
   }

   public ShortTag(short var1) {
      super(var1);
   }

   public byte getID() {
      return 2;
   }

   public void setValue(short var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && this.asShort() == ((ShortTag)var1).asShort();
   }

   public int compareTo(ShortTag var1) {
      return ((Short)this.getValue()).compareTo((Short)var1.getValue());
   }

   public ShortTag clone() {
      return new ShortTag((Short)this.getValue());
   }
}
