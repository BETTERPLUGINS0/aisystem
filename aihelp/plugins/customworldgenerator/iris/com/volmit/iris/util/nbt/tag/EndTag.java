package com.volmit.iris.util.nbt.tag;

public final class EndTag extends Tag<Void> {
   public static final byte ID = 0;
   public static final EndTag INSTANCE = new EndTag();

   private EndTag() {
      super((Object)null);
   }

   public byte getID() {
      return 0;
   }

   protected Void checkValue(Void value) {
      return var1;
   }

   public String valueToString(int maxDepth) {
      return "\"end\"";
   }

   public EndTag clone() {
      return INSTANCE;
   }
}
