package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.api.data.IPlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;

public class Vulcan_iE implements IPlayerData {
   private final User Vulcan_g;
   @Nullable
   private Player Vulcan_v;
   private String Vulcan_N;
   private int Vulcan_T;
   private UUID Vulcan_P;
   private ClientVersion Vulcan_U;
   private String Vulcan_i;
   private boolean Vulcan_y;
   private boolean Vulcan_q;
   private boolean Vulcan_W;
   private boolean Vulcan_H;
   private int Vulcan__;
   private int Vulcan_n;
   private int Vulcan_o;
   private int Vulcan_m;
   private int Vulcan_s;
   private int Vulcan_k;
   private int Vulcan_e;
   private final long Vulcan_S;
   private final int Vulcan_A;
   private long Vulcan_a;
   private long Vulcan_B;
   private WrapperPlayClientPlayerFlying Vulcan_F;
   private WrapperPlayClientInteractEntity Vulcan_I;
   private WrapperPlayClientPlayerDigging Vulcan_l;
   private WrapperPlayClientEntityAction Vulcan_E;
   private WrapperPlayClientHeldItemChange Vulcan_J;
   private WrapperPlayClientPlayerBlockPlacement Vulcan_f;
   private int Vulcan_x;
   private List Vulcan_b;
   private HashSet Vulcan_u;
   private HashSet Vulcan_Q;
   private final Vulcan_iB Vulcan_p;
   private final Vulcan_iO Vulcan_t;
   private final Vulcan_iG Vulcan_d;
   private final Vulcan_it Vulcan_w;
   private final Vulcan_iJ Vulcan_Y;
   private final Vulcan_T Vulcan_h;
   private final Vulcan_k Vulcan_Z;
   private final Vulcan_XU Vulcan_K;
   private final Vulcan_ee Vulcan_L;
   private final Vulcan_c Vulcan_R;
   private final Vulcan_c Vulcan_V;
   private static int[] Vulcan_M;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(254170509371265729L, 1121341562784122365L, MethodHandles.lookup().lookupClass()).a(63762990576106L);
   private static final String[] b;

