package ac.vulcan.anticheat;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Vulcan_iy implements ListIterator, Cloneable {
   private static final Vulcan_iy Vulcan_c;
   private static final Vulcan_iy Vulcan_f;
   private char[] Vulcan_T;
   private String[] Vulcan_X;
   private int Vulcan_o;
   private Vulcan_eb Vulcan_m;
   private Vulcan_eb Vulcan_y;
   private Vulcan_eb Vulcan_F;
   private Vulcan_eb Vulcan_E;
   private boolean Vulcan_i;
   private boolean Vulcan_C;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(7565741583761686575L, -7908552693223754675L, (Object)null).a(65138050322605L);
   private static final String[] b;

   private static Vulcan_iy Vulcan_s(Object[] var0) {
      return (Vulcan_iy)Vulcan_c.clone();
   }

   public static Vulcan_iy Vulcan_I(Object[] var0) {
      return Vulcan_s(new Object[0]);
   }

   public static Vulcan_iy Vulcan_q(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 57058911176643L;
      Vulcan_iy var6 = Vulcan_s(new Object[0]);
      var6.Vulcan_Y(new Object[]{new Long(var4), var3});
      return var6;
   }

   public static Vulcan_iy Vulcan_f(Object[] var0) {
      char[] var1 = (char[])var0[0];
      Vulcan_iy var2 = Vulcan_s(new Object[0]);
      var2.Vulcan_S(new Object[]{var1});
      return var2;
   }

   private static Vulcan_iy Vulcan_p(Object[] var0) {
      return (Vulcan_iy)Vulcan_f.clone();
   }

   public static Vulcan_iy Vulcan_H(Object[] var0) {
      return Vulcan_p(new Object[0]);
   }

   public static Vulcan_iy Vulcan_h(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 27153091870122L;
      Vulcan_iy var6 = Vulcan_p(new Object[0]);
      var6.Vulcan_Y(new Object[]{new Long(var4), var3});
      return var6;
   }

   public static Vulcan_iy Vulcan_j(Object[] var0) {
      char[] var1 = (char[])var0[0];
      Vulcan_iy var2 = Vulcan_p(new Object[0]);
      var2.Vulcan_S(new Object[]{var1});
      return var2;
   }

   public Vulcan_iy() {
      this.Vulcan_m = Vulcan_eb.Vulcan_m(new Object[0]);
      this.Vulcan_y = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_F = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_E = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_i = false;
      this.Vulcan_C = true;
      this.Vulcan_T = null;
   }

   public Vulcan_iy(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iy(String var1, char var2) {
      long var3 = a ^ 76565834623821L;
      long var5 = var3 ^ 55325900811752L;
      this(var1);
      this.Vulcan_Z(new Object[]{new Long(var5), new Integer(var2)});
   }

   public Vulcan_iy(String var1, String var2) {
      long var3 = a ^ 140415851649714L;
      long var5 = var3 ^ 23094146603232L;
      this(var1);
      this.Vulcan_y(new Object[]{var2, new Long(var5)});
   }

   public Vulcan_iy(String var1, Vulcan_eb var2) {
      long var3 = a ^ 2323243899637L;
      long var5 = var3 ^ 52348168177700L;
      this(var1);
      this.Vulcan_P(new Object[]{new Long(var5), var2});
   }

   public Vulcan_iy(String var1, char var2, char var3) {
      long var4 = a ^ 106204201806730L;
      long var6 = var4 ^ 119482579499625L;
      this(var1, var2);
      this.Vulcan_r(new Object[]{new Long(var6), new Integer(var3)});
   }

   public Vulcan_iy(String var1, Vulcan_eb var2, Vulcan_eb var3) {
      long var4 = a ^ 75238623603028L;
      long var6 = var4 ^ 38999466276285L;
      this(var1, var2);
      this.Vulcan_O(new Object[]{var3, new Long(var6)});
   }

   public Vulcan_iy(char[] var1) {
      this.Vulcan_m = Vulcan_eb.Vulcan_m(new Object[0]);
      this.Vulcan_y = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_F = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_E = Vulcan_eb.Vulcan_N(new Object[0]);
      this.Vulcan_i = false;
      this.Vulcan_C = true;
      this.Vulcan_T = var1;
   }

   public Vulcan_iy(char[] var1, char var2) {
      long var3 = a ^ 126464745907240L;
      long var5 = var3 ^ 5461347951245L;
      this(var1);
      this.Vulcan_Z(new Object[]{new Long(var5), new Integer(var2)});
   }

   public Vulcan_iy(char[] var1, String var2) {
      long var3 = a ^ 50486988667480L;
      long var5 = var3 ^ 78473105553418L;
      this(var1);
      this.Vulcan_y(new Object[]{var2, new Long(var5)});
   }

   public Vulcan_iy(char[] var1, Vulcan_eb var2) {
      long var3 = a ^ 139129011625768L;
      long var5 = var3 ^ 91301651959289L;
      this(var1);
      this.Vulcan_P(new Object[]{new Long(var5), var2});
   }

   public Vulcan_iy(char[] var1, char var2, char var3) {
      long var4 = a ^ 132009726809474L;
      long var6 = var4 ^ 127763664225377L;
      this(var1, var2);
      this.Vulcan_r(new Object[]{new Long(var6), new Integer(var3)});
   }

   public Vulcan_iy(char[] var1, Vulcan_eb var2, Vulcan_eb var3) {
      long var4 = a ^ 132128878709382L;
      long var6 = var4 ^ 34352102299247L;
      this(var1, var2);
      this.Vulcan_O(new Object[]{var3, new Long(var6)});
   }

   public int Vulcan_A(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 128269228707405L;
      this.Vulcan_E(new Object[]{new Long(var4)});
      return this.Vulcan_X.length;
   }

   public String Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_G(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String[] Vulcan_S(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 10613485957872L;
      this.Vulcan_E(new Object[]{new Long(var4)});
      return (String[])((String[])this.Vulcan_X.clone());
   }

   public List Vulcan_S(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 119136200111962L;
      this.Vulcan_E(new Object[]{new Long(var4)});
      int[] var10000 = ac.vulcan.anticheat.Vulcan_o.Vulcan_h();
      ArrayList var7 = new ArrayList(this.Vulcan_X.length);
      int var8 = 0;
      int[] var6 = var10000;

      ArrayList var10;
      label33:
      while(var8 < this.Vulcan_X.length) {
         while(true) {
            try {
               if (var2 > 0L) {
                  var10 = var7;
                  if (var6 != null) {
                     return var10;
                  }

                  var7.add(this.Vulcan_X[var8]);
                  ++var8;
               }

               if (var6 == null) {
                  continue;
               }
            } catch (NoSuchElementException var9) {
               throw a(var9);
            }

            if (var2 >= 0L) {
               break label33;
            }
         }
      }

      var10 = var7;
      return var10;
   }

   public Vulcan_iy Vulcan_c(Object[] var1) {
      this.Vulcan_o = 0;
      this.Vulcan_X = null;
      return this;
   }

   public Vulcan_iy Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iy Vulcan_S(Object[] var1) {
      char[] var2 = (char[])var1[0];
      this.Vulcan_c(new Object[0]);
      this.Vulcan_T = var2;
      return this;
   }

   public boolean hasNext() {
      // $FF: Couldn't be decompiled
   }

   public Object next() {
      // $FF: Couldn't be decompiled
   }

   public int nextIndex() {
      return this.Vulcan_o;
   }

   public boolean hasPrevious() {
      long var1 = a ^ 44995045075110L;
      long var3 = var1 ^ 33037419463372L;
      int[] var10000 = ac.vulcan.anticheat.Vulcan_o.Vulcan_h();
      this.Vulcan_E(new Object[]{new Long(var3)});
      int[] var5 = var10000;

      int var7;
      label32: {
         try {
            var7 = this.Vulcan_o;
            if (var5 != null) {
               return (boolean)var7;
            }

            if (var7 > 0) {
               break label32;
            }
         } catch (NoSuchElementException var6) {
            throw a(var6);
         }

         var7 = 0;
         return (boolean)var7;
      }

      var7 = 1;
      return (boolean)var7;
   }

   public Object previous() {
      // $FF: Couldn't be decompiled
   }

   public int previousIndex() {
      return this.Vulcan_o - 1;
   }

   public void remove() {
      throw new UnsupportedOperationException(b[0]);
   }

   public void set(Object var1) {
      throw new UnsupportedOperationException(b[1]);
   }

   public void add(Object var1) {
      throw new UnsupportedOperationException(b[2]);
   }

   private void Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected List Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private int Vulcan_l(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private int Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_a(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_eb Vulcan_D(Object[] var1) {
      return this.Vulcan_m;
   }

   public Vulcan_iy Vulcan_P(Object[] var1) {
      long var3 = (Long)var1[0];
      Vulcan_eb var2 = (Vulcan_eb)var1[1];
      long var10000 = a ^ var3;

      try {
         if (var2 == null) {
            this.Vulcan_m = Vulcan_eb.Vulcan_N(new Object[0]);
            return this;
         }
      } catch (NoSuchElementException var5) {
         throw a(var5);
      }

      this.Vulcan_m = var2;
      return this;
   }

   public Vulcan_iy Vulcan_Z(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 100028328845581L;
      return this.Vulcan_P(new Object[]{new Long(var5), Vulcan_eb.Vulcan_O(new Object[]{new Integer(var4)})});
   }

   public Vulcan_iy Vulcan_y(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 78885603845626L;
      long var7 = var2 ^ 65017992547764L;
      return this.Vulcan_P(new Object[]{new Long(var5), Vulcan_eb.Vulcan__(new Object[]{new Long(var7), var4})});
   }

   public Vulcan_eb Vulcan_Z(Object[] var1) {
      return this.Vulcan_y;
   }

   public Vulcan_iy Vulcan_O(Object[] var1) {
      Vulcan_eb var4 = (Vulcan_eb)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;

      try {
         if (var4 != null) {
            this.Vulcan_y = var4;
         }

         return this;
      } catch (NoSuchElementException var5) {
         throw a(var5);
      }
   }

   public Vulcan_iy Vulcan_r(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 118349805205619L;
      return this.Vulcan_O(new Object[]{Vulcan_eb.Vulcan_O(new Object[]{new Integer(var2)}), new Long(var5)});
   }

   public Vulcan_eb Vulcan_p(Object[] var1) {
      return this.Vulcan_F;
   }

   public Vulcan_iy Vulcan_b(Object[] var1) {
      Vulcan_eb var2 = (Vulcan_eb)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;

      try {
         if (var2 != null) {
            this.Vulcan_F = var2;
         }

         return this;
      } catch (NoSuchElementException var5) {
         throw a(var5);
      }
   }

   public Vulcan_iy Vulcan_L(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 85476068387561L;
      return this.Vulcan_b(new Object[]{Vulcan_eb.Vulcan_O(new Object[]{new Integer(var4)}), new Long(var5)});
   }

   public Vulcan_eb Vulcan_M(Object[] var1) {
      return this.Vulcan_E;
   }

   public Vulcan_iy Vulcan_X(Object[] var1) {
      Vulcan_eb var2 = (Vulcan_eb)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;

      try {
         if (var2 != null) {
            this.Vulcan_E = var2;
         }

         return this;
      } catch (NoSuchElementException var5) {
         throw a(var5);
      }
   }

   public boolean Vulcan_A(Object[] var1) {
      return this.Vulcan_i;
   }

   public Vulcan_iy Vulcan__(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_i = var2;
      return this;
   }

   public boolean Vulcan_n(Object[] var1) {
      return this.Vulcan_C;
   }

   public Vulcan_iy Vulcan_x(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_C = var2;
      return this;
   }

   public String Vulcan_Z(Object[] var1) {
      long var2 = (Long)var1[0];

      try {
         if (this.Vulcan_T == null) {
            return null;
         }
      } catch (NoSuchElementException var4) {
         throw a(var4);
      }

      return new String(this.Vulcan_T);
   }

   public Object clone() {
      long var1 = a ^ 116767895169087L;
      long var3 = var1 ^ 40044344918740L;

      try {
         return this.Vulcan_H(new Object[]{new Long(var3)});
      } catch (CloneNotSupportedException var6) {
         return null;
      }
   }

   Object Vulcan_H(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      long var1 = a ^ 78729378845329L;
      long var3 = var1 ^ 32618119690968L;

      try {
         if (this.Vulcan_X == null) {
            return b[3];
         }
      } catch (NoSuchElementException var5) {
         throw a(var5);
      }

      return b[4] + this.Vulcan_S(new Object[]{new Long(var3)});
   }

   static {
      long var8 = a ^ 8533542443110L;
      long var10 = var8 ^ 106477624722575L;
      long var12 = var8 ^ 46464887458487L;
      long var14 = var8 ^ 118843001488537L;
      long var16 = var8 ^ 70255349397015L;
      String[] var5 = new String[5];
      int var3 = 0;
      String var2 = "9\u0006\";\u0001FAbC&'WV\u00078\u0016?$\u0018Q\u001d.\u0007\u00148\u0006;|^\u0003\u00008C::\u0004V\u0019;\f= \u0012G\u0014*\u0007+|^\u0003\u00008C::\u0004V\u0019;\f= \u0012G";
      int var4 = "9\u0006\";\u0001FAbC&'WV\u00078\u0016?$\u0018Q\u001d.\u0007\u00148\u0006;|^\u0003\u00008C::\u0004V\u0019;\f= \u0012G\u0014*\u0007+|^\u0003\u00008C::\u0004V\u0019;\f= \u0012G".length();
      char var1 = 23;
      int var0 = -1;

      while(true) {
         char var32;
         byte var33;
         int var7;
         char[] var10001;
         char[] var10002;
         int var18;
         int var10003;
         byte var20;
         char[] var10004;
         int var10005;
         byte var22;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var20 = 126;
            var10001 = var10002;
            var18 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var22 = 126;
               var10005 = var7;
            } else {
               var20 = 126;
               var18 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var22 = 126;
               var10005 = var7;
            }

            while(true) {
               var32 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var33 = 53;
                  break;
               case 1:
                  var33 = 29;
                  break;
               case 2:
                  var33 = 49;
                  break;
               case 3:
                  var33 = 42;
                  break;
               case 4:
                  var33 = 9;
                  break;
               case 5:
                  var33 = 93;
                  break;
               default:
                  var33 = 23;
               }

               var10004[var10005] = (char)(var32 ^ var22 ^ var33);
               ++var7;
               if (var20 == 0) {
                  var10005 = var20;
                  var10004 = var10001;
                  var22 = var20;
               } else {
                  if (var18 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var22 = var20;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "\b\u0007-\u0010\bX\u001c5\u001a%!\u0015h\u00174\u0007\u007f0\bX\u001c5\u001a%!\u0003\u0013\u0000>\u0007\u0002\f\b\u0007-\u0010\bX\u001c5\u001a%!\u0015";
            var4 = "\b\u0007-\u0010\bX\u001c5\u001a%!\u0015h\u00174\u0007\u007f0\bX\u001c5\u001a%!\u0003\u0013\u0000>\u0007\u0002\f\b\u0007-\u0010\bX\u001c5\u001a%!\u0015".length();
            var1 = 31;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var20 = 110;
                  var10001 = var10002;
                  var18 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var22 = 110;
                     var10005 = var7;
                  } else {
                     var20 = 110;
                     var18 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var22 = 110;
                     var10005 = var7;
                  }

                  while(true) {
                     var32 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var33 = 53;
                        break;
                     case 1:
                        var33 = 29;
                        break;
                     case 2:
                        var33 = 49;
                        break;
                     case 3:
                        var33 = 42;
                        break;
                     case 4:
                        var33 = 9;
                        break;
                     case 5:
                        var33 = 93;
                        break;
                     default:
                        var33 = 23;
                     }

                     var10004[var10005] = (char)(var32 ^ var22 ^ var33);
                     ++var7;
                     if (var20 == 0) {
                        var10005 = var20;
                        var10004 = var10001;
                        var22 = var20;
                     } else {
                        if (var18 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var22 = var20;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;
                  Vulcan_c = new Vulcan_iy();
                  Vulcan_c.Vulcan_P(new Object[]{new Long(var12), Vulcan_eb.Vulcan_q(new Object[0])});
                  Vulcan_c.Vulcan_O(new Object[]{Vulcan_eb.Vulcan_w(new Object[0]), new Long(var10)});
                  Vulcan_c.Vulcan_b(new Object[]{Vulcan_eb.Vulcan_N(new Object[0]), new Long(var14)});
                  Vulcan_c.Vulcan_X(new Object[]{Vulcan_eb.Vulcan_l(new Object[0]), new Long(var16)});
                  Vulcan_c.Vulcan__(new Object[]{new Boolean(false)});
                  Vulcan_c.Vulcan_x(new Object[]{new Boolean(false)});
                  Vulcan_f = new Vulcan_iy();
                  Vulcan_f.Vulcan_P(new Object[]{new Long(var12), Vulcan_eb.Vulcan_R(new Object[0])});
                  Vulcan_f.Vulcan_O(new Object[]{Vulcan_eb.Vulcan_w(new Object[0]), new Long(var10)});
                  Vulcan_f.Vulcan_b(new Object[]{Vulcan_eb.Vulcan_N(new Object[0]), new Long(var14)});
                  Vulcan_f.Vulcan_X(new Object[]{Vulcan_eb.Vulcan_l(new Object[0]), new Long(var16)});
                  Vulcan_f.Vulcan__(new Object[]{new Boolean(false)});
                  Vulcan_f.Vulcan_x(new Object[]{new Boolean(false)});
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
