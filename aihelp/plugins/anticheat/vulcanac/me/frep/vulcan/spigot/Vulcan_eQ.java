package me.frep.vulcan.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.api.event.VulcanDisableAlertsEvent;
import me.frep.vulcan.api.event.VulcanEnableAlertsEvent;
import me.frep.vulcan.spigot.check.AbstractCheck;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Vulcan_eQ {
   private final Set Vulcan_R = new HashSet();
   private final Set Vulcan_N = new HashSet();
   private final DecimalFormat Vulcan_n;
   private static AbstractCheck[] Vulcan_v;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4442766774794203740L, -8705625654148104188L, MethodHandles.lookup().lookupClass()).a(236929464241931L);
   private static final String[] b;

   public Vulcan_eQ() {
      this.Vulcan_n = new DecimalFormat(b[38]);
   }

   public void Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_P(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_p(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private String Vulcan_v(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 104951163038992L;

      try {
         switch(var2) {
         case 1:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_QS, var5});
         case 2:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_Fq, var5});
         case 3:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_FL, var5});
         case 4:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_s, var5});
         case 5:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_Qp, var5});
         case 6:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ak, var5});
         case 7:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_hC, var5});
         case 8:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_z, var5});
         case 9:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_QC, var5});
         case 10:
            return Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_Fo, var5});
         default:
            return "";
         }
      } catch (RuntimeException var7) {
         throw a((Exception)var7);
      }
   }

   public void Vulcan_I(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_R.forEach(Vulcan_eQ::lambda$sendMessage$18);
   }

   private void Vulcan__(Object[] var1) {
      long var3 = (Long)var1[0];
      Player var2 = (Player)var1[1];
      String var5 = (String)var1[2];
      long var10000 = a ^ var3;

      try {
         if (!Vulcan_i9.Vulcan_Qh) {
            return;
         }
      } catch (RuntimeException var8) {
         throw a((Exception)var8);
      }

      ByteArrayDataOutput var6 = ByteStreams.newDataOutput();
      String[] var7 = b;
      var6.writeUTF(var7[25]);
      var6.writeUTF(var5);
      var2.sendPluginMessage(Vulcan_Xs.INSTANCE.Vulcan_J(), var7[65], var6.toByteArray());
   }

   public Set Vulcan_K(Object[] var1) {
      return this.Vulcan_R;
   }

   public Set Vulcan_j(Object[] var1) {
      return this.Vulcan_N;
   }

   private static void lambda$sendMessage$18(String var0, Player var1) {
      long var2 = a ^ 116907181698333L;
      long var4 = var2 ^ 112471548969380L;
      var1.sendMessage(Vulcan_eR.Vulcan_m(new Object[]{var0, var4}));
   }

   private static void lambda$handleApiAlert$17(String var0, Vulcan_iE var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12) {
      Vulcan_i9.Vulcan_QE.forEach(Vulcan_eQ::lambda$null$16);
   }

   private static void lambda$null$16(String param0, Vulcan_iE param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handleApiAlert$15(Vulcan_iE var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12) {
      Vulcan_i9.Vulcan_QE.forEach(Vulcan_eQ::lambda$null$14);
   }

   private static void lambda$null$14(Vulcan_iE param0, String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleApiAlert$13(TextComponent var1) {
      this.Vulcan_R.forEach(Vulcan_eQ::lambda$null$12);
   }

   private static void lambda$null$12(TextComponent var0, Player var1) {
      var1.spigot().sendMessage(var0);
   }

   private static void lambda$handleApiAlert$11(TextComponent var0, Player var1) {
      var1.spigot().sendMessage(var0);
   }

   private static void lambda$handleAlert$10(AbstractCheck param0, int param1, Vulcan_iE param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13, String param14, String param15, String param16, String param17) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handleAlert$9(String var0, String var1, Vulcan_iE var2, String var3, AbstractCheck var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13) {
      Vulcan_i9.Vulcan_QE.forEach(Vulcan_eQ::lambda$null$8);
   }

   private static void lambda$null$8(String param0, String param1, Vulcan_iE param2, String param3, AbstractCheck param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13, String param14) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handleAlert$7(Vulcan_iE var0, String var1, String var2, AbstractCheck var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13) {
      Vulcan_i9.Vulcan_QE.forEach(Vulcan_eQ::lambda$null$6);
   }

   private static void lambda$null$6(Vulcan_iE param0, String param1, String param2, AbstractCheck param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13, String param14) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleAlert$5(TextComponent var1) {
      this.Vulcan_R.forEach(Vulcan_eQ::lambda$null$4);
   }

   private static void lambda$null$4(TextComponent var0, Player var1) {
      var1.spigot().sendMessage(var0);
   }

   private static void lambda$handleAlert$3(TextComponent var0, Player var1) {
      var1.spigot().sendMessage(var0);
   }

   private static void lambda$handleAlert$2(AbstractCheck var0, Vulcan_iE var1, String var2) {
      long var3 = a ^ 117325088565972L;
      long var5 = var3 ^ 26318131129720L;
      Vulcan_Xh.Vulcan_E(new Object[]{var0, var1, var5, var2});
   }

   private static void lambda$toggleAlerts$1(VulcanEnableAlertsEvent var0) {
      Bukkit.getPluginManager().callEvent(var0);
   }

   private static void lambda$toggleAlerts$0(VulcanDisableAlertsEvent var0) {
      Bukkit.getPluginManager().callEvent(var0);
   }

   public static void Vulcan_d(AbstractCheck[] var0) {
      Vulcan_v = var0;
   }

   public static AbstractCheck[] Vulcan_T() {
      return Vulcan_v;
   }

   static {
      long var0 = a ^ 108088264976638L;
      Vulcan_d((AbstractCheck[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[93];
      int var7 = 0;
      String var6 = "cºIrRÖ}\u0001~0em\u0099A«å\u0018ÕÃ0Ï\u00945ß\n¤\u0011Y\u009fM\u009eÂé\u0086ÂÓ\u001b\u0017ÙY\u008a\u0010H\u0000\u0011\u0001jý1bÖ\u001aðìíót°\b,Cã-Ç\u0016ýÊ\u0010¼a)s\u0014\u008ep¬ú\\\u0012A\u009dÒOí\b\u0093\u0084/ÝXm]ò\bÐ$\u0097\u0082¡¢J\u009c\b\u0097\u0013éÈ{Ü7£\u0018Ý\u0003M\u0092\u000es\u009c'NÀô8\u008b\u0017eñ\u0088f¢þic[\u009e\b\u0097\u0013éÈ{Ü7£\bW²±?%\u008c¨-\u0010\u008f\u0018\u0019ÝÕggBñ\u0006Bé<\u0015E\u001d\u0010\u001d£9\u0010\u0010Á<\u0012z\u0098\u0013þ«V\u001bû\bþ\u000fgÍ\u001eæöÍ\u0018[l\u0005£2VTOC\u0098¸¸\u001e/Ðëoây\u0007\u008b%Ný\u0010Fã7^Ú\fãø\u0082»x_\u0094\u0084x¸\b\u0001Õ¹\u0004!ÝÓ0\b\b×\u007fXôù¯f\b¤Ñ\u0006Þú\u008b\u0096a\u0010Ë¬Å'\u0089\u008eÃ&z-¢ú\u0015j\u0015\u0087\u0010\u009e)bWµö!\u000b§\u008e|Éøl.\u0016\b`òÊ\u0001éU\u0099H\u0010?b+ôÎa\u0089\u0088õ\u0093×<ãÔöÓ\b\r\u0006@vâ\r´>\b§¯ÃÌ×\u001fÇd\b-=\u0096\\ôW¨ä\u0010\u008f\u0018\u0019ÝÕggBñ\u0006Bé<\u0015E\u001d\u0018Øh¦\u0014Ã««\u000e\u0085\u0097yHñ&\u0010Ï/ÊSªÁ$¸\"\u0010Ç+R³.÷Æä\u0097eB\u0099q\u008b¸V\u0010\u000f\u0081¯-ø\u0093ö\u0082\n\u0001³\u0093»51\u00ad\b`òÊ\u0001éU\u0099H\u0010\u001d£9\u0010\u0010Á<\u0012z\u0098\u0013þ«V\u001bû\u0010LÖÙ*\u001f\u009f¨àÊ\u0089ãZÌª\u0092ü\u00102§\u0099ð\u0095,\"1dµ'/\u0084WF×\bZ¾\u0091Ðs\u001a\u0086\u0013\b\fÙÞ.\u009d=m\u009d\u0010\u0082m\u008b:Õ\u0011@>è\u001d\u008bé\u0089Ü2¹\bÙ\u0099¸z\u001fâÇÝ\b\u0082À,¥GË\u009dÔ\u0018Ë¬Å'\u0089\u008eÃ&\u0099\u001dÉ¥\u009eù4Wéeá)\u009bu5¹\b\u0082Åc\u001d\u001añ×l\u0010\u000f\u0081¯-ø\u0093ö\u0082\n\u0001³\u0093»51\u00ad\b\u0018\u0099H=S$Y²\u0010I¾ç\u000f\u001b½«\u000b^\u0095K¹'5®w\u0010þÿ\u001cã\u0093¹\u007fö\u0096\u0082ÅAUî«\u008a\u0010Ë¬Å'\u0089\u008eÃ&z-¢ú\u0015j\u0015\u0087\b\u0093\u0084/ÝXm]ò\u0018Øh¦\u0014Ã««\u000e\u0085\u0097yHñ&\u0010Ï/ÊSªÁ$¸\"\u0010H\u0000\u0011\u0001jý1bÖ\u001aðìíót°\u0010\u0001æõ³÷¾G\u0006 \u0080Iq\u001cÈUð\b\u0001Õ¹\u0004!ÝÓ0\u0010(^²á$¾dS\u0015\u009aéá0WË<\u0010Úñ\u0006âmJ9\u009e·\u001eÕß¼ªs\f\u0018Ë¬Å'\u0089\u008eÃ&\u0099\u001dÉ¥\u009eù4Wéeá)\u009bu5¹\bIÎ\u008fÒlæ[¥\brýg\u001a\u00adHF\u0082\u0010I¾ç\u000f\u001b½«\u000bjd\u0012Émá\u007f§\u0010\u0086\u0004\u009eLÛ\u0001#R\u0002\t\u0087¥j\u00868\"\b\u0018\u0099H=S$Y²\u0010Úñ\u0006âmJ9\u009e·\u001eÕß¼ªs\f\u0010e./v\u0093W\u0082\u001e\"d\u0088,tÎYl\bÐ$\u0097\u0082¡¢J\u009c\u0010Þ±êû\u007f)h¾z\u008a\b\u0095\u0088_\u001fé\u0010\u009e)bWµö!\u000b§\u008e|Éøl.\u0016\bÍD\u0016Ù\u000b!r\u0092\u0010#g[ ë.§$¼`TwF¹5ñ\b\r\u0006@vâ\r´>\bW²±?%\u008c¨-\b§¯ÃÌ×\u001fÇd\bþ\u000fgÍ\u001eæöÍ\b¨°£!EIQß\brýg\u001a\u00adHF\u0082\u0018ÕÃ0Ï\u00945ß\n¤\u0011Y\u009fM\u009eÂé\u0086ÂÓ\u001b\u0017ÙY\u008a\b\b×\u007fXôù¯f\u0010\t\u008d)(¿\u0003\u0010ÿf_¨\u0089\u0002Õ\u0002º\u0010\u0082m\u008b:Õ\u0011@>è\u001d\u008bé\u0089Ü2¹\b<·ÔAL5\u007f¤\u0010Fã7^Ú\fãø\u0082»x_\u0094\u0084x¸\u0018[l\u0005£2VTOC\u0098¸¸\u001e/Ðëoây\u0007\u008b%Ný\u0010Þ±êû\u007f)h¾z\u008a\b\u0095\u0088_\u001fé\b\u0082Åc\u001d\u001añ×l\bÍD\u0016Ù\u000b!r\u0092\u0010\u0082m\u008b:Õ\u0011@>M\u0086p.\u008d\u0014\u000b:\b<·ÔAL5\u007f¤\u0010{Þî(\u0011\u0083\u0003ñ8\u008f\u008c§\tù\u0093Ò\bÙ\u0099¸z\u001fâÇÝ\u0010\u0082m\u008b:Õ\u0011@>M\u0086p.\u008d\u0014\u000b:\u0010\u0086\u0004\u009eLÛ\u0001#R\u0002\t\u0087¥j\u00868\"\b,Cã-Ç\u0016ýÊ\b\fÙÞ.\u009d=m\u009d\u0010LÖÙ*\u001f\u009f¨àÊ\u0089ãZÌª\u0092ü";
      int var8 = "cºIrRÖ}\u0001~0em\u0099A«å\u0018ÕÃ0Ï\u00945ß\n¤\u0011Y\u009fM\u009eÂé\u0086ÂÓ\u001b\u0017ÙY\u008a\u0010H\u0000\u0011\u0001jý1bÖ\u001aðìíót°\b,Cã-Ç\u0016ýÊ\u0010¼a)s\u0014\u008ep¬ú\\\u0012A\u009dÒOí\b\u0093\u0084/ÝXm]ò\bÐ$\u0097\u0082¡¢J\u009c\b\u0097\u0013éÈ{Ü7£\u0018Ý\u0003M\u0092\u000es\u009c'NÀô8\u008b\u0017eñ\u0088f¢þic[\u009e\b\u0097\u0013éÈ{Ü7£\bW²±?%\u008c¨-\u0010\u008f\u0018\u0019ÝÕggBñ\u0006Bé<\u0015E\u001d\u0010\u001d£9\u0010\u0010Á<\u0012z\u0098\u0013þ«V\u001bû\bþ\u000fgÍ\u001eæöÍ\u0018[l\u0005£2VTOC\u0098¸¸\u001e/Ðëoây\u0007\u008b%Ný\u0010Fã7^Ú\fãø\u0082»x_\u0094\u0084x¸\b\u0001Õ¹\u0004!ÝÓ0\b\b×\u007fXôù¯f\b¤Ñ\u0006Þú\u008b\u0096a\u0010Ë¬Å'\u0089\u008eÃ&z-¢ú\u0015j\u0015\u0087\u0010\u009e)bWµö!\u000b§\u008e|Éøl.\u0016\b`òÊ\u0001éU\u0099H\u0010?b+ôÎa\u0089\u0088õ\u0093×<ãÔöÓ\b\r\u0006@vâ\r´>\b§¯ÃÌ×\u001fÇd\b-=\u0096\\ôW¨ä\u0010\u008f\u0018\u0019ÝÕggBñ\u0006Bé<\u0015E\u001d\u0018Øh¦\u0014Ã««\u000e\u0085\u0097yHñ&\u0010Ï/ÊSªÁ$¸\"\u0010Ç+R³.÷Æä\u0097eB\u0099q\u008b¸V\u0010\u000f\u0081¯-ø\u0093ö\u0082\n\u0001³\u0093»51\u00ad\b`òÊ\u0001éU\u0099H\u0010\u001d£9\u0010\u0010Á<\u0012z\u0098\u0013þ«V\u001bû\u0010LÖÙ*\u001f\u009f¨àÊ\u0089ãZÌª\u0092ü\u00102§\u0099ð\u0095,\"1dµ'/\u0084WF×\bZ¾\u0091Ðs\u001a\u0086\u0013\b\fÙÞ.\u009d=m\u009d\u0010\u0082m\u008b:Õ\u0011@>è\u001d\u008bé\u0089Ü2¹\bÙ\u0099¸z\u001fâÇÝ\b\u0082À,¥GË\u009dÔ\u0018Ë¬Å'\u0089\u008eÃ&\u0099\u001dÉ¥\u009eù4Wéeá)\u009bu5¹\b\u0082Åc\u001d\u001añ×l\u0010\u000f\u0081¯-ø\u0093ö\u0082\n\u0001³\u0093»51\u00ad\b\u0018\u0099H=S$Y²\u0010I¾ç\u000f\u001b½«\u000b^\u0095K¹'5®w\u0010þÿ\u001cã\u0093¹\u007fö\u0096\u0082ÅAUî«\u008a\u0010Ë¬Å'\u0089\u008eÃ&z-¢ú\u0015j\u0015\u0087\b\u0093\u0084/ÝXm]ò\u0018Øh¦\u0014Ã««\u000e\u0085\u0097yHñ&\u0010Ï/ÊSªÁ$¸\"\u0010H\u0000\u0011\u0001jý1bÖ\u001aðìíót°\u0010\u0001æõ³÷¾G\u0006 \u0080Iq\u001cÈUð\b\u0001Õ¹\u0004!ÝÓ0\u0010(^²á$¾dS\u0015\u009aéá0WË<\u0010Úñ\u0006âmJ9\u009e·\u001eÕß¼ªs\f\u0018Ë¬Å'\u0089\u008eÃ&\u0099\u001dÉ¥\u009eù4Wéeá)\u009bu5¹\bIÎ\u008fÒlæ[¥\brýg\u001a\u00adHF\u0082\u0010I¾ç\u000f\u001b½«\u000bjd\u0012Émá\u007f§\u0010\u0086\u0004\u009eLÛ\u0001#R\u0002\t\u0087¥j\u00868\"\b\u0018\u0099H=S$Y²\u0010Úñ\u0006âmJ9\u009e·\u001eÕß¼ªs\f\u0010e./v\u0093W\u0082\u001e\"d\u0088,tÎYl\bÐ$\u0097\u0082¡¢J\u009c\u0010Þ±êû\u007f)h¾z\u008a\b\u0095\u0088_\u001fé\u0010\u009e)bWµö!\u000b§\u008e|Éøl.\u0016\bÍD\u0016Ù\u000b!r\u0092\u0010#g[ ë.§$¼`TwF¹5ñ\b\r\u0006@vâ\r´>\bW²±?%\u008c¨-\b§¯ÃÌ×\u001fÇd\bþ\u000fgÍ\u001eæöÍ\b¨°£!EIQß\brýg\u001a\u00adHF\u0082\u0018ÕÃ0Ï\u00945ß\n¤\u0011Y\u009fM\u009eÂé\u0086ÂÓ\u001b\u0017ÙY\u008a\b\b×\u007fXôù¯f\u0010\t\u008d)(¿\u0003\u0010ÿf_¨\u0089\u0002Õ\u0002º\u0010\u0082m\u008b:Õ\u0011@>è\u001d\u008bé\u0089Ü2¹\b<·ÔAL5\u007f¤\u0010Fã7^Ú\fãø\u0082»x_\u0094\u0084x¸\u0018[l\u0005£2VTOC\u0098¸¸\u001e/Ðëoây\u0007\u008b%Ný\u0010Þ±êû\u007f)h¾z\u008a\b\u0095\u0088_\u001fé\b\u0082Åc\u001d\u001añ×l\bÍD\u0016Ù\u000b!r\u0092\u0010\u0082m\u008b:Õ\u0011@>M\u0086p.\u008d\u0014\u000b:\b<·ÔAL5\u007f¤\u0010{Þî(\u0011\u0083\u0003ñ8\u008f\u008c§\tù\u0093Ò\bÙ\u0099¸z\u001fâÇÝ\u0010\u0082m\u008b:Õ\u0011@>M\u0086p.\u008d\u0014\u000b:\u0010\u0086\u0004\u009eLÛ\u0001#R\u0002\t\u0087¥j\u00868\"\b,Cã-Ç\u0016ýÊ\b\fÙÞ.\u009d=m\u009d\u0010LÖÙ*\u001f\u009f¨àÊ\u0089ãZÌª\u0092ü".length();
      char var5 = 16;
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

               var6 = "þÿ\u001cã\u0093¹\u007fö\u0096\u0082ÅAUî«\u008a\u0018Ý\u0003M\u0092\u000es\u009c'NÀô8\u008b\u0017eñ\u0088f¢þic[\u009e";
               var8 = "þÿ\u001cã\u0093¹\u007fö\u0096\u0082ÅAUî«\u008a\u0018Ý\u0003M\u0092\u000es\u009c'NÀô8\u008b\u0017eñ\u0088f¢þic[\u009e".length();
               var5 = 16;
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
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
