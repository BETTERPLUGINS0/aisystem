package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import java.lang.invoke.MethodHandles;
import java.util.function.Function;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

public enum Vulcan_in {
   public static final Vulcan_in TPS;
   public static final Vulcan_in CHUNK;
   public static final Vulcan_in JOINED;
   public static final Vulcan_in CAKE;
   public static final Vulcan_in BAMBOO;
   public static final Vulcan_in HAPPY_GHAST;
   public static final Vulcan_in PACKET_EXPLOSION;
   public static final Vulcan_in ENTITY_CRAM_FIX;
   public static final Vulcan_in AROUND_SLIME;
   public static final Vulcan_in SIGN;
   public static final Vulcan_in PRESSURE_PLATE;
   public static final Vulcan_in SWIMMING;
   public static final Vulcan_in SWIMMING_JESUS;
   public static final Vulcan_in COLLIDING_HORIZONTALLY;
   public static final Vulcan_in DRIPSTONE;
   public static final Vulcan_in HIGH_FLY_SPEED;
   public static final Vulcan_in GLITCHED_BLOCKS_ABOVE;
   public static final Vulcan_in PROJECTILE_DAMAGE;
   public static final Vulcan_in CHEST;
   public static final Vulcan_in ATTACK_DAMAGE;
   public static final Vulcan_in SKULL;
   public static final Vulcan_in EXPLOSION;
   public static final Vulcan_in STAIRS;
   public static final Vulcan_in HIGH_LEVITATION;
   public static final Vulcan_in FENCE_GATE;
   public static final Vulcan_in POWDER_SNOW;
   public static final Vulcan_in DRAGON_DAMAGE;
   public static final Vulcan_in JUMP_BOOST_RAN_OUT;
   public static final Vulcan_in LAGGED_NEAR_GROUND;
   public static final Vulcan_in SNOW;
   public static final Vulcan_in NETHERITE_ARMOR;
   public static final Vulcan_in FAST_ZERO;
   public static final Vulcan_in JOINED_CHUNK_LOAD;
   public static final Vulcan_in NEAR_SOLID;
   public static final Vulcan_in CAMPFIRE;
   public static final Vulcan_in CANCELLED_MOVE;
   public static final Vulcan_in CARPET;
   public static final Vulcan_in FLOWER_POT;
   public static final Vulcan_in EMPTIED_BUCKET;
   public static final Vulcan_in END_ROD;
   public static final Vulcan_in HOPPER;
   public static final Vulcan_in CHAIN;
   public static final Vulcan_in DOOR;
   public static final Vulcan_in PICKED_UP_ITEM;
   public static final Vulcan_in ANVIL;
   public static final Vulcan_in LAGGED_NEAR_GROUND_MODERN;
   public static final Vulcan_in FULLY_SUBMERGED;
   public static final Vulcan_in RESPAWN;
   public static final Vulcan_in ICE;
   public static final Vulcan_in BED;
   public static final Vulcan_in SLAB;
   public static final Vulcan_in HIGH_JUMP_BOOST;
   public static final Vulcan_in LILY_PAD;
   public static final Vulcan_in HIGH_SPEED;
   public static final Vulcan_in WALL;
   public static final Vulcan_in SWEET_BERRIES;
   public static final Vulcan_in CONDUIT;
   public static final Vulcan_in PLACED_WEB;
   public static final Vulcan_in PISTON;
   public static final Vulcan_in CHORUS_FRUIT;
   public static final Vulcan_in ENDER_PEARL;
   public static final Vulcan_in NEAR_GROUND;
   public static final Vulcan_in SLEEPING;
   public static final Vulcan_in SOUL_SPEED;
   public static final Vulcan_in TRAPDOOR;
   public static final Vulcan_in FISHING_ROD;
   public static final Vulcan_in PLUGIN_LOAD;
   public static final Vulcan_in RIPTIDE;
   public static final Vulcan_in FIREWORK;
   public static final Vulcan_in SPECTATOR;
   public static final Vulcan_in CAULDRON;
   public static final Vulcan_in ENTITY_COLLISION;
   public static final Vulcan_in DEATH;
   public static final Vulcan_in DEPTH_STRIDER;
   public static final Vulcan_in LEVITATION;
   public static final Vulcan_in FROZEN;
   public static final Vulcan_in DIGGING;
   public static final Vulcan_in BLOCK_BREAK;
   public static final Vulcan_in PLACING;
   public static final Vulcan_in SERVER_POSITION;
   public static final Vulcan_in SERVER_POSITION_FAST;
   public static final Vulcan_in CANCELLED_PLACE;
   public static final Vulcan_in BLOCK_PLACE;
   public static final Vulcan_in BLOCK_PLACE_FAST;
   public static final Vulcan_in SWIMMING_ON_OLD_VERSION;
   public static final Vulcan_in FULLY_STUCK;
   public static final Vulcan_in PLACED_CLIMBABLE;
   public static final Vulcan_in PARTIALLY_STUCK;
   public static final Vulcan_in NOT_MOVING;
   public static final Vulcan_in PLACED_SLIME;
   public static final Vulcan_in FIREBALL;
   public static final Vulcan_in WORLD_CHANGE;
   public static final Vulcan_in FALL_DAMAGE;
   public static final Vulcan_in ILLEGAL_BLOCK;
   public static final Vulcan_in HONEY;
   public static final Vulcan_in SCAFFOLDING;
   public static final Vulcan_in HALF_BLOCK;
   public static final Vulcan_in SHULKER;
   public static final Vulcan_in GLIDING;
   public static final Vulcan_in ELYTRA;
   public static final Vulcan_in VELOCITY;
   public static final Vulcan_in DEAD;
   public static final Vulcan_in WEB;
   public static final Vulcan_in SOUL_SAND;
   public static final Vulcan_in CINEMATIC;
   public static final Vulcan_in FAST;
   public static final Vulcan_in LENIENT_SCAFFOLDING;
   public static final Vulcan_in WINDOW_CLICK;
   public static final Vulcan_in DROPPED_ITEM;
   public static final Vulcan_in CANCELLED_BREAK;
   public static final Vulcan_in MYTHIC_MOB;
   public static final Vulcan_in CREATIVE;
   public static final Vulcan_in AUTOCLICKER_NON_DIG;
   public static final Vulcan_in AUTOCLICKER;
   public static final Vulcan_in COLLIDING_VERTICALLY;
   public static final Vulcan_in SLOW_FALLING;
   public static final Vulcan_in BUBBLE_COLUMN;
   public static final Vulcan_in FENCE;
   public static final Vulcan_in JUMP_BOOST;
   public static final Vulcan_in FLIGHT;
   public static final Vulcan_in COMBO_MODE;
   public static final Vulcan_in DOLPHINS_GRACE;
   public static final Vulcan_in TELEPORT;
   public static final Vulcan_in SERVER_VERSION;
   public static final Vulcan_in KELP;
   public static final Vulcan_in CLIENT_VERSION;
   public static final Vulcan_in SLIME;
   public static final Vulcan_in BOAT;
   public static final Vulcan_in SEA_PICKLE;
   public static final Vulcan_in SEAGRASS;
   public static final Vulcan_in TURTLE_EGG;
   public static final Vulcan_in SHULKER_BOX;
   public static final Vulcan_in WATERLOGGED;
   public static final Vulcan_in CLIMBABLE;
   public static final Vulcan_in LIQUID;
   public static final Vulcan_in GLASS_PANE;
   public static final Vulcan_in FARMLAND;
   public static final Vulcan_in VEHICLE;
   public static final Vulcan_in VOID;
   private final Function Vulcan_p;
   private static final Vulcan_in[] Vulcan_E;
   private static AbstractCheck[] Vulcan_I;
   private static final long a = Vulcan_n.a(-4207077752791215679L, -8190549249516353966L, MethodHandles.lookup().lookupClass()).a(5213405115859L);

