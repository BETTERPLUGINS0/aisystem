package ac.vulcan.anticheat;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Map;

public class Vulcan_J extends MessageFormat {
   private static final long serialVersionUID = -2362048321261811743L;
   private static final int Vulcan_O = 31;
   private static final String Vulcan_G = "";
   private static final String Vulcan_R;
   private static final char Vulcan_T = ',';
   private static final char Vulcan_W = '}';
   private static final char Vulcan_g = '{';
   private static final char Vulcan_H = '\'';
   private String Vulcan_y;
   private final Map Vulcan_x;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(8550047714742281309L, -2093750165052151953L, (Object)null).a(231176336131217L);
   private static final String[] b;

   public Vulcan_J(String var1) {
      this(var1, Locale.getDefault());
   }

   public Vulcan_J(String var1, Locale var2) {
      this(var1, var2, (Map)null);
   }

   public Vulcan_J(String var1, Map var2) {
      this(var1, Locale.getDefault(), var2);
   }

   public Vulcan_J(String var1, Locale var2, Map var3) {
      super("");
      this.setLocale(var2);
      this.Vulcan_x = var3;
      this.applyPattern(var1);
   }

   public String toPattern() {
      return this.Vulcan_y;
   }

   public final void applyPattern(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void setFormat(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormatByArgumentIndex(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormats(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public void setFormatsByArgumentIndex(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      long var1 = a ^ 212354702311L;
      long var3 = var1 ^ 9890940161915L;
      int var5 = super.hashCode();
      int var10000 = 31 * var5;
      Map var10002 = this.Vulcan_x;
      var5 = var10000 + Vulcan_e8.Vulcan__(new Object[]{new Long(var3), var10002});
      var10000 = 31 * var5;
      String var6 = this.Vulcan_y;
      var5 = var10000 + Vulcan_e8.Vulcan__(new Object[]{new Long(var3), var6});
      return var5;
   }

   private Format Vulcan_o(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      int[] var5 = Vulcan_o.Vulcan_h();
      if (this.Vulcan_x != null) {
         String var6 = var4;
         String var7 = null;
         int var8 = var4.indexOf(44);

         label41: {
            label40: {
               try {
                  if (var5 != null) {
                     break label40;
                  }

                  if (var8 <= 0) {
                     break label41;
                  }
               } catch (IllegalArgumentException var11) {
                  throw a(var11);
               }

               var6 = var4.substring(0, var8).trim();
            }

            var7 = var4.substring(var8 + 1).trim();
         }

         Vulcan_en var9 = (Vulcan_en)this.Vulcan_x.get(var6);

         Vulcan_en var12;
         try {
            var12 = var9;
            if (var5 != null) {
               return var12.Vulcan_M(new Object[]{var6, var7, this.getLocale()});
            }

            if (var9 == null) {
               return null;
            }
         } catch (IllegalArgumentException var10) {
            throw a(var10);
         }

         var12 = var9;
         return var12.Vulcan_M(new Object[]{var6, var7, this.getLocale()});
      } else {
         return null;
      }
   }

   private int Vulcan_A(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private String Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private String Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_j(Object[] var1) {
      long var4 = (Long)var1[0];
      String var3 = (String)var1[1];
      ParsePosition var2 = (ParsePosition)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 107859274750013L;
      boolean var8 = false;
      char[] var9 = var3.toCharArray();

      int var10;
      do {
         var10 = Vulcan_eb.Vulcan_m(new Object[0]).Vulcan_q(new Object[]{new Long(var6), var9, new Integer(var2.getIndex())});
         var2.setIndex(var2.getIndex() + var10);
      } while(var10 > 0 && var2.getIndex() < var3.length());

   }

   private ParsePosition Vulcan_V(Object[] var1) {
      ParsePosition var2 = (ParsePosition)var1[0];
      var2.setIndex(var2.getIndex() + 1);
      return var2;
   }

   private Vulcan_o Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_D(Object[] var1) {
      long var4 = (Long)var1[0];
      String var6 = (String)var1[1];
      ParsePosition var3 = (ParsePosition)var1[2];
      boolean var2 = (Boolean)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 369109348361L;
      this.Vulcan_X(new Object[]{var6, var3, null, new Boolean(var2), new Long(var7)});
   }

   private boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[8];
      int var3 = 0;
      String var2 = "3$(Amz(U\u0002Yzbz(COV{qc,SOUxfc(I\u001b\u0010uw.=H\u001cY`ja#\u0007*]mx,K\u0006T4ea?J\u000eD4b|*R\u0002Uzw.$I\u000bUl#o9\u0007\u001f_gjz$H\u0001\u0010\u0002.#\u00023$(Amz(U\u0002Yzbz(COV{qc,SOUxfc(I\u001b\u0010uw.=H\u001cY`ja#\u0007";
      int var4 = "3$(Amz(U\u0002Yzbz(COV{qc,SOUxfc(I\u001b\u0010uw.=H\u001cY`ja#\u0007*]mx,K\u0006T4ea?J\u000eD4b|*R\u0002Uzw.$I\u000bUl#o9\u0007\u001f_gjz$H\u0001\u0010\u0002.#\u00023$(Amz(U\u0002Yzbz(COV{qc,SOUxfc(I\u001b\u0010uw.=H\u001cY`ja#\u0007".length();
      char var1 = 2;
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
            var10 = 109;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 109;
               var10005 = var7;
            } else {
               var10 = 109;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 109;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 121;
                  break;
               case 1:
                  var23 = 110;
                  break;
               case 2:
                  var23 = 99;
                  break;
               case 3:
                  var23 = 32;
                  break;
               case 4:
                  var23 = 74;
                  break;
               case 5:
                  var23 = 2;
                  break;
               default:
                  var23 = 93;
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
            var2 = "%\t\u0018L\"o5\u0012\u000b\u000f\t%d&\u001d\u0006\u001e\t&g1\u001d\u0002\u0004]cj P\u0017\u0005Z*\u007f=\u001f\tJ'%\t\u001eL1f=\u001e\u0006\u001eL'+%\u0005\b\u001eL'+'\u0004\u0015\u0003G$+5\u0004G\u001aF0b \u0019\b\u0004\t";
            var4 = "%\t\u0018L\"o5\u0012\u000b\u000f\t%d&\u001d\u0006\u001e\t&g1\u001d\u0002\u0004]cj P\u0017\u0005Z*\u007f=\u001f\tJ'%\t\u001eL1f=\u001e\u0006\u001eL'+%\u0005\b\u001eL'+'\u0004\u0015\u0003G$+5\u0004G\u001aF0b \u0019\b\u0004\t".length();
            var1 = '&';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 9;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 9;
                     var10005 = var7;
                  } else {
                     var10 = 9;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 9;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 121;
                        break;
                     case 1:
                        var23 = 110;
                        break;
                     case 2:
                        var23 = 99;
                        break;
                     case 3:
                        var23 = 32;
                        break;
                     case 4:
                        var23 = 74;
                        break;
                     case 5:
                        var23 = 2;
                        break;
                     default:
                        var23 = 93;
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
                  Vulcan_R = b[0];
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
