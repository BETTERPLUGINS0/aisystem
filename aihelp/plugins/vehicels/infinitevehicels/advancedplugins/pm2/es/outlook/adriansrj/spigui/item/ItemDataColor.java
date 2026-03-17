package es.outlook.adriansrj.spigui.item;

public enum ItemDataColor {
   WHITE((short)0),
   ORANGE((short)1),
   MAGENTA((short)2),
   LIGHT_BLUE((short)3),
   YELLOW((short)4),
   LIME((short)5),
   PINK((short)6),
   GRAY((short)7),
   LIGHT_GRAY((short)8),
   CYAN((short)9),
   PURPLE((short)10),
   BLUE((short)11),
   BROWN((short)12),
   GREEN((short)13),
   RED((short)14),
   BLACK((short)15);

   private final short value;

   private ItemDataColor(short var3) {
      if (var3 <= 15 && var3 >= 0) {
         this.value = var3;
      } else {
         throw new IllegalArgumentException("Value must be between 0 and 15.");
      }
   }

   public short getValue() {
      return this.value;
   }

   public static ItemDataColor getByValue(short var0) {
      ItemDataColor[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemDataColor var4 = var1[var3];
         if (var0 == var4.value) {
            return var4;
         }
      }

      return null;
   }
}
