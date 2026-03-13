package me.casperge.realisticseasons.seasonevent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.calendar.TimeManager;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class EventUtils {
   private RealisticSeasons main;

   public EventUtils(RealisticSeasons var1) {
      this.main = var1;
   }

   public void execute(World var1, List<String> var2, Date var3) {
      Iterator var4 = this.main.getSettings().worldsWithoutEvents.iterator();

      String var5;
      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (var5.equalsIgnoreCase(var1.getName())) {
            return;
         }
      }

      var4 = this.replacePlaceholders(var1, var2, var3).iterator();

      while(true) {
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            if (var5.startsWith("/")) {
               Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var5.replaceFirst("/", ""));
            } else if (!var5.equalsIgnoreCase("NONE")) {
               Iterator var6 = var1.getPlayers().iterator();

               while(var6.hasNext()) {
                  Player var7 = (Player)var6.next();
                  var7.sendMessage(JavaUtils.hex(var5));
               }
            }
         }

         return;
      }
   }

   public List<String> replacePlaceholders(World var1, List<String> var2, Date var3) {
      ArrayList var4 = new ArrayList();

      String var7;
      for(Iterator var5 = var2.iterator(); var5.hasNext(); var4.add(var7)) {
         String var6 = (String)var5.next();
         var7 = this.main.setPlaceHolders(var6, (Player)null);
         var7 = var7.replaceAll("%season%", this.main.getSeasonManager().getSeason(var1).toString()).replaceAll("%world%", var1.getName());
         if (this.main.getSettings().calendarEnabled) {
            TimeManager var8 = this.main.getTimeManager();
            var7 = var7.replaceAll("%month_asname%", var8.getCalendar().getMonth(var3.getMonth()).getName()).replaceAll("%weekday%", var8.getWeekDay(var3)).replaceAll("%day%", String.valueOf(var3.getDay())).replaceAll("%year%", String.valueOf(var3.getYear())).replaceAll("%year%", String.valueOf(var3.getMonth())).replaceAll("%month%", String.valueOf(var3.getMonth()));
         }
      }

      return var4;
   }
}
