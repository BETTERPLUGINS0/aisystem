package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Vulcan_Xx implements Listener {
   public final UUID Vulcan_b;
   private static boolean Vulcan_O;
   private static final long a = Vulcan_n.a(-9187083365121858623L, -5503777979396910596L, MethodHandles.lookup().lookupClass()).a(24537963222288L);
   private static final String[] b;

   public Vulcan_Xx() {
      this.Vulcan_b = UUID.fromString(b[4]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_m(EntitySpawnEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_I(BlockPlaceEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void Vulcan_l(BlockBreakEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_X(PlayerPickupItemEvent var1) {
      long var2 = a ^ 98690931085492L;
      boolean var10000 = Vulcan_Z();
      Player var5 = var1.getPlayer();
      boolean var4 = var10000;
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (!var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_J(new Object[0]).Vulcan_P9(new Object[0]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_s(PlayerItemConsumeEvent var1) {
      long var2 = a ^ 36021997420467L;
      Player var4 = var1.getPlayer();
      Vulcan_iE var5 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var4});

      try {
         if (var5 == null) {
            return;
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      try {
         if (var1.getItem().toString().contains(b[3])) {
            var5.Vulcan_J(new Object[0]).Vulcan_C(new Object[0]);
         }

      } catch (RuntimeException var6) {
         throw a(var6);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_W(PlayerBedEnterEvent var1) {
      long var2 = a ^ 101185608403152L;
      Player var5 = var1.getPlayer();
      boolean var10000 = Vulcan_Z();
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});
      boolean var4 = var10000;

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (!var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_J(new Object[0]).Vulcan_PZ(new Object[0]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_M(PlayerBedLeaveEvent var1) {
      long var2 = a ^ 128666980448623L;
      boolean var10000 = Vulcan_a();
      Player var5 = var1.getPlayer();
      boolean var4 = var10000;
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_J(new Object[0]).Vulcan_PS(new Object[0]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_h(PlayerInteractEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_l(PlayerDeathEvent var1) {
      long var2 = a ^ 117020743155377L;
      boolean var10000 = Vulcan_a();
      Player var5 = var1.getEntity();
      boolean var4 = var10000;
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_J(new Object[0]).Vulcan_P3(new Object[0]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_M(EntityDamageEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_K(PlayerInteractEntityEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_o(ProjectileLaunchEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void Vulcan_N(PlayerTeleportEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_f(PlayerChangedWorldEvent var1) {
      long var2 = a ^ 135506902768870L;
      Player var5 = var1.getPlayer();
      boolean var10000 = Vulcan_a();
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});
      boolean var4 = var10000;

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_J(new Object[0]).Vulcan_g(new Object[0]);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_E(PlayerFishEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_J(EntityDamageByEntityEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan_I(PlayerMoveEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler
   public void Vulcan_q(PlayerKickEvent param1) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$onLaunch$3(ProjectileLaunchEvent var0, AbstractCheck var1) {
      long var2 = a ^ 19844338180245L;
      long var4 = var2 ^ 107842446511469L;
      var1.Vulcan_i(new Object[]{var4, var0});
   }

   private static void lambda$onInteract$2(PlayerInteractEvent var0, AbstractCheck var1) {
      long var2 = a ^ 23070881643562L;
      long var4 = var2 ^ 110925109088722L;
      var1.Vulcan_i(new Object[]{var4, var0});
   }

   private static void lambda$onBreak$1(BlockBreakEvent var0, AbstractCheck var1) {
      long var2 = a ^ 48616528008664L;
      long var4 = var2 ^ 101353492641824L;
      var1.Vulcan_i(new Object[]{var4, var0});
   }

   private static void lambda$onPlace$0(BlockPlaceEvent var0, AbstractCheck var1) {
      long var2 = a ^ 105956972423481L;
      long var4 = var2 ^ 17898735060161L;
      var1.Vulcan_i(new Object[]{var4, var0});
   }

   public static void Vulcan_m(boolean var0) {
      Vulcan_O = var0;
   }

   public static boolean Vulcan_Z() {
      return Vulcan_O;
   }

   public static boolean Vulcan_a() {
      boolean var0 = Vulcan_Z();

      try {
         return !var0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      long var0 = a ^ 82060758851248L;
      Vulcan_m(true);
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
      String var6 = "©ÔR ¹¦`ó\u0010>O\n3\u0011\b\u0007ñ^¬\u0090¬µõ\tÇ\býË\n2§í¯å\bÿùy\u0001EdÃ^(\u0094\u001d,ZÛ9\u0010h@ ê|Ãw=\u0001\u0001[\u009e×\u008d´<?Â\u0080Ý\u0016.ÉXEÌÞÂ\u0019õÊ\n0\bù*\u000e\u00045Ö d";
      int var8 = "©ÔR ¹¦`ó\u0010>O\n3\u0011\b\u0007ñ^¬\u0090¬µõ\tÇ\býË\n2§í¯å\bÿùy\u0001EdÃ^(\u0094\u001d,ZÛ9\u0010h@ ê|Ãw=\u0001\u0001[\u009e×\u008d´<?Â\u0080Ý\u0016.ÉXEÌÞÂ\u0019õÊ\n0\bù*\u000e\u00045Ö d".length();
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

               var6 = "¯\u0001ï\fÄþ\fý¡\u001fì3õ\u0012N±\b` ¸a\u001e\u000b\"\u0004";
               var8 = "¯\u0001ï\fÄþ\fý¡\u001fì3õ\u0012N±\b` ¸a\u001e\u000b\"\u0004".length();
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
