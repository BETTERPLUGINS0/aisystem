package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_XU {
   private final Vulcan_iE Vulcan_q;
   private int Vulcan_R;
   private final Map Vulcan_f = new HashMap();
   private final Map Vulcan_W = new HashMap();
   private long Vulcan_s;
   private long Vulcan_P;
   private long Vulcan_V = System.currentTimeMillis();
   private long Vulcan_J = System.currentTimeMillis();
   private long Vulcan_t = System.currentTimeMillis();
   private boolean Vulcan_L;
   private double Vulcan_a;
   private boolean Vulcan_o;
   private int Vulcan_i;
   private boolean Vulcan_M;
   private static String Vulcan_D;
   private static final long a = Vulcan_n.a(5869558297336469063L, -713694148234316646L, MethodHandles.lookup().lookupClass()).a(81867527340463L);
   private static final String[] b;

   public Vulcan_XU(Vulcan_iE var1) {
      this.Vulcan_q = var1;
   }

   public void Vulcan_H(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_V(Object[] var1) {
      this.Vulcan_J = System.currentTimeMillis();
   }

   private void Vulcan_a(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_F(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iE Vulcan_w(Object[] var1) {
      return this.Vulcan_q;
   }

   public int Vulcan_U(Object[] var1) {
      return this.Vulcan_R;
   }

   public Map Vulcan_C(Object[] var1) {
      return this.Vulcan_f;
   }

   public Map Vulcan_S(Object[] var1) {
      return this.Vulcan_W;
   }

   public long Vulcan_o(Object[] var1) {
      return this.Vulcan_s;
   }

   public long Vulcan_s(Object[] var1) {
      return this.Vulcan_P;
   }

   public long Vulcan_K(Object[] var1) {
      return this.Vulcan_V;
   }

   public long Vulcan_L(Object[] var1) {
      return this.Vulcan_J;
   }

   public long Vulcan_O(Object[] var1) {
      return this.Vulcan_t;
   }

   public boolean Vulcan_z(Object[] var1) {
      return this.Vulcan_L;
   }

   public double Vulcan_W(Object[] var1) {
      return this.Vulcan_a;
   }

   public boolean Vulcan_K(Object[] var1) {
      return this.Vulcan_o;
   }

   public int Vulcan_b(Object[] var1) {
      return this.Vulcan_i;
   }

   public boolean Vulcan_b(Object[] var1) {
      return this.Vulcan_M;
   }

   public void Vulcan_A(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_R = var2;
   }

   public void Vulcan_L(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_s = var2;
   }

   public void Vulcan_w(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_P = var2;
   }

   public void Vulcan_D(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_V = var2;
   }

   public void Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_J = var2;
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_t = var2;
   }

   public void Vulcan_q(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan_j(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_a = var2;
   }

   public void Vulcan_c(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_o = var2;
   }

   public void Vulcan_b(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_i = var2;
   }

   public void Vulcan_S(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M = var2;
   }

   private void lambda$handleMaxPingKick$1() {
      long var1 = a ^ 109704838439430L;
      long var3 = var1 ^ 61393175033643L;
      this.Vulcan_q.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_h1, var3}));
   }

   private void lambda$confirmKeepAliveConnection$0() {
      long var1 = a ^ 6378592610548L;
      long var3 = var1 ^ 90012619144665L;
      this.Vulcan_q.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_k, var3}));
   }

   public static void Vulcan_o(String var0) {
      Vulcan_D = var0;
   }

   public static String Vulcan_J() {
      return Vulcan_D;
   }

   static {
      long var0 = a ^ 41203387095384L;
      Vulcan_o((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[8];
      int var7 = 0;
      String var6 = "Éåpl\"âj\u001a\u0098\u0006n`P¥ê¶\u0098ã\u008bÿÒ£sàÀ/-\\¿GOL<· ñ7[\u00862\b\n-{<W\u0098\u0004]\u0010\u0091ç\u0095ù-°æ\u0084û\u001e¥¹þ\nö$\bÎ{Ñ\u00ad\u00059¢\u000b\b\n-{<W\u0098\u0004]\u0010\u0091ç\u0095ù-°æ\u0084û\u001e¥¹þ\nö$";
      int var8 = "Éåpl\"âj\u001a\u0098\u0006n`P¥ê¶\u0098ã\u008bÿÒ£sàÀ/-\\¿GOL<· ñ7[\u00862\b\n-{<W\u0098\u0004]\u0010\u0091ç\u0095ù-°æ\u0084û\u001e¥¹þ\nö$\bÎ{Ñ\u00ad\u00059¢\u000b\b\n-{<W\u0098\u0004]\u0010\u0091ç\u0095ù-°æ\u0084û\u001e¥¹þ\nö$".length();
      char var5 = '(';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var15;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "Éåpl\"âj\u001a\u0098\u0006n`P¥ê¶\u0098ã\u008bÿÒ£sàIQè>bÎ\u0002\u0098¦æ¶\u0099pn\u0090¢\bÎ{Ñ\u00ad\u00059¢\u000b";
               var8 = "Éåpl\"âj\u001a\u0098\u0006n`P¥ê¶\u0098ã\u008bÿÒ£sàIQè>bÎ\u0002\u0098¦æ¶\u0099pn\u0090¢\bÎ{Ñ\u00ad\u00059¢\u000b".length();
               var5 = '(';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   private static String a(byte[] var0) {
      int var1 = 0;
      int var2;
      char[] var3 = new char[var2 = var0.length];

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5;
         if ((var5 = 255 & var0[var4]) < 192) {
            var3[var1++] = (char)var5;
         } else {
            char var6;
            byte var7;
            if (var5 < 224) {
               var6 = (char)((char)(var5 & 31) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            } else if (var4 < var2 - 2) {
               var6 = (char)((char)(var5 & 15) << 12);
               ++var4;
               var7 = var0[var4];
               var6 = (char)(var6 | (char)(var7 & 63) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            }
         }
      }

      return new String(var3, 0, var1);
   }
}
