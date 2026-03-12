package ac.vulcan.anticheat;

public abstract class Vulcan_eS {
   private static String Vulcan_O;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-2629745892466989774L, -4643886480599915507L, (Object)null).a(40332554026256L);
   private static final String c;

   public abstract Number Vulcan_b(Object[] var1);

   public long Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 120412525193589L;
      return this.Vulcan_b(new Object[]{new Long(var4)}).longValue();
   }

   public int Vulcan_C(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 12440756340629L;
      return this.Vulcan_b(new Object[]{new Long(var4)}).intValue();
   }

   public double Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 138191106460352L;
      return this.Vulcan_b(new Object[]{new Long(var4)}).doubleValue();
   }

   public float Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 81339187246565L;
      return this.Vulcan_b(new Object[]{new Long(var4)}).floatValue();
   }

   public abstract Number Vulcan_t(Object[] var1);

   public long Vulcan_c(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 4554792873694L;
      return this.Vulcan_t(new Object[]{new Long(var4)}).longValue();
   }

   public int Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 25013816497780L;
      return this.Vulcan_t(new Object[]{new Long(var4)}).intValue();
   }

   public double Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 39674084581913L;
      return this.Vulcan_t(new Object[]{new Long(var4)}).doubleValue();
   }

   public float Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 90471967675730L;
      return this.Vulcan_t(new Object[]{new Long(var4)}).floatValue();
   }

   public abstract boolean Vulcan_V(Object[] var1);

   public boolean Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      Number var4 = (Number)var1[1];
      var2 ^= b;
      long var5 = var2 ^ 15003525318787L;

      try {
         if (var4 == null) {
            return false;
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      return this.Vulcan_y(new Object[]{new Long(var4.longValue()), new Long(var5)});
   }

   public boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_l(Object[] var1) {
      Number var4 = (Number)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= b;
      long var5 = var2 ^ 90880653630838L;

      try {
         if (var4 == null) {
            return false;
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      return this.Vulcan_Y(new Object[]{new Long(var5), new Integer(var4.intValue())});
   }

   public boolean Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_H(Object[] var1) {
      int var3 = (Integer)var1[0];
      Number var2 = (Number)var1[1];
      int var4 = (Integer)var1[2];
      int var5 = (Integer)var1[3];
      long var6 = ((long)var3 << 48 | (long)var4 << 32 >>> 16 | (long)var5 << 48 >>> 48) ^ b;
      long var8 = var6 ^ 15311905691526L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (RuntimeException var10) {
         throw a(var10);
      }

      return this.Vulcan_G(new Object[]{new Double(var2.doubleValue()), new Long(var8)});
   }

   public boolean Vulcan_G(Object[] var1) {
      double var2 = (Double)var1[0];
      long var4 = (Long)var1[1];
      long var6 = var4 ^ 35970908767937L;
      long var8 = var4 ^ 127616696830515L;
      long var10 = var4 ^ 128915235469633L;
      int var13 = Vulcan_bi.Vulcan__(new Object[]{new Double(this.Vulcan_j(new Object[]{new Long(var6)})), new Long(var8), new Double(var2)});
      String var10000 = Vulcan_y();
      int var14 = Vulcan_bi.Vulcan__(new Object[]{new Double(this.Vulcan_R(new Object[]{new Long(var10)})), new Long(var8), new Double(var2)});
      String var12 = var10000;

      int var17;
      label46: {
         label34: {
            label33: {
               try {
                  var17 = var13;
                  if (var12 != null) {
                     break label33;
                  }

                  if (var13 > 0) {
                     break label34;
                  }
               } catch (RuntimeException var16) {
                  throw a(var16);
               }

               var17 = var14;
            }

            try {
               if (var12 != null) {
                  return (boolean)var17;
               }

               if (var17 >= 0) {
                  break label46;
               }
            } catch (RuntimeException var15) {
               throw a(var15);
            }
         }

         var17 = 0;
         return (boolean)var17;
      }

      var17 = 1;
      return (boolean)var17;
   }

   public boolean Vulcan_v(Object[] var1) {
      Number var2 = (Number)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= b;
      long var5 = var3 ^ 90124049096948L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      return this.Vulcan_D(new Object[]{new Long(var5), new Boolean(var2.floatValue())});
   }

   public boolean Vulcan_D(Object[] var1) {
      long var2 = (Long)var1[0];
      float var4 = (Float)var1[1];
      long var5 = var2 ^ 108726033035026L;
      long var7 = var2 ^ 41247669661138L;
      long var9 = var2 ^ 54990060476988L;
      String var10000 = Vulcan_y();
      int var12 = Vulcan_bi.Vulcan_Y(new Object[]{new Boolean(this.Vulcan_a(new Object[]{new Long(var9)})), new Long(var5), new Boolean(var4)});
      String var11 = var10000;
      int var13 = Vulcan_bi.Vulcan_Y(new Object[]{new Boolean(this.Vulcan_h(new Object[]{new Long(var7)})), new Long(var5), new Boolean(var4)});

      int var16;
      label46: {
         label34: {
            label33: {
               try {
                  var16 = var12;
                  if (var11 != null) {
                     break label33;
                  }

                  if (var12 > 0) {
                     break label34;
                  }
               } catch (RuntimeException var15) {
                  throw a(var15);
               }

               var16 = var13;
            }

            try {
               if (var11 != null) {
                  return (boolean)var16;
               }

               if (var16 >= 0) {
                  break label46;
               }
            } catch (RuntimeException var14) {
               throw a(var14);
            }
         }

         var16 = 0;
         return (boolean)var16;
      }

      var16 = 1;
      return (boolean)var16;
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
      long var1 = b ^ 56915712523575L;
      long var3 = var1 ^ 35439804733930L;
      long var5 = var1 ^ 48419237088947L;
      byte var7 = 17;
      int var8 = 37 * var7 + this.getClass().hashCode();
      var8 = 37 * var8 + this.Vulcan_b(new Object[]{new Long(var5)}).hashCode();
      var8 = 37 * var8 + this.Vulcan_t(new Object[]{new Long(var3)}).hashCode();
      return var8;
   }

   public String toString() {
      long var1 = b ^ 121154419927457L;
      long var3 = var1 ^ 33796044244676L;
      long var5 = var1 ^ 138301000568188L;
      long var7 = var1 ^ 125321602357797L;
      long var9 = var1 ^ 17082324969565L;
      long var11 = var1 ^ 5015971915892L;
      Vulcan_o var13 = new Vulcan_o(32);
      var13.Vulcan_FS(new Object[]{c, new Long(var3)});
      var13.Vulcan_Dw(new Object[]{new Long(var11), this.Vulcan_b(new Object[]{new Long(var7)})});
      var13.Vulcan__O(new Object[]{new Integer(44), new Long(var9)});
      var13.Vulcan_Dw(new Object[]{new Long(var11), this.Vulcan_t(new Object[]{new Long(var5)})});
      var13.Vulcan__O(new Object[]{new Integer(93), new Long(var9)});
      return var13.toString();
   }

   public static void Vulcan_a(String var0) {
      Vulcan_O = var0;
   }

   public static String Vulcan_y() {
      return Vulcan_O;
   }

   static {
      if (Vulcan_y() != null) {
         Vulcan_a("ZP6jCc");
      }

      char[] var10002 = "Sdq)RA".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 58;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 58;
         var10005 = var1;
      } else {
         var13 = 58;
         var11 = var10003;
         if (var10003 <= var1) {
            c = (new String(var10002)).intern();
            return;
         }

         var10004 = var10002;
         var14 = 58;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 59;
            break;
         case 1:
            var10 = 63;
            break;
         case 2:
            var10 = 37;
            break;
         case 3:
            var10 = 116;
            break;
         case 4:
            var10 = 13;
            break;
         case 5:
            var10 = 32;
            break;
         default:
            var10 = 44;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               c = (new String(var10001)).intern();
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
