package me.frep.vulcan.spigot.check.manager;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimA;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimB;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimC;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimD;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimE;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimF;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimG;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimH;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimI;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimK;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimL;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimN;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimO;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimP;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimQ;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimS;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimU;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimW;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimX;
import me.frep.vulcan.spigot.check.impl.combat.aim.AimY;
import me.frep.vulcan.spigot.check.impl.combat.autoblock.AutoBlockA;
import me.frep.vulcan.spigot.check.impl.combat.autoblock.AutoBlockB;
import me.frep.vulcan.spigot.check.impl.combat.autoblock.AutoBlockC;
import me.frep.vulcan.spigot.check.impl.combat.autoblock.AutoBlockD;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerA;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerB;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerC;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerD;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerE;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerF;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerG;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerH;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerI;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerJ;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerK;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerL;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerM;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerN;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerO;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerP;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerQ;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerR;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerS;
import me.frep.vulcan.spigot.check.impl.combat.autoclicker.AutoClickerT;
import me.frep.vulcan.spigot.check.impl.combat.criticals.CriticalsA;
import me.frep.vulcan.spigot.check.impl.combat.criticals.CriticalsB;
import me.frep.vulcan.spigot.check.impl.combat.fastbow.FastBowA;
import me.frep.vulcan.spigot.check.impl.combat.hitbox.HitboxA;
import me.frep.vulcan.spigot.check.impl.combat.hitbox.HitboxB;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraA;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraB;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraC;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraD;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraJ;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraK;
import me.frep.vulcan.spigot.check.impl.combat.killaura.KillAuraL;
import me.frep.vulcan.spigot.check.impl.combat.reach.ReachA;
import me.frep.vulcan.spigot.check.impl.combat.reach.ReachB;
import me.frep.vulcan.spigot.check.impl.combat.velocity.VelocityA;
import me.frep.vulcan.spigot.check.impl.combat.velocity.VelocityB;
import me.frep.vulcan.spigot.check.impl.combat.velocity.VelocityC;
import me.frep.vulcan.spigot.check.impl.combat.velocity.VelocityD;
import me.frep.vulcan.spigot.check.impl.movement.antilevitation.AntiLevitationA;
import me.frep.vulcan.spigot.check.impl.movement.boatfly.BoatFlyA;
import me.frep.vulcan.spigot.check.impl.movement.boatfly.BoatFlyB;
import me.frep.vulcan.spigot.check.impl.movement.boatfly.BoatFlyC;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraA;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraB;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraC;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraF;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraG;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraI;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraK;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraL;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraM;
import me.frep.vulcan.spigot.check.impl.movement.elytra.ElytraN;
import me.frep.vulcan.spigot.check.impl.movement.entityflight.EntityFlightA;
import me.frep.vulcan.spigot.check.impl.movement.entityflight.EntityFlightB;
import me.frep.vulcan.spigot.check.impl.movement.entityspeed.EntitySpeedA;
import me.frep.vulcan.spigot.check.impl.movement.fastclimb.FastClimbA;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightA;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightB;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightC;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightD;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightE;
import me.frep.vulcan.spigot.check.impl.movement.flight.FlightF;
import me.frep.vulcan.spigot.check.impl.movement.jesus.JesusA;
import me.frep.vulcan.spigot.check.impl.movement.jesus.JesusB;
import me.frep.vulcan.spigot.check.impl.movement.jesus.JesusC;
import me.frep.vulcan.spigot.check.impl.movement.jesus.JesusD;
import me.frep.vulcan.spigot.check.impl.movement.jesus.JesusE;
import me.frep.vulcan.spigot.check.impl.movement.jump.JumpA;
import me.frep.vulcan.spigot.check.impl.movement.jump.JumpB;
import me.frep.vulcan.spigot.check.impl.movement.jump.JumpC;
import me.frep.vulcan.spigot.check.impl.movement.motion.MotionA;
import me.frep.vulcan.spigot.check.impl.movement.motion.MotionB;
import me.frep.vulcan.spigot.check.impl.movement.motion.MotionC;
import me.frep.vulcan.spigot.check.impl.movement.motion.MotionE;
import me.frep.vulcan.spigot.check.impl.movement.nosaddle.NoSaddleA;
import me.frep.vulcan.spigot.check.impl.movement.noslow.NoSlowA;
import me.frep.vulcan.spigot.check.impl.movement.noslow.NoSlowB;
import me.frep.vulcan.spigot.check.impl.movement.noslow.NoSlowC;
import me.frep.vulcan.spigot.check.impl.movement.speed.SpeedA;
import me.frep.vulcan.spigot.check.impl.movement.speed.SpeedB;
import me.frep.vulcan.spigot.check.impl.movement.speed.SpeedC;
import me.frep.vulcan.spigot.check.impl.movement.speed.SpeedD;
import me.frep.vulcan.spigot.check.impl.movement.speed.SpeedE;
import me.frep.vulcan.spigot.check.impl.movement.sprint.SprintA;
import me.frep.vulcan.spigot.check.impl.movement.step.StepA;
import me.frep.vulcan.spigot.check.impl.movement.step.StepC;
import me.frep.vulcan.spigot.check.impl.movement.strafe.StrafeA;
import me.frep.vulcan.spigot.check.impl.movement.vclip.VClipA;
import me.frep.vulcan.spigot.check.impl.movement.wallclimb.WallClimbA;
import me.frep.vulcan.spigot.check.impl.player.airplace.AirPlaceA;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPackets5;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPackets6;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPackets8;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPackets9;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsA;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsB;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsC;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsD;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsE;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsF;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsG;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsH;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsI;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsJ;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsK;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsM;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsN;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsO;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsP;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsQ;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsR;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsT;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsV;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsW;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsX;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsY;
import me.frep.vulcan.spigot.check.impl.player.badpackets.BadPacketsZ;
import me.frep.vulcan.spigot.check.impl.player.baritone.BaritoneA;
import me.frep.vulcan.spigot.check.impl.player.baritone.BaritoneB;
import me.frep.vulcan.spigot.check.impl.player.fastbreak.FastBreakA;
import me.frep.vulcan.spigot.check.impl.player.fastplace.FastPlaceA;
import me.frep.vulcan.spigot.check.impl.player.ghosthand.GhostHandA;
import me.frep.vulcan.spigot.check.impl.player.groundspoof.GroundSpoofA;
import me.frep.vulcan.spigot.check.impl.player.groundspoof.GroundSpoofB;
import me.frep.vulcan.spigot.check.impl.player.groundspoof.GroundSpoofC;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableA;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableB;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableC;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableD;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableE;
import me.frep.vulcan.spigot.check.impl.player.improbable.ImprobableF;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidA;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidB;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidC;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidE;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidF;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidI;
import me.frep.vulcan.spigot.check.impl.player.invalid.InvalidJ;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldA;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldB;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldC;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldD;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldE;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldF;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldG;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldH;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldI;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldJ;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldK;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldM;
import me.frep.vulcan.spigot.check.impl.player.scaffold.ScaffoldN;
import me.frep.vulcan.spigot.check.impl.player.timer.TimerA;
import me.frep.vulcan.spigot.check.impl.player.timer.TimerD;
import me.frep.vulcan.spigot.check.impl.player.tower.TowerA;

