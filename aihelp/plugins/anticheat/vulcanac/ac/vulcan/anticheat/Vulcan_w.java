package ac.vulcan.anticheat;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Vulcan_w {
   public static final char Vulcan__ = '$';
   public static final Vulcan_eb Vulcan_E;
   public static final Vulcan_eb Vulcan_v;
   private char Vulcan_o;
   private Vulcan_eb Vulcan_L;
   private Vulcan_eb Vulcan_G;
   private Vulcan_eJ Vulcan_g;
   private boolean Vulcan_V;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-4813858367211093671L, -4686454235315370413L, (Object)null).a(36665793899402L);
   private static final String[] b;

   public static String Vulcan_J(Object[] var0) {
      long var1 = (Long)var0[0];
      Object var3 = (Object)var0[1];
      Map var4 = (Map)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 125006423053057L;
      return (new Vulcan_w(var4)).Vulcan_M(new Object[]{var3, new Long(var5)});
   }

   public static String Vulcan_D(Object[] var0) {
      Object var4 = (Object)var0[0];
      Map var3 = (Map)var0[1];
      String var6 = (String)var0[2];
      long var1 = (Long)var0[3];
      String var5 = (String)var0[4];
      var1 ^= a;
      long var7 = var1 ^ 12548977984807L;
      return (new Vulcan_w(var3, var6, var5)).Vulcan_M(new Object[]{var4, new Long(var7)});
   }

   public static String Vulcan_N(Object[] var0) {
      Object var1 = (Object)var0[0];
      Properties var4 = (Properties)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 49712563438867L;
      int[] var7 = ac.vulcan.anticheat.Vulcan_o.Vulcan_h();

      Object var10000;
      label67: {
         try {
            var10000 = var4;
            if (var7 != null) {
               return var10000.toString();
            }

            if (var4 != null) {
               break label67;
            }
         } catch (IllegalStateException var13) {
            throw a(var13);
         }

         var10000 = var1;
         return var10000.toString();
      }

      HashMap var8 = new HashMap();
      Enumeration var9 = var4.propertyNames();

      String var14;
      label41:
      while(true) {
         if (var9.hasMoreElements()) {
            String var10 = (String)var9.nextElement();
            var14 = var4.getProperty(var10);
            if (var2 <= 0L) {
               return var14;
            }

            String var11 = var14;

            do {
               try {
                  var10000 = var8.put(var10, var11);
                  if (var7 != null) {
                     break label41;
                  }

                  if (var7 == null) {
                     continue label41;
                  }
               } catch (IllegalStateException var12) {
                  throw a(var12);
               }
            } while(var2 <= 0L);
         }

         var10000 = var1;
         break;
      }

      var14 = Vulcan_J(new Object[]{new Long(var5), var10000, var8});
      return var14;
   }

   public static String Vulcan_K(Object[] var0) {
      Object var1 = (Object)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 112536472381334L;
      return (new Vulcan_w(Vulcan_eJ.Vulcan_Z(new Object[0]))).Vulcan_M(new Object[]{var1, new Long(var4)});
   }

   public Vulcan_w() {
      this((Vulcan_eJ)null, Vulcan_E, Vulcan_v, '$');
   }

   public Vulcan_w(Map var1) {
      this(Vulcan_eJ.Vulcan_Q(new Object[]{var1}), Vulcan_E, Vulcan_v, '$');
   }

   public Vulcan_w(Map var1, String var2, String var3) {
      this(Vulcan_eJ.Vulcan_Q(new Object[]{var1}), var2, var3, '$');
   }

   public Vulcan_w(Map var1, String var2, String var3, char var4) {
      this(Vulcan_eJ.Vulcan_Q(new Object[]{var1}), var2, var3, var4);
   }

   public Vulcan_w(Vulcan_eJ var1) {
      this(var1, Vulcan_E, Vulcan_v, '$');
   }

   public Vulcan_w(Vulcan_eJ var1, String var2, String var3, char var4) {
      long var5 = a ^ 18355170381170L;
      long var7 = var5 ^ 67039303884951L;
      long var9 = var5 ^ 63565018995332L;
      super();
      this.Vulcan_H(new Object[]{var1});
      this.Vulcan_P(new Object[]{new Long(var9), var2});
      this.Vulcan_W(new Object[]{var3, new Long(var7)});
      this.Vulcan_G(new Object[]{new Integer(var4)});
   }

   public Vulcan_w(Vulcan_eJ var1, Vulcan_eb var2, Vulcan_eb var3, char var4) {
      long var5 = a ^ 103818941020100L;
      long var7 = var5 ^ 36621554419712L;
      long var9 = var5 ^ 77094556067294L;
      super();
      this.Vulcan_H(new Object[]{var1});
      this.Vulcan_d(new Object[]{var2, new Long(var9)});
      this.Vulcan_Z(new Object[]{var3, new Long(var7)});
      this.Vulcan_G(new Object[]{new Integer(var4)});
   }

   public String Vulcan_u(Object[] var1) {
      String var2 = (String)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 34935278854026L;

      try {
         if (var2 == null) {
            return null;
         }
      } catch (IllegalStateException var9) {
         throw a(var9);
      }

      Vulcan_o var7 = new Vulcan_o(var2);

      try {
         if (!this.Vulcan_a(new Object[]{var7, new Long(var5), new Integer(0), new Integer(var2.length())})) {
            return var2;
         }
      } catch (IllegalStateException var8) {
         throw a(var8);
      }

      return var7.toString();
   }

   public String Vulcan_x(Object[] var1) {
      String var6 = (String)var1[0];
      int var2 = (Integer)var1[1];
      int var5 = (Integer)var1[2];
      long var3 = (Long)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 105746707377040L;
      long var9 = var3 ^ 101989103139731L;

      try {
         if (var6 == null) {
            return null;
         }
      } catch (IllegalStateException var13) {
         throw a(var13);
      }

      Vulcan_o var11 = (new Vulcan_o(var5)).Vulcan_N(new Object[]{new Long(var9), var6, new Integer(var2), new Integer(var5)});

      try {
         if (!this.Vulcan_a(new Object[]{var11, new Long(var7), new Integer(0), new Integer(var5)})) {
            return var6.substring(var2, var2 + var5);
         }
      } catch (IllegalStateException var12) {
         throw a(var12);
      }

      return var11.toString();
   }

   public String Vulcan_v(Object[] var1) {
      char[] var2 = (char[])var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 39264301805293L;
      long var7 = var3 ^ 100125615885249L;

      try {
         if (var2 == null) {
            return null;
         }
      } catch (IllegalStateException var10) {
         throw a(var10);
      }

      Vulcan_o var9 = (new Vulcan_o(var2.length)).Vulcan_X(new Object[]{var2, new Long(var7)});
      this.Vulcan_a(new Object[]{var9, new Long(var5), new Integer(0), new Integer(var2.length)});
      return var9.toString();
   }

   public String Vulcan_n(Object[] var1) {
      char[] var6 = (char[])var1[0];
      long var2 = (Long)var1[1];
      int var4 = (Integer)var1[2];
      int var5 = (Integer)var1[3];
      var2 ^= a;
      long var7 = var2 ^ 135105180308990L;
      long var9 = var2 ^ 273733143417L;

      try {
         if (var6 == null) {
            return null;
         }
      } catch (IllegalStateException var12) {
         throw a(var12);
      }

      Vulcan_o var11 = (new Vulcan_o(var5)).Vulcan_PB(new Object[]{var6, new Integer(var4), new Long(var9), new Integer(var5)});
      this.Vulcan_a(new Object[]{var11, new Long(var7), new Integer(0), new Integer(var5)});
      return var11.toString();
   }

   public String Vulcan_L(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 127879003228442L;
      long var7 = var2 ^ 88916019587313L;

      try {
         if (var4 == null) {
            return null;
         }
      } catch (IllegalStateException var10) {
         throw a(var10);
      }

      Vulcan_o var9 = (new Vulcan_o(var4.length())).Vulcan_BD(new Object[]{new Long(var7), var4});
      this.Vulcan_a(new Object[]{var9, new Long(var5), new Integer(0), new Integer(var9.Vulcan_S(new Object[0]))});
      return var9.toString();
   }

   public String Vulcan_h(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      long var5 = (Long)var1[1];
      int var4 = (Integer)var1[2];
      int var2 = (Integer)var1[3];
      var5 ^= a;
      long var7 = var5 ^ 53610740592253L;
      long var9 = var5 ^ 25701019417240L;

      try {
         if (var3 == null) {
            return null;
         }
      } catch (IllegalStateException var12) {
         throw a(var12);
      }

      Vulcan_o var11 = (new Vulcan_o(var2)).Vulcan_k(new Object[]{var3, new Integer(var4), new Long(var9), new Integer(var2)});
      this.Vulcan_a(new Object[]{var11, new Long(var7), new Integer(0), new Integer(var2)});
      return var11.toString();
   }

   public String Vulcan_T(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_Q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_M(Object[] var1) {
      Object var4 = (Object)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 9122010043185L;
      long var7 = var2 ^ 127690271337337L;

      try {
         if (var4 == null) {
            return null;
         }
      } catch (IllegalStateException var10) {
         throw a(var10);
      }

      Vulcan_o var9 = (new Vulcan_o()).Vulcan_Dw(new Object[]{new Long(var7), var4});
      this.Vulcan_a(new Object[]{var9, new Long(var5), new Integer(0), new Integer(var9.Vulcan_S(new Object[0]))});
      return var9.toString();
   }

   public boolean Vulcan_P(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 43529906939514L;

      try {
         if (var4 == null) {
            return false;
         }
      } catch (IllegalStateException var7) {
         throw a(var7);
      }

      return this.Vulcan_V(new Object[]{var4, new Integer(0), new Long(var5), new Integer(var4.length())});
   }

   public boolean Vulcan_V(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_K(Object[] var1) {
      Vulcan_o var2 = (Vulcan_o)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 33594510941899L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalStateException var7) {
         throw a(var7);
      }

      return this.Vulcan_a(new Object[]{var2, new Long(var5), new Integer(0), new Integer(var2.Vulcan_S(new Object[0]))});
   }

   public boolean Vulcan_n(Object[] var1) {
      Vulcan_o var2 = (Vulcan_o)var1[0];
      int var6 = (Integer)var1[1];
      int var5 = (Integer)var1[2];
      long var3 = (Long)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 31722786572154L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalStateException var9) {
         throw a(var9);
      }

      return this.Vulcan_a(new Object[]{var2, new Long(var7), new Integer(var6), new Integer(var5)});
   }

   protected boolean Vulcan_a(Object[] var1) {
      Vulcan_o var2 = (Vulcan_o)var1[0];
      long var4 = (Long)var1[1];
      int var3 = (Integer)var1[2];
      int var6 = (Integer)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 106646907870913L;
      int[] var9 = ac.vulcan.anticheat.Vulcan_o.Vulcan_h();

      int var10000;
      label32: {
         try {
            var10000 = this.Vulcan_a(new Object[]{var2, new Integer(var3), new Integer(var6), null, new Long(var7)});
            if (var9 != null) {
               return (boolean)var10000;
            }

            if (var10000 > 0) {
               break label32;
            }
         } catch (IllegalStateException var10) {
            throw a(var10);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   private int Vulcan_a(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_B(Object[] var1) {
      String var2 = (String)var1[0];
      long var4 = (Long)var1[1];
      List var3 = (List)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 49919829470624L;
      long var8 = var4 ^ 111481574582380L;
      long var10 = var4 ^ 60841495675664L;

      try {
         if (!var3.contains(var2)) {
            return;
         }
      } catch (IllegalStateException var14) {
         throw a(var14);
      }

      Vulcan_o var12 = new Vulcan_o(256);
      String[] var13 = b;
      var12.Vulcan_FS(new Object[]{var13[7], new Long(var6)});
      var12.Vulcan_Dw(new Object[]{new Long(var10), var3.remove(0)});
      var12.Vulcan_FS(new Object[]{var13[5], new Long(var6)});
      var12.Vulcan_s1(new Object[]{var3, var13[1], new Long(var8)});
      throw new IllegalStateException(var12.toString());
   }

   protected String Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public char Vulcan_E(Object[] var1) {
      return this.Vulcan_o;
   }

   public void Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_o = (char)var2;
   }

   public Vulcan_eb Vulcan_q(Object[] var1) {
      return this.Vulcan_L;
   }

   public Vulcan_w Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_w Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 140165377423771L;
      return this.Vulcan_d(new Object[]{Vulcan_eb.Vulcan_O(new Object[]{new Integer(var4)}), new Long(var5)});
   }

   public Vulcan_w Vulcan_P(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 132939769894928L;
      long var7 = var3 ^ 87689902385920L;

      try {
         if (var2 == null) {
            throw new IllegalArgumentException(b[3]);
         }
      } catch (IllegalStateException var9) {
         throw a(var9);
      }

      return this.Vulcan_d(new Object[]{Vulcan_eb.Vulcan__(new Object[]{new Long(var5), var2}), new Long(var7)});
   }

   public Vulcan_eb Vulcan_Z(Object[] var1) {
      return this.Vulcan_G;
   }

   public Vulcan_w Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_w Vulcan_K(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 137240879026530L;
      return this.Vulcan_Z(new Object[]{Vulcan_eb.Vulcan_O(new Object[]{new Integer(var2)}), new Long(var5)});
   }

   public Vulcan_w Vulcan_W(Object[] var1) {
      String var2 = (String)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 138398279311875L;
      long var7 = var3 ^ 50185315155149L;

      try {
         if (var2 == null) {
            throw new IllegalArgumentException(b[6]);
         }
      } catch (IllegalStateException var9) {
         throw a(var9);
      }

      return this.Vulcan_Z(new Object[]{Vulcan_eb.Vulcan__(new Object[]{new Long(var5), var2}), new Long(var7)});
   }

   public Vulcan_eJ Vulcan_o(Object[] var1) {
      return this.Vulcan_g;
   }

   public void Vulcan_H(Object[] var1) {
      Vulcan_eJ var2 = (Vulcan_eJ)var1[0];
      this.Vulcan_g = var2;
   }

   public boolean Vulcan_g(Object[] var1) {
      return this.Vulcan_V;
   }

   public void Vulcan_m(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_V = var2;
   }

   static {
      long var8 = a ^ 84529869203843L;
      long var10 = var8 ^ 109643818027145L;
      String[] var5 = new String[8];
      int var3 = 0;
      String var2 = "\u000b\u001b$\u0016\b\u000bS8Z&\r\f\u000fV%Z;\u001e\u001d\nW8\bv\u0012\u001c\u001aK}\u00149\u000bI\u000bZ}\u0014#\u0013\u0005H\u0002pD)\u000b\u001b$\u0016\b\u000bS8Z%\n\u000f\u000fV%Z;\u001e\u001d\nW8\bv\u0012\u001c\u001aK}\u00149\u000bI\u000bZ}\u0014#\u0013\u0005H!\u000b\u001b$\u0016\b\u000bS8Z&\r\f\u000fV%Z;\n\u001a\u001d\u001f3\u0015\"_\u000b\f\u001f3\u000f:\u0013H\u0002y\u0001\u0002gZ";
      int var4 = "\u000b\u001b$\u0016\b\u000bS8Z&\r\f\u000fV%Z;\u001e\u001d\nW8\bv\u0012\u001c\u001aK}\u00149\u000bI\u000bZ}\u0014#\u0013\u0005H\u0002pD)\u000b\u001b$\u0016\b\u000bS8Z%\n\u000f\u000fV%Z;\u001e\u001d\nW8\bv\u0012\u001c\u001aK}\u00149\u000bI\u000bZ}\u0014#\u0013\u0005H!\u000b\u001b$\u0016\b\u000bS8Z&\r\f\u000fV%Z;\n\u001a\u001d\u001f3\u0015\"_\u000b\f\u001f3\u000f:\u0013H\u0002y\u0001\u0002gZ".length();
      char var1 = ')';
      int var0 = -1;

      while(true) {
         int var7;
         int var12;
         byte var14;
         byte var16;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var26;
         byte var27;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var14 = 2;
            var10001 = var10002;
            var12 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var16 = 2;
               var10005 = var7;
            } else {
               var14 = 2;
               var12 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var16 = 2;
               var10005 = var7;
            }

            while(true) {
               var26 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var27 = 95;
                  break;
               case 1:
                  var27 = 120;
                  break;
               case 2:
                  var27 = 84;
                  break;
               case 3:
                  var27 = 125;
                  break;
               case 4:
                  var27 = 107;
                  break;
               case 5:
                  var27 = 107;
                  break;
               default:
                  var27 = 61;
               }

               var10004[var10005] = (char)(var26 ^ var16 ^ var27);
               ++var7;
               if (var14 == 0) {
                  var10005 = var14;
                  var10004 = var10001;
                  var16 = var14;
               } else {
                  if (var12 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var16 = var14;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "\u007foPb|\u007f'L.Q~{{\"Q.O~nikGaV+\u007fxkG{Ng<+``Dbst?L.Ndrmk@`\u0002{or;L|Vr=t%]kP{rq*]gMe=r-\t";
            var4 = "\u007foPb|\u007f'L.Q~{{\"Q.O~nikGaV+\u007fxkG{Ng<+``Dbst?L.Ndrmk@`\u0002{or;L|Vr=t%]kP{rq*]gMe=r-\t".length();
            var1 = '!';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var14 = 118;
                  var10001 = var10002;
                  var12 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var16 = 118;
                     var10005 = var7;
                  } else {
                     var14 = 118;
                     var12 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var16 = 118;
                     var10005 = var7;
                  }

                  while(true) {
                     var26 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var27 = 95;
                        break;
                     case 1:
                        var27 = 120;
                        break;
                     case 2:
                        var27 = 84;
                        break;
                     case 3:
                        var27 = 125;
                        break;
                     case 4:
                        var27 = 107;
                        break;
                     case 5:
                        var27 = 107;
                        break;
                     default:
                        var27 = 61;
                     }

                     var10004[var10005] = (char)(var26 ^ var16 ^ var27);
                     ++var7;
                     if (var14 == 0) {
                        var10005 = var14;
                        var10004 = var10001;
                        var16 = var14;
                     } else {
                        if (var12 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var16 = var14;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;
                  String var15 = b[4];
                  Vulcan_E = Vulcan_eb.Vulcan__(new Object[]{new Long(var10), var15});
                  Vulcan_v = Vulcan_eb.Vulcan__(new Object[]{new Long(var10), "}"});
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalStateException a(IllegalStateException var0) {
      return var0;
   }
}
