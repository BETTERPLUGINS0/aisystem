package ac.vulcan.anticheat;

import me.frep.vulcan.spigot.check.AbstractCheck;

class Vulcan_L {
   private transient Vulcan_XS[] Vulcan_l;
   private transient int Vulcan_S;
   private int Vulcan_Y;
   private final float Vulcan_p;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4927734871475316499L, 6086122670852944689L, (Object)null).a(12128065208642L);
   private static final String[] b;

   public Vulcan_L() {
      this(20, 0.75F);
   }

   public Vulcan_L(int var1) {
      this(var1, 0.75F);
   }

   public Vulcan_L(int var1, float var2) {
      long var3 = a ^ 35326034610886L;
      super();
      if (var1 < 0) {
         StringBuffer var10002 = new StringBuffer();
         String[] var5 = b;
         throw new IllegalArgumentException(var10002.append(var5[0]).append(var1).toString());
      } else {
         try {
            if (var2 <= 0.0F) {
               throw new IllegalArgumentException(b[1] + var2);
            }
         } catch (IllegalArgumentException var6) {
            throw a(var6);
         }

         if (var1 == 0) {
            var1 = 1;
         }

         this.Vulcan_p = var2;
         this.Vulcan_l = new Vulcan_XS[var1];
         this.Vulcan_Y = (int)((float)var1 * var2);
      }
   }

   public int Vulcan_j(Object[] var1) {
      return this.Vulcan_S;
   }

   public boolean Vulcan_A(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      String[] var4 = Vulcan_XL.Vulcan_v();

      int var6;
      label32: {
         try {
            var6 = this.Vulcan_S;
            if (var4 == null) {
               return (boolean)var6;
            }

            if (var6 == 0) {
               break label32;
            }
         } catch (IllegalArgumentException var5) {
            throw a(var5);
         }

         var6 = 0;
         return (boolean)var6;
      }

      var6 = 1;
      return (boolean)var6;
   }

   public boolean Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_o(Object[] var1) {
      long var2 = (Long)var1[0];
      Object var4 = (Object)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 870453319081L;
      return this.Vulcan_B(new Object[]{new Long(var5), var4});
   }

   public boolean Vulcan_h(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Object Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_g(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      int var6 = this.Vulcan_l.length;
      Vulcan_XS[] var7 = this.Vulcan_l;
      int var8 = var6 * 2 + 1;
      Vulcan_XS[] var9 = new Vulcan_XS[var8];
      String[] var10000 = Vulcan_XL.Vulcan_v();
      this.Vulcan_Y = (int)((float)var8 * this.Vulcan_p);
      String[] var4 = var10000;
      this.Vulcan_l = var9;
      int var10 = var6;

      label64:
      while(true) {
         Vulcan_XS var11;
         if (var10-- > 0) {
            var11 = var7[var10];
         } else {
            if (var2 >= 0L) {
               return;
            }

            var11 = var7[var10];
         }

         while(true) {
            label60:
            while(true) {
               if (var11 != null) {
                  Vulcan_XS var12 = var11;
                  var11 = var11.Vulcan_u;
                  int var13 = (var12.Vulcan_J & Integer.MAX_VALUE) % var8;
                  var12.Vulcan_u = var9[var13];
                  var9[var13] = var12;
                  if (var4 == null) {
                     continue label64;
                  }

                  while(var2 >= 0L) {
                     if (var4 != null) {
                        continue label60;
                     }

                     int var5 = AbstractCheck.Vulcan_V();
                     ++var5;
                     if (var2 >= 0L) {
                        AbstractCheck.Vulcan_H(var5);
                        if (var4 != null) {
                           continue label64;
                        }
                        break label60;
                     }

                     var12.Vulcan_u = var9[var5];
                     var9[var5] = var12;
                     if (var4 == null) {
                        continue label64;
                     }
                  }

                  if (var4 != null) {
                     continue label64;
                  }
                  break;
               }

               if (var4 != null) {
                  continue label64;
               }
               break;
            }

            if (var2 >= 0L) {
               return;
            }

            var11 = var7[var10];
         }
      }
   }

   public Object Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Object Vulcan_l(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public synchronized void Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "R\u0006\u0013\b\u000e#P;)\u001e\u001d\b!Uo\u0013EM\u000eR\u0006\u0013\b\u000e#P;&\u0010\f\rx\u001c";
      int var4 = "R\u0006\u0013\b\u000e#P;)\u001e\u001d\b!Uo\u0013EM\u000eR\u0006\u0013\b\u000e#P;&\u0010\f\rx\u001c".length();
      char var1 = 18;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 99;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 99;
               var10005 = var7;
            } else {
               var10 = 99;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 99;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 120;
                  break;
               case 1:
                  var23 = 9;
                  break;
               case 2:
                  var23 = 28;
                  break;
               case 3:
                  var23 = 14;
                  break;
               case 4:
                  var23 = 10;
                  break;
               case 5:
                  var23 = 33;
                  break;
               default:
                  var23 = 95;
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
            b = var5;
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