public class CheckManager {
   public static final List Vulcan__;
   public static final Class[] Vulcan_W;
   private static String Vulcan_z;
   private static final long a = Vulcan_n.a(1653085394933946273L, -1996923523360996543L, MethodHandles.lookup().lookupClass()).a(9631944365919L);
   private static final String[] b;

   public static List Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = a ^ 129861841083576L;
      Vulcan_c((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[5];
      int var7 = 0;
      String var6 = "&°³¶®úh\u0018û\u008aP\u0082\u0017§8ÓëaVÇÏ\u0001\u009a|¾¼°t\f\u000e|Õ\u0010\u008e\u0095ä\u000b\u0013\u0091ã\u0016¨\u0088ãÈiØÈ%pFsê\u001fFî\u009f×\\Á_åÓÍg\u0085k\u0014¨\u008e2v´\u0087Lpåó³æïpÝp\u001c }íëª¹ìôQ\u0095|Ç¦¶\u0095Òùkþ&\u0001o\u0091¼Âv\u009c×\u0080P\u0089,T@\u008e&Û¾ö¼\\\u00ad}l8\u0093@\u0018\u00ad\u0001\fÆ\u0089'0iR#}C\u008bb\u009bÓÃ;ÌPq\u0088R`ù\u008d·Q%";
      int var8 = "&°³¶®úh\u0018û\u008aP\u0082\u0017§8ÓëaVÇÏ\u0001\u009a|¾¼°t\f\u000e|Õ\u0010\u008e\u0095ä\u000b\u0013\u0091ã\u0016¨\u0088ãÈiØÈ%pFsê\u001fFî\u009f×\\Á_åÓÍg\u0085k\u0014¨\u008e2v´\u0087Lpåó³æïpÝp\u001c }íëª¹ìôQ\u0095|Ç¦¶\u0095Òùkþ&\u0001o\u0091¼Âv\u009c×\u0080P\u0089,T@\u008e&Û¾ö¼\\\u00ad}l8\u0093@\u0018\u00ad\u0001\fÆ\u0089'0iR#}C\u008bb\u009bÓÃ;ÌPq\u0088R`ù\u008d·Q%".length();
      char var5 = ' ';
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
                  Vulcan__ = new ArrayList();
                  Vulcan_W = new Class[]{AimA.class, AimB.class, AimC.class, AimD.class, AimE.class, AimF.class, AimG.class, AimH.class, AimI.class, AimK.class, AimL.class, AimN.class, AimO.class, AimP.class, AimQ.class, AimS.class, AimU.class, AimW.class, AimX.class, AimY.class, AutoBlockA.class, AutoBlockB.class, AutoBlockC.class, AutoBlockD.class, AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class, AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, AutoClickerH.class, AutoClickerI.class, AutoClickerJ.class, AutoClickerK.class, AutoClickerL.class, AutoClickerM.class, AutoClickerN.class, AutoClickerO.class, AutoClickerP.class, AutoClickerQ.class, AutoClickerR.class, AutoClickerS.class, AutoClickerT.class, FastBowA.class, HitboxA.class, HitboxB.class, KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class, KillAuraJ.class, KillAuraK.class, KillAuraL.class, ReachA.class, ReachB.class, VelocityA.class, VelocityB.class, VelocityC.class, VelocityD.class, BoatFlyA.class, BoatFlyB.class, BoatFlyC.class, AntiLevitationA.class, NoSaddleA.class, EntitySpeedA.class, EntityFlightA.class, EntityFlightB.class, GhostHandA.class, ElytraA.class, ElytraB.class, ElytraC.class, ElytraF.class, ElytraG.class, ElytraI.class, ElytraK.class, ElytraL.class, ElytraM.class, ElytraN.class, CriticalsA.class, CriticalsB.class, FastClimbA.class, FlightA.class, FlightB.class, FlightC.class, FlightD.class, FlightE.class, FlightF.class, JesusA.class, JesusB.class, JesusC.class, JesusD.class, JesusE.class, JumpA.class, JumpB.class, JumpC.class, MotionA.class, MotionB.class, MotionC.class, MotionE.class, NoSlowA.class, NoSlowB.class, NoSlowC.class, SpeedA.class, SpeedB.class, SpeedC.class, SpeedD.class, SpeedE.class, StepA.class, StepC.class, SprintA.class, StrafeA.class, WallClimbA.class, BaritoneA.class, BaritoneB.class, BadPackets5.class, BadPackets6.class, BadPackets8.class, BadPackets9.class, BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, BadPacketsG.class, BadPacketsH.class, BadPacketsI.class, BadPacketsJ.class, BadPacketsK.class, BadPacketsM.class, BadPacketsN.class, BadPacketsO.class, BadPacketsP.class, BadPacketsQ.class, BadPacketsR.class, BadPacketsT.class, BadPacketsV.class, BadPacketsW.class, BadPacketsX.class, BadPacketsY.class, BadPacketsZ.class, FastPlaceA.class, FastBreakA.class, GroundSpoofA.class, GroundSpoofB.class, GroundSpoofC.class, ImprobableA.class, ImprobableB.class, ImprobableC.class, ImprobableD.class, ImprobableE.class, ImprobableF.class, InvalidA.class, InvalidB.class, InvalidC.class, InvalidE.class, InvalidF.class, InvalidI.class, InvalidJ.class, VClipA.class, AirPlaceA.class, ScaffoldA.class, ScaffoldB.class, ScaffoldC.class, ScaffoldD.class, ScaffoldE.class, ScaffoldF.class, ScaffoldG.class, ScaffoldH.class, ScaffoldI.class, ScaffoldJ.class, ScaffoldK.class, ScaffoldM.class, ScaffoldN.class, TimerA.class, TimerD.class, TowerA.class};
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

               var6 = "éOV48g\u0084\u0013½»\u0013\u0013É\u001c(Ó½\u008e²Î\u008a\u0013Tµ\u0080ÕÈ\u0081È(\u009fï éOV48g\u0084\u0013½»\u0013\u0013É\u001c(ÓD\\è£\u001cã<\u0013wWÏ\u008a|«)¿";
               var8 = "éOV48g\u0084\u0013½»\u0013\u0013É\u001c(Ó½\u008e²Î\u008a\u0013Tµ\u0080ÕÈ\u0081È(\u009fï éOV48g\u0084\u0013½»\u0013\u0013É\u001c(ÓD\\è£\u001cã<\u0013wWÏ\u008a|«)¿".length();
               var5 = ' ';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   public static void Vulcan_c(String var0) {
      Vulcan_z = var0;
   }

   public static String Vulcan_x() {
      return Vulcan_z;
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
