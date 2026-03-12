package me.frep.vulcan.spigot.check;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.spigot.Vulcan_Xs;
import me.frep.vulcan.spigot.Vulcan_eG;
import me.frep.vulcan.spigot.Vulcan_i9;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_iI;
import me.frep.vulcan.spigot.Vulcan_in;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.api.CheckInfo;

public abstract class AbstractCheck implements Check {
   protected final Vulcan_iE Vulcan_Q;
   private int Vulcan_I;
   private double Vulcan_q;
   public final String Vulcan_a;
   public final double Vulcan_K;
   public final double Vulcan_c;
   public final double Vulcan_v;
   private static int Vulcan_C;
   private static final long a = Vulcan_n.a(4807560777699080189L, -5861796737408826978L, MethodHandles.lookup().lookupClass()).a(34421015956501L);
   private static final String[] c;

   public AbstractCheck(Vulcan_iE var1) {
      long var2 = a ^ 93527282075501L;
      super();
      boolean var10000 = Vulcan_iI.Vulcan_F();
      this.Vulcan_a = this.getClass().getSimpleName();
      boolean var4 = var10000;
      this.Vulcan_K = (double)(Integer)Vulcan_i9.Vulcan_QB.get(this.Vulcan_a);
      this.Vulcan_c = (Double)Vulcan_i9.Vulcan_Q7.get(this.Vulcan_a);
      this.Vulcan_v = (Double)Vulcan_i9.Vulcan_FV.get(this.Vulcan_a);
      this.Vulcan_Q = var1;
      if (var4) {
         int var5 = Vulcan_m();
         ++var5;
         Vulcan_H(var5);
      }

   }

   public abstract void Vulcan_O(Object[] var1);

   public void Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_A(Object[] var1) {
      Vulcan_Xs.INSTANCE.Vulcan__().execute(this::lambda$punish$2);
   }

   protected boolean Vulcan_K(Object[] var1) {
      long var3 = (Long)var1[0];
      Vulcan_in[] var2 = (Vulcan_in[])var1[1];
      var3 ^= a;
      long var5 = var3 ^ 112707478603802L;
      return this.Vulcan_Q.Vulcan_L(new Object[0]).Vulcan_d(new Object[]{var2, var5});
   }

