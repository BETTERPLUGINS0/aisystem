package emanondev.itemedit.compability;

import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import java.util.Locale;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsListener implements Listener {
   @EventHandler
   private void event(MythicDropLoadEvent event) {
      if (event.getDropName().equalsIgnoreCase("serveritem")) {
         try {
            event.register(new ServerItemDrop(event.getConfig()));
         } catch (IllegalArgumentException var3) {
         }

      }
   }

   @EventHandler
   private void event(MythicMechanicLoadEvent event) {
      String var2 = event.getMechanicName().toLowerCase(Locale.ENGLISH);
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1857365337:
         if (var2.equals("giveserveritem")) {
            var3 = 1;
         }
         break;
      case 2118443269:
         if (var2.equals("dropserveritem")) {
            var3 = 0;
         }
      }

      switch(var3) {
      case 0:
         try {
            event.register(new DropServerItemMechanic(event.getConfig()));
         } catch (IllegalArgumentException var6) {
         }
         break;
      case 1:
         try {
            event.register(new GiveServerItemMechanic(event.getConfig()));
         } catch (IllegalArgumentException var5) {
         }
      }

   }
}
