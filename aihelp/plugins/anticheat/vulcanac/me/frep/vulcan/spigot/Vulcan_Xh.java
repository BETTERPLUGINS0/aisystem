package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class Vulcan_Xh {
   private static final long a = Vulcan_n.a(-1319637131609722545L, 7747473778185171092L, MethodHandles.lookup().lookupClass()).a(233901646803253L);
   private static final String[] b;

   public static final String Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_Xh() {
      throw new UnsupportedOperationException(b[15]);
   }

   static {
      long var0 = a ^ 62799581152475L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[97];
      int var7 = 0;
      String var6 = "\u0018¶/Î\b\u009f Ü\u0010¬ªË\u0096Ö\u0007\u0013ê\t\u00170\u008a.³:³ |\u0010I±\u0007b\u0085KÿÙ eö=k©\u001fà^\u0092»\u0089Ì2å\u000e¥Ã3¯så\u0018ñ¸ ÿö\u001e4ÌÍÿ]¯Adù\u0088\u008f-~ÝóÑÂû\u0010ãNÚ\u0093]Úr¢áhá\u0087¥Þ±\u0014\u0010ü¾\u008cP\u0080D\u0084\u001eÓöð\u0098¹õÎ%\bÍ+àfZz\u0007M »Ã\u0088\u009eÞ\u0086Oàü²£ÌçÓ¡*2|±~b\u0005³6¤g*p\u009b©C¶ \u0019µà\u0015ÅØ~Ô\u0085x·n\u009dg\u008dZ![ñ\u0083R°~ò²\u0013\u0099G_ßFí\b¾bÃQ*S\t\u009c\u0010$\u0007\nÎ7Å\u00ad0ôA¼\u008e\u0016\b0\u009b õ@cÁÿÓ\n\u0010»ñô+x\u0003öÈ\u000bXÎè\u0016\u008cÖüÇ\u0014J\u001d¢g\u0001\u0018\bøyÔ¢I\u007fo\u007f õ@cÁÿÓ\n\u00104Ë\u0012\u001cÛJ\u0086KÌ\n\u000b¶QÒkÉ\u0089´ýãù¸m\r\bi{Z\u0080hÒP?8È\u0004\u008aà\u0099K\u0097® \u0000\u008aô\u009eD(Á\u001dv\u0012\u008báÞÛ\u001a\u0095à\u0015N1\u0004âýi\u0013\u008c\u0081\u0091cE*\u0094Ù\u0093=qwóÖ\u001cÉC(\u0087h9Ä\u0010ì\u0015GùÑÿDÌ'\u0000Vð<\u007f¯o\u0018¶òû\u0096.\u0099\u0001¹ÇÕ÷¯\u001c9G\u0085\u0092\u0005ö¶Ê\u0006|sPFr+\u0099\u0013¬eÐAìÊ)\u008c£\u0006¿8ÔÜ3È|\u0090øðàaí\u009eó¡²L\u0092§®\t\u0090®\u008bE\u008eËÊÐþèé³\u0086`ªÉ)¤Ü\n#z_UQ!ê®³Q\u0099iø¬M_x\u009bÿP¨»è\bQ(åï(\u0089Ë\u008e\u0010ãNÚ\u0093]Úr¢áhá\u0087¥Þ±\u0014 õ@cÁÿÓ\n\u0010\u0017Q½órþ×#¶_\u0087H]ðvØ\u009f¼\u009b\u0017G®\u0099m\u0010Î>£X\u00909®·òk¨³\u009a\u0014'l\u0018\u0093é\u00843Ý¯Ù¹G§ÃÄêè×Á7*\u0007\u0091>÷\u0011b\u0010|\u0016ò«w5\u0091ÂÉHÎ?)/\u0086j\u0018õ@cÁÿÓ\n\u0010+?¦¬\u0087ôéø±k\u000f*råÛ× õ@cÁÿÓ\n\u0010\u0011«O,K{kQ^n\u0007ãh\f\u0001§]\u0011ãx\u00adFùè\u0010»µï\u0096&Àf\u0005ø¹\u0013\b¯gÑ\u001b\u0010%¼Z^(Ó\u0007¿'¨¤±\u0084û\u0002\u0016\u0010ü\u0011v'ñ9Ð¬\u0086\u0014ÞöøI²H\bøyÔ¢I\u007fo\u007f\u0018Fr+\u0099\u0013¬eÐ¥\u0011\u001f5ý\u0018ò©¥æëOs\"Å\u0003\b¡\u0082Q÷ÚhùÒ\b\n\u008b\u0004V87\"!\u0018|\u0010I±\u0007b\u0085K2\u008e<Í\u000e\u0019\u0005eÓ{I\u0001Á\u0015f±\u0010ws\u008bµØäÖ\u008e¬º5N üÈ?\u0010¡}ÜL å\u001fí)À7#¯\u000fìU )2\u0000¡\u0082ÐhÉ\u000b\u000f4\n²öüiçY\u0019\u000f\u007fÅÎä?{Y3i¾[{\bjÏ \u0001\u0014ø\u0085¸\b\u0014ß1v<\u0018§\u008a\b½(ò¨1ªt\u009d\u0010ü¾\u008cP\u0080D\u0084\u001eÓöð\u0098¹õÎ%\u0018\u000b\u009a%á²\u008d±\u0018/«\u0093h|Á¨°_n)÷1ÎÒ\u0090\u0010\u008d¯«ñ,\u0017«\u0095ÜÓ\u0082·¥çfN )2\u0000¡\u0082ÐhÉ§\u0092Ï\u00806÷\u009a\u0012¼½½¡ÿ\"ðþTcY·ÌÐ\u001dâ\u0010m$Un\u0011ü^å\u0003\u0003\u0083È\u0084úpû\u0018\u0090TZvµ\u0005e\r\u009aöz5É\u00850l\u0010\u0003,\u0087\u008eùh\u0083\b¾bÃQ*S\t\u009c\b¥)PòGÊòñ\b|\u000e CÁq\u0015%\u0018\u008d(\u000f]]U!\u0096\\\u00ad\u008e\u0087ã\u001fYß\u0089\u0018æQhßT=\u0018O`\u008eè\u000b¤\u0088@A:èb¶:\u0000J\u0086êCqH\u0094òª\bsj:s\u009bË\u0003m\u0018}\u0007~ce\u008fÛo*JX¼a\u009dIó\u0093GÉÖNö¡Ë õ@cÁÿÓ\n\u0010\u001b\u001f·\u0089#9¹\u0089Fä÷×\u001fLøl!X\u008fÑÆ´\u009f¤ õ@cÁÿÓ\n\u0010n\u0094l? \fw\u0002d\u0098-L\"XÇºýC\u007fÎ\r9Î\u0015\u0010F\u0091=\u0019\u0011õV1ÉþUµ\u0097ý\u000f\u0011\u0010\u0006Uz\u0082{Ç3\u008fiÂ=\u0098\u0004`©è\b£\u0088áTz\u0095i]\u0010²\u001arçä\u0013\u0016\u0081¨\u0096\u001fÊ+þ§X\b\u0099\u0017&\u0088M¨\rÖ\u0010â\u0084\\^ârôlÝ#4\nÎ\u001d;4\u0010ºI?\u000f}b\n\u00ad\f\u0000c}\u008a\u007f\u0086ä\bû§\u0094\u0088#g\u000b\u0090\bÆö¿~\u0015ûp\u0003\u0010\u000fµè\u0091w¦\f\u0015t3ó§x\u0087`!\u0010m$Un\u0011ü^å\u0003\u0003\u0083È\u0084úpû\b³°È\u001f\u009cØ8+\u0010ûÒz>í\u0095£^[æùâ¼+\u0094t\bu\u00010cÓ@H\n\u0010÷\u0006\u009bÁ\u0012\u0094TÇz\\Ù¶fò'\u009f\u0018\u001bx\f$\u0002II9ÞP¥\u0019§\u000bM\u009c//u°öØ~\u0091\u0018\u000b\u0007Y\u007f~\u0091â\u0092\u0096gÜ£\u0000IÞ\u008d!L·\u0010£\u009bI\u0001\u0010¶5Ìj!?º(L)-Û\b\u001a\u009eÄ\u0018p¼\u0012ö$%°\b\u0011a¬\u001dS'Ö¾*\u0011rÅUh×¯\bò?KXeÛæ¹\bå+F\u001fÿ\u0095W\u0017\u0018\u0097ëIáw·Û:´}ÿ\u00018Ò\u0011cÁ\u00ad×6\u0006`û·\b\rhVÕ9L\u009c¾\u0010Öæ\u0006Ø®É¤´mHÅ©ü<\u001e^\u0010ÅwcÃk\nKÿH\u0091ãê(zÇ¼\u0010¸¡}r?QÖ\b3³Ï\u001e+ÑÏß\u0018HÓ\u001d\u000e(8ü\u001d\u008aU\u0099\u0099;Bè3®Þå@G*\u0006B\bå+F\u001fÿ\u0095W\u0017\bû\u001a\u0082dnÖ«Ñ\u0018)2\u0000¡\u0082ÐhÉøÊËwÕmÊ`\u007fP*ë\u0001E\u000eß\u0010\u008f;\u0081Êdök\u008a\u0097\n6S\u0092\rlÃ\b\u007f\u0089ß*r_by\u0010*>Ç\u008a\u009f\u001dµI\u00914?}8ß\u0087z\u0018åÁ¡Ú8K®gÜÓV-Qïàfú\u0086¬\fQRh\u001b\u0010%¼Z^(Ó\u0007¿'¨¤±\u0084û\u0002\u0016 |\u0010I±\u0007b\u0085KÿÙ eö=k©-WÛÌ@r\"\u009fØA_ý\u0000\u0095fA õ@cÁÿÓ\n\u0010I\u0086X\u0099¼LC§VW\u0091G/v©§«_éùªö9¹\u0010¿#y'¨Mm£\u0080\u001câ¼úu\u009dÌ õ@cÁÿÓ\n\u0010\u0088Kµq ¨£Ã~ë¸°\u00ad7QÎ\u009dG½\u0089ÛO2í";
      int var8 = "\u0018¶/Î\b\u009f Ü\u0010¬ªË\u0096Ö\u0007\u0013ê\t\u00170\u008a.³:³ |\u0010I±\u0007b\u0085KÿÙ eö=k©\u001fà^\u0092»\u0089Ì2å\u000e¥Ã3¯så\u0018ñ¸ ÿö\u001e4ÌÍÿ]¯Adù\u0088\u008f-~ÝóÑÂû\u0010ãNÚ\u0093]Úr¢áhá\u0087¥Þ±\u0014\u0010ü¾\u008cP\u0080D\u0084\u001eÓöð\u0098¹õÎ%\bÍ+àfZz\u0007M »Ã\u0088\u009eÞ\u0086Oàü²£ÌçÓ¡*2|±~b\u0005³6¤g*p\u009b©C¶ \u0019µà\u0015ÅØ~Ô\u0085x·n\u009dg\u008dZ![ñ\u0083R°~ò²\u0013\u0099G_ßFí\b¾bÃQ*S\t\u009c\u0010$\u0007\nÎ7Å\u00ad0ôA¼\u008e\u0016\b0\u009b õ@cÁÿÓ\n\u0010»ñô+x\u0003öÈ\u000bXÎè\u0016\u008cÖüÇ\u0014J\u001d¢g\u0001\u0018\bøyÔ¢I\u007fo\u007f õ@cÁÿÓ\n\u00104Ë\u0012\u001cÛJ\u0086KÌ\n\u000b¶QÒkÉ\u0089´ýãù¸m\r\bi{Z\u0080hÒP?8È\u0004\u008aà\u0099K\u0097® \u0000\u008aô\u009eD(Á\u001dv\u0012\u008báÞÛ\u001a\u0095à\u0015N1\u0004âýi\u0013\u008c\u0081\u0091cE*\u0094Ù\u0093=qwóÖ\u001cÉC(\u0087h9Ä\u0010ì\u0015GùÑÿDÌ'\u0000Vð<\u007f¯o\u0018¶òû\u0096.\u0099\u0001¹ÇÕ÷¯\u001c9G\u0085\u0092\u0005ö¶Ê\u0006|sPFr+\u0099\u0013¬eÐAìÊ)\u008c£\u0006¿8ÔÜ3È|\u0090øðàaí\u009eó¡²L\u0092§®\t\u0090®\u008bE\u008eËÊÐþèé³\u0086`ªÉ)¤Ü\n#z_UQ!ê®³Q\u0099iø¬M_x\u009bÿP¨»è\bQ(åï(\u0089Ë\u008e\u0010ãNÚ\u0093]Úr¢áhá\u0087¥Þ±\u0014 õ@cÁÿÓ\n\u0010\u0017Q½órþ×#¶_\u0087H]ðvØ\u009f¼\u009b\u0017G®\u0099m\u0010Î>£X\u00909®·òk¨³\u009a\u0014'l\u0018\u0093é\u00843Ý¯Ù¹G§ÃÄêè×Á7*\u0007\u0091>÷\u0011b\u0010|\u0016ò«w5\u0091ÂÉHÎ?)/\u0086j\u0018õ@cÁÿÓ\n\u0010+?¦¬\u0087ôéø±k\u000f*råÛ× õ@cÁÿÓ\n\u0010\u0011«O,K{kQ^n\u0007ãh\f\u0001§]\u0011ãx\u00adFùè\u0010»µï\u0096&Àf\u0005ø¹\u0013\b¯gÑ\u001b\u0010%¼Z^(Ó\u0007¿'¨¤±\u0084û\u0002\u0016\u0010ü\u0011v'ñ9Ð¬\u0086\u0014ÞöøI²H\bøyÔ¢I\u007fo\u007f\u0018Fr+\u0099\u0013¬eÐ¥\u0011\u001f5ý\u0018ò©¥æëOs\"Å\u0003\b¡\u0082Q÷ÚhùÒ\b\n\u008b\u0004V87\"!\u0018|\u0010I±\u0007b\u0085K2\u008e<Í\u000e\u0019\u0005eÓ{I\u0001Á\u0015f±\u0010ws\u008bµØäÖ\u008e¬º5N üÈ?\u0010¡}ÜL å\u001fí)À7#¯\u000fìU )2\u0000¡\u0082ÐhÉ\u000b\u000f4\n²öüiçY\u0019\u000f\u007fÅÎä?{Y3i¾[{\bjÏ \u0001\u0014ø\u0085¸\b\u0014ß1v<\u0018§\u008a\b½(ò¨1ªt\u009d\u0010ü¾\u008cP\u0080D\u0084\u001eÓöð\u0098¹õÎ%\u0018\u000b\u009a%á²\u008d±\u0018/«\u0093h|Á¨°_n)÷1ÎÒ\u0090\u0010\u008d¯«ñ,\u0017«\u0095ÜÓ\u0082·¥çfN )2\u0000¡\u0082ÐhÉ§\u0092Ï\u00806÷\u009a\u0012¼½½¡ÿ\"ðþTcY·ÌÐ\u001dâ\u0010m$Un\u0011ü^å\u0003\u0003\u0083È\u0084úpû\u0018\u0090TZvµ\u0005e\r\u009aöz5É\u00850l\u0010\u0003,\u0087\u008eùh\u0083\b¾bÃQ*S\t\u009c\b¥)PòGÊòñ\b|\u000e CÁq\u0015%\u0018\u008d(\u000f]]U!\u0096\\\u00ad\u008e\u0087ã\u001fYß\u0089\u0018æQhßT=\u0018O`\u008eè\u000b¤\u0088@A:èb¶:\u0000J\u0086êCqH\u0094òª\bsj:s\u009bË\u0003m\u0018}\u0007~ce\u008fÛo*JX¼a\u009dIó\u0093GÉÖNö¡Ë õ@cÁÿÓ\n\u0010\u001b\u001f·\u0089#9¹\u0089Fä÷×\u001fLøl!X\u008fÑÆ´\u009f¤ õ@cÁÿÓ\n\u0010n\u0094l? \fw\u0002d\u0098-L\"XÇºýC\u007fÎ\r9Î\u0015\u0010F\u0091=\u0019\u0011õV1ÉþUµ\u0097ý\u000f\u0011\u0010\u0006Uz\u0082{Ç3\u008fiÂ=\u0098\u0004`©è\b£\u0088áTz\u0095i]\u0010²\u001arçä\u0013\u0016\u0081¨\u0096\u001fÊ+þ§X\b\u0099\u0017&\u0088M¨\rÖ\u0010â\u0084\\^ârôlÝ#4\nÎ\u001d;4\u0010ºI?\u000f}b\n\u00ad\f\u0000c}\u008a\u007f\u0086ä\bû§\u0094\u0088#g\u000b\u0090\bÆö¿~\u0015ûp\u0003\u0010\u000fµè\u0091w¦\f\u0015t3ó§x\u0087`!\u0010m$Un\u0011ü^å\u0003\u0003\u0083È\u0084úpû\b³°È\u001f\u009cØ8+\u0010ûÒz>í\u0095£^[æùâ¼+\u0094t\bu\u00010cÓ@H\n\u0010÷\u0006\u009bÁ\u0012\u0094TÇz\\Ù¶fò'\u009f\u0018\u001bx\f$\u0002II9ÞP¥\u0019§\u000bM\u009c//u°öØ~\u0091\u0018\u000b\u0007Y\u007f~\u0091â\u0092\u0096gÜ£\u0000IÞ\u008d!L·\u0010£\u009bI\u0001\u0010¶5Ìj!?º(L)-Û\b\u001a\u009eÄ\u0018p¼\u0012ö$%°\b\u0011a¬\u001dS'Ö¾*\u0011rÅUh×¯\bò?KXeÛæ¹\bå+F\u001fÿ\u0095W\u0017\u0018\u0097ëIáw·Û:´}ÿ\u00018Ò\u0011cÁ\u00ad×6\u0006`û·\b\rhVÕ9L\u009c¾\u0010Öæ\u0006Ø®É¤´mHÅ©ü<\u001e^\u0010ÅwcÃk\nKÿH\u0091ãê(zÇ¼\u0010¸¡}r?QÖ\b3³Ï\u001e+ÑÏß\u0018HÓ\u001d\u000e(8ü\u001d\u008aU\u0099\u0099;Bè3®Þå@G*\u0006B\bå+F\u001fÿ\u0095W\u0017\bû\u001a\u0082dnÖ«Ñ\u0018)2\u0000¡\u0082ÐhÉøÊËwÕmÊ`\u007fP*ë\u0001E\u000eß\u0010\u008f;\u0081Êdök\u008a\u0097\n6S\u0092\rlÃ\b\u007f\u0089ß*r_by\u0010*>Ç\u008a\u009f\u001dµI\u00914?}8ß\u0087z\u0018åÁ¡Ú8K®gÜÓV-Qïàfú\u0086¬\fQRh\u001b\u0010%¼Z^(Ó\u0007¿'¨¤±\u0084û\u0002\u0016 |\u0010I±\u0007b\u0085KÿÙ eö=k©-WÛÌ@r\"\u009fØA_ý\u0000\u0095fA õ@cÁÿÓ\n\u0010I\u0086X\u0099¼LC§VW\u0091G/v©§«_éùªö9¹\u0010¿#y'¨Mm£\u0080\u001câ¼úu\u009dÌ õ@cÁÿÓ\n\u0010\u0088Kµq ¨£Ã~ë¸°\u00ad7QÎ\u009dG½\u0089ÛO2í".length();
      char var5 = '\b';
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

               var6 = "Uæöõp\u0098:ªâÐB\u008dó\u0010whàl\u0013íd\u0007~Ó\u0010â\u0084\\^ârôlÝ#4\nÎ\u001d;4";
               var8 = "Uæöõp\u0098:ªâÐB\u008dó\u0010whàl\u0013íd\u0007~Ó\u0010â\u0084\\^ârôlÝ#4\nÎ\u001d;4".length();
               var5 = 24;
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
