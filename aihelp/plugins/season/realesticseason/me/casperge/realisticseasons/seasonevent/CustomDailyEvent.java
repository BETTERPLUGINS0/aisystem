package me.casperge.realisticseasons.seasonevent;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class CustomDailyEvent implements SeasonCustomEvent {
   private List<String> commands = new ArrayList();

   public CustomDailyEvent(ConfigurationSection var1) {
      if (var1.getBoolean("enabled")) {
         this.commands = var1.getStringList("commands");
      }

   }

   public List<String> getCommands(boolean var1) {
      return this.commands;
   }

   public boolean doDisplay() {
      return false;
   }

   public String getName() {
      return "ERROR";
   }
}
