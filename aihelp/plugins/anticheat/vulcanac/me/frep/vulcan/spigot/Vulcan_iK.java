package me.frep.vulcan.spigot;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public enum Vulcan_iK implements Predicate, Vulcan_i4 {
   public static final Vulcan_iK X;
   public static final Vulcan_iK Y;
   public static final Vulcan_iK Z;
   private static final Map Vulcan_k;
   private final String Vulcan_Z;
   private final Vulcan_e2 Vulcan_o;
   private static final Vulcan_iK[] Vulcan_W;
   private static final String Vulcan_l;
   private static final Vulcan_iK[] Vulcan_V;
   private static final long a = Vulcan_n.a(7186626009161589742L, 2480028711437963514L, MethodHandles.lookup().lookupClass()).a(133146182357529L);

   private Vulcan_iK(String var3, int var4, String var5, Vulcan_e2 var6) {
      this.Vulcan_Z = var5;
      this.Vulcan_o = var6;
   }

   public static Vulcan_iK Vulcan_L(String param0) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_S() {
      return this.Vulcan_Z;
   }

   public boolean Vulcan_a() {
      long var1 = a ^ 115028491831433L;

      boolean var10000;
      try {
         if (this.Vulcan_o == Vulcan_e2.VERTICAL) {
            var10000 = true;
            return var10000;
         }
      } catch (RuntimeException var3) {
         throw a(var3);
      }

      var10000 = false;
      return var10000;
   }

   public boolean Vulcan_u() {
      long var1 = a ^ 115508955598616L;

      boolean var10000;
      try {
         if (this.Vulcan_o == Vulcan_e2.HORIZONTAL) {
            var10000 = true;
            return var10000;
         }
      } catch (RuntimeException var3) {
         throw a(var3);
      }

      var10000 = false;
      return var10000;
   }

   public String toString() {
      return this.Vulcan_Z;
   }

   public boolean Vulcan_D(Vulcan_Xy var1) {
      long var2 = a ^ 128951512330527L;
      String var4 = Vulcan_il.Vulcan_U();

      boolean var7;
      label28: {
         Vulcan_Xy var10000;
         label27: {
            try {
               var10000 = var1;
               if (var4 == null) {
                  break label27;
               }

               if (var1 == null) {
                  break label28;
               }
            } catch (RuntimeException var6) {
               throw a(var6);
            }

            var10000 = var1;
         }

         try {
            if (var10000.Vulcan_F() == this) {
               var7 = true;
               return var7;
            }
         } catch (RuntimeException var5) {
            throw a(var5);
         }
      }

      var7 = false;
      return var7;
   }

   public Vulcan_e2 Vulcan_v() {
      return this.Vulcan_o;
   }

   public String Vulcan_h() {
      return this.Vulcan_Z;
   }

   public boolean apply(Object var1) {
      return this.Vulcan_D((Vulcan_Xy)var1);
   }

   static {
      char[] var10003 = "\fY\rQ\u0000W\u000b}&`P".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var8 = 32;
      char[] var10002 = var10003;
      int var5 = var10004;
      Vulcan_iK[] var1;
      int var2;
      int var3;
      Vulcan_iK var4;
      boolean var7;
      char[] var9;
      String var10;
      byte var13;
      String var10000;
      int var10005;
      char var10006;
      byte var10007;
      char var10008;
      if (var10004 <= 1) {
         var9 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 32;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var13 = 111;
            break;
         case 1:
            var13 = 53;
            break;
         case 2:
            var13 = 114;
            break;
         case 3:
            var13 = 65;
            break;
         case 4:
            var13 = 16;
            break;
         case 5:
            var13 = 71;
            break;
         default:
            var13 = 27;
         }
      } else {
         var8 = 32;
         var5 = var10004;
         if (var10004 <= var0) {
            var10 = (new String(var10003)).intern();
            var7 = true;
            Vulcan_l = var10;
            var10000 = Vulcan_l;
            X = new Vulcan_iK("X", 0, "X", 0, "x", Vulcan_e2.HORIZONTAL);
            Y = new Vulcan_iK("Y", 1, "Y", 1, "y", Vulcan_e2.VERTICAL);
            Z = new Vulcan_iK("Z", 2, "Z", 2, "z", Vulcan_e2.HORIZONTAL);
            Vulcan_V = new Vulcan_iK[]{X, Y, Z};
            Vulcan_k = Maps.newHashMap();
            Vulcan_W = new Vulcan_iK[]{X, Y, Z};
            var1 = values();
            var2 = var1.length;

            for(var3 = 0; var3 < var2; ++var3) {
               var4 = var1[var3];
               Vulcan_k.put(var4.Vulcan_S().toLowerCase(), var4);
            }

            return;
         }

         var9 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 32;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var13 = 111;
            break;
         case 1:
            var13 = 53;
            break;
         case 2:
            var13 = 114;
            break;
         case 3:
            var13 = 65;
            break;
         case 4:
            var13 = 16;
            break;
         case 5:
            var13 = 71;
            break;
         default:
            var13 = 27;
         }
      }

      while(true) {
         while(true) {
            var9[var10005] = (char)(var10006 ^ var10007 ^ var13);
            ++var0;
            if (var8 == 0) {
               var9 = var10002;
               var10005 = var8;
               var10008 = var10002[var8];
               var10007 = var8;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var13 = 111;
                  break;
               case 1:
                  var13 = 53;
                  break;
               case 2:
                  var13 = 114;
                  break;
               case 3:
                  var13 = 65;
                  break;
               case 4:
                  var13 = 16;
                  break;
               case 5:
                  var13 = 71;
                  break;
               default:
                  var13 = 27;
               }
            } else {
               if (var5 <= var0) {
                  var10 = (new String(var10002)).intern();
                  var7 = true;
                  Vulcan_l = var10;
                  var10000 = Vulcan_l;
                  X = new Vulcan_iK("X", 0, "X", 0, "x", Vulcan_e2.HORIZONTAL);
                  Y = new Vulcan_iK("Y", 1, "Y", 1, "y", Vulcan_e2.VERTICAL);
                  Z = new Vulcan_iK("Z", 2, "Z", 2, "z", Vulcan_e2.HORIZONTAL);
                  Vulcan_V = new Vulcan_iK[]{X, Y, Z};
                  Vulcan_k = Maps.newHashMap();
                  Vulcan_W = new Vulcan_iK[]{X, Y, Z};
                  var1 = values();
                  var2 = var1.length;

                  for(var3 = 0; var3 < var2; ++var3) {
                     var4 = var1[var3];
                     Vulcan_k.put(var4.Vulcan_S().toLowerCase(), var4);
                  }

                  return;
               }

               var9 = var10002;
               var10005 = var0;
               var10008 = var10002[var0];
               var10007 = var8;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var13 = 111;
                  break;
               case 1:
                  var13 = 53;
                  break;
               case 2:
                  var13 = 114;
                  break;
               case 3:
                  var13 = 65;
                  break;
               case 4:
                  var13 = 16;
                  break;
               case 5:
                  var13 = 71;
                  break;
               default:
                  var13 = 27;
               }
            }
         }
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
