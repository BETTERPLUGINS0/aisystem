package ac.vulcan.anticheat;

import java.io.Serializable;

public class Vulcan_t implements Vulcan_i8, Serializable {
   private static final long serialVersionUID = 86241875189L;
   private Object Vulcan_c;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-6105959918204759085L, -2523104997200461585L, (Object)null).a(188595129215235L);
   private static final String b;

   public Vulcan_t() {
   }

   public Vulcan_t(Object var1) {
      this.Vulcan_c = var1;
   }

   public Object Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      int var4 = (Integer)var1[2];
      return this.Vulcan_c;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_c = var2;
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   static {
      char[] var10002 = "TQ:s".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 105;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 105;
         var10005 = var1;
      } else {
         var13 = 105;
         var11 = var10003;
         if (var10003 <= var1) {
            b = (new String(var10002)).intern();
            return;
         }

         var10004 = var10002;
         var14 = 105;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 83;
            break;
         case 1:
            var10 = 77;
            break;
         case 2:
            var10 = 63;
            break;
         case 3:
            var10 = 118;
            break;
         case 4:
            var10 = 87;
            break;
         case 5:
            var10 = 100;
            break;
         default:
            var10 = 8;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               b = (new String(var10001)).intern();
               return;
            }

            var10004 = var10001;
            var14 = var13;
            var10005 = var1;
         }
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
