package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Vulcan_iJ {
   private final Vulcan_iE Vulcan_MA;
   private boolean Vulcan_jv;
   private boolean Vulcan_Q0;
   private boolean Vulcan_QR;
   private boolean Vulcan_Mh;
   private boolean Vulcan_Md;
   private boolean Vulcan_Ma;
   private boolean Vulcan_Q6;
   private boolean Vulcan_jd;
   private boolean Vulcan_MP;
   private boolean Vulcan_TV;
   private boolean Vulcan_QH;
   private boolean Vulcan_MH;
   private boolean Vulcan_QT;
   private boolean Vulcan_T8;
   private boolean Vulcan_Mz;
   private boolean Vulcan_Ts;
   private boolean Vulcan_To;
   private boolean Vulcan_js;
   private boolean Vulcan_MZ;
   private boolean Vulcan_i;
   private boolean Vulcan_Qj;
   private boolean Vulcan_Qq;
   private boolean Vulcan_M2;
   private boolean Vulcan_v;
   private boolean Vulcan_Qv;
   private boolean Vulcan_Q8;
   private boolean Vulcan_je;
   private boolean Vulcan_k;
   private boolean Vulcan_Me;
   private boolean Vulcan_e;
   private boolean Vulcan_T;
   private boolean Vulcan_QU;
   private boolean Vulcan_u;
   private boolean Vulcan_O;
   private boolean Vulcan_M4;
   private boolean Vulcan_MI;
   private boolean Vulcan_QW;
   private boolean Vulcan_TM;
   private boolean Vulcan_Ta;
   private boolean Vulcan_Qu;
   private boolean Vulcan_MC;
   private boolean Vulcan_jp;
   private boolean Vulcan_Qh;
   private boolean Vulcan_P;
   private boolean Vulcan_Tk;
   private boolean Vulcan_QQ;
   private boolean Vulcan_Qt;
   private boolean Vulcan_z;
   private boolean Vulcan_jr;
   private boolean Vulcan_MW;
   private boolean Vulcan_t;
   private boolean Vulcan_TX;
   private boolean Vulcan_MB;
   private boolean Vulcan_M3;
   private boolean Vulcan_l;
   private boolean Vulcan_M7;
   private boolean Vulcan_MK;
   private boolean Vulcan_T0;
   private boolean Vulcan_Mm;
   private boolean Vulcan_Mg;
   private boolean Vulcan_Mc;
   private boolean Vulcan_N;
   private boolean Vulcan_j;
   private boolean Vulcan_MM;
   private boolean Vulcan_QS;
   private boolean Vulcan_M1;
   private boolean Vulcan_Q5;
   private boolean Vulcan_j1;
   private boolean Vulcan_jK;
   private boolean Vulcan_TO;
   private boolean Vulcan_MD;
   private boolean Vulcan_s;
   private boolean Vulcan_jO;
   private boolean Vulcan_Qg;
   private boolean Vulcan_T6;
   private boolean Vulcan_jk;
   private boolean Vulcan_w;
   private boolean Vulcan_QA;
   private boolean Vulcan_QB;
   private boolean Vulcan_g;
   private boolean Vulcan_Qm;
   private boolean Vulcan_MV;
   private boolean Vulcan_M9;
   private boolean Vulcan_TB;
   private boolean Vulcan_QP;
   private boolean Vulcan_Qo;
   private boolean Vulcan_R;
   private boolean Vulcan_MX;
   private boolean Vulcan_TQ;
   private boolean Vulcan_Mn;
   private boolean Vulcan_Q;
   private boolean Vulcan_QD;
   private boolean Vulcan_Qk;
   private boolean Vulcan_Qf;
   private boolean Vulcan_A;
   private boolean Vulcan_jN;
   private boolean Vulcan_o;
   private boolean Vulcan_Te;
   private boolean Vulcan_TH;
   private boolean Vulcan_Tr;
   private boolean Vulcan_j4;
   private boolean Vulcan_Q2;
   private boolean Vulcan_My;
   private boolean Vulcan_G;
   private boolean Vulcan_jM;
   private boolean Vulcan_Tv;
   private Location Vulcan_T9;
   private Location Vulcan_Tf;
   private final Map Vulcan_jG = new HashMap();
   public boolean Vulcan_T3;
   public List Vulcan_MU;
   private Material Vulcan_Tt;
   private Material Vulcan_Qp;
   private Material Vulcan_Qe;
   private List Vulcan_MJ;
   private List Vulcan_jm;
   private List Vulcan_Mx;
   private List Vulcan_T4;
   private List Vulcan_Ti;
   public List Vulcan_MR;
   public List Vulcan_TZ;
   public List Vulcan_H;
   public Material Vulcan_Tz;
   public Material Vulcan_Qi;
   public Material Vulcan_jc;
   private double Vulcan_q;
   private double Vulcan_j2;
   private double Vulcan_d;
   private double Vulcan_Tl;
   private double Vulcan_TL;
   private double Vulcan_x;
   private double Vulcan_jF;
   private double Vulcan_Mk;
   private double Vulcan_Q4;
   private double Vulcan_W;
   private double Vulcan_M0;
   private double Vulcan_QF;
   private double Vulcan_Td;
   private double Vulcan_Qw;
   private double Vulcan_c;
   private double Vulcan_y;
   private double Vulcan_TT;
   private double Vulcan_Tp;
   private double Vulcan_TW;
   private double Vulcan_Ty;
   private double Vulcan_M8;
   private double Vulcan_Y;
   private double Vulcan_MG;
   private double Vulcan_QN;
   private double Vulcan_Tc;
   private double Vulcan_jj;
   private double Vulcan_TN;
   private double Vulcan_I;
   private double Vulcan_K;
   private double Vulcan_Mb;
   private double Vulcan_T_;
   private double Vulcan_jB;
   private double Vulcan_Mq;
   private double Vulcan_jq;
   private double Vulcan_QJ;
   private double Vulcan_TK;
   private double Vulcan_TE;
   private double Vulcan_TG;
   private double Vulcan_ME;
   private double Vulcan_Q9;
   private double Vulcan_Ms;
   private double Vulcan_jI;
   private double Vulcan_Q_;
   private double Vulcan_QV;
   private double Vulcan_QY;
   private double Vulcan_Qc;
   private double Vulcan_M5;
   private int Vulcan_TP = 100;
   private int Vulcan_Ql = 100;
   private int Vulcan_Mv = 100;
   private int Vulcan_f;
   private int Vulcan_Mt;
   private int Vulcan_T5 = 1000;
   private int Vulcan_QZ = 100;
   private int Vulcan_Z = 200;
   private int Vulcan_S = 100;
   private int Vulcan_E;
   private int Vulcan_TS = 100;
   private int Vulcan_MO = 100;
   private int Vulcan_V = 100;
   private int Vulcan_TR;
   private int Vulcan_TA = 100;
   private int Vulcan_Tn;
   private int Vulcan_QI;
   private int Vulcan_QC = 100;
   private int Vulcan_Q7 = 100;
   private int Vulcan_ML;
   private int Vulcan_Mp = 100;
   private int Vulcan_jn = 100;
   private int Vulcan_jE = 100;
   private int Vulcan_r = 100;
   private int Vulcan_M6 = 100;
   private int Vulcan_jw = 100;
   private int Vulcan_X = 100;
   private int Vulcan_T7;
   private int Vulcan_Tb;
   private int Vulcan_Qn = 200;
   private int Vulcan_Qy = 100;
   private int Vulcan_jC = 100;
   private int Vulcan_F;
   private int Vulcan_n = 100;
   private int Vulcan_jY;
   private int Vulcan_T2 = 500;
   private int Vulcan_jl = 500;
   private int Vulcan_J = 500;
   private int Vulcan_QO = 150;
   private int Vulcan_jZ = 150;
   private int Vulcan_Tu = 150;
   private int Vulcan_a;
   private int Vulcan_Q1;
   private int Vulcan_jt;
   private int Vulcan_Qs;
   private int Vulcan_U = 100;
   private int Vulcan_Ml = 500;
   private int Vulcan__;
   private int Vulcan_TF;
   private int Vulcan_MQ = 100;
   private int Vulcan_Qx = 100;
   private int Vulcan_C;
   private int Vulcan_Qa = 100;
   private int Vulcan_Qd;
   private int Vulcan_Mu;
   private int Vulcan_Tq = 100;
   private int Vulcan_Mr = 100;
   private int Vulcan_TJ = 1000;
   private int Vulcan_Qb = 100;
   private int Vulcan_TY = 200;
   private int Vulcan_Tx = 100;
   private int Vulcan_TI = 1000;
   private int Vulcan_MN = 1000;
   private int Vulcan_Tm = 1000;
   private int Vulcan_Tg = 1000;
   private int Vulcan_Th = 0;
   private int Vulcan_T1 = 1000;
   private int Vulcan_QE = 100;
   private int Vulcan_Tw = 1000;
   private int Vulcan_jD = 100;
   private int Vulcan_b = 100;
   private int Vulcan_MS = 100;
   private int Vulcan_M_ = 100;
   private int Vulcan_jS = 100;
   private int Vulcan_QG = 100;
   private int Vulcan_Mo = 1000;
   private int Vulcan_MT = 100;
   private int Vulcan_QX = 0;
   private World Vulcan_Mw;
   private Entity Vulcan_Qz;
   private Entity Vulcan_MY;
   private float Vulcan_TC = 0.2F;
   private float Vulcan_QK = 0.2F;
   private List Vulcan_p = new ArrayList();
   private LinkedList Vulcan_TU = new LinkedList();
   private boolean Vulcan_D;
   private boolean Vulcan_Q3;
   private boolean Vulcan_Mj;
   private boolean Vulcan_m;
   private boolean Vulcan_TD;
   private boolean Vulcan_jA;
   private boolean Vulcan_Qr;
   private boolean Vulcan_h;
   private boolean Vulcan_L;
   private boolean Vulcan_Mf;
   private boolean Vulcan_QL;
   private boolean Vulcan_Mi;
   private boolean Vulcan_QM;
   private boolean Vulcan_M;
   private boolean Vulcan_MF;
   private boolean Vulcan_Tj;
   private boolean Vulcan_j_;
   private static String[] Vulcan_B;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-375040486825195942L, -4809280450823978032L, MethodHandles.lookup().lookupClass()).a(82951496925928L);
   private static final String[] b;

   public Vulcan_iJ(Vulcan_iE var1) {
      this.Vulcan_MA = var1;
   }

   public void Vulcan_u4(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_e1 Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_p_(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 85797027015731L;
      Vulcan_iB var6 = this.Vulcan_MA.Vulcan_L(new Object[0]);
      Vulcan_in var10003 = Vulcan_in.FLIGHT;
      this.Vulcan_D = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.CREATIVE;
      this.Vulcan_Q3 = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.JOINED;
      this.Vulcan_Mj = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.LIQUID;
      this.Vulcan_m = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.LEVITATION;
      this.Vulcan_TD = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.SLOW_FALLING;
      this.Vulcan_jA = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.RIPTIDE;
      this.Vulcan_Qr = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.VEHICLE;
      this.Vulcan_h = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.LENIENT_SCAFFOLDING;
      this.Vulcan_L = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.GLIDING;
      this.Vulcan_Mf = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.ELYTRA;
      this.Vulcan_QL = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.TELEPORT;
      this.Vulcan_Mi = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.ENDER_PEARL;
      this.Vulcan_QM = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.CHUNK;
      this.Vulcan_M = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.COMBO_MODE;
      this.Vulcan_MF = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.MYTHIC_MOB;
      this.Vulcan_Tj = var6.Vulcan_c(new Object[]{var4, var10003});
      var10003 = Vulcan_in.CLIMBABLE;
      this.Vulcan_j_ = var6.Vulcan_c(new Object[]{var4, var10003});
   }

   private void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_pH(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_pQ(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_pS(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_ms(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_pn(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_pY(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_L6(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_l0(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_D(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_w(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_g(Object[] var1) {
      return Math.abs(this.Vulcan_W - this.Vulcan_c);
   }

   public void Vulcan_pe(Object[] var1) {
      this.Vulcan_T7 = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
      this.Vulcan_TJ = 0;
   }

   public void Vulcan_pi(Object[] var1) {
      this.Vulcan_MN = 0;
      this.Vulcan_F = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   private void Vulcan_pG(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_I9(Object[] var1) {
      WrapperPlayServerPlayerAbilities var4 = (WrapperPlayServerPlayerAbilities)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 79570644366226L;
      this.Vulcan_MA.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleAbilities$0, var5});
   }

   public boolean Vulcan_lF(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_pP(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_p2(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_qS(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      this.Vulcan_p.add(var2);
   }

   public void Vulcan_px(Object[] var1) {
      this.Vulcan_p.forEach(Runnable::run);
      this.Vulcan_p.clear();
   }

   public Vulcan_iE Vulcan_t(Object[] var1) {
      return this.Vulcan_MA;
   }

   public boolean Vulcan_lp(Object[] var1) {
      return this.Vulcan_jv;
   }

   public boolean Vulcan_ln(Object[] var1) {
      return this.Vulcan_Q0;
   }

   public boolean Vulcan_lf(Object[] var1) {
      return this.Vulcan_QR;
   }

   public boolean Vulcan_h(Object[] var1) {
      return this.Vulcan_Mh;
   }

   public boolean Vulcan_r(Object[] var1) {
      return this.Vulcan_Md;
   }

   public boolean Vulcan_lU(Object[] var1) {
      return this.Vulcan_Ma;
   }

   public boolean Vulcan_lL(Object[] var1) {
      return this.Vulcan_Q6;
   }

   public boolean Vulcan_Z(Object[] var1) {
      return this.Vulcan_jd;
   }

   public boolean Vulcan_lo(Object[] var1) {
      return this.Vulcan_MP;
   }

   public boolean Vulcan_l5(Object[] var1) {
      return this.Vulcan_TV;
   }

   public boolean Vulcan__(Object[] var1) {
      return this.Vulcan_QH;
   }

   public boolean Vulcan_J(Object[] var1) {
      return this.Vulcan_MH;
   }

   public boolean Vulcan_e5(Object[] var1) {
      return this.Vulcan_QT;
   }

   public boolean Vulcan_l_(Object[] var1) {
      return this.Vulcan_T8;
   }

   public boolean Vulcan_lz(Object[] var1) {
      return this.Vulcan_Mz;
   }

   public boolean Vulcan_l2(Object[] var1) {
      return this.Vulcan_Ts;
   }

   public boolean Vulcan_eE(Object[] var1) {
      return this.Vulcan_To;
   }

   public boolean Vulcan_lM(Object[] var1) {
      return this.Vulcan_js;
   }

   public boolean Vulcan_e6(Object[] var1) {
      return this.Vulcan_MZ;
   }

   public boolean Vulcan_i(Object[] var1) {
      return this.Vulcan_i;
   }

   public boolean Vulcan_S(Object[] var1) {
      return this.Vulcan_Qj;
   }

   public boolean Vulcan_li(Object[] var1) {
      return this.Vulcan_Qq;
   }

   public boolean Vulcan_lu(Object[] var1) {
      return this.Vulcan_M2;
   }

   public boolean Vulcan_lm(Object[] var1) {
      return this.Vulcan_v;
   }

   public boolean Vulcan_N(Object[] var1) {
      return this.Vulcan_Qv;
   }

   public boolean Vulcan_ey(Object[] var1) {
      return this.Vulcan_Q8;
   }

   public boolean Vulcan_lh(Object[] var1) {
      return this.Vulcan_je;
   }

   public boolean Vulcan_lJ(Object[] var1) {
      return this.Vulcan_k;
   }

   public boolean Vulcan_lI(Object[] var1) {
      return this.Vulcan_Me;
   }

   public boolean Vulcan_U(Object[] var1) {
      return this.Vulcan_e;
   }

   public boolean Vulcan_lx(Object[] var1) {
      return this.Vulcan_T;
   }

   public boolean Vulcan_x(Object[] var1) {
      return this.Vulcan_QU;
   }

   public boolean Vulcan_l7(Object[] var1) {
      return this.Vulcan_u;
   }

   public boolean Vulcan_H(Object[] var1) {
      return this.Vulcan_O;
   }

   public boolean Vulcan_lH(Object[] var1) {
      return this.Vulcan_M4;
   }

   public boolean Vulcan_k(Object[] var1) {
      return this.Vulcan_MI;
   }

   public boolean Vulcan_lS(Object[] var1) {
      return this.Vulcan_QW;
   }

   public boolean Vulcan_lE(Object[] var1) {
      return this.Vulcan_TM;
   }

   public boolean Vulcan_n(Object[] var1) {
      return this.Vulcan_Ta;
   }

   public boolean Vulcan_d(Object[] var1) {
      return this.Vulcan_Qu;
   }

   public boolean Vulcan_lY(Object[] var1) {
      return this.Vulcan_MC;
   }

   public boolean Vulcan_l6(Object[] var1) {
      return this.Vulcan_jp;
   }

   public boolean Vulcan_E(Object[] var1) {
      return this.Vulcan_Qh;
   }

   public boolean Vulcan_eC(Object[] var1) {
      return this.Vulcan_P;
   }

   public boolean Vulcan_e(Object[] var1) {
      return this.Vulcan_Tk;
   }

   public boolean Vulcan_p(Object[] var1) {
      return this.Vulcan_QQ;
   }

   public boolean Vulcan_lk(Object[] var1) {
      return this.Vulcan_Qt;
   }

   public boolean Vulcan_lA(Object[] var1) {
      return this.Vulcan_z;
   }

   public boolean Vulcan_l(Object[] var1) {
      return this.Vulcan_jr;
   }

   public boolean Vulcan_lR(Object[] var1) {
      return this.Vulcan_MW;
   }

   public boolean Vulcan_lc(Object[] var1) {
      return this.Vulcan_t;
   }

   public boolean Vulcan_j(Object[] var1) {
      return this.Vulcan_TX;
   }

   public boolean Vulcan_l8(Object[] var1) {
      return this.Vulcan_MB;
   }

   public boolean Vulcan_ej(Object[] var1) {
      return this.Vulcan_M3;
   }

   public boolean Vulcan_lt(Object[] var1) {
      return this.Vulcan_l;
   }

   public boolean Vulcan_K(Object[] var1) {
      return this.Vulcan_M7;
   }

   public boolean Vulcan_eO(Object[] var1) {
      return this.Vulcan_MK;
   }

   public boolean Vulcan_l3(Object[] var1) {
      return this.Vulcan_T0;
   }

   public boolean Vulcan_a(Object[] var1) {
      return this.Vulcan_Mm;
   }

   public boolean Vulcan_C(Object[] var1) {
      return this.Vulcan_Mg;
   }

   public boolean Vulcan_lK(Object[] var1) {
      return this.Vulcan_Mc;
   }

   public boolean Vulcan_lQ(Object[] var1) {
      return this.Vulcan_N;
   }

   public boolean Vulcan_el(Object[] var1) {
      return this.Vulcan_j;
   }

   public boolean Vulcan_ll(Object[] var1) {
      return this.Vulcan_MM;
   }

   public boolean Vulcan_I(Object[] var1) {
      return this.Vulcan_QS;
   }

   public boolean Vulcan_W(Object[] var1) {
      return this.Vulcan_M1;
   }

   public boolean Vulcan_A(Object[] var1) {
      return this.Vulcan_Q5;
   }

   public boolean Vulcan_m(Object[] var1) {
      return this.Vulcan_j1;
   }

   public boolean Vulcan_Q(Object[] var1) {
      return this.Vulcan_jK;
   }

   public boolean Vulcan_ex(Object[] var1) {
      return this.Vulcan_TO;
   }

   public boolean Vulcan_c(Object[] var1) {
      return this.Vulcan_MD;
   }

   public boolean Vulcan_O(Object[] var1) {
      return this.Vulcan_s;
   }

   public boolean Vulcan_e2(Object[] var1) {
      return this.Vulcan_jO;
   }

   public boolean Vulcan_lG(Object[] var1) {
      return this.Vulcan_Qg;
   }

   public boolean Vulcan_lB(Object[] var1) {
      return this.Vulcan_T6;
   }

   public boolean Vulcan_T(Object[] var1) {
      return this.Vulcan_jk;
   }

   public boolean Vulcan_X(Object[] var1) {
      return this.Vulcan_w;
   }

   public boolean Vulcan_g(Object[] var1) {
      return this.Vulcan_QA;
   }

   public boolean Vulcan_lD(Object[] var1) {
      return this.Vulcan_QB;
   }

   public boolean Vulcan_lX(Object[] var1) {
      return this.Vulcan_g;
   }

   public boolean Vulcan_e_(Object[] var1) {
      return this.Vulcan_Qm;
   }

   public boolean Vulcan_Y(Object[] var1) {
      return this.Vulcan_MV;
   }

   public boolean Vulcan_R(Object[] var1) {
      return this.Vulcan_M9;
   }

   public boolean Vulcan_G(Object[] var1) {
      return this.Vulcan_TB;
   }

   public boolean Vulcan_P(Object[] var1) {
      return this.Vulcan_QP;
   }

   public boolean Vulcan_lT(Object[] var1) {
      return this.Vulcan_Qo;
   }

   public boolean Vulcan_ld(Object[] var1) {
      return this.Vulcan_R;
   }

   public boolean Vulcan_s(Object[] var1) {
      return this.Vulcan_MX;
   }

   public boolean Vulcan_l1(Object[] var1) {
      return this.Vulcan_TQ;
   }

   public boolean Vulcan_v(Object[] var1) {
      return this.Vulcan_Mn;
   }

   public boolean Vulcan_la(Object[] var1) {
      return this.Vulcan_Q;
   }

   public boolean Vulcan_lg(Object[] var1) {
      return this.Vulcan_QD;
   }

   public boolean Vulcan_z(Object[] var1) {
      return this.Vulcan_Qk;
   }

   public boolean Vulcan_ly(Object[] var1) {
      return this.Vulcan_Qf;
   }

   public boolean Vulcan_f(Object[] var1) {
      return this.Vulcan_A;
   }

   public boolean Vulcan_lO(Object[] var1) {
      return this.Vulcan_jN;
   }

   public boolean Vulcan_lw(Object[] var1) {
      return this.Vulcan_o;
   }

   public boolean Vulcan_le(Object[] var1) {
      return this.Vulcan_Te;
   }

   public boolean Vulcan_o(Object[] var1) {
      return this.Vulcan_TH;
   }

   public boolean Vulcan_lq(Object[] var1) {
      return this.Vulcan_Tr;
   }

   public boolean Vulcan_lP(Object[] var1) {
      return this.Vulcan_j4;
   }

   public boolean Vulcan_M(Object[] var1) {
      return this.Vulcan_Q2;
   }

   public boolean Vulcan_lv(Object[] var1) {
      return this.Vulcan_My;
   }

   public boolean Vulcan_l4(Object[] var1) {
      return this.Vulcan_G;
   }

   public boolean Vulcan_lC(Object[] var1) {
      return this.Vulcan_jM;
   }

   public boolean Vulcan_lj(Object[] var1) {
      return this.Vulcan_Tv;
   }

   public Location Vulcan_u(Object[] var1) {
      return this.Vulcan_T9;
   }

   public Location Vulcan_E(Object[] var1) {
      return this.Vulcan_Tf;
   }

   public Map Vulcan_u(Object[] var1) {
      return this.Vulcan_jG;
   }

   public boolean Vulcan_lV(Object[] var1) {
      return this.Vulcan_T3;
   }

   public List Vulcan_V(Object[] var1) {
      return this.Vulcan_MU;
   }

   public Material Vulcan_h(Object[] var1) {
      return this.Vulcan_Tt;
   }

   public Material Vulcan_X(Object[] var1) {
      return this.Vulcan_Qp;
   }

   public Material Vulcan_K(Object[] var1) {
      return this.Vulcan_Qe;
   }

   public List Vulcan_Q(Object[] var1) {
      return this.Vulcan_MJ;
   }

   public List Vulcan_t(Object[] var1) {
      return this.Vulcan_jm;
   }

   public List Vulcan_j(Object[] var1) {
      return this.Vulcan_Mx;
   }

   public List Vulcan_A(Object[] var1) {
      return this.Vulcan_T4;
   }

   public List Vulcan_C(Object[] var1) {
      return this.Vulcan_Ti;
   }

   public List Vulcan_n(Object[] var1) {
      return this.Vulcan_MR;
   }

   public List Vulcan_E(Object[] var1) {
      return this.Vulcan_TZ;
   }

   public List Vulcan_i(Object[] var1) {
      return this.Vulcan_H;
   }

   public Material Vulcan_B(Object[] var1) {
      return this.Vulcan_Tz;
   }

   public Material Vulcan_x(Object[] var1) {
      return this.Vulcan_Qi;
   }

   public Material Vulcan_i(Object[] var1) {
      return this.Vulcan_jc;
   }

   public double Vulcan_O(Object[] var1) {
      return this.Vulcan_q;
   }

   public double Vulcan_D(Object[] var1) {
      return this.Vulcan_j2;
   }

   public double Vulcan_p(Object[] var1) {
      return this.Vulcan_d;
   }

   public double Vulcan_I(Object[] var1) {
      return this.Vulcan_Tl;
   }

   public double Vulcan_j(Object[] var1) {
      return this.Vulcan_TL;
   }

   public double Vulcan_X(Object[] var1) {
      return this.Vulcan_x;
   }

   public double Vulcan_B(Object[] var1) {
      return this.Vulcan_jF;
   }

   public double Vulcan_w(Object[] var1) {
      return this.Vulcan_Mk;
   }

   public double Vulcan_A(Object[] var1) {
      return this.Vulcan_Q4;
   }

   public double Vulcan_r(Object[] var1) {
      return this.Vulcan_W;
   }

   public double Vulcan_J(Object[] var1) {
      return this.Vulcan_M0;
   }

   public double Vulcan_x(Object[] var1) {
      return this.Vulcan_QF;
   }

   public double Vulcan_L(Object[] var1) {
      return this.Vulcan_Td;
   }

   public double Vulcan_G(Object[] var1) {
      return this.Vulcan_Qw;
   }

   public double Vulcan_Y(Object[] var1) {
      return this.Vulcan_c;
   }

   public double Vulcan_m(Object[] var1) {
      return this.Vulcan_y;
   }

   public double Vulcan_v(Object[] var1) {
      return this.Vulcan_TT;
   }

   public double Vulcan_n(Object[] var1) {
      return this.Vulcan_Tp;
   }

   public double Vulcan_c(Object[] var1) {
      return this.Vulcan_TW;
   }

   public double Vulcan__(Object[] var1) {
      return this.Vulcan_Ty;
   }

   public double Vulcan_o(Object[] var1) {
      return this.Vulcan_M8;
   }

   public double Vulcan_P(Object[] var1) {
      return this.Vulcan_Y;
   }

   public double Vulcan_h(Object[] var1) {
      return this.Vulcan_MG;
   }

   public double Vulcan_E(Object[] var1) {
      return this.Vulcan_QN;
   }

   public double Vulcan_l(Object[] var1) {
      return this.Vulcan_Tc;
   }

   public double Vulcan_Z(Object[] var1) {
      return this.Vulcan_jj;
   }

   public double Vulcan_f(Object[] var1) {
      return this.Vulcan_TN;
   }

   public double Vulcan_R(Object[] var1) {
      return this.Vulcan_I;
   }

   public double Vulcan_b(Object[] var1) {
      return this.Vulcan_K;
   }

   public double Vulcan_K(Object[] var1) {
      return this.Vulcan_Mb;
   }

   public double Vulcan_e(Object[] var1) {
      return this.Vulcan_T_;
   }

   public double Vulcan_a(Object[] var1) {
      return this.Vulcan_jB;
   }

   public double Vulcan_u(Object[] var1) {
      return this.Vulcan_Mq;
   }

   public double Vulcan_M(Object[] var1) {
      return this.Vulcan_jq;
   }

   public double Vulcan_d(Object[] var1) {
      return this.Vulcan_QJ;
   }

   public double Vulcan_T(Object[] var1) {
      return this.Vulcan_TK;
   }

   public double Vulcan_C(Object[] var1) {
      return this.Vulcan_TE;
   }

   public double Vulcan_W(Object[] var1) {
      return this.Vulcan_TG;
   }

   public double Vulcan_k(Object[] var1) {
      return this.Vulcan_ME;
   }

   public double Vulcan_N(Object[] var1) {
      return this.Vulcan_Q9;
   }

   public double Vulcan_i(Object[] var1) {
      return this.Vulcan_Ms;
   }

   public double Vulcan_U(Object[] var1) {
      return this.Vulcan_jI;
   }

   public double Vulcan_S(Object[] var1) {
      return this.Vulcan_Q_;
   }

   public double Vulcan_V(Object[] var1) {
      return this.Vulcan_QV;
   }

   public double Vulcan_t(Object[] var1) {
      return this.Vulcan_QY;
   }

   public double Vulcan_z(Object[] var1) {
      return this.Vulcan_Qc;
   }

   public double Vulcan_Q(Object[] var1) {
      return this.Vulcan_M5;
   }

   public int Vulcan_b(Object[] var1) {
      return this.Vulcan_TP;
   }

   public int Vulcan_e(Object[] var1) {
      return this.Vulcan_Ql;
   }

   public int Vulcan_m(Object[] var1) {
      return this.Vulcan_Mv;
   }

   public int Vulcan_gf(Object[] var1) {
      return this.Vulcan_f;
   }

   public int Vulcan_H(Object[] var1) {
      return this.Vulcan_Mt;
   }

   public int Vulcan_n(Object[] var1) {
      return this.Vulcan_T5;
   }

   public int Vulcan_g(Object[] var1) {
      return this.Vulcan_QZ;
   }

   public int Vulcan_W(Object[] var1) {
      return this.Vulcan_Z;
   }

   public int Vulcan_i(Object[] var1) {
      return this.Vulcan_S;
   }

   public int Vulcan_U(Object[] var1) {
      return this.Vulcan_E;
   }

   public int Vulcan_T(Object[] var1) {
      return this.Vulcan_TS;
   }

   public int Vulcan_k(Object[] var1) {
      return this.Vulcan_MO;
   }

   public int Vulcan_g2(Object[] var1) {
      return this.Vulcan_V;
   }

   public int Vulcan_d(Object[] var1) {
      return this.Vulcan_TR;
   }

   public int Vulcan_R(Object[] var1) {
      return this.Vulcan_TA;
   }

   public int Vulcan_gj(Object[] var1) {
      return this.Vulcan_Tn;
   }

   public int Vulcan_o(Object[] var1) {
      return this.Vulcan_QI;
   }

   public int Vulcan_gI(Object[] var1) {
      return this.Vulcan_QC;
   }

   public int Vulcan_gy(Object[] var1) {
      return this.Vulcan_Q7;
   }

   public int Vulcan_u(Object[] var1) {
      return this.Vulcan_ML;
   }

   public int Vulcan_Q(Object[] var1) {
      return this.Vulcan_Mp;
   }

   public int Vulcan_C(Object[] var1) {
      return this.Vulcan_jn;
   }

   public int Vulcan_j(Object[] var1) {
      return this.Vulcan_jE;
   }

   public int Vulcan_t(Object[] var1) {
      return this.Vulcan_r;
   }

   public int Vulcan_p(Object[] var1) {
      return this.Vulcan_M6;
   }

   public int Vulcan_h(Object[] var1) {
      return this.Vulcan_jw;
   }

   public int Vulcan_Z(Object[] var1) {
      return this.Vulcan_X;
   }

   public int Vulcan_s(Object[] var1) {
      return this.Vulcan_T7;
   }

   public int Vulcan_gE(Object[] var1) {
      return this.Vulcan_Tb;
   }

   public int Vulcan_y(Object[] var1) {
      return this.Vulcan_Qn;
   }

   public int Vulcan_P(Object[] var1) {
      return this.Vulcan_Qy;
   }

   public int Vulcan_gJ(Object[] var1) {
      return this.Vulcan_jC;
   }

   public int Vulcan_J(Object[] var1) {
      return this.Vulcan_F;
   }

   public int Vulcan_O(Object[] var1) {
      return this.Vulcan_n;
   }

   public int Vulcan_K(Object[] var1) {
      return this.Vulcan_jY;
   }

   public int Vulcan_w(Object[] var1) {
      return this.Vulcan_T2;
   }

   public int Vulcan_g6(Object[] var1) {
      return this.Vulcan_jl;
   }

   public int Vulcan_v(Object[] var1) {
      return this.Vulcan_J;
   }

   public int Vulcan_gk(Object[] var1) {
      return this.Vulcan_QO;
   }

   public int Vulcan_L(Object[] var1) {
      return this.Vulcan_jZ;
   }

   public int Vulcan_g_(Object[] var1) {
      return this.Vulcan_Tu;
   }

   public int Vulcan_c(Object[] var1) {
      return this.Vulcan_a;
   }

   public int Vulcan_B(Object[] var1) {
      return this.Vulcan_Q1;
   }

   public int Vulcan_q(Object[] var1) {
      return this.Vulcan_jt;
   }

   public int Vulcan_x(Object[] var1) {
      return this.Vulcan_Qs;
   }

   public int Vulcan_gc(Object[] var1) {
      return this.Vulcan_U;
   }

   public int Vulcan_g1(Object[] var1) {
      return this.Vulcan_Ml;
   }

   public int Vulcan_ga(Object[] var1) {
      return this.Vulcan__;
   }

   public int Vulcan_gx(Object[] var1) {
      return this.Vulcan_TF;
   }

   public int Vulcan_r(Object[] var1) {
      return this.Vulcan_MQ;
   }

   public int Vulcan_M(Object[] var1) {
      return this.Vulcan_Qx;
   }

   public int Vulcan_z(Object[] var1) {
      return this.Vulcan_C;
   }

   public int Vulcan_X(Object[] var1) {
      return this.Vulcan_Qa;
   }

   public int Vulcan_Y(Object[] var1) {
      return this.Vulcan_Qd;
   }

   public int Vulcan_gB(Object[] var1) {
      return this.Vulcan_Mu;
   }

   public int Vulcan_V(Object[] var1) {
      return this.Vulcan_Tq;
   }

   public int Vulcan_a(Object[] var1) {
      return this.Vulcan_Mr;
   }

   public int Vulcan_gC(Object[] var1) {
      return this.Vulcan_TJ;
   }

   public int Vulcan_N(Object[] var1) {
      return this.Vulcan_Qb;
   }

   public int Vulcan_I(Object[] var1) {
      return this.Vulcan_TY;
   }

   public int Vulcan_F(Object[] var1) {
      return this.Vulcan_Tx;
   }

   public int Vulcan_A(Object[] var1) {
      return this.Vulcan_TI;
   }

   public int Vulcan_l(Object[] var1) {
      return this.Vulcan_MN;
   }

   public int Vulcan__(Object[] var1) {
      return this.Vulcan_Tm;
   }

   public int Vulcan_G(Object[] var1) {
      return this.Vulcan_Tg;
   }

   public int Vulcan_g5(Object[] var1) {
      return this.Vulcan_Th;
   }

   public int Vulcan_gb(Object[] var1) {
      return this.Vulcan_T1;
   }

   public int Vulcan_S(Object[] var1) {
      return this.Vulcan_QE;
   }

   public int Vulcan_gR(Object[] var1) {
      return this.Vulcan_Tw;
   }

   public int Vulcan_gd(Object[] var1) {
      return this.Vulcan_jD;
   }

   public int Vulcan_gY(Object[] var1) {
      return this.Vulcan_b;
   }

   public int Vulcan_E(Object[] var1) {
      return this.Vulcan_MS;
   }

   public int Vulcan_go(Object[] var1) {
      return this.Vulcan_M_;
   }

   public int Vulcan_D(Object[] var1) {
      return this.Vulcan_jS;
   }

   public int Vulcan_gW(Object[] var1) {
      return this.Vulcan_QG;
   }

   public int Vulcan_g8(Object[] var1) {
      return this.Vulcan_Mo;
   }

   public int Vulcan_gO(Object[] var1) {
      return this.Vulcan_MT;
   }

   public int Vulcan_f(Object[] var1) {
      return this.Vulcan_QX;
   }

   public World Vulcan_C(Object[] var1) {
      return this.Vulcan_Mw;
   }

   public Entity Vulcan_D(Object[] var1) {
      return this.Vulcan_Qz;
   }

   public Entity Vulcan_z(Object[] var1) {
      return this.Vulcan_MY;
   }

   public float Vulcan_M(Object[] var1) {
      return this.Vulcan_TC;
   }

   public float Vulcan_F(Object[] var1) {
      return this.Vulcan_QK;
   }

   public List Vulcan_d(Object[] var1) {
      return this.Vulcan_p;
   }

   public LinkedList Vulcan_g(Object[] var1) {
      return this.Vulcan_TU;
   }

   public boolean Vulcan_lN(Object[] var1) {
      return this.Vulcan_D;
   }

   public boolean Vulcan_lb(Object[] var1) {
      return this.Vulcan_Q3;
   }

   public boolean Vulcan_en(Object[] var1) {
      return this.Vulcan_Mj;
   }

   public boolean Vulcan_l9(Object[] var1) {
      return this.Vulcan_m;
   }

   public boolean Vulcan_lZ(Object[] var1) {
      return this.Vulcan_TD;
   }

   public boolean Vulcan_q(Object[] var1) {
      return this.Vulcan_jA;
   }

   public boolean Vulcan_y(Object[] var1) {
      return this.Vulcan_Qr;
   }

   public boolean Vulcan_V(Object[] var1) {
      return this.Vulcan_h;
   }

   public boolean Vulcan_e8(Object[] var1) {
      return this.Vulcan_L;
   }

   public boolean Vulcan_L(Object[] var1) {
      return this.Vulcan_Mf;
   }

   public boolean Vulcan_b(Object[] var1) {
      return this.Vulcan_QL;
   }

   public boolean Vulcan_B(Object[] var1) {
      return this.Vulcan_Mi;
   }

   public boolean Vulcan_ls(Object[] var1) {
      return this.Vulcan_QM;
   }

   public boolean Vulcan_F(Object[] var1) {
      return this.Vulcan_M;
   }

   public boolean Vulcan_u(Object[] var1) {
      return this.Vulcan_MF;
   }

   public boolean Vulcan_lr(Object[] var1) {
      return this.Vulcan_Tj;
   }

   public boolean Vulcan_lW(Object[] var1) {
      return this.Vulcan_j_;
   }

   public void Vulcan_pw(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jv = var2;
   }

   public void Vulcan_VK(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q0 = var2;
   }

   public void Vulcan_Vc(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QR = var2;
   }

   public void Vulcan_X(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mh = var2;
   }

   public void Vulcan_pA(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Md = var2;
   }

   public void Vulcan_VT(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Ma = var2;
   }

   public void Vulcan_pF(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q6 = var2;
   }

   public void Vulcan_Vv(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jd = var2;
   }

   public void Vulcan_s(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MP = var2;
   }

   public void Vulcan_py(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TV = var2;
   }

   public void Vulcan_Vw(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QH = var2;
   }

   public void Vulcan_VW(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MH = var2;
   }

   public void Vulcan_VG(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QT = var2;
   }

   public void Vulcan_N(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T8 = var2;
   }

   public void Vulcan_Vf(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mz = var2;
   }

   public void Vulcan_Vo(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Ts = var2;
   }

   public void Vulcan_pT(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_To = var2;
   }

   public void Vulcan_pf(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_js = var2;
   }

   public void Vulcan_V_(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MZ = var2;
   }

   public void Vulcan_po(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_i = var2;
   }

   public void Vulcan_V1(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qj = var2;
   }

   public void Vulcan_pN(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qq = var2;
   }

   public void Vulcan_D(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M2 = var2;
   }

   public void Vulcan_pj(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_v = var2;
   }

   public void Vulcan_pW(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qv = var2;
   }

   public void Vulcan_i(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q8 = var2;
   }

   public void Vulcan_p8(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_je = var2;
   }

   public void Vulcan_y(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_k = var2;
   }

   public void Vulcan_Ve(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Me = var2;
   }

   public void Vulcan_p7(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_e = var2;
   }

   public void Vulcan_pb(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T = var2;
   }

   public void Vulcan_pJ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QU = var2;
   }

   public void Vulcan_V(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_u = var2;
   }

   public void Vulcan_Vz(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_O = var2;
   }

   public void Vulcan_p5(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M4 = var2;
   }

   public void Vulcan_VB(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MI = var2;
   }

   public void Vulcan_pZ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QW = var2;
   }

   public void Vulcan_pK(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TM = var2;
   }

   public void Vulcan_p6(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Ta = var2;
   }

   public void Vulcan_pI(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qu = var2;
   }

   public void Vulcan_Va(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MC = var2;
   }

   public void Vulcan_p9(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jp = var2;
   }

   public void Vulcan_pv(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qh = var2;
   }

   public void Vulcan_V0(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_P = var2;
   }

   public void Vulcan_V7(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Tk = var2;
   }

   public void Vulcan_Vp(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QQ = var2;
   }

   public void Vulcan_Vy(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qt = var2;
   }

   public void Vulcan_VP(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_z = var2;
   }

   public void Vulcan_VL(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jr = var2;
   }

   public void Vulcan_VH(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MW = var2;
   }

   public void Vulcan_F(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_t = var2;
   }

   public void Vulcan_z(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TX = var2;
   }

   public void Vulcan_pD(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MB = var2;
   }

   public void Vulcan_Vh(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M3 = var2;
   }

   public void Vulcan_Vk(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_l = var2;
   }

   public void Vulcan_V4(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M7 = var2;
   }

   public void Vulcan_pX(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MK = var2;
   }

   public void Vulcan_W(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T0 = var2;
   }

   public void Vulcan_Vg(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mm = var2;
   }

   public void Vulcan_d(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mg = var2;
   }

   public void Vulcan_VX(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mc = var2;
   }

   public void Vulcan_q(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_N = var2;
   }

   public void Vulcan_Vt(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_j = var2;
   }

   public void Vulcan_VM(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MM = var2;
   }

   public void Vulcan_Vl(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QS = var2;
   }

   public void Vulcan_Vq(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M1 = var2;
   }

   public void Vulcan_Vn(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q5 = var2;
   }

   public void Vulcan_pu(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_j1 = var2;
   }

   public void Vulcan_Vr(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jK = var2;
   }

   public void Vulcan_VD(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TO = var2;
   }

   public void Vulcan_pB(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MD = var2;
   }

   public void Vulcan_V8(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_s = var2;
   }

   public void Vulcan_VF(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jO = var2;
   }

   public void Vulcan_VN(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qg = var2;
   }

   public void Vulcan_V6(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T6 = var2;
   }

   public void Vulcan_VI(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jk = var2;
   }

   public void Vulcan_VR(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_w = var2;
   }

   public void Vulcan_u(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QA = var2;
   }

   public void Vulcan_Vi(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QB = var2;
   }

   public void Vulcan_ps(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_g = var2;
   }

   public void Vulcan_pm(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qm = var2;
   }

   public void Vulcan_p4(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MV = var2;
   }

   public void Vulcan_p3(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M9 = var2;
   }

   public void Vulcan_pp(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TB = var2;
   }

   public void Vulcan_p1(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QP = var2;
   }

   public void Vulcan_K(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qo = var2;
   }

   public void Vulcan_VO(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_R = var2;
   }

   public void Vulcan_VJ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MX = var2;
   }

   public void Vulcan_V5(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TQ = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mn = var2;
   }

   public void Vulcan_B(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q = var2;
   }

   public void Vulcan_Vm(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QD = var2;
   }

   public void Vulcan_Vb(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qk = var2;
   }

   public void Vulcan_VS(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qf = var2;
   }

   public void Vulcan_VY(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_A = var2;
   }

   public void Vulcan_p(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jN = var2;
   }

   public void Vulcan_pz(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_o = var2;
   }

   public void Vulcan_Vd(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Te = var2;
   }

   public void Vulcan_t(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TH = var2;
   }

   public void Vulcan_Vj(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Tr = var2;
   }

   public void Vulcan_VE(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_j4 = var2;
   }

   public void Vulcan_pO(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q2 = var2;
   }

   public void Vulcan_VQ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_My = var2;
   }

   public void Vulcan_V9(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_G = var2;
   }

   public void Vulcan_pa(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jM = var2;
   }

   public void Vulcan_Vs(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Tv = var2;
   }

   public void Vulcan_QK(Object[] var1) {
      Location var2 = (Location)var1[0];
      this.Vulcan_T9 = var2;
   }

   public void Vulcan_QF(Object[] var1) {
      Location var2 = (Location)var1[0];
      this.Vulcan_Tf = var2;
   }

   public void Vulcan_a(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_Tt = var2;
   }

   public void Vulcan_ao(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_Qp = var2;
   }

   public void Vulcan_aJ(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_Qe = var2;
   }

   public void Vulcan_EP(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_MJ = var2;
   }

   public void Vulcan_Ew(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_jm = var2;
   }

   public void Vulcan_E5(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_Mx = var2;
   }

   public void Vulcan_E(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_T4 = var2;
   }

   public void Vulcan_EV(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_Ti = var2;
   }

   public void Vulcan_bw(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_q = var2;
   }

   public void Vulcan_bi(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_j2 = var2;
   }

   public void Vulcan_bx(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_d = var2;
   }

   public void Vulcan_bb(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Tl = var2;
   }

   public void Vulcan_bj(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TL = var2;
   }

   public void Vulcan_b(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_x = var2;
   }

   public void Vulcan_bz(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_jF = var2;
   }

   public void Vulcan_bt(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Mk = var2;
   }

   public void Vulcan_bY(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Q4 = var2;
   }

   public void Vulcan_M(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_W = var2;
   }

   public void Vulcan_T(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_M0 = var2;
   }

   public void Vulcan_Q(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_QF = var2;
   }

   public void Vulcan_bs(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Td = var2;
   }

   public void Vulcan_ba(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Qw = var2;
   }

   public void Vulcan_bP(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_c = var2;
   }

   public void Vulcan_bg(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_y = var2;
   }

   public void Vulcan_b8(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TT = var2;
   }

   public void Vulcan__(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Tp = var2;
   }

   public void Vulcan_bn(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TW = var2;
   }

   public void Vulcan_j(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Ty = var2;
   }

   public void Vulcan_bS(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_M8 = var2;
   }

   public void Vulcan_m(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Y = var2;
   }

   public void Vulcan_b1(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_MG = var2;
   }

   public void Vulcan_bN(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_QN = var2;
   }

   public void Vulcan_bX(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Tc = var2;
   }

   public void Vulcan_bK(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_jj = var2;
   }

   public void Vulcan_bT(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TN = var2;
   }

   public void Vulcan_b4(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_b2(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_K = var2;
   }

   public void Vulcan_bu(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Mb = var2;
   }

   public void Vulcan_bh(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_T_ = var2;
   }

   public void Vulcan_bp(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_jB = var2;
   }

   public void Vulcan_b6(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Mq = var2;
   }

   public void Vulcan_b5(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_jq = var2;
   }

   public void Vulcan_R(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_QJ = var2;
   }

   public void Vulcan_by(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TK = var2;
   }

   public void Vulcan_bd(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TE = var2;
   }

   public void Vulcan_o(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_TG = var2;
   }

   public void Vulcan_bq(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_ME = var2;
   }

   public void Vulcan_bc(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Q9 = var2;
   }

   public void Vulcan_bo(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Ms = var2;
   }

   public void Vulcan_bF(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_jI = var2;
   }

   public void Vulcan_g(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Q_ = var2;
   }

   public void Vulcan_bM(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_QV = var2;
   }

   public void Vulcan_U(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_QY = var2;
   }

   public void Vulcan_c(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Qc = var2;
   }

   public void Vulcan_bE(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_M5 = var2;
   }

   public void Vulcan_Hm(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TP = var2;
   }

   public void Vulcan_H4(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Ql = var2;
   }

   public void Vulcan_Hw(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mv = var2;
   }

   public void Vulcan_L(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_f = var2;
   }

   public void Vulcan_Hh(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mt = var2;
   }

   public void Vulcan_HA(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T5 = var2;
   }

   public void Vulcan_H3(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QZ = var2;
   }

   public void Vulcan_P(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Z = var2;
   }

   public void Vulcan_Hd(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_S = var2;
   }

   public void Vulcan_Hi(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_E = var2;
   }

   public void Vulcan_H7(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TS = var2;
   }

   public void Vulcan_Hf(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_MO = var2;
   }

   public void Vulcan_Hz(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_V = var2;
   }

   public void Vulcan_HU(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TR = var2;
   }

   public void Vulcan_HT(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TA = var2;
   }

   public void Vulcan_Hn(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tn = var2;
   }

   public void Vulcan_Hs(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QI = var2;
   }

   public void Vulcan_Hy(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QC = var2;
   }

   public void Vulcan_v(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Q7 = var2;
   }

   public void Vulcan_Ht(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_ML = var2;
   }

   public void Vulcan_Gj(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mp = var2;
   }

   public void Vulcan_GL(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jn = var2;
   }

   public void Vulcan_Y(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jE = var2;
   }

   public void Vulcan_Hg(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_r = var2;
   }

   public void Vulcan_Hk(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_M6 = var2;
   }

   public void Vulcan_A(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jw = var2;
   }

   public void Vulcan_HO(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_X = var2;
   }

   public void Vulcan_HD(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T7 = var2;
   }

   public void Vulcan_H5(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tb = var2;
   }

   public void Vulcan_Hl(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qn = var2;
   }

   public void Vulcan_HH(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qy = var2;
   }

   public void Vulcan_r(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jC = var2;
   }

   public void Vulcan_H_(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_F = var2;
   }

   public void Vulcan_HK(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_n = var2;
   }

   public void Vulcan_HR(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jY = var2;
   }

   public void Vulcan_Hc(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T2 = var2;
   }

   public void Vulcan_HS(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jl = var2;
   }

   public void Vulcan_HY(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_J = var2;
   }

   public void Vulcan_HQ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QO = var2;
   }

   public void Vulcan_Hq(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jZ = var2;
   }

   public void Vulcan_Ho(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tu = var2;
   }

   public void Vulcan_H8(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_a = var2;
   }

   public void Vulcan_H1(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Q1 = var2;
   }

   public void Vulcan_H0(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jt = var2;
   }

   public void Vulcan_HG(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qs = var2;
   }

   public void Vulcan_Hu(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_U = var2;
   }

   public void Vulcan_f(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Ml = var2;
   }

   public void Vulcan_x(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan__ = var2;
   }

   public void Vulcan_HL(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TF = var2;
   }

   public void Vulcan_HB(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_MQ = var2;
   }

   public void Vulcan_Ha(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qx = var2;
   }

   public void Vulcan_HM(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_C = var2;
   }

   public void Vulcan_HV(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qa = var2;
   }

   public void Vulcan_Gc(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qd = var2;
   }

   public void Vulcan_C(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mu = var2;
   }

   public void Vulcan_Hv(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tq = var2;
   }

   public void Vulcan_H2(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mr = var2;
   }

   public void Vulcan_HE(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TJ = var2;
   }

   public void Vulcan_Hj(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Qb = var2;
   }

   public void Vulcan_HN(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TY = var2;
   }

   public void Vulcan_HZ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tx = var2;
   }

   public void Vulcan_H(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_TI = var2;
   }

   public void Vulcan_HX(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_MN = var2;
   }

   public void Vulcan_He(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tm = var2;
   }

   public void Vulcan_H9(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tg = var2;
   }

   public void Vulcan_HP(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Th = var2;
   }

   public void Vulcan_Hb(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T1 = var2;
   }

   public void Vulcan_Hx(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QE = var2;
   }

   public void Vulcan_Hr(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Tw = var2;
   }

   public void Vulcan_HJ(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jD = var2;
   }

   public void Vulcan_HF(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_b = var2;
   }

   public void Vulcan_Hp(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_MS = var2;
   }

   public void Vulcan_G1(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_M_ = var2;
   }

   public void Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_jS = var2;
   }

   public void Vulcan_HC(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QG = var2;
   }

   public void Vulcan_HI(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_Mo = var2;
   }

   public void Vulcan_H6(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_MT = var2;
   }

   public void Vulcan_HW(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_QX = var2;
   }

   public void Vulcan__g(Object[] var1) {
      World var2 = (World)var1[0];
      this.Vulcan_Mw = var2;
   }

   public void Vulcan_dI(Object[] var1) {
      Entity var2 = (Entity)var1[0];
      this.Vulcan_Qz = var2;
   }

   public void Vulcan_dl(Object[] var1) {
      Entity var2 = (Entity)var1[0];
      this.Vulcan_MY = var2;
   }

   public void Vulcan_h(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_TC = var2;
   }

   public void Vulcan_hE(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_QK = var2;
   }

   public void Vulcan_Ez(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_p = var2;
   }

   public void Vulcan_w(Object[] var1) {
      LinkedList var2 = (LinkedList)var1[0];
      this.Vulcan_TU = var2;
   }

   public void Vulcan_Vu(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_D = var2;
   }

   public void Vulcan_n(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Q3 = var2;
   }

   public void Vulcan_V2(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mj = var2;
   }

   public void Vulcan_pt(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_m = var2;
   }

   public void Vulcan_VA(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_TD = var2;
   }

   public void Vulcan_pR(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_jA = var2;
   }

   public void Vulcan_V3(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Qr = var2;
   }

   public void Vulcan_l(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_h = var2;
   }

   public void Vulcan_VU(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan_VV(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mf = var2;
   }

   public void Vulcan_pC(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QL = var2;
   }

   public void Vulcan_p0(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Mi = var2;
   }

   public void Vulcan_VC(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_QM = var2;
   }

   public void Vulcan_I(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_M = var2;
   }

   public void Vulcan_k(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_MF = var2;
   }

   public void Vulcan_VZ(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Tj = var2;
   }

   public void Vulcan_e(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_j_ = var2;
   }

   public void Vulcan_Vx(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T3 = var2;
   }

   public void Vulcan_Em(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_MU = var2;
   }

   public void Vulcan_E6(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_MR = var2;
   }

   public void Vulcan_EW(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_TZ = var2;
   }

   public void Vulcan_EY(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_H = var2;
   }

   public void Vulcan_J(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_Tz = var2;
   }

   public void Vulcan_a6(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_Qi = var2;
   }

   public void Vulcan_aw(Object[] var1) {
      Material var2 = (Material)var1[0];
      this.Vulcan_jc = var2;
   }

   private void lambda$handleUnloadedChunk$1(Location var1) {
      this.Vulcan_MA.Vulcan_q(new Object[0]).teleport(var1, TeleportCause.UNKNOWN);
   }

   private void lambda$handleAbilities$0(WrapperPlayServerPlayerAbilities var1) {
      this.Vulcan_Ql = 0;
      this.Vulcan__ = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
      this.Vulcan_jK = var1.isFlightAllowed();
      this.Vulcan_QK = var1.getFlySpeed() * 2.0F;
   }

   public static void Vulcan_A(String[] var0) {
      Vulcan_B = var0;
   }

   public static String[] Vulcan_W() {
      return Vulcan_B;
   }

   static {
      long var0 = a ^ 71692962956285L;
      Vulcan_A((String[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[18];
      int var7 = 0;
      String var6 = "nM±h·&B0\u0010Áb8È¾ú\"o\u0014¡ÇÏ\u0096ÜHÇ\bóþ ãö7L\u008e\bnM±h·&B0\bZølñxð¬\u001c\b´½G\u009a`2\u008a\t\bC<ý\u0098\u0000N{^\u0010\u008dõV\u001aÇÐØÜ®#²,ü\u0003ØÁ\u0010Áb8È¾ú\"o\u0014¡ÇÏ\u0096ÜHÇ\b&E\u008b\u0084\u0010\u00110P\u0010¸ªB\u0001c±_¸Ü\u0013p\u0019\u000f\u0017g\u0013\bóþ ãö7L\u008e\b,³zÕÊõ½r\bZølñxð¬\u001c\u0010¸ªB\u0001c±_¸Ü\u0013p\u0019\u000f\u0017g\u0013\b,³zÕÊõ½r";
      int var8 = "nM±h·&B0\u0010Áb8È¾ú\"o\u0014¡ÇÏ\u0096ÜHÇ\bóþ ãö7L\u008e\bnM±h·&B0\bZølñxð¬\u001c\b´½G\u009a`2\u008a\t\bC<ý\u0098\u0000N{^\u0010\u008dõV\u001aÇÐØÜ®#²,ü\u0003ØÁ\u0010Áb8È¾ú\"o\u0014¡ÇÏ\u0096ÜHÇ\b&E\u008b\u0084\u0010\u00110P\u0010¸ªB\u0001c±_¸Ü\u0013p\u0019\u000f\u0017g\u0013\bóþ ãö7L\u008e\b,³zÕÊõ½r\bZølñxð¬\u001c\u0010¸ªB\u0001c±_¸Ü\u0013p\u0019\u000f\u0017g\u0013\b,³zÕÊõ½r".length();
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

               var6 = "ÌAÊI\u0012ñ:\u0082\u001c\u0088Fë\u008aGjïã°%\u0099E5\u0018\u0015ªø}~·,ú\u0003\b´½G\u009a`2\u008a\t";
               var8 = "ÌAÊI\u0012ñ:\u0082\u001c\u0088Fë\u008aGjïã°%\u0099E5\u0018\u0015ªø}~·,ú\u0003\b´½G\u009a`2\u008a\t".length();
               var5 = ' ';
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
