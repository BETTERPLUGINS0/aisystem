package es.outlook.adriansrj.nbt.nbt.tag;

public class ByteTag extends NumberTag<Byte> implements Comparable<ByteTag> {
   public static final byte ID = 1;
   public static final byte ZERO_VALUE = 0;

   public ByteTag() {
      super((byte)0);
   }

   public ByteTag(byte var1) {
      super(var1);
   }

   public ByteTag(boolean var1) {
      super((byte)(var1 ? 1 : 0));
   }

   public byte getID() {
      return 1;
   }

   public boolean asBoolean() {
      return (Byte)this.getValue() > 0;
   }

   public void setValue(byte var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && this.asByte() == ((ByteTag)var1).asByte();
   }

   public int compareTo(ByteTag var1) {
      return ((Byte)this.getValue()).compareTo((Byte)var1.getValue());
   }

   public ByteTag clone() {
      return new ByteTag((Byte)this.getValue());
   }
}
