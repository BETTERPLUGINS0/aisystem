package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus.Action;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Vulcan_iG {
   private final Vulcan_iE Vulcan_X;
   private boolean Vulcan_qn;
   private boolean Vulcan_j;
   private boolean Vulcan_A;
   private boolean Vulcan_OG;
   private boolean Vulcan_Oy;
   private boolean Vulcan_q;
   private boolean Vulcan_t;
   private boolean Vulcan_R;
   private boolean Vulcan_OS;
   private boolean Vulcan_qS;
   private boolean Vulcan_S;
   private boolean Vulcan_q5;
   private boolean Vulcan_i;
   private boolean Vulcan_q1;
   private boolean Vulcan_qQ;
   private boolean Vulcan_Oj;
   private boolean Vulcan_M;
   private boolean Vulcan_q3;
   private boolean Vulcan_v;
   private boolean Vulcan_qF;
   private boolean Vulcan_qH;
   private boolean Vulcan_qf;
   private boolean Vulcan_Ou;
   private boolean Vulcan_O6;
   private boolean Vulcan_u;
   private boolean Vulcan_qX;
   private boolean Vulcan_OE;
   private boolean Vulcan_OT;
   private boolean Vulcan_qK;
   private boolean Vulcan_k;
   private boolean Vulcan_qE;
   private boolean Vulcan_s;
   private boolean Vulcan_a;
   private boolean Vulcan_Os;
   private boolean Vulcan_q4;
   private double Vulcan_J = 0.1D;
   private double Vulcan_O = 0.6D;
   private double Vulcan_f = 0.42D;
   private double Vulcan_O7 = 0.08D;
   private double Vulcan_qt = 0.42D;
   private double Vulcan_O_ = 0.08D;
   private double Vulcan_N = 3.0D;
   final LinkedList Vulcan_qd = new LinkedList();
   private boolean Vulcan_qG;
   private int Vulcan_x;
   private int Vulcan_Og;
   private int Vulcan_Om;
   private int Vulcan_qZ;
   private int Vulcan_q9 = 0;
   private int Vulcan_qI;
   private int Vulcan_h;
   private int Vulcan_qJ;
   private int Vulcan_B;
   private int Vulcan_C;
   private int Vulcan_Oz = 100;
   private int Vulcan_OA;
   private int Vulcan_Oh;
   private int Vulcan_qR;
   private int Vulcan_Oc;
   private int Vulcan_qU;
   private int Vulcan_qN;
   private int Vulcan_qC = 100;
   private int Vulcan_g;
   private int Vulcan_qA;
   private int Vulcan_q7;
   private int Vulcan_qu;
   private int Vulcan_ql;
   private int Vulcan_U = 100;
   private int Vulcan_o = 100;
   private int Vulcan_F = 100;
   private int Vulcan_e;
   private int Vulcan_qP;
   private int Vulcan_qr;
   private int Vulcan_d;
   private int Vulcan_On;
   private int Vulcan_qp;
   private int Vulcan_P = 100;
   private int Vulcan_O0 = 100;
   private int Vulcan_Ok = 100;
   private int Vulcan_O2 = 150;
   private int Vulcan_qa = 100;
   private int Vulcan_OK = 100;
   private int Vulcan_qY;
   private int Vulcan_Q = 100;
   private int Vulcan_O4 = 100;
   private int Vulcan_OJ = 100;
   private int Vulcan_b;
   private int Vulcan_O9;
   private int Vulcan_qb;
   private int Vulcan_q2;
   private int Vulcan__;
   private int Vulcan_D;
   private int Vulcan_qk = 100;
   private int Vulcan_qL = 100;
   private int Vulcan_qs = 0;
   private int Vulcan_qx = 0;
   private int Vulcan_qm = 0;
   private int Vulcan_OB = 0;
   private int Vulcan_q_ = 0;
   private int Vulcan_OM = 100;
   private int Vulcan_qq = 100;
   private int Vulcan_qz = 100;
   private int Vulcan_m = 100;
   private int Vulcan_qc = 100;
   private int Vulcan_q0 = 100;
   private int Vulcan_OP = 100;
   private int Vulcan_O8 = 0;
   private int Vulcan_qT;
   private int Vulcan_Oq = 0;
   private int Vulcan_c = 100;
   private int Vulcan_K = 500;
   private int Vulcan_qw = 1000;
   private int Vulcan_qh = 1000;
   private int Vulcan_O3 = 100;
   private int Vulcan_G = 100;
   private int Vulcan_qV = 100;
   private int Vulcan_q8 = 100;
   private int Vulcan_H = 100;
   private int Vulcan_qM = 100;
   private int Vulcan_Y = 100;
   private int Vulcan_OV = 0;
   private int Vulcan_qj = 0;
   private int Vulcan_qe = 0;
   private int Vulcan_r = 100;
   private int Vulcan_qO = 0;
   private int Vulcan_Oi;
   private int Vulcan_l;
   private int Vulcan_qo = 0;
   private int Vulcan_q6 = 100;
   private double Vulcan_z;
   private double Vulcan_qB;
   private double Vulcan_y;
   private double Vulcan_qg;
   private double Vulcan_OZ;
   private double Vulcan_w;
   private double Vulcan_ON;
   private double Vulcan_I;
   private double Vulcan_OX;
   private double Vulcan_L;
   private double Vulcan_T;
   private double Vulcan_p;
   private double Vulcan_qD;
   private double Vulcan_qy;
   private long Vulcan_Z;
   private long Vulcan_OU;
   private long Vulcan_Of;
   private GameMode Vulcan_Ot;
   private ItemStack Vulcan_qi;
   private ItemStack Vulcan_W;
   private ItemStack Vulcan_V;
   private ItemStack Vulcan_n;
   private ItemStack Vulcan_OC;
   private ItemStack Vulcan_O1;
   private long Vulcan_OI;
   private long Vulcan_qW;
   final Vulcan_c Vulcan_E;
   private static boolean Vulcan_qv;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3748277762326030108L, 5069529082802369886L, MethodHandles.lookup().lookupClass()).a(161306748975725L);
   private static final String[] b;

   public Vulcan_iG(Vulcan_iE var1) {
      this.Vulcan_Ot = GameMode.SURVIVAL;
      this.Vulcan_E = new Vulcan_c(15);
      this.Vulcan_X = var1;
   }

   public void Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Go(Object[] var1) {
      WrapperPlayServerUpdateAttributes var4 = (WrapperPlayServerUpdateAttributes)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 90821256486840L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleUpdateAttributes$0, var5});
   }

   public void Vulcan_P9(Object[] var1) {
      this.Vulcan_qR = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_z(Object[] var1) {
      this.Vulcan_x = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_yO(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Uv(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_r(Object[] var1) {
      this.Vulcan_Oc = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_f(Object[] var1) {
      WrapperPlayClientClientStatus var4 = (WrapperPlayClientClientStatus)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;

      try {
         if (var4.getAction() == Action.PERFORM_RESPAWN) {
            this.Vulcan_qA = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public void Vulcan_Pc(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_L(Object[] var1) {
      this.Vulcan_qI = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_Pr(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Pv(Object[] var1) {
      this.Vulcan_O0 = 0;
   }

   public void Vulcan_PH(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Mc(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Pb(Object[] var1) {
      this.Vulcan_Q = 0;
   }

   public void Vulcan_SI(Object[] var1) {
      WrapperPlayServerPlayerPositionAndLook var2 = (WrapperPlayServerPlayerPositionAndLook)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 92621348532519L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleServerPosition$1, var5});
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_g(new Object[]{this.Vulcan_qd::poll});
   }

   public void Vulcan_Lu(Object[] var1) {
      WrapperPlayServerEntityEffect var2 = (WrapperPlayServerEntityEffect)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 7029511941134L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleEntityEffect$2, var5});
   }

   public void Vulcan_n8(Object[] var1) {
      WrapperPlayServerRemoveEntityEffect var2 = (WrapperPlayServerRemoveEntityEffect)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 31744407640434L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleRemoveEntityEffect$3, var5});
   }

   public void Vulcan_X4(Object[] var1) {
      WrapperPlayServerChangeGameState var4 = (WrapperPlayServerChangeGameState)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 36426067537302L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleGameStateChange$4, var5});
   }

   public void Vulcan_g(Object[] var1) {
      this.Vulcan_OK = 0;
      this.Vulcan_X.Vulcan_e(new Object[0]).Vulcan_Hn(new Object[]{0});
   }

   public void Vulcan_P3(Object[] var1) {
      this.Vulcan_O4 = 0;
   }

   public void Vulcan_OD(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_PM(Object[] var1) {
      this.Vulcan_qz = 0;
   }

   public void Vulcan_PZ(Object[] var1) {
      this.Vulcan_OA = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_PS(Object[] var1) {
      this.Vulcan_Oh = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_W(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_P(Object[] var1) {
      this.Vulcan_O2 = 0;
   }

   public void Vulcan_j(Object[] var1) {
      this.Vulcan_qa = 0;
   }

   public void Vulcan_PB(Object[] var1) {
      this.Vulcan_q0 = 0;
   }

   public void Vulcan_P_(Object[] var1) {
      this.Vulcan_Ok = 0;
   }

   public void Vulcan_M(Object[] var1) {
      this.Vulcan_qq = 0;
   }

   public void Vulcan_Px(Object[] var1) {
      this.Vulcan_qu = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_P1(Object[] var1) {
      this.Vulcan_ql = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_o(Object[] var1) {
      WrapperPlayServerPlayerAbilities var2 = (WrapperPlayServerPlayerAbilities)var1[0];
      this.Vulcan_Oq = 0;
   }

   public void Vulcan_PI(Object[] var1) {
      this.Vulcan_qY = 0;
   }

   public void Vulcan_u(Object[] var1) {
      BlockPlaceEvent var2 = (BlockPlaceEvent)var1[0];
      this.Vulcan_O9 = var2.getBlockPlaced().getX();
      this.Vulcan_qb = var2.getBlockPlaced().getY();
      this.Vulcan_q2 = var2.getBlockPlaced().getZ();
   }

   public void Vulcan_C(Object[] var1) {
      this.Vulcan_OM = 0;
   }

   public void Vulcan_dr(Object[] var1) {
      long var2 = (Long)var1[0];
      WrapperPlayServerEntityMetadata var4 = (WrapperPlayServerEntityMetadata)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 86058232558082L;
      this.Vulcan_X.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleMetaData$5, var5});
   }

   public static EntityData Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iE Vulcan__(Object[] var1) {
      return this.Vulcan_X;
   }

   public boolean Vulcan_M(Object[] var1) {
      return this.Vulcan_qn;
   }

   public boolean Vulcan_N(Object[] var1) {
      return this.Vulcan_j;
   }

   public boolean Vulcan_o(Object[] var1) {
      return this.Vulcan_A;
   }

   public boolean Vulcan_h(Object[] var1) {
      return this.Vulcan_OG;
   }

   public boolean Vulcan_F(Object[] var1) {
      return this.Vulcan_Oy;
   }

   public boolean Vulcan_U(Object[] var1) {
      return this.Vulcan_q;
   }

   public boolean Vulcan_p(Object[] var1) {
      return this.Vulcan_t;
   }

   public boolean Vulcan_x(Object[] var1) {
      return this.Vulcan_R;
   }

   public boolean Vulcan_y(Object[] var1) {
      return this.Vulcan_OS;
   }

   public boolean Vulcan_Z(Object[] var1) {
      return this.Vulcan_qS;
   }

   public boolean Vulcan_t(Object[] var1) {
      return this.Vulcan_S;
   }

   public boolean Vulcan_H(Object[] var1) {
      return this.Vulcan_q5;
   }

   public boolean Vulcan_e(Object[] var1) {
      return this.Vulcan_i;
   }

   public boolean Vulcan_L(Object[] var1) {
      return this.Vulcan_q1;
   }

   public boolean Vulcan_E(Object[] var1) {
      return this.Vulcan_qQ;
   }

   public boolean Vulcan_m(Object[] var1) {
      return this.Vulcan_Oj;
   }

   public boolean Vulcan_R(Object[] var1) {
      return this.Vulcan_M;
   }

   public boolean Vulcan_P(Object[] var1) {
      return this.Vulcan_q3;
   }

   public boolean Vulcan_B(Object[] var1) {
      return this.Vulcan_v;
   }

   public boolean Vulcan_c(Object[] var1) {
      return this.Vulcan_qF;
   }

   public boolean Vulcan_s(Object[] var1) {
      return this.Vulcan_qH;
   }

   public boolean Vulcan_X(Object[] var1) {
      return this.Vulcan_qf;
   }

   public boolean Vulcan_K(Object[] var1) {
      return this.Vulcan_Ou;
   }

   public boolean Vulcan_A(Object[] var1) {
      return this.Vulcan_O6;
   }

   public boolean Vulcan_w(Object[] var1) {
      return this.Vulcan_u;
   }

   public boolean Vulcan_I(Object[] var1) {
      return this.Vulcan_qX;
   }

   public boolean Vulcan_O(Object[] var1) {
      return this.Vulcan_OE;
   }

   public boolean Vulcan_u(Object[] var1) {
      return this.Vulcan_OT;
   }

   public boolean Vulcan_Y(Object[] var1) {
      return this.Vulcan_qK;
   }

   public boolean Vulcan_g(Object[] var1) {
      return this.Vulcan_k;
   }

   public boolean Vulcan_z(Object[] var1) {
      return this.Vulcan_qE;
   }

   public boolean Vulcan_D(Object[] var1) {
      return this.Vulcan_s;
   }

   public boolean Vulcan_v(Object[] var1) {
      return this.Vulcan_a;
   }

   public boolean Vulcan_q(Object[] var1) {
      return this.Vulcan_Os;
   }

   public boolean Vulcan_n(Object[] var1) {
      return this.Vulcan_q4;
   }

   public double Vulcan_E(Object[] var1) {
      return this.Vulcan_J;
   }

   public double Vulcan_g(Object[] var1) {
      return this.Vulcan_O;
   }

   public double Vulcan_s(Object[] var1) {
      return this.Vulcan_f;
   }

   public double Vulcan_o(Object[] var1) {
      return this.Vulcan_O7;
   }

   public double Vulcan_D(Object[] var1) {
      return this.Vulcan_qt;
   }

   public double Vulcan_c(Object[] var1) {
      return this.Vulcan_O_;
   }

   public double Vulcan_O(Object[] var1) {
      return this.Vulcan_N;
   }

   public LinkedList Vulcan_T(Object[] var1) {
      return this.Vulcan_qd;
   }

   public boolean Vulcan_f(Object[] var1) {
      return this.Vulcan_qG;
   }

   public int Vulcan_n(Object[] var1) {
      return this.Vulcan_x;
   }

   public int Vulcan_pU(Object[] var1) {
      return this.Vulcan_Og;
   }

   public int Vulcan_r(Object[] var1) {
      return this.Vulcan_Om;
   }

   public int Vulcan_U(Object[] var1) {
      return this.Vulcan_qZ;
   }

   public int Vulcan_pq(Object[] var1) {
      return this.Vulcan_q9;
   }

   public int Vulcan_pL(Object[] var1) {
      return this.Vulcan_qI;
   }

   public int Vulcan_pS(Object[] var1) {
      return this.Vulcan_h;
   }

   public int Vulcan_b(Object[] var1) {
      return this.Vulcan_qJ;
   }

   public int Vulcan_g(Object[] var1) {
      return this.Vulcan_B;
   }

   public int Vulcan__(Object[] var1) {
      return this.Vulcan_C;
   }

   public int Vulcan_c(Object[] var1) {
      return this.Vulcan_Oz;
   }

   public int Vulcan_T(Object[] var1) {
      return this.Vulcan_OA;
   }

   public int Vulcan_t(Object[] var1) {
      return this.Vulcan_Oh;
   }

   public int Vulcan_A(Object[] var1) {
      return this.Vulcan_qR;
   }

   public int Vulcan_pA(Object[] var1) {
      return this.Vulcan_Oc;
   }

   public int Vulcan_pQ(Object[] var1) {
      return this.Vulcan_qU;
   }

   public int Vulcan_H(Object[] var1) {
      return this.Vulcan_qN;
   }

   public int Vulcan_pM(Object[] var1) {
      return this.Vulcan_qC;
   }

   public int Vulcan_v(Object[] var1) {
      return this.Vulcan_g;
   }

   public int Vulcan_X(Object[] var1) {
      return this.Vulcan_qA;
   }

   public int Vulcan_P(Object[] var1) {
      return this.Vulcan_q7;
   }

   public int Vulcan_s(Object[] var1) {
      return this.Vulcan_qu;
   }

   public int Vulcan_l(Object[] var1) {
      return this.Vulcan_ql;
   }

   public int Vulcan_pz(Object[] var1) {
      return this.Vulcan_U;
   }

   public int Vulcan_h(Object[] var1) {
      return this.Vulcan_o;
   }

   public int Vulcan_f(Object[] var1) {
      return this.Vulcan_F;
   }

   public int Vulcan_p7(Object[] var1) {
      return this.Vulcan_e;
   }

   public int Vulcan_pO(Object[] var1) {
      return this.Vulcan_qP;
   }

   public int Vulcan_a(Object[] var1) {
      return this.Vulcan_qr;
   }

   public int Vulcan_K(Object[] var1) {
      return this.Vulcan_d;
   }

   public int Vulcan_W(Object[] var1) {
      return this.Vulcan_On;
   }

   public int Vulcan_pG(Object[] var1) {
      return this.Vulcan_qp;
   }

   public int Vulcan_px(Object[] var1) {
      return this.Vulcan_P;
   }

   public int Vulcan_u(Object[] var1) {
      return this.Vulcan_O0;
   }

   public int Vulcan_L(Object[] var1) {
      return this.Vulcan_Ok;
   }

   public int Vulcan_pC(Object[] var1) {
      return this.Vulcan_O2;
   }

   public int Vulcan_pg(Object[] var1) {
      return this.Vulcan_qa;
   }

   public int Vulcan_po(Object[] var1) {
      return this.Vulcan_OK;
   }

   public int Vulcan_x(Object[] var1) {
      return this.Vulcan_qY;
   }

   public int Vulcan_i(Object[] var1) {
      return this.Vulcan_Q;
   }

   public int Vulcan_C(Object[] var1) {
      return this.Vulcan_O4;
   }

   public int Vulcan_I(Object[] var1) {
      return this.Vulcan_OJ;
   }

   public int Vulcan_k(Object[] var1) {
      return this.Vulcan_b;
   }

   public int Vulcan_ps(Object[] var1) {
      return this.Vulcan_O9;
   }

   public int Vulcan_w(Object[] var1) {
      return this.Vulcan_qb;
   }

   public int Vulcan_pm(Object[] var1) {
      return this.Vulcan_q2;
   }

   public int Vulcan_B(Object[] var1) {
      return this.Vulcan__;
   }

   public int Vulcan_G(Object[] var1) {
      return this.Vulcan_D;
   }

   public int Vulcan_y(Object[] var1) {
      return this.Vulcan_qk;
   }

   public int Vulcan_J(Object[] var1) {
      return this.Vulcan_qL;
   }

   public int Vulcan_j(Object[] var1) {
      return this.Vulcan_qs;
   }

   public int Vulcan_z(Object[] var1) {
      return this.Vulcan_qx;
   }

   public int Vulcan_d(Object[] var1) {
      return this.Vulcan_qm;
   }

   public int Vulcan_Q(Object[] var1) {
      return this.Vulcan_OB;
   }

   public int Vulcan_pE(Object[] var1) {
      return this.Vulcan_q_;
   }

   public int Vulcan_py(Object[] var1) {
      return this.Vulcan_OM;
   }

   public int Vulcan_pe(Object[] var1) {
      return this.Vulcan_qq;
   }

   public int Vulcan_pa(Object[] var1) {
      return this.Vulcan_qz;
   }

   public int Vulcan_O(Object[] var1) {
      return this.Vulcan_m;
   }

   public int Vulcan_pt(Object[] var1) {
      return this.Vulcan_qc;
   }

   public int Vulcan_Z(Object[] var1) {
      return this.Vulcan_q0;
   }

   public int Vulcan_R(Object[] var1) {
      return this.Vulcan_OP;
   }

   public int Vulcan_M(Object[] var1) {
      return this.Vulcan_O8;
   }

   public int Vulcan_p0(Object[] var1) {
      return this.Vulcan_qT;
   }

   public int Vulcan_pZ(Object[] var1) {
      return this.Vulcan_Oq;
   }

   public int Vulcan_N(Object[] var1) {
      return this.Vulcan_c;
   }

   public int Vulcan_p3(Object[] var1) {
      return this.Vulcan_K;
   }

   public int Vulcan_pv(Object[] var1) {
      return this.Vulcan_qw;
   }

   public int Vulcan_pN(Object[] var1) {
      return this.Vulcan_qh;
   }

   public int Vulcan_Y(Object[] var1) {
      return this.Vulcan_O3;
   }

   public int Vulcan_pb(Object[] var1) {
      return this.Vulcan_G;
   }

   public int Vulcan_e(Object[] var1) {
      return this.Vulcan_qV;
   }

   public int Vulcan_V(Object[] var1) {
      return this.Vulcan_q8;
   }

   public int Vulcan_pK(Object[] var1) {
      return this.Vulcan_H;
   }

   public int Vulcan_pu(Object[] var1) {
      return this.Vulcan_qM;
   }

   public int Vulcan_S(Object[] var1) {
      return this.Vulcan_Y;
   }

   public int Vulcan_p8(Object[] var1) {
      return this.Vulcan_OV;
   }

   public int Vulcan_p9(Object[] var1) {
      return this.Vulcan_qj;
   }

   public int Vulcan_q(Object[] var1) {
      return this.Vulcan_qe;
   }

   public int Vulcan_F(Object[] var1) {
      return this.Vulcan_r;
   }

   public int Vulcan_E(Object[] var1) {
      return this.Vulcan_qO;
   }

   public int Vulcan_p(Object[] var1) {
      return this.Vulcan_Oi;
   }

   public int Vulcan_m(Object[] var1) {
      return this.Vulcan_l;
   }

   public int Vulcan_o(Object[] var1) {
      return this.Vulcan_qo;
   }

   public int Vulcan_D(Object[] var1) {
      return this.Vulcan_q6;
   }

   public double Vulcan_T(Object[] var1) {
      return this.Vulcan_z;
   }

   public double Vulcan_W(Object[] var1) {
      return this.Vulcan_qB;
   }

   public double Vulcan_r(Object[] var1) {
      return this.Vulcan_y;
   }

   public double Vulcan_l(Object[] var1) {
      return this.Vulcan_qg;
   }

   public double Vulcan__(Object[] var1) {
      return this.Vulcan_OZ;
   }

   public double Vulcan_p(Object[] var1) {
      return this.Vulcan_w;
   }

   public double Vulcan_f(Object[] var1) {
      return this.Vulcan_ON;
   }

   public double Vulcan_X(Object[] var1) {
      return this.Vulcan_I;
   }

   public double Vulcan_Y(Object[] var1) {
      return this.Vulcan_OX;
   }

   public double Vulcan_x(Object[] var1) {
      return this.Vulcan_L;
   }

   public double Vulcan_Z(Object[] var1) {
      return this.Vulcan_T;
   }

   public double Vulcan_R(Object[] var1) {
      return this.Vulcan_p;
   }

   public double Vulcan_U(Object[] var1) {
      return this.Vulcan_qD;
   }

   public double Vulcan_I(Object[] var1) {
      return this.Vulcan_qy;
   }

   public long Vulcan_M(Object[] var1) {
      return this.Vulcan_Z;
   }

   public long Vulcan_U(Object[] var1) {
      return this.Vulcan_OU;
   }

   public long Vulcan_h(Object[] var1) {
      return this.Vulcan_Of;
   }

   public GameMode Vulcan_f(Object[] var1) {
      return this.Vulcan_Ot;
   }

   public ItemStack Vulcan_x(Object[] var1) {
      return this.Vulcan_qi;
   }

   public ItemStack Vulcan_V(Object[] var1) {
      return this.Vulcan_W;
   }

   public ItemStack Vulcan_P(Object[] var1) {
      return this.Vulcan_V;
   }

   public ItemStack Vulcan_K(Object[] var1) {
      return this.Vulcan_n;
   }

   public ItemStack Vulcan_i(Object[] var1) {
      return this.Vulcan_OC;
   }

   public ItemStack Vulcan_U(Object[] var1) {
      return this.Vulcan_O1;
   }

   public long Vulcan_q(Object[] var1) {
      return this.Vulcan_OI;
   }

   public long Vulcan_g(Object[] var1) {
      return this.Vulcan_qW;
   }

   public Vulcan_c Vulcan_G(Object[] var1) {
      return this.Vulcan_E;
   }

   public void Vulcan_Z(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qn = var2;
   }

   public void Vulcan_Iz(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_j = var2;
   }

   public void Vulcan_IP(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_A = var2;
   }

   public void Vulcan_IG(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_OG = var2;
   }

   public void Vulcan_I8(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Oy = var2;
   }

   public void Vulcan_I1(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q = var2;
   }

   public void Vulcan_IZ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_t = var2;
   }

   public void Vulcan_J(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_R = var2;
   }

   public void Vulcan_IF(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_OS = var2;
   }

   public void Vulcan_y(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qS = var2;
   }

   public void Vulcan_q(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_S = var2;
   }

   public void Vulcan_If(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q5 = var2;
   }

   public void Vulcan_Ia(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_i = var2;
   }

   public void Vulcan_Ie(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q1 = var2;
   }

   public void Vulcan_G(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qQ = var2;
   }

   public void Vulcan_IB(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Oj = var2;
   }

   public void Vulcan_IW(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M = var2;
   }

   public void Vulcan_F(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q3 = var2;
   }

   public void Vulcan_w(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_v = var2;
   }

   public void Vulcan_II(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qF = var2;
   }

   public void Vulcan_Is(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qH = var2;
   }

   public void Vulcan_U(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qf = var2;
   }

   public void Vulcan_Ip(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Ou = var2;
   }

   public void Vulcan_IL(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_O6 = var2;
   }

   public void Vulcan_Iq(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_u = var2;
   }

   public void Vulcan_IU(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qX = var2;
   }

   public void Vulcan_Y(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_OE = var2;
   }

   public void Vulcan_Io(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_OT = var2;
   }

   public void Vulcan_Ic(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qK = var2;
   }

   public void Vulcan_I(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_k = var2;
   }

   public void Vulcan_Id(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qE = var2;
   }

   public void Vulcan_A(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_s = var2;
   }

   public void Vulcan_B(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_a = var2;
   }

   public void Vulcan_IX(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Os = var2;
   }

   public void Vulcan_l(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_q4 = var2;
   }

   public void Vulcan_d2(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_J = var2;
   }

   public void Vulcan_dP(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_O = var2;
   }

   public void Vulcan_d7(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_f = var2;
   }

   public void Vulcan_dm(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_O7 = var2;
   }

   public void Vulcan_df(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_qt = var2;
   }

   public void Vulcan_V(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_O_ = var2;
   }

   public void Vulcan_T(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_N = var2;
   }

   public void Vulcan_HJ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_x = var2;
   }

   public void Vulcan_O(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Og = var2;
   }

   public void Vulcan_c(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Om = var2;
   }

   public void Vulcan_k(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qZ = var2;
   }

   public void Vulcan_Hr(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q9 = var2;
   }

   public void Vulcan_HE(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qI = var2;
   }

   public void Vulcan_Hp(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_h = var2;
   }

   public void Vulcan_H6(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qJ = var2;
   }

   public void Vulcan_Ht(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_B = var2;
   }

   public void Vulcan_HA(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_C = var2;
   }

   public void Vulcan_HC(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Oz = var2;
   }

   public void Vulcan_H4(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OA = var2;
   }

   public void Vulcan_Hv(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Oh = var2;
   }

   public void Vulcan_HI(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qR = var2;
   }

   public void Vulcan_Hs(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Oc = var2;
   }

   public void Vulcan_HX(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qU = var2;
   }

   public void Vulcan_Hk(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qN = var2;
   }

   public void Vulcan_Hi(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qC = var2;
   }

   public void Vulcan_Hu(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_g = var2;
   }

   public void Vulcan_HD(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qA = var2;
   }

   public void Vulcan_h(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q7 = var2;
   }

   public void Vulcan_HO(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qu = var2;
   }

   public void Vulcan_vS(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_ql = var2;
   }

   public void Vulcan_Hx(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_U = var2;
   }

   public void Vulcan_HQ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_o = var2;
   }

   public void Vulcan_HY(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_F = var2;
   }

   public void Vulcan_Hq(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_e = var2;
   }

   public void Vulcan_vP(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qP = var2;
   }

   public void Vulcan_a(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qr = var2;
   }

   public void Vulcan_Hc(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_d = var2;
   }

   public void Vulcan_Hh(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_On = var2;
   }

   public void Vulcan_HZ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qp = var2;
   }

   public void Vulcan_HP(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_P = var2;
   }

   public void Vulcan_HG(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O0 = var2;
   }

   public void Vulcan_n(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Ok = var2;
   }

   public void Vulcan_S(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O2 = var2;
   }

   public void Vulcan_HW(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qa = var2;
   }

   public void Vulcan_Hn(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OK = var2;
   }

   public void Vulcan_Hm(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qY = var2;
   }

   public void Vulcan_X(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Q = var2;
   }

   public void Vulcan_H9(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O4 = var2;
   }

   public void Vulcan_HL(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OJ = var2;
   }

   public void Vulcan_Hf(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_b = var2;
   }

   public void Vulcan_HH(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O9 = var2;
   }

   public void Vulcan_HR(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qb = var2;
   }

   public void Vulcan_i(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q2 = var2;
   }

   public void Vulcan_HS(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan__ = var2;
   }

   public void Vulcan_HN(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_D = var2;
   }

   public void Vulcan_Hb(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qk = var2;
   }

   public void Vulcan_Hj(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qL = var2;
   }

   public void Vulcan_E(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qs = var2;
   }

   public void Vulcan_Hl(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qx = var2;
   }

   public void Vulcan_Hd(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qm = var2;
   }

   public void Vulcan_H7(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OB = var2;
   }

   public void Vulcan_HF(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q_ = var2;
   }

   public void Vulcan__(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OM = var2;
   }

   public void Vulcan_e(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qq = var2;
   }

   public void Vulcan_H1(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qz = var2;
   }

   public void Vulcan_H2(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = var2;
   }

   public void Vulcan_HM(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qc = var2;
   }

   public void Vulcan_Hy(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q0 = var2;
   }

   public void Vulcan_t(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OP = var2;
   }

   public void Vulcan_H5(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O8 = var2;
   }

   public void Vulcan_Ho(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qT = var2;
   }

   public void Vulcan_H3(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Oq = var2;
   }

   public void Vulcan_Hw(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_c = var2;
   }

   public void Vulcan_Ha(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_K = var2;
   }

   public void Vulcan_p(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qw = var2;
   }

   public void Vulcan_HV(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qh = var2;
   }

   public void Vulcan_H0(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O3 = var2;
   }

   public void Vulcan_v(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_G = var2;
   }

   public void Vulcan_HK(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qV = var2;
   }

   public void Vulcan_H(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q8 = var2;
   }

   public void Vulcan_R(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_H = var2;
   }

   public void Vulcan_K(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qM = var2;
   }

   public void Vulcan_H8(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Y = var2;
   }

   public void Vulcan_HT(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_OV = var2;
   }

   public void Vulcan_H_(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qj = var2;
   }

   public void Vulcan_HU(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qe = var2;
   }

   public void Vulcan_b(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_r = var2;
   }

   public void Vulcan_HB(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qO = var2;
   }

   public void Vulcan_Hg(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Oi = var2;
   }

   public void Vulcan_Hz(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_l = var2;
   }

   public void Vulcan_He(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_qo = var2;
   }

   public void Vulcan_Q(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_q6 = var2;
   }

   public void Vulcan_N(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_z = var2;
   }

   public void Vulcan_dz(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_qB = var2;
   }

   public void Vulcan_du(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_y = var2;
   }

   public void Vulcan_s(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_qg = var2;
   }

   public void Vulcan_d(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_OZ = var2;
   }

   public void Vulcan_de(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_w = var2;
   }

   public void Vulcan_x(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_ON = var2;
   }

   public void Vulcan_dO(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_dn(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_OX = var2;
   }

   public void Vulcan_dE(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan_dY(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_T = var2;
   }

   public void Vulcan_dC(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_p = var2;
   }

   public void Vulcan_dN(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_qD = var2;
   }

   public void Vulcan_d8(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_qy = var2;
   }

   public void Vulcan_sR(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_Z = var2;
   }

   public void Vulcan_sL(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_OU = var2;
   }

   public void Vulcan_s2(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_Of = var2;
   }

   public void Vulcan_D(Object[] var1) {
      GameMode var2 = (GameMode)var1[0];
      this.Vulcan_Ot = var2;
   }

   public void Vulcan_mQ(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_qi = var2;
   }

   public void Vulcan_mq(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_W = var2;
   }

   public void Vulcan_mM(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_V = var2;
   }

   public void Vulcan_mf(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_n = var2;
   }

   public void Vulcan_mZ(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_OC = var2;
   }

   public void Vulcan_mO(Object[] var1) {
      ItemStack var2 = (ItemStack)var1[0];
      this.Vulcan_O1 = var2;
   }

   public void Vulcan_sE(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_OI = var2;
   }

   public void Vulcan_sf(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_qW = var2;
   }

   public void Vulcan_IH(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_qG = var2;
   }

   private void lambda$handleMetaData$5(WrapperPlayServerEntityMetadata param1) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleGameStateChange$4(WrapperPlayServerChangeGameState param1) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleRemoveEntityEffect$3(WrapperPlayServerRemoveEntityEffect param1) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleEntityEffect$2(WrapperPlayServerEntityEffect param1) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$handleServerPosition$1(WrapperPlayServerPlayerPositionAndLook var1) {
      Vector var2 = new Vector(var1.getX(), var1.getY(), var1.getZ());
      this.Vulcan_qd.add(var2);
   }

   private void lambda$handleUpdateAttributes$0(WrapperPlayServerUpdateAttributes param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_g(boolean var0) {
      Vulcan_qv = var0;
   }

   public static boolean Vulcan_j() {
      return Vulcan_qv;
   }

   public static boolean Vulcan_C() {
      boolean var0 = Vulcan_j();

      try {
         return !var0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      long var0 = a ^ 113460543763164L;
      Vulcan_g(false);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[3];
      int var7 = 0;
      String var6 = "}\u0006-\u0018÷\u0092\u0018qA\u009c!^Bß\u0005'\u0010}\u0006-\u0018÷\u0092\u0018qA\u009c!^Bß\u0005'\bqJ\u001e9j\u009f!?";
      int var8 = "}\u0006-\u0018÷\u0092\u0018qA\u009c!^Bß\u0005'\u0010}\u0006-\u0018÷\u0092\u0018qA\u009c!^Bß\u0005'\bqJ\u001e9j\u009f!?".length();
      char var5 = 16;
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            b = var9;
            return;
         }

         var5 = var6.charAt(var4);
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