   private Vulcan_in(Function var3) {
      this.Vulcan_p = var3;
   }

   public Function Vulcan_I() {
      return this.Vulcan_p;
   }

   private static Boolean lambda$static$138(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$137(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$136(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$135(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_a(new Object[0]);
   }

   private static Boolean lambda$static$134(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$133(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$132(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$131(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$130(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_j(new Object[0]);
   }

   private static Boolean lambda$static$129(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_ex(new Object[0]);
   }

   private static Boolean lambda$static$128(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l8(new Object[0]);
   }

   private static Boolean lambda$static$127(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lk(new Object[0]);
   }

   private static Boolean lambda$static$126(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$125(Vulcan_iE var0) {
      return var0.Vulcan_I(new Object[0]).getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
   }

   private static Boolean lambda$static$124(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_d(new Object[0]);
   }

   private static Boolean lambda$static$123(Vulcan_iE var0) {
      return Vulcan_eG.Vulcan_X(new Object[0]);
   }

   private static Boolean lambda$static$122(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$121(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$120(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$119(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$118(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$117(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$116(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$115(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$114(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$113(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$112(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$111(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$110(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$109(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$108(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$107(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$106(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$105(Vulcan_iE var0) {
      return var0.Vulcan_a(new Object[0]).Vulcan_z(new Object[0]);
   }

   private static Boolean lambda$static$104(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$103(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_h(new Object[0]);
   }

   private static Boolean lambda$static$102(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$101(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$100(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$99(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$98(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$97(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$96(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$95(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$94(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$93(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$92(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$91(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$90(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$89(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$88(Vulcan_iE var0) {
      long var1 = a ^ 126245006734141L;
      AbstractCheck[] var3 = Vulcan_G();

      boolean var10000;
      label32: {
         try {
            var10000 = var0.Vulcan_e(new Object[0]).Vulcan_ll(new Object[0]);
            if (var3 != null) {
               return var10000;
            }

            if (!var10000) {
               break label32;
            }
         } catch (RuntimeException var4) {
            throw a(var4);
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   private static Boolean lambda$static$87(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l6(new Object[0]);
   }

   private static Boolean lambda$static$86(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$85(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lY(new Object[0]);
   }

   private static Boolean lambda$static$84(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$83(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$82(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$81(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$80(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$79(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$78(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$77(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$76(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$75(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lV(new Object[0]);
   }

   private static Boolean lambda$static$74(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$73(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$72(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$71(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$70(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lI(new Object[0]);
   }

   private static Boolean lambda$static$69(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$68(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$67(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$66(Vulcan_iE var0) {
      long var1 = a ^ 107711657157577L;
      AbstractCheck[] var3 = Vulcan_G();

      int var10000;
      label32: {
         try {
            long var5;
            var10000 = (var5 = System.currentTimeMillis() - Vulcan_Xs.INSTANCE.Vulcan_s() - 10000L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
            if (var3 != null) {
               return Boolean.valueOf((boolean)var10000);
            }

            if (var10000 < 0) {
               break label32;
            }
         } catch (RuntimeException var4) {
            throw a(var4);
         }

         var10000 = 0;
         return Boolean.valueOf((boolean)var10000);
      }

      var10000 = 1;
      return Boolean.valueOf((boolean)var10000);
   }

   private static Boolean lambda$static$65(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$64(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$63(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$62(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$61(Vulcan_iE var0) {
      long var1 = a ^ 120841154809856L;
      long var3 = var1 ^ 27360875501679L;
      return var0.Vulcan_e(new Object[0]).Vulcan_l0(new Object[]{var3});
   }

   private static Boolean lambda$static$60(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$59(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$58(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$57(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$56(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lt(new Object[0]);
   }

   private static Boolean lambda$static$55(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_e6(new Object[0]);
   }

   private static Boolean lambda$static$54(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l5(new Object[0]);
   }

   private static Boolean lambda$static$53(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$52(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lL(new Object[0]);
   }

   private static Boolean lambda$static$51(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$50(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_J(new Object[0]);
   }

   private static Boolean lambda$static$49(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$48(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$47(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$46(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lS(new Object[0]);
   }

   private static Boolean lambda$static$45(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$44(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lu(new Object[0]);
   }

   private static Boolean lambda$static$43(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$42(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lh(new Object[0]);
   }

   private static Boolean lambda$static$41(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_N(new Object[0]);
   }

   private static Boolean lambda$static$40(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lx(new Object[0]);
   }

   private static Boolean lambda$static$39(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lm(new Object[0]);
   }

   private static Boolean lambda$static$38(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$37(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l7(new Object[0]);
   }

   private static Boolean lambda$static$36(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lR(new Object[0]);
   }

   private static Boolean lambda$static$35(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$34(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_lM(new Object[0]);
   }

   private static Boolean lambda$static$33(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l3(new Object[0]);
   }

   private static Boolean lambda$static$32(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$31(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$30(Vulcan_iE var0) {
      long var1 = a ^ 133293733779744L;
      long var3 = var1 ^ 97758667050040L;
      return Vulcan_bQ.Vulcan_A(new Object[]{var3, var0});
   }

   private static Boolean lambda$static$29(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_li(new Object[0]);
   }

   private static Boolean lambda$static$28(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$27(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$26(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$25(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_X(new Object[0]);
   }

   private static Boolean lambda$static$24(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_x(new Object[0]);
   }

   private static Boolean lambda$static$23(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$22(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan__(new Object[0]);
   }

   private static Boolean lambda$static$21(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$20(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l2(new Object[0]);
   }

   private static Boolean lambda$static$19(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$18(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_c(new Object[0]);
   }

   private static Boolean lambda$static$17(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$16(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$15(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$14(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_T(new Object[0]);
   }

   private static Boolean lambda$static$13(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_K(new Object[0]);
   }

   private static Boolean lambda$static$12(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$11(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$10(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_l1(new Object[0]);
   }

   private static Boolean lambda$static$9(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_ld(new Object[0]);
   }

   private static Boolean lambda$static$8(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$7(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$6(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$5(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$4(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_s(new Object[0]);
   }

   private static Boolean lambda$static$3(Vulcan_iE var0) {
      return var0.Vulcan_e(new Object[0]).Vulcan_e2(new Object[0]);
   }

   private static Boolean lambda$static$2(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$1(Vulcan_iE param0) {
      // $FF: Couldn't be decompiled
   }

   private static Boolean lambda$static$0(Vulcan_iE var0) {
      long var1 = a ^ 3424255450476L;
      long var3 = var1 ^ 50023300161521L;
      AbstractCheck[] var5 = Vulcan_G();

      int var10000;
      label32: {
         try {
            double var7;
            var10000 = (var7 = Vulcan_eG.Vulcan_X(new Object[]{var3}) - 19.25D) == 0.0D ? 0 : (var7 < 0.0D ? -1 : 1);
            if (var5 != null) {
               return Boolean.valueOf((boolean)var10000);
            }

            if (var10000 < 0) {
               break label32;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var10000 = 0;
         return Boolean.valueOf((boolean)var10000);
      }

      var10000 = 1;
      return Boolean.valueOf((boolean)var10000);
   }

   static {
      long var1 = a ^ 71068999679867L;
      Vulcan_s((AbstractCheck[])null);
      Cipher var3;
      Cipher var10000 = var3 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var1 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var4 = 1; var4 < 8; ++var4) {
         var10003[var4] = (byte)((int)(var1 << var4 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var10 = new String[139];
      int var8 = 0;
      String var7 = "\u0005òù\u0084\u0016o}½Sà9s\u0084þ\u0080\u001c\u0010H\u000fó2\n\u008b\u001f]\u00ad\u0097º\u0015®\u0092×Î\u0010ß\u0092\u0091\u008aÍm±\u0016\u0014Q\u000e=\u0003y\u0081©\u0010åV\u0094Ø\u0013\u0016f¡×ÉÄúU¦!z\bñj\u008dÁê\u0099\u009bº\b.y,Äj\u0007_\u009f\b¡\u009bÝ\u0094\u0002@/>\u0010Â\u001d¼ \u0092à«Ëú\u0002è+¿Ø\u0088\u0012\u0018\u0098Ô\\z{\u0018q\u0089ôxiÀ\u0000Ûr4\u001496\u0089C\u00021é\u0010Ï`>ô\u0004\u009bµ)xcÒÍUý¸L\u00109âLóQY\u0083³ßpî6\u009cIZå\bCÿ\u0002R\u009a4\u0086©\b\u008bµ;Kù»»l\u0010vgDÿ1\u0010s\u001d_,kïK\u001asv\b\u0019CÂ§Ñ\u009c\u0096Ò\u0010º¯ÊG\\:&ÀG ê.L`~\u0092\u0010Dï\u0004Òºdn/`òéé\u00889\u008b!\b¼Ôå8-\u0014À_\u0010uEz\u008a\u0016\u0095éMÇCBP??\u0089&\b×t*\u00886\u0080æ\u0003\b\u0097T-\u008e\u00910\u0018b\u0010ë\u0096ªàc\u0017·«^u\u007fìQÝJø\u0010Ý}\u0087òð87gìþù\u00065%\u000f\u009e\u0018\u0085÷O=ÿª\u0085Ë5ÑÌ\fPÃ}\u001b¶\u0088\u0088Í\u009e¸Öp\bÎ%9ð\u0013û\u0084\u0007\u0010lÜÀhÉ\rnq»6ð4ÔÛ,\u0006\u0010¡¿ã5V,>4\u0087×Ýú\u0017XjË\u0018¦Ü\u0093\u0090×\u001d¸\u009a\u0007\u0082©¼k\u000b\u001dêx1\u0087ãÀ²äÉ\u0018N#rÂüa©K¨M\u0082\u001c\u009e§_\u0015\u0012??\t1\u009cd\u0006\b\u009b³J\u0085,\u0017\tê\b|fG¾5\u009f\u0000\u0001\b\u0087\u0018\u0098®S\u008cò\\\u0010qA\u009eêØ\u0011U\u0089 =\u001còtÝ\u0091\u0001\u0010\u0089h ãlGÆ;ÍÛ=Ü(Ù\\\u009f\u0010ùU9¥\u0010\u0099ì¨{¬R\u0092¢½|ç\b\u009cb»r,\u008a\u0099å\u0010Ý}\u0087òð87g\u0099îÎö\u000e¤|\u0010\u0018Ï`>ô\u0004\u009bµ)P\u009e,ÛàÔèV\u007f¾¬`gGrR\u0010T\u0006£Õ¨¨Dt\u0011CµÄ\u00104¹I\u0010A\u0018«\u001cõq#ÝÜ£N\fÍ'\u0019_\u0010\u0002×í\u0080#ß\u009be]?yá]%¾»\u0010fÉeai5\u000f\u001e|@éØl§/w\u0010pç~îÎ¥ËÏ²\u0011}\r\u0083¦^S\b-\u00adÒ(\u0092>µl\u0010Cî\u000b7\u00ad\u0087Ý\u00031\u0082z\u0087¸xXÌ\u0010®>!h+HõA¼ÏÞàG\u008e·\u008d\b\u009f\u001e/\u0003»v1=\u0010?\u0095×\u001e8\u0018\u0087|o\u0080\u001aÿê>ïw\u00108U½ÂÅ\u0018\u001e\n¸\u000f½\u000bÞ<\u009e°\u0010ºØLû7\u0018XCàË\u000fp\u0004¼\u001c\u009e\u0010\u001b^M\u0010\u0005K;iùt\u0097ë\u001bÈ½ë\u0010º\u0096dâ\u000b½Ô\u000f3\u008e0\u009f=KÉÊ\b\u0017ã wç©v\u0018\bÈ¬w.Z\u009f\t¥\bÄh8¸µ\u009elD\b&w\u0011xû«sJ\u0010ìw4ð\u00172|\u001djÞÏó4\u001f]õ\u0018\u0013@ñ\u009a\u0080êQ6(K\u0086d\u008dvYSõ Ù\"h~ØØ\u0018ãV4\u000eÄéÆ\u0085\u001f\u008càM\u000e\u0085Ð¿.'»Qäwè\u0004\u0010\u008dU©>\u0096\u008aÇü¥YP\fèî\u0001E\bH¤)¡0\u009cHë\u0010õô\u001b[¯µ´´ñG\u0084ýÀÅÔ\u009c\u0010¤¨Â?\u00ad\u001c\u0016¡\u0002û}QksÍ:\u0010&\u008bÍ~#ã¿¯\u0002õ\n\u008c\u008c\b'\u0013\u0010Fú\u008ekl6är3õúõÞò£x\u0010\u0088æ§Ôu£S\u001af\u001fp÷¹þÇ)\u0010\u0011\u0093\u000b¾e\u0088»Ù \u0013ã[&J\u008aq\bY\u009b¬\u009f·\u0001$_\b¤\u000f\u000f\u0081ñ¦\u0086i\bñà¶Öç÷ãÄ\u0018qA\u009eêØ\u0011U\u0089\u001a\u0095¦\u0013\b±\u0013\u0090<¯\n×@òñ)\u0010_ùôpºá\u009c\u0080<h\u001c*æøIo\b,³z\u0093\u0001\u009cní\u0010è»\u0089îÝa\u0096Æy\u00adoç\u0090\u0082\u000ej\u0010Ïä[¹JF|\u001f\u00076«\u0015\\\u0001´M\u0010È\bÉ\u0095qgKßtð\u00ad\u0092Ø\u0089±®\u0010m`ù6º¯bí`È°}Ø´¼º\u0010Ý}\u0087òð87gk/ÐÅß?òé\u0010\u0098Pz¾eÿ\u009aÕfD\n5\u0003\u0006Ì\u008b\u0010Ï`>ô\u0004\u009bµ)®\u0017\u0089Ó\u0087\u0090GA\u0010Ú\u0086¥rfÙ±\u00052M\u009fZ\u00ad\u0087\u001e¢\u00105Æ\b½\u000fgÅåk\u001b\u0010Pn\u000f¸Y ¤º\u0091ñtÌn+>\nùIT\u000eýË¸QhÇáÅNeÆI\u0017\u0002»á\u000bÀ\bíÔF4M\u0014c\u001e\b\u008eiý8½¦o·\u0018©êTÎgãE1º\u0001t\u0098U?µ¨\u0015ß+ò¾$\u008eQ\b¥c\u001eY÷ò\u0012R\u0018ð*Y,¹§Û\u008dÑ@¨}\u008dÞE\u0093'ý\u0086å\u0012\u008d+Ð\u0010£\u009aÀ_\u008cù;7]å\u0000Ån§\t\t\u0018pç~îÎ¥ËÏ\u0016\u009cÒ'>û$\u0094\u0010\u0086\u009fóZk{\u0016\u0010Æ\u0010\u0098CHÕréD\u001dÕ\u009bï?\u0004å\u0010\u0010\u001c\u0088Y\u0095A\u0092\u0084\u001f\u0081æ+\u0017\u0096e³\u0010\u0098Ô\\z{\u0018q\u0089\u009d©\u000f\u0088!\u0081´\u001b\u0010d¦wÉ\u00817ÉÜl³w·uwÜÆ\u0010:Ü\u0080)²ð\nª¤\u009ccÝ\u0083nM=\u0010\r?ã\u009fÖ\u009cæ\u0093\"Rt\u0096lÊ>\u0092\b\u0098\u0004\u000e£\u009e´°Ì\bÿ\u0081Û\u008d{\u0084zµ\b44&\u0015yNÍ¸\b.\u00adL\u000b\u0017\u008d³ô\b9\u0080\u000bE¤¤E\u0090\u0018©êTÎgãE1ÿXG1µ\u0080¬\nú6]´ÆÄÌz\u0018î\rF7\u0081§_T\u0081Ê\u001f*j{Æ\"êà\u009cØ\u008a\u0083¾\u0005\b\u0092 kPÉX2v\b\"5a#48¼þ\u0010ÿ1nÍ>wèîÇ WëÃpQ\u0093\u0010ø×Y{\u0082ÄN\u009dn\u008d\u0097ç\u00ad\u007fÐ\u0007\u0010\u008a¤\u0004]? 1®8óq0©-\\7\u0010ãV4\u000eÄéÆ\u0085¨XÞ>½!=w\u0010ÏÛV\u0000É»Û6CÕ\u001cR?¾\u0097\u0011\u0010\u001f§KÕ\u0081D9 JeÂãWä¥C\u0010\u000f}\u009b¿Z\u009aR\u0084ïÚ³#ÿìq\u000e\u0010\u008ea\\éH\u0096\u0089~¼Xðtmñ@s\u0010\u0090iº¿ðüÁfÎíO!/¥\u00ad¤\u0010<\u0018·b\u0083ÍÇ'\u0080K/s\u0095\u0098\u001e\b\u0010Õ\u009c\u0000wQ0º\n\u008bÂ¤K¶Ä/=\u0010\u0002\u0080ÊÂ\u0098H\u0012+é8\u0003\u0091Õ\u0019\u001c\u000b\bÒ\u009dÂ@=Þ^s\bé]ÿ\u0015\u0093,¤ï\u0018¤º\u0091ñtÌn+>\nùIT\u000eýË7éo]ºô!¡\u0018n]Ô±_\u001c¥_\u008b7\u008aß³À$\u0081\u0083/\bØ\u009f2ñ\u008d\u0010ª+\u000f\u008c¨¸o\u009eo\u0095ÚôB3ÍÄ\u0010\u0012+\u0000\u009fD\u009aãÏ7\u0096\n\u0087¼¹£p\u0010U¤ø\u0010\u0011¤¢×óò\u0080´\u009f\u000f\u001c\u0017\b°0Ïø&\u0000:½\u0010\u0089Hô¿\u0092â\u0088u$\u0095;Ï\u0092Õ\u009aé\bñãyÛ>ÀÞ\u001c\b±â\"ýêÆøk\u0010\u0088Ümb2\u009fJ«\u0098Ï¢\u009aEØiÜ\u0010\u0004ûö\u0098G\u0001ÒoÿM+eÂ7\bj\u0010{\u0010\u0001\u0016õù\u0093^ëXäsG\u0015\fW\u0010ØòËëiÞ'\u008al`\r\u009b\u001e\u008cp¾\u0010õwýî 4ó\u0091kØ ¶ Í3r\u0010ã#Wm\u0099°Jf'Nílw\u0099Yý\b4µÛó2\u000e§\u0097\u0010n]Ô±_\u001c¥_kÕØá\u0017,Ù\u0001\bow\u0012\u009b!è\u0090à";
      int var9 = "\u0005òù\u0084\u0016o}½Sà9s\u0084þ\u0080\u001c\u0010H\u000fó2\n\u008b\u001f]\u00ad\u0097º\u0015®\u0092×Î\u0010ß\u0092\u0091\u008aÍm±\u0016\u0014Q\u000e=\u0003y\u0081©\u0010åV\u0094Ø\u0013\u0016f¡×ÉÄúU¦!z\bñj\u008dÁê\u0099\u009bº\b.y,Äj\u0007_\u009f\b¡\u009bÝ\u0094\u0002@/>\u0010Â\u001d¼ \u0092à«Ëú\u0002è+¿Ø\u0088\u0012\u0018\u0098Ô\\z{\u0018q\u0089ôxiÀ\u0000Ûr4\u001496\u0089C\u00021é\u0010Ï`>ô\u0004\u009bµ)xcÒÍUý¸L\u00109âLóQY\u0083³ßpî6\u009cIZå\bCÿ\u0002R\u009a4\u0086©\b\u008bµ;Kù»»l\u0010vgDÿ1\u0010s\u001d_,kïK\u001asv\b\u0019CÂ§Ñ\u009c\u0096Ò\u0010º¯ÊG\\:&ÀG ê.L`~\u0092\u0010Dï\u0004Òºdn/`òéé\u00889\u008b!\b¼Ôå8-\u0014À_\u0010uEz\u008a\u0016\u0095éMÇCBP??\u0089&\b×t*\u00886\u0080æ\u0003\b\u0097T-\u008e\u00910\u0018b\u0010ë\u0096ªàc\u0017·«^u\u007fìQÝJø\u0010Ý}\u0087òð87gìþù\u00065%\u000f\u009e\u0018\u0085÷O=ÿª\u0085Ë5ÑÌ\fPÃ}\u001b¶\u0088\u0088Í\u009e¸Öp\bÎ%9ð\u0013û\u0084\u0007\u0010lÜÀhÉ\rnq»6ð4ÔÛ,\u0006\u0010¡¿ã5V,>4\u0087×Ýú\u0017XjË\u0018¦Ü\u0093\u0090×\u001d¸\u009a\u0007\u0082©¼k\u000b\u001dêx1\u0087ãÀ²äÉ\u0018N#rÂüa©K¨M\u0082\u001c\u009e§_\u0015\u0012??\t1\u009cd\u0006\b\u009b³J\u0085,\u0017\tê\b|fG¾5\u009f\u0000\u0001\b\u0087\u0018\u0098®S\u008cò\\\u0010qA\u009eêØ\u0011U\u0089 =\u001còtÝ\u0091\u0001\u0010\u0089h ãlGÆ;ÍÛ=Ü(Ù\\\u009f\u0010ùU9¥\u0010\u0099ì¨{¬R\u0092¢½|ç\b\u009cb»r,\u008a\u0099å\u0010Ý}\u0087òð87g\u0099îÎö\u000e¤|\u0010\u0018Ï`>ô\u0004\u009bµ)P\u009e,ÛàÔèV\u007f¾¬`gGrR\u0010T\u0006£Õ¨¨Dt\u0011CµÄ\u00104¹I\u0010A\u0018«\u001cõq#ÝÜ£N\fÍ'\u0019_\u0010\u0002×í\u0080#ß\u009be]?yá]%¾»\u0010fÉeai5\u000f\u001e|@éØl§/w\u0010pç~îÎ¥ËÏ²\u0011}\r\u0083¦^S\b-\u00adÒ(\u0092>µl\u0010Cî\u000b7\u00ad\u0087Ý\u00031\u0082z\u0087¸xXÌ\u0010®>!h+HõA¼ÏÞàG\u008e·\u008d\b\u009f\u001e/\u0003»v1=\u0010?\u0095×\u001e8\u0018\u0087|o\u0080\u001aÿê>ïw\u00108U½ÂÅ\u0018\u001e\n¸\u000f½\u000bÞ<\u009e°\u0010ºØLû7\u0018XCàË\u000fp\u0004¼\u001c\u009e\u0010\u001b^M\u0010\u0005K;iùt\u0097ë\u001bÈ½ë\u0010º\u0096dâ\u000b½Ô\u000f3\u008e0\u009f=KÉÊ\b\u0017ã wç©v\u0018\bÈ¬w.Z\u009f\t¥\bÄh8¸µ\u009elD\b&w\u0011xû«sJ\u0010ìw4ð\u00172|\u001djÞÏó4\u001f]õ\u0018\u0013@ñ\u009a\u0080êQ6(K\u0086d\u008dvYSõ Ù\"h~ØØ\u0018ãV4\u000eÄéÆ\u0085\u001f\u008càM\u000e\u0085Ð¿.'»Qäwè\u0004\u0010\u008dU©>\u0096\u008aÇü¥YP\fèî\u0001E\bH¤)¡0\u009cHë\u0010õô\u001b[¯µ´´ñG\u0084ýÀÅÔ\u009c\u0010¤¨Â?\u00ad\u001c\u0016¡\u0002û}QksÍ:\u0010&\u008bÍ~#ã¿¯\u0002õ\n\u008c\u008c\b'\u0013\u0010Fú\u008ekl6är3õúõÞò£x\u0010\u0088æ§Ôu£S\u001af\u001fp÷¹þÇ)\u0010\u0011\u0093\u000b¾e\u0088»Ù \u0013ã[&J\u008aq\bY\u009b¬\u009f·\u0001$_\b¤\u000f\u000f\u0081ñ¦\u0086i\bñà¶Öç÷ãÄ\u0018qA\u009eêØ\u0011U\u0089\u001a\u0095¦\u0013\b±\u0013\u0090<¯\n×@òñ)\u0010_ùôpºá\u009c\u0080<h\u001c*æøIo\b,³z\u0093\u0001\u009cní\u0010è»\u0089îÝa\u0096Æy\u00adoç\u0090\u0082\u000ej\u0010Ïä[¹JF|\u001f\u00076«\u0015\\\u0001´M\u0010È\bÉ\u0095qgKßtð\u00ad\u0092Ø\u0089±®\u0010m`ù6º¯bí`È°}Ø´¼º\u0010Ý}\u0087òð87gk/ÐÅß?òé\u0010\u0098Pz¾eÿ\u009aÕfD\n5\u0003\u0006Ì\u008b\u0010Ï`>ô\u0004\u009bµ)®\u0017\u0089Ó\u0087\u0090GA\u0010Ú\u0086¥rfÙ±\u00052M\u009fZ\u00ad\u0087\u001e¢\u00105Æ\b½\u000fgÅåk\u001b\u0010Pn\u000f¸Y ¤º\u0091ñtÌn+>\nùIT\u000eýË¸QhÇáÅNeÆI\u0017\u0002»á\u000bÀ\bíÔF4M\u0014c\u001e\b\u008eiý8½¦o·\u0018©êTÎgãE1º\u0001t\u0098U?µ¨\u0015ß+ò¾$\u008eQ\b¥c\u001eY÷ò\u0012R\u0018ð*Y,¹§Û\u008dÑ@¨}\u008dÞE\u0093'ý\u0086å\u0012\u008d+Ð\u0010£\u009aÀ_\u008cù;7]å\u0000Ån§\t\t\u0018pç~îÎ¥ËÏ\u0016\u009cÒ'>û$\u0094\u0010\u0086\u009fóZk{\u0016\u0010Æ\u0010\u0098CHÕréD\u001dÕ\u009bï?\u0004å\u0010\u0010\u001c\u0088Y\u0095A\u0092\u0084\u001f\u0081æ+\u0017\u0096e³\u0010\u0098Ô\\z{\u0018q\u0089\u009d©\u000f\u0088!\u0081´\u001b\u0010d¦wÉ\u00817ÉÜl³w·uwÜÆ\u0010:Ü\u0080)²ð\nª¤\u009ccÝ\u0083nM=\u0010\r?ã\u009fÖ\u009cæ\u0093\"Rt\u0096lÊ>\u0092\b\u0098\u0004\u000e£\u009e´°Ì\bÿ\u0081Û\u008d{\u0084zµ\b44&\u0015yNÍ¸\b.\u00adL\u000b\u0017\u008d³ô\b9\u0080\u000bE¤¤E\u0090\u0018©êTÎgãE1ÿXG1µ\u0080¬\nú6]´ÆÄÌz\u0018î\rF7\u0081§_T\u0081Ê\u001f*j{Æ\"êà\u009cØ\u008a\u0083¾\u0005\b\u0092 kPÉX2v\b\"5a#48¼þ\u0010ÿ1nÍ>wèîÇ WëÃpQ\u0093\u0010ø×Y{\u0082ÄN\u009dn\u008d\u0097ç\u00ad\u007fÐ\u0007\u0010\u008a¤\u0004]? 1®8óq0©-\\7\u0010ãV4\u000eÄéÆ\u0085¨XÞ>½!=w\u0010ÏÛV\u0000É»Û6CÕ\u001cR?¾\u0097\u0011\u0010\u001f§KÕ\u0081D9 JeÂãWä¥C\u0010\u000f}\u009b¿Z\u009aR\u0084ïÚ³#ÿìq\u000e\u0010\u008ea\\éH\u0096\u0089~¼Xðtmñ@s\u0010\u0090iº¿ðüÁfÎíO!/¥\u00ad¤\u0010<\u0018·b\u0083ÍÇ'\u0080K/s\u0095\u0098\u001e\b\u0010Õ\u009c\u0000wQ0º\n\u008bÂ¤K¶Ä/=\u0010\u0002\u0080ÊÂ\u0098H\u0012+é8\u0003\u0091Õ\u0019\u001c\u000b\bÒ\u009dÂ@=Þ^s\bé]ÿ\u0015\u0093,¤ï\u0018¤º\u0091ñtÌn+>\nùIT\u000eýË7éo]ºô!¡\u0018n]Ô±_\u001c¥_\u008b7\u008aß³À$\u0081\u0083/\bØ\u009f2ñ\u008d\u0010ª+\u000f\u008c¨¸o\u009eo\u0095ÚôB3ÍÄ\u0010\u0012+\u0000\u009fD\u009aãÏ7\u0096\n\u0087¼¹£p\u0010U¤ø\u0010\u0011¤¢×óò\u0080´\u009f\u000f\u001c\u0017\b°0Ïø&\u0000:½\u0010\u0089Hô¿\u0092â\u0088u$\u0095;Ï\u0092Õ\u009aé\bñãyÛ>ÀÞ\u001c\b±â\"ýêÆøk\u0010\u0088Ümb2\u009fJ«\u0098Ï¢\u009aEØiÜ\u0010\u0004ûö\u0098G\u0001ÒoÿM+eÂ7\bj\u0010{\u0010\u0001\u0016õù\u0093^ëXäsG\u0015\fW\u0010ØòËëiÞ'\u008al`\r\u009b\u001e\u008cp¾\u0010õwýî 4ó\u0091kØ ¶ Í3r\u0010ã#Wm\u0099°Jf'Nílw\u0099Yý\b4µÛó2\u000e§\u0097\u0010n]Ô±_\u001c¥_kÕØá\u0017,Ù\u0001\bow\u0012\u009b!è\u0090à".length();
      char var6 = 16;
      int var5 = -1;

      label27:
      while(true) {
         ++var5;
         String var12 = var7.substring(var5, var5 + var6);
         byte var10001 = -1;

         while(true) {
            byte[] var11 = var3.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = a(var11).intern();
            switch(var10001) {
            case 0:
               var10[var8++] = var16;
               if ((var5 += var6) >= var9) {
                  TPS = new Vulcan_in(var10[30], 0, Vulcan_in::lambda$static$0);
                  CHUNK = new Vulcan_in(var10[103], 1, Vulcan_in::lambda$static$1);
                  JOINED = new Vulcan_in(var10[124], 2, Vulcan_in::lambda$static$2);
                  CAKE = new Vulcan_in(var10[126], 3, Vulcan_in::lambda$static$3);
                  BAMBOO = new Vulcan_in(var10[29], 4, Vulcan_in::lambda$static$4);
                  HAPPY_GHAST = new Vulcan_in(var10[130], 5, Vulcan_in::lambda$static$5);
                  PACKET_EXPLOSION = new Vulcan_in(var10[57], 6, Vulcan_in::lambda$static$6);
                  ENTITY_CRAM_FIX = new Vulcan_in(var10[135], 7, Vulcan_in::lambda$static$7);
                  AROUND_SLIME = new Vulcan_in(var10[65], 8, Vulcan_in::lambda$static$8);
                  SIGN = new Vulcan_in(var10[54], 9, Vulcan_in::lambda$static$9);
                  PRESSURE_PLATE = new Vulcan_in(var10[7], 10, Vulcan_in::lambda$static$10);
                  SWIMMING = new Vulcan_in(var10[9], 11, Vulcan_in::lambda$static$11);
                  SWIMMING_JESUS = new Vulcan_in(var10[79], 12, Vulcan_in::lambda$static$12);
                  COLLIDING_HORIZONTALLY = new Vulcan_in(var10[101], 13, Vulcan_in::lambda$static$13);
                  DRIPSTONE = new Vulcan_in(var10[90], 14, Vulcan_in::lambda$static$14);
                  HIGH_FLY_SPEED = new Vulcan_in(var10[78], 15, Vulcan_in::lambda$static$15);
                  GLITCHED_BLOCKS_ABOVE = new Vulcan_in(var10[87], 16, Vulcan_in::lambda$static$16);
                  PROJECTILE_DAMAGE = new Vulcan_in(var10[28], 17, Vulcan_in::lambda$static$17);
                  CHEST = new Vulcan_in(var10[5], 18, Vulcan_in::lambda$static$18);
                  ATTACK_DAMAGE = new Vulcan_in(var10[50], 19, Vulcan_in::lambda$static$19);
                  SKULL = new Vulcan_in(var10[20], 20, Vulcan_in::lambda$static$20);
                  EXPLOSION = new Vulcan_in(var10[40], 21, Vulcan_in::lambda$static$21);
                  STAIRS = new Vulcan_in(var10[97], 22, Vulcan_in::lambda$static$22);
                  HIGH_LEVITATION = new Vulcan_in(var10[0], 23, Vulcan_in::lambda$static$23);
                  FENCE_GATE = new Vulcan_in(var10[71], 24, Vulcan_in::lambda$static$24);
                  POWDER_SNOW = new Vulcan_in(var10[73], 25, Vulcan_in::lambda$static$25);
                  DRAGON_DAMAGE = new Vulcan_in(var10[26], 26, Vulcan_in::lambda$static$26);
                  JUMP_BOOST_RAN_OUT = new Vulcan_in(var10[58], 27, Vulcan_in::lambda$static$27);
                  LAGGED_NEAR_GROUND = new Vulcan_in(var10[119], 28, Vulcan_in::lambda$static$28);
                  SNOW = new Vulcan_in(var10[35], 29, Vulcan_in::lambda$static$29);
                  NETHERITE_ARMOR = new Vulcan_in(var10[38], 30, Vulcan_in::lambda$static$30);
                  FAST_ZERO = new Vulcan_in(var10[132], 31, Vulcan_in::lambda$static$31);
                  JOINED_CHUNK_LOAD = new Vulcan_in(var10[27], 32, Vulcan_in::lambda$static$32);
                  NEAR_SOLID = new Vulcan_in(var10[51], 33, Vulcan_in::lambda$static$33);
                  CAMPFIRE = new Vulcan_in(var10[106], 34, Vulcan_in::lambda$static$34);
                  CANCELLED_MOVE = new Vulcan_in(var10[77], 35, Vulcan_in::lambda$static$35);
                  CARPET = new Vulcan_in(var10[6], 36, Vulcan_in::lambda$static$36);
                  FLOWER_POT = new Vulcan_in(var10[41], 37, Vulcan_in::lambda$static$37);
                  EMPTIED_BUCKET = new Vulcan_in(var10[116], 38, Vulcan_in::lambda$static$38);
                  END_ROD = new Vulcan_in(var10[55], 39, Vulcan_in::lambda$static$39);
                  HOPPER = new Vulcan_in(var10[84], 40, Vulcan_in::lambda$static$40);
                  CHAIN = new Vulcan_in(var10[104], 41, Vulcan_in::lambda$static$41);
                  DOOR = new Vulcan_in(var10[52], 42, Vulcan_in::lambda$static$42);
                  PICKED_UP_ITEM = new Vulcan_in(var10[59], 43, Vulcan_in::lambda$static$43);
                  ANVIL = new Vulcan_in(var10[14], 44, Vulcan_in::lambda$static$44);
                  LAGGED_NEAR_GROUND_MODERN = new Vulcan_in(var10[82], 45, Vulcan_in::lambda$static$45);
                  FULLY_SUBMERGED = new Vulcan_in(var10[34], 46, Vulcan_in::lambda$static$46);
                  RESPAWN = new Vulcan_in(var10[43], 47, Vulcan_in::lambda$static$47);
                  ICE = new Vulcan_in(var10[67], 48, Vulcan_in::lambda$static$48);
                  BED = new Vulcan_in(var10[72], 49, Vulcan_in::lambda$static$49);
                  SLAB = new Vulcan_in(var10[24], 50, Vulcan_in::lambda$static$50);
                  HIGH_JUMP_BOOST = new Vulcan_in(var10[94], 51, Vulcan_in::lambda$static$51);
                  LILY_PAD = new Vulcan_in(var10[48], 52, Vulcan_in::lambda$static$52);
                  HIGH_SPEED = new Vulcan_in(var10[56], 53, Vulcan_in::lambda$static$53);
                  WALL = new Vulcan_in(var10[31], 54, Vulcan_in::lambda$static$54);
                  SWEET_BERRIES = new Vulcan_in(var10[131], 55, Vulcan_in::lambda$static$55);
                  CONDUIT = new Vulcan_in(var10[11], 56, Vulcan_in::lambda$static$56);
                  PLACED_WEB = new Vulcan_in(var10[115], 57, Vulcan_in::lambda$static$57);
                  PISTON = new Vulcan_in(var10[53], 58, Vulcan_in::lambda$static$58);
                  CHORUS_FRUIT = new Vulcan_in(var10[47], 59, Vulcan_in::lambda$static$59);
                  ENDER_PEARL = new Vulcan_in(var10[112], 60, Vulcan_in::lambda$static$60);
                  NEAR_GROUND = new Vulcan_in(var10[74], 61, Vulcan_in::lambda$static$61);
                  SLEEPING = new Vulcan_in(var10[88], 62, Vulcan_in::lambda$static$62);
                  SOUL_SPEED = new Vulcan_in(var10[44], 63, Vulcan_in::lambda$static$63);
                  TRAPDOOR = new Vulcan_in(var10[49], 64, Vulcan_in::lambda$static$64);
                  FISHING_ROD = new Vulcan_in(var10[76], 65, Vulcan_in::lambda$static$65);
                  PLUGIN_LOAD = new Vulcan_in(var10[107], 66, Vulcan_in::lambda$static$66);
                  RIPTIDE = new Vulcan_in(var10[68], 67, Vulcan_in::lambda$static$67);
                  FIREWORK = new Vulcan_in(var10[39], 68, Vulcan_in::lambda$static$68);
                  SPECTATOR = new Vulcan_in(var10[15], 69, Vulcan_in::lambda$static$69);
                  CAULDRON = new Vulcan_in(var10[18], 70, Vulcan_in::lambda$static$70);
                  ENTITY_COLLISION = new Vulcan_in(var10[120], 71, Vulcan_in::lambda$static$71);
                  DEATH = new Vulcan_in(var10[134], 72, Vulcan_in::lambda$static$72);
                  DEPTH_STRIDER = new Vulcan_in(var10[13], 73, Vulcan_in::lambda$static$73);
                  LEVITATION = new Vulcan_in(var10[1], 74, Vulcan_in::lambda$static$74);
                  FROZEN = new Vulcan_in(var10[117], 75, Vulcan_in::lambda$static$75);
                  DIGGING = new Vulcan_in(var10[19], 76, Vulcan_in::lambda$static$76);
                  BLOCK_BREAK = new Vulcan_in(var10[109], 77, Vulcan_in::lambda$static$77);
                  PLACING = new Vulcan_in(var10[96], 78, Vulcan_in::lambda$static$78);
                  SERVER_POSITION = new Vulcan_in(var10[92], 79, Vulcan_in::lambda$static$79);
                  SERVER_POSITION_FAST = new Vulcan_in(var10[8], 80, Vulcan_in::lambda$static$80);
                  CANCELLED_PLACE = new Vulcan_in(var10[36], 81, Vulcan_in::lambda$static$81);
                  BLOCK_PLACE = new Vulcan_in(var10[32], 82, Vulcan_in::lambda$static$82);
                  BLOCK_PLACE_FAST = new Vulcan_in(var10[70], 83, Vulcan_in::lambda$static$83);
                  SWIMMING_ON_OLD_VERSION = new Vulcan_in(var10[37], 84, Vulcan_in::lambda$static$84);
                  FULLY_STUCK = new Vulcan_in(var10[105], 85, Vulcan_in::lambda$static$85);
                  PLACED_CLIMBABLE = new Vulcan_in(var10[102], 86, Vulcan_in::lambda$static$86);
                  PARTIALLY_STUCK = new Vulcan_in(var10[33], 87, Vulcan_in::lambda$static$87);
                  NOT_MOVING = new Vulcan_in(var10[45], 88, Vulcan_in::lambda$static$88);
                  PLACED_SLIME = new Vulcan_in(var10[2], 89, Vulcan_in::lambda$static$89);
                  FIREBALL = new Vulcan_in(var10[21], 90, Vulcan_in::lambda$static$90);
                  WORLD_CHANGE = new Vulcan_in(var10[128], 91, Vulcan_in::lambda$static$91);
                  FALL_DAMAGE = new Vulcan_in(var10[80], 92, Vulcan_in::lambda$static$92);
                  ILLEGAL_BLOCK = new Vulcan_in(var10[63], 93, Vulcan_in::lambda$static$93);
                  HONEY = new Vulcan_in(var10[4], 94, Vulcan_in::lambda$static$94);
                  SCAFFOLDING = new Vulcan_in(var10[75], 95, Vulcan_in::lambda$static$95);
                  HALF_BLOCK = new Vulcan_in(var10[123], 96, Vulcan_in::lambda$static$96);
                  SHULKER = new Vulcan_in(var10[98], 97, Vulcan_in::lambda$static$97);
                  GLIDING = new Vulcan_in(var10[69], 98, Vulcan_in::lambda$static$98);
                  ELYTRA = new Vulcan_in(var10[118], 99, Vulcan_in::lambda$static$99);
                  VELOCITY = new Vulcan_in(var10[110], 100, Vulcan_in::lambda$static$100);
                  DEAD = new Vulcan_in(var10[83], 101, Vulcan_in::lambda$static$101);
                  WEB = new Vulcan_in(var10[127], 102, Vulcan_in::lambda$static$102);
                  SOUL_SAND = new Vulcan_in(var10[61], 103, Vulcan_in::lambda$static$103);
                  CINEMATIC = new Vulcan_in(var10[16], 104, Vulcan_in::lambda$static$104);
                  FAST = new Vulcan_in(var10[46], 105, Vulcan_in::lambda$static$105);
                  LENIENT_SCAFFOLDING = new Vulcan_in(var10[23], 106, Vulcan_in::lambda$static$106);
                  WINDOW_CLICK = new Vulcan_in(var10[114], 107, Vulcan_in::lambda$static$107);
                  DROPPED_ITEM = new Vulcan_in(var10[25], 108, Vulcan_in::lambda$static$108);
                  CANCELLED_BREAK = new Vulcan_in(var10[22], 109, Vulcan_in::lambda$static$109);
                  MYTHIC_MOB = new Vulcan_in(var10[64], 110, Vulcan_in::lambda$static$110);
                  CREATIVE = new Vulcan_in(var10[125], 111, Vulcan_in::lambda$static$111);
                  AUTOCLICKER_NON_DIG = new Vulcan_in(var10[89], 112, Vulcan_in::lambda$static$112);
                  AUTOCLICKER = new Vulcan_in(var10[42], 113, Vulcan_in::lambda$static$113);
                  COLLIDING_VERTICALLY = new Vulcan_in(var10[85], 114, Vulcan_in::lambda$static$114);
                  SLOW_FALLING = new Vulcan_in(var10[111], 115, Vulcan_in::lambda$static$115);
                  BUBBLE_COLUMN = new Vulcan_in(var10[121], 116, Vulcan_in::lambda$static$116);
                  FENCE = new Vulcan_in(var10[99], 117, Vulcan_in::lambda$static$117);
                  JUMP_BOOST = new Vulcan_in(var10[108], 118, Vulcan_in::lambda$static$118);
                  FLIGHT = new Vulcan_in(var10[136], 119, Vulcan_in::lambda$static$119);
                  COMBO_MODE = new Vulcan_in(var10[3], 120, Vulcan_in::lambda$static$120);
                  DOLPHINS_GRACE = new Vulcan_in(var10[10], 121, Vulcan_in::lambda$static$121);
                  TELEPORT = new Vulcan_in(var10[81], 122, Vulcan_in::lambda$static$122);
                  SERVER_VERSION = new Vulcan_in(var10[122], 123, Vulcan_in::lambda$static$123);
                  KELP = new Vulcan_in(var10[60], 124, Vulcan_in::lambda$static$124);
                  CLIENT_VERSION = new Vulcan_in(var10[95], 125, Vulcan_in::lambda$static$125);
                  SLIME = new Vulcan_in(var10[86], 126, Vulcan_in::lambda$static$126);
                  BOAT = new Vulcan_in(var10[100], 127, Vulcan_in::lambda$static$127);
                  SEA_PICKLE = new Vulcan_in(var10[66], 128, Vulcan_in::lambda$static$128);
                  SEAGRASS = new Vulcan_in(var10[137], 129, Vulcan_in::lambda$static$129);
                  TURTLE_EGG = new Vulcan_in(var10[129], 130, Vulcan_in::lambda$static$130);
                  SHULKER_BOX = new Vulcan_in(var10[133], 131, Vulcan_in::lambda$static$131);
                  WATERLOGGED = new Vulcan_in(var10[93], 132, Vulcan_in::lambda$static$132);
                  CLIMBABLE = new Vulcan_in(var10[113], 133, Vulcan_in::lambda$static$133);
                  LIQUID = new Vulcan_in(var10[138], 134, Vulcan_in::lambda$static$134);
                  GLASS_PANE = new Vulcan_in(var10[62], 135, Vulcan_in::lambda$static$135);
                  FARMLAND = new Vulcan_in(var10[91], 136, Vulcan_in::lambda$static$136);
                  VEHICLE = new Vulcan_in(var10[12], 137, Vulcan_in::lambda$static$137);
                  VOID = new Vulcan_in(var10[17], 138, Vulcan_in::lambda$static$138);
                  Vulcan_E = new Vulcan_in[]{TPS, CHUNK, JOINED, CAKE, BAMBOO, HAPPY_GHAST, PACKET_EXPLOSION, ENTITY_CRAM_FIX, AROUND_SLIME, SIGN, PRESSURE_PLATE, SWIMMING, SWIMMING_JESUS, COLLIDING_HORIZONTALLY, DRIPSTONE, HIGH_FLY_SPEED, GLITCHED_BLOCKS_ABOVE, PROJECTILE_DAMAGE, CHEST, ATTACK_DAMAGE, SKULL, EXPLOSION, STAIRS, HIGH_LEVITATION, FENCE_GATE, POWDER_SNOW, DRAGON_DAMAGE, JUMP_BOOST_RAN_OUT, LAGGED_NEAR_GROUND, SNOW, NETHERITE_ARMOR, FAST_ZERO, JOINED_CHUNK_LOAD, NEAR_SOLID, CAMPFIRE, CANCELLED_MOVE, CARPET, FLOWER_POT, EMPTIED_BUCKET, END_ROD, HOPPER, CHAIN, DOOR, PICKED_UP_ITEM, ANVIL, LAGGED_NEAR_GROUND_MODERN, FULLY_SUBMERGED, RESPAWN, ICE, BED, SLAB, HIGH_JUMP_BOOST, LILY_PAD, HIGH_SPEED, WALL, SWEET_BERRIES, CONDUIT, PLACED_WEB, PISTON, CHORUS_FRUIT, ENDER_PEARL, NEAR_GROUND, SLEEPING, SOUL_SPEED, TRAPDOOR, FISHING_ROD, PLUGIN_LOAD, RIPTIDE, FIREWORK, SPECTATOR, CAULDRON, ENTITY_COLLISION, DEATH, DEPTH_STRIDER, LEVITATION, FROZEN, DIGGING, BLOCK_BREAK, PLACING, SERVER_POSITION, SERVER_POSITION_FAST, CANCELLED_PLACE, BLOCK_PLACE, BLOCK_PLACE_FAST, SWIMMING_ON_OLD_VERSION, FULLY_STUCK, PLACED_CLIMBABLE, PARTIALLY_STUCK, NOT_MOVING, PLACED_SLIME, FIREBALL, WORLD_CHANGE, FALL_DAMAGE, ILLEGAL_BLOCK, HONEY, SCAFFOLDING, HALF_BLOCK, SHULKER, GLIDING, ELYTRA, VELOCITY, DEAD, WEB, SOUL_SAND, CINEMATIC, FAST, LENIENT_SCAFFOLDING, WINDOW_CLICK, DROPPED_ITEM, CANCELLED_BREAK, MYTHIC_MOB, CREATIVE, AUTOCLICKER_NON_DIG, AUTOCLICKER, COLLIDING_VERTICALLY, SLOW_FALLING, BUBBLE_COLUMN, FENCE, JUMP_BOOST, FLIGHT, COMBO_MODE, DOLPHINS_GRACE, TELEPORT, SERVER_VERSION, KELP, CLIENT_VERSION, SLIME, BOAT, SEA_PICKLE, SEAGRASS, TURTLE_EGG, SHULKER_BOX, WATERLOGGED, CLIMBABLE, LIQUID, GLASS_PANE, FARMLAND, VEHICLE, VOID};
                  return;
               }

               var6 = var7.charAt(var5);
               break;
            default:
               var10[var8++] = var16;
               if ((var5 += var6) < var9) {
                  var6 = var7.charAt(var5);
                  continue label27;
               }

               var7 = "ñªf«Î'RðÈ=º(~\u0007\r\u009b\b4Sî\u0094\u0001ÒÝÇ";
               var9 = "ñªf«Î'RðÈ=º(~\u0007\r\u009b\b4Sî\u0094\u0001ÒÝÇ".length();
               var6 = 16;
               var5 = -1;
            }

            ++var5;
            var12 = var7.substring(var5, var5 + var6);
            var10001 = 0;
         }
      }
   }

   public static void Vulcan_s(AbstractCheck[] var0) {
      Vulcan_I = var0;
   }

   public static AbstractCheck[] Vulcan_G() {
      return Vulcan_I;
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
