package emanondev.itemtag.command.itemtag;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.potion.PotionEffect;

public class EffectsResurrectListener implements Listener {
   private final Effects parent;

   EffectsResurrectListener(Effects parent) {
      this.parent = parent;
   }

   @EventHandler(
      ignoreCancelled = true
   )
   private void event(EntityResurrectEvent event) {
      if (event.getEntity() instanceof Player) {
         Bukkit.getScheduler().runTaskLater(this.parent.getPlugin(), () -> {
            List<PotionEffect> l = new ArrayList(event.getEntity().getActivePotionEffects());
            l.forEach((e) -> {
               event.getEntity().removePotionEffect(e.getType());
            });
            this.parent.restoreEffects((Player)event.getEntity());
            l.forEach((e) -> {
               if (!event.getEntity().hasPotionEffect(e.getType()) || e.getAmplifier() > event.getEntity().getPotionEffect(e.getType()).getAmplifier()) {
                  event.getEntity().addPotionEffect(e);
               }

            });
         }, 1L);
      }

   }
}
