package me.casperge.realisticseasons.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.seasonevent.CustomDailyEvent;
import me.casperge.realisticseasons.seasonevent.CustomDatedEvent;
import me.casperge.realisticseasons.seasonevent.CustomWeeklyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DayChangeHandler implements Listener {
   private RealisticSeasons main;
   private final List<Season> seasons;

   public DayChangeHandler(RealisticSeasons var1) {
      this.seasons = new ArrayList(Arrays.asList(Season.FALL, Season.WINTER, Season.SUMMER, Season.SPRING));
      var1.getServer().getPluginManager().registerEvents(this, var1);
      this.main = var1;
   }

   @EventHandler
   public void dayChange(DayChangeEvent var1) {
      Date var2 = var1.getFrom();
      Date var3 = var1.getTo();
      if (this.main.getTemperatureManager().getTempData().isEnabledWorld(var1.getWorld()) && this.main.getTemperatureManager().getTempData().isEnabled()) {
         if (this.seasons.contains(this.main.getSeasonManager().getSeason(var1.getWorld()))) {
            this.main.getTemperatureManager().getTempData().setBaseTemperature(var1.getWorld(), this.main.getTemperatureManager().getTempUtils().generateNewBaseTemperature(var1.getWorld()));
         } else {
            this.main.getTemperatureManager().getTempData().setBaseTemperature(var1.getWorld(), 25);
         }
      }

      if (var2 != null && var3 != null) {
         if (this.main.getSettings().doSeasonCycle && this.main.getSeasonManager().getSeason(var1.getWorld()) != this.main.getTimeManager().getCalendar().getSeason(var3)) {
            this.main.getSeasonManager().setSeason(var1.getWorld(), this.main.getTimeManager().getCalendar().getSeason(var3), false);
         }

         Iterator var4 = this.main.getEventManager().getDailyEvents().iterator();

         while(var4.hasNext()) {
            CustomDailyEvent var5 = (CustomDailyEvent)var4.next();
            this.main.getEventManager().start(var1.getWorld(), var5, var3);
         }

         var4 = this.main.getEventManager().getWeeklyEvents().iterator();

         while(var4.hasNext()) {
            CustomWeeklyEvent var6 = (CustomWeeklyEvent)var4.next();
            if (var6.isToday(this.main.getTimeManager().getWeekDayAsInt(var3, false))) {
               this.main.getEventManager().start(var1.getWorld(), var6, var3);
            } else if (!var6.isToday(this.main.getTimeManager().getWeekDayAsInt(var3, false)) && var6.isToday(this.main.getTimeManager().getWeekDayAsInt(var2, true))) {
               this.main.getEventManager().stop(var1.getWorld(), var6, var3);
            }
         }

         var4 = this.main.getEventManager().getDatedEvents().iterator();

         while(true) {
            while(var4.hasNext()) {
               CustomDatedEvent var7 = (CustomDatedEvent)var4.next();
               if (var7.isActive(var3) && !var7.isActive(var2) && var7.hasEvent(var1.getWorld())) {
                  this.main.getEventManager().start(var1.getWorld(), var7, var3);
               } else if (!var7.isActive(var3) && var7.isActive(var2) && var7.hasEvent(var1.getWorld())) {
                  this.main.getEventManager().stop(var1.getWorld(), var7, var3);
               }
            }

            Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
               public void run() {
                  DayChangeHandler.this.main.getSeasonManager().runSubSeasonCheck();
               }
            }, 1L);
            return;
         }
      }
   }
}
