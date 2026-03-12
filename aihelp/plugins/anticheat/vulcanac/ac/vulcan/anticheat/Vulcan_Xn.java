package ac.vulcan.anticheat;

import java.lang.reflect.Field;
import java.util.Collection;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_Xn extends Vulcan_XT {
   private boolean Vulcan_l;
   private boolean Vulcan_T;
   private String[] Vulcan_i;
   private Class Vulcan_O;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7484982496394362580L, -5626292617905209259L, (Object)null).a(266088961320133L);
   private static final String[] d;

   public static String Vulcan_n(Object[] var0) {
      Object var1 = (Object)var0[0];
      return Vulcan_z(new Object[]{var1, null, new Boolean(false), new Boolean(false), null});
   }

   public static String Vulcan_g(Object[] var0) {
      Object var1 = (Object)var0[0];
      Vulcan_O var2 = (Vulcan_O)var0[1];
      return Vulcan_z(new Object[]{var1, var2, new Boolean(false), new Boolean(false), null});
   }

   public static String Vulcan_r(Object[] var0) {
      Object var3 = (Object)var0[0];
      Vulcan_O var2 = (Vulcan_O)var0[1];
      boolean var1 = (Boolean)var0[2];
      return Vulcan_z(new Object[]{var3, var2, new Boolean(var1), new Boolean(false), null});
   }

   public static String Vulcan_i(Object[] var0) {
      Object var3 = (Object)var0[0];
      Vulcan_O var1 = (Vulcan_O)var0[1];
      boolean var4 = (Boolean)var0[2];
      boolean var2 = (Boolean)var0[3];
      return Vulcan_z(new Object[]{var3, var1, new Boolean(var4), new Boolean(var2), null});
   }

   public static String Vulcan_z(Object[] var0) {
      Object var1 = (Object)var0[0];
      Vulcan_O var2 = (Vulcan_O)var0[1];
      boolean var3 = (Boolean)var0[2];
      boolean var4 = (Boolean)var0[3];
      Class var5 = (Class)var0[4];
      return (new Vulcan_Xn(var1, var2, (StringBuffer)null, var5, var3, var4)).toString();
   }

   /** @deprecated */
   public static String Vulcan_l(Object[] var0) {
      Object var3 = (Object)var0[0];
      Vulcan_O var4 = (Vulcan_O)var0[1];
      boolean var1 = (Boolean)var0[2];
      Class var2 = (Class)var0[3];
      return (new Vulcan_Xn(var3, var4, (StringBuffer)null, var2, var1)).toString();
   }

   public static String Vulcan_L(Object[] var0) {
      long var1 = (Long)var0[0];
      Object var4 = (Object)var0[1];
      String var3 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 58393081567792L;
      return Vulcan_y(new Object[]{var4, new Long(var5), new String[]{var3}});
   }

   public static String Vulcan_V(Object[] var0) {
      Object var3 = (Object)var0[0];
      Collection var4 = (Collection)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 48038208533664L;
      long var7 = var1 ^ 24795110710393L;
      return Vulcan_y(new Object[]{var3, new Long(var7), Vulcan_y(new Object[]{new Long(var5), var4})});
   }

   static String[] Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static String[] Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_y(Object[] var0) {
      Object var4 = (Object)var0[0];
      long var2 = (Long)var0[1];
      String[] var1 = (String[])var0[2];
      var2 ^= a;
      long var5 = var2 ^ 29245614561913L;
      return (new Vulcan_Xn(var4)).Vulcan_c(new Object[]{var1, new Long(var5)}).toString();
   }

   public Vulcan_Xn(Object var1) {
      super(var1);
      this.Vulcan_l = false;
      this.Vulcan_T = false;
      this.Vulcan_O = null;
   }

   public Vulcan_Xn(Object var1, Vulcan_O var2) {
      super(var1, var2);
      this.Vulcan_l = false;
      this.Vulcan_T = false;
      this.Vulcan_O = null;
   }

   public Vulcan_Xn(Object var1, Vulcan_O var2, StringBuffer var3) {
      super(var1, var2, var3);
      this.Vulcan_l = false;
      this.Vulcan_T = false;
      this.Vulcan_O = null;
   }

   /** @deprecated */
   public Vulcan_Xn(Object var1, Vulcan_O var2, StringBuffer var3, Class var4, boolean var5) {
      long var6 = a ^ 7345636637804L;
      long var8 = var6 ^ 52946737357151L;
      super(var1, var2, var3);
      this.Vulcan_l = false;
      this.Vulcan_T = false;
      this.Vulcan_O = null;
      this.Vulcan_x(new Object[]{var4, new Long(var8)});
      this.Vulcan_M(new Object[]{new Boolean(var5)});
   }

   public Vulcan_Xn(Object var1, Vulcan_O var2, StringBuffer var3, Class var4, boolean var5, boolean var6) {
      long var7 = a ^ 83444538581619L;
      long var9 = var7 ^ 137909913239360L;
      int var10000 = Vulcan_iA.Vulcan_V();
      super(var1, var2, var3);
      int var11 = var10000;

      try {
         this.Vulcan_l = false;
         this.Vulcan_T = false;
         this.Vulcan_O = null;
         this.Vulcan_x(new Object[]{var4, new Long(var9)});
         this.Vulcan_M(new Object[]{new Boolean(var5)});
         this.Vulcan_O(new Object[]{new Boolean(var6)});
         if (AbstractCheck.Vulcan_m() != 0) {
            ++var11;
            Vulcan_iA.Vulcan_d(var11);
         }

      } catch (IllegalArgumentException var12) {
         throw a(var12);
      }
   }

   protected boolean Vulcan_I(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_g(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String[] Vulcan_Q(Object[] var1) {
      return this.Vulcan_i;
   }

   public Class Vulcan_v(Object[] var1) {
      return this.Vulcan_O;
   }

   protected Object Vulcan_N(Object[] var1) {
      Field var2 = (Field)var1[0];
      return var2.get(this.Vulcan_Q(new Object[0]));
   }

   public boolean Vulcan_U(Object[] var1) {
      return this.Vulcan_l;
   }

   public boolean Vulcan_V(Object[] var1) {
      return this.Vulcan_T;
   }

   public Vulcan_XT Vulcan_J(Object[] var1) {
      long var2 = (Long)var1[0];
      Object var4 = (Object)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 132921154660884L;
      this.Vulcan_O(new Object[0]).Vulcan_A(new Object[]{this.Vulcan_Q(new Object[0]), null, new Long(var5), var4});
      return this;
   }

   public void Vulcan_O(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_l = var2;
   }

   public void Vulcan_M(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T = var2;
   }

   public Vulcan_Xn Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "\u007f\u0016k'`G{^\u001dj\u007fYNtO\u001fo3QA{O\u000b}\u001ahA}Z\fg0~\u001881y\bk<yDqO\u001c.<|CkYXg,0Lw^Xo\u007fcWhO\nm3qQk\n\u0017h\u007fdJ}\n\u0017l5uAl";
      int var4 = "\u007f\u0016k'`G{^\u001dj\u007fYNtO\u001fo3QA{O\u000b}\u001ahA}Z\fg0~\u001881y\bk<yDqO\u001c.<|CkYXg,0Lw^Xo\u007fcWhO\nm3qQk\n\u0017h\u007fdJ}\n\u0017l5uAl".length();
      char var1 = '#';
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 77;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 77;
               var10005 = var7;
            } else {
               var10 = 77;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 77;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 103;
                  break;
               case 1:
                  var23 = 53;
                  break;
               case 2:
                  var23 = 67;
                  break;
               case 3:
                  var23 = 18;
                  break;
               case 4:
                  var23 = 93;
                  break;
               case 5:
                  var23 = 111;
                  break;
               default:
                  var23 = 85;
               }

               var10004[var10005] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var10 == 0) {
                  var10005 = var10;
                  var10004 = var10001;
                  var12 = var10;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var12 = var10;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            d = var5;
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
