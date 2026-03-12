package me.frep.vulcan.spigot.check.impl.movement.entityspeed;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Entity Speed",
   type = 'A',
   complexType = "Limit",
   description = "Riding an entity too quickly."
)
public class EntitySpeedA extends AbstractCheck {
   private int Vulcan_H;
   private static String Vulcan_O;
   private static final long b = Vulcan_n.a(8643843182366518881L, -3426474400754768734L, MethodHandles.lookup().lookupClass()).a(28600464476836L);
   private static final String[] d;

   public EntitySpeedA(Vulcan_iE var1) {
      long var2 = b ^ 63215521745434L;
      String var10000 = Vulcan_g();
      super(var1);
      String var4 = var10000;
      this.Vulcan_H = 10000;
      if (var4 != null) {
         int var5 = AbstractCheck.Vulcan_m();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   private void lambda$handle$10() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$9() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$8() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$7() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$6() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$5() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$4() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$3() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$2() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$1() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$0() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   public static void Vulcan_G(String var0) {
      Vulcan_O = var0;
   }

   public static String Vulcan_g() {
      return Vulcan_O;
   }

   static {
      long var0 = b ^ 135521483221057L;
      Vulcan_G((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[17];
      int var7 = 0;
      String var6 = "ôÁii\u0097³\u009e¶\u0002\u0015\u009b§ô\u0091øD\u0010ó\u0097\u0004\u008dßÁ?¨PåyQÉB\u0096*\u0010ÀMõ\u0099¯\u0016Ê\u0012ò\"¾\u001a\u009eX\"\u0095\b\u008f.YÔ¤\u00984ø\u0010\u0092M:vs\r_h¹uwÔª\u0087©Û\u00186\tÁÃõ\u001a&*û¯§\u0000îª\u001b\u0000ð\u0019\u009b\u0019xµ\b#\u0018k3¦<¯\f\u0091e¦!â¿\b±¬ªÖ¡)pmb3)\u0010ÎdÏ\u007f<P\rdrsÇ´Îp¼J\u00184ÕÁ\u008ak\u0085\u00ad¬b\u001b\u008c¯ª\u0092^CE\u0095¼¶\u008dÀFÍ\bþ{G\u000bäDGK\u0018kÔÁ3jr\u001eÜ\u0015¿\u0004H<Ã5jÎ`\u0014Ws\bhg\u0010·\u0081ÅÖ\fê\u0017kÅ¼\u0003\u0013À\u0097Üê\bþ{G\u000bäDGK\u0018ÂW)ß¾\u000b\u009d¾ëÈGÄ)\b\u00815óåm\u008d÷\u0002ÝÊ\u0010n×jÙ\u009eûò0@g(×,ê\u001då";
      int var8 = "ôÁii\u0097³\u009e¶\u0002\u0015\u009b§ô\u0091øD\u0010ó\u0097\u0004\u008dßÁ?¨PåyQÉB\u0096*\u0010ÀMõ\u0099¯\u0016Ê\u0012ò\"¾\u001a\u009eX\"\u0095\b\u008f.YÔ¤\u00984ø\u0010\u0092M:vs\r_h¹uwÔª\u0087©Û\u00186\tÁÃõ\u001a&*û¯§\u0000îª\u001b\u0000ð\u0019\u009b\u0019xµ\b#\u0018k3¦<¯\f\u0091e¦!â¿\b±¬ªÖ¡)pmb3)\u0010ÎdÏ\u007f<P\rdrsÇ´Îp¼J\u00184ÕÁ\u008ak\u0085\u00ad¬b\u001b\u008c¯ª\u0092^CE\u0095¼¶\u008dÀFÍ\bþ{G\u000bäDGK\u0018kÔÁ3jr\u001eÜ\u0015¿\u0004H<Ã5jÎ`\u0014Ws\bhg\u0010·\u0081ÅÖ\fê\u0017kÅ¼\u0003\u0013À\u0097Üê\bþ{G\u000bäDGK\u0018ÂW)ß¾\u000b\u009d¾ëÈGÄ)\b\u00815óåm\u008d÷\u0002ÝÊ\u0010n×jÙ\u009eûò0@g(×,ê\u001då".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = b(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  d = var9;
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

               var6 = ")^XÞ\rõÖòÕáý\u0081nÁ¤\u0085\u0010ÎdÏ\u007f<P\rdrsÇ´Îp¼J";
               var8 = ")^XÞ\rõÖòÕáý\u0081nÁ¤\u0085\u0010ÎdÏ\u007f<P\rdrsÇ´Îp¼J".length();
               var5 = 16;
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

   private static String b(byte[] var0) {
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
