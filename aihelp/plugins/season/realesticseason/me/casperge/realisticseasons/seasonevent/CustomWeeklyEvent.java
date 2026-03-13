package me.casperge.realisticseasons.seasonevent;

import java.util.ArrayList;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.configuration.ConfigurationSection;

public class CustomWeeklyEvent implements SeasonCustomEvent {
   private String name;
   private boolean doDisplay;
   private int weekday;
   private List<String> startCommands = new ArrayList();
   private List<String> stopCommands = new ArrayList();

   public CustomWeeklyEvent(ConfigurationSection var1) {
      if (var1.getBoolean("enabled")) {
         this.startCommands = var1.getStringList("commands.start");
         this.stopCommands = var1.getStringList("commands.stop");
      }

      this.name = var1.getString("name");
      this.doDisplay = var1.getBoolean("display-event");
      this.weekday = RealisticSeasons.getInstance().getTimeManager().getCalendar().weekDayFromString(var1.getString("day"));
   }

   public List<String> getCommands(boolean var1) {
      return var1 ? this.startCommands : this.stopCommands;
   }

   public boolean isToday(int var1) {
      return var1 == this.weekday;
   }

   public String getName() {
      return this.name;
   }

   public boolean doDisplay() {
      return this.doDisplay;
   }
}
