package es.outlook.adriansrj.nbt.nbt.tag;

public class LongTag extends NumberTag<Long> implements Comparable<LongTag> {
   public static final byte ID = 4;
   public static final long ZERO_VALUE = 0L;

   public LongTag() {
      super(0L);
   }

   public LongTag(long var1) {
      super(var1);
   }

   public byte getID() {
      return 4;
   }

   public void setValue(long var1) {
      super.setValue(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && this.asLong() == ((LongTag)var1).asLong();
   }

   public int compareTo(LongTag var1) {
      return ((Long)this.getValue()).compareTo((Long)var1.getValue());
   }

   public LongTag clone() {
      return new LongTag((Long)this.getValue());
   }
}
