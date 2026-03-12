package ac.vulcan.anticheat;

import java.io.Serializable;

public final class Vulcan_es extends Vulcan_eS implements Serializable {
   private static final long serialVersionUID = 71849363892740L;
   private final double Vulcan_X;
   private final double Vulcan_m;
   private transient Double Vulcan_d;
   private transient Double Vulcan_Y;
   private transient int Vulcan_k;
   private transient String Vulcan_J;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1694008696698681876L, -7142483949517868325L, (Object)null).a(38904772315898L);
   private static final String[] d;

   public Vulcan_es(double param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_es(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_es(double param1, double param3) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_es(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_X;
   }

   public int Vulcan_C(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_X;
   }

   public double Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_X;
   }

   public float Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_X;
   }

   public Number Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_c(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_m;
   }

   public int Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_m;
   }

   public double Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_m;
   }

   public float Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_m;
   }

   public boolean Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      Number var2 = (Number)var1[1];
      long var5 = var3 ^ 68887008478652L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      return this.Vulcan_G(new Object[]{new Double(var2.doubleValue()), new Long(var5)});
   }

   public boolean Vulcan_G(Object[] param1) {
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
      long var1 = a ^ 29314053926800L;
      String var3 = Vulcan_eS.Vulcan_y();

      int var10000;
      label20: {
         try {
            var10000 = this.Vulcan_k;
            if (var3 != null) {
               return var10000;
            }

            if (var10000 != 0) {
               break label20;
            }
         } catch (IllegalArgumentException var6) {
            throw a(var6);
         }

         this.Vulcan_k = 17;
         this.Vulcan_k = 37 * this.Vulcan_k + this.getClass().hashCode();
         long var4 = Double.doubleToLongBits(this.Vulcan_X);
         this.Vulcan_k = 37 * this.Vulcan_k + (int)(var4 ^ var4 >> 32);
         var4 = Double.doubleToLongBits(this.Vulcan_m);
         this.Vulcan_k = 37 * this.Vulcan_k + (int)(var4 ^ var4 >> 32);
      }

      var10000 = this.Vulcan_k;
      return var10000;
   }

   public String toString() {
      long var1 = a ^ 67832627016555L;
      long var3 = var1 ^ 31456863509167L;
      long var5 = var1 ^ 124984266750376L;
      long var7 = var1 ^ 15022049981494L;
      String var9 = Vulcan_eS.Vulcan_y();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_J;
            if (var9 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var11) {
            throw a(var11);
         }

         Vulcan_o var10 = new Vulcan_o(32);
         var10.Vulcan_FS(new Object[]{d[1], new Long(var3)});
         double var10002 = this.Vulcan_X;
         var10.Vulcan_b(new Object[]{new Long(var5), new Double(var10002)});
         var10.Vulcan__O(new Object[]{new Integer(44), new Long(var7)});
         var10002 = this.Vulcan_m;
         var10.Vulcan_b(new Object[]{new Long(var5), new Double(var10002)});
         var10.Vulcan__O(new Object[]{new Integer(93), new Long(var7)});
         this.Vulcan_J = var10.toString();
      }

      var10000 = this.Vulcan_J;
      return var10000;
   }

   static {
      String[] var5 = new String[7];
      int var3 = 0;
      String var2 = "B\\\u0018\u001d(\u001aOtQ\u000fNf\u0002We@]S)\u001b\u0002tQ]S3\u0003N\u0006DU\u0013Z#4\u001aB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u00143\\\b\u001aB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u00143\\\b\u001bB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u0014\u0013H*\u0003";
      int var4 = "B\\\u0018\u001d(\u001aOtQ\u000fNf\u0002We@]S)\u001b\u0002tQ]S3\u0003N\u0006DU\u0013Z#4\u001aB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u00143\\\b\u001aB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u00143\\\b\u001bB\\\u0018\u001d(\u001aOtQ\u000f\u001d+\u001aQb\u0014\u0013R2O@s\u0014\u0013H*\u0003".length();
      char var1 = 28;
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
            var10 = 106;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 106;
               var10005 = var7;
            } else {
               var10 = 106;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 106;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 124;
                  break;
               case 1:
                  var23 = 94;
                  break;
               case 2:
                  var23 = 23;
                  break;
               case 3:
                  var23 = 87;
                  break;
               case 4:
                  var23 = 44;
                  break;
               case 5:
                  var23 = 5;
                  break;
               default:
                  var23 = 72;
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
            var2 = "A_\u001b\u001e+\u0019LwR\fMe\u0001TfC^P*\u0018\u0001wR^p$\"\u001bA_\u001b\u001e+\u0019LwR\fMe\u0001TfC^P*\u0018\u0001wR^p$\"";
            var4 = "A_\u001b\u001e+\u0019LwR\fMe\u0001TfC^P*\u0018\u0001wR^p$\"\u001bA_\u001b\u001e+\u0019LwR\fMe\u0001TfC^P*\u0018\u0001wR^p$\"".length();
            var1 = 27;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 105;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 105;
                     var10005 = var7;
                  } else {
                     var10 = 105;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 105;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 124;
                        break;
                     case 1:
                        var23 = 94;
                        break;
                     case 2:
                        var23 = 23;
                        break;
                     case 3:
                        var23 = 87;
                        break;
                     case 4:
                        var23 = 44;
                        break;
                     case 5:
                        var23 = 5;
                        break;
                     default:
                        var23 = 72;
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
