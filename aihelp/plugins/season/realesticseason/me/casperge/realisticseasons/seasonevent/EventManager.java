package me.casperge.realisticseasons.seasonevent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.SeasonEventEnd;
import me.casperge.realisticseasons.api.SeasonEventStart;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.seasonevent.buildin.ChristmasEvent;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEvent;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEventType;
import me.casperge.realisticseasons.seasonevent.buildin.EasterEvent;
import me.casperge.realisticseasons.seasonevent.buildin.HalloweenEvent;
import me.casperge.realisticseasons.seasonevent.buildin.NewYearEvent;
import me.casperge.realisticseasons.seasonevent.buildin.StandardEventFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class EventManager {
   private EventUtils utils;
   private List<CustomDatedEvent> dated = new ArrayList();
   private List<CustomWeeklyEvent> weekly = new ArrayList();
   private List<CustomDailyEvent> daily = new ArrayList();
   private WeakHashMap<World, List<String>> activeEvents = new WeakHashMap();
   private RealisticSeasons main;
   public NewYearEvent newyearevent;
   public ChristmasEvent christmasevent;
   public EasterEvent easterevent;
   public HalloweenEvent halloweenevent;

   public EventManager(RealisticSeasons var1) {
      this.main = var1;
      this.utils = new EventUtils(var1);
      this.load();
   }

   public void load() {
      if (this.main.getSettings().calendarEnabled) {
         new EventFileLoader(this.main, this);
         new StandardEventFileLoader(this.main, this);
         this.activeEvents.clear();
         Iterator var1 = this.main.getSeasonManager().worldData.keySet().iterator();

         while(true) {
            World var2;
            Date var3;
            do {
               do {
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     var2 = (World)var1.next();
                     var3 = this.main.getTimeManager().getDate(var2);
                  } while(this.main.getSeasonManager().worldData.get(var2) == Season.DISABLED);
               } while(this.main.getSeasonManager().worldData.get(var2) == Season.RESTORE);
            } while(var3 == null);

            Iterator var4 = this.weekly.iterator();

            Object var6;
            while(var4.hasNext()) {
               CustomWeeklyEvent var5 = (CustomWeeklyEvent)var4.next();
               if (var5.isToday(this.main.getTimeManager().getCalendar().getWeekDayAsInt(var3)) && var5.doDisplay()) {
                  if (this.activeEvents.containsKey(var2)) {
                     var6 = (List)this.activeEvents.get(var2);
                  } else {
                     var6 = new ArrayList();
                  }

                  ((List)var6).add(var5.getName());
                  this.activeEvents.put(var2, var6);
               }
            }

            var4 = this.dated.iterator();

            while(var4.hasNext()) {
               CustomDatedEvent var7 = (CustomDatedEvent)var4.next();
               if (var7.isActive(var3) && var7.hasEvent(var2)) {
                  if (var7.doDisplay()) {
                     if (this.activeEvents.containsKey(var2)) {
                        var6 = (List)this.activeEvents.get(var2);
                     } else {
                        var6 = new ArrayList();
                     }

                     ((List)var6).add(var7.getName());
                     this.activeEvents.put(var2, var6);
                  }

                  if (var7 instanceof DefaultEvent) {
                     DefaultEvent var8 = (DefaultEvent)var7;
                     var8.enable(var2);
                  }
               }
            }
         }
      }
   }

   private void execute(World var1, SeasonCustomEvent var2, boolean var3, Date var4) {
      this.utils.execute(var1, var2.getCommands(var3), var4);
      if (var2 instanceof DefaultEvent) {
         DefaultEvent var5 = (DefaultEvent)var2;
         if (var3) {
            var5.enable(var1);
         } else {
            var5.disable(var1);
         }
      }

   }

   public void start(World var1, SeasonCustomEvent var2, Date var3) {
      SeasonEventStart var4 = new SeasonEventStart(var1, var2);
      Bukkit.getPluginManager().callEvent(var4);
      if (!var4.isCancelled()) {
         if (var2.doDisplay()) {
            Object var5;
            if (this.activeEvents.containsKey(var1)) {
               var5 = (List)this.activeEvents.get(var1);
            } else {
               var5 = new ArrayList();
            }

            ((List)var5).add(var2.getName());
            this.activeEvents.put(var1, var5);
         }

         this.execute(var1, var2, true, var3);
      }
   }

   public DefaultEvent getDefaultEvent(DefaultEventType var1) {
      switch(var1) {
      case CHRISTMAS:
         return this.christmasevent;
      case NEWYEAR:
         return this.newyearevent;
      case EASTER:
         return this.easterevent;
      case HALLOWEEN:
         return this.halloweenevent;
      default:
         return null;
      }
   }

   public void stop(World var1, SeasonCustomEvent var2, Date var3) {
      SeasonEventEnd var4 = new SeasonEventEnd(var1, var2);
      Bukkit.getPluginManager().callEvent(var4);
      if (var2.doDisplay() && this.activeEvents.containsKey(var1)) {
         List var5 = (List)this.activeEvents.get(var1);
         var5.remove(var2.getName());
         this.activeEvents.put(var1, var5);
      }

      this.execute(var1, var2, false, var3);
   }

   public void setDatedEvents(List<CustomDatedEvent> var1) {
      this.dated = var1;
   }

   public void setWeeklyEvents(List<CustomWeeklyEvent> var1) {
      this.weekly = var1;
   }

   public void setDailyEvents(List<CustomDailyEvent> var1) {
      this.daily = var1;
   }

   public List<CustomDatedEvent> getDatedEvents() {
      return this.dated;
   }

   public List<CustomWeeklyEvent> getWeeklyEvents() {
      return this.weekly;
   }

   public List<CustomDailyEvent> getDailyEvents() {
      return this.daily;
   }

   public List<CustomDatedEvent> getNextEvent(World var1) {
      ArrayList var2 = new ArrayList();
      if (!this.main.getSeasonManager().worldData.containsKey(var1)) {
         return var2;
      } else if (!this.main.getSettings().calendarEnabled) {
         return var2;
      } else {
         Date var3 = this.main.getTimeManager().getDate(var1);
         Iterator var4 = this.dated.iterator();

         while(var4.hasNext()) {
            CustomDatedEvent var5 = (CustomDatedEvent)var4.next();
            if (var5.hasEvent(var1) && var5.doDisplay() && !var5.isActive(var3)) {
               if (var2.size() == 0) {
                  var2.add(var5);
               } else if (this.main.getTimeManager().getCalendar().getDaysUntil(var3, var5.getStartDate()) < this.main.getTimeManager().getCalendar().getDaysUntil(var3, ((CustomDatedEvent)var2.get(0)).getStartDate())) {
                  var2.clear();
                  var2.add(var5);
               } else if (this.main.getTimeManager().getCalendar().getDaysUntil(var3, var5.getStartDate()) == this.main.getTimeManager().getCalendar().getDaysUntil(var3, ((CustomDatedEvent)var2.get(0)).getStartDate())) {
                  var2.add(var5);
               }
            }
         }

         return var2;
      }
   }

   public List<String> getActiveEvents(World var1) {
      return (List)(this.activeEvents.containsKey(var1) ? (List)this.activeEvents.get(var1) : new ArrayList());
   }

   public String getActiveEventsAsString(World var1) {
      if (!this.activeEvents.containsKey(var1)) {
         return "-";
      } else {
         boolean var2 = true;
         String var3 = "";
         Iterator var4 = ((List)this.activeEvents.get(var1)).iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var2) {
               var3 = var5;
               var2 = false;
            } else {
               var3 = var3 + ", " + var5;
            }
         }

         if (var3.isEmpty()) {
            var3 = "-";
         }

         return var3;
      }
   }

   public String getNextEventsAsString(World var1) {
      List var2 = this.getNextEvent(var1);
      if (var2.size() == 0) {
         return "-";
      } else {
         boolean var3 = true;
         String var4 = "";
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            CustomDatedEvent var6 = (CustomDatedEvent)var5.next();
            if (var3) {
               var4 = var6.getName();
               var3 = false;
            } else {
               var4 = var4 + ", " + var6.getName();
            }
         }

         if (var4.isEmpty()) {
            var4 = "-";
         }

         return var4;
      }
   }

   public int getDaysUntilNextEvent(World var1) {
      List var2 = this.getNextEvent(var1);
      return var2.size() == 0 ? -1 : this.main.getTimeManager().getCalendar().getDaysUntil(this.main.getTimeManager().getDate(var1), ((CustomDatedEvent)var2.get(0)).getStartDate());
   }
}