   public int Vulcan_j(Object[] var1) {
      return Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public double Vulcan_s(Object[] var1) {
      return this.Vulcan_q = Math.min(10000.0D, this.Vulcan_q + 1.0D);
   }

   public double Vulcan_H(Object[] var1) {
      double var2 = (Double)var1[0];
      return this.Vulcan_q = Math.max(0.0D, this.Vulcan_q - var2);
   }

   public double Vulcan_L(Object[] var1) {
      double var2 = (Double)var1[0];
      return this.Vulcan_q = Math.min(10000.0D, this.Vulcan_q + var2);
   }

   public void Vulcan_L(Object[] var1) {
      this.Vulcan_q = 0.0D;
   }

   public void Vulcan_s(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_q *= var2;
   }

   public void Vulcan_Q(Object[] var1) {
      this.Vulcan_H(new Object[]{this.Vulcan_c});
   }

   public int Vulcan_i(Object[] var1) {
      return this.Vulcan_Q.Vulcan_W(new Object[0]).Vulcan_c(new Object[0]);
   }

   public boolean Vulcan_s(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public CheckInfo Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_j(Object[] var1) {
      long var3 = (Long)var1[0];
      Object var2 = (Object)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 3692612328074L;
      Vulcan_eG.Vulcan_x(new Object[]{var2, var5});
   }

   public String getCategory() {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_V(Object[] var1) {
      return this.Vulcan_Q.Vulcan_J(new Object[0]).Vulcan_x(new Object[0]);
   }

   public boolean Vulcan_c(Object[] var1) {
      return this.Vulcan_Q.Vulcan_e(new Object[0]).Vulcan_v(new Object[0]);
   }

   public String getName() {
      long var1 = a ^ 110217402993126L;
      long var3 = var1 ^ 90436389569875L;
      return this.Vulcan_m(new Object[]{var3}).name().toLowerCase().replaceAll(" ", "");
   }

   public char getType() {
      long var1 = a ^ 74232763932515L;
      long var3 = var1 ^ 129733920625622L;
      return Character.toLowerCase(this.Vulcan_m(new Object[]{var3}).type());
   }

   public String toString() {
      return super.toString();
   }

   public int getVl() {
      return this.Vulcan_I;
   }

   public int getMaxVl() {
      return (Integer)Vulcan_i9.Vulcan_a1.get(this.Vulcan_a);
   }

   public int getMinimumVlToNotify() {
      return (Integer)Vulcan_i9.Vulcan_dW.get(this.Vulcan_a);
   }

   public double getMaxBuffer() {
      return this.Vulcan_K;
   }

   public String getDescription() {
      long var1 = a ^ 98386492044033L;
      long var3 = var1 ^ 122074818412468L;
      return this.Vulcan_m(new Object[]{var3}).description();
   }

   public String getComplexType() {
      long var1 = a ^ 122539041284424L;
      long var3 = var1 ^ 97923002117629L;
      return this.Vulcan_m(new Object[]{var3}).complexType();
   }

   public double getBufferDecay() {
      return this.Vulcan_c;
   }

   public int getAlertInterval() {
      return (Integer)Vulcan_i9.Vulcan_Qf.get(this.Vulcan_a);
   }

   public double getBufferMultiple() {
      return this.Vulcan_v;
   }

   public boolean isExperimental() {
      long var1 = a ^ 99685158091439L;
      long var3 = var1 ^ 119659460867610L;
      return this.Vulcan_m(new Object[]{var3}).experimental();
   }

   public boolean isPunishable() {
      return (Boolean)Vulcan_i9.Vulcan_ht.get(this.Vulcan_a);
   }

   public String getDisplayName() {
      long var1 = a ^ 69386667374991L;
      long var3 = var1 ^ 10337825748282L;
      return this.Vulcan_m(new Object[]{var3}).name();
   }

   public char getDisplayType() {
      long var1 = a ^ 4178673812036L;
      long var3 = var1 ^ 59035627359985L;
      return this.Vulcan_m(new Object[]{var3}).type();
   }

   public void Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_n(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_o(Object[] var1) {
      PacketEvent var4 = (PacketEvent)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.INTERACT_ENTITY) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_P(Object[] var1) {
      PacketEvent var4 = (PacketEvent)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.PLAYER_BLOCK_PLACEMENT) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_g(Object[] var1) {
      PacketEvent var2 = (PacketEvent)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.USE_ITEM) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_U(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.ANIMATION) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.VEHICLE_MOVE) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_F();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.ENTITY_ACTION) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_M(Object[] var1) {
      PacketEvent var4 = (PacketEvent)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.PLAYER_DIGGING) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_X(Object[] var1) {
      PacketEvent var2 = (PacketEvent)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.PLAYER_ABILITIES) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_T(Object[] var1) {
      PacketEvent var2 = (PacketEvent)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.STEER_VEHICLE) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_r(Object[] var1) {
      PacketEvent var2 = (PacketEvent)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.CLIENT_STATUS) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_C(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.HELD_ITEM_CHANGE) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_f(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.KEEP_ALIVE) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_F();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.CLICK_WINDOW) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_z(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_t();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlayReceiveEvent;
            if (!var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var2;

      try {
         if (var6.getPacketType() == Client.SPECTATE) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_Z(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
      long var10000 = a ^ var3;
      boolean var5 = Vulcan_iI.Vulcan_F();

      boolean var9;
      label46: {
         try {
            var9 = var2 instanceof PacketPlaySendEvent;
            if (var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlaySendEvent var6 = (PacketPlaySendEvent)var2;

      try {
         if (var6.getPacketType() == Server.PLAYER_POSITION_AND_LOOK) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public boolean Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
      long var10000 = a ^ var2;
      boolean var5 = Vulcan_iI.Vulcan_F();

      boolean var9;
      label46: {
         try {
            var9 = var4 instanceof PacketPlayReceiveEvent;
            if (var5) {
               return var9;
            }

            if (var9) {
               break label46;
            }
         } catch (RuntimeException var8) {
            throw b(var8);
         }

         var9 = false;
         return var9;
      }

      PacketPlayReceiveEvent var6 = (PacketPlayReceiveEvent)var4;

      try {
         if (var6.getPacketType() == Client.CLIENT_TICK_END) {
            var9 = true;
            return var9;
         }
      } catch (RuntimeException var7) {
         throw b(var7);
      }

      var9 = false;
      return var9;
   }

   public abstract void Vulcan_i(Object[] var1);

   public Vulcan_iE Vulcan_o(Object[] var1) {
      return this.Vulcan_Q;
   }

   public double getBuffer() {
      return this.Vulcan_q;
   }

   public String Vulcan_I(Object[] var1) {
      return this.Vulcan_a;
   }

   public double Vulcan_g(Object[] var1) {
      return this.Vulcan_K;
   }

   public double Vulcan__(Object[] var1) {
      return this.Vulcan_c;
   }

   public double Vulcan_o(Object[] var1) {
      return this.Vulcan_v;
   }

   public void setVl(int var1) {
      this.Vulcan_I = var1;
   }

   public void setBuffer(double var1) {
      this.Vulcan_q = var1;
   }

   private void lambda$punish$2() {
      long var1 = a ^ 8078649803210L;
      long var3 = var1 ^ 61378203677929L;
      Vulcan_Xs.INSTANCE.Vulcan_q().Vulcan_v(new Object[]{this, this.Vulcan_Q, var3});
   }

   private void lambda$fail$1() {
      long var1 = a ^ 24510487753709L;
      long var3 = var1 ^ 10592868173445L;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_P(new Object[]{this, this.Vulcan_Q, var3, ""});
   }

   private void lambda$fail$0(Object var1) {
      long var2 = a ^ 44509526569344L;
      long var4 = var2 ^ 61107658704104L;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_P(new Object[]{this, this.Vulcan_Q, var4, Objects.toString(var1)});
   }

   public static void Vulcan_H(int var0) {
      Vulcan_C = var0;
   }

   public static int Vulcan_m() {
      return Vulcan_C;
   }

   public static int Vulcan_V() {
      int var0 = Vulcan_m();

      try {
         return var0 == 0 ? 47 : 0;
      } catch (RuntimeException var1) {
         throw b(var1);
      }
   }

   static {
      long var0 = a ^ 7644268054185L;
      Vulcan_H(0);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[9];
      int var7 = 0;
      String var6 = "\u00915Vµ³_§+W:qørP\u000b\u0094å~ò\u0004\u001déÐuJ\\ÅØ\u008c$\u0094\u0082>2¥x\r\u00043\u0000Ü0_\u0019YU\u001eÖ\u0099ª\u008a\u0091/4\u0095K\u0010{!\u00ad\u008c\u0093%n°\r\u0087Þ|\u000e¸\u0016\u007f\b<7ä\u0000b\u009a\u0002Ô\u0010{!\u00ad\u008c\u0093%n°\r\u0087Þ|\u000e¸\u0016\u007f\bÕ.ÿ\u007f\bZ\"_\u0018\u001a=^\u000ba[Î\u0091·W\u0005»\u008d8ÓÝ\u0080Ë\u0017\u001b¦\u0091\u0007\u001d\bÕ.ÿ\u007f\bZ\"_";
      int var8 = "\u00915Vµ³_§+W:qørP\u000b\u0094å~ò\u0004\u001déÐuJ\\ÅØ\u008c$\u0094\u0082>2¥x\r\u00043\u0000Ü0_\u0019YU\u001eÖ\u0099ª\u008a\u0091/4\u0095K\u0010{!\u00ad\u008c\u0093%n°\r\u0087Þ|\u000e¸\u0016\u007f\b<7ä\u0000b\u009a\u0002Ô\u0010{!\u00ad\u008c\u0093%n°\r\u0087Þ|\u000e¸\u0016\u007f\bÕ.ÿ\u007f\bZ\"_\u0018\u001a=^\u000ba[Î\u0091·W\u0005»\u008d8ÓÝ\u0080Ë\u0017\u001b¦\u0091\u0007\u001d\bÕ.ÿ\u007f\bZ\"_".length();
      char var5 = '8';
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
                  c = var9;
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

               var6 = "<7ä\u0000b\u009a\u0002Ô\u0018\u001a=^\u000ba[Î\u0091·W\u0005»\u008d8ÓÝ\u0080Ë\u0017\u001b¦\u0091\u0007\u001d";
               var8 = "<7ä\u0000b\u009a\u0002Ô\u0018\u001a=^\u000ba[Î\u0091·W\u0005»\u008d8ÓÝ\u0080Ë\u0017\u001b¦\u0091\u0007\u001d".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static RuntimeException b(RuntimeException var0) {
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
