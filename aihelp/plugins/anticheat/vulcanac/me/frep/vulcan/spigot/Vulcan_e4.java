package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;

public class Vulcan_e4 extends PlaceholderExpansion {
   private final VulcanPlugin Vulcan_V;
   private static String Vulcan_B;
   private static final long a = Vulcan_n.a(-545785600325561172L, -1419527622048444392L, MethodHandles.lookup().lookupClass()).a(255494910656877L);
   private static final String[] b;

   public Vulcan_e4() {
      long var1 = a ^ 133372876200546L;
      String var10000 = Vulcan_m();
      super();
      String var3 = var10000;
      this.Vulcan_V = Vulcan_Xs.INSTANCE.Vulcan_J();
      if (var3 == null) {
         int var4 = AbstractCheck.Vulcan_V();
         ++var4;
         AbstractCheck.Vulcan_H(var4);
      }

   }

   public boolean persist() {
      return true;
   }

   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return this.Vulcan_V.getDescription().getAuthors().toString();
   }

   public String getIdentifier() {
      return b[6];
   }

   public String getVersion() {
      return this.Vulcan_V.getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_A(String var0) {
      Vulcan_B = var0;
   }

   public static String Vulcan_m() {
      return Vulcan_B;
   }

   static {
      long var0 = a ^ 37214158982940L;
      Vulcan_A("vkdixc");
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
      String var6 = "¹®\u0005Eº\u0095#nÆ}3\u001dx\u008bê\u0002&)ië¶w9ò\u0018ã´Y[\u009a\u009b^«l0=Uèûañ¹kzH2$\u0095-\u0018@0=Ú¥Ê\u0082\u0081üûÜ\u009c\u0085üÔ!³Ó\u0080#Ôó\u0002,\u0018³AycËµ÷Ò7Ú½¦èù#\u0087ä\u0013a8) ç\u0019\u0018®´$z6`\u000eÜ¸n½\u0089~\u008bêÉyÎJ\u0001xV3Ú\u0010\u0098þ_G\u008ffyv\u0016¬8ú²ö\u0097¨";
      int var8 = "¹®\u0005Eº\u0095#nÆ}3\u001dx\u008bê\u0002&)ië¶w9ò\u0018ã´Y[\u009a\u009b^«l0=Uèûañ¹kzH2$\u0095-\u0018@0=Ú¥Ê\u0082\u0081üûÜ\u009c\u0085üÔ!³Ó\u0080#Ôó\u0002,\u0018³AycËµ÷Ò7Ú½¦èù#\u0087ä\u0013a8) ç\u0019\u0018®´$z6`\u000eÜ¸n½\u0089~\u008bêÉyÎJ\u0001xV3Ú\u0010\u0098þ_G\u008ffyv\u0016¬8ú²ö\u0097¨".length();
      char var5 = 24;
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

               var6 = "¯H=÷C»\u0090?\b×U~\u00ad\f\u008b«\u0084";
               var8 = "¯H=÷C»\u0090?\b×U~\u00ad\f\u008b«\u0084".length();
               var5 = '\b';
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
