package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import java.lang.invoke.MethodHandles;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_ee {
   private final Vulcan_iE Vulcan_b;
   private final Deque Vulcan_Y = new ConcurrentLinkedDeque();
   private final Deque Vulcan_M = new ConcurrentLinkedDeque();
   private short Vulcan_K = -32348;
   private short Vulcan_n = -69;
   private short Vulcan_R = -69;
   private long Vulcan_T = 0L;
   private long Vulcan_w = -1L;
   private long Vulcan_l = -1L;
   private long Vulcan_o = System.currentTimeMillis();
   private long Vulcan_g = Long.MAX_VALUE;
   private int Vulcan_j = -42069;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4462195114158679096L, 1983180798006538741L, MethodHandles.lookup().lookupClass()).a(270552823113779L);
   private static final String[] b;

   public Vulcan_ee(Vulcan_iE var1) {
      this.Vulcan_b = var1;
   }

   public void Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_g(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      this.Vulcan_M.add(var2);
   }

   public void Vulcan_r(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 20289648898449L;
      StringBuilder var10000 = new StringBuilder();
      String[] var6 = b;
      Vulcan_eG.Vulcan_V(new Object[]{var4, var10000.append(var6[13]).append(this.Vulcan_Y.size()).append(var6[17]).append((String)this.Vulcan_Y.stream().map(Vulcan_ee::lambda$sendDebugMessage$4).collect(Collectors.joining(var6[2]))).toString()});
      Vulcan_eG.Vulcan_V(new Object[]{var4, var6[5] + (System.currentTimeMillis() - this.Vulcan_o)});
      Vulcan_eG.Vulcan_V(new Object[]{var4, var6[7] + (System.currentTimeMillis() - this.Vulcan_b.getJoinTime())});
      Vulcan_eG.Vulcan_V(new Object[]{var4, var6[8] + this.Vulcan_j});
      Vulcan_eG.Vulcan_V(new Object[]{var4, var6[0] + (String)this.Vulcan_b.Vulcan_R(new Object[0]).stream().map(String::valueOf).collect(Collectors.joining(var6[9]))});
      Vulcan_eG.Vulcan_V(new Object[]{var4, var6[4] + (String)this.Vulcan_b.Vulcan_g(new Object[0]).stream().map(String::valueOf).collect(Collectors.joining(var6[9]))});
   }

   public void Vulcan_J(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_q(Object[] var1) {
      Object var6;
      label16: {
         long var2 = (Long)var1[0];
         int var4 = (Integer)var1[1];
         var2 ^= a;
         String[] var5 = Vulcan_iJ.Vulcan_W();
         if (Vulcan_eG.Vulcan_t(new Object[0])) {
            var6 = new WrapperPlayServerPing(var4);
            if (var2 < 0L) {
               return;
            }

            if (var5 == null) {
               break label16;
            }
         }

         var6 = new WrapperPlayServerWindowConfirmation(0, (short)var4, false);
      }

      this.Vulcan_b.Vulcan_I(new Object[0]).sendPacket((PacketWrapper)var6);
   }

   public short Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iE Vulcan_G(Object[] var1) {
      return this.Vulcan_b;
   }

   public Deque Vulcan_f(Object[] var1) {
      return this.Vulcan_Y;
   }

   public Deque Vulcan_h(Object[] var1) {
      return this.Vulcan_M;
   }

   public short Vulcan_t(Object[] var1) {
      return this.Vulcan_K;
   }

   public short Vulcan_T(Object[] var1) {
      return this.Vulcan_n;
   }

   public short Vulcan__(Object[] var1) {
      return this.Vulcan_R;
   }

   public long Vulcan_e(Object[] var1) {
      return this.Vulcan_T;
   }

   public long Vulcan__(Object[] var1) {
      return this.Vulcan_w;
   }

   public long Vulcan_C(Object[] var1) {
      return this.Vulcan_l;
   }

   public long Vulcan_E(Object[] var1) {
      return this.Vulcan_o;
   }

   public long Vulcan_U(Object[] var1) {
      return this.Vulcan_g;
   }

   public int Vulcan_g(Object[] var1) {
      return this.Vulcan_j;
   }

   public void Vulcan_V(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_K = (short)var2;
   }

   public void Vulcan_H(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_n = (short)var2;
   }

   public void Vulcan_F(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_R = (short)var2;
   }

   public void Vulcan_B(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_T = var2;
   }

   public void Vulcan_D(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_w = var2;
   }

   public void Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_l = var2;
   }

   public void Vulcan_K(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_o = var2;
   }

   public void Vulcan_N(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_g = var2;
   }

   public void Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_j = var2;
   }

   private void lambda$sendConfirmation$5() {
      long var1 = a ^ 47895241421915L;
      long var3 = var1 ^ 24539141566784L;
      long var5 = var1 ^ 70068274298054L;
      short var7 = this.Vulcan_U(new Object[]{var3});
      this.Vulcan_q(new Object[]{var5, Integer.valueOf(var7)});
      this.Vulcan_n = (short)var7;
   }

   private static String lambda$sendDebugMessage$4(Vulcan_U var0) {
      return String.valueOf(var0.Vulcan_S(new Object[0]));
   }

   private void lambda$handleFlying$3() {
      long var1 = a ^ 121525089670462L;
      long var3 = var1 ^ 40094167890005L;
      this.Vulcan_b.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ag, var3}));
   }

   private void lambda$handleFlying$2() {
      long var1 = a ^ 9304585850018L;
      long var3 = var1 ^ 73149767459273L;
      this.Vulcan_b.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_Qv, var3}));
   }

   private void lambda$receivingPong$1() {
      long var1 = a ^ 123371171286494L;
      long var3 = var1 ^ 64619724431541L;
      this.Vulcan_b.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_u, var3}));
   }

   private void lambda$receivingPong$0() {
      long var1 = a ^ 111450948451064L;
      long var3 = var1 ^ 52419394164627L;
      this.Vulcan_b.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_m, var3}));
   }

   static {
      long var0 = a ^ 19066574552591L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[19];
      int var7 = 0;
      String var6 = "&öh\u00947\u0082\u001f\u0006LÚ\u0080ÿ$\u001d<\u0016¨.$\u009cÄVå\u0081\u0010\nfÀ\fxñ\u0088\u0002\u009eMBÒñ ³L\bÕ µ\u0012Ð§\u009b{\b\u0093\u000fJ\u0084\u0080{ëf\u0010Ô`±ä}ë\u0093d±\u009fë\u001b\u0018Ä\u0092Ô(\u009dO¼wa\u0003;\u007f\u0081½rÞ\u0080§&\u00adQ\u0010èÂEaî\u0015z\"yÅÓoø\u008f[ië\u009f\r¦Ï\u000f\u0010O\u0082Iv§\u0087\u009eI\u0013\u0089°p×é\u0018P\u0018\u009dO¼wa\u0003;\u007f\u0018Óå\u001f\u0085ÿ0\u00adY\u000e\u001d \u0088TÐ\u001e\u0018&öh\u00947\u0082\u001f\u0006ðP+\u008af\u001bÎ\u001a~ô1\u0006R[1\u000e\bÕ µ\u0012Ð§\u009b{\u0010\nfÀ\fxñ\u0088\u0002\u009eMBÒñ ³L\bae\u0001£L\u0013Bö\b\\Ç?CY¶ÄF\u0018\u0080\u000b=\u008fe\u0013¨TbÐj\u0099Òò|øÐ\u009bÁåo\u0018Èç\b\u0093\u000fJ\u0084\u0080{ëf\bae\u0001£L\u0013Bö\b\\Ç?CY¶ÄF";
      int var8 = "&öh\u00947\u0082\u001f\u0006LÚ\u0080ÿ$\u001d<\u0016¨.$\u009cÄVå\u0081\u0010\nfÀ\fxñ\u0088\u0002\u009eMBÒñ ³L\bÕ µ\u0012Ð§\u009b{\b\u0093\u000fJ\u0084\u0080{ëf\u0010Ô`±ä}ë\u0093d±\u009fë\u001b\u0018Ä\u0092Ô(\u009dO¼wa\u0003;\u007f\u0081½rÞ\u0080§&\u00adQ\u0010èÂEaî\u0015z\"yÅÓoø\u008f[ië\u009f\r¦Ï\u000f\u0010O\u0082Iv§\u0087\u009eI\u0013\u0089°p×é\u0018P\u0018\u009dO¼wa\u0003;\u007f\u0018Óå\u001f\u0085ÿ0\u00adY\u000e\u001d \u0088TÐ\u001e\u0018&öh\u00947\u0082\u001f\u0006ðP+\u008af\u001bÎ\u001a~ô1\u0006R[1\u000e\bÕ µ\u0012Ð§\u009b{\u0010\nfÀ\fxñ\u0088\u0002\u009eMBÒñ ³L\bae\u0001£L\u0013Bö\b\\Ç?CY¶ÄF\u0018\u0080\u000b=\u008fe\u0013¨TbÐj\u0099Òò|øÐ\u009bÁåo\u0018Èç\b\u0093\u000fJ\u0084\u0080{ëf\bae\u0001£L\u0013Bö\b\\Ç?CY¶ÄF".length();
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

               var6 = "FÇG5.\u001ey\t\u0010O\u0082Iv§\u0087\u009eI\u0013\u0089°p×é\u0018P";
               var8 = "FÇG5.\u001ey\t\u0010O\u0082Iv§\u0087\u009eI\u0013\u0089°p×é\u0018P".length();
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