   public Vulcan_iE(User var1) {
      long var2 = a ^ 117841542077054L;
      super();
      String[] var6 = b;
      this.Vulcan_N = var6[3];
      int[] var10000 = Vulcan_X();
      this.Vulcan_T = -42069;
      this.Vulcan_P = null;
      this.Vulcan_U = ClientVersion.UNKNOWN;
      this.Vulcan_i = var6[4];
      this.Vulcan_y = false;
      int[] var4 = var10000;
      this.Vulcan_q = false;
      this.Vulcan_W = false;
      this.Vulcan_H = false;
      this.Vulcan_S = System.currentTimeMillis();
      this.Vulcan_A = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
      this.Vulcan_x = 0;
      this.Vulcan_b = new ArrayList();
      this.Vulcan_u = new HashSet();
      this.Vulcan_Q = new HashSet();
      this.Vulcan_p = new Vulcan_iB(this);
      this.Vulcan_t = new Vulcan_iO(this);
      this.Vulcan_d = new Vulcan_iG(this);
      this.Vulcan_w = new Vulcan_it(this);
      this.Vulcan_Y = new Vulcan_iJ(this);
      this.Vulcan_h = new Vulcan_T(this);
      this.Vulcan_Z = new Vulcan_k(this);
      this.Vulcan_K = new Vulcan_XU(this);
      this.Vulcan_L = new Vulcan_ee(this);
      this.Vulcan_R = new Vulcan_c(100);
      this.Vulcan_V = new Vulcan_c(100);
      this.Vulcan_g = var1;
      if (var4 == null) {
         int var5 = AbstractCheck.Vulcan_m();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public boolean Vulcan_O(Object[] var1) {
      return Vulcan_bQ.Vulcan_P(new Object[]{this});
   }

   public boolean Vulcan_R(Object[] var1) {
      return Vulcan_bQ.Vulcan_o(new Object[]{this});
   }

   public boolean Vulcan_p(Object[] var1) {
      return Vulcan_bQ.Vulcan_l(new Object[]{this});
   }

   public boolean Vulcan_n(Object[] var1) {
      return this.Vulcan_U.isOlderThan(ClientVersion.V_1_9);
   }

   public boolean Vulcan_u(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_H(Object[] var1) {
      return this.Vulcan_U.isOlderThanOrEquals(ClientVersion.V_1_8);
   }

   public List Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_I(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public User Vulcan_I(Object[] var1) {
      return this.Vulcan_g;
   }

   @Nullable
   public Player Vulcan_q(Object[] var1) {
      return this.Vulcan_v;
   }

   public String getClientBrand() {
      return this.Vulcan_N;
   }

   public int Vulcan_d(Object[] var1) {
      return this.Vulcan_T;
   }

   public UUID Vulcan_n(Object[] var1) {
      return this.Vulcan_P;
   }

   public ClientVersion Vulcan_t(Object[] var1) {
      return this.Vulcan_U;
   }

   public String Vulcan_I(Object[] var1) {
      return this.Vulcan_i;
   }

   public boolean Vulcan_S(Object[] var1) {
      return this.Vulcan_y;
   }

   public boolean Vulcan_X(Object[] var1) {
      return this.Vulcan_q;
   }

   public boolean Vulcan_f(Object[] var1) {
      return this.Vulcan_W;
   }

   public boolean Vulcan_K(Object[] var1) {
      return this.Vulcan_H;
   }

   public int getTotalViolations() {
      return this.Vulcan__;
   }

   public int getCombatViolations() {
      return this.Vulcan_n;
   }

   public int getMovementViolations() {
      return this.Vulcan_o;
   }

   public int getPlayerViolations() {
      return this.Vulcan_m;
   }

   public int getAutoClickerViolations() {
      return this.Vulcan_s;
   }

   public int getScaffoldViolations() {
      return this.Vulcan_k;
   }

   public int getTimerViolations() {
      return this.Vulcan_e;
   }

   public long getJoinTime() {
      return this.Vulcan_S;
   }

   public int getJoinTicks() {
      return this.Vulcan_A;
   }

   public long getLastClientBrandAlert() {
      return this.Vulcan_a;
   }

   public long Vulcan_R(Object[] var1) {
      return this.Vulcan_B;
   }

   public WrapperPlayClientPlayerFlying Vulcan_Y(Object[] var1) {
      return this.Vulcan_F;
   }

   public WrapperPlayClientInteractEntity Vulcan_z(Object[] var1) {
      return this.Vulcan_I;
   }

   public WrapperPlayClientPlayerDigging Vulcan_S(Object[] var1) {
      return this.Vulcan_l;
   }

   public WrapperPlayClientEntityAction Vulcan_X(Object[] var1) {
      return this.Vulcan_E;
   }

   public WrapperPlayClientHeldItemChange Vulcan_O(Object[] var1) {
      return this.Vulcan_J;
   }

   public WrapperPlayClientPlayerBlockPlacement Vulcan_l(Object[] var1) {
      return this.Vulcan_f;
   }

   public int Vulcan_M(Object[] var1) {
      return this.Vulcan_x;
   }

   public List Vulcan_N(Object[] var1) {
      return this.Vulcan_b;
   }

   public HashSet Vulcan_E(Object[] var1) {
      return this.Vulcan_u;
   }

   public HashSet Vulcan_B(Object[] var1) {
      return this.Vulcan_Q;
   }

   public Vulcan_iB Vulcan_L(Object[] var1) {
      return this.Vulcan_p;
   }

   public Vulcan_iO Vulcan_W(Object[] var1) {
      return this.Vulcan_t;
   }

   public Vulcan_iG Vulcan_J(Object[] var1) {
      return this.Vulcan_d;
   }

   public Vulcan_it Vulcan_B(Object[] var1) {
      return this.Vulcan_w;
   }

   public Vulcan_iJ Vulcan_e(Object[] var1) {
      return this.Vulcan_Y;
   }

   public Vulcan_T Vulcan_w(Object[] var1) {
      return this.Vulcan_h;
   }

   public Vulcan_k Vulcan_u(Object[] var1) {
      return this.Vulcan_Z;
   }

   public Vulcan_XU Vulcan_a(Object[] var1) {
      return this.Vulcan_K;
   }

   public Vulcan_ee Vulcan_P(Object[] var1) {
      return this.Vulcan_L;
   }

   public Vulcan_c Vulcan_R(Object[] var1) {
      return this.Vulcan_R;
   }

   public Vulcan_c Vulcan_g(Object[] var1) {
      return this.Vulcan_V;
   }

   public void Vulcan_B(@Nullable Player var1) {
      this.Vulcan_v = var1;
   }

   public void Vulcan_t(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_N = var2;
   }

   public void Vulcan_w(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T = var2;
   }

   public void Vulcan_j(Object[] var1) {
      UUID var2 = (UUID)var1[0];
      this.Vulcan_P = var2;
   }

   public void Vulcan_d(Object[] var1) {
      ClientVersion var2 = (ClientVersion)var1[0];
      this.Vulcan_U = var2;
   }

   public void Vulcan_c(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_i = var2;
   }

   public void Vulcan_f(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_y = var2;
   }

   public void Vulcan_T(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q = var2;
   }

   public void Vulcan_p(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_W = var2;
   }

   public void Vulcan_E(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_H = var2;
   }

   public void Vulcan_Q(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan__ = var2;
   }

   public void Vulcan_X(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_n = var2;
   }

   public void Vulcan_R(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_o = var2;
   }

   public void Vulcan_q(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = var2;
   }

   public void Vulcan_r(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_s = var2;
   }

   public void Vulcan_e(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_k = var2;
   }

   public void Vulcan_K(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_e = var2;
   }

   public void Vulcan_N(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_a = var2;
   }

   public void Vulcan_v(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_B = var2;
   }

   public void Vulcan_y(Object[] var1) {
      WrapperPlayClientPlayerFlying var2 = (WrapperPlayClientPlayerFlying)var1[0];
      this.Vulcan_F = var2;
   }

   public void Vulcan_O(Object[] var1) {
      WrapperPlayClientInteractEntity var2 = (WrapperPlayClientInteractEntity)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      WrapperPlayClientPlayerDigging var2 = (WrapperPlayClientPlayerDigging)var1[0];
      this.Vulcan_l = var2;
   }

   public void Vulcan_S(Object[] var1) {
      WrapperPlayClientEntityAction var2 = (WrapperPlayClientEntityAction)var1[0];
      this.Vulcan_E = var2;
   }

   public void Vulcan_P(Object[] var1) {
      WrapperPlayClientHeldItemChange var2 = (WrapperPlayClientHeldItemChange)var1[0];
      this.Vulcan_J = var2;
   }

   public void Vulcan__(Object[] var1) {
      WrapperPlayClientPlayerBlockPlacement var2 = (WrapperPlayClientPlayerBlockPlacement)var1[0];
      this.Vulcan_f = var2;
   }

   public void Vulcan_o(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_x = var2;
   }

   public void Vulcan_U(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_b = var2;
   }

   public void Vulcan_L(Object[] var1) {
      HashSet var2 = (HashSet)var1[0];
      this.Vulcan_u = var2;
   }

   public void Vulcan_x(Object[] var1) {
      HashSet var2 = (HashSet)var1[0];
      this.Vulcan_Q = var2;
   }

   private void lambda$handleClientBrand$3() {
      Vulcan_eQ var10000 = Vulcan_Xs.INSTANCE.Vulcan_y();
      String[] var1 = b;
      var10000.Vulcan_I(new Object[]{Vulcan_i9.Vulcan__.replaceAll(var1[1], this.Vulcan_v.getName()).replaceAll(var1[10], this.Vulcan_g.getClientVersion().getReleaseName()).replaceAll(var1[7], this.Vulcan_N)});
   }

   private static void lambda$handleClientBrand$2(String var0) {
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_I(new Object[]{var0});
   }

   private void lambda$handleClientBrand$1() {
      long var1 = a ^ 44426740531333L;
      long var3 = var1 ^ 19367982438990L;
      String[] var5 = b;
      this.Vulcan_v.sendMessage(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(var5[2], this.Vulcan_N), var3}));
      this.Vulcan_v.kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(var5[7], this.Vulcan_N), var3}));
   }

   private void lambda$handleClientBrand$0() {
      long var1 = a ^ 57208873117241L;
      long var3 = var1 ^ 15382009052402L;
      this.Vulcan_v.kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(b[7], this.Vulcan_N), var3}));
   }

   public static void Vulcan_a(int[] var0) {
      Vulcan_M = var0;
   }

   public static int[] Vulcan_X() {
      return Vulcan_M;
   }

   static {
      int[] var10000 = new int[4];
      long var0 = a ^ 3001040943391L;
      Vulcan_a(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[11];
      int var7 = 0;
      String var6 = "\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u0016¬Ýíò\u0011±_â'´ÁÁÂ#ö\u0010\u0086zãmÌü§\u0016·\u0013¦\u0016\rõ$\u0097\u0010ÏÈO35v\u0092ÍF\u0097Fe:d%Ü\u00105é9\t6\u0003ùc°\u0080t4Ù¨ú\u001e\u00105é9\t6\u0003ùc°\u0080t4Ù¨ú\u001e\u0010\u0086zãmÌü§\u0016·\u0013¦\u0016\rõ$\u0097(\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u0016ùÝáu\u009a»!ô\u009a\u008e½\u0014\nñÝ\u0010ÏÈO35v\u0092ÍF\u0097Fe:d%Ü\u0018ÏÈO35v\u0092ÍÍ\u0088UDó¶&`iÇ\u0019\u0013]\u001a\u0000c";
      int var8 = "\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u0016¬Ýíò\u0011±_â'´ÁÁÂ#ö\u0010\u0086zãmÌü§\u0016·\u0013¦\u0016\rõ$\u0097\u0010ÏÈO35v\u0092ÍF\u0097Fe:d%Ü\u00105é9\t6\u0003ùc°\u0080t4Ù¨ú\u001e\u00105é9\t6\u0003ùc°\u0080t4Ù¨ú\u001e\u0010\u0086zãmÌü§\u0016·\u0013¦\u0016\rõ$\u0097(\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u0016ùÝáu\u009a»!ô\u009a\u008e½\u0014\nñÝ\u0010ÏÈO35v\u0092ÍF\u0097Fe:d%Ü\u0018ÏÈO35v\u0092ÍÍ\u0088UDó¶&`iÇ\u0019\u0013]\u001a\u0000c".length();
      char var5 = '(';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var16;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var16;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u009b¡@\u0015¡ÿºfTµðÏ99dú\u0018ÏÈO35v\u0092ÍÍ\u0088UDó¶&`iÇ\u0019\u0013]\u001a\u0000c";
               var8 = "\u0091aë\u0016çqK\u0017J@\u009d6ÙA*rK\u000e:10Iÿq\u009b¡@\u0015¡ÿºfTµðÏ99dú\u0018ÏÈO35v\u0092ÍÍ\u0088UDó¶&`iÇ\u0019\u0013]\u001a\u0000c".length();
               var5 = '(';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
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
