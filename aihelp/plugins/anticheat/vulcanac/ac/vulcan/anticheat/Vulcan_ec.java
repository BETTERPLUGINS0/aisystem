package ac.vulcan.anticheat;

import java.io.Serializable;

public final class Vulcan_ec extends Vulcan_eS implements Serializable {
   private static final long serialVersionUID = 71849363892750L;
   private final float Vulcan_P;
   private final float Vulcan_d;
   private transient Float Vulcan_b;
   private transient Float Vulcan_e;
   private transient int Vulcan_E;
   private transient String Vulcan_J;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(6244181721199670558L, 7351197927607064510L, (Object)null).a(152160644945790L);
   private static final String[] d;

   public Vulcan_ec(float param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ec(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ec(float param1, float param2) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ec(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_P;
   }

   public int Vulcan_C(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_P;
   }

   public double Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_P;
   }

   public float Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_P;
   }

   public Number Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_c(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_d;
   }

   public int Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_d;
   }

   public double Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_d;
   }

   public float Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_d;
   }

   public boolean Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      Number var2 = (Number)var1[1];
      long var5 = var3 ^ 26689480484452L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      return this.Vulcan_D(new Object[]{new Long(var5), new Boolean(var2.floatValue())});
   }

   public boolean Vulcan_D(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      long var1 = a ^ 93304563958047L;
      String var3 = Vulcan_eS.Vulcan_y();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_J;
            if (var3 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var5) {
            throw a(var5);
         }

         StringBuffer var4 = new StringBuffer(32);
         var4.append(d[0]);
         var4.append(this.Vulcan_P);
         var4.append(',');
         var4.append(this.Vulcan_d);
         var4.append(']');
         this.Vulcan_J = var4.toString();
      }

      var10000 = this.Vulcan_J;
      return var10000;
   }

   static {
      String[] var5 = new String[7];
      int var3 = 0;
      String var2 = "\\y\u0002d_J\u001aZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\"bt\u001bZp\t#Td[l}\u001ep\u001a|C}lLmUe\u0016l}LM[_\u001bZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\u0002vV}\u001aZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\"bt";
      int var4 = "\\y\u0002d_J\u001aZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\"bt\u001bZp\t#Td[l}\u001ep\u001a|C}lLmUe\u0016l}LM[_\u001bZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\u0002vV}\u001aZp\t#Td[l}\u001e#WdEz8\u0002lN1Tk8\"bt".length();
      char var1 = 6;
      int var0 = -1;

      while(true) {
         int var7;
         int var8;
         byte var10;
         byte var12;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var22;
         byte var23;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var10 = 75;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 75;
               var10005 = var7;
            } else {
               var10 = 75;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 75;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 69;
                  break;
               case 1:
                  var23 = 83;
                  break;
               case 2:
                  var23 = 39;
                  break;
               case 3:
                  var23 = 72;
                  break;
               case 4:
                  var23 = 113;
                  break;
               case 5:
                  var23 = 90;
                  break;
               default:
                  var23 = 125;
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
            var2 = "+\u0001xR%\u0015*\u001d\fo\u0001k\r2\f\u001d=\u001c$\u0014g\u001d\f=<*.\u001c+\u0001xR%\u0015*\u001d\fo\u0001k\r2\f\u001d=\u001c$\u0014g\u001d\f=\u001c>\f+";
            var4 = "+\u0001xR%\u0015*\u001d\fo\u0001k\r2\f\u001d=\u001c$\u0014g\u001d\f=<*.\u001c+\u0001xR%\u0015*\u001d\fo\u0001k\r2\f\u001d=\u001c$\u0014g\u001d\f=\u001c>\f+".length();
            var1 = 27;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 58;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 58;
                     var10005 = var7;
                  } else {
                     var10 = 58;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 58;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 69;
                        break;
                     case 1:
                        var23 = 83;
                        break;
                     case 2:
                        var23 = 39;
                        break;
                     case 3:
                        var23 = 72;
                        break;
                     case 4:
                        var23 = 113;
                        break;
                     case 5:
                        var23 = 90;
                        break;
                     default:
                        var23 = 125;
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

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
