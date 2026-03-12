package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.ReservedChannelException;

public enum Vulcan_Xs {
   public static final Vulcan_Xs INSTANCE;
   private VulcanPlugin Vulcan_J;
   private long Vulcan_Y;
   private final String Vulcan_H = Vulcan_ik.spigot();
   private final String Vulcan_B = Vulcan_ik.nonce();
   private final String Vulcan_b = Vulcan_ik.resource();
   private final Logger Vulcan_V = Bukkit.getLogger();
   private boolean Vulcan_v;
   private boolean Vulcan_a;
   private boolean Vulcan_G;
   private boolean Vulcan_o;
   private boolean Vulcan_t;
   private boolean Vulcan_P;
   private boolean Vulcan_M;
   private boolean Vulcan_D;
   private boolean Vulcan_j;
   private boolean Vulcan_l;
   private boolean Vulcan_K;
   private final Map Vulcan_W = new HashMap();
   private final Vulcan_iz Vulcan_E = new Vulcan_iz();
   private final Vulcan_iR Vulcan_m = new Vulcan_iR();
   private final PluginManager Vulcan_Z = Bukkit.getServer().getPluginManager();
   private final Vulcan_m Vulcan_R = new Vulcan_m();
   private final Vulcan_X3 Vulcan_A = new Vulcan_X3();
   private final Vulcan_iV Vulcan_w = new Vulcan_iV();
   private final Vulcan_e7 Vulcan_e = new Vulcan_e7();
   private final Vulcan_eQ Vulcan_s = new Vulcan_eQ();
   private final Vulcan_X2 Vulcan_F = new Vulcan_X2();
   private final ExecutorService Vulcan_T = Executors.newSingleThreadExecutor();
   private final ExecutorService Vulcan_h = Executors.newSingleThreadExecutor();
   private final ExecutorService Vulcan_c = Executors.newSingleThreadExecutor();
   private final ExecutorService Vulcan_L = Executors.newSingleThreadExecutor();
   private static ac.vulcan.anticheat.Vulcan_n Vulcan_i;
   private final Map Vulcan_p = new HashMap();
   private int Vulcan_Q = 0;
   private int Vulcan_g = 0;
   private int Vulcan_y = 0;
   private static final Vulcan_Xs[] Vulcan_k;
   static final boolean Vulcan_I;
   private static String Vulcan_X;
   private static final long a = Vulcan_n.a(1582564131205653373L, 6286772334776397867L, MethodHandles.lookup().lookupClass()).a(271360992354173L);
   private static final String[] b;

