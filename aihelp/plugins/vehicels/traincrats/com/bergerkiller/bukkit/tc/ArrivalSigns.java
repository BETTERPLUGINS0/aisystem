package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.collections.BlockMap;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.sl.API.Variables;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.utils.TimeDurationFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class ArrivalSigns {
   private static HashMap<String, ArrivalSigns.TimeSign> timerSigns = new HashMap();
   private static BlockMap<ArrivalSigns.TimeCalculation> timeCalculations = new BlockMap();
   private static TimeDurationFormat timeFormat = new TimeDurationFormat("HH:mm:ss");
   private static Task updateTask;

   public static ArrivalSigns.TimeSign getTimer(String name) {
      return (ArrivalSigns.TimeSign)timerSigns.computeIfAbsent(name, (new_timesign_name) -> {
         return new ArrivalSigns.TimeSign(new_timesign_name);
      });
   }

   public static boolean isTrigger(Sign sign) {
      SignActionHeader header = SignActionHeader.parseFromSign(sign);
      return header.isValid() && Util.getCleanLine((Sign)sign, 1).equalsIgnoreCase("trigger");
   }

   public static void trigger(Sign sign, MinecartMember<?> mm) {
      if (TrainCarts.plugin.isSignLinkEnabled()) {
         String name = Util.getCleanLine((Sign)sign, 2);
         String duration = Util.getCleanLine((Sign)sign, 3);
         if (!name.isEmpty()) {
            if (mm != null) {
               Variables.get(name + 'N').set(mm.getGroup().getProperties().getDisplayName());
               if (mm.getProperties().hasDestination()) {
                  Variables.get(name + 'D').set(mm.getProperties().getDestination());
               } else {
                  Variables.get(name + 'D').set("Unknown");
               }

               double speed = MathUtil.round(mm.getRealSpeed(), 2);
               speed = Math.min(speed, mm.getGroup().getProperties().getSpeedLimit());
               Variables.get(name + 'V').set(Double.toString(speed));
            }

            ArrivalSigns.TimeSign t = getTimer(name);
            t.duration = ParseUtil.parseTime(duration);
            if (t.duration == 0L) {
               timeCalcStart(sign.getBlock(), mm);
            } else {
               t.trigger();
               t.update();
            }

         }
      }
   }

   public static void setTimeDurationFormat(String format) {
      try {
         timeFormat = new TimeDurationFormat(format);
      } catch (IllegalArgumentException var2) {
         TrainCarts.plugin.log(Level.WARNING, "Time duration format is invalid: " + format);
      }

   }

   public static void updateAll() {
      Iterator var0 = timerSigns.values().iterator();

      ArrivalSigns.TimeSign t;
      do {
         if (!var0.hasNext()) {
            return;
         }

         t = (ArrivalSigns.TimeSign)var0.next();
      } while(t.update());

   }

   public static void init(String filename) {
      FileConfiguration config = new FileConfiguration(filename);
      config.load();
      Iterator var2 = config.getKeys().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         String dur = (String)config.get(key, String.class, (Object)null);
         if (dur != null) {
            ArrivalSigns.TimeSign t = getTimer(key);
            t.duration = ParseUtil.parseTime(dur);
            t.startTime = System.currentTimeMillis();
         }
      }

   }

   public static void save(String filename) {
      FileConfiguration config = new FileConfiguration(filename);
      Iterator var2 = timerSigns.values().iterator();

      while(var2.hasNext()) {
         ArrivalSigns.TimeSign sign = (ArrivalSigns.TimeSign)var2.next();
         config.set(sign.name, sign.getDuration());
      }

      config.save();
   }

   public static void deinit() {
      timerSigns.clear();
      timerSigns = null;
      timeCalculations.clear();
      timeCalculations = null;
      if (updateTask != null && updateTask.isRunning()) {
         updateTask.stop();
      }

      updateTask = null;
   }

   public static void timeCalcStart(Block signblock, MinecartMember<?> member) {
      ArrivalSigns.TimeCalculation calc = new ArrivalSigns.TimeCalculation();
      calc.startTime = System.currentTimeMillis();
      calc.signblock = signblock;
      calc.member = member;
      Iterator var3 = calc.signblock.getWorld().getPlayers().iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         if (player.hasPermission("train.build.trigger")) {
            if (member == null) {
               player.sendMessage(ChatColor.YELLOW + "[Train Carts] Remove the power source to stop recording");
            } else {
               player.sendMessage(ChatColor.YELLOW + "[Train Carts] Stop or destroy the minecart to stop recording");
            }
         }
      }

      timeCalculations.put(calc.signblock, calc);
      if (updateTask == null) {
         updateTask = (new Task(TrainCarts.plugin) {
            public void run() {
               if (ArrivalSigns.timeCalculations.isEmpty()) {
                  this.stop();
                  ArrivalSigns.updateTask = null;
               }

               Iterator var1 = ArrivalSigns.timeCalculations.values().iterator();

               ArrivalSigns.TimeCalculation calc;
               do {
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     calc = (ArrivalSigns.TimeCalculation)var1.next();
                  } while(calc.member == null);
               } while(!calc.member.isUnloaded() && !((Minecart)((CommonMinecart)calc.member.getEntity()).getEntity()).isDead() && ((CommonMinecart)calc.member.getEntity()).isMoving());

               calc.setTime();
               ArrivalSigns.timeCalculations.remove(calc.signblock);
            }
         }).start(0L, 1L);
      }

   }

   public static void timeCalcStop(Block signblock) {
      ArrivalSigns.TimeCalculation calc = (ArrivalSigns.TimeCalculation)timeCalculations.get(signblock);
      if (calc != null && calc.member == null) {
         calc.setTime();
         timeCalculations.remove(signblock);
      }

   }

   public static class TimeSign {
      public long startTime = -1L;
      public long duration;
      private String name;

      public TimeSign(String name) {
         this.name = name;
      }

      public void trigger() {
         this.startTime = System.currentTimeMillis();
      }

      public String getName() {
         return this.name;
      }

      public String getDuration() {
         long elapsed = System.currentTimeMillis() - this.startTime;
         long remaining = this.duration - elapsed;
         if (remaining < 0L) {
            remaining = 0L;
         }

         return ArrivalSigns.timeFormat.format(remaining);
      }

      public boolean update() {
         if (!TrainCarts.plugin.isSignLinkEnabled()) {
            return false;
         } else {
            String dur = this.getDuration();
            Variables.get(this.name).set(dur);
            Variables.get(this.name + 'T').set(dur);
            if (dur.equals("00:00:00")) {
               ArrivalSigns.timerSigns.remove(this.name);
               return false;
            } else {
               return true;
            }
         }
      }
   }

   private static class TimeCalculation {
      public long startTime;
      public Block signblock;
      public MinecartMember<?> member;

      private TimeCalculation() {
         this.member = null;
      }

      public void setTime() {
         long duration = System.currentTimeMillis() - this.startTime;
         if ((Boolean)MaterialUtil.ISSIGN.get(this.signblock)) {
            Sign sign = (Sign)this.signblock.getState();
            String dur = ArrivalSigns.timeFormat.format(duration);
            sign.setLine(3, dur);
            sign.update(true);
            Iterator var5 = sign.getWorld().getPlayers().iterator();

            while(var5.hasNext()) {
               Player player = (Player)var5.next();
               if (player.hasPermission("train.build.trigger")) {
                  player.sendMessage(ChatColor.YELLOW + "[Train Carts] Trigger time of '" + sign.getLine(2) + "' set to " + dur);
               }
            }
         }

      }

      // $FF: synthetic method
      TimeCalculation(Object x0) {
         this();
      }
   }
}