   public void Vulcan_x(VulcanPlugin param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Y(VulcanPlugin param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_r() {
      long var1 = a ^ 99872333019097L;
      long var3 = var1 ^ 124093457997155L;
      long var5 = var1 ^ 34299764672246L;
      long var7 = var1 ^ 51334668889915L;
      long var9 = var1 ^ 137090771140539L;
      Vulcan_i9.Vulcan_o(new Object[]{var9});
      Vulcan_i9.Vulcan_S(new Object[]{var5});
      Vulcan_Xu.Vulcan_O(new Object[]{var3});
      Vulcan_Xu.Vulcan_g(new Object[]{var7});
      INSTANCE.Vulcan_J().reloadConfig();
   }

   public void Vulcan_d(String var1) {
      Bukkit.getLogger().log(Level.INFO, b[5] + var1);
   }

   public long Vulcan_s() {
      return this.Vulcan_Y;
   }

   public String Vulcan_v() {
      return this.Vulcan_H;
   }

   public String Vulcan_f() {
      return this.Vulcan_B;
   }

   public String Vulcan_u() {
      return this.Vulcan_b;
   }

   public Logger Vulcan_O() {
      return this.Vulcan_V;
   }

   public boolean Vulcan_c() {
      return this.Vulcan_v;
   }

   public boolean Vulcan_E() {
      return this.Vulcan_a;
   }

   public boolean Vulcan_w() {
      return this.Vulcan_G;
   }

   public boolean Vulcan_M() {
      return this.Vulcan_o;
   }

   public boolean Vulcan_b() {
      return this.Vulcan_t;
   }

   public boolean Vulcan_N() {
      return this.Vulcan_P;
   }

   public boolean Vulcan_Q() {
      return this.Vulcan_M;
   }

   public boolean Vulcan_p() {
      return this.Vulcan_D;
   }

   public boolean Vulcan_F() {
      return this.Vulcan_j;
   }

   public boolean Vulcan_y() {
      return this.Vulcan_l;
   }

   public boolean Vulcan_g() {
      return this.Vulcan_K;
   }

   public Map Vulcan_D() {
      return this.Vulcan_W;
   }

   public Vulcan_iz Vulcan_S() {
      return this.Vulcan_E;
   }

   public Vulcan_iR Vulcan_J() {
      return this.Vulcan_m;
   }

   public PluginManager Vulcan_d() {
      return this.Vulcan_Z;
   }

   public Vulcan_m Vulcan_a() {
      return this.Vulcan_R;
   }

   public Vulcan_X3 Vulcan_a() {
      return this.Vulcan_A;
   }

   public Vulcan_iV Vulcan_H() {
      return this.Vulcan_w;
   }

   public Vulcan_e7 Vulcan_e() {
      return this.Vulcan_e;
   }

   public Vulcan_eQ Vulcan_y() {
      return this.Vulcan_s;
   }

   public Vulcan_X2 Vulcan_q() {
      return this.Vulcan_F;
   }

   public ExecutorService Vulcan_V() {
      return this.Vulcan_T;
   }

   public ExecutorService Vulcan__() {
      return this.Vulcan_h;
   }

   public ExecutorService Vulcan_n() {
      return this.Vulcan_c;
   }

   public ExecutorService Vulcan_E() {
      return this.Vulcan_L;
   }

   public Map Vulcan_x() {
      return this.Vulcan_p;
   }

   public VulcanPlugin Vulcan_J() {
      return this.Vulcan_J;
   }

   public void Vulcan_F(boolean var1) {
      this.Vulcan_v = var1;
   }

   public void Vulcan_p(boolean var1) {
      this.Vulcan_a = var1;
   }

   public void Vulcan_m(boolean var1) {
      this.Vulcan_G = var1;
   }

   public void Vulcan_e(boolean var1) {
      this.Vulcan_o = var1;
   }

   public void Vulcan_H(boolean var1) {
      this.Vulcan_t = var1;
   }

   public void Vulcan_u(boolean var1) {
      this.Vulcan_P = var1;
   }

   public void Vulcan_q(boolean var1) {
      this.Vulcan_M = var1;
   }

   public void Vulcan_Q(boolean var1) {
      this.Vulcan_D = var1;
   }

   public void Vulcan_N(boolean var1) {
      this.Vulcan_j = var1;
   }

   public void Vulcan_j(boolean var1) {
      this.Vulcan_l = var1;
   }

   public void Vulcan_l(boolean var1) {
      this.Vulcan_K = var1;
   }

   public static ac.vulcan.anticheat.Vulcan_n Vulcan_w() {
      return Vulcan_i;
   }

   public int Vulcan_O() {
      return this.Vulcan_Q;
   }

   public int Vulcan_W() {
      return this.Vulcan_g;
   }

   public int Vulcan_B() {
      return this.Vulcan_y;
   }

   public void Vulcan_L(int var1) {
      this.Vulcan_Q = var1;
   }

   public void Vulcan_G(int var1) {
      this.Vulcan_g = var1;
   }

   public void Vulcan_E(int var1) {
      this.Vulcan_y = var1;
   }

   static {
      long var0 = a ^ 67938371977917L;
      Vulcan_G((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[41];
      int var7 = 0;
      String var6 = "o(\u0003ºM&Pº Þ)jLÉ±Þ\u000fK\u009a\u0085\u0081\\V¸\u001e{âsî[1R\u0010ö\rS\u008fú â>êBc~W\u000eJ°0\u0002b÷DwFõÃ-\u0093F>\u001dü\u009a2&1B-1¨^\u007fÇ\\v²¬®ÈÕ\u000ePTÄBÎ\u0014\u0093§rÑ2S¯ ß\bká\u0015XZ$H\u007f `~\u0092sëÌ\u009cf\u0097Ä½\n\u0099\u0002\u0010t ¹)VWþ\u000b(ó3§»ìÑ\u0083@\u0010ÛÈ\u0095\u001bïW·\r¡cåõ!òÿ\u0011(¯s°RD.\u009ay%\u0085Öä\u0016\u008dÔÀÔeÛ\u0011\ta\u000b\u009e\r´9\u001b\u000eg\u008f&\u0097ÕI\u0092\u008bÌ<ç(©þ\u0090\u0001\f¤Á\u0089 ô\u009f\u001cHÉ\n8÷\u0091¢o¿\nP`L\u0006ÓÉ´Å\u0099\u00829\u0019kiî?lØ\u0010¯s°RD.\u009ay;ÙI\u0006K4\u001c\u008f\u0010\u0012|lû(h\u0014î.¿A\u001cÎâ\u008eV(\u0083ú\u0005\u0094±\u008fìPºoì\u009c¢\u0098Â\u0090$\u007fÆ+ë§\u0089Tý¯\u0019-ê\u0083\u0007\u009e\u0007\u0082\u0082r\u0015)=¿\u0010ìç6àÏ\u0005\u0094v\u0000\u0006Ð¡Ñq¨Ü\u0010æÊ¿°%\u0086]vÍ¢\u0095\rdD7a(ÇC«þ\f^V\u0084V\u009c9ªæûæz}?\u009aìú/Ï ãt5\u0090`Ê\tÒ]äÍ\u0096\u009b\u0085Æì\bsÙ\u0080ýÀ¸¼\u0005\u0010ÇC«þ\f^V\u0084P->kê\u001d\u0091f\b%\u0000ñxK¶î\n\b\u001dÇ/=\u0097¹\u0096\u008c\u0010¯s°RD.\u009ay;ÙI\u0006K4\u001c\u008f\bøþ\u008f<`ú\u0000¤\u0010\u0083ú\u0005\u0094±\u008fìPfdið\u000eè½* jò\u0007Í..\u001bÅ9kèQ\u001fâ\u0005¿$\u0018µ\u00adw\u008eë;Òø\u0015r¬aaµ\bU÷×÷áÌÈ\u0082\bU÷×÷áÌÈ\u0082\u0010\u0010\u0003wT\u008cPÃË\u0089zÇ\u008bx\u0011\u001d\u0089\b`\u0014üD\u00116Ii\u0010ÜqmÒ!¶\u0087\u008b\u0016ÉØ\t\u008c\tã:\b÷\\KeÝ\u0090vÈ\u0018\u0083{«þi'¯à\u008b\\\u009d4M´¿Öw\u0097í3t¬yb\b8SßC\n\\\"7(ÝØçØ¯+aF*Û\u0096\u007f7\u008e½ýËå\n\u0007f_8Â\"Á\u0007>HÁM\u0090Fxh\u001a\u008f/Ñù Úm\u001a¹r9xVÏô\u000e¢\u0091l¿\u009e\u001bÁ4\fì\u009bz\u0001\u00129\u0095RÏÃ\b\u0094(ÝØçØ¯+aFÛ\u009aÙBÛ\u008dzÚÐ¦¿\nÑ0\u001bP\u008e³³f\u0088¯<\u0099\u000es¸/\u000ei\u008f¦ Úm\u001a¹r9xVWØÂÜëû>©¥LûPR5FÂüQG?¶_ 0x\u0086¹1e~f<\u0092'zµGí\u009cSä%\u001c\nJ\u0082ù\u0099Z÷Ôø\u008f)w'\u0083}\u0094\u0001\bþÚWÞ\u0085\u0001Ì÷\u0018\u0094\u009eV\u0007Èß44$\u000bMçgL~\u001fº\u001dó@ \u0004\u0001$m3\r`s\u0004\r\u008d®Ë@\u0007Uþ]]FVÌÇM÷Y\r\u008a\u001dý\u0004²\u009c\u0087\u0011\u0016\u0091¥\u001dþ¯\u00036\u0004è¨\u000b[ÀùÙ8ÐF(o(\u0003ºM&Pº\u000eïD¬õÌ®¨\u00881ÞH@Z\u0002rZ\u0011g=D\u0002A³¨ó\u0001°Bï%\u0088\u00103l-DºÒ\u0016\u001fØ¬õr.*Æo\u0010\u008a=\u0091_i\u0007vöÒXµ?ÞVl\u0018\u0010\u0083{«þi'¯à\u0015w\u0013þ\u0002\r°ì";
      int var8 = "o(\u0003ºM&Pº Þ)jLÉ±Þ\u000fK\u009a\u0085\u0081\\V¸\u001e{âsî[1R\u0010ö\rS\u008fú â>êBc~W\u000eJ°0\u0002b÷DwFõÃ-\u0093F>\u001dü\u009a2&1B-1¨^\u007fÇ\\v²¬®ÈÕ\u000ePTÄBÎ\u0014\u0093§rÑ2S¯ ß\bká\u0015XZ$H\u007f `~\u0092sëÌ\u009cf\u0097Ä½\n\u0099\u0002\u0010t ¹)VWþ\u000b(ó3§»ìÑ\u0083@\u0010ÛÈ\u0095\u001bïW·\r¡cåõ!òÿ\u0011(¯s°RD.\u009ay%\u0085Öä\u0016\u008dÔÀÔeÛ\u0011\ta\u000b\u009e\r´9\u001b\u000eg\u008f&\u0097ÕI\u0092\u008bÌ<ç(©þ\u0090\u0001\f¤Á\u0089 ô\u009f\u001cHÉ\n8÷\u0091¢o¿\nP`L\u0006ÓÉ´Å\u0099\u00829\u0019kiî?lØ\u0010¯s°RD.\u009ay;ÙI\u0006K4\u001c\u008f\u0010\u0012|lû(h\u0014î.¿A\u001cÎâ\u008eV(\u0083ú\u0005\u0094±\u008fìPºoì\u009c¢\u0098Â\u0090$\u007fÆ+ë§\u0089Tý¯\u0019-ê\u0083\u0007\u009e\u0007\u0082\u0082r\u0015)=¿\u0010ìç6àÏ\u0005\u0094v\u0000\u0006Ð¡Ñq¨Ü\u0010æÊ¿°%\u0086]vÍ¢\u0095\rdD7a(ÇC«þ\f^V\u0084V\u009c9ªæûæz}?\u009aìú/Ï ãt5\u0090`Ê\tÒ]äÍ\u0096\u009b\u0085Æì\bsÙ\u0080ýÀ¸¼\u0005\u0010ÇC«þ\f^V\u0084P->kê\u001d\u0091f\b%\u0000ñxK¶î\n\b\u001dÇ/=\u0097¹\u0096\u008c\u0010¯s°RD.\u009ay;ÙI\u0006K4\u001c\u008f\bøþ\u008f<`ú\u0000¤\u0010\u0083ú\u0005\u0094±\u008fìPfdið\u000eè½* jò\u0007Í..\u001bÅ9kèQ\u001fâ\u0005¿$\u0018µ\u00adw\u008eë;Òø\u0015r¬aaµ\bU÷×÷áÌÈ\u0082\bU÷×÷áÌÈ\u0082\u0010\u0010\u0003wT\u008cPÃË\u0089zÇ\u008bx\u0011\u001d\u0089\b`\u0014üD\u00116Ii\u0010ÜqmÒ!¶\u0087\u008b\u0016ÉØ\t\u008c\tã:\b÷\\KeÝ\u0090vÈ\u0018\u0083{«þi'¯à\u008b\\\u009d4M´¿Öw\u0097í3t¬yb\b8SßC\n\\\"7(ÝØçØ¯+aF*Û\u0096\u007f7\u008e½ýËå\n\u0007f_8Â\"Á\u0007>HÁM\u0090Fxh\u001a\u008f/Ñù Úm\u001a¹r9xVÏô\u000e¢\u0091l¿\u009e\u001bÁ4\fì\u009bz\u0001\u00129\u0095RÏÃ\b\u0094(ÝØçØ¯+aFÛ\u009aÙBÛ\u008dzÚÐ¦¿\nÑ0\u001bP\u008e³³f\u0088¯<\u0099\u000es¸/\u000ei\u008f¦ Úm\u001a¹r9xVWØÂÜëû>©¥LûPR5FÂüQG?¶_ 0x\u0086¹1e~f<\u0092'zµGí\u009cSä%\u001c\nJ\u0082ù\u0099Z÷Ôø\u008f)w'\u0083}\u0094\u0001\bþÚWÞ\u0085\u0001Ì÷\u0018\u0094\u009eV\u0007Èß44$\u000bMçgL~\u001fº\u001dó@ \u0004\u0001$m3\r`s\u0004\r\u008d®Ë@\u0007Uþ]]FVÌÇM÷Y\r\u008a\u001dý\u0004²\u009c\u0087\u0011\u0016\u0091¥\u001dþ¯\u00036\u0004è¨\u000b[ÀùÙ8ÐF(o(\u0003ºM&Pº\u000eïD¬õÌ®¨\u00881ÞH@Z\u0002rZ\u0011g=D\u0002A³¨ó\u0001°Bï%\u0088\u00103l-DºÒ\u0016\u001fØ¬õr.*Æo\u0010\u008a=\u0091_i\u0007vöÒXµ?ÞVl\u0018\u0010\u0083{«þi'¯à\u0015w\u0013þ\u0002\r°ì".length();
      char var5 = ' ';
      int var4 = -1;

      label42:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var17 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var17;
               if ((var4 += var5) >= var8) {
                  b = var9;

                  boolean var14;
                  label29: {
                     try {
                        if (!Vulcan_Xs.class.desiredAssertionStatus()) {
                           var14 = true;
                           break label29;
                        }
                     } catch (ReservedChannelException var11) {
                        throw a((Exception)var11);
                     }

                     var14 = false;
                  }

                  Vulcan_I = var14;
                  INSTANCE = new Vulcan_Xs(b[37], 0);
                  Vulcan_k = new Vulcan_Xs[]{INSTANCE};
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var17;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label42;
               }

               var6 = "ÛÈ\u0095\u001bïW·\r¶ë\u007fËÌe\u001d¡üzE\u0087\u001e\u0083û ¨\u0014ª]?`Ùÿu\u008a\u0085»\u0005Ò\u007fýÛIü)XpOq\u0010þ\u001bÌZòóÈÑP\u0092§û\u0014\u0082{\u0012";
               var8 = "ÛÈ\u0095\u001bïW·\r¶ë\u007fËÌe\u001d¡üzE\u0087\u001e\u0083û ¨\u0014ª]?`Ùÿu\u008a\u0085»\u0005Ò\u007fýÛIü)XpOq\u0010þ\u001bÌZòóÈÑP\u0092§û\u0014\u0082{\u0012".length();
               var5 = '0';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   public static void Vulcan_G(String var0) {
      Vulcan_X = var0;
   }

   public static String Vulcan_V() {
      return Vulcan_X;
   }

   private static Exception a(Exception var0) {
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
